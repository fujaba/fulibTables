package org.fulib.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LinkChangeListener implements PropertyChangeListener
{
   private Object start;
   private ArrayList<Object> row;
   private String linkName;
   private ListeningTable listeningTable;
   private LinkedHashMap<Object, ArrayList<Object>> targetRows;

   public LinkChangeListener(Object start, ArrayList<Object> row, String linkName, ListeningTable listeningTable)
   {
      this.start = start;
      this.row = row;
      this.linkName = linkName;
      this.listeningTable = listeningTable;
      this.targetRows = new LinkedHashMap<>();
   }


   public String getLinkName()
   {
      return linkName;
   }

   public LinkedHashMap<Object, ArrayList<Object>> getTargetRows()
   {
      return targetRows;
   }


   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      Object newValue = evt.getNewValue();
      if (newValue != null)
      {
         listeningTable.newPredecessorRowValue(this.row, start, newValue, this);
      }
      else
      {
         Object oldValue = evt.getOldValue();
         if (oldValue != null)
         {
            ArrayList<Object> oldRow = this.targetRows.get(oldValue);

            this.listeningTable.removeRow(oldRow, oldValue);
         }
      }
   }

   public void addTargetRow(Object newValue, ArrayList<Object> newRow)
   {
      this.targetRows.put(newValue, newRow);
   }
}
