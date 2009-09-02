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
 * VoicedCommand Handler Allows clan leaders the power allow clan members
 * withdraw items from clan warehouse.
 * 
 * Syntax : .cwh_withdraw_on/off Author : Sergey V Chursin WWW : www.gludin.ru
 * Email : schursin@gmail.com
 */
public class PlayersWithdrawCWH implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = { "cwh_withdraw_on", "cwh_withdraw_off" };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String targetTxt)
    {
	if (!command.startsWith("cwh_withdraw_on") && !command.startsWith("cwh_withdraw_off"))
	{
	    return false;
	}
	if (!Config.ALLOW_WITHDRAW_CWH_CMD)
	{
	    activeChar.sendMessage(" оманда отключена администратором.");
	    return true;
	}
	if (!activeChar.isClanLeader())
	{
	    activeChar.sendMessage("¬ы не можете использовать эту команду! ¬ы не клан лидер.");
	    return true;
	}
	if (activeChar.getTarget() == null)
	{
	    activeChar.sendMessage("ѕеред использованием команды - выберите игрока.");
	    return true;
	}
	if (!(activeChar.getTarget() instanceof L2PcInstance))
	{
	    activeChar.sendMessage("ѕеред использованием команды - выберите игрока.");
	    return true;
	}
	L2PcInstance target = (L2PcInstance) activeChar.getTarget();
	if (activeChar.getObjectId() == target.getObjectId())
	{
	    activeChar.sendMessage("¬ы и так имеете полный доступ к клан-складу.");
	    return true;
	}
	if (activeChar.getClanId() != target.getClanId())
	{
	    activeChar.sendMessage("¬ыберите игрока из своего клана.");
	    return true;
	}
	if (command.startsWith("cwh_withdraw_on"))
	{
	    target.setCanWithdrawCWH(1);
	    activeChar.sendMessage(target.getName() + " получил полный доступ к клан-складу.");
	    target.sendMessage(" лан лидер разрешил вам полный доступ к клан-складу.");
	} else if (command.startsWith("cwh_withdraw_off"))
	{
	    target.setCanWithdrawCWH(0);
	    activeChar.sendMessage(target.getName() + " лишен полного доступа к клан-складу.");
	    target.sendMessage(" лан лидер лишил вас полного доступа к клан-складу.");
	}
	return true;
    }

    public String[] getVoicedCommandList()
    {
	return VOICED_COMMANDS;
    }
}
