package uniks.studyright.model;

import java.beans.PropertyChangeSupport;

import java.beans.PropertyChangeListener;

public class Student 
{

   public static final String PROPERTY_name = "name";

   private String name;

   public String getName()
   {
      return name;
   }

   public Student setName(String value)
   {
      if (value == null ? this.name != null : ! value.equals(this.name))
      {
         String oldValue = this.name;
         this.name = value;
         firePropertyChange("name", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_studentId = "studentId";

   private String studentId;

   public String getStudentId()
   {
      return studentId;
   }

   public Student setStudentId(String value)
   {
      if (value == null ? this.studentId != null : ! value.equals(this.studentId))
      {
         String oldValue = this.studentId;
         this.studentId = value;
         firePropertyChange("studentId", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_credits = "credits";

   private double credits;

   public double getCredits()
   {
      return credits;
   }

   public Student setCredits(double value)
   {
      if (value != this.credits)
      {
         double oldValue = this.credits;
         this.credits = value;
         firePropertyChange("credits", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_points = "points";

   private double points;

   public double getPoints()
   {
      return points;
   }

   public Student setPoints(double value)
   {
      if (value != this.points)
      {
         double oldValue = this.points;
         this.points = value;
         firePropertyChange("points", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_motivation = "motivation";

   private double motivation;

   public double getMotivation()
   {
      return motivation;
   }

   public Student setMotivation(double value)
   {
      if (value != this.motivation)
      {
         double oldValue = this.motivation;
         this.motivation = value;
         firePropertyChange("motivation", oldValue, value);
      }
      return this;
   }


   public static final String PROPERTY_uni = "uni";

   private University uni = null;

   public University getUni()
   {
      return this.uni;
   }

   public Student setUni(University value)
   {
      if (this.uni != value)
      {
         University oldValue = this.uni;
         if (this.uni != null)
         {
            this.uni = null;
            oldValue.withoutStudents(this);
         }
         this.uni = value;
         if (value != null)
         {
            value.withStudents(this);
         }
         firePropertyChange("uni", oldValue, value);
      }
      return this;
   }



   public static final String PROPERTY_in = "in";

   private Room in = null;

   public Room getIn()
   {
      return this.in;
   }

   public Student setIn(Room value)
   {
      if (this.in != value)
      {
         Room oldValue = this.in;
         if (this.in != null)
         {
            this.in = null;
            oldValue.withoutStudents(this);
         }
         this.in = value;
         if (value != null)
         {
            value.withStudents(this);
         }
         firePropertyChange("in", oldValue, value);
      }
      return this;
   }



   public static final java.util.ArrayList<Assignment> EMPTY_done = new java.util.ArrayList<Assignment>()
   { @Override public boolean add(Assignment value){ throw new UnsupportedOperationException("No direct add! Use xy.withDone(obj)"); }};


   public static final String PROPERTY_done = "done";

   private java.util.ArrayList<Assignment> done = null;

   public java.util.ArrayList<Assignment> getDone()
   {
      if (this.done == null)
      {
         return EMPTY_done;
      }

      return this.done;
   }

   public Student withDone(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withDone(i);
            }
         }
         else if (item instanceof Assignment)
         {
            if (this.done == null)
            {
               this.done = new java.util.ArrayList<Assignment>();
            }
            if ( ! this.done.contains(item))
            {
               this.done.add((Assignment)item);
               ((Assignment)item).withStudents(this);
               firePropertyChange("done", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }



   public Student withoutDone(Object... value)
   {
      if (this.done == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutDone(i);
            }
         }
         else if (item instanceof Assignment)
         {
            if (this.done.contains(item))
            {
               this.done.remove((Assignment)item);
               ((Assignment)item).withoutStudents(this);
               firePropertyChange("done", item, null);
            }
         }
      }
      return this;
   }


public static final java.util.ArrayList<Student> EMPTY_friends = new java.util.ArrayList<Student>()
   { @Override public boolean add(Student value){ throw new UnsupportedOperationException("No direct add! Use xy.withFriends(obj)"); }};


public static final String PROPERTY_friends = "friends";

private java.util.ArrayList<Student> friends = null;

public java.util.ArrayList<Student> getFriends()
   {
      if (this.friends == null)
      {
         return EMPTY_friends;
      }

      return this.friends;
   }

public Student withFriends(Object... value)
   {
      if(value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withFriends(i);
            }
         }
         else if (item instanceof Student)
         {
            if (this.friends == null)
            {
               this.friends = new java.util.ArrayList<Student>();
            }
            if ( ! this.friends.contains(item))
            {
               this.friends.add((Student)item);
               ((Student)item).withFriends(this);
               firePropertyChange("friends", null, item);
            }
         }
         else throw new IllegalArgumentException();
      }
      return this;
   }


public Student withoutFriends(Object... value)
   {
      if (this.friends == null || value==null) return this;
      for (Object item : value)
      {
         if (item == null) continue;
         if (item instanceof java.util.Collection)
         {
            for (Object i : (java.util.Collection) item)
            {
               this.withoutFriends(i);
            }
         }
         else if (item instanceof Student)
         {
            if (this.friends.contains(item))
            {
               this.friends.remove((Student)item);
               ((Student)item).withoutFriends(this);
               firePropertyChange("friends", item, null);
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
      result.append(" ").append(this.getStudentId());


      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUni(null);
      this.setIn(null);

      this.withoutDone(this.getDone().clone());


      this.withoutFriends(this.getFriends().clone());


      this.withoutFriends(this.getFriends().clone());


   }


}