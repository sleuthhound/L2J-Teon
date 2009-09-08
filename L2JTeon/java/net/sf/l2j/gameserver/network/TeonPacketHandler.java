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
package net.sf.l2j.gameserver.network;

import javolution.util.FastMap;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.network.L2GameClient.ClientState;
import net.sf.l2j.gameserver.clientpackets.*;

/**
 * @author Setekh
 */
public class TeonPacketHandler
{
	private FastMap<ClientState, FastMap<Integer, L2GameClientPacket>> _packets;
	private FastMap<Integer, FastMap<Integer, L2GameClientPacket>> _packets2xOP;
	
	private static TeonPacketHandler _instance;
	
	public static TeonPacketHandler getInstance()
	{
		if(_instance == null)
		{
			_instance = new TeonPacketHandler();
		}
		return _instance;
	}
	
	/**
	 * This Constructor adds the packets :)
	 */
	public TeonPacketHandler()
	{
		_packets = new FastMap<ClientState ,FastMap<Integer, L2GameClientPacket>>().setShared(true);
		_packets2xOP = new FastMap<Integer, FastMap<Integer, L2GameClientPacket>>().setShared(true);
		
		addPacket(new ProtocolVersion(),0x00,ClientState.CONNECTED);
		addPacket(new AuthLogin(),0x08,ClientState.CONNECTED);
		addPacket(new Logout(),0x09,ClientState.AUTHED);
		addPacket(new CharacterCreate(),0x0b,ClientState.AUTHED);
		addPacket(new CharacterDelete(),0x0c,ClientState.AUTHED);
		addPacket(new CharacterSelected(),0x0d,ClientState.AUTHED);
		addPacket(new NewCharacter(),0x0e,ClientState.AUTHED);
		addPacket(new CharacterRestore(),0x62,ClientState.AUTHED);
		addPacket(new RequestPledgeCrest(),0x68,ClientState.AUTHED);
		addPacket(new MoveBackwardToLocation(),0x01,ClientState.IN_GAME);
		addPacket(new EnterWorld(),0x03, ClientState.IN_GAME);
		addPacket(new Action(),0x04,ClientState.IN_GAME);
		addPacket(new Logout(),0x09,ClientState.IN_GAME);
		addPacket(new AttackRequest(),0x0a,ClientState.IN_GAME);
		addPacket(new RequestItemList(),0x0f,ClientState.IN_GAME);
		addPacket(new RequestUnEquipItem(),0x11,ClientState.IN_GAME);
		addPacket(new RequestDropItem(),0x12,ClientState.IN_GAME);
		addPacket(new UseItem(),0x14,ClientState.IN_GAME);
		addPacket(new TradeRequest(),0x15,ClientState.IN_GAME);
		addPacket(new AddTradeItem(),0x16,ClientState.IN_GAME);
		addPacket(new TradeDone(),0x17,ClientState.IN_GAME);
		addPacket(new DummyPacket(),0x1a,ClientState.IN_GAME);
		addPacket(new RequestSocialAction(),0x1b,ClientState.IN_GAME);
		addPacket(new ChangeMoveType2(),0x1c,ClientState.IN_GAME);
		addPacket(new ChangeWaitType2(),0x1d,ClientState.IN_GAME);
		addPacket(new RequestSellItem(),0x1e,ClientState.IN_GAME);
		addPacket(new RequestBuyItem(),0x1f,ClientState.IN_GAME);
		addPacket(new RequestLinkHtml(),0x20,ClientState.IN_GAME);
		addPacket(new RequestBypassToServer(),0x21,ClientState.IN_GAME);
		addPacket(new RequestBBSwrite(),0x22,ClientState.IN_GAME);
		addPacket(new DummyPacket(),0x23,ClientState.IN_GAME);
		addPacket(new RequestJoinPledge(),0x24,ClientState.IN_GAME);
		addPacket(new RequestAnswerJoinPledge(),0x25,ClientState.IN_GAME);
		addPacket(new RequestWithdrawalPledge(),0x26,ClientState.IN_GAME);
		addPacket(new RequestOustPledgeMember(),0x27,ClientState.IN_GAME);
		//- 0x28 not used?
		addPacket(new RequestJoinParty(),0x29,ClientState.IN_GAME);
		addPacket(new RequestAnswerJoinParty(),0x2a,ClientState.IN_GAME);
		addPacket(new RequestWithDrawalParty(),0x2b,ClientState.IN_GAME);
		addPacket(new RequestOustPartyMember(),0x2c,ClientState.IN_GAME);
		//- 0x2d RequestDismissParty?
		addPacket(new DummyPacket(),0x2e,ClientState.IN_GAME);
		addPacket(new RequestMagicSkillUse(),0x2f,ClientState.IN_GAME);
		addPacket(new Appearing(),0x30,ClientState.IN_GAME);
		if (Config.ALLOW_WAREHOUSE)
			addPacket(new SendWareHouseDepositList(),0x31,ClientState.IN_GAME);
		addPacket(new SendWareHouseWithDrawList(),0x32,ClientState.IN_GAME);
		addPacket(new RequestShortCutReg(),0x33,ClientState.IN_GAME);
		addPacket(new DummyPacket(),0x34,ClientState.IN_GAME);
		addPacket(new RequestShortCutDel(),0x35,ClientState.IN_GAME);
		addPacket(new CannotMoveAnymore(),0x36,ClientState.IN_GAME);
		addPacket(new RequestTargetCanceld(),0x37,ClientState.IN_GAME);
		addPacket(new Say2(),0x38,ClientState.IN_GAME);
		addPacket(new RequestPledgeMemberList(),0x3c,ClientState.IN_GAME);
		addPacket(new DummyPacket(),0x3e,ClientState.IN_GAME);
		addPacket(new RequestSkillList(),0x3f,ClientState.IN_GAME);
		addPacket(new RequestGetOnVehicle(),0x42,ClientState.IN_GAME);
		addPacket(new RequestGetOffVehicle(),0x43,ClientState.IN_GAME);
		addPacket(new AnswerTradeRequest(),0x44,ClientState.IN_GAME);
		addPacket(new RequestActionUse(),0x45,ClientState.IN_GAME);
		addPacket(new RequestRestart(),0x46,ClientState.IN_GAME);
		addPacket(new ValidatePosition(),0x48,ClientState.IN_GAME);
		// Unused till more info bout them is sniffed
		addPacket(new StartRotating(),0x4a,ClientState.IN_GAME);
		addPacket(new FinishRotating(),0x4b,ClientState.IN_GAME);
		addPacket(new RequestStartPledgeWar(),0x4d,ClientState.IN_GAME);
		addPacket(new RequestReplyStartPledgeWar(),0x4e,ClientState.IN_GAME);
		addPacket(new RequestStopPledgeWar(),0x4f,ClientState.IN_GAME);
		addPacket(new RequestReplyStopPledgeWar(),0x50,ClientState.IN_GAME);
		addPacket(new RequestSurrenderPledgeWar(),0x51,ClientState.IN_GAME);
		addPacket(new RequestReplySurrenderPledgeWar(),0x52,ClientState.IN_GAME);
		addPacket(new RequestSetPledgeCrest(),0x53,ClientState.IN_GAME);
		addPacket(new RequestGiveNickName(),0x55,ClientState.IN_GAME);
		addPacket(new RequestShowBoard(),0x57,ClientState.IN_GAME);
		addPacket(new RequestEnchantItem(),0x58,ClientState.IN_GAME);
		addPacket(new RequestDestroyItem(),0x59,ClientState.IN_GAME);
		addPacket(new SendBypassBuildCmd(),0x5b,ClientState.IN_GAME);
		addPacket(new RequestMoveToLocationInVehicle(),0x5c,ClientState.IN_GAME);
		addPacket(new CannotMoveAnymoreInVehicle(),0x5d,ClientState.IN_GAME);
		addPacket(new RequestFriendInvite(),0x5e,ClientState.IN_GAME);
		addPacket(new RequestAnswerFriendInvite(),0x5f,ClientState.IN_GAME);
		addPacket(new RequestFriendList(),0x60,ClientState.IN_GAME);
		addPacket(new RequestFriendDel(),0x61,ClientState.IN_GAME);
		addPacket(new RequestQuestList(),0x63,ClientState.IN_GAME);
		addPacket(new RequestQuestAbort(),0x64,ClientState.IN_GAME);
		addPacket(new RequestPledgeInfo(),0x66,ClientState.IN_GAME);
		addPacket(new RequestPledgeCrest(),0x68,ClientState.IN_GAME);
		addPacket(new RequestSurrenderPersonally(),0x69,ClientState.IN_GAME);
		addPacket(new RequestAquireSkillInfo(),0x6b,ClientState.IN_GAME);
		addPacket(new RequestAquireSkill(),0x6c,ClientState.IN_GAME);
		addPacket(new RequestRestartPoint(),0x6d,ClientState.IN_GAME);
		addPacket(new RequestGMCommand(),0x6e,ClientState.IN_GAME);
		addPacket(new RequestPartyMatchList(),0x6f,ClientState.IN_GAME);
		addPacket(new RequestPartyMatchConfig(),0x70,ClientState.IN_GAME);
		addPacket(new RequestPartyMatchDetail(),0x71,ClientState.IN_GAME);		
		addPacket(new RequestCrystallizeItem(),0x72,ClientState.IN_GAME);		
		addPacket(new RequestPrivateStoreManageSell(),0x73,ClientState.IN_GAME);		
		addPacket(new SetPrivateStoreListSell(),0x74,ClientState.IN_GAME);		
		addPacket(new RequestPrivateStoreQuitSell(),0x76,ClientState.IN_GAME);		
		addPacket(new SetPrivateStoreMsgSell(),0x77,ClientState.IN_GAME);		
		addPacket(new RequestPrivateStoreBuy(),0x79,ClientState.IN_GAME);		
		addPacket(new RequestPetition(),0x7f,ClientState.IN_GAME);		
		addPacket(new RequestPetitionCancel(),0x80,ClientState.IN_GAME);		
		addPacket(new RequestGmList(),0x81,ClientState.IN_GAME);		
		addPacket(new RequestJoinAlly(),0x82,ClientState.IN_GAME);		
		addPacket(new RequestAnswerJoinAlly(),0x83,ClientState.IN_GAME);		
		addPacket(new AllyLeave(),0x84,ClientState.IN_GAME);		
		addPacket(new AllyDismiss(),0x85,ClientState.IN_GAME);		
		addPacket(new RequestDismissAlly(),0x86,ClientState.IN_GAME);		
		addPacket(new RequestSetAllyCrest(),0x87,ClientState.IN_GAME);		
		addPacket(new RequestAllyCrest(),0x88,ClientState.IN_GAME);		
		addPacket(new RequestChangePetName(),0x89,ClientState.IN_GAME);		
		addPacket(new RequestPetUseItem(),0x8a,ClientState.IN_GAME);		
		addPacket(new RequestGiveItemToPet(),0x8b,ClientState.IN_GAME);		
		addPacket(new RequestGetItemFromPet(),0x8c,ClientState.IN_GAME);		
		addPacket(new RequestAllyInfo(),0x8e,ClientState.IN_GAME);		
		addPacket(new RequestPetGetItem(),0x8f,ClientState.IN_GAME);		
		addPacket(new RequestPrivateStoreManageBuy(),0x90,ClientState.IN_GAME);		
		addPacket(new SetPrivateStoreListBuy(),0x91,ClientState.IN_GAME);		
		addPacket(new RequestPrivateStoreQuitBuy(),0x93,ClientState.IN_GAME);		
		addPacket(new SetPrivateStoreMsgBuy(),0x94,ClientState.IN_GAME);		
		addPacket(new RequestPrivateStoreSell(),0x96,ClientState.IN_GAME);
		addPacket(new DummyPacket(),0x9d,ClientState.IN_GAME);
		addPacket(new RequestPackageSendableItemList(),0x9e,ClientState.IN_GAME);		
		addPacket(new RequestPackageSend(),0x9f,ClientState.IN_GAME);		
		addPacket(new RequestBlock(),0xa0,ClientState.IN_GAME);		
		addPacket(new RequestSiegeAttackerList(),0xa2,ClientState.IN_GAME);		
		addPacket(new RequestSiegeDefenderList(),0xa3,ClientState.IN_GAME);		
		addPacket(new RequestJoinSiege(),0xa4,ClientState.IN_GAME);		
		addPacket(new RequestConfirmSiegeWaitingList(),0xa5,ClientState.IN_GAME);		
		addPacket(new MultiSellChoose(),0xa7,ClientState.IN_GAME);		
		addPacket(new RequestUserCommand(),0xaa,ClientState.IN_GAME);		
		addPacket(new SnoopQuit(),0xab,ClientState.IN_GAME);		
		addPacket(new RequestRecipeBookOpen(),0xac,ClientState.IN_GAME);		
		addPacket(new RequestRecipeBookDestroy(),0xad,ClientState.IN_GAME);		
		addPacket(new RequestRecipeItemMakeInfo(),0xae,ClientState.IN_GAME);		
		addPacket(new RequestRecipeItemMakeSelf(),0xaf,ClientState.IN_GAME);		
		addPacket(new RequestRecipeShopMessageSet(),0xb1,ClientState.IN_GAME);		
		addPacket(new RequestRecipeShopListSet(),0xb2,ClientState.IN_GAME);		
		addPacket(new RequestRecipeShopManageQuit(),0xb3,ClientState.IN_GAME);		
		addPacket(new RequestRecipeShopMakeInfo(),0xb5,ClientState.IN_GAME);		
		addPacket(new RequestRecipeShopMakeItem(),0xb6,ClientState.IN_GAME);		
		addPacket(new RequestRecipeShopManagePrev(),0xb7,ClientState.IN_GAME);		
		addPacket(new ObserverReturn(),0xb8,ClientState.IN_GAME);		
		addPacket(new RequestEvaluate(),0xb9,ClientState.IN_GAME);		
		addPacket(new RequestHennaList(),0xba,ClientState.IN_GAME);		
		addPacket(new RequestHennaItemInfo(),0xbb,ClientState.IN_GAME);		
		addPacket(new RequestHennaEquip(),0xbc,ClientState.IN_GAME);		
		addPacket(new RequestPledgePower(),0xc0,ClientState.IN_GAME);		
		addPacket(new RequestMakeMacro(),0xc1,ClientState.IN_GAME);		
		addPacket(new RequestDeleteMacro(),0xc2,ClientState.IN_GAME);		
		addPacket(new RequestBuyProcure(),0xc3,ClientState.IN_GAME);		
		addPacket(new RequestBuySeed(),0xc4,ClientState.IN_GAME);		
		addPacket(new DlgAnswer(),0xc5,ClientState.IN_GAME);		
		addPacket(new RequestWearItem(),0xc6,ClientState.IN_GAME);		
		addPacket(new RequestSSQStatus(),0xc7,ClientState.IN_GAME);		
		addPacket(new GameGuardReply(),0xCA,ClientState.IN_GAME);		
		addPacket(new RequestSendFriendMsg(),0xcc,ClientState.IN_GAME);		
		addPacket(new RequestShowMiniMap(),0xcd,ClientState.IN_GAME);		
		addPacket(new RequestRecordInfo(),0xcf,ClientState.IN_GAME);
		addPacket2xOP(new RequestOustFromPartyRoom(),1,0xd0);
		addPacket2xOP(new RequestDismissPartyRoom(),2,0xd0);
		addPacket2xOP(new RequestWithdrawPartyRoom(),3,0xd0);
		addPacket2xOP(new RequestChangePartyLeader(),4,0xd0);
		addPacket2xOP(new RequestAutoSoulShot(),5,0xd0);
		addPacket2xOP(new RequestExEnchantSkillInfo(),6,0xd0);
		addPacket2xOP(new RequestExEnchantSkill(),7,0xd0);
		addPacket2xOP(new RequestManorList(),8,0xd0);
		addPacket2xOP(new RequestProcureCropList(),9,0xd0);
		addPacket2xOP(new RequestSetSeed(),0x0a,0xd0);
		addPacket2xOP(new RequestSetCrop(),0x0b,0xd0);
		addPacket2xOP(new RequestWriteHeroWords(),0x0c,0xd0);
		addPacket2xOP(new RequestExAskJoinMPCC(),0x0d,0xd0);
		addPacket2xOP(new RequestExAcceptJoinMPCC(),0x0e,0xd0);
		addPacket2xOP(new RequestExOustFromMPCC(),0x0f,0xd0);
		addPacket2xOP(new RequestExPledgeCrestLarge(),0x10,0xd0);
		addPacket2xOP(new RequestExSetPledgeCrestLarge(),0x11,0xd0);
		addPacket2xOP(new RequestOlympiadObserverEnd(),0x12,0xd0);
		addPacket2xOP(new RequestOlympiadMatchList(),0x13,0xd0);
		addPacket2xOP(new RequestAskJoinPartyRoom(),0x14,0xd0);
		addPacket2xOP(new AnswerJoinPartyRoom(),0x15,0xd0);
		addPacket2xOP(new RequestListPartyMatchingWaitingRoom(),0x16,0xd0);
		addPacket2xOP(new RequestExitPartyMatchingWaitingRoom(),0x17,0xd0);
		addPacket2xOP(new RequestGetBossRecord(),0x18,0xd0);
		addPacket2xOP(new RequestPledgeSetAcademyMaster(),0x19,0xd0);
		addPacket2xOP(new RequestPledgePowerGradeList(),0x1a,0xd0);
		addPacket2xOP(new RequestPledgeMemberPowerInfo(),0x1b,0xd0);
		addPacket2xOP(new RequestPledgeSetMemberPowerGrade(),0x1c,0xd0);
		addPacket2xOP(new RequestPledgeMemberInfo(),0x1d,0xd0);
		addPacket2xOP(new RequestPledgeWarList(),0x1e,0xd0);
		addPacket2xOP(new RequestExFishRanking(),0x1f,0xd0);
		addPacket2xOP(new RequestPCCafeCouponUse(),0x20,0xd0);
		addPacket2xOP(new RequestCursedWeaponList(),0x22,0xd0);
		addPacket2xOP(new RequestCursedWeaponLocation(),0x23,0xd0);
		addPacket2xOP(new RequestPledgeReorganizeMember(),0x24,0xd0);
		addPacket2xOP(new RequestExMPCCShowPartyMembersInfo(),0x26,0xd0);
		addPacket2xOP(new RequestDuelStart(),0x27,0xd0);
		addPacket2xOP(new RequestDuelAnswerStart(),0x28,0xd0);
		addPacket2xOP(new RequestConfirmTargetItem(),0x29,0xd0);
		addPacket2xOP(new RequestConfirmRefinerItem(),0x2a,0xd0);
		addPacket2xOP(new RequestConfirmGemStone(),0x2b,0xd0);
		addPacket2xOP(new RequestRefine(),0x2c,0xd0);
		addPacket2xOP(new RequestConfirmCancelItem(),0x2d,0xd0);
		addPacket2xOP(new RequestRefineCancel(),0x2e,0xd0);
		addPacket2xOP(new RequestExMagicSkillUseGround(),0x2f,0xd0);
		addPacket2xOP(new RequestDuelSurrender(),0x30,0xd0);
	}

	private void addPacket(L2GameClientPacket packet, int opcode, ClientState... states)
	{
		for(ClientState state : states)
		{
			FastMap<Integer, L2GameClientPacket> packets = _packets.get(state);
			if(packets == null)
			{
				packets = new FastMap<Integer, L2GameClientPacket>();
				_packets.put(state, packets);
			}
			packets.put(opcode, packet);
		}
	}

	private void addPacket2xOP(L2GameClientPacket packet, int opcode2, int opcode1)
	{
		FastMap<Integer, L2GameClientPacket> packets = _packets2xOP.get(opcode1);
		if(packets == null)
		{
			packets = new FastMap<Integer, L2GameClientPacket>();
			_packets2xOP.put(opcode1, packets);
		}
		packets.put(opcode2, packet);
	}
	
	public FastMap<ClientState, FastMap<Integer, L2GameClientPacket>> getPackets()
	{
		return _packets;
	}

	public FastMap<Integer, FastMap<Integer, L2GameClientPacket>> get2xOpPackets()
	{
		return _packets2xOP;
	}
}
