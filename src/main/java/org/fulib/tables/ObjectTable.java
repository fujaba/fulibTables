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

   ObjectTable(Table<?> base)
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

   private void checkSameData(ObjectTable<?> otherTable)
   {
      if (this.table != otherTable.table)
      {
         throw new IllegalArgumentException("other table does not share the same underlying data");
      }
   }

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
    * @throws IllegalArgumentException
    *    if the other table does not share the same underlying data structure.
    * @see #filterRows(Predicate)
    */
   public ObjectTable<T> hasLink(String linkName, ObjectTable<?> otherTable)
   {
      this.checkSameData(otherTable);
      return this.hasLink(this.getColumnName(), otherTable.getColumnName(), linkName);
   }

   /**
    * Removes all rows where the cell value in the source column does not have the named link to the cell value in the
    * target column.
    * You can also pass the same column name twice to check for self-associations.
    * <p>
    * Essentially equivalent to:
    *
    * <pre>{@code
    *    this.filterRows(row -> {
    *       Object source = row.get(sourceColumn);
    *       Object target = row.get(targetColumn);
    *       Object linkValue = source.get<linkName>(); // via reflection
    *       return linkValue == target || linkValue instanceof Collection && ((Collection) linkValue).contains(other);
    *    });
    * }</pre>
    *
    * @param sourceColumn
    *    the column with source objects
    * @param targetColumn
    *    the column with target objects
    * @param linkName
    *    the name of the property on this table's objects
    *
    * @return this instance, to allow method chaining
    *
    * @see #filterRows(Predicate)
    * @since 1.4
    */
   public ObjectTable<T> hasLink(String sourceColumn, String targetColumn, String linkName)
   {
      final int thisColumn = this.getColumnIndex(sourceColumn);
      final int otherColumn = this.getColumnIndex(targetColumn);
      this.table.removeIf(row -> {
         final Object source = row.get(thisColumn);
         final Object target = row.get(otherColumn);

         if (!this.reflectorMap.canReflect(source))
         {
            return true;
         }

         final Reflector reflector = this.reflectorMap.getReflector(source);
         final Object linkValue = reflector.getValue(source, linkName);
         final boolean keep =
            linkValue == target || linkValue instanceof Collection && ((Collection<?>) linkValue).contains(target);
         return !keep;
      });
      return this;
   }

   /**
    * Removes all rows where the cell value does not have some link to the cell value of the other table in the
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
    *       for (String linkName : <properties of source>) {
    *          Object linkValue = source.get<linkName>(); // via reflection
    *          if (linkValue == target || linkValue instanceof Collection && ((Collection) linkValue).contains(other)) {
    *             return true;
    *          }
    *       }
    *       return false;
    *    });
    * }</pre>
    *
    * @param otherTable
    *    the table pointing to the column with link targets
    *
    * @return this instance, to allow method chaining
    *
    * @throws IllegalArgumentException
    *    if the other table does not share the same underlying data structure.
    * @see #hasLink(String, ObjectTable)
    * @since 1.3
    */
   public ObjectTable<T> hasAnyLink(ObjectTable<?> otherTable)
   {
      this.checkSameData(otherTable);
      return this.hasAnyLink(this.getColumnName(), otherTable.getColumnName());
   }

   /**
    * Removes all rows where the cell value in the source column does not have some link to the cell value in the target
    * column.
    * You can also the same column name twice to check for self-associations.
    * <p>
    * Essentially equivalent to:
    *
    * <pre>{@code
    *    this.filterRows(row -> {
    *       Object source = row.get(sourceColumn);
    *       Object target = row.get(targetColumn);
    *       for (String linkName : <properties of source>) {
    *          Object linkValue = source.get<linkName>(); // via reflection
    *          if (linkValue == target || linkValue instanceof Collection && ((Collection) linkValue).contains(other)) {
    *             return true;
    *          }
    *       }
    *       return false;
    *    });
    * }</pre>
    *
    * @param sourceColumn
    *    the column with source objects
    * @param targetColumn
    *    the column with target objects
    *
    * @return this instance, to allow method chaining
    *
    * @see #filterRows(Predicate)
    * @see #hasLink(String, String, String)
    * @since 1.4
    */
   public ObjectTable<T> hasAnyLink(String sourceColumn, String targetColumn)
   {
      final int thisColumn = this.getColumnIndex(sourceColumn);
      final int otherColumn = this.getColumnIndex(targetColumn);
      this.table.removeIf(row -> {
         Object start = row.get(thisColumn);
         Object other = row.get(otherColumn);

         if (!this.reflectorMap.canReflect(start))
         {
            return true;
         }

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

   private <U> ObjectTable<U> view(String targetColumn)
   {
      ObjectTable<U> result = new ObjectTable<>(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Creates a new column by expanding the given link from the cells of the column this table points to.
    * Links may be simple objects or collections, the latter of which will be flattened.
    * Links that are {@code null} do not create a row.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expandAll(targetColumn, source -> {
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
    * @param targetColumn
    *    the name of the new column
    * @param linkName
    *    the name of the property to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAll(String, Function)
    */
   public <U> ObjectTable<U> expandLink(String targetColumn, String linkName)
   {
      return this.expandLink(this.getColumnName(), targetColumn, linkName);
   }

   /**
    * Creates a new column by expanding the given link from the cells in the source column.
    * Links may be simple objects or collections, the latter of which will be flattened.
    * Links that are {@code null} do not create a row.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expandAll(sourceColumn, targetColumn, source -> {
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
    * @param sourceColumn
    *    the name of the column to operate on
    * @param targetColumn
    *    the name of the new column
    * @param linkName
    *    the name of the property to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAll(String, String, Function)
    * @since 1.4
    */
   @SuppressWarnings("unchecked")
   public <U> ObjectTable<U> expandLink(String sourceColumn, String targetColumn, String linkName)
   {
      return this.expandAll(sourceColumn, targetColumn, start -> {
         if (!this.reflectorMap.canReflect(start))
         {
            return Collections.emptySet();
         }

         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection)
         {
            return (Collection<U>) value;
         }
         else if (value != null)
         {
            return Collections.singleton((U) value);
         }
         else
         {
            return Collections.emptySet();
         }
      });
   }

   /**
    * Creates a new column by expanding the all links and attributes from the cells of the column this table points to.
    * Links and attributes may be simple objects or collections, the latter of which will be flattened.
    * Links and attributes that are {@code null} do not create a row.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expandAll(targetColumn, source -> {
    *       List<Object> result = new ArrayList<>();
    *       for (String linkName : <properties of source>) {
    *          final Object target = source.get<linkName>(); // via reflection
    *          if (target instanceof Collection) {
    *             result.addAll((Collection<?>) target);
    *          }
    *          else if (target != null) {
    *             result.add(target);
    *          }
    *       }
    *       return result;
    *    }
    * }</pre>
    *
    * @param <U>
    *    the type of the target objects
    * @param targetColumn
    *    the name of the new column
    *
    * @return a table pointing to the new column
    *
    * @see #expandLink(String, String)
    * @since 1.3
    */
   public <U> ObjectTable<U> expandAll(String targetColumn)
   {
      return this.expandAll(this.getColumnName(), targetColumn);
   }

   /**
    * Creates a new column by expanding the all links and attributes from the cells in the source column.
    * Links and attributes may be simple objects or collections, the latter of which will be flattened.
    * Links and attributes that are {@code null} do not create a row.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expandAll(sourceColumn, targetColumn, source -> {
    *       List<Object> result = new ArrayList<>();
    *       for (String linkName : <properties of source>) {
    *          final Object target = source.get<linkName>(); // via reflection
    *          if (target instanceof Collection) {
    *             result.addAll((Collection<?>) target);
    *          }
    *          else if (target != null) {
    *             result.add(target);
    *          }
    *       }
    *       return result;
    *    }
    * }</pre>
    *
    * @param <U>
    *    the type of the target objects
    * @param sourceColumn
    *    the name of the column to operate on
    * @param targetColumn
    *    the name of the new column
    *
    * @return a table pointing to the new column
    *
    * @see #expandAll(String, String, Function)
    * @since 1.4
    */
   @SuppressWarnings("unchecked")
   public <U> ObjectTable<U> expandAll(String sourceColumn, String targetColumn)
   {
      return this.expandAll(sourceColumn, targetColumn, start -> {
         if (!this.reflectorMap.canReflect(start))
         {
            return Collections.emptyList();
         }

         final Reflector reflector = this.reflectorMap.getReflector(start);
         final Collection<U> result = new ArrayList<>();

         for (final String propertyName : reflector.getAllProperties())
         {
            Object value = reflector.getValue(start, propertyName);
            if (value instanceof Collection)
            {
               result.addAll((Collection<U>) value);
            }
            else if (value != null)
            {
               result.add((U) value);
            }
         }

         return result;
      });
   }

   /**
    * Creates a new column by expanding the given attribute from cells of the column this table points to.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expand(targetColumn, start -> {
    *       return start.get<attrName>(); // via reflection
    *    });
    * }</pre>
    *
    * @param <U>
    *    the type of the attribute
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expand(String, Function)
    * @since 1.2
    */
   public <U> Table<U> expandAttribute(String targetColumn, String attrName)
   {
      return this.expandAttribute(this.getColumnName(), targetColumn, attrName);
   }

   /**
    * Creates a new column by expanding the given attribute from cells in the source column.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expand(sourceColumn, targetColumn, start -> {
    *       return start.get<attrName>(); // via reflection
    *    });
    * }</pre>
    *
    * @param <U>
    *    the type of the attribute
    * @param sourceColumn
    *    the name of the column to operate on
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expand(String, String, Function)
    * @since 1.4
    */
   public <U> Table<U> expandAttribute(String sourceColumn, String targetColumn, String attrName)
   {
      this.expandAttributeImpl(sourceColumn, targetColumn, attrName);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(targetColumn, attrName).as(doubleTable.class);
    * }</pre>
    *
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public doubleTable expandDouble(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(targetColumn, attrName);
      doubleTable result = new doubleTable(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(targetColumn, attrName).as(floatTable.class);
    * }</pre>
    *
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public floatTable expandFloat(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(targetColumn, attrName);
      floatTable result = new floatTable(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(targetColumn, attrName).as(intTable.class);
    * }</pre>
    *
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public intTable expandInt(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(targetColumn, attrName);
      intTable result = new intTable(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(targetColumn, attrName).as(longTable.class);
    * }</pre>
    *
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public longTable expandLong(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(targetColumn, attrName);
      longTable result = new longTable(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(targetColumn, attrName).as(StringTable.class);
    * }</pre>
    *
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public StringTable expandString(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(targetColumn, attrName);
      StringTable result = new StringTable(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   /**
    * Equivalent to:
    * <pre>{@code
    *    this.expandAttribute(targetColumn, attrName).as(BooleanTable.class);
    * }</pre>
    *
    * @param targetColumn
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expandAttribute(String, String)
    */
   public BooleanTable expandBoolean(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(targetColumn, attrName);
      BooleanTable result = new BooleanTable(this);
      result.setColumnName_(targetColumn);
      return result;
   }

   private void expandAttributeImpl(String targetColumn, String attrName)
   {
      this.expandAttributeImpl(this.getColumnName(), targetColumn, attrName);
   }

   private void expandAttributeImpl(String sourceColumn, String targetColumn, String attrName)
   {
      this.expandImpl(sourceColumn, targetColumn, start -> {
         if (!this.reflectorMap.canReflect(start))
         {
            return null;
         }

         Reflector reflector = this.reflectorMap.getReflector(start);
         return reflector.getValue(start, attrName);
      });
   }

   // =============== Compatibility Methods ===============

   // --------------- Overriding Return Type as ObjectTable ---------------

   @Override
   public <U> ObjectTable<U> expand(String targetColumn, Function<? super T, ? extends U> function)
   {
      return this.expand(this.getColumnName(), targetColumn, function);
   }

   @Override
   public <V, U> ObjectTable<U> expand(String sourceColumn, String targetColumn,
      Function<? super V, ? extends U> function)
   {
      this.expandImpl(sourceColumn, targetColumn, function);
      return this.view(targetColumn);
   }

   @Override
   public <U> ObjectTable<U> expandAll(String targetColumn,
      Function<? super T, ? extends Collection<? extends U>> function)
   {
      return this.expandAll(this.getColumnName(), targetColumn, function);
   }

   @Override
   public <V, U> ObjectTable<U> expandAll(String sourceColumn, String targetColumn,
      Function<? super V, ? extends Collection<? extends U>> function)
   {
      this.expandAllImpl(sourceColumn, targetColumn, function);
      return this.view(targetColumn);
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

   @Override
   public <V> ObjectTable<T> filter(String sourceColumn, Predicate<? super V> predicate)
   {
      super.filter(sourceColumn, predicate);
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
      this.setColumnMap_(columnMap);
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
