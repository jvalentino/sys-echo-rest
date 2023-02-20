<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Upload</title>
        <spring:url value="/resources/core/css/main.css" var="coreCss" />
        <link href="${coreCss}" rel="stylesheet" />
    </head>
    <body>
        <h1>Upload</h1>

        <%@ include file="menu.jsp" %>

        <form method="POST" action="/upload-file" enctype="multipart/form-data">
        		<input type="file" name="file" /><br />
        		<br /> <input type="submit" value="Submit" />
        </form>
    </body>
</html>