package axis.management.managers;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

import axis.management.ListManager;
import axis.module.Mode;
import axis.module.Module;
import axis.util.Logger;


public class ModeManager extends ListManager {

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
      Logger.logChat("Starting to load up the Modes.");
      this.contents = new ArrayList();
      Reflections reflect = new Reflections(new Object[]{Mode.class});
      Set Mode = reflect.getSubTypesOf(Mode.class);
      Iterator var4 = Mode.iterator();

      while(var4.hasNext()) {
         Class clazz = (Class)var4.next();
         try {
        	 Mode var7 = (Mode)clazz.newInstance();
        	 Logger.logChat("Loaded Mode \"" + var7 + "\"");
         } catch (Exception var6) {
         }
      }
      Logger.logChat("Successfully loaded " + Mode.size() + " Modes.");
   }
}
