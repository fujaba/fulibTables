package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.Collections;

public class Pattern
{
   // =============== Constants ===============

   /** @deprecated since 1.5; use {@link #PROPERTY_OBJECTS} instead */
   @Deprecated
   public static final String PROPERTY_objects = "objects";
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
   public static final String PROPERTY_MATCH_CONSTRAINTS = "matchConstraints";
   /** @since 1.5 */
   public static final String PROPERTY_ATTRIBUTE_CONSTRAINTS = "attributeConstraints";
   /** @since 1.5 */
   public static final String PROPERTY_ROLES = "roles";
   /** @since 1.5 */
   public static final String PROPERTY_OBJECTS = "objects";

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

   private ArrayList<PatternObject> objects /* no fulib */;
   private ArrayList<RoleObject> roles /* no fulib */;
   private ArrayList<AttributeConstraint> attributeConstraints /* no fulib */;
   private ArrayList<MatchConstraint> matchConstraints /* no fulib */;

   protected PropertyChangeSupport listeners;

   // =============== Properties ===============

   public ArrayList<PatternObject> getObjects() // no fulib
   {
      return this.objects != null ? this.objects : EMPTY_objects;
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

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withObjects((PatternObject) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withObjects(PatternObject value)
   {
      if (this.objects == null)
      {
         this.objects = new ArrayList<>();
      }
      if (!this.objects.contains(value))
      {
         this.objects.add(value);
         value.setPattern(this);
         this.firePropertyChange(PROPERTY_OBJECTS, null, value);
      }
      return this;
   }

   public Pattern withObjects(PatternObject... value)
   {
      for (final PatternObject item : value)
      {
         this.withObjects(item);
      }
      return this;
   }

   public Pattern withObjects(Collection<? extends PatternObject> value)
   {
      for (final PatternObject item : value)
      {
         this.withObjects(item);
      }
      return this;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutObjects((PatternObject) item);
         }
      }
      return this;
   }

   public Pattern withoutObjects(PatternObject value)
   {
      if (this.objects != null && this.objects.remove(value))
      {
         value.setPattern(null);
         this.firePropertyChange(PROPERTY_OBJECTS, value, null);
      }
      return this;
   }

   public Pattern withoutObjects(PatternObject... value)
   {
      for (final PatternObject item : value)
      {
         this.withoutObjects(item);
      }
      return this;
   }

   public Pattern withoutObjects(Collection<? extends PatternObject> value)
   {
      for (final PatternObject item : value)
      {
         this.withoutObjects(item);
      }
      return this;
   }

   public ArrayList<RoleObject> getRoles() // no fulib
   {
      return this.roles != null ? this.roles : EMPTY_roles;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withRoles((RoleObject) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withRoles(RoleObject value)
   {
      if (this.roles == null)
      {
         this.roles = new ArrayList<>();
      }
      if (!this.roles.contains(value))
      {
         this.roles.add(value);
         value.setPattern(this);
         this.firePropertyChange(PROPERTY_ROLES, null, value);
      }
      return this;
   }

   public Pattern withRoles(RoleObject... value)
   {
      for (final RoleObject item : value)
      {
         this.withRoles(item);
      }
      return this;
   }

   public Pattern withRoles(Collection<? extends RoleObject> value)
   {
      for (final RoleObject item : value)
      {
         this.withRoles(item);
      }
      return this;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutRoles((RoleObject) item);
         }
      }
      return this;
   }

   public Pattern withoutRoles(RoleObject value)
   {
      if (this.roles != null && this.roles.remove(value))
      {
         value.setPattern(null);
         this.firePropertyChange(PROPERTY_ROLES, value, null);
      }
      return this;
   }

   public Pattern withoutRoles(RoleObject... value)
   {
      for (final RoleObject item : value)
      {
         this.withoutRoles(item);
      }
      return this;
   }

   public Pattern withoutRoles(Collection<? extends RoleObject> value)
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
            this.withAttributeConstraints((AttributeConstraint) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withAttributeConstraints(AttributeConstraint value)
   {
      if (this.attributeConstraints == null)
      {
         this.attributeConstraints = new ArrayList<>();
      }
      if (!this.attributeConstraints.contains(value))
      {
         this.attributeConstraints.add(value);
         value.setPattern(this);
         this.firePropertyChange(PROPERTY_ATTRIBUTE_CONSTRAINTS, null, value);
      }
      return this;
   }

   public Pattern withAttributeConstraints(AttributeConstraint... value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withAttributeConstraints(item);
      }
      return this;
   }

   public Pattern withAttributeConstraints(Collection<? extends AttributeConstraint> value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withAttributeConstraints(item);
      }
      return this;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutAttributeConstraints((AttributeConstraint) item);
         }
      }
      return this;
   }

   public Pattern withoutAttributeConstraints(AttributeConstraint value)
   {
      if (this.attributeConstraints != null && this.attributeConstraints.remove(value))
      {
         value.setPattern(null);
         this.firePropertyChange(PROPERTY_ATTRIBUTE_CONSTRAINTS, value, null);
      }
      return this;
   }

   public Pattern withoutAttributeConstraints(AttributeConstraint... value)
   {
      for (final AttributeConstraint item : value)
      {
         this.withoutAttributeConstraints(item);
      }
      return this;
   }

   public Pattern withoutAttributeConstraints(Collection<? extends AttributeConstraint> value)
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
            this.withMatchConstraints((MatchConstraint) item);
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      return this;
   }

   public Pattern withMatchConstraints(MatchConstraint value)
   {
      if (this.matchConstraints == null)
      {
         this.matchConstraints = new ArrayList<>();
      }
      if (!this.matchConstraints.contains(value))
      {
         this.matchConstraints.add(value);
         value.setPattern(this);
         this.firePropertyChange(PROPERTY_MATCH_CONSTRAINTS, null, value);
      }
      return this;
   }

   public Pattern withMatchConstraints(MatchConstraint... value)
   {
      for (final MatchConstraint item : value)
      {
         this.withMatchConstraints(item);
      }
      return this;
   }

   public Pattern withMatchConstraints(Collection<? extends MatchConstraint> value)
   {
      for (final MatchConstraint item : value)
      {
         this.withMatchConstraints(item);
      }
      return this;
   }

   /** @deprecated since 1.5; use one of the type-safe overloads */
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
            this.withoutMatchConstraints((MatchConstraint) item);
         }
      }
      return this;
   }

   public Pattern withoutMatchConstraints(MatchConstraint value)
   {
      if (this.matchConstraints != null && this.matchConstraints.remove(value))
      {
         value.setPattern(null);
         this.firePropertyChange(PROPERTY_MATCH_CONSTRAINTS, value, null);
      }
      return this;
   }

   public Pattern withoutMatchConstraints(MatchConstraint... value)
   {
      for (final MatchConstraint item : value)
      {
         this.withoutMatchConstraints(item);
      }
      return this;
   }

   public Pattern withoutMatchConstraints(Collection<? extends MatchConstraint> value)
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
      this.withoutObjects(new ArrayList<>(this.getObjects()));
   }

   @Override
   public String toString() // no fulib
   {
      return "Pattern{" + "objects=" + this.objects + ", roles=" + this.roles + ", attributeConstraints="
             + this.attributeConstraints + ", matchConstraints=" + this.matchConstraints + '}';
   }
}
