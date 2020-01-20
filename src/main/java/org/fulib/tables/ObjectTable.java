package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ObjectTable extends AbstractTable<Object>
{
   private ReflectorMap reflectorMap;

   // =============== Constructors ===============

   public ObjectTable(Object... start)
   {
      super(start);
      this.initReflector(start);
   }

   public ObjectTable(String colName, Object... start)
   {
      super(colName, start);
      this.initReflector(start);
   }

   private void initReflector(Object... start)
   {
      if (start.length == 0)
      {
         return;
      }

      this.reflectorMap = new ReflectorMap(start[0].getClass().getPackage().getName());
   }

   // =============== Properties ===============

   public ReflectorMap getReflectorMap()
   {
      return this.reflectorMap;
   }

   public void setReflectorMap(ReflectorMap reflectorMap)
   {
      this.reflectorMap = reflectorMap;
   }

   // =============== Methods ===============

   public ObjectTable expandLink(String newColumnName, String linkName)
   {
      ObjectTable result = new ObjectTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      result.setReflectorMap(this.reflectorMap);
      int newColumnNumber = this.getNewColumnNumber();

      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection)
         {
            for (Object current : (Collection<?>) value)
            {
               List<Object> newRow = new ArrayList<>(row);
               newRow.add(current);
               this.getTable().add(newRow);
            }
         }
         else if (value != null)
         {
            List<Object> newRow = new ArrayList<>(row);
            newRow.add(value);
            this.getTable().add(newRow);
         }
      }
      return result;
   }

   public ObjectTable hasLink(String linkName, ObjectTable rowName)
   {
      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Object other = row.get(this.getColumnMap().get(rowName.getColumnName()));
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection && ((Collection<?>) value).contains(other) || value == other)
         {
            this.getTable().add(row);
         }
      }
      return this;
   }

   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = this.getNewColumnNumber();
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public floatTable expandFloat(String newColumnName, String attrName)
   {
      floatTable result = new floatTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = this.getNewColumnNumber();
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public intTable expandInt(String newColumnName, String attrName)
   {
      intTable result = new intTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = this.getNewColumnNumber();
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public longTable expandLong(String newColumnName, String attrName)
   {
      longTable result = new longTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = this.getNewColumnNumber();
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public StringTable expandString(String newColumnName, String attrName)
   {
      StringTable result = new StringTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = this.getNewColumnNumber();
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public StringTable expandBoolean(String newColumnName, String attrName)
   {
      StringTable result = new StringTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = this.getNewColumnNumber();
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public void addColumn(String columnName, Function<LinkedHashMap<String, Object>, Object> function)
   {
      int newColumnNumber = this.getNewColumnNumber();
      for (List<Object> row : this.getTable())
      {
         LinkedHashMap<String, Object> map = new LinkedHashMap<>();
         for (String key : this.getColumnMap().keySet())
         {
            map.put(key, row.get(this.getColumnMap().get(key)));
         }
         Object result = function.apply(map);
         row.add(result);
      }
      this.getColumnMap().put(columnName, newColumnNumber);
   }

   public ObjectTable dropColumns(String... columnNames)
   {
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.getColumnMap());
      this.getColumnMap().clear();

      LinkedHashSet<String> dropNames = new LinkedHashSet<>(Arrays.asList(columnNames));
      int i = 0;
      for (String name : oldColumnMap.keySet())
      {
         if (!dropNames.contains(name))
         {
            this.getColumnMap().put(name, i);
            i++;
         }
      }

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();

      LinkedHashSet<ArrayList<Object>> rowSet = new LinkedHashSet<>();
      for (List<Object> row : oldTable)
      {
         ArrayList<Object> newRow = new ArrayList<>();
         for (String name : this.getColumnMap().keySet())
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
            this.getTable().add(newRow);
      }

      return this;
   }

   public ObjectTable selectColumns(String... columnNames)
   {
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.getColumnMap());
      this.getColumnMap().clear();

      int i = 0;
      for (String name : columnNames)
      {
         if (oldColumnMap.get(name) == null)
            throw new IllegalArgumentException("unknown column name: " + name);
         this.getColumnMap().put(name, i);
         i++;
      }

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();

      LinkedHashSet<ArrayList<Object>> rowSet = new LinkedHashSet<>();
      for (List<Object> row : oldTable)
      {
         ArrayList<Object> newRow = new ArrayList<>();
         for (String name : columnNames)
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
            this.getTable().add(newRow);
      }

      return this;
   }

   public ObjectTable filter(Predicate<Object> predicate)
   {
      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(this.getColumn());
         if (predicate.test(start))
         {
            this.getTable().add(row);
         }
      }
      return this;
   }

   public ObjectTable filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();
      for (List<Object> row : oldTable)
      {
         LinkedHashMap<String, Object> map = new LinkedHashMap<>();
         for (String key : this.getColumnMap().keySet())
         {
            map.put(key, row.get(this.getColumnMap().get(key)));
         }
         if (predicate.test(map))
         {
            this.getTable().add(row);
         }
      }
      return this;
   }

   @Override
   public LinkedHashSet<Object> toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }

   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder("| ");
      for (String key : this.getColumnMap().keySet())
      {
         buf.append(key).append(" \t| ");
      }
      buf.append("\n| ");
      for (String ignored : this.getColumnMap().keySet())
      {
         buf.append(" --- \t| ");
      }
      buf.append("\n");
      for (List<Object> row : this.getTable())
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
