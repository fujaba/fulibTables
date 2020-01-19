package org.fulib.tables;

import java.util.stream.Collectors;

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
      return this.stream().collect(Collectors.joining(seperator));
   }
}
