package axis.module.modules.render;

import axis.Axis;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class Animation extends Module {

	public Animation() {
		super("Animation");
		this.setCategory(Category.RENDER);
		this.setEnabled(true);
	}
}
