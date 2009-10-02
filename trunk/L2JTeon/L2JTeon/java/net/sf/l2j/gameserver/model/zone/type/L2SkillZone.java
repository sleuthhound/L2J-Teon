// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 17/04/2009 1:19:57
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   L2SkillZone.java

package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;

public class L2SkillZone extends L2ZoneType
{

    public L2SkillZone(int id)
    {
        super(id);
    }

    public void setParameter(String name, String value)
    {
        if(name.equals("skillId"))
            _skillId = Integer.parseInt(value);
        else
        if(name.equals("skillLvl"))
            _skillLvl = Integer.parseInt(value);
        else
        if(name.equals("onSiege"))
            _onSiege = Boolean.parseBoolean(value);
        else
            super.setParameter(name, value);
    }

    protected void onEnter(L2Character character)
    {
        if(((character instanceof L2PcInstance) || (character instanceof L2SummonInstance)) && (!_onSiege || _onSiege && character.isInsideZone(4)))
        {
            if(character instanceof L2PcInstance)
                ((L2PcInstance)character).enterDangerArea();
            SkillTable.getInstance().getInfo(_skillId, _skillLvl).getEffects(character, character);
        }
    }

    protected void onExit(L2Character character)
    {
        if((character instanceof L2PcInstance) || (character instanceof L2SummonInstance))
        {
            character.stopSkillEffects(_skillId);
            if(character instanceof L2PcInstance)
                ((L2PcInstance)character).exitDangerArea();
        }
    }

    protected void onDieInside(L2Character character)
    {
        onExit(character);
    }

    protected void onReviveInside(L2Character character)
    {
        onEnter(character);
    }

    private int _skillId;
    private int _skillLvl;
    private boolean _onSiege;
}