package axis.module.modules.render;

import axis.Axis;
import axis.event.Event;
import axis.event.EventTarget;
import axis.event.events.DrawScreenEvent;
import axis.event.events.KeyboardEvent;
import axis.event.events.UpdateEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.ui.tabgui.TabGui;
import axis.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class DebugInfo extends Module {

	private String sendpacket;
	private String receivepacket;

	public DebugInfo() {
	      super("DebugInfo", 0x00BFFF, Category.RENDER);
	}

	@EventTarget
	public void onDraw(DrawScreenEvent event){
        String motionx = "MotionX: \u00a7f" + (float)(mc.thePlayer.motionX);
		String motiony = "MotionY: \u00a7f" + (float)(mc.thePlayer.motionY);
		String motionz = "MotionZ: \u00a7f" + (float)(mc.thePlayer.motionZ);
		mc.fontRendererObj.drawStringWithShadow(motionx, 2.0F, 192.0F, HUD.hexcolor);
		mc.fontRendererObj.drawStringWithShadow(motiony, 2.0F, 202.0F, HUD.hexcolor);
		mc.fontRendererObj.drawStringWithShadow(motionz, 2.0F, 212.0F, HUD.hexcolor);
		mc.fontRendererObj.drawStringWithShadow("Timer: \u00a7f" + mc.timer.timerSpeed, 2.0F, 222.0F, HUD.hexcolor);
	}

}