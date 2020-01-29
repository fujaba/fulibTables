package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.stream.Collectors;

// TODO ObjectTable<T> ?
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

   public ObjectTable(String columnName, AbstractTable<?> base)
   {
      super(columnName, base);
   }

   private void initReflector(Object... start)
   {
      if (start.length == 0)
      {
         return;
      }

      final Set<String> packageNames = Arrays.stream(start)
                                             .map(Object::getClass)
                                             .map(Class::getPackage)
                                             .map(Package::getName)
                                             .collect(Collectors.toCollection(LinkedHashSet::new));
      this.reflectorMap = new ReflectorMap(packageNames);
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

   // TODO overload where rowName is a String
   public ObjectTable hasLink(String linkName, ObjectTable rowName)
   {
      final int thisColumn = this.getColumn();
      final int otherColumn = this.columnMap.get(rowName.getColumnName());
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

   public ObjectTable hasAnyLink(ObjectTable otherTable)
   {
      final int thisColumn = this.getColumn();
      final int otherColumn = this.columnMap.get(otherTable.getColumnName());
      this.table.removeIf(row -> {
         Object start = row.get(thisColumn);
         Object other = row.get(otherColumn);
         Reflector reflector = this.reflectorMap.getReflector(start);

         for (final String property : reflector.getAllProperties())
         {
            Object value = reflector.getValue(start, property);
            if (value == other || value instanceof Collection && ((Collection<?>) value).contains(other))
            {
               return false;
            }
         }

         return true;
      });
      return this;
   }

   // --------------- Expansion ---------------

   public ObjectTable expandLink(String newColumnName, String linkName)
   {
      ObjectTable result = new ObjectTable(newColumnName, this);
      result.setReflectorMap(this.reflectorMap);

      this.addColumn(newColumnName);

      final int column = this.getColumn();

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(column);
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         this.addToRow(row, value);
      }
      return result;
   }

   public ObjectTable expandAll(String newColumnName)
   {
      ObjectTable result = new ObjectTable(newColumnName, this);
      result.setReflectorMap(this.reflectorMap);

      this.addColumn(newColumnName);

      final int column = this.getColumn();

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         Object start = row.get(column);

         if (!this.reflectorMap.canReflect(start))
         {
            continue;
         }

         final Reflector reflector = this.reflectorMap.getReflector(start);

         for (final String propertyName : reflector.getAllProperties())
         {
            Object value = reflector.getValue(start, propertyName);

            this.addToRow(row, value);
         }
      }
      return result;
   }

   private void addToRow(List<Object> row, Object value)
   {
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

   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
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

   public BooleanTable expandBoolean(String newColumnName, String attrName)
   {
      BooleanTable result = new BooleanTable(newColumnName, this);
      this.expandPrimitive(newColumnName, attrName);
      return result;
   }

   private void expandPrimitive(String newColumnName, String attrName)
   {
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
}
