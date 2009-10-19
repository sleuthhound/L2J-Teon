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
package net.sf.l2j.gameserver.handler;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.handler.skillhandlers.*;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;

public class SkillHandler
{
	private static Logger _log = Logger.getLogger(SkillHandler.class.getName());

	private Map<L2Skill.SkillType, ISkillHandler> _datatable;

	public static SkillHandler getInstance()
	{
		return SingletonHolder._instance;
	}

	private SkillHandler()
	{
		_datatable = new TreeMap<L2Skill.SkillType, ISkillHandler>();
        	registerSkillHandler(new StrSiegeAssault());
        	registerSkillHandler(new SummonFriend());
        	registerSkillHandler(new SiegeFlag());
        	registerSkillHandler(new TakeCastle());
        	_log.config("SkillHandler: Loaded " + _datatable.size() + " handlers.");
	}

	public void registerSkillHandler(ISkillHandler handler)
	{
		SkillType[] types = handler.getSkillIds();
		for (SkillType t : types)
		{
			_datatable.put(t, handler);
		}
	}

	public ISkillHandler getSkillHandler(SkillType skillType)
	{
		return _datatable.get(skillType);
	}

	public int size()
	{
		return _datatable.size();
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final SkillHandler _instance = new SkillHandler();
	}
}
