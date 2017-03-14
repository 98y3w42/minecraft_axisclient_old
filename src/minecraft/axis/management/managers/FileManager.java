package axis.management.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

import axis.file.CustomFile;
import axis.management.ListManager;
import axis.util.Logger;
import net.minecraft.util.StringUtils;

public final class FileManager extends ListManager {

	public final CustomFile getFileByName(String name) {
		if (this.contents == null) {
			return null;
		} else {
			Iterator var3 = this.contents.iterator();

			while (var3.hasNext()) {
				CustomFile file = (CustomFile) var3.next();
				if (file.getName().equalsIgnoreCase(name)) {
					return file;
				}
			}

			return null;
		}
	}

	public boolean isFriend(String name) {
		return this.contents.contains(StringUtils.stripControlCodes(name));
	}

	public void setup() {
		Logger.logConsole("Preparing to load files...");
		this.contents = new ArrayList();
		Reflections reflect = new Reflections(new Object[] { CustomFile.class });
		Set files = reflect.getSubTypesOf(CustomFile.class);
		Iterator var4 = files.iterator();

		while (var4.hasNext()) {
			Class clazz = (Class) var4.next();

			try {
				CustomFile e = (CustomFile) clazz.newInstance();
				this.getContents().add(e);
				e.loadFile();
				Logger.logConsole("Loaded file \"" + e.getName() + "\"");
			} catch (InstantiationException var6) {
				var6.printStackTrace();
				Logger.logConsole("Failed to load file \"" + clazz.getSimpleName() + "\" (InstantiationException)");
			} catch (IllegalAccessException var7) {
				var7.printStackTrace();
				Logger.logConsole("Failed to load file \"" + clazz.getSimpleName() + "\" (IllegalAccessException)");
			}
		}

		Logger.logConsole("Successfully loaded " + this.getContents().size() + " files.");
	}
}
