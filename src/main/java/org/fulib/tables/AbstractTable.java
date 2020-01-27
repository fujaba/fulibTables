package org.fulib.tables;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Adrian Kunz
 * @since 1.2
 */
public abstract class AbstractTable<T>
{
   // =============== Constants ===============

   private static final String DEFAULT_COLUMN_NAME = "A";

   // =============== Fields ===============

   private String               columnName;
   private List<List<Object>>   table;
   private Map<String, Integer> columnMap;

   // =============== Constructors ===============

   public AbstractTable(String columnName)
   {
      this.table = new ArrayList<>();
      this.columnName = columnName;
      this.columnMap = new LinkedHashMap<>();
      this.columnMap.put(columnName, 0);
   }

   @SafeVarargs
   public AbstractTable(T... start)
   {
      this(DEFAULT_COLUMN_NAME, start);
   }

   @SafeVarargs
   public AbstractTable(String columnName, T... start)
   {
      this(columnName);
      for (T current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }

   public AbstractTable(String columnName, AbstractTable<?> base)
   {
      this.columnName = columnName;
      this.columnMap = base.columnMap;
      this.table = base.table;
   }

   // =============== Properties ===============

   public String getColumnName()
   {
      return this.columnName;
   }

   public void setColumnName(String columnName)
   {
      this.columnName = columnName;
   }

   public int getColumn()
   {
      return this.columnMap.get(this.columnName);
   }

   public List<List<Object>> getTable()
   {
      return this.table;
   }

   public void setTable(List<List<Object>> table)
   {
      this.table = table;
   }

   @Deprecated
   public void setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
   }

   public Map<String, Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public void setColumnMap(Map<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
   }

   protected int getNewColumnNumber()
   {
      return this.table.isEmpty() ? 0 : this.table.get(0).size();
   }

   // =============== Methods ===============

   public void addColumn(String columnName)
   {
      this.columnMap.put(columnName, this.getNewColumnNumber());
   }

   // --------------- Columns ---------------

   // TODO deprecate and add overload
   //      <U> ObjectTable<U> addColumn(String columnName, Function<? super Map<String, Object>, ? extends U> function)
   //      needs to use a different name though because of same erasure ...
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

   // TODO what happens to the *Table objects that point to these columns?
   public AbstractTable<T> dropColumns(String... columnNames)
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

   // TODO what happens to the *Table objects that point to the other columns?
   public AbstractTable<T> selectColumns(String... columnNames)
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

   public AbstractTable<T> filter(Predicate<? super Object> predicate)
   {
      int column = this.getColumn();
      this.getTable().removeIf(row -> !predicate.test(row.get(column)));
      return this;
   }

   /**
    * @since 1.2
    */
   public AbstractTable<T> filterRows(Predicate<? super Map<String, Object>> predicate)
   {
      return this.filterRowsImpl(predicate);
   }

   /**
    * @deprecated since 1.2; use {@link #filterRows(Predicate)} instead
    */
   @Deprecated
   public AbstractTable<T> filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      return this.filterRowsImpl(predicate);
   }

   private AbstractTable<T> filterRowsImpl(Predicate<? super LinkedHashMap<String, Object>> predicate)
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

   public int rowCount()
   {
      return this.table.size();
   }

   public List<T> toList()
   {
      return this.stream().collect(Collectors.toList());
   }

   public Set<T> toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }

   public Stream<T> stream()
   {
      int column = this.getColumn();
      return this.table.stream().map(l -> (T) l.get(column));
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
