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
package net.sf.l2j.gameserver.instancemanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javolution.util.FastMap;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;

/**
 * 
 * futuro suporte de los clanhall siege 
 * @author maxi56
 */
public class ChSiegeManager
{
    private static ChSiegeManager _instance;
    private Map<Integer, ClanHall> _clanHall;
    private boolean _loaded = false;

    public static ChSiegeManager getInstance()
    {
	if (_instance == null)
	{
	    System.out.println("Initializing ChSiegeManager");
	    _instance = new ChSiegeManager();
	}
	return _instance;
    }

    public boolean loaded()
    {
	return _loaded;
    }

    private ChSiegeManager()
    {
	_clanHall = new FastMap<Integer, ClanHall>();
    }
    /** Get Map with all ClanHalls */
    public final Map<Integer, ClanHall> getClanHalls()
    {
	return _clanHall;
    }

    /** Get Clan Hall by Id */
    public final ClanHall getClanHallById(int clanHallId)
    {
	if (_clanHall.containsKey(clanHallId))
	    return _clanHall.get(clanHallId);
	return null;
    }

    /** Get Clan Hall by x,y,z */
    /*
     * public final ClanHall getClanHall(int x, int y, int z) { for
     * (Map.Entry<Integer, ClanHall> ch : _clanHall.entrySet()) if
     * (ch.getValue().getZone().isInsideZone(x, y, z)) return ch.getValue();
     * 
     * for (Map.Entry<Integer, ClanHall> ch : _freeClanHall.entrySet()) if
     * (ch.getValue().getZone().isInsideZone(x, y, z)) return ch.getValue();
     * 
     * return null; }
     */
    public final ClanHall getNearbyClanHall(int x, int y, int maxDist)
    {
	for (Map.Entry<Integer, ClanHall> ch : _clanHall.entrySet())
	    if (ch.getValue().getZone().getDistanceToZone(x, y) < maxDist)
		return ch.getValue();
	return null;
    }

    /** Get Clan Hall by Owner */
    public final ClanHall getClanHallByOwner(L2Clan clan)
    {
	for (Map.Entry<Integer, ClanHall> ch : _clanHall.entrySet())
	    if (clan.getClanId() == ch.getValue().getOwnerId())
		return ch.getValue();
	return null;
    }
}