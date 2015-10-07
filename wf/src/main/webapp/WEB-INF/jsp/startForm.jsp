<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Start Form</h1>
<form method="POST" action="../startProcess/${id}">

	<table>
		<c:forEach items="${fpList}" var="fp">
			<tr>
				<td>${fp.name}:</td> 
				<td>
					<c:choose>
           				<c:when test="${fp.getType().getName().equals('enum')}">
					    	<select name="${fp.id}">
    							<c:forEach items="${fp.getType().getInformation(\"values\")}" var="opts">
        							<option value="${opts.key}">${opts.value}</option>
    							</c:forEach>
							</select>
					    </c:when> 
           				<c:otherwise><input name="${fp.id}" type="text" value="${fp.value}"></c:otherwise>   
        			</c:choose>
			    </td>
			</tr>
		</c:forEach>
	</table> 

<table>
    <tr>
    <td>
    <input type="submit" value="Start Process"/>
    </td>
    </tr>
</table>  
</form>
</body>


</html>