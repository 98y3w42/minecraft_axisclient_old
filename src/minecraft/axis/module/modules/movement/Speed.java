package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.module.modules.movement.speed.SpeedMode;
import axis.module.modules.movement.speed.modes.Hop;
import axis.util.Logger;
import net.minecraft.potion.Potion;

public class Speed extends Module {

	public double speed;
	public static boolean canStep;
	public static double yOffset;
	public boolean cancel;
	public SpeedMode currentMode;

	public Speed() {
		super("Speed", 0x00BFFF, Category.MOVEMENT);
		Axis.getAxis().getCommandManager().getContents().add(new Command("speed", "<mode>", new String[] {}) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("bhop")) {
						Logger.logChat("Speed Mode set to Bhop!");
					} else if (message.split(" ")[2].equalsIgnoreCase("latestbhop")) {
						Logger.logChat("Speed Mode set to LatestBhop!");
					} else if (message.split(" ")[2].equalsIgnoreCase("MineZ")) {
						Logger.logChat("Speed Mode set to MineZ!");
					} else if (message.split(" ")[2].equalsIgnoreCase("Other")) {
						Logger.logChat("Speed Mode set to Other!");
					} else if (message.split(" ")[2].equalsIgnoreCase("AAC")) {
						Logger.logChat("Speed Mode set to AAC!");
					} else if (message.split(" ")[2].equalsIgnoreCase("LowHop")) {
						Logger.logChat("Speed Mode set to LowHop!");
					} else if (message.split(" ")[2].equalsIgnoreCase("Yport")) {
						Logger.logChat("Speed Mode set to Yport!");
					} else if (message.split(" ")[2].equalsIgnoreCase("onGround")) {
						Logger.logChat("Speed Mode set to onGround!");
					} else if (message.split(" ")[2].equalsIgnoreCase("MotionTimer")) {
						Logger.logChat("Speed Mode set to MotionTimer!");
					} else if (message.split(" ")[2].equalsIgnoreCase("Bhop2")) {
						Logger.logChat("Speed Mode set to Bhop2!");
					} else if (message.split(" ")[2].equalsIgnoreCase("Hop")) {
						Logger.logChat("Speed Mode set to Hop!");
					} else {
						Logger.logChat("Option not valid! Available options: Bhop, LatestBhop, MineZ, Other, AAC, LowHop, Yport, onGround,  MotionTimer, Hop.");
						return;
					}

					currentMode = SpeedMode.getModeByName(message.split(" ")[2]);
					setTag(currentMode.getName());
					values.setValue("mode", currentMode.getName());
				} else {
					Logger.logChat("Option not valid! Available options: Bhop, LatestBhop, MineZ, Other, AAC, LowHop, Yport, onGround,  MotionTimer, Hop.");
				}
			}
		});
	}

	public void onValueSetup() {
		super.onValueSetup();
		values.addValue("mode", "Bhop");
	}


	private void onMove(MoveEvent event) {
		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()) {
			return;
		}
		currentMode.onMove(event);
	}

	private void onUpdate(UpdateEvent event) {
		if (AutoSetting.setting.getValue().equalsIgnoreCase("Anni")) {
			if (((String)values.getValue("mode")).equalsIgnoreCase("Hypixel")) {
				return;
			}
			values.setValue("mode", "minez");
		} else if (AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			values.setValue("mode", "bhop2");
		}

		currentMode = SpeedMode.getModeByName((String) values.getValue("mode"));
		setTag(currentMode.getName());

		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled()) {
			return;
		}
		currentMode.onUpdate(event);
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2872D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && SpeedMode.getModeByName((String)values.getValue("mode")) instanceof Hop) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	public void onEnabled() {
		super.onEnabled();
		this.cancel = false;
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
