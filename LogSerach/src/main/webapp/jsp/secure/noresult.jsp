<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pluto <%=Constants.VERSION %> - no results found</title>
    </head>
    <body>
        <%@include file="../common/header.jsp" %> 
        <%@include file="search_header.jsp" %> 
        <div  style="top: 102px;position: absolute; width=100%;">
        <h1>Your Search <b>"${model.input.search}"</b> has no result!</h1>
        <c:choose>
            <c:when test="${fn:length(model.faultNodeNames) eq model.serversSearched}">
                <span>
                    The search is not successful as no valid response received from any node .
                </span>
                <span>
                   Encountered following errors while processing your request.
                </span>
                <span>
                    <ul>
                        <c:forEach items="${model.faultNodeNames}" var="node"> 
                            <li>${node}</li>
                            </c:forEach>
                    </ul>
                </span>
            </c:when>
            <c:when test="${model.searchedFilesCount eq 0}">
                <span>The file name or path pattern <b>"${model.input.logPathPatterns}${model.input.logFileNamePtterns}"</b> does not exist on any server. Make sure you are providing correct patterns. However these fields are case insensitive.</span>
            </c:when>
            <c:otherwise>
                <span>The search phrase <b>${model.input.search}</b> is not found in the files searched. Please try with other valid keywords. Search is case insensitive.</span>
                </br>
                </br>
                <c:choose>
                	 <c:when test="${model.searchedFilesCount eq 1}">
                		<span>Only one file was searched.</span>
                	 </c:when>
                	 <c:otherwise>
                	 	<span>${model.searchedFilesCount} files were searched.</span>
                	 </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
        </div>
    </body>
</html>
