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

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.CharSchemesTable;
import net.sf.l2j.gameserver.gameserverpackets.ServerStatus;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.CursedWeaponsManager;
import net.sf.l2j.gameserver.instancemanager.GrandBossManager; 
import net.sf.l2j.gameserver.instancemanager.ItemsOnGroundManager;
import net.sf.l2j.gameserver.instancemanager.RaidBossSpawnManager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.ServerClose;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * 
 * This class provides the functions for shutting down and restarting the server
 * It closes all open clientconnections and saves all data.
 * 
 * @Date: 2007/09/05 00:00:00 $
 */
public class Shutdown extends Thread
{
    private static Logger _log = Logger.getLogger(Shutdown.class.getName());
    private static Shutdown _instance;
    private static Shutdown _counterInstance = null;
    private int _secondsShut;
    private int _shutdownMode;
    public static final int SIGTERM = 0;
    public static final int GM_SHUTDOWN = 1;
    public static final int GM_RESTART = 2;
    public static final int ABORT = 3;
    private static final String[] MODE_TEXT = { "SIGTERM", "shutting down", "restarting", "aborting" };

    /**
     * This function starts a shutdown countdown from Telnet (Copied from
     * Function startShutdown())
     * 
     * @param ip
     *                IP Which Issued shutdown command
     * @param seconds
     *                seconds untill shutdown
     * @param restart
     *                true if the server will restart after shutdown
     */
    public void startTelnetShutdown(String IP, int seconds, boolean restart)
    {
	// restart or shutdown?
	_shutdownMode = restart ? GM_RESTART : GM_SHUTDOWN;
	// log event
	_log.warning("IP: " + IP + " issued shutdown command. " + MODE_TEXT[_shutdownMode] + " in " + seconds + " seconds.");
	// announce to players
	Announcements _an = Announcements.getInstance();
	_an.announceToAll("Attention players!");
	_an.announceToAll("Server is " + MODE_TEXT[_shutdownMode] + " in " + seconds + " seconds!");
	_an.announceToAll("Please, avoid to use Gatekeepers or SoE.");
	// already started? abort it
	if (_counterInstance != null)
	    _counterInstance._abort();
	// init & start countdown
	_counterInstance = new Shutdown(seconds, restart);
	_counterInstance.start();
    }

    /**
     * This function aborts a running countdown
     * 
     * @param IP
     *                IP Which Issued shutdown command
     */
    public void telnetAbort(String IP)
    {
	// log event
	_log.warning("IP: " + IP + " issued shutdown ABORT. " + MODE_TEXT[_shutdownMode] + " has been stopped!");
	// announce to players
	Announcements _an = Announcements.getInstance();
	_an.announceToAll("Server aborts " + MODE_TEXT[_shutdownMode] + " and continues normal operation!");
	// stop countdown
	if (_counterInstance != null)
	    _counterInstance._abort();
    }

    /**
     * Default constucter is only used internal to create the shutdown-hook
     * instance
     * 
     */
    public Shutdown()
    {
	_secondsShut = -1;
	_shutdownMode = SIGTERM;
    }

    /**
     * This creates a countdown instance of Shutdown.
     * 
     * @param seconds
     *                how many seconds until shutdown
     * @param restart
     *                true is the server shall restart after shutdown
     * 
     */
    public Shutdown(int seconds, boolean restart)
    {
	// procedure time
	_secondsShut = seconds < 0 ? 0 : seconds;
	// restart or shutdown?
	_shutdownMode = restart ? GM_RESTART : GM_SHUTDOWN;
    }

    /**
     * get the shutdown-hook instance the shutdown-hook instance is created
     * by the first call of this function, but it has to be registrered
     * externaly.
     * 
     * @return instance of Shutdown, to be used as shutdown hook
     */
    public static Shutdown getInstance()
    {
	if (_instance == null)
	    _instance = new Shutdown();
	return _instance;
    }

    /** Method for the Exploit Prevention (Safe_Sigterm) * */
    public static Shutdown getCounterInstance()
    {
	return _counterInstance;
    }

