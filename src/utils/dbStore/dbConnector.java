package utils.dbStore;

import java.io.Serializable;

public class dbConnector implements Serializable{
    private String ConnectionString = "";
    private String UserName = "";
    private String Password = "";
    private String Schema = "";

    public dbConnector(String sqlConnectionString, String sqlUserName,
                       String sqlPassword)
    {
        ConnectionString = sqlConnectionString;
        UserName = sqlUserName;
        Password = sqlPassword;
        Schema = sqlUserName;
    }

    public dbConnector(String sqlConnectionString, String sqlUserName,
                       String sqlPassword, String sqlShema)
    {
        ConnectionString = sqlConnectionString;
        UserName = sqlUserName;
        Password = sqlPassword;
        Schema = sqlShema;
    }

    public dbTool initDBTool() throws Exception
    {
//    System.out.println( "ConnectionString = " + ConnectionString + ", UserName = " + UserName + ", Password = " + Password );
        dbTool dbt = new dbTool();
        dbt.setSqlSchema(Schema);
        dbt.setInit( ConnectionString, UserName, Password );
        return dbt;
    }
}
