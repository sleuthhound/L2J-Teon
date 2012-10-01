/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.util.FloodProtectorConfig;
import net.sf.l2j.util.StringUtil;

/**
 * This class contains global server configuration.<br>
 * It has static final fields initialized from configuration files.<br>
 * @author mkizub
 */
public final class Config
{
	protected static final Logger _log = Logger.getLogger(Config.class.getName());
	
	/**
	 * -------------------------------------------------- // Property File Definitions //-------------------------------------------------
	 */
	public static final String BANNED_IP_XML = "./config/banned.xml";
	public static final String CLANS_FILE = "./config/clans.properties";
	public static final String EVENTS_FILE = "./config/events.properties";
	public static final String FLOOD_PROTECTOR_FILE = "./config/floodprotector.properties";
	public static final String HEXID_FILE = "./config/hexid.txt";
	public static final String ID_CONFIG_FILE = "./config/idfactory.properties";
	public static final String LOGIN_CONFIGURATION_FILE = "./config/loginserver.properties";
	public static final String NPCS_FILE = "./config/npcs.properties";
	public static final String PLAYERS_FILE = "./config/players.properties";
	public static final String SERVER_FILE = "./config/server.properties";
	public static final String SIEGE_FILE = "./config/siege.properties";
	
	/**
	 * -------------------------------------------------- // Variable Definitions //-------------------------------------------------
	 */
	
	// --------------------------------------------------
	// Clans settings
	// --------------------------------------------------
	
	/** Clans */
	public static int ALT_CLAN_JOIN_DAYS;
	public static int ALT_CLAN_CREATE_DAYS;
	public static int ALT_CLAN_DISSOLVE_DAYS;
	public static int ALT_ALLY_JOIN_DAYS_WHEN_LEAVED;
	public static int ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED;
	public static int ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED;
	public static int ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED;
	public static int ALT_MAX_NUM_OF_CLANS_IN_ALLY;
	public static int ALT_CLAN_MEMBERS_FOR_WAR;
	public static boolean ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH;
	public static boolean REMOVE_CASTLE_CIRCLETS;
	
	/** Manor */
	public static int ALT_MANOR_REFRESH_TIME;
	public static int ALT_MANOR_REFRESH_MIN;
	public static int ALT_MANOR_APPROVE_TIME;
	public static int ALT_MANOR_APPROVE_MIN;
	public static int ALT_MANOR_MAINTENANCE_PERIOD;
	public static boolean ALT_MANOR_SAVE_ALL_ACTIONS;
	public static int ALT_MANOR_SAVE_PERIOD_RATE;
	
	/** Clan Hall function */
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
	
	// --------------------------------------------------
	// Events settings
	// --------------------------------------------------
	
	/** Olympiad */
	public static int ALT_OLY_START_TIME;
	public static int ALT_OLY_MIN;
	public static long ALT_OLY_CPERIOD;
	public static long ALT_OLY_BATTLE;
	public static long ALT_OLY_WPERIOD;
	public static long ALT_OLY_VPERIOD;
	public static int ALT_OLY_WAIT_TIME;
	public static int ALT_OLY_START_POINTS;
	public static int ALT_OLY_WEEKLY_POINTS;
	public static int ALT_OLY_MIN_MATCHES;
	public static int ALT_OLY_CLASSED;
	public static int ALT_OLY_NONCLASSED;
	public static int[][] ALT_OLY_CLASSED_REWARD;
	public static int[][] ALT_OLY_NONCLASSED_REWARD;
	public static int ALT_OLY_COMP_RITEM;
	public static int ALT_OLY_GP_PER_POINT;
	public static int ALT_OLY_HERO_POINTS;
	public static int ALT_OLY_RANK1_POINTS;
	public static int ALT_OLY_RANK2_POINTS;
	public static int ALT_OLY_RANK3_POINTS;
	public static int ALT_OLY_RANK4_POINTS;
	public static int ALT_OLY_RANK5_POINTS;
	public static int ALT_OLY_MAX_POINTS;
	public static int ALT_OLY_DIVIDER_CLASSED;
	public static int ALT_OLY_DIVIDER_NON_CLASSED;
	public static boolean ALT_OLY_ANNOUNCE_GAMES;
	
	/** SevenSigns Festival */
	public static boolean ALT_GAME_REQUIRE_CLAN_CASTLE;
	public static boolean ALT_GAME_CASTLE_DAWN;
	public static boolean ALT_GAME_CASTLE_DUSK;
	public static int ALT_FESTIVAL_MIN_PLAYER;
	public static int ALT_MAXIMUM_PLAYER_CONTRIB;
	public static long ALT_FESTIVAL_MANAGER_START;
	public static long ALT_FESTIVAL_LENGTH;
	public static long ALT_FESTIVAL_CYCLE_LENGTH;
	public static long ALT_FESTIVAL_FIRST_SPAWN;
	public static long ALT_FESTIVAL_FIRST_SWARM;
	public static long ALT_FESTIVAL_SECOND_SPAWN;
	public static long ALT_FESTIVAL_SECOND_SWARM;
	public static long ALT_FESTIVAL_CHEST_SPAWN;
	public static boolean ALT_SEVENSIGNS_LAZY_UPDATE;
	
	/** Four Sepulchers */
	public static int FS_TIME_ATTACK;
	public static int FS_TIME_COOLDOWN;
	public static int FS_TIME_ENTRY;
	public static int FS_TIME_WARMUP;
	public static int FS_PARTY_MEMBER_COUNT;
	
	/** dimensional rift */
	public static int RIFT_MIN_PARTY_SIZE;
	public static int RIFT_SPAWN_DELAY;
	public static int RIFT_MAX_JUMPS;
	public static int RIFT_AUTO_JUMPS_TIME_MIN;
	public static int RIFT_AUTO_JUMPS_TIME_MAX;
	public static int RIFT_ENTER_COST_RECRUIT;
	public static int RIFT_ENTER_COST_SOLDIER;
	public static int RIFT_ENTER_COST_OFFICER;
	public static int RIFT_ENTER_COST_CAPTAIN;
	public static int RIFT_ENTER_COST_COMMANDER;
	public static int RIFT_ENTER_COST_HERO;
	public static float RIFT_BOSS_ROOM_TIME_MUTIPLY;
	
	/** Wedding system */
	public static boolean ALLOW_WEDDING;
	public static int WEDDING_PRICE;
	public static boolean WEDDING_SAMESEX;
	public static boolean WEDDING_FORMALWEAR;
	
	/** Lottery */
	public static int ALT_LOTTERY_PRIZE;
	public static int ALT_LOTTERY_TICKET_PRICE;
	public static float ALT_LOTTERY_5_NUMBER_RATE;
	public static float ALT_LOTTERY_4_NUMBER_RATE;
	public static float ALT_LOTTERY_3_NUMBER_RATE;
	public static int ALT_LOTTERY_2_AND_1_NUMBER_PRIZE;
	
	// --------------------------------------------------
	// HexID
	// --------------------------------------------------
	
	public static int SERVER_ID;
	public static byte[] HEX_ID;
	
	// --------------------------------------------------
	// FloodProtectors
	// --------------------------------------------------
	
	public static FloodProtectorConfig FLOOD_PROTECTOR_ROLL_DICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_HERO_VOICE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SUBCLASS;
	public static FloodProtectorConfig FLOOD_PROTECTOR_DROP_ITEM;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SERVER_BYPASS;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MULTISELL;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MANUFACTURE;
	public static FloodProtectorConfig FLOOD_PROTECTOR_MANOR;
	public static FloodProtectorConfig FLOOD_PROTECTOR_SENDMAIL;
	public static FloodProtectorConfig FLOOD_PROTECTOR_CHARACTER_SELECT;
	
	// --------------------------------------------------
	// Loginserver
	// --------------------------------------------------
	
	public static String LOGIN_BIND_ADDRESS;
	public static int PORT_LOGIN;
	
	public static boolean ACCEPT_NEW_GAMESERVER;
	public static int REQUEST_ID;
	public static boolean ACCEPT_ALTERNATE_ID;
	
	public static int LOGIN_TRY_BEFORE_BAN;
	public static int LOGIN_BLOCK_AFTER_BAN;
	
	public static boolean LOG_LOGIN_CONTROLLER;
	
	public static boolean SHOW_LICENCE;
	public static int IP_UPDATE_TIME;
	public static boolean FORCE_GGAUTH;
	
	public static boolean AUTO_CREATE_ACCOUNTS;
	
	public static boolean FLOOD_PROTECTION;
	public static int FAST_CONNECTION_LIMIT;
	public static int NORMAL_CONNECTION_TIME;
	public static int FAST_CONNECTION_TIME;
	public static int MAX_CONNECTION_PER_IP;
	
	// --------------------------------------------------
	// NPCs / Monsters
	// --------------------------------------------------
	
	/** Champion Mod */
	public static boolean CHAMPION_ENABLE;
	public static int CHAMPION_FREQUENCY;
	public static int CHAMP_MIN_LVL;
	public static int CHAMP_MAX_LVL;
	public static int CHAMPION_HP;
	public static int CHAMPION_REWARDS;
	public static int CHAMPION_ADENAS_REWARDS;
	public static float CHAMPION_HP_REGEN;
	public static float CHAMPION_ATK;
	public static float CHAMPION_SPD_ATK;
	public static int CHAMPION_REWARD;
	public static int CHAMPION_REWARD_ID;
	public static int CHAMPION_REWARD_QTY;
	
	/** Misc */
	public static boolean ALLOW_CLASS_MASTERS;
	public static ClassMasterSettings CLASS_MASTER_SETTINGS;
	public static boolean ALLOW_ENTIRE_TREE;
	public static boolean ANNOUNCE_MAMMON_SPAWN;
	public static boolean ALT_GAME_MOB_ATTACK_AI;
	public static boolean ALT_MOB_AGRO_IN_PEACEZONE;
	public static boolean ALT_GAME_FREE_TELEPORT;
	public static boolean SHOW_NPC_LVL;
	public static boolean SHOW_NPC_CREST;
	public static boolean SHOW_SUMMON_CREST;
	
	/** Wyvern Manager */
	public static boolean WYVERN_ALLOW_UPGRADER;
	public static int WYVERN_REQUIRED_LEVEL;
	public static int WYVERN_REQUIRED_CRYSTALS;
	
	/** Raid Boss */
	public static double RAID_HP_REGEN_MULTIPLIER;
	public static double RAID_MP_REGEN_MULTIPLIER;
	public static double RAID_DEFENCE_MULTIPLIER;
	public static float RAID_MIN_RESPAWN_MULTIPLIER;
	public static float RAID_MAX_RESPAWN_MULTIPLIER;
	public static double RAID_MINION_RESPAWN_TIMER;
	
	public static boolean RAID_DISABLE_CURSE;
	public static int RAID_CHAOS_TIME;
	public static int GRAND_CHAOS_TIME;
	public static int MINION_CHAOS_TIME;
	
	/** Grand Boss */
	public static int SPAWN_INTERVAL_AQ;
	public static int RANDOM_SPAWN_TIME_AQ;
	public static int SPAWN_INTERVAL_BAIUM;
	public static int RANDOM_SPAWN_TIME_BAIUM;
	public static int SPAWN_INTERVAL_CORE;
	public static int RANDOM_SPAWN_TIME_CORE;
	public static int SPAWN_INTERVAL_FRINTEZZA;
	public static int RANDOM_SPAWN_TIME_FRINTEZZA;
	public static int SPAWN_INTERVAL_ORFEN;
	public static int RANDOM_SPAWN_TIME_ORFEN;
	public static int SPAWN_INTERVAL_ZAKEN;
	public static int RANDOM_SPAWN_TIME_ZAKEN;
	
	public static int SPAWN_INTERVAL_ANTHARAS;
	public static int RANDOM_SPAWN_TIME_ANTHARAS;
	public static int WAIT_TIME_ANTHARAS;
	
	public static int SPAWN_INTERVAL_VALAKAS;
	public static int RANDOM_SPAWN_TIME_VALAKAS;
	public static int WAIT_TIME_VALAKAS;
	
	/** IA */
	public static boolean GUARD_ATTACK_AGGRO_MOB;
	public static int MAX_DRIFT_RANGE;
	public static boolean MOVE_BASED_KNOWNLIST;
	public static long KNOWNLIST_UPDATE_INTERVAL;
	public static int MIN_NPC_ANIMATION;
	public static int MAX_NPC_ANIMATION;
	public static int MIN_MONSTER_ANIMATION;
	public static int MAX_MONSTER_ANIMATION;
	
	public static boolean GRIDS_ALWAYS_ON;
	public static int GRID_NEIGHBOR_TURNON_TIME;
	public static int GRID_NEIGHBOR_TURNOFF_TIME;
	
