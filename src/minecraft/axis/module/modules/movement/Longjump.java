package axis.module.modules.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import axis.Axis;
import axis.command.Command;
import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.module.modules.movement.speed.modes.Bhop2;
import axis.module.modules.movement.speed.modes.MineZ;
import axis.util.Logger;
import axis.util.MathUtils;
import axis.value.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class Longjump extends Module {
	private double boost = 3.38D;
	public static double yOffset;
	private int stage;
	private double moveSpeed = 0.2873D;
	private double lastDist;

	public Longjump() {
		super("Longjump", -14308402, ModuleManager.Category.MOVEMENT);
	}

	public void onEnabled() {
		this.lastDist = 0.0D;
		this.stage = 1;
		super.onEnabled();
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	private void onMove(MoveEvent event) {
		if (mc.thePlayer.moveStrafing <= 0.0F && mc.thePlayer.moveForward <= 0.0F) {
			this.stage = 1;
		}

		if (round(mc.thePlayer.posY - (double) ((int) mc.thePlayer.posY), 3) == round(0.943D, 3)) {
			EntityPlayerSP movementInput = mc.thePlayer;
			EntityPlayerSP forward = mc.thePlayer;
			movementInput.motionY -= 0.03D;
			event.setY(event.getY() - 0.03D);
		}

		if (this.stage != 1 || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
			if (this.stage == 2) {
				this.stage = 3;
				mc.thePlayer.motionY = 0.424D;
				event.setY(0.424D);
				this.moveSpeed *= 2.149802D;
			} else if (this.stage == 3) {
				this.stage = 4;
				double movementInput1 = 0.66D * (this.lastDist - this.getBaseMoveSpeed());
				this.moveSpeed = this.lastDist - movementInput1;
			} else {
				if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
					this.stage = 1;
				}

				this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
			}
		} else {
			this.stage = 2;
			this.moveSpeed = this.boost * this.getBaseMoveSpeed() - 0.01D;
		}

		this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
		MovementInput movementInput2 = mc.thePlayer.movementInput;
		float forward1 = movementInput2.moveForward;
		float strafe = movementInput2.moveStrafe;
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

	}

	private void onUpdate(UpdateEvent event) {
		boolean speedy = mc.thePlayer.isPotionActive(Potion.moveSpeed);
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
	}
}