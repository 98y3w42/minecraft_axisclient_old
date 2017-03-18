package axis.module.modules.render;

import axis.Axis;
import axis.command.Command;
import axis.event.events.Render3DEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.ColorUtil;
import axis.util.Logger;
import axis.util.RenderUtils;
import axis.value.Value;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;

public class ESP
		extends Module {
	private static boolean players = true;
	private static boolean monsters;
	private static boolean animals;
	private boolean outline;
	private int state;
	private float r = 0.33F;
	private float g = 0.34F;
	private float b = 0.33F;
	private static Value<String> target = new Value("esp_target", "Invis");

	public ESP() {
		super("ESP", 9623002, Category.RENDER);
		setTag(target.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("esp", "<target>", new String[] { "", "esptarget" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("target")) {
					if (message.split(" ")[2].equalsIgnoreCase("All")) {
						target.setValue("All");
						setTag(target.getValue());
					} else if (message.split(" ")[2].equalsIgnoreCase("Invis")) {
						target.setValue("Invis");
						setTag(target.getValue());
					}
					Logger.logChat("Target type set to " + target.getValue());
				}
			}
		});
	}

	private void onRender3D(Render3DEvent event) {
		if (this.outline) {
			return;
		}
		for (Object o : this.mc.theWorld.loadedEntityList) {
			if (((o instanceof EntityLivingBase)) && (o != this.mc.thePlayer)) {
				EntityLivingBase entity = (EntityLivingBase) o;

				int color = ColorUtil.color(0, 255, 0, 255);
				int thingyt = 1174405120;
				if (entity.hurtTime != 0) {
					color = -6750208;
					thingyt = 1184432128;
				}
				if (Axis.getAxis().getFriendManager().isFriend(entity.getName())) {
					color = HUD.color1;
				}
				if (checkValidity(entity)) {
					RenderUtils.drawEsp(entity, event.partialTicks, color, thingyt);
				}
			}
		}
	}

	private static boolean checkValidity(EntityLivingBase entity) {
		if ((entity instanceof EntityPlayer)) {
			if (target.getValue().equals("Invis")) {
				if (!entity.isInvisible()) {
					return false;
				}
			}
			if (players) {
				return true;
			}
			return false;
		}
		if ((monsters) && ((entity instanceof EntityMob))) {
			return true;
		}
		if ((animals) && ((entity instanceof EntityAnimal))) {
			return true;
		}
		if ((animals) && ((entity instanceof EntityBat))) {
			return true;
		}
		return false;
	}
}
