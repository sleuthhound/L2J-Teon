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
package net.sf.l2j.gameserver.taskmanager.tasks;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.taskmanager.Task;
import net.sf.l2j.gameserver.taskmanager.TaskManager;
import net.sf.l2j.gameserver.taskmanager.TaskTypes;
import net.sf.l2j.gameserver.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Noctarius
 */
public class TaskLoginRestart extends Task
{
	private static final Logger _log = Logger.getLogger(TaskRecom.class.getName());
	private static final String NAME = "LoginRestart";

	/**
	 * @see net.sf.l2j.gameserver.taskmanager.Task#getName()
	 */
	@Override
	public String getName()
	{
		return NAME;
	}

	/**
	 * @see net.sf.l2j.gameserver.taskmanager.Task#onTimeElapsed(net.sf.l2j.gameserver.taskmanager.TaskManager.ExecutedTask)
	 */
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		if (!Config.LOGIN_RESTART_BY_TIME)
			return;
		LoginServerThread.getInstance().sendLoginRestart();
		_log.config("LoginRestart Global Task: launched.");
	}

	@Override
	public void initializate()
	{
		int restartTime = Config.LOGIN_RESTART_TIME * 60000;
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_FIXED_SHEDULED, String.valueOf(restartTime), String.valueOf(restartTime), "");
	}
}
