<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@page import="cn.lmx.flow.bean.module.MenuBean" %>

      <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">

          <!-- Sidebar user panel (optional) -->
<!--           <div class="user-panel"> -->
<!--             <div class="pull-left image"> -->
<%-- 				<img src="${rc.contextPath}/img/default.jpg" class="img-circle" alt="User Image"> --%>
<!--             </div> -->
<!--             <div class="pull-left info"> -->
<%--               <p>${sessionScope.get(currentUser).userName}</p> --%>
<!--             </div> -->
<!--           </div> -->

          <!-- search form (Optional) -->
          <!--
          <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
              <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i></button>
              </span>
            </div>
          </form>
          -->
          <!-- /.search form -->

          <!-- Sidebar Menu -->
          <div id="leftMenuBar" style="overflow-y:auto;">
          <ul class="sidebar-menu">
          	<c:forEach items="${sessionScope.get(\"cn.lmx.flow.bean.module.MenuBean\")}" var="menu">
          		<li id="${menu.no }"><a href="#"><i class="${menu.cssStyle }"></i> <span>${menu.name }</span><i class="fa fa-angle-left pull-right"></i></a>
          		<ul class="treeview-menu">
          		<c:forEach items="${menu.itemList }" var="item">
          			<li id="${item.itemNo}"><a href="${rc.contextPath}${item.actionUrl}"><i class="${item.cssStyle }"></i> <span>${item.itemName }</span></a></li>
          		</c:forEach>
	              </ul>
	            </li>
          	</c:forEach>
          </ul><!-- /.sidebar-menu -->
          </div>
        </section>
        <!-- /.sidebar -->
      </aside>

