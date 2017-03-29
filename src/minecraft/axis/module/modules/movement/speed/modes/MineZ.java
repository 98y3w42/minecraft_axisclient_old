package axis.module.modules.movement.speed.modes;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class MineZ extends SpeedMode implements Mode {

	private int stage = 1;
	private double moveSpeed = 0.2873D;
	private double lastDist;
	private boolean stopMotionUntilNext;
	private boolean spedUp;
	private int fa = 0;
	public static int wait;

	public MineZ() {
		super("MineZ");
	}

	public void onMove(MoveEvent event) {
		if (wait != 0) {
			Timer.timerSpeed = 1.0F;
			Speed.canStep = true;
			this.stage = 1;
			wait--;
			return;
		}
		if (mc.gameSettings.keyBindJump.isKeyDown() || !(mc.thePlayer.onGround)) {
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			return;
		}
		Speed.canStep = true;
		this.mc.thePlayer.stepHeight = 0.6F;

		MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
		} else {
			boolean collideCheck = false;
			if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
				collideCheck = true;
			}
			if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			switch (this.stage) {
			case 1:
				if (mc.thePlayer.getItemInUseCount() == 0) {
					net.minecraft.util.Timer.timerSpeed = 1.35F;
				} else {
					net.minecraft.util.Timer.timerSpeed = 1.0F;
				}
				this.moveSpeed = 0.5799D;
				break;
			case 2:
				net.minecraft.util.Timer.timerSpeed = 1.0F;
				this.moveSpeed = 0.66787F;
				break;
			default:
				this.moveSpeed = speed.getBaseMoveSpeed();
			}
			if (collideCheck) {
				Timer.timerSpeed = 1.0f;
				Speed.canStep = true;
			}
			if (this.mc.thePlayer.isSneaking() || (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f) || this.mc.gameSettings.keyBindJump.isPressed() || LiquidUtils.isOnLiquid()
					|| LiquidUtils.isInLiquid()) {
				Timer.timerSpeed = 1.0f;
				this.moveSpeed = speed.getBaseMoveSpeed();
				Speed.canStep = true;
				this.stage = 1;
				return;
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
			double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;

			event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
			event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
		}
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			switch (this.stage) {
			case 1:
				event.y += 1.0E-4D;
				this.stage += 1;
				break;
			case 2:
				event.y += 2.0E-4D;
				this.stage += 1;
				break;
			default:
				this.stage = 1;
				if ((!this.mc.thePlayer.isSneaking()) && ((this.mc.thePlayer.moveForward != 0.0F) || (this.mc.thePlayer.moveStrafing != 0.0F)) && (!this.mc.gameSettings.keyBindJump.isPressed()) && (!LiquidUtils.isOnLiquid())
						&& (!LiquidUtils.isInLiquid())) {
					this.stage = 1;
				} else {
					this.moveSpeed = speed.getBaseMoveSpeed();
				}
				Speed.yOffset = event.y - this.mc.thePlayer.posY;
			}
		}
	}

	public void onEnabled() {
	}

	public void onDisabled() {
		net.minecraft.util.Timer.timerSpeed = 1.0F;
	}

}
