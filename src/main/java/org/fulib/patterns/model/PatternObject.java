package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.Collections;

public class PatternObject
{
   // =============== Constants ===============

   public static final String PROPERTY_pattern = "pattern";
   public static final String PROPERTY_roles = "roles";
   public static final String PROPERTY_attributeConstraints = "attributeConstraints";
   public static final String PROPERTY_matchConstraints = "matchConstraints";

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
      // =============== Methods ===============
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
      // =============== Methods ===============
      @Override
      public boolean add(MatchConstraint value)
      {
         throw new UnsupportedOperationException("No direct add! Use xy.withMatchConstraints(obj)");
      }
   };

   // =============== Fields ===============

   private Pattern pattern;
   private List<RoleObject> roles;
   private List<AttributeConstraint> attributeConstraints;
   private List<MatchConstraint> matchConstraints;

   protected PropertyChangeSupport listeners;
   public static final String PROPERTY_name = "name";

   private String name;

   // =============== Properties ===============

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public PatternObject setPattern(Pattern value)
   {
      if (this.pattern == value)
      {
         return this;
      }

      final Pattern oldValue = this.pattern;
      if (this.pattern != null)
      {
         this.pattern = null;
         oldValue.withoutObjects(this);
      }
      this.pattern = value;
      if (value != null)
      {
         value.withObjects(this);
      }
      this.firePropertyChange(PROPERTY_pattern, oldValue, value);
      return this;
   }

   public List<RoleObject> getRoles()
   {
      return this.roles != null ? Collections.unmodifiableList(this.roles) : Collections.emptyList();
   }

   public PatternObject withRoles(Object... value)
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
               ((RoleObject) item).setObject(this);
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

   public PatternObject withoutRoles(Object... value)
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
               ((RoleObject) item).setObject(null);
               this.firePropertyChange("roles", item, null);
            }
         }
      }
      return this;
   }

   public List<AttributeConstraint> getAttributeConstraints()
   {
      return this.attributeConstraints != null ? Collections.unmodifiableList(this.attributeConstraints) : Collections.emptyList();
   }

   public PatternObject withAttributeConstraints(Object... value)
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
               ((AttributeConstraint) item).setObject(this);
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

   public PatternObject withoutAttributeConstraints(Object... value)
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
               ((AttributeConstraint) item).setObject(null);
               this.firePropertyChange("attributeConstraints", item, null);
            }
         }
      }
      return this;
   }

   public List<MatchConstraint> getMatchConstraints()
   {
      return this.matchConstraints != null ? Collections.unmodifiableList(this.matchConstraints) : Collections.emptyList();
   }

   public PatternObject withMatchConstraints(Object... value)
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
               ((MatchConstraint) item).withObjects(this);
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

   public PatternObject withoutMatchConstraints(Object... value)
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
               ((MatchConstraint) item).withoutObjects(this);
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
      this.setPattern(null);
      this.withoutRoles(new ArrayList<>(this.getRoles()));
      this.withoutAttributeConstraints(new ArrayList<>(this.getAttributeConstraints()));
      this.withoutMatchConstraints(new ArrayList<>(this.getMatchConstraints()));
   }

   @Override
   public String toString() // no fulib
   {
      return this.name;
   }

   public PatternObject withRoles(RoleObject value)
   {
      if (this.roles == null)
      {
         this.roles = new ArrayList<>();
      }
      if (!this.roles.contains(value))
      {
         this.roles.add(value);
         value.setObject(this);
         this.firePropertyChange(PROPERTY_roles, null, value);
      }
      return this;
   }

   public PatternObject withRoles(RoleObject... value)
   {
      for (final RoleObject item : value)
      {
         this.withRoles(item);
      }
      return this;
   }

   public PatternObject withRoles(Collection<? extends RoleObject> value)
   {
      for (final RoleObject item : value)
      {
         this.withRoles(item);
      }
      return this;
   }

   public PatternObject withoutRoles(RoleObject value)
   {
      if (this.roles != null && this.roles.remove(value))
      {
         value.setObject(null);
         this.firePropertyChange(PROPERTY_roles, value, null);
      }
      return this;
   }

   public PatternObject withoutRoles(RoleObject... value)
   {
      for (final RoleObject item : value)
      {
         this.withoutRoles(item);
      }
      return this;
   }

   public PatternObject withoutRoles(Collection<? extends RoleObject> value)
   {
      for (final RoleObject item : value)
      {
         this.withoutRoles(item);
      }
      return this;
   }

   public PatternObject withAttributeConstraints(AttributeConstraint value)
   {
      if (this.attributeConstraints == null)
      {
         this.attributeConstraints = new ArrayList<>();
      }
      if (!this.attributeConstraints.contains(value))
      {
         this.attributeConstraints.add(value);
         value.setObject(this);
         this.firePropertyChange(PROPERTY_attributeConstraints, null, value);
      }
      return this;
   }

   public PatternObject withAttributeConstraints(AttributeConstraint... value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withAttributeConstraints(item);
      }
      return this;
   }

   public PatternObject withAttributeConstraints(Collection<? extends AttributeConstraint> value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withAttributeConstraints(item);
      }
      return this;
   }

   public PatternObject withoutAttributeConstraints(AttributeConstraint value)
   {
      if (this.attributeConstraints != null && this.attributeConstraints.remove(value))
      {
         value.setObject(null);
         this.firePropertyChange(PROPERTY_attributeConstraints, value, null);
      }
      return this;
   }

   public PatternObject withoutAttributeConstraints(AttributeConstraint... value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withoutAttributeConstraints(item);
      }
      return this;
   }

   public PatternObject withoutAttributeConstraints(Collection<? extends AttributeConstraint> value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withoutAttributeConstraints(item);
      }
      return this;
   }

   public PatternObject withMatchConstraints(MatchConstraint value)
   {
      if (this.matchConstraints == null)
      {
         this.matchConstraints = new ArrayList<>();
      }
      if (!this.matchConstraints.contains(value))
      {
         this.matchConstraints.add(value);
         value.withObjects(this);
         this.firePropertyChange(PROPERTY_matchConstraints, null, value);
      }
      return this;
   }

   public PatternObject withMatchConstraints(MatchConstraint... value)
   {
      for (final MatchConstraint item : value)
      {
         this.withMatchConstraints(item);
      }
      return this;
   }

   public PatternObject withMatchConstraints(Collection<? extends MatchConstraint> value)
   {
      for (final MatchConstraint item : value)
      {
         this.withMatchConstraints(item);
      }
      return this;
   }

   public PatternObject withoutMatchConstraints(MatchConstraint value)
   {
      if (this.matchConstraints != null && this.matchConstraints.remove(value))
      {
         value.withoutObjects(this);
         this.firePropertyChange(PROPERTY_matchConstraints, value, null);
      }
      return this;
   }

   public PatternObject withoutMatchConstraints(MatchConstraint... value)
   {
      for (final MatchConstraint item : value)
      {
         this.withoutMatchConstraints(item);
      }
      return this;
   }

   public PatternObject withoutMatchConstraints(Collection<? extends MatchConstraint> value)
   {
      for (final MatchConstraint item : value)
      {
         this.withoutMatchConstraints(item);
      }
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public PatternObject setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_name, oldValue, value);
      return this;
   }
}
