package net.minecraft.src;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl
{
    private GameSettings.Options option = null;

    public GuiOptionButtonOF(int p_i40_1_, int p_i40_2_, int p_i40_3_, GameSettings.Options p_i40_4_, String p_i40_5_)
    {
        super(p_i40_1_, p_i40_2_, p_i40_3_, p_i40_4_, p_i40_5_);
        this.option = p_i40_4_;
    }

    public GameSettings.Options getOption()
    {
        return this.option;
    }
}
