<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="right" class="span3">
	         <div class="sidebar">
	           <div class="wrapper shop-box">
	             <div class="content">
	               <div class="cart-icon">
	                 <i class="icon-cart-empty cart-icon-icon"></i>
	                 <span class="cart-number">${fn:length(sessionScope.basket)}</span>
	               </div>
	               <div class="shop-box-text">
	                 <a rel="nofollow" href="checkout.jsp">
	                   Your basket
	                 </a>
	               </div>
	               <c:if test="${fn:length(sessionScope.basket) > 0}">
			       		<a rel="nofollow" class="btn btn-block btn-shop-checkout" type="button"
			               href="checkout.jsp">Checkout</a>
			       </c:if>
	             </div>
	           </div>
	           <div class="wrapper share-box">
	             <div class="heading">
	               <h4>Share this page</h4>
	             </div>
	
	             <div class="content">
	               <span
	                 ><ul>
	                   <li>
	                     <a
	                       id="share-facebook"
	                       href="http://us-123farm-store.simplesite.com/424001980/category/582730/online-store#"
	                       ><i class="icon-facebook-sign"></i
	                       ><span>Share on Facebook</span></a
	                     >
	                   </li>
	                   <li>
	                     <a
	                       id="share-twitter"
	                       href="http://us-123farm-store.simplesite.com/424001980/category/582730/online-store#"
	                       ><i class="icon-twitter-sign"></i
	                       ><span>Share on Twitter</span></a
	                     >
	                   </li>
	                   <li>
	                     <a
	                       id="share-google-plus"
	                       href="http://us-123farm-store.simplesite.com/424001980/category/582730/online-store#"
	                       ><i class="icon-google-plus-sign"></i
	                       ><span>Share on Google+</span></a
	                     >
	                   </li>
	                 </ul></span
	               >
	             </div>
	           </div>
	           <div class="wrapper viral-box">
	             <div class="heading">
	               <h4>Create a website</h4>
	             </div>
	
	             <div class="content">
	               <p>Everybody can create a website, it's easy.</p>
	               <div class="bottom">
	                 <a
	                   rel="nofollow"
	                   href="http://www.simplesite.com/pages/receive.aspx?partnerkey=123i%3arightbanner&amp;referercustomerid=15831284&amp;refererpageid=424001980"
	                   class="btn btn-block"
	                   >Try it for FREE now</a
	                 >
	               </div>
	             </div>
	           </div>
	         </div>
		</div>