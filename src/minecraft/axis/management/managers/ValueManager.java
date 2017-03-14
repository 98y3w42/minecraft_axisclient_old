package axis.management.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import axis.management.ListManager;
import axis.util.Logger;
import axis.value.Value;


public class ValueManager extends ListManager {
   public final Value getValueByName(String name) {
      Iterator var3 = this.contents.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         if(value.getName().equalsIgnoreCase(name)) {
            return value;
         }
      }

      return null;
   }

   public void setup() {
      Logger.logConsole("Starting to load up the values.");
      this.contents = new ArrayList();
      Collections.sort(this.contents, new Comparator() {
         public int compare(Value value1, Value value2) {
            return value1.getName().compareTo(value2.getName());
         }

         public int compare(Object var1, Object var2) {
            return this.compare((Value)var1, (Value)var2);
         }
      });
      Logger.logConsole("Successfully loaded " + this.getContents().size() + " values.");
   }
}