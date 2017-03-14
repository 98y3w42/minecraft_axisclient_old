package axis.module.modules.render;

import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.event.Event;
import axis.event.events.TickEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class Chams extends Module {

	public Chams() {
		super("Chams", 9623002, Category.RENDER);
	}
}