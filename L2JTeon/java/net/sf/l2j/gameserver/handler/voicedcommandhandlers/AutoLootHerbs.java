/*
 * This program is free software; you can redistribute it and/or modify
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
 * VoicedCommand ".autoherbs" Handler Allow player to select: use autoloot of
 * Herbs or not
 * 
 * Syntax : .autoherbs_on/off Author : Sergey V Chursin (L2JOneo, Oneo Dev Team)
 * WWW : www.oneodevteam.com Email : schursin@gmail.com
 */
public class AutoLootHerbs implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = { "autoherbs_on", "autoherbs_off" };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
	// is command enabled?
	if (!Config.ALLOW_AUTOHERBS_CMD)
	    return false;
	// check command syntax and do work
	if (command.startsWith("autoherbs_on"))
	{
	    activeChar.setAutoLootHerbs(1);
	} else if (command.startsWith("autoherbs_off"))
	{ // auto loot off
	    activeChar.setAutoLootHerbs(0);
	} else
	{ // show cmd syntax
	    activeChar.sendMessage("AutoHerbs Syntax:");
	    activeChar.sendMessage("  Enable auto loot herbs: .autoherbs_on");
	    activeChar.sendMessage("  Disable auto loot herbs: .autoherbs_off");
	}
	// work's done - exit
	return true;
    }

    public String[] getVoicedCommandList()
    {
	return VOICED_COMMANDS;
    }
}
