package axis.file.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import axis.Axis;
import axis.file.CustomFile;
import axis.util.Alt;

public final class LastAlt extends CustomFile {
   public LastAlt() {
      super("lastalt", true);
   }

   public void loadFile() {
      try {
         BufferedReader var8 = new BufferedReader(new FileReader(this.getFile()));

         while(true) {
            String s;
            while((s = var8.readLine()) != null) {
               if(s.contains("\t")) {
                  s = s.replace("\t", "    ");
               }

               String[] account;
               if(s.contains("    ")) {
                  account = s.split("    ");
                  String[] var9 = account[1].split(":");
                  if(var9.length == 2) {
                	  Axis.getAxis().getAltManager().setLastAlt(new Alt(var9[0], var9[1], account[0]));
                  } else {
                     String var10 = var9[1];

                     for(int i1 = 2; i1 < var9.length; ++i1) {
                        var10 = var10 + ":" + var9[i1];
                     }

                     Axis.getAxis().getAltManager().setLastAlt(new Alt(var9[0], var10, account[0]));
                  }
               } else {
                  account = s.split(":");
                  if(account.length == 1) {
                	  Axis.getAxis().getAltManager().setLastAlt(new Alt(account[0], ""));
                  } else if(account.length == 2) {
                	  Axis.getAxis().getAltManager().setLastAlt(new Alt(account[0], account[1]));
                  } else {
                     String pw = account[1];

                     for(int i = 2; i < account.length; ++i) {
                        pw = pw + ":" + account[i];
                     }

                     Axis.getAxis().getAltManager().setLastAlt(new Alt(account[0], pw));
                  }
               }
            }

            var8.close();
            break;
         }
      } catch (FileNotFoundException var7) {
         var7.printStackTrace();
      } catch (IOException var81) {
         var81.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         PrintWriter var3 = new PrintWriter(this.getFile());
         Alt alt = Axis.getAxis().getAltManager().getLastAlt();
         if(alt != null) {
            if(alt.getMask().equals("")) {
               var3.println(alt.getUsername() + ":" + alt.getPassword());
            } else {
               var3.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
            }
         }

         var3.close();
      } catch (FileNotFoundException var31) {
         var31.printStackTrace();
      }

   }
}
