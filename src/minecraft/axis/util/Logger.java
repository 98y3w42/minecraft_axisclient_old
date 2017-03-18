package axis.util;

import axis.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Logger {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static void logChat(String message) {
		if (mc.thePlayer != null) {
			mc.thePlayer.addChatMessage(new ChatComponentText("§7[§9" + Axis.getAxis().getName() + "§7]§f" + " " + message));
			//DrawScreenEvent.setMessage("§7[§9Hex§7]" + " " + message);
		} else {
			logConsole(message);
		}
	}

	public static void logConsole(String message) {
		System.out.println("[Axis]" + " "+ message);
	}

	public static void logIRC(String message) {
        if (mc.thePlayer == null)
        {
            logConsole(message);
        }
        else
        {
        	logConsole(message);
            mc.thePlayer.addChatMessage(new ChatComponentText("§b[IRC]§f " + message));
        }
    }
}
