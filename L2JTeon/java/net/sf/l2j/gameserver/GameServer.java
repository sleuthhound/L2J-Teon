/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.Server;
import net.sf.l2j.gameserver.OnlinePlayers;
import net.sf.l2j.gameserver.cache.ChatFilterCache;
import net.sf.l2j.gameserver.cache.CrestCache;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.communitybbs.Manager.ForumsBBSManager;
import net.sf.l2j.gameserver.datatables.ArmorSetsTable;
import net.sf.l2j.gameserver.datatables.AugmentationData;
import net.sf.l2j.gameserver.datatables.BufferSkillsTable;
import net.sf.l2j.gameserver.datatables.BuffTemplateTable;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.CharSchemesTable;
import net.sf.l2j.gameserver.datatables.CharTemplateTable;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.datatables.DoorTable;
import net.sf.l2j.gameserver.datatables.EventDroplist;
import net.sf.l2j.gameserver.datatables.ExtractableItemsData;
import net.sf.l2j.gameserver.datatables.FishTable;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.HelperBuffTable;
import net.sf.l2j.gameserver.datatables.HennaTable;
import net.sf.l2j.gameserver.datatables.HennaTreeTable;
import net.sf.l2j.gameserver.datatables.HeroSkillTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.LevelUpData;
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.datatables.NobleSkillTable;
import net.sf.l2j.gameserver.datatables.NpcBufferSkillIdsTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.NpcWalkerRoutesTable;
import net.sf.l2j.gameserver.datatables.PcColorTable;
import net.sf.l2j.gameserver.datatables.SkillSpellbookTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.datatables.StaticObjects;
import net.sf.l2j.gameserver.datatables.SummonItemsData;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.geoeditorcon.GeoEditorListener;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.AutoAnnouncementHandler;
import net.sf.l2j.gameserver.handler.ChatHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.handler.MasterHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.handler.UserCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.AuctionManager;
import net.sf.l2j.gameserver.instancemanager.AwayManager;
import net.sf.l2j.gameserver.instancemanager.BoatManager;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.FortManager;
import net.sf.l2j.gameserver.instancemanager.FortSiegeManager;
import net.sf.l2j.gameserver.instancemanager.FourSepulchersManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.CoupleManager;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.instancemanager.DayNightSpawnManager;
import net.sf.l2j.gameserver.instancemanager.DimensionalRiftManager;
import net.sf.l2j.gameserver.instancemanager.ItemsOnGroundManager;
import net.sf.l2j.gameserver.instancemanager.MercTicketManager;
import net.sf.l2j.gameserver.instancemanager.PetitionManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossPointsManager;
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.lib.L2jConnect;
import net.sf.l2j.gameserver.model.AutoChatHandler;
import net.sf.l2j.gameserver.model.AutoSpawnHandler;
import net.sf.l2j.gameserver.model.L2Manor;
import net.sf.l2j.gameserver.model.L2PetDataTable;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.ChainAutomation.L2JTeonEventManager;
import net.sf.l2j.gameserver.model.item.Item;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.TeonPacketHandler;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.L2GamePacketHandler;
import net.sf.l2j.gameserver.network.L2GameClient.ClientState;
import net.sf.l2j.gameserver.pathfinding.geonodes.GeoPathFinding;
import net.sf.l2j.gameserver.script.faenor.FaenorScriptEngine;
import net.sf.l2j.gameserver.scripting.CompiledScriptCache;
import net.sf.l2j.gameserver.scripting.L2ScriptEngineManager;
import net.sf.l2j.gameserver.taskmanager.TaskManager;
import net.sf.l2j.gameserver.util.DynamicExtension;
import net.sf.l2j.gameserver.util.FloodProtector;
import net.sf.l2j.status.Status;
import com.l2jserver.mmocore.network.SelectorServerConfig;
import com.l2jserver.mmocore.network.SelectorThread;
import net.sf.l2j.util.Util;

/**
 * This class ...
 * 
 * @version $Revision: 1.29.2.15.2.19 $ $Date: 2005/04/05 19:41:23 $
 */
