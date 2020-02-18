package org.fulib.tables;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

public class longTable extends PrimitiveTable<Long>
{
   // =============== Constructors ===============

   public longTable(Long... start)
   {
      super(start);
   }

   protected longTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   private LongStream longStream() // TODO public?
   {
      return this.stream().mapToLong(Long::longValue);
   }

   public long sum()
   {
      return this.longStream().sum();
   }

   public long min()
   {
      return this.longStream().min().orElse(Long.MAX_VALUE);
   }

   public long max()
   {
      return this.longStream().max().orElse(Long.MIN_VALUE);
   }

   /**
    * @since 1.2
    */
   public double average()
   {
      return this.longStream().average().orElse(Double.NaN);
   }

   public long median()
   {
      List<Long> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
