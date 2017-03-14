package axis.module.modules.render;

import axis.event.EventTarget;
import axis.event.events.DrawScreenEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.ui.chat.HexNewChat;
import axis.util.ColorUtil;
import axis.util.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;

public class Hotbar extends Module {
	public Hotbar() {
		super("Hotbar");
		this.setCategory(Category.RENDER);
	}
}
