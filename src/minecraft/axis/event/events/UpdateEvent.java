package axis.event.events;

import axis.event.Event;

public class UpdateEvent extends Event {
	public Event.State state;
	public float yaw;
	public float pitch;
	public double y;
	public boolean ground;

	public UpdateEvent() {
		this.state = Event.State.POST;
	}

	public UpdateEvent(double y, float yaw, float pitch, boolean ground) {
		this.state = Event.State.PRE;
		this.yaw = yaw;
		this.pitch = pitch;
		this.y = y;
		this.ground = ground;
	}
}
