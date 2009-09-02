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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Author Rayan Reworked by DaRkRaGe
 * 
 */
public class AdminDonator implements IAdminCommandHandler
{
    private static String[] _adminCommands = { "admin_setdonator", };
    private final static Log _log = LogFactory.getLog(AdminDonator.class.getName());
    private static final int REQUIRED_LEVEL = Config.GM_MENU;

    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
	if (!Config.ALT_PRIVILEGES_ADMIN)
	{
	    if (!(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM()))
	    {
		return false;
	    }
	}
	if (command.startsWith("admin_setdonator"))
	{
	    L2Object target = activeChar.getTarget();
	    L2PcInstance player = null;
	    SystemMessage sm = new SystemMessage(SystemMessageId.S1_S2);
	    if (target instanceof L2PcInstance)
	    {
		player = (L2PcInstance) target;
	    } else
	    {
		player = activeChar;
	    }
	    if (player.isDonator())
	    {
		player.setDonator(false);
		sm.addString("You are no longer a server donator.");
		Connection connection = null;
		try
		{
		    connection = L2DatabaseFactory.getInstance().getConnection();
		    PreparedStatement statement = connection.prepareStatement("SELECT obj_id FROM characters where char_name=?");
		    statement.setString(1, target.getName());
		    ResultSet rset = statement.executeQuery();
		    int objId = 0;
		    if (rset.next())
		    {
			objId = rset.getInt(1);
		    }
		    rset.close();
		    statement.close();
		    if (objId == 0)
		    {
			connection.close();
			return false;
		    }
		    statement = connection.prepareStatement("UPDATE characters SET donator=0 WHERE obj_id=?");
		    statement.setInt(1, objId);
		    statement.execute();
		    statement.close();
		    connection.close();
		} catch (Exception e)
		{
		    _log.warn("could not set donator stats of char:", e);
		} finally
		{
		    try
		    {
			connection.close();
		    } catch (Exception e)
		    {
		    }
		}
	    } else
	    {
		player.setDonator(true);
		sm.addString("You are now a server donator, congratulations!");
		Connection connection = null;
		try
		{
		    connection = L2DatabaseFactory.getInstance().getConnection();
		    PreparedStatement statement = connection.prepareStatement("SELECT obj_id FROM characters where char_name=?");
		    statement.setString(1, target.getName());
		    ResultSet rset = statement.executeQuery();
		    int objId = 0;
		    if (rset.next())
		    {
			objId = rset.getInt(1);
		    }
		    rset.close();
		    statement.close();
		    if (objId == 0)
		    {
			connection.close();
			return false;
		    }
		    statement = connection.prepareStatement("UPDATE characters SET donator=1 WHERE obj_id=?");
		    statement.setInt(1, objId);
		    statement.execute();
		    statement.close();
		    connection.close();
		} catch (Exception e)
		{
		    _log.warn("could not set donator stats of char:", e);
		} finally
		{
		    try
		    {
			connection.close();
		    } catch (Exception e)
		    {
		    }
		}
	    }
	    player.sendPacket(sm);
	    player.broadcastUserInfo();
	    if (player.isDonator() == true)
	    {
		Announcements.getInstance().announceToAll(player.getName() + " Has Become a Server Donator!");
	    }
	}
	return false;
    }

    public String[] getAdminCommandList()
    {
	return _adminCommands;
    }

    private boolean checkLevel(int level)
    {
	return level >= REQUIRED_LEVEL;
    }
}