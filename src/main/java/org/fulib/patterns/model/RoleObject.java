package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class RoleObject
{
   /** @deprecated since 1.5; use {@link #PROPERTY_PATTERN} instead */
   @Deprecated
   public static final String PROPERTY_pattern = "pattern";
   /** @deprecated since 1.5; use {@link #PROPERTY_NAME} instead */
   @Deprecated
   public static final String PROPERTY_name = "name";
   /** @deprecated since 1.5; use {@link #PROPERTY_OTHER} instead */
   @Deprecated
   public static final String PROPERTY_other = "other";
   /** @deprecated since 1.5; use {@link #PROPERTY_OBJECT} instead */
   @Deprecated
   public static final String PROPERTY_object = "object";

   /** @since 1.5 */
   public static final String PROPERTY_NAME = "name";
   /** @since 1.5 */
   public static final String PROPERTY_PATTERN = "pattern";
   /** @since 1.5 */
   public static final String PROPERTY_OTHER = "other";
   /** @since 1.5 */
   public static final String PROPERTY_OBJECT = "object";

   // =============== Fields ===============

   private Pattern pattern;
   private String name;
   private PatternObject object;
   private RoleObject other;

   protected PropertyChangeSupport listeners;

   // =============== Properties ===============

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public RoleObject setPattern(Pattern value)
   {
      if (this.pattern == value)
      {
         return this;
      }

      final Pattern oldValue = this.pattern;
      if (this.pattern != null)
      {
         this.pattern = null;
         oldValue.withoutRoles(this);
      }
      this.pattern = value;
      if (value != null)
      {
         value.withRoles(this);
      }
      this.firePropertyChange(PROPERTY_PATTERN, oldValue, value);
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public RoleObject setName(String value)
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

   public PatternObject getObject()
   {
      return this.object;
   }

   public RoleObject setObject(PatternObject value)
   {
      if (this.object == value)
      {
         return this;
      }

      final PatternObject oldValue = this.object;
      if (this.object != null)
      {
         this.object = null;
         oldValue.withoutRoles(this);
      }
      this.object = value;
      if (value != null)
      {
         value.withRoles(this);
      }
      this.firePropertyChange(PROPERTY_OBJECT, oldValue, value);
      return this;
   }

   public RoleObject getOther()
   {
      return this.other;
   }

   public RoleObject setOther(RoleObject value)
   {
      if (this.other == value)
      {
         return this;
      }

      final RoleObject oldValue = this.other;
      if (this.other != null)
      {
         this.other = null;
         oldValue.setOther(null);
      }
      this.other = value;
      if (value != null)
      {
         value.setOther(this);
      }
      this.firePropertyChange(PROPERTY_OTHER, oldValue, value);
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
      this.setOther(null);
      this.setObject(null);
   }

   @Override
   public String toString() // no fulib
   {
      return "RoleObject{" + "object=" + this.object + ", name=\"" + this.name + '"' + ", otherName=\""
             + this.other.getName() + '"' + ", otherObject=" + this.other.getObject() + '}';
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }
}
