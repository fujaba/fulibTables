package org.fulib.tables;

import java.util.*;
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
      StringBuilder buf = new StringBuilder();
      for (String key : this.columnMap.keySet())
      {
         buf.append(key).append(" \t");
      }
      buf.append("\n");
      for (List<Object> row : this.table)
      {
         for (Object cell : row)
         {
            buf.append(cell).append(" \t");
         }
         buf.append("\n");
      }
      buf.append("\n");
      return buf.toString();
   }
}
