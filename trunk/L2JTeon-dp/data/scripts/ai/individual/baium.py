#L2JTeon
import sys
from net.sf.l2j.gameserver.ai import CtrlIntention
from net.sf.l2j.gameserver.datatables import SkillTable
from net.sf.l2j.gameserver.datatables import SpawnTable
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.instancemanager import GrandBossManager
from net.sf.l2j.gameserver.serverpackets import SocialAction
from net.sf.l2j.gameserver.serverpackets import Earthquake
from net.sf.l2j.gameserver.serverpackets import PlaySound
from net.sf.l2j.util import Rnd
from java.lang import System
print "Baium"

STONE_BAIUM = 29025
ARCHANGEL = 29021
ANGELIC_VORTEX = 31862
LIVE_BAIUM = 29020

#Baium status tracking
ASLEEP = 0  # baium is in the stone version, waiting to be woken up.  Entry is unlocked
AWAKE = 1   # baium is awake and fighting.  Entry is locked.
DEAD = 2    # baium has been killed and has not yet spawned.  Entry is locked

#SKILL
SPEAR_POUND = 4132
ANGEL_HEAL  = 4133

class baium (JQuest):

  def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

  def init_LoadGlobalData(self) :
    # initialize
    self.lastAttackVsBaiumTime = 0
    self.baiumZone = GrandBossManager.getInstance().getZone(113100,14500,10077)
    info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM)
    status = GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM)
    if status == DEAD :    
      # load the unlock date and time for baium from DB
      temp = long(info.getLong("respawn_time")) - System.currentTimeMillis()
      if temp > 0 :
        # the unlock time has not yet expired.  Mark Baium as currently locked (dead).  Setup a timer
        # to fire at the correct time (calculate the time between now and the unlock time,
        # setup a timer to fire after that many msec)
        self.startQuestTimer("baium_unlock", temp, None, None)
      else :
        # the time has already expired while the server was offline.  Delete the saved time and
        # immediately spawn the stone-baium.  Also the state need not be changed from ASLEEP
        self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)
        GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM,ASLEEP)
    elif status == AWAKE :
      loc_x = info.getInteger("loc_x")
      loc_y = info.getInteger("loc_y")
      loc_z = info.getInteger("loc_z")
      heading = info.getInteger("heading")
      hp = info.getInteger("currentHP")
      mp = info.getInteger("currentMP")
      baium = self.addSpawn(LIVE_BAIUM,loc_x,loc_y,loc_z,heading,False,0)
      GrandBossManager.getInstance().addBoss(baium)
      baium.setCurrentHpMp(hp,mp)
      baium.broadcastPacket(SocialAction(baium.getObjectId(),2))
      self.startQuestTimer("baium_wakeup",15000, baium, None)
    else :
      self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)
    return

  def onAdvEvent (self,event,npc,player):
    if event == "baium_unlock" :
      GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM,ASLEEP)
      self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)
    elif event == "baium_wakeup" and npc:
      if npc.getNpcId() == LIVE_BAIUM :
        npc.broadcastPacket(SocialAction(npc.getObjectId(),1))
        npc.broadcastPacket(Earthquake(npc.getX(), npc.getY(), npc.getZ(),40,5))
        # start monitoring baium's inactivity
        self.lastAttackVsBaiumTime = System.currentTimeMillis()
        self.startQuestTimer("baium_despawn", 60000, npc, None)
        # TODO: the person who woke baium up should be knocked across the room, onto a wall, and
        # lose massive amounts of HP.
    # despawn the live baium after 30 minutes of inactivity
    # also check if the players are cheating, having pulled Baium outside his zone...
    elif event == "baium_despawn" and npc:
      if npc.getNpcId() == LIVE_BAIUM :
        # just in case the zone reference has been lost (somehow...), restore the reference
        if not self.baiumZone :
          self.baiumZone = GrandBossManager.getInstance().getZone(113100,14500,10077)
        if (self.lastAttackVsBaiumTime + 1800000 < System.currentTimeMillis()) :
          npc.deleteMe()   # despawn the live-baium
          self.addSpawn(STONE_BAIUM,115213,16623,10080,41740,False,0)  # spawn stone-baium
          GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM,ASLEEP)    # mark that Baium is not awake any more
          self.baiumZone.oustAllPlayers()
          self.cancelQuestTimer("baium_despawn", npc, None)
        else :
          # if Baium is not inside his zone (i.e. he has been pulled outside), teleport him back in.
          if not self.baiumZone.isInsideZone(npc) :
            npc.teleToLocation(115213,16623,10080)
    return

  def onTalk (self,npc,player):
    npcId = npc.getNpcId()
    htmltext = "none"
    if not self.baiumZone :
      self.baiumZone = GrandBossManager.getInstance().getZone(113100,14500,10077)
    if npcId == STONE_BAIUM and GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == ASLEEP:
      if self.baiumZone.isPlayerAllowed(player) :
        # once Baium is awaken, no more people may enter until he dies, the server reboots, or 
        # 30 minutes pass with no attacks made against Baium.
        GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM,AWAKE)
        npc.deleteMe()
        player.setCurrentHp(0)
        player.doDie(player)
        baium = self.addSpawn(LIVE_BAIUM,player)
        GrandBossManager.getInstance().addBoss(baium)
        baium.broadcastPacket(SocialAction(baium.getObjectId(),2))
        self.startQuestTimer("baium_wakeup",15000, baium, None)
      else:
        htmltext = "Conditions are not right to wake up Baium"
    elif npcId == ANGELIC_VORTEX :
      if GrandBossManager.getInstance().getBossStatus(LIVE_BAIUM) == ASLEEP :
        if player.isFlying() :
          print "Player "+player.getName()+" attempted to enter Baium's lair while flying!"
          htmltext = '<html><body>Angelic Vortex:<br>You may not enter while flying a wyvern</body></html>'
        elif player.getQuestState("baium").getQuestItemsCount(4295) : # bloody fabric
          player.getQuestState("baium").takeItems(4295,1)
          # allow entry for the player for the next 30 secs (more than enough time for the TP to happen)
          # Note: this just means 30secs to get in, no limits on how long it takes before we get out.
          self.baiumZone.allowPlayerEntry(player,30)
          player.teleToLocation(113100,14500,10077)
        else :
          htmltext = '<html><body>Angelic Vortex:<br>You do not have enough items</body></html>'
      else :
        htmltext = '<html><body>Angelic Vortex:<br>You may not enter at this time</body></html>'
    return htmltext
    
  def onAttack(self, npc, player, damage, isPet) :
    if npc.getNpcId() == ARCHANGEL:
      if Rnd.get(100) < 10 :
        self.skill = SkillTable.getInstance().getInfo(SPEAR_POUND,1)
        if self.skill != None :
          npc.setTarget(player)
          npc.doCast(self.skill)
      if Rnd.get(100) < 5 and ((npc.getStatus().getCurrentHp() / npc.getStat().getMaxHp())*100) < 50:
        self.skill = SkillTable.getInstance().getInfo(ANGEL_HEAL,1)
        if self.skill != None :
          npc.setTarget(npc)
          npc.doCast(self.skill)
    else:
      # update a variable with the last action against baium
      self.lastAttackVsBaiumTime = System.currentTimeMillis()
    return
    
  def onKill(self,npc,player,isPet):
    self.cancelQuestTimer("baium_despawn", npc, None)    
    npc.broadcastPacket(PlaySound(1, "BS01_D", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()))
    # spawn the "Teleportation Cubic" for 15 minutes (to allow players to exit the lair)
    self.addSpawn(29055,115203,16620,10078,0,False,900000) ##should we teleport everyone out if the cubic despawns??
    # "lock" baium for 5 days and 1 to 8 hours [i.e. 432,000,000 +  1*3,600,000 + random-less-than(8*3,600,000) millisecs]
    respawnTime = long((121 + Rnd.get(8)) * 3600000)
    GrandBossManager.getInstance().setBossStatus(LIVE_BAIUM,DEAD)
    self.startQuestTimer("baium_unlock", respawnTime, None, None)
    # also save the respawn time so that the info is maintained past reboots
    info = GrandBossManager.getInstance().getStatsSet(LIVE_BAIUM)
    info.set("respawn_time",(long(System.currentTimeMillis()) + respawnTime))
    GrandBossManager.getInstance().setStatsSet(LIVE_BAIUM,info)

# Quest class and state definition
QUEST       = baium(-1, "baium", "ai")

# Quest NPC starter initialization
QUEST.addStartNpc(STONE_BAIUM)
QUEST.addStartNpc(ANGELIC_VORTEX)
QUEST.addTalkId(STONE_BAIUM)
QUEST.addTalkId(ANGELIC_VORTEX)

QUEST.addAttackId(ARCHANGEL)
QUEST.addAttackId(LIVE_BAIUM)
QUEST.addKillId(LIVE_BAIUM)