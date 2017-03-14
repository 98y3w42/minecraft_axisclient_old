package axis.module.modules.movement.speed.modes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import axis.Axis;
import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.Step;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.BlockHelper;
import axis.util.LiquidUtils;

public class Other extends SpeedMode {

	private int state = 1;
	private double moveSpeed = 0.2873D;
	private double lastDist;
	private boolean stopMotionUntilNext;
	private boolean spedUp;
	public static int wait;

	public Other(Speed speed) {
		super("Other", speed);
	}

	public void onMove(MoveEvent event) {
		EntityPlayerSP localEntityPlayerSP2;
		if (!allowStep()) {
			if (!mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 1.0F;
				return;
			}
			mc.timer.timerSpeed = 1.0F;
			mc.thePlayer.motionX *= 0.98D;
			localEntityPlayerSP2 = mc.thePlayer;
			localEntityPlayerSP2.motionZ *= 0.98D;
			this.state = 0;
			return;
		}
		EntityPlayerSP localEntityPlayerSP1;
		switch (this.state) {
		case 1:
			mc.timer.timerSpeed = 1.0F;
			mc.thePlayer.motionX /= 3.6D;
			localEntityPlayerSP2 = mc.thePlayer;
			localEntityPlayerSP2.motionZ /= 3.6D;
			break;
		case 2:
			if (!mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 1.0F;
				return;
			}
			double d3 = 5.45D;
			for (Object localObject2 = mc.thePlayer.getActivePotionEffects().iterator(); ((Iterator) localObject2).hasNext();) {
				Object localObject3 = (PotionEffect) ((Iterator) localObject2).next();
				if (Potion.potionTypes[((PotionEffect) localObject3).getPotionID()].getId() == Potion.moveSpeed.getId()) {
					if (((PotionEffect) localObject3).getAmplifier() == 0) {
						d3 = 4.7D;
					} else if (((PotionEffect) localObject3).getAmplifier() == 1) {
						d3 = 3.7D;
					} else if (((PotionEffect) localObject3).getAmplifier() == 2) {
						d3 = 2.7D;
					} else if (((PotionEffect) localObject3).getAmplifier() >= 3) {
						d3 = 1.7D;
					}
				}
			}
			if (mc.thePlayer.movementInput.moveStrafe != 0.0F) {
				d3 -= 0.1D;
			}
			mc.timer.timerSpeed = (mc.thePlayer.getHealth() == mc.thePlayer.getMaxHealth() ? 1.3F : 1.0F);
			mc.timer.timerSpeed = 1.5F;
			mc.thePlayer.motionX *= d3;
			Object localObject3 = mc.thePlayer;
			mc.thePlayer.motionZ *= d3;
			break;
		case 3:
			mc.timer.timerSpeed = 1.0F;
			localEntityPlayerSP1 = mc.thePlayer;
			localEntityPlayerSP1.motionX /= 1.3D;
			localEntityPlayerSP2 = mc.thePlayer;
			localEntityPlayerSP2.motionZ /= 1.3D;
			break;
		case 4:
			this.state = 0;
			break;
		default:
			if (!mc.thePlayer.onGround) {
				mc.timer.timerSpeed = 1.0F;
				return;
			}
			mc.timer.timerSpeed = 1.0F;
			localEntityPlayerSP1 = mc.thePlayer;
			localEntityPlayerSP1.motionX *= 0.98D;
			localEntityPlayerSP2 = mc.thePlayer;
			localEntityPlayerSP2.motionZ *= 0.98D;
			break;
		}
		this.state += 1;
	}

	public void onUpdate(UpdateEvent event) {

	}

	public boolean allowStep() {
		Step localStep = (Step) Axis.getModuleManager().getModuleByName("step");
		int i = (mc.thePlayer.movementInput.moveForward != 0.0F) || (mc.thePlayer.movementInput.moveStrafe != 0.0F) ? 1 : 0;
		return (!mc.thePlayer.isInWater()) && (!BlockHelper.isInLiquid()) && (!BlockHelper.isOnLiquid()) && (!mc.thePlayer.isCollidedHorizontally) && (!BlockHelper.isOnIce()) && (!BlockHelper.isOnLadder())
				&& (!mc.thePlayer.isSneaking()) && (mc.thePlayer.onGround) && (i != 0);
	}

	private double getBaseMoveSpeed() {
		double d = 0.2872D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int i = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			d *= (1.0D + 0.2D * (i + 1));
		}
		return d;
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			net.minecraft.util.Timer.timerSpeed = 1.0F;
		}
	}

	public void onEnabled() {
		super.onEnabled();
		this.moveSpeed = (mc.thePlayer == null ? 0.2873D : getBaseMoveSpeed());
		super.onEnabled();
	}

	boolean shouldSpeedUp() {
		int i = (mc.thePlayer.movementInput.moveForward != 0.0F) || (mc.thePlayer.movementInput.moveStrafe != 0.0F) ? 1 : 0;
		return (!mc.thePlayer.isInWater()) && (!BlockHelper.isInLiquid()) && (!BlockHelper.isOnLiquid()) && (!mc.thePlayer.isCollidedHorizontally) && (!BlockHelper.isOnIce()) && (!BlockHelper.isOnLadder())
				&& (!mc.thePlayer.isSneaking()) && (mc.thePlayer.onGround) && (i != 0);
	}

	private int getMultiplier() {
		int i = MathHelper.floor_double(mc.thePlayer.posX);
		int j = MathHelper.floor_double(mc.thePlayer.posZ);
		int k = MathHelper.floor_double(mc.thePlayer.posY) - 2;
		if (mc.theWorld.getBlock(i, k, j).slipperiness > 0.7D) {
			return 2;
		}
		if (mc.thePlayer.onGround) {
			return 1;
		}
		return 0;
	}
}
