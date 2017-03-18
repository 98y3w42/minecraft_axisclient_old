package axis.module.modules.render;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.command.Command;
import axis.event.events.Render3DEvent;
import axis.management.managers.ModuleManager;
import axis.module.Module;
import axis.util.Logger;
import axis.util.RenderUtils;
import axis.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

public class StorageESP
		extends Module {
	public final Value<String> mode = new Value("storageesp_mode", "Outline");
	public static boolean chest = true;
	public static boolean dispenser = true;
	public static boolean enderChest = true;
	public boolean render = true;

	public StorageESP() {
		super("StorageESP", -6165654, ModuleManager.Category.RENDER);
		setTag(mode.getValue());
		Axis.getAxis().getCommandManager().getContents().add(new Command("storageesp", "<mode>", new String[] { "chestesp", "storage" }) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("mode")) {
					if (message.split(" ")[2].equalsIgnoreCase("Outline")) {
						mode.setValue("Outline");
					} else if (message.split(" ")[2].equalsIgnoreCase("Boxes")) {
						mode.setValue("Boxes");
					}
					Logger.logChat("StorageESP Mode is " + mode.getValue());
					setTag(mode.getValue());
				}
			}
		});
	}

	private void onRender(Render3DEvent event) {
		if (mode.getValue().equals("Boxes")) {
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
		} else if (mode.getValue().equals("Outline")) {
			EntityLivingBase saveEnt = null;
			TileEntity saveTile = null;

			Boolean oldFancyGraphics = Boolean.valueOf(mc.gameSettings.fancyGraphics);
			mc.gameSettings.fancyGraphics = false;
			this.render = false;
			float gamma = mc.gameSettings.gammaSetting;
			mc.gameSettings.gammaSetting = 100000.0F;

			checkSetupFBO();
			outlineOne();
			renderEntitys();
			outlineTwo();
			renderEntitys();
			outlineThree();
			renderEntitys();
			outlineFour();
			renderEntitys();
			outlineFive();
			outlineOne();
			renderTileEntity();
			outlineTwo();
			renderTileEntity();
			outlineThree();
			renderTileEntity();
			outlineFour();
			renderTileEntity();
			outlineFive();
			this.render = true;
			mc.gameSettings.gammaSetting = gamma;
			mc.gameSettings.fancyGraphics = oldFancyGraphics.booleanValue();

			setColor(new Color(255, 255, 255, 255));
		}
	}

	private void drawEsp(TileEntity ent, float pTicks) {
		if (mode.getValue().equals("Boxes")) {
			double x1 = ent.getPos().getX() - mc.getRenderManager().viewerPosX;
			double y1 = ent.getPos().getY() - mc.getRenderManager().viewerPosY;
			double z1 = ent.getPos().getZ() - mc.getRenderManager().viewerPosZ;
			AxisAlignedBB box = new AxisAlignedBB(x1, y1, z1, x1 + 1.0D, y1 + 1.0D, z1 + 1.0D);
			if ((ent instanceof TileEntityChest)) {
				TileEntityChest chest = (TileEntityChest) TileEntityChest.class.cast(ent);
				if (chest.adjacentChestZPos != null) {
					box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 0.9375D, y1 + 0.875D, z1 + 1.9375D);
				} else if (chest.adjacentChestXPos != null) {
					box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 1.9375D, y1 + 0.875D, z1 + 0.9375D);
				} else {
					if ((chest.adjacentChestZPos != null) || (chest.adjacentChestXPos != null) || (chest.adjacentChestZNeg != null) || (chest.adjacentChestXNeg != null)) {
						return;
					}
					box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 0.9375D, y1 + 0.875D, z1 + 0.9375D);
				}
			} else if (((ent instanceof TileEntityChest)) || ((ent instanceof TileEntityEnderChest)) || ((ent instanceof TileEntityBrewingStand))) {
				box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 0.9375D, y1 + 0.875D, z1 + 0.9375D);
			}
			GlStateManager.E();
			GL11.glLineWidth(0.75F);

			disableLighting();
			if ((ent instanceof TileEntityEnderChest)) {
				GL11.glColor4f(0.5F, 0.2F, 1.0F, 0.4F);
				RenderGlobal.func_181561_a(box);

				GL11.glColor4f(0.5F, 0.2F, 1.0F, 0.2F);
				RenderUtils.drawFilledBox(box);
			} else if ((ent instanceof TileEntityChest)) {
				float red = (HUD.color1 >> 16 & 0xFF) / 255.0F;
				float blue = (HUD.color1 >> 8 & 0xFF) / 255.0F;
				float green = (HUD.color1 & 0xFF) / 255.0F;
				float alpha = (HUD.color1 >> 24 & 0xFF) / 255.0F;
				float[] color1;
				color1 = new float[] { red, blue, green, alpha };
				GL11.glColor4f(color1[0], color1[1], color1[2], 0.4F);
				RenderGlobal.func_181561_a(box);

				GL11.glColor4f(color1[0], color1[1], color1[2], 0.2F);
				RenderUtils.drawFilledBox(box);
			} else if ((ent instanceof TileEntityFurnace)) {
				float red = (HUD.hexcolor >> 16 & 0xFF) / 255.0F;
				float blue = (HUD.hexcolor >> 8 & 0xFF) / 255.0F;
				float green = (HUD.hexcolor & 0xFF) / 255.0F;
				float alpha = (HUD.hexcolor >> 24 & 0xFF) / 255.0F;
				float[] color1;
				color1 = new float[] { red, blue, green, alpha };
				GL11.glColor4f(color1[0], color1[1], color1[2], 0.4F);
				RenderGlobal.func_181561_a(box);

				GL11.glColor4f(color1[0], color1[1], color1[2], 0.2F);
				RenderUtils.drawFilledBox(box);
			}
			if ((ent instanceof TileEntityBrewingStand)) {
				GL11.glColor4f(0.8F, 0.4F, 0.3F, 0.4F);
				RenderGlobal.func_181561_a(box);

				GL11.glColor4f(0.8F, 0.4F, 0.3F, 0.2F);
				RenderUtils.drawFilledBox(box);
			}
			if ((ent instanceof TileEntityMobSpawner)) {
				GL11.glColor4f(0.3F, 0.7F, 0.3F, 0.4F);
				RenderGlobal.func_181561_a(box);

				GL11.glColor4f(0.3F, 0.7F, 0.3F, 0.2F);
				RenderUtils.drawFilledBox(box);
			} else if (((ent instanceof TileEntityHopper)) || ((ent instanceof TileEntityDropper)) || ((ent instanceof TileEntityDispenser))) {
				GL11.glColor4f(0.2F, 1.2F, 1.2F, 0.4F);
				RenderGlobal.func_181561_a(box);

				GL11.glColor4f(0.2F, 1.2F, 1.2F, 0.2F);
				RenderUtils.drawFilledBox(box);
			}
			GlStateManager.enableLighting();
			GlStateManager.F();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
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

	public EntityLivingBase renderEntitys() {
		EntityLivingBase saveEnt = null;
		for (Object o : mc.theWorld.loadedEntityList) {
			if (isValid(o).booleanValue()) {
				EntityLivingBase entity = (EntityLivingBase) o;
				Frustum var8 = new Frustum();
				Entity var9 = mc.getRenderViewEntity();
				double var10 = var9.lastTickPosX + (var9.posX - var9.lastTickPosX) * mc.timer.renderPartialTicks;

				double var12 = var9.lastTickPosY + (var9.posY - var9.lastTickPosY) * mc.timer.renderPartialTicks;

				double var14 = var9.lastTickPosZ + (var9.posZ - var9.lastTickPosZ) * mc.timer.renderPartialTicks;

				var8.setPosition(var10, var12, var14);
				if (var8.isBoundingBoxInFrustum(entity.getEntityBoundingBox())) {
					if (saveEnt == null) {
						saveEnt = entity;
					}
					setColor(entity);
					mc.renderGlobal.renderedEntity = entity;
					Boolean water = Boolean.valueOf(entity.isInWater());
					entity.inWater = false;
					mc.getRenderManager().renderEntityStatic(entity, mc.timer.renderPartialTicks, true);
					entity.inWater = water.booleanValue();
					mc.renderGlobal.renderedEntity = null;
				}
			}
		}
		return saveEnt;
	}

	public void setColor(Entity ent) {
		if ((ent instanceof EntityPlayer)) {
			setColor(new Color(200, 160, 60, 255));
		}
	}

	public void setColor(TileEntity renderEntity) {
		if ((renderEntity instanceof TileEntityChest)) {
			float red = (HUD.color1 >> 16 & 0xFF) / 255.0F;
			float blue = (HUD.color1 >> 8 & 0xFF) / 255.0F;
			float green = (HUD.color1 & 0xFF) / 255.0F;
			float alpha = (HUD.color1 >> 24 & 0xFF) / 255.0F;
			float[] color1;
			color1 = new float[] { red, blue, green, alpha };
			setColor(new Color(color1[0], color1[1], color1[2]));
		} else if ((renderEntity instanceof TileEntityEnderChest)) {
			setColor(new Color(204, 0, 204, 255));
		} else {
			setColor(new Color(60, 200, 60, 255));
		}
	}

	public Boolean isValid(Object ent) {
		return Boolean.valueOf(false);
	}

	public static void setColor(Color c) {
		GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c
				.getAlpha() / 255.0F);
	}

	public TileEntity renderTileEntity() {
		TileEntity saveEnt = null;
		for (Object o : mc.theWorld.loadedTileEntityList) {
			if (((o instanceof TileEntityChest)) || ((o instanceof TileEntityEnderChest))) {
				TileEntity entity = (TileEntity) o;
				Frustum var8 = new Frustum();
				Entity var9 = mc.getRenderViewEntity();
				double var10 = var9.posX + (var9.posX - var9.lastTickPosX) * mc.timer.renderPartialTicks;
				double var12 = var9.lastTickPosY + (var9.posY - var9.lastTickPosY) * mc.timer.renderPartialTicks;

				double var14 = var9.lastTickPosZ + (var9.posZ - var9.lastTickPosZ) * mc.timer.renderPartialTicks;

				var8.setPosition(var10, var12, var14);
				if (var8.isBoxInFrustum(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), entity
						.getPos().getX() + 1, entity.getPos().getY() + 1, entity.getPos().getZ() + 1)) {
					if (saveEnt == null) {
						saveEnt = entity;
					}
					setColor(entity);

					TileEntityRendererDispatcher.instance.renderTileEntity(entity, mc.timer.renderPartialTicks, -1);
				}
			}
		}
		return saveEnt;
	}

	public static void checkSetupFBO() {
		Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
		if (fbo != null) {
			if (fbo.depthBuffer > -1) {
				setupFBO(fbo);

				fbo.depthBuffer = -1;
			}
		}
	}

	public static void setupFBO(Framebuffer fbo) {
		EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);

		int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();

		EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);

		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041,
				Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);

		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);

		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	}

	public void outlineOne() {
		GL11.glPushAttrib(1048575);
		GL11.glDisable(3008);
		GL11.glDisable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.0f);
		GL11.glEnable(2848);
		GL11.glEnable(2960);
		GL11.glClear(1024);
		GL11.glClearStencil(15);
		GL11.glStencilFunc(512, 1, 15);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glPolygonMode(1028, 6913);
	}

	public void outlineTwo() {
		GL11.glStencilFunc(512, 0, 15);
		GL11.glStencilOp(7681, 7681, 7681);
		GL11.glPolygonMode(1028, 6914);
	}

	public void outlineThree() {
		GL11.glStencilFunc(515, 1, 15);
		GL11.glStencilOp(7680, 7680, 7680);
		GL11.glPolygonMode(1028, 6913);
	}

	public void outlineFour() {
		GL11.glDepthMask(false);
		GL11.glDisable(2929);
		GL11.glEnable(10754);
		GL11.glPolygonOffset(1.0F, -2000000.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	}

	public void outlineFive() {
		GL11.glPolygonOffset(1.0F, 2000000.0F);
		GL11.glDisable(10754);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(2960);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glDisable(3042);
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glEnable(3008);

		GL11.glPopAttrib();
	}
}
