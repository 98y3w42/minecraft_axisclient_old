package axis.event.events;

import axis.event.Cancellable;
import axis.event.Event;
import net.minecraft.network.Packet;

public final class PacketReceiveEvent extends Event implements Cancellable
{
    private boolean cancel;
    public Packet packet;

    public PacketReceiveEvent(Packet packet)
    {
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
