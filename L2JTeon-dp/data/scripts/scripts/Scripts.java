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

import scripts.admin.*;
import scripts.items.*;
import scripts.usercommand.*;
import scripts.voicedcommand.*;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.ItemHandler;
import net.sf.l2j.gameserver.handler.UserCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;

/**
 * @author  Maxi
 */
public class Scripts
{
	private static Logger _log = Logger.getLogger(Scripts.class.getName());

	private static void loadAdminHandlers()
	{
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminAdmin());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminInvul());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminDelete());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminKill());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminTarget());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminShop());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminAnnouncements());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminAutoAnnouncements());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminCreateItem());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminHeal());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminHelpPage());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminShutdown());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminSpawn());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminSkill());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminExpSp());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminEventEngine());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminGmChat());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminEditChar());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminGm());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminTeleport());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminRepairChar());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminChangeAccessLevel());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminBan());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminPolymorph());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminBanChat());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminKick());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminMonsterRace());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminEditNpc());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminEditPrivs());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminFightCalculator());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminMenu());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminSiege());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminFort());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminClanHallSieges());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminPathNode());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminPetition());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminPForge());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminBBS());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminEffects());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminDoorControl());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminTest());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminEnchant());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminMobGroup());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminRes());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminMammon());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminUnblockIp());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminPledge());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminRideWyvern());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminReload());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminLogin());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminCache());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminLevel());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminQuest());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminZone());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminDonator());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminCTFEngine());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminCursedWeapons());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminGeodata());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminGeoEditor());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminManor());
	AdminCommandHandler.getInstance().registerAdminCommandHandler(new AdminMassRecall());
	}

	private static void loadItemHandlers()
	{
		ItemHandler.getInstance().registerItemHandler(new ScrollOfEscape());
		ItemHandler.getInstance().registerItemHandler(new ScrollOfResurrection());
		ItemHandler.getInstance().registerItemHandler(new SoulShots());
		ItemHandler.getInstance().registerItemHandler(new SpiritShot());
		ItemHandler.getInstance().registerItemHandler(new BlessedSpiritShot());
		ItemHandler.getInstance().registerItemHandler(new BeastSoulShot());
		ItemHandler.getInstance().registerItemHandler(new BeastSpiritShot());
		ItemHandler.getInstance().registerItemHandler(new ChestKey());
		ItemHandler.getInstance().registerItemHandler(new PaganKeys());
		ItemHandler.getInstance().registerItemHandler(new Maps());
		ItemHandler.getInstance().registerItemHandler(new Potions());
		ItemHandler.getInstance().registerItemHandler(new Recipes());
		ItemHandler.getInstance().registerItemHandler(new RollingDice());
		ItemHandler.getInstance().registerItemHandler(new MysteryPotion());
		ItemHandler.getInstance().registerItemHandler(new EnchantScrolls());
		ItemHandler.getInstance().registerItemHandler(new EnergyStone());
		ItemHandler.getInstance().registerItemHandler(new Book());
		ItemHandler.getInstance().registerItemHandler(new Remedy());
		ItemHandler.getInstance().registerItemHandler(new Scrolls());
		ItemHandler.getInstance().registerItemHandler(new CrystalCarol());
		ItemHandler.getInstance().registerItemHandler(new DonatorItems());
		ItemHandler.getInstance().registerItemHandler(new SoulCrystals());
		ItemHandler.getInstance().registerItemHandler(new SevenSignsRecord());
		ItemHandler.getInstance().registerItemHandler(new CharChangePotions());
		ItemHandler.getInstance().registerItemHandler(new Firework());
		ItemHandler.getInstance().registerItemHandler(new Seed());
		ItemHandler.getInstance().registerItemHandler(new Harvester());
		ItemHandler.getInstance().registerItemHandler(new MercTicket());
		ItemHandler.getInstance().registerItemHandler(new FishShots());
		ItemHandler.getInstance().registerItemHandler(new JackpotSeed());
		ItemHandler.getInstance().registerItemHandler(new ExtractableItems());
		ItemHandler.getInstance().registerItemHandler(new SpecialXMas());
		ItemHandler.getInstance().registerItemHandler(new SplendorKeys());
		ItemHandler.getInstance().registerItemHandler(new SummonItems());
		ItemHandler.getInstance().registerItemHandler(new BeastSpice());
		ItemHandler.getInstance().registerItemHandler(new PrimevalPotions());
		ItemHandler.getInstance().registerItemHandler(new ScrollsValakas());
		_log.config("Loaded " + ItemHandler.getInstance().size() + " ItemHandlers");
	}

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
		loadAdminHandlers();
		loadItemHandlers();
		loadUserHandlers();
		loadVoicedHandlers();
		_log.config("Handlers Loaded...");
	}
}