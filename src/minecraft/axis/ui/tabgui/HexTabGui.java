package axis.ui.tabgui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import axis.Axis;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.render.HUD;
import axis.util.ColorUtil;
import axis.util.RenderHelper;
import axis.util.StringUtil;
import net.minecraft.client.Minecraft;

public class HexTabGui extends TabGui {

	private static int daley1;

	public static void init() {
		int highestWidth = 0;
		Category[] arrayOfCategory;
		int j = (arrayOfCategory = Category.values()).length;
		for (int i = 0; i < j; i++) {
			Category category = arrayOfCategory[i];

			String name = StringUtil.capitalize(category.name().toLowerCase());
			int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
			highestWidth = Math.max(stringWidth, highestWidth);
		}
		baseCategoryWidth = highestWidth + 6;
		baseCategoryHeight = Category.values().length * 14 + 2;
	}

	public static void render() {
		updateBars();
		int o = ColorUtil.color(0, 0, 0, 70);
		RenderHelper.drawRect(2.0F, 14.0F - 1, 2 + baseCategoryWidth, 14 + baseCategoryHeight - 1, o);
		RenderHelper.drawRect(3.0F, categoryPosition, 1 + baseCategoryWidth, categoryPosition + 14, HUD.hexcolor);

		int yPos = 15;
		int yPosBottom = 29;
		for (int i = 0; i < Category.values().length; i++) {
			Category category = Category.values()[i];
			String name = StringUtil.capitalize(category.name().toLowerCase());

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, baseCategoryWidth / 10 + 1, yPos + 3, -1);
			yPos += 14;
			yPosBottom += 14;
		}
		if (section == Section.MODS) {
			RenderHelper.drawRect(baseCategoryWidth + 4, categoryPosition - 1, baseCategoryWidth + 2 + baseModWidth, categoryPosition + getModsInCategory(Category.values()[categoryTab]).size() * 14 + 1, o);
			RenderHelper.drawRect(baseCategoryWidth + 5, modPosition, baseCategoryWidth + baseModWidth + 1, modPosition + 14, HUD.hexcolor);

			int yPos1 = categoryPosition;
			int yPosBottom1 = categoryPosition + 14;
			for (int i = 0; i < getModsInCategory(Category.values()[categoryTab]).size(); i++) {
				Module mod = (Module) getModsInCategory(Category.values()[categoryTab]).get(i);
				String name = mod.getName();

				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, baseCategoryWidth + baseModWidth / 10, yPos1 + 3, mod.isEnabled() ? -1 : -5723992);
				yPos1 += 14;
				yPosBottom1 += 14;
			}
		}
	}
}