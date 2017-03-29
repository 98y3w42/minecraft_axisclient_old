package axis.module.modules.movement.speed.modes;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import axis.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class SlowHop extends SpeedMode implements Mode {

	private int state = 1;
	private double moveSpeed;
	private double lastDist;
	private int stage;
	private boolean test = false;

	public SlowHop() {
		super("SlowHop");
	}

	public void onMove(MoveEvent event) {
		net.minecraft.util.Timer.timerSpeed = 1.0F;
		if (LiquidUtils.isInLiquid() || mc.gameSettings.keyBindJump.isKeyDown()) {
			return;
		}
		if (MathUtils.roundToPlace(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.138D, 3)) {
			EntityPlayerSP thePlayer = mc.thePlayer;
			thePlayer.motionY -= 0.08D;
			event.y -= 0.09316090325960147D;
			EntityPlayerSP thePlayer2 = mc.thePlayer;
			thePlayer2.posY -= 0.09316090325960147D;
		}
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
			this.stage = 2;
			this.moveSpeed = (1.35D * speed.getBaseMoveSpeed() - 0.01D);
		} else if (this.stage == 2) {
			this.stage = 3;
			mc.thePlayer.motionY = 0.5D;
			event.y = 0.5D;
			this.moveSpeed = speed.getBaseMoveSpeed() * 1.9D;
		} else if (this.stage == 3) {
			this.stage = 4;
			double difference = 0.66D * (this.lastDist - speed.getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
		} else {
			if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) || (mc.thePlayer.isCollidedVertically)) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		event.setY(event.getY() * 0.7);
		if (this.test) {
			this.test = false;
			this.moveSpeed *= 0.75D;
		} else {
			this.test = true;
		}
		this.moveSpeed = Math.max(this.moveSpeed, speed.getBaseMoveSpeed());
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F) {
				forward = 1.0F;
			} else if (forward < 0.0F) {
				forward = -1.0F;
			}
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
		double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
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
