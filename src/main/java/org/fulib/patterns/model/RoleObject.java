package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class RoleObject
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return this.name;
   }

   public RoleObject setName(String value)
   {
      if (!Objects.equals(value, this.name))
      {
         String oldValue = this.name;
         this.name = value;
         this.firePropertyChange("name", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_pattern = "pattern";

   private Pattern pattern = null;

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public RoleObject setPattern(Pattern value)
   {
      if (this.pattern != value)
      {
         Pattern oldValue = this.pattern;
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
         this.firePropertyChange("pattern", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_other = "other";

   private RoleObject other = null;

   public RoleObject getOther()
   {
      return this.other;
   }

   public RoleObject setOther(RoleObject value)
   {
      if (this.other != value)
      {
         RoleObject oldValue = this.other;
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
         this.firePropertyChange("other", oldValue, value);
      }
      return this;
   }

   public static final String PROPERTY_object = "object";

   private PatternObject object = null;

   public PatternObject getObject()
   {
      return this.object;
   }

   public RoleObject setObject(PatternObject value)
   {
      if (this.object != value)
      {
         PatternObject oldValue = this.object;
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
         this.firePropertyChange("object", oldValue, value);
      }
      return this;
   }

   protected PropertyChangeSupport listeners = null;

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

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getName());

      return result.substring(1);
   }

   public void removeYou()
   {
      this.setPattern(null);
      this.setOther(null);
      this.setOther(null);
      this.setObject(null);
   }
}
