package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@link Table} specialized for model objects.
 * Using reflection, it provides more functionality for expanding and filtering based on attributes and associations.
 *
 * @param <T>
 *    the type of values contained in the column this table points to
 */
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
      if (base instanceof ObjectTable)
      {
         this.reflectorMap = ((ObjectTable<?>) base).getReflectorMap();
      }
   }

   @SafeVarargs
   private final void initReflector(T... start)
   {
      if (start.length == 0)
      {
         return;
      }

      final Set<String> packageNames = Arrays
         .stream(start)
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

   /**
    * Removes all rows where the cell value does not have the named link to the cell value of the other table in the
    * same row.
    * This requires this and the other table to share the same underlying data structure.
    * You can also pass this table as {@code otherTable} to check for self-associations.
    * <p>
    * Essentially equivalent to:
    *
    * <pre>{@code
    *    this.filterRows(row -> {
    *       Object source = row.get(this.getColumn());
    *       Object target = row.get(otherTable.getColumn());
    *       Object linkValue = source.get<linkName>(); // via reflection
    *       return linkValue == target || linkValue instanceof Collection && ((Collection) linkValue).contains(other);
    *    });
    * }</pre>
    *
    * @param linkName
    *    the name of the property on this table's objects
    * @param otherTable
    *    the table pointing to the column with link targets
    *
    * @return this instance, to allow method chaining
    *
    * @see #filterRows(Predicate)
    */
   public ObjectTable<T> hasLink(String linkName, ObjectTable<?> otherTable)
   {
      final int thisColumn = this.getColumnIndex();
      final int otherColumn = this.columnMap.get(otherTable.getColumnName());
      this.table.removeIf(row -> {
         final Object source = row.get(thisColumn);
         final Object target = row.get(otherColumn);
         final Reflector reflector = this.reflectorMap.getReflector(source);
         final Object linkValue = reflector.getValue(source, linkName);
         final boolean keep =
            linkValue == target || linkValue instanceof Collection && ((Collection<?>) linkValue).contains(target);
         return !keep;
      });
      return this;
   }

   // --------------- Expansion ---------------

   /**
    * Creates a new column by expanding the given link from the cells of the column this table points to.
    * Links may be simple objects or collections, the latter of which will be flattened.
    * Links that are {@code null} do not create a row.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expandAll(newColumnName, source -> {
    *       final Object target = source.get<linkName>(); // via reflection
    *       if (target instanceof Collection) {
    *          return target;
    *       }
    *       else if (target == null) {
    *          return Collections.emptyList();
    *       }
    *       else {
    *          return Collections.singleton(target);
    *       }
    *    }
    * }</pre>
    *
    * @param <U>
    *    the type of the target objects
    * @param newColumnName
    *    the name of the new column
    * @param linkName
    *    the name of the property to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAll(String, Function)
    */
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
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Creates a new column by expanding the given attribute from cells of the column this table points to.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expand(newColumnName, start -> {
    *       return start.get<attrName>(); // via reflection
    *    });
    * }</pre>
    *
    * @param <U>
    *    the type of the attribute
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expand(String, Function)
    * @since 1.2
    */
   public <U> Table<U> expandAttribute(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(newColumnName, attrName).as(doubleTable.class);
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      doubleTable result = new doubleTable(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(newColumnName, attrName).as(floatTable.class);
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public floatTable expandFloat(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      floatTable result = new floatTable(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(newColumnName, attrName).as(intTable.class);
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public intTable expandInt(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      intTable result = new intTable(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(newColumnName, attrName).as(longTable.class);
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public longTable expandLong(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      longTable result = new longTable(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(newColumnName, attrName).as(StringTable.class);
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public StringTable expandString(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      StringTable result = new StringTable(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(newColumnName, attrName).as(BooleanTable.class);
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public BooleanTable expandBoolean(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      BooleanTable result = new BooleanTable(this);
      result.setColumnName_(newColumnName);
      return result;
   }

   private void expandAttributeImpl(String newColumnName, String attrName)
   {
      this.expandImpl(newColumnName, start -> {
         Reflector reflector = this.reflectorMap.getReflector(start);
         return reflector.getValue(start, attrName);
      });
   }

   // =============== Compatibility Methods ===============

   // --------------- Overriding Return Type as ObjectTable ---------------

   @Override
   public <U> ObjectTable<U> expand(String columnName, Function<? super T, ? extends U> function)
   {
      this.expandImpl(columnName, function);
      final ObjectTable<U> result = new ObjectTable<>(this);
      result.setColumnName_(columnName);
      return result;
   }

   @Override
   public <U> ObjectTable<U> expandAll(String columnName,
      Function<? super T, ? extends Collection<? extends U>> function)
   {
      this.expandAllImpl(columnName, function);
      final ObjectTable<U> result = new ObjectTable<>(this);
      result.setColumnName_(columnName);
      return result;
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
   public ObjectTable<T> filter(Predicate<? super T> predicate)
   {
      super.filter(predicate);
      return this;
   }

   // --------------- Overriding Return Type (other) ---------------

   @Override
   public LinkedHashSet<T> toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }

   // =============== Deprecated Members ===============

   /**
    * @param columnName
    *    the name of the column this table should point to
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; set via the constructor and not meant to be changed afterward
    */
   @Deprecated
   public ObjectTable<T> setColumnName(String columnName)
   {
      this.setColumnName_(columnName);
      return this;
   }

   /**
    * @param table
    *    the list of rows
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ObjectTable<T> setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
      return this;
   }

   /**
    * @param columnMap
    *    the map from column name to index in the lists that make up rows
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ObjectTable<T> setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
      return this;
   }

   /**
    * Same as {@link #derive(String, Function)}, except it has stricter requirements on the parameter type of the
    * predicate and does not return a table pointing to the new column.
    *
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a value for the new column
    *
    * @see #derive(String, Function)
    * @deprecated since 1.2; use {@link #derive(String, Function)} instead
    */
   @Deprecated
   public void addColumn(String columnName, Function<LinkedHashMap<String, Object>, Object> function)
   {
      this.deriveImpl(columnName, function);
   }

   /**
    * Same as {@link #filterRows(Predicate)}, except it has stricter requirements on the parameter type of the
    * predicate.
    *
    * @param predicate
    *    the predicate that determines which rows should be kept
    *
    * @return this table, to allow method chaining
    *
    * @see #filterRows(Predicate)
    * @deprecated since 1.2; use {@link #filterRows(Predicate)} instead
    */
   @Deprecated
   public ObjectTable<T> filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      this.filterRowsImpl(predicate);
      return this;
   }
}
