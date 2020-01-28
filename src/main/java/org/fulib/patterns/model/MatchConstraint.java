package org.fulib.patterns.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class MatchConstraint  
{
   public static final String PROPERTY_predicate = "predicate";

   public java.util.function.Predicate predicate;

   public MatchConstraint setPredicate(java.util.function.Predicate value)
   {
      if (value != this.predicate)
      {
         java.util.function.Predicate oldValue = this.predicate;
         this.predicate = value;
         firePropertyChange("predicate", oldValue, value);
      }
      return this;
   }



   public static final java.util.ArrayList<PatternObject> EMPTY_objects = new java.util.ArrayList<PatternObject>()
   { @Override public boolean add(PatternObject value){ throw new UnsupportedOperationException("No direct add! Use xy.withObjects(obj)"); }};


   public static final String PROPERTY_objects = "objects";

   private java.util.ArrayList<PatternObject> objects = null;

   public java.util.ArrayList<PatternObject> getObjects()
   {
      if (this.objects == null)
      {
         return EMPTY_objects;
      }

      return this.objects;
   }

   public MatchConstraint withObjects(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withObjects(i);
            }
         }
         else if (item instanceof PatternObject)
         {
            if (this.objects == null)
            {
               this.objects = new java.util.ArrayList<PatternObject>();
            }
            if ( ! this.objects.contains(item))
            {
               this.objects.add((PatternObject)item);
               ((PatternObject)item).withMatchConstraints(this);
               firePropertyChange("objects", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public MatchConstraint withoutObjects(Object... value)
   {
      if (this.objects == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutObjects(i);
            }
         }
         else if (item instanceof PatternObject)
         {
            if (this.objects.contains(item))
            {
               this.objects.remove((PatternObject)item);
               ((PatternObject)item).withoutMatchConstraints(this);
               firePropertyChange("objects", item, null);
            }
         }
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

      this.withoutObjects(this.getObjects().clone());


   }




   public static final String PROPERTY_pattern = "pattern";

   private Pattern pattern = null;

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public MatchConstraint setPattern(Pattern value)
   {
      if (this.pattern != value)
      {
         Pattern oldValue = this.pattern;
         if (this.pattern != null)
         {
            this.pattern = null;
            oldValue.withoutMatchConstraints(this);
         }
         this.pattern = value;
         if (value != null)
         {
            value.withMatchConstraints(this);
         }
         firePropertyChange("pattern", oldValue, value);
      }
      return this;
   }

   @Override
   public String toString() // no fulib
   {
      return "MatchConstraint{" + "predicate=" + this.predicate + ", objects=" + this.objects + '}';
   }
}
