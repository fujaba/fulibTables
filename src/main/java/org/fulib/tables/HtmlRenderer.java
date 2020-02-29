package org.fulib.tables;

import java.io.IOException;

public class HtmlRenderer
{
   private static final String DEFAULT_INDENT = "    ";

   private String caption;
   private String indent = DEFAULT_INDENT;

   public String getCaption()
   {
      return this.caption;
   }

   public void setCaption(String caption)
   {
      this.caption = caption;
   }

   public String getIndent()
   {
      return this.indent;
   }

   public void setIndent(String indent)
   {
      this.indent = indent;
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
      // TODO
   }
}
