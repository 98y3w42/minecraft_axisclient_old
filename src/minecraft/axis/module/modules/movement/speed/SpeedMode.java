package axis.module.modules.movement.speed;

import axis.Axis;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.modes.AAC;
import axis.module.modules.movement.speed.modes.Bhop;
import axis.module.modules.movement.speed.modes.Bhop2;
import axis.module.modules.movement.speed.modes.Hop;
import axis.module.modules.movement.speed.modes.LatestBhop;
import axis.module.modules.movement.speed.modes.LowHop;
import axis.module.modules.movement.speed.modes.MineZ;
import axis.module.modules.movement.speed.modes.MotionTimer;
import axis.module.modules.movement.speed.modes.Other;
import axis.module.modules.movement.speed.modes.Yport;
import axis.module.modules.movement.speed.modes.onGround;
import net.minecraft.client.Minecraft;

public abstract class SpeedMode {
	private String name;
	protected final Speed speed;
	protected final Minecraft mc = Minecraft.getMinecraft();

	public SpeedMode(String name, Speed speed) {
		this.name = name;
		this.speed = speed;
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

	public static SpeedMode getMode(String name, Speed speed) {
		if (name.indexOf("Bhop") != -1 && name.indexOf("Latest") == -1) {
			speed.setTag("Bhop");
			return new Bhop(speed);
		} else if (name.indexOf("LatestBhop") != -1) {
			speed.setTag("LatestBhop");
			return new LatestBhop(speed);
		} else if (name.indexOf("MineZ") != -1) {
			speed.setTag("MineZ");
			return new MineZ(speed);
		} else if (name.indexOf("Other") != -1) {
			speed.setTag("Other");
			return new Other(speed);
		} else if (name.indexOf("AAC") != -1) {
			speed.setTag("AAC");
			return new AAC(speed);
		} else if (name.indexOf("LowHop") != -1) {
			speed.setTag("LowHop");
			return new LowHop(speed);
		} else if (name.indexOf("Yport") != -1) {
			speed.setTag("Yport");
			return new Yport(speed);
		} else if (name.indexOf("onGround") != -1) {
			speed.setTag("onGround");
			return new onGround(speed);
		} else if (name.indexOf("MotionTimer") != -1) {
			speed.setTag("MotionTimer");
			return new MotionTimer(speed);
		} else if (name.indexOf("Bhop2") != -1) {
			speed.setTag("Bhop2");
			return new Bhop2(speed);
		} else if (name.indexOf("Hop") != -1) {
			speed.setTag("Hop");
			return new Hop(speed);
		} else {
			speed.setTag(speed.currentMode.getDefaultValue().getName());
			return ((Speed) Axis.getAxis().getModuleManager().getModuleByName("Speed")).currentMode.getDefaultValue();
		}
	}
}
