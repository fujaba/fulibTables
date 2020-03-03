package org.fulib.tables;

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

   /**
    * @return the sum of the cell values of the column this table points to
    */
   public long sum()
   {
      return this.longStream().sum();
   }

   /**
    * @return the minimum of the cell values of the column this table points to,
    * or {@link Long#MAX_VALUE} if this table has no rows
    */
   public long min()
   {
      return this.longStream().min().orElse(Long.MAX_VALUE);
   }

   /**
    * @return the maximum of the cell values of the column this table points to,
    * or {@link Long#MIN_VALUE} if this table has no rows
    */
   public long max()
   {
      return this.longStream().max().orElse(Long.MIN_VALUE);
   }

   /**
    * @return the average of the cell values of the column this table points to,
    * or {@link Double#NaN} if this table has no rows
    *
    * @since 1.2
    */
   public double average()
   {
      return this.longStream().average().orElse(Double.NaN);
   }

   /**
    * @return the median of the cell values of the column this table points to
    *
    * @deprecated since 1.2; this method does not work correctly for 0 or an even number of rows
    */
   @Deprecated
   public long median()
   {
      return PrimitiveTable.medianImpl(this);
   }
}
