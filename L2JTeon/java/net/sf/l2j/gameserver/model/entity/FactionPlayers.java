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
package net.sf.l2j.gameserver.model.entity;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2World;

/**
 * 
 * @author DaRkRaGe
 * 
 */
public class FactionPlayers
{
    private static FactionPlayers _instance;

    class AnnounceFaction implements Runnable
    {
	public void run()
	{
	    Announcements.getInstance().announceToAll(Config.KOOFS_NAME_TEAM + L2World.getInstance().getAllkoofPlayersCount() + " || " + Config.NOOBS_NAME_TEAM + L2World.getInstance().getAllnoobPlayersCount());
	    ThreadPoolManager.getInstance().scheduleGeneral(new AnnounceFaction(), Config.FACTION_ANNOUNCE_TIME);
	}
    }

    public static FactionPlayers getInstance()
    {
	if (_instance == null)
	{
	    _instance = new FactionPlayers();
	}
	return _instance;
    }

    private FactionPlayers()
    {
	if (Config.FACTION_ANNOUNCE_TIME > 0)
	{
	    ThreadPoolManager.getInstance().scheduleGeneral(new AnnounceFaction(), Config.FACTION_ANNOUNCE_TIME);
	}
    }
}