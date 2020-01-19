package org.fulib.tables;

import java.util.Collections;
import java.util.List;

public class longTable extends AbstractTable<Long>
{
   // =============== Constructors ===============

   public longTable(Long... start)
   {
      super(start);
   }

   // =============== Methods ===============

   public long sum()
   {
      int column = this.getColumn();
      long result = 0;
      for (List<Object> row : this.getTable())
      {
         result += (Long) row.get(column);
      }
      return result;
   }

   public long min()
   {
      int column = this.getColumn();
      long result = Long.MAX_VALUE;
      for (List<Object> row : this.getTable())
      {
         long value = (Long) row.get(column);
         if (value < result)
         {
            result = value;
         }
      }
      return result;
   }

   public long max()
   {
      int column = this.getColumn();
      long result = Long.MIN_VALUE;
      for (List<Object> row : this.getTable())
      {
         long value = (Long) row.get(column);
         if (value > result)
         {
            result = value;
         }
      }
      return result;
   }

   public long median()
   {
      List<Long> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
