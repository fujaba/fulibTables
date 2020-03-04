package org.fulib.tables;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a mechanism for managing object structures in a row and column format.
 * This is not to be viewed as a way to store arbitrary objects in a matrix of sorts;
 * the operations of this class are specific to object structures.
 * It is intentionally not possible to access specific cells with given row and column.
 * <p>
 * Each table instance maintains a column name, called the column pointer, and an underlying data structure.
 * The underlying data is shared between table instances derived from one another.
 * As such, operations on one table usually cause changes in derived or original tables.
 * This can be prevented with the {@link #copy()} operation, which does a full copy of all data such that the copy
 * is no longer connected to the original.
 * <p>
 * Various subclasses exist for type-specific operations, e.g. primitive numbers ({@link intTable}, {@link longTable},
 * {@link floatTable}, {@link doubleTable}), other standard data types ({@link StringTable}, {@link BooleanTable}),
 * and model objects ({@link ObjectTable}).
 *
 * @param <T>
 *    the type of values contained in the column this table points to
 *
 * @since 1.2
 */
public class Table<T> implements Iterable<T>
{
   // =============== Constants ===============

   private static final String DEFAULT_COLUMN_NAME = "A";

   private static final Renderer TOSTRING_RENDERER = new MarkdownRenderer();

   // =============== Fields ===============

   private String columnName;
   List<List<Object>> table;
   List<String> columns;

   // =============== Constructors ===============

   /**
    * Creates a new empty table with zero rows and zero columns.
    */
   public Table()
   {
      this.table = new ArrayList<>();
      this.columns = new ArrayList<>();
   }

   /**
    * Creates a new empty table with zero rows and one column with the given name.
    *
    * @param columnName
    *    the column name
    */
   public Table(String columnName)
   {
      this();
      this.columnName = columnName;
      this.columns.add(columnName);
   }

   /**
    * Creates a new table with the given objects in the first column, which uses the default name "A".
    *
    * @param start
    *    the objects in the first column
    */
   @SafeVarargs
   public Table(T... start)
   {
      this(DEFAULT_COLUMN_NAME, start);
   }

   /**
    * Creates a new table with the given objects in the first column.
    *
    * @param columnName
    *    the name of the first column
    * @param start
    *    the objects in the first column
    */
   @SafeVarargs
   public Table(String columnName, T... start)
   {
      this(columnName);
      for (T current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }

   Table(Table<?> base)
   {
      this.columnName = base.columnName;
      this.columns = base.columns;
      this.table = base.table;
   }

   /**
    * Creates a copy of this table. Modifications of any kind, to the original or copied table, are not reflected in the
    * other instance.
    *
    * @return a copy of this table
    */
   public Table<T> copy()
   {
      final Table<T> result = new Table<>();
      this.copyTo(result);
      return result;
   }

   void copyTo(Table<T> copy)
   {
      copy.columnName = this.columnName;
      copy.columns.addAll(this.columns);
      for (List<Object> row : this.table)
      {
         copy.table.add(new ArrayList<>(row));
      }
   }

   // =============== Properties ===============

   /**
    * @return the name of the column this table points to
    */
   public String getColumnName()
   {
      return this.columnName;
   }

   // TODO rename to setColumnName in v2.0
   // trailing _ added because subclasses use incompatible signatures for legacy reasons:
   //   void setColumnName(String) in PrimitiveTable classes
   //   ObjectTable setColumnName(String) in ObjectTable
   void setColumnName_(String columnName)
   {
      this.columnName = columnName;
   }

   int getColumnIndex()
   {
      final int index = this.columns.indexOf(this.columnName);
      if (index < 0)
      {
         throw new IllegalStateException(
            "Column '" + this.columnName + "' is no longer part of table columns " + this.columns + ". "
            + "It was likely evicted after a selectColumns or dropColumns operation. "
            + "This Table instance is no longer valid");
      }
      return index;
   }

   // =============== Methods ===============

   void addColumn(String columnName)
   {
      this.columns.add(columnName);
   }

   /**
    * Coerces this table to the given type by creating a new instance of that type that points to the same underlying
    * data structure and column.
    * <p>
    * The type must be either one of the {@link Table} subclasses in the {@link org.fulib.tables} package,
    * or provide a public constructor with one parameter of type {@link Table}.
    *
    * @param <TAB>
    *    the result type
    * @param type
    *    the {@link Class} corresponding to {@code <TAB>}.
    *
    * @return a table of the given type pointing to the same underlying data and column as this one
    *
    * @since 1.2
    */
   public <TAB extends Table<T>> TAB as(Class<? extends TAB> type)
   {
      try
      {
         return type.getDeclaredConstructor(Table.class).newInstance(this);
      }
      catch (InstantiationException | NoSuchMethodException | IllegalAccessException e)
      {
         throw new RuntimeException(type + " does not provide a constructor '" + type.getCanonicalName() + "(Table)'",
                                    e.getCause());
      }
      catch (InvocationTargetException e)
      {
         throw new RuntimeException(e.getTargetException());
      }
   }

   /**
    * Coerces this table to the given type by creating a new instance of that type that points to the same underlying
    * data structure and column.
    *
    * @param <TAB>
    *    the result type
    * @param constructor
    *    the constructor that accepts this table and produce a new table of the expected type.
    *
    * @return a table of the given type pointing to the same underlying data and column as this one
    *
    * @since 1.2
    */
   <TAB extends Table<T>> TAB as(Function<? super Table<T>, ? extends TAB> constructor)
   {
      return constructor.apply(this);
   }

   // --------------- Columns ---------------

   /**
    * Creates a new column with the given name by applying the given function to each object in the column pointed to by
    * this table.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.expand | javadoc -->
    * <pre>{@code
    * Table<Integer> a = new Table<>("A", 1, 2, 3);
    * Table<Integer> b = a.expand("B", i -> i * 2);
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.expand.b -->
    * <table>
    *     <caption>
    *         b
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>B</th>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>2</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>4</td>
    *     </tr>
    *     <tr>
    *         <td>3</td>
    *         <td>6</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    *
    * @param <U>
    *    the cell type of the new column
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a value for the new column
    *
    * @return a table pointing to the new column
    *
    * @since 1.2
    */
   public <U> Table<U> expand(String columnName, Function<? super T, ? extends U> function)
   {
      this.expandImpl(columnName, function);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(columnName);
      return result;
   }

   void expandImpl(String columnName, Function<? super T, ?> function)
   {
      final int column = this.getColumnIndex();
      this.addColumn(columnName);
      for (List<Object> row : this.table)
      {
         Object result = function.apply((T) row.get(column));
         row.add(result);
      }
   }

   /**
    * Creates a new column with the given name by applying the given function to each object in the column pointed to by
    * this table, and flattening the result.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.expandAll | javadoc -->
    * <pre>{@code
    * Table<Integer> a = new Table<>("A", 1, 2);
    * Table<Integer> b = a.expandAll("B", i -> Arrays.asList(i + 10, i + 20));
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.expandAll.b -->
    * <table>
    *     <caption>
    *         b
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>B</th>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>11</td>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>21</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>12</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>22</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    *
    * @param <U>
    *    the cell type of the new column
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a collection of values for the new column
    *
    * @return a table pointing to the new column
    *
    * @since 1.2
    */
   public <U> Table<U> expandAll(String columnName, Function<? super T, ? extends Collection<? extends U>> function)
   {
      this.expandAllImpl(columnName, function);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(columnName);
      return result;
   }

   void expandAllImpl(String columnName, Function<? super T, ? extends Collection<?>> function)
   {
      final int column = this.getColumnIndex();
      this.addColumn(columnName);

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         final Collection<?> newItems = function.apply((T) row.get(column));
         for (Object item : newItems)
         {
            final List<Object> newRow = new ArrayList<>(row);
            newRow.add(item);
            this.table.add(newRow);
         }
      }
   }

   /**
    * Creates a new column with the given name by applying the given function to each row.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.derive | javadoc -->
    * <pre>{@code
    * Table<Integer> a = new Table<>("A", 1, 2);
    * Table<Integer> b = a.expand("B", i -> i * 10);
    * Table<Integer> c = b.derive("C", row -> (int) row.get("A") + (int) row.get("B"));
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.derive.c -->
    * <table>
    *     <caption>
    *         c
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>B</th>
    *         <th>C</th>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>10</td>
    *         <td>11</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>20</td>
    *         <td>22</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    *
    * @param <U>
    *    the cell type of the new column
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a value for the new column
    *
    * @return a table pointing to the new column
    *
    * @since 1.2
    */
   public <U> Table<U> derive(String columnName, Function<? super Map<String, Object>, ? extends U> function)
   {
      this.deriveImpl(columnName, function);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(columnName);
      return result;
   }

   void deriveImpl(String columnName, Function<? super LinkedHashMap<String, Object>, ?> function)
   {
      for (List<Object> row : this.table)
      {
         LinkedHashMap<String, Object> map = this.convertRowToMap(row);
         Object result = function.apply(map);
         row.add(result);
      }
      this.addColumn(columnName);
   }

   /**
    * Creates a new column with the given name by applying the given function to each row, and flattening the result.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.deriveAll | javadoc -->
    * <pre>{@code
    * Table<Integer> a = new Table<>("A", 1, 2);
    * Table<Integer> b = a.expand("B", i -> i * 10);
    * Table<Integer> c = b.deriveAll("C", row -> {
    *    int a1 = (int) row.get("A");
    *    int b1 = (int) row.get("B");
    *    return Arrays.asList(a1 + b1, a1 * b1);
    * });
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.deriveAll.c -->
    * <table>
    *     <caption>
    *         c
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>B</th>
    *         <th>C</th>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>10</td>
    *         <td>11</td>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>10</td>
    *         <td>10</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>20</td>
    *         <td>22</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>20</td>
    *         <td>40</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    *
    * @param <U>
    *    the cell type of the new column
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a collection of values for the new column
    *
    * @return a table pointing to the new column
    *
    * @since 1.2
    */
   public <U> Table<U> deriveAll(String columnName,
      Function<? super Map<String, Object>, ? extends Collection<? extends U>> function)
   {
      this.deriveAllImpl(columnName, function);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(columnName);
      return result;
   }

   void deriveAllImpl(String columnName,
      Function<? super LinkedHashMap<String, Object>, ? extends Collection<?>> function)
   {
      final List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         final LinkedHashMap<String, Object> map = this.convertRowToMap(row);
         final Collection<?> result = function.apply(map);
         for (final Object item : result)
         {
            final List<Object> newRow = new ArrayList<>(row);
            newRow.add(item);
            this.table.add(newRow);
         }
      }
      this.addColumn(columnName);
   }

   /**
    * Removes all given columns from the underlying data structure and ensures no duplicate rows exist afterwards.
    * Given column names that don't exist are ignored.
    * <p>
    * After this operation, table instances with the same underlying data that pointed to one of these columns will
    * throw an exception when operated on in any way that accesses their corresponding column.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.dropColumns.initial | javadoc -->
    * <pre>{@code
    * Table<String> names = new StringTable("Alice", "Bob", "Charlie", "alice");
    * Table<String> uppercase = names.expand("uppercase", String::toUpperCase);
    * Table<String> lowercase = names.expand("lowercase", String::toLowerCase);
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.dropColumns.before -->
    * <table>
    *     <caption>
    *         before
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>uppercase</th>
    *         <th>lowercase</th>
    *     </tr>
    *     <tr>
    *         <td>Alice</td>
    *         <td>ALICE</td>
    *         <td>alice</td>
    *     </tr>
    *     <tr>
    *         <td>Bob</td>
    *         <td>BOB</td>
    *         <td>bob</td>
    *     </tr>
    *     <tr>
    *         <td>Charlie</td>
    *         <td>CHARLIE</td>
    *         <td>charlie</td>
    *     </tr>
    *     <tr>
    *         <td>alice</td>
    *         <td>ALICE</td>
    *         <td>alice</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.dropColumns.select | javadoc -->
    * <pre>{@code
    * names.dropColumns(names.getColumnName());
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.dropColumns.after -->
    * <table>
    *     <caption>
    *         after
    *     </caption>
    *     <tr>
    *         <th>uppercase</th>
    *         <th>lowercase</th>
    *     </tr>
    *     <tr>
    *         <td>ALICE</td>
    *         <td>alice</td>
    *     </tr>
    *     <tr>
    *         <td>BOB</td>
    *         <td>bob</td>
    *     </tr>
    *     <tr>
    *         <td>CHARLIE</td>
    *         <td>charlie</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.dropColumns.exception | javadoc -->
    * <pre>{@code
    * names.toList(); // throws IllegalStateException
    * }</pre>
    * <!-- end_code_fragment: -->
    *
    * @param columnNames
    *    the names of columns to drop
    *
    * @return this instance, to allow method chaining
    */
   public Table<T> dropColumns(String... columnNames)
   {
      for (final String name : columnNames)
      {
         this.dropColumnImpl(name);
      }

      this.removeDuplicateRows();

      return this;
   }

   private void dropColumnImpl(String columnName)
   {
      final int index = this.columns.indexOf(columnName);
      if (index < 0)
      {
         return;
      }

      this.columns.remove(index);
      for (final List<Object> row : this.table)
      {
         row.remove(index);
      }
   }

   private void removeDuplicateRows()
   {
      final Set<List<Object>> rowSet = new LinkedHashSet<>(this.table);
      this.table.clear();
      this.table.addAll(rowSet);
   }

   /**
    * Removes all columns from the underlying data structure except the give ones, and ensures no duplicate rows exist
    * afterwards. If one of the given column names is not part of this table, an {@link IllegalArgumentException} is
    * thrown and no changes are made.
    * <p>
    * After this operation, table instances with the same underlying data that pointed to a columns not part of the
    * given ones will throw an exception when operated on in any way that accesses their corresponding column.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.selectColumns.initial | javadoc -->
    * <pre>{@code
    * Table<String> names = new StringTable("Alice", "Bob", "Charlie", "alice");
    * Table<String> uppercase = names.expand("uppercase", String::toUpperCase);
    * Table<String> lowercase = names.expand("lowercase", String::toLowerCase);
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.selectColumns.before -->
    * <table>
    *     <caption>
    *         before
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>uppercase</th>
    *         <th>lowercase</th>
    *     </tr>
    *     <tr>
    *         <td>Alice</td>
    *         <td>ALICE</td>
    *         <td>alice</td>
    *     </tr>
    *     <tr>
    *         <td>Bob</td>
    *         <td>BOB</td>
    *         <td>bob</td>
    *     </tr>
    *     <tr>
    *         <td>Charlie</td>
    *         <td>CHARLIE</td>
    *         <td>charlie</td>
    *     </tr>
    *     <tr>
    *         <td>alice</td>
    *         <td>ALICE</td>
    *         <td>alice</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.selectColumns.select | javadoc -->
    * <pre>{@code
    * names.selectColumns("uppercase", "lowercase");
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.selectColumns.after -->
    * <table>
    *     <caption>
    *         after
    *     </caption>
    *     <tr>
    *         <th>uppercase</th>
    *         <th>lowercase</th>
    *     </tr>
    *     <tr>
    *         <td>ALICE</td>
    *         <td>alice</td>
    *     </tr>
    *     <tr>
    *         <td>BOB</td>
    *         <td>bob</td>
    *     </tr>
    *     <tr>
    *         <td>CHARLIE</td>
    *         <td>charlie</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.selectColumns.exception | javadoc -->
    * <pre>{@code
    * names.toList(); // throws IllegalStateException
    * }</pre>
    * <!-- end_code_fragment: -->
    *
    * @param columnNames
    *    the names of columns to retain
    *
    * @return this instance, to allow method chaining
    *
    * @throws IllegalArgumentException
    *    when {@code columnNames} contains a column name that is not part of this table
    */
   public Table<T> selectColumns(String... columnNames)
   {
      final Set<String> toBeDropped = new HashSet<>(this.columns);
      for (String columnName : columnNames)
      {
         if (!toBeDropped.remove(columnName))
         {
            // failure to remove means either the columnName was not part of this table to begin with,
            // or it was selected twice.
            throw new IllegalArgumentException("unknown column name: " + columnName);
         }
      }

      for (final String drop : toBeDropped)
      {
         this.dropColumnImpl(drop);
      }

      this.removeDuplicateRows();

      return this;
   }

   // --------------- Filter ---------------

   /**
    * Removes all rows from this table for which the predicate returned {@code false} when passed the cell value of the
    * column this table points to.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.filter | javadoc -->
    * <pre>{@code
    * final Table<Integer> numbers = new Table<>("A", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    * numbers.filter(i -> i % 2 == 0);
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.filter.result -->
    * <table>
    *     <caption>
    *         result
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *     </tr>
    *     <tr>
    *         <td>4</td>
    *     </tr>
    *     <tr>
    *         <td>6</td>
    *     </tr>
    *     <tr>
    *         <td>8</td>
    *     </tr>
    *     <tr>
    *         <td>10</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    *
    * @param predicate
    *    the predicate that determines which rows should be kept
    *
    * @return this table, to allow method chaining
    */
   public Table<T> filter(Predicate<? super T> predicate)
   {
      int column = this.getColumnIndex();
      this.table.removeIf(row -> !predicate.test((T) row.get(column)));
      return this;
   }

   /**
    * Removes all rows from this table for which the predicate returned {@code false}.
    * The rows are passed as maps from column name to cell value.
    * <p>
    * Example:
    * <!-- insert_code_fragment: TableTest.filterRows.initial | javadoc -->
    * <pre>{@code
    * Table<Integer> a = new Table<>("A", 1, 2);
    * Table<Integer> b = a.expandAll("B", i -> Arrays.asList(1, 2));
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.filterRows.before -->
    * <table>
    *     <caption>
    *         before
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>B</th>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>1</td>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>2</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>1</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>2</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.filterRows.action | javadoc -->
    * <pre>{@code
    * a.filterRows(row -> (int) row.get("A") != (int) row.get("B"));
    * }</pre>
    * <!-- end_code_fragment: -->
    * <p>
    * <!-- insert_code_fragment: TableTest.filterRows.after -->
    * <table>
    *     <caption>
    *         after
    *     </caption>
    *     <tr>
    *         <th>A</th>
    *         <th>B</th>
    *     </tr>
    *     <tr>
    *         <td>1</td>
    *         <td>2</td>
    *     </tr>
    *     <tr>
    *         <td>2</td>
    *         <td>1</td>
    *     </tr>
    * </table>
    * <!-- end_code_fragment: -->
    *
    * @param predicate
    *    the predicate that determines which rows should be kept
    *
    * @return this table, to allow method chaining
    *
    * @since 1.2
    */
   public Table<T> filterRows(Predicate<? super Map<String, Object>> predicate)
   {
      return this.filterRowsImpl(predicate);
   }

   Table<T> filterRowsImpl(Predicate<? super LinkedHashMap<String, Object>> predicate)
   {
      this.table.removeIf(row -> !predicate.test(this.convertRowToMap(row)));
      return this;
   }

   private LinkedHashMap<String, Object> convertRowToMap(List<Object> row)
   {
      LinkedHashMap<String, Object> map = new LinkedHashMap<>();
      final List<String> strings = this.columns;
      for (int i = 0; i < strings.size(); i++)
      {
         final String column = strings.get(i);
         map.put(column, row.get(i));
      }
      return map;
   }

   /**
    * @return the number of rows of this table
    *
    * @since 1.2
    */
   public int rowCount()
   {
      return this.table.size();
   }

   /**
    * {@inheritDoc}
    *
    * @return an iterator over the cell values of the column this table instance points to
    *
    * @since 1.2
    */
   @Override
   public Iterator<T> iterator()
   {
      return new Iterator<T>()
      {
         private final Iterator<List<Object>> listIterator = Table.this.table.iterator();
         private final int column = Table.this.getColumnIndex();

         @Override
         public boolean hasNext()
         {
            return this.listIterator.hasNext();
         }

         @Override
         public T next()
         {
            return (T) this.listIterator.next().get(this.column);
         }
      };
   }

   /**
    * @return a list of cell values of the column this table instance points to
    */
   public List<T> toList()
   {
      return this.stream().collect(Collectors.toList());
   }

   /**
    * @return a set of cell values of the column this table instance points to
    */
   public Set<T> toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }

   /**
    * @return a stream of cell values of the column this table instance points to
    *
    * @since 1.2
    */
   public Stream<T> stream()
   {
      int column = this.getColumnIndex();
      return this.table.stream().map(l -> (T) l.get(column));
   }

   @Override
   public String toString()
   {
      return TOSTRING_RENDERER.render(this);
   }

   // =============== Deprecated Members ===============

   /**
    * @return a 2D copy of the internal table
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public ArrayList<ArrayList<Object>> getTable()
   {
      // defensive copy.
      return this.table.stream().map(ArrayList::new).collect(Collectors.toCollection(ArrayList::new));
   }

   /**
    * @return a copy of the internal column map
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public LinkedHashMap<String, Integer> getColumnMap()
   {
      final int columnCount = this.columns.size();
      final LinkedHashMap<String, Integer> map = new LinkedHashMap<>(columnCount);
      for (int i = 0; i < columnCount; i++)
      {
         map.put(this.columns.get(i), i);
      }
      return map;
   }

   @Deprecated
   void setColumnMap_(LinkedHashMap<String, Integer> columnMap)
   {
      final int max = columnMap.values().stream().mapToInt(Integer::intValue).max().orElse(0);
      final List<String> columns = new ArrayList<>(Collections.nCopies(max + 1, null));

      for (final Map.Entry<String, Integer> entry : columnMap.entrySet())
      {
         columns.set(entry.getValue(), entry.getKey());
      }

      this.columns = columns;
   }
}
