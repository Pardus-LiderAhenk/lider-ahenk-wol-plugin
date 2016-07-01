#!/usr/bin/python3
# -*- coding: utf-8 -*-

from base.plugin.abstract_plugin import AbstractPlugin
from base.system.system import System


class ManageWol(AbstractPlugin):
    def __init__(self, username, context):
        super(ManageWol, self).__init__()
        self.username = username
        self.context = context
        self.logger = self.get_logger()
        self.connected_interfaces = System.Hardware.Network.interfaces()

        self.logger.debug('[WOL - safe.py] Parameters were initialized.')

    def handle_safe_mode(self):

        for interface in self.connected_interfaces:
            self.logger.debug('[Wol - safe.py] Activating magic packet for ' + str(interface))
            self.execute('ethtool -s ' + str(interface) + ' wol g')

def handle_safe_mode(username, context):
    manage = ManageWol(username, context)
    manage.handle_safe_mode()
