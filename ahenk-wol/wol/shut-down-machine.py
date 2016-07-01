#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

from base.plugin.abstract_plugin import AbstractPlugin

class ShutDownMachine(AbstractPlugin):
    def __init__(self, task, context):
        super(AbstractPlugin, self).__init__()
        self.task = task
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()

        self.shut_down_command = 'shutdown -h now'

        self.logger.debug('[Wol - Shut Down Machine] Parameters were initialized.')

    def handle_task(self):
        try:
            self.logger.debug('[Wol - Shut Down Machine] Shutting down the machine...')
            result_code, out, err = self.execute(self.shut_down_command)

            if err != '':
                self.logger.debug('[Wol - Shut Down Machine] An error occured while shutting down the machine.')
                raise Exception(err)

            self.context.create_response(code=self.message_code.TASK_PROCESSED.value,
                                         message='User wol task processed successfully')
            self.logger.info('[Wol - Shut Down Machine] WOL task is handled successfully')

        except Exception as e:
            self.logger.error('[Wol - Shut Down Machine] A problem occured while handling WOL task: {0}'.format(str(e)))
            self.context.create_response(code=self.message_code.TASK_ERROR.value,
                                     message='A problem occured while handling WOL task: {0}'.format(str(e)))

def handle_task(task, context):
    shut_down = ShutDownMachine(task, context)
    shut_down.handle_task()