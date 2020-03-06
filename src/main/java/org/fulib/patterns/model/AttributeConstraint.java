package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.function.Predicate;

public class AttributeConstraint
{
   // =============== Constants ===============

   public static final String PROPERTY_predicate = "predicate";
   public static final String PROPERTY_object = "object";
   public static final String PROPERTY_pattern = "pattern";

   // =============== Fields ===============

   /**
    * @deprecated since 1.2; use {@link #getPredicate()} or {@link #setPredicate(Predicate)} instead
    */
   @Deprecated
   public Predicate<? super Object> predicate;

   private PatternObject object;
   private Pattern pattern;

   protected PropertyChangeSupport listeners;

   // =============== Properties ===============

   public PatternObject getObject()
   {
      return this.object;
   }

   public AttributeConstraint setObject(PatternObject value)
   {
      if (this.object != value)
      {
         PatternObject oldValue = this.object;
         if (this.object != null)
         {
            this.object = null;
            oldValue.withoutAttributeConstraints(this);
         }
         this.object = value;
         if (value != null)
         {
            value.withAttributeConstraints(this);
         }
         this.firePropertyChange("object", oldValue, value);
      }
      return this;
   }

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public AttributeConstraint setPattern(Pattern value)
   {
      if (this.pattern != value)
      {
         Pattern oldValue = this.pattern;
         if (this.pattern != null)
         {
            this.pattern = null;
            oldValue.withoutAttributeConstraints(this);
         }
         this.pattern = value;
         if (value != null)
         {
            value.withAttributeConstraints(this);
         }
         this.firePropertyChange("pattern", oldValue, value);
      }
      return this;
   }

   /**
    * @return the predicate
    *
    * @since 1.2
    */
   public Predicate<? super Object> getPredicate()
   {
      return this.predicate;
   }

   public AttributeConstraint setPredicate(Predicate<? super Object> value)
   {
      if (value != this.predicate)
      {
         Predicate<? super Object> oldValue = this.predicate;
         this.predicate = value;
         this.firePropertyChange("predicate", oldValue, value);
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
      this.setObject(null);
   }

   @Override
   public String toString() // no fulib
   {
      return "AttributeConstraint{" + "object=" + this.object + ", predicate=" + this.predicate + '}';
   }
}
