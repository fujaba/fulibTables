package org.fulib.tables;

public class BooleanTable extends AbstractTable<Boolean>
{
   // =============== Constructors ===============

   public BooleanTable(Boolean... start)
   {
      super(start);
   }

   protected BooleanTable(AbstractTable<?> base)
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
