<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table width="100%">
    <c:forEach items="orderItem" >
        <tr>
            <td align="center"><img width="40" height="45"
                     src="${ pageContext.request.contextPath }/<s:property value="#orderItem.product.image"/>"></td>
            <td><s:property value="#orderItem.product.pname"/></td>
            <td><s:property value="#orderItem.count"/></td>
            <td><s:property value="#orderItem.subtotal"/></td>
        </tr>
    </c:forEach>
</table>