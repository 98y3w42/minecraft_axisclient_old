package axis.event.events;

import axis.event.Cancellable;
import axis.event.Event;

public final class PushOutOfBlocksEvent extends Event implements Cancellable
{
  public boolean isCancelled()
  {
    return true;
  }

  public void setCancelled(boolean cancel) {}
}
