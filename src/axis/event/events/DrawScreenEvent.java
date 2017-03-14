package axis.event.events;

import axis.event.Event;
import net.minecraft.client.Minecraft;

public class DrawScreenEvent extends Event {

	public static int tick;
	public static String message;
	public Minecraft mc = Minecraft.getMinecraft();

	public void onDraw() {
		if (message != null) {
			tick++;
			mc.fontRendererObj.drawStringWithShadow(message, 2.0F, 2.0F, -1);
			if (tick > 200) {
				tick = 0;
				message = null;
			}
		}
	}

	public static void setMessage(String message) {
		DrawScreenEvent.message = message;
	}

	public static String getMessage() {
		return DrawScreenEvent.message;
	}
}
