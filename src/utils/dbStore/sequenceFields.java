package utils.dbStore;

import java.util.Vector;

public class sequenceFields {
    private Vector columnsNames = new Vector();
    private Vector sequencesNames = new Vector();
    public sequenceFields()
    {
    }
    public void addSequence(String columnName, String sequenceName)
    {
        columnsNames.addElement(columnName);
        sequencesNames.addElement(sequenceName);
    }
    public void removeSequence(String columnName)
    {
        removeSequence(columnsNames.indexOf(columnName));
    }
    public void removeSequence(int columnName)
    {
        if(columnName != -1 && columnName < columnsNames.size() && columnName < sequencesNames.size())
        {
            columnsNames.remove(columnName);
            sequencesNames.remove(columnName);
        }
    }
    public int getSequenceCount()
    {
        return columnsNames.size();
    }
    public String getSequenceName(int index)
    {
        if(index != -1 && index < sequencesNames.size())
        {
            return (sequencesNames.elementAt(index) != null ? sequencesNames.elementAt(index).toString() : "");
        }
        return "";
    }

    public String getColumnName(int index)
    {
        if(index != -1 && index < columnsNames.size())
        {
            return (columnsNames.elementAt(index) != null ? columnsNames.elementAt(index).toString() : "");
        }
        return "";
    }

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        try
        {
            str.append("<sequenceFields>\n");

            for (int ii = 0; ii < getSequenceCount(); ii++)
            {
                str.append("  <sequence>");
                str.append("ColumnName = ");
                str.append(getColumnName(ii));
                str.append("; SequenceName = ");
                str.append(getSequenceName(ii));
                str.append("; </sequence>\n");
            }

            str.append("</sequenceFields>\n");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return str.toString();
    }
}
