package org.fulib.tables;

import java.util.stream.Collectors;

/**
 * A specialization of the {@link Table} class for {@link String} cell values.
 */
public class StringTable extends PrimitiveTable<String>
{
   // =============== Constructors ===============

   public StringTable(String... start)
   {
      super(start);
   }

   StringTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   /**
    * Joins the cell values of the column this table points to with the given separator between items.
    *
    * @param separator
    *    the separator, can be empty but must not be {@code null}
    *
    * @return a string containing the values joined with the separator in between
    *
    * @throws NullPointerException
    *    if the separator is {@code null}
    */
   public String join(String separator)
   {
      return this.stream().collect(Collectors.joining(separator));
   }
}
