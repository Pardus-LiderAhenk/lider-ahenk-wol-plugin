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
        self.check_installation = 'dpkg-query -W -f=\'${Status}\' ethtool'
        self.installed = 'install ok installed'
        self.install = 'apt-get -y install ethtool'
        self.script_path = '/etc/init.d/wol.sh'
        self.execute_script = 'update-rc.d -f wol.sh defaults'
        self.connected_interfaces = System.Hardware.Network.interfaces()
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()


    def handle_task(self):
        try:
            checking = os.popen(self.check_installation)
            result = checking.read()

            if result != self.installed:
                self.logger.debug('[WOL] Installing ethtool')
                self.execute(self.install)

            for interface in self.connected_interfaces:
                self.logger.debug('[WOL] Activating magic packet for ' + str(interface))
                self.execute('ethtool -s ' + str(interface) + ' wol g')

            self.make_script()
            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='User wol task processed successfully')
            self.logger.info('[WOL] WOL task is handled successfully')
        except Exception as e:
            self.logger.error('[WOL] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='A problem occured while handling WOL task: {0}'.format(str(e)))


    def make_script(self):
        self.logger.debug('[WOL] Making script to activate magic packet on every runlevel')
        with open('/etc/init.d/wol.sh', 'w+') as f:
            for interface in self.connected_interfaces:
                if f.tell() == 0:
                    f.write('### BEGIN INIT INFO\n'
                            '# Provides:          wake-on-lan\n'
                            '# Required-Start:    $remote_fs $syslog\n'
                            '# Required-Stop:     $remote_fs $syslog\n'
                            '# Default-Start:     2 3 4 5\n'
                            '# Default-Stop:      0 1 6\n'
                            '### END INIT INFO\n'
                            '#!/bin/bash\n'
                            'ethtool -s ' + interface + ' wol g\n')
                else:
                    f.write('ethtool -s ' + interface + ' wol g\n')
        f.close()

        # Make the script executable
        self.make_executable(self.script_path)
        # Tell Linux to execute the script on every runlevel
        self.execute(self.execute_script)


def handle_task(task, context):
    wol = ManageWol(task, context)
    wol.handle_task()
