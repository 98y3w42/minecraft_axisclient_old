package axis.module.modules.render;

import axis.Axis;
import axis.command.Command;
import axis.event.Event.State;
import axis.event.events.PacketReceiveEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class WorldTimer extends Module {
	private float rain;
	public final Value<Long> day = new Value<>("day", 18000L);

	public WorldTimer() {
		super("WorldTimer", 9623002, Category.RENDER);
		Axis.getCommandManager().getContents().add(new Command("worldtimer", "<day>", new String[] { "wt" }) {
			public void run(String message) {
				WorldTimer.this.day.setValue(Long.valueOf(Long.parseLong(message.split(" ")[1])));
				Logger.logChat("WorldTimer set to: " + WorldTimer.this.day.getValue());
			}
		});
	}

	private void onPacketRecieve(PacketReceiveEvent event) {
		if (event.packet instanceof S03PacketTimeUpdate) {
			event.setCancelled(true);
		}
	}

	private void onTick(TickEvent event) {
		this.mc.theWorld.setWorldTime(day.getValue());
		if (mc.theWorld.isThundering() || mc.theWorld.isRaining()) {
			mc.theWorld.setRainStrength(this.rain);
			mc.theWorld.setThunderStrength(this.rain);
		}
	}
}
