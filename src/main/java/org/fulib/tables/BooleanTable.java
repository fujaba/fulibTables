package org.fulib.tables;

/**
 * A specialization of the {@link Table} class for {@link Boolean} cell values.
 *
 * @since 1.2
 */
public class BooleanTable extends Table<Boolean>
{
   // =============== Constructors ===============

   public BooleanTable(Boolean... start)
   {
      super(start);
   }

   public BooleanTable(String columnName, Boolean... start)
   {
      super(start);
   }

   BooleanTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   /**
    * @return {@code true} if all cell values in the column pointed to by this table are {@code true},
    * or this table has no rows
    */
   public boolean all()
   {
      return this.stream().allMatch(Boolean::booleanValue);
   }

   /**
    * @return {@code true} if at least one cell value in the column pointed to by this table is {@code true}
    */
   public boolean any()
   {
      return this.stream().anyMatch(Boolean::booleanValue);
   }

   /**
    * @return {@code true} if none of the cell values in the column pointed to by this table are {@code true},
    * or this table has no rows
    */
   public boolean none()
   {
      return this.stream().noneMatch(Boolean::booleanValue);
   }
}
