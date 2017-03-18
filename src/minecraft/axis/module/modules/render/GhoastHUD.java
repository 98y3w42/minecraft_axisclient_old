package axis.module.modules.render;

import axis.Axis;
import axis.event.EventTarget;
import axis.event.events.DrawScreenEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class GhoastHUD extends Module {


	protected static final Minecraft mc = Minecraft.getMinecraft();
	public boolean Sprint = false;
	public boolean Sneak = false;
	private boolean sneakkyeheld = false;
	private int sneaking = 0;
	private boolean sprintkyeheld = false;
	private int sprinting = 0;
	private boolean sneak1 = false;

	public GhoastHUD() {
	      super("GhoastHUD", 0x00BFFF, Category.RENDER);
	}

	@EventTarget
	public void onDraw(DrawScreenEvent event) {
		ScaledResolution scaledRes = new ScaledResolution(mc);
		if (event.getMessage() == null) {
			mc.fontRendererObj.drawStringWithShadow(Axis.getAxis().getName() + " " + Axis.getAxis().getVersion(), 2.0F, 2.0F, HUD.hexcolor);
			if (sneakkyeheld && !mc.gameSettings.keyBindSneak.pressed) {
				if (sprinting > 100) {
					Sprint = false;
				}
				sprintkyeheld = false;
				sprinting = 0;
			}

			if ((!Sneak || !mc.gameSettings.keyBindSneak.pressed) && mc.gameSettings.keyBindSprint.pressed && (!Axis.getAxis().getModuleManager().getModuleByName("Sprint").isEnabled())) {
				mc.fontRendererObj.drawStringWithShadow("[Sprinting (Key Held)]", 2.0F, 12.0F, -1);
				sprintkyeheld = false;
				sprinting++;
			} else if ((!Sneak || !mc.gameSettings.keyBindSneak.pressed) && (Axis.getAxis().getModuleManager().getModuleByName("Sprint").isEnabled())){
				if(!sneak1){
		            mc.fontRendererObj.drawStringWithShadow("[Sprinting (Toggled)]", 2.0F, 12.0F, -1);
				}
			}

			if (sneakkyeheld && !mc.gameSettings.keyBindSneak.pressed) {
				if (sneaking > 100) {
					Sneak = false;
				}
				sneakkyeheld = false;
				sneaking = 0;
			}
			if (mc.gameSettings.keyBindSneak.pressed) {
				sneak1 = true;
				mc.fontRendererObj.drawStringWithShadow("[Sneaking (Key Held)]", 2.0F, 12.0F, -1);
				sneakkyeheld = true;
				sneaking++;
			} else if (Sneak && (!Axis.getAxis().getModuleManager().getModuleByName("Sprint").isEnabled())) {
				sneak1 = true;
				mc.fontRendererObj.drawStringWithShadow("[Sneaking (Toggled)]", 2.0F, 12.0F, -1);
			}else{
				sneak1 = false;
			}
		}
	}
}
