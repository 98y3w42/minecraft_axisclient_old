package axis.module.modules.combat.killaura.modes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import axis.Axis;
import axis.event.events.PacketSentEvent;
import axis.event.events.Render3DEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.combat.KillAura;
import axis.module.modules.combat.killaura.AuraMode;
import axis.util.ColorUtil;
import axis.util.EntityUtils;
import axis.util.Location;
import axis.util.Logger;
import axis.util.RenderUtils;
import axis.util.RotationUtils;
import axis.util.TimeHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Switch extends AuraMode {

	private double blockRange = 8.0D;
	private TimeHelper pseudoTimer = new TimeHelper();
	private TimeHelper angleTimer = new TimeHelper();
	private static EntityLivingBase target;
	public static EntityLivingBase pseudoTarget;
	private boolean dura;
	private boolean angle = true;
	private double speed = 8.0D;
	private double range = 4.8D;
	private int rendercolor = ColorUtil.color(255, 0, 0, 255);

	public Switch(KillAura killAura) {
		super("Switch", killAura);
	}

	public void onUpdate(UpdateEvent event) {
		switch (event.state) {
		case PRE:
			event.ground = true;
			target = null;

			List<EntityLivingBase> attackableEntities = new ArrayList();
			for (Object o : this.mc.theWorld.loadedEntityList) {
				if ((o instanceof EntityLivingBase)) {
					EntityLivingBase entity = (EntityLivingBase) o;

					entity.auraTicks -= 1;
					if (killAura.isValidEntity(entity) && (double) mc.thePlayer.getDistanceToEntity(entity) <= (Double)killAura.values.getValue("range")) {
						if (attackableEntities.size() >= 5) {
							break;
						}
						if (((entity.auraTicks == 10) && (!this.dura)) || (entity.auraTicks == 9) || (entity.auraTicks <= 0)) {
							attackableEntities.add(entity);
						}
					}
				}
			}
			Collections.sort(attackableEntities, new Comparator<EntityLivingBase>() {
				public int compare(EntityLivingBase o1, EntityLivingBase o2) {
					return o1.auraTicks - o2.auraTicks;
				}
			});
			for (EntityLivingBase entity : attackableEntities) {
				if (((pseudoTarget != null) && (pseudoTarget == entity)) || (this.angleTimer.hasReached(50.0d))) {
					if ((pseudoTarget == null) || (pseudoTarget != entity)) {
						this.angleTimer.reset();
					}
					pseudoTarget = target = entity;
					break;
				}
			}
			if ((pseudoTarget != null) && (!killAura.isValidEntity(pseudoTarget))) {
				pseudoTarget = null;
			}
			if (pseudoTarget != null) {
				float[] rotations = RotationUtils.getRotations(pseudoTarget);

				event.yaw = rotations[0];
				event.pitch = rotations[1];
			}
			break;
		case POST:
			if (target != null) {
				if (this.angle) {
					if (this.dura) {
						if (target.auraTicks != 10) {
							swap(9, this.mc.thePlayer.inventory.currentItem);
							this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));

							killAura.onAttack(target);
							killAura.onAttack(target);

							swap(9, this.mc.thePlayer.inventory.currentItem);
							this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
							killAura.onAttack(target);
							killAura.onAttack(target);
						}
					} else {
						float[] rotations = RotationUtils.getRotations(pseudoTarget);

						event.yaw = rotations[0];
						event.pitch = rotations[1];
						killAura.onAttack(target);
					}
				} else if (this.dura) {
					killAura.onAttack(target);
					if (target.auraTicks != 10) {
						killAura.onAttack(target);
					}
				} else {
					killAura.onAttack(target);
				}
				if (target.auraTicks != 10) {
					target.auraTicks = 20;
				}
			} else if ((pseudoTarget != null) && (this.pseudoTimer.hasReached((float) (10.0D)))) {
				this.fakeAttack(target);
				Logger.logChat("" + target.getEntityId());
				this.pseudoTimer.reset();
			}
			double oldRange = this.range;
			this.range = this.blockRange;

			int enemiesArmound = 0;
			for (Object o : this.mc.theWorld.loadedEntityList) {
				if ((o instanceof EntityLivingBase)) {
					EntityLivingBase entity = (EntityLivingBase) o;
					if (killAura.isValidEntity(entity)) {
						enemiesArmound++;
					}
				}
			}
			this.range = oldRange;
			if ((enemiesArmound > 0) && (this.mc.thePlayer.getHeldItem() != null) && (this.mc.thePlayer.getHeldItem().getItem() != null) && ((this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))
					&& (this.mc.gameSettings.keyBindUseItem.pressed)) {
				this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getHeldItem(), this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
			}
			break;
		}
	}

	public void onPacketSent(PacketSentEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
			Entity entity = mc.objectMouseOver.entityHit;
			if (entity != null && (double) mc.thePlayer.getDistanceToEntity(target) <= (Double)killAura.values.getValue("range")) {
				if (entity == null || target != entity) {
					float[] rots = RotationUtils.getRotations(pseudoTarget);
					C03PacketPlayer p = (C03PacketPlayer) event.getPacket();
					float yaw = rots[0] / 2;
					float pitch = rots[1] / 2;
					p.setRotating(true);
					p.setYaw(yaw);
					p.setPitch(pitch);
					event.setPacket(p);
					yaw = rots[0];
					pitch = rots[1];
					p.setYaw(yaw);
					p.setPitch(pitch);
					event.setPacket(p);
				}
			}
		}
	}

	private void fakeAttack(EntityLivingBase ent) {
		fakeSwingItem();
		this.mc.thePlayer.onCriticalHit(ent);
		float sharpLevel = EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), ent.getCreatureAttribute());
		boolean vanillaCrit = (this.mc.thePlayer.fallDistance > 0.0F) && (!this.mc.thePlayer.onGround) && (!this.mc.thePlayer.isOnLadder()) && (!this.mc.thePlayer.isInWater())
				&& (!this.mc.thePlayer.isPotionActive(Potion.blindness)) && (this.mc.thePlayer.ridingEntity == null);
		if ((vanillaCrit) || (Axis.getAxis().getModuleManager().getModuleByName("Criticals").isEnabled())) {
			this.mc.thePlayer.onCriticalHit(ent);
		}
		if (sharpLevel > 0.0F) {
			this.mc.thePlayer.onEnchantmentCritical(ent);
		}
		this.pseudoTimer.reset();
	}

	private void fakeSwingItem() {
		this.mc.thePlayer.fakeSwingItem();
	}

	protected void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.mc.thePlayer);
	}

	public void onMotionPacket(C03PacketPlayer packet) {
	}

	public void onRender(Render3DEvent event) {
		if ((Boolean)killAura.values.getValue("renderbox")) {
			RenderUtils.drawEsp(this.target, event.partialTicks, this.rendercolor, 1184432128);

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
		double weight = 0;// el.getMaxHealth() - (el.getAbsorptionAmount() +
							// el.getTotalArmorValue() * 5);
		weight -= mc.thePlayer.getDistanceSqToEntity(el) / 2.0D;
		// Logger.logChat(el.getName() + " " + weight);
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

		// float distCombined = distYaw + distPitch;
		// weight -= distCombined;
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