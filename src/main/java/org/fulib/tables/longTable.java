package org.fulib.tables;

import java.util.*;

public class longTable
{
   // =============== Fields ===============

   private String               columnName;
   private List<List<Object>>   table     = new ArrayList<>();
   private Map<String, Integer> columnMap = new LinkedHashMap<>();

   // =============== Constructors ===============

   public longTable(Long... start)
   {
      this.columnName = "A";
      this.columnMap.put(this.columnName, 0);
      for (Long current : start)
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

   public long sum()
   {
      int column = this.getColumn();
      long result = 0;
      for (List<Object> row : this.table)
      {
         result += (Long) row.get(column);
      }
      return result;
   }

   public long min()
   {
      int column = this.getColumn();
      long result = Long.MAX_VALUE;
      for (List<Object> row : this.table)
      {
         long value = (Long) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public long max()
   {
      int column = this.getColumn();
      long result = Long.MIN_VALUE;
      for (List<Object> row : this.table)
      {
         long value = (Long) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public long median()
   {
      List<Long> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }

   public List<Long> toList()
   {
      int column = this.getColumn();
      List<Long> result = new ArrayList<>();
      for (List<Object> row : this.table)
      {
         long value = (Long) row.get(column);
         result.add(value);
      }
      return result;
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
