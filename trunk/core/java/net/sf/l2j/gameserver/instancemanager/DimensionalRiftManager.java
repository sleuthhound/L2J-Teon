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

import gnu.trove.map.hash.TByteObjectHashMap;

import java.awt.Polygon;
import java.awt.Shape;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastList;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Party;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.DimensionalRift;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.templates.chars.L2NpcTemplate;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.gameserver.xmlfactory.XMLDocumentFactory;
import net.sf.l2j.util.Rnd;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Thanks to L2Fortress and balancer.ru - kombat
 */
public class DimensionalRiftManager
{
	private static Logger _log = Logger.getLogger(DimensionalRiftManager.class.getName());
	private final TByteObjectHashMap<TByteObjectHashMap<DimensionalRiftRoom>> _rooms = new TByteObjectHashMap<>(7);
	private final short DIMENSIONAL_FRAGMENT_ITEM_ID = 7079;
	
	public static DimensionalRiftManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected DimensionalRiftManager()
	{
		loadRooms();
		loadSpawns();
	}
	
	public DimensionalRiftRoom getRoom(byte type, byte room)
	{
		return _rooms.get(type) == null ? null : _rooms.get(type).get(room);
	}
	
	private void loadRooms()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement s = con.prepareStatement("SELECT * FROM dimensional_rift");
			ResultSet rs = s.executeQuery();
			
			while (rs.next())
			{
				// 0 waiting room, 1 recruit, 2 soldier, 3 officer, 4 captain , 5 commander, 6 hero
				byte type = rs.getByte("type");
				byte room_id = rs.getByte("room_id");
				
				// coords related
				int xMin = rs.getInt("xMin");
				int xMax = rs.getInt("xMax");
				int yMin = rs.getInt("yMin");
				int yMax = rs.getInt("yMax");
				int z1 = rs.getInt("zMin");
				int z2 = rs.getInt("zMax");
				int xT = rs.getInt("xT");
				int yT = rs.getInt("yT");
				int zT = rs.getInt("zT");
				boolean isBossRoom = rs.getByte("boss") > 0;
				
				if (!_rooms.containsKey(type))
					_rooms.put(type, new TByteObjectHashMap<DimensionalRiftRoom>(9));
				
				_rooms.get(type).put(room_id, new DimensionalRiftRoom(type, room_id, xMin, xMax, yMin, yMax, z1, z2, xT, yT, zT, isBossRoom));
			}
			
