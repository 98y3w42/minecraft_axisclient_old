package axis.util;

import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.module.modules.render.HUD;
import axis.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class RenderUtils {
	private static final Value<Float> LINE_WIDTH = new Value<>("render_line_width", 1.0F);
	private static final Value<Boolean> ANTI_ALIASING = new Value<>("render_anti_aliasing", false);
	private static final Value<Boolean> WORLD_BOBBING = new Value<>("render_world_bobbing", false);
	private static final Value<Float> NAMETAG_OPACITY = new Value<>("render_nametag_opacity", 0.25F);
	private static final Value<Float> NAMETAG_SIZE = new Value<>("render_nametag_size", 1.0F);
	private static final Value<Boolean> TRACER_ENTITY = new Value<>("render_tracer_entity", true);
	private static final Value<Boolean> SHOW_TAGS = new Value<>("render_show_tags", true);

	public static Value<Float> getLineWidth() {
		return LINE_WIDTH;
	}

	public static Value<Boolean> getAntiAliasing() {
		return ANTI_ALIASING;
	}

	public static Value<Boolean> getWorldBobbing() {
		return WORLD_BOBBING;
	}

	public static Value<Float> getNametagOpacity() {
		return NAMETAG_OPACITY;
	}

	public static Value<Float> getNametagSize() {
		return NAMETAG_SIZE;
	}

	public static Value<Boolean> getTracerEntity() {
		return TRACER_ENTITY;
	}

	public static Value<Boolean> getShowTags() {
		return SHOW_TAGS;
	}

	public static void beginGl() {
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.disableTexture2D();
		if (ANTI_ALIASING.getValue())
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(LINE_WIDTH.getValue());
	}

	public static void endGl() {
		GL11.glLineWidth(2.0F);
		if (ANTI_ALIASING.getValue())
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	public static void drawLines(AxisAlignedBB bb) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(2, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(2, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(2, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(2, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(2, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(2, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawFilledBox(AxisAlignedBB bb) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);
		var10.begin(7, DefaultVertexFormats.POSITION);
		var10.pos(left, bottom, 0.0D);
		var10.pos(right, bottom, 0.0D);
		var10.pos(right, top, 0.0D);
		var10.pos(left, top, 0.0D);
		var9.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRect(double left, double top, double right, double bottom, int red, int green, int blue, int alpha) {
		Tessellator var9 = Tessellator.getInstance();
		WorldRenderer var10 = var9.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.color(red, green, blue, alpha);
		var10.begin(7, DefaultVertexFormats.POSITION);
		var10.pos(left, bottom, 0.0D);
		var10.pos(right, bottom, 0.0D);
		var10.pos(right, top, 0.0D);
		var10.pos(left, top, 0.0D);
		var9.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
		float var7 = (float) (startColor >> 24 & 255) / 255.0F;
		float var8 = (float) (startColor >> 16 & 255) / 255.0F;
		float var9 = (float) (startColor >> 8 & 255) / 255.0F;
		float var10 = (float) (startColor & 255) / 255.0F;
		float var11 = (float) (endColor >> 24 & 255) / 255.0F;
		float var12 = (float) (endColor >> 16 & 255) / 255.0F;
		float var13 = (float) (endColor >> 8 & 255) / 255.0F;
		float var14 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator var15 = Tessellator.getInstance();
		WorldRenderer var16 = var15.getWorldRenderer();
		var16.begin(7, DefaultVertexFormats.POSITION);
		var16.color(var8, var9, var10, var7);
		var16.pos(right, top, 0);
		var16.pos(left, top, 0);
		var16.color(var12, var13, var14, var11);
		var16.pos(left, bottom, 0);
		var16.pos(right, bottom, 0);
		var15.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawBorderedRect(double left, double top, double right, double bottom, float borderWidth, int borderColor, int color) {
		float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		float red = (borderColor >> 16 & 0xFF) / 255.0f;
		float green = (borderColor >> 8 & 0xFF) / 255.0f;
		float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		drawRect(left, top, right, bottom, color);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);

		if (borderWidth == 1.0F) {
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
		}

		GL11.glLineWidth(borderWidth);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(left, top, 0.0F);
		worldRenderer.pos(left, bottom, 0.0F);
		worldRenderer.pos(right, bottom, 0.0F);
		worldRenderer.pos(right, top, 0.0F);
		worldRenderer.pos(left, top, 0.0F);
		worldRenderer.pos(right, top, 0.0F);
		worldRenderer.pos(left, bottom, 0.0F);
		worldRenderer.pos(right, bottom, 0.0F);
		tessellator.draw();
		GL11.glLineWidth(2.0F);

		if (borderWidth == 1.0F) {
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawHollowRect(double left, double top, double right, double bottom, float borderWidth, int borderColor) {
		float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		float red = (borderColor >> 16 & 0xFF) / 255.0f;
		float green = (borderColor >> 8 & 0xFF) / 255.0f;
		float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		GL11.glLineWidth(borderWidth);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(left, top, 0.0F);
		worldRenderer.pos(left, bottom, 0.0F);
		worldRenderer.pos(right, bottom, 0.0F);
		worldRenderer.pos(right, top, 0.0F);
		worldRenderer.pos(left, top, 0.0F);
		worldRenderer.pos(right, top, 0.0F);
		worldRenderer.pos(left, bottom, 0.0F);
		worldRenderer.pos(right, bottom, 0.0F);
		tessellator.draw();
		GL11.glLineWidth(2.0F);

		GL11.glDisable(GL11.GL_LINE_SMOOTH);

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedRect(double left, double top, double right, double bottom, int borderColor, int color) {
		drawBorderedRect(left, top, right, bottom, 1.0F, borderColor, color);
	}

	public static void drawBorderedGradientRect(double left, double top, double right, double bottom, float borderWidth, int borderColor, int startColor, int endColor) {
		float alpha = (borderColor >> 24 & 0xFF) / 255.0f;
		float red = (borderColor >> 16 & 0xFF) / 255.0f;
		float green = (borderColor >> 8 & 0xFF) / 255.0f;
		float blue = (borderColor & 0xFF) / 255.0f;
		GlStateManager.pushMatrix();
		drawGradientRect(left, top, right, bottom, startColor, endColor);
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(red, green, blue, alpha);

		if (borderWidth == 1.0F) {
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
		}

		GL11.glLineWidth(borderWidth);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(left, top, 0.0F);
		worldRenderer.pos(left, bottom, 0.0F);
		worldRenderer.pos(right, bottom, 0.0F);
		worldRenderer.pos(right, top, 0.0F);
		worldRenderer.pos(left, top, 0.0F);
		worldRenderer.pos(right, top, 0.0F);
		worldRenderer.pos(left, bottom, 0.0F);
		worldRenderer.pos(right, bottom, 0.0F);
		tessellator.draw();
		GL11.glLineWidth(2.0F);

		if (borderWidth == 1.0F) {
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void drawBorderedGradientRect(double left, double top, double right, double bottom, int borderColor, int startColor, int endColor) {
		drawBorderedGradientRect(left, top, right, bottom, 1.0F, borderColor, startColor, endColor);
	}

	public static ScaledResolution newScaledResolution() {
		return new ScaledResolution(Minecraft.getMinecraft());
	}

	public static void enableGL3D(float lineWidth) {
		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2884);
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
		GL11.glLineWidth(lineWidth);
	}

	public static void disableGL3D() {
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glDepthMask(true);
		GL11.glCullFace(1029);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawEsp(EntityLivingBase entity, float pTicks, int hexColor, int hexColorIn) {
		if (entity.isEntityAlive()) {
			double x = getDiff(entity.lastTickPosX, entity.posX, pTicks, RenderManager.renderPosX);
			double y = getDiff(entity.lastTickPosY, entity.posY, pTicks, RenderManager.renderPosY);
			double z = getDiff(entity.lastTickPosZ, entity.posZ, pTicks, RenderManager.renderPosZ);
			boundingBox(entity, x, y, z, hexColor, hexColorIn);
		}
	}

	private static double getDiff(double lastI, double i, float ticks, double ownI) {
		return lastI + (i - lastI) * ticks - ownI;
	}

	public static void disableLighting() {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(3553);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(2896);
		GL11.glDisable(3553);
	}

	public static void boundingBox(EntityLivingBase entity, double x, double y, double z, int color, int colorIn) {
		GL11.glPushMatrix();
		AxisAlignedBB var11 = entity.getEntityBoundingBox();
		AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
		disableLighting();
		float[] color1;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float blue = (color >> 8 & 0xFF) / 255.0F;
		float green = (color & 0xFF) / 255.0F;
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		color1 = new float[] { red, blue, green, alpha };

		GL11.glLineWidth(1.0F);
		GL11.glColor4f(color1[0], color1[1], color1[2], 0.8F);
		RenderGlobal.drawOutlinedBoundingBox(var12);
		GlStateManager.disableDepth();
		GlStateManager.F();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public static void drawRect1(float g, float h, float i, float j, int col1) {
		float f = (col1 >> 24 & 0xFF) / 255.0F;
		float f1 = (col1 >> 16 & 0xFF) / 255.0F;
		float f2 = (col1 >> 8 & 0xFF) / 255.0F;
		float f3 = (col1 & 0xFF) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(7);
		GL11.glVertex2d(i, h);
		GL11.glVertex2d(g, h);
		GL11.glVertex2d(g, j);
		GL11.glVertex2d(i, j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawBorderedCircle(int x, int y, float radius, int outsideC, int insideC) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		float scale = 0.1F;
		GL11.glScalef(scale, scale, scale);
		x = (int) (x * (1.0F / scale));
		y = (int) (y * (1.0F / scale));
		radius *= 1.0F / scale;
		drawCircle(x, y, radius, insideC);
		drawUnfilledCircle(x, y, radius, 1.0F, outsideC);
		GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawUnfilledCircle(int x, int y, float radius, float lineWidth, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glLineWidth(lineWidth);
		GL11.glEnable(2848);
		GL11.glBegin(2);
		for (int i = 0; i <= 360; i++) {
			GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
		}
		GL11.glEnd();
		GL11.glDisable(2848);
	}

	public static void drawCircle(int x, int y, float radius, int color) {
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(9);
		for (int i = 0; i <= 360; i++) {
			GL11.glVertex2d(x + Math.sin(i * 3.141526D / 180.0D) * radius, y + Math.cos(i * 3.141526D / 180.0D) * radius);
		}
		GL11.glEnd();
	}
}