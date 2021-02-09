package utils.dbStore;

import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.internal.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.BLOB;
import sun.jdbc.odbc.JdbcOdbcConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class dbTool {
    private Connection connection = null;
    private DatabaseMetaData mtdt = null;

    private String connectionString = "";
    private String name = "";
    private String password = "";
    private String sqlSchema = "";

    private String productName = "";
    private int databaseMajorVersion = 0;
    private int driverVersion = 0;
    public String identifierQuoteString = "\"";

    public boolean isPackAllBlob = false;
    public boolean isCommitTransaction = true;
    public boolean TRIM_ALL_CHAR = false;
    public boolean IS_NVARCHAR = false;

    public void close() throws Exception
    {
        if(connection != null && !connection.isClosed())
        {
            if(!connection.getAutoCommit()) connection.commit();
            connection.close();
        }
    }

    public void commit() throws Exception
    {
        if(connection != null && !connection.isClosed()) connection.commit();
    }

    public void rollback() throws Exception
    {
        if(connection != null && !connection.isClosed()) connection.rollback();
    }

    public void setSqlSchema(String value) { sqlSchema = value; }
    public String getSchema() { return sqlSchema; }

    public void setInit(String connectString, String Name, String Password) throws Exception
    {
        connectionString = connectString;
        name = Name;
        password = Password;
        if( sqlSchema.length() == 0 ) sqlSchema = Name;

//System.out.println("connectString = " + connectString + ", Name = " + Name + ", Password = " + Password);

        connection = DriverManager.getConnection(connectString, Name, Password);

        mtdt = connection.getMetaData();
        productName = mtdt.getDatabaseProductName();

        try {
            driverVersion = Integer.parseInt( mtdt.getDriverVersion().split("[.]")[0] );
            databaseMajorVersion = mtdt.getDatabaseMajorVersion();
        } catch (Throwable ex) { }
//    System.out.println(driverVersion);


        if(productName.equalsIgnoreCase("Oracle")) connection.setAutoCommit(false);
        else if(productName.equalsIgnoreCase("MySql")) connection.setAutoCommit(false);
        else if(productName.toUpperCase().startsWith("DB2")) connection.setAutoCommit(false);

//    System.out.println(connection.getAutoCommit());
//    isCommitTransaction = !connection.getAutoCommit();

        identifierQuoteString = mtdt.getIdentifierQuoteString();

//    System.out.println("productName = " + productName + //"  productVersion = " + productVersion +
//                       "  databaseMajorVersion = " + databaseMajorVersion + "  identifierQuoteString = " + identifierQuoteString);

//    System.out.println("Database Name = " + mtdt.getDatabaseProductName());
//    System.out.println("Database Version = " + mtdt.getDatabaseProductVersion());
//    System.out.println("Driver = " + mtdt.getDriverName());
//    System.out.println("Driver version = " + mtdt.getDriverVersion());
//    System.out.println("URL = " + mtdt.getURL());

//    System.out.println(mtdt.getDriverMinorVersion());
//    System.out.println(new java.util.Date());

    }

    public void receiveTypeInfo(modelDbStore st) throws Exception
    {
//    DatabaseMetaData mtdt = connection.getMetaData();
        ResultSet tt = mtdt.getTypeInfo();
        ResultSetMetaData md = tt.getMetaData();
        String[] clmnnm = new String[md.getColumnCount()];
        for (int i = 0; i < clmnnm.length; i++) {
            clmnnm[i] = md.getColumnLabel(i+1).toLowerCase();
        }
        st.setInit(clmnnm);
        st.setIdentifierQuoteString(identifierQuoteString);
        for (int i = 0; i < st.getColumnCount(); i++)
        {
            st.setType( i, md.getColumnType( i + 1 ) );
            st.setTypeName(i, md.getColumnTypeName(i+1) );
            st.setDisplaySize(i, md.getColumnDisplaySize(i+1) );
            if(st.getType(i) != Types.BLOB && st.getType(i) != Types.CLOB) st.setPrecision(i, md.getPrecision(i+1) );
            else st.setPrecision(i, 0);
            st.setScale(i, md.getScale(i+1) );
            st.setNullable(i, md.isNullable(i+1) );
            st.setAutoIncrement(i, md.isAutoIncrement(i+1) );
        }
        fillStoreFromResultSet(st, tt, 0, 0);
    }

    public void setInit(String connectString, java.util.Properties property) throws Exception
    {
        connectionString = connectString;

//    if( sqlSchema.length() == 0 ) sqlSchema = Name;

//    java.util.Properties properties=new java.util.Properties();
//    properties.setProperty("charSet","sv_SE");

        connection = DriverManager.getConnection(connectString, property);

        mtdt = connection.getMetaData();
        productName = mtdt.getDatabaseProductName();
        try {
            driverVersion = Integer.parseInt( mtdt.getDriverVersion().split("[.]")[0] );
        } catch (Exception ex) { }
        if(productName.toUpperCase().equals("ORACLE")) databaseMajorVersion = mtdt.getDatabaseMajorVersion();

        if(productName.equalsIgnoreCase("Oracle")) connection.setAutoCommit(false);
        else if(productName.equalsIgnoreCase("MySql")) connection.setAutoCommit(false);
        else if(productName.toUpperCase().startsWith("DB2")) connection.setAutoCommit(false);

        identifierQuoteString = mtdt.getIdentifierQuoteString();

/*    System.out.println(mtdt.getDatabaseProductName());
    System.out.println(mtdt.getDriverMajorVersion());
    System.out.println(mtdt.getDriverMinorVersion());
    System.out.println(mtdt.getDriverName());
    System.out.println(mtdt.getDriverVersion());
    System.out.println(new java.util.Date());*/
    }

    public void setReInit() throws Exception
    {
        if( connection == null || connection.isClosed() )
        {
            connection = DriverManager.getConnection(connectionString, name, password);

            mtdt = connection.getMetaData();
            productName = mtdt.getDatabaseProductName();
            try {
                driverVersion = Integer.parseInt( mtdt.getDriverVersion().split("[.]")[0] );
            } catch (Exception ex) { }
            if(productName.toUpperCase().equals("ORACLE")) databaseMajorVersion = mtdt.getDatabaseMajorVersion();

            if(productName.equalsIgnoreCase("Oracle")) connection.setAutoCommit(false);
            else if(productName.equalsIgnoreCase("MySql")) connection.setAutoCommit(false);
            else if(productName.toUpperCase().startsWith("DB2")) connection.setAutoCommit(false);
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    public void setInit(Connection conn) throws Exception
    {
        connection = conn;
        if(connection != null && !connection.isClosed())
        {
            mtdt = connection.getMetaData();
            productName = mtdt.getDatabaseProductName();

            identifierQuoteString = mtdt.getIdentifierQuoteString();

            try {
                driverVersion = Integer.parseInt( mtdt.getDriverVersion().split("[.]")[0] );
            } catch (Exception ex) { }
            if(productName.toUpperCase().equals("ORACLE")) databaseMajorVersion = mtdt.getDatabaseMajorVersion();
        }
    }

    public String getCatalog() throws Exception
    {
        if(connection != null) return connection.getCatalog();
        return "";
    }


    /**
     * Заполнение хранилища данными из БД.  Хранилище дополняется данными, поэтому перед
     * использованием метода считывания данных необходимо очистить Хранилище от устаревших
     * данных.
     * @param stColumn Хранилище
     * @param tableName имя таблицы БД
     * @param where условие считывания данных из таблицы БД.
     * Часть SQL запроса начиная с "WHERE ...".
     * @throws Exception Ошибки возникшие при считывании данных из БД и заполнения
     * Хранилища
     */
    public void readSt(modelDbStore stColumn, String tableName, String where) throws Exception
    {
        Statement          statement=null;
        ResultSet          resultSet=null;
        ResultSetMetaData  metaData=null;
        try
        {
            stColumn.setIdentifierQuoteString(identifierQuoteString);
            statement = connection.createStatement();

            String query = stColumn.getSelect();
            query += "FROM " + tableName;
            if(where != null && where.length() > 0) query += " " + where;
//System.out.println("-------------------");
//System.out.println(query);
//System.out.println("-------------------");
            resultSet = statement.executeQuery(query);

            metaData = resultSet.getMetaData();

            for (int i = 0; i < stColumn.getColumnCount(); i++)
            {
                stColumn.setType( i, metaData.getColumnType( i + 1 ) );
                stColumn.setTypeName(i, metaData.getColumnTypeName(i+1) );
                stColumn.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                if(stColumn.getType(i) != Types.BLOB && stColumn.getType(i) != Types.CLOB) stColumn.setPrecision(i, metaData.getPrecision(i+1) );
                else stColumn.setPrecision(i, 0);
                stColumn.setScale(i, metaData.getScale(i+1) );
                stColumn.setNullable(i, metaData.isNullable(i+1) );
                stColumn.setAutoIncrement(i, metaData.isAutoIncrement(i+1) );
            }

            fillStoreFromResultSet(stColumn, resultSet, 0, 0);
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
                statement.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
    }

    public void readSt(modelDbStore st, String select) throws Exception
    {
        Statement          statement=null;
        ResultSet          resultSet=null;
        ResultSetMetaData  metaData=null;
        try
        {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(select);

            metaData = resultSet.getMetaData();

            String[] clnm = new String[metaData.getColumnCount()];

            for (int i = 0; i < clnm.length; i++)
            {
                clnm[i] = metaData.getColumnLabel( i + 1 ).toLowerCase();
            }
            st.setInit(clnm);
            st.setIdentifierQuoteString(identifierQuoteString);

            for (int i = 0; i < st.getColumnCount(); i++)
            {
                st.setType( i, metaData.getColumnType( i + 1 ) );
                st.setTypeName(i, metaData.getColumnTypeName(i+1) );
                st.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                if(st.getType(i) != Types.BLOB && st.getType(i) != Types.CLOB) st.setPrecision(i, metaData.getPrecision(i+1) );
                else st.setPrecision(i, 0);
                st.setScale(i, metaData.getScale(i+1) );
                st.setNullable(i, metaData.isNullable(i+1) );
                st.setAutoIncrement(i, metaData.isAutoIncrement(i+1) );
            }

            fillStoreFromResultSet(st, resultSet, 0, 0);
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
                statement.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
    }

    private Statement          statement_read=null;
    private ResultSet          resultSet_read=null;
    private ResultSetMetaData  metaData_read=null;

    public boolean readSt(modelDbStore stColumn, String tableName, String where, int bufferSize) throws Exception
    {
        boolean ret = false;
        try
        {
            stColumn.setIdentifierQuoteString(identifierQuoteString);
            if(statement_read == null)
            {
                statement_read = connection.createStatement();

                String query = stColumn.getSelect();
                query += "FROM " + tableName;
                if (where != null && where.length() > 0) query += " " + where;
                resultSet_read = statement_read.executeQuery(query);

                metaData_read = resultSet_read.getMetaData();

                for (int i = 0; i < stColumn.getColumnCount(); i++)
                {
                    stColumn.setType(i, metaData_read.getColumnType(i + 1));
                    stColumn.setTypeName(i, metaData_read.getColumnTypeName(i + 1));
                    stColumn.setDisplaySize(i, metaData_read.getColumnDisplaySize(i + 1));
                    if (stColumn.getType(i) != Types.BLOB && stColumn.getType(i) != Types.CLOB)
                        stColumn.setPrecision(i, metaData_read.getPrecision(i + 1));
                    else
                        stColumn.setPrecision(i, 0);
                    stColumn.setScale(i, metaData_read.getScale(i + 1));
                    stColumn.setNullable(i, metaData_read.isNullable(i + 1));
                    stColumn.setAutoIncrement(i, metaData_read.isAutoIncrement(i+1) );
                }
            }

            ret = fillStoreFromResultSet(stColumn, resultSet_read, 0, bufferSize);

            if(!ret)
            {
                try {
                    if (resultSet_read != null) resultSet_read.close();
                    if (statement_read != null) statement_read.close();
                }
                catch (Exception ex) { ex.printStackTrace(); }
                statement_read = null;
                resultSet_read = null;
                metaData_read = null;
            }
        }
        catch (Exception ex)
        {
            try {
                if (resultSet_read != null) resultSet_read.close();
                if (statement_read != null) statement_read.close();
            }
            catch (Exception exx) { exx.printStackTrace(); }
            statement_read = null;
            resultSet_read = null;
            metaData_read = null;
            throw ex;
        }

        return ret;
    }

    /**
     * Считывание данных из базы. Встроена возможность поиска по БД в национальных кодировках.
     * @param stColumn Хранилище для считываемых данных. Хранилище дополняется данными, поэтому перед
     * использованием метода считывания данных необходимо очистить Хранилище от устаревших данных.
     * Так же предидущее самостоятельное заполнении Хранилища может привести к не корректному
     * дополнению данных, поэтому возможность дополнения данных требует понимания внутреннего
     * механизма хранения данных в Хранилище, и лучше ею не пользоваться :)
     * @param tableName имя таблицы БД
     * @param whereSt Условие считывания данных. Часть SQL запроса начиная с "WHERE ...".
     * При использовании возможности поиска данных в национальных кодировках в строке запроса
     * вместо данных в национальных кодировках указывается символ -- ?
     * @param ncharVal Вектор содержащий данные для поиска в национальных кодировках.
     * Порядок параметров в строке запроса whereSt и данных в векторе для национальных
     * кодировок должен совпадать.
     * @throws Exception Любые ошибки формирования запроса и считывания данных из БД
     * перенаправляются наружу, а так же сбрасываются в лог ошибок. После успешного считывания
     * данных любые ошибки завершения работы метода сбрасываются в лог ошибок, отработка метода
     * считается успешной.
     */
    public void readStNCHAR(modelDbStore stColumn, String tableName, String whereSt, Vector ncharVal) throws Exception
    {
        IS_NVARCHAR = true;
        OraclePreparedStatement statement=null;
        ResultSet resultSet=null;

        try
        {
            receiveInfo(stColumn, tableName, "");

            String query = stColumn.getSelect();
            query += "FROM " + tableName;
            if(whereSt != null || !whereSt.equals("")) query += " " + whereSt;
//System.out.println(query);
            statement = (OraclePreparedStatement)connection.prepareStatement(query);
            for (int i = 0;ncharVal != null && i < ncharVal.size(); i++)
                setStatementObject(OracleTypes.VARCHAR, ncharVal.elementAt(i), i + 1, statement);
            resultSet = statement.executeQuery();

            fillStoreFromResultSet(stColumn, resultSet, 0, 0);
        }
        catch (Exception ex)
        {
//      ex.printStackTrace();
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
                statement.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
    }

    public void read(modelDbStore stColumn, String tableName, String where, typesAndValues whereValues) throws Exception
    {
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        ResultSetMetaData  metaData=null;

        try
        {
            stColumn.setIdentifierQuoteString(identifierQuoteString);
//      receiveInfo(stColumn, tableName, "");

            String query = stColumn.getSelect();
            query += "FROM " + tableName;
            if(where != null || !where.equals("")) query += " " + where;
//System.out.println(query);
            statement = (PreparedStatement)connection.prepareStatement(query);
            for (int i = 0;whereValues != null && i < whereValues.getCount(); i++)
                setStatementObject(whereValues.getType(i), whereValues.getValue(i), i + 1, statement);
            resultSet = statement.executeQuery();

            metaData = resultSet.getMetaData();

            String[] clnm = new String[metaData.getColumnCount()];

            for (int i = 0; i < clnm.length; i++)
            {
                clnm[i] = metaData.getColumnLabel( i + 1 ).toLowerCase();
            }
            stColumn.setInit(clnm);

            for (int i = 0; i < stColumn.getColumnCount(); i++)
            {
                stColumn.setType( i, metaData.getColumnType( i + 1 ) );
                stColumn.setTypeName(i, metaData.getColumnTypeName(i+1) );
                stColumn.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                if(stColumn.getType(i) != Types.BLOB && stColumn.getType(i) != Types.CLOB) stColumn.setPrecision(i, metaData.getPrecision(i+1) );
                else stColumn.setPrecision(i, 0);
                stColumn.setScale(i, metaData.getScale(i+1) );
                stColumn.setNullable(i, metaData.isNullable(i+1) );
                stColumn.setAutoIncrement(i, metaData.isAutoIncrement(i+1) );
            }
            fillStoreFromResultSet(stColumn, resultSet, 0, 0);
        }
        catch (Exception ex)
        {
//      ex.printStackTrace();
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
                if(statement != null) statement.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
    }

    public void read(String Select, typesAndValues whereValues, modelDbStore st) throws Exception
    {
        read(Select, whereValues, st, 0, 0);
    }
    public int read(String Select, typesAndValues whereValues, modelDbStore st, int skipRow, int rowCount) throws Exception
    {
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        ResultSetMetaData  metaData=null;
        int countrowinselect = 0;
        try
        {
//      receiveInfo(st, tableName, "");

//      for (int i = 0; whereValues != null && whereValues.getCount() > i; i++) {
//        System.out.println("type : " + whereValues.getType(i) + " value: " + whereValues.getValue(i) );
//      }
//      System.out.println(Select);

            statement = (PreparedStatement)connection.prepareStatement(Select);
            for (int i = 0;whereValues != null && i < whereValues.getCount(); i++)
                setStatementObject(whereValues.getType(i), whereValues.getValue(i), i + 1, statement);
            resultSet = statement.executeQuery();

            metaData = resultSet.getMetaData();

            String[] clnm = new String[metaData.getColumnCount()];

            for (int i = 0; i < clnm.length; i++)
            {
                clnm[i] = metaData.getColumnLabel( i + 1 ).toLowerCase();
            }
            st.setInit(clnm);
            st.setIdentifierQuoteString(identifierQuoteString);

            for (int i = 0; i < st.getColumnCount(); i++)
            {
                st.setType( i, metaData.getColumnType( i + 1 ) );
                st.setTypeName(i, metaData.getColumnTypeName(i+1) );
                st.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                if(st.getType(i) != Types.BLOB && st.getType(i) != Types.CLOB) st.setPrecision(i, metaData.getPrecision(i+1) );
                else st.setPrecision(i, 0);
                st.setScale(i, metaData.getScale(i+1) );
                st.setNullable(i, metaData.isNullable(i+1) );
                st.setAutoIncrement(i, metaData.isAutoIncrement(i+1) );
            }
            boolean is_next = fillStoreFromResultSet(st, resultSet, skipRow, rowCount);
            countrowinselect = skipRow + st.getRowCount();
            while(is_next && resultSet.next()) countrowinselect++;
        }
        catch (Exception ex)
        {
//      ex.printStackTrace();
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
                statement.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
        return countrowinselect;
    }

    /**
     * Получение выбрки по строке запроса.
     * @param query String - строка запроса.
     * @param stWhere Table - значения для параметризированного запроса
     * (stWhere = null - не параметризированный запрос)
     * @param rr int - номер строки в stWhere для формирования выборки
     * (rr = -1 - выборка по всем строкам)
     * @param columnsWhere String - список колонок в порядке подстановки в запрос из stWhere
     * (columnsWhere = null - подстановка значений по всем колонкам из stWhere)
     * @param st - результат выборки
     * @throws Exception
     */
    public void read(String query, modelDbStore stWhere, int rowWhere, String columnsWhere, modelDbStore st) throws Exception
    {
        PreparedStatement stm = null;
        ResultSetMetaData  metaData = null;
        try {
            String[] clmnNm = null;
            if(stWhere != null && stWhere.getColumnCount() > 0)
            { // Список параметров выборки в запросе
                if(columnsWhere != null && columnsWhere.length() > 0) clmnNm = columnsWhere.toLowerCase().split(",");
                else
                {
                    clmnNm = new String[stWhere.getColumnCount()];
                    for (int i = 0; i < clmnNm.length; i++) {
                        clmnNm[i] = stWhere.getColumnName(i);
                    }
                }
            }
            else rowWhere = 0;

            stm = connection.prepareStatement(query);

            int rcb = (rowWhere == -1 ? 0 : rowWhere);
            int rce = (rowWhere == -1 ? stWhere.getRowCount() : rowWhere + 1);

            for (int rc = rcb; rc < rce; rc++)
            {
                if(stWhere != null && stWhere.getColumnCount() > 0)
                { // Установка параметров выборки в запросе
                    for (int i = 0; clmnNm != null && i < clmnNm.length; i++)
                    {
                        int icl = stWhere.getColumnIndex(clmnNm[i]);
                        setStatementObject(stWhere.getType(icl),
                                stWhere.getObject(rc, icl), i + 1, stm);
                    }
                }

                ResultSet results = stm.executeQuery();

                if(rc == rcb)
                {
                    metaData = results.getMetaData();
                    String[] clnm = new String[metaData.getColumnCount()];
                    for (int i = 0; i < clnm.length; i++)
                    {
                        clnm[i] = metaData.getColumnLabel( i + 1 ).toLowerCase();
                    }
                    st.setInit(clnm);
                    st.setIdentifierQuoteString(identifierQuoteString);

                    for (int i = 0; i < st.getColumnCount(); i++)
                    {
                        st.setType( i, metaData.getColumnType( i + 1 ) );
                        st.setTypeName(i, metaData.getColumnTypeName(i+1) );
                        st.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                        if(st.getType(i) != Types.BLOB && st.getType(i) != Types.CLOB) st.setPrecision(i, metaData.getPrecision(i+1) );
                        else st.setPrecision(i, 0);
                        st.setScale(i, metaData.getScale(i+1) );
                        st.setNullable(i, metaData.isNullable(i+1) );
                        st.setAutoIncrement(i, metaData.isAutoIncrement(i+1) );
                    }
                }

                fillStoreFromResultSet(st, results, 0, 0);
                results.close();
            }
            stm.close();
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try {
                if(stm != null) stm.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
    }

    public int update(String query, modelDbStore stWhere, int rowWhere, String columnsWhere) throws Exception
    {
        int ret = 0;
        PreparedStatement stm = null;
//    ResultSetMetaData  metaData = null;
        try {
            String[] clmnNm = null;
            if(stWhere != null && stWhere.getColumnCount() > 0)
            { // Список параметров выборки в запросе
                if(columnsWhere != null && columnsWhere.length() > 0) clmnNm = columnsWhere.toLowerCase().split(",");
                else
                {
                    clmnNm = new String[stWhere.getColumnCount()];
                    for (int i = 0; i < clmnNm.length; i++) {
                        clmnNm[i] = stWhere.getColumnName(i);
                    }
                }
            }
            else rowWhere = 0;

            stm = connection.prepareStatement(query);

            int rcb = (rowWhere == -1 ? 0 : rowWhere);
            int rce = (rowWhere == -1 ? stWhere.getRowCount() : rowWhere + 1);

            for (int rc = rcb; rc < rce; rc++)
            {
                if(stWhere != null && stWhere.getColumnCount() > 0)
                { // Установка параметров выборки в запросе
                    for (int i = 0; clmnNm != null && i < clmnNm.length; i++)
                    {
                        int icl = stWhere.getColumnIndex(clmnNm[i]);
                        setStatementObject(stWhere.getType(icl),
                                stWhere.getObject(rc, icl), i + 1, stm);
                    }
                }

                ret = ret + stm.executeUpdate();
            }
//      stm.close();
            if(isCommitTransaction) connection.commit();
        }
        catch (Exception ex)
        {
            try {  connection.rollback();  } catch (Exception exx) { }
            throw ex;
        }
        finally
        {
            try {
                if(stm != null) stm.close();
            }
            catch (Exception ex)  {  ex.printStackTrace();  }
        }
        return ret;
    }

    /**
     * Заполнение Store из ResultSet
     * @param st заполняемое Store
     * @param resultSet возвращённый базой результат запроса
     * @param bufferSize число считываемых за раз строк (если bufferSize = 0 то
     * считываются все данные)
     * @return false - если прочитанны все данные; true - если данные из ResultSet
     * прочитанны не все
     * @throws Exception ошибка при считывании данных из ResultSet или
     * заполнении Store
     */
    private boolean fillStoreFromResultSet(modelDbStore st, ResultSet resultSet, int skipRow, int bufferSize) throws Exception
    {
        int row_p = st.getRowCount();
        int pn = 0;
        boolean ret = true;
        for (int j = 0; j < skipRow; j++) {
            if(!resultSet.next()) return false;
        }
        while (ret && (bufferSize == 0 || pn < bufferSize))
        {
            if(ret = resultSet.next())
            {
                for (int i = 0; i < st.getColumnCount(); i++)
                    switch (st.getType(i)) {
                        case Types.BLOB:
                            BLOB blob = ( (OracleResultSet) resultSet).getBLOB(i + 1);
                            if (blob != null) {
                                java.io.InputStream instream = blob.getBinaryStream();
                                byte[] buffer = new byte[ (int) blob.length()];
                                instream.read(buffer);
                                st.setObject(row_p, i, buffer);
                            }
                            else
                                st.setObject(row_p, i, new byte[] {});
                            break;
                        case Types.CLOB:
                            Clob clob = (resultSet).getClob(i + 1);
                            if (clob != null) {
                                java.io.Reader instream = clob.getCharacterStream();
                                char[] buffer = new char[ (int) clob.length()];
                                instream.read(buffer);
                                st.setObject(row_p, i, buffer);
                            }
                            else
                                st.setObject(row_p, i, new char[] {});
                            break;
                        case Types.DATE:
                        case Types.TIMESTAMP:
                            st.setObject(row_p, i, resultSet.getTimestamp(i + 1));
                            break;
                        default:

                            //System.out.println(st.getColumnName(i) + "[" + st.getType(i) + "]:" + st.getObject(row_p, i));
                            st.setObject(row_p, i, resultSet.getObject(i + 1));
                            break;
                    }
                row_p++;
                pn++;
            }
        }
        return ret;
    }

    /**
     * Считывание служебной информации из БД по таблице, структура которой задана в
     * Хранилище. Данный метод должен быть вызван в обязательном порядке для редактируемых
     * таблиц Хранилища. Это обеспечит работу блока проверки входной информации.
     * В данном случае входная информация может быть проверенна на возможность корректного
     * помещения её в БД. При просмотре данных использование этого метода излишне.
     * @param stColumn Хранилище данных
     * @param tableName имя таблицы БД
     * @param dir имя каталога таблиц БД VFP, в общем случае -- ""
     * @throws Exception Все ошибки возникающие при получении служебной информации по
     * таблице, структура которой уже помещеня в Хранилище
     */
    public void receiveInfo(modelDbStore stColumn, String tableName, String dir) throws Exception
    { // modelDbStore
        Statement statement=null;
        ResultSet resultSet=null;
        Statement statement2=null;
        ResultSet resultSet2=null;
        ResultSetMetaData  metaData=null;

//    SELECT table_name,column_name,comments FROM USER_COL_COMMENTS WHERE table_name='EXT_VAGNO'

//    select table_name,comments from user_tab_comments
//    select table_name,column_name,comments from user_col_comments

//    SELECT owner,table_name,column_name,data_type,data_length,data_precision,data_scale,
//    nullable,char_length
//    FROM all_tab_columns WHERE owner='REPORT' AND table_name='EXT_VAGNO'

        try
        {
            stColumn.setIdentifierQuoteString(identifierQuoteString);
            statement = connection.createStatement();
            statement2 = connection.createStatement();

            String query = stColumn.getSelect();
            query += "FROM " + dir + tableName + " WHERE 1=0";

//System.out.println(query);
            resultSet = statement.executeQuery(query);
            metaData = resultSet.getMetaData();
            for (int i = 0; i < stColumn.getColumnCount(); i++)
            {
                stColumn.setType(i, metaData.getColumnType(i+1) );

                String cc = metaData.getColumnTypeName(i+1);
                stColumn.setTypeName(i, cc );

                if(IS_NVARCHAR &&
                        stColumn.getType(i) == Types.VARCHAR &&
                        productName.equalsIgnoreCase("Oracle"))
                {
                    try
                    { // Блок инициализации типа NVARCHAR2
                        resultSet2 = statement2.executeQuery("SELECT data_length,data_type,char_length FROM all_tab_columns WHERE owner='"+
                                sqlSchema.toUpperCase()+"' AND table_name='"+tableName.toUpperCase()+"' AND column_name='"+
                                stColumn.getColumnName(i).toUpperCase()+"'");
                        if(resultSet2.next())
                        {
                            stColumn.setDisplaySize(i, resultSet2.getInt("char_length"));
                            stColumn.setDataLength(i, resultSet2.getInt("data_length"));
                            stColumn.setTypeName(i, resultSet2.getString("data_type"));
                        }
                        resultSet2.close();
                    }
                    catch (Exception ex)
                    {
                        stColumn.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                    }
                }
                else
                {
                    stColumn.setDisplaySize(i, metaData.getColumnDisplaySize(i+1) );
                }

                if(stColumn.getType(i) != Types.BLOB && stColumn.getType(i) != Types.CLOB) stColumn.setPrecision(i, metaData.getPrecision(i+1) );
                else stColumn.setPrecision(i, 0);
                stColumn.setScale(i, metaData.getScale(i+1) );
                stColumn.setNullable(i, metaData.isNullable(i+1) );
                stColumn.setAutoIncrement(i, metaData.isAutoIncrement(i+1) );
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
            }
            catch (Exception ex) {  ex.printStackTrace();  }
            try
            {
                statement.close();
                statement2.close();
            }
            catch (Exception ex) {  ex.printStackTrace();  }
        }
    }

    /**
     * \uFFFD\uFFFDсполнение запроса к БД
     * @param query SQL запрос к БД
     * @return Число записей над которыми был выполнен запрос к БД
     * @throws Exception Ошибки возникшие во время исполнения запроса
     */
    public int runQuery(String query) throws Exception
    {
        Statement          statement=null;
        int                upd = 0;
        try
        {
            statement = connection.createStatement();
//System.out.println(query);
            upd = statement.executeUpdate(query);
            if(isCommitTransaction) connection.commit();
        }
        catch (Exception ex)
        {
            try {  connection.rollback();  } catch (Exception exx) { }
            throw ex;
        }
        finally
        {
            try
            {
                statement.close();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
        return upd;
    }

    /**
     * Метод позволяющий вернуть значение полученное из последовательности для вставки
     * его в строку тригером повешенным на вставку строки в таблицу. В основном
     * используется для связывания строк нескольких таблиц электронным ключём.
     * @param query SQL запрос
     * @param sequenceNm Название последовательности возвращающей значения для
     * последующей вставки их тригером в строку
     * @return Вставленное в строку тригером значение (электронный ключ)
     * @throws Exception Если чего случилось до момента завершения запроса.
     */
//  public Object backValueSequenceFromInsert(String query, String sequenceNm) throws Exception
//   {
//    Statement          statement=null;
//    ResultSet          resultSet=null;
//    Object obj = "";
//    try
//     {
//      statement = connection.createStatement();
//      if(statement.executeUpdate(query) == 1)
//       {
//        resultSet = statement.executeQuery("SELECT "+sequenceNm+".CURRVAL FROM dual");
//        if(resultSet.next()) obj = resultSet.getObject(1);
//        if(isCommitTransaction) connection.commit();
//       }
//      else connection.rollback();
//     }
//    catch (Exception ex)
//     {
//      ex.printStackTrace();
//      try {  connection.rollback();  } catch (Exception exx) { }
//      throw ex;
//     }
//    finally
//     {
//      try
//       {
//        if(resultSet != null) resultSet.close();
//        statement.close();
//       }
//      catch (Exception ex) { ex.printStackTrace(); }
//     }
//    return obj;
//   }

    /**
     * Вставка строки из хранилища в базу
     * @param st хранилище данных
     * @param rowStore вставляемая строка хранилища
     * @param tableName имя таблицы базы в которую вставляется строка
     * @param sequenceNm список последовательностей текущие значения которых будут
     * возвращены после выполнения вставки строки
     * @return число вставленных строк, по идее одна ...
     */
    public int insertSt(modelDbStore st, int rowStore, String tableName) throws Exception
    {
        return ((Integer)insertSt( st, rowStore, tableName, null).elementAt(0)).intValue();
    }
    /**
     * Вставка строки из хранилища в базу
     * @param st хранилище данных
     * @param rowStore вставляемая строка хранилища
     * @param tableName имя таблицы базы в которую вставляется строка
     * @param sequenceNm список последовательностей текущие значения которых будут
     * возвращены после выполнения вставки строки
     * @return текущие значения последовательностей sequenceNm
     */
    public Vector insertSt(modelDbStore st, int rowStore, String tableName,
                           String[] sequenceNm) throws Exception
    {
        Vector rt = new Vector();
        PreparedStatement pstmt = null;
        Statement          statement=null;
        ResultSet          resultSet=null;
        try
        {
            if(st.getType(0) == Types.OTHER) receiveInfo(st, tableName, "");

            pstmt = (PreparedStatement)connection.prepareStatement
                    ("INSERT INTO "+tableName+" "+st.getInsertForStatement());
            int j=1;
            for (int i = 0; i < st.getColumnCount(); i++)
            {
                if(!st.getColumnName(i).equals("rowidtochar(rowid)"))
                {
                    if ( st.getType(i) != Types.BLOB )
                    {
                        setStatementObject( st.getType(i),  st.getObject(rowStore,i), j, pstmt );
                        j++;
                    }
                }
            }
            int zz = pstmt.executeUpdate();
            pstmt.close();
            writeStatementBlob( st, tableName, rowStore );

            if(sequenceNm == null)
            {
                rt.addElement(new Integer(zz));
            }
            else
            { // Вытаскиваем из базы текущие значения sequence
                statement = connection.createStatement();
                for (int i = 0; i < sequenceNm.length; i++)
                {
                    resultSet = statement.executeQuery("SELECT "+sequenceNm[i]+".CURRVAL FROM dual");
                    if(resultSet.next()) rt.addElement( resultSet.getObject(1) );
                    resultSet.close();
                }
                statement.close();
            }
            if(isCommitTransaction) connection.commit();
        }
        catch (Exception ex)
        {
//      ex.printStackTrace();
            try {  connection.rollback();  } catch (Exception exx) { }
            throw ex;
        }
        finally
        {
            try
            {
                if(pstmt != null) pstmt.close();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
        return rt;
    }

    public Object[] insert(modelDbStore st, String tableName) throws Exception
    {
        return   insert(st, 0, st.getRowCount() - 1, tableName, null);
    }
    public Object[] insert(modelDbStore st, String tableName, sequenceFields sequences) throws Exception
    {
        return   insert(st, 0, st.getRowCount() - 1, tableName, sequences);
    }
    /**
     * Вставка массива строки из хранилища в базу
     * @param st хранилище данных
     * @param beginRow номер первой строки вставляемого массива строк
     * @param endRow номер последней строки вставляемого массива строк, включительно
     * @param tableName имя таблицы базы в которую вставляется массив строк
     * @param sequenceNm список последовательностей значения которых будут полученны
     * и вставленны в массив строк перед записью в БД
     * @return {Integer numRow, null} -- numRow - число вставленных строк; второй
     * параматр равен null в случае успешной вставки массива в БД<br/>
     * {Integer numRow, Exception ex} -- numRow - номер строки вызвавшей в момент
     * вставки исключение; ex - исключение вызванное в момент неуспешной вставки строки
     */
    public Object[] insert(modelDbStore st, int beginRow, int endRow, String tableName,
                           sequenceFields sequences) throws Exception
    {
        Object[] rt = {null, null};
        int numRow = 0;
        PreparedStatement pstmt = null;
        Statement          statement=null;
        ResultSet          resultSet=null;
        try
        {
            if(st.getType(0) == Types.OTHER) receiveInfo(st, tableName, "");
            st.setIdentifierQuoteString(identifierQuoteString);

            String[] gk = null;
            if(sequences != null)
            { // Заполнение ключевых полей из счётчиков
                statement = connection.createStatement();
                for (int r = beginRow; r <= endRow; r++)
                { // Заполнение Хранилища значениями из указанных Последовательностей
                    for (int i = 0; i < sequences.getSequenceCount(); i++)
                    {
//            String col_nm = sequences.getColumnName(i);
//            String seq_nm = sequences.getSequenceName(i);
//            if(seq_nm != null && seq_nm.length() > 0 && !seq_nm.equalsIgnoreCase("GENERATED_KEY"))
//            {
                        if(productName.toUpperCase().startsWith("DB2"))
                        {
                            resultSet = statement.executeQuery("VALUES NEXTVAL FOR " + sequences.getSequenceName(i));
                        }
                        else
                        {
                            resultSet = statement.executeQuery("SELECT " + sequences.getSequenceName(i) +
                                    ".NEXTVAL FROM dual");
                        }
                        if (resultSet.next())
                        {
                            st.setObject(r, st.getColumnIndex(sequences.getColumnName(i)), resultSet.getObject(1));
                        }
                        resultSet.close();
//            }
//            else gk = new String[] {sequences.getColumnName(i)};
                    }
                }
                statement.close();
            }
            if(st.getAutoIncrementCol() != null)
            { // Автоинкрементное поле
                gk = new String[] {st.getAutoIncrementCol()};
            }

//System.out.println("INSERT INTO "+tableName+" "+st.getInsertForStatement());
            /** @todo Специально для версии драйвера mySql 5.1.7
             */
            if(gk == null) pstmt = connection.prepareStatement("INSERT INTO "+tableName+" "+st.getInsertForStatement());
            else pstmt = connection.prepareStatement("INSERT INTO "+tableName+" "+st.getInsertForStatement(), gk);

            for (numRow = beginRow; numRow <= endRow; numRow++)
            {
                int j = 1;
                for (int i = 0; i < st.getColumnCount(); i++)
                {
                    if (!st.getAutoIncrement(i) && !st.getColumnName(i).equals("rowidtochar(rowid)") && st.getType(i) != Types.BLOB)
                    {
//System.out.println( st.getColumnName(i) + " = " + st.getObject(numRow, i) + "; type = " + st.getType(i));
                        setStatementObject(st.getType(i), st.getObject(numRow, i), j, pstmt);
                        j++;
                    }
                }
                int zz = pstmt.executeUpdate();
                if(st.getAutoIncrementCol() != null)
                { // Получение автоинкрементного поля
                    resultSet = pstmt.getGeneratedKeys();
                    if (resultSet.next())
                    {
                        st.setObject(numRow, st.getColumnIndex(st.getAutoIncrementCol()), resultSet.getObject(1));
                    }
                    resultSet.close();
/*          for (int i = 0; i < sequences.getSequenceCount(); i++)
          {
            String col_nm = sequences.getColumnName(i);
            String seq_nm = sequences.getSequenceName(i);
            if(seq_nm == null || seq_nm.length() == 0 || seq_nm.equalsIgnoreCase("GENERATED_KEY"))
            { // Если последовательность GENERATED_KEY
              resultSet = pstmt.getGeneratedKeys();
//System.out.println(   resultSet.getMetaData().getColumnCount() );
//System.out.println(   resultSet.getMetaData().getColumnName(1) );
              Object gkk = null;
              if (resultSet.next())
              {
                gkk = resultSet.getObject(seq_nm);
                System.out.println(gkk);
                st.setObject(numRow, st.getColumnIndex(col_nm), gk);
              }
              resultSet.close();
            }
          }*/
                }
                writeStatementBlob(st, tableName, numRow);
            }
            if(isCommitTransaction) connection.commit();
            rt[0] = new Integer(numRow - beginRow);
        }
        catch (Exception ex)
        {
//ex.printStackTrace();
            try {  connection.rollback();  } catch (Exception exx) { }
            rt[0] = new Integer(numRow);
            rt[1] = ex;
            throw ex;
        }
        finally
        {
            try
            {
                if(pstmt != null) pstmt.close();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
        return rt;
    }


    /**
     * Производится обновление строки по ключу where, может быть обновленна
     * только одна строка, при задании не уникального условия для обновления строки
     * буде производиться откат
     * @param st хранилище данных
     * @param rowStore обновляемая строка хранилища
     * @param tableName имя таблицы базы в которой будет происходить обновление
     * @param where условие по которому будет происходить обновление
     * @return число обновлённых строк, по идее одна
     */
    public int updateSt(modelDbStore st, int rowStore, String tableName, String where) throws Exception
    {
        int rt = 0;
        PreparedStatement pstmt = null;
        try
        {
            if(st.getType(0) == Types.OTHER) receiveInfo(st, tableName, "");
//System.out.println("UPDATE "+tableName+" "+st.getUpdateForStatement() +" "+where);
            pstmt = (PreparedStatement)connection.prepareStatement
                    ("UPDATE "+tableName+" "+st.getUpdateForStatement() +" "+where);
            int j=0;
            for (int i = 0; i < st.getColumnCount(); i++)
            {
                if(!st.getColumnName(i).equals("rowidtochar(rowid)"))
                {
                    if ( st.getType(i) != Types.BLOB )
                    {
                        setStatementObject( st.getType(i),  st.getObject(rowStore,i), j+1, pstmt );
                        j++;
                    }
                }
            }
            rt = pstmt.executeUpdate();
            pstmt.close();
            writeStatementBlob( st, tableName, rowStore );

            if(isCommitTransaction) connection.commit();
      /*if(rt == 1) connection.commit();
      else
      {
        rt = -1;
        connection.rollback();
      }*/
        }
        catch (Exception ex)
        {
//      ex.printStackTrace();
            try {  connection.rollback();  } catch (Exception exx) { }
            throw ex;
        }
        finally
        {
            try
            {
                if(pstmt != null) pstmt.close();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
        return rt;
    }
    /**
     * Производится обновление строки по уникальному ключу хранилища, если значение
     * ключа хранилища не уникально обновление строки не состоится, будет произведен
     * откат
     * @param st хранилище данных
     * @param rowStore обновляемая строка хранилища
     * @param tableName имя таблицы базы в которой будет происходить обновление
     * @return число обновлённых строк, по идее одна
     */
    public int updateSt(modelDbStore st, int rowStore, String tableName) throws Exception
    {
        if(st.getType(0) == Types.OTHER) receiveInfo(st, tableName, "");
        return updateSt(st, rowStore, tableName, st.getKeyWhere(rowStore));
    }

    /**
     * Удаление из базы
     * @param tableName имя таблицы базы из которой удаляется строка
     * @param where условие для удаления
     * @return true - удаление произошло успешно <BR>
     * false - ошибка при выполнении удаления
     */
    public boolean delete(String tableName, String where) throws Exception
    {
        Statement          statement=null;
        boolean rt = false;
        if(where != null && where.length() > 0)
            try
            {
                statement = connection.createStatement();
                int upd = statement.executeUpdate("DELETE FROM "+tableName+" "+ where);
//       if(upd == 1)
//       {
                if(isCommitTransaction) connection.commit();
                rt = true;
//       }
//       else connection.rollback();
            }
            catch (Exception ex)
            {
//       ex.printStackTrace();
                try {  connection.rollback();  } catch (Exception exx) { }
                throw ex;
            }
            finally
            {
                try
                {
                    if(statement != null) statement.close();
                }
                catch (Exception ex) { ex.printStackTrace(); }
            }
        return rt;
    }
    /**
     * Удаление строки помещённой в хранилище из базы,  удаление не выполнится
     * если составленное условие для SQL запроса указанной строки выберет больше
     * одной строки
     * @param st хранилище
     * @param rowStore удаляемая строка хранилища
     * @param tableName имя таблицы базы из которой удаляется строка
     * @return true - удаление произошло успешно <BR>
     * false - ошибка при выполнении удаления
     */
    public boolean deleteSt(modelDbStore st, int rowStore, String tableName) throws Exception
    {
        if(st.getType(0) == Types.OTHER) receiveInfo(st, tableName, "");
        return delete(tableName, st.getKeyWhere( rowStore ));
    }

    public int saveSt(modelDbStore st, String tableName) throws Exception
    {
        int s_row = 0;
        PreparedStatement pstmt_i = null;
        PreparedStatement pstmt_u = null;
        Statement          statement = null;
        ResultSet          resultSet = null;

        try
        {
            if(st.getType(0) == Types.OTHER) receiveInfo(st, tableName, "");

            String selectFrom = st.getSelect() + "FROM " + tableName;

            String prepareQuery_i;
            String prepareQuery_u;
            prepareQuery_u = "UPDATE " + tableName + " " + st.getUpdateForStatement();
            prepareQuery_i = "INSERT INTO " + tableName + " " + st.getInsertForStatement();

            statement = connection.createStatement();
            pstmt_i = connection.prepareStatement( prepareQuery_i );

            for (int rowStore = 0; rowStore < st.getRowCount(); rowStore++)
            { // Цикл по строкам Хранилища

                String where = st.getKeyWhere(rowStore);
                String query = selectFrom + ((where != null && where.length() > 0)?" " + where:"");
                resultSet = statement.executeQuery(query);

                if(resultSet.next())
                { // Обновление строки
                    pstmt_u = connection.prepareStatement( prepareQuery_u + " " + where );
                    int j = 0;
                    for (int k = 0; k < st.getColumnCount(); k++)
                    {
                        if (!st.getColumnName(k).equals("rowidtochar(rowid)"))
                        {
                            if (st.getType(k) != Types.BLOB)
                            {
                                setStatementObject(st.getType(k), st.getObject(rowStore, k), j + 1, pstmt_u);
                                j++;
                            }
                        }
                    }
                    if(pstmt_u.executeUpdate() != 1)
                    { // Откат при не уникальности ключа строки (Облом всего сохранения)
                        connection.rollback();
                        return -1;
                    }
                    pstmt_u.close();
                }
                else
                { // Вставка новой строки
                    int j = 0;
                    for (int k = 0; k < st.getColumnCount(); k++)
                    {
                        if (!st.getColumnName(k).equals("rowidtochar(rowid)"))
                        {
                            if (st.getType(k) != Types.BLOB)
                            {
                                setStatementObject(st.getType(k), st.getObject(rowStore, k), j + 1, pstmt_i);
                                j++;
                            }
                        }
                    }
                    pstmt_i.executeUpdate();
                }
                resultSet.close();

                writeStatementBlob(st, tableName, rowStore);
            }
            statement.close();
            pstmt_i.close();

            if(isCommitTransaction) connection.commit();
        }
        catch (Exception ex)
        {
//      ex.printStackTrace();
            try {  connection.rollback();  } catch (Exception exx) { }
            throw ex;
        }
        finally
        {
            try
            {
                if(pstmt_i != null) pstmt_i.close();
                if(pstmt_u != null) pstmt_u.close();
                if(statement != null) statement.close();
            }
            catch (Exception ex) { }
        }
        return s_row;
    }


    public int[] save(String tbName, modelDbStore st, int row, sequenceFields sequences) throws Exception
    {
        int ret[] = {0,0,0};
        PreparedStatement stm_s = null;
        PreparedStatement stm_u = null;
        PreparedStatement stm_i = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = connection.createStatement();
            if(st.getType(0) == Types.OTHER) receiveInfo(st, tbName, "");
            String qselect = "SELECT * FROM " + tbName + " " + st.getWhereForStatement();
            String qinsert = "INSERT INTO " + tbName + " " + st.getInsertForStatement();
            String qupdate = "UPDATE " + tbName + " " + st.getUpdateForStatement() + " " + st.getWhereForStatement();
            int rcb = (row == -1 ? 0 : row);
            int rce = (row == -1 ? st.getRowCount() : row + 1);
            String[] gk = null;
            for (int i = 0; sequences != null && i < sequences.getSequenceCount(); i++)
            {
                int ci = st.getColumnIndex(sequences.getColumnName(i));
                if(!st.getAutoIncrement(ci))
                { // Подстановка значений из последовательностей
                    String query1 = null;
                    if (productName.toUpperCase().indexOf("ORACLE") != -1) {
                        query1 = "SELECT " + sequences.getSequenceName(i) + ".NEXTVAL FROM dual";
                    }
                    else if (productName.toUpperCase().startsWith("DB2")) {
                        query1 = "VALUES NEXTVAL FOR " + sequences.getSequenceName(i);
                    }
                    if (query1 != null)
                        for (int r = rcb; r < rce; r++) {
                            // Сохраняем значение
                            Object ob1 = st.getObject(r, ci);
                            String val1 = (ob1 == null ? "" : ob1.toString());
                            if (val1.length() == 0 || val1.startsWith("-") || val1.equals("0"))
                            { // Поле необходимо заполнить новым значением
                                resultSet = statement.executeQuery(query1);
                                if (resultSet.next()) {
                                    Object ob2 = resultSet.getObject(1);
                                    st.setObject(r, ci, ob2);
                                }
                                if (resultSet != null) resultSet.close();
                            }
                        }
                }
            }
            if(statement != null) statement.close();
            if(st.getAutoIncrementCol() != null && st.getAutoIncrementCol().length() > 0)
            { // Автоинкрементное поле
                gk = new String[] {st.getAutoIncrementCol()};
            }
            if(gk == null) stm_i = connection.prepareStatement(qinsert);
            else stm_i = connection.prepareStatement(qinsert, gk);
            if(st.getWhereColumnCount() > 0)
            { // Обновление записей при наличии ключевых колонок
                stm_s = connection.prepareStatement(qselect);
                stm_u = connection.prepareStatement(qupdate);
            }
            //int rret = 0;
            for (int rc = rcb; rc < rce; rc++)
            { // Строки
                boolean is_update = false;
                if(st.getWhereColumnCount() > 0)
                { // Обновление записей при наличии ключевых колонок
                    for (int i = 0; i < st.getWhereColumnCount(); i++)
                    {
                        int wi = st.getWhereColumnIndex(i);
                        setStatementObject(st.getType(wi) ,st.getObject(rc, wi), i+1, stm_s);
                    }
                    resultSet = stm_s.executeQuery();
                    is_update = resultSet.next();
                    if (resultSet != null) resultSet.close();
                }
                if(is_update)
                { // Обновление
                    int j = 1;
                    for (int i = 0; i < st.getColumnCount(); i++)
                    {
                        if( st.getType(i) != Types.BLOB )
                        {
                            setStatementObject( st.getType(i), st.getObject(rc,i), j, stm_u );
                            j++;
                        }
                    }
                    for (int i = 0; i < st.getWhereColumnCount(); i++)
                    {
                        int wi = st.getWhereColumnIndex(i);
                        setStatementObject(st.getType(wi) ,st.getObject(rc, wi), i+j, stm_u);
                    }
                    ret[1] += stm_u.executeUpdate();
                    writeStatementBlob(st, tbName, rc);
                }
                else
                { // Вставка
                    int j = 1;
                    for (int i = 0; i < st.getColumnCount(); i++)
                    {
                        if( !st.getAutoIncrement(i) && st.getType(i) != Types.BLOB )
                        {
                            setStatementObject( st.getType(i), st.getObject(rc,i), j, stm_i );
                            j++;
                        }
                    }
                    ret[0] += stm_i.executeUpdate();
                    if(st.getAutoIncrementCol() != null && st.getAutoIncrementCol().length() > 0)
                    { // Получение автоинкрементного поля
                        resultSet = stm_i.getGeneratedKeys();
                        if (resultSet.next())
                        {
                            st.setObject(rc, st.getColumnIndex(st.getAutoIncrementCol()), resultSet.getObject(1));
                        }
                        resultSet.close();
                    }
                    writeStatementBlob(st, tbName, rc);
                }
                ret[2]++;
            }
            if(isCommitTransaction) connection.commit();
        }
        catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ex) { }

            throw e;
        }
        finally
        {
            if(stm_s != null) stm_s.close();
            if(stm_u != null) stm_u.close();
            if(stm_i != null) stm_i.close();
        }
        return ret;
    }


    /**
     *
     * @param st Хранилище
     * @param rowStore срока формирования ключа выборки
     * @param tableName имя таблицы
     * @return количество строк выборки БД по ключу Хранилища
     * @throws Exception
     */
    public int getRowCount(modelDbStore st, int rowStore, String tableName) throws Exception
    {
        String where = st.getKeyWhere(rowStore);
        return getRowCount(tableName, where);
    }
    public int getRowCount(String tableName, String where) throws Exception
    {
        int count = 0;
        Statement          statement = null;
        ResultSet          resultSet = null;
        try
        {
            String selectFrom = "SELECT count(*) FROM " + tableName;
            statement = connection.createStatement();
            //String where = st.getKeyWhere(rowStore);
            String query = selectFrom + ( (where != null && where.length() > 0) ? " " + where : "");
            resultSet = statement.executeQuery(query);
            if(resultSet.next())
            {
                count = resultSet.getInt(1);
            }
            resultSet.close();
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                if(statement != null) statement.close();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        return count;
    }


    /**
     * Метод установи данных в поле курсора для записи в БД
     * @param type Тип поля
     * @param value Значение поля
     * @param column Номер поля в курсоре
     * @param pstmt Курсор
     * @throws Exception Ошибки возникшие в момент помещения данных в поле курсора
     */
    private void setStatementObject(int type, Object value, int column, PreparedStatement pstmt ) throws Exception
    {
    /*if ( value == null )
    {
      switch (type) {
        case Types.DATE:
        case Types.TIMESTAMP:
          if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, new java.sql.Date(-1, 11, 30));
          else pstmt.setNull(column, type);
        break;
        default:
          if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, "");
          else pstmt.setNull(column, type);
        break;
      }
    }*/

//System.out.println(column + " - " + type + " - " + value);
        switch( type )
        {
            case Types.BLOB:
                break;
            case Types.LONGVARCHAR:
            case Types.CLOB:
                if ( value != null )
                {
                    if (value instanceof byte[]) value = new String( (byte[]) value, mf.cp);
                    else if (value instanceof char[]) value = new String( (char[]) value);
//System.out.println(value);
                    pstmt.setString(column, (String) value);
                }
                else
                {
                    if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, "");
                    else pstmt.setNull(column, type);
                }
                break;
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.BIGINT:
                if ( value != null && value.toString().trim().length() != 0 )
                {
                    if(value instanceof Boolean)
                    { // Источник логическая переменная
                        if(((Boolean)value).booleanValue()) pstmt.setObject(column, new java.math.BigDecimal(1));
                        else pstmt.setObject(column, new java.math.BigDecimal(0));
                    }
                    else
                    {
                        pstmt.setObject(column, new java.math.BigDecimal(value.toString()));
                    }
                }
                else
                {
                    if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, new java.math.BigDecimal( "0" ));
                    else pstmt.setNull(column, type);
                }
                break;
            case Types.DATE:
            case Types.TIMESTAMP:
                if ( value != null && value.toString().length() != 0 )
                {
                    //java.sql.Date myDate = null;
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

                    if (value instanceof String)
                    {
                        if(value.equals("")) value = null;
                        else if(value.toString().trim().length() < 12) value = df1.parse(value.toString());
                        else value = df.parse(value.toString());
                        value = new Timestamp( ((java.util.Date)value).getTime());
                    }
                    else if (value instanceof Date );
                    else if (value instanceof Timestamp );
                    else if (value instanceof java.util.Date )
                    {
                        value = new Timestamp( ((java.util.Date)value).getTime());
                    }
                    if(databaseMajorVersion <= 8 && productName.toUpperCase().indexOf("ORACLE") != -1)
                    {
                        value = new Date( ( (java.util.Date) value).getTime());
                    }
                    pstmt.setObject( column , value );
                }
                else
                {
                    if( productName.toUpperCase().indexOf("FOXPRO") != -1 )
                    {
//            pstmt.setObject(column, new java.sql.Date( -1, 11, 30 ));
                        pstmt.setObject(column, new Date( -2209168800000L ));
                    }
                    else pstmt.setNull(column, type);
                }
                break;
            case Types.VARCHAR:
                if(value != null)
                {
                    if (value instanceof byte[]) value = new String( (byte[]) value, mf.cp);
                    else if (value instanceof char[]) value = new String( (char[]) value);
                }
                if(TRIM_ALL_CHAR && value != null) value = value.toString().trim();
                if(productName.toUpperCase().indexOf("ORACLE") != -1 && databaseMajorVersion > 8)
                {
                    ((OraclePreparedStatement)pstmt).setFormOfUse(column, OraclePreparedStatement.FORM_NCHAR);
                }
                if ( value != null && value.toString().length() != 0 )
                {
                    pstmt.setString(column, value.toString());
                }
                else
                {
                    if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, "");
                    else pstmt.setNull(column, type);
                }
                break;
            case Types.CHAR:
                if(value != null)
                {
                    if (value instanceof byte[]) value = new String( (byte[]) value, mf.cp);
                    else if (value instanceof char[]) value = new String( (char[]) value);
                }
                if(TRIM_ALL_CHAR && value != null) value = value.toString().trim();
                if ( value != null && value.toString().length() != 0 )
                {
                    pstmt.setString(column, value.toString());
                }
                else
                {
                    if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, "");
                    else pstmt.setNull(column, type);
                }
                break;
            default:
                if(TRIM_ALL_CHAR && value != null && value instanceof String) value = value.toString().trim();
                if ( value != null && value.toString().length() != 0 )
                {
                    pstmt.setObject(column, value);
                }
                else
                {
                    if (connection instanceof JdbcOdbcConnection) pstmt.setObject(column, "");
                    else pstmt.setNull(column, type);
                }
                break;
        };
    }

    /**
     * Метод записи данных в поля типа BLOB подстановкой потоков содержащих
     * данные этих полей
     * @param st Хранилище, содержащее данные для подстановки
     * @param tableName имя таблицы
     * @param row Строка данных в таблице
     * @throws Exception Возможные ошибки при подстановке потоков с данными
     */
    private void writeStatementBlob(modelDbStore st, String tableName, int row) throws Exception
    {
        Statement statement = null;
        ResultSet resultSet = null;
        try
        {
            boolean is_blob = false;
            for (int i = 0; i < st.getColumnCount(); i++)
            {
                if ( st.getType(i) == Types.BLOB && st.getObject( row, i ) != null &&
                        (st.getObject( row, i ) instanceof byte[] || st.getObject( row, i ) instanceof String) )
                {
                    is_blob = true;
                }
            }
            if(!is_blob) return;

            statement = connection.createStatement();
            resultSet = statement.executeQuery(st.getSelect()+"FROM "+tableName+
                    " "+st.getKeyWhere( row ));
            if ( resultSet.next ()) {
                for (int i = 0; i < st.getColumnCount(); i++)
                {
                    if ( st.getType(i) == Types.BLOB && st.getObject( row, i ) != null &&
                            (st.getObject( row, i ) instanceof byte[] || st.getObject( row, i ) instanceof String))
                    {
                        BLOB blob = ((OracleResultSet)resultSet).getBLOB( i+1 );
//            if(!blob.isEmptyLob()) blob.truncate(0);

                        java.io.OutputStream outstream = null;
                        if(productName.toUpperCase().indexOf("ORACLE") != -1 && databaseMajorVersion > 9 && driverVersion > 9) {
                            outstream = blob.setBinaryStream(0);
                        }
                        else {
                            outstream = blob.getBinaryOutputStream();
                        }

                        if(st.getObject( row, i ) instanceof byte[]) outstream.write((byte[])st.getObject( row, i ));
                        else if(st.getObject( row, i ) instanceof String)
                            outstream.write(((String)st.getObject( row, i )).getBytes(mf.cp));
                        outstream.close();
                        if(isPackAllBlob)
                        {
                            ///////////*********************////////////
                            CallableStatement cst = connection.prepareCall("{CALL ARC_BLOB.PACK(?)}");
                            cst.setBlob(1, blob);
                            cst.executeUpdate();
                            cst.close();
                            ///////////*********************///////////
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                if(resultSet != null) resultSet.close();
                if(statement != null) statement.close();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
    }
    //******************************************************************************
    public Vector getColumnName(String catalog, String tableName) throws Exception
    {
        return getColumnName(sqlSchema, catalog, tableName);
    }
    //******************************************************************************
    public Vector getColumnName(String sxema, String catalog, String tableName) throws Exception
    {
//    DatabaseMetaData    meta_data=null;
        ResultSet           rset=null;
        Vector info = new Vector();
        try
        {
            String sx = (sxema == null || sxema.length() == 0 ? null : sxema.toUpperCase());
            String cat = (catalog == null ? "" : catalog).toUpperCase();
            String tabn = (tableName == null ? "" : tableName).toUpperCase();
//      meta_data = connection.getMetaData();
//      if(catalog == null || catalog.length() == 0)
//        rset = mtdt.getColumns(catalog, null, tableName, "%");
//      else
            rset = mtdt.getColumns(cat, sx, tabn, "%");
            while(rset.next())
            {
                info.addElement(rset.getString(4).toLowerCase()); // Name
                //rset.getShort(5);       // Type
                //int ii=rset.getInt(7);  // size
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            try
            {
                if(rset != null) rset.close();
            } catch (Exception ex) {}
        }
        return info;
    }
    //******************************************************************************
    public Vector scanSxema(String catalog, String mask) throws Exception
    {
        Vector tbl = new Vector();
        try
        {
            ResultSet rset = null;
//      DatabaseMetaData meta_data = connection.getMetaData();

            if(mtdt.isCatalogAtStart())
            {
                rset = mtdt.getTables(catalog, null,
                        (mask == null || mask.length() == 0 ? "%" : mask), null);
            }
            else
            {
                rset = mtdt.getTables("", sqlSchema.toUpperCase(),
                        (mask == null || mask.length() == 0 ? "%" : mask).toUpperCase(), null);
            }
            //rset = meta_data.getColumns("",OracleNm.toUpperCase(),tb.toUpperCase(),"%");

//      stPack st = new stPack();
//      aaa(rset, st);
//      System.out.println(st.info);
//      System.out.println(st);

            while(rset.next())
            {
                if(rset.getString("table_type").equalsIgnoreCase("TABLE"))
                    tbl.addElement(rset.getString("table_name"));
            }
            rset.close();

        }
        catch (Exception ex)
        {
            throw ex;
        }
        return tbl;
    }

 /*public void aaa(ResultSet tt, modelDbStore st) throws Exception
 {
   ResultSetMetaData md = tt.getMetaData();
   String[] clmnnm = new String[md.getColumnCount()];
   for (int i = 0; i < clmnnm.length; i++) {
     clmnnm[i] = md.getColumnName(i+1).toLowerCase();
   }
   st.setInit(clmnnm);
   for (int i = 0; i < st.getColumnCount(); i++)
   {
     st.setType( i, md.getColumnType( i + 1 ) );
     st.setTypeName(i, md.getColumnTypeName(i+1) );
     st.setDisplaySize(i, md.getColumnDisplaySize(i+1) );
     if(st.getType(i) != Types.BLOB && st.getType(i) != Types.CLOB) st.setPrecision(i, md.getPrecision(i+1) );
     else st.setPrecision(i, 0);
     st.setScale(i, md.getScale(i+1) );
     st.setNullable(i, md.isNullable(i+1) );
     st.setAutoIncrement(i, md.isAutoIncrement(i+1) );
   }
   fillStoreFromResultSet(st, tt, 0, 0);
  }*/
//******************************************************************************
}
