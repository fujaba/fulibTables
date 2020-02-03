package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class MatchConstraint
{
   // =============== Constants ===============

   public static final String PROPERTY_predicate = "predicate";
   public static final String PROPERTY_objects = "objects";
   public static final String PROPERTY_pattern = "pattern";

   public static final ArrayList<PatternObject> EMPTY_objects = new ArrayList<PatternObject>()
   {
      @Override
      public boolean add(PatternObject value)
      {
         throw new UnsupportedOperationException("No direct add! Use xy.withObjects(obj)");
      }
   };

   // =============== Fields ===============

   public Predicate predicate;

   private ArrayList<PatternObject> objects = null;
   private Pattern pattern = null;

   protected PropertyChangeSupport listeners = null;

   // =============== Properties ===============

   public ArrayList<PatternObject> getObjects()
   {
      if (this.objects == null)
      {
         return EMPTY_objects;
      }

      return this.objects;
   }

   public MatchConstraint withObjects(Object... value)
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
               ((PatternObject) item).withMatchConstraints(this);
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

   public MatchConstraint withoutObjects(Object... value)
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
               ((PatternObject) item).withoutMatchConstraints(this);
               this.firePropertyChange("objects", item, null);
            }
         }
      }
      return this;
   }

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
         this.firePropertyChange("pattern", oldValue, value);
      }
      return this;
   }

   public MatchConstraint setPredicate(Predicate value)
   {
      if (value != this.predicate)
      {
         Predicate oldValue = this.predicate;
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

      this.withoutObjects(this.getObjects().clone());
   }
}
