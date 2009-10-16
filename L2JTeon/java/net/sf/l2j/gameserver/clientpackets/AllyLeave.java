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
package net.sf.l2j.gameserver.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

public final class AllyLeave extends L2GameClientPacket
{
	private static final String _C__84_ALLYLEAVE = "[C] 84 AllyLeave";

	// private static Logger _log =
	// Logger.getLogger(AllyLeave.class.getName());
	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (player.getClan() == null)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER));
			return;
		}
		if (!player.isClanLeader())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.ONLY_CLAN_LEADER_WITHDRAW_ALLY));
			return;
		}
		L2Clan clan = player.getClan();
		if (clan.getAllyId() == 0)
		{
			player.sendPacket(new SystemMessage(SystemMessageId.NO_CURRENT_ALLIANCES));
			return;
		}
		if (clan.getClanId() == clan.getAllyId())
		{
			player.sendPacket(new SystemMessage(SystemMessageId.ALLIANCE_LEADER_CANT_WITHDRAW));
			return;
		}
		long currentTime = System.currentTimeMillis();
		clan.setAllyId(0);
		clan.setAllyName(null);
		clan.setAllyPenaltyExpiryTime(currentTime + Config.ALT_ALLY_JOIN_DAYS_WHEN_LEAVED * 86400000L, L2Clan.PENALTY_TYPE_CLAN_LEAVED); // 24*60*60*1000
		// =
		// 86400000
		clan.updateClanInDB();
		player.sendPacket(new SystemMessage(SystemMessageId.YOU_HAVE_WITHDRAWN_FROM_ALLIANCE));
	}

	@Override
	public String getType()
	{
		return _C__84_ALLYLEAVE;
	}
}