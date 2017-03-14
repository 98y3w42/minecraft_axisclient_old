package axis.ui.chat;

import java.util.List;

import axis.module.modules.render.HUD;
import axis.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class HexNewChat extends GuiNewChat {

   private final Minecraft mc;
   private int y1;
private int allmin;
private int allmax;
private int worldmin;
private int worldmax;
private int ircmin;
private int ircmax;
private int testncpmin;
private int testncpmax;
private float y2;
private boolean ball = false;
private boolean bworld = false;
private boolean birc = false;
private boolean btestncp = false;


   public HexNewChat(Minecraft mc) {
      super(mc);
      this.mc = mc;
      this.x = 20;
      this.y = 0;
      this.ball = true;
   }

   public void drawChat(int p_146230_1_) {
	   if(this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
	         int var2 = this.getLineCount();
	         boolean var3 = false;
	         int var4 = 0;
	         int var5 = this.field_146253_i.size();
	         float var6 = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
	         if(var5 > 0) {
	            if(getChatOpen()) {
	               var3 = true;
	            }

	            float var7 = this.getChatScale();
	            int var8 = MathHelper.ceiling_float_int((float)this.getChatWidth() / var7);
	            GlStateManager.pushMatrix();
	            GlStateManager.translate(2.0F, 20.0F, 0.0F);
	            GlStateManager.scale(var7, var7, 1.0F);

	            int var9;
	            int var11;
	            int var14;
	            List chatList = null;
	            if (ball) {
	            	chatList = field_146253_i;
	            } else if (bworld) {
	            	chatList = world;
	            } else if (birc) {
	            	chatList = irc;
	            } else if (btestncp) {
	            	//Logger.logChat("test");
	            	chatList = ncp;
	            }

	            for(var9 = 0; var9 + this.scrollPos < chatList.size() && var9 < var2; ++var9) {
	               ChatLine var18 = (ChatLine)chatList.get(var9 + this.scrollPos);
	               if(var18 != null) {
	                  var11 = p_146230_1_ - var18.getUpdatedCounter();
	                  if(var11 < 200 || var3) {
	                     double var19 = (double)var11 / 200.0D;
	                     var19 = 1.0D - var19;
	                     var19 *= 10.0D;
	                     var19 = MathHelper.clamp_double(var19, 0.0D, 1.0D);
	                     var19 *= var19;
	                     var14 = (int)(255.0D * var19);
	                     if(var3) {
	                        var14 = 255;
	                     }

	                     var14 = (int)((float)var14 * var6);
	                     ++var4;
	                     if(var14 > 3) {
	                        byte var20 = 0;
	                        int var16 = -var9 * 9;
	                        drawRect(var20, var16 - 9, var20 + var8 + 4, var16, var14 / 2 << 24);
	                        y1 = var16 - 9;
	                        String var17 = var18.getChatComponent().getFormattedText();
	                        GlStateManager.enableBlend();
	                        mc.fontRendererObj.drawStringWithShadow(var17, var20, var16 - 8 , 16777215 + (var14 << 24));
	                        GlStateManager.disableAlpha();
	                        GlStateManager.disableBlend();
	                     }
	                  }
	               }
	            }

	            if(var3) {
	            	//Logger.logChat("" + y1);
	            	var11 = p_146230_1_;
	            	double var19 = (double)var11 / 200.0D;
	            	var19 = 1.0D - var19;
	            	var19 *= 10.0D;
	            	var19 = MathHelper.clamp_double(var19, 0.0D, 1.0D);
	            	var19 *= var19;
	            	var14 = (int)(255.0D * var19);
	            	int var16 = y1;
	            	y2 =  (float)var16  - 14;
	            	allmin = 0;
	            }

	            if(var3) {
	               var9 = this.mc.fontRendererObj.FONT_HEIGHT;
	               GlStateManager.translate(-3.0F, 0.0F, 0.0F);
	               int var181 = var5 * var9 + var5;
	               var11 = var4 * var9 + var4;
	               int var191 = this.scrollPos * var11 / var5;
	               int var13 = var11 * var11 / var181;
	               if(var181 != var11) {
	                  var14 = var191 > 0?170:96;
	                  int var201 = this.isScrolled?13382451:3355562;
	                  drawRect(0, -var191, 2, -var191 - var13, var201 + (var14 << 24));
	                  drawRect(2, -var191, 1, -var191 - var13, 13421772 + (var14 << 24));
	               }
	            }

	            y1 = 0;
	            GlStateManager.popMatrix();
	         }
	      }
   }

   private void isMouseOverTitle(int par1, int par2) {
	   int height = RenderHelper.getScaledRes().getScaledHeight();
	   if (par1 > this.allmin && par1 < this.allmax && (par2 - 20 - height + 48 < y2 +13 && par2 - 20 - height + 48 > y2 || y2 == 0.0)) {
		   this.ball = true;
		   this.bworld = false;
		   this.birc = false;
		   this.btestncp = false;
	   } else if (par1 < this.worldmin + 1 && par1 > this.worldmax && (par2 - 20 - height + 48 < y2 +13 && par2 - 20 - height + 48 > y2 || y2 == 0.0)) {
		   this.ball = false;
		   this.bworld = true;
		   this.birc = false;
		   this.btestncp = false;
	   } else if (par1 < this.ircmin && par1 > this.ircmax && (par2 - 20 - height + 48 < y2 +13 && par2 - 20 - height + 48 > y2 || y2 == 0.0)) {
		   this.ball = false;
		   this.bworld = false;
		   this.birc = true;
		   this.btestncp = false;
	   } else if (par1 < this.testncpmin && par1 > this.testncpmax && (par2 - 20 - height + 48 < y2 +13 && par2 - 20 - height + 48 > y2 || y2 == 0.0)) {
		   this.ball = false;
		   this.bworld = false;
		   this.birc = false;
		   this.btestncp = true;
	   }
   }

   public void mouseClicked(int par1, int par2, int par3) {
      this.isMouseOverTitle(par1, par2);
   }

   public void mouseReleased(int par1, int par2, int par3) {
      if(par3 == 0) {
         this.drag = false;
      }
   }
}