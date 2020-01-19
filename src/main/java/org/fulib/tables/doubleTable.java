package org.fulib.tables;

import java.util.Collections;
import java.util.List;

public class doubleTable extends AbstractTable<Double>
{
   // =============== Constructors ===============

   public doubleTable(Double... start)
   {
      super(start);
   }

   // =============== Methods ===============

   public double sum()
   {
      int column = this.getColumn();
      double result = 0;
      for (List<Object> row : this.getTable())
      {
         result += (Double) row.get(column);
      }
      return result;
   }

   public double min()
   {
      int column = this.getColumn();
      double result = Double.MAX_VALUE;
      for (List<Object> row : this.getTable())
      {
         double value = (Double) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public double max()
   {
      int column = this.getColumn();
      double result = Double.MIN_VALUE;
      for (List<Object> row : this.getTable())
      {
         double value = (Double) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public double median()
   {
      List<Double> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
