package utils.dbStore;

public interface modelDbStore {
    public void setInit(String[] nameColumns) throws Exception;

    public String getColumnName(int aColumn);
    public int getColumnIndex(String aColumn);

    public void setType(int aColumn, int value);
    public void setTypeName(int aColumn, String value);
    public void setDisplaySize(int aColumn, int value);
    public void setDataLength(int aColumn, int value);
    public void setPrecision(int aColumn, int value);
    public void setScale(int aColumn, int value);
    public void setNullable(int aColumn, int value);
    public void setAutoIncrement(int aColumn, boolean value);


    public int getType(int aColumn);
    public boolean getAutoIncrement(int column);
    public String getAutoIncrementCol();

    public void setIdentifierQuoteString(String identifierQuoteString);
//  public String getIdentifierQuoteString();

    public int getColumnCount();
    public int getRowCount();
    public String getSelect() throws Exception;
    public String getInsertForStatement() throws Exception;
    public String getUpdateForStatement() throws Exception;
    public String getKeyWhere(int rowIndex) throws Exception;
    public String getWhereForStatement() throws Exception;
    public int getWhereColumnCount() throws Exception;
    public int getWhereColumnIndex(int aColumn) throws Exception;

//  public String getWhereForRow(String[] clNames, int row) throws Exception;

    public Object getObject(int rowIndex, int columnIndex);
    public void setObject(int row, int column, Object value) throws Exception;
}
