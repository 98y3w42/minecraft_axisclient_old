package axis.event.events;

import axis.event.Event;

public class WalkingEvent
		extends Event {
	boolean walk = false;

	public void setSafeWalk(boolean walk) {
		this.walk = walk;
	}

	public boolean getSafeWalk() {
		return this.walk;
	}
}
