package table;

import javax.servlet.http.HttpServlet;
import java.io.UnsupportedEncodingException;

public class dateCreate extends HttpServlet {


    private boolean filtrYes = false;

    private String name = "";
    private String tema = "";
    private String email = "";

    private boolean filtrName = false;
    private boolean filtrTema = false;
    private boolean filtrEmail = false;

    private String name1 = "";
    private String tema1 = "";
    private String email1 = "";



    int hit;


    private String rezs;
    private String rezf;


    public void dataF(String dataSrart) {
        setName1(dataSrart);
    }

    //***** Фильтр по выбранным данным *****
    public void ollFiltr() {
        if (getName().equals(""))
            setFiltrName(true);
        if (getTema().equals(""))
            setFiltrTema(true);
        if (getEmail().equals(""))
            setFiltrEmail(true);
    }

    public boolean filtrY() {
        boolean yes;
        if (!filterSpis().equals(""))
            yes= true;
        else yes = false;
        return yes;
    }

    public String filterSpis() {
        String voidFilter = "";
        if (!filtrName) {
            voidFilter += "  Отправитель: " + getName();
        }
        if (!filtrTema) {
            voidFilter += "  Тема: " + getTema();
        }
        if (!filtrEmail) {
            voidFilter += "   Адрес: " + getEmail();
        }
        return voidFilter;
    }

    //***** Фильтр по Имени отправителя *****
    public boolean filtrName(String name) {
        boolean fName;
        if (getFiltrName()) {
            fName = true;
        } else if (name.contains(getName()))
            fName = true;
        else
            fName = false;

        return fName;
    }

    //***** Фильт по теме сфармированой ошибки *****
    public boolean filtrTema(String tema) {
        boolean fTema;
        if (getFiltrTema())
            fTema = true;
        else if (tema.contains(getTema()))
            fTema = true;
        else
            fTema = false;

        return fTema;
    }

    //****** Поиск по Адресу получателя ******
    public boolean filtrEmail(String mail) {
        boolean fEmale;
        if (isFiltrEmail())
            fEmale = true;
        else if (mail.contains(getEmail())) {
            fEmale = true;
        } else fEmale = false;

        return fEmale;
    }

    //***** Обработка общего фильтра *****
    public boolean Filtr(String name, String tema, String email) {
//        && hidReal>hitFinish
        boolean rez = false;
//        int hidReal = hidI; //500
        int hitFinish = getHit() - 50; //450

        if (filtrName(name) && filtrTema(tema) && filtrEmail(email))
            rez = true;
        else rez = false;

        return rez;
    }

    public String spisAdd() {
        String rez = "";


        return rez;
    }

    //*********************************

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnsupportedEncodingException {
        this.name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) throws UnsupportedEncodingException {
        this.tema = new String(tema.getBytes("ISO-8859-1"), "UTF-8");
    }

    public boolean getFiltrName() {
        return filtrName;
    }

    public void setFiltrName(boolean filtrName) {
        this.filtrName = filtrName;
    }

    public boolean getFiltrTema() {
        return filtrTema;
    }

    public void setFiltrTema(boolean filtrTema) {
        this.filtrTema = filtrTema;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws UnsupportedEncodingException {
        this.email = new String(email.getBytes("ISO-8859-1"), "UTF-8");
    }

    public boolean isFiltrEmail() {
        return filtrEmail;
    }

    public void setFiltrEmail(boolean filtrEmail) {
        this.filtrEmail = filtrEmail;
    }

    public boolean isFiltrYes() {
        return filtrYes;
    }

    public void setFiltrYes(boolean filtrYes) {
        this.filtrYes = filtrYes;
    }

//    ------------------------------------/*/

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }
}