public class GameServer
{
    private static final Logger _log = Logger.getLogger(GameServer.class.getName());
    private final SelectorThread<L2GameClient> _selectorThread;
    private final SkillTable _skillTable;
    private final ItemTable _itemTable;
    private final NpcTable _npcTable;
    private final HennaTable _hennaTable;
    private final IdFactory _idFactory;
    public static GameServer gameServer;
    private static ClanHallManager _cHManager;
    private final Shutdown _shutdownHandler;
    private final DoorTable _doorTable;
    private final SevenSigns _sevenSignsEngine;
    private final AutoChatHandler _autoChatHandler;
    private final AutoAnnouncementHandler _autoAnnouncementHandler;
    private final AutoSpawnHandler _autoSpawnHandler;
    private final LoginServerThread _loginThread;
    private final HelperBuffTable _helperBuffTable;
    private static Status _statusServer;
    @SuppressWarnings("unused")
    private final ThreadPoolManager _threadpools;
    public static final Calendar dateTimeServerStarted = Calendar.getInstance();

    public long getUsedMemoryMB()
    {
	return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // 1024
	// *
	// 1024
	// =
	// 1048576;
    }

    public SelectorThread<L2GameClient> getSelectorThread()
    {
	return _selectorThread;
    }

    public ClanHallManager getCHManager()
    {
	return _cHManager;
    }

    public GameServer() throws Exception
    {
		long serverLoadStart = System.currentTimeMillis();
        // Prints General System Info+        
        Util.printSection("L2JTeon-Info");
        L2JTeon.L2JTeon();
	gameServer = this;
	_log.finest("used mem:" + getUsedMemoryMB() + "MB");
	//Teon packet mapping
	TeonPacketHandler.getInstance();
	_idFactory = IdFactory.getInstance();
	_threadpools = ThreadPoolManager.getInstance();
	new File(Config.DATAPACK_ROOT, "data/clans").mkdirs();
	new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
	// load script engines
	L2ScriptEngineManager.getInstance();
	// start game time control early
	GameTimeController.getInstance();
        Util.printSection("Database");
	// keep the references of Singletons to prevent garbage collection
	CharNameTable.getInstance();
	if (!_idFactory.isInitialized())
	{
	    _log.severe("Could not read object IDs from DB. Please Check Your Data.");
	    throw new Exception("Could not initialize the ID factory");
	}
	_itemTable = ItemTable.getInstance();
	if (!_itemTable.isInitialized())
	{
	    _log.severe("Could not find the extraced files. Please Check Your Data.");
	    throw new Exception("Could not initialize the item table");
	}
	ExtractableItemsData.getInstance();
	SummonItemsData.getInstance();
	TradeController.getInstance();
	_skillTable = SkillTable.getInstance();
	if (!_skillTable.isInitialized())
	{
	    _log.severe("Could not find the extraced files. Please Check Your Data.");
	    throw new Exception("Could not initialize the skill table");
	}
    if (Config.ALLOW_NPC_WALKERS) 
    {
    	NpcWalkerRoutesTable.getInstance().load();
    }
	RecipeController.getInstance();
        Util.printSection("Skill Trees");
	SkillTreeTable.getInstance();
	ArmorSetsTable.getInstance();
	FishTable.getInstance();
	SkillSpellbookTable.getInstance();
	CharTemplateTable.getInstance();
	NobleSkillTable.getInstance();
	HeroSkillTable.getInstance();
	PcColorTable.getInstance();
	Util.printSection("Cache");
	// Call to load caches
	ChatFilterCache.getInstance();
	HtmCache.getInstance();
	CrestCache.getInstance();
	ClanTable.getInstance();
	MapRegionTable.getInstance();
	SpawnTable.getInstance();
	GmListTable.getInstance();
	_npcTable = NpcTable.getInstance();
	if (!_npcTable.isInitialized())
	{
	    _log.severe("Could not find the extraced files. Please Check Your Data.");
	    throw new Exception("Could not initialize the npc table");
	}
	_hennaTable = HennaTable.getInstance();
	if (!_hennaTable.isInitialized())
	{
	    throw new Exception("Could not initialize the Henna Table");
	}
	HennaTreeTable.getInstance();
	if (!_hennaTable.isInitialized())
	{
	    throw new Exception("Could not initialize the Henna Tree Table");
	}
	_helperBuffTable = HelperBuffTable.getInstance();
	/**
	 * NPCBUFFER: Import Table for NpcBuffer Core Side Buffer
	 */
	BuffTemplateTable.getInstance();
	_log.config("BuffTemplateTable Initialized");
	if (!_helperBuffTable.isInitialized())
	{
	    throw new Exception("Could not initialize the Helper Buff Table");
	}
        Util.printSection("Geodata");
	GeoData.getInstance();
	if (Config.GEODATA == 2)
	{
	    GeoPathFinding.getInstance();
	}
        Util.printSection("Instances Managers");
	// Load clan hall data before zone data
	_cHManager = ClanHallManager.getInstance();
	CastleManager.getInstance();
	FortManager.getInstance();
	SiegeManager.getInstance();
	FortSiegeManager.getInstance();
	TeleportLocationTable.getInstance();
	LevelUpData.getInstance();
	L2World.getInstance();
	ZoneManager.getInstance();
	DayNightSpawnManager.getInstance().notifyChangeMode();
	GrandBossManager.getInstance();
	RaidBossSpawnManager.getInstance();
        RaidBossPointsManager.init();
	DimensionalRiftManager.getInstance();
	Announcements.getInstance();
	EventDroplist.getInstance();
	/** Load Manor data */
	L2Manor.getInstance();
	/** Load Manager */
	AuctionManager.getInstance();
	BoatManager.getInstance();
	CastleManorManager.getInstance();
	MercTicketManager.getInstance();
	// PartyCommandManager.getInstance();
	PetitionManager.getInstance();
	// Init of a cursed weapon manager
	CursedWeaponsManager.getInstance();
	FourSepulchersManager.getInstance().init();
        Util.printSection("Quests - Scripts");
		QuestManager.getInstance();
		
		try
		{
			_log.info("Loading Server Scripts");
			File scripts = new File(Config.DATAPACK_ROOT + "/data/scripts.cfg");
			L2ScriptEngineManager.getInstance().executeScriptList(scripts);
		}
		catch (IOException ioe)
		{
			_log.severe("Failed loading scripts.cfg, no script going to be loaded");
		}
		try
		{
			CompiledScriptCache compiledScriptCache = L2ScriptEngineManager.getInstance().getCompiledScriptCache();
			if (compiledScriptCache == null)
			{
				_log.info("Compiled Scripts Cache is disabled.");
			}
			else
			{
				compiledScriptCache.purge();
				
				if (compiledScriptCache.isModified())
				{
					compiledScriptCache.save();
					_log.info("Compiled Scripts Cache was saved.");
				}
				else
				{
					_log.info("Compiled Scripts Cache is up-to-date.");
				}
			}
			
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "Failed to store Compiled Scripts Cache.", e);
		}
		QuestManager.getInstance().report();
		
