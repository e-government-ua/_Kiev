<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>Hello, world!</h2>
	<div>
    <c:url value="/auth" var="loginUrl" />
    <form action="${loginUrl}" method="post">
        <h2>Please sign in</h2>
        <input type="text" name="sec_username"/>
        <input type="password" name="sec_password"/>
        <button type="submit">Sign in</button>
    </form>
</div>
</body>
</html>