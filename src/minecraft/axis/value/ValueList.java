package axis.value;

import java.io.Serializable;
import java.util.HashMap;

import axis.Axis;

public class ValueList<T> implements Serializable {
	private String name;
	public final HashMap<String, T> values = new HashMap();
	public final HashMap<String, T> defaultValues = new HashMap();

	public ValueList(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addValue(final String name, final T value) {
		values.put(name, value);
		defaultValues.put(name, value);
	}

	public T getValue(final String name) {
		return values.get(name);
	}

	public final T getDefaultValue(final String name) {
		return defaultValues.get(name);
	}

	public void setValue(final String name, final T value) {
		values.put(name, value);
		Axis.getAxis().getFileManager().getFileByName("valueconfig2").saveFile();
	}
}
