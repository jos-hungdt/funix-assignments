<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/lab4asm11" user="root" password="123456"/>

<sql:query dataSource="${snapshot}" var="entries">
     SELECT * FROM lab4asm11.entries 
     WHERE title = 'About Me'
     LIMIT 1;
</sql:query>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>About me</title>
<link rel="stylesheet" type="text/css" href="assets/css/9767673.design.v27169.css">
</head>
<body>
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
		                    	<div class="section article">
		                            <div class="heading">
		                                <h3>${ row.title }</h3>
		                            </div>
		
		                            <div class="content">${ row.content }</div>
		                        </div>
		                        <div class="section signature">
	                                <div class="content">		
		                                <div class="signature-text-noimg">
	                                         <p>Kind regards</p>
	                                        <div class="signature-sign" style="font-family: &#39;Pacifico&#39;,serif;">
	                                            ${row.author}
	                                        </div>
		                                </div>
	                            	</div>
		                        </div>
		                    </c:forEach>
		                    </div>
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