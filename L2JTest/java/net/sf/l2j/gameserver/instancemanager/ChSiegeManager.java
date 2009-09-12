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
}