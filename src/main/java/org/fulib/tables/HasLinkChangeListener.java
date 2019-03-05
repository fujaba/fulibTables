package org.fulib.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class HasLinkChangeListener implements PropertyChangeListener
{
   private Object start;
   private Object other;
   private ArrayList<Object> row;
   private ListeningTable targetTable;

   public HasLinkChangeListener(Object start, Object other, ArrayList<Object> row, ListeningTable targetTable)
   {
      this.start = start;
      this.other = other;
      this.row = row;
      this.targetTable = targetTable;
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      Object newValue = evt.getNewValue();
      if (newValue != null && newValue == other)
      {
         // add row to target table
         targetTable.addRow(row);
      }

      Object oldValue = evt.getOldValue();
      if (oldValue != null && oldValue == other)
      {
         targetTable.removeRow(row, start);
      }
   }
}
