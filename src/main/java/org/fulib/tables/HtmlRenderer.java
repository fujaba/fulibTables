package org.fulib.tables;

import java.io.IOException;
import java.util.List;

/**
 * Allows rendering {@link Table} instances as HTML {@code <table>} elements.
 *
 * @since 1.2
 */
public class HtmlRenderer implements Renderer
{
   /**
    * The default value for {@link #setIndent(String)} (4 spaces)
    */
   public static final String DEFAULT_INDENT = "    ";

   private String caption;
   private String indent = DEFAULT_INDENT;

   /**
    * @return the caption, or {@code null} if unset
    *
    * @see #setCaption(String)
    */
   public String getCaption()
   {
      return this.caption;
   }

   /**
    * @param caption
    *    the caption to use within {@code <caption>...</caption>}, which is required in JavaDoc;
    *    can be {@code null} to omit the element
    *
    * @return this instance, to allow method chaining
    */
   public HtmlRenderer setCaption(String caption)
   {
      this.caption = caption;
      return this;
   }

   /**
    * @return the indent string
    *
    * @see #setIndent(String)
    */
   public String getIndent()
   {
      return this.indent;
   }

   /**
    * @param indent
    *    the string to indent nested tags; defaults to {@link #DEFAULT_INDENT}
    *
    * @return this instance, to allow method chaining
    */
   public HtmlRenderer setIndent(String indent)
   {
      this.indent = indent;
      return this;
   }

   @Override
   public void render(Table<?> table, Appendable builder) throws IOException
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
