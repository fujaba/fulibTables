package org.fulib.tables;

import java.util.stream.Collectors;

public class StringTable extends PrimitiveTable<String>
{
   // =============== Constructors ===============

   public StringTable(String... start)
   {
      super(start);
   }

   protected StringTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   public String join(String separator)
   {
      return this.stream().collect(Collectors.joining(separator));
   }
}
