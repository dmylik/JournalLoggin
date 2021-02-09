package table;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class downlandFile extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String param = req.getParameter("btn");

        if (param.equals("dwnFile")) {
            String dateStart = req.getParameter("monthStart1");
            System.out.println(dateStart);

            String mdUID = req.getParameter("mdUID");
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-disposition", "attachment;filename=" + req.getParameter("FileName"));
            OutputStream out = resp.getOutputStream();

            try {
                String que = "select MSG from SLEJ_KONT_MAIL where HID=" + mdUID;
                Class.forName("oracle.jdbc.OracleDriver");
                String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.3.0.54)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=bivc.brest.rw)))";

                Connection connection = DriverManager.getConnection(url, "ppv", "ppv");

                Statement statement = connection.createStatement();
                System.out.println("statement: " + statement + "\n");
                ResultSet resultSet = statement.executeQuery(que);
                System.out.println("resultSet: " + resultSet + "\n");
//                FileOutputStream fos = null;

                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob(1);
                    InputStream is = blob.getBinaryStream();
                    int b = 0;
                    while ((b = is.read()) != -1) {
                        out.write(b);
                    }
                }
                statement.close();
//                fos.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.close();
        }

    }

//    private InputStream getWorkbookFromBlob(String mdUID) throws ClassNotFoundException, SQLException, IOException {
//
//        String que = "select MSG from SLEJ_KONT_MAIL where HID=" + mdUID;
//        Class.forName("oracle.jdbc.OracleDriver");
//        String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.3.0.54)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=bivc.brest.rw)))";
//
//        Connection connection = DriverManager.getConnection(url, "ppv", "ppv");
//
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(que);
//        BLOB fileWB = null;
//        FileOutputStream fos = null;
//
//        while (resultSet.next()) {
//            Blob blob = resultSet.getBlob(1);
//            InputStream is = blob.getBinaryStream();
//            String filepath = "F:/Flk.xls";
//
//            fos = new FileOutputStream(filepath);
//
//            fileWB = (BLOB) resultSet.getBlob("MSG");
//
//            int b = 0;
//            while ((b = is.read()) != -1) {
//                fos.write(b);
//            }
//
//        }
//        statement.close();
//        fos.close();
//
//        assert fileWB != null;
//        System.out.println();
//        System.out.println(fileWB.getBinaryStream());
//        InputStream inputStream = fileWB.getBinaryStream();
//        return inputStream;
//
//        // return new HSSFWorkbook(inputStream);
//    }
}
