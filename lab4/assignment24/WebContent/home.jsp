<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="static funix.lab.asm24.AddToBasket.*" %>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>

<%@include file="WEB-INF/components/header.jsp"%>
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/lab4asm24" user="root" password="123456"/>
<sql:query dataSource="${snapshot}" var="products">
     SELECT * FROM lab4asm24.product
     ORDER BY Name DESC;
</sql:query>

<div class="container-fluid content-wrapper" id="content">
	<!-- this is the Content Wrapper -->
	<div class="container">
		<div class="row-fluid content-inner">
			<div id="left" class="span9">
         		<div class="wrapper shop products-list">
					<div class="heading">
             			<h1 class="page-title">Online store</h1>
           			</div>

           			<div class="content">
             			<div class="section">
               				<div class="content">
                 				<p>
                   					<span>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</span>
                 				</p>
               				</div>
             			</div>
             			
		             	<div class="section">
		               		<div class="content">
		                		<div class="row-fluid">
		                			<c:forEach var="row" items="${products.rows}">
		                				<div class="span4" style="margin-top: 10px;">
				                          <div class="product">
				                            <div class="product-image">
				                              <a rel="nofollow" href="/product/1799305/homemade-jam?catid=582730">
				                                <div class="images-inner">
				                                  <img src="assets/img/${row.Picture}" />
				                                </div>
				                              </a>
				                              <h4 class="product-title">
				                                <a rel="nofollow" href="/product/1799305/homemade-jam?catid=582730">
				                                	${row.Name}
				                               </a>
				                              </h4>
				
				                              <div class="product-price">Price: ${row.Price} USD</div>
				                              <div class="product-button">
				                              	<a href="AddToBasket?<%=PRODUCT_ID%>=${row.id}&<%=CALLBACK_URL%>=${pageContext.request.requestURL}">
                                                    <button name="product" data-hassizes="False" class="btn" type="button">
                                                        Add to basket
                                                    </button>
                                                </a>
				                              </div>
				                              <div class="product-description">
				                                ${row.Description}
				                              </div>
				                            </div>
				                          </div>
				                        </div>
		                			</c:forEach>
		                		</div>
		             		</div>
		           		</div>
         			</div>
       			</div>
       		</div>
       		
       		<%@include file="WEB-INF/components/sidebar.jsp"%>
     </div>
   </div>
</div>
<!-- the controller has determined which view to be shown in the content -->

<%@include file="WEB-INF/components/footer.jsp"%>