package axis.module.modules.movement.speed.modes;

import java.util.Iterator;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.LiquidUtils;
import axis.util.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Other extends SpeedMode implements Mode {
	private boolean newest;
	private int ticks;
	private int turnTicks;
	private float prevYaw;
	private boolean turnCancel;
	private boolean prevStrafing;
	private boolean hasJumped;
	private int state = 1;
	private double moveSpeed;

	public Other() {
		super("Other");
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			if (!this.mc.thePlayer.onGround) {
				this.ticks = -5;
			}
			Timer.timerSpeed = 1.0F;
			if (checks()) {
				double speed = this.newest ? 2.25D : 2.495D;
				speed = 2.19D;
				if (isStrafing() != this.prevStrafing) {
					this.turnTicks = 1;
					this.turnCancel = true;
				}
				if (this.ticks == 1) {
					Timer.timerSpeed = 1.3F;
					if (this.turnTicks <= 0) {
						this.turnCancel = false;
					}
					if (!isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed))) {
						this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed);
					}
				} else if (this.ticks == 2) {
					speed = 0.604D;
					if ((!this.turnCancel) && (!isTurning()) && (!isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed)))) {
						this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed);
					}
				} else if (this.ticks == 3) {
					speed = 0.532D;
					if ((!this.turnCancel) && (!isTurning()) && (!isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed)))) {
						this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed);
					}
				} else if (this.ticks >= 4) {
					if (this.ticks == 4) {
						speed = 0.521D;
						if ((!this.turnCancel) && (!isTurning()) && (!isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed)))) {
							this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0D, this.mc.thePlayer.motionZ * speed);
						}
					}
					this.ticks = 0;
				}
			}
			this.prevYaw = this.mc.thePlayer.rotationYaw;
			this.ticks += 1;
			this.turnTicks -= 1;
			this.prevStrafing = isStrafing();
		}
	}

	public void onMove(MoveEvent event) {
		if ((this.mc.thePlayer.moveStrafing != 0.0F) && (this.mc.thePlayer.moveForward != 0.0F)) {
			event.x *= 0.91D;
			event.z *= 0.91D;
			if (!this.mc.thePlayer.onGround) {
				Speed.canStep = false;
				final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
				float forward = movementInput.moveForward;
				float strafe = movementInput.moveStrafe;
				float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
				if (forward == 0.0f && strafe == 0.0f || !mc.thePlayer.onGround || mc.thePlayer.fallDistance != 0.0f) {
					Speed.canStep = false;
					// event.x = 0.0;
					// event.z = 0.0;
					return;
				}
				boolean collideCheck = false;
				if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
					collideCheck = true;
				}
				if (forward != 0.0f) {
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
				Timer.timerSpeed = 1.0f;
				switch (this.state) {
				case 1:
					this.moveSpeed = speed.getBaseMoveSpeed();
					break;
				case 2:
					this.moveSpeed = speed.getBaseMoveSpeed();
					break;
				default:
					this.moveSpeed = speed.getBaseMoveSpeed();
					break;
				}
				if (collideCheck) {
					Timer.timerSpeed = 1.0f;
					Speed.canStep = true;
				}
				if (this.mc.thePlayer.isSneaking() || (this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f) || this.mc.gameSettings.keyBindJump.isPressed() || LiquidUtils.isOnLiquid()
						|| LiquidUtils.isInLiquid()) {
					Timer.timerSpeed = 1.0f;
					this.moveSpeed = speed.getBaseMoveSpeed();
					Speed.canStep = true;
					this.state = 1;
					return;
				}
				this.moveSpeed = Math.max(this.moveSpeed, speed.getBaseMoveSpeed());
				final double mx = Math.cos(Math.toRadians((double) (yaw + 90.0f)));
				final double mz = Math.sin(Math.toRadians((double) (yaw + 90.0f)));

				event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
				event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
				// mc.thePlayer.motionX = forward * this.moveSpeed * mx + strafe
				// * this.moveSpeed * mz;
				// mc.thePlayer.motionZ = forward * this.moveSpeed * mz - strafe
				// * this.moveSpeed * mx;
			}
		}
	}

	private boolean isColliding(AxisAlignedBB bb) {
		boolean colliding = false;
		Iterator iterator = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, bb).iterator();
		while (iterator.hasNext()) {
			AxisAlignedBB boundingBox = (AxisAlignedBB) iterator.next();
			colliding = true;
		}
		if ((getBlock(bb.offset(0.0D, -0.1D, 0.0D)) instanceof BlockAir)) {
			colliding = true;
		}
		return colliding;
	}

	private boolean isTurning() {
		return RotationUtils.getDistanceBetweenAngles(this.mc.thePlayer.rotationYaw, this.prevYaw) > 4.0F;
	}

	public Block getBlock(AxisAlignedBB bb) {
		int y = (int) bb.minY;
		for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++) {
				Block block = this.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null) {
					return block;
				}
			}
		}
		return null;
	}

	public void onDisabled() {
		net.minecraft.util.Timer.timerSpeed = 1.0F;
		super.onDisabled();
	}

	private boolean isStrafing() {
		return (this.mc.thePlayer.movementInput.moveStrafe != 0.0F) && (this.mc.thePlayer.moveForward != 0.0F);
	}

	public boolean checks() {
		return (!this.mc.thePlayer.isSneaking()) && (!this.mc.thePlayer.isCollidedHorizontally) && ((this.mc.thePlayer.isCollidedHorizontally) || (this.mc.thePlayer.moveForward != 0.0F) || (this.mc.thePlayer.moveStrafing != 0.0F))
				&& (!this.mc.gameSettings.keyBindJump.isPressed()) && (!LiquidUtils.isOnLiquid()) && (!LiquidUtils.isInLiquid());
	}
}