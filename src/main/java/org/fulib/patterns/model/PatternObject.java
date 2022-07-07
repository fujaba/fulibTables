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

   /** @deprecated since 1.5; use {@link #PROPERTY_NAME} instead */
   @Deprecated
   public static final String PROPERTY_name = "name";
   /** @deprecated since 1.5; use {@link #PROPERTY_PATTERN} instead */
   @Deprecated
   public static final String PROPERTY_pattern = "pattern";
   /** @deprecated since 1.5; use {@link #PROPERTY_ROLES} instead */
   @Deprecated
   public static final String PROPERTY_roles = "roles";
   /** @deprecated since 1.5; use {@link #PROPERTY_ATTRIBUTE_CONSTRAINTS} instead */
   @Deprecated
   public static final String PROPERTY_attributeConstraints = "attributeConstraints";
   /** @deprecated since 1.5; use {@link #PROPERTY_MATCH_CONSTRAINTS} instead */
   @Deprecated
   public static final String PROPERTY_matchConstraints = "matchConstraints";

   /** @since 1.5 */
   public static final String PROPERTY_NAME = "name";
   /** @since 1.5 */
   public static final String PROPERTY_ATTRIBUTE_CONSTRAINTS = "attributeConstraints";
   /** @since 1.5 */
   public static final String PROPERTY_ROLES = "roles";
   /** @since 1.5 */
   public static final String PROPERTY_PATTERN = "pattern";
   /** @since 1.5 */
   public static final String PROPERTY_MATCH_CONSTRAINTS = "matchConstraints";

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
   private ArrayList<RoleObject> roles /* no fulib */;
   private ArrayList<AttributeConstraint> attributeConstraints /* no fulib */;
   private ArrayList<MatchConstraint> matchConstraints /* no fulib */;

   protected PropertyChangeSupport listeners;

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
      this.firePropertyChange(PROPERTY_PATTERN, oldValue, value);
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
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public ArrayList<RoleObject> getRoles() // no fulib
   {
      return this.roles != null ? this.roles : EMPTY_roles;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withRoles((RoleObject) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
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
         this.firePropertyChange(PROPERTY_ROLES, null, value);
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

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutRoles((RoleObject) item);
         }
      }
      return this;
   }

   public PatternObject withoutRoles(RoleObject value)
   {
      if (this.roles != null && this.roles.remove(value))
      {
         value.setObject(null);
         this.firePropertyChange(PROPERTY_ROLES, value, null);
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

   public ArrayList<AttributeConstraint> getAttributeConstraints() // no fulib
   {
      return this.attributeConstraints != null ? this.attributeConstraints : EMPTY_attributeConstraints;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withAttributeConstraints((AttributeConstraint) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
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
         this.firePropertyChange(PROPERTY_ATTRIBUTE_CONSTRAINTS, null, value);
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

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutAttributeConstraints((AttributeConstraint) item);
         }
      }
      return this;
   }

   public PatternObject withoutAttributeConstraints(AttributeConstraint value)
   {
      if (this.attributeConstraints != null && this.attributeConstraints.remove(value))
      {
         value.setObject(null);
         this.firePropertyChange(PROPERTY_ATTRIBUTE_CONSTRAINTS, value, null);
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

   public ArrayList<MatchConstraint> getMatchConstraints() // no fulib
   {
      return this.matchConstraints != null ? this.matchConstraints : EMPTY_matchConstraints;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withMatchConstraints((MatchConstraint) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutMatchConstraints((MatchConstraint) item);
         }
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
         this.firePropertyChange(PROPERTY_MATCH_CONSTRAINTS, null, value);
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
         this.firePropertyChange(PROPERTY_MATCH_CONSTRAINTS, value, null);
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

   // =============== Methods ===============

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

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
      this.withoutMatchConstraints(new ArrayList<>(this.getMatchConstraints()));
      this.withoutAttributeConstraints(new ArrayList<>(this.getAttributeConstraints()));
      this.withoutRoles(new ArrayList<>(this.getRoles()));
      this.setPattern(null);
   }

   @Override
   public String toString() // no fulib
   {
      return this.name;
   }
}
