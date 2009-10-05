package net.sf.l2j.gameserver.handler.skillhandlers;

import java.util.Map;
import javolution.util.FastMap;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class ClanGate implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS = {SkillType.CLAN_GATE};

	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!(activeChar instanceof L2PcInstance))
			return;
		L2PcInstance player = ((L2PcInstance)activeChar);
		player.getClan().giveMeOnlineMembers(player);
	}

	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}