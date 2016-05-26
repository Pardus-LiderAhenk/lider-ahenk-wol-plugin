#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

import os
from base.plugin.abstract_plugin import AbstractPlugin


class ManageWol(AbstractPlugin):
    def __init__(self, task, context):
        super(AbstractPlugin, self).__init__()
        self.task = task
        self.context = context
        self.logger = self.get_logger()
        self.message_code = self.get_message_code()
        self.connected_devices = self.get_connected_devices()

    def handle_task(self):
        checking = os.popen('dpkg-query -W -f=\'${Status}\' ethtool')
        result = checking.read()

        if result != 'install ok installed':
            self.execute('apt-get -y install ethtool')

        for device in self.connected_devices:
            self.execute('ethtool -s ' + device + ' wol g')
        self.make_script()
        # TODO is task handled successfully? keep and response code and message
        self.context.create_response(code=self.message_code.TASK_PROCESSED.value, message='User wol task processed successfully')

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
        self.execute('chmod +x /etc/init.d/wol.sh')
        # Tell Linux to execute the script on every runlevel
        self.execute('update-rc.d -f wol.sh defaults')


def handle_task(task, context):
    wol = ManageWol(task, context)
    wol.handle_task()
