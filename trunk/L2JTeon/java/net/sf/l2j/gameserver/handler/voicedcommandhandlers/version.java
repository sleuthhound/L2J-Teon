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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class allows a player to type in the command .joinvip and join the vip
 * event.
 * 
 * 
 * @author DaRkRaGe [L2JOneo]
 */
public class version implements IVoicedCommandHandler
{
    private static String[] _voicedCommands = { "version" };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
	if (command.equalsIgnoreCase("version"))
	{
	    activeChar.sendMessage("L2JOneo Rev " + Config.SERVER_VERSION + " http://www.oneodevteam.com");
	}
	return true;
    }

    public String[] getVoicedCommandList()
    {
	return _voicedCommands;
    }
}