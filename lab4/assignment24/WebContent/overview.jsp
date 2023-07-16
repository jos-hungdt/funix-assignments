<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>

<%@include file="WEB-INF/components/header.jsp"%>
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/lab4asm24" user="root" password="123456"/>
<sql:query dataSource="${snapshot}" var="blogs">
     SELECT * FROM lab4asm24.blog
     ORDER BY publish_date DESC;
</sql:query>

<div class="container-fluid content-wrapper" id="content">
	<!-- this is the Content Wrapper -->
	<div class="container">
		<div class="row-fluid content-inner">
			<div id="left" class="span9">
         		<div class="wrapper blog">
	                <div class="heading">
	                  <h1 class="page-title">Blog</h1>
	                </div>

	                <div class="content">
	                <c:forEach var="row" items="${blogs.rows}">
	                	<div class="section">
	                    	<div class="content">
	                      		<div class="item">
	                        		<div class="controls">
	                          			<span class="date-text">${row.publish_date}</span>
	                        		</div>
			                        <div class="heading">
			                          <p>
			                            <a rel="nofollow" href="posting/lorem-ipsum-dolor">${row.title}</a>
			                          </p>
			                        </div>
	                      		</div>
	                    	</div>
	                  	</div>
	                  </c:forEach>
					</div>
	        	</div>
			</div>
			
			<%@include file="WEB-INF/components/sidebar.jsp"%>
       	</div>
	</div>
</div>
<!-- the controller has determined which view to be shown in the content -->

<%@include file="WEB-INF/components/footer.jsp"%>