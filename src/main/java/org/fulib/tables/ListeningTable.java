package org.fulib.tables;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListeningTable
{
   // =============== Fields ===============

   private IncrementalTable incrementalTable;
   private List<List<Object>> baseTable;
   private String linkName;
   private String startColumnName;
   private ListeningTable targetTable;
   private Map<Object, PropertyChangeListener> rowListeners;

   // =============== Constructors ===============

   public ListeningTable(IncrementalTable incrementalTable, List<List<Object>> baseTable)
   {
      this.incrementalTable = incrementalTable;
      this.baseTable = baseTable;
      this.rowListeners = new LinkedHashMap<>();
   }

   // =============== Properties ===============

   public List<List<Object>> getBaseTable()
   {
      return this.baseTable;
   }

   public ListeningTable getTargetTable()
   {
      return this.targetTable;
   }

   public void setTargetTable(ListeningTable targetTable)
   {
      this.targetTable = targetTable;
   }

   public Map<Object, PropertyChangeListener> getRowListeners()
   {
      return this.rowListeners;
   }

   // =============== Methods ===============

   public void setColumnAndLink(String newColumn, String newLink)
   {
      this.startColumnName = newColumn;
      this.linkName = newLink;
   }

   public void addRow(ArrayList<Object> row)
   {
      this.baseTable.add(row);

      if (this.targetTable != null)
      {
         this.targetTable.newPredecessorRow(row, this);
      }
   }

   public void addRowListener(ArrayList<Object> row, LinkChangeListener linkChangeListener)
   {
      this.rowListeners.put(row, linkChangeListener);
   }

   public void removeRow(ArrayList<Object> oldRow, Object oldStart)
   {
      this.baseTable.remove(oldRow);

      if (this.targetTable != null)
      {
         this.targetTable.deletedPredecessorRow(oldRow, oldStart, this);
      }
   }

   private void newPredecessorRow(ArrayList<Object> row, ListeningTable oldListeningTable)
   {
      this.incrementalTable.addRowsForLink(this.linkName, this.startColumnName, this, oldListeningTable, row);
   }

   public void newPredecessorRowValue(ArrayList<Object> row, Object start, Object value,
      LinkChangeListener linkChangeListener)
   {
      this.incrementalTable.addRowsForLinkValues(this.linkName, this, row, start, value, linkChangeListener);
   }

   private void deletedPredecessorRow(ArrayList<Object> oldRow, Object oldStart, ListeningTable oldListeningTable)
   {
      this.incrementalTable.removeRowsForLink(oldRow, oldStart, this, oldListeningTable);
   }

   @Override
   public String toString()
   {
      if (this.baseTable == null || this.baseTable.isEmpty())
      {
         return "| empty |\n";
      }

      StringBuilder buf = new StringBuilder("| ");
      for (int i = 0; i < this.baseTable.get(0).size(); i++)
      {
         buf.append(i).append(" \t| ");
      }
      buf.append("\n| ");
      for (int i = 0; i < this.baseTable.get(0).size(); i++)
      {
         buf.append(" --- \t| ");
      }
      buf.append("\n");
      for (List<Object> row : this.baseTable)
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