	// --------------------------------------------------
	// Players
	// --------------------------------------------------
	
	/** Misc */
	public static int STARTING_ADENA;
	public static boolean EFFECT_CANCELING;
	public static double HP_REGEN_MULTIPLIER;
	public static double MP_REGEN_MULTIPLIER;
	public static double CP_REGEN_MULTIPLIER;
	public static int PLAYER_SPAWN_PROTECTION;
	public static int PLAYER_FAKEDEATH_UP_PROTECTION;
	public static double RESPAWN_RESTORE_HP;
	public static boolean RESPAWN_RANDOM_ENABLED;
	public static int RESPAWN_RANDOM_MAX_OFFSET;
	public static int MAX_PVTSTORE_SLOTS_DWARF;
	public static int MAX_PVTSTORE_SLOTS_OTHER;
	public static boolean DEEPBLUE_DROP_RULES;
	public static boolean ALT_GAME_DELEVEL;
	public static int DEATH_PENALTY_CHANCE;
	
	/** Inventory & WH */
	public static int INVENTORY_MAXIMUM_NO_DWARF;
	public static int INVENTORY_MAXIMUM_DWARF;
	public static int INVENTORY_MAXIMUM_QUEST_ITEMS;
	public static int INVENTORY_MAXIMUM_PET;
	public static int MAX_ITEM_IN_PACKET;
	public static double ALT_WEIGHT_LIMIT;
	public static int WAREHOUSE_SLOTS_NO_DWARF;
	public static int WAREHOUSE_SLOTS_DWARF;
	public static int WAREHOUSE_SLOTS_CLAN;
	public static int FREIGHT_SLOTS;
	public static boolean ALT_GAME_FREIGHTS;
	public static int ALT_GAME_FREIGHT_PRICE;
	
	/** Enchant */
	public static double ENCHANT_CHANCE_WEAPON_MAGIC;
	public static double ENCHANT_CHANCE_WEAPON_MAGIC_15PLUS;
	public static double ENCHANT_CHANCE_WEAPON_NONMAGIC;
	public static double ENCHANT_CHANCE_WEAPON_NONMAGIC_15PLUS;
	public static double ENCHANT_CHANCE_ARMOR;
	public static int ENCHANT_MAX_WEAPON;
	public static int ENCHANT_MAX_ARMOR;
	public static int ENCHANT_SAFE_MAX;
	public static int ENCHANT_SAFE_MAX_FULL;
	
	/** Augmentations */
	public static int AUGMENTATION_NG_SKILL_CHANCE;
	public static int AUGMENTATION_NG_GLOW_CHANCE;
	public static int AUGMENTATION_MID_SKILL_CHANCE;
	public static int AUGMENTATION_MID_GLOW_CHANCE;
	public static int AUGMENTATION_HIGH_SKILL_CHANCE;
	public static int AUGMENTATION_HIGH_GLOW_CHANCE;
	public static int AUGMENTATION_TOP_SKILL_CHANCE;
	public static int AUGMENTATION_TOP_GLOW_CHANCE;
	public static int AUGMENTATION_BASESTAT_CHANCE;
	
	/** Karma & PvP */
	public static boolean KARMA_PLAYER_CAN_BE_KILLED_IN_PZ;
	public static boolean KARMA_PLAYER_CAN_SHOP;
	public static boolean KARMA_PLAYER_CAN_USE_GK;
	public static boolean KARMA_PLAYER_CAN_TELEPORT;
	public static boolean KARMA_PLAYER_CAN_TRADE;
	public static boolean KARMA_PLAYER_CAN_USE_WH;
	
	public static int KARMA_MIN_KARMA;
	public static int KARMA_MAX_KARMA;
	public static int KARMA_XP_DIVIDER;
	public static int KARMA_LOST_BASE;
	public static boolean KARMA_DROP_GM;
	public static boolean KARMA_AWARD_PK_KILL;
	public static int KARMA_PK_LIMIT;
	
	public static String KARMA_NONDROPPABLE_PET_ITEMS;
	public static String KARMA_NONDROPPABLE_ITEMS;
	public static int[] KARMA_LIST_NONDROPPABLE_PET_ITEMS;
	public static int[] KARMA_LIST_NONDROPPABLE_ITEMS;
	
	public static int PVP_NORMAL_TIME;
	public static int PVP_PVP_TIME;
	
	/** Party */
	public static String PARTY_XP_CUTOFF_METHOD;
	public static int PARTY_XP_CUTOFF_LEVEL;
	public static double PARTY_XP_CUTOFF_PERCENT;
	public static int ALT_PARTY_RANGE;
	public static int ALT_PARTY_RANGE2;
	
	/** GMs & Admin Stuff */
	public static boolean EVERYBODY_HAS_ADMIN_RIGHTS;
	public static int MASTERACCESS_LEVEL;
	public static int MASTERACCESS_NAME_COLOR;
	public static int MASTERACCESS_TITLE_COLOR;
	public static boolean GM_HERO_AURA;
	public static boolean GM_STARTUP_INVULNERABLE;
	public static boolean GM_STARTUP_INVISIBLE;
	public static boolean GM_STARTUP_SILENCE;
	public static boolean GM_STARTUP_AUTO_LIST;
	
	/** petitions */
	public static boolean PETITIONING_ALLOWED;
	public static int MAX_PETITIONS_PER_PLAYER;
	public static int MAX_PETITIONS_PENDING;
	
	/** Crafting **/
	public static boolean IS_CRAFTING_ENABLED;
	public static int DWARF_RECIPE_LIMIT;
	public static int COMMON_RECIPE_LIMIT;
	public static boolean ALT_BLACKSMITH_USE_RECIPES;
	
	/** Skills & Classes **/
	public static boolean AUTO_LEARN_SKILLS;
	public static boolean ALT_GAME_MAGICFAILURES;
	public static boolean ALT_GAME_SHIELD_BLOCKS;
	public static int ALT_PERFECT_SHLD_BLOCK;
	public static boolean LIFE_CRYSTAL_NEEDED;
	public static boolean SP_BOOK_NEEDED;
	public static boolean ES_SP_BOOK_NEEDED;
	public static boolean DIVINE_SP_BOOK_NEEDED;
	public static boolean ALT_GAME_SUBCLASS_WITHOUT_QUESTS;
	
	/** Buffs */
	public static boolean STORE_SKILL_COOLTIME;
	public static byte BUFFS_MAX_AMOUNT;
	
	// --------------------------------------------------
	// Server
	// --------------------------------------------------
	
	public static String GAMESERVER_HOSTNAME;
	public static int PORT_GAME;
	public static String EXTERNAL_HOSTNAME;
	public static String INTERNAL_HOSTNAME;
	public static int GAME_SERVER_LOGIN_PORT;
	public static String GAME_SERVER_LOGIN_HOST;
	
	/** Access to database */
	public static String DATABASE_DRIVER;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIME;
	public static File DATAPACK_ROOT;
	
	/** IDFactory */
	public static enum IdFactoryType
	{
		BitSet,
		Stack
	}
	
	public static IdFactoryType IDFACTORY_TYPE;
	public static boolean BAD_ID_CHECKING;
	
	/** serverList & Test */
	public static boolean SERVER_LIST_BRACKET;
	public static boolean SERVER_LIST_CLOCK;
	public static boolean SERVER_LIST_TESTSERVER;
	public static boolean SERVER_GMONLY;
	public static boolean TEST_SERVER;
	
	/** clients related */
	public static int DELETE_DAYS;
	public static int MAXIMUM_ONLINE_USERS;
	public static int MIN_PROTOCOL_REVISION;
	public static int MAX_PROTOCOL_REVISION;
	
	/** Jail & Punishements **/
	public static boolean JAIL_IS_PVP;
	public static int DEFAULT_PUNISH;
	public static int DEFAULT_PUNISH_PARAM;
	
	/** Auto-loot */
	public static boolean AUTO_LOOT;
	public static boolean AUTO_LOOT_HERBS;
	public static boolean AUTO_LOOT_RAID;
	
	/** Items Management */
	public static boolean LAZY_ITEMS_UPDATE;
	public static boolean ALLOW_DISCARDITEM;
	public static boolean MULTIPLE_ITEM_DROP;
	public static int AUTODESTROY_ITEM_AFTER;
	public static int HERB_AUTO_DESTROY_TIME;
	public static String PROTECTED_ITEMS;
	
	public static List<Integer> LIST_PROTECTED_ITEMS;
	
	public static boolean DESTROY_DROPPED_PLAYER_ITEM;
	public static boolean DESTROY_EQUIPABLE_PLAYER_ITEM;
	public static boolean SAVE_DROPPED_ITEM;
	public static boolean EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD;
	public static int SAVE_DROPPED_ITEM_INTERVAL;
	public static boolean CLEAR_DROPPED_ITEM_TABLE;
	
	/** Rate control */
	public static float RATE_XP;
	public static float RATE_SP;
	public static float RATE_PARTY_XP;
	public static float RATE_PARTY_SP;
	public static float RATE_DROP_ADENA;
	public static float RATE_CONSUMABLE_COST;
	public static float RATE_DROP_ITEMS;
	public static float RATE_DROP_ITEMS_BY_RAID;
	public static float RATE_DROP_SPOIL;
	public static int RATE_DROP_MANOR;
	
	public static float RATE_QUEST_DROP;
	public static float RATE_QUEST_REWARD;
	public static float RATE_QUEST_REWARD_XP;
	public static float RATE_QUEST_REWARD_SP;
	public static float RATE_QUEST_REWARD_ADENA;
	
	public static float RATE_KARMA_EXP_LOST;
	public static float RATE_SIEGE_GUARDS_PRICE;
	
	public static int PLAYER_DROP_LIMIT;
	public static int PLAYER_RATE_DROP;
	public static int PLAYER_RATE_DROP_ITEM;
	public static int PLAYER_RATE_DROP_EQUIP;
	public static int PLAYER_RATE_DROP_EQUIP_WEAPON;
	
	public static int KARMA_DROP_LIMIT;
	public static int KARMA_RATE_DROP;
	public static int KARMA_RATE_DROP_ITEM;
	public static int KARMA_RATE_DROP_EQUIP;
	public static int KARMA_RATE_DROP_EQUIP_WEAPON;
	
	public static float PET_XP_RATE;
	public static int PET_FOOD_RATE;
	public static float SINEATER_XP_RATE;
	
	public static float RATE_DROP_COMMON_HERBS;
	public static float RATE_DROP_HP_HERBS;
	public static float RATE_DROP_MP_HERBS;
	public static float RATE_DROP_SPECIAL_HERBS;
	
	/** Allow types */
	public static boolean ALLOW_FREIGHT;
	public static boolean ALLOW_WAREHOUSE;
	public static boolean ALLOW_WEAR;
	public static int WEAR_DELAY;
	public static int WEAR_PRICE;
	public static boolean ALLOW_LOTTERY;
	public static boolean ALLOW_RACE;
	public static boolean ALLOW_WATER;
	public static boolean ALLOWFISHING;
	public static boolean ALLOW_BOAT;
	public static boolean ALLOW_CURSED_WEAPONS;
	public static boolean ALLOW_MANOR;
	public static boolean ENABLE_FALLING_DAMAGE;
	
	/** Debug & Dev */
	public static boolean ALT_DEV_NO_QUESTS;
	public static boolean ALT_DEV_NO_SPAWNS;
	public static boolean DEBUG;
	public static boolean DEVELOPER;
	public static boolean PACKET_HANDLER_DEBUG;
	
	/** Deadlock Detector */
	public static boolean DEADLOCK_DETECTOR;
	public static int DEADLOCK_CHECK_INTERVAL;
	public static boolean RESTART_ON_DEADLOCK;
	
	/** Logs */
	public static boolean LOG_CHAT;
	public static boolean LOG_ITEMS;
	public static boolean GMAUDIT;
	
	/** Community Board */
	public static boolean ENABLE_COMMUNITY_BOARD;
	public static String BBS_DEFAULT;
	
	/** Geodata */
	public static int COORD_SYNCHRONIZE;
	public static int GEODATA;
	public static boolean FORCE_GEODATA;
	
	public static boolean GEODATA_CELLFINDING;
	public static String PATHFIND_BUFFERS;
	public static float LOW_WEIGHT;
	public static float MEDIUM_WEIGHT;
	public static float HIGH_WEIGHT;
	public static boolean ADVANCED_DIAGONAL_STRATEGY;
	public static float DIAGONAL_WEIGHT;
	public static int MAX_POSTFILTER_PASSES;
	public static boolean DEBUG_PATH;
	
