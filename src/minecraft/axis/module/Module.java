package axis.module;

import axis.Axis;
import axis.management.managers.ModuleManager;
import axis.module.modules.render.HUD;
import axis.util.Logger;
import axis.value.ValueList;
import net.minecraft.client.Minecraft;

public abstract class Module {
	public static String type;
	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected int keybind;
	private int color;
	protected boolean enabled;
	protected boolean visible;
	protected ModuleManager.Category category;
	protected String name;
	protected String tag;
	protected String displayName;
	public ValueList values;

	public Module(String name, int keybind, int color, ModuleManager.Category category) {
		this.category = category;
		this.name = name;
		values = new ValueList(name);
		this.displayName = name;
		this.tag = "";
		this.keybind = keybind;
		this.color = color;
		this.enabled = false;
		this.visible = true;

		onValueSetup();
	}

	public Module(String name) {
		this(name, 0, -1, ModuleManager.Category.EXPLOITS);
		this.visible = false;
	}

	public Module(String name, int color, ModuleManager.Category category) {
		this(name, 0, color, category);
		this.category = category;
	}

	public final ModuleManager.Category getCategory() {
		return this.category;
	}

	public void setCategory(ModuleManager.Category category) {
		this.category = category;
	}

	public int getColor() {
		return this.color;
	}

	public int getKeybind() {
		return this.keybind;
	}

	public String getName() {
		return this.name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		String name = displayName;
		if (!tag.equals("")) {
			if (HUD.currentMode.getValue().equals("Axis")) {
				name += " \u00a7f" + tag;
			} else if (HUD.currentMode.getValue().equals("Hex")) {
				name += " \2477" + tag;
			}
		}
		return name;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isEnabled() {
		if (Axis.getAxis() == null) {
			return false;
		}
		if (this.enabled) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void onDisabled() {
		Axis.getAxis().getEventManager().unregister(this);
	}

	public void onEnabled() {
		Axis.getAxis().getEventManager().register(this);
	}

	public void onValueSetup() {
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (Axis.getAxis().getFileManager().getFileByName("modconfig") != null) {
			Axis.getAxis().getFileManager().getFileByName("modconfig").saveFile();
		}

		if (this.enabled) {
			this.onEnabled();
		} else {
			this.onDisabled();
		}

	}

	public void setKeybind(int keybind) {
		this.keybind = keybind;
		if (Axis.getAxis().getFileManager().getFileByName("modconfig") != null) {
			Axis.getAxis().getFileManager().getFileByName("modconfig").saveFile();
		}

	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void toggle() {
		this.enabled = !this.enabled;
		if (Axis.getAxis().getFileManager().getFileByName("modconfig") != null) {
			Axis.getAxis().getFileManager().getFileByName("modconfig").saveFile();
		}

		if (this.enabled) {
			onEnabled();
			if (Axis.getAxis().getModuleManager().getModuleByName("ToggleLogger").isEnabled()) {
				Logger.logChat("Mod \"" + this.getName() + "\" was toggled §2on §f.");
			}
			int i = 0;
			while (i < 3) {
				if (i != 3) {
					if (HUD.currentMode.getValue().equals("Axis")) {
						mc.thePlayer.playSound("random.orb", 0.2F, 1.0F);
					}
					i++;
				}
			}
		} else {
			onDisabled();
			if (Axis.getAxis().getModuleManager().getModuleByName("ToggleLogger").isEnabled()) {
				Logger.logChat("Mod \"" + this.getName() + "\" was toggled §4off §f.");
			}
			int i = 0;
			while (i < 3) {
				if (i != 3) {
					if (HUD.currentMode.getValue().equals("Axis")) {
						mc.thePlayer.playSound("random.pop", 0.5F, 1.0F);
					}
					i++;
				}
			}
		}
	}
}
