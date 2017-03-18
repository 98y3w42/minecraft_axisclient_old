package axis.value;

import java.util.HashMap;

import axis.Axis;

public class ValueList<T> {
	private String name;
	public HashMap<String, T> values = new HashMap();
	public HashMap<String, T> defaultValues = new HashMap();

	public ValueList(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addValue(String name, T value) {
		values.put(name, value);
		defaultValues.put(name, value);
	}

	public T getValue(String name) {
		return values.get(name);
	}

	public final T getDefaultValue(String name) {
		return defaultValues.get(name);
	}

	public void setValue(String name, T value) {
		values.put(name, value);
		Axis.getAxis().getFileManager().getFileByName("valueconfig").saveFile();
	}
}
