package axis.module.modules.movement;

import axis.event.Event.State;
import axis.event.events.ItemSpeedEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {

	public NoSlowdown() {
		super("NoSlowdown", -12302521, ModuleManager.Category.MOVEMENT);
		this.setDisplayName("No Slowdown");
	}

	private void onItemUse(ItemSpeedEvent event) {
		event.setCancelled(true);
	}

	public void onUpdate(UpdateEvent event) {
		if ((this.mc.thePlayer.isBlocking()) && ((this.mc.thePlayer.motionX != 0.0D) || (this.mc.thePlayer.motionZ != 0.0D))) {
			if (event.state == State.PRE) {
				this.mc.getNetHandler().getNetworkManager().dispatchPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN), null);
			} else if (event.state == State.POST) {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
			}
		}
	}
}