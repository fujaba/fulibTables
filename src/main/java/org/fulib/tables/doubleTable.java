package org.fulib.tables;

import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

public class doubleTable extends Table<Double>
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
      return this.doubleStream().max().orElse(Double.MIN_VALUE);
   }

   public double average()
   {
      return this.doubleStream().average().orElse(Double.NaN);
   }

   public double median()
   {
      List<Double> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
