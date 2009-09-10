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
package net.sf.l2j.gameserver.model.entity.L2JTeonEvents;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class TvTEventTeleporter implements Runnable
{
    /** The instance of the player to teleport */
    private final L2PcInstance _playerInstance;
    /** Coordinates of the spot to teleport to */
    private int[] _coordinates = new int[3];

    /**
     * Initialize the teleporter and start the delayed task
     * 
     * @param playerInstance
     * @param coordinates
     * @param reAdd
     */
    public TvTEventTeleporter(L2PcInstance playerInstance, int[] coordinates, boolean reAdd)
    {
	_playerInstance = playerInstance;
	_coordinates = coordinates;
	// in config as seconds
	long delay = (TvTEvent.isStarted() ? Config.TVT_EVENT_RESPAWN_TELEPORT_DELAY : Config.TVT_EVENT_START_LEAVE_TELEPORT_DELAY) * 1000;
	if (reAdd)
	{
	    delay = 0;
	}
	ThreadPoolManager.getInstance().scheduleGeneral(this, delay);
    }

    /**
     * The task method to teleport the player<br>
     * 1. Unsummon pet if there is one 2. Remove all effects 3. Revive and
     * full heal the player 4. Teleport the player 5. Broadcast status and
     * user info
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
	if (_playerInstance == null)
	{
	    return;
	}
	L2Summon summon = _playerInstance.getPet();
	if (summon != null)
	{
	    summon.unSummon(_playerInstance);
	}
	for (L2Effect effect : _playerInstance.getAllEffects())
	{
	    if (effect != null)
	    {
		effect.exit();
	    }
	}
	_playerInstance.doRevive();
	_playerInstance.setCurrentCp(_playerInstance.getMaxCp());
	_playerInstance.setCurrentHp(_playerInstance.getMaxHp());
	_playerInstance.setCurrentMp(_playerInstance.getMaxMp());
	_playerInstance.teleToLocation(_coordinates[0], _coordinates[1], _coordinates[2], false);
	if (TvTEvent.isStarted())
	{
	    _playerInstance.setTeam(TvTEvent.getParticipantTeamId(_playerInstance.getName()) + 1);
	} else
	{
	    _playerInstance.setTeam(0);
	}
	_playerInstance.broadcastStatusUpdate();
	_playerInstance.broadcastUserInfo();
    }
}
