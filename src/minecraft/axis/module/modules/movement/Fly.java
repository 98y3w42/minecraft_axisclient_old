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
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;

public class Fly extends Module {

	private double moveSpeed;
	private int state = 1;
	private int stage = 0;
	private boolean ss = false;

	public Fly() {
		super("Fly", 9623002, Category.MOVEMENT);
	}

	public void onMove(MoveEvent event) {
		event.setY(-0.000000000000001F);
		if (this.mc.thePlayer.movementInput.jump) {
			event.setY(0.8F);
		} else if (this.mc.thePlayer.movementInput.sneak) {
			event.setY(-0.8F);
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
		super.onEnabled();
	}

	public void onDisabled() {
		super.onDisabled();
		mc.timer.timerSpeed = 1.0f;
	}
}
