package org.fulib.tables;

import java.io.IOException;

public interface Renderer
{
   default String render(Table<?> table)
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

   void render(Table<?> table, Appendable builder) throws IOException;
}
