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
     ORDER BY publish_date DESC LIMIT 3;
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
	                      		<div class="avatar">
	                        		<i class="icon-pencil icon-4x" title="Lorem ipsum dolor"></i>
	                      		</div>
	                      		<div class="item">
	                        		<div class="controls">
	                          			<span class="date-text">${row.publish_date}</span>
	                        		</div>
			                        <div class="heading">
			                          <h4>
			                            <a rel="nofollow" href="posting/lorem-ipsum-dolor">${row.title}</a>
			                          </h4>
			                        </div>
	
			                        <div class="content">
				                        <c:if test="${row.image != null && row.image != '' && row.title != 'Photo'}">
				           					<div class="img-simple span6 pull-left">
					                            <div class="image">
					                              <a rel="nofollow" data-ss="imagemodal">
					                              	<img src="assets/img/${row.image}"/>
					                              </a>
					                            </div>
					                    	</div>             	
				                        </c:if>
				                        
				                        <c:if test="${row.image != null && row.image != '' && row.title == 'Photo'}">
				                        	<div class="img-simple">
					                            <div class="image">
					                              <a rel="nofollow" data-ss="imagemodal">
					                              	<img src="assets/img/${row.image}"/></a>
					                            </div>
					                    	</div>
				                        </c:if>
			                          <p>${row.content}</p>
			                        </div>
	                      		</div>
	                    	</div>
	                  	</div>
	                  </c:forEach>
					</div>
	                <ul class="pager">
	                    <li>
	                      <a rel="nofollow" href="overview.jsp">Overview</a>
	                    </li>
	                </ul>
	        	</div>
			</div>
			
			<%@include file="WEB-INF/components/sidebar.jsp"%>
       	</div>
	</div>
</div>
<!-- the controller has determined which view to be shown in the content -->

<%@include file="WEB-INF/components/footer.jsp"%>