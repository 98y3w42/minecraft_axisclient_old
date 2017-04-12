package axis.module.modules.combat.killaura.modes;

import java.util.ArrayList;
import java.util.Random;

import axis.event.Event.State;
import axis.event.events.PacketSentEvent;
import axis.event.events.Render3DEvent;
import axis.event.events.UpdateEvent;
import axis.module.Mode;
import axis.module.modules.combat.killaura.AuraMode;
import axis.util.ColorUtil;
import axis.util.EntityUtils;
import axis.util.Location;
import axis.util.Logger;
import axis.util.RenderUtils;
import axis.util.TimeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Multi extends AuraMode implements Mode {

	private TimeHelper time = new TimeHelper();
	private TimeHelper time1 = new TimeHelper();
	private TimeHelper time2 = new TimeHelper();
	private Random rand = new Random();

	public EntityLivingBase target = null;
	private ArrayList<EntityLivingBase> targets = new ArrayList();
	private ArrayList<EntityLivingBase> targets1 = new ArrayList();

	private long attackDelay;
	private float rnddelay;
	private boolean loop;
	private int rendercolor = ColorUtil.color(255, 0, 0, 255);
	public static int targets1size = 0;

	public Multi() {
		super("Multi");
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == State.PRE) {
			attackDelay = killAura.getDelay();
			rnddelay = killAura.getDelay();
			double targetWeight = Double.NEGATIVE_INFINITY;
			EntityLivingBase highestWeightedTarget = null;
			targets.clear();
			targets1.clear();
			if (this.time2.hasReached(100.0D)) {
				for (Object o : this.mc.theWorld.loadedEntityList) {
					if ((o instanceof EntityLivingBase)) {
						EntityLivingBase el = (EntityLivingBase) o;
						if (this.killAura.isValidEntity(el)) {
							this.targets.add(el);
						}
					}
				}
				for (EntityLivingBase el : this.targets) {
					if ((this.killAura.isValidEntity(el)) &&
							(getTargetWeight(el) > targetWeight)) {
						targetWeight = getTargetWeight(el);
						highestWeightedTarget = el;
					}
				}
				if (highestWeightedTarget != null) {
					this.target = highestWeightedTarget;
				}
			}

			if (killAura.isValidEntity(target)) {
				boolean shouldLook = true;
				float[] values = EntityUtils.getAnglesToEntity(target);
				if (shouldLook) {
					for (int i = 0; i < (Integer) killAura.values.getValue("maxtarget"); i++) {
						double semiMultiWeight = Double.NEGATIVE_INFINITY;
						EntityLivingBase smHighestWeightedTarget = null;
						for (EntityLivingBase el : targets) {
							if ((el != target) && (getDirectionCheckVal(el, getLookVecForAngles(values[0], values[1])) < 0.1D) && (el.getDistanceSqToEntity(target) < (Double) killAura.values.getValue("range"))
									&& (killAura.isValidEntity(el))
									&& (!targets1.contains(el)) && (getTargetWeight(el) > semiMultiWeight)) {
								semiMultiWeight = getTargetWeight(el);
								smHighestWeightedTarget = el;
							}
						}
						if (smHighestWeightedTarget != null) {
							targets1.add(smHighestWeightedTarget);
						}
						double averageX = 0.0D;
						double averageY = 0.0D;
						double averageZ = 0.0D;
						for (EntityLivingBase entity : targets1) {
							averageX += entity.posX;
							averageY += entity.posY + entity.getEyeHeight();
							averageZ += entity.posZ;
						}
						averageX /= targets1.size();
						averageY /= targets1.size();
						averageZ /= targets1.size();

						float[] rotations = EntityUtils.facePosition(mc.thePlayer, averageX, averageY, averageZ);

						event.yaw = rotations[0];
						event.pitch = rotations[1];

						if ((Boolean) killAura.values.getValue("lockview")) {
							mc.thePlayer.rotationYaw = rotations[0];
							mc.thePlayer.rotationPitch = rotations[1];
						}
					}

					double averageX = 0.0D;
					double averageY = 0.0D;
					double averageZ = 0.0D;
					targets1.add(target);
					for (EntityLivingBase entity : targets1) {
						averageX += entity.posX;
						averageY += entity.posY +
								entity.getEyeHeight();
						averageZ += entity.posZ;
					}
					averageX /= targets1.size();
					averageY /= targets1.size();
					averageZ /= targets1.size();

					float[] rotations = EntityUtils.facePosition(mc.thePlayer, averageX,
							averageY, averageZ);

					event.yaw = rotations[0];
					event.pitch = rotations[1];
				}
			}
		}

		if (event.state == State.POST) {
			targets1size = targets1.size();
			this.attackDelay = killAura.getDelay();
			if (killAura.isValidEntity(this.target) && !targets1.isEmpty()) {
				if (this.time.hasReached(this.attackDelay)) {
					this.time.reset();
					if (!this.loop) {
						this.loop = true;
						for (int i = 0; i < targets1.size(); i++) {
							double averageX = 0.0D;
							double averageY = 0.0D;
							double averageZ = 0.0D;
							for (EntityLivingBase entity : targets1) {
								averageX += entity.posX;
								averageY += entity.posY + entity.getEyeHeight();
								averageZ += entity.posZ;
							}
							averageX /= targets1.size();
							averageY /= targets1.size();
							averageZ /= targets1.size();

							float[] rotations = EntityUtils.facePosition(mc.thePlayer, averageX, averageY, averageZ);

							event.yaw = rotations[0];
							event.pitch = rotations[1];
							Logger.logChat("" + targets1.get(i).getEntityId());
							killAura.onAttack(targets1.get(i));
						}
					}
					this.loop = false;
				} else {
					this.mc.thePlayer.fakeSwingItem();
				}
			}
		}
	}

	public void onMotionPacket(C03PacketPlayer packet) {
	}

	public void onRender(Render3DEvent event) {
		if ((Boolean) killAura.values.getValue("renderbox")) {
			for (int i = 0; i < targets1.size(); i++) {
				RenderUtils.drawEsp(this.targets1.get(i), event.partialTicks, this.rendercolor, 1184432128);
			}
		}
	}

	public void onPacketSent(PacketSentEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
			Entity entity = mc.objectMouseOver.entityHit;
			if (entity != null && (double) mc.thePlayer.getDistanceToEntity(target) <= (Double) killAura.values.getValue("range")) {
				if (entity == null || target != entity) {
					double averageX = 0.0D;
					double averageY = 0.0D;
					double averageZ = 0.0D;
					targets1.add(target);
					for (EntityLivingBase entity1 : targets1) {
						averageX += entity1.posX;
						averageY += entity1.posY + entity1.getEyeHeight();
						averageZ += entity1.posZ;
					}
					averageX /= targets1.size();
					averageY /= targets1.size();
					averageZ /= targets1.size();

					float[] rots = EntityUtils.facePosition(mc.thePlayer, averageX, averageY, averageZ);
					C03PacketPlayer p = (C03PacketPlayer) event.getPacket();
					p.setRotating(true);
					p.setYaw(rots[0]);
					p.setPitch(rots[1]);
					event.setPacket(p);
				}
			}
		}
	}

	public double getDirectionCheckVal(Entity e, Vec3 lookVec) {
		return directionCheck(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ, lookVec.xCoord, lookVec.yCoord, lookVec.zCoord, e.posX, e.posY + e.height / 2.0D, e.posZ, e.width, e.height,
				5.0D);
	}

	private double directionCheck(double sourceX, double sourceY, double sourceZ, double dirX, double dirY, double dirZ, double targetX, double targetY, double targetZ, double targetWidth, double targetHeight, double precision) {
		double dirLength = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
		if (dirLength == 0.0D) {
			dirLength = 1.0D;
		}
		double dX = targetX - sourceX;
		double dY = targetY - sourceY;
		double dZ = targetZ - sourceZ;

		double targetDist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

		double xPrediction = targetDist * dirX / dirLength;
		double yPrediction = targetDist * dirY / dirLength;
		double zPrediction = targetDist * dirZ / dirLength;

		double off = 0.0D;

		off += Math.max(Math.abs(dX - xPrediction) - (targetWidth / 2.0D + precision), 0.0D);
		off += Math.max(Math.abs(dZ - zPrediction) - (targetWidth / 2.0D + precision), 0.0D);
		off += Math.max(Math.abs(dY - yPrediction) - (targetHeight / 2.0D + precision), 0.0D);
		if (off > 1.0D) {
			off = Math.sqrt(off);
		}
		return off;
	}

	protected final Vec3 getLookVecForAngles(float yaw, float pitch) {
		float var3 = MathHelper.cos(-pitch * 0.017453292F - 3.1415927F);
		float var4 = MathHelper.sin(-pitch * 0.017453292F - 3.1415927F);
		float var5 = -MathHelper.cos(-yaw * 0.017453292F);
		float var6 = MathHelper.sin(-yaw * 0.017453292F);
		Vec3 vector = new Vec3(var4 * var5, var6, var3 * var5);
		return vector;
	}

	private double getDistToEntitySqPred(Entity e) {
		if ((e instanceof EntityPlayer)) {
			EntityPlayer p = (EntityPlayer) e;
			Location loc = predictEntityLocation(p, mc
					.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime());
			if (loc != null) {
				return Math.min(mc.thePlayer.getDistanceSq(loc.x, loc.y, loc.z), mc.thePlayer.getDistanceSqToEntity(e));
			}
		}
		return mc.thePlayer.getDistanceSqToEntity(e);
	}

	public Location predictEntityLocation(Entity e, double milliseconds) {
		if (e != null) {
			if ((e.posX == e.lastTickPosX) && (e.posY == e.lastTickPosY) && (e.posZ == e.lastTickPosZ)) {
				return new Location(e.posX, e.posY, e.posZ);
			}
			double ticks = milliseconds / 1000.0D;
			ticks *= 20.0D;
			return interp(new Location(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ), new Location(e.posX + e.motionX, e.posY + e.motionY, e.posZ + e.motionZ), ticks);
		}
		return null;
	}

	public Location interp(Location from, Location to, double pct) {
		double x = from.x + (to.x - from.x) * pct;
		double y = from.y + (to.y - from.y) * pct;
		double z = from.z + (to.z - from.z) * pct;
		return new Location(x, y, z);
	}

	public double getTargetWeight(EntityLivingBase el) {
		double weight = 0;
		weight -= mc.thePlayer.getDistanceSqToEntity(el) / 2.0D;
		if ((el instanceof EntityPlayer)) {
			weight += 50.0D;
		}
		if ((el instanceof EntityCreeper)) {
			weight += 35.0D;
		} else if ((el instanceof EntitySkeleton)) {
			weight += 25.0D;
		}
		float distYaw = Math.abs(EntityUtils.getYawChangeToEntity(el));
		float distPitch = Math.abs(EntityUtils.getPitchChangeToEntity(el));
		return weight;
	}

	public float getDistanceBetweenAngles(float a, float b) {
		float d = Math.abs(a - b) % 360.0F;
		float r = d > 180.0F ? 360.0F - d : d;
		return r;
	}

	public void onDisabled() {
	}

}
