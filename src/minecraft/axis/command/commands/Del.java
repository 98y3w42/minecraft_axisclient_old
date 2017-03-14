package axis.command.commands;

import axis.Axis;
import axis.command.Command;
import axis.event.events.TickEvent;
import axis.util.Logger;

public class Del extends Command {

	public Del() {
		super("del", "<name>", new String[] { "delete" });
	}

	public void run(String message) {
		if (message.split(" ")[1].equalsIgnoreCase("del")) {
			String name = message.split(" ")[2];
			Axis.getFriendManager().removeFriend(name);
			Logger.logChat("Friend \"" + name + "\" removed.");
		}
		Axis.getFileManager().getFileByName("friends").saveFile();
	}
}
