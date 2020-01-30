package org.fulib.tables;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

// TODO remove in v2
// only exists for the deprecated setTable and setColumnMap methods
class PrimitiveTable<T> extends Table<T>
{
   @SafeVarargs
   public PrimitiveTable(T... start)
   {
      super(start);
   }

   public PrimitiveTable(Table<?> base)
   {
      super(base);
   }

   // =============== Properties ===============

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public void setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
   }

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public void setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
   }

   @Override
   public ArrayList<T> toList()
   {
      return this.stream().collect(Collectors.toCollection(ArrayList::new));
   }
}
