package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class intPathTableTest
{
   private final PathTable empty = new PathTable("A");
   private final PathTable a = new PathTable("A",1, 2, 3);
   private final PathTable b = new PathTable("A", -5, 2, -7, 9);

   @Test
   public void sum()
   {
      assertTrue(0 == this.empty.sum("A"));
      assertTrue(6 == this.a.sum("A"));
      assertTrue(-1 == this.b.sum("A"));
   }

   @Test
   public void min()
   {
      assertTrue(-7 == this.b.min("A"));
   }

   @Test
   public void max()
   {
      assertTrue(9 == this.b.max("A"));
   }

   @Test
   public void average()
   {
      assertEquals(2.0, this.a.average("A"), 0);
   }
}
