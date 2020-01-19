package org.fulib.tables;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class intTable extends AbstractTable<Integer>
{
   // =============== Constructors ===============

   public intTable(Integer... start)
   {
      super(start);
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

   public int median()
   {
      List<Integer> list = this.toList();
      Collections.sort(list);
      int index = list.size() / 2;
      return list.get(index);
   }
}
