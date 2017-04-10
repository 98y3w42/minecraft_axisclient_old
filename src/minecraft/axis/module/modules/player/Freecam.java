package axis.module.modules.player;

import axis.event.Event.State;
import axis.event.events.BoundingBoxEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class Freecam extends Module {

	private EntityOtherPlayerMP prayerCopy;
	private double startX;
	private double startY;
	private double startZ;
	private float startYaw;
	private float startPitch;

	public Freecam() {
		super("Freecam", -5192482, ModuleManager.Category.PLAYER);
	}

	private void onPreUpdate(UpdateEvent event) {
		if (event.state == State.PRE) {
			this.mc.thePlayer.capabilities.isFlying = true;
			this.mc.thePlayer.noClip = true;
			event.setCancelled(true);
		}
	}

	private void onBoundingBox(BoundingBoxEvent event) {
		if ((Math.abs(event.pos.getX() - mc.thePlayer.posX) <= 3) && (Math.abs(event.pos.getZ() - mc.thePlayer.posZ) <= 3) && (Math.abs(event.pos.getY() - mc.thePlayer.posY) <= 3)) {
			event.boundingBox = null;
		}
	}

	public void onEnabled() {
		super.onEnabled();
		if (this.mc.thePlayer != null) {

			this.startX = this.mc.thePlayer.posX;
			this.startY = this.mc.thePlayer.posY;
			this.startZ = this.mc.thePlayer.posZ;
			this.startYaw = this.mc.thePlayer.rotationYaw;
			this.startPitch = this.mc.thePlayer.rotationPitch;
			mc.thePlayer.motionY = 0.0F;

			this.prayerCopy = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
			this.prayerCopy.inventory = this.mc.thePlayer.inventory;
			this.prayerCopy.inventoryContainer = this.mc.thePlayer.inventoryContainer;
			this.prayerCopy.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
			this.prayerCopy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
			this.mc.theWorld.addEntityToWorld(-1, this.prayerCopy);
		}
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			this.mc.thePlayer.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
			this.mc.thePlayer.noClip = false;
			this.mc.theWorld.removeEntityFromWorld(-1);
			this.mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionX = 0.0F;
			mc.thePlayer.motionZ = 0.0F;
		}
	}
}
