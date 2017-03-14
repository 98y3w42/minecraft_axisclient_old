package axis.command.commands;

import axis.command.Command;
import axis.util.Logger;
import net.minecraft.client.Minecraft;

public class VClipX extends Command {

   private final Minecraft mc = Minecraft.getMinecraft();


   public VClipX() {
      super("vclipx", "<blocks>", new String[]{"vcx"});
   }

   public void run(String message) {
      double blocks = Double.parseDouble(message.split(" ")[1]);
      this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + blocks, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
      Logger.logChat("Teleported \"" + blocks + "\" blocks.");
   }
}