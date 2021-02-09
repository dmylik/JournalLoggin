package utils.dbStore;

import java.io.Serializable;
import java.util.Vector;

public class typesAndValues implements Serializable{
    private Vector types = new Vector();
    private Vector values = new Vector();
    public typesAndValues()
    {
    }
    public void add(int type, Object value)
    {
        types.addElement(new Integer(type));
        values.addElement(value);
    }
    public int getCount()
    {
        return types.size();
    }
    public int getType(int index)
    {
        if(index != -1 && index < types.size())
        {
            return (types.elementAt(index) != null ? ((Integer)types.elementAt(index)).intValue() : java.sql.Types.OTHER);
        }
        return java.sql.Types.OTHER;
    }

    public Object getValue(int index)
    {
        if(index != -1 && index < values.size())
        {
            return values.elementAt(index);
        }
        return null;
    }
}
