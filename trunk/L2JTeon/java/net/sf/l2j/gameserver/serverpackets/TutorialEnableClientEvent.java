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
package net.sf.l2j.gameserver.serverpackets;

public class TutorialEnableClientEvent extends L2GameServerPacket
{
	private int _event = 0;

	public TutorialEnableClientEvent(int event)
	{
		_event = event;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xA2);
		writeD(_event);
	}

	@Override
	public String getType()
	{
		return "[S] A2 TutorialEnableClientEvent";
	}
}
