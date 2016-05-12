#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

import os
from base.model.enum.MessageCode import MessageCode
from base.plugin.AbstractCommand import AbstractCommand

class ManageWol(AbstractCommand):

    def __init__(self, task, context):
        super(ManageWol, self).__init__()
        self.task = task
        self.context = context
        self.connected_devices = self.get_connected_devices()

    def handle_task(self):
        process = self.context.execute('apt-get -y install ethtool')
        process.wait()

        for device in self.connected_devices:
            process = self.context.execute('ethtool -s ' + device + ' wol g')
            process.wait()

        self.make_script()

        self.create_response(message='_message')

    def get_connected_devices(self):
        f = os.popen("nmcli --terse --fields DEVICE,STATE dev | grep -w 'connected' | awk -F':' '{print $1}'")
        a = f.read()
        b = a.split()
        return b

    def make_script(self):
        with open('/etc/init.d/wol.sh', 'w+') as f:
            for device in self.connected_devices:
                if f.tell() == 0:
                    f.write('### BEGIN INIT INFO\n'
                            '# Provides:          wake-on-lan\n'
                            '# Required-Start:    $remote_fs $syslog\n'
                            '# Required-Stop:     $remote_fs $syslog\n'
                            '# Default-Start:     2 3 4 5\n'
                            '# Default-Stop:      0 1 6\n'
                            '### END INIT INFO\n'
                            '#!/bin/bash\n'
                            'ethtool -s ' + device + ' wol g\n')
                else:
                    f.write('ethtool -s ' + device + ' wol g\n')

        f.close()

        # Make the script executable
        process = self.context.execute('chmod +x /etc/init.d/wol.sh')
        process.wait()

        # Tell Linux to execute the script on every runlevel
        process = self.context.execute('update-rc.d -f wol.sh defaults')
        process.wait()

    def create_response(self, success=True, message=None, data=None, content_type=None):
        if success:
            self.context.put('responseCode', MessageCode.TASK_PROCESSED.value)
        else:
            self.context.put('responseCode', MessageCode.TASK_ERROR.value)
        self.context.put('responseMessage', message)
        # self.context.put('responseData')
        # self.context.put('contentType')


def handle_task(task, context):
    wol = ManageWol(task, context)
    wol.handle_task()