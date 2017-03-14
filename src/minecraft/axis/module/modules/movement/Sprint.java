package axis.module.modules.movement;

import axis.event.Event.State;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.value.Value;
import net.minecraft.item.ItemBow;

public class Sprint extends Module {
	private final Value<Boolean> particle = new Value("sprint_particle", Boolean.valueOf(false));

	public Sprint() {
		super("Sprint", -7536856, ModuleManager.Category.MOVEMENT);
		this.setEnabled(true);
	}

	public void onUpdate(UpdateEvent event) {
		if (event.state == State.PRE) {
			mc.thePlayer.setSprinting(canSprint());
		}
	}

    public boolean canSprint() {
        boolean movingForward = mc.thePlayer.movementInput.moveForward > 0;
        boolean strafing = mc.thePlayer.movementInput.moveStrafe != 0;
        boolean moving = movingForward && strafing || movingForward;

        boolean sneaking = mc.thePlayer.isSneaking();
        boolean collided = mc.thePlayer.isCollidedHorizontally;
        boolean hungry = mc.thePlayer.getFoodStats().getFoodLevel() <= 6;
        boolean creative = mc.playerController.isInCreativeMode();

        return moving && !sneaking && !collided && (!hungry && !creative || creative) && !shouldSlowdown();
    }

    public boolean shouldSlowdown() {
        if (mc.thePlayer.getItemInUse() != null) {
            if (mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)
                //return true;
            return false;
            int maxUseDuration = mc.thePlayer.getItemInUse().getMaxItemUseDuration();
            if (mc.thePlayer.getItemInUseDuration() > maxUseDuration - 6)
                //return true;
            return false;
        }

        return false;
    }
}