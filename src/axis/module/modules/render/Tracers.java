package axis.module.modules.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.command.Command;
import axis.event.events.NametagRenderEvent;
import axis.event.events.Render3DEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.ColorCode;
import axis.util.Logger;

public class Tracers
		extends Module {
	// private boolean shouldBob;
	private final List<Entity> entities = new ArrayList();
	private int ticks = 0;
	private int state;
	private float r = 0.33F;
	private float g = 0.34F;
	private float b = 0.33F;
	public boolean rainbow;
	public boolean players = true;
	public boolean monsters;
	public boolean farmHunt;
	Minecraft mc = Minecraft.getMinecraft();

	public Tracers() {
		super("Tracers", 9623002, Category.RENDER);
	}

	private void onRender(Render3DEvent event) {
		GlStateManager.pushMatrix();
		for (Entity ent : this.mc.theWorld.loadedEntityList) {
			if ((ent != this.mc.thePlayer) && ((this.farmHunt) || (

			(((ent instanceof EntityPlayer)) || ((ent instanceof EntityMob))) &&
					((!(ent instanceof EntityPlayer)) || (this.players)) && ((!(ent instanceof EntityMob)) || (this.monsters))))) {
				if ((((ent instanceof EntityLivingBase)) && (((EntityLivingBase) ent).getMaxHealth() > 20.0F) && (!((EntityLivingBase) ent).isInvisible()) && (!(ent instanceof EntityHorse))) ||

						(!this.farmHunt)) {
					if (ent.isEntityAlive()) {
						double x = getDiff(ent.lastTickPosX, ent.posX, event.partialTicks, RenderManager.renderPosX);
						double y = getDiff(ent.lastTickPosY, ent.posY, event.partialTicks, RenderManager.renderPosY);
						double z = getDiff(ent.lastTickPosZ, ent.posZ, event.partialTicks, RenderManager.renderPosZ);
						float distance = this.mc.thePlayer.getDistanceToEntity(ent);
						float fade = 0.0F;
						this.ticks += 1;
						for (int i = 0; i < 100; i++) {
							if (this.ticks >= i) {
								fade += 0.01F;
							}
						}
						if (this.ticks > 100) {
							fade = 1.0F;
						}
						if (this.mc.theWorld == null) {
							this.ticks = 0;
						}
						if (this.entities.isEmpty()) {
							this.ticks = 0;
						}
						if (ent instanceof EntityPlayer)
							;
						float[] color;
						if (ent.isInvisibleToPlayer(this.mc.thePlayer)) {
							color = new float[] { 1.0F, 0.9F, 0.0F };
						} else {
							color = new float[] { 1.0F, 1.0F, 1.0F };
							if (Axis.getFriendManager().isFriend(ent.getName())) {
								float red = (HUD.color1 >> 16 & 0xFF) / 255.0F;
								float blue = (HUD.color1 >> 8 & 0xFF) / 255.0F;
								float green = (HUD.color1 & 0xFF) / 255.0F;
								float alpha = (HUD.color1 >> 24 & 0xFF) / 255.0F;
								color = new float[] { red, blue, green, alpha };
							}
						}
						if ((ent instanceof EntityPlayer) && (Axis.getFriendManager().isFriend(ent.getName()))) {
							EntityPlayer player = (EntityPlayer) ent;
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
									float f = (colora.getCode() >> 16 & 0xFF) / 255.0F;
									float f1 = (colora.getCode() >> 8 & 0xFF) / 255.0F;
									float f2 = (colora.getCode() & 0xFF) / 255.0F;
									color = new float[] { f, f1, f2 };
									if (Axis.getFriendManager().isFriend(ent.getName())) {
										float red = (HUD.color1 >> 16 & 0xFF) / 255.0F;
										float blue = (HUD.color1 >> 8 & 0xFF) / 255.0F;
										float green = (HUD.color1 & 0xFF) / 255.0F;
										float alpha = (HUD.color1 >> 24 & 0xFF) / 255.0F;
										color = new float[] { red, blue, green, alpha };
									}
								}
							}
						}
						GL11.glColor4f(color[0], color[1], color[2], 1.0F);
						GL11.glLoadIdentity();
						boolean bobbing = this.mc.gameSettings.viewBobbing;
						this.mc.gameSettings.viewBobbing = false;
						this.mc.entityRenderer.orientCamera(event.partialTicks);
						GL11.glLineWidth(1.2F);
						GL11.glBegin(3);
						GL11.glVertex3d(0.0D, this.mc.thePlayer.getEyeHeight(), 0.0D);
						GL11.glVertex3d(x, y, z);
						GL11.glEnd();
						this.mc.gameSettings.viewBobbing = bobbing;
					}
				}
			}
		}
		GlStateManager.popMatrix();
	}

	private double getDiff(double lastI, double i, float ticks, double ownI) {
		return lastI + (i - lastI) * ticks - ownI;
	}

	/*
	 * private boolean isValidTarget(Entity entity) { if ((entity instanceof
	 * EntityPlayer)) { if(entity == mc.thePlayer){ return false; }
	 * if(entity.getEntityId() == -1){ return false; } return true; } return
	 * false; }
	 *
	 * public void Render(Render3DEvent render) {
	 *
	 * for (Entity entity : mc.theWorld.loadedEntityList) { if ((entity != null)
	 * && (entity.isEntityAlive())) { this.entities.add(entity); } }
	 * GL11.glPushMatrix(); GL11.glDisable(3553); GL11.glDisable(2896);
	 * GL11.glEnable(3042); GL11.glBlendFunc(770, 771); GL11.glDisable(2929);
	 * GL11.glEnable(2848); GL11.glDepthMask(false); GL11.glLineWidth(1.2F); for
	 * (Entity entity : mc.theWorld.loadedEntityList) { if
	 * (isValidTarget(entity)) { double x = entity.lastTickPosX + (entity.posX -
	 * entity.lastTickPosX) * render.partialTicks - RenderManager.renderPosX;
	 * double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) *
	 * render.partialTicks - RenderManager.renderPosY; double z =
	 * entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) *
	 * render.partialTicks - RenderManager.renderPosZ; float distance =
	 * mc.thePlayer.getDistanceToEntity(entity); GL11.glPushMatrix(); float fade
	 * = 0.0F; this.ticks += 1; for (int i = 0; i < 100; i++) { if (this.ticks
	 * >= i) { fade += 0.01F; } } if (this.ticks > 100) { fade = 1.0F; } if
	 * (mc.theWorld == null) { this.ticks = 0; } if (this.entities.isEmpty()) {
	 * this.ticks = 0; } float[] color; if
	 * (entity.isInvisibleToPlayer(mc.thePlayer)) { color = new float[] { 1.0F,
	 * 0.9F, 0.0F }; } else{ color = new float[] { 0.2F, 1.2F, 0.4F }; } if
	 * (((entity instanceof EntityLivingBase)) && (((EntityLivingBase)
	 * entity).hurtTime > 0)) { color = new float[] { 2.55F, 0.0F, 0.0F }; } if
	 * (entity instanceof EntityPlayer) { EntityPlayer player = (EntityPlayer)
	 * entity; if (player.getTeam() != null) { ScorePlayerTeam team =
	 * (ScorePlayerTeam) player.getTeam(); String code = ""; for (String a :
	 * team.getColorPrefix().split("§")) { if (!a.contains("r") ||
	 * !a.contains("f")) { code = a; } } ColorCode colora = null; for (ColorCode
	 * c : ColorCode.values()) { if (c.getformat().contains(code)) { colora = c;
	 * } } if (colora == null) { color = new float[] { 0.2F, 1.2F, 0.4F }; }
	 * else { float f = (colora.getCode() >> 16 & 255) / 255.0F; float f1 =
	 * (colora.getCode() >> 8 & 255) / 255.0F; float f2 = (colora.getCode() &
	 * 255) / 255.0F; color = new float[] { f, f1, f2 }; } } }
	 *
	 * GL11.glColor4f(color[0], color[1], color[2], fade); GL11.glBegin(1);
	 * GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
	 * GL11.glVertex3d(x, y, z); GL11.glEnd();
	 *
	 * GL11.glPopMatrix(); } } GL11.glDepthMask(true); GL11.glDisable(2848);
	 * GL11.glEnable(2929); GL11.glEnable(2896); GL11.glDisable(3042);
	 * GL11.glEnable(3553); GL11.glPopMatrix(); }
	 */
}
