package axis.module.modules.movement.speed.modes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import axis.util.MathUtils;
import axis.util.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;

public class SlowHop extends SpeedMode implements Mode {
	private boolean forward;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private int state = 1;

	public SlowHop() {
		super("SlowHop");
	}

	public void onMove(MoveEvent event) {
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			this.state = 1;
			this.moveSpeed = speed.getBaseMoveSpeed();
			return;
		}
		mc.thePlayer.isAirBorne = true;
		if (!(mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed)) {
			this.state = 1;
		}

		if (this.round(mc.thePlayer.posY - (double) ((int) mc.thePlayer.posY), 3) == this.round(0.138D, 3)) {
			this.mc.thePlayer.motionY -= 0.08;
			event.y -= 0.09316090325960147;
			this.mc.thePlayer.posY -= 0.09316090325960147;
		}

		if (this.state == 1) {
			this.state = 2;
		} else if (this.state == 2) {
			this.state = 3;
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0, this.mc.thePlayer.motionY, 0)).size() < 1) {
				return;
			}
			event.setY(0.4D);
			mc.thePlayer.motionY = 0.4F;
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX, this.mc.thePlayer.motionY, this.mc.thePlayer.motionZ)).size() > 0) {
				return;
			}
			event.y = 0.4;
			this.moveSpeed *= 1.4;
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
		mc.thePlayer.motionX *= 1.1085D;
		mc.thePlayer.motionZ *= 1.1085D;
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
		if (mc.thePlayer != null) {
			this.stage = 1;
			mc.thePlayer.onGround = true;
			this.moveSpeed = speed.getBaseMoveSpeed();
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			mc.thePlayer.motionX = 0.0D;
			mc.thePlayer.motionZ = 0.0D;
			mc.thePlayer.setSpeedInAir(0.002F);
		}
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