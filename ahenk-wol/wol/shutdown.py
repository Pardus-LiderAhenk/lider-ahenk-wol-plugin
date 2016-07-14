#!/usr/bin/python3
# -*- coding: utf-8 -*-
# Author:Mine DOGAN <mine.dogan@agem.com.tr>

from base.plugin.abstract_plugin import AbstractPlugin
from base.system.system import System


class Shutdown(AbstractPlugin):
    def __init__(self, context):
        super(Shutdown, self).__init__()
        self.context = context
        self.logger = self.get_logger()
        self.connected_interfaces = System.Hardware.Network.interfaces()

        self.logger.debug('[WOL - shutdown] Parameters were initialized.')

    def handle_shutdown_mode(self):

        for interface in self.connected_interfaces:
            self.logger.debug('[WOL - shutdown] Activating magic packet for ' + str(interface))
            self.execute('ethtool -s ' + str(interface) + ' wol g')
            self.logger.debug('[WOL - shutdown] Activated magic packet for ' + str(interface))


def handle_mode(context):
    shutdown = Shutdown(context)
    shutdown.handle_shutdown_mode()
