package org.fulib.tables;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ListeningTable
{
   private IncrementalTable incrementalTable;
   private ArrayList<ArrayList<Object>> baseTable;
   private String linkName;
   private String startColumnName;
   private ListeningTable targetTable;
   private LinkedHashMap<Object, PropertyChangeListener> rowListeners;

   public ListeningTable(IncrementalTable incrementalTable, ArrayList<ArrayList<Object>> baseTable)
   {
      this.incrementalTable = incrementalTable;
      this.baseTable = baseTable;
      this.rowListeners = new LinkedHashMap<>();
   }


   public ArrayList<ArrayList<Object>> getBaseTable()
   {
      return baseTable;
   }

   public ListeningTable getTargetTable()
   {
      return targetTable;
   }

   public void setTargetTable(ListeningTable targetTable)
   {
      this.targetTable = targetTable;
   }

   public LinkedHashMap<Object, PropertyChangeListener> getRowListeners()
   {
      return rowListeners;
   }

   public void setColumnAndLink(String newColumn, String newLink)
   {
      this.startColumnName = newColumn;
      this.linkName = newLink;
   }



   public void addRow(ArrayList<Object> row)
   {
      baseTable.add(row);

      if (targetTable != null)
      {
         targetTable.newPredecessorRow(row, this);
      }
   }



   public void addRowListener(ArrayList<Object> row, LinkChangeListener linkChangeListener)
   {
      rowListeners.put(row, linkChangeListener);
   }



   public void removeRow(ArrayList<Object> oldRow, Object oldStart)
   {
      baseTable.remove(oldRow);

      if (targetTable != null)
      {
         targetTable.deletedPredecessorRow(oldRow, oldStart, this);
      }
   }



   private void newPredecessorRow(ArrayList<Object> row, ListeningTable oldListeningTable)
   {
      incrementalTable.addRowsForLink(linkName, startColumnName, this, oldListeningTable, row);
   }


   public void newPredecessorRowValue(ArrayList<Object> row, Object start, Object value, LinkChangeListener linkChangeListener)
   {
      incrementalTable.addRowsForLinkValues(linkName, this, row, start, value, linkChangeListener);
   }



   private void deletedPredecessorRow(ArrayList<Object> oldRow, Object oldStart, ListeningTable oldListeningTable)
   {
      incrementalTable.removeRowsForLink(oldRow, oldStart, this, oldListeningTable);
   }



   @Override
   public String toString()
   {
      if (baseTable == null || baseTable.size() == 0)
      {
         return "| empty |\n";
      }

      StringBuilder buf = new StringBuilder("| ");
      for (int i = 0; i <  baseTable.get(0).size(); i++)
      {
         buf.append(i).append(" \t| ");
      }
      buf.append("\n| ");
      for (int i = 0; i <  baseTable.get(0).size(); i++)
      {
         buf.append(" --- \t| ");
      }
      buf.append("\n");
      for (ArrayList<Object> row : baseTable)
      {
         buf.append("| ");
         for (Object cell : row)
         {
            buf.append(cell).append(" \t| ");
         }
         buf.append("\n");
      }
      buf.append("\n");
      return buf.toString();
   }


}
