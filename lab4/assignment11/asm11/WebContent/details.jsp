<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/lab4asm11" user="root" password="123456"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Blogging about my life</title>
<link rel="stylesheet" type="text/css" href="assets/css/9767673.design.v27169.css">
</head>
<body>
<sql:query dataSource="${snapshot}" var="entries"> 
	SELECT * FROM lab4asm11.entries WHERE id = ?;
	<sql:param value="${param.id}" />
</sql:query>
	<div class="container-fluid site-wrapper"> <!-- this is the Sheet -->
    	<!-- Header section -->
    	<%@include file="WEB-INF/components/header.jsp"%>
            
	    <div class="container-fluid content-wrapper" id="content"> <!-- this is the Content Wrapper -->
		    <div class="container">
		        <div class="row-fluid content-inner">
		            <div id="left" class="span9"> 
		                <div class="wrapper blog">							
							<div class="content">
		                        <c:forEach var="row" items="${entries.rows}">
		                            <div class="section">
		                                <div class="content">
		                                    <div class="avatar">
		                                        <i class="icon-pencil icon-4x" title="${row.title}"></i>
		                                    </div>
		                                    <div class="item">
		                                        <div class="controls">
		                                            <span class="date-text">${row.publish_date}</span>
		                                        </div>
		
		                                        <div class="heading">
		                                            <h4><a rel="nofollow" href="http://us-123my-life.simplesite.com/422328564/3232873/posting/essential-skills-for-a-happy-life">${row.title}</a></h4>
		                                        </div>
		
		                                        <div class="content">
		                                        	<c:if test="${row.image != null && row.image != '' && row.title != 'Photo'}">
		                                            <div class="img-simple span6 pull-left">
		                                                <div class="image">
		                                                    <a rel="nofollow" data-ss="imagemodal" data-href="http://cdn.simplesite.com/i/2b/ac/283445309157387307/i283445314544979646._szw1280h1280_.jpg"><img src="assets/img/${row.image}"></a>
		                                                </div>
		                                            </div>
													</c:if>
													
													<c:if test="${row.image != null && row.image != '' && row.title == 'Photo'}">
		                                            <div class="img-simple">
		                                                <div class="image">
		                                                    <a rel="nofollow" data-ss="imagemodal" data-href="http://cdn.simplesite.com/i/2b/ac/283445309157387307/i283445314544979646._szw1280h1280_.jpg"><img src="assets/img/${row.image}"></a>
		                                                </div>
		                                            </div>
													</c:if>
		                                            <div>${row.content}</div>
		                                        </div>
		                                    </div>
		                                </div>
		                            </div>
		                        </c:forEach>
			                </div>
			                <ul class="pager">
                				<li><a rel="nofollow" href="overview.jsp">Overview</a></li>
            				</ul>
			            </div>
		        	</div>
		            <div id="right" class="span3">
		            	<!-- Sidebar -->
		                <%@include file="WEB-INF/components/sidebar.jsp"%>
		            </div>        
		    	</div>
			</div>  <!-- the controller has determined which view to be shown in the content -->
	
			<!-- Footer section -->
			<%@include file="WEB-INF/components/footer.jsp"%>
		</div>
	</div>
</body>
</html>