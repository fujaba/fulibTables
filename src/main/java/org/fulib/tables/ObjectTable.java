package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;

public class ObjectTable<T> extends AbstractTable<T>
{
   private ReflectorMap reflectorMap;

   // =============== Constructors ===============

   @SafeVarargs
   public ObjectTable(T... start)
   {
      super(start);
      this.initReflector(start);
   }

   @SafeVarargs
   public ObjectTable(String colName, T... start)
   {
      super(colName, start);
      this.initReflector(start);
   }

   public ObjectTable(String columnName, AbstractTable<?> base)
   {
      super(columnName, base);
   }

   // TODO consider the packages of start[1..] too?
   @SafeVarargs
   private final void initReflector(T... start)
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

   // TODO overload where rowName is a String
   public ObjectTable<T> hasLink(String linkName, ObjectTable<?> rowName)
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

   // --------------- Expansion ---------------

   public <U> ObjectTable<U> expandLink(String newColumnName, String linkName)
   {
      ObjectTable<U> result = new ObjectTable<>(newColumnName, this);
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
