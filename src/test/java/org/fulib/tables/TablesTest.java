package org.fulib.tables;

import org.junit.Test;

public class TablesTest
{
   @Test(expected = IllegalStateException.class)
   public void evictedColumnViaSelect()
   {
      final Table<String> table0 = new StringTable("a", "b", "c");
      final Table<String> table1 = table0.deriveColumn("uppercase", map -> {
         return map.get("A").toString().toUpperCase();
      });
      System.out.println(table1);

      table1.selectColumns("uppercase");

      System.out.println(table1);

      table0.getColumnIndex();
   }

   @Test(expected = IllegalStateException.class)
   public void evictedColumnViaDrop()
   {
      final Table<String> table0 = new StringTable("a", "b", "c");
      final Table<String> table1 = table0.deriveColumn("uppercase", map -> {
         return map.get("A").toString().toUpperCase();
      });
      table1.dropColumns("A");

      table0.getColumnIndex();
   }
}
