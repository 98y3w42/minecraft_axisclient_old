package axis.event.events;

import axis.event.Event;

public class HurtCamEvent extends Event {
	boolean cancel;

	public boolean isCancelled() {
		return this.cancel;
	}

	public void setCancelled(boolean shouldCancel) {
		this.cancel = shouldCancel;
	}
}
