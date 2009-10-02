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
package net.sf.l2j.gameserver.skills.effects;

import java.util.logging.Logger;

import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SpawnTable;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.L2WorldRegion;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.zone.form.ZoneCylinder;
import net.sf.l2j.gameserver.model.zone.type.L2SignetZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillMagicOnGround;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Point3D;


/**
 * @author Forsaiken
 */

public class EffectSignet extends L2Effect
{
	static Logger _log = Logger.getLogger(EffectSignet.class.getName());

	private L2Spawn _spawn;

	protected L2SignetZone zone;

	public EffectSignet(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		int x = getEffected().getX();
		int y = getEffected().getY();
		int z = getEffected().getZ();

		if (getEffected() instanceof L2PcInstance
				&& getSkill().getTargetType() == SkillTargetType.TARGET_SIGNET_GROUND)
		{
			Point3D wordPosition = ((L2PcInstance) getEffected())
					.getCurrentSkillWorldPosition();

			if (wordPosition != null)
			{
				x = wordPosition.getX();
				y = wordPosition.getY();
				z = wordPosition.getZ();
			}
		}

		L2NpcTemplate template = NpcTable.getInstance().getTemplate(
				((L2SkillMagicOnGround) getSkill()).effectNpcId);
		if (template != null)
		{
			try
			{
				_spawn = new L2Spawn(template);
				_spawn.setLocx(x);
				_spawn.setLocy(y);
				_spawn.setLocz(z);
				_spawn.setAmount(1);
				_spawn.setHeading(getEffector().getHeading());
				_spawn.setRespawnDelay(0);
				SpawnTable.getInstance().addNewSpawn(_spawn, false);
				_spawn.init();
				_spawn.stopRespawn();
			} catch (Throwable e)
			{
				e.printStackTrace();
			}
		}

		L2WorldRegion region = getEffected().getWorldRegion();

		L2Skill skill = SkillTable.getInstance()
				.getInfo(((L2SkillMagicOnGround) getSkill()).triggerEffectId,
						getLevel());

		if (skill == null)
		{
			_log.warning("EffectSignet: Could not get the tigger effect "
					+ ((L2SkillMagicOnGround) getSkill()).triggerEffectId);
			onExit();
			return;
		}

		zone = new L2SignetZone(region, getEffected(), !getSkill()
				.isOffensive(), getSkill().getId(), skill);

		zone.setZone(new ZoneCylinder(x, y, z - 200, z + 200, getSkill()
				.getSkillRadius()));

		region.addZone(zone);

		for (L2Character c : getEffected().getKnownList().getKnownCharacters())
			zone.revalidateInZone(c);

		zone.revalidateInZone(getEffected());
	}

	@Override
	public void onExit()
	{
		if (_spawn != null)
		{
			_spawn.getLastSpawn().deleteMe();
			SpawnTable.getInstance().deleteSpawn(_spawn, false);
		}

		if (zone != null)
			zone.remove();
	}

	@Override
	public EffectType getEffectType()
	{
		return EffectType.SIGNET;
	}

	@Override
	public boolean onActionTime()
	{
		int mpConsume = getSkill().getMpConsume();

		if (mpConsume > getEffected().getCurrentMp())
		{
			getEffected()
					.sendPacket(
							new SystemMessage(
									SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
			return false;
		} else
			getEffected().reduceCurrentMp(mpConsume);

		return true;
	}
}