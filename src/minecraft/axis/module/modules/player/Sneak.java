package axis.module.modules.player;

import axis.Axis;
import axis.event.Event.State;
import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sneak extends Module {

	public Sneak() {
		super("Sneak", 0x00BFFF, Category.PLAYER);
	}

	public void onUpdate(UpdateEvent event) {
		if (Axis.getAxis().getModuleManager().getModuleByName("Glide").isEnabled()) {
			return;
		}
		if (event.state == State.PRE) {
			if ((this.mc.thePlayer.isSneaking()) || ((this.mc.thePlayer.movementInput.moveForward == 0.0F) && (this.mc.thePlayer.movementInput.moveStrafe == 0.0F))) {
				return;
			}
			this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}

		if (event.state == State.POST) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
		}
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
		}
	}
}
