package utils.dbStore;

import java.io.Serializable;
import java.sql.Types;
import java.util.Vector;

public class infoDbStore implements Serializable{
    public Vector name = new Vector(); // Имена колонок таблицы
    public Vector title = new Vector(); // Описание колонок таблицы
    public Vector keyName = new Vector(); // Имена ключевых колонок таблицы
    public String autoIncrementCol = null;
    public int firstLineNumber = 1;

    private int[] type = null;
    private String[] typeName = null;
    private int[] displaySize = null;
    private int[] dataLength = null;
    private int[] precision = null;
    private int[] scale = null;
    private int[] nullable = null;
    private boolean[] autoIncrement = null;
    public String identifierQuoteString = "\"";

    public infoDbStore() {
    }

    public void setType(int column, int tp) {
        if(type == null || type.length < name.size()) type = new int[name.size()];
        if(column < type.length && column > -1) type[column] = tp;
    }
    public int getType(int column) {
        if(type != null && column < type.length && column > -1) return type[column];
        else return Types.OTHER;
    }

    public void setTypeName(int column, String tp) {
        if(typeName == null || typeName.length < name.size()) typeName = new String[name.size()];
        if(column < typeName.length && column > -1) typeName[column] = tp;
    }
    public String getTypeName(int column) {
        if(typeName != null && column < typeName.length && column > -1) return typeName[column];
        else return "OTHER";
    }

    public void setDisplaySize(int column, int size) {
        if(displaySize == null || displaySize.length < name.size()) displaySize = new int[name.size()];
        if(column < displaySize.length && column > -1) displaySize[column] = size;
    }
    public int getDisplaySize(int column) {
        if(displaySize != null && column < displaySize.length && column > -1) return displaySize[column];
        else return -1;
    }

    public void setDataLength(int column, int DataLength) {
        if(dataLength == null || dataLength.length < name.size()) dataLength = new int[name.size()];
        if(column < dataLength.length && column > -1) dataLength[column] = DataLength;
    }
    public int getDataLength(int column) {
        if(dataLength != null && column < dataLength.length && column > -1) return dataLength[column];
        else return -1;
    }

    public void setPrecision(int column, int Precision) {
        if(precision == null || precision.length < name.size()) precision = new int[name.size()];
        if(column < precision.length && column > -1) precision[column] = Precision;
    }
    public int getPrecision(int column) {
        if(precision != null && column < precision.length && column > -1) return precision[column];
        else return -1;
    }

    public void setScale(int column, int Scale) {
        if(scale == null || scale.length < name.size()) scale = new int[name.size()];
        if(column < scale.length && column > -1) scale[column] = Scale;
    }
    public int getScale(int column) {
        if(scale != null && column < scale.length && column > -1) return scale[column];
        else return -1;
    }

    public void setNullable(int column, int Nullable) {
        if(nullable == null || nullable.length < name.size()) nullable = new int[name.size()];
        if(column < nullable.length && column > -1) nullable[column] = Nullable;
    }
    public int getNullable(int column) {
        if(nullable != null && column < nullable.length && column > -1) return nullable[column];
        else return -1;
    }

    public void setAutoIncrement(int column, boolean AutoIncrement) {
        if(autoIncrement == null || autoIncrement.length < name.size()) autoIncrement = new boolean[name.size()];
        if(column < autoIncrement.length && column > -1)
        {
//      autoIncrement[column] = false;
            autoIncrement[column] = AutoIncrement;
            if(AutoIncrement) autoIncrementCol = (String) name.elementAt(column);
        }
    }
    public boolean getAutoIncrement(int column) {
        if(autoIncrement != null && column < autoIncrement.length && column > -1) return autoIncrement[column];
        else return false;
    }

    public String toString()
    {
        StringBuffer rt2 = new StringBuffer();
        for (int i = 0; i < name.size(); i++)
        {
            StringBuffer rt = new StringBuffer();
            rt.append(name.elementAt(i).toString().toUpperCase());

            while(rt.length() < 13) rt.append(" ");

//      rt.append(" ");
//      rt.append(getType(i));

            rt.append(" ");
            rt.append(getTypeName(i));
            switch(getType(i))
            {
                case Types.TIME:
                case Types.DATE:
                case Types.TIMESTAMP:
                case Types.BLOB:
                case Types.LONGVARCHAR:
                    break;
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.BIGINT:
                    rt.append(" (");
                    rt.append(getPrecision(i));
                    rt.append(", ");
                    rt.append(getScale(i));
                    rt.append(")");
                    break;
                default:
                    rt.append(" (");
                    rt.append(getDisplaySize(i));
                    rt.append(")");
                    break;
            }
            if(getNullable(i) == 0)
            {
                rt.append(" NOT NULL");
            }
            if(i < name.size()-1)
            {
                rt.append(",");
            }
            rt.append("\n");
            rt2.append(rt);
        }
        return rt2.toString();
    }
}
