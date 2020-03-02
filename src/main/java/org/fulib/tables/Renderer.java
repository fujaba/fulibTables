package org.fulib.tables;

import java.io.IOException;

/**
 * Instances of this interface can render {@link Table} to strings or {@link Appendable}s.
 *
 * @since 1.2
 */
public interface Renderer
{
   /**
    * Renders the given table as a string.
    * <p>
    * Defaults to {@link #render(Table, Appendable)}.
    * This method should not be overriden.
    *
    * @param table
    *    the table
    *
    * @return the rendered string
    */
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

   /**
    * Renders the given table into the given appendable.
    *
    * @param table
    *    the table to render
    * @param out
    *    the appendable into which should be rendered
    *
    * @throws IOException
    *    if it occurs when {@linkplain Appendable#append(CharSequence) appending} to the appendable
    */
   void render(Table<?> table, Appendable out) throws IOException;
}
