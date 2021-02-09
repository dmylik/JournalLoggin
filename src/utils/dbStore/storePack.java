package utils.dbStore;

import org.apache.struts.action.ActionForm;

import java.io.Serializable;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.Vector;

public class storePack extends ActionForm implements Serializable, modelDbStore {
    public infoDbStore info = new infoDbStore();
    public infoDbStore getInfo() { return info; }
//  private Vector name = new Vector(); // Имена колонок таблицы
//  private Vector title = new Vector(); // Описание колонок таблицы
//  private Vector keyName = new Vector(); // Имена ключевых колонок таблицы
//
//  private int[] type = null;
//  private String[] typeName = null;
//  private int[] displaySize = null;
//  private int[] precision = null;
//  private int[] scale = null;
//  private int[] nullable = null;

    private Vector data = new Vector(); // Хранилище данных, стопка форм, где форма - строка таблицы

    /**
     * Если вам нужна выборка уникальных строк присвойте этой переменной значение "DISTINCT"
     */
    public String DISTINCT = "";

    /**  Хранилище данных
     */
    public storePack() {
    }

    /**  Хранилище данных
     * @param nameColumns вектор содержащий имена колонок создаваемой модели хранения
     */
    public void setInit(Vector nameColumns) {
        info.name = nameColumns;
    }

    /**  Хранилище данных
     * @param nameColumns массив строк представляющий имена колонок создаваемой модели хранения
     */
    public void setInit(String[] nameColumns) {
        info.name = new Vector();
        if(nameColumns != null)
            for (int i = 0; i < nameColumns.length; i++)
                info.name.addElement(nameColumns[i]);
    }

    /**  Хранилище данных
     * @param nameColumns строка содержащая имена колонок создаваемой модели хранения
     * в следующем формате: columnName1,columnName2,...
     */
    public void setInit(String nameColumns) throws Exception
    { // "name \n [...]"
        info.name = new Vector();
        if(nameColumns != null)
        {
            StringTokenizer stcol = new StringTokenizer( nameColumns, "\n\r," );
            while ( stcol.hasMoreTokens() )
            {
                info.name.addElement( stcol.nextToken().trim() );
            }
        }
    }

    /**
     * @param nameColumns строка содержащая имена колонок создаваемой модели хранения
     * в следующем формате: columnName1,columnName2,...
     * @param titleColumns описание колонок
     * @throws Exception
     */
    public void setInit(String nameColumns, String titleColumns, String delim) throws Exception
    { // "name \n [...]"
        info.name = new Vector();
        if(nameColumns != null)
        {
            StringTokenizer stcol = new StringTokenizer( nameColumns, "\n\r" + delim );
            while ( stcol.hasMoreTokens() )
                info.name.addElement( stcol.nextToken().trim() );
        }
        info.title = new Vector();
        if(titleColumns != null)
        {
            StringTokenizer stcol = new StringTokenizer( titleColumns, "\n\r" + delim );
            while ( stcol.hasMoreTokens() )
                info.title.addElement( stcol.nextToken().trim() );
        }
    }

    public void setInit(String nameColumns, String titleColumns) throws Exception
    {
        setInit(nameColumns, titleColumns, ",");
    }

    public void setInit(Vector nameColumns, Vector titleColumns)
    {
        info.name = nameColumns;
        info.title = titleColumns;
    }

    /**
     * Установка ключа, указанные колонки dbTool будет считать уникальным ключём
     * для этого хранилища
     * @param nameColumns ключ
     */
    public void setKeyName(String nameColumns)
    { // "name \n [...]"
        info.keyName = new Vector();
        if(nameColumns != null)
        {
            StringTokenizer stcol = new StringTokenizer( nameColumns, "\n\r," );
            while ( stcol.hasMoreTokens() )
            {
                info.keyName.addElement( stcol.nextToken().trim() );
            }
        }
    }

    public int getColumnCount() {
        return info.name.size();
    }

    public String getIsColumnNotEmpty(String column)
    {
        for (int i = 0; i < getRowCount(); i++) {
            if( getTextValue(i, column).trim().length() > 0 ) return "yes";
        }
        return "";
    }

    public int getRowCount()  {
        return data.size();
    }

