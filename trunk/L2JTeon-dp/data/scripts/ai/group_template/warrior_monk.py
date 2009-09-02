import sys
from net.sf.l2j.util import Rnd
from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import NpcSay

class warrior_monk(JQuest) :

  def __init__(self, id, name, descr) :
    self.warrior_monk = 22129
    self.FirstAttacked = False
    JQuest.__init__(self, id, name, descr)

  def onAttack(self, npc, player, damage, isPet) :
    objId = npc.getObjectId()
    if self.FirstAttacked:
       if Rnd.get(100) : return
       npc.broadcastPacket(NpcSay(objId, 0, npc.getNpcId(), "Brother "+player.getName()+", move your weapon away!!"))
    else :
       self.FirstAttacked = True
    return

  def onSpawn(self, npc) :
    objId = npc.getObjectId()
    for player in npc.getKnownList().getKnownPlayers().values() :
      if player.isInsideRadius(npc, 500, False, False) :
        if player.getActiveWeaponItem() :
          npc.broadcastPacket(NpcSay(objId, 0, npc.getNpcId(), "Brother "+player.getName()+", move your weapon away!!"))
          npc.addDamageHate(player, 0, 999)
          npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player)
        else :
          npc.getAggroListRP().remove(player)
          npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, None, None)
    return

  def onAggroRangeEnter(self, npc, player, isPet) :
    objId = npc.getObjectId()
    for player in npc.getKnownList().getKnownPlayers().values() :
      if player.isInsideRadius(npc, 500, False, False) :
        if player.getActiveWeaponItem() :
          npc.broadcastPacket(NpcSay(objId, 0, npc.getNpcId(), "Brother "+player.getName()+", move your weapon away!!"))
          npc.addDamageHate(player, 0, 999)
          npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player)
        else :
          npc.getAggroListRP().remove(player)
          npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, None, None)
    return

QUEST = warrior_monk(-1, "warrior_monk", "ai")

QUEST.addAttackId(QUEST.warrior_monk)

QUEST.addSpawnId(QUEST.warrior_monk)

QUEST.addAggroRangeEnterId(QUEST.warrior_monk)