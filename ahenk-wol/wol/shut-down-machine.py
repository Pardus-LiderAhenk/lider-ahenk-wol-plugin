#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

from base.plugin.abstract_plugin import AbstractPlugin
from base.system.system import System

class ShutDownMachine(AbstractPlugin):
    def __init__(self, task, context):
        super(AbstractPlugin, self).__init__()
        self.task = task
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()

        self.waiting_time = self.task['time']
        self.shut_down_command = 'sleep {0}s; shutdown -h now'

        self.logger.debug('[Wol - Shut Down Machine] Parameters were initialized.')

    def handle_task(self):
        try:
            self.logger.debug('[Wol - Shut Down Machine] Shutting down the machine...')
            self.execute(self.shut_down_command.format(self.waiting_time), result=False)

            response = 'Shutdown command executed successfully. The machine will turn off. Mac Address(es): {0}, Ip Address(es): {1}'\
                .format(System.Hardware.Network.mac_addresses(), System.Hardware.Network.ip_addresses())
            
            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message=response)
            self.logger.info('[Wol - Shut Down Machine] WOL task is handled successfully')

        except Exception as e:
            self.logger.error('[Wol - Shut Down Machine] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                     message='A problem occured while handling WOL task: {0}'.format(str(e)))

def handle_task(task, context):
    shut_down = ShutDownMachine(task, context)
    shut_down.handle_task()