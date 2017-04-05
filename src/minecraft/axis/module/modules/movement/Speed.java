package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.events.AttackEvent;
import axis.event.events.MoveEvent;
import axis.event.events.StepEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.module.modules.exploits.AutoSetting;
import axis.module.modules.movement.speed.SpeedMode;
import axis.module.modules.movement.speed.modes.MineZ;
import axis.module.modules.movement.speed.modes.SlowHop;
import axis.util.Logger;
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;

public class Speed extends Module {

	public double speed;
	public static boolean canStep;
	public static double yOffset;
	public boolean cancel;
	public SpeedMode currentMode;
	public static String speedmode;
	private boolean step = false;

	public Speed() {
		super("Speed", 0x00BFFF, Category.MOVEMENT);
		Axis.getAxis().getCommandManager().getContents().add(new Command("speed", "<mode>", new String[] {}) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {

					if (SpeedMode.getModeByName(message.split(" ")[2]) != null) {
						currentMode = SpeedMode.getModeByName(message.split(" ")[2]);
						setTag(currentMode.getName());
						values.setValue("mode", currentMode.getName());
						Logger.logChat("Speed Mode set to " + currentMode.getName() + "!");
					} else {
						Logger.logChat("Option not valid! Available options: " + SpeedMode.getModes());
						return;
					}
				} else {
					Logger.logChat("Option not valid! Available options: mode");
				}
			}
		});
	}

	public void onValueSetup() {
		super.onValueSetup();
		values.addValue("mode", "Bhop");
	}

	private void onMove(MoveEvent event) {
		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled() || this.step) {
			return;
		}
		currentMode.onMove(event);
	}

	private void onUpdate(UpdateEvent event) {
		currentMode = SpeedMode.getModeByName((String) values.getValue("mode"));
		setTag(currentMode.getName());
		speedmode = currentMode.getName();
		this.step = false;

		if (Axis.getAxis().getModuleManager().getModuleByName("Freecam").isEnabled() || this.step) {
			return;
		}
		currentMode.onUpdate(event);
	}

	public void onAttack(AttackEvent event) {
		if (event.getEntity() != null) {
			Timer.timerSpeed = 1.0F;
			if (values.getValue("mode").equals("MineZ")) {
				MineZ.wait = 1;
			}
		}
	}

	public void onStep(StepEvent event){
		this.step = true;
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2872D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && SpeedMode.getModeByName((String) values.getValue("mode")) instanceof SlowHop) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	public void onEnabled() {
		super.onEnabled();
		if (AutoSetting.setting.getValue().equalsIgnoreCase("Anni")) {
			if (!values.getValue("mode").equals("SlowHop")) {
				values.setValue("mode", "minez");
			}
		} else if (AutoSetting.setting.getValue().equalsIgnoreCase("Hypixel")) {
			if (!values.getValue("mode").equals("SlowHop")) {
				values.setValue("mode", "bhop2");
			}
		}
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
