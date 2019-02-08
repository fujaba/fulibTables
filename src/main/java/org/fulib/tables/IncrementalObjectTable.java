package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncrementalObjectTable
{

   private ListeningTable listeningTable = null;

   public ListeningTable getListeningTable()
   {
      return listeningTable;
   }

   public void setListeningTable(ListeningTable listeningTable)
   {
      this.listeningTable = listeningTable;
   }



   private ArrayList<ArrayList<Object>> table;

   public ArrayList<ArrayList<Object>> getTable()
   {
      return table;
   }

   public IncrementalObjectTable setTable(ArrayList<ArrayList<Object>> value)
   {
      this.table = value;
      return this;
   }


   public IncrementalObjectTable(Object... start)
   {
      this("A", start);
   }


   public IncrementalObjectTable(String colName, Object... start)
   {
      this.table = new ArrayList<>();
      this.listeningTable = new ListeningTable(this, this.table);
      this.getAllObjectTables().add(this);
      this.setColumnName(colName);
      columnMap.put(colName, 0);
      for (Object current : start)
      {
         ArrayList<Object> row = new ArrayList<>();
         row.add(current);
         listeningTable.addRow(row);

         if (reflectorMap == null)
         {
            reflectorMap = new ReflectorMap(current.getClass().getPackage().getName());
         }
      }
   }



   private ArrayList<IncrementalObjectTable> allObjectTables = null;

   public void setAllObjectTables(ArrayList<IncrementalObjectTable> allObjectTables)
   {
      this.allObjectTables = allObjectTables;
   }

   public ArrayList<IncrementalObjectTable> getAllObjectTables()
   {
      if (allObjectTables == null)
      {
         allObjectTables = new ArrayList<>();
      }

      return allObjectTables;
   }

   private ReflectorMap reflectorMap;

   public ReflectorMap getReflectorMap()
   {
      return reflectorMap;
   }

   public void setReflectorMap(ReflectorMap reflectorMap)
   {
      this.reflectorMap = reflectorMap;
   }




   private String columnName = null;

   public String getColumnName()
   {
      return columnName;
   }

   public IncrementalObjectTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }


   private LinkedHashMap<String, Integer> columnMap = new LinkedHashMap<>();

   public LinkedHashMap<String, Integer> getColumnMap()
   {
      return columnMap;
   }

   public IncrementalObjectTable setColumnMap(LinkedHashMap<String, Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public IncrementalObjectTable expandLink(String newColumnName, String linkName)
   {
      IncrementalObjectTable result = new IncrementalObjectTable();
      this.getAllObjectTables().add(result);
      result.setAllObjectTables(this.getAllObjectTables());
      result.setColumnMap(this.columnMap);
      result.setReflectorMap(reflectorMap);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;

      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ListeningTable oldListeningTable = this.listeningTable;
      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();

      ListeningTable targetTable = result.getListeningTable();

      updateAllObjectTables(targetTable);

      oldListeningTable.setTargetTable(targetTable);

      targetTable.setColumnAndLink(columnName, linkName);

      for (ArrayList<Object> row : oldTable)
      {
         addRowsForLink(linkName, this.getColumnName(), this.listeningTable, row);
      }

      return result;
   }

   public void addRowsForLink(String linkName, String startColumnName, ListeningTable newListeningTable, ArrayList<Object> row)
   {
      Integer index = columnMap.get(startColumnName);
      Object start = row.get(index);
      Reflector reflector = reflectorMap.getReflector(start);
      Object value = reflector.getValue(start, linkName);

      LinkChangeListener linkChangeListener = new LinkChangeListener(start, row, linkName, newListeningTable);
      try
      {
         Method addPropertyChangeListenerMethod = start.getClass().getMethod("addPropertyChangeListener", String.class, PropertyChangeListener.class);
         addPropertyChangeListenerMethod.invoke(start, linkName, linkChangeListener);
      }
      catch (Exception e)
      {
         Logger.getGlobal().log(Level.SEVERE, "could not add property change listener to " + start, e);
      }

      addRowsForLinkValues(linkName, newListeningTable, row, start, value);
   }

   public void addRowsForLinkValues(String linkName, ListeningTable newListeningTable, ArrayList<Object> row, Object start, Object value)
   {
      if (value instanceof Collection)
      {
         for (Object current : (Collection) value)
         {
            ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
            newRow.add(current);
            newListeningTable.addRow(newRow);
         }
      }
      else if (value != null)
      {
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         newListeningTable.addRow(newRow);
      }
   }

   private void updateAllObjectTables(ListeningTable newTable)
   {
      for (IncrementalObjectTable previousTable : allObjectTables)
      {
         previousTable.setListeningTable(newTable);
         previousTable.setTable(newTable.getBaseTable());
      }
   }

   public IncrementalObjectTable hasLink(String linkName, IncrementalObjectTable rowName)
   {
      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(columnMap.get(this.getColumnName()));
         Object other = row.get(columnMap.get(rowName.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, linkName);

         if (value instanceof Collection && ((Collection) value).contains(other)
               || value == other)
         {
            this.table.add(row);
         }
      }
      return this;
   }

   public doubleTable expandDouble(String newColumnName, String attrName)
   {
      doubleTable result = new doubleTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = (Object) row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.table.add(newRow);
      }

      return result;
   }



   public floatTable expandFloat(String newColumnName, String attrName)
   {
      floatTable result = new floatTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = (Object) row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.table.add(newRow);
      }

      return result;
   }



   public intTable expandInt(String newColumnName, String attrName)
   {
      intTable result = new intTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = (Object) row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.table.add(newRow);
      }

      return result;
   }



   public longTable expandLong(String newColumnName, String attrName)
   {
      longTable result = new longTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = (Object) row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.table.add(newRow);
      }

      return result;
   }


   public StringTable expandString(String newColumnName, String attrName)
   {
      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      result.setColumnName(newColumnName);
      columnMap.put(newColumnName, newColumnNumber);

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = (Object) row.get(columnMap.get(this.getColumnName()));
         Reflector reflector = reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, attrName);
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         this.table.add(newRow);
      }

      return result;
   }



   public void addColumn(String columnName, java.util.function.Function<LinkedHashMap<String,Object>,Object> function)
   {
      int newColumnNumber = this.table.size() > 0 ? this.table.get(0).size() : 0;
      for (ArrayList<Object> row : this.table)
      {
         LinkedHashMap<String,Object> map = new LinkedHashMap<>();
         for (String key : columnMap.keySet())
         {
            map.put(key, row.get(columnMap.get(key)));
         }
         Object result = function.apply(map);
         row.add(result);
      }
      this.columnMap.put(columnName, newColumnNumber);
   }



   public IncrementalObjectTable dropColumns(String... columnNames)
   {
      LinkedHashMap<String, Integer> oldColumnMap = (LinkedHashMap<String, Integer>) this.columnMap.clone();
      this.columnMap.clear();

      LinkedHashSet<String> dropNames = new LinkedHashSet<>();
      dropNames.addAll(Arrays.asList(columnNames));
      int i = 0;
      for (String name : oldColumnMap.keySet())
      {
         if ( ! dropNames.contains(name))
         {
            this.columnMap.put(name, i);
            i++;
         }
      }

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();

      LinkedHashSet<ArrayList<Object> > rowSet = new LinkedHashSet<>();
      for (ArrayList row : oldTable)
      {
         ArrayList newRow = new ArrayList();
         for (String name : this.columnMap.keySet())
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
            this.table.add(newRow);
      }

      return this;
   }

   public IncrementalObjectTable selectColumns(String... columnNames)
   {
      LinkedHashMap<String, Integer> oldColumnMap = (LinkedHashMap<String, Integer>) this.columnMap.clone();
      this.columnMap.clear();

      int i = 0;
      for (String name : columnNames)
      {
         if (oldColumnMap.get(name) == null)
            throw new IllegalArgumentException("unknown column name: " + name);
         this.columnMap.put(name, i);
         i++;
      }

      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();

      LinkedHashSet<ArrayList<Object> > rowSet = new LinkedHashSet<>();
      for (ArrayList row : oldTable)
      {
         ArrayList newRow = new ArrayList();
         for (String name : columnNames)
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
            this.table.add(newRow);
      }

      return this;
   }



   public IncrementalObjectTable filter(Predicate< Object > predicate)
   {
      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         Object start = row.get(columnMap.get(this.getColumnName()));
         if (predicate.test(start))
         {
            this.table.add(row);
         }
      }
      return this;
   }


   public IncrementalObjectTable filterRow(Predicate<LinkedHashMap<String,Object> > predicate)
   {
      ArrayList<ArrayList<Object> > oldTable = (ArrayList<ArrayList<Object> >) this.table.clone();
      this.table.clear();
      for (ArrayList<Object> row : oldTable)
      {
         LinkedHashMap<String,Object> map = new LinkedHashMap< >();
         for (String key : columnMap.keySet())
         {
            map.put(key, row.get(columnMap.get(key)));
         }
         if (predicate.test(map))
         {
            this.table.add(row);
         }
      }
      return this;
   }


   public LinkedHashSet< Object > toSet()
   {
      LinkedHashSet< Object > result = new LinkedHashSet<>();
      for (ArrayList row : this.table)
      {
         Object value = row.get(columnMap.get(columnName));
         result.add(value);
      }
      return result;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder("| ");
      for (String key : columnMap.keySet())
      {
         buf.append(key).append(" \t| ");
      }
      buf.append("\n| ");
      for (String key : columnMap.keySet())
      {
         buf.append(" --- \t| ");
      }
      buf.append("\n");
      for (ArrayList<Object> row : table)
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
