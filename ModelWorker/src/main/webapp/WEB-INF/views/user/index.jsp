<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="userCenter"/>
	<script type="text/javascript">	
	</script>
</head>
<body> 
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>用户管理</a></li>
            <li class="active">个人中心</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<hq:auth act="userSave">
          			<button class="btn_header" id="btn_save" type="button">保存</button>
          		</hq:auth>
          	</div>
          	<!--  <div class="line_left"></div> -->
          	<div class="btn-group">          		
          	</div>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">个人资料</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
                            	<tbody>
	                            	<tr>
	                            		<td>用户编码</td>
	                            		<td><input type="text" id="userNo" name="userNo" value="${user.userNo}" placeholder="用户编码" class="form-control input-sm fl w15"></td>
	                            	</tr>
	                            	<tr>
	                            		<td>用户姓名</td>
	                            		<td><input type="text" id="userName" name="userName" value="${user.userName}" placeholder="用户姓名" class="form-control input-sm fl w15"></td>
	                            	</tr>
	                            	<tr>
	                            		<td>邮箱</td>
	                            		<td><input type="text" id="mailAddress" name="mailAddress" value="${user.mailAddress}" placeholder="邮箱" class="form-control input-sm fl w15"></td>
	                            	</tr>
								</tbody>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->
</body>
</html>