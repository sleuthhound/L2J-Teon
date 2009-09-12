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
package net.sf.l2j.gameserver.network;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.clientpackets.*;
import net.sf.l2j.gameserver.network.L2GameClient.ClientState;
import net.sf.l2j.util.Util;

import com.l2jserver.mmocore.network.IClientFactory;
import com.l2jserver.mmocore.network.IMMOExecutor;
import com.l2jserver.mmocore.network.IPacketHandler;
import com.l2jserver.mmocore.network.MMOConnection;
import com.l2jserver.mmocore.network.ReceivablePacket;

/**
 * Stateful Packet Handler<BR>
 * The Stateful approach prevents the server from handling inconsistent packets, examples:<BR>
 * <li>Clients sends a MoveToLocation packet without having a character attached. (Potential errors handling the packet).</li>
 * <li>Clients sends a RequestAuthLogin being already authed. (Potential exploit).</li>
 * <BR><BR>
 * Note: If for a given exception a packet needs to be handled on more then one state, then it should be added to all these states.
 * @author  KenM
 */
public final class L2GamePacketHandler implements IPacketHandler<L2GameClient>, IClientFactory<L2GameClient>, IMMOExecutor<L2GameClient>
{
	private static final Logger _log = Logger.getLogger(L2GamePacketHandler.class.getName());

	// implementation
	public ReceivablePacket<L2GameClient> handlePacket(ByteBuffer buf, L2GameClient client)
	{
		int opcode = buf.get() & 0xFF;

		ReceivablePacket<L2GameClient> msg = null;
		
		ClientState state = client.getState();

		if(opcode == 0xd0)
		{
        	int id2 = -1;
        	if (buf.remaining() >= 2)
        		id2 = buf.getShort() & 0xffff;
        	else
        	{
        		_log.warning("Client: "+client.toString()+" sent a 0xd0 without the second opcode.");
        		return msg;
        	}
            return getPacket2xOP(opcode, id2, state, buf, client);
		}
		return getPacket(state, opcode, buf ,client);
	}

	public L2GameClientPacket getPacket(ClientState state,int opcode, ByteBuffer buf, L2GameClient client)
	{
		L2GameClientPacket packet = null;
		Map<Integer, L2GameClientPacket> packets = TeonPacketHandler.getInstance().getPackets().get(state);

		if(packets != null)
			packet = packets.get(opcode);
		else
			printDebug(opcode, buf, state, client);

		return packet;
	}

	public L2GameClientPacket getPacket2xOP(int opcode,int id2, ClientState state, ByteBuffer buf, L2GameClient client)
	{
		L2GameClientPacket packet = null;
		Map<Integer, L2GameClientPacket> packets = TeonPacketHandler.getInstance().get2xOpPackets().get(opcode);

		if(packets != null)
			packet = packets.get(id2);
		else
			printDebugDoubleOpcode(opcode, id2, buf, state, client);

		return packet;
	}

	private void printDebug(int opcode, ByteBuffer buf, ClientState state, L2GameClient client)
	{
		int size = buf.remaining();
     	_log.warning("Unknown Packet: "+Integer.toHexString(opcode)+" on State: "+state.name()+" Client: "+client.toString());
     	byte[] array = new byte[size];
     	buf.get(array);
     	_log.warning(Util.printData(array, size));
	}

	private void printDebugDoubleOpcode(int opcode, int id2, ByteBuffer buf, ClientState state, L2GameClient client)
	{
		int size = buf.remaining();
     	_log.warning("Unknown Packet: "+Integer.toHexString(opcode)+":" + Integer.toHexString(id2)+" on State: "+state.name()+" Client: "+client.toString());
     	byte[] array = new byte[size];
     	buf.get(array);
     	_log.warning(Util.printData(array, size));
	}

	// impl
	public L2GameClient create(MMOConnection<L2GameClient> con)
	{
		return new L2GameClient(con);
	}

	public void execute(ReceivablePacket<L2GameClient> rp)
	{
		try
		{
			if (rp.getClient().getState() == ClientState.IN_GAME)
			{
				ThreadPoolManager.getInstance().executePacket(rp);
			}
			else
			{
				ThreadPoolManager.getInstance().executeIOPacket(rp);
			}
		}
		catch (RejectedExecutionException e)
		{
			// if the server is shutdown we ignore
			if (!ThreadPoolManager.getInstance().isShutdown())
			{
				_log.severe("Failed executing: "+rp.getClass().getSimpleName()+" for Client: "+rp.getClient().toString());
			}
		}
	}
}
