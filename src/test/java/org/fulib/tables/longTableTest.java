package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class longTableTest
{
   private final longTable empty = new longTable();
   private final longTable a = new longTable(1L, 2L, 3L);
   private final longTable b = new longTable(-5L, 2L, -7L, 9L);

   @Test
   public void sum()
   {
      assertEquals(0, this.empty.sum());
      assertEquals(6, this.a.sum());
      assertEquals(-1, this.b.sum());
   }

   @Test
   public void min()
   {
      assertEquals(Long.MAX_VALUE, this.empty.min());
      assertEquals(1, this.a.min());
      assertEquals(-7, this.b.min());
   }

   @Test
   public void max()
   {
      assertEquals(Long.MIN_VALUE, this.empty.max());
      assertEquals(3, this.a.max());
      assertEquals(9, this.b.max());
   }

   @Test
   public void average()
   {
      assertEquals(Double.NaN, this.empty.average(), 0);
      assertEquals(2, this.a.average(), 0);
      assertEquals(-0.25, this.b.average(), 0);
   }
}
