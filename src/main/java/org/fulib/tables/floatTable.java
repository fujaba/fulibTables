package org.fulib.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class floatTable
{
   private ArrayList<ArrayList<Object> > table = new ArrayList<>();

   public ArrayList<ArrayList<Object> > getTable()
   {
      return table;
   }

   public void setTable(ArrayList<ArrayList<Object> > table)
   {
      this.table = table;
   }

   private String columnName = null;

   public String getColumnName()
   {
      return columnName;
   }

   public void setColumnName(String columnName)
   {
      this.columnName = columnName;
   }

   LinkedHashMap<String, Integer> columnMap = new LinkedHashMap<>();

   public void setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
   }

   public floatTable(Float... start)
   {
      columnName = "A";
      columnMap.put(columnName, 0);
      for (Float current : start)
      {
         ArrayList<Object> row = new ArrayList<>();
         row.add(current);
         table.add(row);
      }
   }


   public float sum ()
   {
      float result = 0;
      for (ArrayList<Object> row : table)
      {
         result += (Float) row.get(columnMap.get(columnName));
      }
      return result;
   }


   public float min ()
   {
      float result = Float.MAX_VALUE;
      for (ArrayList<Object> row : table)
      {
         float value =  (Float) row.get(columnMap.get(columnName));
         if (value < result)
            result = value;
      }
      return result;
   }


   public float max ()
   {
      float result = Float.MIN_VALUE;
      for (ArrayList<Object> row : table)
      {
         float value =  (Float) row.get(columnMap.get(columnName));
         if (value > result)
            result = value;
      }
      return result;
   }


   public float median ()
   {
      ArrayList< Float > list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      float result = list.get(index);
      return result;
   }


   public ArrayList< Float > toList()
   {
      ArrayList< Float > result = new ArrayList<>();
      for (ArrayList<Object> row : table)
      {
         float value =  (Float) row.get(columnMap.get(columnName));
         result.add(value);
      }
      return result;
   }

   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      for (String key : columnMap.keySet())
      {
         buf.append(key).append(" \t");
      }
      buf.append("\n");
      for (ArrayList<Object> row : table)
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