	/** Misc */
	public static boolean L2WALKER_PROTECTION;
	public static boolean FORCE_INVENTORY_UPDATE;
	public static boolean AUTODELETE_INVALID_QUEST_DATA;
	public static boolean GAMEGUARD_ENFORCE;
	public static boolean SERVER_NEWS;
	public static int ZONE_TOWN;
	public static boolean DISABLE_TUTORIAL;
	
	// --------------------------------------------------
	// Those "hidden" settings haven't configs to avoid admins to fuck their server
	// You still can experiment changing values here. But don't say I didn't warn you.
	// --------------------------------------------------
	
	/** Threads & Packets size */
	public static int THREAD_P_EFFECTS = 6; // default 6
	public static int THREAD_P_GENERAL = 15; // default 15
	public static int GENERAL_PACKET_THREAD_CORE_SIZE = 4; // default 4
	public static int IO_PACKET_THREAD_CORE_SIZE = 2; // default 2
	public static int GENERAL_THREAD_CORE_SIZE = 4; // default 4
	public static int AI_MAX_THREAD = 10; // default 10
	
	/** Packet information */
	public static boolean COUNT_PACKETS = false; // default false
	public static boolean DUMP_PACKET_COUNTS = false; // default false
	public static int DUMP_INTERVAL_SECONDS = 60; // default 60
	
	/** IA settings */
	public static int MINIMUM_UPDATE_DISTANCE = 50; // default 50
	public static int MINIMUN_UPDATE_TIME = 500; // default 500
	public static int KNOWNLIST_FORGET_DELAY = 10000; // default 10000
	
	/** Time after which a packet is considered as lost */
	public static int PACKET_LIFETIME = 0; // default 0 (unlimited)
	
	/** Reserve Host on LoginServerThread */
	public static boolean RESERVE_HOST_ON_LOGIN = false; // default false
	
	/** MMO settings */
	public static int MMO_SELECTOR_SLEEP_TIME = 20; // default 20
	public static int MMO_MAX_SEND_PER_PASS = 12; // default 12
	public static int MMO_MAX_READ_PER_PASS = 12; // default 12
	public static int MMO_HELPER_BUFFER_COUNT = 20; // default 20
	
	/** Client Packets Queue settings */
	public static int CLIENT_PACKET_QUEUE_SIZE = 14; // default MMO_MAX_READ_PER_PASS + 2
	public static int CLIENT_PACKET_QUEUE_MAX_BURST_SIZE = 13; // default MMO_MAX_READ_PER_PASS + 1
	public static int CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND = 80; // default 80
	public static int CLIENT_PACKET_QUEUE_MEASURE_INTERVAL = 5; // default 5
	public static int CLIENT_PACKET_QUEUE_MAX_AVERAGE_PACKETS_PER_SECOND = 40; // default 40
	public static int CLIENT_PACKET_QUEUE_MAX_FLOODS_PER_MIN = 2; // default 2
	public static int CLIENT_PACKET_QUEUE_MAX_OVERFLOWS_PER_MIN = 1; // default 1
	public static int CLIENT_PACKET_QUEUE_MAX_UNDERFLOWS_PER_MIN = 1; // default 1
	public static int CLIENT_PACKET_QUEUE_MAX_UNKNOWN_PER_MIN = 5; // default 5
	
	// --------------------------------------------------
	
