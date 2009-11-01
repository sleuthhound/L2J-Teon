# SANDMAN
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.instancemanager import VanHalterManager

#NPC
VAN_HALTER = 29062
CAPTAIN    = 22188

#TRIOL'S REVELATIONS
TRIOLS = [32058,32059,32060,32061,32062,32063,32064,32065,32066]

# Main Quest Code
class vanhalter(JQuest):

  def __init__(self,id,name,descr) : JQuest.__init__(self,id,name,descr)

  def onAttack (self,npc,player,damage,isPet,skill) :
    npcId = npc.getNpcId()
    if npcId == VAN_HALTER :
      maxHp = npc.getMaxHp()
      curHp = npc.getCurrentHp()
      if (curHp / maxHp) * 100 <= 20 :
        VanHalterManager.getInstance().callRoyalGuardHelper()

  def onKill (self,npc,player,isPet) :
    npcId = npc.getNpcId()
    if npcId in TRIOLS :
      VanHalterManager.getInstance().removeBleeding(npcId)
      VanHalterManager.getInstance().checkTriolRevelationDestroy() # checkToriolRevelationDestroy()
    if npcId == CAPTAIN :
      VanHalterManager.getInstance().checkRoyalGuardCaptainDestroy()
    if npcId == VAN_HALTER :
      VanHalterManager.getInstance().enterInterval()

# Quest class and state definition
QUEST = vanhalter(-1,"vanhalter","ai")

# Quest NPC starter initialization
# High Priestess van Halter
QUEST.addAttackId(VAN_HALTER)
QUEST.addKillId(VAN_HALTER)
# Andreas' Captain of the Royal Guard
QUEST.addKillId(CAPTAIN)
# Triol's Revelation
for Triol in TRIOLS :
    QUEST.addKillId(Triol)