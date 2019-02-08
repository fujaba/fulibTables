package org.fulib.patterns.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class AttributeConstraint  
{
   public static final String PROPERTY_predicate = "predicate";

   public java.util.function.Predicate predicate;

   public AttributeConstraint setPredicate(java.util.function.Predicate value)
   {
      if (value != this.predicate)
      {
         java.util.function.Predicate oldValue = this.predicate;
         this.predicate = value;
         firePropertyChange("predicate", oldValue, value);
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
      this.setObject(null);

   }




   public static final String PROPERTY_object = "object";

   private PatternObject object = null;

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
         firePropertyChange("object", oldValue, value);
      }
      return this;
   }







   public static final String PROPERTY_pattern = "pattern";

   private Pattern pattern = null;

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
         firePropertyChange("pattern", oldValue, value);
      }
      return this;
   }























}