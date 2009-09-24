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

import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.clanhallsiege.FortResistSiegeManager;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * @author Maxi
 */
public class AdminFortResist implements IAdminCommandHandler
{
    private static final String[] ADMIN_COMMANDS =
    {
        "admin_startfortresist", "admin_endfortresist"
    };
    private static final int REQUIRED_LEVEL = Config.GM_FORTSIEGE;

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#useAdminCommand(java.lang.String, net.sf.l2j.gameserver.model.L2PcInstance)
     */
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
	if (!Config.ALT_PRIVILEGES_ADMIN)
	    if (!(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM()))
		return false;
        if (command.startsWith("admin_startfortresist"))
        {
			if (activeChar instanceof L2PcInstance)
		FortResistSiegeManager.getInstance().startSiege();
                SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
                sm.addString("Start Siege Fortress of Resistence");
                activeChar.sendPacket(sm);
                return false;
        } else if (command.startsWith("admin_endfortresist"))
        {
			if (activeChar instanceof L2PcInstance)
		FortResistSiegeManager.getInstance().endSiege(true);
                SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
                sm.addString("End Siege Fortress of Resistence");
                activeChar.sendPacket(sm);
                return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#getAdminCommandList()
     */
    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }

    private boolean checkLevel(int level)
    {
	return level >= REQUIRED_LEVEL;
    }
}

