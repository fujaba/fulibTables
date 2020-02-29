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
   public void deriveColumn()
   {
      // start_code_fragment: TableTest.deriveColumn
      Table<Integer> a = new Table<>("A", 1, 2);
      Table<Integer> b = a.expand("B", i -> i * 10);
      Table<Integer> c = b.deriveColumn("C", row -> (int) row.get("A") + (int) row.get("B"));
      // end_code_fragment:

      fragments.addFragment("TableTest.deriveColumn.c", new HtmlRenderer().setCaption("c").render(b));

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

   @Test(expected = IllegalStateException.class)
   public void evictedColumnViaDrop()
   {
      final Table<String> table0 = new StringTable("a", "b", "c");
      final Table<String> table1 = table0.deriveColumn("uppercase", map -> {
         return map.get("A").toString().toUpperCase();
      });

      table1.dropColumns("A");

      table0.getColumnIndex();
   }

   @AfterClass
   public static void updateFragments()
   {
      fragments.update("src/main/java/org/fulib/tables/Table.java", "src/test/java/org/fulib/tables/TableTest.java");
   }
}
