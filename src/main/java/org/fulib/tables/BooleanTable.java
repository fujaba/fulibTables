package org.fulib.tables;

/**
 * @since 1.2
 */
public class BooleanTable extends Table<Boolean>
{
   // =============== Constructors ===============

   public BooleanTable(Boolean... start)
   {
      super(start);
   }

   protected BooleanTable(Table<?> base)
   {
      super(base);
   }

   // =============== Methods ===============

   public boolean all()
   {
      return this.stream().allMatch(Boolean::booleanValue);
   }

   public boolean any()
   {
      return this.stream().anyMatch(Boolean::booleanValue);
   }

   public boolean none()
   {
      return this.stream().noneMatch(Boolean::booleanValue);
   }
}
