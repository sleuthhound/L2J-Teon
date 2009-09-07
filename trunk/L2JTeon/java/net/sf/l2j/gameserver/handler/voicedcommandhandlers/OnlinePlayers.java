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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;


import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;

/**
 * 
 *
 * 
 * this class...
 * shows the amount of online players to anyone who calls it.
 */
public class OnlinePlayers implements IVoicedCommandHandler
{

	private static final String[] VOICED_COMMANDS = {"online"};

	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.startsWith("online"))
		{
		showPlayers(activeChar, target);		
		}
		
		return true;

	}



	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}


    public void showPlayers (L2PcInstance player, String target)
	{
 
		{
			
			SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
			sm = new SystemMessage(SystemMessageId.S1_S2);
			sm.addString("======<Players Online>======");
			player.sendPacket(sm);
           	sm = new SystemMessage(SystemMessageId.S1_S2);	
           	sm.addString("players online!");
           	sm.addNumber(L2World.getInstance().getAllPlayers().size());
           	player.sendPacket(sm);
			sm = new SystemMessage(SystemMessageId.S1_S2);
			sm.addString("=======================");
			player.sendPacket(sm);
			
		}
	}



}