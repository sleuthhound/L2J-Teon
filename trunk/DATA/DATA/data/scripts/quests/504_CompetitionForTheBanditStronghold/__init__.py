# L2JTeon
import sys
from net.sf.l2j.gameserver.instancemanager.clanhallsiege import BanditStrongholdManager
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "504_CompetitionForTheBanditStronghold"

# npcId
Messenger = 35437

# itemId list
TarlkAmulet = 4332
AlianceTrophey = 5009

# Quest mobs
TarlkBugbear = 20570
TarlkBugbearWarrior = 20571
TarlkBugbearHighWarrior = 20572
TarlkBasilisk = 20573
ElderTarlkBasilisk = 20574

class Quest (JQuest) :

	def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

	def onEvent(self, event, st):
		htmltext = event
		if event == "a2.htm" :
			st.set("cond", "1")
			st.setState(State.STARTED)
			st.playSound("ItemSound.quest_accept")
		if event == "a4.htm" :
			if st.getQuestItemsCount(TarlkAmulet) == 30 :
				st.takeItems(TarlkAmulet,-30)
				st.giveItems(AlianceTrophey,1)
				st.playSound("ItemSound.quest_finish")
				st.exitQuest(1)
			else :
				htmltext = "a5.htm"
		return htmltext

	def onTalk (self,npc,player):
		htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>"
		st = player.getQuestState(qn)
		npcId = npc.getNpcId()
		cond = st.getInt("cond")
		clan = player.getClan();
		if clan == None:
			htmltext = "a6.htm"
			return htmltext
		if clan.getLevel() < 4:
			htmltext = "a6.htm"
			return htmltext
		if not clan.getLeaderName() == player.getName():
			htmltext = "a6.htm"
			return htmltext
		if BanditStrongholdManager.getInstance().isRegistrationPeriod():
			if npcId == Messenger :
				if cond == 0 :
					htmltext = "a1.htm"
				elif cond > 1 :
					htmltext = "a3.htm"
		else:
			htmltext = None
			npc.showChatWindow(player,3)
		return htmltext

	def onKill(self,npc,player,isPet): 
		st = player.getQuestState(qn) 
		if not st : return 
		if st.getState() != State.STARTED : return 
		npcId = npc.getNpcId() 
		if st.getQuestItemsCount(TarlkAmulet) < 30 :
			st.giveItems(TarlkAmulet,1)
			st.playSound("ItemSound.quest_itemget")
			if st.getQuestItemsCount(TarlkAmulet) == 30 :
				st.set("cond", "2")

QUEST = Quest(504,qn,"Competition for the Bandit Stronghold")

QUEST.addStartNpc(Messenger)
QUEST.addTalkId(Messenger)
QUEST.addKillId(TarlkBugbear)
QUEST.addKillId(TarlkBugbearWarrior)
QUEST.addKillId(TarlkBugbearHighWarrior)
QUEST.addKillId(TarlkBasilisk)
QUEST.addKillId(ElderTarlkBasilisk)