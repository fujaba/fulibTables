package org.fulib.tables;

import java.util.Collections;
import java.util.List;

public class intTable extends AbstractTable<Integer>
{
   // =============== Constructors ===============

   public intTable(Integer... start)
   {
      super(start);
   }

   // =============== Methods ===============

   public int sum()
   {
      int column = this.getColumn();
      int result = 0;
      for (List<Object> row : this.getTable())
      {
         result += (Integer) row.get(column);
      }
      return result;
   }

   public int min()
   {
      int column = this.getColumn();
      int result = Integer.MAX_VALUE;
      for (List<Object> row : this.getTable())
      {
         int value = (Integer) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public int max()
   {
      int column = this.getColumn();
      int result = Integer.MIN_VALUE;
      for (List<Object> row : this.getTable())
      {
         int value = (Integer) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public int median()
   {
      List<Integer> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
