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
/**
 * @author godson
 */
package net.sf.l2j.gameserver.model.olympiad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.instancemanager.OlympiadStadiaManager;
import net.sf.l2j.gameserver.model.L2World; 
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.serverpackets.ExOlympiadUserInfoSpectator;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.StatsSet;

public class Olympiad
{
    protected static final Logger _log = Logger.getLogger(Olympiad.class.getName());
    private static Olympiad _instance; 
    protected static Map<String, L2PcInstance> _IPRegisters;
    protected static Map<Integer, StatsSet> _nobles;
    protected static List<StatsSet> _heroesToBe;
    protected static List<L2PcInstance> _nonClassBasedRegisters;
    protected static Map<Integer, List<L2PcInstance>> _classBasedRegisters;
    private static final String OLYMPIAD_DATA_FILE = "config/olympiad.properties";
    public static final String OLYMPIAD_HTML_FILE = "data/html/olympiad/";
    private static final String OLYMPIAD_LOAD_NOBLES = "SELECT * from olympiad_nobles";
    private static final String OLYMPIAD_SAVE_NOBLES = "INSERT INTO olympiad_nobles " + "values (?,?,?,?,?)";
    private static final String OLYMPIAD_UPDATE_NOBLES = "UPDATE olympiad_nobles set " + "olympiad_points = ?, competitions_done = ? where char_id = ?";
    private static final String OLYMPIAD_GET_HEROS = "SELECT char_id, char_name from " + "olympiad_nobles where class_id = ? and competitions_done >= 9 order by " + "olympiad_points desc, competitions_done desc";
    private static final String GET_EACH_CLASS_LEADER = "SELECT char_name from " + "olympiad_nobles where class_id = ? order by olympiad_points desc, " + "competitions_done desc";
    private static final String OLYMPIAD_DELETE_ALL = "DELETE from olympiad_nobles";
    private static final int COMP_START = Config.ALT_OLY_START_TIME; // 6PM
    private static final int COMP_MIN = Config.ALT_OLY_MIN; // 00 mins
    private static final long COMP_PERIOD = Config.ALT_OLY_CPERIOD; // 6
    // hours
    protected static final long BATTLE_PERIOD = Config.ALT_OLY_BATTLE; // 6
    // mins
    protected static final long BATTLE_WAIT = Config.ALT_OLY_BWAIT; // 10mins
    protected static final long INITIAL_WAIT = Config.ALT_OLY_IWAIT; // 5mins
    protected static final long WEEKLY_PERIOD = Config.ALT_OLY_WPERIOD; // 1
    // week
    protected static final long VALIDATION_PERIOD = Config.ALT_OLY_VPERIOD; // 24
    // hours
    /*
     * FOR TESTING private static final int COMP_START = 8; // 1PM - 2PM
     * private static final int COMP_MIN = 15; // 20mins private static
     * final long COMP_PERIOD = 7200000; // 2hours private static final long
     * BATTLE_PERIOD = 180000; // 3mins private static final long
     * BATTLE_WAIT = 600000; // 10mins private static final long
     * INITIAL_WAIT = 300000; // 5mins private static final long
     * WEEKLY_PERIOD = 7200000; // 2 hours private static final long
     * VALIDATION_PERIOD = 3600000; // 1 hour
     */
    // private static final int COLLISIEUMS = 11; // 22 in all, 11 each for
    static final int COLLISIEUMS = 11;
    // (Classed and NonClassed)
    private static final int DEFAULT_POINTS = 18;
    protected static final int WEEKLY_POINTS = 3;
    public static final String CHAR_ID = "char_id";
    public static final String CLASS_ID = "class_id";
    public static final String CHAR_NAME = "char_name";
    public static final String POINTS = "olympiad_points";
    public static final String COMP_DONE = "competitions_done";
    protected long _olympiadEnd;
    protected long _validationEnd;
    protected int _period;
    protected long _nextWeeklyChange;
    protected int _currentCycle;
    private long _compEnd;
    private Calendar _compStart;
    protected static boolean _inCompPeriod;
    protected static boolean _isOlympiadEnd;
    protected static boolean _compStarted;
    protected static boolean _battleStarted;
    protected ScheduledFuture _scheduledCompStart;
    protected ScheduledFuture _scheduledCompEnd;
    protected ScheduledFuture _scheduledOlympiadEnd;
    protected ScheduledFuture _scheduledManagerTask;
    protected ScheduledFuture _scheduledWeeklyTask;
    protected ScheduledFuture _scheduledValdationTask;
    protected static final int[][] STADIUMS = { { -20814, -21189, -3030 }, { -120324, -225077, -3331 }, { -102495, -209023, -3331 }, { -120156, -207378, -3331 }, { -87628, -225021, -3331 }, { -81705, -213209, -3331 }, { -87593, -207339, -3331 }, { -93709, -218304, -3331 }, { -77157, -218608, -3331 }, { -69682, -209027, -3331 }, { -76887, -201256, -3331 }, { -109985, -218701, -3331 }, { -126367, -218228, -3331 }, { -109629, -201292, -3331 }, { -87523, -240169, -3331 }, { -81748, -245950, -3331 }, { -77123, -251473, -3331 }, { -69778, -241801, -3331 }, { -76754, -234014, -3331 }, { -93742, -251032, -3331 }, { -87466, -257752, -3331 }, { -114413, -213241, -3331 } };