			rs.close();
			s.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Can't load Dimension Rift zones. " + e);
		}
		
		int typeSize = _rooms.keys().length;
		int roomSize = 0;
		
		for (byte b : _rooms.keys())
			roomSize += _rooms.get(b).keys().length;
		
		_log.info("DimensionalRiftManager: Loaded " + typeSize + " room types with " + roomSize + " rooms.");
	}
	
	public void loadSpawns()
	{
		int countGood = 0, countBad = 0;
		try
		{
			File file = new File(Config.DATAPACK_ROOT + "/data/xml/dimensional_rift.xml");
			Document doc = XMLDocumentFactory.getInstance().loadDocument(file);
			
			NamedNodeMap attrs;
			byte type, roomId;
			int mobId, x, y, z, delay, count;
			L2Spawn spawnDat;
			L2NpcTemplate template;
			
			for (Node rift = doc.getFirstChild(); rift != null; rift = rift.getNextSibling())
			{
				if ("rift".equalsIgnoreCase(rift.getNodeName()))
				{
					for (Node area = rift.getFirstChild(); area != null; area = area.getNextSibling())
					{
						if ("area".equalsIgnoreCase(area.getNodeName()))
						{
							attrs = area.getAttributes();
							type = Byte.parseByte(attrs.getNamedItem("type").getNodeValue());
							
							for (Node room = area.getFirstChild(); room != null; room = room.getNextSibling())
							{
								if ("room".equalsIgnoreCase(room.getNodeName()))
								{
									attrs = room.getAttributes();
									roomId = Byte.parseByte(attrs.getNamedItem("id").getNodeValue());
									
									for (Node spawn = room.getFirstChild(); spawn != null; spawn = spawn.getNextSibling())
									{
										if ("spawn".equalsIgnoreCase(spawn.getNodeName()))
										{
											attrs = spawn.getAttributes();
											mobId = Integer.parseInt(attrs.getNamedItem("mobId").getNodeValue());
											delay = Integer.parseInt(attrs.getNamedItem("delay").getNodeValue());
											count = Integer.parseInt(attrs.getNamedItem("count").getNodeValue());
											
											template = NpcTable.getInstance().getTemplate(mobId);
											if (template == null)
												_log.log(Level.WARNING, "Template " + mobId + " not found!");
											if (!_rooms.containsKey(type))
												_log.log(Level.WARNING, "Type " + type + " not found!");
											else if (!_rooms.get(type).containsKey(roomId))
												_log.log(Level.WARNING, "Room " + roomId + " in Type " + type + " not found!");
											
											for (int i = 0; i < count; i++)
											{
												DimensionalRiftRoom riftRoom = _rooms.get(type).get(roomId);
												x = riftRoom.getRandomX();
												y = riftRoom.getRandomY();
												z = riftRoom.getTeleportCoords()[2];
												
												if (template != null && _rooms.containsKey(type) && _rooms.get(type).containsKey(roomId))
												{
													spawnDat = new L2Spawn(template);
													spawnDat.setLocx(x);
													spawnDat.setLocy(y);
													spawnDat.setLocz(z);
													spawnDat.setHeading(-1);
													spawnDat.setRespawnDelay(delay);
													SpawnTable.getInstance().addNewSpawn(spawnDat, false);
													_rooms.get(type).get(roomId).getSpawns().add(spawnDat);
													countGood++;
												}
												else
												{
													countBad++;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Error on loading dimensional rift spawns: " + e);
		}
		_log.info("DimensionalRiftManager: Loaded " + countGood + " dimensional rift spawns, " + countBad + " errors.");
	}
	
	public void reload()
	{
		for (byte b : _rooms.keys())
		{
			for (byte i : _rooms.get(b).keys())
				_rooms.get(b).get(i).getSpawns().clear();
			
			_rooms.get(b).clear();
		}
		_rooms.clear();
		loadRooms();
		loadSpawns();
	}
	
	public boolean checkIfInRiftZone(int x, int y, int z, boolean ignorePeaceZone)
	{
		if (ignorePeaceZone)
			return _rooms.get((byte) 0).get((byte) 1).checkIfInZone(x, y, z);
		
		return _rooms.get((byte) 0).get((byte) 1).checkIfInZone(x, y, z) && !_rooms.get((byte) 0).get((byte) 0).checkIfInZone(x, y, z);
	}
	
	public boolean checkIfInPeaceZone(int x, int y, int z)
	{
		return _rooms.get((byte) 0).get((byte) 0).checkIfInZone(x, y, z);
	}
	
	public void teleportToWaitingRoom(L2PcInstance player)
	{
		int[] coords = getRoom((byte) 0, (byte) 0).getTeleportCoords();
		player.teleToLocation(coords[0], coords[1], coords[2]);
	}
	
	public synchronized void start(L2PcInstance player, byte type, L2Npc npc)
	{
		final L2Party party = player.getParty();
		
		// No party.
		if (party == null)
		{
			showHtmlFile(player, "data/html/seven_signs/rift/NoParty.htm", npc);
			return;
		}
		
		// Player isn't the party leader.
		if (!party.isLeader(player))
		{
			showHtmlFile(player, "data/html/seven_signs/rift/NotPartyLeader.htm", npc);
			return;
		}
		
		// Party is already in rift.
		if (party.isInDimensionalRift())
		{
			handleCheat(player, npc);
			return;
		}
		
		// Party members' count is lower than config.
		if (party.getMemberCount() < Config.RIFT_MIN_PARTY_SIZE)
		{
			NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
			html.setFile("data/html/seven_signs/rift/SmallParty.htm");
			html.replace("%npc_name%", npc.getName());
			html.replace("%count%", Integer.toString(Config.RIFT_MIN_PARTY_SIZE));
			player.sendPacket(html);
			return;
		}
		
		// Rift is full.
		if (!isAllowedEnter(type))
		{
			player.sendMessage("Rift is full. Try later.");
			return;
		}
		
		L2ItemInstance i;
		final int count = getNeededItems(type);
		
		// One of teammates isn't on peace zone or hasn't required amount of items.
		for (L2PcInstance p : party.getPartyMembers())
		{
			if (!checkIfInPeaceZone(p.getX(), p.getY(), p.getZ()))
			{
				showHtmlFile(player, "data/html/seven_signs/rift/NotInWaitingRoom.htm", npc);
				return;
			}
			
			i = p.getInventory().getItemByItemId(DIMENSIONAL_FRAGMENT_ITEM_ID);
			if (i == null || !p.destroyItem("RiftEntrance", i, count, null, true))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
				html.setFile("data/html/seven_signs/rift/NoFragments.htm");
				html.replace("%npc_name%", npc.getName());
				html.replace("%count%", Integer.toString(count));
				player.sendPacket(html);
				return;
			}
		}
		
		byte room;
		FastList<Byte> emptyRooms;
		do
		{
			emptyRooms = getFreeRooms(type);
			room = emptyRooms.get(Rnd.get(1, emptyRooms.size()) - 1);
			
			// Relaunch random number until another room than room boss popups.
			while (room == 9)
			{
				room = emptyRooms.get(Rnd.get(1, emptyRooms.size()) - 1);
			}
		}
		// Find empty room
		while (_rooms.get(type).get(room).ispartyInside());
		
		// Creates an instance of the rift.
		new DimensionalRift(party, type, room);
	}
	
	public void killRift(DimensionalRift d)
	{
		if (d.getTeleportTimerTask() != null)
			d.getTeleportTimerTask().cancel();
		d.setTeleportTimerTask(null);
		
		if (d.getTeleportTimer() != null)
			d.getTeleportTimer().cancel();
		d.setTeleportTimer(null);
		
		if (d.getSpawnTimerTask() != null)
			d.getSpawnTimerTask().cancel();
		d.setSpawnTimerTask(null);
		
		if (d.getSpawnTimer() != null)
			d.getSpawnTimer().cancel();
		d.setSpawnTimer(null);
	}
	
	public static class DimensionalRiftRoom
	{
		protected final byte _type;
		protected final byte _room;
		private final int _xMin;
		private final int _xMax;
		private final int _yMin;
		private final int _yMax;
		private final int _zMin;
		private final int _zMax;
		private final int[] _teleportCoords;
		private final Shape _s;
		private final boolean _isBossRoom;
		private final FastList<L2Spawn> _roomSpawns;
		protected final FastList<L2Npc> _roomMobs;
		private boolean _partyInside = false;
		
		public DimensionalRiftRoom(byte type, byte room, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, int xT, int yT, int zT, boolean isBossRoom)
		{
			_type = type;
			_room = room;
			_xMin = (xMin + 128);
			_xMax = (xMax - 128);
			_yMin = (yMin + 128);
			_yMax = (yMax - 128);
			_zMin = zMin;
			_zMax = zMax;
			_teleportCoords = new int[]
			{
				xT,
				yT,
				zT
			};
			_isBossRoom = isBossRoom;
			_roomSpawns = new FastList<>();
			_roomMobs = new FastList<>();
			_s = new Polygon(new int[]
			{
				xMin,
				xMax,
				xMax,
				xMin
			}, new int[]
			{
				yMin,
				yMin,
				yMax,
				yMax
			}, 4);
		}
		
		public int getRandomX()
		{
			return Rnd.get(_xMin, _xMax);
		}
		
		public int getRandomY()
		{
			return Rnd.get(_yMin, _yMax);
		}
		
		public int[] getTeleportCoords()
		{
			return _teleportCoords;
		}
		
		public boolean checkIfInZone(int x, int y, int z)
		{
			return _s.contains(x, y) && z >= _zMin && z <= _zMax;
		}
		
		public boolean isBossRoom()
		{
			return _isBossRoom;
		}
		
		public FastList<L2Spawn> getSpawns()
		{
			return _roomSpawns;
		}
		
		public void spawn()
		{
			for (L2Spawn spawn : _roomSpawns)
			{
				spawn.doSpawn();
				spawn.startRespawn();
			}
		}
		
		public DimensionalRiftRoom unspawn()
		{
			for (L2Spawn spawn : _roomSpawns)
			{
				spawn.stopRespawn();
				if (spawn.getLastSpawn() != null)
					spawn.getLastSpawn().deleteMe();
			}
			return this;
		}
		
		/**
		 * @return the _partyInside
		 */
		public boolean ispartyInside()
		{
			return _partyInside;
		}
		
		public void setPartyInside(boolean partyInside)
		{
			_partyInside = partyInside;
		}
	}
	
	private static int getNeededItems(byte type)
	{
		switch (type)
		{
			case 1:
				return Config.RIFT_ENTER_COST_RECRUIT;
			case 2:
				return Config.RIFT_ENTER_COST_SOLDIER;
			case 3:
				return Config.RIFT_ENTER_COST_OFFICER;
			case 4:
				return Config.RIFT_ENTER_COST_CAPTAIN;
			case 5:
				return Config.RIFT_ENTER_COST_COMMANDER;
			case 6:
				return Config.RIFT_ENTER_COST_HERO;
			default:
				throw new IndexOutOfBoundsException();
		}
	}
	
	public void showHtmlFile(L2PcInstance player, String file, L2Npc npc)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile(file);
		html.replace("%npc_name%", npc.getName());
		player.sendPacket(html);
	}
	
	public void handleCheat(L2PcInstance player, L2Npc npc)
	{
		showHtmlFile(player, "data/html/seven_signs/rift/Cheater.htm", npc);
		if (!player.isGM())
		{
			_log.log(Level.WARNING, "Player " + player.getName() + "(" + player.getObjectId() + ") was cheating in dimension rift area!");
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " tried to cheat in dimensional rift.", Config.DEFAULT_PUNISH);
		}
	}
	
	public boolean isAllowedEnter(byte type)
	{
		int count = 0;
		for (Object room : _rooms.get(type).values())
		{
			if (((DimensionalRiftRoom) room).ispartyInside())
				count++;
		}
		return (count < (_rooms.get(type).size() - 1));
	}
	
	public FastList<Byte> getFreeRooms(byte type)
	{
		FastList<Byte> list = new FastList<>();
		for (Object room : _rooms.get(type).values())
		{
			if (!((DimensionalRiftRoom) room).ispartyInside())
				list.add(((DimensionalRiftRoom) room)._room);
		}
		return list;
	}
	
	private static class SingletonHolder
	{
		protected static final DimensionalRiftManager _instance = new DimensionalRiftManager();
	}
}