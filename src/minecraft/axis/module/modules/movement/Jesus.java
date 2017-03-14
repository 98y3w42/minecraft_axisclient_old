package axis.module.modules.movement;

import axis.Axis;
import axis.event.Event;
import axis.event.Event.State;
import axis.event.events.BoundingBoxEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.LiquidUtils;
import axis.util.Logger;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class Jesus extends Module {

	public Jesus() {
		super("Jesus", -8355712, ModuleManager.Category.MOVEMENT);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			if (mc.thePlayer.movementInput.sneak) {
				return;
			}

			if (LiquidUtils.isInLiquid() && mc.thePlayer.isInsideOfMaterial(Material.air)) {
				mc.thePlayer.motionY = 1.11111112E8D;
				mc.thePlayer.onGround = true;
				mc.thePlayer.motionY = 0.03799999877810478D;
				mc.thePlayer.fallDistance = 0.42F;
				net.minecraft.util.Timer.timerSpeed = 10.0F;
				net.minecraft.util.Timer.timerSpeed = 1.0F;
			}
		}
	}
}