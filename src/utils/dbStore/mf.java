package utils.dbStore;

import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class mf {
    public static String cp = "Cp1251";

    //******************************************************************************
    public static String sqlTimeNow()
    { // Форматирование даты
        try
        {
            SimpleDateFormat dateformat = new SimpleDateFormat("'{ts '''yyyy-MM-dd HH:mm:ss'''}'");
            java.util.GregorianCalendar d = new java.util.GregorianCalendar();
            return dateformat.format(d.getTime());
        }
        catch(Exception ex)
        {
            System.err.println( ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    //******************************************************************************
    public static String currentDate(String mask, int add)
    {
        try
        {
            SimpleDateFormat fd = new SimpleDateFormat(mask);
            java.util.GregorianCalendar dt = new java.util.GregorianCalendar();
            dt.add(java.util.GregorianCalendar.DATE, add);
            return fd.format(dt.getTime());
        }
        catch (Exception ex)
        {
            System.err.println( ex.getMessage());
            ex.printStackTrace();
        }
        return "";
    }
    //******************************************************************************
    public static String currentDate(String mask, int addDay, int addHour, int addMin, int addSec)
    {
        try
        {
            SimpleDateFormat fd = new SimpleDateFormat(mask);
            java.util.GregorianCalendar dt = new java.util.GregorianCalendar();
            dt.add(java.util.GregorianCalendar.DATE, addDay);
            dt.add(java.util.GregorianCalendar.HOUR, addHour);
            dt.add(java.util.GregorianCalendar.MINUTE, addMin);
            dt.add(java.util.GregorianCalendar.SECOND, addSec);
            return fd.format(dt.getTime());
        }
        catch (Exception ex)
        {
            System.err.println( ex.getMessage());
            ex.printStackTrace();
        }
        return "";
    }
    //******************************************************************************
    public static String currentDate(String mask)
    {
        try
        {
            SimpleDateFormat fd = new SimpleDateFormat(mask);
            return fd.format(new java.util.GregorianCalendar().getTime());
        }
        catch (Exception ex)
        {
            System.err.println( ex.getMessage());
            ex.printStackTrace();
        }
        return "";
    }
    //******************************************************************************
    public static java.util.Date currentDate()
    {
        try
        {
            return new java.util.GregorianCalendar().getTime();
        }
        catch (Exception ex)
        {
            System.err.println( ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    //******************************************************************************
    public static String DataFormat(Object value,String mask) throws ParseException
    { // Форматирование даты
        if(value == null) return null;
        SimpleDateFormat fd = new SimpleDateFormat(mask);
        return fd.format(value);
    }
    //******************************************************************************
    public static java.util.Date data(String value, String mask) throws ParseException
    { // Форматирование даты
        SimpleDateFormat fd = new SimpleDateFormat(mask);
        return fd.parse(value);
    }
    //******************************************************************************
    public static String DataFormat(String value, int add_date, String mask) throws ParseException
    { // Форматирование даты
        SimpleDateFormat fd = new SimpleDateFormat(mask);
        java.util.GregorianCalendar d = new java.util.GregorianCalendar();
        d.setTime(fd.parse(value.toString()));
        d.add(java.util.Calendar.DATE, add_date);
        return fd.format(d.getTime());
    }
    //******************************************************************************
    public static String formatWhere(int col, Object value) throws ParseException
    {
        String s = "";
        s = sqlFormat(col,value);
        if(s == null) return " is null";
        if(s.equals("''") || s.equals("")) return " is null";
        if((Types.TIMESTAMP == col || Types.DATE == col) && s.equals("null")) return " is null";
        return("="+s);
    }
    //******************************************************************************
    public static String sqlFormat(Object col, Object value) throws ParseException
    {
        return sqlFormat(((Integer)col).intValue(),value);
    }
    //******************************************************************************
    public static String sqlFormat(int col, Object value) throws ParseException
    {

        SimpleDateFormat dateformat=null;
        if (value == null)
            switch(col) {
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.DECIMAL:
                case Types.BIT:

                case Types.NUMERIC:
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.VARBINARY:
                    return "''";
                case Types.TIME:
                case Types.TIMESTAMP:
                case Types.DATE:
                    return "null";
                default:
                    return "null";
            }
        else
            switch(col) {
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.BIGINT:
                    if(value.equals("")) return "''";
                    return value.toString();
                case Types.BIT:
                    return ((Boolean)value).booleanValue() ? "1" : "0";
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    return "'"+value.toString().trim().replace('\'', '"')+"'";
/*			case Types.DATE:
        dateformat = new SimpleDateFormat("'{d '''yyyy-MM-dd'''}'");
        if(value.toString().trim().equals("1899.12.30")) return "null";
        return dateformat.format(value);*/
                case Types.TIME:
                    dateformat = new SimpleDateFormat("'{t '''HH:mm:ss'''}'");
                    return dateformat.format(value);
                case Types.DATE:
                case Types.TIMESTAMP:
                    dateformat = new SimpleDateFormat("'{ts '''yyyy-MM-dd HH:mm:ss'''}'");
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
                    if(value.toString().trim().equals("1899.12.30 00:00:00.0") || value.toString().trim().equals("1899.12.30")) return "null";
                    if (value instanceof String)
                        if(value.equals("")) return null;
                        else if(value.toString().trim().length() < 12) value = df1.parse(value.toString());
                        else value = df.parse(value.toString());
                    return dateformat.format(value);
                case Types.VARBINARY:
                    String vl = "";
                    try
                    {
                        byte b[];
                        if(value instanceof String)  b = ((String)value).getBytes(cp);
                        else b = ((byte[])value);
                        for (int i = 0; i < b.length; i++)
                        {
                            String vlb = Integer.toHexString(b[i]);
                            if(vlb.length() > 2) vl += vlb.substring(vlb.length()-2,vlb.length());
                            else vl += vlb;
                        }
                        vl = vl.toUpperCase();
                    }
                    catch (java.io.UnsupportedEncodingException ex) {
                        System.err.println( ex.getMessage());
                        ex.printStackTrace();
                    }
//          catch (Exception e) { writeLog.log(e); }
                    return "'"+vl+"'";
                case Types.BLOB:
                    return "empty_blob()";
                case oracle.jdbc.driver.OracleTypes.ROWID:
                    return "'"+value.toString()+"'";
                default:
                    return value.toString();
            }
    }
    //******************************************************************************
    public static String textFormat(Object col, Object value) throws Exception
    {
        return textFormat(((Integer)col).intValue(),value);
    }
    public static String textFormat(int col, Object value) throws Exception
    {
        SimpleDateFormat dateformat=null;

        if (value == null)  return "";
        else
            switch(col) {
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.BIGINT:
                    return value.toString();
                case Types.BIT:
                    return ((Boolean)value).booleanValue() ? "1" : "0";
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    return value.toString().trim().replace('\'', '"');
/*      case Types.DATE:
        dateformat = new SimpleDateFormat("dd.MM.yyyy");
        if(value.toString().trim().equals("1899.12.30")) return "";
        if (value instanceof String)
          if(value.equals("")) return "";
            else value = dateformat.parse(value.toString());
        return dateformat.format(value);*/
                case Types.TIME:
                    dateformat = new SimpleDateFormat("HH:mm:ss");
                    if (value instanceof String)
                        if(value.equals("")) return "";
                        else value = dateformat.parse(value.toString());
                    return dateformat.format(value);
                case Types.DATE:
                case Types.TIMESTAMP:
                    dateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    if(value.toString().trim().equals("1899.12.30 00:00:00.0") || value.toString().trim().equals("1899.12.30")) return "";
                    SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
                    if (value instanceof String)
                        if(value.equals("")) return "";
                        else if(value.toString().trim().length() < 12)
                        {
                            value = df1.parse(value.toString());
                        }
                        else value = dateformat.parse(value.toString());
                    String dt = dateformat.format(value);
                    if(dt.length() > 10 && dt.indexOf("00:00:00") != -1) return dt.substring(0, 10);
                    return dt;
                case Types.VARBINARY:
                    try
                    {
                        if(value instanceof byte[])  return new String(((byte[])value),cp);
                        else return value.toString().trim();
                    }
                    catch (java.io.UnsupportedEncodingException ex) {
                        System.err.println( ex.getMessage());
                        ex.printStackTrace();
                    }
                case Types.BLOB:
                    if(value instanceof byte[])  return new String((byte[])value,cp);
                    else if(value instanceof char[])  return new String((char[])value);
                    else return value.toString().trim();
                case Types.CLOB:
                    if (value instanceof byte[]) return new String( (byte[]) value, mf.cp);
                    else if (value instanceof char[]) return new String( (char[]) value);
                    return value.toString().trim();
                default:
                    return value.toString();
            }
    }
    //******************************************************************************
    static public String xmlText(String text) throws Exception
    {
        String ttt = text;
        ttt = ttt.replaceAll("&", "&amp;");
        ttt = ttt.replaceAll("<", "&lt;");
        ttt = ttt.replaceAll(">", "&gt;");
        ttt = ttt.replaceAll("'", "&apos;");
        ttt = ttt.replaceAll("\"", "&quot;");
        return ttt;
    }

    public static String hrefA(String httpUrl) throws Exception
    {
        if(httpUrl != null && httpUrl.length() > 0)
        {
            return "<A href=\"" + httpUrl + "\">" + httpUrl + "</A>";
        }
        return "";
    }
    public static String hrefHTTP(String httpUrl) throws Exception
    {
        if(httpUrl != null && httpUrl.length() > 0)
        {
            if(httpUrl.length() > 7 && !httpUrl.substring(0,7).equalsIgnoreCase("http://")) httpUrl = "http://" + httpUrl;
            return "<A href=\"" + httpUrl + "\" target=\"_blank\">" + httpUrl + "</A>";
        }
        return "";
    }
    public static String hrefHTTP(String httpUrl, String httpTtl) throws Exception
    {
        if(httpUrl != null && httpUrl.length() > 0)
        {
            if(httpUrl.length() > 7 && !httpUrl.substring(0,7).equalsIgnoreCase("http://")) httpUrl = "http://" + httpUrl;
            return "<A href=\"" + httpUrl + "\" target=\"_blank\">" + httpTtl + "</A>";
        }
        return "";
    }
    public static String hrefEMAIL(String emailUrl) throws Exception
    {
        if(emailUrl != null && emailUrl.length() > 0)
        {
            return "<A href=\"mailto:" + emailUrl + "\">" + emailUrl + "</A>";
        }
        return "";
    }
    public static String hrefEMAIL(String emailUrl, String emailTtl) throws Exception
    {
        if(emailUrl != null && emailUrl.length() > 0)
        {
            return "<A href=\"mailto:" + emailUrl + "\">" + emailTtl + "</A>";
        }
        return "";
    }
//******************************************************************************
}
