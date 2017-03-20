package axis.file;

import java.io.File;

import axis.Axis;

public abstract class CustomFile {
   private File file;
   private String name;

   public CustomFile(String name, Boolean txt) {
      this.name = name;
      this.file = new File(Axis.getAxis().getDirectory(), name + ".txt");
      if(!this.file.exists() && txt) {
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
