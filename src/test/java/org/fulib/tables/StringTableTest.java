package org.fulib.tables;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringTableTest
{
   private final StringTable empty = new StringTable();
   private final StringTable abc = new StringTable("A", "B", "C");

   @Test
   public void join()
   {
      assertEquals("", this.empty.join(""));
      assertEquals("", this.empty.join(","));

      assertEquals("ABC", this.abc.join(""));
      assertEquals("A,B,C", this.abc.join(","));
      assertEquals("A, B, C", this.abc.join(", "));
   }

   @Test(expected = NullPointerException.class)
   public void joinThrowsNPEForEmpty()
   {
      this.empty.join(null);
   }

   @Test(expected = NullPointerException.class)
   public void joinThrowsNPEForNonEmpty()
   {
      this.abc.join(null);
   }
}
