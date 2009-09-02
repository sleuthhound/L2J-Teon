import sys
from net.sf.l2j.util import Rnd
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import NpcSay
from net.sf.l2j.gameserver.serverpackets import PlaySound

# delu_lizardman_special_commander
class delu_lizardman_special_commander(JQuest) :

    # init function.  Add in here variables that you'd like to be inherited by subclasses (if any)
    def __init__(self,id,name,descr):
        self.delu_lizardman_special_commander = 21107
        self.FirstAttacked = False
        # finally, don't forget to call the parent constructor to prepare the event triggering
        # mechanisms etc.
        JQuest.__init__(self,id,name,descr)

    def onAttack (self,npc,player,damage,isPet):
        objId=npc.getObjectId()
        if self.FirstAttacked:
           if Rnd.get(40) : return
           npc.broadcastPacket(NpcSay(objId,0,npc.getNpcId(),"Come on, Ill take you on!"))
        else :
           self.FirstAttacked = True
           npc.broadcastPacket(NpcSay(objId,0,npc.getNpcId(),"How dare you interrupt a sacred duel! You must be taught a lesson!"))
        return 

    def onKill (self,npc,player,isPet):
        npcId = npc.getNpcId()
        if npcId == self.delu_lizardman_special_commander:
            objId=npc.getObjectId()
            self.FirstAttacked = False
        elif self.FirstAttacked :
            self.addSpawn(npcId,npc.getX(), npc.getY(), npc.getZ(),npc.getHeading(),True,0)
        return 

QUEST		= delu_lizardman_special_commander(-1,"delu_lizardman_special_commander","ai")

QUEST.addKillId(QUEST.delu_lizardman_special_commander)

QUEST.addAttackId(QUEST.delu_lizardman_special_commander)