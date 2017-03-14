package axis.module.modules.movement.speed.modes;

import java.util.Iterator;
import java.util.List;

import axis.Axis;
import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.util.Logger;
import axis.util.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Capsar extends SpeedMode {

	private int state = 0;
	private Object option;
	private double moveSpeed = 2.5D;
	private double lastDist;
	private boolean motionMultiplied;
	private TimeHelper timer = new TimeHelper();
	private static int status;
	private long c;
	private static int d;

	public Capsar(Speed speed) {
		super("Capsar", speed);
	}

	public void onMove(MoveEvent event) {
		if (status == 0) {
			moveSpeed = 2.2D;
		} else if (status >= 1) {
			moveSpeed = 1.0D;
		} else if (status == 2) {
			moveSpeed = 2.9D;
		}
		if ((!mc.thePlayer.onGround) || (mc.gameSettings.keyBindJump.pressed)) {
			moveSpeed = 0.8D;
		}
		final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward == 0.0f && strafe == 0.0f || !mc.thePlayer.onGround || mc.thePlayer.fallDistance != 0.0f) {
			Timer.timerSpeed = 1.0F;
			Speed.canStep = false;
			// event.x = 0.0;
			// event.z = 0.0;
			return;
		}
		boolean collideCheck = false;
		if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
			collideCheck = true;
		}
		if ((forward != 0.0f) || (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0)) {
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
	}

	public void onUpdate(UpdateEvent event) {
		int i = (getPlayer().motionX != 0.0D) || (getPlayer().motionZ != 0.0D) ? 1 : 0;
		if (i != 0) {
			if (event.state == Event.State.PRE) {
				int j = getPlayer().moveStrafing != 0.0F ? 1 : 0;
				int k = 1;
				boolean bool = false;
				if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
					bool = true;
				}
				if (bool) {
					Iterator localIterator = mc.theWorld.getCollidingBoundingBoxes(getPlayer(), getPlayer().boundingBox.getThisBounds().contract(0.5D, 0.0D, 0.5D)).iterator();
					while (localIterator.hasNext()) {
						Object localObject = localIterator.next();
						if (Axis.getModuleManager().getModuleByName("Step").isEnabled()) {
							k = 0;
						}
					}
				}
				int m = getMultiplier();
				if ((!getPlayer().isSneaking()) && ((getPlayer().moveForward != 0.0F) || (getPlayer().moveStrafing != 0.0F)) && (!getPlayer().isOnLadder()) && (k != 0)) {
					double d1 = getPlayer().isSprinting() ? 0.021D : 0.4D;
					double d2 = j != 0 ? moveSpeed + 0.01D : moveSpeed + 0.05D;
					double d3 = d2 + d1;
					if (m == 1) {
						if (status == 0) {
							status += 1;
							mc.timer.timerSpeed = 1.1F;
							getPlayer().motionX *= d3;
							getPlayer().motionZ *= d3;
							getPlayer().boundingBox.expand(0.0D, 1.0E-4D, 0.0D);
						} else if (status >= 1) {
							mc.timer.timerSpeed = 1.0F;
							// mc.thePlayer.setPosition(this.mc.thePlayer.posX,
							// this.mc.thePlayer.posY + 0.01D,
							// this.mc.thePlayer.posZ);
							getPlayer().motionX /= 1.5D;
							getPlayer().motionZ /= 1.5D;
							status = 0;
						}
					} else if (m == 0) {
						mc.timer.timerSpeed = 1.0F;
						status = 3;
					} else if (m == 2) {
						int n = getPlayer().moveForward < 0.0F ? 1 : 0;
						int i1 = getPlayer().moveForward > 0.0F ? 1 : 0;
						double d4 = getPlayer().isSprinting() ? 0.02754D : 1.3352D;
						double d5 = moveSpeed;
						d5 *= 2.0D;
						if ((status <= 2) && (!getPlayer().isSneaking()) && ((getPlayer().moveForward != 0.0F) || (getPlayer().moveStrafing != 0.0F)) && (!getPlayer().isOnLadder())) {
							status += 1;
							mc.timer.timerSpeed = 1.0F;
							if (status == 2) {
								mc.timer.timerSpeed = 1.15F;
								status = 0;
								if ((n == 0) && (((j != 0) && (i1 == 0)) || ((j == 0) && (i1 != 0)))) {
									getPlayer().boundingBox.expand(getPlayer().motionX * (d5 + d4), 0.01D, getPlayer().motionZ * (d5 + d1));
								} else if (n != 0) {
									getPlayer().boundingBox.expand(getPlayer().motionX * (d5 / 1.2D + 0.02D), 0.01D, getPlayer().motionZ * (d5 / 1.2D + 0.02D));
								} else if (j != 0) {
									getPlayer().boundingBox.expand(getPlayer().motionX * (d5 / 1.1D + 0.02D), 0.01D, getPlayer().motionZ * (d5 / 1.1D + 0.02D));
								}
							}
						}
					}
				}
			}
		} else {
			Minecraft.getMinecraft().timer.timerSpeed = 1.0F;
		}
	}

	private int getMultiplier() {
		int i = MathHelper.floor_double(getPlayer().posX);
		int j = MathHelper.floor_double(getPlayer().posZ);
		int k = MathHelper.floor_double(getPlayer().posY) - 2;
		if (mc.theWorld.getBlock(i, k, j).slipperiness > 0.7D) {
			return 2;
		}
		if (getPlayer().onGround) {
			return 1;
		}
		return 0;
	}

	protected static EntityPlayerSP getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	public void onEnabled() {

	}

	public void onDisabled() {
		mc.timer.timerSpeed = 1.0f;
	}

}
