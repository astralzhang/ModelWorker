<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>

  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

      <!-- Sidebar user panel (optional) -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="${rc.contextPath}/img/avatar5.png" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p>张三</p>
          <!-- Status -->
          <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
        </div>
      </div>

      <!-- search form (Optional) -->
      <form action="#" method="get" class="sidebar-form">
        <div class="input-group">
          <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
        </div>
      </form>
      <!-- /.search form -->

      <!-- Sidebar Menu -->
      <ul class="sidebar-menu">
        <li class="header">XXXX公司员工</li>
        <!-- Optionally, you can add icons to the links -->
        <li><a href="home.html"><i class="fa fa-home"></i><span>首页</span></a></li>
        <li class="treeview">
          <a href="#"><i class="fa fa-user"></i> <span>个人中心</span> <i class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu">
            <li><a href="userInfo.html">个人资料</a></li>
            <li><a href="passwordEdit.html">修改密码</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-tags"></i> <span>积分管理</span> <i class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu">
            <li><a href="#">当前积分</a></li>
            <li><a href="#">充值换积分</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-share-alt"></i> <span>福利管理</span> <i class="fa fa-angle-left pull-right"></i></a>
          <ul class="treeview-menu">
            <li><a href="welfare1.html">基本福利</a></li>
            <li><a href="#">可选福利</a></li>
          </ul>
        </li>
      </ul>
      <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
  </aside>