    /**
     * this function is called, when a new thread starts
     * 
     * if this thread is the thread of getInstance, then this is the
     * shutdown hook and we save all data and disconnect all clients.
     * 
     * after this thread ends, the server will completely exit
     * 
     * if this is not the thread of getInstance, then this is a countdown
     * thread. we start the countdown, and when we finished it, and it was
     * not aborted, we tell the shutdown-hook why we call exit, and then
     * call exit
     * 
     * when the exit status of the server is 1, startServer.sh /
     * startServer.bat will restart the server.
     * 
     */
    @Override
    public void run()
    {
	if (this == _instance)
	{
	    // ensure all services are stopped
	    try
	    {
		GameTimeController.getInstance().stopTimer();
	    } catch (Throwable t)
	    { /* ignore all */
	    }
	    // stop all threadpolls
	    try
	    {
		ThreadPoolManager.getInstance().shutdown();
	    } catch (Throwable t)
	    { /* ignore all */
	    }
	    // last byebye, save all data and quit this server
	    saveData(); // logging doesnt work here :(
	    try
	    {
		LoginServerThread.getInstance().interrupt();
	    } catch (Throwable t)
	    { /* ignore all */
	    }
	    // saveData sends messages to exit players, so sgutdown selector
	    // after it
	    try
	    {
		GameServer.gameServer.getSelectorThread().shutdown();
		GameServer.gameServer.getSelectorThread().setDaemon(true);
	    } catch (Throwable t)
	    { /* ignore all */
	    }
	    // commit data, last chance
	    try
	    {
		L2DatabaseFactory.getInstance().shutdown();
	    } catch (Throwable t)
	    {
	    }
	    // server will quit, when this function ends.
	    int haltCode = _instance._shutdownMode == GM_SHUTDOWN ? 0 : 2;
	    Runtime.getRuntime().halt(haltCode);
	} else
	{
	    // gm shutdown: send warnings and then call exit to start
	    // shutdown
	    // sequence
	    countdown();
	    // last point where logging is operational :(
	    _log.warning("GM shutdown countdown is over. " + MODE_TEXT[_shutdownMode] + " NOW!");
	    // setting mode
	    if (_shutdownMode != ABORT)
	    {
		int exitCode = _shutdownMode == GM_SHUTDOWN ? 0 : 2;
		_instance.setMode(_shutdownMode);
		System.exit(exitCode);
	    }
	}
    }

    /**
     * This functions starts a shutdown countdown
     * 
     * @param activeChar
     *                GM who issued the shutdown command
     * @param seconds
     *                seconds until shutdown
     * @param restart
     *                true if the server will restart after shutdown
     */
    public void startShutdown(L2PcInstance activeChar, int seconds, boolean restart)
    {
	// restart or shutdown?
	_shutdownMode = restart ? GM_RESTART : GM_SHUTDOWN;
	// log event
	_log.warning("GM: " + activeChar.getName() + " (" + activeChar.getObjectId() + ") issued shutdown command. " + MODE_TEXT[_shutdownMode] + " in " + seconds + " seconds!");
	// announce to playres
	Announcements _an = Announcements.getInstance();
	_an.announceToAll("Attention players!");
	_an.announceToAll("Server is " + MODE_TEXT[_shutdownMode] + " in " + seconds + " seconds!");
	_an.announceToAll("Please, avoid to use Gatekeepers or SoE.");
	// already started? abort it
	if (_counterInstance != null)
	    _counterInstance._abort();
	// init & start countdown
	_counterInstance = new Shutdown(seconds, restart);
	_counterInstance.start();
    }

    /**
     * This function aborts a running countdown
     * 
     * @param activeChar
     *                GM who issued the abort command
     */
    public void abort(L2PcInstance activeChar)
    {
	// log event
	_log.warning("GM: " + activeChar.getName() + " (" + activeChar.getObjectId() + ") issued shutdown ABORT. " + MODE_TEXT[_shutdownMode] + " has been stopped!");
	// announce to players
	Announcements _an = Announcements.getInstance();
	_an.announceToAll("Server aborts " + MODE_TEXT[_shutdownMode] + " and continues normal operation!");
	// about procedure
	if (_counterInstance != null)
	    _counterInstance._abort();
    }

    /**
     * set the shutdown mode
     * 
     * @param mode
     *                what mode shall be set
     */
    private void setMode(int mode)
    {
	_shutdownMode = mode;
    }

    /**
     * set shutdown mode to ABORT
     * 
     */
    private void _abort()
    {
	_shutdownMode = ABORT;
    }

    /**
     * this counts the countdown and reports it to all players countdown is
     * aborted if mode changes to ABORT
     */
    private void countdown()
    {
	Announcements _an = Announcements.getInstance();
	try
	{
	    while (_secondsShut > 0)
	    {
		switch (_secondsShut)
		{
		case 1200:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 20 minutes.");
		    break;
		case 1140:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 19 minutes.");
		    break;
		case 1080:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 18 minutes.");
		    break;
		case 1020:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 17 minutes.");
		    break;
		case 960:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 16 minutes.");
		    break;
		case 900:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 15 minutes.");
		    break;
		case 840:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 14 minutes.");
		    break;
		case 780:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 13 minutes.");
		    break;
		case 720:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 12 minutes.");
		    break;
		case 660:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 11 minutes.");
		    break;
		case 600:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 10 minutes.");
		    break;
		case 540:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 9 minutes.");
		    break;
		case 480:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 8 minutes.");
		    break;
		case 420:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 7 minutes.");
		    break;
		case 360:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 6 minutes.");
		    break;
		case 300:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 5 minutes.");
		    break;
		case 240:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 4 minutes.");
		    break;
		case 180:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 3 minutes.");
		    break;
		case 120:
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 2 minutes.");
		    break;
		case 60: // avoids new players from logging in
		    LoginServerThread.getInstance().setServerStatus(ServerStatus.STATUS_DOWN);
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 1 minute.");
		    break;
		case 30: // inform players about 30 seconds game close
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 30 seconds.");
		    informAllCharacters(30);
		    break;
		case 10: // inform players about 10 seconds game close
		    _an.announceToAll("The server is " + MODE_TEXT[_shutdownMode] + " in 10 seconds, please log out now!");
		    informAllCharacters(10);
		    break;
		}
		_secondsShut--;
		Thread.sleep(1000);
		if (_shutdownMode == ABORT)
		    break;
	    }
	} catch (InterruptedException e)
	{ /* this will never happen */
	}
    }

