package axis.command.commands;

import axis.Axis;
import axis.command.Command;
import axis.module.Module;
import axis.util.Logger;

public final class Toggle extends Command {
	public Toggle() {
		super("toggle", "<mod name>", new String[] { "t" });
	}

	public void run(String message) {
		String[] arguments = message.split(" ");
		Module mod = Axis.getModuleManager().getModuleByName(arguments[1]);
		if (mod == null) {
			Logger.logChat("Mod \"" + arguments[1] + "\" was not found!");
		} else {
			mod.toggle();
			Logger.logChat("Mod \"" + mod.getName() + "\" was toggled " + (mod.isEnabled() ? "§2on" : "§4off") + "§f.");
		}
	}
}
