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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.BanditStrongholdManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.DevastatedCastleManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortResistSiegeManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortressofTheDeadManager;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.WildBeastFarmManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Maxi
 */
public class AdminClanHallSieges implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS = { 
		"admin_startfortresist", 
		"admin_endfortresist", 
		"admin_startdevastated", 
		"admin_enddevastated", 
		"admin_startbandit", 
		"admin_endbandit", 
		"admin_startwildbeastfarm", 
		"admin_endwildbeastfarm", 
		"admin_startfortress", 
		"admin_endfortress" 
	};

	private static final int REQUIRED_LEVEL = Config.GM_FORTSIEGE;

	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (!Config.ALT_PRIVILEGES_ADMIN)
			if (!(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM()))
				return false;

		if (command.startsWith("admin_startfortresist"))
		{
			if (!FortResistSiegeManager.getInstance().getIsInProgress())
			{
				FortResistSiegeManager.getInstance().startSiege();
				activeChar.sendMessage("Start Siege Fortress of Resistence");
			}
		}
		else if (command.startsWith("admin_endfortresist"))
		{
			if (FortResistSiegeManager.getInstance().getIsInProgress())
			{
				FortResistSiegeManager.getInstance().endSiege(true);
				activeChar.sendMessage("End Siege Fortress of Resistence");
			}
		}
		else if (command.startsWith("admin_startdevastated"))
		{
			if (!DevastatedCastleManager.getInstance().getIsInProgress())
			{
				DevastatedCastleManager.getInstance().startSiege();
				activeChar.sendMessage("Start Siege Devastated Castle");
			}
		}
		else if (command.startsWith("admin_enddevastated"))
		{
			if (DevastatedCastleManager.getInstance().getIsInProgress())
			{
				DevastatedCastleManager.getInstance().endSiege(true);
				activeChar.sendMessage("End Siege Devastated Castle");
			}
		}
		else if (command.startsWith("admin_startbandit"))
		{
			if (!BanditStrongholdManager.getInstance().getIsInProgress())
			{
				BanditStrongholdManager.getInstance().startSiege();
				activeChar.sendMessage("Start Siege Bandit Stronghold Siege");
			}
		}
		else if (command.startsWith("admin_endbandit"))
		{
			if (BanditStrongholdManager.getInstance().getIsInProgress())
			{
				BanditStrongholdManager.getInstance().endSiege(true);
				activeChar.sendMessage("End Siege Bandit Stronghold Siege");
			}
		}
		else if (command.startsWith("admin_startwildbeastfarm"))
		{
			if (!WildBeastFarmManager.getInstance().getIsInProgress())
			{
				WildBeastFarmManager.getInstance().startSiege();
				activeChar.sendMessage("Start Siege Wild Beast Farm");
			}
		}
		else if (command.startsWith("admin_endwildbeastfarm"))
		{
			if (WildBeastFarmManager.getInstance().getIsInProgress())
			{
				WildBeastFarmManager.getInstance().endSiege(true);
				activeChar.sendMessage("End Siege Wild Beast Farm");
			}
		}
		else if (command.startsWith("admin_startfortress"))
		{
			if (!FortressofTheDeadManager.getInstance().getIsInProgress())
			{
				FortressofTheDeadManager.getInstance().startSiege();
				activeChar.sendMessage("Start Siege Fortress of The Dead");
			}
		}
		else if (command.startsWith("admin_endfortress"))
		{
			if (FortressofTheDeadManager.getInstance().getIsInProgress())
			{
				FortressofTheDeadManager.getInstance().endSiege(true);
				activeChar.sendMessage("End Siege Fortress of The Dead");
			}
		}
		return true;
	}

	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}

	private boolean checkLevel(int level)
	{
		return level >= REQUIRED_LEVEL;
	}
}
