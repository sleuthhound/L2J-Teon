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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.base.Race;
import net.sf.l2j.gameserver.model.entity.L2JTeonEvents.VIP;

/**
 * This class allows a player to type in the command .joinvip and join the vip event.
 * 
 * @author Ahmed
 */
public class JoinVIP implements IVoicedCommandHandler
{
	private static String[] VOICED_COMMANDS = { "vip", "joinvip", "leavevip" };

	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("vip"))
		{
			activeChar.sendMessage("Type \".joinvip\" to join the VIP Event, and \".leavevip\" to leave the event.");
		}
		else if (command.equalsIgnoreCase("leavevip"))
		{
			if (VIP._joining)
			{
				if (activeChar._inEventVIP)
				{
					VIP.removePlayerVIP(activeChar);
					activeChar.sendMessage("You have removed yourself from the VIP event.");
				}
				else
				{
					activeChar.sendMessage("You are not signed up for the VIP event.");
				}
			}
		}
		else if (command.equalsIgnoreCase("joinvip"))
		{
			if (VIP._joining)
			{
				if (activeChar._inEventVIP)
				{
					activeChar.sendMessage("You are already signed up.");
				}
				else if ((activeChar.getRace() == Race.human) && (VIP._team == 1))
				{
					VIP.addPlayerVIP(activeChar);
					activeChar.sendMessage("You have signed up for VIP Event, and your race has the VIP.... When the participation period ends and the event starts you must PROTECT HIM!");
				}
				else if ((activeChar.getRace() == Race.elf) && (VIP._team == 2))
				{
					VIP.addPlayerVIP(activeChar);
					activeChar.sendMessage("You have signed up for VIP Event, and your race has the VIP.... When the participation period ends and the event starts you must PROTECT HIM!");
				}
				else if ((activeChar.getRace() == Race.darkelf) && (VIP._team == 3))
				{
					VIP.addPlayerVIP(activeChar);
					activeChar.sendMessage("You have signed up for VIP Event, and your race has the VIP.... When the participation period ends and the event starts you must PROTECT HIM!");
				}
				else if ((activeChar.getRace() == Race.orc) && (VIP._team == 4))
				{
					VIP.addPlayerVIP(activeChar);
					activeChar.sendMessage("You have signed up for VIP Event, and your race has the VIP.... When the participation period ends and the event starts you must PROTECT HIM!");
				}
				else if ((activeChar.getRace() == Race.dwarf) && (VIP._team == 5))
				{
					VIP.addPlayerVIP(activeChar);
					activeChar.sendMessage("You have signed up for VIP Event, and your race has the VIP.... When the participation period ends and the event starts you must PROTECT HIM!");
				}
				else
				{
					VIP.addPlayerNotVIP(activeChar);
					activeChar.sendMessage("You have signed up for VIP Event, and your race does not have the VIP...When the participation period ends and the event starts you must find and KILL the VIP Character!");
				}
			}
			else
			{
				activeChar.sendMessage("The VIP Event joining period has not started, please wait.");
			}
		}
		return true;
	}

	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}