<%--
  Created by IntelliJ IDEA.
  User: Dmytriy
  Date: 08.04.2020
  Time: 9:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="myshortname" uri="/WEB-INF/struts-html.tld" %>
<html>
<%--<logic:notEmpty name="privilegeAF" property="viewRep(LG_1)">--%>
<head>
    <title>New DataBase</title>

    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="css/styles.css" rel="stylesheet">
    <link href="css/menu.css" rel="stylesheet">
    <link href="css/table.css" rel="stylesheet">
    <link href="css/modal.css" rel="stylesheet">
    <link href="jquery/themes/sunny/jquery-ui.css">

    <script src="jquery/jquery.min.js"></script>
    <script src="jquery/jquery-ui.min.js"></script>
    <script src="js/DatepickerForFindKont.js"></script>
    <script src="jquery/datepicker-ru.js"></script>
    <link rel="stylesheet" href="jquery/themes/sunny/jquery-ui.css">

    <script type="text/javascript">
        $(function () {
            $('#dp_1').datepicker({
                changeMonth: true,
                changeYear: true,
                yearRange: "-1:+0"
            });
            $('#dp_2').datepicker({
                changeMonth: true,
                changeYear: true,
                yearRange: "-1:+0",
                maxDate: "+0"
            });
        });
    </script>


</head>

<jsp:useBean id="tb_1" class="table.TableData"/>
<jsp:useBean id="tb_2" class="table.dateCreate"/>
<jsp:setProperty name="tb_1" property="*"/>
<jsp:setProperty name="tb_2" property="*"/>
<sql:setDataSource url="jdbc:oracle:thin:@10.3.0.54:1521:bivc" driver="oracle.jdbc.OracleDriver" user="ppv"
                   password="ppv"/>


<body>
<!-- Шапка (выбор периода)  -->
<div id="header">
    <div id="prib1">
        <%--<h1>Список полученных ошибок</h1>--%>
        <div id="nav">
            <form id="Data" style=" margin-bottom: 0px;" accept-charset="UTF-8">
                <%--21.01.2020--%>
                <i style=" margin-left: 25px;">
                    <b>Выберите период с </b>
                    <input readonly id="dp_1" onchange="d1()" class="date" name='st_dt' type="text" size="8"
                           value="<%=tb_1.getSt_dt()%>">
                    <b>по</b>
                    <input readonly id="dp_2" onchange="d2()" class="date" name='fin_dt' type="text" size="8"
                           value="<%=tb_1.getFin_dt()%>">
                </i>
                <!-- Кнопка активации -->
                <button style="width: 150px;" form="Data" type="submit">ОK</button>
                <%--                            </form>--%>
                <form id="Modal" style="margin-bottom: 0px;" accept-charset="UTF-8">
                    <label style="width: 150px;" class="btn" for="modal-1">Фильтр</label>
                    <!-- Модальное окно -->
                    <div class="modal">
                        <input class="modal-open" id="modal-1" type="checkbox" hidden>
                        <div class="modal-wrap" aria-hidden="true" role="dialog">
                            <label class="modal-overlay" for="modal-1"></label>
                            <div class="modal-dialog">
                                <div class="modal-header">
                                    <h2>Фильтр </h2>
                                    <label class="btn-close" type="result" for="modal-1" aria-hidden="true">×</label>
                                </div>
                                <div class="modal-body">
                                    <h3>Введите имя отправителя </h3>
                                    <input style="width: 150px; height: 25px;" type="text" name="name"
                                           value="<%=tb_2.getName()%>"/>
                                    <button style="width: 80px; height: 35px;" type="submit">Принять</button>
                                    <%--             <%=tb_2.getName()%>                   --%>
                                </div>
                                <div class="modal-body">
                                    <h3>Введите тему ошибки </h3>
                                    <input style="width: 150px; height: 25px;" type="text" name="tema"
                                           value="<%=tb_2.getTema()%>"/>
                                    <button style="width: 80px;  height: 35px;" type="submit">Принять</button>
                                    <%--                              --%>
                                </div>
                                <div class="modal-footer">
                                    <h3>Введите адрес получателя </h3>
                                    <input style="width: 150px; height: 25px;" type="text" name="email"/>
                                    <button style="width: 80px; height: 35px;" type="submit">Найти</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%-- <button style="width: 150px;" type="submit-reset">Сброс</button>--%>
                </form>
            </form>

            <%
                tb_2.ollFiltr();
                if (!tb_2.filterSpis().equals("")) {
            %>
            <form post="filter" action="Filter" id="Filter" style="margin-bottom: 0px;">
                <b style="font-size: 18px; margin-left: 25px;"><%=tb_2.filterSpis()%></b>

                <button style="width: 150px;" form="Modal" type="submit" onclick="dadar()">Сброс</button>
                <script>
                    function dadar() {

                    }
                </script>
            </form>
            <%}%>

        </div>
    </div>
