package net.minecraft.src;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock
{
    private int blockId = -1;
    private int[] metadatas = null;

    public MatchBlock(int p_i54_1_)
    {
        this.blockId = p_i54_1_;
    }

    public MatchBlock(int p_i55_1_, int p_i55_2_)
    {
        this.blockId = p_i55_1_;

        if (p_i55_2_ >= 0 && p_i55_2_ <= 15)
        {
            this.metadatas = new int[] {p_i55_2_};
        }
    }

    public MatchBlock(int p_i56_1_, int[] p_i56_2_)
    {
        this.blockId = p_i56_1_;
        this.metadatas = p_i56_2_;
    }

    public int getBlockId()
    {
        return this.blockId;
    }

    public int[] getMetadatas()
    {
        return this.metadatas;
    }

    public boolean matches(BlockStateBase p_matches_1_)
    {
        if (p_matches_1_.getBlockId() != this.blockId)
        {
            return false;
        }
        else
        {
            if (this.metadatas != null)
            {
                boolean flag = false;
                int i = p_matches_1_.getMetadata();

                for (int j = 0; j < this.metadatas.length; ++j)
                {
                    int k = this.metadatas[j];

                    if (k == i)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public void addMetadata(int p_addMetadata_1_)
    {
        if (this.metadatas != null)
        {
            if (p_addMetadata_1_ >= 0 && p_addMetadata_1_ <= 15)
            {
                for (int i = 0; i < this.metadatas.length; ++i)
                {
                    if (this.metadatas[i] == p_addMetadata_1_)
                    {
                        return;
                    }
                }

                this.metadatas = Config.addIntToArray(this.metadatas, p_addMetadata_1_);
            }
        }
    }

    public String toString()
    {
        return "" + this.blockId + ":" + Config.arrayToString(this.metadatas);
    }
}
