package axis.module.modules.movement;

import axis.Axis;
import axis.event.Event;
import axis.event.Event.State;
import axis.event.events.BoundingBoxEvent;
import axis.event.events.MoveEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.LiquidUtils;
import axis.util.Logger;
import axis.util.MathUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Jesus extends Module {

	private int stage = 1;
	private double moveSpeed = 0.2873D;
	private Speed speed;

	public Jesus() {
		super("Jesus", -8355712, ModuleManager.Category.MOVEMENT);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			if (mc.thePlayer.movementInput.sneak) {
				net.minecraft.util.Timer.timerSpeed = 1.0F;
				return;
			}
			if (mc.gameSettings.keyBindJump.isPressed()) {
				net.minecraft.util.Timer.timerSpeed = 1.0F;
			}
			boolean water = false;

			if (LiquidUtils.isInLiquid() && mc.thePlayer.isInsideOfMaterial(Material.air)) {
				mc.thePlayer.onGround = true;
				mc.thePlayer.motionY = 0.03799999877810478D;
				mc.thePlayer.fallDistance = 0.29F;
				mc.thePlayer.cameraPitch = 0.0F;

				switch (this.stage) {
				case 1:
					event.y += 1.0E-4D;
					this.stage += 1;
					if (mc.thePlayer.getItemInUseCount() == 0) {
						net.minecraft.util.Timer.timerSpeed = 1.5F;
						mc.thePlayer.motionX *= 1.1F;
						mc.thePlayer.motionZ *= 1.1F;
					}
					break;
				case 2:
					event.y += 2.0E-4D;
					this.stage += 1;
					net.minecraft.util.Timer.timerSpeed = 1.0F;
					if (mc.thePlayer.getItemInUseCount() == 0) {
						mc.thePlayer.motionX *= 0.95F;
						mc.thePlayer.motionZ *= 0.95F;
					}
					break;
				default:
					this.stage = 1;
					Speed.yOffset = event.y - this.mc.thePlayer.posY;
					break;
				}
			} else {
				net.minecraft.util.Timer.timerSpeed = 1.0F;
			}

			if (!mc.thePlayer.isInsideOfMaterial(Material.air)) {
				net.minecraft.util.Timer.timerSpeed = 1.0F;
			}
		}
	}

	public void onEnabled() {
		super.onEnabled();
		this.stage = 1;
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			Timer.timerSpeed = 1.0F;
		}
	}

}