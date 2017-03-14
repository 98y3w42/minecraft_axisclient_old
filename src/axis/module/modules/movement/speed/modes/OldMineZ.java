package axis.module.modules.movement.speed.modes;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class OldMineZ extends SpeedMode {

	private int state = 1;
	private double moveSpeed = 0.2873D;
	private double lastDist;
	private boolean stopMotionUntilNext;
	private boolean spedUp;
	public static int wait;

	public OldMineZ(Speed speed) {
		super("OldMineZ", speed);
	}

	public void onMove(MoveEvent event) {
		if (wait != 0) {
			Speed.canStep = true;
			this.state = 1;
			wait--;
			return;
		}

		Speed.canStep = false;
		final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward == 0.0f && strafe == 0.0f || !mc.thePlayer.onGround || mc.thePlayer.fallDistance != 0.0f) {
			wait = 5;
			Speed.canStep = false;
			// event.x = 0.0;
			// event.z = 0.0;
			return;
		}
		boolean collideCheck = false;
		if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
			collideCheck = true;
		}
		if (forward != 0.0f) {
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
		Timer.timerSpeed = 1.0f;
		switch (this.state) {
		case 1:
			this.moveSpeed = 0.579;
			break;
		case 2:
			this.moveSpeed = 0.66781;
			break;
		default:
			this.moveSpeed = speed.getBaseMoveSpeed();
			break;
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
			this.state = 1;
			return;
		}
		this.moveSpeed = Math.max(this.moveSpeed, speed.getBaseMoveSpeed());
		final double mx = Math.cos(Math.toRadians((double) (yaw + 90.0f)));
		final double mz = Math.sin(Math.toRadians((double) (yaw + 90.0f)));

		event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
		event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
		// mc.thePlayer.motionX = forward * this.moveSpeed * mx + strafe *
		// this.moveSpeed * mz;
		// mc.thePlayer.motionZ = forward * this.moveSpeed * mz - strafe *
		// this.moveSpeed * mx;

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
				this.moveSpeed = speed.getBaseMoveSpeed();
				break;
			}
			Speed.yOffset = event.y - this.mc.thePlayer.posY;
		}
	}

	public void onEnabled() {
	}

	public void onDisabled() {

	}

}
