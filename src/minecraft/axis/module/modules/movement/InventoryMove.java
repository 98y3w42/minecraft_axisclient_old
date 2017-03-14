package axis.module.modules.movement;

import org.lwjgl.input.Keyboard;

import axis.Axis;
import axis.event.Event.State;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove extends Module {

	public InventoryMove() {
		super("InventoryMove", -3308225, ModuleManager.Category.MOVEMENT);
		this.setDisplayName("I Move");
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == State.PRE) {
			if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
				KeyBinding[] moveKeys = new KeyBinding[] { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump };
				KeyBinding[] array = moveKeys;
				int length = moveKeys.length;
				if (Axis.getModuleManager().getModuleByName("Sprint").isEnabled() && (mc.gameSettings.keyBindForward.isPressed())) {
					mc.thePlayer.setSprinting(true);
				}

				for (int i = 0; i < length; ++i) {
					if (Axis.getModuleManager().getModuleByName("Sprint").isEnabled() && (mc.gameSettings.keyBindForward.isPressed())) {
						mc.thePlayer.setSprinting(true);
					}
					KeyBinding bind = array[i];
					KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
				}
			}
		}
	}
}
