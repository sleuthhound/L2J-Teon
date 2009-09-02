/* This program is free software; you can redistribute it and/or modify
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
package net.sf.l2j.gameserver.model.entity.L2JOneoEvents.ChainAutomation;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.entity.L2JOneoEvents.TvTEvent;
import net.sf.l2j.gameserver.model.entity.L2JOneoEvents.VIP;

/**
 * This class is the L2J Oneo Event Chain Two which is called upon by the
 * L2JOneoEventChainOne. <br>
 * This class runs the VIP Engine.<br>
 * This class also starts the L2J Oneo Event Manager Instance when the VIP is
 * completed/ended/disabled causing an infinite cycle.<br>
 * Part of the L2J Event Automation System.<br>
 * 
 * @author Ahmed
 */
public class L2JOneoEventChainTwo
{
    public static void run()
    {
	if (Config.VIP_EVENT_ENABLED)
	{
	    waiter(Config.TIME_BETWEEN_EVENTS * 60); // Waits the delay
	    // before starting the
	    // event (in minutes)
	    if ((VIP._joining == false) && (VIP._started == false))
	    {
		VIP.setRandomTeam();
		VIP.startJoin();
		VIP._joining = true;
	    }
	    if ((VIP._joining == true) && (VIP._started == false))
	    {
		waiter(VIP._delay * 60); // Waits the delay before
		// starting the event (in
		// minutes)
		VIP._joining = false;
		VIP._started = true;
		VIP.startEvent();
		waiter(VIP._time * 60); // Waits the delay before starting the
		// event (in minutes)
		VIP.endEventTime();
		waiter(Config.TIME_BETWEEN_EVENTS * 60);
		TvTEvent.EventInProgress = false;
		L2JOneoEventManager.getInstance().init();
		return;
	    }
	} else if (!Config.VIP_EVENT_ENABLED)
	{
	    TvTEvent.EventInProgress = false;
	    L2JOneoEventManager.getInstance().init();
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
    static void waiter(int seconds)
    {
	while (seconds > 1)
	{
	    seconds--;
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
		    // meh...
		}
	    }
	}
    }
}