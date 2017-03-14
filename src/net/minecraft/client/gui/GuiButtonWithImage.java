package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonWithImage
  extends GuiButton
{
  public ResourceLocation texture;

  public GuiButtonWithImage(int id, int x, int y, int w, int h, ResourceLocation texture)
  {
    super(id, x, y, 20, 20, "");
    this.texture = texture;
    this.width = w;
    this.height = h;
  }

  public void drawButton(Minecraft mc, int x, int y)
  {
    if (this.visible)
    {
      mc.getTextureManager().bindTexture(this.texture);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.hovered = ((x >= this.xPosition) && (y >= this.yPosition) && (x < this.xPosition + this.width) && (y < this.yPosition + this.height));
      int k = getHoverState(this.hovered);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glBlendFunc(770, 771);
      drawTexturedModalRect(this.xPosition, this.yPosition, k * 20, 0, this.width, this.height);
      mouseDragged(mc, x, y);
    }
  }
}
