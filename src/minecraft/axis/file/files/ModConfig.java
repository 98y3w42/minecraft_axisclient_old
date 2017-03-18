package axis.file.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import axis.Axis;
import axis.file.CustomFile;
import axis.module.Module;

public final class ModConfig extends CustomFile {
   public ModConfig() {
      super("modconfig");
   }

   public void loadFile() {
      try {
         BufferedReader var6 = new BufferedReader(new FileReader(this.getFile()));

         String line;
         while((line = var6.readLine()) != null) {
            String[] arguments = line.split(":");
            if(arguments.length == 4) {
               Module mod = Axis.getAxis().getModuleManager().getModuleByName(arguments[0]);
               if(mod != null) {
                  mod.setEnabled(Boolean.parseBoolean(arguments[1]));
                  mod.setKeybind(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                  mod.setVisible(Boolean.parseBoolean(arguments[3]));
               }
            }
         }

         var6.close();
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var61) {
         var61.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         BufferedWriter var4 = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var3 = Axis.getAxis().getModuleManager().getContents().iterator();

         while(var3.hasNext()) {
        	 Module mod = (Module)var3.next();
            var4.write(mod.getName().toLowerCase() + ":" + mod.isEnabled() + ":" + Keyboard.getKeyName(mod.getKeybind()) + ":" + mod.isVisible());
            var4.newLine();
         }

         var4.close();
      } catch (IOException var41) {
         var41.printStackTrace();
      }

   }
}
