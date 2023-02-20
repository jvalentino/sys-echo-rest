<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Dashboard</title>
        <spring:url value="/resources/core/css/main.css" var="coreCss" />
        <link href="${coreCss}" rel="stylesheet" />
    </head>
    <body>
        <h1>Dashboard</h1>

        <%@ include file="menu.jsp" %>

        <table>
            <theader>
                <tr>
                    <th>Document Name</th>
                    <th>Last Updated By</th>
                    <th>Last Updated</td>
                    <th></th>
                </tr>
            </theader>
            <tbody>
                <c:forEach items="${model.documents}" var="document">
                    <tr>
                        <td>${document.name}</td>
                        <td>${document.updatedByUser.email}</td>
                        <td>${document.updatedDateTime}</td>
                        <td>
                            <a href="./view-versions/${document.docId}">View Versions</a>
                        </td>

                    </tr>
                </c:forEach>
            </tbody>
        <table>

    </body>
</html>