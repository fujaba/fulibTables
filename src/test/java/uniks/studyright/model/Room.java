package uniks.studyright.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Room
{
   public static final String PROPERTY_roomNo = "roomNo";
   public static final String PROPERTY_topic = "topic";
   public static final String PROPERTY_credits = "credits";
   public static final String PROPERTY_uni = "uni";
   public static final String PROPERTY_students = "students";
   public static final String PROPERTY_assignments = "assignments";
   public static final String PROPERTY_ROOM_NO = "roomNo";
   public static final String PROPERTY_TOPIC = "topic";
   public static final String PROPERTY_CREDITS = "credits";
   public static final String PROPERTY_ASSIGNMENTS = "assignments";
   public static final String PROPERTY_UNI = "uni";
   public static final String PROPERTY_STUDENTS = "students";

   private String roomNo;
   private String topic;
   private double credits;

   protected PropertyChangeSupport listeners;
   private List<Assignment> assignments;
   private University uni;
   private List<Student> students;

   public String getRoomNo()
   {
      return this.roomNo;
   }

   public Room setRoomNo(String value)
   {
      if (Objects.equals(value, this.roomNo))
      {
         return this;
      }

      final String oldValue = this.roomNo;
      this.roomNo = value;
      this.firePropertyChange(PROPERTY_ROOM_NO, oldValue, value);
      return this;
   }

   public String getTopic()
   {
      return this.topic;
   }

   public Room setTopic(String value)
   {
      if (Objects.equals(value, this.topic))
      {
         return this;
      }

      final String oldValue = this.topic;
      this.topic = value;
      this.firePropertyChange(PROPERTY_TOPIC, oldValue, value);
      return this;
   }

   public double getCredits()
   {
      return this.credits;
   }

   public Room setCredits(double value)
   {
      if (value == this.credits)
      {
         return this;
      }

      final double oldValue = this.credits;
      this.credits = value;
      this.firePropertyChange(PROPERTY_CREDITS, oldValue, value);
      return this;
   }

   public List<Assignment> getAssignments()
   {
      return this.assignments != null ? Collections.unmodifiableList(this.assignments) : Collections.emptyList();
   }

   public Room withAssignments(Assignment value)
   {
      if (this.assignments == null)
      {
         this.assignments = new ArrayList<>();
      }
      if (!this.assignments.contains(value))
      {
         this.assignments.add(value);
         value.setRoom(this);
         this.firePropertyChange(PROPERTY_ASSIGNMENTS, null, value);
      }
      return this;
   }

   public Room withAssignments(Assignment... value)
   {
      for (final Assignment item : value)
      {
         this.withAssignments(item);
      }
      return this;
   }

   public Room withAssignments(Collection<? extends Assignment> value)
   {
      for (final Assignment item : value)
      {
         this.withAssignments(item);
      }
      return this;
   }

   public Room withoutAssignments(Assignment value)
   {
      if (this.assignments != null && this.assignments.remove(value))
      {
         value.setRoom(null);
         this.firePropertyChange(PROPERTY_ASSIGNMENTS, value, null);
      }
      return this;
   }

   public Room withoutAssignments(Assignment... value)
   {
      for (final Assignment item : value)
      {
         this.withoutAssignments(item);
      }
      return this;
   }

   public Room withoutAssignments(Collection<? extends Assignment> value)
   {
      for (final Assignment item : value)
      {
         this.withoutAssignments(item);
      }
      return this;
   }

   public University getUni()
   {
      return this.uni;
   }

   public Room setUni(University value)
   {
      if (this.uni == value)
      {
         return this;
      }

      final University oldValue = this.uni;
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
      this.firePropertyChange(PROPERTY_UNI, oldValue, value);
      return this;
   }

   public List<Student> getStudents()
   {
      return this.students != null ? Collections.unmodifiableList(this.students) : Collections.emptyList();
   }

   public Room withStudents(Student value)
   {
      if (this.students == null)
      {
         this.students = new ArrayList<>();
      }
      if (!this.students.contains(value))
      {
         this.students.add(value);
         value.setIn(this);
         this.firePropertyChange(PROPERTY_STUDENTS, null, value);
      }
      return this;
   }

   public Room withStudents(Student... value)
   {
      for (final Student item : value)
      {
         this.withStudents(item);
      }
      return this;
   }

   public Room withStudents(Collection<? extends Student> value)
   {
      for (final Student item : value)
      {
         this.withStudents(item);
      }
      return this;
   }

   public Room withoutStudents(Student value)
   {
      if (this.students != null && this.students.remove(value))
      {
         value.setIn(null);
         this.firePropertyChange(PROPERTY_STUDENTS, value, null);
      }
      return this;
   }

   public Room withoutStudents(Student... value)
   {
      for (final Student item : value)
      {
         this.withoutStudents(item);
      }
      return this;
   }

   public Room withoutStudents(Collection<? extends Student> value)
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
      result.append(' ').append(this.getRoomNo());
      result.append(' ').append(this.getTopic());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutAssignments(new ArrayList<>(this.getAssignments()));
      this.setUni(null);
      this.withoutStudents(new ArrayList<>(this.getStudents()));
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }
}
