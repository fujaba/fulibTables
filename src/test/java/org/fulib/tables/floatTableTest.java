package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class floatTableTest
{
   private final floatTable empty = new floatTable();
   private final floatTable a = new floatTable(1.0f, 2.5f, 3.75f);
   private final floatTable b = new floatTable(-4.8f, 2.3f, -6.5f, 8.5f);

   @Test
   public void sum()
   {
      assertEquals(0f, this.empty.sum(), 0);
      assertEquals(7.25f, this.a.sum(), 1e-6f);
      assertEquals(-0.5f, this.b.sum(), 1e-6f);
   }

   @Test
   public void min()
   {
      assertEquals(Float.MAX_VALUE, this.empty.min(), 0);
      assertEquals(1.0f, this.a.min(), 0);
      assertEquals(-6.5f, this.b.min(), 0);
   }

   @Test
   public void max()
   {
      assertEquals(-Float.MAX_VALUE, this.empty.max(), 0);
      assertEquals(3.75f, this.a.max(), 0);
      assertEquals(8.5f, this.b.max(), 0);
   }

   @Test
   public void average()
   {
      assertEquals(Float.NaN, this.empty.average(), 0);
      assertEquals(7.25f / 3, this.a.average(), 1e-6f);
      assertEquals(-0.125f, this.b.average(), 1e-6f);
   }
}
