package org.fulib.tables;

import java.util.ArrayList;
import java.util.Collections;

public class IncrementalDoubleTable extends IncrementalTable
{
   private String columnName = null;

   @Override
   public String getColumnName()
   {
      return this.columnName;
   }

   @Override
   public void setColumnName(String columnName)
   {
      this.columnName = columnName;
   }

   public IncrementalDoubleTable(Double... start)
   {
      this.setTable(new ArrayList<>());
      this.setListeningTable(new ListeningTable(this, this.getTable()));
      this.getAllObjectTables().add(this);
      this.setAllListeningTables(new ArrayList<>());
      this.setAllListeningTables(this.getAllObjectTables().get(0).getAllListeningTables());
      this.getAllListeningTables().add(this.getListeningTable());
      this.setColumnName("A");

      this.getColumnMap().put(this.columnName, 0);
      for (Double current : start)
      {
         ArrayList<Object> row = new ArrayList<>();
         row.add(current);
         this.getTable().add(row);
      }
   }

   public double sum()
   {
      double result = 0;
      for (ArrayList<Object> row : this.getTable())
      {
         result += (Double) row.get(this.getColumnMap().get(this.columnName));
      }
      return result;
   }

   public double min()
   {
      double result = Double.MAX_VALUE;
      for (ArrayList<Object> row : this.getTable())
      {
         double value = (Double) row.get(this.getColumnMap().get(this.columnName));
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public double max()
   {
      double result = Double.MIN_VALUE;
      for (ArrayList<Object> row : this.getTable())
      {
         double value = (Double) row.get(this.getColumnMap().get(this.columnName));
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public double median()
   {
      ArrayList<Double> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }

   @Override
   public ArrayList<Double> toList()
   {
      ArrayList<Double> result = new ArrayList<>();
      for (ArrayList<Object> row : this.getTable())
      {
         double value = (Double) row.get(this.getColumnMap().get(this.columnName));
         result.add(value);
      }
      return result;
   }

   @Override
   public String toString()
   {
      if (this.getColumnMap() == null)
      {
         return "|empty|\n"; //<===========================
      }

      StringBuilder buf = new StringBuilder();
      for (String key : this.getColumnMap().keySet())
      {
         buf.append(key).append(" \t");
      }
      buf.append("\n");
      for (ArrayList<Object> row : this.getTable())
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