		AugmentationData.getInstance();
		if (Config.ALLOW_AWAY_STATUS)
			_log.info("Away System");
			AwayManager.getInstance();
	if (Config.SAVE_DROPPED_ITEM)
	{
	    ItemsOnGroundManager.getInstance();
	}
	if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
	{
	    ItemsAutoDestroy.getInstance();
	}
	// Couple manager
	if (!Config.ALLOW_WEDDING)
	{
	    CoupleManager.getInstance();
	    // if ( _log.isDebugEnabled())_log.debug("CoupleManager
	    // initialized");
	}
	MonsterRace.getInstance();
	_doorTable = DoorTable.getInstance();
	_doorTable.parseData();
	StaticObjects.getInstance();
	_sevenSignsEngine = SevenSigns.getInstance();
	SevenSignsFestival.getInstance();
	_autoSpawnHandler = AutoSpawnHandler.getInstance();
	_autoChatHandler = AutoChatHandler.getInstance();
	_autoAnnouncementHandler = AutoAnnouncementHandler.getInstance();
	// Spawn the Orators/Preachers if in the Seal Validation period.
	_sevenSignsEngine.spawnSevenSignsNPC();
	Olympiad.getInstance();
	Hero.getInstance();
	FaenorScriptEngine.getInstance();
	TaskManager.getInstance();
	GmListTable.getInstance();
	Util.printSection("Handlers");
	AdminCommandHandler.getInstance();
	ChatHandler.getInstance();
	ItemHandler.getInstance();
	SkillHandler.getInstance();
	UserCommandHandler.getInstance();
	VoicedCommandHandler.getInstance();
	MasterHandler.load();
	AutoAnnouncementHandler.getInstance();
	// read pet stats from db
	L2PetDataTable.getInstance().loadPetsData();
	Universe.getInstance();
	if (Config.ACCEPT_GEOEDITOR_CONN)
	    GeoEditorListener.getInstance();
	_shutdownHandler = Shutdown.getInstance();
	Runtime.getRuntime().addShutdownHook(_shutdownHandler);
	try
	{
	    _doorTable.getDoor(24190001).openMe();
	    _doorTable.getDoor(24190002).openMe();
	    _doorTable.getDoor(24190003).openMe();
	    _doorTable.getDoor(24190004).openMe();
	    _doorTable.getDoor(23180001).openMe();
	    _doorTable.getDoor(23180002).openMe();
	    _doorTable.getDoor(23180003).openMe();
	    _doorTable.getDoor(23180004).openMe();
	    _doorTable.getDoor(23180005).openMe();
	    _doorTable.getDoor(23180006).openMe();
	    _doorTable.checkAutoOpen();
	} catch (NullPointerException e)
	{
	    _log.warning("There is errors in your Door.csv file. Update door.csv");
	    if (Config.DEBUG)
	    {
		e.printStackTrace();
	    }
	}
	ForumsBBSManager.getInstance();
	_log.config("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());
	// initialize the dynamic extension loader
	try
	{
	    DynamicExtension.getInstance();
	} catch (Exception ex)
	{
	    _log.log(Level.WARNING, "DynamicExtension could not be loaded and initialized", ex);
	}
	FloodProtector.getInstance();
        /** NPC Buffer by House */
        if (Config.NPCBUFFER_FEATURE_ENABLED)
        {
        	BufferSkillsTable.getInstance();
        	CharSchemesTable.getInstance();
        }
	L2Manor.getInstance();
	System.gc();
	// maxMemory is the upper limit the jvm can use, totalMemory the size of
	// the current allocation pool, freeMemory the unused memory in the
	// allocation pool
	long freeMem = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1048576; // 1024
	// *
	// 1024
	// =
	// 1048576;
	long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
	_log.info("GameServer Started, free memory " + freeMem + " Mb of " + totalMem + " Mb");
	_loginThread = LoginServerThread.getInstance();
	_loginThread.start();
	SelectorServerConfig ssc = new SelectorServerConfig(Config.PORT_GAME);
	L2GamePacketHandler gph = new L2GamePacketHandler();
	_selectorThread = new SelectorThread<L2GameClient>(ssc, gph, gph, gph);
	_selectorThread.openServerSocket();
	_selectorThread.start();
	_log.config("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);

	Util.printSection("Teon Packet System");
	_log.info("Loaded: "+TeonPacketHandler.getInstance().getPackets().get(ClientState.CONNECTED).size()+" Connection Packet(s).");
	_log.info("Loaded: "+TeonPacketHandler.getInstance().getPackets().get(ClientState.AUTHED).size()+" Auth Packet(s).");
	_log.info("Loaded: "+TeonPacketHandler.getInstance().getPackets().get(ClientState.IN_GAME).size()+" In Game Packet(s).");
	_log.info("Loaded: "+TeonPacketHandler.getInstance().get2xOpPackets().get(0xd0).size()+" Double OpCode Packet(s).");
	// Start IRC Connection
	if (Config.IRC_LOAD)
	{
	    L2jConnect serverConnect = new L2jConnect();
	    serverConnect.L2jConnect();
	    serverConnect.doSendMsg();
	}
	if (Config.ONLINE_PLAYERS_AT_STARTUP)
	{
	    OnlinePlayers.getInstance();
	}
	L2JTeonEventManager.getInstance();
	if (Config.ENABLE_FACTION_KOOFS_NOOBS)
	{
	    System.out.println("####################################");
	    System.out.println("## L2JTeon KvN Mode is Activated. ##");
	    System.out.println("####################################");
	} else
	{
	    System.out.println("###################################");
	    System.out.println("## L2JTeon KvN Mode is Disabled. ##");
	    System.out.println("###################################");
	}
    }

    public static void main(String[] args) throws Exception
    {
	Server.serverMode = Server.MODE_GAMESERVER;
	// Local Constants
	final String LOG_FOLDER = "log"; // Name of folder for log file
	final String LOG_NAME = "./log.cfg"; // Name of log file
	/** * Main ** */
	// Create log folder
	File logFolder = new File(Config.DATAPACK_ROOT, LOG_FOLDER);
	logFolder.mkdir();
	// Create input stream for log file -- or store file data into memory
	InputStream is = new FileInputStream(new File(LOG_NAME));
	LogManager.getLogManager().readConfiguration(is);
	is.close();
	Util.printSection("Configs");
	// Initialize config
	Config.load();
	L2DatabaseFactory.getInstance();
	gameServer = new GameServer();
	if (Config.IS_TELNET_ENABLED)
	{
	    _statusServer = new Status(Server.serverMode);
	    _statusServer.start();
	} else
	{
	    System.out.println("Telnet server is currently disabled.");
	}
    }
}