package org.fulib.tables;

import java.io.IOException;
import java.util.List;

public class HtmlRenderer
{
   private static final String DEFAULT_INDENT = "    ";

   private String caption;
   private String indent = DEFAULT_INDENT;

   public String getCaption()
   {
      return this.caption;
   }

   public HtmlRenderer setCaption(String caption)
   {
      this.caption = caption;
      return this;
   }

   public String getIndent()
   {
      return this.indent;
   }

   public HtmlRenderer setIndent(String indent)
   {
      this.indent = indent;
      return this;
   }

   public String render(Table<?> table)
   {
      final StringBuilder builder = new StringBuilder();
      try
      {
         this.render(table, builder);
      }
      catch (IOException ignored)
      {
      }
      return builder.toString();
   }

   private void render(Table<?> table, Appendable builder) throws IOException
   {
      final String i = this.indent;

      builder.append("<table>\n");
      if (this.caption != null)
      {
         builder.append(i).append("<caption>\n");
         builder.append(i).append(i).append(this.caption).append('\n');
         builder.append(i).append("</caption>\n");
      }

      builder.append(i).append("<tr>\n");
      for (String column : table.columnMap.keySet())
      {
         builder.append(i).append(i).append("<th>").append(column).append("</th>\n");
      }
      builder.append(i).append("</tr>\n");

      for (List<?> row : table.table)
      {
         builder.append(i).append("<tr>\n");
         for (Object cell : row)
         {
            builder.append(i).append(i).append("<td>").append(String.valueOf(cell)).append("</td>\n");
         }
         builder.append(i).append("</tr>\n");
      }

      builder.append("</table>\n");
   }
}
