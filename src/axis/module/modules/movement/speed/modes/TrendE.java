package axis.module.modules.movement.speed.modes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import axis.Axis;
import axis.event.events.BoundingBoxEvent;
import axis.event.events.MoveEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import axis.util.Logger;
import axis.util.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class TrendE
		extends SpeedMode {
	public static double yOffset;
	private int stage;
	private double moveSpeed = 1.0F;
	private double moveSpeed1;
	private double lastDist;
	private MovementInput movementInput;
	private TimeHelper timer1 = new TimeHelper();
	private float time = 3.0F;

	public TrendE(Speed speed) {
		super("TrendE", speed);
	}

	Timer1 timer = new Timer1();

	public void onMove(MoveEvent event) {
		if (!mc.thePlayer.onGround) {
			if (mc.gameSettings.keyBindJump.pressed) {
				Timer.timerSpeed = 1.0F;
				return;
			}
			Timer.timerSpeed = 1.0F;
			this.time = 3.0F;
			this.timer.reset();
			this.moveSpeed1 = Math.max(this.moveSpeed1, speed.getBaseMoveSpeed());
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
			final double motionX = forward * this.moveSpeed1 * mx + strafe * this.moveSpeed1 * mz;
			final double motionZ = forward * this.moveSpeed1 * mz - strafe * this.moveSpeed1 * mx;
			// mc.thePlayer.motionX = forward * this.moveSpeed * mx + strafe *
			// this.moveSpeed * mz;
			// mc.thePlayer.motionZ = forward * this.moveSpeed * mz - strafe *
			// this.moveSpeed * mx;
			event.x = motionX;
			event.z = motionZ;
		}
		if (roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == roundToPlace(0.481D, 3)) {
			if (mc.gameSettings.keyBindSneak.isPressed()) {
				mc.thePlayer.motionY = -0.5D;
			}
			if (isAir() || (mc.gameSettings.keyBindJump.pressed)) {
				return;
			}
			mc.thePlayer.motionY -= 0.075D;
			event.setY(-0.075D);
			mc.thePlayer.setSpeedInAir(0.02F);
		} else if (roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == roundToPlace(0.406D, 3)) {
			if (isAir() || (mc.gameSettings.keyBindJump.pressed)) {
				return;
			}
			if (mc.gameSettings.keyBindSneak.isPressed()) {
				mc.thePlayer.motionY = -0.5D;
			}
			mc.thePlayer.motionY = -0.1D;
			event.setY(-0.1D);
			mc.thePlayer.setSpeedInAir(0.02F);
		} else if (roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == roundToPlace(0.306D, 3)) {
			if (isAir() || (mc.gameSettings.keyBindJump.pressed)) {
				return;
			}
			if (mc.gameSettings.keyBindSneak.isPressed()) {
				mc.thePlayer.motionY = -0.5D;
			}
			mc.thePlayer.motionY = -9.0E-6D;
			event.setY(-9.0E-6D);
			mc.thePlayer.setSpeedInAir(0.02F);
		} else if (roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == roundToPlace(0.305D, 3)) {
			this.stage = 0;
		}
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
			this.stage = 2;
			this.moveSpeed = (!mc.thePlayer.isPotionActive(1) ? 6.48D * getBaseMoveSpeed() - 0.01D : 3.5D * getBaseMoveSpeed() - 0.01D);
			Timer.timerSpeed = 1.2F;
		} else if (this.stage == 2) {
			Timer.timerSpeed = 1.2F;
			this.stage = 3;
			mc.thePlayer.motionY = 0.42D;
			event.setY(0.42D);
			this.moveSpeed *= 0.987D;
		} else if (this.stage == 3) {
			this.stage = 4;
			double difference = 0.67D * (this.lastDist - getBaseMoveSpeed());
			Timer.timerSpeed = 1.2F;
			this.moveSpeed = (this.lastDist - difference);
			if (mc.thePlayer.fallDistance > 2.0F) {
				mc.thePlayer.motionY = -0.5D;
			}
		} else {
			if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) || (mc.thePlayer.isCollidedVertically)) {
				if (this.timer.delay(250.0F)) {
					this.stage = 1;
				} else {
					this.stage = 0;
				}
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		if (((this.stage == 0) &&
				(mc.thePlayer.isSprinting())) || (mc.thePlayer.fallDistance > 1.0F)) {
			mc.thePlayer.setSprinting(false);
		}
		setMoveSpeed(event, this.stage != 0 ? (this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed())) : 0.15D);
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.287D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public void onUpdate(UpdateEvent event) {
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}

	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public void setMoveSpeed(MoveEvent event, double speed) {
		double forward = movementInput.moveForward;
		double strafe = movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0D) && (strafe == 0.0D)) {
			event.setX(0.0D);
			event.setZ(0.0D);
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -50 : 50);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 50 : -50);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}
			event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
			event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
		}
	}

	public class Timer1 {
		private long prevMS;

		public Timer1() {
			this.prevMS = 0L;
		}

		public boolean delay(float milliSec) {
			return (float) (getTime() - this.prevMS) >= milliSec;
		}

		public void reset() {
			this.prevMS = getTime();
		}

		public long getTime() {
			return System.nanoTime() / 1000000L;
		}

		public long getDifference() {
			return getTime() - this.prevMS;
		}
	}

	public void onDisabled(){
		super.onDisabled();
		if(mc.thePlayer != null){
		mc.thePlayer.setSpeedInAir(0.02F);
		Timer.timerSpeed = 1.0f;
		}
	}

	public boolean isAir() {
		boolean onIce = false;
		int y = (int) (mc.thePlayer.boundingBox.minY - 1.0D);

		for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
				Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();

				if (block != null && (block instanceof BlockAir)) {
					onIce = true;
				}
			}
		}

		return onIce;
	}
}
