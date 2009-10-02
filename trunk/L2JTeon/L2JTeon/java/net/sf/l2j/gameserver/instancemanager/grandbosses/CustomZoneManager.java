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
package net.sf.l2j.gameserver.instancemanager.grandbosses;

import javolution.util.FastList;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.zone.type.L2CustomZone;

public class CustomZoneManager
{
    // =========================================================
    private static CustomZoneManager _instance;
    public static final CustomZoneManager getInstance()
    {
        if (_instance == null)
        {
    		System.out.println("Initializing CustomZoneManager");
        	_instance = new CustomZoneManager();
        }
        return _instance;
    }
    // =========================================================

    
    // =========================================================
    // Data Field
    private FastList<L2CustomZone> _zones;
    
    // =========================================================
    // Constructor
    public CustomZoneManager()
    {
    }

    // =========================================================
    // Property - Public
    
    public void addZone(L2CustomZone zone)
    {
    	if (_zones == null)
    		_zones = new FastList<L2CustomZone>();
    	
    	_zones.add(zone);
    }

    public final L2CustomZone getZone(L2Character character)
    {
    	for (L2CustomZone temp : _zones)
    		if (temp.isCharacterInZone(character)) return temp;
    	return null;
    }
    
    /**
     * Returns the town at that position (if any)
     * @param x
     * @param y
     * @param z
     * @return
     */
    public final L2CustomZone getZone(int x, int y, int z)
    {
        for (L2CustomZone temp : _zones)
        	if (temp.isInsideZone(x, y, z)) return temp;
        return null;
    }
    
    public boolean checkIfInZone(String zoneType, L2Object obj)
    {
    	L2CustomZone temp = getZone(obj.getX(), obj.getY(), obj.getZ());
    	if (temp == null) return false;
        return temp.getZoneName().equalsIgnoreCase(zoneType);
    }
    
}
