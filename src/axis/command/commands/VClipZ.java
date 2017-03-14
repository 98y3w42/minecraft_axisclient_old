package axis.command.commands;

import axis.command.Command;
import axis.util.Logger;
import net.minecraft.client.Minecraft;

public class VClipZ extends Command {

   private final Minecraft mc = Minecraft.getMinecraft();


   public VClipZ() {
      super("vclipz", "<blocks>", new String[]{"vcz"});
   }

   public void run(String message) {
      double blocks = Double.parseDouble(message.split(" ")[1]);
      this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + blocks);
      Logger.logChat("Teleported \"" + blocks + "\" blocks.");
   }
}