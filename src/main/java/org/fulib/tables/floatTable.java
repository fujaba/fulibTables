package org.fulib.tables;

import java.util.Collections;
import java.util.List;

public class floatTable extends AbstractTable<Float>
{
   // =============== Constructors ===============

   public floatTable(Float... start)
   {
      super(start);
   }

   // =============== Methods ===============

   public float sum()
   {
      int column = this.getColumn();
      float result = 0;
      for (List<Object> row : this.getTable())
      {
         result += (Float) row.get(column);
      }
      return result;
   }

   public float min()
   {
      int column = this.getColumn();
      float result = Float.MAX_VALUE;
      for (List<Object> row : this.getTable())
      {
         float value = (Float) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public float max()
   {
      int column = this.getColumn();
      float result = Float.MIN_VALUE;
      for (List<Object> row : this.getTable())
      {
         float value = (Float) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public float median()
   {
      List<Float> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
