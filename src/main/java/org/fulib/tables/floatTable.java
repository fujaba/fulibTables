package org.fulib.tables;

import java.util.*;

public class floatTable
{
   // =============== Fields ===============

   private String               columnName;
   private List<List<Object>>   table     = new ArrayList<>();
   private Map<String, Integer> columnMap = new LinkedHashMap<>();

   // =============== Constructors ===============

   public floatTable(Float... start)
   {
      this.columnName = "A";
      this.columnMap.put(this.columnName, 0);
      for (Float current : start)
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

   public float sum()
   {
      int column = this.getColumn();
      float result = 0;
      for (List<Object> row : this.table)
      {
         result += (Float) row.get(column);
      }
      return result;
   }

   public float min()
   {
      int column = this.getColumn();
      float result = Float.MAX_VALUE;
      for (List<Object> row : this.table)
      {
         float value = (Float) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public float max()
   {
      int column = this.getColumn();
      float result = Float.MIN_VALUE;
      for (List<Object> row : this.table)
      {
         float value = (Float) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public float median()
   {
      List<Float> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }

   public List<Float> toList()
   {
      int column = this.getColumn();
      List<Float> result = new ArrayList<>();
      for (List<Object> row : this.table)
      {
         float value = (Float) row.get(column);
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
