package axis.module.modules.movement.speed.modes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.Logger;
import axis.util.MathUtils;
import axis.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Bhop2 extends SpeedMode {

	private boolean forward;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private Random random = new Random();
	private TimeHelper time = new TimeHelper();
	private boolean fast = false;

	public Bhop2(Speed speed) {
		super("Bhop2", speed);
	}

	public void onMove(MoveEvent event) {
		if (mc.thePlayer.capabilities.isFlying) {
			return;
		}

		if (mc.gameSettings.keyBindForward.isPressed()) {
			this.forward = false;
		}
		if (mc.thePlayer.onGround) {
			this.stage = 2;
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			this.moveSpeed = speed.getBaseMoveSpeed();
		}

		if (this.round(mc.thePlayer.posY - (double) ((int) mc.thePlayer.posY), 3) == this.round(0.138D, 3)) {
			--mc.thePlayer.motionY;
			net.minecraft.util.Timer.timerSpeed = 1.1F;
			event.setY(event.getY() - 0.0931D);
			mc.thePlayer.posY -= 0.0931D;
			mc.thePlayer.motionX *= 1.5085D;
			mc.thePlayer.motionZ *= 1.5085D;
		}

		if (this.stage != 2 || (mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F)) {
			if (this.stage == 3) {
				double forward = 0.66D * (this.lastDist - speed.getBaseMoveSpeed());
				this.moveSpeed = this.lastDist - forward;
				this.forward = true;
			} else {
				if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
					if (mc.gameSettings.keyBindForward.isKeyDown()) {
						this.fast = true;
					}
					mc.thePlayer.motionX *= 1.9085D;
					mc.thePlayer.motionZ *= 1.9085D;
				}

				this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
			}
		} else {
			this.fast = false;
			event.setY(mc.thePlayer.motionY = 0.39936D);
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			mc.thePlayer.motionX *= 1.6585D;
			mc.thePlayer.motionZ *= 1.6585D;
			this.moveSpeed *= 1.68999D + (float) (random.nextInt(2)) / 10;
		}
		if (this.fast) {
			this.moveSpeed *= 1.1D;
			Timer.timerSpeed = 1.15F;
		}
		if (this.moveSpeed >= (speed.getBaseMoveSpeed() * 2)) {
			Logger.logChat("Over");
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			this.moveSpeed = speed.getBaseMoveSpeed();
		}

		this.moveSpeed = Math.max(this.moveSpeed, speed.getBaseMoveSpeed());
		final MovementInput movementInput = this.mc.thePlayer.movementInput;
		float forward1 = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if (forward1 == 0.0F && strafe == 0.0F) {
			event.setX(0.0D);
			event.setZ(0.0D);
		} else if (forward1 != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (float) (forward1 > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (float) (forward1 > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}

			if (forward1 > 0.0F) {
				forward1 = 1.0F;
			} else if (forward1 < 0.0F) {
				forward1 = -1.0F;
			}
		}

		double mx = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
		double mz = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
		event.setX((double) forward1 * this.moveSpeed * mx + (double) strafe * this.moveSpeed * mz);
		event.setZ((double) forward1 * this.moveSpeed * mz - (double) strafe * this.moveSpeed * mx);
		mc.thePlayer.stepHeight = 0.6F;
		if (forward1 == 0.0F && strafe == 0.0F) {
			event.setX(0.0D);
			event.setZ(0.0D);
		} else if (forward1 != 0.0F) {
			float var10000;
			if (strafe >= 1.0F) {
				var10000 = yaw + (float) (forward1 > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				var10000 = yaw + (float) (forward1 > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}

			if (forward1 > 0.0F) {
				forward1 = 1.0F;
			} else if (forward1 < 0.0F) {
				forward1 = -1.0F;
			}
		}

		++this.stage;
		mc.thePlayer.motionX = 0.0D;
		mc.thePlayer.motionZ = 0.0D;
	}

	public void onUpdate(UpdateEvent event) {
		double movementInput;
		movementInput = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double strafe = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(movementInput * movementInput + strafe * strafe);
	}

	public void onEnabled() {
	}

	public void onDisabled() {
		if (mc.thePlayer != null) {
			this.moveSpeed = speed.getBaseMoveSpeed();
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			mc.thePlayer.motionX = 0.0D;
			mc.thePlayer.motionZ = 0.0D;
			mc.thePlayer.setSpeedInAir(0.002F);
			this.forward = true;
		}
	}

	private static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
	}

}