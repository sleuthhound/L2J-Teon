/*
 * package org.relayirc.chatengine;

 import org.relayirc.*;

 public class ChannelLogger extends ChannelAdapter {
 private int _msgCount = 0;

 public void onMessage(ChannelEvent event) {
 System.out.println(
 event.getOriginNick()+" says "+(String)event.getValue());

 if (_msgCount++ > 10) {
 L2JConnect.l2jConnect.stop();
 }
 }
 }
 */
package net.sf.l2j.gameserver.lib;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class Listener extends IRCEventAdapter implements IRCEventListener
{
	public void onConnect()
	{
		System.out.println("Connected successfully.");
	}

	@Override
	public void onDisconnected()
	{
		System.out.println("Disconnected.");
	}

	@Override
	public void onError(String msg)
	{
		System.out.println("ERROR: " + msg);
	}

	@Override
	public void onError(int num, String msg)
	{
		System.out.println("Error #" + num + ": " + msg);
	}

	@Override
	public void onInvite(String chan, IRCUser user, String nickPass)
	{
		System.out.println("INVITE: " + user.getNick() + " invites " + nickPass + " to " + chan);
	}

	@Override
	public void onJoin(String chan, IRCUser user)
	{
		System.out.println("JOIN: " + user.getNick() + " joins " + chan);
		// add the nickname to the nickname-table
	}

	@Override
	public void onKick(String chan, IRCUser user, String nickPass, String msg)
	{
		System.out.println("KICK: " + user.getNick() + " kicks " + nickPass + "(" + msg + ")");
		// remove the nickname from the nickname-table
	}

	@Override
	public void onMode(String chan, IRCUser user, IRCModeParser modeParser)
	{
		System.out.println("MODE: " + user.getNick() + " changes modes in " + chan + ": " + modeParser.getLine());
		// some operations with the modes
	}

	@Override
	public void onNick(IRCUser user, String nickNew)
	{
		System.out.println("NICK: " + user.getNick() + " is now known as " + nickNew);
		// update the nickname in the nickname-table
	}

	@Override
	public void onPart(String chan, IRCUser user, String msg)
	{
		System.out.println("PART: " + user.getNick() + " parts from " + chan + "(" + msg + ")");
		// remove the nickname from the nickname-table
	}

	@Override
	public void onPrivmsg(String target, IRCUser user, String msg)
	{
		if (msg.contains(Config.IRC_KEYWORDTOIG))
		{
			String text = msg.replace(Config.IRC_KEYWORDTOIG, "");
			CreatureSay csg = new CreatureSay(2000, 1, user.getNick() + "_IRC", text);
			for (L2PcInstance player : L2World.getInstance().getAllPlayers())
				player.sendPacket(csg);
		}
	}

	@Override
	public void onQuit(IRCUser user, String msg)
	{
		System.out.println("QUIT: " + user.getNick() + " (" + user.getUsername() + "@" + user.getHost() + ") (" + msg + ")");
		// remove the nickname from the nickname-table
	}

	@Override
	public void onReply(int num, String value, String msg)
	{
		System.out.println("Reply #" + num + ": Message: " + msg + " | Value: " + value);
	}

	@Override
	public void onTopic(String chan, IRCUser user, String topic)
	{
		System.out.println("TOPIC: " + user.getNick() + " changes topic of " + chan + " into: " + topic);
	}
}
