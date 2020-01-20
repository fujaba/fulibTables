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

   public ObjectTable hasLink(String linkName, ObjectTable rowName)
   {
      this.getTable().removeIf(row -> {
         Object start = row.get(this.getColumn());
         Object other = row.get(this.getColumnMap().get(rowName.getColumnName()));
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);
         boolean keep = value == other || value instanceof Collection && ((Collection<?>) value).contains(other);
         return !keep;
      });
      return this;
   }

   // --------------- Expansion ---------------

   public ObjectTable expandLink(String newColumnName, String linkName)
   {
      ObjectTable result = new ObjectTable(newColumnName, this);
      result.setReflectorMap(this.reflectorMap);

      this.addColumn(newColumnName);

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

   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable(newColumnName, this);

      expandPrimitive(newColumnName, attrName);

      return result;
   }

   public floatTable expandFloat(String newColumnName, String attrName)
   {
      floatTable result = new floatTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
      return result;
   }

   public intTable expandInt(String newColumnName, String attrName)
   {
      intTable result = new intTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
      return result;
   }

   public longTable expandLong(String newColumnName, String attrName)
   {
      longTable result = new longTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
      return result;
   }

   public StringTable expandString(String newColumnName, String attrName)
   {
      StringTable result = new StringTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
      return result;
   }

   public StringTable expandBoolean(String newColumnName, String attrName)
   {
      StringTable result = new StringTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
      return result;
   }

   private void expandPrimitive(String newColumnName, String attrName)
   {
      this.addColumn(newColumnName);

      this.getTable().replaceAll(row -> {
         Object start = row.get(this.getColumn());
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         List<Object> newRow = new ArrayList<>(row);
         newRow.add(value);
         return newRow;
      });
   }

   // --------------- Columns ---------------

   // TODO deprecate and add addColumn???(String columnName, Function<? super Map<String, Object>, ?> function)
   public void addColumn(String columnName, Function<LinkedHashMap<String, Object>, Object> function)
   {
      this.addColumnImpl(columnName, function);
   }

   private void addColumnImpl(String columnName, Function<? super LinkedHashMap<String, Object>, ?> function)
   {
      int newColumnNumber = this.getNewColumnNumber();
      for (List<Object> row : this.getTable())
      {
         LinkedHashMap<String, Object> map = this.convertRowToMap(row);
         Object result = function.apply(map);
         row.add(result);
      }
      this.getColumnMap().put(columnName, newColumnNumber);
   }

   public ObjectTable dropColumns(String... columnNames)
   {
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.getColumnMap());
      this.getColumnMap().clear();

      Set<String> dropNames = new HashSet<>(Arrays.asList(columnNames));
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

      Set<List<Object>> rowSet = new HashSet<>();
      for (List<Object> row : oldTable)
      {
         List<Object> newRow = new ArrayList<>();
         for (String name : this.getColumnMap().keySet())
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
         {
            this.getTable().add(newRow);
         }
      }

      return this;
   }

   public ObjectTable selectColumns(String... columnNames)
   {
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.getColumnMap());
      this.getColumnMap().clear();

      for (int i = 0; i < columnNames.length; i++)
      {
         String name = columnNames[i];
         if (oldColumnMap.get(name) == null)
         {
            throw new IllegalArgumentException("unknown column name: " + name);
         }
         this.getColumnMap().put(name, i);
      }

      List<List<Object>> oldTable = new ArrayList<>(this.getTable());
      this.getTable().clear();

      Set<List<Object>> rowSet = new HashSet<>();
      for (List<Object> row : oldTable)
      {
         List<Object> newRow = new ArrayList<>();
         for (String name : columnNames)
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
         {
            this.getTable().add(newRow);
         }
      }

      return this;
   }

   // --------------- Filter ---------------

   public ObjectTable filter(Predicate<? super Object> predicate)
   {
      int column = this.getColumn();
      this.getTable().removeIf(row -> !predicate.test(row.get(column)));
      return this;
   }

   /**
    * @since 1.2
    */
   public ObjectTable filterRows(Predicate<? super Map<String, Object>> predicate)
   {
      return this.filterRowsImpl(predicate);
   }

   /**
    * @deprecated since 1.2; use {@link #filterRows(Predicate)} instead
    */
   @Deprecated
   public ObjectTable filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      return this.filterRowsImpl(predicate);
   }

   private ObjectTable filterRowsImpl(Predicate<? super LinkedHashMap<String, Object>> predicate)
   {
      this.getTable().removeIf(row -> !predicate.test(this.convertRowToMap(row)));
      return this;
   }

   private LinkedHashMap<String, Object> convertRowToMap(List<Object> row)
   {
      LinkedHashMap<String, Object> map = new LinkedHashMap<>();
      for (Map.Entry<String, Integer> entry : this.getColumnMap().entrySet())
      {
         map.put(entry.getKey(), row.get(entry.getValue()));
      }
      return map;
   }

   // --------------- Misc. Conversions ---------------

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