    // private static enum COMP_TYPE
    static enum COMP_TYPE
    {
	CLASSED, NON_CLASSED
    }

    protected static OlympiadManager _manager;
    
    public static Olympiad getInstance()
    {
	if (_instance == null)
	    _instance = new Olympiad();
	return _instance;
    }

    public Olympiad()
    {
	try
	{
	    load();
	} catch (IOException e)
	{
	    e.printStackTrace();
	} catch (SQLException s)
	{
	    s.printStackTrace();
	}
	if (_period == 0)
	    init();
    }

    private void load() throws IOException, SQLException
    {
	_nobles = new FastMap<Integer, StatsSet>();
	Properties OlympiadProperties = new Properties();
	InputStream is = new FileInputStream(new File("./" + OLYMPIAD_DATA_FILE));
	OlympiadProperties.load(is);
	is.close();
	_currentCycle = Integer.parseInt(OlympiadProperties.getProperty("CurrentCycle", "1"));
	_period = Integer.parseInt(OlympiadProperties.getProperty("Period", "0"));
	_olympiadEnd = Long.parseLong(OlympiadProperties.getProperty("OlympiadEnd", "0"));
	_validationEnd = Long.parseLong(OlympiadProperties.getProperty("ValdationEnd", "0"));
	_nextWeeklyChange = Long.parseLong(OlympiadProperties.getProperty("NextWeeklyChange", "0"));
	switch (_period)
	{
	case 0:
	    if ((_olympiadEnd == 0) || (_olympiadEnd < Calendar.getInstance().getTimeInMillis()))
		setNewOlympiadEnd();
	    else
		_isOlympiadEnd = false;
	    break;
	case 1:
	    if (_validationEnd > Calendar.getInstance().getTimeInMillis())
	    {
		_isOlympiadEnd = true;
		_scheduledValdationTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
		    public void run()
		    {
			_period = 0;
			_currentCycle++;
			deleteNobles();
			setNewOlympiadEnd();
			init();
		    }
		}, getMillisToValidationEnd());
	    } else
	    {
		_currentCycle++;
		_period = 0;
		deleteNobles();
		setNewOlympiadEnd();
	    }
	    break;
	default:
	    _log.warning("Olympiad System: Omg something went wrong in loading!! Period = " + _period);
	    return;
	}
	try
	{
	    Connection con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement = con.prepareStatement(OLYMPIAD_LOAD_NOBLES);
	    ResultSet rset = statement.executeQuery();
	    while (rset.next())
	    {
		StatsSet statDat = new StatsSet();
		int charId = rset.getInt(CHAR_ID);
		statDat.set(CLASS_ID, rset.getInt(CLASS_ID));
		statDat.set(CHAR_NAME, rset.getString(CHAR_NAME));
		statDat.set(POINTS, rset.getInt(POINTS));
		statDat.set(COMP_DONE, rset.getInt(COMP_DONE));
		statDat.set("to_save", false);
		_nobles.put(charId, statDat);
	    }
	    rset.close();
	    statement.close();
	    con.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
	synchronized (this)
	{
	    _log.info("Olympiad System: Loading Olympiad System....");
	    if (_period == 0)
		_log.info("Olympiad System: Currently in Olympiad Period");
	    else
		_log.info("Olympiad System: Currently in Validation Period");
	    _log.info("Olympiad System: Period Ends....");
	    long milliToEnd;
	    if (_period == 0)
		milliToEnd = getMillisToOlympiadEnd();
	    else
		milliToEnd = getMillisToValidationEnd();
	    double numSecs = milliToEnd / 1000 % 60;
	    double countDown = (milliToEnd / 1000 - numSecs) / 60;
	    int numMins = (int) Math.floor(countDown % 60);
	    countDown = (countDown - numMins) / 60;
	    int numHours = (int) Math.floor(countDown % 24);
	    int numDays = (int) Math.floor((countDown - numHours) / 24);
	    _log.info("Olympiad System: In " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
	    if (_period == 0)
	    {
		_log.info("Olympiad System: Next Weekly Change is in....");
		milliToEnd = getMillisToWeekChange();
		double numSecs2 = milliToEnd / 1000 % 60;
		double countDown2 = (milliToEnd / 1000 - numSecs2) / 60;
		int numMins2 = (int) Math.floor(countDown2 % 60);
		countDown2 = (countDown2 - numMins2) / 60;
		int numHours2 = (int) Math.floor(countDown2 % 24);
		int numDays2 = (int) Math.floor((countDown2 - numHours2) / 24);
		_log.info("Olympiad System: " + numDays2 + " days, " + numHours2 + " hours and " + numMins2 + " mins.");
	    }
	}
	_log.info("Olympiad System: Loaded " + _nobles.size() + " Nobles");
    }

    protected void init()
    {
	if (_period == 1)
	    return;
	_nonClassBasedRegisters = new FastList<L2PcInstance>();
	_classBasedRegisters = new FastMap<Integer, List<L2PcInstance>>();
	if (Config.DISABLE_OLY_DUALBOX)
	{
	    _IPRegisters = new FastMap<String, L2PcInstance>();
	}
	_compStart = Calendar.getInstance();
	_compStart.set(Calendar.HOUR_OF_DAY, COMP_START);
	_compStart.set(Calendar.MINUTE, COMP_MIN);
	_compEnd = _compStart.getTimeInMillis() + COMP_PERIOD;
	_scheduledOlympiadEnd = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
	{
	    public void run()
	    {
		SystemMessage sm = new SystemMessage(SystemMessageId.OLYMPIAD_PERIOD_S1_HAS_ENDED);
		sm.addNumber(_currentCycle);
		Announcements.getInstance().announceToAll(sm);
		Announcements.getInstance().announceToAll("Olympiad Validation Period has began");
		_isOlympiadEnd = true;
		if (_scheduledManagerTask != null)
		    _scheduledManagerTask.cancel(true);
		if (_scheduledWeeklyTask != null)
		    _scheduledWeeklyTask.cancel(true);
		Calendar validationEnd = Calendar.getInstance();
		_validationEnd = validationEnd.getTimeInMillis() + VALIDATION_PERIOD;
		saveNobleData();
		_period = 1;
		sortHerosToBe();
		giveHeroBonus();
		Hero.getInstance().computeNewHeroes(_heroesToBe);
		try
		{
		    save();
		} catch (Exception e)
		{
		    _log.warning("Olympiad System: Failed to save Olympiad configuration: " + e);
		}
		_scheduledValdationTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
		    public void run()
		    {
			Announcements.getInstance().announceToAll("Olympiad Validation Period has ended");
			_period = 0;
			_currentCycle++;
			deleteNobles();
			setNewOlympiadEnd();
			init();
		    }
		}, getMillisToValidationEnd());
	    }
	}, getMillisToOlympiadEnd());
	updateCompStatus();
	scheduleWeeklyChange();
    }

    public boolean registerNoble(L2PcInstance noble, boolean classBased)
    {
	SystemMessage sm;
	if (_compStarted)
	{
	    noble.sendMessage("Cant Register whilst competition is under way");
	    return false;
	}
	if (!_inCompPeriod)
	{
	    sm = new SystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS);
	    noble.sendPacket(sm);
	    return false;
	}
	if (noble.isCursedWeaponEquiped())
	{
	    noble.sendMessage("You can't participate to Olympiad while holding a cursed weapon.");
	    return false;
	}
	if (!noble.isNoble())
	{
	    sm = new SystemMessage(SystemMessageId.ONLY_NOBLESS_CAN_PARTICIPATE_IN_THE_OLYMPIAD);
	    noble.sendPacket(sm);
	    return false;
	}
	if (noble.getBaseClass() != noble.getClassId().getId())
	{
	    sm = new SystemMessage(SystemMessageId.YOU_CANT_JOIN_THE_OLYMPIAD_WITH_A_SUB_JOB_CHARACTER);
	    noble.sendPacket(sm);
	    return false;
	}
	if (!_nobles.containsKey(noble.getObjectId()))
	{
	    StatsSet statDat = new StatsSet();
	    statDat.set(CLASS_ID, noble.getClassId().getId());
	    statDat.set(CHAR_NAME, noble.getName());
	    statDat.set(POINTS, DEFAULT_POINTS);
	    statDat.set(COMP_DONE, 0);
	    statDat.set("to_save", true);
	    _nobles.put(noble.getObjectId(), statDat);
	}
	if (_classBasedRegisters.containsKey(noble.getClassId().getId()))
	{
	    List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
	    for (L2PcInstance partecipant : classed)
	    {
		if (partecipant.getObjectId() == noble.getObjectId())
		{
		    sm = new SystemMessage(SystemMessageId.YOU_ARE_ALREADY_ON_THE_WAITING_LIST_TO_PARTICIPATE_IN_THE_GAME_FOR_YOUR_CLASS);
		    noble.sendPacket(sm);
		    return false;
		}
	    }
	}
	for (L2PcInstance partecipant : _nonClassBasedRegisters)
	{
	    if (partecipant.getObjectId() == noble.getObjectId())
	    {
		sm = new SystemMessage(SystemMessageId.YOU_ARE_ALREADY_ON_THE_WAITING_LIST_FOR_ALL_CLASSES_WAITING_TO_PARTICIPATE_IN_THE_GAME);
		noble.sendPacket(sm);
		return false;
	    }
	}
	if (Config.DISABLE_OLY_DUALBOX)
	{
	    if (_IPRegisters.containsKey(noble.getClient().getConnection().getSocketChannel().socket().getInetAddress().getHostAddress()))
	    {
		L2PcInstance classed = _IPRegisters.get(noble.getClient().getConnection().getSocketChannel().socket().getInetAddress().getHostAddress());
		if (classed != null)
		{
		    noble.sendMessage("Dual box is not allowed on Olympiad.");
		    return false;
		} else
		{
		    _IPRegisters.remove(noble.getClient().getConnection().getSocketChannel().socket().getInetAddress().getHostAddress());
		    _IPRegisters.put(noble.getClient().getConnection().getSocketChannel().socket().getInetAddress().getHostAddress(), noble);
		}
	    } else
	    {
		_IPRegisters.put(noble.getClient().getConnection().getSocketChannel().socket().getInetAddress().getHostAddress(), noble);
	    }
	}
	if (getNoblePoints(noble.getObjectId()) < 3)
	{
	    noble.sendMessage("Cant register when you have less than 3 points");
	    return false;
	}
	if (classBased)
	{
	    if (_classBasedRegisters.containsKey(noble.getClassId().getId()))
	    {
		List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
		classed.add(noble);
		_classBasedRegisters.remove(noble.getClassId().getId());
		_classBasedRegisters.put(noble.getClassId().getId(), classed);
		sm = new SystemMessage(SystemMessageId.YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_CLASSIFIED_GAMES);
		noble.sendPacket(sm);
	    } else
	    {
		List<L2PcInstance> classed = new FastList<L2PcInstance>();
		classed.add(noble);
		_classBasedRegisters.put(noble.getClassId().getId(), classed);
		sm = new SystemMessage(SystemMessageId.YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_CLASSIFIED_GAMES);
		noble.sendPacket(sm);
	    }
	} else
	{
	    _nonClassBasedRegisters.add(noble);
	    sm = new SystemMessage(SystemMessageId.YOU_HAVE_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_NO_CLASS_GAMES);
	    noble.sendPacket(sm);
	}
	return true;
    }

    public boolean isRegistered(L2PcInstance noble)
    {
	if (_nonClassBasedRegisters == null)
	    return false;
	if (_classBasedRegisters == null)
	    return false;
	if (!_nonClassBasedRegisters.contains(noble))
	{
	    if (!_classBasedRegisters.containsKey(noble.getClassId().getId()))
	    {
		return false;
	    } else
	    {
		List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
		if (!classed.contains(noble))
		{
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean unRegisterNoble(L2PcInstance noble)
    {
	SystemMessage sm;
	if (_compStarted)
	{
	    noble.sendMessage("Cant Unregister whilst competition is under way");
	    return false;
	}
	if (!_inCompPeriod)
	{
	    sm = new SystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS);
	    noble.sendPacket(sm);
	    return false;
	}
	if (!noble.isNoble())
	{
	    sm = new SystemMessage(SystemMessageId.ONLY_NOBLESS_CAN_PARTICIPATE_IN_THE_OLYMPIAD);
	    noble.sendPacket(sm);
	    return false;
	}
	if (!isRegistered(noble))
	{
	    sm = new SystemMessage(SystemMessageId.YOU_HAVE_NOT_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_A_GAME);
	    noble.sendPacket(sm);
	    return false;
	}
	if (Config.DISABLE_OLY_DUALBOX)
	{
	    _IPRegisters.remove(noble.getClient().getConnection().getSocketChannel().socket().getInetAddress().getHostAddress());
	}
	if (_nonClassBasedRegisters.contains(noble))
	    _nonClassBasedRegisters.remove(noble);
	else
	{
	    List<L2PcInstance> classed = _classBasedRegisters.get(noble.getClassId().getId());
	    classed.remove(noble);
	    _classBasedRegisters.remove(noble.getClassId().getId());
	    _classBasedRegisters.put(noble.getClassId().getId(), classed);
	}
	sm = new SystemMessage(SystemMessageId.YOU_HAVE_BEEN_DELETED_FROM_THE_WAITING_LIST_OF_A_GAME);
	noble.sendPacket(sm);
	return true;
    }

    public void removeDisconnectedCompetitor(L2PcInstance player) 
    {
        if (_manager == null || (_manager.getOlympiadInstance(player.getOlympiadGameId()) == null)) return; 
        
        _manager.getOlympiadInstance(player.getOlympiadGameId()).handleDisconnect(player); 
    }
    
    private void updateCompStatus()
    {
	_compStarted = false;
	synchronized (this)
	{
	    long milliToStart = getMillisToCompBegin();
	    double numSecs = milliToStart / 1000 % 60;
	    double countDown = (milliToStart / 1000 - numSecs) / 60;
	    int numMins = (int) Math.floor(countDown % 60);
	    countDown = (countDown - numMins) / 60;
	    int numHours = (int) Math.floor(countDown % 24);
	    int numDays = (int) Math.floor((countDown - numHours) / 24);
	    _log.info("Olympiad System: Competition Period Starts in " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
	    _log.info("Olympiad System: Event starts/started : " + _compStart.getTime());
	}
	_scheduledCompStart = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
	{
	    public void run()
	    {
		if (isOlympiadEnd())
		    return;
		_inCompPeriod = true;
		// OlympiadManager om = new OlympiadManager();
		OlympiadManager om = new OlympiadManager(Olympiad.this);
		Announcements.getInstance().announceToAll(new SystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_HAS_STARTED));
		_log.info("Olympiad System: Olympiad Game Started");
		_scheduledManagerTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(om, INITIAL_WAIT, BATTLE_WAIT);
		_scheduledCompEnd = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
		    public void run()
		    {
			if (isOlympiadEnd())
			    return;
			_scheduledManagerTask.cancel(true);
			_inCompPeriod = false;
			Announcements.getInstance().announceToAll(new SystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_HAS_ENDED));
			_log.info("Olympiad System: Olympiad Game Ended");
			try
			{
			    save();
			} catch (Exception e)
			{
			    _log.warning("Olympiad System: Failed to save Olympiad configuration: " + e);
			}
			init();
		    }
		}, getMillisToCompEnd());
	    }
	}, getMillisToCompBegin());
    }

    private long getMillisToOlympiadEnd()
    {
	// if (_olympiadEnd > Calendar.getInstance().getTimeInMillis())
	return _olympiadEnd - Calendar.getInstance().getTimeInMillis();
	// return 10L;
    }

    public long getOlympiadEnd()
    {
	return _olympiadEnd;
    }

    public void manualSelectHeroes()
    {
	SystemMessage sm = new SystemMessage(SystemMessageId.OLYMPIAD_PERIOD_S1_HAS_ENDED);
	sm.addNumber(_currentCycle);
	Announcements.getInstance().announceToAll(sm);
	Announcements.getInstance().announceToAll("Olympiad Validation Period has began");
	_isOlympiadEnd = true;
	if (_scheduledManagerTask != null)
	    _scheduledManagerTask.cancel(true);
	if (_scheduledWeeklyTask != null)
	    _scheduledWeeklyTask.cancel(true);
	if (_scheduledOlympiadEnd != null)
	    _scheduledOlympiadEnd.cancel(true);
	Calendar validationEnd = Calendar.getInstance();
	_validationEnd = validationEnd.getTimeInMillis() + VALIDATION_PERIOD;
	saveNobleData();
	_period = 1;
	sortHerosToBe();
	giveHeroBonus();
	Hero.getInstance().computeNewHeroes(_heroesToBe);
	try
	{
	    save();
	} catch (Exception e)
	{
	    _log.warning("Olympiad System: Failed to save Olympiad configuration: " + e);
	}
	_scheduledValdationTask = ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
	{
	    public void run()
	    {
		Announcements.getInstance().announceToAll("Olympiad Validation Period has ended");
		_period = 0;
		_currentCycle++;
		deleteNobles();
		setNewOlympiadEnd();
		init();
	    }
	}, getMillisToValidationEnd());
    }

    protected long getMillisToValidationEnd()
    {
	if (_validationEnd > Calendar.getInstance().getTimeInMillis())
	    return _validationEnd - Calendar.getInstance().getTimeInMillis();
	return 10L;
    }

    public boolean isOlympiadEnd()
    {
	return _isOlympiadEnd;
    }

    protected void setNewOlympiadEnd()
    {
	SystemMessage sm = new SystemMessage(SystemMessageId.OLYMPIAD_PERIOD_S1_HAS_STARTED);
	sm.addNumber(_currentCycle);
	Announcements.getInstance().announceToAll(sm);
	Calendar currentTime = Calendar.getInstance();
	currentTime.add(Calendar.MONTH, 1);
	currentTime.set(Calendar.DAY_OF_MONTH, 1);
	currentTime.set(Calendar.AM_PM, Calendar.AM);
	currentTime.set(Calendar.HOUR, 12);
	currentTime.set(Calendar.MINUTE, 0);
	currentTime.set(Calendar.SECOND, 0);
	_olympiadEnd = currentTime.getTimeInMillis();
	Calendar nextChange = Calendar.getInstance();
	_nextWeeklyChange = nextChange.getTimeInMillis() + WEEKLY_PERIOD;
	_isOlympiadEnd = false;
    }

    public boolean inCompPeriod()
    {
	return _inCompPeriod;
    }

    private long getMillisToCompBegin()
    {
	if ((_compStart.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) && (_compEnd > Calendar.getInstance().getTimeInMillis()))
	    return 10L;
	if (_compStart.getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
	    return _compStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
	return setNewCompBegin();
    }

    private long setNewCompBegin()
    {
	_compStart = Calendar.getInstance();
	_compStart.set(Calendar.HOUR_OF_DAY, COMP_START);
	_compStart.set(Calendar.MINUTE, COMP_MIN);
	_compStart.add(Calendar.HOUR_OF_DAY, 24);
	_compEnd = _compStart.getTimeInMillis() + COMP_PERIOD;
	_log.info("Olympiad System: New Schedule @ " + _compStart.getTime());
	return _compStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
    }

    protected long getMillisToCompEnd()
    {
	// if (_compEnd > Calendar.getInstance().getTimeInMillis())
	return _compEnd - Calendar.getInstance().getTimeInMillis();
	// return 10L;
    }

    private long getMillisToWeekChange()
    {
	if (_nextWeeklyChange > Calendar.getInstance().getTimeInMillis())
	    return _nextWeeklyChange - Calendar.getInstance().getTimeInMillis();
	return 10L;
    }

    private void scheduleWeeklyChange()
    {
	_scheduledWeeklyTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable()
	{
	    public void run()
	    {
		addWeeklyPoints();
		_log.info("Olympiad System: Added weekly points to nobles");
		Calendar nextChange = Calendar.getInstance();
		_nextWeeklyChange = nextChange.getTimeInMillis() + WEEKLY_PERIOD;
	    }
	}, getMillisToWeekChange(), WEEKLY_PERIOD);
    }

    protected synchronized void addWeeklyPoints()
    {
	if (_period == 1)
	    return;
	for (Integer nobleId : _nobles.keySet())
	{
	    StatsSet nobleInfo = _nobles.get(nobleId);
	    int currentPoints = nobleInfo.getInteger(POINTS);
	    currentPoints += WEEKLY_POINTS;
	    nobleInfo.set(POINTS, currentPoints);
	    _nobles.remove(nobleId);
	    _nobles.put(nobleId, nobleInfo);
	}
    }

    public String[] getMatchList()
    {
	return _manager == null ? null : _manager.getAllTitles();
    }

    public int getCurrentCycle()
    {
	return _currentCycle;
    }

    public void addSpectator(int id, L2PcInstance spectator)
    {
	if ((_manager == null) || (_manager.getOlympiadInstance(id) == null) || !_battleStarted)
	{
	    spectator.sendPacket(new SystemMessage(SystemMessageId.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS));
	    return;
	}
	L2PcInstance[] players = _manager.getOlympiadInstance(id).getPlayers();
	if (players == null)
	    return;
	spectator.enterOlympiadObserverMode(STADIUMS[id][0], STADIUMS[id][1], STADIUMS[id][2], id);
	_manager.getOlympiadInstance(id).addSpectator(spectator);
	players[0].setOlympiadSide(2);
	players[1].setOlympiadSide(1);
	spectator.sendPacket(new ExOlympiadUserInfoSpectator(players[0], 2));
	spectator.sendPacket(new ExOlympiadUserInfoSpectator(players[1], 1));
    }

    public void removeSpectator(int id, L2PcInstance spectator)
    {
	if ((_manager == null) || (_manager.getOlympiadInstance(id) == null))
	    return;
	_manager.getOlympiadInstance(id).removeSpectator(spectator);
    }

    public List<L2PcInstance> getSpectators(int id)
    {
	return _manager.getOlympiadInstance(id).getSpectators();
    }

    public Map<Integer, L2OlympiadGame> getOlympiadGames()
    {
	return _manager.getOlympiadGames();
    }

    public boolean playerInStadia(L2PcInstance player)
    {
	return OlympiadStadiaManager.getInstance().getStadium(player) != null;
    }

    public int[] getWaitingList()
    {
	int[] array = new int[2];
	if (!inCompPeriod())
	    return null;
	int classCount = 0;
	if (_classBasedRegisters.size() != 0)
	    for (List<L2PcInstance> classed : _classBasedRegisters.values())
	    {
		classCount += classed.size();
	    }
	array[0] = classCount;
	array[1] = _nonClassBasedRegisters.size();
	return array;
    }

    protected synchronized void saveNobleData()
    {
	Connection con = null;
	if (_nobles == null)
	    return;
	try
	{
	    con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement;
	    for (Integer nobleId : _nobles.keySet())
	    {
		StatsSet nobleInfo = _nobles.get(nobleId);
		int charId = nobleId;
		int classId = nobleInfo.getInteger(CLASS_ID);
		String charName = nobleInfo.getString(CHAR_NAME);
		int points = nobleInfo.getInteger(POINTS);
		int compDone = nobleInfo.getInteger(COMP_DONE);
		boolean toSave = nobleInfo.getBool("to_save");
		if (toSave)
		{
		    statement = con.prepareStatement(OLYMPIAD_SAVE_NOBLES);
		    statement.setInt(1, charId);
		    statement.setInt(2, classId);
		    statement.setString(3, charName);
		    statement.setInt(4, points);
		    statement.setInt(5, compDone);
		    statement.execute();
		    statement.close();
		    nobleInfo.set("to_save", false);
		    _nobles.remove(nobleId);
		    _nobles.put(nobleId, nobleInfo);
		} else
		{
		    statement = con.prepareStatement(OLYMPIAD_UPDATE_NOBLES);
		    statement.setInt(1, points);
		    statement.setInt(2, compDone);
		    statement.setInt(3, charId);
		    statement.execute();
		    statement.close();
		}
	    }
	} catch (SQLException e)
	{
	    _log.warning("Olympiad System: Couldnt save nobles info in db");
	} finally
	{
	    try
	    {
		con.close();
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
    }

    protected void sortHerosToBe()
    {
	if (_period != 1)
	    return;
	_heroesToBe = new FastList<StatsSet>();
	Connection con = null;
	try
	{
	    con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement;
	    ResultSet rset;
	    StatsSet hero;
	    for (int i = 88; i < 119; i++)
	    {
		statement = con.prepareStatement(OLYMPIAD_GET_HEROS);
		statement.setInt(1, i);
		rset = statement.executeQuery();
		if (rset.next())
		{
		    hero = new StatsSet();
		    hero.set(CLASS_ID, i);
		    hero.set(CHAR_ID, rset.getInt(CHAR_ID));
		    hero.set(CHAR_NAME, rset.getString(CHAR_NAME));
		    _heroesToBe.add(hero);
		}
		statement.close();
		rset.close();
	    }
	} catch (SQLException e)
	{
	    _log.warning("Olympiad System: Couldnt heros from db");
	} finally
	{
	    try
	    {
		con.close();
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
    }

    public List<String> getClassLeaderBoard(int classId)
    {
	// if (_period != 1) return;
	List<String> names = new FastList<String>();
	Connection con = null;
	try
	{
	    con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement;
	    ResultSet rset;
	    statement = con.prepareStatement(GET_EACH_CLASS_LEADER);
	    statement.setInt(1, classId);
	    rset = statement.executeQuery();
	    while (rset.next())
	    {
		names.add(rset.getString(CHAR_NAME));
	    }
	    statement.close();
	    rset.close();
	    return names;
	} catch (SQLException e)
	{
	    _log.warning("Olympiad System: Couldnt heros from db");
	} finally
	{
	    try
	    {
		con.close();
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
	return names;
    }

    protected void giveHeroBonus()
    {
	if (_heroesToBe.size() == 0)
	    return;
	for (StatsSet hero : _heroesToBe)
	{
	    int charId = hero.getInteger(CHAR_ID);
	    StatsSet noble = _nobles.get(charId);
	    int currentPoints = noble.getInteger(POINTS);
	    currentPoints += 300;
	    noble.set(POINTS, currentPoints);
	    _nobles.remove(charId);
	    _nobles.put(charId, noble);
	}
    }

    public int getNoblessePasses(int objId)
    {
	if ((_period != 1) || (_nobles.size() == 0))
	    return 0;
	StatsSet noble = _nobles.get(objId);
	if (noble == null)
	    return 0;
	int points = noble.getInteger(POINTS);
	if (points <= 50)
	    return 0;
	noble.set(POINTS, 0);
	_nobles.remove(objId);
	_nobles.put(objId, noble);
	points *= 1000;
	return points;
    }

    public boolean isRegisteredInComp(L2PcInstance player)
    {
	boolean result = false;
	if ((_nonClassBasedRegisters != null) && _nonClassBasedRegisters.contains(player))
	    result = true;
	else if ((_classBasedRegisters != null) && _classBasedRegisters.containsKey(player.getClassId().getId()))
	{
	    List<L2PcInstance> classed = _classBasedRegisters.get(player.getClassId().getId());
	    if (classed.contains(player))
		result = true;
	}
	return result;
    }

    public int getNoblePoints(int objId)
    {
	if (_nobles.size() == 0)
	    return 0;
	StatsSet noble = _nobles.get(objId);
	if (noble == null)
	    return 0;
	int points = noble.getInteger(POINTS);
	return points;
    }

    public int getCompetitionDone(int objId)
    {
	if (_nobles.size() == 0)
	    return 0;
	StatsSet noble = _nobles.get(objId);
	if (noble == null)
	    return 0;
	int points = noble.getInteger(COMP_DONE);
	return points;
    }

    // returns the players for the given olympiad game Id 
    public L2PcInstance[] getPlayers(int Id) 
    { 
    	if (_manager.getOlympiadInstance(Id) == null) 
    		return null; 
    	else 
    		return _manager.getOlympiadInstance(Id).getPlayers(); 
    } 
    
    protected void deleteNobles()
    {
	Connection con = null;
	try
	{
	    con = L2DatabaseFactory.getInstance().getConnection();
	    PreparedStatement statement = con.prepareStatement(OLYMPIAD_DELETE_ALL);
	    statement.execute();
	    statement.close();
	} catch (SQLException e)
	{
	    _log.warning("Olympiad System: Couldnt delete nobles from db");
	} finally
	{
	    try
	    {
		con.close();
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}
	_nobles.clear();
    }

    public void save() throws IOException
    {
	saveNobleData();
	Properties OlympiadProperties = new Properties();
	FileOutputStream fos = new FileOutputStream(new File(Config.DATAPACK_ROOT, OLYMPIAD_DATA_FILE));
	OlympiadProperties.setProperty("CurrentCycle", String.valueOf(_currentCycle));
	OlympiadProperties.setProperty("Period", String.valueOf(_period));
	OlympiadProperties.setProperty("OlympiadEnd", String.valueOf(_olympiadEnd));
	OlympiadProperties.setProperty("ValdationEnd", String.valueOf(_validationEnd));
	OlympiadProperties.setProperty("NextWeeklyChange", String.valueOf(_nextWeeklyChange));
	OlympiadProperties.store(fos, "Olympiad Properties");
	fos.close();
    }
    
    public static void processPlayer(L2PcInstance player) 
    { 
    	L2PcInstance _opponent = L2World.getInstance().getPlayer(player.getName()); 
    	StatsSet playerStat = _nobles.get(player.getObjectId()); 
    	int playerPoints = playerStat.getInteger(POINTS); 
    	int lostPoints = playerPoints / 3; 
    	_nobles.remove(player.getObjectId()); 
    	_nobles.put(player.getObjectId(), playerStat); 
    	playerStat.set("olympiad_points", playerPoints - lostPoints); 
    	SystemMessage sm = new SystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS); 
    	sm.addString(player.getName()); 
    	sm.addNumber(lostPoints); 
    	_opponent.sendPacket(sm); 
    } 
}