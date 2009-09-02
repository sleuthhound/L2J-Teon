import sys
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.gameserver.instancemanager import BenomManager

# Boss: benom
class benom(JQuest) :

    def __init__(self,id,name,descr):
        JQuest.__init__(self,id,name,descr)

    def onKill (self,npc,player,isPet):
        npcId = npc.getNpcId()
        if npcId == 29054:
            BenomManager.getInstance().BenomKilling()
        elif npcId == 13002:
            BenomManager.getInstance().SiegeAggroSpawn(npc.getX(), npc.getY(), npc.getZ(),0)
            return 

# now call the constructor (starts up the ai)
QUEST		= benom(-1,"benom","ai")


QUEST.addKillId(29054)
QUEST.addKillId(13002)

