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

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.entity.L2JOneoEvents.TvTEvent;

/**
 * This class organizes and calls upon the L2J Oneo Event Chains which are part
 * of the L2J Oneo Event Automation System.<br>
 * Part of the L2J Event Automation System.<br>
 * 
 * @author Ahmed
 */
public class L2JOneoEventManager
{
    private static L2JOneoEventManager _instance;

    public static L2JOneoEventManager getInstance()
    {
	if (_instance == null)
	{
	    System.out.println("Initializing L2JOneo Event Manager");
	    _instance = new L2JOneoEventManager();
	}
	return _instance;
    }

    public L2JOneoEventManager()
    {
	init();
    }

    public void init()
    {
	if (!TvTEvent.EventInProgress)
	{
	    System.out.println("L2J Oneo Event Manager has started the L2J Oneo Task Manager.");
	    ThreadPoolManager.getInstance().scheduleGeneral(new L2JOneoEventChainOne(), 0);
	} else if (TvTEvent.EventInProgress)
	    System.out.println("L2J Oneo Event Manager is waiting for an event in progress before starting another cycle.");
	return;
    }
}