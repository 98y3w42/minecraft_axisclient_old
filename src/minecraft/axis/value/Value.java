package axis.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import axis.Axis;
import axis.module.modules.combat.killaura.AuraMode;
import axis.module.modules.movement.speed.SpeedMode;

public class Value<T> {
	private T value;
	private final T defaultValue;
	private final String name;
	public boolean isValueBoolean;
	public boolean isValueInteger;
	public boolean isValueFloat;
	public boolean isValueDouble;
	public boolean isValueLong;
	public boolean isValueByte;
	public boolean isValueString;
	public boolean isValueAuraMode;
	public boolean isValueSpeedMode;
	public static final List<Value> list;

	static {
		list = new ArrayList<Value>();
	}

	public Value(final String name, final T value) {
		this.isValueBoolean = false;
		this.isValueInteger = false;
		this.isValueFloat = false;
		this.isValueDouble = false;
		this.isValueLong = false;
		this.isValueByte = false;
		this.isValueString = false;
		this.isValueAuraMode = false;
		this.isValueSpeedMode = false;
		this.defaultValue = value;
		this.name = name;
		this.value = value;
		if (value instanceof Boolean) {
			this.isValueBoolean = true;
		} else if (value instanceof Integer) {
			this.isValueInteger = true;
		} else if (value instanceof Float) {
			this.isValueFloat = true;
		} else if (value instanceof Double) {
			this.isValueDouble = true;
		} else if (value instanceof Long) {
			this.isValueLong = true;
		} else if (value instanceof Byte) {
			this.isValueByte = true;
		} else if (value instanceof String) {
			this.isValueString = true;
		} else if (value instanceof AuraMode) {
			this.isValueAuraMode = true;
		} else if (value instanceof SpeedMode) {
			this.isValueSpeedMode = true;
		}
		Value.list.add(this);
		Collections.sort(Value.list, new Comparator() {
			public int compare(final Value val1, final Value val2) {
				return val1.getName().compareTo(val2.getName());
			}

			@Override
			public int compare(final Object var1, final Object var2) {
				return this.compare((Value) var1, (Value) var2);
			}
		});
	}

	public final String getName() {
		return this.name;
	}

	public final T getDefaultValue() {
		return this.defaultValue;
	}

	public final T getValue() {
		return this.value;
	}

	public final void setValue(final T value) {
		this.value = value;
		Axis.getAxis().getFileManager().getFileByName("valueconfig").saveFile();
	}
}
