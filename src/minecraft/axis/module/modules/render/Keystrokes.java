package axis.module.modules.render;

import axis.Axis;
import axis.event.events.DrawScreenEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.ColorUtil;
import axis.util.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;

public class Keystrokes extends Module {

	public Keystrokes() {
		super("Keystrokes", 9623002, Category.RENDER);
	}

	public void onDraw(DrawScreenEvent event) {
    	ScaledResolution scaledRes = new ScaledResolution(mc);
		float h = scaledRes.getScaledHeight() - 70;
		float f = scaledRes.getScaledWidth() - 50;
		if(Axis.getAxis().getModuleManager().getModuleByName("GhoastHUD").isEnabled()){
			h = f + 20;
		}
		int i = Integer.MIN_VALUE;
		int o = ColorUtil.color(255, 255, 255, 150);
		if(mc.gameSettings.keyBindForward.pressed){
			RenderHelper.drawRect(f, h - 22, f + 20, h + 20 - 22, o);
			HUD.Minecraft18.drawStringWithShadow("§0W", f + 7, h - 16, -1);
		}else{
	    	RenderHelper.drawRect(f, h - 22, f + 20, h + 20 - 22, i);
	    	HUD.Minecraft18.drawStringWithShadow("W", f + 7, h - 16, -1);
		}
		if(mc.gameSettings.keyBindLeft.pressed){
			RenderHelper.drawRect(f - 22, h, f + 20 - 22, h + 20, o);
			HUD.Minecraft18.drawStringWithShadow("§0A", f + 7 - 22, h + 6, -1);
		}else{
	    	RenderHelper.drawRect(f - 22, h, f + 20 - 22, h + 20, i);
	    	HUD.Minecraft18.drawStringWithShadow("A", f + 7 - 22, h + 6, -1);
		}
		if(mc.gameSettings.keyBindRight.pressed){
			RenderHelper.drawRect(f + 22, h, f + 20 + 22, h + 20, o);
			HUD.Minecraft18.drawStringWithShadow("§0D", f + 7 + 22, h + 6, -1);
		}else{
	    	RenderHelper.drawRect(f + 22, h, f + 20 + 22, h + 20, i);
	    	HUD.Minecraft18.drawStringWithShadow("D", f + 7 + 22, h + 6, -1);
		}
		if(mc.gameSettings.keyBindBack.pressed){
	    	RenderHelper.drawRect(f, h, f + 20, h + 20, o);
	    	HUD.Minecraft18.drawStringWithShadow("§0S", f + 7, h + 6, -1);
		}else{
	    	RenderHelper.drawRect(f, h, f + 20, h + 20, i);
	    	HUD.Minecraft18.drawStringWithShadow("S", f + 7, h + 6, -1);
		}
		if(mc.gameSettings.keyBindAttack.pressed){
			RenderHelper.drawRect(f - 22, h + 22, f + 20 - 22 + 11, h + 42, o);
			HUD.Minecraft18.drawStringWithShadow("§0LMB", f + 7 - 22, h + 6 + 22, -1);
		}else{
	    	RenderHelper.drawRect(f - 22, h + 22, f + 20 - 22 + 11, h + 42, i);
	    	HUD.Minecraft18.drawStringWithShadow("LMB", f + 7 - 22, h + 6 + 22, -1);
		}
		if(mc.gameSettings.keyBindUseItem.pressed){
			RenderHelper.drawRect(f - 22 + 33, h + 22, f + 20 - 22 + 11 + 33, h + 42, o);
			HUD.Minecraft18.drawStringWithShadow("§0RMB", f + 7 - 22 + 33, h + 6 + 22, -1);
		}else{
			RenderHelper.drawRect(f - 22 + 33, h + 22, f + 20 - 22 + 11 + 33, h + 42, i);
	    	HUD.Minecraft18.drawStringWithShadow("RMB", f + 7 - 22 + 33, h + 6 + 22, -1);
		}
	}
}
