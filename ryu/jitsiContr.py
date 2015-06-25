import ofctl_rest

from ryu.controller import ofp_event
from ryu.ofproto import ofproto_v1_3
from ryu.controller.handler import CONFIG_DISPATCHER, MAIN_DISPATCHER
from ryu.controller.handler import set_ev_cls

class JitsiContr(ofctl_rest.RestStatsApi):
    OFP_VERSIONS = [ofproto_v1_3.OFP_VERSION]

    def __init__(self, *args, **kwargs):
        super(JitsiContr, self).__init__(*args, **kwargs)

    @set_ev_cls(ofp_event.EventOFPPacketIn, MAIN_DISPATCHER)
    def _packet_in_handler(self, ev):
        # TODO: Classify XMPP packet and notify the java program
        # of the physical port binding

        # TODO: Classify RTP packet
        # If there is lease, renew the entries
        # If there is no lease, put a resisting entry

        return
