package axis.module.modules.movement.speed.modes;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.Logger;
import axis.util.MathUtils;
import axis.util.TimeHelper;
import axis.util.moveutil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class LowHop extends SpeedMode {

	private int state = 1;
	private double moveSpeed;
	private double lastDist;
	private boolean motionMultiplied;
	private TimeHelper timer = new TimeHelper();
	private boolean ff = false;

	public LowHop(Speed speed) {
		super("LowHop", speed);
	}

	public void onMove(MoveEvent event) {
		if (mc.gameSettings.keyBindJump.isPressed()) {
			return;
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
				forward = 1.5f;
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
			final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
			final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
			if ((mc.gameSettings.keyBindForward.pressed) && (this.timer.hasReached(10.0D)) && (mc.thePlayer.onGround) && (!mc.thePlayer.isCollidedHorizontally)) {
				mc.thePlayer.jump();
				mc.thePlayer.motionY = 0.2D;
				this.timer.reset();
			} else if (!mc.gameSettings.keyBindForward.pressed) {
				mc.timer.timerSpeed = 1.0f;
			}
		}
	}

	public void onEnabled() {

	}

	public void onDisabled() {
		mc.timer.timerSpeed = 1.0f;
	}

}