package axis.event;

import java.lang.reflect.InvocationTargetException;

import axis.management.managers.EventManager;

public abstract class Event {
	private boolean cancelled;
	public State state;

	public Event call() {
		this.cancelled = false;
		call(this);
		return this;
	}



	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(final boolean state) {
		this.cancelled = state;
	}

	private static final void call(final Event event) {
		final FlexibleArray<MethodData> dataList = EventManager.get(event.getClass());
		if (dataList != null) {
			for (final MethodData data : dataList) {
				try {
					data.target.invoke(data.source, event);
				}
				catch (IllegalAccessException e) {
					System.out.println("Can't invoke '" + data.target.getName() + "' because it's not accessible.");
				}
				catch (IllegalArgumentException e2) {
					System.out.println("Can't invoke '" + data.target.getName() + "' because the parameter/s don't match.");
				}
				catch (InvocationTargetException ex) {}
			}
		}
	}

	public static enum State
	{
	  PRE,  POST;
	}
}
