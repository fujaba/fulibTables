package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ObjectTable<T> extends Table<T>
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

   protected ObjectTable(Table<?> base)
   {
      super(base);
   }

   @SafeVarargs
   private final void initReflector(T... start)
   {
      if (start.length == 0)
      {
         return;
      }

      final Set<String> packageNames = Arrays.stream(start)
                                             .map(o -> o.getClass().getPackage().getName())
                                             .collect(Collectors.toSet());
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

   public ObjectTable<T> hasLink(String linkName, ObjectTable<?> otherTable)
   {
      final int thisColumn = this.getColumnIndex();
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

   public <U> ObjectTable<U> expandLink(String newColumnName, String linkName)
   {
      this.expandAllImpl(newColumnName, start -> {
         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection)
         {
            return (Collection<?>) value;
         }
         else if (value != null)
         {
            return Collections.singleton(value);
         }
         else
         {
            return Collections.emptySet();
         }
      });
      ObjectTable<U> result = new ObjectTable<>(this);
      result.setReflectorMap(this.reflectorMap);
      result.setColumnName_(newColumnName);
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
      this.expandImpl(newColumnName, start -> {
         Reflector reflector = this.reflectorMap.getReflector(start);
         return reflector.getValue(start, attrName);
      });
      result.setColumnName_(newColumnName);
   }

   // =============== Deprecated and Compatibility Methods ===============

   /**
    * @deprecated since 1.2; set via the constructor and not meant to be changed afterward
    */
   @Deprecated
   public ObjectTable<T> setColumnName(String columnName)
   {
      this.setColumnName_(columnName);
      return this;
   }

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ObjectTable<T> setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
      return this;
   }

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ObjectTable<T> setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
      return this;
   }

   @Override
   public ObjectTable<T> selectColumns(String... columnNames)
   {
      super.selectColumns(columnNames);
      return this;
   }

   @Override
   public ObjectTable<T> dropColumns(String... columnNames)
   {
      super.dropColumns(columnNames);
      return this;
   }

   @Override
   public ObjectTable<T> filter(Predicate<? super Object> predicate)
   {
      super.filter(predicate);
      return this;
   }

   @Override
   @Deprecated
   public ObjectTable<T> filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      super.filterRow(predicate);
      return this;
   }

   @Override
   public LinkedHashSet<T> toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }
}
