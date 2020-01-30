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

   /**
    * @since 1.2
    */
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

         this.addToRow(row, value);
      }
      return result;
   }

   /**
    * @since 1.2
    */
   public ObjectTable expandAll(String newColumnName)
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

   /**
    * Functionally equivalent to {@link #multiply(String, Collection)}, with the difference that it takes a varargs
    * array instead of a collection as the items.
    *
    * @param newColumnName
    *    see {@link #multiply(String, Collection)}
    * @param items
    *    see {@link #multiply(String, Collection)}
    *
    * @return see {@link #multiply(String, Collection)}
    *
    * @see #multiply(String, Collection)
    * @since 1.2
    */
   public ObjectTable multiply(String newColumnName, Object... items)
   {
      return this.multiply(newColumnName, Arrays.asList(items));
   }

   /**
    * Performs a cartesian product of all rows of this table with the given items.
    * <p>
    * For example, if this table is:
    *
    * <table>
    *    <tr>
    *       <th>A</th>
    *       <th>B</th>
    *    </tr>
    *    <tr>
    *       <td>A1</td>
    *       <td>B1</td>
    *    </tr>
    *    <tr>
    *       <td>A2</td>
    *       <td>B2</td>
    *    </tr>
    * </table>
    *
    * <p>
    * and the new items and column name are:
    *
    * <table>
    *    <tr>
    *       <th>C</th>
    *    </tr>
    *    <tr>
    *       <td>C1</td>
    *    </tr>
    *    <tr>
    *       <td>C2</td>
    *    </tr>
    * </table>
    *
    * <p>
    * the resulting table is:
    *
    * <table>
    *    <tr>
    *       <th>A</th>
    *       <th>B</th>
    *       <th>C</th>
    *    </tr>
    *    <tr>
    *       <td>A1</td>
    *       <td>B1</td>
    *       <td>C1</td>
    *    </tr>
    *    <tr>
    *       <td>A1</td>
    *       <td>B1</td>
    *       <td>C2</td>
    *    </tr>
    *    <tr>
    *       <td>A2</td>
    *       <td>B2</td>
    *       <td>C1</td>
    *    </tr>
    *    <tr>
    *       <td>A2</td>
    *       <td>B2</td>
    *       <td>C2</td>
    *    </tr>
    * </table>
    *
    * <p>
    * As a result of this, if this table or items is empty, the resulting table will be empty too.
    *
    * @param newColumnName
    *    the name of the new column
    * @param items
    *    the items which will become the cells of the new column
    *
    * @return a new table pointing to the new column
    *
    * @since 1.2
    */
   // TODO name, could also be crossProduct, crossMultiply, cartesianProduct
   public ObjectTable multiply(String newColumnName, Collection<?> items)
   {
      final ObjectTable newTable = new ObjectTable(this);
      newTable.setReflectorMap(this.reflectorMap);

      newTable.setColumnName(newColumnName);
      this.addColumn(newColumnName);

      final List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         for (final Object item : items)
         {
            final List<Object> newRow = new ArrayList<>(row);
            newRow.add(item);
            this.table.add(newRow);
         }
      }

      return newTable;
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
