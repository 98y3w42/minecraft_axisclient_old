package axis.module.modules.movement.speed.modes;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class Bhop extends SpeedMode implements Mode {

	private int state = 1;
	private double moveSpeed;
	private double lastDist;

	public Bhop() {
		super("Bhop");
	}

	public void onMove(MoveEvent event) {
		mc.thePlayer.isAirBorne = true;
		if (!(mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed)) {
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
				this.state = 1;
			}
		}

		if (MathUtils.round(this.mc.thePlayer.posY - (double) (int) this.mc.thePlayer.posY, 3) == MathUtils.round(0.138, 3)) {
			this.mc.thePlayer.motionY -= 0.08;
			event.y -= 0.09316090325960147;
			this.mc.thePlayer.posY -= 0.09316090325960147;
		}

		if (this.state == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
			this.state = 2;
			this.moveSpeed = 1.35 * speed.getBaseMoveSpeed() - 0.01;
		} else if (this.state == 2) {
			this.state = 3;
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0, this.mc.thePlayer.motionY, 0)).size() < 1) {
				return;
			}
			event.setY(0.4D);
			this.mc.thePlayer.motionY = 0.4;
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX, this.mc.thePlayer.motionY, this.mc.thePlayer.motionZ)).size() > 0) {
				return;
			}
			event.y = 0.4;
			this.moveSpeed *= 2.149;
		} else if (this.state == 3) {
			this.state = 4;
			final double difference = 0.66 * (this.lastDist - speed.getBaseMoveSpeed());
			this.moveSpeed = this.lastDist - difference;
		} else {
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
				this.state = 1;
			}
			this.moveSpeed = this.lastDist - this.lastDist / 159.0;
		}

		this.moveSpeed = Math.max(this.moveSpeed, speed.getBaseMoveSpeed());
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
		// mc.thePlayer.motionX = forward * this.moveSpeed * mx + strafe *
		// this.moveSpeed * mz;
		// mc.thePlayer.motionZ = forward * this.moveSpeed * mz - strafe *
		// this.moveSpeed * mx;
		event.x = motionX;
		event.z = motionZ;
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
			final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		}
	}

	public void onEnabled() {

	}

	public void onDisabled() {
		mc.timer.timerSpeed = 1.0f;
	}

}
