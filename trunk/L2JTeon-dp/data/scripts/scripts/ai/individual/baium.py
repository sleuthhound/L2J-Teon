import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import SocialAction
from net.sf.l2j.gameserver.serverpackets import Earthquake
from net.sf.l2j.gameserver.model import L2CharPosition
from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.util import Rnd
from java.lang import System
from net.sf.l2j.gameserver.serverpackets import MagicSkillUser
from net.sf.l2j.gameserver.datatables import SkillTable
from net.sf.l2j.gameserver.serverpackets import PlaySound

STONE_BAIUM = 29025
ANGELIC_VORTEX = 31862
LIVE_BAIUM = 29020
ARHANGEL = 29021
BLOODED_FABRIC = 4295

class Baium(JQuest):

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def init_LoadGlobalData(self) :
   underatak = self.loadGlobalQuestVar("underattack")
   ubitt = self.loadGlobalQuestVar("ubit")
   if ubitt == "1" :    
     temp = long(self.loadGlobalQuestVar("respawn")) - System.currentTimeMillis()
     if temp > 0 :
       print "Baium:  dead"
       self.deleteGlobalQuestVar("underattack")
       self.deleteGlobalQuestVar("life")
       self.deleteGlobalQuestVar("open")
       self.deleteGlobalQuestVar("lasthit")
       self.startQuestTimer("resp", temp, None, None)
     else :
       print "Baium:  live"
       self.deleteGlobalQuestVar("open")
       self.deleteGlobalQuestVar("ubit")
       self.deleteGlobalQuestVar("respawn")
       self.deleteGlobalQuestVar("lasthit")
       self.saveGlobalQuestVar("spit", "1")
       self.addSpawn(STONE_BAIUM,116040,17455,10078,40240,False,0)
   elif underatak == "1" :
     print "Baium:  under attack"
     self.deleteGlobalQuestVar("lasthit")
     self.deleteGlobalQuestVar("underattack")
   else :
     print "Baium: live"
     self.deleteGlobalQuestVar("open")
     self.deleteGlobalQuestVar("ubit")
     self.deleteGlobalQuestVar("respawn")
     self.deleteGlobalQuestVar("lasthit")
     self.saveGlobalQuestVar("spit", "1")
     self.addSpawn(STONE_BAIUM,116040,17455,10078,40240,False,0)
   return

 def onAdvEvent (self,event,npc,player):
   underatak = self.loadGlobalQuestVar("underattack")
   statue = self.loadGlobalQuestVar("spit")
   ubitt = self.loadGlobalQuestVar("ubit")
   if event == "komne" :
     player.teleToLocation(115922,17342,10051)
     self.cancelQuestTimer("komne",npc,None)
   if event == "prosnuca" :
     player.broadcastPacket(SocialAction(npc.getObjectId(),1))
     player.broadcastPacket(Earthquake(npc.getX(), npc.getY(), npc.getZ(),40,5))
     if Rnd.get(100) < 45 : 
       #player.reduceCurrentHp(player.getCurrentHp(),player)
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4136,1))
     self.startQuestTimer("angels",2000, npc, player) 
     self.cancelQuestTimer("prosnuca",npc,None)
   if event == "angels":
     angel = self.addSpawn(ARHANGEL,115792,16608,10136,0,False,0)
     angel.setTarget(angel)
     angel.doCast(SkillTable.getInstance().getInfo(45,1))
     angell = self.addSpawn(ARHANGEL,115168,17200,10136,0,False,0)
     angell.setTarget(angell)
     angell.doCast(SkillTable.getInstance().getInfo(45,1))
     angelll = self.addSpawn(ARHANGEL,115780,15564,10136,13620,False,0)
     angelll.setTarget(angelll)
     angelll.doCast(SkillTable.getInstance().getInfo(45,1))
     angellll = self.addSpawn(ARHANGEL,114880,16236,10136,5400,False,0)
     angellll.setTarget(angellll)
     angellll.doCast(SkillTable.getInstance().getInfo(45,1))
     angelllll = self.addSpawn(ARHANGEL,114239,17168,10136,-1992,False,0)
     angelllll.setTarget(angelllll)
     angelllll.doCast(SkillTable.getInstance().getInfo(45,1))
     self.startQuestTimer("desp_angel",1200000,angel,None) #1800000
     self.startQuestTimer("desp_angell",1200000,angell,None) #1800000
     self.startQuestTimer("desp_angelll",1200000,angelll,None) #1800000
     self.startQuestTimer("desp_angellll",1200000,angellll,None) #1800000
     self.startQuestTimer("desp_angelllll",1200000,angelllll,None) #1800000
   if event == "desp_angel" :
     if underatak == "" or statue == "1" or ubitt == "1":
       npc.deleteMe()
     else :
       self.startQuestTimer("desp_angel",1200000,npc,None)
   if event == "desp_angell" :
     if underatak == "" or statue == "1" or ubitt == "1":
       npc.deleteMe()
     else :
       self.startQuestTimer("desp_angel",1200000,npc,None)
   if event == "desp_angelll" :
     if underatak == "" or statue == "1" or ubitt == "1":
       npc.deleteMe()
     else :
       self.startQuestTimer("desp_angel",1200000,npc,None)
   if event == "desp_angellll" :
     if underatak == "" or statue == "1" or ubitt == "1":
       npc.deleteMe()
     else :
       self.startQuestTimer("desp_angel",1200000,npc,None)
   if event == "desp_angelllll" :
     if underatak == "" or statue == "1" or ubitt == "1":
       npc.deleteMe()
     else :
       self.startQuestTimer("desp_angel",1200000,npc,None)
   if event == "close" :
     self.saveGlobalQuestVar("underattack", "1")
     self.cancelQuestTimer("close",npc,None) 
   if event == "vkrovatku" :
     if statue == "" :
       if underatak == "" :
         npc.deleteMe()
         self.saveGlobalQuestVar("spit", "1")
         self.deleteGlobalQuestVar("open")
         self.deleteGlobalQuestVar("underattack")
         self.addSpawn(STONE_BAIUM,116040,17455,10078,40240,False,0)
         self.cancelQuestTimer("vkrovatku",npc,None)
       else :
         self.deleteGlobalQuestVar("underattack")
         self.startQuestTimer("lastchek",30000, npc, player)
   if event == "lastchek" :
     if underatak == "" :
       npc.deleteMe()
       self.saveGlobalQuestVar("spit", "1")
       self.deleteGlobalQuestVar("open")
       self.deleteGlobalQuestVar("underattack")
       self.addSpawn(STONE_BAIUM,116040,17455,10078,40240,False,0)
       self.cancelQuestTimer("lastchek",npc,None)
     else :
       self.deleteGlobalQuestVar("underattack")
       self.startQuestTimer("vkrovatku",1200000,npc,None)
   elif event == "resp" :
     self.deleteGlobalQuestVar("ubit")
     self.saveGlobalQuestVar("spit", "1")
     self.addSpawn(STONE_BAIUM,116108,17526,10080,41740,False,0)
     self.cancelQuestTimer("resp",npc,None)      
   return

 def onTalk (self,npc,player):
   st = player.getQuestState("baium")  
   npcId = npc.getNpcId()
   openn = self.loadGlobalQuestVar("open")
   underatak = self.loadGlobalQuestVar("underattack")
   ubitt = self.loadGlobalQuestVar("ubit")
   if npcId == ANGELIC_VORTEX :
     if ubitt == "" :
       if st.getQuestItemsCount(BLOODED_FABRIC) >= 1:
         if underatak == "" :
           if openn == "" :
             self.saveGlobalQuestVar("open", "1")
             st.takeItems(BLOODED_FABRIC,1)
             player.teleToLocation(113100,14500,10077)
             self.startQuestTimer("close",30000,npc,player)
           elif openn == "1" :
             st.takeItems(BLOODED_FABRIC,1)
             player.teleToLocation(113100,14500,10077)
           return
         else :
           return "<html><body><font color=LEVEL>Baium is under attack...</font><br>Try another time.</body></html>"
       else :
         return "<html><body>You need <font color=LEVEL>Blooded Fabric</font> to enter...</body></html>"
     else : 
       return "<html><body><font color=LEVEL>Baium was killed...</font><br>Try another time.</body></html>"
   if npcId == STONE_BAIUM :
     if openn == "1" :
       npc.deleteMe()
       baium = self.addSpawn(LIVE_BAIUM,npc)
       baium.broadcastPacket(SocialAction(baium.getObjectId(),2))
       self.startQuestTimer("komne", 9000, baium, player)
       self.startQuestTimer("prosnuca",11000, baium, player)
       self.deleteGlobalQuestVar("spit")
       self.startQuestTimer("vkrovatku",1200000,baium,None) #1800000
     else:
       return "<html><body><font color=LEVEL>Statuja ne reagiruet na vas...</font></body></html>"
   return
    
 def onAttack (self,npc,player,damage,isPet):
   self.saveGlobalQuestVar("underattack", "1")
   maxHp = npc.getMaxHp()
   nowHp = npc.getCurrentHp()
   if nowHp < maxHp*0.25:
     if (Rnd.get(100) < 10):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4130,1))
     elif (Rnd.get(100) < 15):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4131,1))
     elif (Rnd.get(100) < 20):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4128,1))
     elif (Rnd.get(100) < 30):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4129,1))
     else:
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4127,1))
   elif nowHp < maxHp*0.5:
     if (Rnd.get(100) < 10):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4131,1))
     elif (Rnd.get(100) < 15):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4128,1))
     elif (Rnd.get(100) < 25):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4129,1))
     else:
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4127,1))
   elif nowHp < maxHp*0.75:
     if (Rnd.get(100) < 10):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4128,1))
     elif (Rnd.get(100) < 15):
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4129,1))
     else:
       npc.setTarget(player)
       npc.doCast(SkillTable.getInstance().getInfo(4127,1))
   elif (Rnd.get(100) < 10):
     npc.setTarget(player)
     npc.doCast(SkillTable.getInstance().getInfo(4130,1))
   elif (Rnd.get(100) < 15):
     npc.setTarget(player)
     npc.doCast(SkillTable.getInstance().getInfo(4131,1))
   elif (Rnd.get(100) < 25):
     npc.setTarget(player)
     npc.doCast(SkillTable.getInstance().getInfo(4128,1))
   elif (Rnd.get(100) < 30):
     npc.setTarget(player)
     npc.doCast(SkillTable.getInstance().getInfo(4129,1))
   else:
     npc.setTarget(player)
     npc.doCast(SkillTable.getInstance().getInfo(4127,1))
   return
    
 def onKill(self,npc,player,isPet):
   npc.broadcastPacket(PlaySound(1, "BS01_D", 1, npc.getObjectId(), player.getX(), player.getY(), player.getZ()))
   self.addSpawn(29055,115203,16620,10078,0,False,900000)
   self.deleteGlobalQuestVar("underattack")
   self.deleteGlobalQuestVar("lasthit")
   self.deleteGlobalQuestVar("open")
   self.cancelQuestTimer("vkrovatku",None,None)
   respawnTime = long((121 + Rnd.get(8)) * 3600000)
   self.saveGlobalQuestVar("ubit", "1")
   self.saveGlobalQuestVar("respawn", str(System.currentTimeMillis() + respawnTime))
   self.startQuestTimer("resp", respawnTime, None, None)
   print "GrandBossManager:  Baium was killed."
   return

QUEST = Baium(-1,"baium","ai")

QUEST.addStartNpc(ANGELIC_VORTEX)
QUEST.addStartNpc(STONE_BAIUM)
QUEST.addTalkId(ANGELIC_VORTEX)
QUEST.addTalkId(STONE_BAIUM)
QUEST.addKillId(LIVE_BAIUM)
QUEST.addAttackId(LIVE_BAIUM)
