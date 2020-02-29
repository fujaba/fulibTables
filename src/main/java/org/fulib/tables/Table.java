package org.fulib.tables;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Adrian Kunz
 * @since 1.2
 */
public class Table<T> implements Iterable<T>
{
   // =============== Constants ===============

   private static final String DEFAULT_COLUMN_NAME = "A";

   private static final Renderer TOSTRING_RENDERER = new MarkdownRenderer();

   // =============== Fields ===============

   private String columnName;
   protected List<List<Object>> table;
   protected Map<String, Integer> columnMap;

   // =============== Constructors ===============

   /**
    * Creates a new empty table with zero rows and zero columns.
    */
   public Table()
   {
      this.table = new ArrayList<>();
      this.columnMap = new LinkedHashMap<>();
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
      this.columnMap.put(columnName, 0);
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

   protected Table(Table<?> base)
   {
      this.columnName = base.columnName;
      this.columnMap = base.columnMap;
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

   protected void copyTo(Table<T> copy)
   {
      copy.columnName = this.columnName;
      copy.columnMap.putAll(this.columnMap);
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
   protected void setColumnName_(String columnName)
   {
      this.columnName = columnName;
   }

   protected int getColumnIndex()
   {
      final Integer column = this.columnMap.get(this.columnName);
      if (column == null)
      {
         throw new IllegalStateException(
            "Column '" + this.columnName + "' is no longer part of table columns " + this.columnMap.keySet() + ". "
            + "It was likely evicted after a selectColumns or dropColumns operation. "
            + "This Table instance is no longer valid");
      }
      return column;
   }

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
      return new LinkedHashMap<>(this.columnMap);
   }

   protected int getNewColumnNumber()
   {
      return this.table.isEmpty() ? 0 : this.table.get(0).size();
   }

   // =============== Methods ===============

   protected void addColumn(String columnName)
   {
      this.columnMap.put(columnName, this.getNewColumnNumber());
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
   public <TAB extends Table<T>> TAB as(Function<? super Table<T>, ? extends TAB> constructor)
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
    *
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

   /**
    * Implements the {@link #expand(String, Function)} operation, with the exception that it does not create the table
    * instance that points to the new column.
    *
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a value for the new column
    *
    * @since 1.2
    */
   protected void expandImpl(String columnName, Function<? super T, ?> function)
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
    *
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

   /**
    * Implements the {@link #expandAll(String, Function)} operation, with the exception that it does not create the
    * table instance that points to the new column.
    *
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a collection of values for the new column
    *
    * @since 1.2
    */
   protected void expandAllImpl(String columnName, Function<? super T, ? extends Collection<?>> function)
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
    * <!-- insert_code_fragment: TableTest.deriveColumn | javadoc -->
    * <pre>{@code
    * Table<Integer> a = new Table<>("A", 1, 2);
    * Table<Integer> b = a.expand("B", i -> i * 10);
    * Table<Integer> c = b.deriveColumn("C", row -> (int) row.get("A") + (int) row.get("B"));
    * }</pre>
    * <!-- end_code_fragment: -->
    *
    * <!-- insert_code_fragment: TableTest.deriveColumn.c -->
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
   public <U> Table<U> deriveColumn(String columnName, Function<? super Map<String, Object>, ? extends U> function)
   {
      this.addColumnImpl(columnName, function);
      final Table<U> result = new Table<>(this);
      result.setColumnName_(columnName);
      return result;
   }

   /**
    * Same as {@link #deriveColumn(String, Function)}, except it has stricter requirements on the parameter type of the
    * predicate and does not return a table pointing to the new column.
    *
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a value for the new column
    *
    * @see #deriveColumn(String, Function)
    * @deprecated since 1.2; use {@link #deriveColumn(String, Function)} instead
    */
   @Deprecated
   public void addColumn(String columnName, Function<LinkedHashMap<String, Object>, Object> function)
   {
      this.addColumnImpl(columnName, function);
   }

   private void addColumnImpl(String columnName, Function<? super LinkedHashMap<String, Object>, ?> function)
   {
      int newColumnNumber = this.getNewColumnNumber();
      for (List<Object> row : this.table)
      {
         LinkedHashMap<String, Object> map = this.convertRowToMap(row);
         Object result = function.apply(map);
         row.add(result);
      }
      this.columnMap.put(columnName, newColumnNumber);
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
    *
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
    *
    * <!-- insert_code_fragment: TableTest.dropColumns.select | javadoc -->
    * <pre>{@code
    * names.dropColumns(names.getColumnName());
    * }</pre>
    * <!-- end_code_fragment: -->
    *
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
    *
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
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.columnMap);
      this.columnMap.clear();

      Set<String> dropNames = new HashSet<>(Arrays.asList(columnNames));
      int i = 0;
      for (String name : oldColumnMap.keySet())
      {
         if (!dropNames.contains(name))
         {
            this.columnMap.put(name, i);
            i++;
         }
      }

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();

      Set<List<Object>> rowSet = new HashSet<>();
      for (List<Object> row : oldTable)
      {
         List<Object> newRow = new ArrayList<>();
         for (String name : this.columnMap.keySet())
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
         {
            this.table.add(newRow);
         }
      }

      return this;
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
    *
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
    *
    * <!-- insert_code_fragment: TableTest.selectColumns.select | javadoc -->
    * <pre>{@code
    * names.selectColumns("uppercase", "lowercase");
    * }</pre>
    * <!-- end_code_fragment: -->
    *
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
    *
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
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.columnMap);
      this.columnMap.clear();

      for (int i = 0; i < columnNames.length; i++)
      {
         String name = columnNames[i];
         if (oldColumnMap.get(name) == null)
         {
            throw new IllegalArgumentException("unknown column name: " + name);
         }
         this.columnMap.put(name, i);
      }

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();

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
            this.table.add(newRow);
         }
      }

      return this;
   }

   // --------------- Filter ---------------

   /**
    * Removes all rows from this table for which the predicate returned {@code false} when passed the cell value of the
    * column this table points to.
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
   public Table<T> filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      return this.filterRowsImpl(predicate);
   }

   private Table<T> filterRowsImpl(Predicate<? super LinkedHashMap<String, Object>> predicate)
   {
      this.table.removeIf(row -> !predicate.test(this.convertRowToMap(row)));
      return this;
   }

   private LinkedHashMap<String, Object> convertRowToMap(List<Object> row)
   {
      LinkedHashMap<String, Object> map = new LinkedHashMap<>();
      for (Map.Entry<String, Integer> entry : this.columnMap.entrySet())
      {
         map.put(entry.getKey(), row.get(entry.getValue()));
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
}
