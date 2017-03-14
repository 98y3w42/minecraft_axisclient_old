package axis.module.modules.player;

import java.util.List;

import axis.event.Event;
import axis.event.events.PacketSentEvent;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastUse extends Module {

	public FastUse() {
		super("FastUse", -5985391, ModuleManager.Category.PLAYER);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			if ((this.mc.thePlayer.getItemInUseDuration() == 10) && (!(this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) && (!(this.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword))) {
				for (int i = 0; i < 16; i++) {
					this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
				}
			}
		}
	}
}