    public int getRowIndex(int column, Object value)
    {
        return getRowIndex(0, column, value);
    }
    public int getRowIndex(int startRow, int column, Object value)
    {
        int ind = -1;
        if (column < getColumnCount() && column > -1)
        {
            for (int i = startRow; i < getRowCount(); i++)
            {
                if(getObject(i, column).equals(value))
                {
                    return i;
                }
            }
        }
        return -1;
    }
    public int getRowIndex(String column, String value)
    {
        return getRowIndex(0, column, value);
    }
    public int getRowIndex(int startRow, String column, String value)
    {
        int ind = -1;
        for (int i = startRow; i < getRowCount(); i++)
        {
            if(getTextValue(i, column).equalsIgnoreCase(value))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Удаление данных из модели, исключая служебные
     */
    public void eraseData() {
        data = new Vector();
    }

    /** Получение имени колонки по её номеру
     * @param column номер колонки
     * @return название колонки
     */
    public String getColumnName(int column)
    {
        if (column < getColumnCount() && column > -1) {
            return info.name.elementAt(column).toString();
        }
        else {
            return "";
        }
    }

    /** Получение описания колонки по её номеру
     * @param column номер колонки
     * @return название колонки
     */
    public String getColumnTitle(int column)
    {
        if (column < info.title.size() && column > -1) {
            return info.title.elementAt(column).toString();
        }
        else {
            return getColumnName(column);
        }
    }
    public String getTitle(String column)
    {
        int ind = getColumnIndex(column);
        if(ind != -1)
        {
            return getColumnTitle(ind);
        }
        else
        {
            return column;
        }
    }

    private String _getColumnTitle(int column)
    {
        return "<strong>'" + getColumnTitle(column) + "'</strong>";
    }

    public boolean changeColumnName(String oldNm, String newNm) throws Exception {
        int in = getColumnIndex(oldNm);
        if (in != -1) {
            info.name.setElementAt(newNm, in);
            return true;
        }
        else return false;
    }

    /** Получение номера колонки по имени
     * @param column название колонки
     * @return номер колонки
     */
    public int getColumnIndex(String column) {
        for (int i = 0; i < info.name.size(); i++)
        {
            if( ((String)info.name.elementAt(i)).equalsIgnoreCase(column)) return i;
        }
        return -1;
    }

//?????????????????????????????????????????????????????????????????????????????

    /**
     * Добавление формы в хранилище
     * @param form Сама форма, заполненая
     */
    public void setForm(ActionForm form)
    {
        ((storeForm)form).setParent(this);
        data.addElement( form );
    }


    public void setForms(Vector forms)
    {
        data = forms;
    }
    /**
     * @return Получение форм сложенных в вектор
     */
    public Vector getForms()
    {
        return data;
    }
    /**
     * @return Получение первой формы в векторе
     */
    public storeForm getForm_0()
    {
        if(data.size() == 0) setForm(new storeForm());
        return (storeForm)data.elementAt(0);
    }

    public String getTextValue(int row, String aColumn)
    {
        if(row >= 0 && row < getRowCount())
        {
            return ((storeForm) data.elementAt(row)).getTextValue(aColumn);
        }
        return "";
    }
    public String getText(int row, int aColumn) throws Exception
    {
        if(row >= 0 && row < getRowCount())
        {
            return ((storeForm) data.elementAt(row)).getText(aColumn);
        }
        else throw new Exception("Out of range index row");
    }

    public Vector getTextValues(String aColumn)
    {
        Vector clmn = new Vector();
        for (int i = 0; i < getRowCount(); i++) {
            clmn.addElement(((storeForm) data.elementAt( i )).getTextValue(aColumn));
        }
        return clmn;
    }

    public Object getObject(int row, int aColumn)
    {
        if(row >= 0 && row < getRowCount())
        {
            return ((storeForm) data.elementAt(row)).getSqlValue(aColumn);
        }
        return null;
    }
    public Object getObject(int row, String aColumn)
    {
        int indCl = getColumnIndex(aColumn);
        if(row >= 0 && row < getRowCount())
        {
            return ((storeForm) data.elementAt(row)).getObjectValue(aColumn);
        }
        return null;
    }

    /**
     * Заполнение хранилища
     * @param row строка хранилища
     * @param aColumn колонка хранилища
     * @param value заносимое значение
     */
    public void setObject(int row, int aColumn, Object value) throws Exception
    {
        if(row < 0) return;
        while(row >= getRowCount()) data.addElement(new storeForm(this));
        ((storeForm) data.elementAt(row)).setSqlValue(aColumn, value);
    }
    public void setObject(int row, String aColumn, Object value) throws Exception
    {
        int indCl = getColumnIndex(aColumn);
        if(indCl == -1)
        {
            System.out.println("в storePack нет колонки [" + aColumn + "]");
        }
        else setObject(row, indCl, value);
    }

//  public String getXmlText(int row, int column) throws Exception { //Замена спецсимволов в содержании xml контекста на спецпоследовательности
//    try {
//      String val = mf.textFormat(type[column], getObject(row, column));
//      val = replaceStr(val, "&", "&amp;");
//      val = replaceStr(val, "<", "&lt;");
//      val = replaceStr(val, ">", "&gt;");
//      val = replaceStr(val, "'", "&apos;");
//      val = replaceStr(val, "\"", "&quot;");
//      return val;
//    }
//    catch (Exception ex) {
//      if (column != -1 && getObject(row, column) != null &&
//          getObject(row, column)instanceof String) {
//        return getObject(row, column).toString();
//      }
//    }
//    return "";
//  }

    public String getSelect() throws Exception
    {
        if (info.name == null || info.name.size() == 0) return "";
        StringBuffer s1 = new StringBuffer();
        s1.append("SELECT ");
        s1.append( (DISTINCT.length()==0?"":DISTINCT + " ") );
        for (int i = 0; i < info.name.size(); i++)
        {
            s1.append( info.name.elementAt(i).toString() );
            if(i < info.name.size() - 1) s1.append( "," );
        }
        return s1.append(" ").toString();
    }

    /**
     * Составление условия по всем колонкам указанной строки
     * @param row строка, по которой составляется условие для SQL запроса
     * @return условие для SQL запроса
     * @throws Exception
     */
    public String getWhere(int row) throws Exception
    {
        String w = "";
        for (int i = 0; i < info.name.size(); i++) {
            if (getType(i) != Types.BLOB) {
                w += getColumnName(i) + mf.formatWhere(getType(i), getObject(row, i)) + " AND ";
            }
        }
        if (w.length() > 5) {
            return "WHERE " + w.substring(0, w.length() - 5);
        }
        return w;
    }

    public int getWhereColumnIndex(int aColumn) throws Exception
    {
        if(aColumn >= 0 && aColumn < info.keyName.size())
            return getColumnIndex( (String) info.keyName.elementAt(aColumn) );
        return -1;
    }
    public int getWhereColumnCount() throws Exception
    {
        return info.keyName.size();
    }
    public String getWhereForStatement() throws Exception
    {
        if(info.keyName.size() == 0) return "";
        StringBuffer w = new StringBuffer();
        w.append("WHERE ");
        for (int i = 0; i < info.keyName.size(); i++) {
//      if(prefKeys != null && prefKeys.length() > 0)
//      {
//        w.append(prefKeys);
//        w.append(".");
//      }
            w.append(info.identifierQuoteString);
            w.append(info.keyName.elementAt(i).toString().toUpperCase());
            w.append(info.identifierQuoteString);
            w.append("=?");
            if(i < info.keyName.size()-1) w.append(" AND ");
        }
        return w.toString();
    }

    /**
     * Составление условия по ключевым колонкам, если ключевые колонки не указанны
     * условие будет составлятся по всем колонкам
     * @param row строка, по которой составляется условие для SQL запроса
     * @return условие для SQL запроса
     * @throws Exception
     */
    public String getKeyWhere(int row) throws Exception
    {
        String w = "";
        if(info.keyName.size() == 0) return getWhere(row);

        for (int i = 0; i < info.keyName.size(); i++) {
            int cl = info.name.indexOf(info.keyName.elementAt(i));
            if (cl != -1) {
                w += info.keyName.elementAt(i) + mf.formatWhere(getType(cl), getObject(row, cl)) +
                        " AND ";
            }
        }
        if (w.length() > 5) {
            return "WHERE " + w.substring(0, w.length() - 5);
        }
        return w;
    }

    public String getInsertForStatement() throws Exception { // (...) VALUES (?,?,...)
        StringBuffer nm = new StringBuffer();
        StringBuffer fl = new StringBuffer();
        for (int i = 0; i < info.name.size(); i++)
        {
            if (getType(i) == Types.BLOB)
            {
                if(nm.length() > 0)
                {
                    nm.append( "," );
                    fl.append( "," );
                }
                nm.append( info.name.elementAt(i) );
                fl.append( "empty_blob()" );
            }
            else if (!getAutoIncrement(i) &&!getColumnName(i).equals("rowidtochar(rowid)"))
            {
                if(nm.length() > 0)
                {
                    nm.append( "," );
                    fl.append( "," );
                }
                nm.append( info.name.elementAt(i) );
                fl.append( "?" );
            }
        }
        if (nm.length() > 0 && fl.length() > 0)
        {
            return "(" + nm.toString() + ") VALUES (" + fl.toString() + ")";
        }
        return "";
    }

    public String getUpdateForStatement() throws Exception { // SET ...
        StringBuffer st = new StringBuffer();
        for (int i = 0; i < info.name.size(); i++)
        {
            if (getType(i) == Types.BLOB) {
                st.append( info.name.elementAt(i) );
                st.append( "=empty_blob()" );
            }
            else if (!getColumnName(i).equals("rowidtochar(rowid)")) {
                st.append( info.name.elementAt(i) );
                st.append( "=?" );
            }
            if(i < info.name.size()-1)
            {
                st.append( "," );
            }
        }
        if (st.length() > 1) {
            return "SET " + st.toString() + " ";
        }
        return "";
    }

    public void setMetaData(int column, int type, int displaySize, int precision,
                            int scale, int nullable)throws Exception
    {
        setType(column, type);
        setDisplaySize(column, displaySize);
        setPrecision(column, precision);
        setScale(column, scale);
        setNullable(column, nullable);
    }
    public void setType(int column, int tp) {
        info.setType(column, tp);
    }
    public int getType(int column) {
        return info.getType(column);
    }

    public void setTypeName(int column, String tp) {
        info.setTypeName(column, tp);
    }
    public String getTypeName(int column) {
        return info.getTypeName(column);
    }

    public void setDisplaySize(int column, int size) {
        info.setDisplaySize(column, size);
    }
    public int getDisplaySize(int column) {
        return info.getDisplaySize(column);
    }

    public void setDataLength(int column, int DataLength) {
        info.setDataLength(column, DataLength);
    }
    public int getDataLength(int column) {
        return info.getDataLength(column);
    }

    public void setPrecision(int column, int Precision) {
        info.setPrecision(column, Precision);
    }
    public int getPrecision(int column) {
        return info.getPrecision(column);
    }

    public void setScale(int column, int Scale) {
        info.setScale(column, Scale);
    }
    public int getScale(int column) {
        return info.getScale(column);
    }

    public void setNullable(int column, int Nullable) {
        info.setNullable(column, Nullable);
    }
    public int getNullable(int column) {
        return info.getNullable(column);
    }

    public void setAutoIncrement(int column, boolean AutoIncrement) {
        info.setAutoIncrement(column, AutoIncrement);
    }
    public boolean getAutoIncrement(int column) {
        return info.getAutoIncrement(column);
    }
    public String getAutoIncrementCol() {
        return info.autoIncrementCol;
    }
    public void setIdentifierQuoteString(String identifierQuoteString) {
        info.identifierQuoteString = identifierQuoteString;
    }

    public String verifier(int column, Object value)
    {
        Object ob = value;
        String val = "";
        if (ob != null) val = ob.toString();

        if (getColumnName(column).equals("rowidtochar(rowid)")) return "";
        if (getNullable(column) == 0 && val.length() == 0)
            return "Значение поля " + _getColumnTitle(column) + " не может быть пустым";

        switch (getType(column))
        {
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.BIGINT:
                StringTokenizer stnm = new StringTokenizer(val, "-.");
                if (getPrecision(column) != -1 && stnm.hasMoreTokens() &&
                        stnm.nextToken().length() > (getPrecision(column) - getScale(column)) &&
                        getPrecision(column) != 0)
                {
                    return "В поле " + _getColumnTitle(column) +
                            " целое значение не больше " + (getPrecision(column) - getScale(column)) +
                            " цифр";
                }
                for (int i = 0; i < val.length(); i++)
                {
                    if ("-0123456789.".indexOf(val.charAt(i)) == -1)
                    {
                        return "В поле " + _getColumnTitle(column) +
                                " допускаются только цифры и символ '.'";
                    }
                }
                break;

            //case Types.BIT: return ((Boolean)value).booleanValue() ? "1" : "0";
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.VARBINARY:
                if (getDisplaySize(column) != -1 && val.length() > getDisplaySize(column))
                {
                    return "Длина поля " + _getColumnTitle(column) +
                            " не должна превышать " + getDisplaySize(column) + " символов";
                }
                try {
                    if(getDisplaySize(column) != -1 && info.getTypeName(column).equals("NVARCHAR2") &&
                            val.toString().getBytes("UTF8").length > getDataLength(column) )
                    {
                        return "Данные поля " + _getColumnTitle(column) +
                                " сохраняемые в национальной кодировке превышают допустимую длину в " + getDataLength(column) + " байт (1 символ = 1..3 байта)";
                    }
                    return "";
                }
                catch (Exception ex) {}
                break;

            //case Types.TIME:  dateformat = new SimpleDateFormat("'{t '''HH:mm:ss'''}'");
            case Types.DATE:
            case Types.TIMESTAMP:
                try
                {
                    if (val.equals("")) break;

                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
                    if (ob instanceof String) {
                        if (ob.toString().trim().length() < 12) {
                            df1.parse(val);
                        }
                        else {
                            df.parse(val);
                        }
                    }
                }
                catch (Exception ex)
                {
                    return "Неверный формат поля " + _getColumnTitle(column) +
                            ". Формат даты: ДД.ММ.ГГГГ чч:мм:сс";
                }
                break;

            case Types.BLOB:
                break;

            default:
                if (getDisplaySize(column) != -1 && val.length() > getDisplaySize(column))
                {
                    return "Длинна поля " + _getColumnTitle(column) +
                            " не должна превышать " + getDisplaySize(column) + " символов";
                }
                break;
        }
        return "";
    }

//  public String xmlContent(String[] tagsTree) throws Exception {
//    return xmlContent(0, rowCount(), tagsTree);
//  }

//  public String xmlContent(int row) throws Exception {
//    String xml = "";
//    String verif = "";
//    for (int j = 0; j < sqlColumnCount(); j++) {
//      if (verifier(row, j)) {
//        verif = "1";
//      }
//      else {
//        verif = "0";
//      }
//      if (getName(j).indexOf("rowidtochar")==-1) {
//        xml += "  <" + getName(j) + " verifier=\"" + verif + "\">" +
//            getXmlText(row, j) + "</" + getName(j) + ">\n";
//      }
//      else {
//        xml += "  <exrowidstore>" + getXmlText(row, j) + "</exrowidstore>\n";
//      }
//    }
//    return xml;
//  }

//  public String xmlContent(int beginPosition, int endPosition,
//                           String[] tagsTree) throws Exception {
//    String xml = "";
//    String tab = "";
//    String verif = "";
//    int g = 0;
//    for (int i = 0; i < tagsTree.length - 1; i++) {
//      xml += tab + "<" + tagsTree[i] + ">\n";
//      tab += "  ";
//    }
//    for (int i = beginPosition; i < endPosition; i++) {
//      xml += tab + "<" + tagsTree[tagsTree.length - 1] + ">\n";
//      for (int j = 0; j < sqlColumnCount(); j++) {
//        if (verifier(i, j)) {
//          verif = "1";
//        }
//        else {
//          verif = "0";
//        }
//        if (getName(j).indexOf("rowidtochar")==-1) {
//          xml += tab + "  <" + getName(j) + " verifier=\"" + verif + "\">" +
//              getXmlText(i, j) + "</" + getName(j) + ">\n";
//        }
//        else {
//          xml += tab + "  <exrowidstore>" + getXmlText(i, j) +
//              "</exrowidstore>\n";
//          xml = xml + "<introw>" + g + "</introw>";
//          g++;
//        }
//      }
//      xml += tab + "</" + tagsTree[tagsTree.length - 1] + ">\n";
//    }
//    String xml2 = "";
//    tab = "";
//    for (int i = 0; i < tagsTree.length - 1; i++) {
//      xml2 = tab + "</" + tagsTree[i] + ">\n" + xml2;
//      tab += "  ";
//    }
//    xml += xml2;
//    return xml;
//  }

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        try
        {
            str.append("store {\n");

            for (int j = 0; j < getRowCount(); j++)
            {
                str.append("  row[");
                str.append(j);
                str.append("] { ");
                for (int i = 0; i < getColumnCount(); i++)
                {
                    str.append(getColumnName(i));
                    str.append("=");
                    str.append(getText(j, i));
                    if(i<getColumnCount()-1) str.append(", ");
                }
                str.append(" }\n");
            }
            str.append("}\n");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return str.toString();
    }
}
