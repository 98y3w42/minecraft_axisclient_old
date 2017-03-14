package axis.management;

import java.util.List;

public abstract class ListManager <T extends Object> {

	protected List<T>  contents;

	public final List<T> getContents() {
		return this.contents;
	}

	public abstract void setup();

}
