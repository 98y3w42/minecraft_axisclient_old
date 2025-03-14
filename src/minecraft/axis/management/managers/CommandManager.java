package axis.management.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

import axis.command.Command;
import axis.management.ListManager;
import axis.util.Logger;

public final class CommandManager extends ListManager {
   public Command getCommandByName(String name) {
      if(this.contents == null) {
         return null;
      } else {
         Iterator var3 = this.contents.iterator();

         while(var3.hasNext()) {
            Command command = (Command)var3.next();
            if(command.getCommand().equalsIgnoreCase(name)) {
               return command;
            }

            String[] var7;
            int var6 = (var7 = command.getAliases()).length;

            for(int var5 = 0; var5 < var6; ++var5) {
               String alias = var7[var5];
               if(alias.equalsIgnoreCase(name)) {
                  return command;
               }
            }
         }

         return null;
      }
   }

   public void setup() {
      Logger.logConsole("Preparing to load commands...");
      this.contents = new ArrayList();
      Reflections reflect = new Reflections(new Object[]{Command.class});
      Set mods = reflect.getSubTypesOf(Command.class);
      Iterator var4 = mods.iterator();

      while(var4.hasNext()) {
         Class clazz = (Class)var4.next();

         try {
            Command var7 = (Command)clazz.newInstance();
            this.getContents().add(var7);
            Logger.logConsole("Loaded command \"" + var7.getCommand() + "\"");
         } catch (InstantiationException var6) {
            var6.printStackTrace();
            Logger.logConsole("Failed to load command \"" + clazz.getSimpleName() + "\" (InstantiationException)");
         } catch (IllegalAccessException var71) {
            var71.printStackTrace();
            Logger.logConsole("Failed to load command \"" + clazz.getSimpleName() + "\" (IllegalAccessException)");
         }
      }

      Logger.logConsole("Successfully loaded " + this.getContents().size() + " commands.");
   }
}
