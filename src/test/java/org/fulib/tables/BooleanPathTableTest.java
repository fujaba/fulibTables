package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanPathTableTest
{
   private final PathTable empty = new PathTable("A");
   private final PathTable none = new PathTable("A", false, false, false);
   private final PathTable one = new PathTable("A", false, true, false);
   private final PathTable two = new PathTable("A", true, false, true);
   private final PathTable all = new PathTable("A", true, true, true);

   @Test
   public void all()
   {
      assertTrue(this.empty.all("A"));
      assertFalse(this.none.all("A"));
      assertFalse(this.one.all("A"));
      assertFalse(this.two.all("A"));
      assertTrue(this.all.all("A"));
   }

   @Test
   public void any()
   {
      assertFalse(this.empty.any("A"));
      assertFalse(this.none.any("A"));
      assertTrue(this.one.any("A"));
      assertTrue(this.two.any("A"));
      assertTrue(this.all.any("A"));
   }

   @Test
   public void none()
   {
      assertTrue(this.empty.none("A"));
      assertTrue(this.none.none("A"));
      assertFalse(this.one.none("A"));
      assertFalse(this.two.none("A"));
      assertFalse(this.all.none("A"));
   }
}
