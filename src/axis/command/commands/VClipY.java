package axis.command.commands;

import axis.command.Command;
import axis.util.Logger;
import net.minecraft.client.Minecraft;

public class VClipY extends Command {

   private final Minecraft mc = Minecraft.getMinecraft();


   public VClipY() {
      super("vclipy", "<blocks>", new String[]{"vcy"});
   }

   public void run(String message) {
      double blocks = Double.parseDouble(message.split(" ")[1]);
      this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + blocks, this.mc.thePlayer.posZ);
      Logger.logChat("Teleported \"" + blocks + "\" blocks.");
   }
}