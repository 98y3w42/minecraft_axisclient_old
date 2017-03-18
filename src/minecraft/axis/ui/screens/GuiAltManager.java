package axis.ui.screens;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import axis.Axis;
import axis.threads.AltLoginThread;
import axis.util.Alt;
import axis.util.RenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class GuiAltManager extends GuiScreen {
   private GuiButton login;
   private GuiButton remove;
   private GuiButton rename;
   private AltLoginThread loginThread;
   private int offset;
   public Alt selectedAlt = null;
   private String status = "§7Waiting...";

   public GuiAltManager() {
	  Axis.getAxis().getAltManager().getContents().clear();
	  Axis.getAxis().getFileManager().getFileByName("alts").loadFile();
   }

   public void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         if(this.loginThread == null) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else if(!this.loginThread.getStatus().equals("§eLogging in...") && !this.loginThread.getStatus().equals("§cDo not hit back! §eLogging in...")) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else {
            this.loginThread.setStatus("§cDo not hit back! §eLogging in...");
         }
         break;
      case 1:
         String user = this.selectedAlt.getUsername();
         String pass = this.selectedAlt.getPassword();
         this.loginThread = new AltLoginThread(user, pass);
         this.loginThread.start();
         break;
      case 2:
         if(this.loginThread != null) {
            this.loginThread = null;
         }

         Axis.getAxis().getAltManager().getContents().remove(this.selectedAlt);
         this.status = "§aRemoved.";
         this.selectedAlt = null;
         Axis.getAxis().getFileManager().getFileByName("alts").saveFile();
         break;
      case 3:
         this.mc.displayGuiScreen(new GuiAddAlt(this));
         break;
      case 4:
         this.mc.displayGuiScreen(new GuiAltLogin(this));
         break;
      case 5:
         Alt randomAlt = (Alt)Axis.getAxis().getAltManager().getContents().get((new Random()).nextInt(Axis.getAxis().getAltManager().getContents().size()));
         String user1 = randomAlt.getUsername();
         String pass1 = randomAlt.getPassword();
         this.loginThread = new AltLoginThread(user1, pass1);
         this.loginThread.start();
         break;
      case 6:
         this.mc.displayGuiScreen(new GuiRenameAlt(this));
         break;
      case 7:
         Alt lastAlt = Axis.getAxis().getAltManager().getLastAlt();
         if(lastAlt == null) {
            if(this.loginThread == null) {
               this.status = "§cThere is no last used alt!";
            } else {
               this.loginThread.setStatus("§cThere is no last used alt!");
            }
         } else {
            String user2 = lastAlt.getUsername();
            String pass2 = lastAlt.getPassword();
            this.loginThread = new AltLoginThread(user2, pass2);
            this.loginThread.start();
         }
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
      int y;
      if(Mouse.hasWheel()) {
         y = Mouse.getDWheel();
         if(y < 0) {
            this.offset += 26;
            if(this.offset < 0) {
               this.offset = 0;
            }
         } else if(y > 0) {
            this.offset -= 26;
            if(this.offset < 0) {
               this.offset = 0;
            }
         }
      }

      GuiMainMenu.renderBackground();
	  GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, -7829368);
      this.drawCenteredString(this.fontRendererObj, "Account Manager - " + Axis.getAxis().getAltManager().getContents().size() + " alts", this.width / 2, 10, -1);
      this.drawCenteredString(this.fontRendererObj, this.loginThread == null?this.status:this.loginThread.getStatus(), this.width / 2, 20, -1);
      RenderHelper.drawBorderedRect(50.0F, 33.0F, (float)(this.width - 50), (float)(this.height - 50), 1.0F, -16777216, Integer.MIN_VALUE);
      GL11.glPushMatrix();
      this.prepareScissorBox(0.0F, 33.0F, (float)this.width, (float)(this.height - 50));
      GL11.glEnable(3089);
      y = 38;
      Iterator var6 = Axis.getAxis().getAltManager().getContents().iterator();

      while(true) {
         Alt alt;
         do {
            if(!var6.hasNext()) {
               GL11.glDisable(3089);
               GL11.glPopMatrix();
               super.drawScreen(par1, par2, par3);
               if(this.selectedAlt == null) {
                  this.login.enabled = false;
                  this.remove.enabled = false;
                  this.rename.enabled = false;
               } else {
                  this.login.enabled = true;
                  this.remove.enabled = true;
                  this.rename.enabled = true;
               }

               if(Keyboard.isKeyDown(200)) {
                  this.offset -= 26;
                  if(this.offset < 0) {
                     this.offset = 0;
                  }
               } else if(Keyboard.isKeyDown(208)) {
                  this.offset += 26;
                  if(this.offset < 0) {
                     this.offset = 0;
                  }
               }

               return;
            }

            alt = (Alt)var6.next();
         } while(!this.isAltInArea(y));

         String name;
         if(alt.getMask().equals("")) {
            name = alt.getUsername();
         } else {
            name = alt.getMask();
         }

         String pass;
         if(alt.getPassword().equals("")) {
            pass = "§cCracked";
         } else {
            pass = alt.getPassword().replaceAll(".", "*");
         }

         if(alt == this.selectedAlt) {
            if(this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
               RenderHelper.drawBorderedRect(52.0F, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0F, -16777216, -2142943931);
            } else if(this.isMouseOverAlt(par1, par2, y - this.offset)) {
               RenderHelper.drawBorderedRect(52.0F, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0F, -16777216, -2142088622);
            } else {
               RenderHelper.drawBorderedRect(52.0F, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0F, -16777216, -2144259791);
            }
         } else if(this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
            RenderHelper.drawBorderedRect(52.0F, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0F, -16777216, -2146101995);
         } else if(this.isMouseOverAlt(par1, par2, y - this.offset)) {
            RenderHelper.drawBorderedRect(52.0F, (float)(y - this.offset - 4), (float)(this.width - 52), (float)(y - this.offset + 20), 1.0F, -16777216, -2145180893);
         }

         this.drawCenteredString(this.fontRendererObj, name, this.width / 2, y - this.offset, -1);
         this.drawCenteredString(this.fontRendererObj, pass, this.width / 2, y - this.offset + 10, 5592405);
         y += 26;
      }
   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 24, 75, 20, "Cancel"));
      this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
      this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 74, this.height - 24, 70, 20, "Remove"));
      this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
      this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 24, 70, 20, "Random"));
      this.buttonList.add(this.rename = new GuiButton(6, this.width / 2 + 4, this.height - 24, 70, 20, "Rename"));
      this.buttonList.add(new GuiButton(7, this.width / 2 - 206, this.height - 24, 50, 20, "Last Alt"));
      this.login.enabled = false;
      this.remove.enabled = false;
      this.rename.enabled = false;
   }

   private boolean isAltInArea(int y) {
      return y - this.offset <= this.height - 50;
   }

   private boolean isMouseOverAlt(int x, int y, int y1) {
      return x >= 52 && y >= y1 - 4 && x <= this.width - 52 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= this.width && y <= this.height - 50;
   }

   protected void mouseClicked(int par1, int par2, int par3) {
      if(this.offset < 0) {
         this.offset = 0;
      }

      int y = 38 - this.offset;

      for(Iterator var7 = Axis.getAxis().getAltManager().getContents().iterator(); var7.hasNext(); y += 26) {
         Alt e = (Alt)var7.next();
         if(this.isMouseOverAlt(par1, par2, y)) {
            if(e == this.selectedAlt) {
               this.actionPerformed((GuiButton)this.buttonList.get(1));
               return;
            }

            this.selectedAlt = e;
         }
      }

      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException var71) {
         var71.printStackTrace();
      }

   }

   public void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(this.mc);
      int factor = scale.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }
}