    /**
     * this sends a last byebye, disconnects all players and saves data
     * 
     */
    private void saveData()
    {
	switch (_shutdownMode)
	{
	case SIGTERM:
	    System.err.println("SIGTERM received. Shutting down NOW!");
	    break;
	case GM_SHUTDOWN:
	    System.err.println("GM shutdown received. Shutting down NOW!");
	    break;
	case GM_RESTART:
	    System.err.println("GM restart received. Restarting NOW!");
	    break;
	}
	if (Config.ACTIVATE_POSITION_RECORDER)
	    Universe.getInstance().implode(true);
	try
	{
	    Announcements _an = Announcements.getInstance();
	    _an.announceToAll("Server is " + MODE_TEXT[_shutdownMode] + " NOW!");
	} catch (Throwable t)
	{
	    _log.log(Level.INFO, "", t);
	}
	disconnectAllCharacters();
	// seven signs data is now saved along with festival data
	if (!SevenSigns.getInstance().isSealValidationPeriod())
	    SevenSignsFestival.getInstance().saveFestivalData(false);
	// save seven signs data before closing
	SevenSigns.getInstance().saveSevenSignsData(null, true);
	// save all Grandboss status
        GrandBossManager.getInstance().cleanUp();
	System.err.println("GrandBossManager: Data Saved.");
	// save all raidboss status
	RaidBossSpawnManager.getInstance().cleanUp();
	System.err.println("RaidBossSpawnManager: Data Saved.");
	// trade controller
	TradeController.getInstance().dataCountStore();
	System.err.println("TradeController: Data Saved.");
	// olympiad
	try
	{
	    Olympiad.getInstance().save();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
	System.err.println("Olympiad System: Data Saved.");
	// save cursed weapons data
	CursedWeaponsManager.getInstance().saveData();
	System.err.println("CursedWeaponsManager: Data Saved.");
	// save manor data
	CastleManorManager.getInstance().save();
	System.err.println("CastleManorManager: Data Saved.");
	// Save all global (non-player specific) Quest data that needs to persist after reboot
	QuestManager.getInstance().save();
        
        // NPCBuffer: save player schemes data
        if (Config.NPCBUFFER_FEATURE_ENABLED && Config.NPCBUFFER_STORE_SCHEMES) CharSchemesTable.getInstance().onServerShutdown();
        
	System.err.println("Quest Engine: Data Saved.");
	// save items on ground
	if (Config.SAVE_DROPPED_ITEM)
	{
	    ItemsOnGroundManager.getInstance().saveInDb();
	    ItemsOnGroundManager.getInstance().cleanUp();
	    System.err.println("ItemsOnGroundManager:  Data Saved.");
	}
	System.err.println("Data saved. All players disconnected, shutting down.");
	try
	{
	    Thread.sleep(5000);
	} catch (InterruptedException e)
	{ /* never happens :p */
	}
    }

    /**
     * inform all chars about procedure by sending sys message (1)
     * 
     */
    private void informAllCharacters(int seconds)
    {
	for (L2PcInstance player : L2World.getInstance().getAllPlayers())
	{
	    try
	    {
		SystemMessage sm = new SystemMessage(SystemMessageId.THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_SECONDS);
		sm.addNumber(seconds);
		player.sendPacket(sm);
	    } catch (Throwable t)
	    { /* ignore all */
	    }
	}
    }

    /**
     * this disconnects all clients from the server
     * 
     */
    private void disconnectAllCharacters()
    {
	// logout character
	for (L2PcInstance player : L2World.getInstance().getAllPlayers())
	{
	    try
	    {
		L2GameClient.saveCharToDisk(player);
		ServerClose ql = new ServerClose();
		player.sendPacket(ql);
	    } catch (Throwable t)
	    { /* ignore all */
	    }
	}
	try
	{
	    Thread.sleep(1000);
	} catch (Throwable t)
	{
	    _log.log(Level.INFO, "", t);
	}
	for (L2PcInstance player : L2World.getInstance().getAllPlayers())
	{
	    try
	    {
		player.closeNetConnection();
	    } catch (Throwable t)
	    { /*
	     * just to make sure we try to kill the connection
	     */
	    }
	}
    }
}
