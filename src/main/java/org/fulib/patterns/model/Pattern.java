package org.fulib.patterns.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class Pattern  
{

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


   public PatternObject getObjects(String patternObjectName)
   {
      for (PatternObject object : this.getObjects())
      {
         if (Objects.equals(patternObjectName, object.getName()))
         {
            return object;
         }
      }

      return null;
   }

   public Pattern withObjects(Object... value)
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
               ((PatternObject)item).setPattern(this);
               firePropertyChange("objects", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Pattern withoutObjects(Object... value)
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
               ((PatternObject)item).setPattern(null);
               firePropertyChange("objects", item, null);
            }
         }
      }
      return this;
   }


   public static final java.util.ArrayList<RoleObject> EMPTY_roles = new java.util.ArrayList<RoleObject>()
   { @Override public boolean add(RoleObject value){ throw new UnsupportedOperationException("No direct add! Use xy.withRoles(obj)"); }};


   public static final String PROPERTY_roles = "roles";

   private java.util.ArrayList<RoleObject> roles = null;

   public java.util.ArrayList<RoleObject> getRoles()
   {
      if (this.roles == null)
      {
         return EMPTY_roles;
      }

      return this.roles;
   }

   public Pattern withRoles(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withRoles(i);
            }
         }
         else if (item instanceof RoleObject)
         {
            if (this.roles == null)
            {
               this.roles = new java.util.ArrayList<RoleObject>();
            }
            if ( ! this.roles.contains(item))
            {
               this.roles.add((RoleObject)item);
               ((RoleObject)item).setPattern(this);
               firePropertyChange("roles", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Pattern withoutRoles(Object... value)
   {
      if (this.roles == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutRoles(i);
            }
         }
         else if (item instanceof RoleObject)
         {
            if (this.roles.contains(item))
            {
               this.roles.remove((RoleObject)item);
               ((RoleObject)item).setPattern(null);
               firePropertyChange("roles", item, null);
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
      this.withoutObjects(this.getObjects().clone());


      this.withoutRoles(this.getRoles().clone());


      this.withoutAttributeConstraints(this.getAttributeConstraints().clone());


      this.withoutMatchConstraints(this.getMatchConstraints().clone());


   }








   public static final java.util.ArrayList<AttributeConstraint> EMPTY_attributeConstraints = new java.util.ArrayList<AttributeConstraint>()
   { @Override public boolean add(AttributeConstraint value){ throw new UnsupportedOperationException("No direct add! Use xy.withAttributeConstraints(obj)"); }};


   public static final String PROPERTY_attributeConstraints = "attributeConstraints";

   private java.util.ArrayList<AttributeConstraint> attributeConstraints = null;

   public java.util.ArrayList<AttributeConstraint> getAttributeConstraints()
   {
      if (this.attributeConstraints == null)
      {
         return EMPTY_attributeConstraints;
      }

      return this.attributeConstraints;
   }

   public Pattern withAttributeConstraints(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withAttributeConstraints(i);
            }
         }
         else if (item instanceof AttributeConstraint)
         {
            if (this.attributeConstraints == null)
            {
               this.attributeConstraints = new java.util.ArrayList<AttributeConstraint>();
            }
            if ( ! this.attributeConstraints.contains(item))
            {
               this.attributeConstraints.add((AttributeConstraint)item);
               ((AttributeConstraint)item).setPattern(this);
               firePropertyChange("attributeConstraints", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Pattern withoutAttributeConstraints(Object... value)
   {
      if (this.attributeConstraints == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutAttributeConstraints(i);
            }
         }
         else if (item instanceof AttributeConstraint)
         {
            if (this.attributeConstraints.contains(item))
            {
               this.attributeConstraints.remove((AttributeConstraint)item);
               ((AttributeConstraint)item).setPattern(null);
               firePropertyChange("attributeConstraints", item, null);
            }
         }
      }
      return this;
   }


   public static final java.util.ArrayList<MatchConstraint> EMPTY_matchConstraints = new java.util.ArrayList<MatchConstraint>()
   { @Override public boolean add(MatchConstraint value){ throw new UnsupportedOperationException("No direct add! Use xy.withMatchConstraints(obj)"); }};


   public static final String PROPERTY_matchConstraints = "matchConstraints";

   private java.util.ArrayList<MatchConstraint> matchConstraints = null;

   public java.util.ArrayList<MatchConstraint> getMatchConstraints()
   {
      if (this.matchConstraints == null)
      {
         return EMPTY_matchConstraints;
      }

      return this.matchConstraints;
   }

   public Pattern withMatchConstraints(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withMatchConstraints(i);
            }
         }
         else if (item instanceof MatchConstraint)
         {
            if (this.matchConstraints == null)
            {
               this.matchConstraints = new java.util.ArrayList<MatchConstraint>();
            }
            if ( ! this.matchConstraints.contains(item))
            {
               this.matchConstraints.add((MatchConstraint)item);
               ((MatchConstraint)item).setPattern(this);
               firePropertyChange("matchConstraints", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Pattern withoutMatchConstraints(Object... value)
   {
      if (this.matchConstraints == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutMatchConstraints(i);
            }
         }
         else if (item instanceof MatchConstraint)
         {
            if (this.matchConstraints.contains(item))
            {
               this.matchConstraints.remove((MatchConstraint)item);
               ((MatchConstraint)item).setPattern(null);
               firePropertyChange("matchConstraints", item, null);
            }
         }
      }
      return this;
   }

   @Override
   public String toString() // no fulib
   {
      return "Pattern{" + "objects=" + this.objects + ", roles=" + this.roles + ", attributeConstraints="
             + this.attributeConstraints + ", matchConstraints=" + this.matchConstraints + '}';
   }
}
