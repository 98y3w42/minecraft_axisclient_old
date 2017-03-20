package axis.file.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import axis.Axis;
import axis.file.CustomFile;
import axis.module.Module;
import axis.value.ValueList;

public class ValueConfig2 extends CustomFile {

	public ValueConfig2() {
		super("valueconfig2", false);
	}

	public void loadFile() {
		try {
			for (Object obj : Axis.getAxis().getModuleManager().getContents()) {
				Module module = (Module)obj;
				ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream(Axis.getAxis().getDirectory() + "/Values/" + module.getName()));
				module.values = (ValueList) objInStream.readObject();
				objInStream.close();
			}

		} catch (FileNotFoundException e) {
			saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveFile() {
		try {
			for (Object obj : Axis.getAxis().getModuleManager().getContents()) {
				Module module = (Module)obj;
				ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(Axis.getAxis().getDirectory() + "/Values/" + module.getName()));
				objOutStream.writeObject(module.values);
				objOutStream.close();
			}
		} catch (FileNotFoundException e) {
			new File(Axis.getAxis().getDirectory(), "Values").mkdirs();
			saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
