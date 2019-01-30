package uniks.studyright.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class Room  
{

   public static final String PROPERTY_roomNo = "roomNo";

   private String roomNo;

   public String getRoomNo()
   {
      return roomNo;
   }

   public Room setRoomNo(String value)
   {
      if (value == null ? this.roomNo != null : ! value.equals(this.roomNo))
      {
         String oldValue = this.roomNo;
         this.roomNo = value;
         firePropertyChange("roomNo", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_topic = "topic";

   private String topic;

   public String getTopic()
   {
      return topic;
   }

   public Room setTopic(String value)
   {
      if (value == null ? this.topic != null : ! value.equals(this.topic))
      {
         String oldValue = this.topic;
         this.topic = value;
         firePropertyChange("topic", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_credits = "credits";

   private double credits;

   public double getCredits()
   {
      return credits;
   }

   public Room setCredits(double value)
   {
      if (value != this.credits)
      {
         double oldValue = this.credits;
         this.credits = value;
         firePropertyChange("credits", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_uni = "uni";

   private University uni = null;

   public University getUni()
   {
      return this.uni;
   }

   public Room setUni(University value)
   {
      if (this.uni != value)
      {
         University oldValue = this.uni;
         if (this.uni != null)
         {
            this.uni = null;
            oldValue.withoutRooms(this);
         }
         this.uni = value;
         if (value != null)
         {
            value.withRooms(this);
         }
         firePropertyChange("uni", oldValue, value);
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

   public Room withStudents(Object... value)
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
               ((Student)item).setIn(this);
               firePropertyChange("students", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Room withoutStudents(Object... value)
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
               ((Student)item).setIn(null);
               firePropertyChange("students", item, null);
            }
         }
      }
      return this;
   }


   public static final java.util.ArrayList<Assignment> EMPTY_assignments = new java.util.ArrayList<Assignment>()
   { @Override public boolean add(Assignment value){ throw new UnsupportedOperationException("No direct add! Use xy.withAssignments(obj)"); }};


   public static final String PROPERTY_assignments = "assignments";

   private java.util.ArrayList<Assignment> assignments = null;

   public java.util.ArrayList<Assignment> getAssignments()
   {
      if (this.assignments == null)
      {
         return EMPTY_assignments;
      }

      return this.assignments;
   }

   public Room withAssignments(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withAssignments(i);
            }
         }
         else if (item instanceof Assignment)
         {
            if (this.assignments == null)
            {
               this.assignments = new java.util.ArrayList<Assignment>();
            }
            if ( ! this.assignments.contains(item))
            {
               this.assignments.add((Assignment)item);
               ((Assignment)item).setRoom(this);
               firePropertyChange("assignments", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Room withoutAssignments(Object... value)
   {
      if (this.assignments == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutAssignments(i);
            }
         }
         else if (item instanceof Assignment)
         {
            if (this.assignments.contains(item))
            {
               this.assignments.remove((Assignment)item);
               ((Assignment)item).setRoom(null);
               firePropertyChange("assignments", item, null);
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

      result.append(" ").append(this.getRoomNo());
      result.append(" ").append(this.getTopic());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUni(null);

      this.withoutStudents(this.getStudents().clone());


      this.withoutAssignments(this.getAssignments().clone());


   }


}