package axis.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class moveutil {
	protected static final Minecraft mc = Minecraft.getMinecraft();

	public static void setSpeed(final float speed) {
		final EntityPlayerSP player = mc.thePlayer;
		double yaw = (double) player.rotationYaw;
		final boolean isMoving = player.moveForward != 0.0f || player.moveStrafing != 0.0f;
		final boolean isMovingForward = player.moveForward > 0.0f;
		final boolean isMovingBackward = player.moveForward < 0.0f;
		final boolean isMovingRight = player.moveStrafing > 0.0f;
		final boolean isMovingLeft = player.moveStrafing < 0.0f;
		final boolean isMovingSideways = isMovingLeft || isMovingRight;
		final boolean isMovingStraight = isMovingForward || isMovingBackward;
		if (isMoving) {
			if (isMovingForward && !isMovingSideways) {
				yaw += 0.0;
			} else if (isMovingBackward && !isMovingSideways) {
				yaw += 180.0;
			} else if (isMovingForward && isMovingLeft) {
				yaw += 45.0;
			} else if (isMovingForward) {
				yaw -= 45.0;
			} else if (!isMovingStraight && isMovingLeft) {
				yaw += 90.0;
			} else if (!isMovingStraight && isMovingRight) {
				yaw -= 90.0;
			} else if (isMovingBackward && isMovingLeft) {
				yaw += 135.0;
			} else if (isMovingBackward) {
				yaw -= 135.0;
			}
			yaw = Math.toRadians(yaw);
			player.motionX = -Math.sin(yaw) * speed;
			player.motionZ = Math.cos(yaw) * speed;
		}
	}

	public static void multiplySpeed(final Float multiplier) {
		final EntityPlayerSP player2;
		final EntityPlayerSP player = player2 = mc.thePlayer;
		player2.motionX *= multiplier;
		final EntityPlayerSP entityPlayerSP = player;
		entityPlayerSP.motionZ *= multiplier;
	}

	public static void toFwd(double amount) {
		net.minecraft.client.entity.EntityPlayerSP player = mc.thePlayer;
		double yaw = player.rotationYaw;
		yaw = Math.toRadians(yaw);
		double dX = -Math.sin(yaw) * amount;
		double dZ = Math.cos(yaw) * amount;
		mc.thePlayer.setPosition(mc.thePlayer.posX + dX, mc.thePlayer.posY, mc.thePlayer.posZ + dZ);
	}
}
