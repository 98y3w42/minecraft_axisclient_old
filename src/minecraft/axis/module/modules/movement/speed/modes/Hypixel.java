package axis.module.modules.movement.speed.modes;

import java.math.BigDecimal;
import java.math.RoundingMode;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.MathUtils;
import axis.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class Hypixel extends SpeedMode {

	private int stage;
	private int groundTicks;
	private double moveSpeed;
	private double lastDist;
	private boolean canHop;
	private Speed speed;
	private TimeHelper cock = new TimeHelper();
	private boolean speedbool;
	private boolean glide;

	public Hypixel(Speed speed) {
		super("Hypixel", speed);
	}

	public void onMove(MoveEvent event) {
		if (this.speedbool) {
			if (mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 1.0f;
				this.cock.reset();
			}
			if (roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == roundToPlace(0.41D, 3)) {
				mc.thePlayer.motionY = 0.0D;
			}
			if ((mc.thePlayer.moveStrafing <= 0.0F) && (mc.thePlayer.moveForward <= 0.0F)) {
				this.stage = 1;
			}
			if (round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == round(0.943D, 3)) {
				mc.thePlayer.motionY = 0.0D;
			}
			if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
				this.stage = 2;
				moveSpeed = 4.515D * speed.getBaseMoveSpeed() - 0.01D;
			} else if (this.stage == 2) {
				this.stage = 3;
				event.y = 0.424D;
				moveSpeed *= 2.149802D;
			} else if (this.stage == 3) {
				this.stage = 4;
				mc.timer.timerSpeed = 2.0f;
				double difference = 0.66D * (this.lastDist - speed.getBaseMoveSpeed());
				moveSpeed = this.lastDist - difference;
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) || (mc.thePlayer.isCollidedVertically)) {
					this.stage = 1;
				}
				moveSpeed = this.lastDist - this.lastDist / 159.0D;
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
			event.x = motionX;
			event.z = motionZ;
		}
		if (mc.thePlayer.onGround) {
			this.groundTicks += 1;
		} else if ((!mc.thePlayer.onGround) && (this.groundTicks != 0)) {
			this.groundTicks -= 1;
		}
		if (this.cock.hasReached(35L)) {
			this.glide = true;
		}
		this.cock.hasReached(2200L);
		if (this.cock.hasReached(2490L)) {
			this.glide = false;
			this.speedbool = false;
			mc.thePlayer.motionX *= 0.0D;
			mc.thePlayer.motionZ *= 0.0D;
		}
		if (this.cock.hasReached(2820L)) {
			this.speedbool = true;
			mc.thePlayer.motionX *= 0.0D;
			mc.thePlayer.motionZ *= 0.0D;
			this.cock.reset();
		}
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			double xDist4 = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist4 = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist4 * xDist4 + zDist4 * zDist4);
			if (this.glide) {
				mc.thePlayer.motionY = -0.005D;
			}
		}
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public void onEnabled() {
		this.glide = false;
		this.cock.reset();
		this.speedbool = true;
		this.canHop = false;
	}

	public void onDisabled() {
		this.glide = false;
		moveSpeed = speed.getBaseMoveSpeed();
		this.lastDist = 0.0D;
		this.stage = 4;
		mc.timer.timerSpeed = 1.0F;
	}

}
