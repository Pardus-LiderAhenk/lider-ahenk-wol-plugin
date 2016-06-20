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
        self.wake_command = 'wakeonlan ' + self.mac_address

        self.logger.debug('[Wol - Wake Machine] Parameters were initialized.')

    def handle_task(self):
        try:
            if self.is_installed('wakeonlan') == False:
                self.logger.debug('[Wol - Wake Machine] Installing wakeonlan with apt-get...')
                self.install_with_apt_get('wakeonlan')

            self.logger.debug('[Wol - Wake Machine] Sending magic package to wake the machine...')
            result_code_wake, p_out_wake, p_err_wake = self.execute(self.wake_command)

            if p_err_wake != '':
                self.logger.debug('[Wol - Wake Machine] An error occured while sending magic package.')
                raise Exception(p_err_wake)


            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='User wol task (sending magic package) processed successfully')
            self.logger.info('[Wol - Wake Machine] WOL task is handled successfully')

        except Exception as e:
            self.logger.error('[Wol - Wake Machine] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='A problem occured while handling WOL task: {0}'.format(str(e)))


def handle_task(task, context):
    wake = WakeMachine(task, context)
    wake.handle_task()
