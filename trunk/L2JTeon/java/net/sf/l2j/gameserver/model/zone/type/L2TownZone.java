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
package net.sf.l2j.gameserver.model.zone.type;

import javolution.util.FastList;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.util.Rnd;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * A Town zone
 *
 * @author  durgus
 */
public class L2TownZone extends L2ZoneType
{
	private String _townName;
	private int _townId;
	private int _redirectTownId;
	private int _taxById;
	private boolean _isPeaceZone;
	private FastList _spawnLocs;

	public L2TownZone(int id)
	{
		super(id);

		_taxById = 0;
		_spawnLocs = new FastList();
		// Default to Giran
		_redirectTownId = 9;
		// Default peace zone
		_isPeaceZone = true;
	}

	@Override
	public void setParameter(String s, String s1)
	{
        if(s.equals("name"))
            _townName = s1;
        else
        if(s.equals("townId"))
            _townId = Integer.parseInt(s1);
        else
        if(s.equals("redirectTownId"))
            _redirectTownId = Integer.parseInt(s1);
        else
        if(s.equals("taxById"))
            _taxById = Integer.parseInt(s1);
        else
        if(s.equals("isPeaceZone"))
            _isPeaceZone = Boolean.parseBoolean(s1);
        else
            super.setParameter(s, s1);
	}

	// L2JTeon Maxi
    public void setSpawnLocs(Node node)
    {
        int ai[] = new int[3];
        Node node1 = node.getAttributes().getNamedItem("X");
        if(node1 != null)
            ai[0] = Integer.parseInt(node1.getNodeValue());
        node1 = node.getAttributes().getNamedItem("Y");
        if(node1 != null)
            ai[1] = Integer.parseInt(node1.getNodeValue());
        node1 = node.getAttributes().getNamedItem("Z");
        if(node1 != null)
            ai[2] = Integer.parseInt(node1.getNodeValue());
        if(ai != null)
            _spawnLocs.add(ai);
    }

	@Override
	protected void onEnter(L2Character character)
	{
		if (character instanceof L2PcInstance)
		{
			// PVP possible during siege, now for siege participants only
			// Could also check if this town is in siege, or if any siege is going on
			if (((L2PcInstance)character).getSiegeState() != 0 && Config.ZONE_TOWN == 1)
				return;
		}
		if (_isPeaceZone && Config.ZONE_TOWN != 2) character.setInsideZone(L2Character.ZONE_PEACE, true);
	}

	@Override
	protected void onExit(L2Character character)
	{
		// TODO: there should be no exit if there was possibly no enter
		if (_isPeaceZone) character.setInsideZone(L2Character.ZONE_PEACE, false);
	}

	@Override
	protected void onDieInside(L2Character character)
	{
	}

	@Override
	protected void onReviveInside(L2Character character)
	{
	}

	/**
	 * Returns this town zones name
	 * @return
	 */
	@Deprecated
	public String getName()
	{
		return _townName;
	}

	/**
	 * Returns this zones town id (if any)
	 * @return
	 */
	public int getTownId()
	{
		return _townId;
	}

	/**
	 * Gets the id for this town zones redir town
	 * @return
	 */
	@Deprecated
	public int getRedirectTownId()
	{
		return _redirectTownId;
	}

	/**
	 * Returns this zones spawn location
	 * @return
	 */
	public final int[] getSpawnLoc()
	{
        int ai[] = new int[3];
        ai = (int[])_spawnLocs.get(Rnd.get(_spawnLocs.size()));
        return ai;
	}

	/**
	 * Returns this town zones castle id
	 * @return
	 */
	public final int getTaxById()
	{
    	return _taxById;
	}

	public final boolean isPeaceZone()
	{
		return _isPeaceZone;
	}
}
