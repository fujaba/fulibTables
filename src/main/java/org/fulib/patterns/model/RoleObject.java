package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RoleObject  
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public RoleObject setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
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
         firePropertyChange("pattern", oldValue, value);
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
         firePropertyChange("other", oldValue, value);
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
         firePropertyChange("object", oldValue, value);
      }
      return this;
   }



   protected PropertyChangeSupport listeners = null;

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (listeners != null)
      {
         listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public boolean addPropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(listener);
      return true;
   }

   public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
   {
      if (listeners == null)
      {
         listeners = new PropertyChangeSupport(this);
      }
      listeners.addPropertyChangeListener(propertyName, listener);
      return true;
   }

   public boolean removePropertyChangeListener(PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(listener);
      }
      return true;
   }

   public boolean removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
   {
      if (listeners != null)
      {
         listeners.removePropertyChangeListener(propertyName, listener);
      }
      return true;
   }

   public void removeYou()
   {
      this.setPattern(null);
      this.setOther(null);
      this.setOther(null);
      this.setObject(null);

   }

   @Override
   public String toString() // no fulib
   {
      return "RoleObject{" + "object=" + this.object + ", name='" + this.name + '\'' + ", otherName="
             + this.other.getName() + ", otherObject=" + this.other.getObject() + '}';
   }
}
