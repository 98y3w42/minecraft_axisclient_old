package axis.ui.tabgui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import axis.Axis;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.render.HUD;
import axis.util.ColorUtil;
import axis.util.FontUtils;
import axis.util.RenderHelper;
import axis.util.StringUtil;
import net.minecraft.client.Minecraft;

public class TabGui {
	protected static final int NO_COLOR = 0;
	protected static final int INSIDE_COLOR = -2093;
	protected static final int BORDER_COLOR = 2093295920;
	protected static final int COMPONENT_HEIGHT = 14;
	protected static int baseCategoryWidth;
	protected static int baseCategoryHeight;
	protected static int baseModWidth;
	protected static int baseModHeight;
	protected static Section section = Section.CATEGORY;
	protected static int categoryTab = 0;
	protected static int modTab = 0;
	protected static int categoryPosition = 15;
	protected static int categoryTargetPosition = 14;
	protected static int modPosition = 15;
	protected static int modTargetPosition = 15;
	protected static boolean transitionQuickly;
	protected static long lastUpdateTime;
	public static int tabguicolor;
	private static int delay = 0;
	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected static FontUtils Comfortaa18 = new FontUtils("Comfortaa", Font.PLAIN, 18);

	public static void init() {
		int highestWidth = 0;
		Category[] arrayOfCategory;
		int j = (arrayOfCategory = Category.values()).length;
		for (int i = 0; i < j; i++) {
			Category category = arrayOfCategory[i];

			String name = StringUtil.capitalize(category.name().toLowerCase());
			int stringWidth = (int) Comfortaa18.getWidth(name);
			highestWidth = Math.max(stringWidth, highestWidth);
		}
		baseCategoryWidth = highestWidth + 6;
		baseCategoryHeight = Category.values().length * 14 + 2;
	}

	public static void render() {
		updateBars();
		tabguicolor = HUD.color1;
		int o = ColorUtil.color(255, 255, 255, 100);
		RenderHelper.drawRect(2.0F, 14.0F + 3, 2 + baseCategoryWidth + 20, 14 + baseCategoryHeight + 4 - 2, o);
		RenderHelper.drawRect(2.0F, 1, 2 + baseCategoryWidth + 20, 15, o);
		RenderHelper.drawRect(2.0F, 1, 5, 15, tabguicolor);
		RenderHelper.drawRect(3.0F, categoryPosition + 4, 1 + baseCategoryWidth + 20, categoryPosition + 14 + 4 - 1, tabguicolor);

		int yPos = 15 + 10;
		int yPosBottom = 29;
		for (int i = 0; i < Category.values().length; i++) {
			Category category = Category.values()[i];
			String name = StringUtil.capitalize(category.name().toLowerCase());

			Comfortaa18.drawString(name, baseCategoryWidth / 10, yPos - 3 - 1, -1);
			yPos += 14;
			yPosBottom += 14;
		}
		if (section == Section.MODS) {
			RenderHelper.drawRect(baseCategoryWidth + 4 + 20, categoryPosition - 1 + 6, baseCategoryWidth + 2 + baseModWidth + 20 + 2, categoryPosition + getModsInCategory(Category.values()[categoryTab]).size() * 14 + 1 + 6 - 1,
					o);
			RenderHelper.drawRect(baseCategoryWidth + 5 + 20, modPosition + 6, baseCategoryWidth + baseModWidth + 1 + 20 + 2, modPosition + 14 + 6 - 1, tabguicolor);

			int yPos1 = categoryPosition;
			int yPosBottom1 = categoryPosition + 14 + 10;
			for (int i = 0; i < getModsInCategory(Category.values()[categoryTab]).size(); i++) {
				Module mod = (Module) getModsInCategory(Category.values()[categoryTab]).get(i);
				String name = mod.getName();

				Comfortaa18.drawString(name, baseCategoryWidth + baseModWidth / 10 + 20, yPos1 + 3 + 6 - 1, mod.isEnabled() ? -1 : HUD.hexcolor);
				yPos1 += 14;
				yPosBottom1 += 14;
			}
		}
	}

