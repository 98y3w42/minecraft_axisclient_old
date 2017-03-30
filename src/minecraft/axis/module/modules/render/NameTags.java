package axis.module.modules.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.command.Command;
import axis.command.commands.Add;
import axis.event.events.NametagRenderEvent;
import axis.event.events.Render3DEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import axis.util.Logger;
import axis.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class NameTags extends Module {
	public Value<Boolean> armor = new Value("nametags_armor", Boolean.valueOf(true));
	public final Value<Double> distance = new Value<>("nametag_distance", 8.0D);

	private Character formatChar = new Character('§');
	private Add friend;

	public NameTags() {
		super("NameTags", 9623002, Category.RENDER);
		Axis.getAxis().getCommandManager().getContents().add(new Command("nametags", "<armorstatus>", new String[0]) {
			public void run(String message) {
				if (message.split(" ")[1].equalsIgnoreCase("armorstatus")) {
					NameTags.this.armor.setValue(Boolean.valueOf(!((Boolean) NameTags.this.armor.getValue()).booleanValue()));
					Logger.logChat("Name Tags will " + (((Boolean) NameTags.this.armor.getValue()).booleanValue() ? "now" : "no longer") + " display the armor above the head.");
				}
			}
		});
	}

	private void onRender(Render3DEvent event) {

		for (Entity ent : this.mc.theWorld.loadedEntityList) {
			if (ent != this.mc.thePlayer) {
				if ((ent instanceof EntityPlayer)) {
					double posX = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * event.getPartialTicks() - RenderManager.renderPosX;
					double posY = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * event.getPartialTicks() - RenderManager.renderPosY + ent.height + 0.5D;
					double posZ = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * event.getPartialTicks() - RenderManager.renderPosZ;
					String str = ent.getDisplayName().getFormattedText();

					String colorString = this.formatChar.toString();

					float dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(ent) / 4.0F;
					dist = dist <= 2.0F ? 2.0F : dist;
					float scale = 0.016666668F * dist;
					GlStateManager.pushMatrix();
					GlStateManager.disableDepth();
					GlStateManager.translate(posX, posY, posZ);
					GL11.glNormal3f(0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(-this.mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(this.mc.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					// GlStateManager.scale(-scale, -scale, scale);
					GlStateManager.scale(-0.04F, -0.04F, 0.04F);

					GlStateManager.disableLighting();
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
					Tessellator tessellator = Tessellator.getInstance();
					WorldRenderer worldRenderer = tessellator.getWorldRenderer();
					GlStateManager.disableTexture2D();
					if (Axis.getAxis().getFriendManager().isFriend(ent.getName())) {
						str = Axis.getAxis().getFriendManager().replaceNames(ent.getName(), true);
					}
					str = str + " : " + ent.getEntityId();
					int stringWidth = mc.fontRendererObj.getStringWidth(str) / 2;

					worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
					worldRenderer.pos((double) (-stringWidth - 2), (double) (-0.8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
					worldRenderer.pos((double) (-stringWidth - 2), (double) (8.8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
					worldRenderer.pos((double) (stringWidth + 2), (double) (8.8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
					worldRenderer.pos((double) (stringWidth + 2), (double) (-0.8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.5F).endVertex();
					tessellator.draw();

					GL11.glColor3f(0.0F, 0.0F, 0.0F);
					GL11.glLineWidth(1.0E-6F);
					GL11.glBegin(3);
					GL11.glVertex2d(-stringWidth - 2, -0.8D);
					GL11.glVertex2d(-stringWidth - 2, 8.8D);
					GL11.glVertex2d(-stringWidth - 2, 8.8D);
					GL11.glVertex2d(stringWidth + 2, 8.8D);
					GL11.glVertex2d(stringWidth + 2, 8.8D);
					GL11.glVertex2d(stringWidth + 2, -0.8D);
					GL11.glVertex2d(stringWidth + 2, -0.8D);
					GL11.glVertex2d(-stringWidth - 2, -0.8D);
					GL11.glEnd();

					GlStateManager.enableTexture2D();

					mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, 0, -1);

					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
					if ((this.armor.getValue()) && ((ent instanceof EntityPlayer))) {
						List<ItemStack> itemsToRender = new ArrayList();
						for (int i = 0; i < 5; i++) {
							ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(i);
							if (stack != null) {
								itemsToRender.add(stack);
							}
						}
						int x = -(itemsToRender.size() * 8);
						for (ItemStack stack : itemsToRender) {
							GlStateManager.disableDepth();
							RenderHelper.enableGUIStandardItemLighting();
							this.mc.getRenderItem().zLevel = -100.0F;
							this.mc.getRenderItem().renderItemIntoGUI(stack, x, -18);
							this.mc.getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack, x, -18);
							this.mc.getRenderItem().zLevel = 0.0F;
							RenderHelper.disableStandardItemLighting();
							GlStateManager.enableDepth();

							String text = "";
							if (stack != null) {
								int y = 0;
								int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
								int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
								int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
								if (sLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("Sh" + sLevel, x, y);
									y -= 9;
								}
								if (fLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("Fir" + fLevel, x, y);
									y -= 9;
								}
								if (kLevel > 0) {
									GL11.glDisable(2896);
									drawEnchantTag("Kb" + kLevel, x, y);
								} else if ((stack.getItem() instanceof ItemArmor)) {
									int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
									int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
									int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
									if (pLevel > 0) {
										GL11.glDisable(2896);
										drawEnchantTag("P" + pLevel, x, y);
										y -= 9;
									}
									if (tLevel > 0) {
										GL11.glDisable(2896);
										drawEnchantTag("Th" + tLevel, x, y);
										y -= 9;
									}
									if (uLevel > 0) {
										GL11.glDisable(2896);
										drawEnchantTag("Unb" + uLevel, x, y);
									}
								} else if ((stack.getItem() instanceof ItemBow)) {
									int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
									int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
									int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
									if (powLevel > 0) {
										GL11.glDisable(2896);
										drawEnchantTag("Pow" + powLevel, x, y);
										y -= 9;
									}
									if (punLevel > 0) {
										GL11.glDisable(2896);
										drawEnchantTag("Pun" + punLevel, x, y);
										y -= 9;
									}
									if (fireLevel > 0) {
										GL11.glDisable(2896);
										drawEnchantTag("Fir" + fireLevel, x, y);
									}
								} else if (stack.getRarity() == EnumRarity.EPIC) {
									drawEnchantTag(this.formatChar + "lGod", x, y);
								}
								x += 16;
							}
						}
					}
					GlStateManager.popMatrix();
				}
				GlStateManager.disableBlend();
			}
		}
	}

	private static void drawEnchantTag(String text, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		x = (int) (x * 1.75D);
		y -= 4;
		GL11.glScalef(0.57F, 0.57F, 0.57F);
		mc.fontRendererObj.drawStringWithShadow(text, x, -36 - y, -1);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	private void onNametagRender(NametagRenderEvent event) {
		event.setCancelled(true);
	}
}