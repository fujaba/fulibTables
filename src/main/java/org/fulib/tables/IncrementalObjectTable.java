package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncrementalObjectTable extends IncrementalTable
{

   public IncrementalObjectTable(Object... start)
   {
      this("A", start);
   }

   public IncrementalObjectTable(String colName, Object... start)
   {
      this.setTable(new ArrayList<>());
      this.setListeningTable(new ListeningTable(this, this.getTable()));
      this.getAllObjectTables().add(this);
      this.setAllListeningTables(new ArrayList<>());
      this.setAllListeningTables(this.getAllObjectTables().get(0).getAllListeningTables());
      this.getAllListeningTables().add(this.getListeningTable());
      this.setColumnName(colName);
      this.getColumnMap().put(colName, 0);
      for (Object current : start)
      {
         ArrayList<Object> row = new ArrayList<>();
         row.add(current);
         this.getListeningTable().addRow(row);

         if (this.getReflectorMap() == null)
         {
            this.setReflectorMap(new ReflectorMap(current.getClass().getPackage().getName()));
         }
      }
   }

   private String columnName = null;

   @Override
   public String getColumnName()
   {
      return this.columnName;
   }

   @Override
   public IncrementalObjectTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }

   public IncrementalObjectTable expandLink(String newColumnName, String linkName)
   {
      IncrementalObjectTable result = new IncrementalObjectTable();
      this.getAllObjectTables().add(result);
      result.setAllObjectTables(this.getAllObjectTables());
      result.setAllListeningTables(this.getAllListeningTables());
      result.getAllListeningTables().add(result.getListeningTable());
      result.setColumnMap(this.getColumnMap());
      result.setReflectorMap(this.getReflectorMap());
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;

      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      ListeningTable oldListeningTable = this.getListeningTable();
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();

      ListeningTable targetTable = result.getListeningTable();

      this.updateAllObjectTables(targetTable);

      oldListeningTable.setTargetTable(targetTable);

      targetTable.setColumnAndLink(this.columnName, linkName);

      for (ArrayList<Object> row : oldTable)
      {
         this.addRowsForLink(linkName, this.getColumnName(), this.getListeningTable(), oldListeningTable, row);
      }

      return result;
   }

   public IncrementalDoubleTable expandDouble(String newColumnName, String attrName)
   {
      IncrementalDoubleTable result = new IncrementalDoubleTable();
      this.getAllObjectTables().add(result);
      result.setAllObjectTables(this.getAllObjectTables());
      result.setAllListeningTables(this.getAllListeningTables());
      result.getAllListeningTables().add(result.getListeningTable());
      result.setColumnMap(this.getColumnMap());
      result.setReflectorMap(this.getReflectorMap());
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      ListeningTable oldListeningTable = this.getListeningTable();
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      ListeningTable targetTable = result.getListeningTable();
      this.updateAllObjectTables(targetTable);

      oldListeningTable.setTargetTable(targetTable);

      targetTable.setColumnAndLink(this.columnName, attrName);

      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         Reflector reflector = this.getReflectorMap().getReflector(start);
         Object value = reflector.getValue(start, attrName);

         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.getTable().add(newRow);

         AttrChangeListener attrChangeListener = new AttrChangeListener(start, row, newRow, targetTable);
         oldListeningTable.getRowListeners().put(row, attrChangeListener);
         try
         {
            Method addPropertyChangeListenerMethod = start.getClass()
                                                          .getMethod("addPropertyChangeListener", String.class,
                                                                     PropertyChangeListener.class);
            addPropertyChangeListenerMethod.invoke(start, attrName, attrChangeListener);
         }
         catch (Exception e)
         {
            Logger.getGlobal().log(Level.SEVERE, "could not add property change listener to " + start, e);
         }
      }

      return result;
   }

   public IncrementalObjectTable hasLink(String linkName, IncrementalObjectTable rowName)
   {
      ListeningTable oldListeningTable = this.getListeningTable();
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.setTable(new ArrayList<>());

      ListeningTable targetTable = new ListeningTable(this, this.getTable());
      this.updateAllObjectTables(targetTable);
      oldListeningTable.setTargetTable(targetTable);
      targetTable.setColumnAndLink(this.columnName, linkName);

      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         Object other = row.get(this.getColumnMap().get(rowName.getColumnName()));
         Reflector reflector = this.getReflectorMap().getReflector(start);
         Object value = reflector.getValue(start, linkName);

         HasLinkChangeListener hasLinkChangeListener = new HasLinkChangeListener(start, other, row, targetTable);
         try
         {
            Method addPropertyChangeListenerMethod = start.getClass()
                                                          .getMethod("addPropertyChangeListener", String.class,
                                                                     PropertyChangeListener.class);
            addPropertyChangeListenerMethod.invoke(start, linkName, hasLinkChangeListener);
         }
         catch (Exception e)
         {
            Logger.getGlobal().log(Level.SEVERE, "could not add property change listener to " + start, e);
         }

         oldListeningTable.getRowListeners().put(row, hasLinkChangeListener);

         if (value instanceof Collection && ((Collection<?>) value).contains(other) || value == other)
         {
            this.getTable().add(row);
         }
      }

      return this;
   }

   public floatTable expandFloat(String newColumnName, String attrName)
   {
      floatTable result = new floatTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         Reflector reflector = this.getReflectorMap().getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public intTable expandInt(String newColumnName, String attrName)
   {
      intTable result = new intTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         Reflector reflector = this.getReflectorMap().getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public longTable expandLong(String newColumnName, String attrName)
   {
      longTable result = new longTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         Reflector reflector = this.getReflectorMap().getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public StringTable expandString(String newColumnName, String attrName)
   {
      StringTable result = new StringTable();
      result.setColumnMap(this.getColumnMap());
      result.setTable(this.getTable());
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;
      result.setColumnName(newColumnName);
      this.getColumnMap().put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         Reflector reflector = this.getReflectorMap().getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.getTable().add(newRow);
      }

      return result;
   }

   public void addColumn(String columnName, Function<LinkedHashMap<String, Object>, Object> function)
   {
      int newColumnNumber = !this.getTable().isEmpty() ? this.getTable().get(0).size() : 0;
      for (ArrayList<Object> row : this.getTable())
      {
         LinkedHashMap<String, Object> map = new LinkedHashMap<>();
         for (String key : this.getColumnMap().keySet())
         {
            map.put(key, row.get(this.getColumnMap().get(key)));
         }
         Object result = function.apply(map);
         row.add(result);
      }
      this.getColumnMap().put(columnName, newColumnNumber);
   }

   public IncrementalObjectTable dropColumns(String... columnNames)
   {
      LinkedHashMap<String, Integer> oldColumnMap = (LinkedHashMap<String, Integer>) this.getColumnMap().clone();
      this.getColumnMap().clear();

      LinkedHashSet<String> dropNames = new LinkedHashSet<>(Arrays.asList(columnNames));
      int i = 0;
      for (String name : oldColumnMap.keySet())
      {
         if (!dropNames.contains(name))
         {
            this.getColumnMap().put(name, i);
            i++;
         }
      }

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();

      LinkedHashSet<ArrayList<Object>> rowSet = new LinkedHashSet<>();
      for (ArrayList<Object> row : oldTable)
      {
         ArrayList<Object> newRow = new ArrayList<>();
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

   public IncrementalObjectTable selectColumns(String... columnNames)
   {
      LinkedHashMap<String, Integer> oldColumnMap = (LinkedHashMap<String, Integer>) this.getColumnMap().clone();
      this.getColumnMap().clear();

      int i = 0;
      for (String name : columnNames)
      {
         if (oldColumnMap.get(name) == null)
         {
            throw new IllegalArgumentException("unknown column name: " + name);
         }
         this.getColumnMap().put(name, i);
         i++;
      }

      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();

      LinkedHashSet<ArrayList<Object>> rowSet = new LinkedHashSet<>();
      for (ArrayList<Object> row : oldTable)
      {
         ArrayList<Object> newRow = new ArrayList<>();
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

   public IncrementalObjectTable filter(Predicate<Object> predicate)
   {
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(this.getColumnMap().get(this.getColumnName()));
         if (predicate.test(start))
         {
            this.getTable().add(row);
         }
      }
      return this;
   }

   public IncrementalObjectTable filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      ArrayList<ArrayList<Object>> oldTable = (ArrayList<ArrayList<Object>>) this.getTable().clone();
      this.getTable().clear();
      for (ArrayList<Object> row : oldTable)
      {
         LinkedHashMap<String, Object> map = new LinkedHashMap<>();
         for (String key : this.getColumnMap().keySet())
         {
            map.put(key, row.get(this.getColumnMap().get(key)));
         }
         if (predicate.test(map))
         {
            this.getTable().add(row);
         }
      }
      return this;
   }

   @Override
   public LinkedHashSet<Object> toSet()
   {
      LinkedHashSet<Object> result = new LinkedHashSet<>();
      for (ArrayList row : this.getTable())
      {
         Object value = row.get(this.getColumnMap().get(this.columnName));
         result.add(value);
      }
      return result;
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
      for (String key : this.getColumnMap().keySet())
      {
         buf.append(" --- \t| ");
      }
      buf.append("\n");
      for (ArrayList<Object> row : this.getTable())
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
