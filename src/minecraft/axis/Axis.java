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
import axis.management.managers.ModeManager;
import axis.management.managers.ModuleManager;
import axis.management.managers.ValueManager;
import axis.module.Module;
import axis.ui.chat.HexNewChat;
import axis.ui.tabgui.TabGui;
import axis.util.FontUtil;
import axis.util.Logger;
import net.minecraft.client.Minecraft;

public class Axis {

	private static Axis theAxis;
	private String  clientName = "Axis";
	private String version =  "ver 0.5";
	private final File directory = new File(Minecraft.getMinecraft().mcDataDir, getName());

	private EventManager eventManager = new EventManager();
	private FileManager fileManager = new FileManager();
	private ModuleManager moduleManager = new ModuleManager();
	private ModeManager modeManager = new ModeManager();
	private CommandManager commandManager = new CommandManager();
	private ValueManager valueManager = new ValueManager();
	private FriendManager friendManager = new FriendManager();
	private AltManager altManager = new AltManager();
	private TabGui tabGui;
	public FontUtil font;

	public Axis() {
		theAxis = this;
		Display.setTitle(getName() + " " + getVersion());
	}

	public static Axis getAxis() {
		return theAxis;
	}

	public String getName() {
		return clientName;
	}

	public String getVersion() {
		return version;
	}

	public void onCheckUpdate() {
	}

	public File getDirectory() {
		return directory;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public ValueManager getValueManager() {
		return valueManager;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public ModeManager getModeManager() {
		return modeManager;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void onSetupManagers() {
		getValueManager().setup();
		friendManager.setup();
		getCommandManager().setup();
		getModuleManager().setup();
		getModeManager().setup();
		getAltManager().setup();
		getFileManager().setup();
		Minecraft.getMinecraft().ingameGUI.persistantChatGUI = new HexNewChat(Minecraft.getMinecraft());
	}

	public void onStartup()  {
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

	public FriendManager getFriendManager() {
		return friendManager;
	}

	public TabGui getTabGUI() {
		return tabGui;
	}

	public AltManager getAltManager() {
		return altManager;
	}
}
