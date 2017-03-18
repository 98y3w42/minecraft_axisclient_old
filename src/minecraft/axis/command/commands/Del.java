package axis.command.commands;

import axis.Axis;
import axis.command.Command;
import axis.util.Logger;

public class Del extends Command {

	public Del() {
		super("del", "<name>", new String[] { "delete" });
	}

	public void run(String message) {
		String name = message.split(" ")[1];
		Axis.getAxis().getFriendManager().removeFriend(name);
		Logger.logChat("Friend \"" + name + "\" removed.");
		Axis.getAxis().getFileManager().getFileByName("friends").saveFile();
	}
}
