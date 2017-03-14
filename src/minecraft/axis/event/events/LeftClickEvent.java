package axis.event.events;

import axis.event.Cancellable;
import axis.event.Event;

public class LeftClickEvent extends Event  implements Cancellable {

	private boolean cancel;

    public boolean isCancelled()
    {
        return this.cancel;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }
}
