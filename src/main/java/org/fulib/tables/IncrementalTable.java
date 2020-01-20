package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncrementalTable extends AbstractTable<Object>
{
   // =============== Fields ===============

   private ReflectorMap reflectorMap;
   private ListeningTable listeningTable = null;
   private ArrayList<ListeningTable> allListeningTables = null;
   private ArrayList<IncrementalTable> allObjectTables = null;

   // =============== Properties ===============

   public ReflectorMap getReflectorMap()
   {
      return this.reflectorMap;
   }

   public void setReflectorMap(ReflectorMap reflectorMap)
   {
      this.reflectorMap = reflectorMap;
   }

   public ListeningTable getListeningTable()
   {
      return this.listeningTable;
   }

   public void setListeningTable(ListeningTable listeningTable)
   {
      this.listeningTable = listeningTable;
   }

   public ArrayList<ListeningTable> getAllListeningTables()
   {
      return this.allListeningTables;
   }

   public void setAllListeningTables(ArrayList<ListeningTable> allListeningTables)
   {
      this.allListeningTables = allListeningTables;
   }

   public ArrayList<IncrementalTable> getAllObjectTables()
   {
      if (this.allObjectTables == null)
      {
         this.allObjectTables = new ArrayList<>();
      }

      return this.allObjectTables;
   }

   public void setAllObjectTables(ArrayList<IncrementalTable> allObjectTables)
   {
      this.allObjectTables = allObjectTables;
   }

   // =============== Methods ===============

   public void updateAllObjectTables(ListeningTable newTable)
   {
      for (IncrementalTable previousTable : this.getAllObjectTables())
      {
         previousTable.setListeningTable(newTable);
         previousTable.setTable(newTable.getBaseTable());
      }
   }

   public void addRowsForLink(String linkName, String startColumnName, ListeningTable newListeningTable,
      ListeningTable oldListeningTable, ArrayList<Object> row)
   {
      Integer index = this.getColumnMap().get(startColumnName);
      Object start = row.get(index);
      Reflector reflector = this.reflectorMap.getReflector(start);
      Object value = reflector.getValue(start, linkName);

      LinkChangeListener linkChangeListener = new LinkChangeListener(start, row, linkName, newListeningTable);
      try
      {
         Method addPropertyChangeListenerMethod = start.getClass().getMethod("addPropertyChangeListener", String.class,
                                                                             PropertyChangeListener.class);
         addPropertyChangeListenerMethod.invoke(start, linkName, linkChangeListener);
      }
      catch (Exception e)
      {
         Logger.getGlobal().log(Level.SEVERE, "could not add property change listener to " + start, e);
      }

      oldListeningTable.addRowListener(row, linkChangeListener);
      this.addRowsForLinkValues(linkName, newListeningTable, row, start, value, linkChangeListener);
   }

   public void addRowsForLinkValues(String linkName, ListeningTable newListeningTable, ArrayList<Object> row,
      Object start, Object value, LinkChangeListener linkChangeListener)
   {
      if (value instanceof Collection)
      {
         for (Object current : (Collection) value)
         {
            ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
            newRow.add(current);
            newListeningTable.addRow(newRow);
            linkChangeListener.addTargetRow(current, newRow);
         }
      }
      else if (value != null)
      {
         ArrayList<Object> newRow = (ArrayList<Object>) row.clone();
         newRow.add(value);
         newListeningTable.addRow(newRow);
         linkChangeListener.addTargetRow(value, newRow);
      }
   }

   public void removeRowsForLink(ArrayList<Object> oldRow, Object oldStart, ListeningTable newListeningTable,
      ListeningTable oldListeningTable)
   {
      Object o = oldListeningTable.getRowListeners().get(oldRow);

      if (!(o instanceof LinkChangeListener))
         return;

      LinkChangeListener oldLinkChangeListener = (LinkChangeListener) o;
      LinkedHashMap<Object, ArrayList<Object>> rowList = oldLinkChangeListener.getTargetRows();

      for (ArrayList<Object> row : rowList.values())
      {
         newListeningTable.removeRow(row, oldStart);
      }

      oldLinkChangeListener.getTargetRows().clear();
      oldListeningTable.getRowListeners().remove(oldLinkChangeListener);

      try
      {
         // removePropertyChangeListener(String propertyName,PropertyChangeListener listener)
         Method removePropertyChangeListenerMethod = oldStart.getClass()
                                                             .getMethod("removePropertyChangeListener", String.class,
                                                                        PropertyChangeListener.class);
         removePropertyChangeListenerMethod
            .invoke(oldStart, oldLinkChangeListener.getLinkName(), oldLinkChangeListener);
      }
      catch (Exception e)
      {
         Logger.getGlobal().log(Level.SEVERE, "could not remove property change listener from " + oldStart, e);
      }
   }
}
