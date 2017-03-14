package axis.module.modules.render;

import axis.event.events.HurtCamEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class NoHurtCam extends Module{

	public NoHurtCam() {
		  super("NoHurtCam", 9623002, Category.RENDER);
	}

	public void onHurtCam(HurtCamEvent event){
		event.setCancelled(true);
	}

}
