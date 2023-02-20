<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Document Management</title>
        <spring:url value="/resources/core/css/main.css" var="coreCss" />
        <link href="${coreCss}" rel="stylesheet" />
    </head>
    <body>
        <h1>Document Management - Example System</h1>
        <p>This is an example system that it used to demonstrate different architectural approaches
        as they relate to scalability. Its core functions are the following:</p>
        <ul>
            <li>The system shall allow a user to add documents</li>
            <li>The system shall version documents</li>
            <li>The system shall allow a user to download a document</li>
        </ul>

        <hr />

        <table>
            <tbody>
                <tr>
                    <td><b>Served Users:</b></td>
                    <td>${model.users}</td>
                </tr>
                <tr>
                    <td><b>Served Documents:</b></td>
                    <td>${model.documents}</td>
                </tr>
            </tbody>
        </table>
        <hr />
        <div>
            <c:url var="login_url" value="/custom-login"/>
             <form:form action="${login_url}" method="post" modelAttribute="user">
                <table>
                    <tbody>
                        <tr>
                            <td>Email Address:</td>
                            <td><form:input type="text" path="email"/></td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td><form:input type="password" path="password"/></td>
                        </tr>
                    </tbody>
                </table>
                <input type="submit" value="Login"/>
             </form:form>
        </div>
    </body>
</html>