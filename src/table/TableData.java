package table;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TableData {
    private String fileName;
    private String st_dt = new SimpleDateFormat("dd.MM.yyyy").format(new Date().getTime() - (24 * 60 * 60 * 1000));
    private String fin_dt = new SimpleDateFormat("dd.MM.yyyy").format(new Date());

    public String getQuery() throws IOException, URISyntaxException {
        String path = TableData.class.getClassLoader()
                .getResource("table/TableData.class").toURI().getPath()
                .replaceAll("TableData.class", getFileName());
        String que = WorkWithFile.readByte(path)
                .replaceAll("ff.ff.ffff", getFin_dt())
                .replaceAll("ss.ss.ssss", getSt_dt());
//        .replaceAll("ff.ff.ffff", getFin_dt())
//                .replaceAll("ss.ss.ssss", getSt_dt());

        System.out.println(que);

        return que;
    }


    public String getFileName() {
        return fileName;
    }

    public String getFin_dt() {
        return fin_dt;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSt_dt() {
        return st_dt;
    }

    public void setSt_dt(String st_dt) {
        this.st_dt = st_dt;
    }

    public void setFin_dt(String fin_dt) {
        this.fin_dt = fin_dt;
    }
}







