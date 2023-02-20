<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Invalid</title>
        <spring:url value="/resources/core/css/main.css" var="coreCss" />
        <link href="${coreCss}" rel="stylesheet" />
    </head>
    <body>
        <h1>${model.doc.name}</h1>

        <%@ include file="menu.jsp" %>

        <h2>Add New Version</h2>

        <form method="POST" action="/version/new/${model.doc.docId}" enctype="multipart/form-data">
                <input type="file" name="file" /><br />
                <br /> <input type="submit" value="Submit" />
        </form>

        <hr />

        <h2>Versions</h2>

        <table>
            <theader>
                <tr>
                    <th>Version</th>
                    <th>Uploaded</th>
                    <th>Download</th>
                </tr
            </theader>
            <tbody>
                <c:forEach items="${model.doc.versions}" var="version">
                    <tr>
                        <td>${version.versionNum}</td>
                        <td>${version.createdDateTime}</td>
                        <td><a href="/version/download/${version.docVersionId}">Download</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        <table>

    </body>
</html>