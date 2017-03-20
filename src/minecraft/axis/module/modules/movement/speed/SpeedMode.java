package axis.module.modules.movement.speed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import axis.Axis;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import net.minecraft.client.Minecraft;

public abstract class SpeedMode {
	private String name;
	protected final Speed speed = (Speed)Axis.getAxis().getModuleManager().getModuleByName("Speed");
	protected final Minecraft mc = Minecraft.getMinecraft();

	public static List<SpeedMode> modes = new ArrayList();


	public SpeedMode(String name) {
		this.name = name;
		modes.add(this);
	}

	public static SpeedMode getModeByName(String name) {
        Iterator var3 = modes.iterator();

        while(var3.hasNext()) {
        	SpeedMode speedMode = (SpeedMode)var3.next();
           if(speedMode.getName().equalsIgnoreCase(name)) {
              return speedMode;
           }
        }

		return null;
	}

	public String getName() {
		return name;
	}

	public abstract void onMove(MoveEvent event);

	public abstract void onUpdate(UpdateEvent event);

	public void onEnabled() {

	}

	public void onDisabled() {

	}
}
