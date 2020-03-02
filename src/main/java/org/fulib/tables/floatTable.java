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

   private DoubleStream doubleStream()
   {
      return this.stream().mapToDouble(Float::doubleValue);
   }

   public float sum()
   {
      return (float) this.doubleStream().sum();
   }

   public float min()
   {
      return (float) this.doubleStream().min().orElse(Float.MAX_VALUE);
   }

   public float max()
   {
      return (float) this.doubleStream().max().orElse(Float.MIN_VALUE);
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

   public float median()
   {
      return PrimitiveTable.medianImpl(this);
   }
}
