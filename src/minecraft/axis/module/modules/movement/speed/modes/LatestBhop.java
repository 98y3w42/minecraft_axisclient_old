package axis.module.modules.movement.speed.modes;

import axis.Axis;
import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;

public class LatestBhop extends SpeedMode implements Mode {

	private int state = 1;
	private double moveSpeed;
	private double lastDist;
	private boolean changedtimer = false;

	public LatestBhop() {
		super("LatestBhop");
	}

	public void onMove(MoveEvent event) {
		if (!(mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed)) {
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
				this.state = 1;
			}
		}

		if (MathUtils.round(this.mc.thePlayer.posY - (double)(int)this.mc.thePlayer.posY, 3) == MathUtils.round(0.138, 3)) {
			final EntityPlayerSP thePlayer = this.mc.thePlayer;
			thePlayer.motionY -= 0.08;
			event.y -= 0.09316090325960147;
			final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
			thePlayer2.posY -= 0.09316090325960147;
		}

		if (this.state == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
				this.state = 2;
				mc.timer.timerSpeed = 1.0f;
				this.moveSpeed = 2.3 * speed.getBaseMoveSpeed() - 0.01;
		} else if (this.state == 2) {
				this.state = 3;
				if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0, this.mc.thePlayer.motionY, 0)).size() < 1) {
					return;
				}
				this.mc.thePlayer.motionY = 0.4;
				if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX, this.mc.thePlayer.motionY, this.mc.thePlayer.motionZ)).size() > 0) {
					return;
				}
				mc.timer.timerSpeed = 1.0f;
				this.mc.thePlayer.motionY = 0.0;
				event.y = 0.4;
				this.moveSpeed *= 2.149;
		} else if (this.state == 3) {
				mc.timer.timerSpeed = 1.0f;
				this.state = 4;
				event.y -= 0.15;
				final double difference = 0.66 * (this.lastDist - speed.getBaseMoveSpeed());
				this.moveSpeed = this.lastDist - difference;
		} else {
			mc.timer.timerSpeed = 1.0f;
			if ((this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f))
				mc.timer.timerSpeed = 1.6f;
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
				event.y -= 0.15;
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
		}
		else if (forward != 0.0f) {
			if (strafe >= 1.0f) {
				yaw += ((forward > 0.0f) ? -45 : 45);
				strafe = 0.0f;
			}
			else if (strafe <= -1.0f) {
				yaw += ((forward > 0.0f) ? 45 : -45);
				strafe = 0.0f;
			}
			if (forward > 0.0f) {
				forward = 1.0f;
			}
			else if (forward < 0.0f) {
				forward = -1.0f;
			}
		}
		final double mx = Math.cos(Math.toRadians((double)(yaw + 90.0f)));
		final double mz = Math.sin(Math.toRadians((double)(yaw + 90.0f)));
		final double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
		final double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
		//mc.thePlayer.motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
		//mc.thePlayer.motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
		event.x = motionX;
		event.z = motionZ;
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
			final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
			if(!Axis.getAxis().getModuleManager().getModuleByName("fly").isEnabled() && !mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0D && !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -1.0D, 0.0D)).isEmpty()) {
				mc.thePlayer.motionY -= 1.0f;
            }
			if (changedtimer) {
				mc.timer.timerSpeed = 1.0f;
				changedtimer = false;
			}
			if (mc.timer.timerSpeed != 1.0f) {
				changedtimer  = true;
			}
		}
	}

	public void onEnabled() {

	}

	public void onDisabled() {
		mc.timer.timerSpeed = 1.0f;
	}

}
