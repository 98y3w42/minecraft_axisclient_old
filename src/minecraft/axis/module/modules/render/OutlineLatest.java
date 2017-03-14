package axis.module.modules.render;

import axis.event.events.Render3DEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.RenderUtils;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class OutlineLatest
		extends Module {

	public OutlineLatest() {
		super("OutlinedESP", -1, ModuleManager.Category.RENDER);
		setDisplayName("Outlined ESP");
	}

	private void onRender(Render3DEvent event) {
		GlStateManager.E();
		Iterator var3 = mc.theWorld.loadedTileEntityList.iterator();
		for (;;) {
			if (!var3.hasNext()) {
				GlStateManager.F();
				return;
			}
			TileEntity ent = (TileEntity) var3.next();

			drawEsp(ent, Render3DEvent.partialTicks);
		}
	}

	private void drawEsp(TileEntity ent, float pTicks) {
		AxisAlignedBB box = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		GlStateManager.E();
		GL11.glLineWidth(0.75F);

		disableLighting();
		GL11.glColor4f(0.5F, 0.2F, 1.0F, 0.4F);
		RenderGlobal.func_181561_a(box);

		GL11.glColor4f(0.5F, 0.2F, 1.0F, 0.2F);
		RenderUtils.drawFilledBox(box);

		GlStateManager.enableLighting();
		GlStateManager.F();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void disableLighting() {
		OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(3553);
		OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(2896);
		GL11.glDisable(3553);
	}
}
