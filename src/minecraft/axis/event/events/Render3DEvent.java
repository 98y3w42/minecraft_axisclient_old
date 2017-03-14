package axis.event.events;

import axis.event.Event;

public final class Render3DEvent extends Event
{
    public static float partialTicks;

    public Render3DEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }
}
