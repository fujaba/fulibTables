package uniks.studyright.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Student
{
   public static final String PROPERTY_name = "name";
   public static final String PROPERTY_studentId = "studentId";
   public static final String PROPERTY_credits = "credits";
   public static final String PROPERTY_points = "points";
   public static final String PROPERTY_motivation = "motivation";
   public static final String PROPERTY_uni = "uni";
   public static final String PROPERTY_in = "in";
   public static final String PROPERTY_done = "done";
   public static final String PROPERTY_friends = "friends";

   private String name;
   private String studentId;
   private double credits;
   private double points;
   private double motivation;
   private University uni;
   private Room in;
   private List<Assignment> done;
   private List<Student> friends;

   protected PropertyChangeSupport listeners;

   public String getName()
   {
      return this.name;
   }

   public Student setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_name, oldValue, value);
      return this;
   }

   public String getStudentId()
   {
      return this.studentId;
   }

   public Student setStudentId(String value)
   {
      if (Objects.equals(value, this.studentId))
      {
         return this;
      }

      final String oldValue = this.studentId;
      this.studentId = value;
      this.firePropertyChange(PROPERTY_studentId, oldValue, value);
      return this;
   }

   public double getCredits()
   {
      return this.credits;
   }

   public Student setCredits(double value)
   {
      if (value == this.credits)
      {
         return this;
      }

      final double oldValue = this.credits;
      this.credits = value;
      this.firePropertyChange(PROPERTY_credits, oldValue, value);
      return this;
   }

   public double getPoints()
   {
      return this.points;
   }

   public Student setPoints(double value)
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

   public double getMotivation()
   {
      return this.motivation;
   }

   public Student setMotivation(double value)
   {
      if (value == this.motivation)
      {
         return this;
      }

      final double oldValue = this.motivation;
      this.motivation = value;
      this.firePropertyChange(PROPERTY_motivation, oldValue, value);
      return this;
   }

   public University getUni()
   {
      return this.uni;
   }

   public Student setUni(University value)
   {
      if (this.uni == value)
      {
         return this;
      }

      final University oldValue = this.uni;
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
      this.firePropertyChange(PROPERTY_uni, oldValue, value);
      return this;
   }

   public Room getIn()
   {
      return this.in;
   }

   public Student setIn(Room value)
   {
      if (this.in == value)
      {
         return this;
      }

      final Room oldValue = this.in;
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
      this.firePropertyChange(PROPERTY_in, oldValue, value);
      return this;
   }

   public List<Assignment> getDone()
   {
      return this.done != null ? Collections.unmodifiableList(this.done) : Collections.emptyList();
   }

   public Student withDone(Assignment value)
   {
      if (this.done == null)
      {
         this.done = new ArrayList<>();
      }
      if (!this.done.contains(value))
      {
         this.done.add(value);
         value.withStudents(this);
         this.firePropertyChange(PROPERTY_done, null, value);
      }
      return this;
   }

   public Student withDone(Assignment... value)
   {
      for (final Assignment item : value)
      {
         this.withDone(item);
      }
      return this;
   }

   public Student withDone(Collection<? extends Assignment> value)
   {
      for (final Assignment item : value)
      {
         this.withDone(item);
      }
      return this;
   }

   public Student withoutDone(Assignment value)
   {
      if (this.done != null && this.done.remove(value))
      {
         value.withoutStudents(this);
         this.firePropertyChange(PROPERTY_done, value, null);
      }
      return this;
   }

   public Student withoutDone(Assignment... value)
   {
      for (final Assignment item : value)
      {
         this.withoutDone(item);
      }
      return this;
   }

   public Student withoutDone(Collection<? extends Assignment> value)
   {
      for (final Assignment item : value)
      {
         this.withoutDone(item);
      }
      return this;
   }

   public List<Student> getFriends()
   {
      return this.friends != null ? Collections.unmodifiableList(this.friends) : Collections.emptyList();
   }

   public Student withFriends(Student value)
   {
      if (this.friends == null)
      {
         this.friends = new ArrayList<>();
      }
      if (!this.friends.contains(value))
      {
         this.friends.add(value);
         value.withFriends(this);
         this.firePropertyChange(PROPERTY_friends, null, value);
      }
      return this;
   }

   public Student withFriends(Student... value)
   {
      for (final Student item : value)
      {
         this.withFriends(item);
      }
      return this;
   }

   public Student withFriends(Collection<? extends Student> value)
   {
      for (final Student item : value)
      {
         this.withFriends(item);
      }
      return this;
   }

   public Student withoutFriends(Student value)
   {
      if (this.friends != null && this.friends.remove(value))
      {
         value.withoutFriends(this);
         this.firePropertyChange(PROPERTY_friends, value, null);
      }
      return this;
   }

   public Student withoutFriends(Student... value)
   {
      for (final Student item : value)
      {
         this.withoutFriends(item);
      }
      return this;
   }

   public Student withoutFriends(Collection<? extends Student> value)
   {
      for (final Student item : value)
      {
         this.withoutFriends(item);
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
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getStudentId());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setUni(null);
      this.setIn(null);
      this.withoutDone(new ArrayList<>(this.getDone()));
      this.withoutFriends(new ArrayList<>(this.getFriends()));
      this.withoutFriends(new ArrayList<>(this.getFriends()));
   }
}
