package axis.module.modules.render;

import axis.Axis;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class FontShadow extends Module {

	public FontShadow() {
		super("FontShadow");
		this.setCategory(Category.RENDER);
		setDisplayName("Font");
	}
}