	public static void keyPress(int key) {
		delay++;
		if (delay < 2) {
			return;
		}
		delay = 0;
		if (section == Section.CATEGORY) {
			switch (key) {
			case 205:
				int highestWidth = 0;
				for (Module module : getModsInCategory(Category.values()[categoryTab])) {
					String name = StringUtil.capitalize(module.getName().toLowerCase());
					int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
					highestWidth = Math.max(stringWidth, highestWidth);
				}
				baseModWidth = highestWidth + 6;
				baseModHeight = getModsInCategory(Category.values()[categoryTab]).size() * 14 + 2;
				modTargetPosition = modPosition = categoryTargetPosition;
				modTab = 0;

				section = Section.MODS;
				break;
			case 208:
				categoryTab++;
				categoryTargetPosition += 14;
				if (categoryTab <= Category.values().length - 1) {
					break;
				}
				transitionQuickly = true;
				categoryTargetPosition = 14;
				categoryTab = 0;

				break;
			case 200:
				categoryTab--;
				categoryTargetPosition -= 14;
				if (categoryTab >= 0) {
					break;
				}
				transitionQuickly = true;
				categoryTargetPosition = 14 + (Category.values().length - 1) * 14;
				categoryTab = Category.values().length - 1;
				break;
			}
		} else if (section == Section.MODS) {
			switch (key) {
			case 203:
				section = Section.CATEGORY;
				break;
			case 205:
				Module mod = (Module) getModsInCategory(Category.values()[categoryTab]).get(modTab);
				mod.toggle();
				break;
			case 208:
				modTab += 1;
				modTargetPosition += 14;
				if (modTab > getModsInCategory(Category.values()[categoryTab]).size() - 1) {
					transitionQuickly = true;
					modTargetPosition = categoryTargetPosition;
					modTab = 0;
				}
				break;
			case 200:
				modTab -= 1;
				modTargetPosition -= 14;
				if (modTab < 0) {
					transitionQuickly = true;
					modTargetPosition = categoryTargetPosition + (getModsInCategory(Category.values()[categoryTab]).size() - 1) * 14;
					modTab = getModsInCategory(Category.values()[categoryTab]).size() - 1;
				}
				break;
			}
		}
	}

	protected static void updateBars() {
		long timeDifference = System.currentTimeMillis() - lastUpdateTime;
		lastUpdateTime = System.currentTimeMillis();

		int increment = transitionQuickly ? 100 : 20;
		increment = Math.max(1, Math.round((float) (increment * timeDifference / 100L)));
		if (categoryPosition < categoryTargetPosition) {
			categoryPosition += increment;
			if (categoryPosition >= categoryTargetPosition) {
				categoryPosition = categoryTargetPosition;
				transitionQuickly = false;
			}
		} else if (categoryPosition > categoryTargetPosition) {
			categoryPosition -= increment;
			if (categoryPosition <= categoryTargetPosition) {
				categoryPosition = categoryTargetPosition;
				transitionQuickly = false;
			}
		}
		if (modPosition < modTargetPosition) {
			modPosition += increment;
			if (modPosition >= modTargetPosition) {
				modPosition = modTargetPosition;
				transitionQuickly = false;
			}
		} else if (modPosition > modTargetPosition) {
			modPosition -= increment;
			if (modPosition <= modTargetPosition) {
				modPosition = modTargetPosition;
				transitionQuickly = false;
			}
		}
	}

	protected static List<Module> getModsInCategory(Category category) {
		List<Module> modList = new ArrayList();
		Iterator tab = Axis.getAxis().getModuleManager().getContents().iterator();

		while (tab.hasNext()) {
			Module mod = (Module) tab.next();
			if (mod.getCategory() == category) {
				modList.add(mod);
			}
		}
		return modList;
	}

	protected static enum Section {
		CATEGORY, MODS;
	}
}
