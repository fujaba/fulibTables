package uniks.studyright.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Assignment
{
   public static final String PROPERTY_task = "task";
   public static final String PROPERTY_points = "points";
   public static final String PROPERTY_room = "room";
   public static final String PROPERTY_students = "students";

   private String task;
   private double points;
   private Room room;
   private List<Student> students;

   protected PropertyChangeSupport listeners;

   public String getTask()
   {
      return this.task;
   }

   public Assignment setTask(String value)
   {
      if (Objects.equals(value, this.task))
      {
         return this;
      }

      final String oldValue = this.task;
      this.task = value;
      this.firePropertyChange(PROPERTY_task, oldValue, value);
      return this;
   }

   public double getPoints()
   {
      return this.points;
   }

   public Assignment setPoints(double value)
   {
      if (value == this.points)
      {
         return this;
      }

      final double oldValue = this.points;
      this.points = value;
      this.firePropertyChange(PROPERTY_points, oldValue, value);
      return this;
   }

   public Room getRoom()
   {
      return this.room;
   }

   public Assignment setRoom(Room value)
   {
      if (this.room == value)
      {
         return this;
      }

      final Room oldValue = this.room;
      if (this.room != null)
      {
         this.room = null;
         oldValue.withoutAssignments(this);
      }
      this.room = value;
      if (value != null)
      {
         value.withAssignments(this);
      }
      this.firePropertyChange(PROPERTY_room, oldValue, value);
      return this;
   }

   public List<Student> getStudents()
   {
      return this.students != null ? Collections.unmodifiableList(this.students) : Collections.emptyList();
   }

   public Assignment withStudents(Student value)
   {
      if (this.students == null)
      {
         this.students = new ArrayList<>();
      }
      if (!this.students.contains(value))
      {
         this.students.add(value);
         value.withDone(this);
         this.firePropertyChange(PROPERTY_students, null, value);
      }
      return this;
   }

   public Assignment withStudents(Student... value)
   {
      for (final Student item : value)
      {
         this.withStudents(item);
      }
      return this;
   }

   public Assignment withStudents(Collection<? extends Student> value)
   {
      for (final Student item : value)
      {
         this.withStudents(item);
      }
      return this;
   }

   public Assignment withoutStudents(Student value)
   {
      if (this.students != null && this.students.remove(value))
      {
         value.withoutDone(this);
         this.firePropertyChange(PROPERTY_students, value, null);
      }
      return this;
   }

   public Assignment withoutStudents(Student... value)
   {
      for (final Student item : value)
      {
         this.withoutStudents(item);
      }
      return this;
   }

   public Assignment withoutStudents(Collection<? extends Student> value)
   {
      for (final Student item : value)
      {
         this.withoutStudents(item);
      }
      return this;
   }

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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getTask());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setRoom(null);
      this.withoutStudents(new ArrayList<>(this.getStudents()));
   }
}
