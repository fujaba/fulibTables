package org.fulib.tables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class AttrChangeListener implements PropertyChangeListener
{
   private Object start;
   private ArrayList<Object> row;
   private ArrayList<Object> newRow;
   private ListeningTable targetTable;

   public AttrChangeListener(Object start, ArrayList<Object> row, ArrayList<Object> newRow, ListeningTable targetTable)
   {
      this.start = start;
      this.row = row;
      this.newRow = newRow;
      this.targetTable = targetTable;
   }


   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      Object newValue = evt.getNewValue();
      newRow.set(newRow.size()-1, newValue);
   }
}
