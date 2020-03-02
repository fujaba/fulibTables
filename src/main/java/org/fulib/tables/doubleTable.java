package org.fulib.tables;

import java.util.stream.DoubleStream;

public class doubleTable extends PrimitiveTable<Double>
{
   // =============== Constructors ===============

   public doubleTable(Double... start)
   {
      super(start);
   }

   protected doubleTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   private DoubleStream doubleStream() // TODO public?
   {
      return this.stream().mapToDouble(Double::doubleValue);
   }

   public double sum()
   {
      return this.doubleStream().sum();
   }

   public double min()
   {
      return this.doubleStream().min().orElse(Double.MAX_VALUE);
   }

   public double max()
   {
      return this.doubleStream().max().orElse(-Double.MAX_VALUE);
   }

   /**
    * @return the average of the cell values of the column this table points to,
    * or {@link Double#NaN} if this table is empty
    *
    * @since 1.2
    */
   public double average()
   {
      return this.doubleStream().average().orElse(Double.NaN);
   }

   /**
    * @return the median of the cell values of the column this table points to
    *
    * @deprecated since 1.2; this method does not work correctly for 0 or an even number of rows
    */
   @Deprecated
   public double median()
   {
      return PrimitiveTable.medianImpl(this);
   }
}
