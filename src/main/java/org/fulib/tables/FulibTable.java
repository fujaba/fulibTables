package org.fulib.tables;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @deprecated since 1.2; unused
 */
@Deprecated
public class FulibTable
{
   // =============== Fields ===============

   private ArrayList<ArrayList> table = new ArrayList<>();
   private String columnName = null;
   private LinkedHashMap<String, Integer> columnMap = new LinkedHashMap<>();

   // =============== Constructors ===============

   public FulibTable(String... columnNames)
   {
      for (String name : columnNames)
      {
         this.addColumn(name);
      }
   }

   // =============== Properties ===============

   public ArrayList<ArrayList> getTable()
   {
      return this.table;
   }

   public void setTable(ArrayList<ArrayList> table)
   {
      this.table = table;
   }

   public String getColumnName()
   {
      return this.columnName;
   }

   public void setColumnName(String columnName)
   {
      this.columnName = columnName;
   }

   public LinkedHashMap<String, Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public void setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.columnMap = columnMap;
   }

   // =============== Methods ===============

   public void addColumn(String name)
   {
      this.columnMap.put(name, this.columnMap.size());

      for (ArrayList<Object> row : this.table)
      {
         row.add(null);
      }

      this.setColumnName(name);
   }

   public void addRow(ArrayList row)
   {
      this.table.add(row);
   }

   public Object getValue(String colName, int rowNumber)
   {
      Integer index = this.getColumnMap().get(colName);
      ArrayList row = this.table.get(rowNumber);
      return row.get(index);
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
      for (ArrayList<Object> row : this.table)
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
