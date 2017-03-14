package axis.module.modules.combat;

import axis.Axis;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class SwordAnimation extends Module {

	public SwordAnimation() {
		super("SwordAnimation", 9623002, Category.COMBAT);
		setDisplayName("Sword");
	}
}
