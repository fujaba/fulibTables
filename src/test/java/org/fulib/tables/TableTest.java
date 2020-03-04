package org.fulib.tables;

import org.fulib.tools.CodeFragments;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TableTest
{
   private static CodeFragments fragments = new CodeFragments();

   @Test
   public void expand()
   {
      // start_code_fragment: TableTest.expand
      Table<Integer> a = new Table<>("A", 1, 2, 3);
      Table<Integer> b = a.expand("B", i -> i * 2);
      // end_code_fragment:

      fragments.addFragment("TableTest.expand.b", new HtmlRenderer().setCaption("b").render(b));

      assertEquals("B", b.getColumnName());
      assertEquals(Arrays.asList(2, 4, 6), b.toList());
   }

   @Test
   public void expandAll()
   {
      // start_code_fragment: TableTest.expandAll
      Table<Integer> a = new Table<>("A", 1, 2);
      Table<Integer> b = a.expandAll("B", i -> Arrays.asList(i + 10, i + 20));
      // end_code_fragment:

      fragments.addFragment("TableTest.expandAll.b", new HtmlRenderer().setCaption("b").render(b));

      assertEquals("B", b.getColumnName());
      assertEquals(Arrays.asList(11, 21, 12, 22), b.toList());
   }

   @Test
   public void derive()
   {
      // start_code_fragment: TableTest.derive
      Table<Integer> a = new Table<>("A", 1, 2);
      Table<Integer> b = a.expand("B", i -> i * 10);
      Table<Integer> c = b.derive("C", row -> (int) row.get("A") + (int) row.get("B"));
      // end_code_fragment:

      fragments.addFragment("TableTest.derive.c", new HtmlRenderer().setCaption("c").render(b));

      assertEquals("C", c.getColumnName());
      assertEquals(Arrays.asList(11, 22), c.toList());
   }

   @Test
   public void selectColumns()
   {
      // start_code_fragment: TableTest.selectColumns.initial
      Table<String> names = new StringTable("Alice", "Bob", "Charlie", "alice");
      Table<String> uppercase = names.expand("uppercase", String::toUpperCase);
      Table<String> lowercase = names.expand("lowercase", String::toLowerCase);
      // end_code_fragment:

      fragments.addFragment("TableTest.selectColumns.before", new HtmlRenderer().setCaption("before").render(names));

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE", "ALICE"), uppercase.toList());

      // start_code_fragment: TableTest.selectColumns.select
      names.selectColumns("uppercase", "lowercase");
      // end_code_fragment:

      fragments.addFragment("TableTest.selectColumns.after", new HtmlRenderer().setCaption("after").render(names));

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE"), uppercase.toList());

      try
      {
         // start_code_fragment: TableTest.selectColumns.exception
         names.toList(); // throws IllegalStateException
         // end_code_fragment:
         fail("did not throw IllegalStateException");
      }
      catch (IllegalStateException expected)
      {
      }
   }

   @Test
   public void dropColumns()
   {
      // start_code_fragment: TableTest.dropColumns.initial
      Table<String> names = new StringTable("Alice", "Bob", "Charlie", "alice");
      Table<String> uppercase = names.expand("uppercase", String::toUpperCase);
      Table<String> lowercase = names.expand("lowercase", String::toLowerCase);
      // end_code_fragment:

      fragments.addFragment("TableTest.dropColumns.before", new HtmlRenderer().setCaption("before").render(names));

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE", "ALICE"), uppercase.toList());

      // start_code_fragment: TableTest.dropColumns.select
      names.dropColumns(names.getColumnName());
      // end_code_fragment:

      fragments.addFragment("TableTest.dropColumns.after", new HtmlRenderer().setCaption("after").render(names));

      assertEquals(Arrays.asList("ALICE", "BOB", "CHARLIE"), uppercase.toList());

      try
      {
         // start_code_fragment: TableTest.dropColumns.exception
         names.toList(); // throws IllegalStateException
         // end_code_fragment:
         fail("did not throw IllegalStateException");
      }
      catch (IllegalStateException expected)
      {
      }
   }

   @Test
   public void filter()
   {
      // start_code_fragment: TableTest.filter
      final Table<Integer> numbers = new Table<>("A", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
      numbers.filter(i -> i % 2 == 0);
      // end_code_fragment:

      assertEquals(Arrays.asList(2, 4, 6, 8, 10), numbers.toList());

      fragments.addFragment("TableTest.filter.result", new HtmlRenderer().setCaption("result").render(numbers));
   }

   @Test
   public void filterRows()
   {
      // start_code_fragment: TableTest.filterRows.initial
      Table<Integer> a = new Table<>("A", 1, 2);
      Table<Integer> b = a.expandAll("B", i -> Arrays.asList(1, 2));
      // end_code_fragment:

      fragments.addFragment("TableTest.filterRows.before", new HtmlRenderer().setCaption("before").render(b));

      // start_code_fragment: TableTest.filterRows.action
      a.filterRows(row -> (int) row.get("A") != (int) row.get("B"));
      // end_code_fragment:

      fragments.addFragment("TableTest.filterRows.after", new HtmlRenderer().setCaption("after").render(b));

      assertEquals(Arrays.asList(1, 2), a.toList());
      assertEquals(Arrays.asList(2, 1), b.toList());
   }

   @AfterClass
   public static void updateFragments()
   {
      fragments.update("src/main/java/org/fulib/tables/Table.java", "src/test/java/org/fulib/tables/TableTest.java");
   }
}
