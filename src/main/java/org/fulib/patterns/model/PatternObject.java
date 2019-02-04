package org.fulib.patterns.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class PatternObject  
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public PatternObject setName(String value)
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

   public PatternObject setPattern(Pattern value)
   {
      if (this.pattern != value)
      {
         Pattern oldValue = this.pattern;
         if (this.pattern != null)
         {
            this.pattern = null;
            oldValue.withoutObjects(this);
         }
         this.pattern = value;
         if (value != null)
         {
            value.withObjects(this);
         }
         firePropertyChange("pattern", oldValue, value);
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

   public PatternObject withRoles(Object... value)
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
               ((RoleObject)item).setObject(this);
               firePropertyChange("roles", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public PatternObject withoutRoles(Object... value)
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
               ((RoleObject)item).setObject(null);
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
      this.setPattern(null);

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

   public PatternObject withAttributeConstraints(Object... value)
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
               ((AttributeConstraint)item).setObject(this);
               firePropertyChange("attributeConstraints", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public PatternObject withoutAttributeConstraints(Object... value)
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
               ((AttributeConstraint)item).setObject(null);
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

   public PatternObject withMatchConstraints(Object... value)
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
               ((MatchConstraint)item).withObjects(this);
               firePropertyChange("matchConstraints", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public PatternObject withoutMatchConstraints(Object... value)
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
               ((MatchConstraint)item).withoutObjects(this);
               firePropertyChange("matchConstraints", item, null);
            }
         }
      }
      return this;
   }


   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getName());


      return result.substring(1);
   }

}