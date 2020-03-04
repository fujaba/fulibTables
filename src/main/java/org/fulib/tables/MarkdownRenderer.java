package org.fulib.tables;

import java.io.IOException;
import java.util.List;

/**
 * Allows rendering {@link Table} instances as Markdown tables.
 *
 * @since 1.2
 */
public class MarkdownRenderer implements Renderer
{
   @Override
   public void render(Table<?> table, Appendable out) throws IOException
   {
      for (String key : table.columns)
      {
         out.append("| ").append(key).append(" \t");
      }
      out.append("|\n");

      for (String ignored : table.columns)
      {
         out.append("| --- ");
      }
      out.append("|\n");

      for (List<Object> row : table.table)
      {
         for (Object cell : row)
         {
            out.append("| ").append(String.valueOf(cell)).append(" \t");
         }
         out.append("|\n");
      }
   }
}
