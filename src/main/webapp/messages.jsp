<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" 
	  xmlns:ui="http://java.sun.com/jsf/facelets"
	  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	  xmlns:c="http://java.sun.com/jsp/jstl/core">

<c:if test="${not empty message}">
    <div class="message"><c:out value="${message}"/></div>
</c:if>

</html>