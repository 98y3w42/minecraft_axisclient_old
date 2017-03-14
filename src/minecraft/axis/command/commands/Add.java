package axis.command.commands;

import axis.Axis;
import axis.command.Command;
import axis.event.events.TickEvent;
import axis.util.Logger;

public class Add extends Command {

	public String alias;

	public Add() {
		super("add", "<name alias>", new String[] { "f" });
	}

	public void run(String message) {
		String name = message.split(" ")[1];
		this.alias = message.substring((message.split(" ")[0] + " " + name + " ").length());
		Axis.getFriendManager().addFriend(name, this.alias);
		Logger.logChat("Friend " + name + " added with the alias of " + this.alias + ".");

		if (Axis.getFileManager().getFileByName("friends") != null) {
			Axis.getFileManager().getFileByName("friends").saveFile();
		}
	}
}