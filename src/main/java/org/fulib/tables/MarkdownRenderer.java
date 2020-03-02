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
   public void render(Table<?> table, Appendable builder) throws IOException
   {
      for (String key : table.columnMap.keySet())
      {
         builder.append("| ").append(key).append(" \t");
      }
      builder.append("|\n");

      for (String ignored : table.columnMap.keySet())
      {
         builder.append("| --- ");
      }
      builder.append("|\n");

      for (List<Object> row : table.table)
      {
         for (Object cell : row)
         {
            builder.append("| ").append(String.valueOf(cell)).append(" \t");
         }
         builder.append("|\n");
      }
   }
}
