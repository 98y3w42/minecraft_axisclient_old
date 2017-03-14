package axis.event.events;

import axis.event.Event;

public class RayTraceEvent
  extends Event
{
  public double reach;
  public float partialTicks;

  public RayTraceEvent(double rea, float par)
  {
    this.reach = rea;
    this.partialTicks = par;
  }
}
