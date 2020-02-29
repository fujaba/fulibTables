package org.fulib.tables;

import java.io.IOException;
import java.util.List;

public class MarkdownRenderer implements Renderer
{
   @Override
   public void render(Table<?> table, Appendable builder) throws IOException
   {
      builder.append("| ");
      for (String key : table.columnMap.keySet())
      {
         builder.append(key).append(" \t| ");
      }
      builder.append("\n| ");
      for (String ignored : table.columnMap.keySet())
      {
         builder.append(" --- \t| ");
      }
      builder.append("\n");
      for (List<Object> row : table.table)
      {
         builder.append("| ");
         for (Object cell : row)
         {
            builder.append(String.valueOf(cell)).append(" \t| ");
         }
         builder.append("\n");
      }
      builder.append("\n");
   }
}
