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
package net.sf.l2j.gameserver.datatables;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.ClanHallManager;
import net.sf.l2j.gameserver.instancemanager.TownManager;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.L2Character;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.ClanHall;
import net.sf.l2j.gameserver.model.zone.type.L2ArenaZone;
import net.sf.l2j.gameserver.model.zone.type.L2ClanHallZone;
import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class MapRegionTable
{
	private static Logger _log = Logger.getLogger(MapRegionTable.class.getName());
	
	private final int[][] _regions = new int[19][21];
	
	public static enum TeleportWhereType
	{
		Castle,
		ClanHall,
		SiegeFlag,
		Town
	}
	
	public static MapRegionTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected MapRegionTable()
	{
		int count = 0;
		
		try
		{
			File f = new File(Config.DATAPACK_ROOT + "/data/xml/map_region.xml");
			Document doc = XMLDocumentFactory.getInstance().loadDocument(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equalsIgnoreCase("map"))
				{
					int region = Integer.valueOf(d.getAttributes().getNamedItem("region").getNodeValue());
					for (int j = 0; j < 10; j++)
						_regions[j][region] = Integer.valueOf(d.getAttributes().getNamedItem("sec" + j).getNodeValue());
					
					count++;
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error loading Map Region Table.", e);
		}
		_log.info("MapRegionTable: Loaded " + count + " regions.");
	}
	
	public final int getMapRegion(int posX, int posY)
	{
		try
		{
			return _regions[getMapRegionX(posX)][getMapRegionY(posY)];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			// Position sent is outside MapRegionTable area.
			if (Config.DEBUG)
				_log.log(Level.WARNING, "MapRegionTable: Player outside map regions at X,Y=" + posX + "," + posY, e);
			return 0;
		}
	}
	
	public final static int getMapRegionX(int posX)
	{
		return (posX >> 15) + 4;// + centerTileX;
	}
	
	public final static int getMapRegionY(int posY)
	{
		return (posY >> 15) + 10;// + centerTileX;
	}
	
	public int getAreaCastle(L2Character activeChar)
	{
		int area = getClosestTownNumber(activeChar);
		int castle;
		switch (area)
		{
			case 0:
				castle = 1;
				break;// Talking Island Village
			case 1:
				castle = 4;
				break; // Elven Village
			case 2:
				castle = 4;
				break; // Dark Elven Village
			case 3:
				castle = 9;
				break; // Orc Village
			case 4:
				castle = 9;
				break; // Dwarven Village
			case 5:
				castle = 1;
				break; // Town of Gludio
			case 6:
				castle = 1;
				break; // Gludin Village
			case 7:
				castle = 2;
				break; // Town of Dion
			case 8:
				castle = 3;
				break; // Town of Giran
			case 9:
				castle = 4;
				break; // Town of Oren
			case 10:
				castle = 5;
				break; // Town of Aden
			case 11:
				castle = 5;
				break; // Hunters Village
			case 12:
				castle = 3;
				break; // Giran Harbor
			case 13:
				castle = 6;
				break; // Heine
			case 14:
				castle = 8;
				break; // Rune Township
			case 15:
				castle = 7;
				break; // Town of Goddard
			case 16:
				castle = 9;
				break; // Town of Shuttgart
			case 17:
				castle = 4;
				break; // Ivory Tower
			case 18:
				castle = 8;
				break; // Primeval Isle Wharf
			default:
				castle = 5;
				break; // Town of Aden
		}
		return castle;
	}
	
	public int getClosestTownNumber(L2Character activeChar)
	{
		return getMapRegion(activeChar.getX(), activeChar.getY());
	}
	
	/**
	 * Get town name by character position
	 * @param activeChar
	 * @return String
	 */
	public String getClosestTownName(L2Character activeChar)
	{
		return getClosestTownName(getMapRegion(activeChar.getX(), activeChar.getY()));
	}
	
	public String getClosestTownName(int townId)
	{
		String nearestTown = null;
		switch (townId)
		{
			case 0:
				nearestTown = "Talking Island Village";
				break;
			case 1:
				nearestTown = "Elven Village";
				break;
			case 2:
				nearestTown = "Dark Elven Village";
				break;
			case 3:
				nearestTown = "Orc Village";
				break;
			case 4:
				nearestTown = "Dwarven Village";
				break;
			case 5:
				nearestTown = "Town of Gludio";
				break;
			case 6:
				nearestTown = "Gludin Village";
				break;
			case 7:
				nearestTown = "Town of Dion";
				break;
			case 8:
				nearestTown = "Town of Giran";
				break;
			case 9:
				nearestTown = "Town of Oren";
				break;
			case 10:
				nearestTown = "Town of Aden";
				break;
			case 11:
				nearestTown = "Hunters Village";
				break;
			case 12:
				nearestTown = "Giran Harbor";
				break;
			case 13:
				nearestTown = "Heine";
				break;
			case 14:
				nearestTown = "Rune Township";
				break;
			case 15:
				nearestTown = "Town of Goddard";
				break;
			case 16:
				nearestTown = "Town of Shuttgart";
				break;
			case 18:
				nearestTown = "Primeval Isle";
				break;
			default:
				nearestTown = "Town of Aden";
				break;
		}
		return nearestTown;
	}
	
	public Location getTeleToLocation(L2Character activeChar, TeleportWhereType teleportWhere)
	{
		if (activeChar instanceof L2PcInstance)
		{
			L2PcInstance player = ((L2PcInstance) activeChar);
			
			// If in Monster Derby Track
			if (player.isInsideZone(L2Character.ZONE_MONSTERTRACK))
				return new Location(12661, 181687, -3560);
			
			Castle castle = null;
			ClanHall clanhall = null;
			
			if (player.getClan() != null)
			{
				// If teleport to clan hall
				if (teleportWhere == TeleportWhereType.ClanHall)
				{
					clanhall = ClanHallManager.getInstance().getClanHallByOwner(player.getClan());
					if (clanhall != null)
					{
						L2ClanHallZone zone = clanhall.getZone();
						if (zone != null)
							return zone.getSpawnLoc();
					}
				}
				
				// If teleport to castle
				if (teleportWhere == TeleportWhereType.Castle)
				{
					castle = CastleManager.getInstance().getCastleByOwner(player.getClan());
					
					// check if player is on castle and player's clan is defender
					if (castle == null)
					{
						castle = CastleManager.getInstance().getCastle(player);
						if (!(castle != null && castle.getSiege().getIsInProgress() && castle.getSiege().getDefenderClan(player.getClan()) != null))
							castle = null;
					}
					
					if (castle != null && castle.getCastleId() > 0)
						return castle.getCastleZone().getSpawnLoc();
				}
				
				// If teleport to SiegeHQ
				if (teleportWhere == TeleportWhereType.SiegeFlag)
				{
					castle = CastleManager.getInstance().getCastle(player);
					
					if (castle != null && castle.getSiege().getIsInProgress())
					{
						// Check if player's clan is attacker
						List<L2Npc> flags = castle.getSiege().getFlag(player.getClan());
						if (flags != null && !flags.isEmpty())
						{
							// Spawn to flag - Need more work to get player to the nearest flag
							L2Npc flag = flags.get(0);
							return new Location(flag.getX(), flag.getY(), flag.getZ());
						}
					}
				}
			}
			
			// Karma player land out of city
			if (player.getKarma() > 0)
				return TownManager.getClosestTown(activeChar).getChaoticSpawnLoc();
			
			// Checking if in arena
			L2ArenaZone arena = ZoneManager.getArena(player);
			if (arena != null)
				return arena.getSpawnLoc();
			
			// Checking if needed to be respawned in "far" town from the castle;
			castle = CastleManager.getInstance().getCastle(player);
			if (castle != null)
			{
				if (castle.getSiege().getIsInProgress())
				{
					// Check if player's clan is participating
					if ((castle.getSiege().checkIsDefender(player.getClan()) || castle.getSiege().checkIsAttacker(player.getClan())) && SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DAWN)
						return TownManager.getSecondClosestTown(activeChar).getSpawnLoc();
				}
			}
		}
		
		// Get the nearest town
		return TownManager.getClosestTown(activeChar).getSpawnLoc();
	}
	
	private static class SingletonHolder
	{
		protected static final MapRegionTable _instance = new MapRegionTable();
	}
}