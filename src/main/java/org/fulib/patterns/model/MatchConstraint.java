package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.List;
import java.util.Collections;

public class MatchConstraint
{
   // =============== Constants ===============

   public static final String PROPERTY_predicate = "predicate";
   public static final String PROPERTY_objects = "objects";
   public static final String PROPERTY_pattern = "pattern";

   public static final String PROPERTY_PREDICATE = "predicate";
   public static final String PROPERTY_PATTERN = "pattern";
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

   // =============== Fields ===============

   /**
    * @deprecated since 1.2; use {@link #getPredicate()} or {@link #setPredicate(Predicate)} instead
    */
   @Deprecated
   public Predicate<? super Map<String, Object>> predicate;

   private ArrayList<PatternObject> objects /* no fulib */;
   private Pattern pattern;

   protected PropertyChangeSupport listeners;

   // =============== Properties ===============

   public ArrayList<PatternObject> getObjects() // no fulib
   {
      return this.objects != null ? this.objects : EMPTY_objects;
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

   public MatchConstraint withObjects(PatternObject value)
   {
      if (this.objects == null)
      {
         this.objects = new ArrayList<>();
      }
      if (!this.objects.contains(value))
      {
         this.objects.add(value);
         value.withMatchConstraints(this);
         this.firePropertyChange(PROPERTY_OBJECTS, null, value);
      }
      return this;
   }

   public MatchConstraint withObjects(PatternObject... value)
   {
      for (final PatternObject item : value)
      {
         this.withObjects(item);
      }
      return this;
   }

   public MatchConstraint withObjects(Collection<? extends PatternObject> value)
   {
      for (final PatternObject item : value)
      {
         this.withObjects(item);
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

   public MatchConstraint withoutObjects(PatternObject value)
   {
      if (this.objects != null && this.objects.remove(value))
      {
         value.withoutMatchConstraints(this);
         this.firePropertyChange(PROPERTY_OBJECTS, value, null);
      }
      return this;
   }

   public MatchConstraint withoutObjects(PatternObject... value)
   {
      for (final PatternObject item : value)
      {
         this.withoutObjects(item);
      }
      return this;
   }

   public MatchConstraint withoutObjects(Collection<? extends PatternObject> value)
   {
      for (final PatternObject item : value)
      {
         this.withoutObjects(item);
      }
      return this;
   }

   public Pattern getPattern()
   {
      return this.pattern;
   }

   public MatchConstraint setPattern(Pattern value)
   {
      if (this.pattern == value)
      {
         return this;
      }

      final Pattern oldValue = this.pattern;
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
      this.firePropertyChange(PROPERTY_PATTERN, oldValue, value);
      return this;
   }

   /**
    * @return the predicate
    *
    * @since 1.2
    */
   public Predicate<? super Map<String, Object>> getPredicate()
   {
      return this.predicate;
   }

   public MatchConstraint setPredicate(Predicate<? super Map<String, Object>> value)
   {
      if (value != this.predicate)
      {
         Predicate<? super Map<String, Object>> oldValue = this.predicate;
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
      this.withoutObjects(new ArrayList<>(this.getObjects()));
   }

   @Override
   public String toString() // no fulib
   {
      return "MatchConstraint{" + "predicate=" + this.predicate + ", objects=" + this.objects + '}';
   }
}
