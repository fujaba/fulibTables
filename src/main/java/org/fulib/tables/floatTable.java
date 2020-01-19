package org.fulib.tables;

import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

public class floatTable extends AbstractTable<Float>
{
   // =============== Constructors ===============

   public floatTable(Float... start)
   {
      super(start);
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

   public float median()
   {
      List<Float> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
