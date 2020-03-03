package org.fulib.tables;

import java.util.stream.DoubleStream;

public class floatTable extends PrimitiveTable<Float>
{
   // =============== Constructors ===============

   public floatTable(Float... start)
   {
      super(start);
   }

   protected floatTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   private DoubleStream doubleStream() // TODO public?
   {
      return this.stream().mapToDouble(Float::doubleValue);
   }

   /**
    * @return the sum of the cell values of the column this table points to
    */
   public float sum()
   {
      return (float) this.doubleStream().sum();
   }

   /**
    * @return the minimum of the cell values of the column this table points to,
    * or {@link Float#MAX_VALUE} if this table has no rows
    */
   public float min()
   {
      return (float) this.doubleStream().min().orElse(Float.MAX_VALUE);
   }

   /**
    * @return the maximum of the cell values of the column this table points to,
    * or negative {@link Float#MAX_VALUE} if this table has no rows
    */
   public float max()
   {
      return (float) this.doubleStream().max().orElse(-Float.MAX_VALUE);
   }

   /**
    * @return the average of the cell values of the column this table points to,
    * or {@link Double#NaN} if this table has no rows
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
   public float median()
   {
      return PrimitiveTable.medianImpl(this);
   }
}
