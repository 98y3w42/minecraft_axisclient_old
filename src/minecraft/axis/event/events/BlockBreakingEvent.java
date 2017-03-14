package axis.event.events;

import axis.event.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockBreakingEvent extends Event
{
    private BlockBreakingEvent.EnumBlock state;
    public BlockPos pos;
    private EnumFacing side;

    public BlockBreakingEvent(BlockBreakingEvent.EnumBlock state, BlockPos pos, EnumFacing side)
    {
        this.side = side;
        this.state = state;
        this.pos = pos;
    }

    public void setState(BlockBreakingEvent.EnumBlock state)
    {
        this.state = state;
    }

    public void setPos(BlockPos pos)
    {
        this.pos = pos;
    }

    public void setSide(EnumFacing side)
    {
        this.side = side;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    public BlockBreakingEvent.EnumBlock getState()
    {
        return this.state;
    }

    public EnumFacing getSide()
    {
        return this.side;
    }

    public static enum EnumBlock
    {
        CLICK("CLICK", 0),
        DAMAGE("DAMAGE", 1),
        DESTROY("DESTROY", 2);
        private static final BlockBreakingEvent.EnumBlock[] ENUM$VALUES = new BlockBreakingEvent.EnumBlock[]{CLICK, DAMAGE, DESTROY};

        private EnumBlock(String var1, int var2) {}
    }
}
