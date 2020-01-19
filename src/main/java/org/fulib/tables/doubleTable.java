package org.fulib.tables;

import java.util.*;

public class doubleTable
{
   // =============== Fields ===============

   private String               columnName;
   private List<List<Object>>   table     = new ArrayList<>();
   private Map<String, Integer> columnMap = new LinkedHashMap<>();

   // =============== Constructors ===============

   public doubleTable(Double... start)
   {
      this.columnName = "A";
      this.columnMap.put(this.columnName, 0);
      for (Double current : start)
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

   public double sum()
   {
      int column = this.getColumn();
      double result = 0;
      for (List<Object> row : this.table)
      {
         result += (Double) row.get(column);
      }
      return result;
   }

   public double min()
   {
      int column = this.getColumn();
      double result = Double.MAX_VALUE;
      for (List<Object> row : this.table)
      {
         double value = (Double) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public double max()
   {
      int column = this.getColumn();
      double result = Double.MIN_VALUE;
      for (List<Object> row : this.table)
      {
         double value = (Double) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public double median()
   {
      List<Double> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }

   public List<Double> toList()
   {
      int column = this.getColumn();
      List<Double> result = new ArrayList<>();
      for (List<Object> row : this.table)
      {
         double value = (Double) row.get(column);
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
