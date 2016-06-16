#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

import time

from base.plugin.abstract_plugin import AbstractPlugin


class WakeMachine(AbstractPlugin):
    def __init__(self, task, context):
        super(AbstractPlugin, self).__init__()
        self.task = task
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()

        self.mac_address = self.task['macAddress']
        self.ip_address_list = self.task['ipAddresses']
        self.wake_command = 'wakeonlan ' + self.mac_address
        self.ping_command = 'ping -c1 {} > /dev/null 2>&1 && echo "success" || echo "fail"'

    def handle_task(self):
        try:
            result_code_wake, p_out_wake, p_err_wake = self.execute(self.wake_command)

            if p_err_wake != '':
                raise Exception(p_err_wake)

            time.sleep(5)

            for i, val in enumerate(self.ip_address_list):
                command = self.ping_command.format(val.replace("'", ""))
                result_code, p_out, p_err = self.execute(command)

                if p_err != '':
                    raise Exception(p_err)
                elif p_out != 'success\n':
                    raise Exception('ping: ' + p_out + ' for ' + val)

            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='User wol task processed successfully')
            self.logger.info('[WOL] WOL task is handled successfully')

        except Exception as e:
            self.logger.error('[WOL] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='A problem occured while handling WOL task: {0}'.format(str(e)))


def handle_task(task, context):
    wake = WakeMachine(task, context)
    wake.handle_task()
