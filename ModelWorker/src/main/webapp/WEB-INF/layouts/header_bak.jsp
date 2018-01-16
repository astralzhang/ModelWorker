<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


    <!-- Main Header -->
    <header class="main-header">

        <!-- Logo -->
        <a href="#" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <img width="65%" alt="logo" src="${rc.contextPath}/img/login_title.png" />
          <!-- logo for regular state and mobile devices -->
          <!-- <span class="logo-lg">Fesco<b>Adecco</b></span> -->
        </a>

        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
          <!-- Navbar Right Menu -->
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
			  <li><a href="${rc.contextPath }/desktop/desktop"><img src="${rc.contextPath }/img/timg.jpg" width="20" height="20">我的桌面</a></li>
              <!-- User Account Menu -->
              <li class="dropdown user user-menu">
                <!-- Menu Toggle Button -->
                <a href="#" class="dropdown-toggle">
                  <!-- The user image in the navbar-->
                  <%--
                  <c:if test="${not empty sessionScope.get(currentUser).account.displayPicture}">
						<img src="${sessionScope.get(currentUser).account.displayPicture}" class="user-image" alt="User Image">
				  </c:if>
				   --%>
                  <%-- <c:if test="${empty sessionScope.get(currentUser).account.displayPicture}"> --%>
						<img src="${rc.contextPath}/img/default.jpg" class="user-image" alt="User Image">
				  <%-- </c:if> --%>
                  <!-- hidden-xs hides the username on small devices so only the image appears. -->
                  <span class="hidden-xs">${sessionScope.get(currentUser).userName}</span>
                </a>
              </li>
              <!-- Control Sidebar Toggle Button -->
              <li>
                <a href="${rc.contextPath}/user/logout" ><i class="fa fa-sign-out">退出</i></a>
              </li>
            </ul>
          </div>
        </nav>
    </header>