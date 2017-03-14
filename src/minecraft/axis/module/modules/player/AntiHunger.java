package axis.module.modules.player;

import axis.event.events.PacketSentEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiHunger extends Module{

	public AntiHunger() {
		super("AntiHunger", 3453924, ModuleManager.Category.PLAYER);
	}


	public void onPacketSent(PacketSentEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
			double yDifference = this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY;
			boolean groundCheck = yDifference == 0.0D;
			if ((groundCheck) && (!this.mc.playerController.isHittingBlock) && !mc.gameSettings.keyBindJump.isPressed()) {
				packet.setOnGround(false);
			}
		}
	}
}
