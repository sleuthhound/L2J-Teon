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
package scripts;

import java.util.logging.Logger;

import scripts.items.*;
//import scripts.skills.*;
import scripts.usercommand.*;
import scripts.voicedcommand.*;

import net.sf.l2j.Config;
// import net.sf.l2j.gameserver.handler.ChatHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
// import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.handler.UserCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
// import net.sf.l2j.gameserver.model.L2Skill;
// import net.sf.l2j.gameserver.model.L2Character;

/**
 *
 * @author  Maxi
 */
public class Scripts
{
	private static Logger _log = Logger.getLogger(Scripts.class.getName());
/*
	private static void loadChatHandlers()
	{
		_log.config("Loaded " + ChatHandler.getInstance().size() + "  ChatHandlers");
	}
	*/
	private static void loadItemHandlers()
	{
		ItemHandler.getInstance().registerItemHandler(new PrimevalPotions());
		ItemHandler.getInstance().registerItemHandler(new ScrollsValakas());
		_log.config("Loaded " + ItemHandler.getInstance().size() + " ItemHandlers");
	}
	/*
	private static void loadSkillHandlers()
	{
		_log.config("Loaded " + SkillHandler.getInstance().size() + " SkillHandlers");
	}
	*/
	private static void loadUserHandlers()
	{
		UserCommandHandler.getInstance().registerUserCommandHandler(new ClanPenalty());
		UserCommandHandler.getInstance().registerUserCommandHandler(new ClanWarsList());
		UserCommandHandler.getInstance().registerUserCommandHandler(new DisMount());
		UserCommandHandler.getInstance().registerUserCommandHandler(new Escape());
		UserCommandHandler.getInstance().registerUserCommandHandler(new Loc());
		UserCommandHandler.getInstance().registerUserCommandHandler(new Mount());
		UserCommandHandler.getInstance().registerUserCommandHandler(new PartyInfo());
		UserCommandHandler.getInstance().registerUserCommandHandler(new Time());
		UserCommandHandler.getInstance().registerUserCommandHandler(new OlympiadStat());
		UserCommandHandler.getInstance().registerUserCommandHandler(new ChannelLeave());
		UserCommandHandler.getInstance().registerUserCommandHandler(new ChannelDelete());
		UserCommandHandler.getInstance().registerUserCommandHandler(new ChannelListUpdate());
		_log.config("Loaded " + UserCommandHandler.getInstance().size() + " UserHandlers");
	}
	
	private static void loadVoicedHandlers()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new stats());
		if (Config.ALLOW_WEDDING)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new Wedding());
		if (Config.BANKING_SYSTEM_ENABLED)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new Banking());
		if(Config.ONLINE_VOICE_COMMAND)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new OnlinePlayers());
		if(Config.ALLOW_TRADEOFF_VOICE_COMMAND)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new tradeoff());
		if (Config.ALLOW_AWAY_STATUS)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new Away());
		if(Config.VIP_EVENT_ENABLED)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new JoinVIP());
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new PlayersWithdrawCWH());
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(new version());
		_log.config("Loaded " + VoicedCommandHandler.getInstance().size() + " VoicedHandlers");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		_log.config("Loading Handlers...");
		// loadChatHandlers();
		loadItemHandlers();
		// loadSkillHandlers();
		loadUserHandlers();
		loadVoicedHandlers();
		_log.config("Handlers Loaded...");
	}
}