package axis.module.modules.render;

import axis.Axis;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class AntiFC extends Module {

	public AntiFC() {
		super("AntiFC", 9623002, Category.RENDER);
		setDisplayName("Anti FC");
	}
}