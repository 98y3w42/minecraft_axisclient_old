package axis;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.opengl.Display;

import axis.command.Command;
import axis.management.managers.AltManager;
import axis.management.managers.CommandManager;
import axis.management.managers.EventManager;
import axis.management.managers.FileManager;
import axis.management.managers.FriendManager;
import axis.management.managers.ModuleManager;
import axis.management.managers.ValueManager;
import axis.module.Module;
import axis.ui.chat.HexNewChat;
import axis.ui.tabgui.TabGui;
import axis.util.FontUtil;
import axis.util.Logger;
import net.minecraft.client.Minecraft;

public class Axis {

	private static String  clientName = "Axis";
	private static String version =  "ver 0.5";
	private static final File directory = new File(Minecraft.getMinecraft().mcDataDir, getName());

	private static EventManager eventManager = new EventManager();
	private static FileManager fileManager = new FileManager();
	private static ModuleManager moduleManager = new ModuleManager();
	private static CommandManager commandManager = new CommandManager();
	private static ValueManager valueManager = new ValueManager();
	private static FriendManager friendManager = new FriendManager();
	private static AltManager altManager = new AltManager();
	private static TabGui tabGui;
	public static FontUtil font;

	public static String getName() {
		return clientName;
	}

	public static String getVersion() {
		return version;
	}

	public void onCheckUpdate() {
	}

	public static File getDirectory() {
		return directory;
	}

	public static EventManager getEventManager() {
		return eventManager;
	}

	public static ValueManager getValueManager() {
		return valueManager;
	}

	public static ModuleManager getModuleManager() {
		return moduleManager;
	}

	public static FileManager getFileManager() {
		return fileManager;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static void onSetupManagers() {
		getValueManager().setup();
		friendManager.setup();
		getCommandManager().setup();
		getModuleManager().setup();
		getAltManager().setup();
		getFileManager().setup();
		Minecraft.getMinecraft().ingameGUI.persistantChatGUI = new HexNewChat(Minecraft.getMinecraft());
	}

	public static void onStartup()  {
		Display.setTitle(getName() + " " + getVersion());
		Logger.logConsole("Version" + getVersion());
		if (!directory.isDirectory()) {
			directory.mkdirs();
		}
		Logger.logConsole("Waiting Login...");

		onSetupManagers();

		Collections.sort(moduleManager.getContents(), new Comparator() {
			public int compare(final Module mod1, final Module mod2) {
				return mod1.getName().compareTo(mod2.getName());
			}

			@Override
			public int compare(final Object var1, final Object var2) {
				return this.compare((Module)var1, (Module)var2);
			}
		});

		Collections.sort(commandManager.getContents(), new Comparator() {
			public int compare(final Command mod1, final Command mod2) {
				return mod1.getCommand().compareTo(mod2.getCommand());
			}

			@Override
			public int compare(final Object var1, final Object var2) {
				return this.compare((Command)var1, (Command)var2);
			}
		});

		Logger.logConsole("Successfully loaded " + getName() + " " + getVersion());
	}

	public static FriendManager getFriendManager() {
		return friendManager;
	}

	public static TabGui getTabGUI() {
		return tabGui;
	}

	public static AltManager getAltManager() {
		return altManager;
	}
}
