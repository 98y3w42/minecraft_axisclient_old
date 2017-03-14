package axis.module.modules.player;

import axis.event.Event.State;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class ToggleSneak extends Module {

	public ToggleSneak() {
		super("ToggleSneak", 0x00BFFF, Category.PLAYER);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == State.PRE) {
			mc.gameSettings.keyBindSneak.pressed = true;
		}
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			mc.gameSettings.keyBindSneak.pressed = false;
		}
	}

}