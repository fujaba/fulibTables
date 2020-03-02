package org.fulib.tables;

import java.util.stream.IntStream;

public class intTable extends PrimitiveTable<Integer>
{
   // =============== Constructors ===============

   public intTable(Integer... start)
   {
      super(start);
   }

   protected intTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   private IntStream intStream() // TODO public?
   {
      return this.stream().mapToInt(Integer::intValue);
   }

   public int sum()
   {
      return this.intStream().sum();
   }

   public int min()
   {
      return this.intStream().min().orElse(Integer.MAX_VALUE);
   }

   public int max()
   {
      return this.intStream().max().orElse(Integer.MIN_VALUE);
   }

   /**
    * @return the average of the cell values of the column this table points to,
    * or {@link Double#NaN} if this table is empty
    *
    * @since 1.2
    */
   public double average()
   {
      return this.intStream().average().orElse(Double.NaN);
   }

   /**
    * @return the median of the cell values of the column this table points to
    *
    * @deprecated since 1.2; this method does not work correctly for 0 or an even number of rows
    */
   @Deprecated
   public int median()
   {
      return PrimitiveTable.medianImpl(this);
   }
}
