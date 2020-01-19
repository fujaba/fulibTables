package org.fulib.tables;

import java.util.List;

public class StringTable extends AbstractTable<String>
{
   // =============== Constructors ===============

   public StringTable(String... start)
   {
      super(start);
   }

   // =============== Methods ===============

   public String join(String seperator)
   {
      int column = this.getColumn();
      StringBuilder buf = new StringBuilder();
      boolean first = true;
      for (List<Object> row : this.getTable())
      {
         String value = (String) row.get(column);
         if (!first)
         {
            buf.append(seperator);
         }
         first = false;
         buf.append(value);
      }
      return buf.toString();
   }
}
