import sys
from net.sf.l2j.util import Rnd
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import NpcSay
from net.sf.l2j.gameserver.serverpackets import PlaySound

# turek_orc_warlord
class turek_orc_warlord(JQuest) :

    # init function.  Add in here variables that you'd like to be inherited by subclasses (if any)
    def __init__(self,id,name,descr):
        self.turek_orc_warlord = 20495
        self.FirstAttacked = False
        # finally, don't forget to call the parent constructor to prepare the event triggering
        # mechanisms etc.
        JQuest.__init__(self,id,name,descr)

    def onAttack (self,npc,player,damage,isPet):
        objId=npc.getObjectId()
        if self.FirstAttacked:
           if Rnd.get(40) : return
           npc.broadcastPacket(NpcSay(objId,0,npc.getNpcId(),"You wont take me down easily."))
        else :
           self.FirstAttacked = True
           npc.broadcastPacket(NpcSay(objId,0,npc.getNpcId(),"The battle has just begun!"))
        return 

    def onKill (self,npc,player,isPet):
        npcId = npc.getNpcId()
        if npcId == self.turek_orc_warlord:
            objId=npc.getObjectId()
            self.FirstAttacked = False
        elif self.FirstAttacked :
            self.addSpawn(npcId,npc.getX(), npc.getY(), npc.getZ(),npc.getHeading(),True,0)
        return 

QUEST		= turek_orc_warlord(-1,"turek_orc_warlord","ai")

QUEST.addKillId(QUEST.turek_orc_warlord)

QUEST.addAttackId(QUEST.turek_orc_warlord)