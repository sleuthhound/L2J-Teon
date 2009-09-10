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
package net.sf.l2j.gameserver.model.entity.L2JTeonEvents.ChainAutomation;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.TvTEvent;

/**
 * This class is the L2J Teon Event Chain One which is called upon by the L2J
 * Teon Event Manager. <br>
 * This class runs the TvT Engine by FBIagent.<br>
 * This class also calls onto the L2J Teon Event Chain Two.<br>
 * Part of the L2J Event Automation System.<br>
 * 
 * @author Ahmed
 */
public class L2JTeonEventChainOne implements Runnable
{
    public void run()
    {
	if (Config.TVT_EVENT_ENABLED)
	{
	    System.out.println("L2J Teon Event Manager called TvT Task, and TvT is enabled. Event should Start.");
	    waiter(Config.TIME_BETWEEN_EVENTS * 60); // Waits the delay
	    // before starting the
	    // event (in minutes)
	    TvTEvent.init();
	    TvTEvent.startParticipation();
	    System.out.println("TvT Event registration is open for " + Config.TVT_EVENT_PARTICIPATION_TIME + " minutes.");
	    Announcements.getInstance().announceToAll("TvT Event registration is open for " + Config.TVT_EVENT_PARTICIPATION_TIME + " minutes.");
	    waiter(Config.TVT_EVENT_PARTICIPATION_TIME * 60); // in config
	    // given as
	    // minutes
	    System.out.println("TvT Task has ended the wait time for the TvT Engine.");
	    Announcements.getInstance().announceToAll("TvT Event Participation period has ended.");
	    TvTEvent.ParticipationCount();
	    if (TvTEvent.ParticipationCount() == true)
	    {
		TvTEvent.sysMsgToAllParticipants("TvT Event participants will be teleported in " + Config.TVT_EVENT_START_LEAVE_TELEPORT_DELAY + " seconds.");
		TvTEvent.startFight();
		System.out.println("TvT Task Manager has teleported the players to start locations, and TvT Event Tournament Started.");
		System.out.println("TvT Task waiting for the Event to run out of time.");
		waiter(Config.TVT_EVENT_RUNNING_TIME * 60); // in config
		// given
		// as minutes
		System.out.println("TvT Event has run out of time.");
		Announcements.getInstance().announceToAll(TvTEvent.calculateRewards());
		TvTEvent.sysMsgToAllParticipants("TvT Event participants will be teleported back to registration area in " + Config.TVT_EVENT_START_LEAVE_TELEPORT_DELAY + " seconds.");
		TvTEvent.stopFight();
	    }
	} else if (!Config.TVT_EVENT_ENABLED && !Config.VIP_EVENT_ENABLED)
	{
	    System.out.println("L2J Teon TvT Task has stopped the L2J Teon Event Managers execution of the TvT Task due to the TvT Event Engine being disabled.");
	    System.out.println("L2J Teon VIP Engine is disabled and will not start.");
	    return;
	} else if (!Config.TVT_EVENT_ENABLED && Config.VIP_EVENT_ENABLED)
	{
	    L2JTeonEventChainTwo.run();
	    return;
	}
    }

    /**
     * This method waits for a period time delay<br>
     * <br>
     * 
     * @param interval
     *                <br>
     */
    void waiter(int seconds)
    {
	while (seconds > 1)
	{
	    seconds--; // here because we don't want to see two time announce
	    // at the same time
	    if (TvTEvent.isParticipating() || TvTEvent.isStarted())
	    {
		switch (seconds)
		{
		case 3600: // 1 hour left
		    if (TvTEvent.isParticipating())
		    {
			Announcements.getInstance().announceToAll("TvT Event registration opened for: " + seconds / 60 / 60 + "hour(s)!");
		    } else if (TvTEvent.isStarted())
		    {
			TvTEvent.sysMsgToAllParticipants("TvT Event will finish in " + seconds / 60 / 60 + "hour(s)!");
		    }
		    break;
		case 180: // 3 minutes left
		case 60: // 1 minute left
		    if (TvTEvent.isParticipating())
		    {
			Announcements.getInstance().announceToAll("TvT Event registration opened for: " + seconds / 60 + "minute(s)!");
		    } else if (TvTEvent.isStarted())
		    {
			TvTEvent.sysMsgToAllParticipants("TvT Event will finish in " + seconds / 60 + "minute(s)!");
		    }
		    break;
		case 3: // 3 seconds left
		    if (TvTEvent.isParticipating())
		    {
			Announcements.getInstance().announceToAll("TvT Event registration opened for: " + seconds + " second(s)!");
		    } else if (TvTEvent.isStarted())
		    {
			TvTEvent.sysMsgToAllParticipants("TvT Event will finish in " + seconds + " second(s)!");
		    }
		    break;
		}
	    }
	    long oneSecWaitStart = System.currentTimeMillis();
	    // only the try catch with Thread.sleep(1000) give bad
	    // count-down on
	    // high wait times
	    while (oneSecWaitStart + 1000L > System.currentTimeMillis())
	    {
		try
		{
		    Thread.sleep(1);
		} catch (InterruptedException ie)
		{
		}
	    }
	}
    }
}