package org.fulib.tables;

import java.util.ArrayList;

public class ListeningTable
{
   private IncrementalObjectTable incrementalObjectTable;
   private ArrayList<ArrayList<Object>> baseTable;
   private String linkName;
   private String startColumnName;
   private ListeningTable targetTable;

   public ListeningTable(IncrementalObjectTable incrementalObjectTable, ArrayList<ArrayList<Object>> baseTable)
   {
      this.incrementalObjectTable = incrementalObjectTable;
      this.baseTable = baseTable;
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
         targetTable.newPredecessorRow(row);
      }
   }

   private void newPredecessorRow(ArrayList<Object> row)
   {
      incrementalObjectTable.addRowsForLink(linkName, startColumnName, this, row);
   }


   public void newPredecessorRowValue(ArrayList<Object> row, Object start, Object value)
   {
      incrementalObjectTable.addRowsForLinkValues(linkName, this, row, start, value);
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
