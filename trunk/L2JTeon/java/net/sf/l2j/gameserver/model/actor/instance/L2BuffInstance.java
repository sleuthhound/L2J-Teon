package net.sf.l2j.gameserver.model.actor.instance;

import static net.sf.l2j.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import java.text.DateFormat;
import java.util.StringTokenizer;
import java.util.List;
import javolution.text.TextBuilder;
import java.util.*;
import javolution.util.FastList;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Multisell;
import net.sf.l2j.gameserver.model.actor.stat.NpcStat;
import net.sf.l2j.gameserver.model.actor.status.NpcStatus;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.model.quest.State;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.serverpackets.*;
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2ddt.engine.Npcbuffer;

public class L2BuffInstance extends L2NpcInstance
{

    public L2BuffInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }

    public void onAction(L2PcInstance client)
    {
        if(this != client.getTarget())
        {
            client.setTarget(this);
            client.sendPacket(new MyTargetSelected(getObjectId(), 0));
            client.sendPacket(new ValidateLocation(this));
        } else
        {
            client.sendPacket(new MyTargetSelected(getObjectId(), 0));
            client.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
            if(!isInsideRadius(client, 150, false, false))
            {
                client.sendPacket(new ActionFailed());
            } else
            {
                NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
                html.setFile((new StringBuilder("data/html/buffer-by-maxi/buffer/main.htm").toString()));
                html.replace("%objectId%", String.valueOf(getObjectId()));
                client.sendPacket(html);
            }
        }
    }

    public void onBypassFeedback(L2PcInstance client, String command)
    {
        boolean bFail = true;
        StringTokenizer st = new StringTokenizer(command, " ");
        String cmd = st.nextToken();
        if(cmd.equalsIgnoreCase("chat"))
        {
            showChatWnd(client, st.nextToken());
            bFail = false;
        }
        if(cmd.equalsIgnoreCase("main"))
        {
            showChatWnd(client, "-1");
            bFail = false;
        }
        if(cmd.equalsIgnoreCase("buff"))
        {
            Npcbuffer.getInstance().useBuff(this, client, st.nextToken(), st.nextToken());
            bFail = false;
        }
        if(cmd.equalsIgnoreCase("restore"))
        {
            Npcbuffer.getInstance().useRestore(this, client, st.nextToken(), st.nextToken());
            bFail = false;
        }
        if(cmd.equalsIgnoreCase("reload"))
        {
            Npcbuffer.getInstance().reload(client);
            bFail = false;
        }
        if(cmd.equalsIgnoreCase("multisell"))
        {
            L2Multisell.getInstance().SeparateAndSend(Integer.parseInt(st.nextToken()), client, false, 0.0D);
            bFail = false;
        }
        if(bFail)
            client.sendPacket(new ActionFailed());
    }

    public void onActionShift(L2GameClient game)
    {
        L2PcInstance client = game.getActiveChar();
        if(client.isGM())
        {
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            TextBuilder html1 = new TextBuilder("<html><body><center><font color=\"LEVEL\">Buffer Information</font></center>");
            html1.append("<br><br><br>");
            html1.append((new StringBuilder("<br1><a action=\"bypass -h npc_")).append(getObjectId()).append("_reload\">Reload buff.txt</a>").toString());
            html1.append((new StringBuilder("<br1><a action=\"bypass -h npc_")).append(getObjectId()).append("_storefavs\">Force to save fav.txt</a>").toString());
            html1.append("<br1><a action=\"bypass -h admin_kill\">Kill</a>");
            html1.append("<br1><a action=\"bypass -h admin_delete\">Delete</a>");
		html1.append("Respawn Time: " + (getSpawn() != null ? getSpawn().getRespawnDelay() / 1000 + "  Seconds<br>" : "?  Seconds<br>"));
	    html1.append("<table border=\"0\" width=\"100%\">");
	    html1.append("<tr><td>Object ID</td><td>" + getObjectId() + "</td><td>NPC ID</td><td>" + getTemplate().npcId + "</td></tr>");
	    html1.append("<tr><td>Castle</td><td>" + getCastle().getCastleId() + "</td><td>Coords</td><td>" + getX() + "," + getY() + "," + getZ() + "</td></tr>");
	    html1.append("</table><br>");
	    html1.append("<font color=\"LEVEL\">Combat</font>");
	    html1.append("<table border=\"0\" width=\"100%\">");
	    html1.append("<tr><td>Current HP</td><td>" + getCurrentHp() + "</td><td>Current MP</td><td>" + getCurrentMp() + "</td></tr>");
	    html1.append("<tr><td>Max.HP</td><td>" + (int) (getMaxHp() / getStat().calcStat(Stats.MAX_HP, 1, this, null)) + "*" + getStat().calcStat(Stats.MAX_HP, 1, this, null) + "</td><td>Max.MP</td><td>" + getMaxMp() + "</td></tr>");
	    html1.append("<tr><td>P.Atk.</td><td>" + getPAtk(null) + "</td><td>M.Atk.</td><td>" + getMAtk(null, null) + "</td></tr>");
	    html1.append("<tr><td>P.Def.</td><td>" + getPDef(null) + "</td><td>M.Def.</td><td>" + getMDef(null, null) + "</td></tr>");
	    html1.append("<tr><td>Accuracy</td><td>" + getAccuracy() + "</td><td>Evasion</td><td>" + getEvasionRate(null) + "</td></tr>");
	    html1.append("<tr><td>Critical</td><td>" + getCriticalHit(null, null) + "</td><td>Speed</td><td>" + getRunSpeed() + "</td></tr>");
	    html1.append("<tr><td>Atk.Speed</td><td>" + getPAtkSpd() + "</td><td>Cast.Speed</td><td>" + getMAtkSpd() + "</td></tr>");
	    html1.append("</table><br>");
	    html1.append("<font color=\"LEVEL\">Basic Stats</font>");
	    html1.append("<table border=\"0\" width=\"100%\">");
	    html1.append("<tr><td>STR</td><td>" + getSTR() + "</td><td>DEX</td><td>" + getDEX() + "</td><td>CON</td><td>" + getCON() + "</td></tr>");
	    html1.append("<tr><td>INT</td><td>" + getINT() + "</td><td>WIT</td><td>" + getWIT() + "</td><td>MEN</td><td>" + getMEN() + "</td></tr>");
	    html1.append("</table>");
	    html1.append("<br><center><table><tr><td><button value=\"Edit NPC\" action=\"bypass -h admin_edit_npc " + getTemplate().npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br1></td>");
	    html1.append("<td><button value=\"Kill\" action=\"bypass -h admin_kill\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><br1></tr>");
	    html1.append("<tr><td><button value=\"Show DropList\" action=\"bypass -h admin_show_droplist " + getTemplate().npcId + "\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
	    html1.append("<td><button value=\"Delete\" action=\"bypass -h admin_delete\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
	    html1.append("</table></center><br>");
	    html1.append("</body></html>");
            html.setHtml(html1.toString());
            client.sendPacket(html);
            client.setTarget(this);
            client.sendPacket(new MyTargetSelected(getObjectId(), 0));
        } else
        {
            onAction(client);
        }
    }

    public void reduceCurrentHp(double d, L2Character l2character, boolean flag)
    {
    }

    public void showChatWnd(L2PcInstance client, String id)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile((new StringBuilder("data/html/maxi/buffer/").append(id.equals("-1") ? "main" : (new StringBuilder("main-")).append(id).toString()).append(".htm").toString()));
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%after%", id);
        if(id.endsWith("_repl"))
        {
			net.sf.l2ddt.engine.Npcbuffer.BuffGroup bg;
            for(Iterator iterator = Npcbuffer.buffs().values().iterator(); iterator.hasNext(); html.replace((new StringBuilder("%e")).append(bg.nId).append("i%").toString(), (new StringBuilder("&#")).append(bg.itemId).append(";").toString()))
            {
				bg = (net.sf.l2ddt.engine.Npcbuffer.BuffGroup)iterator.next();
                html.replace((new StringBuilder("%e")).append(bg.nId).append("c%").toString(), String.valueOf(bg.itemCount));
            }

        }
        client.sendPacket(html);
    }

    public void showChatWnd(L2PcInstance client, String id, int count, int item)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile((new StringBuilder("data/html/maxi/buffer/main-notenought.htm").toString()));
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%after%", id);
        html.replace("%count%", String.valueOf(count));
        html.replace("%item%", (new StringBuilder("&#")).append(item).append(";").toString());
        client.sendPacket(html);
    }

    public void showChatErrWnd(L2PcInstance client, String after, String text)
    {
        NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
        html.setFile((new StringBuilder("data/html/maxi/buffer/main-err.htm").toString()));
        html.replace("%objectId%", String.valueOf(getObjectId()));
        html.replace("%text%", text);
        html.replace("%after%", after);
        client.sendPacket(html);
    }
}
