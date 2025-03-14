package shadersmod.client;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;

class GuiSlotShaders extends GuiSlot
{
    private ArrayList shaderslist;
    private int selectedIndex;
    private long lastClickedCached = 0L;
    final GuiShaders shadersGui;

    public GuiSlotShaders(GuiShaders par1GuiShaders, int width, int height, int top, int bottom, int slotHeight)
    {
        super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
        this.shadersGui = par1GuiShaders;
        this.updateList();
        this.amountScrolled = 0.0F;
        int i = this.selectedIndex * slotHeight;
        int j = (bottom - top) / 2;

        if (i > j)
        {
            this.scrollBy(i - j);
        }
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return this.width - 20;
    }

    public void updateList()
    {
        this.shaderslist = Shaders.listOfShaders();
        this.selectedIndex = 0;
        int i = 0;

        for (int j = this.shaderslist.size(); i < j; ++i)
        {
            if (((String)this.shaderslist.get(i)).equals(Shaders.currentshadername))
            {
                this.selectedIndex = i;
                break;
            }
        }
    }

    protected int getSize()
    {
        return this.shaderslist.size();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked(int index, boolean doubleClicked, int mouseX, int mouseY)
    {
        if (index != this.selectedIndex || this.lastClicked != this.lastClickedCached)
        {
            this.selectedIndex = index;
            this.lastClickedCached = this.lastClicked;
            Shaders.setShaderPack((String)this.shaderslist.get(index));
            Shaders.uninit();
            this.shadersGui.updateButtons();
        }
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int index)
    {
        return index == this.selectedIndex;
    }

    protected int getScrollBarX()
    {
        return this.width - 6;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * 18;
    }

    protected void drawBackground()
    {
    }

    protected void drawSlot(int index, int posX, int posY, int contentY, int mouseX, int mouseY)
    {
        this.shadersGui.drawCenteredString((String)this.shaderslist.get(index), this.width / 2, posY + 1, 16777215);
    }

    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
}
