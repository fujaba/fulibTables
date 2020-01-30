package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TODO ObjectTable<T> ?
public class ObjectTable extends Table<Object>
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

   protected ObjectTable(Table<?> base)
   {
      super(base);
   }

   // TODO consider the packages of start[1..] too?
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

   public ObjectTable hasLink(String linkName, ObjectTable otherTable)
   {
      final int thisColumn = this.getColumn();
      final int otherColumn = this.columnMap.get(otherTable.getColumnName());
      this.table.removeIf(row -> {
         Object start = row.get(thisColumn);
         Object other = row.get(otherColumn);
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
      ObjectTable result = new ObjectTable(this);
      result.setReflectorMap(this.reflectorMap);

      result.setColumnName(newColumnName);
      this.addColumn(newColumnName);

      final int column = this.getColumn();

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(column);
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection)
         {
            for (Object current : (Collection<?>) value)
            {
               List<Object> newRow = new ArrayList<>(row);
               newRow.add(current);
               this.table.add(newRow);
            }
         }
         else if (value != null)
         {
            List<Object> newRow = new ArrayList<>(row);
            newRow.add(value);
            this.table.add(newRow);
         }
      }
      return result;
   }

   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable(this);
      this.expandPrimitive(result, newColumnName, attrName);
      return result;
   }

   public floatTable expandFloat(String newColumnName, String attrName)
   {
      floatTable result = new floatTable(this);
      this.expandPrimitive(result, newColumnName, attrName);
      return result;
   }

   public intTable expandInt(String newColumnName, String attrName)
   {
      intTable result = new intTable(this);
      this.expandPrimitive(result, newColumnName, attrName);
      return result;
   }

   public longTable expandLong(String newColumnName, String attrName)
   {
      longTable result = new longTable(this);
      this.expandPrimitive(result, newColumnName, attrName);
      return result;
   }

   public StringTable expandString(String newColumnName, String attrName)
   {
      StringTable result = new StringTable(this);
      this.expandPrimitive(result, newColumnName, attrName);
      return result;
   }

   public BooleanTable expandBoolean(String newColumnName, String attrName)
   {
      BooleanTable result = new BooleanTable(this);
      this.expandPrimitive(result, newColumnName, attrName);
      return result;
   }

   private void expandPrimitive(Table<?> result, String newColumnName, String attrName)
   {
      result.setColumnName_(newColumnName);
      this.addColumn(newColumnName);

      final int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         Object start = row.get(column);
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         row.add(value);
      }
   }

   // =============== Deprecated and Compatibility Methods ===============

   /**
    * @deprecated since 1.2; set via the constructor and not meant to be changed afterward
    */
   @Deprecated
   public ObjectTable setColumnName(String columnName)
   {
      this.setColumnName_(columnName);
      return this;
   }

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ObjectTable setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
      return this;
   }

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ObjectTable setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
      return this;
   }

   @Override
   public ObjectTable selectColumns(String... columnNames)
   {
      super.selectColumns(columnNames);
      return this;
   }

   @Override
   public ObjectTable dropColumns(String... columnNames)
   {
      super.dropColumns(columnNames);
      return this;
   }

   @Override
   public ObjectTable filter(Predicate<? super Object> predicate)
   {
      super.filter(predicate);
      return this;
   }

   @Override
   @Deprecated
   public ObjectTable filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      super.filterRow(predicate);
      return this;
   }

   @Override
   public LinkedHashSet<Object> toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }
}
