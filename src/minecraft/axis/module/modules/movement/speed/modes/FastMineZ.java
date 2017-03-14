package axis.module.modules.movement.speed.modes;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class FastMineZ extends SpeedMode {

	private int state = 1;
	private double moveSpeed = 0.2873D;
	private double lastDist;
	private boolean stopMotionUntilNext;
	private boolean spedUp;
	public static int wait;

	public FastMineZ(Speed speed) {
		super("FastMineZ", speed);
	}

	public void onMove(MoveEvent event) {
		if (wait != 0) {
			Speed.canStep = true;
			this.state = 1;
			wait -= 1;
			return;
		}
		Speed.canStep = false;
		MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (((forward == 0.0F) && (strafe == 0.0F)) || (!this.mc.thePlayer.onGround) || (this.mc.thePlayer.fallDistance != 0.0F)) {
			wait = 5;
			Speed.canStep = false;

			return;
		}
		boolean collideCheck = false;
		if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5D, 0.0D, 0.5D)).size() > 0) {
			collideCheck = true;
		}
		if (forward != 0.0F) {
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
		net.minecraft.util.Timer.timerSpeed = 1.0F;
		switch (this.state) {
		case 1:
			this.moveSpeed = 0.579D;
			break;
		case 2:
			this.moveSpeed = 0.66781D;
			break;
		default:
			this.moveSpeed = this.speed.getBaseMoveSpeed();
		}
		if (collideCheck) {
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			Speed.canStep = true;
		}
		if ((this.mc.thePlayer.isSneaking()) || ((this.mc.thePlayer.moveForward == 0.0F) && (this.mc.thePlayer.moveStrafing == 0.0F)) || (this.mc.gameSettings.keyBindJump.isPressed()) || (LiquidUtils.isOnLiquid())
				|| (LiquidUtils.isInLiquid())) {
			net.minecraft.util.Timer.timerSpeed = 1.0F;
			this.moveSpeed = this.speed.getBaseMoveSpeed();
			Speed.canStep = true;
			this.state = 1;
			return;
		}
		this.moveSpeed = Math.max(this.moveSpeed, this.speed.getBaseMoveSpeed());
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));

		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			Speed.canStep = true;
			if (((this.mc.thePlayer.moveForward == 0.0F) && (this.mc.thePlayer.moveStrafing == 0.0F)) || ((!this.mc.thePlayer.onGround) && (this.mc.thePlayer.fallDistance != 0.0F))) {
				if ((!this.mc.thePlayer.onGround) && (this.mc.thePlayer.fallDistance != 0.0F)) {
					Speed.canStep = false;
				}
				return;
			}
			switch (this.state) {
			case 1:
				Speed.canStep = false;
				event.y += 1.0E-4D;
				this.state += 1;
				break;
			case 2:
				Speed.canStep = false;
				event.y += 2.0E-4D;
				this.state += 1;
				break;
			default:
				this.state = 1;
				if ((!this.mc.thePlayer.isSneaking()) && ((this.mc.thePlayer.moveForward != 0.0F) || (this.mc.thePlayer.moveStrafing != 0.0F)) && (!this.mc.gameSettings.keyBindJump.isPressed()) && (!LiquidUtils.isOnLiquid())
						&& (!LiquidUtils.isInLiquid())) {
					Speed.canStep = true;
					this.state = 1;
				} else {
					this.moveSpeed = this.speed.getBaseMoveSpeed();
				}
				break;
			}
			Speed.yOffset = event.y - this.mc.thePlayer.posY;
		}
	}

	public void onEnabled() {
	}

	public void onDisabled() {

	}

}
