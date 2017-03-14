package axis.event.events;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockDataEvent
{
  public BlockPos position;
  public EnumFacing face;

  public BlockDataEvent(BlockPos position, EnumFacing face)
  {
    this.position = position;
    this.face = face;
  }
}