</div>

<!-- Таблица -->
<div id="prib">
    <div id="main">
        <%tb_1.setFileName("sql/skm.sql");%>
        <sql:query var="que1">
            <jsp:getProperty name="tb_1" property="query"/>
        </sql:query>
        <%
            tb_2.ollFiltr();
        %>
        <%--        Шапка таблицы и распредление ширины--%>
        <table class="timecard" width="98%">
            <caption>Employee Timecard</caption>
            <thead>
            <tr>
                <th width="10%" id="thDatCreate">Дата формирования файла</th>
                <th width="10%" id="thDatSend">Дата и время отправления письма</th>
                <th width="10%" id="thTema">Тема письма</th>
                <th width="7%" id="thAcc">Отправитель</th>
                <th width="50%" id="thMailto">E-mail адреса</th>
                <th width="10%" id="thSave">Скачать</th>
            </tr>

            <c:forEach var="n" begin="0" items="${que1.rows}">
                <c:set var="mail" value="${n.mailto}"/>
                <%
                    String str = String.valueOf(pageContext.getAttribute("mail"));
                    str = str.replace(",", ", ");
                %>

                <%--*****  fileName*****--%>
                <c:set var="fileName1" value="${n.file_name}"/>
                <%
                    String fileName = String.valueOf(pageContext.getAttribute("fileName1"));
                %>

                <%--&lt;%&ndash;*****  fName  *****&ndash;%&gt;--%>
                <c:set var="name" value="${n.acc}"/>
                <c:set var="tema" value="${n.tema}"/>

                <%if (tb_2.Filtr(String.valueOf(pageContext.getAttribute("name")), String.valueOf(pageContext.getAttribute("tema")), str)) {%>
                <c:set var="Filtr" value="true"/>
                <%} else {%>
                <c:set var="Filtr" value="false"/>
                <%}%>
                <%--**********--%>
                <c:choose>
                    <c:when test="${Filtr}">
                        <tr>
                            <td style="text-align: center;"><c:out value="${n.view_dat_create}"/>&nbsp;</td>
                            <td style="text-align: center;"><c:out escapeXml="false" value="${n.dat_send}"/>&nbsp;</td>
                            <td style="text-align: center;"><c:out escapeXml="false" value="${n.tema}"/>&nbsp;</td>
                            <td style="text-align: center;"><c:out escapeXml="false" value="${n.acc}"/>&nbsp;</td>
                            <td font-size="0.5em"><%=str%>&nbsp;</td>
                            <td style="text-align: center;">
                                <form method="post" action="dwlFile" style="margin-bottom: 0px;">
                                    <c:choose>
                                        <c:when test="${n.file_name==null}">
                                            <b style="font-size: 24px;">Файл отсутствует</b>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" name="btn" value="dwnFile"><%=fileName%>
                                            </button>
                                            <input type="hidden" name="mdUID" value="<c:out value="${n.hid}"/>">
                                            <input type="hidden" name="FileName"
                                                   value="<c:out value="${n.file_name}"/>">
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </td>
                        </tr>
                    </c:when>
                </c:choose>
            </c:forEach>
            </thead>
            <tbody>
            </tfoot>
        </table>
    </div>
</div>

<!-- footer -->
<div id="footer">
    <form>


    </form>
</div>
</body>
<%--</logic:notEmpty>--%>
</html>