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

        self.mac_address_list = self.task['macAddress']
        self.wake_command = 'wakeonlan {}'
        self.ip_address_list = self.task['ipAddress']
        self.port_list = self.task['port']
        self.port_control = 'nmap -p {0} {1} | grep {2}/tcp'

        self.logger.debug('[Wol - Wake Machine] Parameters were initialized.')

    def handle_task(self):
        try:
            if self.is_installed('wakeonlan') == False:
                self.logger.debug('[Wol - Wake Machine] Installing wakeonlan with apt-get...')
                self.install_with_apt_get('wakeonlan')

            self.logger.debug('[Wol - Wake Machine] Sending magic package(s) to wake the machine(s)...')

            for i, val in enumerate(self.mac_address_list):
                result_code_wake, p_out_wake, p_err_wake = self.execute(self.wake_command.format(val))

                if p_err_wake != '':
                    self.logger.debug('[Wol - Wake Machine] An error occured while sending magic package. Mac Address: {}'.format(val))
                    raise Exception(p_err_wake)

            time.sleep(120)

            for i, val in enumerate(self.ip_address_list):
                ip_addresses = str(val).split(',')
                for j, ip in enumerate(ip_addresses):
                    result_code, out, err = self.execute(self.port_control.format(self.port_list[i], ip, self.port_list[i]))

                    if err != '':
                        self.logger.debug('[Wol - Wake Machine] An error occured while scanning the port. Host: {}'.format(val))
                        raise Exception(err)

                    if 'open' not in out:
                        self.logger.debug('[Wol - Wake Machine] Port is closed or the machine was not turned on. Host: {}'.format(val))
                        raise Exception


            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='User wol task processed successfully')
            self.logger.info('[Wol - Wake Machine] WOL task is handled successfully')

        except Exception as e:
            self.logger.error('[Wol - Wake Machine] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                         message='A problem occured while handling WOL task: {0}'.format(str(e)))


def handle_task(task, context):
    wake = WakeMachine(task, context)
    wake.handle_task()
