package axis.event.events;

import axis.event.Cancellable;
import axis.event.Event;

public class EventPreSendMotionUpdates extends Event implements Cancellable {

	   private boolean cancel;
	   private boolean onGround;
	   private static float rotationYaw;
	   private static float rotationPitch;
	   public double posX;
	   public double posY;
	   public double posZ;


	   public EventPreSendMotionUpdates(float rotationYaw, float rotationPitch, double posX, double posY, double posZ, boolean onGround) {
	      this.rotationPitch = rotationPitch;
	      this.rotationYaw = rotationYaw;
	      this.posX = posX;
	      this.posY = posY;
	      this.posZ = posZ;
	      this.onGround = onGround;
	   }

	   public boolean isOnGround() {
	      return this.onGround;
	   }

	   public void setOnGround(boolean onGround) {
	      this.onGround = onGround;
	   }

	   public double getPosX() {
	      return this.posX;
	   }

	   public void setPosX(double posX) {
	      this.posX = posX;
	   }

	   public double getPosY() {
	      return this.posY;
	   }

	   public void setPosY(double posY) {
	      this.posY = posY;
	   }

	   public double getPosZ() {
	      return this.posZ;
	   }

	   public void setPosZ(double posZ) {
	      this.posZ = posZ;
	   }

	   public float getPitch() {
	      return this.rotationPitch;
	   }

	   public float getYaw() {
	      return this.rotationYaw;
	   }

	   public boolean isCancelled() {
	      return this.cancel;
	   }

	   public void setCancelled(boolean cancel) {
	      this.cancel = cancel;
	   }

	   public static void setPitch(float pitch) {
		   EventPreSendMotionUpdates.rotationPitch = pitch;
	   }

	   public static void setYaw(float yaw) {
		   EventPreSendMotionUpdates.rotationYaw = yaw;
	   }
	}
