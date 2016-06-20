#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

import os
from base.plugin.abstract_plugin import AbstractPlugin
from base.system.system import System


class ManageWol(AbstractPlugin):
    def __init__(self, task, context):
        super(AbstractPlugin, self).__init__()
        self.task = task
        self.context = context
        self.message_code = self.get_message_code()
        self.logger = self.get_logger()

        self.script_path = '/etc/init.d/wol.sh'
        self.execute_script = 'update-rc.d -f wol.sh defaults'
        self.connected_interfaces = System.Hardware.Network.interfaces()

        self.logger.debug('[Wol - Manage Wol] Parameters were initialized.')

    def handle_task(self):
        try:
            for interface in self.connected_interfaces:
                self.logger.debug('[Wol - Manage Wol] Activating magic packet for ' + str(interface))
                self.execute('ethtool -s ' + str(interface) + ' wol g')

            self.make_script()

            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='User wol task processed successfully. Wake-On-LAN configuration was performed.')
            self.logger.info('[Wol - Manage Wol] WOL task is handled successfully')
        except Exception as e:
            self.logger.error('[Wol - Manage Wol] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='A problem occured while handling WOL task: {0}'.format(str(e)))


    def make_script(self):
        self.logger.debug('[Wol - Manage Wol] Making script to activate magic packet on every runlevel')
        # /etc/init.d path was used due to the plugin is machine oriented
        with open('/etc/init.d/wol.sh', 'w+') as f:
            for interface in self.connected_interfaces:
                if f.tell() == 0:
                    f.write('#!/bin/sh\n'
                            'ethtool -s ' + interface + ' wol g\n')
                else:
                    f.write('ethtool -s ' + interface + ' wol g\n')
        f.close()

        self.logger.debug('[Wol - Manage Wol] Making script executable...')
        self.make_executable(self.script_path)


def handle_task(task, context):
    manage = ManageWol(task, context)
    manage.handle_task()
