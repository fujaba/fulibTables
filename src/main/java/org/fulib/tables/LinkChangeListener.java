package org.fulib.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class LinkChangeListener implements PropertyChangeListener
{
   private Object start;
   private ArrayList<Object> row;
   private String linkName;
   private ListeningTable listeningTable;

   public LinkChangeListener(Object start, ArrayList<Object> row, String linkName, ListeningTable listeningTable)
   {
      this.start = start;
      this.row = row;
      this.linkName = linkName;
      this.listeningTable = listeningTable;
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      Object newValue = evt.getNewValue();
      if (newValue != null)
      {
         listeningTable.newPredecessorRowValue(this.row, start, newValue);
      }
   }
}
