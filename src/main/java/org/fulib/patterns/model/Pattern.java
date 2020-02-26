package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Pattern
{
   // =============== Constants ===============

   public static final String PROPERTY_objects = "objects";
   public static final String PROPERTY_roles = "roles";
   public static final String PROPERTY_attributeConstraints = "attributeConstraints";
   public static final String PROPERTY_matchConstraints = "matchConstraints";

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public static final ArrayList<PatternObject> EMPTY_objects = new ArrayList<PatternObject>()
   {
      @Override
      public boolean add(PatternObject value)
      {
         throw new UnsupportedOperationException("No direct add! Use xy.withObjects(obj)");
      }
   };

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public static final ArrayList<RoleObject> EMPTY_roles = new ArrayList<RoleObject>()
   {
      @Override
      public boolean add(RoleObject value)
      {
         throw new UnsupportedOperationException("No direct add! Use xy.withRoles(obj)");
      }
   };

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public static final ArrayList<AttributeConstraint> EMPTY_attributeConstraints = new ArrayList<AttributeConstraint>()
   {
      @Override
      public boolean add(AttributeConstraint value)
      {
         throw new UnsupportedOperationException("No direct add! Use xy.withAttributeConstraints(obj)");
      }
   };

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public static final ArrayList<MatchConstraint> EMPTY_matchConstraints = new ArrayList<MatchConstraint>()
   {
      @Override
      public boolean add(MatchConstraint value)
      {
         throw new UnsupportedOperationException("No direct add! Use xy.withMatchConstraints(obj)");
      }
   };

   // =============== Fields ===============

   private ArrayList<PatternObject> objects;
   private ArrayList<RoleObject> roles;
   private ArrayList<AttributeConstraint> attributeConstraints;
   private ArrayList<MatchConstraint> matchConstraints;

   protected PropertyChangeSupport listeners;

   // =============== Properties ===============

   public ArrayList<PatternObject> getObjects()
   {
      if (this.objects == null)
      {
         return EMPTY_objects;
      }

      return this.objects;
   }

   /**
    * @param name
    *    the name of the pattern object to find
    *
    * @return the pattern object with the given name, or {@code null} if not found
    *
    * @deprecated since 1.2; use {@link #getObject(String)} instead
    */
   @Deprecated
   public PatternObject getObjects(String name)
   {
      return this.getObject(name);
   }

   /**
    * @param name
    *    the name of the pattern object to find
    *
    * @return the pattern object with the given name, or {@code null} if not found
    *
    * @since 1.2
    */
   public PatternObject getObject(String name)
   {
      for (PatternObject object : this.getObjects())
      {
         if (Objects.equals(name, object.getName()))
         {
            return object;
         }
      }

      return null;
   }

   public Pattern withObjects(Object... value)
   {
      if (value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withObjects(i);
            }
         }
         else if (item instanceof PatternObject)
         {
            if (this.objects == null)
            {
               this.objects = new ArrayList<>();
            }
            if (!this.objects.contains(item))
            {
               this.objects.add((PatternObject) item);
               ((PatternObject) item).setPattern(this);
               this.firePropertyChange("objects", null, item);
            }
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withoutObjects(Object... value)
   {
      if (this.objects == null || value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withoutObjects(i);
            }
         }
         else if (item instanceof PatternObject)
         {
            if (this.objects.contains(item))
            {
               this.objects.remove(item);
               ((PatternObject) item).setPattern(null);
               this.firePropertyChange("objects", item, null);
            }
         }
      }
      return this;
   }

   public ArrayList<RoleObject> getRoles()
   {
      if (this.roles == null)
      {
         return EMPTY_roles;
      }

      return this.roles;
   }

   public Pattern withRoles(Object... value)
   {
      if (value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withRoles(i);
            }
         }
         else if (item instanceof RoleObject)
         {
            if (this.roles == null)
            {
               this.roles = new ArrayList<>();
            }
            if (!this.roles.contains(item))
            {
               this.roles.add((RoleObject) item);
               ((RoleObject) item).setPattern(this);
               this.firePropertyChange("roles", null, item);
            }
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withoutRoles(Object... value)
   {
      if (this.roles == null || value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withoutRoles(i);
            }
         }
         else if (item instanceof RoleObject)
         {
            if (this.roles.contains(item))
            {
               this.roles.remove(item);
               ((RoleObject) item).setPattern(null);
               this.firePropertyChange("roles", item, null);
            }
         }
      }
      return this;
   }

   public ArrayList<AttributeConstraint> getAttributeConstraints()
   {
      if (this.attributeConstraints == null)
      {
         return EMPTY_attributeConstraints;
      }

      return this.attributeConstraints;
   }

   public Pattern withAttributeConstraints(Object... value)
   {
      if (value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withAttributeConstraints(i);
            }
         }
         else if (item instanceof AttributeConstraint)
         {
            if (this.attributeConstraints == null)
            {
               this.attributeConstraints = new ArrayList<>();
            }
            if (!this.attributeConstraints.contains(item))
            {
               this.attributeConstraints.add((AttributeConstraint) item);
               ((AttributeConstraint) item).setPattern(this);
               this.firePropertyChange("attributeConstraints", null, item);
            }
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withoutAttributeConstraints(Object... value)
   {
      if (this.attributeConstraints == null || value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withoutAttributeConstraints(i);
            }
         }
         else if (item instanceof AttributeConstraint)
         {
            if (this.attributeConstraints.contains(item))
            {
               this.attributeConstraints.remove(item);
               ((AttributeConstraint) item).setPattern(null);
               this.firePropertyChange("attributeConstraints", item, null);
            }
         }
      }
      return this;
   }

   public ArrayList<MatchConstraint> getMatchConstraints()
   {
      if (this.matchConstraints == null)
      {
         return EMPTY_matchConstraints;
      }

      return this.matchConstraints;
   }

   public Pattern withMatchConstraints(Object... value)
   {
      if (value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withMatchConstraints(i);
            }
         }
         else if (item instanceof MatchConstraint)
         {
            if (this.matchConstraints == null)
            {
               this.matchConstraints = new ArrayList<>();
            }
            if (!this.matchConstraints.contains(item))
            {
               this.matchConstraints.add((MatchConstraint) item);
               ((MatchConstraint) item).setPattern(this);
               this.firePropertyChange("matchConstraints", null, item);
            }
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withoutMatchConstraints(Object... value)
   {
      if (this.matchConstraints == null || value == null)
      {
         return this;
      }
      for (Object item : value)
      {
         if (item == null)
         {
            continue;
         }
         if (item instanceof Collection)
         {
            for (Object i : (Collection<?>) item)
            {
               this.withoutMatchConstraints(i);
            }
         }
         else if (item instanceof MatchConstraint)
         {
            if (this.matchConstraints.contains(item))
            {
               this.matchConstraints.remove(item);
               ((MatchConstraint) item).setPattern(null);
               this.firePropertyChange("matchConstraints", item, null);
            }
         }
      }
      return this;
   }

   // =============== Methods ===============

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      this.listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (this.listeners != null)
      {
         this.listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   public void removeYou()
   {
      this.withoutObjects(this.getObjects().clone());

      this.withoutRoles(this.getRoles().clone());

      this.withoutAttributeConstraints(this.getAttributeConstraints().clone());

      this.withoutMatchConstraints(this.getMatchConstraints().clone());
   }
}
