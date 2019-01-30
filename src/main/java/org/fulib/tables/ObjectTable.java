package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ObjectTable
{
   public ObjectTable(Object... start)
   {
      this("A", start);
   }

   public ObjectTable(String colName, Object... start)
   {
      this.setColumnName(colName);
      columnMap.put(colName, 0);
      for (Object current : start)
      {
         ArrayList<Object> row = new ArrayList<>();
         row.add(current);
         table.add(row);

         if (reflectorMap == null)
         {
            reflectorMap = new ReflectorMap(current.getClass().getPackage().getName());
         }
      }
   }

   private ReflectorMap reflectorMap;

   public ReflectorMap getReflectorMap()
   {
      return reflectorMap;
   }

   public void setReflectorMap(ReflectorMap reflectorMap)
   {
      this.reflectorMap = reflectorMap;
   }

   private ArrayList<ArrayList<Object>> table = new ArrayList<>();

   public ArrayList<ArrayList<Object>> getTable()
   {
      return table;
   }

   public ObjectTable setTable(ArrayList<ArrayList<Object>> value)
   {
      this.table = value;
      return this;
   }


   private String columnName = null;

   public String getColumnName()
   {
      return columnName;
   }

   public ObjectTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }


   private LinkedHashMap<String, Integer> columnMap = new LinkedHashMap<>();

   public LinkedHashMap<String, Integer> getColumnMap()
   {
      return columnMap;
   }

   public ObjectTable setColumnMap(LinkedHashMap<String, Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public ObjectTable expandLink(String newColumnName, String linkName)
   {
      ObjectTable result = new ObjectTable();
      result.setColumnMap(this.columnMap);
      result.setTable(table);
      result.setReflectorMap(reflectorMap);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;

      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection)
         {
            for (Object current : (Collection) value)
            {
               ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
               newRow.add(current);
               this.table.add(newRow);
            }
         }
         else
         {
            ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
            newRow.add(value);
            this.table.add(newRow);
         }
      }
      return result;
   }


   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = (Object) row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.table.add(newRow);
      }

      return result;
   }



   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder("| ");
      for (String key : columnMap.keySet())
      {
         buf.append(key).append(" \t| ");
      }
      buf.append("\n| ");
      for (String key : columnMap.keySet())
      {
         buf.append(" --- \t| ");
      }
      buf.append("\n");
      for (ArrayList<Object> row : table)
      {
         buf.append("| ");
         for (Object cell : row)
         {
            buf.append(cell).append(" \t| ");
         }
         buf.append("\n");
      }
      buf.append("\n");
      return buf.toString();
   }

}
