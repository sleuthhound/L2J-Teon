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
package net.sf.l2j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * This class containce global server configuration.<br>
 * It has static final fields initialized from configuration files.<br>
 * It's initialized at the very begin of startup, and later JIT will optimize
 * away debug/unused code.
 *
 * @author mkizub
 */
public final class Config
{
    protected static final Logger _log = Logger.getLogger(Config.class.getName());
    
    // Configuration files
    /**
     * Properties file that allows selection of new Classes for storage of World Objects. <br>
     * This may help servers with large amounts of players recieving error messages related to the 
     * <i>L2ObjectHashMap</i> and
     * <i>L2ObejctHashSet</i> classes.
     */
    /**
     * Properties file for game server (connection and ingame) configurations
     */
    public static final String CONFIGURATION_FILE = "./config/server.properties";
    /** Properties file for game server options */
    public static final String OPTIONS_FILE = "./config/options.properties";
    /** Properties file for login server configurations */
    public static final String LOGIN_CONFIGURATION_FILE = "./config/loginserver.properties";
    /** Properties file for the ID factory */
    public static final String ID_CONFIG_FILE = "./config/idfactory.properties";
    /** Properties file for other configurations */
    public static final String OTHER_CONFIG_FILE = "./config/other.properties";
    /** Properties file for rates configurations */
    public static final String RATES_CONFIG_FILE = "./config/rates.properties";
    /** Properties file for alternative configuration */
    public static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";
    /** Properties file for PVP configurations */
    public static final String PVP_CONFIG_FILE = "./config/pvp.properties";
    /** Properties file for GM access configurations */
    public static final String GM_ACCESS_FILE = "./config/GMAccess.properties";
    /** Properties file for telnet configuration */
    public static final String TELNET_FILE = "./config/telnet.properties";
    /** Properties file for l2j server version configurations */
    public static final String SERVER_VERSION_FILE = "./config/l2j-version.properties";
    /** Properties file for l2j datapack version configurations */
    public static final String DATAPACK_VERSION_FILE = "./config/l2jdp-version.properties";
    /** Properties file for siege configuration */
    public static final String SIEGE_CONFIGURATION_FILE = "./config/siege.properties";
    /** XML file for banned IP */
    public static final String BANNED_IP_XML = "./config/banned.xml";
    /** Text file containing hexadecimal value of server ID */
    public static final String HEXID_FILE = "./config/hexid.txt";
    /**
     * Properties file for alternative configure GM commands access level.<br>
     * Note that this file only read if "AltPrivilegesAdmin = true"
     */
    public static final String COMMAND_PRIVILEGES_FILE = "./config/command-privileges.properties";
    /** Properties file for AI configurations */
    public static final String AI_FILE = "./config/ai.properties";
    /** Properties file for 7 Signs Festival */
    public static final String SEVENSIGNS_FILE = "./config/sevensigns.properties";
    public static final String CLANHALL_CONFIG_FILE = "./config/clanhall.properties";
    public static final String L2JMOD_CONFIG_FILE = "./config/l2jmods.properties";
    public static int MAX_ITEM_IN_PACKET;
    /** Properties file for irc configuration */
    public static final String IRC_FILE = "./config/irc.properties";
    /** L2J Oneo Custom Properties File */
    public static final String L2J_ONEO_CUSTOM = "./config/L2JOneoCustom.properties";
    /** L2J Oneo Event Mods Properties File */
    public static final String L2JONEO_MODS = "./config/L2JOneoMods.properties";
    /** Properties file FloodProtector Configuration */ 
    public static final String FLOODPROTECTOR_CONFIG_FILE =  "./config/FloodProtector.properties";   
    /** Properties file for Bosses */
    public static final String  BOS_CONFIG_FILE = "./config/boss/bosses.properties";
	public static final String FS_CONFIG_FILE = "./config/boss/foursepulchers.properties";
    /** Properties file for Feature Extensions*/
    public static final String  FEATURE_CONFIG_FILE = "./config/Custom/Feature.properties";
    /** Properties file for General Configurations */
    public static final String  GENERAL_CONFIG_FILE = "./config/Custom/General.properties";
    /** Properties file for Olympiad Extensions*/
    public static final String  OLYMPIAD_FILE = "./config/Custom/Olympiad.properties";
    
    /** Debug/release mode */
    public static boolean DEBUG;
    /** Enable/disable assertions */
    public static boolean ASSERT;
    /** Enable/disable code 'in progress' */
    public static boolean DEVELOPER;
    /** Set if this server is a test server used for development */
    public static boolean TEST_SERVER;
    /** Game Server ports */
    public static int PORT_GAME;
    /** Login Server port */
    public static int PORT_LOGIN;
    /** Login Server bind ip */
    public static String LOGIN_BIND_ADDRESS;
    /** Number of login tries before IP ban gets activated, default 10 */
    public static int LOGIN_TRY_BEFORE_BAN;
    /** Number of seconds the IP ban will last, default 10 minutes */
    public static int LOGIN_BLOCK_AFTER_BAN;
    /** Hostname of the Game Server */
    public static String GAMESERVER_HOSTNAME;
    // Access to database
    /** Driver to access to database */
    public static String DATABASE_DRIVER;
    /** Path to access to database */
    public static String DATABASE_URL;
    /** Database login */
    public static String DATABASE_LOGIN;
    /** Database password */
    public static String DATABASE_PASSWORD;
    /** Maximum number of Statements to the players */
    public static int DATABASE_MAXSTATEMENTS;
    /** Maximum number of connections to the database */
    public static int DATABASE_MAX_CONNECTIONS;
    /** Maximum PoolSize */
    public static int DATABASE_MIN_POOLSIZE;
    /** Minimum PoolSize */
    public static int DATABASE_MAX_POOLSIZE;
    /** If Pool Is Exhausted, Get 1 More Connections At A Time */
    public static int DATABASE_ACQUIREINCREMENT;
    /** Test Idle Connection Time */
    public static int DATABASE_IDLECONNECTIONTEST;
    /** Max Idle Time */
    public static int DATABASE_MAXIDLETIME;
    /** Maximum number of players allowed to play simultaneously on server */
    public static int MAXIMUM_ONLINE_USERS;
    // Setting for serverList
    /** Displays [] in front of server name ? */
    public static boolean SERVER_LIST_BRACKET;
    /** Displays a clock next to the server name ? */
    public static boolean SERVER_LIST_CLOCK;
    /** Display test server in the list of servers ? */
    public static boolean SERVER_LIST_TESTSERVER;
    /** Set the server as gm only at startup ? */
    public static boolean SERVER_GMONLY;
    // Thread pools size
    /** Thread pool size effect */
    public static int THREAD_P_EFFECTS;
    /** Thread pool size general */
    public static int THREAD_P_GENERAL;
    /** Packet max thread */
    public static int GENERAL_PACKET_THREAD_CORE_SIZE;
    public static int IO_PACKET_THREAD_CORE_SIZE;
    /** General max thread */
    public static int GENERAL_THREAD_CORE_SIZE;
    /** AI max thread */
    public static int AI_MAX_THREAD;
    /** Accept auto-loot ? */
    public static boolean AUTO_LOOT;
    public static boolean AUTO_LOOT_HERBS;
    /** Character name template */
    public static String CNAME_TEMPLATE;
    /** Pet name template */
    public static String PET_NAME_TEMPLATE;
    /** Maximum number of characters per account */
    public static int MAX_CHARACTERS_NUMBER_PER_ACCOUNT;
    /** Global chat state */
    public static String DEFAULT_GLOBAL_CHAT;
    /** Trade chat state */
    public static String DEFAULT_TRADE_CHAT;
    /** For test servers - everybody has admin rights */
    public static boolean EVERYBODY_HAS_ADMIN_RIGHTS;
    /** Alternative game crafting */
    public static boolean ALT_GAME_CREATION;
    /**
     * Alternative game crafting speed mutiplier - default 0 (fastest but
     * still not instant)
     */
    public static double ALT_GAME_CREATION_SPEED;
    /** Alternative game crafting XP rate multiplier - default 1 */
    public static double ALT_GAME_CREATION_XP_RATE;
    /** Alternative game crafting SP rate multiplier - default 1 */
    public static double ALT_GAME_CREATION_SP_RATE;
    /**
     * Alternative setting to blacksmith use of recipes to craft - default True
     */
    public static boolean ALT_BLACKSMITH_USE_RECIPES;
    /** Alternative game skill learning */
    public static boolean ALT_GAME_SKILL_LEARN;
    /** Alternative auto skill learning */
    public static boolean AUTO_LEARN_SKILLS;
    /** Cancel attack bow by hit */
    public static boolean ALT_GAME_CANCEL_BOW;
    /** Cancel cast by hit */
    public static boolean ALT_GAME_CANCEL_CAST;
    /** Soul Crystal chances */  
    public static int SOUL_CRYSTAL_BREAK_CHANCE;   
    public static int SOUL_CRYSTAL_LEVEL_CHANCE;   
    /** Alternative game - use tiredness, instead of CP */
    public static boolean ALT_GAME_TIREDNESS;
    public static int ALT_PARTY_RANGE;
    public static int ALT_PARTY_RANGE2;
    /** Alternative shield defence */
    public static boolean ALT_GAME_SHIELD_BLOCKS;
    /** Alternative game mob ATTACK AI */
    public static boolean ALT_GAME_MOB_ATTACK_AI;
    public static boolean ALT_MOB_AGRO_IN_PEACEZONE;
    /**
     * Alternative freight modes - Freights can be withdrawed from any village
     */
    public static boolean ALT_GAME_FREIGHTS;
    /**
     * Alternative freight modes - Sets the price value for each freightened item
     */
    public static int ALT_GAME_FREIGHT_PRICE;
    /** Alternative gameing - loss of XP on death */
    public static boolean ALT_GAME_DELEVEL;
    /** Alternative gameing - magic dmg failures */
    public static boolean ALT_GAME_MAGICFAILURES;
    /**
     * Alternative gaming - player must be in a castle-owning clan or ally to sign up for Dawn.
     */
    public static boolean ALT_GAME_REQUIRE_CASTLE_DAWN;
    /**
     * Alternative gaming - allow clan-based castle ownage check rather than ally-based.
     */
    public static boolean ALT_GAME_REQUIRE_CLAN_CASTLE;
    /** Alternative gaming - allow free teleporting around the world. */
    public static boolean ALT_GAME_FREE_TELEPORT;
    /** Disallow recommend character twice or more a day ? */
    public static boolean ALT_RECOMMEND;
    /**
     * Alternative gaming - allow sub-class addition without quest completion.
     */
    public static boolean ALT_GAME_SUBCLASS_WITHOUT_QUESTS;
    
    /** Restore effects on subclass change? */
    public static boolean RESTORE_EFFECTS_ON_SUBCLASS_CHANGE;
    /** Minimum number of player to participate in SevenSigns Festival */
    public static int ALT_FESTIVAL_MIN_PLAYER;
    /** Maximum of player contrib during Festival */
    public static int ALT_MAXIMUM_PLAYER_CONTRIB;
    /** Festival Manager start time. */
    public static long ALT_FESTIVAL_MANAGER_START;
    /** Festival Length */
    public static long ALT_FESTIVAL_LENGTH;
    /** Festival Cycle Length */
    public static long ALT_FESTIVAL_CYCLE_LENGTH;
    /** Festival First Spawn */
    public static long ALT_FESTIVAL_FIRST_SPAWN;
    /** Festival First Swarm */
    public static long ALT_FESTIVAL_FIRST_SWARM;
    /** Festival Second Spawn */
    public static long ALT_FESTIVAL_SECOND_SPAWN;
    /** Festival Second Swarm */
    public static long ALT_FESTIVAL_SECOND_SWARM;
    /** Festival Chest Spawn */
    public static long ALT_FESTIVAL_CHEST_SPAWN;
    /** Number of members needed to request a clan war */
    public static int ALT_CLAN_MEMBERS_FOR_WAR;
    /** Number of days before joining a new clan */
    public static int ALT_CLAN_JOIN_DAYS;
    /** Number of days before creating a new clan */
    public static int ALT_CLAN_CREATE_DAYS;
    /** Number of days it takes to dissolve a clan */
    public static int ALT_CLAN_DISSOLVE_DAYS;
    /**
     * Number of days before joining a new alliance when clan voluntarily leave an alliance
     */
    public static int ALT_ALLY_JOIN_DAYS_WHEN_LEAVED;
    /**
     * Number of days before joining a new alliance when clan was dismissed from an alliance
     */
    public static int ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED;
    /**
     * Number of days before accepting a new clan for alliance when clan was dismissed from an alliance
     */
    public static int ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED;
    /**
     * Number of days before creating a new alliance when dissolved an alliance
     */
    public static int ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED;
    /** Alternative gaming - all new characters always are newbies. */
    public static boolean ALT_GAME_NEW_CHAR_ALWAYS_IS_NEWBIE;
    /**
     * Alternative gaming - clan members with see privilege can also withdraw from clan warehouse.
     */
    public static boolean ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH;
    /** Maximum number of clans in ally */
    public static int ALT_MAX_NUM_OF_CLANS_IN_ALLY;
    /** Logging Chat Window */
    public static boolean LOG_CHAT;
    /** Logging Item Window */
    public static boolean LOG_ITEMS;
    /** Alternative privileges for admin */
    public static boolean ALT_PRIVILEGES_ADMIN;
    /** Alternative secure check privileges */
    public static boolean ALT_PRIVILEGES_SECURE_CHECK;
    /** Alternative default level for privileges */
    public static int ALT_PRIVILEGES_DEFAULT_LEVEL;
    /** Olympiad Compitition Starting time */
    public static int ALT_OLY_START_TIME;
    /** Olympiad Compition Min */
    public static int ALT_OLY_MIN;
    /** Olympaid Comptetition Period */
    public static long ALT_OLY_CPERIOD;
    /** Olympiad Battle Period */
    public static long ALT_OLY_BATTLE;
    /** Olympiad Battle Wait */
    public static long ALT_OLY_BWAIT;
    /** Olympiad Inital Wait */
    public static long ALT_OLY_IWAIT;
    /** Olympaid Weekly Period */
    public static long ALT_OLY_WPERIOD;
    /** Olympaid Validation Period */
    public static long ALT_OLY_VPERIOD;
    /** Initial Lottery prize */
    public static int ALT_LOTTERY_PRIZE;
    /** Lottery Ticket Price */
    public static int ALT_LOTTERY_TICKET_PRICE;
    /**
     * What part of jackpot amount should receive characters who pick 5 wining numbers
     */
    public static float ALT_LOTTERY_5_NUMBER_RATE;
    /**
     * What part of jackpot amount should receive characters who pick 4 wining numbers
     */
    public static float ALT_LOTTERY_4_NUMBER_RATE;
    /**
     * What part of jackpot amount should receive characters who pick 3 wining numbers
     */
    public static float ALT_LOTTERY_3_NUMBER_RATE;
    /**
     * How much adena receive characters who pick two or less of the winning number
     */
    public static int ALT_LOTTERY_2_AND_1_NUMBER_PRIZE;
    /** ************************************************** */
    /** L2J Oneo Mods Customizations -Begin * */
    /** ************************************************** */
    // * Event Engine Automation *//
    /** Time in between Automated event cycling */
    public static int TIME_BETWEEN_EVENTS;
    /**
     * Time interval to check and see if L2JOneoEvent Manager should start a cycle of events if non in progress, or wait
     */
    // * TvT Event Engine */
    /** Enable or Disable TvT Engine with this option */
    public static boolean TVT_EVENT_ENABLED;
    /** This is the event participation time. */
    public static int TVT_EVENT_PARTICIPATION_TIME;
    /** This is the event running time after participation. */
    public static int TVT_EVENT_RUNNING_TIME;
    /** This is the events participation NPC Id. */
    public static int TVT_EVENT_PARTICIPATION_NPC_ID;
    /** This is the coordinates for the event participation NPC. */
    public static int[] TVT_EVENT_PARTICIPATION_NPC_COORDINATES = new int[3];
    /** Minimum participants on both sides. */
    public static int TVT_EVENT_MIN_PLAYERS_IN_TEAMS;
    /** Maximum participants on both sides. */
    public static int TVT_EVENT_MAX_PLAYERS_IN_TEAMS;
    /** Delay before a player respawns at start point after death in event. */
    public static int TVT_EVENT_RESPAWN_TELEPORT_DELAY;
    /** TvT Event Start/Leave Teleport Delay. */
    public static int TVT_EVENT_START_LEAVE_TELEPORT_DELAY;
    /** TvT Team 1 name. */
    public static String TVT_EVENT_TEAM_1_NAME;
    /** TvT Team 1 coordinates. */
    public static int[] TVT_EVENT_TEAM_1_COORDINATES = new int[3];
    /** TvT Team 2 name. */
    public static String TVT_EVENT_TEAM_2_NAME;
    /** TvT Team 2 coordinates. */
    public static int[] TVT_EVENT_TEAM_2_COORDINATES = new int[3];
    /** TvT Event Reward ID and Reward count. */
    public static List<int[]> TVT_EVENT_REWARDS = new FastList<int[]>();
    /** TvT Allow targeting team members. */
    public static boolean TVT_EVENT_TARGET_TEAM_MEMBERS_ALLOWED;
    /** This checks if players can use Potions during TvT event or not. */
    public static boolean TVT_EVENT_POTIONS_ALLOWED;
    /** Specific Potions enable/disable during Tvt */
    public static boolean TVT_EVENT_POTIONS_MP_ALLOWED;
    public static boolean TVT_EVENT_POTIONS_HP_ALLOWED;
    public static boolean TVT_EVENT_POTIONS_CP_ALLOWED;
    /**
     * This checks if players can use Summon by Item during TvT event or not.
     */
    public static boolean TVT_EVENT_SUMMON_BY_ITEM_ALLOWED;
    /** This option will close/open event doors during the TvT Event. */
    public static List<Integer> TVT_EVENT_DOOR_IDS = new FastList<Integer>();
    /**
     * This option will check what the administrator sets for a minimum participation level.
     */
    public static byte TVT_EVENT_MIN_LVL;
    /**
     * This option will check what the administrator sets for a maximum participation level.
     */
    public static byte TVT_EVENT_MAX_LVL;
    // * VIP Event Engine *//
    /** Enable or Disable VIP Engine with this option. */
    public static boolean VIP_EVENT_ENABLED;
    /** Force Players to sit during */
    public static boolean VIP_FORCE_SIT;
    /** Minimum VIP Players */
    public static int MIN_VIP_PLAYERS;
    /** Minimum Non-VIP Players */
    public static int MIN_NON_VIP_PLAYERS;
    /**
     * Name of place where people will be teleported after event is over, ex: Giran
     */
    public static String VIP_ENDTP_DESC;
    /**
     * Reward for VIP Engine in format: Reward Item ID for VIP,Reward Item ID for VIP's Team;
     */
    public static List<int[]> VIP_REWARD = new FastList<int[]>();
    public static List<int[]> VIP_TEAM_REWARD = new FastList<int[]>();
    public static List<int[]> VIP_NON_REWARD = new FastList<int[]>();
    /**
     * Finish NPC ID, YOU MUST SET THIS TO AN EXISTING NPC (Built in NPC is 50054)
     */
    public static int VIP_FINISH_NPC_ID;
    /** Location where all participants are teleported after event is over. */
    public static int VIP_TP_LOC_X;
    public static int VIP_TP_LOC_Y;
    public static int VIP_TP_LOC_Z;
    /**
     * This is the time participation will last for, when participation is over event will start. (in minutes)
     */
    public static int VIP_PARTICIPATION_TO_START_DELAY;
    /**
     * This is the event lasting time after the Participation ends. (in minutes)
     */
    public static int VIP_LASTING_TIME;
    /** This is the time people are forced to sit. (in seconds) */
    public static int VIP_SIT_TIME;
    /**
     * This is the time before players teleported to finish location after event is over. (in seconds)
     */
    public static int VIP_TELEPORT_TIME;
    /**
     * This is the time before players teleported to start location. (in seconds)
     */
    public static int VIP_TIME_BEFORE_TELEPORT;
    /** This checks if players can use Potions during VIP event or not. */
    public static boolean VIP_EVENT_POTIONS_ALLOWED;
    /** This checks if players can summon by item during VIP event or not. */
    public static boolean VIP_EVENT_SUMMON_BY_ITEM_ALLOWED;
    // * CTF Event Engine *//
    /** Enable or Disable CTF Engine with this option. */
    public static boolean CTF_EVENT_ENABLED;
    /** Allow/Disallow players not in CTF to interfere with CTF participants */
    public static boolean CTF_ALLOW_INTERFERENCE;
    /** This checks if player has the option to use potions or not. */
    public static boolean CTF_ALLOW_POTIONS;
    /** This checks if player has the option to Summon by item or not. */
    public static boolean CTF_ALLOW_SUMMON;
    /**
     * This checks if the administrator or Owner want all
     * buffs/debuffs/effects on a character removed on event start.
     */
    public static boolean CTF_ON_START_REMOVE_ALL_EFFECTS;
    /**
     * This checks if the administrator or Owner want characters pets unsummoned on event start.
     */
    public static boolean CTF_ON_START_UNSUMMON_PET;
    /** This is the Team name for Team 1 */
    public static String CTF_TEAM_NAME_1;
    /** This is the start co-ordinates of team 1 * */
    public static int[] CTF_TEAM_CORDS_1 = new int[3];
    /** This is the ID of the Flag for Team 1 */
    public static int CTF_TEAM_FLAG_ID_1;
    /** This is the Team Color for Team 1 */
    public static int CTF_TEAM_COLOR_1;
    /** This is the Team name for Team 2 */
    public static String CTF_TEAM_NAME_2;
    /** This is the start co-ordinates of team 2 * */
    public static int[] CTF_TEAM_CORDS_2 = new int[3];
    /** This is the ID of the Flag for Team 2 */
    public static int CTF_TEAM_FLAG_ID_2;
    /** This is the Team Color for Team 2 */
    public static int CTF_TEAM_COLOR_2;
    /** This is the Minimum amount of players on both sides of the CTF Event. */
    public static int CTF_MIN_PLAYERS;
    /** This is the Participation time before the event starts. */
    public static int CTF_PARTICIPATION_TIME;
    /** This is the delay before players are teleported to start locations. */
    public static int CTF_TELEPORT_DELAY_TIME;
    /** This is the delay after the teleport and before the event starts. */
    public static int CTF_DELAY_AFTER_TELEPORT;
    // * Raid Event Engine *//
    public static boolean RAID_SYSTEM_ENABLED;
    public static int RAID_SYSTEM_MAX_EVENTS;
    public static boolean RAID_SYSTEM_GIVE_BUFFS;
    public static boolean RAID_SYSTEM_RESURRECT_PLAYER;
    public static int RAID_SYSTEM_FIGHT_TIME;
    // * Wedding System *//
    /** Enable or Disable wedding system with this option. */
    public static boolean ALLOW_WEDDING;
    /** This is the cost to get married. */
    public static int WEDDING_PRICE;
    /** This checks if a player should get punished for infedelity. */
    public static boolean WEDDING_PUNISH_INFIDELITY;
    /** This checks if couples can be teleported. */
    public static boolean WEDDING_TELEPORT;
    /** This is the price to teleport. */
    public static int WEDDING_TELEPORT_PRICE;
    /** This is the time before teleporing takes place. */
    public static int WEDDING_TELEPORT_INTERVAL;
    /** This allows Homosexual marriage. */
    public static boolean WEDDING_SAMESEX;
    /** This checks if players are required to wear formal wear or not. */
    public static boolean WEDDING_FORMALWEAR;
    /** This checks the cost percentage to divorce. */
    public static int WEDDING_DIVORCE_COSTS;
    // * Champion Mobs *//
    /** Enable or Disable Champion Mobs with this option. */
    public static boolean CHAMPION_ENABLE;
    /** Frequency of a mob being a champion. */
    public static int CHAMPION_FREQUENCY;
    /** Minimum champion mob level. */
    public static int CHAMPION_MIN_LVL;
    /** Maximum champion mob level. */
    public static int CHAMPION_MAX_LVL;
    /** Champion Mob HP Multiplier. */
    public static int CHAMPION_HP;
    /** Champion Mob reward for kill. */
    public static int CHAMPION_REWARDS;
    /** Champion Mob adena reward. */
    public static int CHAMPION_ADENAS_REWARDS;
    /** Champion Mob HP Regen Multiplier. */
    public static float CHAMPION_HP_REGEN;
    /** Champion Mob P.Atk Multiplier. */
    public static float CHAMPION_ATK;
    /** Champion Mob P.Atk.Spd Multiplier. */
    public static float CHAMPION_SPD_ATK;
    /** Champion Mob reward. */
    public static int CHAMPION_REWARD;
    /** Champion Mob reward ID. */
    public static int CHAMPION_REWARD_ID;
    /** Champion Mob reward quantity. */
    public static int CHAMPION_REWARD_QTY;
    /** ************************************************** */
    /** L2J Oneo Event Mods Customizations -End * */
    /** ************************************************** */
    /** ************************************************** */
    /** L2J Oneo Customizations -Begin * */
    /** ************************************************** */
    // * Baby Pets Customizations *//
    /** Can Baby Pets Grow Up? * */
    public static boolean BABY_PETS_CAN_GROWUP;
    /** Can Wolf Pets Grow Up? * */
    public static boolean WOLF_PETS_CAN_GROWUP;
    /** Min Grow Up Pet Level? * */
    public static int BABY_PETS_GROWUP_LVL;
    // *Character Customizations*//
    /** Max Critical Rate. */
    public static int MAX_RCRIT;
    /** Maximum P. Attack Speed. */
    public static int MAX_PATK_SPEED;
    /** Maximum M. Attack Speed. */
    public static int MAX_MATK_SPEED;
    /** Config to keep skills of previous class after switching subclass. */
    public static boolean KEEP_SUBCLASS_SKILLS;
    /** Maximum Allowed subclasses for a player. */
    public static int MAX_SUBCLASSES;
    public static boolean NPC_ATTACKABLE;
    public static List INVUL_NPC_LIST;
    /** Low Level Protection System. */
    public static int PLAYER_PROTECTION_SYSTEM;
    /** Front Blow Success Rate. */
    public static int FRONT_BLOW_SUCCESS;
    /** Back Blow Success Rate. */
    public static int BACK_BLOW_SUCCESS;
    /** Side Blow Success Rate. */
    public static int SIDE_BLOW_SUCCESS;
    /** Config to disable or enable Grade Penalty. */
    public static boolean DISABLE_GRADE_PENALTIES;
    /** Config to disable or enable Weight Penalty. */
    public static boolean DISABLE_WEIGHT_PENALTIES;
    /**
     * Config to
     * @Checkup and delete delayed rented items.
     */
    public static boolean DONATOR_DELETE_RENTED_ITEMS;
    /** Config to choose donator Color on Enter. */
    public static int DONATOR_NAME_COLOR;
    /** Config to Enable Donator Items. */
    public static boolean DONATOR_ITEMS;
    /** Config to Enable Donator Auto revive. */
    public static boolean DONATORS_REVIVE;
    /** Config to Enable Custom Hero Item */
    public static boolean HERO_CUSTOM_ITEMS;
    /** Config to Enable Donators Pass Check Unlegit skills. */
    public static boolean ALLOW_DONATORS_UNLEGIT_SKILLS;
    /** Config to choose koofs Color on Enter. */
    public static int KOOFS_NAME_COLOR;
    /** Config to choose noobs Color on Enter. */
    public static int NOOBS_NAME_COLOR;
    /** Config to Enable koofs and noobs Faction by DaRkRaGe. */
    public static boolean ENABLE_FACTION_KOOFS_NOOBS;
    /** Config for Announce Faction Players */
    public static int FACTION_ANNOUNCE_TIME;
    /** Config to choose koofs Alternative Name. */
    public static String KOOFS_NAME_TEAM;
    /** Config to choose Noobs Alternative Name. */
    public static String NOOBS_NAME_TEAM;
    /**
     * Config option allowing Rebirth Engine.
     */
    public static int REBIRTH_ITEM;
    public static int REBIRTH_SKILL1;
    public static int REBIRTH_SKILL1_LVL;
    public static int REBIRTH_SKILL2;
    public static int REBIRTH_SKILL2_LVL;
    public static int REBIRTH_SKILL3;
    public static int REBIRTH_SKILL3_LVL;
    public static int REBIRTH_SKILL4;
    public static int REBIRTH_SKILL4_LVL;
    public static int REBIRTH_SKILL5;
    public static int REBIRTH_SKILL5_LVL;
    public static int REBIRTH_SKILL6;
    public static int REBIRTH_SKILL6_LVL;
    public static int REBIRTH_SKILL7;
    public static int REBIRTH_SKILL7_LVL;
    public static int REBIRTH_SKILL8;
    public static int REBIRTH_SKILL8_LVL;
    public static int REBIRTH_SKILL9;
    public static int REBIRTH_SKILL9_LVL;
    public static int REBIRTH_SKILL10;
    public static int REBIRTH_SKILL10_LVL;
    /**
     * Config option allowing server administrators/owners the ability to set a title for new players.
     */
    public static boolean CHAR_TITLE;
    /** This is the new players title. */
    public static String ADD_CHAR_TITLE;
    /** Adds the set number to a players maximum load capacity. */
    public static int ADD_MAX_LOAD;
    /** Configurable addition/subtraction to Running speed. */
    public static int CUSTOM_RUN_SPEED;
    /** Configurable for donators TO loose Buffs and effects on death */
    public static boolean KEEP_BUFFS_ON_DEATH;
    /** Death Penalty chance */
    public static int DEATH_PENALTY_CHANCE;
    /** Player can drop AA ? */
    public static boolean ALT_PLAYER_CAN_DROP_AA;
    /** Ammount of Ancient Adena can drop */
    public static int PLAYER_DROP_AA;
    /** Player can Get Adena from pvp ? */
    public static boolean ALLOW_ADENA_REWARD;
    public static int ADENA_NUMBER_REWARD_ON_PVP;
    /** Player can loose Adena on die by pvp ? */
    public static boolean LOOSE_ADENA_ON_DIE;
    public static int ADENA_NUMBER_LOST_ON_DIE;
    /** Player will be automatic 80 lvl on Join ? */
    public static boolean SET_LVL_ON_START;
    /** Configuration for an Admin to disallow subclassers to get max level 80 through the SetHighLevelOnStart */
    public static boolean HIGH_LEVEL_ON_START_FOR_SUBCLASS;
    /** Configuration to allow custom items to be given on character creation */
    public static boolean CUSTOM_STARTER_ITEMS_ENABLED;
    /** Configuration to disable official items given on character creation */
    public static boolean DISABLE_OFFICIAL_STARTER_ITEMS;
    /**
     * This allows the administrator to set up additional items for players to start off with, items are put in the format: 
     * id,count;id,count;id,count
     */
    public static List<int[]> CUSTOM_STARTER_ITEMS = new FastList<int[]>();
    // * NPC Customizations*//
    /** View NPC Information for non-GM Characters. */
    public static boolean GAME_VIEWNPC;
    /** View NPC Combat Information. */
    public static boolean GAME_VIEWNPC_COMBAT;
    /** View NPC Basic Information. */
    public static boolean GAME_VIEWNPC_BASIC;
    /** View NPC Drop Information. */
    public static boolean GAME_VIEWNPC_DROP;
    /** View NPC Quest Drop Information. */
    public static boolean GAME_VIEWNPC_QUESTDROP;
    /** Minimal time between animations of a MONSTER */
    public static int MIN_MONSTER_ANIMATION;
    /** Maximal time between animations of a MONSTER */
    public static int MAX_MONSTER_ANIMATION;
    
