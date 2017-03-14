package axis.event.events;

import axis.event.Cancellable;
import axis.event.Event;
import net.minecraft.network.Packet;

public final class PacketSentEvent extends Event implements Cancellable
{
    private boolean cancel;
    public Packet packet;
    public Event.State state;

    public PacketSentEvent(Event.State state, Packet packet)
    {
      this.state = state;
      this.packet = packet;
    }

    public Packet getPacket()
    {
        return this.packet;
    }

    public boolean isCancelled()
    {
        return this.cancel;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }

    public void setPacket(Packet packet)
    {
        this.packet = packet;
    }
}
