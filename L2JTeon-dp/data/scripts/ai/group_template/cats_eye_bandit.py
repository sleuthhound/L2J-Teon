import sys
from net.sf.l2j.util import Rnd
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import NpcSay
from net.sf.l2j.gameserver.serverpackets import PlaySound

# Cats_Eye_Bandit
class cats_eye_bandit(JQuest) :

    # init function.  Add in here variables that you'd like to be inherited by subclasses (if any)
    def __init__(self,id,name,descr):
        self.cats_eye_bandit = 27038
        self.FirstAttacked = False
        # finally, don't forget to call the parent constructor to prepare the event triggering
        # mechanisms etc.
        JQuest.__init__(self,id,name,descr)

    def onAttack (self,npc,player,damage,isPet):
        objId=npc.getObjectId()
        if self.FirstAttacked:
           if Rnd.get(40) : return
           npc.broadcastPacket(NpcSay(objId,0,npc.getNpcId(),"You childish fool, do you think you can catch me?"))
        else :
           self.FirstAttacked = True
        return

    def onKill (self,npc,player,isPet):
        npcId = npc.getNpcId()
        if npcId == self.cats_eye_bandit:
            objId=npc.getObjectId()
            if Rnd.get(80) : npc.broadcastPacket(NpcSay(objId,0,npc.getNpcId(),"I must do something about this shameful incident..."))

            self.FirstAttacked = False
        elif self.FirstAttacked :
            self.addSpawn(npcId, npc.getX(), npc.getY(), npc.getZ())
        return

QUEST      = cats_eye_bandit(-1,"cats_eye_bandit","ai")

QUEST.addKillId(QUEST.cats_eye_bandit)

QUEST.addAttackId(QUEST.cats_eye_bandit)