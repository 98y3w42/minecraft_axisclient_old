package axis.event.events;

import axis.event.Event;

public class EventRenderTracers
  extends Event
{
  private float partialTicks;

  public EventRenderTracers(float partialTicks2)
  {
    this.partialTicks = partialTicks2;
  }

  public float getPartialTicks()
  {
    return this.partialTicks;
  }
}
