package axis.module.modules.player;

import axis.event.Event;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.util.Logger;
import axis.util.TimeHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryCleaner extends Module {

	private TimeHelper timer = new TimeHelper();
	private int cleanslot = -1;

	public InventoryCleaner() {
		super("InventoryCleaner", -1, Category.PLAYER);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == Event.State.PRE) {
			InventoryPlayer invp = this.mc.thePlayer.inventory;
			for (int i = 9; i < 45; i++) {
				this.cleanslot = i;
				ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (itemStack != null) {
					itemStack.getItem();
					if ((shouldClean(Item.getIdFromItem(itemStack.getItem())) && this.timer.hasReached(50.0D))) {
						this.timer.reset();
						this.mc.playerController.windowClick(0, i, 1, 4, this.mc.thePlayer);
						this.mc.playerController.windowClick(0, 64537, 1, 4, this.mc.thePlayer);
						mc.playerController.updateController();
					}
				}
			}
		}
	}

	private boolean shouldClean(int i) {
		if (AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			if (i == 332) {
				return true;
			}
			if (i == 4) {
				return true;
			}
			if (i == 3) {
				return true;
			}
			if (i == 116) {
				return true;
			}
			if (i == 46) {
				return true;
			}
			if (i == 145) {
				return true;
			}
			if (i == 259) {
				return true;
			}
			if (i == 295) {
				return true;
			}
			if (i == 325) {
				return true;
			}
			if (i == 346) {
				return true;
			}
			if (i == 344) {
				return true;
			}
			if (i == 269) {
				return true;
			}
			if (i == 270) {
				return true;
			}
			if (i == 271) {
				return true;
			}
			if (i == 261 || i == 262) {
				return true;
			}
			if (i == 384) {
				return true;
			}
			if (i == 54) {
				return true;
			}
			if (i == 50) {
				return true;
			}
			if (i == 374) {
				return true;
			}
			if (i == 298 || i == 299 || i == 300 || i == 301) {
				return true;
			}
			if (i == 314 || i == 315 || i == 316 || i == 317) {
				return true;
			}
			if (i == 256 || i == 257 || i == 258) {
				return true;
			}
			if ((i == 272) && this.findItemb(276)) {
				return true;
			}
			if ((i == 278) && this.findItemb(278)) {
				return true;
			}
			if (!(mc.thePlayer.inventory.armorInventory[3] == null)) {
				if (i == 306) {
					return true;
				}
				if (i == 310 && Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[3].getItem()) == 310) {
					return true;
				}
			}
			if (!(mc.thePlayer.inventory.armorInventory[2] == null)) {
				if (i == 307) {
					return true;
				}
				if (i == 311 && Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[2].getItem()) == 311) {
					return true;
				}
			}
			if (!(mc.thePlayer.inventory.armorInventory[1] == null)) {
				if (i == 308) {
					return true;
				}
				if (i == 312 && Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[1].getItem()) == 312) {
					return true;
				}
			}
			if (!(mc.thePlayer.inventory.armorInventory[0] == null)) {
				if (i == 309) {
					return true;
				}
				if (i == 313 && Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[0].getItem()) == 313) {
					return true;
				}
			}
		} else if (AutoSetting.setting.getValue().equalsIgnoreCase("Anni")) {
			if (i == 3) {
				return true;
			}
			if (i == 4) {
				return true;
			}
			if (i == 61) {
				return true;
			}
			if (i == 287) {
				return true;
			}
			if (i == 288) {
				return true;
			}
			if (i == 289) {
				return true;
			}
			if (i == 290) {
				return true;
			}
			if (i == 295) {
				return true;
			}
			if (i == 331) {
				return true;
			}
			if (i == 338) {
				return true;
			}
		}
		return false;
	}

	private boolean findItemb(int id) {
		for (int index = 9; index < 45; index++) {
			ItemStack item = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if ((item != null) && (Item.getIdFromItem(item.getItem()) == id) && index != this.cleanslot) {
				return true;
			}
		}
		return false;
	}
}
