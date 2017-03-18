package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.module.modules.movement.speed.SpeedMode;
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
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.potion.Potion;

public class Speed extends Module {

	public double speed;
	public static boolean canStep;
	public static double yOffset;
	public boolean cancel;
	public final Value<SpeedMode> currentMode = new Value<SpeedMode>("speed_mode", new Bhop(this));
	public Speed speed1 = this;

	public Speed() {
		super("Speed", 0x00BFFF, Category.MOVEMENT);
		setTag(currentMode.getValue().getName());
		Axis.getAxis().getCommandManager().getContents().add(new Command("speed", "<mode>", new String[] {}) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("bhop")) {
						currentMode.setValue(new Bhop(speed1));
						Logger.logChat("Speed Mode set to Bhop!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("latestbhop")) {
						currentMode.setValue(new LatestBhop(speed1));
						Logger.logChat("Speed Mode set to LatestBhop!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("MineZ")) {
						currentMode.setValue(new MineZ(speed1));
						Logger.logChat("Speed Mode set to MineZ!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("Other")) {
						currentMode.setValue(new Other(speed1));
						Logger.logChat("Speed Mode set to Other!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("AAC")) {
						currentMode.setValue(new AAC(speed1));
						Logger.logChat("Speed Mode set to AAC!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("LowHop")) {
						currentMode.setValue(new LowHop(speed1));
						Logger.logChat("Speed Mode set to LowHop!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("Yport")) {
						currentMode.setValue(new Yport(speed1));
						Logger.logChat("Speed Mode set to Yport!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("onGround")) {
						currentMode.setValue(new onGround(speed1));
						Logger.logChat("Speed Mode set to onGround!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("MotionTimer")) {
						currentMode.setValue(new MotionTimer(speed1));
						Logger.logChat("Speed Mode set to MotionTimer!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("Bhop2")) {
						currentMode.setValue(new Bhop2(speed1));
						Logger.logChat("Speed Mode set to Bhop2!");
						setTag(currentMode.getValue().getName());
					} else if (message.split(" ")[2].equalsIgnoreCase("Hop")) {
						currentMode.setValue(new Hop(speed1));
						Logger.logChat("Speed Mode set to Hop!");
						setTag(currentMode.getValue().getName());
					} else {
						Logger.logChat("Option not valid! Available options: Bhop, LatestBhop, MineZ, Other, AAC, LowHop, Yport, onGround,  MotionTimer, Hop.");
					}
				} else {
					Logger.logChat("Option not valid! Available options: Bhop, LatestBhop, MineZ, Other, AAC, LowHop, Yport, onGround,  MotionTimer, Hop.");
				}
			}
		});
	}

	private void onMove(MoveEvent event) {
		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()) {
			return;
		}
		currentMode.getValue().onMove(event);
	}

	private void onUpdate(UpdateEvent event) {
		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()) {
			return;
		}
		currentMode.getValue().onUpdate(event);
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2872D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && this.currentMode.getValue() instanceof Hop) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	public void onEnabled() {
		super.onEnabled();
		this.cancel = false;
		if (AutoSetting.setting.getValue().equalsIgnoreCase("Anni")) {
			if (currentMode.getValue() instanceof Hop) {
				return;
			}
			currentMode.setValue(new MineZ(speed1));
			setTag(currentMode.getValue().getName());
		} else if (AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			currentMode.setValue(new Bhop2(speed1));
			setTag(currentMode.getValue().getName());
		}
	}

	public void onDisabled() {
		super.onDisabled();
		mc.timer.timerSpeed = 1.0f;
		if (mc.thePlayer != null) {
			this.mc.thePlayer.stepHeight = 0.6f;
		}
		Speed.canStep = true;
		Speed.yOffset = 0.0;
	}

}
