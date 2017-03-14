package axis.event.events;

import axis.event.Event;

public class StepEvent extends Event {

	public double stepHeight;
	public boolean editing;
	public boolean canStep;
	public State state;

	public StepEvent(double stepHeight, boolean canStep, State state) {
		this.stepHeight = stepHeight;
		this.state = state;
		this.canStep = canStep;
	}

	public StepEvent(State state) {
		this.state = state;
	}

	public void setEdit(boolean editing) {
		this.editing = editing;
	}

	public boolean getEdit() {
		return editing;
	}

	public double getStepHeight() {
		return stepHeight;
	}

	public boolean canStep() {
		return canStep;
	}

}
