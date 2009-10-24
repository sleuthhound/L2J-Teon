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
import net.sf.l2j.gameserver.datatables.MapRegionTable;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.BanditStrongholdSiege;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.DevastatedCastleManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortResistSiegeManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortressofTheDeadManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.WildBeastFarmSiege;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * @author Maxi
 */
public class L2ClanHallSiegeZone extends L2ZoneType
{
	private String _zoneName;

	public L2ClanHallSiegeZone(int id)
	{
		super(id);
	}

	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("name"))
		{
			_zoneName = value;
		}
		else
			super.setParameter(name, value);
	}

	@Override
	protected void onEnter(L2Character character)
	{
		if (character instanceof L2PcInstance && FortResistSiegeManager.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Fortress of Resistance"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.ENTERED_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && BanditStrongholdSiege.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Bandit Stronghold"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.ENTERED_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && DevastatedCastleManager.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Devastated Castle"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.ENTERED_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && WildBeastFarmSiege.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Beast Farm"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.ENTERED_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && FortressofTheDeadManager.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Fortress of the Dead"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.ENTERED_COMBAT_ZONE));
		}
		// if (character instanceof L2SiegeSummonInstance)
		// ((L2SiegeSummonInstance)character).unSummon(((L2SiegeSummonInstance)character).getOwner());
	}

	@Override
	protected void onExit(L2Character character)
	{
		if (character instanceof L2PcInstance && FortResistSiegeManager.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Fortress of Resistance"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && BanditStrongholdSiege.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Bandit Stronghold"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && DevastatedCastleManager.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Devastated Castle"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && WildBeastFarmSiege.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Beast Farm"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
		}
		if (character instanceof L2PcInstance && FortressofTheDeadManager.getInstance().getIsInProgress())
		{
			if (_zoneName.equalsIgnoreCase("Fortress of the Dead"))
				character.setInsideZone(L2Character.ZONE_PVP, true);
			character.setInsideZone(L2Character.ZONE_SIEGE, true);
			character.setInsideZone(L2Character.ZONE_NOSUMMONFRIEND, true);
			((L2PcInstance) character).sendPacket(new SystemMessage(SystemMessageId.LEFT_COMBAT_ZONE));
		}
		// if (character instanceof L2SiegeSummonInstance)
		// ((L2SiegeSummonInstance)character).unSummon(((L2SiegeSummonInstance)character).getOwner());
	}

	@Override
	public void onDieInside(L2Character character)
	{
	}

	@Override
	public void onReviveInside(L2Character character)
	{
	}

	/**
	 * Removes all foreigners from the clanhall
	 * 
	 * @param owningClanId
	 */
	public void banishForeigners(int owningClanId)
	{
		for (L2Character temp : _characterList.values())
		{
			if (!(temp instanceof L2PcInstance))
				continue;
			if (((L2PcInstance) temp).getClanId() == owningClanId)
				continue;
			((L2PcInstance) temp).teleToLocation(MapRegionTable.TeleportWhereType.Town);
		}
	}

	/**
	 * Sends a message to all players in this zone
	 * 
	 * @param message
	 */
	public void announceToPlayers(String message)
	{
		for (L2Character temp : _characterList.values())
		{
			if (temp instanceof L2PcInstance)
				((L2PcInstance) temp).sendMessage(message);
		}
	}

	/**
	 * Returns all players within this zone
	 * 
	 * @return
	 */
	public FastList<L2PcInstance> getAllPlayers()
	{
		FastList<L2PcInstance> players = new FastList<L2PcInstance>();
		for (L2Character temp : _characterList.values())
		{
			if (temp instanceof L2PcInstance)
				players.add((L2PcInstance) temp);
		}
		return players;
	}

	public String getZoneName()
	{
		return _zoneName;
	}
}
