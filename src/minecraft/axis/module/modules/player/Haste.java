package axis.module.modules.player;

import axis.Axis;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Haste
		extends Module {
	public Haste() {
		super("Haste", 3453924, ModuleManager.Category.PLAYER);
	}

	Minecraft mc = Minecraft.getMinecraft();

	public void update(UpdateEvent e) {
		if ((this.mc.thePlayer != null) && (this.mc.theWorld != null)) {
			this.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 1, 1));
		}
	}

	public void onDisabled() {
		super.onDisabled();
		if (mc.thePlayer != null) {
			this.mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
		}
	}

	public void onEnabled() {
		super.onEnabled();
		if ((this.mc.thePlayer != null) && (this.mc.theWorld != null)) {
			this.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 1, 1));
		}
	}
}
