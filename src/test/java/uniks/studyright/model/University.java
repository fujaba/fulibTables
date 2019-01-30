package uniks.studyright.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class University  
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public University setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }


   public static final java.util.ArrayList<Student> EMPTY_students = new java.util.ArrayList<Student>()
   { @Override public boolean add(Student value){ throw new UnsupportedOperationException("No direct add! Use xy.withStudents(obj)"); }};


   public static final String PROPERTY_students = "students";

   private java.util.ArrayList<Student> students = null;

   public java.util.ArrayList<Student> getStudents()
   {
      if (this.students == null)
      {
         return EMPTY_students;
      }

      return this.students;
   }

   public University withStudents(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withStudents(i);
            }
         }
         else if (item instanceof Student)
         {
            if (this.students == null)
            {
               this.students = new java.util.ArrayList<Student>();
            }
            if ( ! this.students.contains(item))
            {
               this.students.add((Student)item);
               ((Student)item).setUni(this);
               firePropertyChange("students", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public University withoutStudents(Object... value)
   {
      if (this.students == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutStudents(i);
            }
         }
         else if (item instanceof Student)
         {
            if (this.students.contains(item))
            {
               this.students.remove((Student)item);
               ((Student)item).setUni(null);
               firePropertyChange("students", item, null);
            }
         }
      }
      return this;
   }


   public static final java.util.ArrayList<Room> EMPTY_rooms = new java.util.ArrayList<Room>()
   { @Override public boolean add(Room value){ throw new UnsupportedOperationException("No direct add! Use xy.withRooms(obj)"); }};


   public static final String PROPERTY_rooms = "rooms";

   private java.util.ArrayList<Room> rooms = null;

   public java.util.ArrayList<Room> getRooms()
   {
      if (this.rooms == null)
      {
         return EMPTY_rooms;
      }

      return this.rooms;
   }

   public University withRooms(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withRooms(i);
            }
         }
         else if (item instanceof Room)
         {
            if (this.rooms == null)
            {
               this.rooms = new java.util.ArrayList<Room>();
            }
            if ( ! this.rooms.contains(item))
            {
               this.rooms.add((Room)item);
               ((Room)item).setUni(this);
               firePropertyChange("rooms", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public University withoutRooms(Object... value)
   {
      if (this.rooms == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutRooms(i);
            }
         }
         else if (item instanceof Room)
         {
            if (this.rooms.contains(item))
            {
               this.rooms.remove((Room)item);
               ((Room)item).setUni(null);
               firePropertyChange("rooms", item, null);
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

   @Override
   public String toString()
   {
      StringBuilder result = new StringBuilder();

      result.append(" ").append(this.getName());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutStudents(this.getStudents().clone());


      this.withoutRooms(this.getRooms().clone());


   }


}