	/**
	 * This class initializes all global variables for configuration.<br>
	 * If key doesn't appear in properties file, a default value is setting on by this class.
	 */
	public static void load()
	{
		if (Server.serverMode == Server.MODE_GAMESERVER)
		{
			_log.info("Loading flood protectors.");
			FLOOD_PROTECTOR_ROLL_DICE = new FloodProtectorConfig("RollDiceFloodProtector");
			FLOOD_PROTECTOR_HERO_VOICE = new FloodProtectorConfig("HeroVoiceFloodProtector");
			FLOOD_PROTECTOR_SUBCLASS = new FloodProtectorConfig("SubclassFloodProtector");
			FLOOD_PROTECTOR_DROP_ITEM = new FloodProtectorConfig("DropItemFloodProtector");
			FLOOD_PROTECTOR_SERVER_BYPASS = new FloodProtectorConfig("ServerBypassFloodProtector");
			FLOOD_PROTECTOR_MULTISELL = new FloodProtectorConfig("MultiSellFloodProtector");
			FLOOD_PROTECTOR_MANUFACTURE = new FloodProtectorConfig("ManufactureFloodProtector");
			FLOOD_PROTECTOR_MANOR = new FloodProtectorConfig("ManorFloodProtector");
			FLOOD_PROTECTOR_SENDMAIL = new FloodProtectorConfig("SendMailFloodProtector");
			FLOOD_PROTECTOR_CHARACTER_SELECT = new FloodProtectorConfig("CharacterSelectFloodProtector");
			
			_log.info("Loading gameserver configuration files.");
			
			// Clans settings
			try
			{
				Properties clans = new Properties();
				InputStream is = new FileInputStream(new File(CLANS_FILE));
				clans.load(is);
				is.close();
				
				ALT_CLAN_JOIN_DAYS = Integer.parseInt(clans.getProperty("DaysBeforeJoinAClan", "5"));
				ALT_CLAN_CREATE_DAYS = Integer.parseInt(clans.getProperty("DaysBeforeCreateAClan", "10"));
				ALT_MAX_NUM_OF_CLANS_IN_ALLY = Integer.parseInt(clans.getProperty("AltMaxNumOfClansInAlly", "3"));
				ALT_CLAN_MEMBERS_FOR_WAR = Integer.parseInt(clans.getProperty("AltClanMembersForWar", "15"));
				ALT_CLAN_DISSOLVE_DAYS = Integer.parseInt(clans.getProperty("DaysToPassToDissolveAClan", "7"));
				ALT_ALLY_JOIN_DAYS_WHEN_LEAVED = Integer.parseInt(clans.getProperty("DaysBeforeJoinAllyWhenLeaved", "1"));
				ALT_ALLY_JOIN_DAYS_WHEN_DISMISSED = Integer.parseInt(clans.getProperty("DaysBeforeJoinAllyWhenDismissed", "1"));
				ALT_ACCEPT_CLAN_DAYS_WHEN_DISMISSED = Integer.parseInt(clans.getProperty("DaysBeforeAcceptNewClanWhenDismissed", "1"));
				ALT_CREATE_ALLY_DAYS_WHEN_DISSOLVED = Integer.parseInt(clans.getProperty("DaysBeforeCreateNewAllyWhenDissolved", "10"));
				ALT_MEMBERS_CAN_WITHDRAW_FROM_CLANWH = Boolean.parseBoolean(clans.getProperty("AltMembersCanWithdrawFromClanWH", "False"));
				REMOVE_CASTLE_CIRCLETS = Boolean.parseBoolean(clans.getProperty("RemoveCastleCirclets", "True"));
				
				ALT_MANOR_REFRESH_TIME = Integer.parseInt(clans.getProperty("AltManorRefreshTime", "20"));
				ALT_MANOR_REFRESH_MIN = Integer.parseInt(clans.getProperty("AltManorRefreshMin", "00"));
				ALT_MANOR_APPROVE_TIME = Integer.parseInt(clans.getProperty("AltManorApproveTime", "6"));
				ALT_MANOR_APPROVE_MIN = Integer.parseInt(clans.getProperty("AltManorApproveMin", "00"));
				ALT_MANOR_MAINTENANCE_PERIOD = Integer.parseInt(clans.getProperty("AltManorMaintenancePeriod", "360000"));
				ALT_MANOR_SAVE_ALL_ACTIONS = Boolean.parseBoolean(clans.getProperty("AltManorSaveAllActions", "False"));
				ALT_MANOR_SAVE_PERIOD_RATE = Integer.parseInt(clans.getProperty("AltManorSavePeriodRate", "2"));
				
				CH_TELE_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallTeleportFunctionFeeRation", "86400000"));
				CH_TELE1_FEE = Integer.parseInt(clans.getProperty("ClanHallTeleportFunctionFeeLvl1", "86400000"));
				CH_TELE2_FEE = Integer.parseInt(clans.getProperty("ClanHallTeleportFunctionFeeLvl2", "86400000"));
				CH_SUPPORT_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallSupportFunctionFeeRation", "86400000"));
				CH_SUPPORT1_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl1", "86400000"));
				CH_SUPPORT2_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl2", "86400000"));
				CH_SUPPORT3_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl3", "86400000"));
				CH_SUPPORT4_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl4", "86400000"));
				CH_SUPPORT5_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl5", "86400000"));
				CH_SUPPORT6_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl6", "86400000"));
				CH_SUPPORT7_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl7", "86400000"));
				CH_SUPPORT8_FEE = Integer.parseInt(clans.getProperty("ClanHallSupportFeeLvl8", "86400000"));
				CH_MPREG_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallMpRegenerationFunctionFeeRation", "86400000"));
				CH_MPREG1_FEE = Integer.parseInt(clans.getProperty("ClanHallMpRegenerationFeeLvl1", "86400000"));
				CH_MPREG2_FEE = Integer.parseInt(clans.getProperty("ClanHallMpRegenerationFeeLvl2", "86400000"));
				CH_MPREG3_FEE = Integer.parseInt(clans.getProperty("ClanHallMpRegenerationFeeLvl3", "86400000"));
				CH_MPREG4_FEE = Integer.parseInt(clans.getProperty("ClanHallMpRegenerationFeeLvl4", "86400000"));
				CH_MPREG5_FEE = Integer.parseInt(clans.getProperty("ClanHallMpRegenerationFeeLvl5", "86400000"));
				CH_HPREG_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallHpRegenerationFunctionFeeRation", "86400000"));
				CH_HPREG1_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl1", "86400000"));
				CH_HPREG2_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl2", "86400000"));
				CH_HPREG3_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl3", "86400000"));
				CH_HPREG4_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl4", "86400000"));
				CH_HPREG5_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl5", "86400000"));
				CH_HPREG6_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl6", "86400000"));
				CH_HPREG7_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl7", "86400000"));
				CH_HPREG8_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl8", "86400000"));
				CH_HPREG9_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl9", "86400000"));
				CH_HPREG10_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl10", "86400000"));
				CH_HPREG11_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl11", "86400000"));
				CH_HPREG12_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl12", "86400000"));
				CH_HPREG13_FEE = Integer.parseInt(clans.getProperty("ClanHallHpRegenerationFeeLvl13", "86400000"));
				CH_EXPREG_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallExpRegenerationFunctionFeeRation", "86400000"));
				CH_EXPREG1_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl1", "86400000"));
				CH_EXPREG2_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl2", "86400000"));
				CH_EXPREG3_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl3", "86400000"));
				CH_EXPREG4_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl4", "86400000"));
				CH_EXPREG5_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl5", "86400000"));
				CH_EXPREG6_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl6", "86400000"));
				CH_EXPREG7_FEE = Integer.parseInt(clans.getProperty("ClanHallExpRegenerationFeeLvl7", "86400000"));
				CH_ITEM_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallItemCreationFunctionFeeRation", "86400000"));
				CH_ITEM1_FEE = Integer.parseInt(clans.getProperty("ClanHallItemCreationFunctionFeeLvl1", "86400000"));
				CH_ITEM2_FEE = Integer.parseInt(clans.getProperty("ClanHallItemCreationFunctionFeeLvl2", "86400000"));
				CH_ITEM3_FEE = Integer.parseInt(clans.getProperty("ClanHallItemCreationFunctionFeeLvl3", "86400000"));
				CH_CURTAIN_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallCurtainFunctionFeeRation", "86400000"));
				CH_CURTAIN1_FEE = Integer.parseInt(clans.getProperty("ClanHallCurtainFunctionFeeLvl1", "86400000"));
				CH_CURTAIN2_FEE = Integer.parseInt(clans.getProperty("ClanHallCurtainFunctionFeeLvl2", "86400000"));
				CH_FRONT_FEE_RATIO = Long.parseLong(clans.getProperty("ClanHallFrontPlatformFunctionFeeRation", "86400000"));
				CH_FRONT1_FEE = Integer.parseInt(clans.getProperty("ClanHallFrontPlatformFunctionFeeLvl1", "86400000"));
				CH_FRONT2_FEE = Integer.parseInt(clans.getProperty("ClanHallFrontPlatformFunctionFeeLvl2", "86400000"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Server failed to load " + CLANS_FILE + " file.");
			}
			
			// Events config
			try
			{
				Properties events = new Properties();
				InputStream is = new FileInputStream(new File(EVENTS_FILE));
				events.load(is);
				is.close();
				
				ALT_OLY_START_TIME = Integer.parseInt(events.getProperty("AltOlyStartTime", "18"));
				ALT_OLY_MIN = Integer.parseInt(events.getProperty("AltOlyMin", "00"));
				ALT_OLY_CPERIOD = Long.parseLong(events.getProperty("AltOlyCPeriod", "21600000"));
				ALT_OLY_BATTLE = Long.parseLong(events.getProperty("AltOlyBattle", "180000"));
				ALT_OLY_WPERIOD = Long.parseLong(events.getProperty("AltOlyWPeriod", "604800000"));
				ALT_OLY_VPERIOD = Long.parseLong(events.getProperty("AltOlyVPeriod", "86400000"));
				ALT_OLY_WAIT_TIME = Integer.parseInt(events.getProperty("AltOlyWaitTime", "30"));
				ALT_OLY_START_POINTS = Integer.parseInt(events.getProperty("AltOlyStartPoints", "18"));
				ALT_OLY_WEEKLY_POINTS = Integer.parseInt(events.getProperty("AltOlyWeeklyPoints", "3"));
				ALT_OLY_MIN_MATCHES = Integer.parseInt(events.getProperty("AltOlyMinMatchesToBeClassed", "5"));
				ALT_OLY_CLASSED = Integer.parseInt(events.getProperty("AltOlyClassedParticipants", "5"));
				ALT_OLY_NONCLASSED = Integer.parseInt(events.getProperty("AltOlyNonClassedParticipants", "9"));
				ALT_OLY_CLASSED_REWARD = parseItemsList(events.getProperty("AltOlyClassedReward", "6651,50"));
				ALT_OLY_NONCLASSED_REWARD = parseItemsList(events.getProperty("AltOlyNonClassedReward", "6651,30"));
				ALT_OLY_COMP_RITEM = Integer.parseInt(events.getProperty("AltOlyCompRewItem", "6651"));
				ALT_OLY_GP_PER_POINT = Integer.parseInt(events.getProperty("AltOlyGPPerPoint", "1000"));
				ALT_OLY_HERO_POINTS = Integer.parseInt(events.getProperty("AltOlyHeroPoints", "300"));
				ALT_OLY_RANK1_POINTS = Integer.parseInt(events.getProperty("AltOlyRank1Points", "100"));
				ALT_OLY_RANK2_POINTS = Integer.parseInt(events.getProperty("AltOlyRank2Points", "75"));
				ALT_OLY_RANK3_POINTS = Integer.parseInt(events.getProperty("AltOlyRank3Points", "55"));
				ALT_OLY_RANK4_POINTS = Integer.parseInt(events.getProperty("AltOlyRank4Points", "40"));
				ALT_OLY_RANK5_POINTS = Integer.parseInt(events.getProperty("AltOlyRank5Points", "30"));
				ALT_OLY_MAX_POINTS = Integer.parseInt(events.getProperty("AltOlyMaxPoints", "10"));
				ALT_OLY_DIVIDER_CLASSED = Integer.parseInt(events.getProperty("AltOlyDividerClassed", "3"));
				ALT_OLY_DIVIDER_NON_CLASSED = Integer.parseInt(events.getProperty("AltOlyDividerNonClassed", "3"));
				ALT_OLY_ANNOUNCE_GAMES = Boolean.parseBoolean(events.getProperty("AltOlyAnnounceGames", "True"));
				
				ALT_GAME_REQUIRE_CLAN_CASTLE = Boolean.parseBoolean(events.getProperty("AltRequireClanCastle", "False"));
				ALT_GAME_CASTLE_DAWN = Boolean.parseBoolean(events.getProperty("AltCastleForDawn", "True"));
				ALT_GAME_CASTLE_DUSK = Boolean.parseBoolean(events.getProperty("AltCastleForDusk", "True"));
				ALT_FESTIVAL_MIN_PLAYER = Integer.parseInt(events.getProperty("AltFestivalMinPlayer", "5"));
				ALT_MAXIMUM_PLAYER_CONTRIB = Integer.parseInt(events.getProperty("AltMaxPlayerContrib", "1000000"));
				ALT_FESTIVAL_MANAGER_START = Long.parseLong(events.getProperty("AltFestivalManagerStart", "120000"));
				ALT_FESTIVAL_LENGTH = Long.parseLong(events.getProperty("AltFestivalLength", "1080000"));
				ALT_FESTIVAL_CYCLE_LENGTH = Long.parseLong(events.getProperty("AltFestivalCycleLength", "2280000"));
				ALT_FESTIVAL_FIRST_SPAWN = Long.parseLong(events.getProperty("AltFestivalFirstSpawn", "120000"));
				ALT_FESTIVAL_FIRST_SWARM = Long.parseLong(events.getProperty("AltFestivalFirstSwarm", "300000"));
				ALT_FESTIVAL_SECOND_SPAWN = Long.parseLong(events.getProperty("AltFestivalSecondSpawn", "540000"));
				ALT_FESTIVAL_SECOND_SWARM = Long.parseLong(events.getProperty("AltFestivalSecondSwarm", "720000"));
				ALT_FESTIVAL_CHEST_SPAWN = Long.parseLong(events.getProperty("AltFestivalChestSpawn", "900000"));
				ALT_SEVENSIGNS_LAZY_UPDATE = Boolean.parseBoolean(events.getProperty("AltSevenSignsLazyUpdate", "True"));
				
				FS_TIME_ATTACK = Integer.parseInt(events.getProperty("TimeOfAttack", "50"));
				FS_TIME_COOLDOWN = Integer.parseInt(events.getProperty("TimeOfCoolDown", "5"));
				FS_TIME_ENTRY = Integer.parseInt(events.getProperty("TimeOfEntry", "3"));
				FS_TIME_WARMUP = Integer.parseInt(events.getProperty("TimeOfWarmUp", "2"));
				FS_PARTY_MEMBER_COUNT = Integer.parseInt(events.getProperty("NumberOfNecessaryPartyMembers", "4"));
				
				// Security valves :P
				if (FS_TIME_ATTACK <= 0)
					FS_TIME_ATTACK = 50;
				if (FS_TIME_COOLDOWN <= 0)
					FS_TIME_COOLDOWN = 5;
				if (FS_TIME_ENTRY <= 0)
					FS_TIME_ENTRY = 3;
				if (FS_TIME_WARMUP <= 0)
					FS_TIME_WARMUP = 2;
				if (FS_PARTY_MEMBER_COUNT <= 0)
					FS_PARTY_MEMBER_COUNT = 4;
				
				RIFT_MIN_PARTY_SIZE = Integer.parseInt(events.getProperty("RiftMinPartySize", "2"));
				RIFT_MAX_JUMPS = Integer.parseInt(events.getProperty("MaxRiftJumps", "4"));
				RIFT_SPAWN_DELAY = Integer.parseInt(events.getProperty("RiftSpawnDelay", "10000"));
				RIFT_AUTO_JUMPS_TIME_MIN = Integer.parseInt(events.getProperty("AutoJumpsDelayMin", "480"));
				RIFT_AUTO_JUMPS_TIME_MAX = Integer.parseInt(events.getProperty("AutoJumpsDelayMax", "600"));
				RIFT_ENTER_COST_RECRUIT = Integer.parseInt(events.getProperty("RecruitCost", "18"));
				RIFT_ENTER_COST_SOLDIER = Integer.parseInt(events.getProperty("SoldierCost", "21"));
				RIFT_ENTER_COST_OFFICER = Integer.parseInt(events.getProperty("OfficerCost", "24"));
				RIFT_ENTER_COST_CAPTAIN = Integer.parseInt(events.getProperty("CaptainCost", "27"));
				RIFT_ENTER_COST_COMMANDER = Integer.parseInt(events.getProperty("CommanderCost", "30"));
				RIFT_ENTER_COST_HERO = Integer.parseInt(events.getProperty("HeroCost", "33"));
				RIFT_BOSS_ROOM_TIME_MUTIPLY = Float.parseFloat(events.getProperty("BossRoomTimeMultiply", "1.0"));
				
				ALLOW_WEDDING = Boolean.parseBoolean(events.getProperty("AllowWedding", "False"));
				WEDDING_PRICE = Integer.parseInt(events.getProperty("WeddingPrice", "1000000"));
				WEDDING_SAMESEX = Boolean.parseBoolean(events.getProperty("WeddingAllowSameSex", "False"));
				WEDDING_FORMALWEAR = Boolean.parseBoolean(events.getProperty("WeddingFormalWear", "True"));
				
				ALT_LOTTERY_PRIZE = Integer.parseInt(events.getProperty("AltLotteryPrize", "50000"));
				ALT_LOTTERY_TICKET_PRICE = Integer.parseInt(events.getProperty("AltLotteryTicketPrice", "2000"));
				ALT_LOTTERY_5_NUMBER_RATE = Float.parseFloat(events.getProperty("AltLottery5NumberRate", "0.6"));
				ALT_LOTTERY_4_NUMBER_RATE = Float.parseFloat(events.getProperty("AltLottery4NumberRate", "0.2"));
				ALT_LOTTERY_3_NUMBER_RATE = Float.parseFloat(events.getProperty("AltLottery3NumberRate", "0.2"));
				ALT_LOTTERY_2_AND_1_NUMBER_PRIZE = Integer.parseInt(events.getProperty("AltLottery2and1NumberPrize", "200"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Server failed to load " + EVENTS_FILE + " file.");
			}
			
			// FloodProtector
			try
			{
				Properties security = new Properties();
				InputStream is = new FileInputStream(new File(FLOOD_PROTECTOR_FILE));
				security.load(is);
				
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_ROLL_DICE, "RollDice", "42");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_HERO_VOICE, "HeroVoice", "100");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SUBCLASS, "Subclass", "20");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_DROP_ITEM, "DropItem", "10");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SERVER_BYPASS, "ServerBypass", "5");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_MULTISELL, "MultiSell", "1");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_MANUFACTURE, "Manufacture", "3");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_MANOR, "Manor", "30");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_SENDMAIL, "SendMail", "100");
				loadFloodProtectorConfig(security, FLOOD_PROTECTOR_CHARACTER_SELECT, "CharacterSelect", "30");
			}
			catch (Exception e)
			{
				_log.warning("Server failed to load " + FLOOD_PROTECTOR_FILE + " file.");
			}
			
			// HexID
			try
			{
				Properties hexid = new Properties();
				InputStream is = new FileInputStream(HEXID_FILE);
				hexid.load(is);
				is.close();
				
				SERVER_ID = Integer.parseInt(hexid.getProperty("ServerID"));
				HEX_ID = new BigInteger(hexid.getProperty("HexID"), 16).toByteArray();
			}
			catch (Exception e)
			{
				_log.warning("Server failed to load " + HEXID_FILE + " file.");
			}
			
			// NPCs / Monsters
			try
			{
				Properties npcs = new Properties();
				InputStream is = new FileInputStream(new File(NPCS_FILE));
				npcs.load(is);
				is.close();
				
				CHAMPION_ENABLE = Boolean.parseBoolean(npcs.getProperty("ChampionEnable", "False"));
				CHAMPION_FREQUENCY = Integer.parseInt(npcs.getProperty("ChampionFrequency", "0"));
				CHAMP_MIN_LVL = Integer.parseInt(npcs.getProperty("ChampionMinLevel", "20"));
				CHAMP_MAX_LVL = Integer.parseInt(npcs.getProperty("ChampionMaxLevel", "60"));
				CHAMPION_HP = Integer.parseInt(npcs.getProperty("ChampionHp", "7"));
				CHAMPION_HP_REGEN = Float.parseFloat(npcs.getProperty("ChampionHpRegen", "1."));
				CHAMPION_REWARDS = Integer.parseInt(npcs.getProperty("ChampionRewards", "8"));
				CHAMPION_ADENAS_REWARDS = Integer.parseInt(npcs.getProperty("ChampionAdenasRewards", "1"));
				CHAMPION_ATK = Float.parseFloat(npcs.getProperty("ChampionAtk", "1."));
				CHAMPION_SPD_ATK = Float.parseFloat(npcs.getProperty("ChampionSpdAtk", "1."));
				CHAMPION_REWARD = Integer.parseInt(npcs.getProperty("ChampionRewardItem", "0"));
				CHAMPION_REWARD_ID = Integer.parseInt(npcs.getProperty("ChampionRewardItemID", "6393"));
				CHAMPION_REWARD_QTY = Integer.parseInt(npcs.getProperty("ChampionRewardItemQty", "1"));
				
				ALLOW_CLASS_MASTERS = Boolean.parseBoolean(npcs.getProperty("AllowClassMasters", "False"));
				ALLOW_ENTIRE_TREE = Boolean.parseBoolean(npcs.getProperty("AllowEntireTree", "False"));
				if (ALLOW_CLASS_MASTERS)
					CLASS_MASTER_SETTINGS = new ClassMasterSettings(npcs.getProperty("ConfigClassMaster"));
				
				ALT_GAME_FREE_TELEPORT = Boolean.parseBoolean(npcs.getProperty("AltFreeTeleporting", "False"));
				ANNOUNCE_MAMMON_SPAWN = Boolean.parseBoolean(npcs.getProperty("AnnounceMammonSpawn", "True"));
				ALT_GAME_MOB_ATTACK_AI = Boolean.parseBoolean(npcs.getProperty("AltGameMobAttackAI", "False"));
				ALT_MOB_AGRO_IN_PEACEZONE = Boolean.parseBoolean(npcs.getProperty("AltMobAgroInPeaceZone", "True"));
				SHOW_NPC_LVL = Boolean.parseBoolean(npcs.getProperty("ShowNpcLevel", "False"));
				SHOW_NPC_CREST = Boolean.parseBoolean(npcs.getProperty("ShowNpcCrest", "False"));
				SHOW_SUMMON_CREST = Boolean.parseBoolean(npcs.getProperty("ShowSummonCrest", "False"));
				
				WYVERN_ALLOW_UPGRADER = Boolean.parseBoolean(npcs.getProperty("AllowWyvernUpgrader", "True"));
				WYVERN_REQUIRED_LEVEL = Integer.parseInt(npcs.getProperty("RequiredStriderLevel", "55"));
				if (WYVERN_REQUIRED_LEVEL > 80 && WYVERN_REQUIRED_LEVEL < 1) // Sanity check
					WYVERN_REQUIRED_LEVEL = 55;
				WYVERN_REQUIRED_CRYSTALS = Integer.parseInt(npcs.getProperty("RequiredCrystalsNumber", "10"));
				
				RAID_HP_REGEN_MULTIPLIER = Double.parseDouble(npcs.getProperty("RaidHpRegenMultiplier", "100")) / 100;
				RAID_MP_REGEN_MULTIPLIER = Double.parseDouble(npcs.getProperty("RaidMpRegenMultiplier", "100")) / 100;
				RAID_DEFENCE_MULTIPLIER = Double.parseDouble(npcs.getProperty("RaidDefenceMultiplier", "100")) / 100;
				RAID_MINION_RESPAWN_TIMER = Integer.parseInt(npcs.getProperty("RaidMinionRespawnTime", "300000"));
				RAID_MIN_RESPAWN_MULTIPLIER = Float.parseFloat(npcs.getProperty("RaidMinRespawnMultiplier", "1.0"));
				RAID_MAX_RESPAWN_MULTIPLIER = Float.parseFloat(npcs.getProperty("RaidMaxRespawnMultiplier", "1.0"));
				
				RAID_DISABLE_CURSE = Boolean.parseBoolean(npcs.getProperty("DisableRaidCurse", "False"));
				RAID_CHAOS_TIME = Integer.parseInt(npcs.getProperty("RaidChaosTime", "10"));
				GRAND_CHAOS_TIME = Integer.parseInt(npcs.getProperty("GrandChaosTime", "10"));
				MINION_CHAOS_TIME = Integer.parseInt(npcs.getProperty("MinionChaosTime", "10"));
				
				WAIT_TIME_ANTHARAS = Integer.parseInt(npcs.getProperty("AntharasWaitTime", "30"));
				if (WAIT_TIME_ANTHARAS < 3 || WAIT_TIME_ANTHARAS > 60)
					WAIT_TIME_ANTHARAS = 30;
				WAIT_TIME_ANTHARAS = WAIT_TIME_ANTHARAS * 60000;
				
				WAIT_TIME_VALAKAS = Integer.parseInt(npcs.getProperty("ValakasWaitTime", "30"));
				if (WAIT_TIME_VALAKAS < 3 || WAIT_TIME_VALAKAS > 60)
					WAIT_TIME_VALAKAS = 30;
				WAIT_TIME_VALAKAS = WAIT_TIME_VALAKAS * 60000;
				
				SPAWN_INTERVAL_ANTHARAS = Integer.parseInt(npcs.getProperty("IntervalOfAntharasSpawn", "264"));
				if (SPAWN_INTERVAL_ANTHARAS < 1 || SPAWN_INTERVAL_ANTHARAS > 480)
					SPAWN_INTERVAL_ANTHARAS = 192;
				SPAWN_INTERVAL_ANTHARAS = SPAWN_INTERVAL_ANTHARAS * 3600000;
				
				RANDOM_SPAWN_TIME_ANTHARAS = Integer.parseInt(npcs.getProperty("RandomOfAntharasSpawn", "72"));
				if (RANDOM_SPAWN_TIME_ANTHARAS < 1 || RANDOM_SPAWN_TIME_ANTHARAS > 192)
					RANDOM_SPAWN_TIME_ANTHARAS = 145;
				RANDOM_SPAWN_TIME_ANTHARAS = RANDOM_SPAWN_TIME_ANTHARAS * 3600000;
				
				SPAWN_INTERVAL_VALAKAS = Integer.parseInt(npcs.getProperty("IntervalOfValakasSpawn", "264"));
				if (SPAWN_INTERVAL_VALAKAS < 1 || SPAWN_INTERVAL_VALAKAS > 480)
					SPAWN_INTERVAL_VALAKAS = 192;
				SPAWN_INTERVAL_VALAKAS = SPAWN_INTERVAL_VALAKAS * 3600000;
				
				RANDOM_SPAWN_TIME_VALAKAS = Integer.parseInt(npcs.getProperty("RandomOfValakasSpawn", "72"));
				if (RANDOM_SPAWN_TIME_VALAKAS < 1 || RANDOM_SPAWN_TIME_VALAKAS > 192)
					RANDOM_SPAWN_TIME_VALAKAS = 145;
				RANDOM_SPAWN_TIME_VALAKAS = RANDOM_SPAWN_TIME_VALAKAS * 3600000;
				
				SPAWN_INTERVAL_BAIUM = Integer.parseInt(npcs.getProperty("IntervalOfBaiumSpawn", "168"));
				if (SPAWN_INTERVAL_BAIUM < 1 || SPAWN_INTERVAL_BAIUM > 480)
					SPAWN_INTERVAL_BAIUM = 121;
				SPAWN_INTERVAL_BAIUM = SPAWN_INTERVAL_BAIUM * 3600000;
				
				RANDOM_SPAWN_TIME_BAIUM = Integer.parseInt(npcs.getProperty("RandomOfBaiumSpawn", "48"));
				if (RANDOM_SPAWN_TIME_BAIUM < 1 || RANDOM_SPAWN_TIME_BAIUM > 192)
					RANDOM_SPAWN_TIME_BAIUM = 8;
				RANDOM_SPAWN_TIME_BAIUM = RANDOM_SPAWN_TIME_BAIUM * 3600000;
				
				SPAWN_INTERVAL_CORE = Integer.parseInt(npcs.getProperty("IntervalOfCoreSpawn", "60"));
				if (SPAWN_INTERVAL_CORE < 1 || SPAWN_INTERVAL_CORE > 480)
					SPAWN_INTERVAL_CORE = 27;
				SPAWN_INTERVAL_CORE = SPAWN_INTERVAL_CORE * 3600000;
				
				RANDOM_SPAWN_TIME_CORE = Integer.parseInt(npcs.getProperty("RandomOfCoreSpawn", "24"));
				if (RANDOM_SPAWN_TIME_CORE < 1 || RANDOM_SPAWN_TIME_CORE > 192)
					RANDOM_SPAWN_TIME_CORE = 47;
				RANDOM_SPAWN_TIME_CORE = RANDOM_SPAWN_TIME_CORE * 3600000;
				
				SPAWN_INTERVAL_ORFEN = Integer.parseInt(npcs.getProperty("IntervalOfOrfenSpawn", "48"));
				if (SPAWN_INTERVAL_ORFEN < 1 || SPAWN_INTERVAL_ORFEN > 480)
					SPAWN_INTERVAL_ORFEN = 28;
				SPAWN_INTERVAL_ORFEN = SPAWN_INTERVAL_ORFEN * 3600000;
				
				RANDOM_SPAWN_TIME_ORFEN = Integer.parseInt(npcs.getProperty("RandomOfOrfenSpawn", "20"));
				if (RANDOM_SPAWN_TIME_ORFEN < 1 || RANDOM_SPAWN_TIME_ORFEN > 192)
					RANDOM_SPAWN_TIME_ORFEN = 41;
				RANDOM_SPAWN_TIME_ORFEN = RANDOM_SPAWN_TIME_ORFEN * 3600000;
				
				SPAWN_INTERVAL_AQ = Integer.parseInt(npcs.getProperty("IntervalOfQueenAntSpawn", "36"));
				if (SPAWN_INTERVAL_AQ < 1 || SPAWN_INTERVAL_AQ > 480)
					SPAWN_INTERVAL_AQ = 19;
				SPAWN_INTERVAL_AQ = SPAWN_INTERVAL_AQ * 3600000;
				
				RANDOM_SPAWN_TIME_AQ = Integer.parseInt(npcs.getProperty("RandomOfQueenAntSpawn", "17"));
				if (RANDOM_SPAWN_TIME_AQ < 1 || RANDOM_SPAWN_TIME_AQ > 192)
					RANDOM_SPAWN_TIME_AQ = 35;
				RANDOM_SPAWN_TIME_AQ = RANDOM_SPAWN_TIME_AQ * 3600000;
				
				SPAWN_INTERVAL_ZAKEN = Integer.parseInt(npcs.getProperty("IntervalOfZakenSpawn", "19"));
				if (SPAWN_INTERVAL_ZAKEN < 1 || SPAWN_INTERVAL_ZAKEN > 480)
					SPAWN_INTERVAL_ZAKEN = 19;
				SPAWN_INTERVAL_ZAKEN = SPAWN_INTERVAL_ZAKEN * 3600000;
				
				RANDOM_SPAWN_TIME_ZAKEN = Integer.parseInt(npcs.getProperty("RandomOfZakenSpawn", "35"));
				if (RANDOM_SPAWN_TIME_ZAKEN < 1 || RANDOM_SPAWN_TIME_ZAKEN > 192)
					RANDOM_SPAWN_TIME_ZAKEN = 35;
				RANDOM_SPAWN_TIME_ZAKEN = RANDOM_SPAWN_TIME_ZAKEN * 3600000;
				
				SPAWN_INTERVAL_FRINTEZZA = Integer.parseInt(npcs.getProperty("IntervalOfFrintezzaSpawn", "48"));
				if (SPAWN_INTERVAL_FRINTEZZA < 1 || SPAWN_INTERVAL_FRINTEZZA > 480)
					SPAWN_INTERVAL_FRINTEZZA = 121;
				SPAWN_INTERVAL_FRINTEZZA = SPAWN_INTERVAL_FRINTEZZA * 3600000;
				
				RANDOM_SPAWN_TIME_FRINTEZZA = Integer.parseInt(npcs.getProperty("RandomOfFrintezzaSpawn", "8"));
				if (RANDOM_SPAWN_TIME_FRINTEZZA < 1 || RANDOM_SPAWN_TIME_FRINTEZZA > 192)
					RANDOM_SPAWN_TIME_FRINTEZZA = 8;
				RANDOM_SPAWN_TIME_FRINTEZZA = RANDOM_SPAWN_TIME_FRINTEZZA * 3600000;
				
				GUARD_ATTACK_AGGRO_MOB = Boolean.parseBoolean(npcs.getProperty("GuardAttackAggroMob", "False"));
				MAX_DRIFT_RANGE = Integer.parseInt(npcs.getProperty("MaxDriftRange", "300"));
				MOVE_BASED_KNOWNLIST = Boolean.parseBoolean(npcs.getProperty("MoveBasedKnownlist", "False"));
				KNOWNLIST_UPDATE_INTERVAL = Long.parseLong(npcs.getProperty("KnownListUpdateInterval", "1250"));
				MIN_NPC_ANIMATION = Integer.parseInt(npcs.getProperty("MinNPCAnimation", "20"));
				MAX_NPC_ANIMATION = Integer.parseInt(npcs.getProperty("MaxNPCAnimation", "40"));
				MIN_MONSTER_ANIMATION = Integer.parseInt(npcs.getProperty("MinMonsterAnimation", "10"));
				MAX_MONSTER_ANIMATION = Integer.parseInt(npcs.getProperty("MaxMonsterAnimation", "40"));
				
				GRIDS_ALWAYS_ON = Boolean.parseBoolean(npcs.getProperty("GridsAlwaysOn", "False"));
				GRID_NEIGHBOR_TURNON_TIME = Integer.parseInt(npcs.getProperty("GridNeighborTurnOnTime", "1"));
				GRID_NEIGHBOR_TURNOFF_TIME = Integer.parseInt(npcs.getProperty("GridNeighborTurnOffTime", "90"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Server failed to load " + NPCS_FILE + " file.");
			}
			
			// players
			try
			{
				Properties players = new Properties();
				InputStream is = new FileInputStream(new File(PLAYERS_FILE));
				players.load(is);
				is.close();
				
				STARTING_ADENA = Integer.parseInt(players.getProperty("StartingAdena", "100"));
				EFFECT_CANCELING = Boolean.parseBoolean(players.getProperty("CancelLesserEffect", "True"));
				HP_REGEN_MULTIPLIER = Double.parseDouble(players.getProperty("HpRegenMultiplier", "100")) / 100;
				MP_REGEN_MULTIPLIER = Double.parseDouble(players.getProperty("MpRegenMultiplier", "100")) / 100;
				CP_REGEN_MULTIPLIER = Double.parseDouble(players.getProperty("CpRegenMultiplier", "100")) / 100;
				PLAYER_SPAWN_PROTECTION = Integer.parseInt(players.getProperty("PlayerSpawnProtection", "0"));
				PLAYER_FAKEDEATH_UP_PROTECTION = Integer.parseInt(players.getProperty("PlayerFakeDeathUpProtection", "0"));
				RESPAWN_RESTORE_HP = Double.parseDouble(players.getProperty("RespawnRestoreHP", "70")) / 100;
				RESPAWN_RANDOM_ENABLED = Boolean.parseBoolean(players.getProperty("RespawnRandomInTown", "False"));
				RESPAWN_RANDOM_MAX_OFFSET = Integer.parseInt(players.getProperty("RespawnRandomMaxOffset", "50"));
				MAX_PVTSTORE_SLOTS_DWARF = Integer.parseInt(players.getProperty("MaxPvtStoreSlotsDwarf", "5"));
				MAX_PVTSTORE_SLOTS_OTHER = Integer.parseInt(players.getProperty("MaxPvtStoreSlotsOther", "4"));
				DEEPBLUE_DROP_RULES = Boolean.parseBoolean(players.getProperty("UseDeepBlueDropRules", "True"));
				ALT_GAME_DELEVEL = Boolean.parseBoolean(players.getProperty("Delevel", "True"));
				DEATH_PENALTY_CHANCE = Integer.parseInt(players.getProperty("DeathPenaltyChance", "20"));
				
				INVENTORY_MAXIMUM_NO_DWARF = Integer.parseInt(players.getProperty("MaximumSlotsForNoDwarf", "80"));
				INVENTORY_MAXIMUM_DWARF = Integer.parseInt(players.getProperty("MaximumSlotsForDwarf", "100"));
				INVENTORY_MAXIMUM_QUEST_ITEMS = Integer.parseInt(players.getProperty("MaximumSlotsForQuestItems", "100"));
				INVENTORY_MAXIMUM_PET = Integer.parseInt(players.getProperty("MaximumSlotsForPet", "12"));
				MAX_ITEM_IN_PACKET = Math.max(INVENTORY_MAXIMUM_NO_DWARF, INVENTORY_MAXIMUM_DWARF);
				ALT_WEIGHT_LIMIT = Double.parseDouble(players.getProperty("AltWeightLimit", "1"));
				WAREHOUSE_SLOTS_NO_DWARF = Integer.parseInt(players.getProperty("MaximumWarehouseSlotsForNoDwarf", "100"));
				WAREHOUSE_SLOTS_DWARF = Integer.parseInt(players.getProperty("MaximumWarehouseSlotsForDwarf", "120"));
				WAREHOUSE_SLOTS_CLAN = Integer.parseInt(players.getProperty("MaximumWarehouseSlotsForClan", "150"));
				FREIGHT_SLOTS = Integer.parseInt(players.getProperty("MaximumFreightSlots", "20"));
				ALT_GAME_FREIGHTS = Boolean.parseBoolean(players.getProperty("AltGameFreights", "False"));
				ALT_GAME_FREIGHT_PRICE = Integer.parseInt(players.getProperty("AltGameFreightPrice", "1000"));
				
				ENCHANT_CHANCE_WEAPON_MAGIC = Double.parseDouble(players.getProperty("EnchantChanceMagicWeapon", "0.4"));
				ENCHANT_CHANCE_WEAPON_MAGIC_15PLUS = Double.parseDouble(players.getProperty("EnchantChanceMagicWeapon15Plus", "0.2"));
				ENCHANT_CHANCE_WEAPON_NONMAGIC = Double.parseDouble(players.getProperty("EnchantChanceNonMagicWeapon", "0.7"));
				ENCHANT_CHANCE_WEAPON_NONMAGIC_15PLUS = Double.parseDouble(players.getProperty("EnchantChanceNonMagicWeapon15Plus", "0.35"));
				ENCHANT_CHANCE_ARMOR = Double.parseDouble(players.getProperty("EnchantChanceArmor", "0.66"));
				ENCHANT_MAX_WEAPON = Integer.parseInt(players.getProperty("EnchantMaxWeapon", "0"));
				ENCHANT_MAX_ARMOR = Integer.parseInt(players.getProperty("EnchantMaxArmor", "0"));
				ENCHANT_SAFE_MAX = Integer.parseInt(players.getProperty("EnchantSafeMax", "3"));
				ENCHANT_SAFE_MAX_FULL = Integer.parseInt(players.getProperty("EnchantSafeMaxFull", "4"));
				
				AUGMENTATION_NG_SKILL_CHANCE = Integer.parseInt(players.getProperty("AugmentationNGSkillChance", "15"));
				AUGMENTATION_NG_GLOW_CHANCE = Integer.parseInt(players.getProperty("AugmentationNGGlowChance", "0"));
				AUGMENTATION_MID_SKILL_CHANCE = Integer.parseInt(players.getProperty("AugmentationMidSkillChance", "30"));
				AUGMENTATION_MID_GLOW_CHANCE = Integer.parseInt(players.getProperty("AugmentationMidGlowChance", "40"));
				AUGMENTATION_HIGH_SKILL_CHANCE = Integer.parseInt(players.getProperty("AugmentationHighSkillChance", "45"));
				AUGMENTATION_HIGH_GLOW_CHANCE = Integer.parseInt(players.getProperty("AugmentationHighGlowChance", "70"));
				AUGMENTATION_TOP_SKILL_CHANCE = Integer.parseInt(players.getProperty("AugmentationTopSkillChance", "60"));
				AUGMENTATION_TOP_GLOW_CHANCE = Integer.parseInt(players.getProperty("AugmentationTopGlowChance", "100"));
				AUGMENTATION_BASESTAT_CHANCE = Integer.parseInt(players.getProperty("AugmentationBaseStatChance", "1"));
				
				KARMA_PLAYER_CAN_BE_KILLED_IN_PZ = Boolean.parseBoolean(players.getProperty("KarmaPlayerCanBeKilledInPeaceZone", "False"));
				KARMA_PLAYER_CAN_SHOP = Boolean.parseBoolean(players.getProperty("KarmaPlayerCanShop", "True"));
				KARMA_PLAYER_CAN_USE_GK = Boolean.parseBoolean(players.getProperty("KarmaPlayerCanUseGK", "False"));
				KARMA_PLAYER_CAN_TELEPORT = Boolean.parseBoolean(players.getProperty("KarmaPlayerCanTeleport", "True"));
				KARMA_PLAYER_CAN_TRADE = Boolean.parseBoolean(players.getProperty("KarmaPlayerCanTrade", "True"));
				KARMA_PLAYER_CAN_USE_WH = Boolean.parseBoolean(players.getProperty("KarmaPlayerCanUseWareHouse", "True"));
				KARMA_MIN_KARMA = Integer.parseInt(players.getProperty("MinKarma", "240"));
				KARMA_MAX_KARMA = Integer.parseInt(players.getProperty("MaxKarma", "10000"));
				KARMA_XP_DIVIDER = Integer.parseInt(players.getProperty("XPDivider", "260"));
				KARMA_LOST_BASE = Integer.parseInt(players.getProperty("BaseKarmaLost", "0"));
				KARMA_DROP_GM = Boolean.parseBoolean(players.getProperty("CanGMDropEquipment", "false"));
				KARMA_AWARD_PK_KILL = Boolean.parseBoolean(players.getProperty("AwardPKKillPVPPoint", "true"));
				KARMA_PK_LIMIT = Integer.parseInt(players.getProperty("MinimumPKRequiredToDrop", "5"));
				KARMA_NONDROPPABLE_PET_ITEMS = players.getProperty("ListOfPetItems", "2375,3500,3501,3502,4422,4423,4424,4425,6648,6649,6650");
				KARMA_NONDROPPABLE_ITEMS = players.getProperty("ListOfNonDroppableItemsForPK", "1147,425,1146,461,10,2368,7,6,2370,2369");
				
				String[] array = KARMA_NONDROPPABLE_PET_ITEMS.split(",");
				KARMA_LIST_NONDROPPABLE_PET_ITEMS = new int[array.length];
				
				for (int i = 0; i < array.length; i++)
					KARMA_LIST_NONDROPPABLE_PET_ITEMS[i] = Integer.parseInt(array[i]);
				
				array = KARMA_NONDROPPABLE_ITEMS.split(",");
				KARMA_LIST_NONDROPPABLE_ITEMS = new int[array.length];
				
				for (int i = 0; i < array.length; i++)
					KARMA_LIST_NONDROPPABLE_ITEMS[i] = Integer.parseInt(array[i]);
				
				// sorting so binarySearch can be used later
				Arrays.sort(KARMA_LIST_NONDROPPABLE_PET_ITEMS);
				Arrays.sort(KARMA_LIST_NONDROPPABLE_ITEMS);
				
				PVP_NORMAL_TIME = Integer.parseInt(players.getProperty("PvPVsNormalTime", "15000"));
				PVP_PVP_TIME = Integer.parseInt(players.getProperty("PvPVsPvPTime", "30000"));
				
				PARTY_XP_CUTOFF_METHOD = players.getProperty("PartyXpCutoffMethod", "level");
				PARTY_XP_CUTOFF_PERCENT = Double.parseDouble(players.getProperty("PartyXpCutoffPercent", "3."));
				PARTY_XP_CUTOFF_LEVEL = Integer.parseInt(players.getProperty("PartyXpCutoffLevel", "20"));
				ALT_PARTY_RANGE = Integer.parseInt(players.getProperty("AltPartyRange", "1600"));
				ALT_PARTY_RANGE2 = Integer.parseInt(players.getProperty("AltPartyRange2", "1400"));
				
				EVERYBODY_HAS_ADMIN_RIGHTS = Boolean.parseBoolean(players.getProperty("EverybodyHasAdminRights", "False"));
				MASTERACCESS_LEVEL = Integer.parseInt(players.getProperty("MasterAccessLevel", "127"));
				MASTERACCESS_NAME_COLOR = Integer.decode(StringUtil.concat("0x", players.getProperty("MasterNameColor", "00FF00")));
				MASTERACCESS_TITLE_COLOR = Integer.decode(StringUtil.concat("0x", players.getProperty("MasterTitleColor", "00FF00")));
				GM_HERO_AURA = Boolean.parseBoolean(players.getProperty("GMHeroAura", "False"));
				GM_STARTUP_INVULNERABLE = Boolean.parseBoolean(players.getProperty("GMStartupInvulnerable", "True"));
				GM_STARTUP_INVISIBLE = Boolean.parseBoolean(players.getProperty("GMStartupInvisible", "True"));
				GM_STARTUP_SILENCE = Boolean.parseBoolean(players.getProperty("GMStartupSilence", "True"));
				GM_STARTUP_AUTO_LIST = Boolean.parseBoolean(players.getProperty("GMStartupAutoList", "True"));
				
				PETITIONING_ALLOWED = Boolean.parseBoolean(players.getProperty("PetitioningAllowed", "True"));
				MAX_PETITIONS_PER_PLAYER = Integer.parseInt(players.getProperty("MaxPetitionsPerPlayer", "5"));
				MAX_PETITIONS_PENDING = Integer.parseInt(players.getProperty("MaxPetitionsPending", "25"));
				
				IS_CRAFTING_ENABLED = Boolean.parseBoolean(players.getProperty("CraftingEnabled", "True"));
				DWARF_RECIPE_LIMIT = Integer.parseInt(players.getProperty("DwarfRecipeLimit", "50"));
				COMMON_RECIPE_LIMIT = Integer.parseInt(players.getProperty("CommonRecipeLimit", "50"));
				ALT_BLACKSMITH_USE_RECIPES = Boolean.parseBoolean(players.getProperty("AltBlacksmithUseRecipes", "True"));
				
				AUTO_LEARN_SKILLS = Boolean.parseBoolean(players.getProperty("AutoLearnSkills", "false"));
				ALT_GAME_MAGICFAILURES = Boolean.parseBoolean(players.getProperty("MagicFailures", "True"));
				ALT_GAME_SHIELD_BLOCKS = Boolean.parseBoolean(players.getProperty("AltShieldBlocks", "false"));
				ALT_PERFECT_SHLD_BLOCK = Integer.parseInt(players.getProperty("AltPerfectShieldBlockRate", "10"));
				LIFE_CRYSTAL_NEEDED = Boolean.parseBoolean(players.getProperty("LifeCrystalNeeded", "true"));
				SP_BOOK_NEEDED = Boolean.parseBoolean(players.getProperty("SpBookNeeded", "true"));
				ES_SP_BOOK_NEEDED = Boolean.parseBoolean(players.getProperty("EnchantSkillSpBookNeeded", "true"));
				DIVINE_SP_BOOK_NEEDED = Boolean.parseBoolean(players.getProperty("DivineInspirationSpBookNeeded", "true"));
				ALT_GAME_SUBCLASS_WITHOUT_QUESTS = Boolean.parseBoolean(players.getProperty("AltSubClassWithoutQuests", "False"));
				
				BUFFS_MAX_AMOUNT = Byte.parseByte(players.getProperty("MaxBuffsAmount", "20"));
				STORE_SKILL_COOLTIME = Boolean.parseBoolean(players.getProperty("StoreSkillCooltime", "true"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Server failed to load " + PLAYERS_FILE + " file.");
			}
			
			// server
			try
			{
				Properties server = new Properties();
				InputStream is = new FileInputStream(new File(SERVER_FILE));
				server.load(is);
				is.close();
				
				GAMESERVER_HOSTNAME = server.getProperty("GameserverHostname");
				PORT_GAME = Integer.parseInt(server.getProperty("GameserverPort", "7777"));
				
				EXTERNAL_HOSTNAME = server.getProperty("ExternalHostname", "*");
				INTERNAL_HOSTNAME = server.getProperty("InternalHostname", "*");
				
				GAME_SERVER_LOGIN_PORT = Integer.parseInt(server.getProperty("LoginPort", "9014"));
				GAME_SERVER_LOGIN_HOST = server.getProperty("LoginHost", "127.0.0.1");
				
				REQUEST_ID = Integer.parseInt(server.getProperty("RequestServerID", "0"));
				ACCEPT_ALTERNATE_ID = Boolean.parseBoolean(server.getProperty("AcceptAlternateID", "True"));
				
				DATABASE_DRIVER = server.getProperty("Driver", "com.mysql.jdbc.Driver");
				DATABASE_URL = server.getProperty("URL", "jdbc:mysql://localhost/acis");
				DATABASE_LOGIN = server.getProperty("Login", "root");
				DATABASE_PASSWORD = server.getProperty("Password", "");
				DATABASE_MAX_CONNECTIONS = Integer.parseInt(server.getProperty("MaximumDbConnections", "10"));
				DATABASE_MAX_IDLE_TIME = Integer.parseInt(server.getProperty("MaximumDbIdleTime", "0"));
				DATAPACK_ROOT = new File(server.getProperty("DatapackRoot", ".")).getCanonicalFile();
				
				IDFACTORY_TYPE = IdFactoryType.valueOf(server.getProperty("IDFactory", "BitSet"));
				BAD_ID_CHECKING = Boolean.parseBoolean(server.getProperty("BadIdChecking", "True"));
				
				SERVER_LIST_BRACKET = Boolean.parseBoolean(server.getProperty("ServerListBrackets", "false"));
				SERVER_LIST_CLOCK = Boolean.parseBoolean(server.getProperty("ServerListClock", "false"));
				SERVER_GMONLY = Boolean.parseBoolean(server.getProperty("ServerGMOnly", "false"));
				TEST_SERVER = Boolean.parseBoolean(server.getProperty("TestServer", "false"));
				SERVER_LIST_TESTSERVER = Boolean.parseBoolean(server.getProperty("TestServer", "false"));
				
				DELETE_DAYS = Integer.parseInt(server.getProperty("DeleteCharAfterDays", "7"));
				MAXIMUM_ONLINE_USERS = Integer.parseInt(server.getProperty("MaximumOnlineUsers", "100"));
				MIN_PROTOCOL_REVISION = Integer.parseInt(server.getProperty("MinProtocolRevision", "730"));
				MAX_PROTOCOL_REVISION = Integer.parseInt(server.getProperty("MaxProtocolRevision", "746"));
				if (MIN_PROTOCOL_REVISION > MAX_PROTOCOL_REVISION)
					throw new Error("MinProtocolRevision is bigger than MaxProtocolRevision in server.properties.");
				
				JAIL_IS_PVP = Boolean.parseBoolean(server.getProperty("JailIsPvp", "True"));
				DEFAULT_PUNISH = Integer.parseInt(server.getProperty("DefaultPunish", "2"));
				DEFAULT_PUNISH_PARAM = Integer.parseInt(server.getProperty("DefaultPunishParam", "0"));
				
				AUTO_LOOT = Boolean.parseBoolean(server.getProperty("AutoLoot", "False"));
				AUTO_LOOT_HERBS = Boolean.parseBoolean(server.getProperty("AutoLootHerbs", "False"));
				AUTO_LOOT_RAID = Boolean.parseBoolean(server.getProperty("AutoLootRaid", "False"));
				
				LAZY_ITEMS_UPDATE = Boolean.parseBoolean(server.getProperty("LazyItemsUpdate", "False"));
				ALLOW_DISCARDITEM = Boolean.parseBoolean(server.getProperty("AllowDiscardItem", "True"));
				MULTIPLE_ITEM_DROP = Boolean.parseBoolean(server.getProperty("MultipleItemDrop", "True"));
				AUTODESTROY_ITEM_AFTER = Integer.parseInt(server.getProperty("AutoDestroyDroppedItemAfter", "0"));
				HERB_AUTO_DESTROY_TIME = Integer.parseInt(server.getProperty("AutoDestroyHerbTime", "15")) * 1000;
				PROTECTED_ITEMS = server.getProperty("ListOfProtectedItems");
				
				LIST_PROTECTED_ITEMS = new ArrayList<>();
				for (String id : PROTECTED_ITEMS.split(","))
					LIST_PROTECTED_ITEMS.add(Integer.parseInt(id));
				
				DESTROY_DROPPED_PLAYER_ITEM = Boolean.parseBoolean(server.getProperty("DestroyPlayerDroppedItem", "False"));
				DESTROY_EQUIPABLE_PLAYER_ITEM = Boolean.parseBoolean(server.getProperty("DestroyEquipableItem", "False"));
				SAVE_DROPPED_ITEM = Boolean.parseBoolean(server.getProperty("SaveDroppedItem", "False"));
				EMPTY_DROPPED_ITEM_TABLE_AFTER_LOAD = Boolean.parseBoolean(server.getProperty("EmptyDroppedItemTableAfterLoad", "False"));
				SAVE_DROPPED_ITEM_INTERVAL = Integer.parseInt(server.getProperty("SaveDroppedItemInterval", "0")) * 60000;
				CLEAR_DROPPED_ITEM_TABLE = Boolean.parseBoolean(server.getProperty("ClearDroppedItemTable", "False"));
				
				RATE_XP = Float.parseFloat(server.getProperty("RateXp", "1."));
				RATE_SP = Float.parseFloat(server.getProperty("RateSp", "1."));
				RATE_PARTY_XP = Float.parseFloat(server.getProperty("RatePartyXp", "1."));
				RATE_PARTY_SP = Float.parseFloat(server.getProperty("RatePartySp", "1."));
				RATE_DROP_ADENA = Float.parseFloat(server.getProperty("RateDropAdena", "1."));
				RATE_CONSUMABLE_COST = Float.parseFloat(server.getProperty("RateConsumableCost", "1."));
				RATE_DROP_ITEMS = Float.parseFloat(server.getProperty("RateDropItems", "1."));
				RATE_DROP_ITEMS_BY_RAID = Float.parseFloat(server.getProperty("RateRaidDropItems", "1."));
				RATE_DROP_SPOIL = Float.parseFloat(server.getProperty("RateDropSpoil", "1."));
				RATE_DROP_MANOR = Integer.parseInt(server.getProperty("RateDropManor", "1"));
				RATE_QUEST_DROP = Float.parseFloat(server.getProperty("RateQuestDrop", "1."));
				RATE_QUEST_REWARD = Float.parseFloat(server.getProperty("RateQuestReward", "1."));
				RATE_QUEST_REWARD_XP = Float.parseFloat(server.getProperty("RateQuestRewardXP", "1."));
				RATE_QUEST_REWARD_SP = Float.parseFloat(server.getProperty("RateQuestRewardSP", "1."));
				RATE_QUEST_REWARD_ADENA = Float.parseFloat(server.getProperty("RateQuestRewardAdena", "1."));
				RATE_KARMA_EXP_LOST = Float.parseFloat(server.getProperty("RateKarmaExpLost", "1."));
				RATE_SIEGE_GUARDS_PRICE = Float.parseFloat(server.getProperty("RateSiegeGuardsPrice", "1."));
				RATE_DROP_COMMON_HERBS = Float.parseFloat(server.getProperty("RateCommonHerbs", "1."));
				RATE_DROP_HP_HERBS = Float.parseFloat(server.getProperty("RateHpHerbs", "1."));
				RATE_DROP_MP_HERBS = Float.parseFloat(server.getProperty("RateMpHerbs", "1."));
				RATE_DROP_SPECIAL_HERBS = Float.parseFloat(server.getProperty("RateSpecialHerbs", "1."));
				PLAYER_DROP_LIMIT = Integer.parseInt(server.getProperty("PlayerDropLimit", "3"));
				PLAYER_RATE_DROP = Integer.parseInt(server.getProperty("PlayerRateDrop", "5"));
				PLAYER_RATE_DROP_ITEM = Integer.parseInt(server.getProperty("PlayerRateDropItem", "70"));
				PLAYER_RATE_DROP_EQUIP = Integer.parseInt(server.getProperty("PlayerRateDropEquip", "25"));
				PLAYER_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(server.getProperty("PlayerRateDropEquipWeapon", "5"));
				PET_XP_RATE = Float.parseFloat(server.getProperty("PetXpRate", "1."));
				PET_FOOD_RATE = Integer.parseInt(server.getProperty("PetFoodRate", "1"));
				SINEATER_XP_RATE = Float.parseFloat(server.getProperty("SinEaterXpRate", "1."));
				KARMA_DROP_LIMIT = Integer.parseInt(server.getProperty("KarmaDropLimit", "10"));
				KARMA_RATE_DROP = Integer.parseInt(server.getProperty("KarmaRateDrop", "70"));
				KARMA_RATE_DROP_ITEM = Integer.parseInt(server.getProperty("KarmaRateDropItem", "50"));
				KARMA_RATE_DROP_EQUIP = Integer.parseInt(server.getProperty("KarmaRateDropEquip", "40"));
				KARMA_RATE_DROP_EQUIP_WEAPON = Integer.parseInt(server.getProperty("KarmaRateDropEquipWeapon", "10"));
				
				ALLOW_FREIGHT = Boolean.parseBoolean(server.getProperty("AllowFreight", "True"));
				ALLOW_WAREHOUSE = Boolean.parseBoolean(server.getProperty("AllowWarehouse", "True"));
				ALLOW_WEAR = Boolean.parseBoolean(server.getProperty("AllowWear", "True"));
				WEAR_DELAY = Integer.parseInt(server.getProperty("WearDelay", "5"));
				WEAR_PRICE = Integer.parseInt(server.getProperty("WearPrice", "10"));
				ALLOW_LOTTERY = Boolean.parseBoolean(server.getProperty("AllowLottery", "True"));
				ALLOW_RACE = Boolean.parseBoolean(server.getProperty("AllowRace", "True"));
				ALLOW_WATER = Boolean.parseBoolean(server.getProperty("AllowWater", "True"));
				ALLOWFISHING = Boolean.parseBoolean(server.getProperty("AllowFishing", "False"));
				ALLOW_MANOR = Boolean.parseBoolean(server.getProperty("AllowManor", "True"));
				ALLOW_BOAT = Boolean.parseBoolean(server.getProperty("AllowBoat", "True"));
				ALLOW_CURSED_WEAPONS = Boolean.parseBoolean(server.getProperty("AllowCursedWeapons", "True"));
				
				String str = server.getProperty("EnableFallingDamage", "auto");
				ENABLE_FALLING_DAMAGE = "auto".equalsIgnoreCase(str) ? GEODATA > 0 : Boolean.parseBoolean(str);
				
				ALT_DEV_NO_QUESTS = Boolean.parseBoolean(server.getProperty("NoQuests", "False"));
				ALT_DEV_NO_SPAWNS = Boolean.parseBoolean(server.getProperty("NoSpawns", "False"));
				DEBUG = Boolean.parseBoolean(server.getProperty("Debug", "False"));
				DEVELOPER = Boolean.parseBoolean(server.getProperty("Developer", "False"));
				PACKET_HANDLER_DEBUG = Boolean.parseBoolean(server.getProperty("PacketHandlerDebug", "False"));
				
				DEADLOCK_DETECTOR = Boolean.parseBoolean(server.getProperty("DeadLockDetector", "False"));
				DEADLOCK_CHECK_INTERVAL = Integer.parseInt(server.getProperty("DeadLockCheckInterval", "20"));
				RESTART_ON_DEADLOCK = Boolean.parseBoolean(server.getProperty("RestartOnDeadlock", "False"));
				
				LOG_CHAT = Boolean.parseBoolean(server.getProperty("LogChat", "false"));
				LOG_ITEMS = Boolean.parseBoolean(server.getProperty("LogItems", "false"));
				GMAUDIT = Boolean.parseBoolean(server.getProperty("GMAudit", "False"));
				
				ENABLE_COMMUNITY_BOARD = Boolean.parseBoolean(server.getProperty("EnableCommunityBoard", "False"));
				BBS_DEFAULT = server.getProperty("BBSDefault", "_bbshome");
				
				COORD_SYNCHRONIZE = Integer.parseInt(server.getProperty("CoordSynchronize", "-1"));
				GEODATA = Integer.parseInt(server.getProperty("GeoData", "0"));
				FORCE_GEODATA = Boolean.parseBoolean(server.getProperty("ForceGeoData", "True"));
				
				GEODATA_CELLFINDING = Boolean.parseBoolean(server.getProperty("CellPathFinding", "False"));
				PATHFIND_BUFFERS = server.getProperty("PathFindBuffers", "100x6;128x6;192x6;256x4;320x4;384x4;500x2");
				LOW_WEIGHT = Float.parseFloat(server.getProperty("LowWeight", "0.5"));
				MEDIUM_WEIGHT = Float.parseFloat(server.getProperty("MediumWeight", "2"));
				HIGH_WEIGHT = Float.parseFloat(server.getProperty("HighWeight", "3"));
				ADVANCED_DIAGONAL_STRATEGY = Boolean.parseBoolean(server.getProperty("AdvancedDiagonalStrategy", "True"));
				DIAGONAL_WEIGHT = Float.parseFloat(server.getProperty("DiagonalWeight", "0.707"));
				MAX_POSTFILTER_PASSES = Integer.parseInt(server.getProperty("MaxPostfilterPasses", "3"));
				DEBUG_PATH = Boolean.parseBoolean(server.getProperty("DebugPath", "False"));
				
				L2WALKER_PROTECTION = Boolean.parseBoolean(server.getProperty("L2WalkerProtection", "False"));
				FORCE_INVENTORY_UPDATE = Boolean.parseBoolean(server.getProperty("ForceInventoryUpdate", "False"));
				AUTODELETE_INVALID_QUEST_DATA = Boolean.parseBoolean(server.getProperty("AutoDeleteInvalidQuestData", "False"));
				GAMEGUARD_ENFORCE = Boolean.parseBoolean(server.getProperty("GameGuardEnforce", "False"));
				ZONE_TOWN = Integer.parseInt(server.getProperty("ZoneTown", "0"));
				SERVER_NEWS = Boolean.parseBoolean(server.getProperty("ShowServerNews", "False"));
				DISABLE_TUTORIAL = Boolean.parseBoolean(server.getProperty("DisableTutorial", "False"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Server failed to load " + SERVER_FILE + " file.");
			}
		}
		else if (Server.serverMode == Server.MODE_LOGINSERVER)
		{
			_log.info("Loading loginserver configuration files.");
			try
			{
				Properties server = new Properties();
				InputStream is = new FileInputStream(new File(LOGIN_CONFIGURATION_FILE));
				server.load(is);
				is.close();
				
				GAME_SERVER_LOGIN_HOST = server.getProperty("LoginHostname", "*");
				GAME_SERVER_LOGIN_PORT = Integer.parseInt(server.getProperty("LoginPort", "9013"));
				
				LOGIN_BIND_ADDRESS = server.getProperty("LoginserverHostname", "*");
				PORT_LOGIN = Integer.parseInt(server.getProperty("LoginserverPort", "2106"));
				
				DEBUG = Boolean.parseBoolean(server.getProperty("Debug", "false"));
				DEVELOPER = Boolean.parseBoolean(server.getProperty("Developer", "false"));
				PACKET_HANDLER_DEBUG = Boolean.parseBoolean(server.getProperty("PacketHandlerDebug", "False"));
				ACCEPT_NEW_GAMESERVER = Boolean.parseBoolean(server.getProperty("AcceptNewGameServer", "True"));
				REQUEST_ID = Integer.parseInt(server.getProperty("RequestServerID", "0"));
				ACCEPT_ALTERNATE_ID = Boolean.parseBoolean(server.getProperty("AcceptAlternateID", "True"));
				
				LOGIN_TRY_BEFORE_BAN = Integer.parseInt(server.getProperty("LoginTryBeforeBan", "10"));
				LOGIN_BLOCK_AFTER_BAN = Integer.parseInt(server.getProperty("LoginBlockAfterBan", "600"));
				
				LOG_LOGIN_CONTROLLER = Boolean.parseBoolean(server.getProperty("LogLoginController", "False"));
				
				DATAPACK_ROOT = new File(server.getProperty("DatapackRoot", ".")).getCanonicalFile(); // FIXME: in login?
				
				INTERNAL_HOSTNAME = server.getProperty("InternalHostname", "localhost");
				EXTERNAL_HOSTNAME = server.getProperty("ExternalHostname", "localhost");
				
				DATABASE_DRIVER = server.getProperty("Driver", "com.mysql.jdbc.Driver");
				DATABASE_URL = server.getProperty("URL", "jdbc:mysql://localhost/acis");
				DATABASE_LOGIN = server.getProperty("Login", "root");
				DATABASE_PASSWORD = server.getProperty("Password", "");
				DATABASE_MAX_CONNECTIONS = Integer.parseInt(server.getProperty("MaximumDbConnections", "10"));
				DATABASE_MAX_IDLE_TIME = Integer.parseInt(server.getProperty("MaximumDbIdleTime", "0"));
				
				SHOW_LICENCE = Boolean.parseBoolean(server.getProperty("ShowLicence", "true"));
				IP_UPDATE_TIME = Integer.parseInt(server.getProperty("IpUpdateTime", "15"));
				FORCE_GGAUTH = Boolean.parseBoolean(server.getProperty("ForceGGAuth", "false"));
				
				AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(server.getProperty("AutoCreateAccounts", "True"));
				
				FLOOD_PROTECTION = Boolean.parseBoolean(server.getProperty("EnableFloodProtection", "True"));
				FAST_CONNECTION_LIMIT = Integer.parseInt(server.getProperty("FastConnectionLimit", "15"));
				NORMAL_CONNECTION_TIME = Integer.parseInt(server.getProperty("NormalConnectionTime", "700"));
				FAST_CONNECTION_TIME = Integer.parseInt(server.getProperty("FastConnectionTime", "350"));
				MAX_CONNECTION_PER_IP = Integer.parseInt(server.getProperty("MaxConnectionPerIP", "50"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Error("Server failed to load " + SERVER_FILE + " file.");
			}
		}
		else
			_log.severe("Couldn't load configs: server mode wasn't set.");
	}
	
	// It has no instances
	private Config()
	{
	}
	
	public static void saveHexid(int serverId, String string)
	{
		Config.saveHexid(serverId, string, HEXID_FILE);
	}
	
	public static void saveHexid(int serverId, String hexId, String fileName)
	{
		try
		{
			Properties hexSetting = new Properties();
			File file = new File(fileName);
			file.createNewFile();
			
			OutputStream out = new FileOutputStream(file);
			hexSetting.setProperty("ServerID", String.valueOf(serverId));
			hexSetting.setProperty("HexID", hexId);
			hexSetting.store(out, "the hexID to auth into login");
			out.close();
		}
		catch (Exception e)
		{
			_log.warning("Failed to save hex id to " + fileName + " file.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads single flood protector configuration.
	 * @param properties L2Properties file reader
	 * @param config flood protector configuration instance
	 * @param configString flood protector configuration string that determines for which flood protector configuration should be read
	 * @param defaultInterval default flood protector interval
	 */
	private static void loadFloodProtectorConfig(final Properties properties, final FloodProtectorConfig config, final String configString, final String defaultInterval)
	{
		config.FLOOD_PROTECTION_INTERVAL = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "Interval"), defaultInterval));
		config.LOG_FLOODING = Boolean.parseBoolean(properties.getProperty(StringUtil.concat("FloodProtector", configString, "LogFlooding"), "False"));
		config.PUNISHMENT_LIMIT = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentLimit"), "0"));
		config.PUNISHMENT_TYPE = properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentType"), "none");
		config.PUNISHMENT_TIME = Integer.parseInt(properties.getProperty(StringUtil.concat("FloodProtector", configString, "PunishmentTime"), "0"));
	}
	
	public static class ClassMasterSettings
	{
		private final TIntObjectHashMap<TIntIntHashMap> _claimItems;
		private final TIntObjectHashMap<TIntIntHashMap> _rewardItems;
		private final TIntObjectHashMap<Boolean> _allowedClassChange;
		
		public ClassMasterSettings(String _configLine)
		{
			_claimItems = new TIntObjectHashMap<>(3);
			_rewardItems = new TIntObjectHashMap<>(3);
			_allowedClassChange = new TIntObjectHashMap<>(3);
			if (_configLine != null)
				parseConfigLine(_configLine.trim());
		}
		
		private void parseConfigLine(String _configLine)
		{
			StringTokenizer st = new StringTokenizer(_configLine, ";");
			
			while (st.hasMoreTokens())
			{
				// get allowed class change
				int job = Integer.parseInt(st.nextToken());
				
				_allowedClassChange.put(job, true);
				
				TIntIntHashMap _items = new TIntIntHashMap();
				// parse items needed for class change
				if (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					
					while (st2.hasMoreTokens())
					{
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						int _itemId = Integer.parseInt(st3.nextToken());
						int _quantity = Integer.parseInt(st3.nextToken());
						_items.put(_itemId, _quantity);
					}
				}
				
				_claimItems.put(job, _items);
				
				_items = new TIntIntHashMap();
				// parse gifts after class change
				if (st.hasMoreTokens())
				{
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), "[],");
					
					while (st2.hasMoreTokens())
					{
						StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "()");
						int _itemId = Integer.parseInt(st3.nextToken());
						int _quantity = Integer.parseInt(st3.nextToken());
						_items.put(_itemId, _quantity);
					}
				}
				
				_rewardItems.put(job, _items);
			}
		}
		
		public boolean isAllowed(int job)
		{
			if (_allowedClassChange == null)
				return false;
			if (_allowedClassChange.containsKey(job))
				return _allowedClassChange.get(job);
			
			return false;
		}
		
		public TIntIntHashMap getRewardItems(int job)
		{
			if (_rewardItems.containsKey(job))
				return _rewardItems.get(job);
			
			return null;
		}
		
		public TIntIntHashMap getRequireItems(int job)
		{
			if (_claimItems.containsKey(job))
				return _claimItems.get(job);
			
			return null;
		}
	}
	
	/**
	 * itemId1,itemNumber1;itemId2,itemNumber2... to the int[n][2] = [itemId1][itemNumber1],[itemId2][itemNumber2]...
	 * @param line
	 * @return an array consisting of parsed items.
	 */
	private static int[][] parseItemsList(String line)
	{
		final String[] propertySplit = line.split(";");
		if (propertySplit.length == 0)
			return null;
		
		int i = 0;
		String[] valueSplit;
		final int[][] result = new int[propertySplit.length][];
		for (String value : propertySplit)
		{
			valueSplit = value.split(",");
			if (valueSplit.length != 2)
			{
				_log.warning(StringUtil.concat("parseItemsList[Config.load()]: invalid entry -> \"", valueSplit[0], "\", should be itemId,itemNumber"));
				return null;
			}
			
			result[i] = new int[2];
			try
			{
				result[i][0] = Integer.parseInt(valueSplit[0]);
			}
			catch (NumberFormatException e)
			{
				_log.warning(StringUtil.concat("parseItemsList[Config.load()]: invalid itemId -> \"", valueSplit[0], "\""));
				return null;
			}
			
			try
			{
				result[i][1] = Integer.parseInt(valueSplit[1]);
			}
			catch (NumberFormatException e)
			{
				_log.warning(StringUtil.concat("parseItemsList[Config.load()]: invalid item number -> \"", valueSplit[1], "\""));
				return null;
			}
			i++;
		}
		return result;
	}
}