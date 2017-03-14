package axis.module.modules.render;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.event.events.Render3DEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
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

public class Viewclip extends Module {

	public Viewclip() {
		super("ViewClip", 9623002, Category.RENDER);
	}
}