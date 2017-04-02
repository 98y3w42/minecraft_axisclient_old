package axis.module.modules.movement;

import axis.Axis;
import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.PacketSentEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.LiquidUtils;
import axis.util.Logger;
import axis.util.MathUtils;
import axis.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class Glide extends Module {

	private TimeHelper time = new TimeHelper();
	private int state = 1;
	private double moveSpeed;
	private Speed speed;

	public Glide() {
		super("Glide", 0, ModuleManager.Category.MOVEMENT);
	}

	public void onEnabled() {
		if (mc.thePlayer != null) {
			mc.thePlayer.fallDistance = 0.0F;
		}
		super.onEnabled();
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			mc.thePlayer.motionY = 0.0D;
		}
	}

	public void onMove(MoveEvent event) {
		if ((mc.thePlayer.isInWater()) || (mc.thePlayer.isOnLadder())) {
			return;
		}
		event.setY(0.0F);
		if (this.mc.thePlayer.movementInput.sneak) {
			event.setY(-0.8F);
		}
		mc.thePlayer.isAirBorne = true;

		this.moveSpeed = 0.2872D;
		final MovementInput movementInput = this.mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward == 0.0f && strafe == 0.0f) {
			event.x = 0.0;
			event.z = 0.0;
		} else if (forward != 0.0f) {
			if (strafe >= 1.0f) {
				yaw += ((forward > 0.0f) ? -45 : 45);
				strafe = 0.0f;
			} else if (strafe <= -1.0f) {
				yaw += ((forward > 0.0f) ? 45 : -45);
				strafe = 0.0f;
			}
			if (forward > 0.0f) {
				forward = 1.0f;
			} else if (forward < 0.0f) {
				forward = -1.0f;
			}
		}
		final double mx = Math.cos(Math.toRadians((double) (yaw + 90.0f)));
		final double mz = Math.sin(Math.toRadians((double) (yaw + 90.0f)));
		final double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
		final double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
		event.x = motionX;
		event.z = motionZ;
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			Speed.canStep = true;
			if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f || !mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0.0f) {
				if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0.0f) {
					Speed.canStep = false;
				}
				return;
			}
			switch (this.state) {
			case 1:
				Speed.canStep = false;
				event.y += 0.0001;
				++this.state;
				break;
			case 2:
				Speed.canStep = false;
				event.y += 0.0002;
				++this.state;
				break;
			default:
				this.state = 1;
				if (!this.mc.thePlayer.isSneaking() && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && !this.mc.gameSettings.keyBindJump.isPressed() && !LiquidUtils.isOnLiquid()
						&& !LiquidUtils.isInLiquid()) {
					Speed.canStep = true;
					this.state = 1;
					break;
				}
				break;
			}
			Speed.yOffset = event.y - this.mc.thePlayer.posY;
		}
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2872D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}
}
