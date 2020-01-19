package org.fulib.tables;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class AbstractTable<T>
{
   // =============== Fields ===============

   private String               columnName;
   private List<List<Object>>   table     = new ArrayList<>();
   private Map<String, Integer> columnMap = new LinkedHashMap<>();

   // =============== Constructors ===============

   @SafeVarargs
   public AbstractTable(T... start)
   {
      this.columnName = "A";
      this.columnMap.put(this.columnName, 0);
      for (T current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }

   // =============== Properties ===============

   public String getColumnName()
   {
      return this.columnName;
   }

   public void setColumnName(String columnName)
   {
      this.columnName = columnName;
   }

   public int getColumn()
   {
      return this.columnMap.get(this.columnName);
   }

   public List<List<Object>> getTable()
   {
      return this.table;
   }

   public void setTable(List<List<Object>> table)
   {
      this.table = table;
   }

   public void setColumnMap(Map<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
   }

   // =============== Methods ===============

   public List<T> toList()
   {
      int column = this.getColumn();
      List<T> result = new ArrayList<>();
      for (List<Object> row : this.getTable())
      {
         T value = (T) row.get(column);
         result.add(value);
      }
      return result;
   }

   public Stream<T> stream()
   {
      int column = this.getColumn();
      return this.table.stream().map(l -> (T) l.get(column));
   }

   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      for (String key : this.columnMap.keySet())
      {
         buf.append(key).append(" \t");
      }
      buf.append("\n");
      for (List<Object> row : this.table)
      {
         for (Object cell : row)
         {
            buf.append(cell).append(" \t");
         }
         buf.append("\n");
      }
      buf.append("\n");
      return buf.toString();
   }
}
