package axis.file.files;

import java.io.*;
import java.util.Iterator;

import axis.Axis;
import axis.file.CustomFile;

public final class Friends extends CustomFile {
	public Friends() {
		super("friends");
	}

	public void loadFile() {
		try {
			BufferedReader var5 = new BufferedReader(new FileReader(this.getFile()));
			String line;
			while ((line = var5.readLine()) != null) {
				String[] arguments = line.split(":");
				Axis.getFriendManager().addFriend(arguments[0], arguments[1]);
			}
			var5.close();
		} catch (FileNotFoundException var4) {
			var4.printStackTrace();
		} catch (IOException var51) {
			var51.printStackTrace();
		}
	}

	public void saveFile() {
		try {
			BufferedWriter var4 = new BufferedWriter(new FileWriter(this.getFile()));
			Iterator var3 = Axis.getFriendManager().getContents().keySet().iterator();
			while (var3.hasNext()) {
				String name = (String) var3.next();
				var4.write(name + ":" + (String) Axis.getFriendManager().getContents().get(name));
				var4.newLine();
			}
			var4.close();
		} catch (IOException var41) {
			var41.printStackTrace();
		}
	}
}
