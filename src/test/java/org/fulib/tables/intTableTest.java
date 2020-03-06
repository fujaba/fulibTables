package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class intTableTest
{
   private final intTable empty = new intTable();
   private final intTable a = new intTable(1, 2, 3);
   private final intTable b = new intTable(-5, 2, -7, 9);

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
      assertEquals(Integer.MAX_VALUE, this.empty.min());
      assertEquals(1, this.a.min());
      assertEquals(-7, this.b.min());
   }

   @Test
   public void max()
   {
      assertEquals(Integer.MIN_VALUE, this.empty.max());
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
