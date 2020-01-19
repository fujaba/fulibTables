package org.fulib.tables;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StringTable
{
   // =============== Fields ===============

   private String               columnName;
   private List<List<Object>>   table     = new ArrayList<>();
   private Map<String, Integer> columnMap = new LinkedHashMap<>();

   // =============== Constructors ===============

   public StringTable(String... start)
   {
      this.columnName = "A";
      this.columnMap.put(this.columnName, 0);
      for (String current : start)
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

   public List<String> toList()
   {
      int column = this.getColumn();
      List<String> result = new ArrayList<>();
      for (List<Object> row : this.table)
      {
         String value =  (String) row.get(column);
         result.add(value);
      }
      return result;
   }

   public String join(String seperator)
   {
      int column = this.getColumn();
      StringBuilder buf = new StringBuilder();
      boolean first = true;
      for (List<Object> row : this.table)
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
