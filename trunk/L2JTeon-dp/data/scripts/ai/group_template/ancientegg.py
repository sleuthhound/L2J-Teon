import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.datatables import SkillTable
from java.lang import System

EGG = 18344

class AncientEgg(JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onAttack (self,npc,player,damage,isPet):
   player.setTarget(player)
   player.doCast(SkillTable.getInstance().getInfo(5088,1))
   return

QUEST = AncientEgg(-1, "ancientegg", "ai")

QUEST.addAttackId(EGG)
