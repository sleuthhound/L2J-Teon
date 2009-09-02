package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.skills.Env;

public class EffectMute extends L2Effect
{
    public EffectMute(Env env, EffectTemplate template)
    {
	super(env, template);
    }

    @Override
    public EffectType getEffectType()
    {
	return L2Effect.EffectType.MUTE;
    }

    @Override
    public void onStart()
    {
	getEffected().startMuted();
    }

    @Override
    public boolean onActionTime()
    {
	// Simply stop the effect
	getEffected().stopMuted(this);
	return false;
    }

    @Override
    public void onExit()
    {
	getEffected().stopMuted(this);
    }
}
