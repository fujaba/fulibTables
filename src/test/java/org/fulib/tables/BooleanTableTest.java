package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanTableTest
{
   private final BooleanTable empty = new BooleanTable("empty");
   private final BooleanTable none = new BooleanTable("none", false, false, false);
   private final BooleanTable one = new BooleanTable("one", false, true, false);
   private final BooleanTable two = new BooleanTable("two", true, false, true);
   private final BooleanTable all = new BooleanTable("all", true, true, true);

   @Test
   public void all()
   {
      assertTrue(this.empty.all());
      assertFalse(this.none.all());
      assertFalse(this.one.all());
      assertFalse(this.two.all());
      assertTrue(this.all.all());
   }

   @Test
   public void any()
   {
      assertFalse(this.empty.any());
      assertFalse(this.none.any());
      assertTrue(this.one.any());
      assertTrue(this.two.any());
      assertTrue(this.all.any());
   }

   @Test
   public void none()
   {
      assertTrue(this.empty.none());
      assertTrue(this.none.none());
      assertFalse(this.one.none());
      assertFalse(this.two.none());
      assertFalse(this.all.none());
   }
}
