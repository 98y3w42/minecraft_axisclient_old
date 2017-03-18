package axis.file;

import java.io.File;

import axis.Axis;

public abstract class CustomFile {
   private final File file;
   private final String name;

   public CustomFile(String name) {
      this.name = name;
      this.file = new File(Axis.getAxis().getDirectory(), name + ".txt");
      if(!this.file.exists()) {
         this.saveFile();
      }

   }

   public final File getFile() {
      return this.file;
   }

   public final String getName() {
      return this.name;
   }

   public abstract void loadFile();

   public abstract void saveFile();
}
