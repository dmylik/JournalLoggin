package utils.dbStore;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Vector;

public class storeForm extends ActionForm implements Serializable{
    private Vector row = new Vector();

    private storePack parent = null;

    public storeForm()
    {
    }

    public storeForm(storePack store)
    {
        parent = store;
    }

    public storePack getParent()
    {
        return parent;
    }
    public void setParent(storePack Parent)
    {
        parent = Parent;
    }

    public int getRownum()
    {
        if(parent != null) return parent.getForms().indexOf(this) + parent.info.firstLineNumber;
        else return 0;
    }

    public Object getSqlValue(int aColumn)
    {
        if(aColumn < row.size() && aColumn > -1) return row.elementAt( aColumn );
        return null;
    }
    public void setSqlValue(int aColumn, Object value) throws Exception
    {
        if(aColumn > -1)
        {
            while(aColumn >= row.size()) row.addElement(null);
            row.setElementAt(value, aColumn);
        }
    }

    public String getText(int aColumn) throws Exception
    {
        if(aColumn < row.size() && aColumn > -1)
            return mf.textFormat(parent.getType(aColumn), getSqlValue(aColumn));
        else throw new Exception("Out of range index column");
    }
    public String getTextValue(String aColumn)
    {
        int cl = parent.getColumnIndex(aColumn);
        try {
            if(cl < row.size() && cl > -1) return mf.textFormat(parent.getType(cl), getSqlValue(cl));
            else if("rownum".equalsIgnoreCase(aColumn)) return "" + getRownum();
        }
        catch (Exception ex) {
            return (getSqlValue(cl) != null ? getSqlValue(cl).toString() : "");
        }
        return "";
    }
    public void setTextValue(String aColumn, String value) throws Exception
    {
        if(parent != null) setSqlValue(parent.getColumnIndex(aColumn), value);
    }
    public Object getObjectValue(String aColumn)
    {
        int cl = parent.getColumnIndex(aColumn);
        return getSqlValue(cl);
    }
    public void setObjectValue(String aColumn, Object value) throws Exception
    {
        int cl = parent.getColumnIndex(aColumn);
        if(cl > -1)
        {
            while(cl >= row.size()) row.addElement(null);
            row.setElementAt(value, cl);
        }
    }

    public Vector getForms(String aClNm)
    {
        storePack stt = (storePack) getObjectValue(aClNm);
        if(stt != null && stt instanceof storePack) return ((storePack) getObjectValue(aClNm)).getForms();
        else return new Vector();
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if(parent == null) return null;
        String err = "";
        for (int j = 0; j < parent.getColumnCount(); j++)
        {
            err = parent.verifier(j, getSqlValue(j));
//System.out.println(" --- " + err);
            if(err.length() > 0)
            {
                if(errors.isEmpty()) errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.z","<span class=\"err_h\">Неверно заполненные поля формы\n</span>"));
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "error.z", err ) );
            }
        }
        if( !errors.isEmpty() ) return errors;
        return null;
    }
    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
    {
    }

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        try
        {
            for (int i = 0; i < getParent().getColumnCount(); i++)
            {
//          str.append("  <tr>\n    <th>" + getParent().getColumnTitle(i) + "</th>\n");
//          str.append("    <td><bean:write name=\"reportAF\" property=\"textValue(" + getParent().getColumnName(i) + ")\"/></td>\n  </tr>\n");

                str.append( getParent().getColumnName(i));
                str.append(" = ");
                str.append(getTextValue( getParent().getColumnName(i) ));
                str.append("\n");
            }
            str.append("\n");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return str.toString();
    }
}
