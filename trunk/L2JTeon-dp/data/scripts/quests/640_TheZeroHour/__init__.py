# Made by bareta
# Rate fix by Gnat
import sys
from net.sf.l2j import Config
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest

qn = "640_TheZeroHour"

#Npcs
KAHMAN = 31554

#Items
FANG = 8085 #FANG OF STAKATO

#MOBS
MOBS = [21508,21509,21510,21511,22105,22106,22107,22108,22109,22110,22111,22112,22113,22114,22115,22116,22117,22118,22119,22120,22121]

#chances
DROP_CHANCE = 80

#REWARDS(ID) ITEM - COST - QUANTITE
REWARDS = {
"1":[4042,12,1],   # 1 ENRIA COST 12 STAKATO FANGS
"2":[4043,6,1],    # 1 ASOFE COST 6 STAKATO FANGS
"3":[4044,5,1],    # 1 THONS COST 5 STAKATO FANGS
"4":[1887,81,10],  # 10 VARNISHES OF PURITY COST 81 STAKATO FANGS
"5":[1888,33,5],   # 5 SYNTETIC COKES COST 33 STAKATO FANGS
"6":[1889,30,10],  # 10 COMPOUND BRAIDS COST 30 STAKATO FANGS
"7":[5550,150,10], # 10 DURABLE METAL PLATES COST 150 STAKATO FANGS
"8":[1890,131,10], # 10 MITHRIL ALLOY COST 131 STAKATO FANGS
"9":[1893,123,5]   # 5 ORIHARUKON COST 123 STAKATO FANGS
}

class Quest (JQuest) :

 def __init__(self,id,name,descr): 
     JQuest.__init__(self,id,name,descr)
     self.questItemIds = []

 def onEvent (self,event,st) :
   cond = st.getInt("cond")
   htmltext = event
   if event == "" or event == "finish" :
     st.playSound("ItemSound.quest_finish")
     st.exitQuest(1)
     return #need to check for retail chat
   elif event == "31554-02.htm" :
     st.set("cond","1")
     st.setState(State.STARTED)
     st.playSound("ItemSound.quest_accept")
     htmltext = "31554-02.htm"
   elif event in REWARDS.keys() :
     item,cost,amount = REWARDS[event]
     if st.getQuestItemsCount(FANG) >= cost :
       st.takeItems(FANG,cost)
       st.rewardItems(item, amount)
     htmltext = "31554-06.htm"
   elif event == "more" :
     htmltext = "31554-02.htm"
   return htmltext

 def onTalk (self,npc,player):
   st = player.getQuestState(qn)
   htmltext = "<html><body>You are either not on a quest that involves this NPC, or you don't meet this NPC's minimum quest requirements.</body></html>"
   if not st : return htmltext

   npcId = npc.getNpcId()
   cond=st.getInt("cond")
   if npcId == KAHMAN : #Starting the quest
     if cond == 0 :
       st2 = player.getQuestState("109_InSearchOfTheNest")
       if st2 :
         if st2.getState() != State.COMPLETED :
           htmltext = "31554-00.htm"
           st.exitQuest(1)
         else :
           if st.getPlayer().getLevel() < 66 :
             htmltext = "31554-03.htm"
             st.exitQuest(1)
           else :
             htmltext = "31554-01.htm"
       else :
         htmltext = "31554-00.htm"
         st.exitQuest(1)
     elif cond == 1 and npcId == KAHMAN :
       htmltext = "31554-04.htm"
   return htmltext

 def onKill(self,npc,player,isPet):
   partyMember = self.getRandomPartyMember(player,"1")
   if not partyMember: return

   st = partyMember.getQuestState(qn)
   npcId = npc.getNpcId()
   if st :
     if npcId in MOBS :
       if st.getState() == State.STARTED and st.getInt("cond") == 1:
         numItems, chance = divmod(DROP_CHANCE*Config.RATE_DROP_QUEST,100)
         if st.getRandom(100) < chance :
            numItems += 1
         if numItems :
            st.playSound("ItemSound.quest_middle")
            st.giveItems(FANG,int(numItems))
   return

QUEST       = Quest(640,qn,"The Zero Hour")

QUEST.addStartNpc(KAHMAN)

QUEST.addTalkId(KAHMAN)

for mob in MOBS :
   QUEST.addKillId(mob)