    // Limits 
    public static int   MAX_RUN_SPEED; 
    public static int   MAX_EVASION; 
    public static int   MAX_MCRIT_RATE;
    
    /**
     * Config option to either allow or disallow a player getting petrified by a raidboss, due to the 8+ level difference rule.
     */
    public static boolean RAID_FOSSILIZATION_PENALTY;
    /** Allow Manor system */
    public static boolean ALLOW_MANOR;
    /** Manor Refresh Starting time */
    public static int ALT_MANOR_REFRESH_TIME;
    /** Manor Refresh Min */
    public static int ALT_MANOR_REFRESH_MIN;
    /** Manor Next Period Approve Starting time */
    public static int ALT_MANOR_APPROVE_TIME;
    /** Manor Next Period Approve Min */
    public static int ALT_MANOR_APPROVE_MIN;
    /** Manor Maintenance Time */
    public static int ALT_MANOR_MAINTENANCE_PERIOD;
    /** Manor Save All Actions */
    public static boolean ALT_MANOR_SAVE_ALL_ACTIONS;
    /** Manor Save Period Rate */
    public static int ALT_MANOR_SAVE_PERIOD_RATE;
    /** Allow walker NPC ? */
    public static boolean ALLOW_NPC_WALKERS;
    // * Player Commands *//
    /**
     * Config allowing players to select auto-loot enabled or disabled for themselves.
     */
    public static boolean ALLOW_AUTOHERBS_CMD;
    /**
     * Allows clan leaders the power allow clan members withdraw items from clan warehouse.
     */
    public static boolean ALLOW_WITHDRAW_CWH_CMD;
    // * Announcements and Messages *//
    /* Show html window at login */
    public static boolean SHOW_HTML_WELCOME;
    /** Announcement of GM Login. */
    public static boolean SHOW_GM_LOGIN;
    /** Show L2J License at login */
    public static boolean SHOW_L2J_LICENSE;
    /** Show html window at login for newbies */
    public static boolean SHOW_HTML_NEWBIE;
    public static int LEVEL_HTML_NEWBIE;
    /** Show Online Players announcement */
    public static boolean ONLINE_PLAYERS_AT_STARTUP;
    /** Increases the # of players announcment by this set amount */
    public static int PLAYERS_ONLINE_TRICK;
    /** Interval at which the Online Player Announcement occurs */
    public static int ONLINE_PLAYERS_ANNOUNCE_INTERVAL;
    /** Announce Castle Lords on login */
    public static boolean ANNOUNCE_CASTLE_LORDS;
    /** Announce Pk players */
    public static boolean ALT_ANNOUNCE_PK;
    /** Enable Pk Info mod. Displays number of times player has killed other */
    public static boolean ENABLE_PK_INFO;
    /** NPC Announcer */
    public static int NPC_ANNOUNCER_PRICE_PER_ANNOUNCE;
    public static int NPC_ANNOUNCER_MAX_ANNOUNCES_PER_DAY;
    public static int NPC_ANNOUNCER_MIN_LVL_TO_ANNOUNCE;
    public static int NPC_ANNOUNCER_MAX_LVL_TO_ANNOUNCE;
    public static boolean ALLOW_NPC_ANNOUNCER;
    public static boolean NPC_ANNOUNCER_DONATOR_ONLY;
    // * Dimensional Drift *//
    /**
     * Minimum size of a party that may enter dimensional rift, if number becomes lower then defined number, the party will be teleported back.
     */
    public static int RIFT_MIN_PARTY_SIZE;
    /**
     * Maximum number of jumps between rooms allowed. After Maximum number, party will be teleported back.
     */
    public static int RIFT_MAX_JUMPS;
    /**
     * After entering the room, this is the wait time before mobs will spawn in the room. (in ms, 1 second = 1000 ms)
     */
    public static int RIFT_SPAWN_DELAY;
    /** Time between automatic jumps (in seconds). */
    public static int RIFT_AUTO_JUMPS_TIME_MIN;
    public static int RIFT_AUTO_JUMPS_TIME_MAX;
    /**
     * To enter the dimension rift, each person in your party must have the below amount of dimension fragments.
     */
    public static int RIFT_ENTER_COST_RECRUIT;
    public static int RIFT_ENTER_COST_SOLDIER;
    public static int RIFT_ENTER_COST_OFFICER;
    public static int RIFT_ENTER_COST_CAPTAIN;
    public static int RIFT_ENTER_COST_COMMANDER;
    public static int RIFT_ENTER_COST_HERO;
    // * Clan Customizations *//
    /** Customize the clan hall system as you like. */
    public static int CLAN_RAISE_FIRST_COST;
    public static int CLAN_RAISE_SEC_COST;
    public static int CLAN_MEMBERS_FIRST;
    public static int CLAN_MEMBERS_SEC;
    public static int CLAN_MEMBERS_THIRD;
    public static int CLAN_REPUTATION_FIRST;
    public static int CLAN_REPUTATION_SEC;
    public static int CLAN_REPUTATION_THIRD;
    public static int CLAN_SP_FIRST;
    public static int CLAN_SP_SEC;
    public static int CLAN_SP_THIRD;
    public static int CLAN_SP_FORTH;
    public static int CLAN_SP_FIFTH;
    // * Server Customizations *//
    /** Restart Login Server when Game Server restarts. */
    public static boolean LOGIN_RESTART_WITH_GAMESERVER;
    /** Restart Login Server on a scheduled time */
    public static boolean LOGIN_RESTART_BY_TIME;
    /** Restart time for Login Server */
    public static int LOGIN_RESTART_TIME;
    /**
     * Safe Sigterm will disable some features during restart/shutdown to prevent enchant and sublcass exploits! *
     */
    public static boolean SAFE_SIGTERM;
    /** GM Over Enchant value */
    public static int GM_OVER_ENCHANT;
    /** Amount of Ancient Adena when starting a new character */
    public static int STARTING_AA;
    /** This is the configuration for the Chat Filter */
    public static boolean USE_CHAT_FILTER;
    public static boolean USE_POWERFULL_CHAT_FILTER;
    public static String CHAT_FILTER_CHARS;
    public static int CHAT_FILTER_PUNISHMENT;
    public static int CHAT_FILTER_PUNISHMENT_TIME;
    /** Check players for illegitimate skills on player entering the server. */
    public static boolean CHECK_SKILLS_ON_ENTER;
    /**
     * Allows the Administrator/Owner the ability to change the default coordinates of ALL characters making them all at the same spawn point.
     */
    public static boolean SPAWN_CHAR;
    /** X Coordinate of the SPAWN_CHAR setting. */
    public static int SPAWN_X;
    /** Y Coordinate of the SPAWN_CHAR setting. */
    public static int SPAWN_Y;
    /** Z Coordinate of the SPAWN_CHAR setting. */
    public static int SPAWN_Z;
    /** Chance that a Crystal Scroll will have to enchant over safe limit */
    public static int ENCHANT_CHANCE_WEAPON_CRYSTAL;
    /** Chance that a Crystal Scroll will have to enchant over safe limit */
    public static int ENCHANT_CHANCE_ARMOR_CRYSTAL;
    /** Chance that a Crystal Scroll will have to enchant over safe limit */
    public static int ENCHANT_CHANCE_JEWELRY_CRYSTAL;
    /** Chance that a Blessed Scroll will have to enchant over safe limit */
    public static int ENCHANT_CHANCE_WEAPON_BLESSED;
    /** Chance that a Blessed Scroll will have to enchant over safe limit */
    public static int ENCHANT_CHANCE_ARMOR_BLESSED;
    /** Chance that a Blessed Scroll will have to enchant over safe limit */
    public static int ENCHANT_CHANCE_JEWELRY_BLESSED;
    /** Dwarfs Enchant System */
    public static boolean ENABLE_DWARF_ENCHANT_BONUS;
    public static int DWARF_ENCHANT_MIN_LEVEL;
    public static int DWARF_ENCHANT_BONUS;
    /** Change the way admin panel is shown */
    public static String GM_ADMIN_MENU_STYLE;
    /** Unknown Packet handler protection */
    public static boolean ENABLE_PACKET_PROTECTION;
    public static int MAX_UNKNOWN_PACKETS;
    public static int UNKNOWN_PACKETS_PUNiSHMENT;
    /** GM Audit ? */
    public static boolean GMAUDIT;
    // * Miscellaneous Customizations *//
    /** Config to enable or disable strict enforcement of the Hero System. */
    public static boolean STRICT_HERO_SYSTEM;
    /** Enchant hero weapons? */
    public static boolean ENCHANT_HERO_WEAPONS;
    /** Allow subclass with only subclass items and no quest. */
    public static boolean SUBCLASS_WITH_ITEM_AND_NO_QUEST;
    /**
     * Config option which allows or dis-allows use of Wyverns during Sieges.
     */
    public static boolean FLYING_WYVERN_DURING_SIEGE;
    /**
     * Config option to require or not require Skill Book for enchanting a skill.
     */
    public static boolean ES_SP_BOOK_NEEDED;
    /** Life Crystal needed to learn clan skill */
    public static boolean LIFE_CRYSTAL_NEEDED;
    /** Config for reuse delay of potions (in seconds). */
    public static int POTIONS_REUSE_DELAY;
    /** Config for reuse delay of potions Elixirs (in seconds). */
    public static int ELIXIRS_REUSE_DELAY;
    /** Remove Castle circlets after clan lose his castle? - default True */
    public static boolean REMOVE_CASTLE_CIRCLETS;
    /** Warehouse Sorting */
    public static boolean ENABLE_WAREHOUSESORTING_CLAN;
    public static boolean ENABLE_WAREHOUSESORTING_PRIVATE;
    public static boolean ENABLE_WAREHOUSESORTING_FREIGHT;
    /** Allow Summon Pet in Combat ? */
    public static boolean DISABLE_SUMMON_IN_COMBAT;
    /** Config for activeChar Attack Npcs in the list */
    public static boolean DISABLE_ATTACK_NPC_TYPE;
    /** Alternative Perfect shield defence rate */
    public static int ALT_PERFECT_SHLD_BLOCK;
    /**
     * Allows or dis-allows the option for NPC types that won't allow casting
     */
    public static String ALLOWED_NPC_TYPES;
    /** List of NPC types that won't allow casting */
    public static FastList<String> LIST_ALLOWED_NPC_TYPES = new FastList<String>();
    /** Enable Custom Spawlist table */
    public static boolean CUSTOM_SPAWNLIST_TABLE;
    /** Save GM Spawn only on custom table */
    public static boolean SAVE_GMSPAWN_ON_CUSTOM;
    /** Enable GM Delete spawn in alternate table */
    public static boolean DELETE_GMSPAWN_ON_CUSTOM;
    /** Enable Custom Npc table */
    public static boolean CUSTOM_NPC_TABLE;
    /** Enable Custom Items tables */
    public static boolean CUSTOM_ETCITEM_TABLE;
    public static boolean CUSTOM_ARMOR_TABLE;
    public static boolean CUSTOM_ARMORSETS_TABLE;
    public static boolean CUSTOM_WEAPON_TABLE;
    public static boolean CUSTOM_TELEPORT_TABLE;
    /** Enable Custom Droplist Table */
    public static boolean CUSTOM_DROPLIST_TABLE;
    /** Enable Custom Merchant Tables */
    public static boolean CUSTOM_MERCHANT_TABLES;
    /** Enable the check for Olympiads max enchant weapons */
    public static boolean ENABLE_OLY_WEAPON_ENCH;
    public static int MAX_OLY_WEAPON_ENCH;
    /** Disable Dual Boxing on Olympiads */
    public static boolean DISABLE_OLY_DUALBOX;
    public static boolean OLYMPIAD_ALLOW_AUTO_SS;
    public static boolean OLYMPIAD_GIVE_ACUMEN_MAGES;
    public static boolean OLYMPIAD_GIVE_HASTE_FIGHTERS;
    public static int OLYMPIAD_ACUMEN_LVL;
    public static int OLYMPIAD_HASTE_LVL;
    /** Enable skill duration mod */
    public static boolean ENABLE_MODIFY_SKILL_DURATION;
    public static Map<Integer, Integer> SKILL_DURATION_LIST;
    public static Integer MODIFIED_SKILL_COUNT;
    /** Enable skill NO autolearn */
    public static boolean ENABLE_NO_AUTOLEARN_LIST;
    /** List of Skills not autolearned */
    public static FastList<Integer> NO_AUTOLEARN_LIST;
    /**
     * Alternative gaming - Castle Shield can be equiped by all clan members if they own a castle. - default True
     */
    public static boolean CASTLE_SHIELD;
    /**
     * Alternative gaming - Clan Hall Shield can be equiped by all clan members if they own a clan hall. - default True
     */
    public static boolean CLANHALL_SHIELD;
    /**
     * Alternative gaming - Apella armors can be equiped only by clan members if their class is Baron or higher - default True
     */
    public static boolean APELLA_ARMORS;
    /**
     * Alternative gaming - Clan Oath Armors can be equiped only by clan members - default True
     */
    public static boolean OATH_ARMORS;
    /**
     * Alternative gaming - Castle Crown can be equiped only by castle lord - default True
     */
    public static boolean CASTLE_CROWN;
    /**
     * Alternative gaming - Castle Circlets can be equiped only by clan members if they own a castle - default True
     */
    public static boolean CASTLE_CIRCLETS;
    // * Banking System *//
    /** Banking System Enabled */
    public static boolean BANKING_SYSTEM_ENABLED;
    /**
     * Configure the amount of Adena to trade for the below configured number of Gold Bars.
     */
    public static int BANKING_SYSTEM_ADENA;
    /**
     * Configure the amount of Gold Bars to trade for the above configured number of Adena.
     */
    public static int BANKING_SYSTEM_GOLDBARS;
    /** ************************************************** */
    /** L2J Oneo Customizations -End * */
	/** ************************************************** */

	/***************************************************************************
	 * Valakas Custom CONFIG
	 **************************************************************************/
	public static int FWV_FIXINTERVALOFVALAKAS;
	public static int FWV_RANDOMINTERVALOFVALAKAS;
	public static int FWV_APPTIMEOFVALAKAS;
	public static int FWV_ACTIVITYTIMEOFVALAKAS;
	public static int FWV_CAPACITYOFLAIR;
	public static boolean FWV_MOVEATRANDOM;
    /****************************************** 
	/** Castle Settings -Begin                           **/
	/** ************************************************** **/

    public static long 		CS_TELE_FEE_RATIO;
    public static int 		CS_TELE1_FEE;
    public static int 		CS_TELE2_FEE;
    public static long 		CS_MPREG_FEE_RATIO;
    public static int 		CS_MPREG1_FEE;
    public static int 		CS_MPREG2_FEE;
    public static int 		CS_MPREG3_FEE;
    public static int 		CS_MPREG4_FEE;
    public static long 		CS_HPREG_FEE_RATIO;
    public static int 		CS_HPREG1_FEE;
    public static int 		CS_HPREG2_FEE;
    public static int 		CS_HPREG3_FEE;
    public static int 		CS_HPREG4_FEE;
    public static int 		CS_HPREG5_FEE;
    public static long 		CS_EXPREG_FEE_RATIO;
    public static int 		CS_EXPREG1_FEE;
    public static int 		CS_EXPREG2_FEE;
    public static int 		CS_EXPREG3_FEE;
    public static int 		CS_EXPREG4_FEE;
    public static long 		CS_SUPPORT_FEE_RATIO;
    public static int 		CS_SUPPORT1_FEE;
    public static int 		CS_SUPPORT2_FEE;
    public static int 		CS_SUPPORT3_FEE;
    public static int 		CS_SUPPORT4_FEE;
    public static List<String> CL_SET_SIEGE_TIME_LIST;
    public static List<Integer> SIEGE_HOUR_LIST_MORNING;
    public static List<Integer> SIEGE_HOUR_LIST_AFTERNOON;
    
    /** ************************************************** **/
	/** Castle Settings -End                               **/
 	/** ************************************************** **/
    
    /** ************************************************** **/
	/** Fortress Settings -Begin                           **/
	/** ************************************************** **/
 
    public static long 		FS_TELE_FEE_RATIO;
    public static int 		FS_TELE1_FEE;
    public static int 		FS_TELE2_FEE;
    public static long 		FS_MPREG_FEE_RATIO;
    public static int 		FS_MPREG1_FEE;
    public static int 		FS_MPREG2_FEE;
    public static long 		FS_HPREG_FEE_RATIO;
    public static int 		FS_HPREG1_FEE;
    public static int 		FS_HPREG2_FEE;
    public static long 		FS_EXPREG_FEE_RATIO;
    public static int 		FS_EXPREG1_FEE;
    public static int 		FS_EXPREG2_FEE;
    public static long 		FS_SUPPORT_FEE_RATIO;
    public static int 		FS_SUPPORT1_FEE;
    public static int 		FS_SUPPORT2_FEE;
    
     /** ************************************************** **/
	/** Fortress Settings -End                             **/
	/** ************************************************** **/
    
    /** ************************************************** **/
	/** Feature Settings -Begin                            **/
	/** ************************************************** **/
    
    // TODO: Fill this area up!
    
    /** ************************************************** **/
	/** Feature Settings -End                              **/
	/** ************************************************** **/
    
    //FloodProtector.properties Configs 
    public static int USEITEM_DELAY; 
    public static int ROLLDICE_DELAY; 
    public static int FIREWORK_DELAY; 
    public static int ITEMPETSUMMON_DELAY; 
    public static int HEROVOICE_DELAY; 
    public static int UNKNOWN_PACKET_DELAY; 
    public static int SUBCLASS_DELAY;
    public static int MULTISELL_DELAY; 
    
    /** ************************************************** **/
	/** Boss Settings -Begin                            **/
	/** ************************************************** **/
    /** Multiplies stay time in boss room. */
    public static float RIFT_BOSS_ROOM_TIME_MUTIPLY;
    
    /** multiples damages Char's / Pet's / Npc's */ 
    public static float ALT_MAGES_PHYSICAL_DAMAGE_MULTI; 
    public static float ALT_MAGES_MAGICAL_DAMAGE_MULTI; 
    public static float ALT_FIGHTERS_PHYSICAL_DAMAGE_MULTI; 
    public static float ALT_FIGHTERS_MAGICAL_DAMAGE_MULTI; 
    public static float ALT_PETS_PHYSICAL_DAMAGE_MULTI; 
 	public static float ALT_PETS_MAGICAL_DAMAGE_MULTI; 
 	public static float ALT_NPC_PHYSICAL_DAMAGE_MULTI; 
 	public static float ALT_NPC_MAGICAL_DAMAGE_MULTI; 
 	
	/** Time limit of invade to lair of bosses after server restarted */
	public static int TIMELIMITOFINVADE;
	public static int TIME_IN_A_DAY_OF_OPEN_A_DOOR;
	public static int TIME_OF_OPENING_A_DOOR;
	/** ************************************************** **/
	/** Boss Settings -End                              **/
	/** ************************************************** **/
    
    /***************************************************************************
	 * sepulche Custom CONFIG
	 **************************************************************************/
	public static int		FS_TIME_ATTACK;
	public static int		FS_TIME_COOLDOWN;
	public static int		FS_TIME_ENTRY;
	public static int		FS_TIME_WARMUP;
	public static int		FS_PARTY_MEMBER_COUNT;
    
    /******************************************* 
    /** ************************************************** **/
	/** General Settings -Begin                            **/
	/** ************************************************** **/
    /** Config for Fake Death Faild Feature **/
    public static boolean   FAILD_FAKEDEATH;
    public static boolean 	NPCBUFFER_FEATURE_ENABLED;
    public static int		NPCBUFFER_MAX_SCHEMES;
    public static int		NPCBUFFER_MAX_SKILLS;
    public static boolean	NPCBUFFER_STORE_SCHEMES;
    public static int		NPCBUFFER_STATIC_BUFF_COST;
	
	/** ************************************************** **/
	/** General Settings -End                              **/
	/** ************************************************** **/
    
    /** ************************************************** **/
	/** Olympiad Settings -Begin                            **/
	/** ************************************************** **/
    
    // TODO: Fill this area up!
    
