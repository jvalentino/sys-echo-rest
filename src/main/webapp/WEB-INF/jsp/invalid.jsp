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
        <h1>Invalid Email and/or Password</h1>
    </body>
</html>