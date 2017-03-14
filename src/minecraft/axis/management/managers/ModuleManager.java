package axis.management.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

import axis.management.ListManager;
import axis.module.Module;
import axis.util.Logger;


public class ModuleManager extends ListManager {

private String type;

public final Module getModuleByName(String name) {
      if(this.contents == null) {
         return null;
      } else {
         try {
            Iterator var3 = this.contents.iterator();

            while(var3.hasNext()) {
               Module Module = (Module)var3.next();
               if(Module.getName().equalsIgnoreCase(name)) {
                  return Module;
               }
            }
         } catch (ConcurrentModificationException var4) {
            ;
         }

         return null;
      }
   }

   public void setup() {
      Logger.logChat("Starting to load up the Modules.");
      this.contents = new ArrayList();
      Reflections reflect = new Reflections(new Object[]{Module.class});
      Set Modules = reflect.getSubTypesOf(Module.class);
      Iterator var4 = Modules.iterator();

      while(var4.hasNext()) {
         Class clazz = (Class)var4.next();
         try {
            Module var7 = (Module)clazz.newInstance();
            this.getContents().add(var7);
            Logger.logChat("Loaded Module \"" + var7.getName() + "\"");
         } catch (InstantiationException var6) {
            var6.printStackTrace();
            Logger.logChat("Failed to load Module \"" + clazz.getSimpleName() + "\" (InstantiationException)");
         } catch (IllegalAccessException var71) {
            var71.printStackTrace();
            Logger.logChat("Failed to load Module \"" + clazz.getSimpleName() + "\" (IllegalAccessException)");
         }
      }

      Collections.sort(this.contents, new Comparator() {
         public int compare(Module Module1, Module Module2) {
            return Module1.getName().compareTo(Module2.getName());
         }

         public int compare(Object var1, Object var2) {
            return this.compare((Module)var1, (Module)var2);
         }
      });
      Logger.logChat("Successfully loaded " + this.getContents().size() + " Modules.");
   }

   public static enum Category {
	   MOVEMENT("MOVEMENT", 1),
	   EXPLOITS("EXPLOITS", 2),
	   RENDER("RENDER", 3),
	   COMBAT("COMBAT", 4),
	   PLAYER("PLAYER", 5);

      private static final ModuleManager.Category[] ENUM$VALUES;

      static {
         ENUM$VALUES = new ModuleManager.Category[]{COMBAT,MOVEMENT, PLAYER, RENDER, EXPLOITS};
      }

      private Category(String var11, int var21) {
      }
   }
}
