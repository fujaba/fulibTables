package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.function.Predicate;

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

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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
         else if (value != null)
         {
            ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
            newRow.add(value);
            this.table.add(newRow);
         }
      }
      return result;
   }

   public ObjectTable hasLink(String linkName, ObjectTable rowName)
   {
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(columnMap.get(this.getColumnName()));
         Object other = row.get(columnMap.get(rowName.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection && ((Collection) value).contains(other) || value == other)
         {
            this.table.add(row);
         }
      }
      return this;
   }

   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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

   public floatTable expandFloat(String newColumnName, String attrName)
   {
      floatTable result = new floatTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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

   public intTable expandInt(String newColumnName, String attrName)
   {
      intTable result = new intTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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

   public longTable expandLong(String newColumnName, String attrName)
   {
      longTable result = new longTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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

   public StringTable expandString(String newColumnName, String attrName)
   {
      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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

   public StringTable expandBoolean(String newColumnName, String attrName)
   {
      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
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

   public void addColumn(String columnName,
      java.util.function.Function<java.util.LinkedHashMap<String, Object>, Object> function)
   {
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      for (ArrayList<Object> row : this.table)
      {
         java.util.LinkedHashMap<String, Object> map = new java.util.LinkedHashMap<>();
         for (String key : columnMap.keySet())
         {
            map.put(key, row.get(columnMap.get(key)));
         }
         Object result = function.apply(map);
         row.add(result);
      }
      this.columnMap.put(columnName, newColumnNumber);
   }

   public ObjectTable dropColumns(String... columnNames)
   {
      LinkedHashMap<String, Integer> oldColumnMap = (LinkedHashMap<String, Integer>) this.columnMap.clone();
      this.columnMap.clear();

      LinkedHashSet<String> dropNames = new LinkedHashSet<>();
      dropNames.addAll(Arrays.asList(columnNames));
      int i = 0;
      for (String name : oldColumnMap.keySet())
      {
         if (!dropNames.contains(name))
         {
            this.columnMap.put(name, i);
            i++;
         }
      }

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
      this.table.clear();

      LinkedHashSet<ArrayList<Object>> rowSet = new LinkedHashSet<>();
      for (ArrayList row : oldTable)
      {
         ArrayList newRow = new ArrayList();
         for (String name : this.columnMap.keySet())
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
            this.table.add(newRow);
      }

      return this;
   }

   public ObjectTable selectColumns(String... columnNames)
   {
      LinkedHashMap<String, Integer> oldColumnMap = (LinkedHashMap<String, Integer>) this.columnMap.clone();
      this.columnMap.clear();

      int i = 0;
      for (String name : columnNames)
      {
         if (oldColumnMap.get(name) == null)
            throw new IllegalArgumentException("unknown column name: " + name);
         this.columnMap.put(name, i);
         i++;
      }

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
      this.table.clear();

      LinkedHashSet<ArrayList<Object>> rowSet = new LinkedHashSet<>();
      for (ArrayList row : oldTable)
      {
         ArrayList newRow = new ArrayList();
         for (String name : columnNames)
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
            this.table.add(newRow);
      }

      return this;
   }

   public ObjectTable filter(Predicate<Object> predicate)
   {
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(columnMap.get(this.getColumnName()));
         if (predicate.test(start))
         {
            this.table.add(row);
         }
      }
      return this;
   }

   public ObjectTable filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         LinkedHashMap<String, Object> map = new LinkedHashMap<>();
         for (String key : columnMap.keySet())
         {
            map.put(key, row.get(columnMap.get(key)));
         }
         if (predicate.test(map))
         {
            this.table.add(row);
         }
      }
      return this;
   }

   public LinkedHashSet<Object> toSet()
   {
      LinkedHashSet<Object> result = new LinkedHashSet<>();
      for (ArrayList row : this.table)
      {
         Object value = row.get(columnMap.get(columnName));
         result.add(value);
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
