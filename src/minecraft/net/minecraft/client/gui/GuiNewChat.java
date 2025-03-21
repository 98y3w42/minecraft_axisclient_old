package net.minecraft.client.gui;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import axis.Axis;
import axis.util.TeamUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class GuiNewChat extends Gui {

	private static final Logger logger = LogManager.getLogger();
	private final Minecraft mc;
	private final List sentMessages = Lists.newArrayList();
	private final List chatLines = Lists.newArrayList();
	protected final List field_146253_i = Lists.newArrayList();
	protected final List world = Lists.newArrayList();
	protected final List irc = Lists.newArrayList();
	protected final List ncp = Lists.newArrayList();
	protected int scrollPos;
	protected int x;
	protected int y;
	protected int dragX;
	protected int dragY;
	protected boolean drag;
	protected boolean isScrolled;
	private boolean bfn;
	private boolean bfi;
	private boolean bfl;

	public GuiNewChat(Minecraft mcIn) {
		this.mc = mcIn;
	}

	public void drag(int x, int y) {
		this.x = x + this.dragX;
		this.y = y + this.dragY;
	}

	public void mouseClicked(int x, int y, int button) {
	}

	public void mouseReleased(int x, int y, int button) {
	}

	public void drawChat(int p_146230_1_) {
		if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
			int var2 = this.getLineCount();
			boolean var3 = false;
			int var4 = 0;
			int var5 = this.field_146253_i.size();
			float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (var5 > 0) {
				if (getChatOpen()) {
					var3 = true;
				}

				float var7 = this.getChatScale();
				int var8 = MathHelper.ceiling_float_int((float) this.getChatWidth() / var7);
				GlStateManager.pushMatrix();
				GlStateManager.translate(2.0F, 20.0F, 0.0F);
				GlStateManager.scale(var7, var7, 1.0F);

				int var9;
				int var11;
				int var14;
				for (var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; ++var9) {
					ChatLine var18 = (ChatLine) this.field_146253_i.get(var9 + this.scrollPos);
					if (var18 != null) {
						var11 = p_146230_1_ - var18.getUpdatedCounter();
						if (var11 < 200 || var3) {
							double var19 = (double) var11 / 200.0D;
							var19 = 1.0D - var19;
							var19 *= 10.0D;
							var19 = MathHelper.clamp_double(var19, 0.0D, 1.0D);
							var19 *= var19;
							var14 = (int) (255.0D * var19);
							if (var3) {
								var14 = 255;
							}

							var14 = (int) ((float) var14 * var6);
							++var4;
							if (var14 > 3) {
								byte var20 = 0;
								int var16 = -var9 * 9;
								drawRect(var20, var16 - 9, var20 + var8 + 4, var16, var14 / 2 << 24);
								String var17 = Axis.getAxis().getFriendManager().replaceNames(var18.getChatComponent().getFormattedText(), true);
								GlStateManager.enableBlend();
								this.mc.fontRendererObj.drawStringWithShadow(var17, (float) var20, (float) (var16 - 8), 16777215 + (var14 << 24));
								GlStateManager.disableAlpha();
								GlStateManager.disableBlend();
							}
						}
					}
				}

				if (var3) {
					var9 = this.mc.fontRendererObj.FONT_HEIGHT;
					GlStateManager.translate(-3.0F, 0.0F, 0.0F);
					int var181 = var5 * var9 + var5;
					var11 = var4 * var9 + var4;
					int var191 = this.scrollPos * var11 / var5;
					int var13 = var11 * var11 / var181;
					if (var181 != var11) {
						var14 = var191 > 0 ? 170 : 96;
						int var201 = this.isScrolled ? 13382451 : 3355562;
						drawRect(0, -var191, 2, -var191 - var13, var201 + (var14 << 24));
						drawRect(2, -var191, 1, -var191 - var13, 13421772 + (var14 << 24));
					}
				}

				GlStateManager.popMatrix();
			}
		}

	}

	public void clearChatMessages() {
		this.field_146253_i.clear();
		this.chatLines.clear();
		this.sentMessages.clear();
	}

	public void printChatMessage(IChatComponent p_146227_1_) {
		this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
	}

	public void printChatMessageWithOptionalDeletion(IChatComponent p_146234_1_, int p_146234_2_) {
		this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
		logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
		sortChat(p_146234_1_);
	}

	private void setChatLine(IChatComponent p_146237_1_, int p_146237_2_, int p_146237_3_, boolean p_146237_4_) {
		if (p_146237_2_ != 0) {
			this.deleteChatLine(p_146237_2_);
		}

		int var5 = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
		List var6 = GuiUtilRenderComponents.func_178908_a(p_146237_1_, var5, this.mc.fontRendererObj, false, false);
		boolean var7 = getChatOpen();

		IChatComponent var9 = null;
		for (Iterator var8 = var6.iterator(); var8.hasNext(); addChat(var9, p_146237_2_, p_146237_3_)) {

			var9 = (IChatComponent) var8.next();
			if (var7 && this.scrollPos > 0) {
				this.isScrolled = true;
				this.scroll(1);
			}
		}

		while (this.field_146253_i.size() > 100) {
			this.field_146253_i.remove(this.field_146253_i.size() - 1);
		}

		if (!p_146237_4_) {
			this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));

			while (this.chatLines.size() > 100) {
				this.chatLines.remove(this.chatLines.size() - 1);
			}
		}

		bfn = false;
		bfi = false;
		bfl = false;
	}

	public void sortChat(IChatComponent chat) {
		if (chat.getUnformattedText().indexOf("[TestNCP]") != -1) {
			bfn = true;
		} else if (chat.getUnformattedText().indexOf("[IRC]") != -1) {
			bfi = true;
		} else if (chat.getUnformattedText().indexOf("[MCLeaks] Logging in as ") != -1) {
			String msg1 = chat.getUnformattedText().replace("[MCLeaks] Logging in as ", "");
			String msg2 = msg1.replace(" ...", "");
			TeamUtils.setUsername(msg2);
			;
			bfl = true;
		}
	}

	public void addChat(IChatComponent chat, int p_146237_2_, int p_146237_3_) {
		this.field_146253_i.add(0, new ChatLine(p_146237_3_, chat, p_146237_2_));
		if (bfn) {
			this.ncp.add(0, new ChatLine(p_146237_3_, chat, p_146237_2_));
		} else if (bfi) {
			this.irc.add(0, new ChatLine(p_146237_3_, chat, p_146237_2_));
		} else {
			this.world.add(0, new ChatLine(p_146237_3_, chat, p_146237_2_));
		}
	}

	public void refreshChat() {
		this.field_146253_i.clear();
		this.resetScroll();

		for (int var1 = this.chatLines.size() - 1; var1 >= 0; --var1) {
			ChatLine var2 = (ChatLine) this.chatLines.get(var1);
			this.setChatLine(var2.getChatComponent(), var2.getChatLineID(), var2.getUpdatedCounter(), true);
		}

	}

	public List getSentMessages() {
		return this.sentMessages;
	}

	public void addToSentMessages(String p_146239_1_) {
		if (this.sentMessages.isEmpty() || !((String) this.sentMessages.get(this.sentMessages.size() - 1)).equals(p_146239_1_)) {
			this.sentMessages.add(p_146239_1_);
		}

	}

	public void resetScroll() {
		this.scrollPos = 0;
		this.isScrolled = false;
	}

	public void scroll(int p_146229_1_) {
		this.scrollPos += p_146229_1_;
		int var2 = this.field_146253_i.size();
		if (this.scrollPos > var2 - this.getLineCount()) {
			this.scrollPos = var2 - this.getLineCount();
		}

		if (this.scrollPos <= 0) {
			this.scrollPos = 0;
			this.isScrolled = false;
		}

	}

	public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
		if (!getChatOpen()) {
			return null;
		} else {
			ScaledResolution var3 = new ScaledResolution(this.mc);
			int var4 = var3.getScaleFactor();
			float var5 = this.getChatScale();
			int var6 = p_146236_1_ / var4 - 3;
			int var7 = p_146236_2_ / var4 - 27;
			var6 = MathHelper.floor_float((float) var6 / var5);
			var7 = MathHelper.floor_float((float) var7 / var5);
			if (var6 >= 0 && var7 >= 0) {
				int var8 = Math.min(this.getLineCount(), this.field_146253_i.size());
				if (var6 <= MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) && var7 < this.mc.fontRendererObj.FONT_HEIGHT * var8 + var8) {
					int var9 = var7 / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
					if (var9 >= 0 && var9 < this.field_146253_i.size()) {
						ChatLine var10 = (ChatLine) this.field_146253_i.get(var9);
						int var11 = 0;
						Iterator var12 = var10.getChatComponent().iterator();

						while (var12.hasNext()) {
							IChatComponent var13 = (IChatComponent) var12.next();
							if (var13 instanceof ChatComponentText) {
								var11 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) var13).getChatComponentText_TextValue(), false));
								if (var11 > var6) {
									return var13;
								}
							}
						}
					}

					return null;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	public static boolean getChatOpen() {
		return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
	}

	public void deleteChatLine(int p_146242_1_) {
		Iterator var2 = this.field_146253_i.iterator();

		ChatLine var3;
		while (var2.hasNext()) {
			var3 = (ChatLine) var2.next();
			if (var3.getChatLineID() == p_146242_1_) {
				var2.remove();
			}
		}

		var2 = this.chatLines.iterator();

		while (var2.hasNext()) {
			var3 = (ChatLine) var2.next();
			if (var3.getChatLineID() == p_146242_1_) {
				var2.remove();
				break;
			}
		}

	}

	public int getChatWidth() {
		return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
	}

	public int getChatHeight() {
		return calculateChatboxHeight(getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
	}

	public float getChatScale() {
		return this.mc.gameSettings.chatScale;
	}

	public static int calculateChatboxWidth(float p_146233_0_) {
		short var1 = 320;
		byte var2 = 40;
		return MathHelper.floor_float(p_146233_0_ * (float) (var1 - var2) + (float) var2);
	}

	public static int calculateChatboxHeight(float p_146243_0_) {
		short var1 = 180;
		byte var2 = 20;
		return MathHelper.floor_float(p_146243_0_ * (float) (var1 - var2) + (float) var2);
	}

	public int getLineCount() {
		return this.getChatHeight() / 9;
	}
}