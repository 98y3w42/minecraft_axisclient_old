package axis.module.modules.movement;

import axis.Axis;
import axis.command.Command;
import axis.event.Event.State;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Fly extends Module {

	private final Value<String> currentMode = new Value("fly_mode", "Skywalk");
	private double moveSpeed;
	private int state = 1;
	private int stage = 0;
	private boolean ss = false;

	public Fly() {
		super("Fly", 9623002, Category.MOVEMENT);
		setTag(currentMode.getValue());
		Axis.getCommandManager().getContents().add(new Command("fly", "<mode>", new String[] { "fly", "fy" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("Skywalk")) {
						currentMode.setValue("Skywalk");
						Logger.logChat("Fly Mode is " + currentMode.getValue());
					} else if (message.split(" ")[2].equalsIgnoreCase("Pos")) {
						currentMode.setValue("Pos");
						Logger.logChat("Fly Mode is " + currentMode.getValue());
					} else {
						Logger.logChat("Option not valid! Available options: Skywalk, Pos.");
					}
					setTag(currentMode.getValue());
				}
			}
		});
	}

	public void onMove(MoveEvent event) {
		if (currentMode.getValue().equalsIgnoreCase("Skywalk")) {
			event.setY(-0.000000000000001F);
			if (this.mc.thePlayer.movementInput.jump) {
				event.setY(0.8F);
			} else if (this.mc.thePlayer.movementInput.sneak) {
				event.setY(-0.8F);
			}
		} else {
			event.setY(0.0f);
			if (this.mc.thePlayer.movementInput.jump) {
				event.setY(0.4D);
			} else if (this.mc.thePlayer.movementInput.sneak) {
				event.setY(-0.4D);
			}
			Timer.timerSpeed = 1.0f;
		}
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == State.PRE) {
			if (!this.mc.thePlayer.movementInput.jump && !this.mc.thePlayer.movementInput.sneak && this.mc.thePlayer.movementInput.moveForward == 0.0D && this.mc.thePlayer.movementInput.moveStrafe == 0.0D) {
				this.mc.thePlayer.motionX = (this.mc.thePlayer.motionZ = this.mc.thePlayer.motionY = 0.0D);
				event.setCancelled(true);
			} else if (this.mc.thePlayer.movementInput.jump) {
				this.mc.thePlayer.motionY = 0.8D;
			} else if (this.mc.thePlayer.movementInput.sneak) {
				this.mc.thePlayer.motionY = -0.8D;
			} else {
				this.mc.thePlayer.motionY = 0.0D;
			}
		}
	}

	public void onEnabled() {
		if ((currentMode.getValue().equalsIgnoreCase("Pos")) && (mc.thePlayer != null)) {
			this.mc.thePlayer.motionY = 0.0D;
			stage = 0;
			mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.4D, this.mc.thePlayer.posZ);
		}
		super.onEnabled();
	}

	public void onDisabled() {
		super.onDisabled();
		mc.timer.timerSpeed = 1.0f;
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

}
