package org.fulib.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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

   protected PrimitiveTable(Table<?> base)
   {
      super(base);
   }

   protected static <T extends Comparable<T>> T medianImpl(Table<T> table)
   {
      List<T> list = table.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }

   // =============== Properties ===============

   /**
    * @param columnName
    *    the name of the column this table should point to
    *
    * @deprecated since 1.2; set via the constructor and not meant to be changed afterward
    */
   @Deprecated
   public void setColumnName(String columnName)
   {
      this.setColumnName_(columnName);
   }

   /**
    * @param table
    *    the list of rows
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public void setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
   }

   /**
    * @param columnMap
    *    the map from column name to index in the lists that make up rows
    *
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