    /** ************************************************** **/
	/** Olympiad Settings -End                              **/
	/** ************************************************** **/
    /***************************************************************************
	 * GM CONFIG General GM AccessLevel *
     **********************************************************************/
    /** General GM access level */
    public static int GM_ACCESSLEVEL;
    /** General GM Minimal AccessLevel */
    public static int GM_MIN;
    /** Minimum privileges level for a GM to do Alt+G */
    public static int GM_ALTG_MIN_LEVEL;
    /** General GM AccessLevel to change announcements */
    public static int GM_ANNOUNCE;
    /** General GM AccessLevel can /ban /unban */
    public static int GM_BAN;
    /** General GM AccessLevel can /ban /unban for chat */
    public static int GM_BAN_CHAT;
    /** General GM AccessLevel can /create_item and /gmshop */
    public static int GM_CREATE_ITEM;
    /** General GM AccessLevel can /delete */
    public static int GM_DELETE;
    /** General GM AccessLevel can /kick /disconnect */
    public static int GM_KICK;
    /** General GM AccessLevel for access to GMMenu */
    public static int GM_MENU;
    /** General GM AccessLevel to use god mode command */
    public static int GM_GODMODE;
    /** General GM AccessLevel with character edit rights */
    public static int GM_CHAR_EDIT;
    /** General GM AccessLevel with edit rights for other characters */
    public static int GM_CHAR_EDIT_OTHER;
    /** General GM AccessLevel with character view rights */
    public static int GM_CHAR_VIEW;
    /** General GM AccessLevel with NPC edit rights */
    public static int GM_NPC_EDIT;
    public static int GM_NPC_VIEW;
    /** General GM AccessLevel with NPC edit rights */
    public static int GM_PRIV_EDIT;
    public static int GM_PRIV_VIEW;
    /** General GM AccessLevel to teleport to any location */
    public static int GM_TELEPORT;
    /** General GM AccessLevel to teleport character to any location */
    public static int GM_TELEPORT_OTHER;
    /** General GM AccessLevel to restart server */
    public static int GM_RESTART;
    /** General GM AccessLevel for MonsterRace */
    public static int GM_MONSTERRACE;
    /** General GM AccessLevel to ride Wyvern */
    public static int GM_RIDER;
    /** General GM AccessLevel to unstuck without 5min delay */
    public static int GM_ESCAPE;
    /** General GM AccessLevel to resurect fixed after death */
    public static int GM_FIXED;
    /** General GM AccessLevel to create Path Nodes */
    public static int GM_CREATE_NODES;
    /** General GM AccessLevel with Enchant rights */
    public static int GM_ENCHANT;
    /** General GM AccessLevel to close/open Doors */
    public static int GM_DOOR;
    /** General GM AccessLevel with Resurrection rights */
    public static int GM_RES;
    /** General GM AccessLevel to attack in the peace zone */
    public static int GM_PEACEATTACK;
    /** General GM AccessLevel to heal */
    public static int GM_HEAL;
    /** General GM AccessLevel to unblock IPs detected as hack IPs */
    public static int GM_UNBLOCK;
    /** General GM AccessLevel to use Cache commands */
    public static int GM_CACHE;
    /** General GM AccessLevel to use test&st commands */
    public static int GM_TALK_BLOCK;
    public static int GM_TEST;
    public static int GM_FORTSIEGE;
    /** Minimum level to Manage Clan Panel */
    public static int GM_CLAN_PANEL;
    /** Disable transaction on AccessLevel * */
    public static boolean GM_DISABLE_TRANSACTION;
    /** GM transactions disabled from this range */
    public static int GM_TRANSACTION_MIN;
    /** GM transactions disabled to this range */
    public static int GM_TRANSACTION_MAX;
    /** Minimum level to allow a GM giving damage */
    public static int GM_CAN_GIVE_DAMAGE;
    /** Minimum level to don't give Exp/Sp in party */
    public static int GM_DONT_TAKE_EXPSP;
    /** Minimum level to don't take aggro */
    public static int GM_DONT_TAKE_AGGRO;
    public static int GM_REPAIR = 75;
    /* Rate control */
    /** Rate for eXperience Point rewards */
    public static float RATE_XP;
    /** Rate for Skill Point rewards */
    public static float RATE_SP;
    /** Rate for party eXperience Point rewards */
    public static float RATE_PARTY_XP;
    /** Rate for party Skill Point rewards */
    public static float RATE_PARTY_SP;
    /** Rate for Quest rewards (XP and SP) */
    public static float RATE_QUESTS_REWARD;
    /** Rate for drop adena */
    public static float RATE_DROP_ADENA;
    /** Rate for cost of consumable */
    public static float RATE_CONSUMABLE_COST;
    /** Rate for dropped items */
    public static float RATE_DROP_ITEMS;
    /** Rate for spoiled items */
    public static float RATE_DROP_SPOIL;
    /** Rate for manored items */
    public static int RATE_DROP_MANOR;
    /** Raids Config */
    // ADENA BOSS
    public static float ADENA_BOSS;
    public static float ADENA_RAID;
    public static float ADENA_MINON;
    // ITEMS BOSS
    public static float ITEMS_BOSS;
    public static float ITEMS_RAID;
    public static float ITEMS_MINON;
    // SPOIL BOSS
    public static float SPOIL_BOSS;
    public static float SPOIL_RAID;
    public static float SPOIL_MINON;
    /** Rate for quest items */
    public static float RATE_DROP_QUEST;
    /** Rate for karma and experience lose */
    public static float RATE_KARMA_EXP_LOST;
    /** Rate siege guards prices */
    public static float RATE_SIEGE_GUARDS_PRICE;
    /*
     * Alternative Xp/Sp rewards, if not 0, then calculated as 2^((mob.level-player.level) / coef), A few examples for
     * "AltGameExponentXp = 5." and "AltGameExponentSp = 3." diff = 0 (player and mob has the same level), XP bonus rate = 1, SP bonus rate =
     * 1 diff = 3 (mob is 3 levels above), XP bonus rate = 1.52, SP bonus rate = 2 diff = 5 (mob is 5 levels above), XP bonus rate = 2, SP
     * bonus rate = 3.17 diff = -8 (mob is 8 levels below), XP bonus rate = 0.4, SP bonus rate = 0.16
     */
    /** Alternative eXperience Point rewards */
    public static float ALT_GAME_EXPONENT_XP;
    /** Alternative Spirit Point rewards */
    public static float ALT_GAME_EXPONENT_SP;
    /** Rate Common herbs */
    public static float RATE_DROP_COMMON_HERBS;
    /** Rate MP/HP herbs */
    public static float RATE_DROP_MP_HP_HERBS;
    /** Rate Common herbs */
    public static float RATE_DROP_GREATER_HERBS;
    /** Rate Common herbs */
    public static float RATE_DROP_SUPERIOR_HERBS;
    /** Rate Common herbs */
    public static float RATE_DROP_SPECIAL_HERBS;
    // Player Drop Rate control
    /** Limit for player drop */
    public static int PLAYER_DROP_LIMIT;
    /** Rate for drop */
    public static int PLAYER_RATE_DROP;
    /** Rate for player's item drop */
    public static int PLAYER_RATE_DROP_ITEM;
    /** Rate for player's equipment drop */
    public static int PLAYER_RATE_DROP_EQUIP;
    /** Rate for player's equipment and weapon drop */
    public static int PLAYER_RATE_DROP_EQUIP_WEAPON;
    // Pet Rates (Multipliers)
    /** Rate for experience rewards of the pet */
    public static float PET_XP_RATE;
    /** Rate for food consumption of the pet */
    public static int PET_FOOD_RATE;
    /** Rate for experience rewards of the Sin Eater */
    public static float SINEATER_XP_RATE;
    // Karma Drop Rate control
    /** Karma drop limit */
    public static int KARMA_DROP_LIMIT;
    /** Karma drop rate */
    public static int KARMA_RATE_DROP;
    /** Karma drop rate for item */
    public static int KARMA_RATE_DROP_ITEM;
    /** Karma drop rate for equipment */
    public static int KARMA_RATE_DROP_EQUIP;
    /** Karma drop rate for equipment and weapon */
    public static int KARMA_RATE_DROP_EQUIP_WEAPON;
    /** Time after which item will auto-destroy */
    public static int AUTODESTROY_ITEM_AFTER;
    /** Auto destroy herb time */
    public static int HERB_AUTO_DESTROY_TIME;
    /** List of items that will not be destroyed (seperated by ",") */
    public static String PROTECTED_ITEMS;
    /** List of items that will not be destroyed */
    public static List<Integer> LIST_PROTECTED_ITEMS = new FastList<Integer>();
    /** Auto destroy nonequipable items dropped by players */
    public static boolean DESTROY_DROPPED_PLAYER_ITEM;
    /** Auto destroy equipable items dropped by players */
    public static boolean DESTROY_EQUIPABLE_PLAYER_ITEM;
    /** Save items on ground for restoration on server restart */
    public static boolean SAVE_DROPPED_ITEM;
    /** Empty table ItemsOnGround after load all items */
    public static boolean EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD;
    /** Time interval to save into db items on ground */
    public static int SAVE_DROPPED_ITEM_INTERVAL;
    /** Clear all items stored in ItemsOnGround table */
    public static boolean CLEAR_DROPPED_ITEM_TABLE;
    /** Accept precise drop calculation ? */
    public static boolean PRECISE_DROP_CALCULATION;
    /** Accept multi-items drop ? */
    public static boolean MULTIPLE_ITEM_DROP;
    /**
     * This is setting of experimental Client <--> Server Player coordinates synchronization<br>
     * <b><u>Valeurs :</u></b>
     * <li>0 - no synchronization at all</li>
     * <li>1 - parcial synchronization Client --> Server only * using this option it is difficult for players to bypass obstacles</li>
     * <li>2 - parcial synchronization Server --> Client only</li>
     * <li>3 - full synchronization Client <--> Server</li>
     * <li>-1 - Old system: will synchronize Z only</li>
     */
    public static int COORD_SYNCHRONIZE;
    /** Period in days after which character is deleted */
    public static int DELETE_DAYS;
    /** Datapack root directory */
    public static File DATAPACK_ROOT;
    /** Maximum range mobs can randomly go from spawn point */
    public static int MAX_DRIFT_RANGE;
    /** Allow fishing ? */
    public static boolean ALLOWFISHING;
    /** Jail config * */
    public static boolean JAIL_IS_PVP;
    public static boolean JAIL_DISABLE_CHAT;
    /*
     * Removed by Ahmed, Reason: This has no effect at all whether players can use L2Walker or not.
     */
    /**
     * Enumeration describing values for Allowing the use of L2Walker client public static enum L2WalkerAllowed { True, False, GM }
     *
     * /** Allow the use of L2Walker client ? public static L2WalkerAllowed ALLOW_L2WALKER_CLIENT;
     *
     * /** Auto-ban client that use L2Walker ? public static boolean AUTOBAN_L2WALKER_ACC;
     *
     * /** Revision of L2Walker public static int L2WALKER_REVISION;
     */
    /** FloodProtector initial capacity */
    public static int FLOODPROTECTOR_INITIALSIZE;
    /** Allow Discard item ? */
    public static boolean ALLOW_DISCARDITEM;
    /** Allow freight ? */
    public static boolean ALLOW_FREIGHT;
    /** Allow warehouse ? */
    public static boolean ALLOW_WAREHOUSE;
    /** Allow warehouse cache? */
    public static boolean WAREHOUSE_CACHE;
    /** How long store WH datas */
    public static int WAREHOUSE_CACHE_TIME;
    /** Allow wear ? (try on in shop) */
    public static boolean ALLOW_WEAR;
    /** Duration of the try on after which items are taken back */
    public static int WEAR_DELAY;
    /** Price of the try on of one item */
    public static int WEAR_PRICE;
    /** Allow lottery ? */
    public static boolean ALLOW_LOTTERY;
    /** Allow race ? */
    public static boolean ALLOW_RACE;
    /** Allow water ? */
    public static boolean ALLOW_WATER;
    /** Allow Fishing ? */
    public static boolean ALLOW_FISHING;
    /** Allow rent pet ? */
    public static boolean ALLOW_RENTPET;
    /** Allow boat ? */
    public static boolean ALLOW_BOAT;
    /** Allow cursed weapons ? */
    public static boolean ALLOW_CURSED_WEAPONS;
    /** Time after which a packet is considered as lost */
    public static int PACKET_LIFETIME;
    // Pets
    /** Speed of Weverns */
    public static int WYVERN_SPEED;
    /** Speed of Striders */
    public static int STRIDER_SPEED;
    /** Allow Wyvern Upgrader ? */
    public static boolean ALLOW_WYVERN_UPGRADER;
    // protocol revision
    /** Minimal protocol revision */
    public static int MIN_PROTOCOL_REVISION;
    /** Maximal protocol revision */
    public static int MAX_PROTOCOL_REVISION;
    // random animation interval
    /** Minimal time between 2 animations of a NPC */
    public static int MIN_NPC_ANIMATION;
    /** Maximal time between 2 animations of a NPC */
    public static int MAX_NPC_ANIMATION;
    /** Activate position recorder ? */
    public static boolean ACTIVATE_POSITION_RECORDER;
    /** Use 3D Map ? */
    public static boolean USE_3D_MAP;
    // Community Board
    /** Type of community */
    public static String COMMUNITY_TYPE;
    public static String BBS_DEFAULT;
    /** Show level of the community board ? */
    public static boolean SHOW_LEVEL_COMMUNITYBOARD;
    /** Show status of the community board ? */
    public static boolean SHOW_STATUS_COMMUNITYBOARD;
    /** Size of the name page on the community board */
    public static int NAME_PAGE_SIZE_COMMUNITYBOARD;
    /** Name per row on community board */
    public static int NAME_PER_ROW_COMMUNITYBOARD;

    public static boolean CHECK_KNOWN;
    /** Game Server login port */
    public static int GAME_SERVER_LOGIN_PORT;
    /** Game Server login Host */
    public static String GAME_SERVER_LOGIN_HOST;
    /** Internal Hostname */
    public static String INTERNAL_HOSTNAME;
    /** External Hostname */
    public static String EXTERNAL_HOSTNAME;
    public static int PATH_NODE_RADIUS;
    public static int NEW_NODE_ID;
    public static int SELECTED_NODE_ID;
    public static int LINKED_NODE_ID;
    public static String NEW_NODE_TYPE;
    /** Show L2Monster level and aggro ? */
    public static boolean SHOW_NPC_LVL;
    /**
     * Force full item inventory packet to be sent for any item change ?<br>
     * <u><i>Note:</i></u> This can increase network traffic
     */
    public static boolean FORCE_INVENTORY_UPDATE;
    /** Disable the use of guards against agressive monsters ? */
    public static boolean ALLOW_GUARDS;
    /** Allow use Event Managers for change occupation ? */
    public static boolean ALLOW_CLASS_MASTERS;
    public static boolean ALLOW_CLASS_FIRST;
    public static boolean ALLOW_CLASS_SECOND;
    public static boolean ALLOW_CLASS_THIRD;
    /** Time between 2 updates of IP */
    public static int IP_UPDATE_TIME;
    // Server version
    /** Server version */
    public static String SERVER_VERSION;
    /** Date of server build */
    public static String SERVER_BUILD_DATE;
    // Datapack version
    /** Datapack version */
    public static String DATAPACK_VERSION;
    /** Zone Setting */
    public static int ZONE_TOWN;
    /** Crafting Enabled? */
    public static boolean IS_CRAFTING_ENABLED;
    // Inventory slots limit
    /** Maximum inventory slots limits for non dwarf characters */
    public static int INVENTORY_MAXIMUM_NO_DWARF;
    /** Maximum inventory slots limits for dwarf characters */
    public static int INVENTORY_MAXIMUM_DWARF;
    /** Maximum inventory slots limits for GM */
    public static int INVENTORY_MAXIMUM_GM;
    // Warehouse slots limits
    /** Maximum inventory slots limits for non dwarf warehouse */
    public static int WAREHOUSE_SLOTS_NO_DWARF;
    /** Maximum inventory slots limits for dwarf warehouse */
    public static int WAREHOUSE_SLOTS_DWARF;
    /** Maximum inventory slots limits for clan warehouse */
    public static int WAREHOUSE_SLOTS_CLAN;
    /** Maximum inventory slots limits for freight */
    public static int FREIGHT_SLOTS;
    // Karma System Variables
    /** Minimum karma gain/loss */
    public static int KARMA_MIN_KARMA;
    /** Maximum karma gain/loss */
    public static int KARMA_MAX_KARMA;
    /**
     * Number to divide the xp recieved by, to calculate karma lost on xp
     * gain/lost
     */
    public static int KARMA_XP_DIVIDER;
    /** The Minimum Karma lost if 0 karma is to be removed */
    public static int KARMA_LOST_BASE;
    /** Can a GM drop item ? */
    public static boolean KARMA_DROP_GM;
    /** Should award a pvp point for killing a player with karma ? */
    public static boolean KARMA_AWARD_PK_KILL;
    /** Minimum PK required to drop */
    public static int KARMA_PK_LIMIT;
    /** List of pet items that cannot be dropped (seperated by ",") when PVP */
    public static String KARMA_NONDROPPABLE_PET_ITEMS;
    /** List of items that cannot be dropped (seperated by ",") when PVP */
    public static String KARMA_NONDROPPABLE_ITEMS;
    /** List of pet items that cannot be dropped when PVP */
    public static List<Integer> KARMA_LIST_NONDROPPABLE_PET_ITEMS = new FastList<Integer>();
    /** List of items that cannot be dropped when PVP */
    public static List<Integer> KARMA_LIST_NONDROPPABLE_ITEMS = new FastList<Integer>();
    /** List of items that cannot be dropped (seperated by ",") */
    public static String NONDROPPABLE_ITEMS;
    /** List of items that cannot be dropped */
    public static List<Integer> LIST_NONDROPPABLE_ITEMS = new FastList<Integer>();
    /** List of NPCs that rent pets (seperated by ",") */
    public static String PET_RENT_NPC;
    /** List of NPCs that rent pets */
    public static List<Integer> LIST_PET_RENT_NPC = new FastList<Integer>();
    /**
     * Duration (in ms) while a player stay in PVP mode after hitting an
     * innocent
     */
    public static int PVP_NORMAL_TIME;
    /**
     * Duration (in ms) while a player stay in PVP mode after hitting a
     * purple player
     */
    public static int PVP_PVP_TIME;
    // Karma Punishment
    /** Allow player with karma to be killed in peace zone ? */
    public static boolean ALT_GAME_KARMA_PLAYER_CAN_BE_KILLED_IN_PEACEZONE;
    /** Allow player with karma to shop ? */
    public static boolean ALT_GAME_KARMA_PLAYER_CAN_SHOP;
    /** Allow player with karma to use gatekeepers ? */
    public static boolean ALT_GAME_KARMA_PLAYER_CAN_USE_GK;
    /** Allow player with karma to use SOE or Return skill ? */
    public static boolean ALT_GAME_KARMA_PLAYER_CAN_TELEPORT;
    /** Allow player with karma to trade ? */
    public static boolean ALT_GAME_KARMA_PLAYER_CAN_TRADE;
    /** Allow player with karma to use warehouse ? */
    public static boolean ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE;
    // Packet information
    /** Count the amount of packets per minute ? */
    public static boolean COUNT_PACKETS = false;
    /** Dump packet count ? */
    public static boolean DUMP_PACKET_COUNTS = false;
    /** Time interval between 2 dumps */
    public static int DUMP_INTERVAL_SECONDS = 60;

    /** Enumeration for type of ID Factory */
    public static enum IdFactoryType
    {
	Compaction, BitSet, Stack
    }

    /** ID Factory type */
    public static IdFactoryType IDFACTORY_TYPE;
    /** Check for bad ID ? */
    public static boolean BAD_ID_CHECKING;

    /** Enumeration for type of maps object */
    public static enum ObjectMapType
    {
	L2ObjectHashMap, WorldObjectMap
    }

    /** Enumeration for type of set object */
    public static enum ObjectSetType
    {
	L2ObjectHashSet, WorldObjectSet
    }

    /** Type of map object */
    public static ObjectMapType MAP_TYPE;
    /** Type of set object */
    public static ObjectSetType SET_TYPE;
    /**
     * Allow lesser effects to be canceled if stronger effects are used when
     * effects of the same stack group are used.<br>
     * New effects that are added will be canceled if they are of lesser
     * priority to the old one.
     */
    public static boolean EFFECT_CANCELING;
    /** Auto-delete invalid quest data ? */
    public static boolean AUTODELETE_INVALID_QUEST_DATA;
    /** Chance that an item will succesfully be enchanted */
    public static int ENCHANT_CHANCE_WEAPON;
    public static int ENCHANT_CHANCE_ARMOR;
    public static int ENCHANT_CHANCE_JEWELRY;
    /** Maximum level of enchantment */
    public static int ENCHANT_MAX_WEAPON;
    public static int ENCHANT_MAX_ARMOR;
    public static int ENCHANT_MAX_JEWELRY;
    /** maximum level of safe enchantment for normal items */
    public static int ENCHANT_SAFE_MAX;
    /** maximum level of safe enchantment for full body armor */
    public static int ENCHANT_SAFE_MAX_FULL;
    // Character multipliers
    /** Multiplier for character HP regeneration */
    public static double HP_REGEN_MULTIPLIER;
    /** Mutilplier for character MP regeneration */
    public static double MP_REGEN_MULTIPLIER;
    /** Multiplier for character CP regeneration */
    public static double CP_REGEN_MULTIPLIER;
    // Raid Boss multipliers
    /** Multiplier for Raid boss HP regeneration */
    public static double RAID_HP_REGEN_MULTIPLIER;
    /** Mulitplier for Raid boss MP regeneration */
    public static double RAID_MP_REGEN_MULTIPLIER;
    /** Multiplier for Raid boss defense multiplier */
    public static double RAID_DEFENCE_MULTIPLIER;
    /** Raid Boss Minin Spawn Timer */
    public static double RAID_MINION_RESPAWN_TIMER;
    /** Mulitplier for Raid boss minimum time respawn */
    public static float RAID_MIN_RESPAWN_MULTIPLIER;
    /** Mulitplier for Raid boss maximum time respawn */
    public static float RAID_MAX_RESPAWN_MULTIPLIER;
    /** Amount of adenas when starting a new character */
    public static int STARTING_ADENA;
    /** Deep Blue Mobs' Drop Rules Enabled */
    public static boolean DEEPBLUE_DROP_RULES;
    public static int UNSTUCK_INTERVAL;
    /** Is telnet enabled ? */
    public static boolean IS_TELNET_ENABLED;
    /** Player Protection control */
    public static int PLAYER_SPAWN_PROTECTION;
    public static int PLAYER_FAKEDEATH_UP_PROTECTION;
    /**
     * Define Party XP cutoff point method - Possible values: level and
     * percentage
     */
    public static String PARTY_XP_CUTOFF_METHOD;
    /** Define the cutoff point value for the "level" method */
    public static int PARTY_XP_CUTOFF_LEVEL;
    /** Define the cutoff point value for the "percentage" method */
    public static double PARTY_XP_CUTOFF_PERCENT;
    /** Percent CP is restore on respawn */
    public static double RESPAWN_RESTORE_CP;
    /** Percent HP is restore on respawn */
    public static double RESPAWN_RESTORE_HP;
    /** Percent MP is restore on respawn */
    public static double RESPAWN_RESTORE_MP;
    /** Allow randomizing of the respawn point in towns. */
    public static boolean RESPAWN_RANDOM_ENABLED;
    /** The maximum offset from the base respawn point to allow. */
    public static int RESPAWN_RANDOM_MAX_OFFSET;
    /** Maximum number of available slots for pvt stores (sell/buy) - Dwarves */
    public static int MAX_PVTSTORE_SLOTS_DWARF;
    /** Maximum number of available slots for pvt stores (sell/buy) - Others */
    public static int MAX_PVTSTORE_SLOTS_OTHER;
    /** Store skills cooltime on char exit/relogin */
    public static boolean STORE_SKILL_COOLTIME;
    /**
     * Show licence or not just after login (if False, will directly go to
     * the Server List
     */
    public static boolean SHOW_LICENCE;
    /** Force GameGuard authorization in loginserver */
    public static boolean FORCE_GGAUTH;
    /** Default punishment for illegal actions */
    public static int DEFAULT_PUNISH;
    /** Parameter for default punishment */
    public static int DEFAULT_PUNISH_PARAM;
    /** Accept new game server ? */
    public static boolean ACCEPT_NEW_GAMESERVER;
    /** Server ID used with the HexID */
    public static int SERVER_ID;
    /** Hexadecimal ID of the game server */
    public static byte[] HEX_ID;
    /** Accept alternate ID for server ? */
    public static boolean ACCEPT_ALTERNATE_ID;
    /** ID for request to the server */
    public static int REQUEST_ID;
    public static boolean RESERVE_HOST_ON_LOGIN = false;
    public static int MINIMUM_UPDATE_DISTANCE;
    public static int KNOWNLIST_FORGET_DELAY;
    public static int MINIMUN_UPDATE_TIME;
    public static boolean ANNOUNCE_MAMMON_SPAWN;
    public static boolean LAZY_CACHE;
    /** Enable colored name for GM ? */
    public static boolean GM_NAME_COLOR_ENABLED;
    /** Color of GM name */
    public static int GM_NAME_COLOR;
    /** Color of admin name */
    public static int ADMIN_NAME_COLOR;
    /** Place an aura around the GM ? */
    public static boolean GM_HERO_AURA;
    /** Set the GM invulnerable at startup ? */
    public static boolean GM_STARTUP_INVULNERABLE;
    /** Set the GM invisible at startup ? */
    public static boolean GM_STARTUP_INVISIBLE;
    /** Set silence to GM at startup ? */
    public static boolean GM_STARTUP_SILENCE;
    public static int MASTERACCESS_LEVEL;
    public static int MASTERACCESS_NAME_COLOR;
    public static int MASTERACCESS_TITLE_COLOR;
    /** Add GM in the GM list at startup ? */
    public static boolean GM_STARTUP_AUTO_LIST;
    /** Allow petition ? */
    public static boolean PETITIONING_ALLOWED;
    /** Maximum number of petitions per player */
    public static int MAX_PETITIONS_PER_PLAYER;
    /** Maximum number of petitions pending */
    public static int MAX_PETITIONS_PENDING;
    /** High Priestess van Halter */
    public static int HPH_FIXINTERVALOFHALTER;
    public static int HPH_RANDOMINTERVALOFHALTER;
    public static int HPH_APPTIMEOFHALTER;
    public static int HPH_ACTIVITYTIMEOFHALTER;
    public static int HPH_FIGHTTIMEOFHALTER;
    public static int HPH_CALLROYALGUARDHELPERCOUNT;
    public static int HPH_CALLROYALGUARDHELPERINTERVAL;
    public static int HPH_INTERVALOFDOOROFALTER;
    public static int HPH_TIMEOFLOCKUPDOOROFALTAR;
    /** Bypass exploit protection ? */
    public static boolean BYPASS_VALIDATION;
    /** Only GM buy items for free* */
    public static boolean ONLY_GM_ITEMS_FREE;
    /** Allow auto-create account ? */
    public static boolean AUTO_CREATE_ACCOUNTS;
    public static boolean FLOOD_PROTECTION;
    public static int FAST_CONNECTION_LIMIT;
    public static int NORMAL_CONNECTION_TIME;
    public static int FAST_CONNECTION_TIME;
    public static int MAX_CONNECTION_PER_IP;
    /** Enforce gameguard query on character login ? */
    public static boolean GAMEGUARD_ENFORCE;
    /**
     * Don't allow player to perform trade,talk with npc and move until
     * gameguard reply received ?
     */
    public static boolean GAMEGUARD_PROHIBITACTION;
    /** Recipebook limits */
    public static int DWARF_RECIPE_LIMIT;
    public static int COMMON_RECIPE_LIMIT;
    /** Grid Options */
    public static boolean GRIDS_ALWAYS_ON;
    public static int GRID_NEIGHBOR_TURNON_TIME;
    public static int GRID_NEIGHBOR_TURNOFF_TIME;
    /** Clan Hall function related configs */
    public static long CH_TELE_FEE_RATIO;
    public static int CH_TELE1_FEE;
    public static int CH_TELE2_FEE;
    public static long CH_ITEM_FEE_RATIO;
    public static int CH_ITEM1_FEE;
    public static int CH_ITEM2_FEE;
    public static int CH_ITEM3_FEE;
    public static long CH_MPREG_FEE_RATIO;
    public static int CH_MPREG1_FEE;
    public static int CH_MPREG2_FEE;
    public static int CH_MPREG3_FEE;
    public static int CH_MPREG4_FEE;
    public static int CH_MPREG5_FEE;
    public static long CH_HPREG_FEE_RATIO;
    public static int CH_HPREG1_FEE;
    public static int CH_HPREG2_FEE;
    public static int CH_HPREG3_FEE;
    public static int CH_HPREG4_FEE;
    public static int CH_HPREG5_FEE;
    public static int CH_HPREG6_FEE;
    public static int CH_HPREG7_FEE;
    public static int CH_HPREG8_FEE;
    public static int CH_HPREG9_FEE;
    public static int CH_HPREG10_FEE;
    public static int CH_HPREG11_FEE;
    public static int CH_HPREG12_FEE;
    public static int CH_HPREG13_FEE;
    public static long CH_EXPREG_FEE_RATIO;
    public static int CH_EXPREG1_FEE;
    public static int CH_EXPREG2_FEE;
    public static int CH_EXPREG3_FEE;
    public static int CH_EXPREG4_FEE;
    public static int CH_EXPREG5_FEE;
    public static int CH_EXPREG6_FEE;
    public static int CH_EXPREG7_FEE;
    public static long CH_SUPPORT_FEE_RATIO;
    public static int CH_SUPPORT1_FEE;
    public static int CH_SUPPORT2_FEE;
    public static int CH_SUPPORT3_FEE;
    public static int CH_SUPPORT4_FEE;
    public static int CH_SUPPORT5_FEE;
    public static int CH_SUPPORT6_FEE;
    public static int CH_SUPPORT7_FEE;
    public static int CH_SUPPORT8_FEE;
    public static long CH_CURTAIN_FEE_RATIO;
    public static int CH_CURTAIN1_FEE;
    public static int CH_CURTAIN2_FEE;
    public static long CH_FRONT_FEE_RATIO;
    public static int CH_FRONT1_FEE;
    public static int CH_FRONT2_FEE;
    /** GeoData 0/1/2 */
    public static int GEODATA;
    /** Force loading GeoData to psychical memory */
    public static boolean FORCE_GEODATA;
    public static boolean ACCEPT_GEOEDITOR_CONN;
    /** Max amount of buffs */
    public static byte BUFFS_MAX_AMOUNT;
    /** Alt Settings for devs */
    public static boolean ALT_DEV_NO_QUESTS;
    public static boolean ALT_DEV_NO_SPAWNS;
    /** IRC Settings * */
    public static String IRC_HOSTNAME;
    public static int IRC_PORT;
    public static String IRC_USERNAME;
    public static String IRC_USERREALNAME;
    public static String IRC_USERMAIL;
    public static String IRC_CHANNEL;
    public static String IRC_IGTOIRC_FILE;
    public static String IRC_KEYWORDTOIG;
    public static String IRC_KEYWORDTOIRC;
    public static boolean IRC_LOAD;

