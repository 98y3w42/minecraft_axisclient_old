package axis.util;

import java.awt.Color;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.module.modules.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;

public class RenderHelper {

	private static final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

	public static final ScaledResolution getScaledRes() {
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
		return scaledRes;
	}

	public static void drawHollowRect(float posX, float posY, float posX2, float posY2, float width, int color, boolean center) {
		float corners = width / 2.0F;
		float side = width / 2.0F;
		if (center) {
			drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
			drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
			drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
			drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
		} else {
			drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
			drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
			drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
			drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
		}

	}

	public static void drawGradientBorderedRect(float posX, float posY, float posX2, float posY2, float width, int color, int startColor, int endColor, boolean center) {
		drawGradientRect(posX, posY, posX2, posY2, startColor, endColor);
		drawHollowRect(posX, posY, posX2, posY2, width, color, center);
	}

	public static void drawCoolLines(AxisAlignedBB mask) {
		GL11.glPushMatrix();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
		drawRect(x, y, x2, y2, col2);
		float f = (float) (col1 >> 24 & 255) / 255.0F;
		float f1 = (float) (col1 >> 16 & 255) / 255.0F;
		float f2 = (float) (col1 >> 8 & 255) / 255.0F;
		float f3 = (float) (col1 & 255) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawBorderedCorneredRect(float x, float y, float x2, float y2, float lineWidth, int lineColor, int bgColor) {
		drawRect(x, y, x2, y2, bgColor);
		float f = (float) (lineColor >> 24 & 255) / 255.0F;
		float f1 = (float) (lineColor >> 16 & 255) / 255.0F;
		float f2 = (float) (lineColor >> 8 & 255) / 255.0F;
		float f3 = (float) (lineColor & 255) / 255.0F;
		GL11.glEnable(3042);
		GL11.glEnable(3553);
		drawRect(x - 1.0F, y, x2 + 1.0F, y - lineWidth, lineColor);
		drawRect(x, y, x - lineWidth, y2, lineColor);
		drawRect(x - 1.0F, y2, x2 + 1.0F, y2 + lineWidth, lineColor);
		drawRect(x2, y, x2 + lineWidth, y2, lineColor);
		GL11.glDisable(3553);
		GL11.glDisable(3042);
	}

	public static double interp(double from, double to, double pct) {
		return from + (to - from) * pct;
	}

	public static double interpPlayerX() {
		return interp(Minecraft.getMinecraft().thePlayer.lastTickPosX, Minecraft.getMinecraft().thePlayer.posX, (double) Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public static double interpPlayerY() {
		return interp(Minecraft.getMinecraft().thePlayer.lastTickPosY, Minecraft.getMinecraft().thePlayer.posY, (double) Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public static double interpPlayerZ() {
		return interp(Minecraft.getMinecraft().thePlayer.lastTickPosZ, Minecraft.getMinecraft().thePlayer.posZ, (double) Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public static void glColor(Color color) {
		GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
	}

	public static void glColor(int hex) {
		float alpha = (float) (hex >> 24 & 255) / 255.0F;
		float red = (float) (hex >> 16 & 255) / 255.0F;
		float green = (float) (hex >> 8 & 255) / 255.0F;
		float blue = (float) (hex & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}

	public static void drawGradientSideRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
		enableGL2D();
		GL11.glShadeModel(7425);
		GL11.glBegin(7);
		glColor(topColor);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x1, y1);
		glColor(bottomColor);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glShadeModel(7424);
		disableGL2D();
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}

	public static void drawLines(AxisAlignedBB mask) {
		GL11.glPushMatrix();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.maxY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.maxY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.minX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glBegin(2);
		GL11.glVertex3d(mask.maxX, mask.minY, mask.minZ);
		GL11.glVertex3d(mask.minX, mask.minY, mask.maxZ);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB mask) {
		WorldRenderer var2 = Tessellator.instance.getWorldRenderer();
		Tessellator var1 = Tessellator.instance;
		var2.begin(3, new VertexFormat());
		var2.pos(mask.minX, mask.minY, mask.minZ);
		var2.pos(mask.maxX, mask.minY, mask.minZ);
		var2.pos(mask.maxX, mask.minY, mask.maxZ);
		var2.pos(mask.minX, mask.minY, mask.maxZ);
		var2.pos(mask.minX, mask.minY, mask.minZ);
		var1.draw();
		var2.begin(3, new VertexFormat());
		var2.pos(mask.minX, mask.maxY, mask.minZ);
		var2.pos(mask.maxX, mask.maxY, mask.minZ);
		var2.pos(mask.maxX, mask.maxY, mask.maxZ);
		var2.pos(mask.minX, mask.maxY, mask.maxZ);
		var2.pos(mask.minX, mask.maxY, mask.minZ);
		var1.draw();
		var2.begin(1, new VertexFormat());
		var2.pos(mask.minX, mask.minY, mask.minZ);
		var2.pos(mask.minX, mask.maxY, mask.minZ);
		var2.pos(mask.maxX, mask.minY, mask.minZ);
		var2.pos(mask.maxX, mask.maxY, mask.minZ);
		var2.pos(mask.maxX, mask.minY, mask.maxZ);
		var2.pos(mask.maxX, mask.maxY, mask.maxZ);
		var2.pos(mask.minX, mask.minY, mask.maxZ);
		var2.pos(mask.minX, mask.maxY, mask.maxZ);
		var1.draw();
	}

	public static void drawRect(float g, float h, float i, float j, int col1) {
		float f = (float) (col1 >> 24 & 255) / 255.0F;
		float f1 = (float) (col1 >> 16 & 255) / 255.0F;
		float f2 = (float) (col1 >> 8 & 255) / 255.0F;
		float f3 = (float) (col1 & 255) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(7);
		GL11.glVertex2d((double) i, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glVertex2d((double) g, (double) j);
		GL11.glVertex2d((double) i, (double) j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void drawRect(float g, float h, float i, float j) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(0.3f, 0.0f, 0.5f, 1);
		GL11.glBegin(7);
		GL11.glVertex2d((double) i, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glVertex2d((double) g, (double) j);
		GL11.glVertex2d((double) i, (double) j);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static int createShader(final String shaderCode, final int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			if (shader == 0) {
				return 0;
			}
			ARBShaderObjects.glShaderSourceARB(shader, (CharSequence) shaderCode);
			ARBShaderObjects.glCompileShaderARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}
			return shader;
		} catch (Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}

	public static String getLogInfo(final int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
	}

	public static void renderOne() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(1.5F);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glClearStencil(0xF);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
	}

	public static void renderTwo() {
		GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
	}

	public static void renderThree() {
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
	}

	public static void renderFour(Minecraft mc, Entity renderEntity) {
		float[] color = new float[] { 1.0F, 1.0F, 1.0F };

		if (renderEntity instanceof EntityLivingBase && renderEntity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) renderEntity;
			if (renderEntity instanceof EntityPlayer) {
				if (player.getTeam() != null) {
					ScorePlayerTeam team = (ScorePlayerTeam) player.getTeam();
					String code = "";
					String[] arrayOfString;
					int j = (arrayOfString = team.getColorPrefix().split("§")).length;
					for (int i = 0; i < j; i++) {
						String a = arrayOfString[i];
						if ((!a.contains("r")) || (!a.contains("f"))) {
							code = a;
						}
					}
					ColorCode colora = null;
					ColorCode[] arrayOfColorCode;
					int k = (arrayOfColorCode = ColorCode.values()).length;
					for (j = 0; j < k; j++) {
						ColorCode c = arrayOfColorCode[j];
						if (c.getformat().contains(code)) {
							colora = c;
						}
					}
					if (colora == null) {
						color = new float[] { 0.2F, 1.2F, 1.2F };
					} else {
						float g = (colora.getCode() >> 16 & 0xFF) / 255.0F;
						float g1 = (colora.getCode() >> 8 & 0xFF) / 255.0F;
						float g2 = (colora.getCode() & 0xFF) / 255.0F;
						color = new float[] { g, g1, g2 };
						if (Axis.getAxis().getFriendManager().isFriend(renderEntity.getName())) {
							float red = (HUD.color1 >> 16 & 0xFF) / 255.0F;
							float blue = (HUD.color1 >> 8 & 0xFF) / 255.0F;
							float green = (HUD.color1 & 0xFF) / 255.0F;
							float alpha = (HUD.color1 >> 24 & 0xFF) / 255.0F;
							color = new float[] { red, blue, green, alpha };
						}
					}
				} else {
					color = new float[] { 1.0F, 1.0F, 1.0F };
				}
			}
		}
		GL11.glColor4f(color[0], color[1], color[2], 1.0F);

		renderFour();
	}

	public static void renderFour() {
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
		GL11.glPolygonOffset(1.0F, -2000000F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	}

	public static void renderFive() {
		GL11.glPolygonOffset(1.0F, 2000000F);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glPopAttrib();
	}
}