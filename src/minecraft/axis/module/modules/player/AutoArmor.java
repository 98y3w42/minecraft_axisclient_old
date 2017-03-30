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
		this.setDisplayName("Armor");
		this.setTag("C: " + (Boolean) this.values.getValue("cleaner"));
		Axis.getAxis().getCommandManager().getContents().add(new Command("autormor", "<cleaner>", new String[] { "AutoArmor", "aa" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("cleaner")) {
					values.setValue("cleaner", !(Boolean) values.getValue("cleaner"));
					Logger.logChat("Cleaner: " + (Boolean) values.getValue("cleaner"));
				}
			}
		});
	}

	public void onValueSetup() {
		super.onValueSetup();
		values.addValue("cleaner", true);
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
		if (event.state == Event.State.PRE && (Boolean) this.values.getValue("cleaner")) {
			InventoryPlayer invp = this.mc.thePlayer.inventory;
			for (int i = 9; i < 45; i++) {
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
			if (!(mc.thePlayer.inventory.armorInventory[3] == null)) {
				if (i == 306) {
					return true;
				}
			}
			if (!(mc.thePlayer.inventory.armorInventory[2] == null)) {
				if (i == 307) {
					return true;
				}
			}
			if (!(mc.thePlayer.inventory.armorInventory[1] == null)) {
				if (i == 308) {
					return true;
				}
			}
			if (!(mc.thePlayer.inventory.armorInventory[0] == null)) {
				if (i == 309) {
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

	private boolean findItemb(int id) {
		for (int index = 9; index < 45; index++) {
			ItemStack item = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if ((item != null) && (Item.getIdFromItem(item.getItem()) == id)) {
				return true;
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
