package axis.command.commands;

import axis.Axis;
import axis.command.Command;
import axis.module.Module;
import axis.util.Logger;

public final class Help extends Command {
	public Help() {
		super("Help", "<>", new String[] { "he" });
	}

	public void run(String message) {
		Logger.logChat(
				"Commands list: add, bind, del, help, toggle, vclipx, vclipy, vclipz, aimassist, autopot, criticals, killaura, regen, velocity, setting, civbreak, leastcivbreak, jesus, speed, step, esp, storageesp, worldtimer");
	}
}
