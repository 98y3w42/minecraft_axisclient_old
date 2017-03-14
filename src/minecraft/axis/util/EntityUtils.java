package axis.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public final class EntityUtils
{
	private static final Minecraft mc = Minecraft.getMinecraft();

	private static boolean set = false;
	private static EntityPlayer reference;

	public static EntityPlayer getReference() {
		return reference == null ? reference = mc.thePlayer : ((set || !RenderUtils.getTracerEntity().getValue()) ? mc.thePlayer : reference);
	}

	public static float[] getEntityRotations(Entity target) {
		final double var4 = target.posX - mc.thePlayer.posX;
		final double var6 = target.posZ - mc.thePlayer.posZ;
		final double var8 = target.posY + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
		final double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
		final float yaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
		final float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / Math.PI);
		return new float[]{yaw, pitch};
	}

	public static float[] getEntityRotations2(Entity target, Entity target2) {
		final double var4 = target.posX - target2.posX;
		final double var6 = target.posZ - target2.posZ;
		final double var8 = target.posY + target.getEyeHeight() / 1.3 - (target2.posY + target2.getEyeHeight());
		final double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
		final float yaw = (float) (Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
		final float pitch = (float) -(Math.atan2(var8, var14) * 180.0D / Math.PI);
		return new float[]{yaw, pitch};
	}

	public static float[] facePosition(Entity entity , double x , double y , double z){
        double var4 = x - entity.posX;
        double var8 = z - entity.posZ;
        double var6 = y  - (entity.posY + entity.getEyeHeight());
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        return new float[]{entity.rotationYaw + MathHelper.wrapAngleTo180_float(var12 - entity.rotationYaw), entity.rotationPitch + MathHelper.wrapAngleTo180_float(var13 - entity.rotationPitch)};
	}

	   public static float getYawChangeToEntity(Entity entity) {
		      double deltaX = entity.posX - mc.thePlayer.posX;
		      double deltaZ = entity.posZ - mc.thePlayer.posZ;
		      double yawToEntity;
		      if(deltaZ < 0.0D && deltaX < 0.0D) {
		         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		      } else if(deltaZ < 0.0D && deltaX > 0.0D) {
		         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		      } else {
		         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		      }

		      return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)yawToEntity));
		   }

		   public static float getPitchChangeToEntity(Entity entity) {
		      double deltaX = entity.posX - mc.thePlayer.posX;
		      double deltaZ = entity.posZ - mc.thePlayer.posZ;
		      double deltaY = entity.posY - 1.6D + (double)entity.getEyeHeight() - mc.thePlayer.posY;
		      double distanceXZ = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		      double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		      return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)pitchToEntity);
		   }

	public static float getAngle(float[] original, float[] rotations) {
        float curYaw = normalizeAngle(original[0]);
        rotations[0] = normalizeAngle(rotations[0]);
        float curPitch = normalizeAngle(original[1]);
        rotations[1] = normalizeAngle(rotations[1]);
        float fixedYaw = normalizeAngle(curYaw - rotations[0]);
        float fixedPitch = normalizeAngle(curPitch - rotations[1]);
        return Math.abs(normalizeAngle(fixedYaw) + Math.abs(fixedPitch));
    }

	public static float getAngle(float[] rotations) {
        return getAngle(new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch}, rotations);
    }

	public static float getAngletest(float yaw, float pitch, float[] rotations) {
        return getAngle(new float[]{yaw, pitch}, rotations);
    }

	public static float[] getAnglesToEntity(Entity e) {
	      return new float[]{getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch};
	}

	public static float getAngle2(Entity target, float[] rotations) {
        return getAngle(new float[]{target.rotationYaw, target.rotationPitch}, rotations);
    }

	public static float normalizeAngle(float angle) {
        return MathHelper.wrapAngleTo180_float((angle + 180.0F) % 360.0F - 180.0F);
    }

    public static boolean isLiving(Entity entity)
    {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAnimal(Entity entity)
    {
        return entity instanceof IAnimals && !(entity instanceof IMob);
    }

    public static boolean isMonster(Entity entity)
    {
        return entity instanceof IMob;
    }

    public static boolean isPlayer(Entity entity)
    {
        return entity instanceof EntityPlayer;
    }

    public static boolean isVehicle(Entity entity)
    {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isInRange(Entity entity, float range)
    {
        return Minecraft.getMinecraft().thePlayer.getDistance(entity.posX, entity.posY, entity.posZ) <= (double)range || Minecraft.getMinecraft().thePlayer.getDistance(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ) <= (double)range;
    }

    public static boolean isMoving(Entity entity)
    {
        return entity.motionX != 0.0D && entity.motionZ != 0.0D && entity.motionY != 0.0D;
    }

    public static double getDiff(double lastI, double i, float ticks, double ownI)
    {
        return lastI + (i - lastI) * (double)ticks - ownI;
    }
}