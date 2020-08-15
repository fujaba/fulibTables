package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class doublePathTableTest
{
   private final PathTable empty = new PathTable("A");
   private final PathTable a = new PathTable("A", 1.0, 2.5, 3.75);
   private final PathTable b = new PathTable("A", -4.8, 2.3, -6.5, 8.5);

   @Test
   public void sum()
   {
      assertEquals(0, this.empty.sum("A"), 0);
      assertEquals(7.25, this.a.sum("A"), 0);
      assertEquals(-0.5, this.b.sum("A"), 0);
   }

   @Test
   public void min()
   {
      assertEquals(Double.MAX_VALUE, this.empty.min("A"), 0);
      assertEquals(1.0, this.a.min("A"), 0);
      assertEquals(-6.5, this.b.min("A"), 0);
   }

   @Test
   public void max()
   {
      assertEquals(-Double.MAX_VALUE, this.empty.max("A"), 0);
      assertEquals(3.75, this.a.max("A"), 0);
      assertEquals(8.5, this.b.max("A"), 0);
   }

   @Test
   public void average()
   {
      assertEquals(Double.NaN, this.empty.average("A"), 0);
      assertEquals(7.25 / 3, this.a.average("A"), 0);
      assertEquals(-0.125, this.b.average("A"), 0);
   }
}
