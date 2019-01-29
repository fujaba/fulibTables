package org.fulib.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class longTable
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

   public longTable(Long... start)
   {
      columnName = "A";
      columnMap.put(columnName, 0);
      for (Long current : start)
      {
         ArrayList<Object> row = new ArrayList<>();
         row.add(current);
         table.add(row);
      }
   }


   public long sum ()
   {
      long result = 0;
      for (ArrayList<Object> row : table)
      {
         result += (Long) row.get(columnMap.get(columnName));
      }
      return result;
   }


   public long min ()
   {
      long result = Long.MAX_VALUE;
      for (ArrayList<Object> row : table)
      {
         long value =  (Long) row.get(columnMap.get(columnName));
         if (value < result)
            result = value;
      }
      return result;
   }


   public long max ()
   {
      long result = Long.MIN_VALUE;
      for (ArrayList<Object> row : table)
      {
         long value =  (Long) row.get(columnMap.get(columnName));
         if (value > result)
            result = value;
      }
      return result;
   }


   public long median ()
   {
      ArrayList< Long > list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      long result = list.get(index);
      return result;
   }


   public ArrayList< Long > toList()
   {
      ArrayList< Long > result = new ArrayList<>();
      for (ArrayList<Object> row : table)
      {
         long value =  (Long) row.get(columnMap.get(columnName));
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