package axis.command.commands;

import org.lwjgl.input.Keyboard;

import axis.Axis;
import axis.command.Command;
import axis.module.Module;
import axis.util.Logger;


public final class Bind extends Command {
   public Bind() {
      super("bind", "<mod name> <key>", new String[0]);
   }

   public void run(String message) {
	   Module mod = Axis.getModuleManager().getModuleByName(message.split(" ")[1]);
      if(mod == null) {
         Logger.logChat("Module \"" + message.split(" ")[1] + "\" was not found!");
      } else {
         mod.setKeybind(Keyboard.getKeyIndex(message.split(" ")[2].toUpperCase()));
         Logger.logChat("Module \"" + mod.getName() + "\" is now bound to: " + Keyboard.getKeyName(mod.getKeybind()));
      }

   }
}
