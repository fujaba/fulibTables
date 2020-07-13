package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class doubleTableTest
{
   private final doubleTable empty = new doubleTable();
   private final doubleTable a = new doubleTable(1.0, 2.5, 3.75);
   private final doubleTable b = new doubleTable(-4.8, 2.3, -6.5, 8.5);

   @Test
   public void sum()
   {
      assertEquals(0, this.empty.sum(), 0);
      assertEquals(7.25, this.a.sum(), 0);
      assertEquals(-0.5, this.b.sum(), 0);
   }

   @Test
   public void min()
   {
      assertEquals(Double.MAX_VALUE, this.empty.min(), 0);
      assertEquals(1.0, this.a.min(), 0);
      assertEquals(-6.5, this.b.min(), 0);
   }

   @Test
   public void max()
   {
      assertEquals(-Double.MAX_VALUE, this.empty.max(), 0);
      assertEquals(3.75, this.a.max(), 0);
      assertEquals(8.5, this.b.max(), 0);
   }

   @Test
   public void average()
   {
      assertEquals(Double.NaN, this.empty.average(), 0);
      assertEquals(7.25 / 3, this.a.average(), 0);
      assertEquals(-0.125, this.b.average(), 0);
   }
}