    /**
     * This class initializes all global variables for configuration.<br>
     * If key doesn't appear in properties file, a default value is setting
     * on by this class.
     *
     * @see CONFIGURATION_FILE (propertie file) for configuring your server.
     */
    public static void load()
    {
	if (Server.serverMode == Server.MODE_GAMESERVER)
	{
	    _log.info("loading gameserver config");
	    try
	    {
		Properties serverSettings = new Properties();
		InputStream is = new FileInputStream(new File(CONFIGURATION_FILE));
		serverSettings.load(is);
		is.close();
		GAMESERVER_HOSTNAME = serverSettings.getProperty("GameserverHostname");
		PORT_GAME = Integer.parseInt(serverSettings.getProperty("GameserverPort", "7777"));
		EXTERNAL_HOSTNAME = serverSettings.getProperty("ExternalHostname", "*");
		INTERNAL_HOSTNAME = serverSettings.getProperty("InternalHostname", "*");
		GAME_SERVER_LOGIN_PORT = Integer.parseInt(serverSettings.getProperty("LoginPort", "9014"));
		GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHost", "127.0.0.1");
		REQUEST_ID = Integer.parseInt(serverSettings.getProperty("RequestServerID", "0"));
		ACCEPT_ALTERNATE_ID = Boolean.parseBoolean(serverSettings.getProperty("AcceptAlternateID", "True"));
		DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
		DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l2jdb");
		DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
		DATABASE_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaximumDbConnections", "50"));
		DATABASE_MAXSTATEMENTS = Integer.parseInt(serverSettings.getProperty("MaximumStateMents", "100"));
		DATABASE_MIN_POOLSIZE = Integer.parseInt(serverSettings.getProperty("MinPoolSize", "50"));
		DATABASE_MAX_POOLSIZE = Integer.parseInt(serverSettings.getProperty("MaxPoolSize", "10"));
		DATABASE_ACQUIREINCREMENT = Integer.parseInt(serverSettings.getProperty("AquireIncrement", "1"));
		DATABASE_IDLECONNECTIONTEST = Integer.parseInt(serverSettings.getProperty("IdleConnectionTest", "10800"));
		DATABASE_MAXIDLETIME = Integer.parseInt(serverSettings.getProperty("MaxIdleTime", "0"));
		DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".")).getCanonicalFile();
		CNAME_TEMPLATE = serverSettings.getProperty("CnameTemplate", ".*");
		PET_NAME_TEMPLATE = serverSettings.getProperty("PetNameTemplate", ".*");
		MAX_CHARACTERS_NUMBER_PER_ACCOUNT = Integer.parseInt(serverSettings.getProperty("CharMaxNumber", "0"));
		MAXIMUM_ONLINE_USERS = Integer.parseInt(serverSettings.getProperty("MaximumOnlineUsers", "100"));
		MIN_PROTOCOL_REVISION = Integer.parseInt(serverSettings.getProperty("MinProtocolRevision", "660"));
		MAX_PROTOCOL_REVISION = Integer.parseInt(serverSettings.getProperty("MaxProtocolRevision", "665"));
		if (MIN_PROTOCOL_REVISION > MAX_PROTOCOL_REVISION)
		{
		    throw new Error("MinProtocolRevision is bigger than MaxProtocolRevision in server configuration file.");
		}
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + CONFIGURATION_FILE + " File.");
	    }
	            
	            // General Config File
	            try
	            {
		Properties General = new Properties();
		InputStream is = new FileInputStream(new File(GENERAL_CONFIG_FILE));
		General.load(is);
		is.close();
	                
                FAILD_FAKEDEATH = Boolean.parseBoolean(General.getProperty("FaildFakeDeath", "True"));
                /** L2J NPC Buffer by House  */
                NPCBUFFER_FEATURE_ENABLED				= Boolean.valueOf(General.getProperty("NPCBufferEnabled", "False"));		
                NPCBUFFER_MAX_SCHEMES					= Integer.parseInt(General.getProperty("NPCBufferMaxSchemesPerChar", "4"));
                NPCBUFFER_MAX_SKILLS					= Integer.parseInt(General.getProperty("NPCBufferMaxSkllsperScheme", "24"));
                NPCBUFFER_STORE_SCHEMES					= Boolean.valueOf(General.getProperty("NPCBufferStoreSchemes", "True"));
                NPCBUFFER_STATIC_BUFF_COST				= Integer.parseInt(General.getProperty("NPCBufferStaticCostPerBuff", "-1"));
		
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + OLYMPIAD_FILE + " File.");
	    }
	            
	            // Olympiad Config File
	            try
	            {
		Properties OlympiadSettings = new Properties();
		InputStream is = new FileInputStream(new File(OLYMPIAD_FILE));
		OlympiadSettings.load(is);
		is.close();
			OLYMPIAD_ALLOW_AUTO_SS                              = Boolean.parseBoolean(OlympiadSettings.getProperty("OlympiadAllowAutoSS","True"));
			OLYMPIAD_GIVE_ACUMEN_MAGES                          = Boolean.parseBoolean(OlympiadSettings.getProperty("OlympiadGiveAcumenMages","False"));
			OLYMPIAD_GIVE_HASTE_FIGHTERS                        = Boolean.parseBoolean(OlympiadSettings.getProperty("OlympiadGiveHasteFighters","True"));
			OLYMPIAD_ACUMEN_LVL                                 = Integer.parseInt(OlympiadSettings.getProperty("OlympiadAcumenLvl", "1"));
			OLYMPIAD_HASTE_LVL                                  = Integer.parseInt(OlympiadSettings.getProperty("OlympiadHasteLvl", "2"));
		
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + OLYMPIAD_FILE + " File.");
	    }
	            
	            // Feature Config File
	            try
	            {
		Properties Feature = new Properties();
		InputStream is = new FileInputStream(new File(FEATURE_CONFIG_FILE));
		Feature.load(is);
		is.close();
		INVUL_NPC_LIST = new FastList();
			    String t = Feature.getProperty("InvulNpcList", "30001-32132,35092-35103,35142-35146,35176-35187,35218-35232,35261-35278,35308-35319,35352-35367,35382-35407,35417-35427,35433-35469,35497-35513,35544-35587,35600-35617,35623-35628,35638-35640,35644,35645,50007,70010,99999");
			    String as[];
			    int k = (as = t.split(",")).length;
				for(int j = 0; j < k; j++)
				{
					String t2 = as[j];
					if(t2.contains("-"))
					{
						int a1 = Integer.parseInt(t2.split("-")[0]);
						int a2 = Integer.parseInt(t2.split("-")[1]);
						for(int i = a1; i <= a2; i++)
							INVUL_NPC_LIST.add(Integer.valueOf(i));
					} else
					{
						INVUL_NPC_LIST.add(Integer.valueOf(Integer.parseInt(t2)));
					}
			    }
		FS_TELE_FEE_RATIO                                   = Long.parseLong(Feature.getProperty("FortressTeleportFunctionFeeRatio", "604800000"));
	                FS_TELE1_FEE                                        = Integer.parseInt(Feature.getProperty("FortressTeleportFunctionFeeLvl1", "1000"));
	                FS_TELE2_FEE                                        = Integer.parseInt(Feature.getProperty("FortressTeleportFunctionFeeLvl2", "10000"));
	                FS_SUPPORT_FEE_RATIO                                = Long.parseLong(Feature.getProperty("FortressSupportFunctionFeeRatio", "86400000"));
	                FS_SUPPORT1_FEE                                     = Integer.parseInt(Feature.getProperty("FortressSupportFeeLvl1", "7000"));
	                FS_SUPPORT2_FEE                                     = Integer.parseInt(Feature.getProperty("FortressSupportFeeLvl2", "17000"));
	                FS_MPREG_FEE_RATIO                                  = Long.parseLong(Feature.getProperty("FortressMpRegenerationFunctionFeeRatio", "86400000"));
	                FS_MPREG1_FEE                                       = Integer.parseInt(Feature.getProperty("FortressMpRegenerationFeeLvl1", "6500"));
	                FS_MPREG2_FEE                                       = Integer.parseInt(Feature.getProperty("FortressMpRegenerationFeeLvl2", "9300"));
	                FS_HPREG_FEE_RATIO                                  = Long.parseLong(Feature.getProperty("FortressHpRegenerationFunctionFeeRatio", "86400000"));
	                FS_HPREG1_FEE                                       = Integer.parseInt(Feature.getProperty("FortressHpRegenerationFeeLvl1", "2000"));
	                FS_HPREG2_FEE                                       = Integer.parseInt(Feature.getProperty("FortressHpRegenerationFeeLvl2", "3500"));
	                FS_EXPREG_FEE_RATIO                                 = Long.parseLong(Feature.getProperty("FortressExpRegenerationFunctionFeeRatio", "86400000"));
	                FS_EXPREG1_FEE                                      = Integer.parseInt(Feature.getProperty("FortressExpRegenerationFeeLvl1", "9000"));
	                FS_EXPREG2_FEE                                      = Integer.parseInt(Feature.getProperty("FortressExpRegenerationFeeLvl2", "10000"));
	                //
	                	                CS_TELE_FEE_RATIO                                   = Long.parseLong(Feature.getProperty("CastleTeleportFunctionFeeRatio", "604800000"));
	                CS_TELE1_FEE                                        = Integer.parseInt(Feature.getProperty("CastleTeleportFunctionFeeLvl1", "7000"));
	                CS_TELE2_FEE                                        = Integer.parseInt(Feature.getProperty("CastleTeleportFunctionFeeLvl2", "14000"));
	                CS_SUPPORT_FEE_RATIO                                = Long.parseLong(Feature.getProperty("CastleSupportFunctionFeeRatio", "86400000"));
	                CS_SUPPORT1_FEE                                     = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl1", "7000"));
	                CS_SUPPORT2_FEE                                     = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl2", "21000"));
	                CS_SUPPORT3_FEE                                     = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl3", "37000"));
	                CS_SUPPORT4_FEE                                     = Integer.parseInt(Feature.getProperty("CastleSupportFeeLvl4", "52000"));
	                CS_MPREG_FEE_RATIO                                  = Long.parseLong(Feature.getProperty("CastleMpRegenerationFunctionFeeRatio", "86400000"));
	                CS_MPREG1_FEE                                       = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl1", "2000"));
	                CS_MPREG2_FEE                                       = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl2", "6500"));
	                CS_MPREG3_FEE                                       = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl3", "13750"));
	                CS_MPREG4_FEE                                       = Integer.parseInt(Feature.getProperty("CastleMpRegenerationFeeLvl4", "20000"));
	                CS_HPREG_FEE_RATIO                                  = Long.parseLong(Feature.getProperty("CastleHpRegenerationFunctionFeeRatio", "86400000"));
	                CS_HPREG1_FEE                                       = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl1", "1000"));
	                CS_HPREG2_FEE                                       = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl2", "1500"));
	                CS_HPREG3_FEE                                       = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl3", "2250"));
	                CS_HPREG4_FEE                                      = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl14", "3270"));
	                CS_HPREG5_FEE                                      = Integer.parseInt(Feature.getProperty("CastleHpRegenerationFeeLvl15", "5166"));
	                CS_EXPREG_FEE_RATIO                                 = Long.parseLong(Feature.getProperty("CastleExpRegenerationFunctionFeeRatio", "86400000"));
	                CS_EXPREG1_FEE                                      = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl1", "9000"));
	                CS_EXPREG2_FEE                                      = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl2", "15000"));
	                CS_EXPREG3_FEE                                      = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl3", "21000"));
	                CS_EXPREG4_FEE                                      = Integer.parseInt(Feature.getProperty("CastleExpRegenerationFeeLvl4", "30000"));
	                NPC_ATTACKABLE = Boolean.valueOf(Feature.getProperty("NpcAttackable", "False"));
	                }
	            catch (Exception e)
	            {
	                e.printStackTrace();
	                throw new Error("Failed to Load "+FEATURE_CONFIG_FILE+" File.");
	            }
	            
	            // Character Config File
	            try
	            {
		Properties optionsSettings = new Properties();
		InputStream is = new FileInputStream(new File(OPTIONS_FILE));
		optionsSettings.load(is);
		is.close();
		EVERYBODY_HAS_ADMIN_RIGHTS = Boolean.parseBoolean(optionsSettings.getProperty("EverybodyHasAdminRights", "False"));
		DEBUG = Boolean.parseBoolean(optionsSettings.getProperty("Debug", "False"));
		ASSERT = Boolean.parseBoolean(optionsSettings.getProperty("Assert", "False"));
		DEVELOPER = Boolean.parseBoolean(optionsSettings.getProperty("Developer", "False"));
		TEST_SERVER = Boolean.parseBoolean(optionsSettings.getProperty("TestServer", "False"));
		SERVER_LIST_TESTSERVER = Boolean.parseBoolean(optionsSettings.getProperty("TestServer", "False"));
		SERVER_LIST_BRACKET = Boolean.valueOf(optionsSettings.getProperty("ServerListBrackets", "False"));
		SERVER_LIST_CLOCK = Boolean.valueOf(optionsSettings.getProperty("ServerListClock", "False"));
		SERVER_GMONLY = Boolean.valueOf(optionsSettings.getProperty("ServerGMOnly", "False"));
		AUTODESTROY_ITEM_AFTER = Integer.parseInt(optionsSettings.getProperty("AutoDestroyDroppedItemAfter", "0"));
		HERB_AUTO_DESTROY_TIME = Integer.parseInt(optionsSettings.getProperty("AutoDestroyHerbTime", "15")) * 1000;
		PROTECTED_ITEMS = optionsSettings.getProperty("ListOfProtectedItems");
		LIST_PROTECTED_ITEMS = new FastList<Integer>();
		for (String id : PROTECTED_ITEMS.split(","))
		{
		    LIST_PROTECTED_ITEMS.add(Integer.parseInt(id));
		}
		DESTROY_DROPPED_PLAYER_ITEM = Boolean.valueOf(optionsSettings.getProperty("DestroyPlayerDroppedItem", "False"));
		DESTROY_EQUIPABLE_PLAYER_ITEM = Boolean.valueOf(optionsSettings.getProperty("DestroyEquipableItem", "False"));
		SAVE_DROPPED_ITEM = Boolean.valueOf(optionsSettings.getProperty("SaveDroppedItem", "False"));
		EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD = Boolean.valueOf(optionsSettings.getProperty("EmptyDroppedItemTableAfterLoad", "False"));
		SAVE_DROPPED_ITEM_INTERVAL = Integer.parseInt(optionsSettings.getProperty("SaveDroppedItemInterval", "0")) * 60000;
		CLEAR_DROPPED_ITEM_TABLE = Boolean.valueOf(optionsSettings.getProperty("ClearDroppedItemTable", "False"));
		PRECISE_DROP_CALCULATION = Boolean.valueOf(optionsSettings.getProperty("PreciseDropCalculation", "True"));
		MULTIPLE_ITEM_DROP = Boolean.valueOf(optionsSettings.getProperty("MultipleItemDrop", "True"));
		COORD_SYNCHRONIZE = Integer.parseInt(optionsSettings.getProperty("CoordSynchronize", "-1"));
		ONLY_GM_ITEMS_FREE = Boolean.valueOf(optionsSettings.getProperty("OnlyGMItemsFree", "True"));
		ALLOW_WAREHOUSE = Boolean.valueOf(optionsSettings.getProperty("AllowWarehouse", "True"));
		WAREHOUSE_CACHE = Boolean.valueOf(optionsSettings.getProperty("WarehouseCache", "False"));
		WAREHOUSE_CACHE_TIME = Integer.parseInt(optionsSettings.getProperty("WarehouseCacheTime", "15"));
		ALLOW_FREIGHT = Boolean.valueOf(optionsSettings.getProperty("AllowFreight", "True"));
		ALLOW_WEAR = Boolean.valueOf(optionsSettings.getProperty("AllowWear", "False"));
		WEAR_DELAY = Integer.parseInt(optionsSettings.getProperty("WearDelay", "5"));
		WEAR_PRICE = Integer.parseInt(optionsSettings.getProperty("WearPrice", "10"));
		ALLOW_LOTTERY = Boolean.valueOf(optionsSettings.getProperty("AllowLottery", "False"));
		ALLOW_RACE = Boolean.valueOf(optionsSettings.getProperty("AllowRace", "False"));
		ALLOW_WATER = Boolean.valueOf(optionsSettings.getProperty("AllowWater", "False"));
		ALLOW_FISHING = Boolean.valueOf(optionsSettings.getProperty("AllowFishing_2", "False"));
		ALLOW_RENTPET = Boolean.valueOf(optionsSettings.getProperty("AllowRentPet", "False"));
		FLOODPROTECTOR_INITIALSIZE = Integer.parseInt(optionsSettings.getProperty("FloodProtectorInitialSize", "50"));
		ALLOW_DISCARDITEM = Boolean.valueOf(optionsSettings.getProperty("AllowDiscardItem", "True"));
		ALLOWFISHING = Boolean.valueOf(optionsSettings.getProperty("AllowFishing", "False"));
		ALLOW_BOAT = Boolean.valueOf(optionsSettings.getProperty("AllowBoat", "False"));
		ALLOW_CURSED_WEAPONS = Boolean.valueOf(optionsSettings.getProperty("AllowCursedWeapons", "False"));
		/*
		 * Removed by Ahmed, Reason: This has no effect at all whether
		 * players can use L2Walker or not.
		 */
		/**
		 * ALLOW_L2WALKER_CLIENT =
		 * L2WalkerAllowed.valueOf(optionsSettings.getProperty("AllowL2Walker",
		 * "False")); L2WALKER_REVISION =
		 * Integer.parseInt(optionsSettings.getProperty("L2WalkerRevision",
		 * "537")); AUTOBAN_L2WALKER_ACC =
		 * Boolean.valueOf(optionsSettings.getProperty("AutobanL2WalkerAcc",
		 * "False"));
		 */
		ACTIVATE_POSITION_RECORDER = Boolean.valueOf(optionsSettings.getProperty("ActivatePositionRecorder", "False"));
		DEFAULT_GLOBAL_CHAT = optionsSettings.getProperty("GlobalChat", "ON");
		DEFAULT_TRADE_CHAT = optionsSettings.getProperty("TradeChat", "ON");
		LOG_CHAT = Boolean.valueOf(optionsSettings.getProperty("LogChat", "False"));
		LOG_ITEMS = Boolean.valueOf(optionsSettings.getProperty("LogItems", "False"));
		COMMUNITY_TYPE = optionsSettings.getProperty("CommunityType", "old").toLowerCase();
		BBS_DEFAULT = optionsSettings.getProperty("BBSDefault", "_bbshome");
		SHOW_LEVEL_COMMUNITYBOARD = Boolean.valueOf(optionsSettings.getProperty("ShowLevelOnCommunityBoard", "False"));
		SHOW_STATUS_COMMUNITYBOARD = Boolean.valueOf(optionsSettings.getProperty("ShowStatusOnCommunityBoard", "True"));
		NAME_PAGE_SIZE_COMMUNITYBOARD = Integer.parseInt(optionsSettings.getProperty("NamePageSizeOnCommunityBoard", "50"));
		NAME_PER_ROW_COMMUNITYBOARD = Integer.parseInt(optionsSettings.getProperty("NamePerRowOnCommunityBoard", "5"));
		ZONE_TOWN = Integer.parseInt(optionsSettings.getProperty("ZoneTown", "0"));
		MAX_DRIFT_RANGE = Integer.parseInt(optionsSettings.getProperty("MaxDriftRange", "300"));
		MIN_NPC_ANIMATION = Integer.parseInt(optionsSettings.getProperty("MinNPCAnimation", "10"));
		MAX_NPC_ANIMATION = Integer.parseInt(optionsSettings.getProperty("MaxNPCAnimation", "20"));
		SHOW_NPC_LVL = Boolean.valueOf(optionsSettings.getProperty("ShowNpcLevel", "False"));
		FORCE_INVENTORY_UPDATE = Boolean.valueOf(optionsSettings.getProperty("ForceInventoryUpdate", "False"));
		AUTODELETE_INVALID_QUEST_DATA = Boolean.valueOf(optionsSettings.getProperty("AutoDeleteInvalidQuestData", "False"));
		THREAD_P_EFFECTS = Integer.parseInt(optionsSettings.getProperty("ThreadPoolSizeEffects", "6"));
		THREAD_P_GENERAL = Integer.parseInt(optionsSettings.getProperty("ThreadPoolSizeGeneral", "15"));
		GENERAL_PACKET_THREAD_CORE_SIZE = Integer.parseInt(optionsSettings.getProperty("GeneralPacketThreadCoreSize", "4"));
		IO_PACKET_THREAD_CORE_SIZE = Integer.parseInt(optionsSettings.getProperty("UrgentPacketThreadCoreSize", "2"));
		AI_MAX_THREAD = Integer.parseInt(optionsSettings.getProperty("AiMaxThread", "10"));
		GENERAL_THREAD_CORE_SIZE = Integer.parseInt(optionsSettings.getProperty("GeneralThreadCoreSize", "4"));
		DELETE_DAYS = Integer.parseInt(optionsSettings.getProperty("DeleteCharAfterDays", "7"));
		DEFAULT_PUNISH = Integer.parseInt(optionsSettings.getProperty("DefaultPunish", "2"));
		DEFAULT_PUNISH_PARAM = Integer.parseInt(optionsSettings.getProperty("DefaultPunishParam", "0"));
		LAZY_CACHE = Boolean.valueOf(optionsSettings.getProperty("LazyCache", "False"));
		PACKET_LIFETIME = Integer.parseInt(optionsSettings.getProperty("PacketLifeTime", "0"));
		BYPASS_VALIDATION = Boolean.valueOf(optionsSettings.getProperty("BypassValidation", "True"));
		GAMEGUARD_ENFORCE = Boolean.valueOf(optionsSettings.getProperty("GameGuardEnforce", "False"));
		GAMEGUARD_PROHIBITACTION = Boolean.valueOf(optionsSettings.getProperty("GameGuardProhibitAction", "False"));
		GRIDS_ALWAYS_ON = Boolean.parseBoolean(optionsSettings.getProperty("GridsAlwaysOn", "False"));
		GRID_NEIGHBOR_TURNON_TIME = Integer.parseInt(optionsSettings.getProperty("GridNeighborTurnOnTime", "30"));
		GRID_NEIGHBOR_TURNOFF_TIME = Integer.parseInt(optionsSettings.getProperty("GridNeighborTurnOffTime", "300"));
		GEODATA = Integer.parseInt(optionsSettings.getProperty("GeoData", "0"));
		FORCE_GEODATA = Boolean.parseBoolean(optionsSettings.getProperty("ForceGeoData", "True"));
		ACCEPT_GEOEDITOR_CONN = Boolean.parseBoolean(optionsSettings.getProperty("AcceptGeoeditorConn", "False"));
		// ---------------------------------------------------
		// Configuration values not found in config files
		// ---------------------------------------------------
		USE_3D_MAP = Boolean.valueOf(optionsSettings.getProperty("Use3DMap", "False"));
		PATH_NODE_RADIUS = Integer.parseInt(optionsSettings.getProperty("PathNodeRadius", "50"));
		NEW_NODE_ID = Integer.parseInt(optionsSettings.getProperty("NewNodeId", "7952"));
		SELECTED_NODE_ID = Integer.parseInt(optionsSettings.getProperty("NewNodeId", "7952"));
		LINKED_NODE_ID = Integer.parseInt(optionsSettings.getProperty("NewNodeId", "7952"));
		NEW_NODE_TYPE = optionsSettings.getProperty("NewNodeType", "npc");
		COUNT_PACKETS = Boolean.valueOf(optionsSettings.getProperty("CountPacket", "False"));
		DUMP_PACKET_COUNTS = Boolean.valueOf(optionsSettings.getProperty("DumpPacketCounts", "False"));
		DUMP_INTERVAL_SECONDS = Integer.parseInt(optionsSettings.getProperty("PacketDumpInterval", "60"));
		MINIMUM_UPDATE_DISTANCE = Integer.parseInt(optionsSettings.getProperty("MaximumUpdateDistance", "50"));
		MINIMUN_UPDATE_TIME = Integer.parseInt(optionsSettings.getProperty("MinimumUpdateTime", "500"));
		CHECK_KNOWN = Boolean.valueOf(optionsSettings.getProperty("CheckKnownList", "False"));
		KNOWNLIST_FORGET_DELAY = Integer.parseInt(optionsSettings.getProperty("KnownListForgetDelay", "10000"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + OPTIONS_FILE + " File.");
	    }
	    /*
	     * Load L2J Server Version Properties file (if exists)
	     */
	    try
	    {
		Properties serverVersion = new Properties();
		InputStream is = new FileInputStream(new File(SERVER_VERSION_FILE));
		serverVersion.load(is);
		is.close();
		SERVER_VERSION = serverVersion.getProperty("committed-rev", "Unsupported Custom Version.");
		SERVER_BUILD_DATE = serverVersion.getProperty("builddate", "Undefined Date.");
	    } catch (Exception e)
	    {
		// Ignore Properties file if it doesnt exist
		SERVER_VERSION = "Unsupported Custom Version.";
		SERVER_BUILD_DATE = "Undefined Date.";
	    }
	    /*
	     * Load L2J Datapack Version Properties file (if exists)
	     */
	    try
	    {
		Properties serverVersion = new Properties();
		InputStream is = new FileInputStream(new File(DATAPACK_VERSION_FILE));
		serverVersion.load(is);
		is.close();
		DATAPACK_VERSION = serverVersion.getProperty("committed-rev", "Unsupported Custom Version.");
	    } catch (Exception e)
	    {
		// Ignore Properties file if it doesnt exist
		DATAPACK_VERSION = "Unsupported Custom Version.";
	    }
	    // telnet
	    try
	    {
		Properties telnetSettings = new Properties();
		InputStream is = new FileInputStream(new File(TELNET_FILE));
		telnetSettings.load(is);
		is.close();
		IS_TELNET_ENABLED = Boolean.valueOf(telnetSettings.getProperty("EnableTelnet", "False"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + TELNET_FILE + " File.");
	    }
	    // id factory
	    try
	    {
		Properties idSettings = new Properties();
		InputStream is = new FileInputStream(new File(ID_CONFIG_FILE));
		idSettings.load(is);
		is.close();
		MAP_TYPE = ObjectMapType.valueOf(idSettings.getProperty("L2Map", "WorldObjectMap"));
		SET_TYPE = ObjectSetType.valueOf(idSettings.getProperty("L2Set", "WorldObjectSet"));
		IDFACTORY_TYPE = IdFactoryType.valueOf(idSettings.getProperty("IDFactory", "Compaction"));
		BAD_ID_CHECKING = Boolean.valueOf(idSettings.getProperty("BadIdChecking", "True"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + ID_CONFIG_FILE + " File.");
	    }
	    // other
	    try
	    {
		Properties otherSettings = new Properties();
		InputStream is = new FileInputStream(new File(OTHER_CONFIG_FILE));
		otherSettings.load(is);
		is.close();
		DEEPBLUE_DROP_RULES = Boolean.parseBoolean(otherSettings.getProperty("UseDeepBlueDropRules", "True"));
		ALLOW_GUARDS = Boolean.valueOf(otherSettings.getProperty("AllowGuards", "False"));
		EFFECT_CANCELING = Boolean.valueOf(otherSettings.getProperty("CancelLesserEffect", "True"));
		WYVERN_SPEED = Integer.parseInt(otherSettings.getProperty("WyvernSpeed", "100"));
		STRIDER_SPEED = Integer.parseInt(otherSettings.getProperty("StriderSpeed", "80"));
		ALLOW_WYVERN_UPGRADER = Boolean.valueOf(otherSettings.getProperty("AllowWyvernUpgrader", "False"));
		/* Inventory slots limits */
		INVENTORY_MAXIMUM_NO_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForNoDwarf", "80"));
		INVENTORY_MAXIMUM_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForDwarf", "100"));
		INVENTORY_MAXIMUM_GM = Integer.parseInt(otherSettings.getProperty("MaximumSlotsForGMPlayer", "250"));
		MAX_ITEM_IN_PACKET = Math.max(INVENTORY_MAXIMUM_NO_DWARF, Math.max(INVENTORY_MAXIMUM_DWARF, INVENTORY_MAXIMUM_GM));
		/* Inventory slots limits */
		WAREHOUSE_SLOTS_NO_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumWarehouseSlotsForNoDwarf", "100"));
		WAREHOUSE_SLOTS_DWARF = Integer.parseInt(otherSettings.getProperty("MaximumWarehouseSlotsForDwarf", "120"));
		WAREHOUSE_SLOTS_CLAN = Integer.parseInt(otherSettings.getProperty("MaximumWarehouseSlotsForClan", "150"));
		FREIGHT_SLOTS = Integer.parseInt(otherSettings.getProperty("MaximumFreightSlots", "20"));
		/* chance to enchant an item over +3 */
		ENCHANT_CHANCE_WEAPON = Integer.parseInt(otherSettings.getProperty("EnchantChanceWeapon", "68"));
		ENCHANT_CHANCE_ARMOR = Integer.parseInt(otherSettings.getProperty("EnchantChanceArmor", "52"));
		ENCHANT_CHANCE_JEWELRY = Integer.parseInt(otherSettings.getProperty("EnchantChanceJewelry", "54"));
		/* limit on enchant */
		ENCHANT_MAX_WEAPON = Integer.parseInt(otherSettings.getProperty("EnchantMaxWeapon", "255"));
		ENCHANT_MAX_ARMOR = Integer.parseInt(otherSettings.getProperty("EnchantMaxArmor", "255"));
		ENCHANT_MAX_JEWELRY = Integer.parseInt(otherSettings.getProperty("EnchantMaxJewelry", "255"));
		/* limit of safe enchant normal */
		ENCHANT_SAFE_MAX = Integer.parseInt(otherSettings.getProperty("EnchantSafeMax", "3"));
		/* limit of safe enchant full */
		ENCHANT_SAFE_MAX_FULL = Integer.parseInt(otherSettings.getProperty("EnchantSafeMaxFull", "4"));
		/*
		 * if different from 100 (ie 100%) heal rate is modified
		 * acordingly
		 */
		HP_REGEN_MULTIPLIER = Double.parseDouble(otherSettings.getProperty("HpRegenMultiplier", "100")) / 100;
		MP_REGEN_MULTIPLIER = Double.parseDouble(otherSettings.getProperty("MpRegenMultiplier", "100")) / 100;
		CP_REGEN_MULTIPLIER = Double.parseDouble(otherSettings.getProperty("CpRegenMultiplier", "100")) / 100;
		RAID_HP_REGEN_MULTIPLIER = Double.parseDouble(otherSettings.getProperty("RaidHpRegenMultiplier", "100")) / 100;
		RAID_MP_REGEN_MULTIPLIER = Double.parseDouble(otherSettings.getProperty("RaidMpRegenMultiplier", "100")) / 100;
		RAID_DEFENCE_MULTIPLIER = Double.parseDouble(otherSettings.getProperty("RaidDefenceMultiplier", "100")) / 100;
		RAID_MINION_RESPAWN_TIMER = Integer.parseInt(otherSettings.getProperty("RaidMinionRespawnTime", "300000"));
		RAID_MIN_RESPAWN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidMinRespawnMultiplier", "1.0"));
		RAID_MAX_RESPAWN_MULTIPLIER = Float.parseFloat(otherSettings.getProperty("RaidMaxRespawnMultiplier", "1.0"));
		STARTING_ADENA = Integer.parseInt(otherSettings.getProperty("StartingAdena", "100"));
		UNSTUCK_INTERVAL = Integer.parseInt(otherSettings.getProperty("UnstuckInterval", "300"));
		/* Player protection after teleport or login */
		PLAYER_SPAWN_PROTECTION = Integer.parseInt(otherSettings.getProperty("PlayerSpawnProtection", "0"));
		/*
		 * Player protection after recovering from fake death (works
		 * against mobs only)
		 */
		PLAYER_FAKEDEATH_UP_PROTECTION = Integer.parseInt(otherSettings.getProperty("PlayerFakeDeathUpProtection", "0"));
		/* Defines some Party XP related values */
		PARTY_XP_CUTOFF_METHOD = otherSettings.getProperty("PartyXpCutoffMethod", "percentage");
		PARTY_XP_CUTOFF_PERCENT = Double.parseDouble(otherSettings.getProperty("PartyXpCutoffPercent", "3."));
		PARTY_XP_CUTOFF_LEVEL = Integer.parseInt(otherSettings.getProperty("PartyXpCutoffLevel", "30"));
		/* Amount of HP, MP, and CP is restored */
		RESPAWN_RESTORE_CP = Double.parseDouble(otherSettings.getProperty("RespawnRestoreCP", "0")) / 100;
		RESPAWN_RESTORE_HP = Double.parseDouble(otherSettings.getProperty("RespawnRestoreHP", "70")) / 100;
		RESPAWN_RESTORE_MP = Double.parseDouble(otherSettings.getProperty("RespawnRestoreMP", "70")) / 100;
		RESPAWN_RANDOM_ENABLED = Boolean.parseBoolean(otherSettings.getProperty("RespawnRandomInTown", "False"));
		RESPAWN_RANDOM_MAX_OFFSET = Integer.parseInt(otherSettings.getProperty("RespawnRandomMaxOffset", "50"));
		/* Maximum number of available slots for pvt stores */
		MAX_PVTSTORE_SLOTS_DWARF = Integer.parseInt(otherSettings.getProperty("MaxPvtStoreSlotsDwarf", "5"));
		MAX_PVTSTORE_SLOTS_OTHER = Integer.parseInt(otherSettings.getProperty("MaxPvtStoreSlotsOther", "4"));
		STORE_SKILL_COOLTIME = Boolean.parseBoolean(otherSettings.getProperty("StoreSkillCooltime", "True"));
		PET_RENT_NPC = otherSettings.getProperty("ListPetRentNpc", "30827");
		LIST_PET_RENT_NPC = new FastList<Integer>();
		for (String id : PET_RENT_NPC.split(","))
		{
		    LIST_PET_RENT_NPC.add(Integer.parseInt(id));
		}
		NONDROPPABLE_ITEMS = otherSettings.getProperty("ListOfNonDroppableItems", "1147,425,1146,461,10,2368,7,6,2370,2369,5598");
		LIST_NONDROPPABLE_ITEMS = new FastList<Integer>();
		for (String id : NONDROPPABLE_ITEMS.split(","))
		{
		    LIST_NONDROPPABLE_ITEMS.add(Integer.parseInt(id));
		}
		ANNOUNCE_MAMMON_SPAWN = Boolean.parseBoolean(otherSettings.getProperty("AnnounceMammonSpawn", "True"));
		ALT_PRIVILEGES_ADMIN = Boolean.parseBoolean(otherSettings.getProperty("AltPrivilegesAdmin", "False"));
		ALT_PRIVILEGES_SECURE_CHECK = Boolean.parseBoolean(otherSettings.getProperty("AltPrivilegesSecureCheck", "True"));
		ALT_PRIVILEGES_DEFAULT_LEVEL = Integer.parseInt(otherSettings.getProperty("AltPrivilegesDefaultLevel", "100"));
		GM_NAME_COLOR_ENABLED = Boolean.parseBoolean(otherSettings.getProperty("GMNameColorEnabled", "False"));
		GM_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("GMNameColor", "FFFF00"));
		ADMIN_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("AdminNameColor", "00FF00"));
		GM_HERO_AURA = Boolean.parseBoolean(otherSettings.getProperty("GMHeroAura", "True"));
		GM_STARTUP_INVULNERABLE = Boolean.parseBoolean(otherSettings.getProperty("GMStartupInvulnerable", "True"));
		GM_STARTUP_INVISIBLE = Boolean.parseBoolean(otherSettings.getProperty("GMStartupInvisible", "True"));
			MASTERACCESS_LEVEL = Integer.parseInt(otherSettings.getProperty("MasterAccessLevel", "1"));
			MASTERACCESS_NAME_COLOR = Integer.decode("0x" + otherSettings.getProperty("MasterNameColor", "00FF00"));
			MASTERACCESS_TITLE_COLOR = Integer.decode("0x" + otherSettings.getProperty("MasterTitleColor", "00FF00"));
		GM_STARTUP_SILENCE = Boolean.parseBoolean(otherSettings.getProperty("GMStartupSilence", "True"));
		GM_STARTUP_AUTO_LIST = Boolean.parseBoolean(otherSettings.getProperty("GMStartupAutoList", "True"));
		PETITIONING_ALLOWED = Boolean.parseBoolean(otherSettings.getProperty("PetitioningAllowed", "True"));
		MAX_PETITIONS_PER_PLAYER = Integer.parseInt(otherSettings.getProperty("MaxPetitionsPerPlayer", "5"));
		MAX_PETITIONS_PENDING = Integer.parseInt(otherSettings.getProperty("MaxPetitionsPending", "25"));
		JAIL_IS_PVP = Boolean.valueOf(otherSettings.getProperty("JailIsPvp", "True"));
		JAIL_DISABLE_CHAT = Boolean.valueOf(otherSettings.getProperty("JailDisableChat", "True"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + OTHER_CONFIG_FILE + " File.");
	    }
	    // rates
	    try
	    {
		Properties ratesSettings = new Properties();
		InputStream is = new FileInputStream(new File(RATES_CONFIG_FILE));
		ratesSettings.load(is);
		is.close();
		RATE_XP = Float.parseFloat(ratesSettings.getProperty("RateXp", "1."));
		RATE_SP = Float.parseFloat(ratesSettings.getProperty("RateSp", "1."));
		RATE_PARTY_XP = Float.parseFloat(ratesSettings.getProperty("RatePartyXp", "1."));
		RATE_PARTY_SP = Float.parseFloat(ratesSettings.getProperty("RatePartySp", "1."));
		RATE_QUESTS_REWARD = Float.parseFloat(ratesSettings.getProperty("RateQuestsReward", "1."));
		RATE_DROP_ADENA = Float.parseFloat(ratesSettings.getProperty("RateDropAdena", "1."));
		RATE_CONSUMABLE_COST = Float.parseFloat(ratesSettings.getProperty("RateConsumableCost", "1."));
		RATE_DROP_ITEMS = Float.parseFloat(ratesSettings.getProperty("RateDropItems", "1."));
		RATE_DROP_SPOIL = Float.parseFloat(ratesSettings.getProperty("RateDropSpoil", "1."));
		RATE_DROP_MANOR = Integer.parseInt(ratesSettings.getProperty("RateDropManor", "1"));
		// ADENA BOSS
		ADENA_BOSS = Float.parseFloat(ratesSettings.getProperty("AdenaBoss", "1."));
		ADENA_RAID = Float.parseFloat(ratesSettings.getProperty("AdenaRaid", "1."));
		ADENA_MINON = Float.parseFloat(ratesSettings.getProperty("AdenaMinon", "1."));
		// ITEMS BOSS
		ITEMS_BOSS = Float.parseFloat(ratesSettings.getProperty("ItemsBoss", "1."));
		ITEMS_RAID = Float.parseFloat(ratesSettings.getProperty("ItemsRaid", "1."));
		ITEMS_MINON = Float.parseFloat(ratesSettings.getProperty("ItemsMinon", "1."));
		// SPOIL BOSS
		SPOIL_BOSS = Float.parseFloat(ratesSettings.getProperty("SpoilBoss", "1."));
		SPOIL_RAID = Float.parseFloat(ratesSettings.getProperty("SpoilRaid", "1."));
		SPOIL_MINON = Float.parseFloat(ratesSettings.getProperty("SpoilMinon", "1."));
		RATE_DROP_QUEST = Float.parseFloat(ratesSettings.getProperty("RateDropQuest", "1."));
		RATE_KARMA_EXP_LOST = Float.parseFloat(ratesSettings.getProperty("RateKarmaExpLost", "1."));
		RATE_SIEGE_GUARDS_PRICE = Float.parseFloat(ratesSettings.getProperty("RateSiegeGuardsPrice", "1."));
		RATE_DROP_COMMON_HERBS = Float.parseFloat(ratesSettings.getProperty("RateCommonHerbs", "15."));
		RATE_DROP_MP_HP_HERBS = Float.parseFloat(ratesSettings.getProperty("RateHpMpHerbs", "10."));
		RATE_DROP_GREATER_HERBS = Float.parseFloat(ratesSettings.getProperty("RateGreaterHerbs", "4."));
		RATE_DROP_SUPERIOR_HERBS = Float.parseFloat(ratesSettings.getProperty("RateSuperiorHerbs", "0.8")) * 10;
		RATE_DROP_SPECIAL_HERBS = Float.parseFloat(ratesSettings.getProperty("RateSpecialHerbs", "0.2")) * 10;
		PLAYER_DROP_LIMIT = Integer.parseInt(ratesSettings.getProperty("PlayerDropLimit", "3"));
		PLAYER_RATE_DROP = Integer.parseInt(ratesSettings.getProperty("PlayerRateDrop", "5"));
		PLAYER_RATE_DROP_ITEM = Integer.parseInt(ratesSettings.getProperty("PlayerRateDropItem", "70"));
		PLAYER_RATE_DROP_EQUIP = Integer.parseInt(ratesSettings.getProperty("PlayerRateDropEquip", "25"));
		PLAYER_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(ratesSettings.getProperty("PlayerRateDropEquipWeapon", "5"));
		PET_XP_RATE = Float.parseFloat(ratesSettings.getProperty("PetXpRate", "1."));
		PET_FOOD_RATE = Integer.parseInt(ratesSettings.getProperty("PetFoodRate", "1"));
		SINEATER_XP_RATE = Float.parseFloat(ratesSettings.getProperty("SinEaterXpRate", "1."));
		KARMA_DROP_LIMIT = Integer.parseInt(ratesSettings.getProperty("KarmaDropLimit", "10"));
		KARMA_RATE_DROP = Integer.parseInt(ratesSettings.getProperty("KarmaRateDrop", "70"));
		KARMA_RATE_DROP_ITEM = Integer.parseInt(ratesSettings.getProperty("KarmaRateDropItem", "50"));
		KARMA_RATE_DROP_EQUIP = Integer.parseInt(ratesSettings.getProperty("KarmaRateDropEquip", "40"));
		KARMA_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(ratesSettings.getProperty("KarmaRateDropEquipWeapon", "10"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + RATES_CONFIG_FILE + " File.");
	    }
	    // alternative settings
	    try
	    {
		Properties altSettings = new Properties();
		InputStream is = new FileInputStream(new File(ALT_SETTINGS_FILE));
		altSettings.load(is);
		is.close();
		ALT_GAME_TIREDNESS = Boolean.parseBoolean(altSettings.getProperty("AltGameTiredness", "False"));
        SOUL_CRYSTAL_BREAK_CHANCE = Integer.parseInt(altSettings.getProperty("SoulCrystalBreakChance","10"));   
        SOUL_CRYSTAL_LEVEL_CHANCE = Integer.parseInt(altSettings.getProperty("SoulCrystalLevelChance", "32"));   
		ALT_GAME_CREATION = Boolean.parseBoolean(altSettings.getProperty("AltGameCreation", "False"));
		ALT_GAME_CREATION_SPEED = Double.parseDouble(altSettings.getProperty("AltGameCreationSpeed", "1"));
		ALT_GAME_CREATION_XP_RATE = Double.parseDouble(altSettings.getProperty("AltGameCreationRateXp", "1"));
		ALT_GAME_CREATION_SP_RATE = Double.parseDouble(altSettings.getProperty("AltGameCreationRateSp", "1"));
		ALT_BLACKSMITH_USE_RECIPES = Boolean.parseBoolean(altSettings.getProperty("AltBlacksmithUseRecipes", "True"));
        ALT_MAGES_PHYSICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltPDamageMages", "1.00")); 
        ALT_MAGES_MAGICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltMDamageMages", "1.00")); 
        ALT_FIGHTERS_PHYSICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltPDamageFighters", "1.00")); 
        ALT_FIGHTERS_MAGICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltMDamageFighters", "1.00")); 
        ALT_PETS_PHYSICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltPDamagePets", "1.00")); 
        ALT_PETS_MAGICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltMDamagePets", "1.00")); 
        ALT_NPC_PHYSICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltPDamageNpc", "1.00")); 
        ALT_NPC_MAGICAL_DAMAGE_MULTI = Float.parseFloat(altSettings.getProperty("AltMDamageNpc", "1.00")); 
		ALT_GAME_SKILL_LEARN = Boolean.parseBoolean(altSettings.getProperty("AltGameSkillLearn", "False"));
		AUTO_LEARN_SKILLS = Boolean.parseBoolean(altSettings.getProperty("AutoLearnSkills", "False"));
		ALT_GAME_CANCEL_BOW = altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("bow") || altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("all");
		ALT_GAME_CANCEL_CAST = altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("cast") || altSettings.getProperty("AltGameCancelByHit", "Cast").equalsIgnoreCase("all");
		ALT_GAME_SHIELD_BLOCKS = Boolean.parseBoolean(altSettings.getProperty("AltShieldBlocks", "False"));
		ALT_GAME_DELEVEL = Boolean.parseBoolean(altSettings.getProperty("Delevel", "True"));
		ALT_GAME_MAGICFAILURES = Boolean.parseBoolean(altSettings.getProperty("MagicFailures", "False"));
		ALT_GAME_MOB_ATTACK_AI = Boolean.parseBoolean(altSettings.getProperty("AltGameMobAttackAI", "False"));
		ALT_MOB_AGRO_IN_PEACEZONE = Boolean.parseBoolean(altSettings.getProperty("AltMobAgroInPeaceZone", "True"));
		ALT_GAME_EXPONENT_XP = Float.parseFloat(altSettings.getProperty("AltGameExponentXp", "0."));
		ALT_GAME_EXPONENT_SP = Float.parseFloat(altSettings.getProperty("AltGameExponentSp", "0."));
		ALLOW_CLASS_MASTERS = Boolean.valueOf(altSettings.getProperty("AllowClassMasters", "False"));
		ALLOW_CLASS_FIRST = Boolean.valueOf(altSettings.getProperty("AllowFirstClass", "False"));
		ALLOW_CLASS_SECOND = Boolean.valueOf(altSettings.getProperty("AllowSecondClass", "False"));
		ALLOW_CLASS_THIRD = Boolean.valueOf(altSettings.getProperty("AllowThirdClass", "False"));
		ALT_GAME_FREIGHTS = Boolean.parseBoolean(altSettings.getProperty("AltGameFreights", "False"));
		ALT_GAME_FREIGHT_PRICE = Integer.parseInt(altSettings.getProperty("AltGameFreightPrice", "1000"));
		ALT_PARTY_RANGE = Integer.parseInt(altSettings.getProperty("AltPartyRange", "1600"));
		ALT_PARTY_RANGE2 = Integer.parseInt(altSettings.getProperty("AltPartyRange2", "1400"));
		IS_CRAFTING_ENABLED = Boolean.parseBoolean(altSettings.getProperty("CraftingEnabled", "True"));
		AUTO_LOOT = Boolean.valueOf(altSettings.getProperty("AutoLoot", "True"));
		AUTO_LOOT_HERBS = Boolean.valueOf(altSettings.getProperty("AutoLootHerbs", "True"));
		ALT_GAME_KARMA_PLAYER_CAN_BE_KILLED_IN_PEACEZONE = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanBeKilledInPeaceZone", "False"));
		ALT_GAME_KARMA_PLAYER_CAN_SHOP = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanShop", "True"));
		ALT_GAME_KARMA_PLAYER_CAN_USE_GK = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanUseGK", "False"));
		ALT_GAME_KARMA_PLAYER_CAN_TELEPORT = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanTeleport", "True"));
		ALT_GAME_KARMA_PLAYER_CAN_TRADE = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanTrade", "True"));
		ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE = Boolean.valueOf(altSettings.getProperty("AltKarmaPlayerCanUseWareHouse", "True"));
		ALT_GAME_FREE_TELEPORT = Boolean.parseBoolean(altSettings.getProperty("AltFreeTeleporting", "False"));
		ALT_RECOMMEND = Boolean.parseBoolean(altSettings.getProperty("AltRecommend", "False"));
		ALT_GAME_SUBCLASS_WITHOUT_QUESTS = Boolean.parseBoolean(altSettings.getProperty("AltSubClassWithoutQuests", "False"));
        RESTORE_EFFECTS_ON_SUBCLASS_CHANGE = Boolean.parseBoolean(altSettings.getProperty("RestoreEffectsOnSubClassChange", "False"));
		ALT_GAME_NEW_CHAR_ALWAYS_IS_NEWBIE = Boolean.parseBoolean(altSettings.getProperty("AltNewCharAlwaysIsNewbie", "False"));
		ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH = Boolean.parseBoolean(altSettings.getProperty("AltMembersCanWithdrawFromClanWH", "False"));
		ALT_MAX_NUM_OF_CLANS_IN_ALLY = Integer.parseInt(altSettings.getProperty("AltMaxNumOfClansInAlly", "3"));
		DWARF_RECIPE_LIMIT = Integer.parseInt(altSettings.getProperty("DwarfRecipeLimit", "50"));
		COMMON_RECIPE_LIMIT = Integer.parseInt(altSettings.getProperty("CommonRecipeLimit", "50"));
		ALT_CLAN_MEMBERS_FOR_WAR = Integer.parseInt(altSettings.getProperty("AltClanMembersForWar", "15"));
		ALT_CLAN_JOIN_DAYS = Integer.parseInt(altSettings.getProperty("DaysBeforeJoinAClan", "5"));
		ALT_CLAN_CREATE_DAYS = Integer.parseInt(altSettings.getProperty("DaysBeforeCreateAClan", "10"));
		ALT_CLAN_DISSOLVE_DAYS = Integer.parseInt(altSettings.getProperty("DaysToPassToDissolveAClan", "7"));
		ALT_ALLY_JOIN_DAYS_WHEN_LEAVED = Integer.parseInt(altSettings.getProperty("DaysBeforeJoinAllyWhenLeaved", "1"));
		ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED = Integer.parseInt(altSettings.getProperty("DaysBeforeJoinAllyWhenDismissed", "1"));
		ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED = Integer.parseInt(altSettings.getProperty("DaysBeforeAcceptNewClanWhenDismissed", "1"));
		ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED = Integer.parseInt(altSettings.getProperty("DaysBeforeCreateNewAllyWhenDissolved", "10"));
		ALT_OLY_START_TIME = Integer.parseInt(altSettings.getProperty("AltOlyStartTime", "18"));
		ALT_OLY_MIN = Integer.parseInt(altSettings.getProperty("AltOlyMin", "00"));
		ALT_OLY_CPERIOD = Long.parseLong(altSettings.getProperty("AltOlyCPeriod", "21600")) * 1000;
		ALT_OLY_BATTLE = Long.parseLong(altSettings.getProperty("AltOlyBattle", "360")) * 1000;
		ALT_OLY_BWAIT = Long.parseLong(altSettings.getProperty("AltOlyBWait", "600")) * 1000;
		ALT_OLY_IWAIT = Long.parseLong(altSettings.getProperty("AltOlyIWait", "300")) * 1000;
		ALT_OLY_WPERIOD = Long.parseLong(altSettings.getProperty("AltOlyWPeriod", "604800")) * 1000;
		ALT_OLY_VPERIOD = Long.parseLong(altSettings.getProperty("AltOlyVPeriod", "86400")) * 1000;
		ALT_LOTTERY_PRIZE = Integer.parseInt(altSettings.getProperty("AltLotteryPrize", "50000"));
		ALT_LOTTERY_TICKET_PRICE = Integer.parseInt(altSettings.getProperty("AltLotteryTicketPrice", "2000"));
		ALT_LOTTERY_5_NUMBER_RATE = Float.parseFloat(altSettings.getProperty("AltLottery5NumberRate", "0.6"));
		ALT_LOTTERY_4_NUMBER_RATE = Float.parseFloat(altSettings.getProperty("AltLottery4NumberRate", "0.2"));
		ALT_LOTTERY_3_NUMBER_RATE = Float.parseFloat(altSettings.getProperty("AltLottery3NumberRate", "0.2"));
		ALT_LOTTERY_2_AND_1_NUMBER_PRIZE = Integer.parseInt(altSettings.getProperty("AltLottery2and1NumberPrize", "200"));
		BUFFS_MAX_AMOUNT = Byte.parseByte(altSettings.getProperty("maxbuffamount", "24"));
		ALT_DEV_NO_QUESTS = Boolean.parseBoolean(altSettings.getProperty("AltDevNoQuests", "False"));
		ALT_DEV_NO_SPAWNS = Boolean.parseBoolean(altSettings.getProperty("AltDevNoSpawns", "False"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + ALT_SETTINGS_FILE + " File.");
	    }
	    /** ************************************************** */
	    /** L2J Oneo Mods Properties File -Begin * */
	    /** ************************************************** */
	    try
	    {
		Properties L2JOneoEventMods = new Properties();
		InputStream is = new FileInputStream(new File(L2JONEO_MODS));
		L2JOneoEventMods.load(is);
		is.close();
		// ********************//
		/* Event Automation */
		// ********************//
		TIME_BETWEEN_EVENTS = Integer.parseInt(L2JOneoEventMods.getProperty("TimeInBetweenEvents", "60"));
		// ********************//
		/* TvT Event Engine */
		// ********************//
		TVT_EVENT_ENABLED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventEnabled", "False"));
		TVT_EVENT_PARTICIPATION_TIME = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventParticipationTime", "3600"));
		TVT_EVENT_RUNNING_TIME = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventRunningTime", "1800"));
		TVT_EVENT_PARTICIPATION_NPC_ID = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventParticipationNpcId", "0"));
		if (TVT_EVENT_PARTICIPATION_NPC_ID == 0)
		{
		    TVT_EVENT_ENABLED = false;
		    System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventParticipationNpcId");
		} else
		{
		    String[] propertySplit = L2JOneoEventMods.getProperty("TvTEventParticipationNpcCoordinates", "0,0,0").split(",");
		    if (propertySplit.length < 3)
		    {
			TVT_EVENT_ENABLED = false;
			System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventParticipationNpcCoordinates");
		    } else
		    {
			TVT_EVENT_PARTICIPATION_NPC_COORDINATES[0] = Integer.parseInt(propertySplit[0]);
			TVT_EVENT_PARTICIPATION_NPC_COORDINATES[1] = Integer.parseInt(propertySplit[1]);
			TVT_EVENT_PARTICIPATION_NPC_COORDINATES[2] = Integer.parseInt(propertySplit[2]);
			TVT_EVENT_MIN_PLAYERS_IN_TEAMS = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventMinPlayersInTeams", "1"));
			TVT_EVENT_MAX_PLAYERS_IN_TEAMS = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventMaxPlayersInTeams", "20"));
			TVT_EVENT_MIN_LVL = (byte) Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventMinPlayerLevel", "1"));
			TVT_EVENT_MAX_LVL = (byte) Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventMaxPlayerLevel", "80"));
			TVT_EVENT_RESPAWN_TELEPORT_DELAY = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventRespawnTeleportDelay", "20"));
			TVT_EVENT_START_LEAVE_TELEPORT_DELAY = Integer.parseInt(L2JOneoEventMods.getProperty("TvTEventStartLeaveTeleportDelay", "20"));
			TVT_EVENT_TEAM_1_NAME = L2JOneoEventMods.getProperty("TvTEventTeam1Name", "Team1");
			propertySplit = L2JOneoEventMods.getProperty("TvTEventTeam1Coordinates", "0,0,0").split(",");
			if (propertySplit.length < 3)
			{
			    TVT_EVENT_ENABLED = false;
			    System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventTeam1Coordinates");
			} else
			{
			    TVT_EVENT_TEAM_1_COORDINATES[0] = Integer.parseInt(propertySplit[0]);
			    TVT_EVENT_TEAM_1_COORDINATES[1] = Integer.parseInt(propertySplit[1]);
			    TVT_EVENT_TEAM_1_COORDINATES[2] = Integer.parseInt(propertySplit[2]);
			    TVT_EVENT_TEAM_2_NAME = L2JOneoEventMods.getProperty("TvTEventTeam2Name", "Team2");
			    propertySplit = L2JOneoEventMods.getProperty("TvTEventTeam2Coordinates", "0,0,0").split(",");
			    if (propertySplit.length < 3)
			    {
				TVT_EVENT_ENABLED = false;
				System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventTeam2Coordinates");
			    } else
			    {
				TVT_EVENT_TEAM_2_COORDINATES[0] = Integer.parseInt(propertySplit[0]);
				TVT_EVENT_TEAM_2_COORDINATES[1] = Integer.parseInt(propertySplit[1]);
				TVT_EVENT_TEAM_2_COORDINATES[2] = Integer.parseInt(propertySplit[2]);
				propertySplit = L2JOneoEventMods.getProperty("TvTEventReward", "57,100000").split(";");
				for (String reward : propertySplit)
				{
				    String[] rewardSplit = reward.split(",");
				    if (rewardSplit.length != 2)
				    {
					System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventReward \"" + reward + "\"");
				    } else
				    {
					try
					{
					    TVT_EVENT_REWARDS.add(new int[] { Integer.valueOf(rewardSplit[0]), Integer.valueOf(rewardSplit[1]) });
					} catch (NumberFormatException nfe)
					{
					    if (!reward.equals(""))
					    {
						System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventReward \"" + reward + "\"");
					    }
					}
				    }
				}
				TVT_EVENT_TARGET_TEAM_MEMBERS_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventTargetTeamMembersAllowed", "True"));
				TVT_EVENT_POTIONS_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventPotionsAllowed", "False"));
				TVT_EVENT_POTIONS_MP_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventPotionsMPAllowed", "False"));
				TVT_EVENT_POTIONS_HP_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventPotionsHPAllowed", "False"));
				TVT_EVENT_POTIONS_CP_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventPotionsCPAllowed", "False"));
				TVT_EVENT_SUMMON_BY_ITEM_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("TvTEventSummonByItemAllowed", "False"));
				propertySplit = L2JOneoEventMods.getProperty("TvTEventDoorsCloseOpenOnStartEnd", "").split(";");
				for (String door : propertySplit)
				{
				    try
				    {
					TVT_EVENT_DOOR_IDS.add(Integer.valueOf(door));
				    } catch (NumberFormatException nfe)
				    {
					if (!door.equals(""))
					{
					    System.out.println("TvTEventEngine[Config.load()]: invalid config property -> TvTEventDoorsCloseOpenOnStartEnd \"" + door + "\"");
					}
				    }
				}
			    }
			}
		    }
		}
		// ********************//
		/* VIP Event Engine */
		// ********************//
		VIP_EVENT_ENABLED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("VIPEventEnabled", "False"));
		VIP_FORCE_SIT = Boolean.parseBoolean(L2JOneoEventMods.getProperty("VIPForceSit", "True"));
		MIN_VIP_PLAYERS = Integer.parseInt(L2JOneoEventMods.getProperty("MinVIPPlayers", "1"));
		MIN_NON_VIP_PLAYERS = Integer.parseInt(L2JOneoEventMods.getProperty("MinNonVIPPlayers", "1"));
		VIP_EVENT_POTIONS_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("VIPEventPotionsAllowed", "False"));
		VIP_EVENT_SUMMON_BY_ITEM_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("VIPEventSummonByItemAllowed", "False"));
		VIP_ENDTP_DESC = L2JOneoEventMods.getProperty("EndTPDescription", "Giran");
		if (Config.VIP_EVENT_ENABLED)
		{
		    String[] propertySplit = L2JOneoEventMods.getProperty("VIPReward", "57,1000").split(";");
		    for (String vipreward : propertySplit)
		    {
			String[] viprewardSplit = vipreward.split(",");
			if (viprewardSplit.length != 2)
			{
			    VIP_EVENT_ENABLED = false;
			    System.out.println("VIPEventEngine[Config.load()]: invalid config property -> VIP Reward \"" + vipreward + "\"");
			} else
			{
			    try
			    {
				VIP_REWARD.add(new int[] { Integer.valueOf(viprewardSplit[0]), Integer.valueOf(viprewardSplit[1]) });
			    } catch (NumberFormatException nfe)
			    {
				if (!vipreward.equals(""))
				{
				    VIP_EVENT_ENABLED = false;
				    System.out.println("VIPEventEngine[Config.load()]: invalid config property -> VIP Reward \"" + vipreward + "\"");
				}
			    }
			}
		    }
		}
		if (Config.VIP_EVENT_ENABLED)
		{
		    String[] propertySplit = L2JOneoEventMods.getProperty("VIPTeamReward", "57,1000").split(";");
		    for (String VIPTeamReward : propertySplit)
		    {
			String[] VIPTeamRewardSplit = VIPTeamReward.split(",");
			if (VIPTeamRewardSplit.length != 2)
			{
			    VIP_EVENT_ENABLED = false;
			    System.out.println("VIPEventEngine[Config.load()]: invalid config property -> VIP Reward \"" + VIPTeamReward + "\"");
			} else
			{
			    try
			    {
				VIP_REWARD.add(new int[] { Integer.valueOf(VIPTeamRewardSplit[0]), Integer.valueOf(VIPTeamRewardSplit[1]) });
			    } catch (NumberFormatException nfe)
			    {
				if (!VIPTeamReward.equals(""))
				{
				    VIP_EVENT_ENABLED = false;
				    System.out.println("VIPEventEngine[Config.load()]: invalid config property -> VIP Reward \"" + VIPTeamReward + "\"");
				}
			    }
			}
		    }
		}
		if (Config.VIP_EVENT_ENABLED)
		{
		    String[] propertySplit = L2JOneoEventMods.getProperty("NonVIPReward", "57,1000").split(";");
		    for (String nonvipreward : propertySplit)
		    {
			String[] nonviprewardSplit = nonvipreward.split(",");
			if (nonviprewardSplit.length != 2)
			{
			    VIP_EVENT_ENABLED = false;
			    System.out.println("VIPEventEngine[Config.load()]: invalid config property -> VIP Reward \"" + nonvipreward + "\"");
			} else
			{
			    try
			    {
				VIP_REWARD.add(new int[] { Integer.valueOf(nonviprewardSplit[0]), Integer.valueOf(nonviprewardSplit[1]) });
			    } catch (NumberFormatException nfe)
			    {
				if (!nonvipreward.equals(""))
				{
				    VIP_EVENT_ENABLED = false;
				    System.out.println("VIPEventEngine[Config.load()]: invalid config property -> VIP Reward \"" + nonvipreward + "\"");
				}
			    }
			}
		    }
		}
		VIP_FINISH_NPC_ID = Integer.parseInt(L2JOneoEventMods.getProperty("VIPNPCID", "50084"));
		VIP_TP_LOC_X = Integer.parseInt(L2JOneoEventMods.getProperty("VIPTPLocX", "83427"));
		VIP_TP_LOC_Y = Integer.parseInt(L2JOneoEventMods.getProperty("VIPTPLocY", "148435"));
		VIP_TP_LOC_Z = Integer.parseInt(L2JOneoEventMods.getProperty("VIPTPLocZ", "-3404"));
		VIP_PARTICIPATION_TO_START_DELAY = Integer.parseInt(L2JOneoEventMods.getProperty("VIPParticipationToStartDelay", "20"));
		VIP_LASTING_TIME = Integer.parseInt(L2JOneoEventMods.getProperty("VIPEventTime", "20"));
		VIP_SIT_TIME = Integer.parseInt(L2JOneoEventMods.getProperty("VIPSitTime", "20"));
		VIP_TELEPORT_TIME = Integer.parseInt(L2JOneoEventMods.getProperty("VIPFinishTeleportTime", "20"));
		VIP_TIME_BEFORE_TELEPORT = Integer.parseInt(L2JOneoEventMods.getProperty("VIPStartTeleportTime", "20"));
		VIP_EVENT_POTIONS_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("VIPEventPotionsAllowed", "False"));
		VIP_EVENT_SUMMON_BY_ITEM_ALLOWED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("VIPEventSummonByItemAllowed", "False"));
		// ********************//
		/* CTF Event Engine */
		// ********************//
		CTF_EVENT_ENABLED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("CTFEventEnabled", "False"));
		CTF_ALLOW_INTERFERENCE = Boolean.parseBoolean(L2JOneoEventMods.getProperty("CTFAllowInterference", "False"));
		CTF_ALLOW_POTIONS = Boolean.parseBoolean(L2JOneoEventMods.getProperty("CTFAllowPotions", "False"));
		CTF_ALLOW_SUMMON = Boolean.parseBoolean(L2JOneoEventMods.getProperty("CTFAllowSummon", "False"));
		CTF_ON_START_REMOVE_ALL_EFFECTS = Boolean.parseBoolean(L2JOneoEventMods.getProperty("CTFOnStartRemoveAllEffects", "True"));
		CTF_ON_START_UNSUMMON_PET = Boolean.parseBoolean(L2JOneoEventMods.getProperty("CTFOnStartUnsummonPet", "True"));
		CTF_TEAM_NAME_1 = L2JOneoEventMods.getProperty("CTFTeamName1", "Heaven");
		CTF_TEAM_NAME_2 = L2JOneoEventMods.getProperty("CTFTeamName2", "Hell");
		CTF_TEAM_FLAG_ID_1 = Integer.parseInt(L2JOneoEventMods.getProperty("CTFTeamFlagId1", "20001"));
		CTF_TEAM_COLOR_1 = Integer.decode("0x" + L2JOneoEventMods.getProperty("CTFTeamColor1", "0000FF"));
		CTF_TEAM_FLAG_ID_2 = Integer.parseInt(L2JOneoEventMods.getProperty("CTFTeamFlagId2", "20001"));
		CTF_TEAM_COLOR_1 = Integer.decode("0x" + L2JOneoEventMods.getProperty("CTFTeamColor2", "FF0000"));
		CTF_MIN_PLAYERS = Integer.parseInt(L2JOneoEventMods.getProperty("CTFMinplayers", "10"));
		if (true)
		{
		    String[] propertySplit = L2JOneoEventMods.getProperty("CTFTeamCords1", "0,0,0").split(",");
		    if (propertySplit.length < 3)
		    {
			CTF_EVENT_ENABLED = false;
			System.out.println("CTF[Config.load()]: invalid config property -> CTFTeamCoordinates for Team 1.");
		    } else
		    {
			CTF_TEAM_CORDS_1[0] = Integer.parseInt(propertySplit[0]);
			CTF_TEAM_CORDS_1[1] = Integer.parseInt(propertySplit[1]);
			CTF_TEAM_CORDS_1[2] = Integer.parseInt(propertySplit[2]);
		    }
		}
		if (true)
		{
		    String[] propertySplit = L2JOneoEventMods.getProperty("CTFTeamCords2", "0,0,0").split(",");
		    if (propertySplit.length < 3)
		    {
			CTF_EVENT_ENABLED = false;
			System.out.println("CTF[Config.load()]: invalid config property -> CTFTeamCoordinates for Team 2.");
		    } else
		    {
			CTF_TEAM_CORDS_2[0] = Integer.parseInt(propertySplit[0]);
			CTF_TEAM_CORDS_2[1] = Integer.parseInt(propertySplit[1]);
			CTF_TEAM_CORDS_2[2] = Integer.parseInt(propertySplit[2]);
		    }
		}
		// ********************//
		/* RAID Event Engine */
		// ********************//
		RAID_SYSTEM_ENABLED = Boolean.parseBoolean(L2JOneoEventMods.getProperty("RaidEnginesEnabled", "False"));
		RAID_SYSTEM_GIVE_BUFFS = Boolean.parseBoolean(L2JOneoEventMods.getProperty("RaidGiveBuffs", "True"));
		RAID_SYSTEM_RESURRECT_PLAYER = Boolean.parseBoolean(L2JOneoEventMods.getProperty("RaidResurrectPlayer", "True"));
		RAID_SYSTEM_MAX_EVENTS = Integer.parseInt(L2JOneoEventMods.getProperty("RaidMaxNumEvents", "3"));
		RAID_SYSTEM_FIGHT_TIME = Integer.parseInt(L2JOneoEventMods.getProperty("RaidSystemFightTime", "60"));
		if (RAID_SYSTEM_MAX_EVENTS == 0)
		{
		    RAID_SYSTEM_ENABLED = false;
		    System.out.println("Raid Engine[Config.load()]: Invalid config property: Max Events = 0?!");
		}
		// ********************//
		/* Wedding System */
		// ********************//
		ALLOW_WEDDING = Boolean.valueOf(L2JOneoEventMods.getProperty("AllowWedding", "True"));
		WEDDING_PRICE = Integer.parseInt(L2JOneoEventMods.getProperty("WeddingPrice", "500000"));
		WEDDING_PUNISH_INFIDELITY = Boolean.parseBoolean(L2JOneoEventMods.getProperty("WeddingPunishInfidelity", "True"));
		WEDDING_TELEPORT = Boolean.parseBoolean(L2JOneoEventMods.getProperty("WeddingTeleport", "True"));
		WEDDING_TELEPORT_PRICE = Integer.parseInt(L2JOneoEventMods.getProperty("WeddingTeleportPrice", "500000"));
		WEDDING_TELEPORT_INTERVAL = Integer.parseInt(L2JOneoEventMods.getProperty("WeddingTeleportInterval", "120"));
		WEDDING_SAMESEX = Boolean.parseBoolean(L2JOneoEventMods.getProperty("WeddingAllowSameSex", "False"));
		// ********************//
		/* Champion Mods */
		// ********************//
		CHAMPION_ENABLE = Boolean.parseBoolean(L2JOneoEventMods.getProperty("ChampionEnable", "False"));
		CHAMPION_FREQUENCY = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionFrequency", "0"));
		CHAMPION_MIN_LVL = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionMinLevel", "20"));
		CHAMPION_MAX_LVL = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionMaxLevel", "60"));
		CHAMPION_HP = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionHp", "7"));
		CHAMPION_HP_REGEN = Float.parseFloat(L2JOneoEventMods.getProperty("ChampionHpRegen", "1."));
		CHAMPION_REWARDS = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionRewards", "8"));
		CHAMPION_ADENAS_REWARDS = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionAdenasRewards", "1"));
		CHAMPION_ATK = Float.parseFloat(L2JOneoEventMods.getProperty("ChampionAtk", "1."));
		CHAMPION_SPD_ATK = Float.parseFloat(L2JOneoEventMods.getProperty("ChampionSpdAtk", "1."));
		CHAMPION_REWARD = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionRewardItem", "0"));
		CHAMPION_REWARD_ID = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionRewardItemID", "6393"));
		CHAMPION_REWARD_QTY = Integer.parseInt(L2JOneoEventMods.getProperty("ChampionRewardItemQty", "1"));

	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + L2JONEO_MODS + " File.");
	    }
	    /** ************************************************** */
	    /** L2J Oneo Mods Properties File -End * */
	    /** ************************************************** */
	    /** ************************************************** */
	    /** L2J Oneo Custom Properties File -Begin * */
	    /** ************************************************** */
	    try
	    {
		Properties L2JOneoCustom = new Properties();
		InputStream is = new FileInputStream(new File(L2J_ONEO_CUSTOM));
		L2JOneoCustom.load(is);
		is.close();
		// **************************//
		/* Baby Pets Customizations */
		// **************************//
		BABY_PETS_CAN_GROWUP = Boolean.parseBoolean(L2JOneoCustom.getProperty("BabyPetsCanGrowUp", "False"));
		WOLF_PETS_CAN_GROWUP = Boolean.parseBoolean(L2JOneoCustom.getProperty("WolfPetsCanGrowUp", "False"));
		BABY_PETS_GROWUP_LVL = Integer.parseInt(L2JOneoCustom.getProperty("BabyPetsGrowUpLvl", "65"));
		// ********************//
		/* Character Statistics */
		// ********************//
        MAX_RUN_SPEED = Integer.parseInt(L2JOneoCustom.getProperty("MaxRunSpeed", "250")); 
        MAX_EVASION = Integer.parseInt(L2JOneoCustom.getProperty("MaxEvasion", "200")); 
        MAX_MCRIT_RATE = Integer.parseInt(L2JOneoCustom.getProperty("MaxMCritRate", "300"));  
		MAX_RCRIT = Integer.parseInt(L2JOneoCustom.getProperty("MaxCritical", "500"));
		MAX_PATK_SPEED = Integer.parseInt(L2JOneoCustom.getProperty("MaxPAtkSpeed", "0"));
		MAX_MATK_SPEED = Integer.parseInt(L2JOneoCustom.getProperty("MaxMAtkSpeed", "0"));
		KEEP_SUBCLASS_SKILLS = Boolean.parseBoolean(L2JOneoCustom.getProperty("KeepSubClassSkills", "False"));
		MAX_SUBCLASSES = Integer.parseInt(L2JOneoCustom.getProperty("MaxSubClasses", "3"));
		PLAYER_PROTECTION_SYSTEM = Integer.parseInt(L2JOneoCustom.getProperty("PlayerProtectionLevel", "0"));
		FRONT_BLOW_SUCCESS = Integer.parseInt(L2JOneoCustom.getProperty("FrontBlow", "50"));
		BACK_BLOW_SUCCESS = Integer.parseInt(L2JOneoCustom.getProperty("BackBlow", "70"));
		SIDE_BLOW_SUCCESS = Integer.parseInt(L2JOneoCustom.getProperty("SideBlow", "60"));
		DISABLE_GRADE_PENALTIES = Boolean.parseBoolean(L2JOneoCustom.getProperty("DisableGradePenalties", "False"));
		DISABLE_WEIGHT_PENALTIES = Boolean.parseBoolean(L2JOneoCustom.getProperty("DisableWeightPenalties", "False"));
		DONATOR_DELETE_RENTED_ITEMS = Boolean.parseBoolean(L2JOneoCustom.getProperty("DonatorDeleteRentedItems", "False"));
		DONATOR_NAME_COLOR = Integer.decode("0x" + L2JOneoCustom.getProperty("DonatorColorName", "00FFFF"));
		DONATOR_ITEMS = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowDonatorItems", "False"));
		DONATORS_REVIVE = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowDonatorAutoRevive", "False"));
		HERO_CUSTOM_ITEMS = Boolean.parseBoolean(L2JOneoCustom.getProperty("EnableHeroCustomItem", "False"));
		Config.ALLOW_DONATORS_UNLEGIT_SKILLS = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowDonatorsUnlegit", "False"));
		KOOFS_NAME_COLOR = Integer.decode("0x" + L2JOneoCustom.getProperty("KoofsColorName", "00FFFF"));
		NOOBS_NAME_COLOR = Integer.decode("0x" + L2JOneoCustom.getProperty("NoobsColorName", "00FF00"));
		ENABLE_FACTION_KOOFS_NOOBS = Boolean.parseBoolean(L2JOneoCustom.getProperty("FactionKoofsNoobs", "False"));
		FACTION_ANNOUNCE_TIME = Integer.parseInt(L2JOneoCustom.getProperty("AnnounceTimeFaction", "0"));
		KOOFS_NAME_TEAM = L2JOneoCustom.getProperty("KoofsTeamName", "koofs");
		NOOBS_NAME_TEAM = L2JOneoCustom.getProperty("NoobsTeamName", "noobs");
		REBIRTH_ITEM = Integer.parseInt(L2JOneoCustom.getProperty("RebirthItemId", "0"));
		REBIRTH_SKILL1 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill1", "0"));
		REBIRTH_SKILL1_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL1", "0"));
		REBIRTH_SKILL2 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill2", "0"));
		REBIRTH_SKILL2_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL2", "0"));
		REBIRTH_SKILL3 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill3", "0"));
		REBIRTH_SKILL3_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL3", "0"));
		REBIRTH_SKILL4 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill4", "0"));
		REBIRTH_SKILL4_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL4", "0"));
		REBIRTH_SKILL5 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill5", "0"));
		REBIRTH_SKILL5_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL5", "0"));
		REBIRTH_SKILL6 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill6", "0"));
		REBIRTH_SKILL6_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL6", "0"));
		REBIRTH_SKILL7 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill7", "0"));
		REBIRTH_SKILL7_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL7", "0"));
		REBIRTH_SKILL8 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill8", "0"));
		REBIRTH_SKILL8_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL8", "0"));
		REBIRTH_SKILL9 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill9", "0"));
		REBIRTH_SKILL9_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL9", "0"));
		REBIRTH_SKILL10 = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKill10", "0"));
		REBIRTH_SKILL10_LVL = Integer.parseInt(L2JOneoCustom.getProperty("RewardSKillLvL10", "0"));
		CHAR_TITLE = Boolean.parseBoolean(L2JOneoCustom.getProperty("CharTitle", "False"));
		ADD_CHAR_TITLE = L2JOneoCustom.getProperty("CharAddTitle", "OneoDevTeam");
		ADD_MAX_LOAD = Integer.parseInt(L2JOneoCustom.getProperty("WeightLimit", "0"));
		CUSTOM_RUN_SPEED = Integer.parseInt(L2JOneoCustom.getProperty("CustomRunSpeed", "0"));
		KEEP_BUFFS_ON_DEATH = Boolean.parseBoolean(L2JOneoCustom.getProperty("KeepBuffsOnDeath", "False"));
		DEATH_PENALTY_CHANCE = Integer.parseInt(L2JOneoCustom.getProperty("DeathPenaltyChance", "20"));
		ALT_PLAYER_CAN_DROP_AA = Boolean.parseBoolean(L2JOneoCustom.getProperty("PlayerCanDropAncientAdena", "False"));
		PLAYER_DROP_AA = Integer.parseInt(L2JOneoCustom.getProperty("DropAncientAdena", "1"));
		ALLOW_ADENA_REWARD = Boolean.parseBoolean(L2JOneoCustom.getProperty("PlayerGetAdenaByPvP", "False"));
		ADENA_NUMBER_REWARD_ON_PVP = Integer.parseInt(L2JOneoCustom.getProperty("AmmountAdenaGetByPvP", "1"));
		LOOSE_ADENA_ON_DIE = Boolean.parseBoolean(L2JOneoCustom.getProperty("PlayerLooseAdena", "False"));
		ADENA_NUMBER_LOST_ON_DIE = Integer.parseInt(L2JOneoCustom.getProperty("AmmountAdenaLostWhenDies", "1"));
		SET_LVL_ON_START = Boolean.parseBoolean(L2JOneoCustom.getProperty("SetHighLevelOnStart", "False"));
		HIGH_LEVEL_ON_START_FOR_SUBCLASS = Boolean.parseBoolean(L2JOneoCustom.getProperty("HighLevelOnStartForSubclass", "True"));
		CUSTOM_STARTER_ITEMS_ENABLED = Boolean.parseBoolean(L2JOneoCustom.getProperty("CustomStarterItemsEnabled", "False"));
		DISABLE_OFFICIAL_STARTER_ITEMS = Boolean.parseBoolean(L2JOneoCustom.getProperty("DisableOfficialStarterItems", "False"));
		if (Config.CUSTOM_STARTER_ITEMS_ENABLED)
		{
		    String[] propertySplit = L2JOneoCustom.getProperty("CustomStarterItems", "0,0").split(";");
		    for (String starteritems : propertySplit)
		    {
			String[] starteritemsSplit = starteritems.split(",");
			if (starteritemsSplit.length != 2)
			{
			    CUSTOM_STARTER_ITEMS_ENABLED = false;
			    System.out.println("StarterItems[Config.load()]: invalid config property -> starter items \"" + starteritems + "\"");
			} else
			{
			    try
			    {
				CUSTOM_STARTER_ITEMS.add(new int[] { Integer.valueOf(starteritemsSplit[0]), Integer.valueOf(starteritemsSplit[1]) });
			    } catch (NumberFormatException nfe)
			    {
				if (!starteritems.equals(""))
				{
				    CUSTOM_STARTER_ITEMS_ENABLED = false;
				    System.out.println("StarterItems[Config.load()]: invalid config property -> starter items \"" + starteritems + "\"");
				}
			    }
			}
		    }
		}
		// ********************//
		/* NPC Customizations */
		// ********************//
		GAME_VIEWNPC = Boolean.parseBoolean(L2JOneoCustom.getProperty("GameViewNpc", "False"));
		GAME_VIEWNPC_COMBAT = Boolean.parseBoolean(L2JOneoCustom.getProperty("GameViewNpcCombat", "False"));
		GAME_VIEWNPC_BASIC = Boolean.parseBoolean(L2JOneoCustom.getProperty("GameViewNpcBasic", "False"));
		GAME_VIEWNPC_DROP = Boolean.parseBoolean(L2JOneoCustom.getProperty("GameViewNpcDrop", "False"));
		GAME_VIEWNPC_QUESTDROP = Boolean.parseBoolean(L2JOneoCustom.getProperty("GameViewNpcQuestDrop", "False"));
		MIN_MONSTER_ANIMATION = Integer.parseInt(L2JOneoCustom.getProperty("MinMonsterAnimation", "0"));
		MAX_MONSTER_ANIMATION = Integer.parseInt(L2JOneoCustom.getProperty("MaxMonsterAnimation", "0"));
		RAID_FOSSILIZATION_PENALTY = Boolean.parseBoolean(L2JOneoCustom.getProperty("RaidFossilizationPenalty", "False"));
		ALLOW_MANOR = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowManor", "False"));
		ALT_MANOR_REFRESH_TIME = Integer.parseInt(L2JOneoCustom.getProperty("AltManorRefreshTime", "20"));
		ALT_MANOR_REFRESH_MIN = Integer.parseInt(L2JOneoCustom.getProperty("AltManorRefreshMin", "00"));
		ALT_MANOR_APPROVE_TIME = Integer.parseInt(L2JOneoCustom.getProperty("AltManorApproveTime", "6"));
		ALT_MANOR_APPROVE_MIN = Integer.parseInt(L2JOneoCustom.getProperty("AltManorApproveMin", "00"));
		ALT_MANOR_MAINTENANCE_PERIOD = Integer.parseInt(L2JOneoCustom.getProperty("AltManorMaintenancePreiod", "360000"));
		ALT_MANOR_SAVE_ALL_ACTIONS = Boolean.parseBoolean(L2JOneoCustom.getProperty("AltManorSaveAllActions", "False"));
		ALT_MANOR_SAVE_PERIOD_RATE = Integer.parseInt(L2JOneoCustom.getProperty("AltManorSavePeriodRate", "2"));
		ALLOW_NPC_WALKERS = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowNpcWalkers", "False"));
		// ********************//
		/* Player Command */
		// ********************//
		ALLOW_AUTOHERBS_CMD = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowAutoHerbsCommand", "False"));
		ALLOW_WITHDRAW_CWH_CMD = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowPlayersWithdrawCWH", "False"));
		// ********************//
		/* Announcements */
		// ********************//
		SHOW_HTML_WELCOME = Boolean.parseBoolean(L2JOneoCustom.getProperty("ShowOneoInfo", "True"));
		SHOW_GM_LOGIN = Boolean.parseBoolean(L2JOneoCustom.getProperty("ShowGMLogin", "False"));
		SHOW_L2J_LICENSE = Boolean.parseBoolean(L2JOneoCustom.getProperty("ShowL2JLicense", "False"));
		SHOW_HTML_NEWBIE = Boolean.parseBoolean(L2JOneoCustom.getProperty("ShowHTMLNewbie", "False"));
		LEVEL_HTML_NEWBIE = Integer.parseInt(L2JOneoCustom.getProperty("LevelShowHTMLNewbie", "10"));
		ONLINE_PLAYERS_AT_STARTUP = Boolean.parseBoolean(L2JOneoCustom.getProperty("ShowOnlinePlayersAtStartup", "False"));
		PLAYERS_ONLINE_TRICK = Integer.parseInt(L2JOneoCustom.getProperty("OnlinePlayerCountTrick", "0"));
		ONLINE_PLAYERS_ANNOUNCE_INTERVAL = Integer.parseInt(L2JOneoCustom.getProperty("OnlinePlayersAnnounceInterval", "900000"));
		ANNOUNCE_CASTLE_LORDS = Boolean.parseBoolean(L2JOneoCustom.getProperty("AnnounceCastleLords", "False"));
		ALT_ANNOUNCE_PK = Boolean.parseBoolean(L2JOneoCustom.getProperty("NoticePK", "False"));
		ENABLE_PK_INFO = Boolean.valueOf(L2JOneoCustom.getProperty("EnablePkInfo", "False"));
		NPC_ANNOUNCER_DONATOR_ONLY = Boolean.parseBoolean(L2JOneoCustom.getProperty("NpcAnnouncerDonatorOnly", "False"));
		ALLOW_NPC_ANNOUNCER = Boolean.parseBoolean(L2JOneoCustom.getProperty("AllowNpcAnnouncer", "False"));
		NPC_ANNOUNCER_PRICE_PER_ANNOUNCE = Integer.parseInt(L2JOneoCustom.getProperty("PricePerAnnounce", "10000"));
		NPC_ANNOUNCER_MAX_ANNOUNCES_PER_DAY = Integer.parseInt(L2JOneoCustom.getProperty("AnnouncesPerDay", "20"));
		NPC_ANNOUNCER_MIN_LVL_TO_ANNOUNCE = Integer.parseInt(L2JOneoCustom.getProperty("MinLevelToAnnounce", "0"));
		NPC_ANNOUNCER_MAX_LVL_TO_ANNOUNCE = Integer.parseInt(L2JOneoCustom.getProperty("MaxLevelToAnnounce", "80"));
		// ********************//
		/* Dimensional Rift */
		// ********************//
		RIFT_MIN_PARTY_SIZE = Integer.parseInt(L2JOneoCustom.getProperty("RiftMinPartySize", "5"));
		RIFT_MAX_JUMPS = Integer.parseInt(L2JOneoCustom.getProperty("MaxRiftJumps", "4"));
		RIFT_SPAWN_DELAY = Integer.parseInt(L2JOneoCustom.getProperty("RiftSpawnDelay", "10000"));
		RIFT_AUTO_JUMPS_TIME_MIN = Integer.parseInt(L2JOneoCustom.getProperty("AutoJumpsDelayMin", "480"));
		RIFT_AUTO_JUMPS_TIME_MAX = Integer.parseInt(L2JOneoCustom.getProperty("AutoJumpsDelayMax", "600"));
		RIFT_BOSS_ROOM_TIME_MUTIPLY = Float.parseFloat(L2JOneoCustom.getProperty("BossRoomTimeMultiply", "1.5"));
		RIFT_ENTER_COST_RECRUIT = Integer.parseInt(L2JOneoCustom.getProperty("RecruitCost", "18"));
		RIFT_ENTER_COST_SOLDIER = Integer.parseInt(L2JOneoCustom.getProperty("SoldierCost", "21"));
		RIFT_ENTER_COST_OFFICER = Integer.parseInt(L2JOneoCustom.getProperty("OfficerCost", "24"));
		RIFT_ENTER_COST_CAPTAIN = Integer.parseInt(L2JOneoCustom.getProperty("CaptainCost", "27"));
		RIFT_ENTER_COST_COMMANDER = Integer.parseInt(L2JOneoCustom.getProperty("CommanderCost", "30"));
		RIFT_ENTER_COST_HERO = Integer.parseInt(L2JOneoCustom.getProperty("HeroCost", "33"));
		// ********************//
		/* Clan. Customizes. */
		// ********************//
		CLAN_RAISE_FIRST_COST = Integer.parseInt(L2JOneoCustom.getProperty("ClanFirstCost", "650000"));
		CLAN_RAISE_SEC_COST = Integer.parseInt(L2JOneoCustom.getProperty("ClanSecondCOst", "2500000"));
		CLAN_MEMBERS_FIRST = Integer.parseInt(L2JOneoCustom.getProperty("ClanMembersNeedSix", "30"));
		CLAN_MEMBERS_SEC = Integer.parseInt(L2JOneoCustom.getProperty("ClanMembersNeedSeven", "80"));
		CLAN_MEMBERS_THIRD = Integer.parseInt(L2JOneoCustom.getProperty("ClanMembersNeedEight", "120"));
		CLAN_REPUTATION_FIRST = Integer.parseInt(L2JOneoCustom.getProperty("ClanReputationSix", "10000"));
		CLAN_REPUTATION_SEC = Integer.parseInt(L2JOneoCustom.getProperty("ClanReputationSeven", "20000"));
		CLAN_REPUTATION_THIRD = Integer.parseInt(L2JOneoCustom.getProperty("ClanReputationEight", "40000"));
		CLAN_SP_FIRST = Integer.parseInt(L2JOneoCustom.getProperty("ClanSpFirst", "30000"));
		CLAN_SP_SEC = Integer.parseInt(L2JOneoCustom.getProperty("ClanSpSecond", "150000"));
		CLAN_SP_THIRD = Integer.parseInt(L2JOneoCustom.getProperty("ClanSpThird", "500000"));
		CLAN_SP_FORTH = Integer.parseInt(L2JOneoCustom.getProperty("ClanSpForth", "1400000"));
		CLAN_SP_FIFTH = Integer.parseInt(L2JOneoCustom.getProperty("ClanSpFifth", "3500000"));
		// ********************//
		/* Serv. Customizes. */
		// ********************//
		LOGIN_RESTART_WITH_GAMESERVER = Boolean.parseBoolean(L2JOneoCustom.getProperty("LoginRestartWithGameserver", "False"));
		LOGIN_RESTART_BY_TIME = Boolean.parseBoolean(L2JOneoCustom.getProperty("LoginRestartByTime", "False"));
		LOGIN_RESTART_TIME = Integer.parseInt(L2JOneoCustom.getProperty("LoginRestartTime", "60"));
		SAFE_SIGTERM = Boolean.parseBoolean(L2JOneoCustom.getProperty("SafeSigterm", "False"));
		GM_OVER_ENCHANT = Integer.parseInt(L2JOneoCustom.getProperty("GMOverEnchant", "0"));
		STARTING_AA = Integer.parseInt(L2JOneoCustom.getProperty("StartingAA", "0"));
		USE_CHAT_FILTER = Boolean.parseBoolean(L2JOneoCustom.getProperty("UseChatFilter", "False"));
		USE_POWERFULL_CHAT_FILTER = Boolean.parseBoolean(L2JOneoCustom.getProperty("UsePowerfullChatFilter", "False"));
		CHAT_FILTER_CHARS = L2JOneoCustom.getProperty("ChatFilterChars", "***");
		CHAT_FILTER_PUNISHMENT = Integer.parseInt(L2JOneoCustom.getProperty("ChatFilterPunishment", "1"));
		CHAT_FILTER_PUNISHMENT_TIME = Integer.parseInt(L2JOneoCustom.getProperty("ChatFilterPunishmentTime", "5"));
		CHECK_SKILLS_ON_ENTER = Boolean.parseBoolean(L2JOneoCustom.getProperty("CheckSkillsOnEnter", "False"));
		SPAWN_CHAR = Boolean.parseBoolean(L2JOneoCustom.getProperty("CustomSpawn", "False"));
		SPAWN_X = Integer.parseInt(L2JOneoCustom.getProperty("SpawnX", ""));
		SPAWN_Y = Integer.parseInt(L2JOneoCustom.getProperty("SpawnY", ""));
		SPAWN_Z = Integer.parseInt(L2JOneoCustom.getProperty("SpawnZ", ""));
		ENCHANT_CHANCE_WEAPON_CRYSTAL = Integer.parseInt(L2JOneoCustom.getProperty("EnchantChanceWeaponCrystal", "85"));
		ENCHANT_CHANCE_ARMOR_CRYSTAL = Integer.parseInt(L2JOneoCustom.getProperty("EnchantChanceArmorCrystal", "85"));
		ENCHANT_CHANCE_JEWELRY_CRYSTAL = Integer.parseInt(L2JOneoCustom.getProperty("EnchantChanceJewelryCrystal", "85"));
		ENCHANT_CHANCE_WEAPON_BLESSED = Integer.parseInt(L2JOneoCustom.getProperty("EnchantChanceWeaponBlessed", "55"));
		ENCHANT_CHANCE_ARMOR_BLESSED = Integer.parseInt(L2JOneoCustom.getProperty("EnchantChanceArmorBlessed", "55"));
		ENCHANT_CHANCE_JEWELRY_BLESSED = Integer.parseInt(L2JOneoCustom.getProperty("EnchantChanceJewelryBlessed", "55"));
		ENABLE_DWARF_ENCHANT_BONUS = Boolean.parseBoolean(L2JOneoCustom.getProperty("EnableDwarfEnchantBonus", "False"));
		DWARF_ENCHANT_MIN_LEVEL = Integer.parseInt(L2JOneoCustom.getProperty("DwarfEnchantMinLevel", "80"));
		DWARF_ENCHANT_BONUS = Integer.parseInt(L2JOneoCustom.getProperty("DwarfEnchantBonus", "15"));
		GM_ADMIN_MENU_STYLE = L2JOneoCustom.getProperty("GMAdminMenuStyle", "modern");
		ENABLE_PACKET_PROTECTION = Boolean.parseBoolean(L2JOneoCustom.getProperty("PacketProtection", "False"));
		MAX_UNKNOWN_PACKETS = Integer.parseInt(L2JOneoCustom.getProperty("UnknownPacketsBeforeBan", "5"));
		UNKNOWN_PACKETS_PUNiSHMENT = Integer.parseInt(L2JOneoCustom.getProperty("UnknownPacketsPunishment", "2"));
		GMAUDIT = Boolean.valueOf(L2JOneoCustom.getProperty("GMAudit", "True"));
		// ********************//
		/* Misc. Customizes. */
		// ********************//
		STRICT_HERO_SYSTEM = Boolean.parseBoolean(L2JOneoCustom.getProperty("StrictHeroSystem", "True"));
                ENCHANT_HERO_WEAPONS  = Boolean.parseBoolean(L2JOneoCustom.getProperty("EnchantHeroWeapons", "False"));
		SUBCLASS_WITH_ITEM_AND_NO_QUEST = Boolean.parseBoolean(L2JOneoCustom.getProperty("SubclassWithItemAndNoQuest", "False"));
		FLYING_WYVERN_DURING_SIEGE = Boolean.parseBoolean(L2JOneoCustom.getProperty("FlyingWyvernDuringSiege", "False"));
		ES_SP_BOOK_NEEDED = Boolean.parseBoolean(L2JOneoCustom.getProperty("EnchantSkillSpBookNeeded", "True"));
		LIFE_CRYSTAL_NEEDED = Boolean.parseBoolean(L2JOneoCustom.getProperty("LifeCrystalNeeded", "True"));
		POTIONS_REUSE_DELAY = Integer.parseInt(L2JOneoCustom.getProperty("PotionsDelay", "0")) * 1000;
		ELIXIRS_REUSE_DELAY = Integer.parseInt(L2JOneoCustom.getProperty("ElixirsDelay", "0")) * 1000;
		REMOVE_CASTLE_CIRCLETS = Boolean.parseBoolean(L2JOneoCustom.getProperty("RemoveCastleCirclets", "True"));
		ENABLE_WAREHOUSESORTING_CLAN = Boolean.valueOf(L2JOneoCustom.getProperty("EnableWarehouseSortingClan", "False"));
		ENABLE_WAREHOUSESORTING_PRIVATE = Boolean.valueOf(L2JOneoCustom.getProperty("EnableWarehouseSortingPrivate", "False"));
		ENABLE_WAREHOUSESORTING_FREIGHT = Boolean.valueOf(L2JOneoCustom.getProperty("EnableWarehouseSortingFreight", "False"));
		DISABLE_SUMMON_IN_COMBAT = Boolean.valueOf(L2JOneoCustom.getProperty("DisableSummonInCombat", "True"));
		DISABLE_ATTACK_NPC_TYPE = Boolean.valueOf(L2JOneoCustom.getProperty("DisableAttackToNpcs", "False"));
		ALT_PERFECT_SHLD_BLOCK = Integer.parseInt(L2JOneoCustom.getProperty("AltPerfectShieldBlockRate", "5"));
		ALLOWED_NPC_TYPES = L2JOneoCustom.getProperty("AllowedNPCTypes");
		LIST_ALLOWED_NPC_TYPES = new FastList<String>();
		for (String npc_type : ALLOWED_NPC_TYPES.split(","))
		{
		    LIST_ALLOWED_NPC_TYPES.add(npc_type);
		}
		CUSTOM_SPAWNLIST_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomSpawnlistTable", "False"));
		SAVE_GMSPAWN_ON_CUSTOM = Boolean.valueOf(L2JOneoCustom.getProperty("SaveGmSpawnOnCustom", "False"));
		DELETE_GMSPAWN_ON_CUSTOM = Boolean.valueOf(L2JOneoCustom.getProperty("DeleteGmSpawnOnCustom", "False"));
		CUSTOM_NPC_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomNpcTable", "False"));
		CUSTOM_ETCITEM_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomEtcitemTable", "False"));
		CUSTOM_ARMOR_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomArmorTable", "False"));
		CUSTOM_ARMORSETS_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomArmorSetsTable", "False"));
		CUSTOM_WEAPON_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomWeaponTable", "False"));
		CUSTOM_TELEPORT_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomTeleportTable", "False"));
		CUSTOM_DROPLIST_TABLE = Boolean.valueOf(L2JOneoCustom.getProperty("CustomDroplistTable", "False"));
		CUSTOM_MERCHANT_TABLES = Boolean.valueOf(L2JOneoCustom.getProperty("CustomMerchantTables", "False"));
		ENABLE_OLY_WEAPON_ENCH = Boolean.valueOf(L2JOneoCustom.getProperty("EnableOlyWeaponEnch", "False"));
		MAX_OLY_WEAPON_ENCH = Integer.parseInt(L2JOneoCustom.getProperty("MaxOlyWeaponEnch", "0"));
		DISABLE_OLY_DUALBOX = Boolean.valueOf(L2JOneoCustom.getProperty("DisableOlyDualBox", "False"));
		ENABLE_MODIFY_SKILL_DURATION = Boolean.valueOf(L2JOneoCustom.getProperty("EnableModifySkillDuration", "False"));
		// Create Map only if enabled
		if (ENABLE_MODIFY_SKILL_DURATION)
		{
		    SKILL_DURATION_LIST = new FastMap<Integer, Integer>();
		    String[] propertySplit;
		    propertySplit = L2JOneoCustom.getProperty("SkillDurationList", "").split(";");
		    for (String skill : propertySplit)
		    {
			String[] skillSplit = skill.split(",");
			if (skillSplit.length != 2)
			{
			    System.out.println("[SkillDurationList]: invalid config property -> SkillDurationList \"" + skill + "\"");
			} else
			{
			    try
			    {
				SKILL_DURATION_LIST.put(Integer.valueOf(skillSplit[0]), Integer.valueOf(skillSplit[1]));
			    } catch (NumberFormatException nfe)
			    {
				if (!skill.equals(""))
				{
				    System.out.println("[SkillDurationList]: invalid config property -> SkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
				}
			    }
			}
		    }
		}
		ENABLE_NO_AUTOLEARN_LIST = Boolean.valueOf(L2JOneoCustom.getProperty("EnableNoAutoLearnList", "False"));
		if (ENABLE_NO_AUTOLEARN_LIST)
		{
		    NO_AUTOLEARN_LIST = new FastList<Integer>();
		    String[] propertySplit;
		    propertySplit = L2JOneoCustom.getProperty("NoAutoLearnList", "").split(";");
		    for (String skill : propertySplit)
		    {
			try
			{
			    NO_AUTOLEARN_LIST.add(Integer.valueOf(skill));
			} catch (NumberFormatException nfe)
			{
			    if (!skill.equals(""))
			    {
				System.out.println("[NoAutoLearnList]: invalid config property -> NoAutoLearnList \"" + skill);
			    }
			}
		    }
		}
		// ************************//
		/* Equipment Restriction. */
		// **********************//
		CASTLE_SHIELD = Boolean.parseBoolean(L2JOneoCustom.getProperty("CastleShieldRestriction", "True"));
		CLANHALL_SHIELD = Boolean.parseBoolean(L2JOneoCustom.getProperty("ClanHallShieldRestriction", "True"));
		APELLA_ARMORS = Boolean.parseBoolean(L2JOneoCustom.getProperty("ApellaArmorsRestriction", "True"));
		OATH_ARMORS = Boolean.parseBoolean(L2JOneoCustom.getProperty("OathArmorsRestriction", "True"));
		CASTLE_CROWN = Boolean.parseBoolean(L2JOneoCustom.getProperty("CastleLordsCrownRestriction", "True"));
		CASTLE_CIRCLETS = Boolean.parseBoolean(L2JOneoCustom.getProperty("CastleCircletsRestriction", "True"));
		// ************************//
		/* Banking System */
		// **********************//
		BANKING_SYSTEM_ENABLED = Boolean.parseBoolean(L2JOneoCustom.getProperty("BankingSystemEnabled", "False"));
		BANKING_SYSTEM_ADENA = Integer.parseInt(L2JOneoCustom.getProperty("BankingSystemAdena", "0"));
		BANKING_SYSTEM_GOLDBARS = Integer.parseInt(L2JOneoCustom.getProperty("BankingSystemGoldBars", "0"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + L2J_ONEO_CUSTOM + " File.");
	    }
	    /** ************************************************** */
	    /** L2J Oneo Custom Properties File -End * */
	    /** ************************************************** */
	    // irc to ig
	    try
	    {
		Properties ircSettings = new Properties();
		InputStream is = new FileInputStream(new File(IRC_FILE));
		ircSettings.load(is);
		is.close();
		IRC_HOSTNAME = ircSettings.getProperty("IrcHostname", "localhost");
		IRC_PORT = Integer.parseInt(ircSettings.getProperty("IrcPort", "6667"));
		IRC_USERNAME = ircSettings.getProperty("IrcUserNick", "L2JBot");
		IRC_USERREALNAME = ircSettings.getProperty("IrcUserRealName", "L2JBot");
		IRC_USERMAIL = ircSettings.getProperty("IrcUserMail");
		IRC_CHANNEL = ircSettings.getProperty("IrcChannel");
		IRC_IGTOIRC_FILE = ircSettings.getProperty("IrcIgToIrcFile", "./log/igtoirc.log");
		IRC_KEYWORDTOIG = ircSettings.getProperty("IrcKeywordToIg");
		IRC_KEYWORDTOIRC = ircSettings.getProperty("IrcKeywordToIrc");
		IRC_LOAD = Boolean.parseBoolean(ircSettings.getProperty("StartIrcBot", "False"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + IRC_FILE + " File.");
	    }
	    // Seven Signs Config
	    try
	    {
		Properties SevenSettings = new Properties();
		InputStream is = new FileInputStream(new File(SEVENSIGNS_FILE));
		SevenSettings.load(is);
		is.close();
		ALT_GAME_REQUIRE_CASTLE_DAWN = Boolean.parseBoolean(SevenSettings.getProperty("AltRequireCastleForDawn", "False"));
		ALT_GAME_REQUIRE_CLAN_CASTLE = Boolean.parseBoolean(SevenSettings.getProperty("AltRequireClanCastle", "False"));
		ALT_FESTIVAL_MIN_PLAYER = Integer.parseInt(SevenSettings.getProperty("AltFestivalMinPlayer", "5"));
		ALT_MAXIMUM_PLAYER_CONTRIB = Integer.parseInt(SevenSettings.getProperty("AltMaxPlayerContrib", "1000000"));
		ALT_FESTIVAL_MANAGER_START = Long.parseLong(SevenSettings.getProperty("AltFestivalManagerStart", "120000"));
		ALT_FESTIVAL_LENGTH = Long.parseLong(SevenSettings.getProperty("AltFestivalLength", "1080000"));
		ALT_FESTIVAL_CYCLE_LENGTH = Long.parseLong(SevenSettings.getProperty("AltFestivalCycleLength", "2280000"));
		ALT_FESTIVAL_FIRST_SPAWN = Long.parseLong(SevenSettings.getProperty("AltFestivalFirstSpawn", "120000"));
		ALT_FESTIVAL_FIRST_SWARM = Long.parseLong(SevenSettings.getProperty("AltFestivalFirstSwarm", "300000"));
		ALT_FESTIVAL_SECOND_SPAWN = Long.parseLong(SevenSettings.getProperty("AltFestivalSecondSpawn", "540000"));
		ALT_FESTIVAL_SECOND_SWARM = Long.parseLong(SevenSettings.getProperty("AltFestivalSecondSwarm", "720000"));
		ALT_FESTIVAL_CHEST_SPAWN = Long.parseLong(SevenSettings.getProperty("AltFestivalChestSpawn", "900000"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + SEVENSIGNS_FILE + " File.");
	    }
	    // clanhall settings
	    try
	    {
		Properties clanhallSettings = new Properties();
		InputStream is = new FileInputStream(new File(CLANHALL_CONFIG_FILE));
		clanhallSettings.load(is);
		is.close();
		CH_TELE_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallTeleportFunctionFeeRation", "86400000"));
		CH_TELE1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallTeleportFunctionFeeLvl1", "86400000"));
		CH_TELE2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallTeleportFunctionFeeLvl2", "86400000"));
		CH_SUPPORT_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallSupportFunctionFeeRation", "86400000"));
		CH_SUPPORT1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl1", "86400000"));
		CH_SUPPORT2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl2", "86400000"));
		CH_SUPPORT3_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl3", "86400000"));
		CH_SUPPORT4_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl4", "86400000"));
		CH_SUPPORT5_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl5", "86400000"));
		CH_SUPPORT6_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl6", "86400000"));
		CH_SUPPORT7_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl7", "86400000"));
		CH_SUPPORT8_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallSupportFeeLvl8", "86400000"));
		CH_MPREG_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallMpRegenerationFunctionFeeRation", "86400000"));
		CH_MPREG1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallMpRegenerationFeeLvl1", "86400000"));
		CH_MPREG2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallMpRegenerationFeeLvl2", "86400000"));
		CH_MPREG3_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallMpRegenerationFeeLvl3", "86400000"));
		CH_MPREG4_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallMpRegenerationFeeLvl4", "86400000"));
		CH_MPREG5_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallMpRegenerationFeeLvl5", "86400000"));
		CH_HPREG_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFunctionFeeRation", "86400000"));
		CH_HPREG1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl1", "86400000"));
		CH_HPREG2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl2", "86400000"));
		CH_HPREG3_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl3", "86400000"));
		CH_HPREG4_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl4", "86400000"));
		CH_HPREG5_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl5", "86400000"));
		CH_HPREG6_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl6", "86400000"));
		CH_HPREG7_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl7", "86400000"));
		CH_HPREG8_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl8", "86400000"));
		CH_HPREG9_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl9", "86400000"));
		CH_HPREG10_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl10", "86400000"));
		CH_HPREG11_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl11", "86400000"));
		CH_HPREG12_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl12", "86400000"));
		CH_HPREG13_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallHpRegenerationFeeLvl13", "86400000"));
		CH_EXPREG_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFunctionFeeRation", "86400000"));
		CH_EXPREG1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl1", "86400000"));
		CH_EXPREG2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl2", "86400000"));
		CH_EXPREG3_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl3", "86400000"));
		CH_EXPREG4_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl4", "86400000"));
		CH_EXPREG5_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl5", "86400000"));
		CH_EXPREG6_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl6", "86400000"));
		CH_EXPREG7_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallExpRegenerationFeeLvl7", "86400000"));
		CH_ITEM_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallItemCreationFunctionFeeRation", "86400000"));
		CH_ITEM1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallItemCreationFunctionFeeLvl1", "86400000"));
		CH_ITEM2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallItemCreationFunctionFeeLvl2", "86400000"));
		CH_ITEM3_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallItemCreationFunctionFeeLvl3", "86400000"));
		CH_CURTAIN_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallCurtainFunctionFeeRation", "86400000"));
		CH_CURTAIN1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallCurtainFunctionFeeLvl1", "86400000"));
		CH_CURTAIN2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallCurtainFunctionFeeLvl2", "86400000"));
		CH_FRONT_FEE_RATIO = Long.valueOf(clanhallSettings.getProperty("ClanHallFrontPlatformFunctionFeeRation", "86400000"));
		CH_FRONT1_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallFrontPlatformFunctionFeeLvl1", "86400000"));
		CH_FRONT2_FEE = Integer.valueOf(clanhallSettings.getProperty("ClanHallFrontPlatformFunctionFeeLvl2", "86400000"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + CLANHALL_CONFIG_FILE + " File.");
	    }
	    // pvp config
	    try
	    {
		Properties pvpSettings = new Properties();
		InputStream is = new FileInputStream(new File(PVP_CONFIG_FILE));
		pvpSettings.load(is);
		is.close();
		/* KARMA SYSTEM */
		KARMA_MIN_KARMA = Integer.parseInt(pvpSettings.getProperty("MinKarma", "240"));
		KARMA_MAX_KARMA = Integer.parseInt(pvpSettings.getProperty("MaxKarma", "10000"));
		KARMA_XP_DIVIDER = Integer.parseInt(pvpSettings.getProperty("XPDivider", "260"));
		KARMA_LOST_BASE = Integer.parseInt(pvpSettings.getProperty("BaseKarmaLost", "0"));
		KARMA_DROP_GM = Boolean.parseBoolean(pvpSettings.getProperty("CanGMDropEquipment", "False"));
		KARMA_AWARD_PK_KILL = Boolean.parseBoolean(pvpSettings.getProperty("AwardPKKillPVPPoint", "True"));
		KARMA_PK_LIMIT = Integer.parseInt(pvpSettings.getProperty("MinimumPKRequiredToDrop", "5"));
		KARMA_NONDROPPABLE_PET_ITEMS = pvpSettings.getProperty("ListOfPetItems", "2375,3500,3501,3502,4422,4423,4424,4425,6648,6649,6650");
		KARMA_NONDROPPABLE_ITEMS = pvpSettings.getProperty("ListOfNonDroppableItems", "57,1147,425,1146,461,10,2368,7,6,2370,2369,6842,6611,6612,6613,6614,6615,6616,6617,6618,6619,6620,6621");
		KARMA_LIST_NONDROPPABLE_PET_ITEMS = new FastList<Integer>();
		for (String id : KARMA_NONDROPPABLE_PET_ITEMS.split(","))
		{
		    KARMA_LIST_NONDROPPABLE_PET_ITEMS.add(Integer.parseInt(id));
		}
		KARMA_LIST_NONDROPPABLE_ITEMS = new FastList<Integer>();
		for (String id : KARMA_NONDROPPABLE_ITEMS.split(","))
		{
		    KARMA_LIST_NONDROPPABLE_ITEMS.add(Integer.parseInt(id));
		}
		PVP_NORMAL_TIME = Integer.parseInt(pvpSettings.getProperty("PvPVsNormalTime", "15000"));
		PVP_PVP_TIME = Integer.parseInt(pvpSettings.getProperty("PvPVsPvPTime", "30000"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + PVP_CONFIG_FILE + " File.");
	    }
	    // access levels
	    try
	    {
		Properties gmSettings = new Properties();
		InputStream is = new FileInputStream(new File(GM_ACCESS_FILE));
		gmSettings.load(is);
		is.close();
		GM_ACCESSLEVEL = Integer.parseInt(gmSettings.getProperty("GMAccessLevel", "100"));
		GM_MIN = Integer.parseInt(gmSettings.getProperty("GMMinLevel", "100"));
		GM_ALTG_MIN_LEVEL = Integer.parseInt(gmSettings.getProperty("GMCanAltG", "100"));
		GM_ANNOUNCE = Integer.parseInt(gmSettings.getProperty("GMCanAnnounce", "100"));
		GM_BAN = Integer.parseInt(gmSettings.getProperty("GMCanBan", "100"));
		GM_BAN_CHAT = Integer.parseInt(gmSettings.getProperty("GMCanBanChat", "100"));
		GM_CREATE_ITEM = Integer.parseInt(gmSettings.getProperty("GMCanShop", "100"));
		GM_DELETE = Integer.parseInt(gmSettings.getProperty("GMCanDelete", "100"));
		GM_KICK = Integer.parseInt(gmSettings.getProperty("GMCanKick", "100"));
		GM_MENU = Integer.parseInt(gmSettings.getProperty("GMMenu", "100"));
		GM_GODMODE = Integer.parseInt(gmSettings.getProperty("GMGodMode", "100"));
		GM_CHAR_EDIT = Integer.parseInt(gmSettings.getProperty("GMCanEditChar", "100"));
		GM_CHAR_EDIT_OTHER = Integer.parseInt(gmSettings.getProperty("GMCanEditCharOther", "100"));
		GM_CHAR_VIEW = Integer.parseInt(gmSettings.getProperty("GMCanViewChar", "100"));
		GM_NPC_EDIT = Integer.parseInt(gmSettings.getProperty("GMCanEditNPC", "100"));
		GM_NPC_VIEW = Integer.parseInt(gmSettings.getProperty("GMCanViewNPC", "100"));
		GM_PRIV_EDIT = Integer.parseInt(gmSettings.getProperty("GMCanEditPriv", "100"));
		GM_PRIV_VIEW = Integer.parseInt(gmSettings.getProperty("GMCanViewPriv", "100"));
		GM_TELEPORT = Integer.parseInt(gmSettings.getProperty("GMCanTeleport", "100"));
		GM_TELEPORT_OTHER = Integer.parseInt(gmSettings.getProperty("GMCanTeleportOther", "100"));
		GM_RESTART = Integer.parseInt(gmSettings.getProperty("GMCanRestart", "100"));
		GM_MONSTERRACE = Integer.parseInt(gmSettings.getProperty("GMMonsterRace", "100"));
		GM_RIDER = Integer.parseInt(gmSettings.getProperty("GMRider", "100"));
		GM_ESCAPE = Integer.parseInt(gmSettings.getProperty("GMFastUnstuck", "100"));
		GM_FIXED = Integer.parseInt(gmSettings.getProperty("GMResurectFixed", "100"));
		GM_CREATE_NODES = Integer.parseInt(gmSettings.getProperty("GMCreateNodes", "100"));
		GM_ENCHANT = Integer.parseInt(gmSettings.getProperty("GMEnchant", "100"));
		GM_DOOR = Integer.parseInt(gmSettings.getProperty("GMDoor", "100"));
		GM_RES = Integer.parseInt(gmSettings.getProperty("GMRes", "100"));
		GM_PEACEATTACK = Integer.parseInt(gmSettings.getProperty("GMPeaceAttack", "100"));
		GM_HEAL = Integer.parseInt(gmSettings.getProperty("GMHeal", "100"));
		GM_UNBLOCK = Integer.parseInt(gmSettings.getProperty("GMUnblock", "100"));
		GM_CACHE = Integer.parseInt(gmSettings.getProperty("GMCache", "100"));
		GM_TALK_BLOCK = Integer.parseInt(gmSettings.getProperty("GMTalkBlock", "100"));
		GM_TEST = Integer.parseInt(gmSettings.getProperty("GMTest", "100"));
		GM_FORTSIEGE = Integer.parseInt(gmSettings.getProperty("GMFortSiege", "100"));
		GM_CLAN_PANEL = Integer.parseInt(gmSettings.getProperty("GMClanPanel", "100"));
		String gmTrans = gmSettings.getProperty("GMDisableTransaction", "False");
		if (!gmTrans.equalsIgnoreCase("False"))
		{
		    String[] params = gmTrans.split(",");
		    GM_DISABLE_TRANSACTION = true;
		    GM_TRANSACTION_MIN = Integer.parseInt(params[0]);
		    GM_TRANSACTION_MAX = Integer.parseInt(params[1]);
		} else
		{
		    GM_DISABLE_TRANSACTION = false;
		}
		GM_CAN_GIVE_DAMAGE = Integer.parseInt(gmSettings.getProperty("GMCanGiveDamage", "90"));
		GM_DONT_TAKE_AGGRO = Integer.parseInt(gmSettings.getProperty("GMDontTakeAggro", "90"));
		GM_DONT_TAKE_EXPSP = Integer.parseInt(gmSettings.getProperty("GMDontGiveExpSp", "90"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + GM_ACCESS_FILE + " File.");
	    }
	    try
	    {
		Properties Settings = new Properties();
		InputStream is = new FileInputStream(HEXID_FILE);
		Settings.load(is);
		is.close();
		SERVER_ID = Integer.parseInt(Settings.getProperty("ServerID"));
		HEX_ID = new BigInteger(Settings.getProperty("HexID"), 16).toByteArray();
	    } catch (Exception e)
	    {
		_log.warning("Could not load HexID file (" + HEXID_FILE + "). Hopefully login will give us one.");
	    }
			// sepulche Custom Setting
			try
			{
				Properties Settings = new Properties();
				InputStream is = new FileInputStream(FS_CONFIG_FILE);
				Settings.load(is);
				is.close();
	                FS_TIME_ATTACK								= Integer.parseInt(Settings.getProperty("TimeOfAttack", "50"));
	    			FS_TIME_COOLDOWN 							= Integer.parseInt(Settings.getProperty("TimeOfCoolDown", "5"));
	    			FS_TIME_ENTRY 								= Integer.parseInt(Settings.getProperty("TimeOfEntry", "3"));
	    			FS_TIME_WARMUP 								= Integer.parseInt(Settings.getProperty("TimeOfWarmUp", "2"));
	    			FS_PARTY_MEMBER_COUNT 						= Integer.parseInt(Settings.getProperty("NumberOfNecessaryPartyMembers", "4"));
	    			if (FS_TIME_ATTACK <= 0)
	    				FS_TIME_ATTACK = 50;
	    			if (FS_TIME_COOLDOWN <= 0)
	    				FS_TIME_COOLDOWN = 5;
	    			if (FS_TIME_ENTRY <= 0)
	    				FS_TIME_ENTRY = 3;
	    			if (FS_TIME_ENTRY <= 0)
	    				FS_TIME_ENTRY = 3;
	    			if (FS_TIME_ENTRY <= 0)
	    				FS_TIME_ENTRY = 3;
			} catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + FS_CONFIG_FILE + " File.");
			}

			// Bosses config Custom Setting
			try
			{
				Properties Settings = new Properties();
				InputStream is = new FileInputStream(BOS_CONFIG_FILE);
				Settings.load(is);
				is.close();
                TIME_IN_A_DAY_OF_OPEN_A_DOOR = Integer.parseInt(Settings.getProperty("TimeInADayOfOpenADoor", "0"));
                TIME_OF_OPENING_A_DOOR = Integer.parseInt(Settings.getProperty("TimeOfOpeningADoor", "2"));
                TIMELIMITOFINVADE = Integer.parseInt(Settings.getProperty("TimeLimitOfInvade", "1800000"));


			} catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Failed to Load " + BOS_CONFIG_FILE + " File.");
			}
            // FloodProtector 
			try 
			{ 
				Properties FloodProtector = new Properties(); 
				InputStream is = new FileInputStream(new File(FLOODPROTECTOR_CONFIG_FILE)); 
				FloodProtector.load(is); 
				is.close(); 
				
				USEITEM_DELAY = Integer.parseInt(FloodProtector.getProperty("UseItemDelay", "4")); 
				ROLLDICE_DELAY = Integer.parseInt(FloodProtector.getProperty("RollDiceDelay", "42")); 
				FIREWORK_DELAY = Integer.parseInt(FloodProtector.getProperty("FireWorkDelay", "42")); 
				ITEMPETSUMMON_DELAY = Integer.parseInt(FloodProtector.getProperty("ItemPetSummonDelay", "16")); 
				HEROVOICE_DELAY = Integer.parseInt(FloodProtector.getProperty("HeroChatDelay", "100")); 
				UNKNOWN_PACKET_DELAY = Integer.parseInt(FloodProtector.getProperty("UnknownPacketDelay", "15")); 
				SUBCLASS_DELAY = Integer.parseInt(FloodProtector.getProperty("SubClassDelay", "100"));
                MULTISELL_DELAY = Integer.parseInt(FloodProtector.getProperty("MultisellDelay", "100"));  
			}  
			
			catch (Exception e) 
			{ 
				e.printStackTrace(); 
				throw new Error("Failed to Load " + FLOODPROTECTOR_CONFIG_FILE + " File."); 
			}
		} else if (Server.serverMode == Server.MODE_LOGINSERVER)
		{
			_log.info("loading login config");
			try
			{
		Properties serverSettings = new Properties();
		InputStream is = new FileInputStream(new File(LOGIN_CONFIGURATION_FILE));
		serverSettings.load(is);
		is.close();
		GAME_SERVER_LOGIN_HOST = serverSettings.getProperty("LoginHostname", "*");
		GAME_SERVER_LOGIN_PORT = Integer.parseInt(serverSettings.getProperty("LoginPort", "9013"));
		LOGIN_BIND_ADDRESS = serverSettings.getProperty("LoginserverHostname", "*");
		PORT_LOGIN = Integer.parseInt(serverSettings.getProperty("LoginserverPort", "2106"));
		DEBUG = Boolean.parseBoolean(serverSettings.getProperty("Debug", "False"));
		DEVELOPER = Boolean.parseBoolean(serverSettings.getProperty("Developer", "False"));
		ASSERT = Boolean.parseBoolean(serverSettings.getProperty("Assert", "False"));
		ACCEPT_NEW_GAMESERVER = Boolean.parseBoolean(serverSettings.getProperty("AcceptNewGameServer", "True"));
		REQUEST_ID = Integer.parseInt(serverSettings.getProperty("RequestServerID", "0"));
		ACCEPT_ALTERNATE_ID = Boolean.parseBoolean(serverSettings.getProperty("AcceptAlternateID", "True"));
		LOGIN_TRY_BEFORE_BAN = Integer.parseInt(serverSettings.getProperty("LoginTryBeforeBan", "10"));
		LOGIN_BLOCK_AFTER_BAN = Integer.parseInt(serverSettings.getProperty("LoginBlockAfterBan", "600"));
		GM_MIN = Integer.parseInt(serverSettings.getProperty("GMMinLevel", "100"));
		DATAPACK_ROOT = new File(serverSettings.getProperty("DatapackRoot", ".")).getCanonicalFile(); // FIXME:
		// in
		// login?
		INTERNAL_HOSTNAME = serverSettings.getProperty("InternalHostname", "localhost");
		EXTERNAL_HOSTNAME = serverSettings.getProperty("ExternalHostname", "localhost");
		DATABASE_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");
		DATABASE_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l2jdb");
		DATABASE_LOGIN = serverSettings.getProperty("Login", "root");
		DATABASE_PASSWORD = serverSettings.getProperty("Password", "");
		DATABASE_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaximumDbConnections", "50"));
		DATABASE_MAXSTATEMENTS = Integer.parseInt(serverSettings.getProperty("MaximumStateMents", "100"));
		DATABASE_MIN_POOLSIZE = Integer.parseInt(serverSettings.getProperty("MinPoolSize", "50"));
		DATABASE_MAX_POOLSIZE = Integer.parseInt(serverSettings.getProperty("MaxPoolSize", "10"));
		DATABASE_ACQUIREINCREMENT = Integer.parseInt(serverSettings.getProperty("AquireIncrement", "1"));
		DATABASE_IDLECONNECTIONTEST = Integer.parseInt(serverSettings.getProperty("IdleConnectionTest", "10800"));
		DATABASE_MAXIDLETIME = Integer.parseInt(serverSettings.getProperty("MaxIdleTime", "0"));
		SHOW_LICENCE = Boolean.parseBoolean(serverSettings.getProperty("ShowLicence", "True"));
		IP_UPDATE_TIME = Integer.parseInt(serverSettings.getProperty("IpUpdateTime", "0"));
		FORCE_GGAUTH = Boolean.parseBoolean(serverSettings.getProperty("ForceGGAuth", "False"));
		AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(serverSettings.getProperty("AutoCreateAccounts", "True"));
		FLOOD_PROTECTION = Boolean.parseBoolean(serverSettings.getProperty("EnableFloodProtection", "True"));
		FAST_CONNECTION_LIMIT = Integer.parseInt(serverSettings.getProperty("FastConnectionLimit", "15"));
		NORMAL_CONNECTION_TIME = Integer.parseInt(serverSettings.getProperty("NormalConnectionTime", "700"));
		FAST_CONNECTION_TIME = Integer.parseInt(serverSettings.getProperty("FastConnectionTime", "350"));
		MAX_CONNECTION_PER_IP = Integer.parseInt(serverSettings.getProperty("MaxConnectionPerIP", "50"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + CONFIGURATION_FILE + " File.");
	    }
	    // telnet
	    try
	    {
		Properties telnetSettings = new Properties();
		InputStream is = new FileInputStream(new File(TELNET_FILE));
		telnetSettings.load(is);
		is.close();
		IS_TELNET_ENABLED = Boolean.valueOf(telnetSettings.getProperty("EnableTelnet", "False"));
	    } catch (Exception e)
	    {
		e.printStackTrace();
		throw new Error("Failed to Load " + TELNET_FILE + " File.");
	    }
	} else
	{
	    _log.severe("Could not Load Config: server mode was not set");
	}
    }

    /**
     * Set a new value to a game parameter from the admin console.
     *
     * @param pName
     *                (String) : name of the parameter to change
     * @param pValue
     *                (String) : new value of the parameter
     * @return boolean : True if modification has been made
     * @link useAdminCommand
     */
    public static boolean setParameterValue(String pName, String pValue)
    {
	// Server settings
	if (pName.equalsIgnoreCase("RateXp"))
	{
	    RATE_XP = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateSp"))
	{
	    RATE_SP = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RatePartyXp"))
	{
	    RATE_PARTY_XP = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RatePartySp"))
	{
	    RATE_PARTY_SP = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateQuestsReward"))
	{
	    RATE_QUESTS_REWARD = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateDropAdena"))
	{
	    RATE_DROP_ADENA = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateConsumableCost"))
	{
	    RATE_CONSUMABLE_COST = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateDropItems"))
	{
	    RATE_DROP_ITEMS = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateDropSpoil"))
	{
	    RATE_DROP_SPOIL = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateDropManor"))
	{
	    RATE_DROP_MANOR = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AdenaBoss"))
	{
	    ADENA_BOSS = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("AdenaRaid"))
	{
	    ADENA_RAID = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("LifeCrystalNeeded"))
	{
	    LIFE_CRYSTAL_NEEDED = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AdenaMinon"))
	{
	    ADENA_MINON = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("ItemsBoss"))
	{
	    ITEMS_BOSS = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("ItemsRaid"))
	{
	    ITEMS_RAID = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("ItemsMinon"))
	{
	    ITEMS_MINON = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("SpoilBoss"))
	{
	    SPOIL_BOSS = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("SpoilRaid"))
	{
	    SPOIL_RAID = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("SpoilMinon"))
	{
	    SPOIL_MINON = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateDropQuest"))
	{
	    RATE_DROP_QUEST = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateKarmaExpLost"))
	{
	    RATE_KARMA_EXP_LOST = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("RateSiegeGuardsPrice"))
	{
	    RATE_SIEGE_GUARDS_PRICE = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("PlayerDropLimit"))
	{
	    PLAYER_DROP_LIMIT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PlayerRateDrop"))
	{
	    PLAYER_RATE_DROP = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PlayerRateDropItem"))
	{
	    PLAYER_RATE_DROP_ITEM = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PlayerRateDropEquip"))
	{
	    PLAYER_RATE_DROP_EQUIP = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PlayerRateDropEquipWeapon"))
	{
	    PLAYER_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("KarmaDropLimit"))
	{
	    KARMA_DROP_LIMIT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("KarmaRateDrop"))
	{
	    KARMA_RATE_DROP = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("KarmaRateDropItem"))
	{
	    KARMA_RATE_DROP_ITEM = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("KarmaRateDropEquip"))
	{
	    KARMA_RATE_DROP_EQUIP = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("KarmaRateDropEquipWeapon"))
	{
	    KARMA_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AutoDestroyDroppedItemAfter"))
	{
	    AUTODESTROY_ITEM_AFTER = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("DestroyPlayerDroppedItem"))
	{
	    DESTROY_DROPPED_PLAYER_ITEM = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("DestroyEquipableItem"))
	{
	    DESTROY_EQUIPABLE_PLAYER_ITEM = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("SaveDroppedItem"))
	{
	    SAVE_DROPPED_ITEM = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("EmptyDroppedItemTableAfterLoad"))
	{
	    EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("SaveDroppedItemInterval"))
	{
	    SAVE_DROPPED_ITEM_INTERVAL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ClearDroppedItemTable"))
	{
	    CLEAR_DROPPED_ITEM_TABLE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("PreciseDropCalculation"))
	{
	    PRECISE_DROP_CALCULATION = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("MultipleItemDrop"))
	{
	    MULTIPLE_ITEM_DROP = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("CoordSynchronize"))
	{
	    COORD_SYNCHRONIZE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("DeleteCharAfterDays"))
	{
	    DELETE_DAYS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AllowDiscardItem"))
	{
	    ALLOW_DISCARDITEM = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowFreight"))
	{
	    ALLOW_FREIGHT = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowWarehouse"))
	{
	    ALLOW_WAREHOUSE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowWear"))
	{
	    ALLOW_WEAR = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("WearDelay"))
	{
	    WEAR_DELAY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("WearPrice"))
	{
	    WEAR_PRICE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AllowWater"))
	{
	    ALLOW_WATER = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowRentPet"))
	{
	    ALLOW_RENTPET = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowBoat"))
	{
	    ALLOW_BOAT = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowCursedWeapons"))
	{
	    ALLOW_CURSED_WEAPONS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowManor"))
	{
	    ALLOW_MANOR = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("BypassValidation"))
	{
	    BYPASS_VALIDATION = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("CommunityType"))
	{
	    COMMUNITY_TYPE = pValue.toLowerCase();
	} else if (pName.equalsIgnoreCase("BBSDefault"))
	{
	    BBS_DEFAULT = pValue;
	} else if (pName.equalsIgnoreCase("ShowLevelOnCommunityBoard"))
	{
	    SHOW_LEVEL_COMMUNITYBOARD = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("ShowStatusOnCommunityBoard"))
	{
	    SHOW_STATUS_COMMUNITYBOARD = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("NamePageSizeOnCommunityBoard"))
	{
	    NAME_PAGE_SIZE_COMMUNITYBOARD = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("NamePerRowOnCommunityBoard"))
	{
	    NAME_PER_ROW_COMMUNITYBOARD = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ShowNpcLevel"))
	{
	    SHOW_NPC_LVL = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("ForceInventoryUpdate"))
	{
	    FORCE_INVENTORY_UPDATE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AutoDeleteInvalidQuestData"))
	{
	    AUTODELETE_INVALID_QUEST_DATA = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("MaximumOnlineUsers"))
	{
	    MAXIMUM_ONLINE_USERS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PacketProtection"))
	{
	    ENABLE_PACKET_PROTECTION = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("UnknownPacketsBeforeBan"))
	{
	    MAX_UNKNOWN_PACKETS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("UnknownPacketsPunishment"))
	{
	    UNKNOWN_PACKETS_PUNiSHMENT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ZoneTown"))
	{
	    ZONE_TOWN = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ShowGMLogin"))
	{
	    SHOW_GM_LOGIN = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("MaximumUpdateDistance"))
	{
	    MINIMUM_UPDATE_DISTANCE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MinimumUpdateTime"))
	{
	    MINIMUN_UPDATE_TIME = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("CheckKnownList"))
	{
	    CHECK_KNOWN = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("KnownListForgetDelay"))
	{
	    KNOWNLIST_FORGET_DELAY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("UseDeepBlueDropRules"))
	{
	    DEEPBLUE_DROP_RULES = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowGuards"))
	{
	    ALLOW_GUARDS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("CancelLesserEffect"))
	{
	    EFFECT_CANCELING = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("WyvernSpeed"))
	{
	    WYVERN_SPEED = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("StriderSpeed"))
	{
	    STRIDER_SPEED = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumSlotsForNoDwarf"))
	{
	    INVENTORY_MAXIMUM_NO_DWARF = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumSlotsForDwarf"))
	{
	    INVENTORY_MAXIMUM_DWARF = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumSlotsForGMPlayer"))
	{
	    INVENTORY_MAXIMUM_GM = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumWarehouseSlotsForNoDwarf"))
	{
	    WAREHOUSE_SLOTS_NO_DWARF = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumWarehouseSlotsForDwarf"))
	{
	    WAREHOUSE_SLOTS_DWARF = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumWarehouseSlotsForClan"))
	{
	    WAREHOUSE_SLOTS_CLAN = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaximumFreightSlots"))
	{
	    FREIGHT_SLOTS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceWeapon"))
	{
	    ENCHANT_CHANCE_WEAPON = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceArmor"))
	{
	    ENCHANT_CHANCE_ARMOR = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceJewelry"))
	{
	    ENCHANT_CHANCE_JEWELRY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceWeaponCrystal"))
	{
	    ENCHANT_CHANCE_WEAPON_CRYSTAL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceArmorCrystal"))
	{
	    ENCHANT_CHANCE_ARMOR_CRYSTAL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceWeaponBlessed"))
	{
	    ENCHANT_CHANCE_WEAPON_BLESSED = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantChanceArmorBlessed"))
	{
	    ENCHANT_CHANCE_ARMOR_BLESSED = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantMaxWeapon"))
	{
	    ENCHANT_MAX_WEAPON = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantMaxArmor"))
	{
	    ENCHANT_MAX_ARMOR = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantMaxJewelry"))
	{
	    ENCHANT_MAX_JEWELRY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantSafeMax"))
	{
	    ENCHANT_SAFE_MAX = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantSafeMaxFull"))
	{
	    ENCHANT_SAFE_MAX_FULL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("GMOverEnchant"))
	{
	    GM_OVER_ENCHANT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("HpRegenMultiplier"))
	{
	    HP_REGEN_MULTIPLIER = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("MpRegenMultiplier"))
	{
	    MP_REGEN_MULTIPLIER = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("CpRegenMultiplier"))
	{
	    CP_REGEN_MULTIPLIER = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("RaidHpRegenMultiplier"))
	{
	    RAID_HP_REGEN_MULTIPLIER = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("RaidMpRegenMultiplier"))
	{
	    RAID_MP_REGEN_MULTIPLIER = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("RaidDefenceMultiplier"))
	{
	    RAID_DEFENCE_MULTIPLIER = Double.parseDouble(pValue) / 100;
	} else if (pName.equalsIgnoreCase("RaidMinionRespawnTime"))
	{
	    RAID_MINION_RESPAWN_TIMER = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("StartingAdena"))
	{
	    STARTING_ADENA = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("StartingAA"))
	{
	    STARTING_AA = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("UnstuckInterval"))
	{
	    UNSTUCK_INTERVAL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PlayerSpawnProtection"))
	{
	    PLAYER_SPAWN_PROTECTION = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PlayerFakeDeathUpProtection"))
	{
	    PLAYER_FAKEDEATH_UP_PROTECTION = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PartyXpCutoffMethod"))
	{
	    PARTY_XP_CUTOFF_METHOD = pValue;
	} else if (pName.equalsIgnoreCase("PartyXpCutoffPercent"))
	{
	    PARTY_XP_CUTOFF_PERCENT = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("PartyXpCutoffLevel"))
	{
	    PARTY_XP_CUTOFF_LEVEL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("RespawnRestoreCP"))
	{
	    RESPAWN_RESTORE_CP = Double.parseDouble(pValue) / 100;
	} else if (pName.equalsIgnoreCase("RespawnRestoreHP"))
	{
	    RESPAWN_RESTORE_HP = Double.parseDouble(pValue) / 100;
	} else if (pName.equalsIgnoreCase("RespawnRestoreMP"))
	{
	    RESPAWN_RESTORE_MP = Double.parseDouble(pValue) / 100;
	} else if (pName.equalsIgnoreCase("MaxPvtStoreSlotsDwarf"))
	{
	    MAX_PVTSTORE_SLOTS_DWARF = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaxPvtStoreSlotsOther"))
	{
	    MAX_PVTSTORE_SLOTS_OTHER = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("StoreSkillCooltime"))
	{
	    STORE_SKILL_COOLTIME = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AnnounceMammonSpawn"))
	{
	    ANNOUNCE_MAMMON_SPAWN = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameTiredness"))
	{
	    ALT_GAME_TIREDNESS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameCreation"))
	{
	    ALT_GAME_CREATION = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameCreationSpeed"))
	{
	    ALT_GAME_CREATION_SPEED = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("AltGameCreationXpRate"))
	{
	    ALT_GAME_CREATION_XP_RATE = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("AltGameCreationSpRate"))
	{
	    ALT_GAME_CREATION_SP_RATE = Double.parseDouble(pValue);
	} else if (pName.equalsIgnoreCase("AltBlacksmithUseRecipes"))
	{
	    ALT_BLACKSMITH_USE_RECIPES = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameSkillLearn"))
	{
	    ALT_GAME_SKILL_LEARN = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("MaxPAtkSpeed"))
	{
	    MAX_PATK_SPEED = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaxMAtkSpeed"))
	{
	    MAX_MATK_SPEED = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("EnchantSkillSpBookNeeded"))
	{
	    ES_SP_BOOK_NEEDED = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("FrontBlow"))
	{
	    FRONT_BLOW_SUCCESS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("BackBlow"))
	{
	    BACK_BLOW_SUCCESS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("SideBlow"))
	{
	    SIDE_BLOW_SUCCESS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AltGameCancelByHit"))
	{
	    ALT_GAME_CANCEL_BOW = pValue.equalsIgnoreCase("bow") || pValue.equalsIgnoreCase("all");
	    ALT_GAME_CANCEL_CAST = pValue.equalsIgnoreCase("cast") || pValue.equalsIgnoreCase("all");
	} else if (pName.equalsIgnoreCase("AltShieldBlocks"))
	{
	    ALT_GAME_SHIELD_BLOCKS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltPerfectShieldBlockRate"))
	{
	    ALT_PERFECT_SHLD_BLOCK = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("Delevel"))
	{
	    ALT_GAME_DELEVEL = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("MagicFailures"))
	{
	    ALT_GAME_MAGICFAILURES = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameMobAttackAI"))
	{
	    ALT_GAME_MOB_ATTACK_AI = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltMobAgroInPeaceZone"))
	{
	    ALT_MOB_AGRO_IN_PEACEZONE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameExponentXp"))
	{
	    ALT_GAME_EXPONENT_XP = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("AltGameExponentSp"))
	{
	    ALT_GAME_EXPONENT_SP = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("AllowClassMaster"))
	{
	    ALLOW_CLASS_MASTERS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowFirstClass"))
	{
	    ALLOW_CLASS_FIRST = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowSecondClass"))
	{
	    ALLOW_CLASS_SECOND = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowThirdClass"))
	{
	    ALLOW_CLASS_THIRD = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameFreights"))
	{
	    ALT_GAME_FREIGHTS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltGameFreightPrice"))
	{
	    ALT_GAME_FREIGHT_PRICE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AltPartyRange"))
	{
	    ALT_PARTY_RANGE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AltPartyRange2"))
	{
	    ALT_PARTY_RANGE2 = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("CraftingEnabled"))
	{
	    IS_CRAFTING_ENABLED = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AutoLoot"))
	{
	    AUTO_LOOT = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AutoLootHerbs"))
	{
	    AUTO_LOOT_HERBS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AllowAutoHerbsCommand"))
	{
	    ALLOW_AUTOHERBS_CMD = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("DisableGradePenalties"))
	{
	    DISABLE_GRADE_PENALTIES = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("DisableWeightPenalties"))
	{
	    DISABLE_GRADE_PENALTIES = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("DisableWeightPenalties"))
	{
	    DONATOR_NAME_COLOR = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("DonatorColorName"))
	{
	    ALT_GAME_KARMA_PLAYER_CAN_BE_KILLED_IN_PEACEZONE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltKarmaPlayerCanShop"))
	{
	    ALT_GAME_KARMA_PLAYER_CAN_SHOP = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltKarmaPlayerCanUseGK"))
	{
	    ALT_GAME_KARMA_PLAYER_CAN_USE_GK = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltKarmaPlayerCanTeleport"))
	{
	    ALT_GAME_KARMA_PLAYER_CAN_TELEPORT = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltKarmaPlayerCanTrade"))
	{
	    ALT_GAME_KARMA_PLAYER_CAN_TRADE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltKarmaPlayerCanUseWareHouse"))
	{
	    ALT_GAME_KARMA_PLAYER_CAN_USE_WAREHOUSE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltRequireCastleForDawn"))
	{
	    ALT_GAME_REQUIRE_CASTLE_DAWN = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltRequireClanCastle"))
	{
	    ALT_GAME_REQUIRE_CLAN_CASTLE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltFreeTeleporting"))
	{
	    ALT_GAME_FREE_TELEPORT = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltSubClassWithoutQuests"))
	{
	    ALT_GAME_SUBCLASS_WITHOUT_QUESTS = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltNewCharAlwaysIsNewbie"))
	{
	    ALT_GAME_NEW_CHAR_ALWAYS_IS_NEWBIE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AltMembersCanWithdrawFromClanWH"))
	{
	    ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("DwarfRecipeLimit"))
	{
	    DWARF_RECIPE_LIMIT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("CommonRecipeLimit"))
	{
	    COMMON_RECIPE_LIMIT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionEnable"))
	{
	    CHAMPION_ENABLE = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("ChampionFrequency"))
	{
	    CHAMPION_FREQUENCY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionMinLevel"))
	{
	    CHAMPION_MIN_LVL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionMaxLevel"))
	{
	    CHAMPION_MAX_LVL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionHp"))
	{
	    CHAMPION_HP = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionHpRegen"))
	{
	    CHAMPION_HP_REGEN = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("ChampionRewards"))
	{
	    CHAMPION_REWARDS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionAdenasRewards"))
	{
	    CHAMPION_ADENAS_REWARDS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionAtk"))
	{
	    CHAMPION_ATK = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("ChampionSpdAtk"))
	{
	    CHAMPION_SPD_ATK = Float.parseFloat(pValue);
	} else if (pName.equalsIgnoreCase("ChampionRewardItem"))
	{
	    CHAMPION_REWARD = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionRewardItemID"))
	{
	    CHAMPION_REWARD_ID = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("ChampionRewardItemQty"))
	{
	    CHAMPION_REWARD_QTY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("AllowWedding"))
	{
	    ALLOW_WEDDING = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("WeddingPrice"))
	{
	    WEDDING_PRICE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("WeddingPunishInfidelity"))
	{
	    WEDDING_PUNISH_INFIDELITY = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("WeddingTeleport"))
	{
	    WEDDING_TELEPORT = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("WeddingTeleportPrice"))
	{
	    WEDDING_TELEPORT_PRICE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("WeddingTeleportInterval"))
	{
	    WEDDING_TELEPORT_INTERVAL = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("WeddingAllowSameSex"))
	{
	    WEDDING_SAMESEX = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("WeddingFormalWear"))
	{
	    WEDDING_FORMALWEAR = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("WeddingDivorceCosts"))
	{
	    WEDDING_DIVORCE_COSTS = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TvTEventEnabled"))
	{
	    TVT_EVENT_ENABLED = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("TvTEventParticipationTime"))
	{
	    TVT_EVENT_PARTICIPATION_TIME = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TvTEventRunningTime"))
	{
	    TVT_EVENT_RUNNING_TIME = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TvTEventParticipationNpcId"))
	{
	    TVT_EVENT_PARTICIPATION_NPC_ID = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("CTFAllowInterference"))
	{
	    CTF_ALLOW_INTERFERENCE = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("CTFAllowPotions"))
	{
	    CTF_ALLOW_POTIONS = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("CTFAllowSummon"))
	{
	    CTF_ALLOW_SUMMON = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("CTFOnStartRemoveAllEffects"))
	{
	    CTF_ON_START_REMOVE_ALL_EFFECTS = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("CTFOnStartUnsummonPet"))
	{
	    CTF_ON_START_UNSUMMON_PET = Boolean.parseBoolean(pValue);
	} else if (pName.equalsIgnoreCase("MinKarma"))
	{
	    KARMA_MIN_KARMA = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MaxKarma"))
	{
	    KARMA_MAX_KARMA = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("XPDivider"))
	{
	    KARMA_XP_DIVIDER = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("BaseKarmaLost"))
	{
	    KARMA_LOST_BASE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("CanGMDropEquipment"))
	{
	    KARMA_DROP_GM = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("AwardPKKillPVPPoint"))
	{
	    KARMA_AWARD_PK_KILL = Boolean.valueOf(pValue);
	} else if (pName.equalsIgnoreCase("MinimumPKRequiredToDrop"))
	{
	    KARMA_PK_LIMIT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PvPVsNormalTime"))
	{
	    PVP_NORMAL_TIME = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("PvPVsPvPTime"))
	{
	    PVP_PVP_TIME = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("GlobalChat"))
	{
	    DEFAULT_GLOBAL_CHAT = pValue;
	} else if (pName.equalsIgnoreCase("TradeChat"))
	{
	    DEFAULT_TRADE_CHAT = pValue;

	} else if (pName.equalsIgnoreCase("TimeInADayOfOpenADoor"))
	{
	    TIME_IN_A_DAY_OF_OPEN_A_DOOR = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TimeOfOpeningADoor"))
	{
	    TIME_OF_OPENING_A_DOOR = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TimeLimitOfInvade"))
	{
	    TIMELIMITOFINVADE = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TimeOfAttack"))
	{
	FS_TIME_ATTACK = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TimeOfCoolDown"))
	{
	FS_TIME_COOLDOWN = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TimeOfEntry"))
	{
	FS_TIME_ENTRY = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("TimeOfWarmUp"))
	{
	FS_TIME_WARMUP = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("NumberOfNecessaryPartyMembers"))
	{
	FS_PARTY_MEMBER_COUNT = Integer.parseInt(pValue);
	} else if (pName.equalsIgnoreCase("MenuStyle"))
	{
	    GM_ADMIN_MENU_STYLE = pValue;
	} else
	{
	    return false;
	}
	return true;
    }

    /*
     * Removed by Ahmed, Reason: This has no effect at all whether players
     * can use L2Walker or not.
     */
    /**
     * Allow the player to use L2Walker ?
     *
     * @param player
     *                (L2PcInstance) : Player trying to use L2Walker
     * @return boolean : True if (L2Walker allowed as a general rule) or
     *         (L2Walker client allowed for GM and player is a GM)
     *
     * public static boolean allowL2Walker(L2PcInstance player) { return
     * ALLOW_L2WALKER_CLIENT == L2WalkerAllowed.True ||
     * ALLOW_L2WALKER_CLIENT == L2WalkerAllowed.GM && player != null &&
     * player.isGM(); }
     */
    // it has no instancies
    private Config()
    {
    }

    /**
     * Save hexadecimal ID of the server in the properties file.
     *
     * @param string
     *                (String) : hexadecimal ID of the server to store
     * @see HEXID_FILE
     * @see saveHexid(String string, String fileName)
     * @link LoginServerThread
     */
    public static void saveHexid(int serverId, String string)
    {
	Config.saveHexid(serverId, string, HEXID_FILE);
    }

    /**
     * Save hexadecimal ID of the server in the properties file.
     *
     * @param hexId
     *                (String) : hexadecimal ID of the server to store
     * @param fileName
     *                (String) : name of the properties file
     */
    public static void saveHexid(int serverId, String hexId, String fileName)
    {
	try
	{
	    Properties hexSetting = new Properties();
	    File file = new File(fileName);
	    // Create a new empty file only if it doesn't exist
	    file.createNewFile();
	    OutputStream out = new FileOutputStream(file);
	    hexSetting.setProperty("ServerID", String.valueOf(serverId));
	    hexSetting.setProperty("HexID", hexId);
	    hexSetting.store(out, "the hexID to auth into login");
	    out.close();
	} catch (Exception e)
	{
	    _log.warning("Failed to save hex id to " + fileName + " File.");
	    e.printStackTrace();
	}
    }
}