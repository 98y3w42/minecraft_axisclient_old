package axis.module.modules.movement;

import axis.event.Event;
import axis.event.events.MoveEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.LiquidUtils;
import axis.util.TimeHelper;
import net.minecraft.potion.Potion;

public class Glide extends Module {

	private TimeHelper time = new TimeHelper();
	private int state = 1;

	public Glide() {
		super("Glide", 0, ModuleManager.Category.MOVEMENT);
	}

	public void onEnabled() {
		mc.thePlayer.fallDistance = 0.0F;
		super.onEnabled();
	}

	public void onDisabled() {
		super.onDisabled();
		if(mc.thePlayer != null){
			mc.thePlayer.motionY = 0.0D;
		}
	}

	public void onMove(MoveEvent event) {
		if ((mc.thePlayer.isInWater()) || (mc.thePlayer.isOnLadder())) {
			return;
		}
		event.setY(0.0D);
		if ((!mc.thePlayer.movementInput.jump) && (!mc.thePlayer.movementInput.sneak) && (mc.thePlayer.movementInput.moveForward == 0.0D) && (mc.thePlayer.movementInput.moveStrafe == 0.0D)) {
			mc.thePlayer.motionX = (mc.thePlayer.motionZ = mc.thePlayer.motionY = 0.0D);
			event.setCancelled(true);
		} else if ((!mc.thePlayer.capabilities.isFlying) && (!mc.thePlayer.isSneaking())) {
			mc.thePlayer.motionY = 0.0D;
		} else if (mc.gameSettings.keyBindSneak.pressed) {
			mc.thePlayer.motionY = -0.3D;
		}
	}

	public void onUpdate(UpdateEvent event){
		if (event.state == Event.State.PRE) {
			Speed.canStep = true;
			if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f || !mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0.0f) {
				if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0.0f) {
					Speed.canStep = false;
				}
				return;
			}
			switch (this.state) {
			case 1:
				Speed.canStep = false;
				//event.y += 0.0001;
				++this.state;
				break;
			case 2:
				Speed.canStep = false;
				//event.y += 0.0002;
				++this.state;
				break;
			default:
				this.state = 1;
				if (!this.mc.thePlayer.isSneaking() && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && !this.mc.gameSettings.keyBindJump.isPressed() && !LiquidUtils.isOnLiquid()
						&& !LiquidUtils.isInLiquid()) {
					Speed.canStep = true;
					this.state = 1;
					break;
				}
				break;
			}
			Speed.yOffset = event.y - this.mc.thePlayer.posY;
		}
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2872D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}
}
