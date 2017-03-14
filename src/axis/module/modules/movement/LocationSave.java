package axis.module.modules.movement;

import axis.event.Event.State;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class LocationSave extends Module {

	private EntityOtherPlayerMP prayerCopy;
	private double startX;
	private double startY;
	private double startZ;
	private float startYaw;
	private float startPitch;

	public LocationSave() {
		super("LocationSave", -5192482, ModuleManager.Category.MOVEMENT);
	}

	private void onPreUpdate(UpdateEvent event)
	{
		if (event.state == State.PRE)
		{
			event.y += 0.2f;
		}
	}

	public void onEnabled() {
		super.onEnabled();
		if (this.mc.thePlayer != null)
		{

			this.startX = this.mc.thePlayer.posX;
			this.startY = this.mc.thePlayer.posY;
			this.startZ = this.mc.thePlayer.posZ;
			this.startYaw = this.mc.thePlayer.rotationYaw;
			this.startPitch = this.mc.thePlayer.rotationPitch;

			this.prayerCopy = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
			this.prayerCopy.inventory = this.mc.thePlayer.inventory;
			this.prayerCopy.inventoryContainer = this.mc.thePlayer.inventoryContainer;
			this.prayerCopy.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
			this.prayerCopy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
			this.mc.theWorld.addEntityToWorld(-1, this.prayerCopy);

			for (int i = 0; i < 80.0D + 40.0D; i++)
			{
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.049D, this.mc.thePlayer.posZ, false));
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
			}
			this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
		}
	}

	public void onDisabled()
	{
		super.onDisabled();
		if (mc.thePlayer != null) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 100, this.mc.thePlayer.posZ, true));
			this.mc.theWorld.removeEntityFromWorld(-1);
		}
	}
}
