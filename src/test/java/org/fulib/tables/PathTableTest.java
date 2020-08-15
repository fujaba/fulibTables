package org.fulib.tables;

import org.fulib.tools.CodeFragments;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PathTableTest
{
   private static CodeFragments fragments = new CodeFragments();

   @Test
   public void expand()
   {
      // start_code_fragment: TableTest.expand
      PathTable a = new PathTable("A", 1, 2, 3);
      a.expand("A", i -> ((int) i) * 2, "B");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.expand.b", "<pre>" + a.toString() + "</pre>");

      assertEquals(Arrays.asList(2, 4, 6), a.toList("B"));
   }

   @Test
   public void expandAll()
   {
      // start_code_fragment: TableTest.expandAll
      PathTable a = new PathTable("A", 1, 2);
      a.expandAll("A", i -> Arrays.asList(((int) i) + 10, ((int) i) + 20), "B");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.expandAll.b", "<pre>" + a.toString() + "</pre>");

      assertEquals(Arrays.asList(11, 21, 12, 22), a.toList("B"));
   }

   @Test
   public void derive()
   {
      // start_code_fragment: PathTableTest.derive
      PathTable a = new PathTable("A", 1, 2);
      a.expand("A", i -> ((int) i) * 10, "B");
      a.derive("C", row -> (int) row.get("A") + (int) row.get("B"));
      // end_code_fragment:

      fragments.addFragment("PathTableTest.derive.c", "<pre>" + a.toString() + "</pre>");

      assertEquals(Arrays.asList(11, 22), a.toList("C"));
   }

   @Test
   public void deriveAll()
   {
      // start_code_fragment: PathTableTest.deriveAll
      PathTable a = new PathTable("A", 1, 2);
      a.expand("A", i -> ((int) i) * 10, "B");
      a.deriveAll("C", row -> {
         int a1 = (int) row.get("A");
         int b1 = (int) row.get("B");
         return Arrays.asList(a1 + b1, a1 * b1);
      });
      // end_code_fragment:

      fragments.addFragment("PathTableTest.deriveAll.c", "<pre>" + a.toString() + "</pre>");

      assertEquals(Arrays.asList(11, 10, 22, 40), a.toList("C"));
   }

   @Test
   public void selectColumns()
   {
      // start_code_fragment: TableTest.selectColumns.initial
      PathTable names = new PathTable("Name","Alice", "Bob", "Charlie", "alice");
      names.expand("Name", obj -> obj.toString().toUpperCase(), "uppercase");
      names.expand("Name", obj -> obj.toString().toLowerCase(), "lowercase");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.selectColumns.before", "<pre>" + names + "</pre>");

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE", "ALICE"), names.toList("uppercase"));

      // start_code_fragment: TableTest.selectColumns.select
      names.selectColumns("uppercase", "lowercase");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.selectColumns.after", "<pre>" + names + "</pre>");

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE"), names.toList("uppercase"));

      try
      {
         names.selectColumns("unknown");
         fail("did not throw IllegalArgumentException despite unknown column name");
      }
      catch (IllegalArgumentException expected)
      {
         try
         {
            names.toList("uppercase");
         }
         catch (IllegalStateException ex)
         {
            fail("modified columns before throwing exception");
         }
      }

      try
      {
         names.selectColumns("uppercase", "uppercase");
         fail("did not throw IllegalArgumentException despite duplicate name");
      }
      catch (IllegalArgumentException expected)
      {
         try
         {
            names.toList("lowercase");
         }
         catch (IllegalStateException ex)
         {
            fail("modified columns before throwing exception");
         }
      }
   }

   @Test
   public void dropColumns()
   {
      // start_code_fragment: PathTableTest.dropColumns.initial
      PathTable names = new PathTable("Name","Alice", "Bob", "Charlie", "alice");
      names.expand("Name", obj -> obj.toString().toUpperCase(), "uppercase");
      names.expand("Name", obj -> obj.toString().toLowerCase(), "lowercase");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.dropColumns.before", "<pre>" + names + "</pre>");

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE", "ALICE"), names.toList("uppercase"));

      // start_code_fragment: PathTableTest.dropColumns.select
      names.dropColumns("Name");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.dropColumns.after", "<pre>" + names + "</pre>");

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE"), names.toList("uppercase"));
   }

   @Test
   public void filter()
   {
      // start_code_fragment: PathTableTest.filter
      PathTable numbers = new PathTable("A", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
      numbers.filter("A", i -> ((int) i) % 2 == 0);
      // end_code_fragment:

      assertEquals(Arrays.asList(2, 4, 6, 8, 10), numbers.toList("A"));

      fragments.addFragment("PathTableTest.filter.result", "<pre>" + numbers + "</pre>");
   }

   @Test
   public void filterRows()
   {
      // start_code_fragment: PathTableTest.filterRows.initial
      PathTable a = new PathTable("A", 1, 2);
      a.expandAll("A", i -> Arrays.asList(1, 2), "B");
      // end_code_fragment:

      fragments.addFragment("PathTableTest.filterRows.before", "<pre>" + a + "</pre>");

      // start_code_fragment: PathTableTest.filterRows.action
      a.filterRows(row -> (int) row.get("A") != (int) row.get("B"));
      // end_code_fragment:

      fragments.addFragment("PathTableTest.filterRows.after", "<pre>" + a + "</pre>");

      assertEquals(Arrays.asList(1, 2), a.toList("A"));
      assertEquals(Arrays.asList(2, 1), a.toList("B"));
   }

   @AfterClass
   public static void updateFragments()
   {
      fragments.update("src/main/java/org/fulib/tables/Table.java", "src/test/java/org/fulib/tables/TableTest.java");
   }
}
