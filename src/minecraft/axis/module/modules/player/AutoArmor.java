package axis.module.modules.player;

import axis.Axis;
import axis.command.Command;
import axis.event.Event;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.util.Logger;
import axis.util.TimeHelper;
import axis.value.Value;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {

	private final int[] boots = { 313, 309, 317, 305, 301 };
	private final int[] chestplate = { 311, 307, 315, 303, 299 };
	private final int[] helmet = { 310, 306, 314, 302, 298 };
	private final int[] leggings = { 312, 308, 316, 304, 300 };
	private final TimeHelper time = new TimeHelper();
	private TimeHelper timer = new TimeHelper();

	public AutoArmor() {
		super("AutoArmor", 0x00BFFF, Category.PLAYER);
	}

	public void onUpdate(UpdateEvent event) {
		if (!this.time.hasReached(65.0F)) {
			return;
		}
		if ((mc.thePlayer.openContainer != null) && (mc.thePlayer.openContainer.windowId != 0)) {
			return;
		}
		int item = -1;
		if (mc.thePlayer.inventory.armorInventory[0] == null) {
			int[] arrayOfInt = this.boots;
			int j = this.boots.length;
			for (int i = 0; i < j; i++) {
				int id = arrayOfInt[i];
				if (findItem(id) != -1) {
					item = findItem(id);
					break;
				}
			}
		}
		if (armourIsBetter(0, this.boots)) {
			item = 8;
		}
		if (mc.thePlayer.inventory.armorInventory[3] == null) {
			int[] arrayOfInt = this.helmet;
			int j = this.helmet.length;
			for (int i = 0; i < j; i++) {
				int id = arrayOfInt[i];
				if (findItem(id) != -1) {
					item = findItem(id);
					break;
				}
			}
		}
		if (armourIsBetter(3, this.helmet)) {
			item = 5;
		}
		if (mc.thePlayer.inventory.armorInventory[1] == null) {
			int[] arrayOfInt = this.leggings;
			int j = this.leggings.length;
			for (int i = 0; i < j; i++) {
				int id = arrayOfInt[i];
				if (findItem(id) != -1) {
					item = findItem(id);
					break;
				}
			}
		}
		if (armourIsBetter(1, this.leggings)) {
			item = 7;
		}
		if (mc.thePlayer.inventory.armorInventory[2] == null) {
			int[] arrayOfInt = this.chestplate;
			int j = this.chestplate.length;
			for (int i = 0; i < j; i++) {
				int id = arrayOfInt[i];
				if (findItem(id) != -1) {
					item = findItem(id);
					break;
				}
			}
		}
		if (armourIsBetter(2, this.chestplate)) {
			item = 6;
		}
		if (item != -1) {
			mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
			mc.playerController.updateController();
			this.time.setLastMS(this.time.getCurrentMS());
			return;
		}
	}

	public boolean armourIsBetter(int slot, int[] armourtype) {
		if (mc.thePlayer.inventory.armorInventory[slot] != null) {
			int currentIndex = 0;
			int finalCurrentIndex = -1;
			int invIndex = 0;
			int finalInvIndex = -1;
			int[] arrayOfInt = armourtype;
			int j = armourtype.length;
			for (int i = 0; i < j; i++) {
				int armour = arrayOfInt[i];
				if (Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[slot].getItem()) == armour) {
					finalCurrentIndex = currentIndex;
					break;
				}
				currentIndex++;
			}
			arrayOfInt = armourtype;
			j = armourtype.length;
			for (int i = 0; i < j; i++) {
				int armour = arrayOfInt[i];
				if (findItem(armour) != -1) {
					finalInvIndex = invIndex;
					break;
				}
				invIndex++;
			}
			if (finalInvIndex > -1) {
				if (finalInvIndex < finalCurrentIndex) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	private int findItem(int id) {
		for (int index = 9; index < 45; index++) {
			ItemStack item = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if ((item != null) && (Item.getIdFromItem(item.getItem()) == id)) {
				return index;
			}
		}
		return -1;
	}
}
