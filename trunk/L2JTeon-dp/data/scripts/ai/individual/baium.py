# version 0.1
# by Fulminus
# L2J_JP EDIT SANDMAN

import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.instancemanager.grandbosses import BaiumManager

# Boss: Baium
class baium (JQuest):

  def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

  def onTalk (self,npc,player):
    st = player.getQuestState("baium")
    if not st : return "<html><head><body>I have no tasks for you</body></html>"
    npcId = npc.getNpcId()
    if npcId == 29025 :
      if st.getInt("ok"):
        if not npc.isBusy():
           npc.onBypassFeedback(player,"wake_baium")
           npc.setBusy(True)
           npc.setBusyMessage("Attending another player's request")
      else:
        st.exitQuest(1)
        return "Conditions are not right to wake up Baium"
    elif npcId == 31862 :
      if BaiumManager.getInstance().isEnableEnterToLair() :
        if player.isFlying() :
          return "<html><body>Angelic Vortex:<br>You may not enter while flying a wyvern</body></html>"
        if st.getQuestItemsCount(4295) : # bloody fabric
          st.takeItems(4295,1)
          player.teleToLocation(113100,14500,10077)
          BaiumManager.getInstance().addPlayerToLair(player)
          st.set("ok","1")
        else :
          return "<html><head><body>Angelic Vortex:<br>You do not have enough items</body></html>"
      else :
        return "<html><body>Angelic Vortex:<br>You may not enter at this time</body></html>"
    return

  def onKill (self,npc,player,isPet):
    st = player.getQuestState("baium")
    if not st: return
    BaiumManager.getInstance().setCubeSpawn()
    st.exitQuest(1)

  def onAttack(self,npc,player,damage,isPet):
    st = player.getQuestState("baium")
    if not st: return
    BaiumManager.getInstance().setLastAttackTime()

# Quest class and state definition
QUEST       = baium(-1, "baium", "ai")

# Quest NPC starter initialization
QUEST.addStartNpc(29025)
QUEST.addStartNpc(31862)
QUEST.addTalkId(29025)
QUEST.addTalkId(31862)
QUEST.addKillId(29020)
QUEST.addAttackId(29020)
