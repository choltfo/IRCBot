package org.zzl.choltfo.IRCBot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class IRCBotMain {
	final public static String chan = "#CholtfoTesting";
	public static String name = "LTRB";
	
	public static void main(String[] args) throws NickAlreadyInUseException, IOException, IrcException {
		IRCBot bot = new IRCBot();
		bot.setVerbose(true);
		int i = 0;
	    while (!bot.isConnected()) {
	    	try {
	    		bot.connect("irc.dal.net", 6662);
	    	} catch (NickAlreadyInUseException NAIUE) {
	    		System.out.println("");
	    		bot.setNameExt(name+i);
	    	}
	    }
	    bot.joinChannel(chan);
	    //bot.setVerbose(false);
	    bot.sendMessage(chan,Colors.GREEN+"Hello there!");
	}
}

enum BTEOptions {
	broadcastOnChannel,
	sendPrivate,
	off
}

class userSettings {
	boolean op = true;
	boolean canTranslate = true;
	BTEOptions broadcastTranslatedEchoes = BTEOptions.sendPrivate;
	
	public void toFile(PrintStream out) {
		out.print(op);
		out.print(canTranslate);
		out.println(broadcastTranslatedEchoes);
	}
	
	public static userSettings fromString(String[] input) {
		userSettings nUser = new userSettings();
		nUser.op = input[0].equals("true");
		nUser.canTranslate = input[1].equals("true");
		nUser.broadcastTranslatedEchoes = BTEOptions.valueOf(input[2]);
		return nUser;
	}
}

class IRCBot extends PircBot {
	LindTranslator LT;
	
	HashMap<String, userSettings> userSettings = new HashMap<String, userSettings>();
	
	public IRCBot () {
		setName(IRCBotMain.name);
		LT = new LindTranslator();
	}
	
	public void setNameExt(String name) {
		setName(name);
	}
	
	public void onJoin(String channel, String sender, String login, String hostname) {
		for (User user : getUsers(IRCBotMain.chan)) {
			if (user.getNick() == getNick()) {
				if (!userSettings.containsKey(user.getNick())) {
					userSettings NUS = new userSettings();
					NUS.op = user.isOp();
					userSettings.put(user.getNick(), NUS);
				}
			}
		}
	}
	
	public void onNickChange(String oldNick,String login,String hostname,String newNick) {
		userSettings NUS = userSettings.get(oldNick);
		userSettings.put(newNick,NUS);
		userSettings.remove(oldNick);
	}
	
	public void saveSettings() {
		PrintStream out;
		
		try {
			out = new PrintStream(new FileOutputStream("Settings.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Saving failed! Try again!");
			return;
		}
		
		Set<String> keys = userSettings.keySet();
		
		for (int i = 0; i < keys.size(); i++) {
			out.println(keys.toArray()[i]);
			userSettings.get(keys.toArray()[i]).toFile(out);
		}
		
		out.close();
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		
		Set<String> keys = userSettings.keySet();
		System.out.println(userSettings.size());
		for (int i = 0; i < keys.size(); i++) {
			System.out.println(keys.toArray()[i] + ", " + userSettings.get(keys.toArray()[i]));
		}
		
		System.out.println(channel + " -> "+login+"(" + sender + "): "+message);
		if (!message.startsWith("~")) {
			System.out.println("No command found.");
			return;
		}
		if (message.startsWith("~T")) {
			System.out.println("Received ~T from "+sender+". Replying....");
			sendMessage(sender,message);
			sendMessage(channel,message);
			try {
				if (userSettings.get(sender).broadcastTranslatedEchoes == BTEOptions.sendPrivate) {
					sendMessage(sender,sender+": "+LT.translate(message.substring(2)));
				} else if (userSettings.get(sender).broadcastTranslatedEchoes == BTEOptions.broadcastOnChannel) {
					sendMessage(channel,sender+": "+LT.translate(message.substring(2)));
				}
				System.out.println("Sending translated message to "+sender+" via "+ sender);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (message.startsWith("~Settings")) {
			System.out.println("Received Settings from "+sender+" on "+channel+". Replying.");
			sendMessage(channel,sender+": LTRBOP = "+userSettings.get(sender).op+", canTranslate = "+userSettings.get(sender).canTranslate+
					", echoMode = "+userSettings.get(sender).broadcastTranslatedEchoes);
		}
		if (userSettings.get(sender).op) {			// OPs only after here.
			if (message.startsWith("~S")) {
				saveSettings();
			}
			if (message.startsWith("~Q")) {
				quitServer("And so the time has come....");
				saveSettings();
				System.out.println("Received ~Q, exiting.");
				System.exit(0);
			}
		}
	}
}
