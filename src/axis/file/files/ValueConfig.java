package axis.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import axis.Axis;
import axis.file.CustomFile;
import axis.module.modules.combat.KillAura;
import axis.module.modules.combat.killaura.AuraMode;
import axis.module.modules.movement.Speed;
import axis.module.modules.movement.speed.SpeedMode;
import axis.value.Value;

public class ValueConfig extends CustomFile {
	private Value[] values;

	public ValueConfig() {
		super("valueconfig");
	}

	public void loadFile() {
		try {
			BufferedReader var7 = new BufferedReader(new FileReader(this.getFile()));

			while (true) {
				String[] arguments;
				do {
					String line;
					if ((line = var7.readLine()) == null) {
						var7.close();
						return;
					}

					arguments = line.split(":");
				} while (arguments.length != 2);

				Iterator var5 = Value.list.iterator();

				while (var5.hasNext()) {
					Value value = (Value) var5.next();
					if (value != null && arguments[0].equalsIgnoreCase(value.getName())) {
						if (value.isValueBoolean) {
							value.setValue(Boolean.valueOf(Boolean.parseBoolean(arguments[1])));
						} else if (value.isValueByte) {
							value.setValue(Byte.valueOf(Byte.parseByte(arguments[1])));
						} else if (value.isValueDouble) {
							value.setValue(Double.valueOf(Double.parseDouble(arguments[1])));
						} else if (value.isValueFloat) {
							value.setValue(Float.valueOf(Float.parseFloat(arguments[1])));
						} else if (value.isValueInteger) {
							value.setValue(Integer.valueOf(Integer.parseInt(arguments[1])));
						} else if (value.isValueLong) {
							value.setValue(Long.valueOf(Long.parseLong(arguments[1])));
						} else if (value.isValueString) {
							value.setValue(String.valueOf(arguments[1]));
						} else if (value.isValueAuraMode) {
							value.setValue(AuraMode.getMode(arguments[1], ((KillAura) Axis.getModuleManager().getModuleByName("KillAura"))));
						} else if (value.isValueSpeedMode) {
							value.setValue(SpeedMode.getMode(arguments[1], ((Speed) Axis.getModuleManager().getModuleByName("Speed"))));
						}
					}
				}
			}
		} catch (FileNotFoundException var6) {
			var6.printStackTrace();
		} catch (IOException var71) {
			var71.printStackTrace();
		}

	}

	public void saveFile() {
		try {
			BufferedWriter var4 = new BufferedWriter(new FileWriter(this.getFile()));
			Iterator var3 = Value.list.iterator();

			while (var3.hasNext()) {
				Value value = (Value) var3.next();
				if (value != null) {
					var4.write(value.getName() + ":" + value.getValue());
					var4.newLine();
				}
			}

			var4.close();
		} catch (IOException var41) {
			var41.printStackTrace();
		}

	}
}
