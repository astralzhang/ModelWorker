<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="SystemUserEditPwd"/>
	<script type="text/javascript">
	var objUserNo;
	var objUserName;
	$(function(){
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		<%-- 保存 --%>
		$("#btn_save").click(function() {
			if ($("#password1").val() == null || $("#password1").val() == "") {
				MessageBox.info("请输入新密码！");
				return;
			}
			if ($("#password2").val() == null || $("#password2").val() == "") {
				MessageBox.info("请输入确认密码！");
				return;
			}
			var password1 = $("#password1").val();
			var password2 = $("#password2").val();
			if (password1.length > 20) {
				MessageBox.info("请输入20位内的密码！");
				return;
			}
			if (password1 != password2) {
				MessageBox.info("您两次输入的密码不一致！");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/user/saveNewPassword",
				  type:"post",
				  dataType:"json",
				  data:{
					  newPassword:password1,
					  confirmPassword:password2
				  },
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd("密码修改成功！", function() {
							  $("#password1").val("");
							  $("#password2").val("");
						  });
					  } else {
						  MessageBox.processEnd(data.errMessage);
					  }
				  },
				  error:function(XMLHttpRequest, txtStatus, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
	});
	</script>
</head>
<body> 
    <!-- Content Wrapper. Contains page content -->
    <fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyyMMdd" var="bb"/>
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>用户管理</a></li>
            <li class="active">修改密码</li>
          </ol>
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
			          <div class="btn_box">
			          	<div class="search">
			          		<hq:auth act="SystemUserEditPwd">
			          			<button class="btn_header" id="btn_save" type="button">保存</button>
			          		</hq:auth>
			          	</div>
			          	<%--
			          	<div class="line_left"></div>
			          	<div class="btn-group">
			          		<a class="btn_header" id="btn-back" href="${rc.contextPath }/user/list">返回</a>
			          	</div>
			          	--%>
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">用户编辑</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
                            	<tbody>
                            		<tr>
	                            		<td>新密码</td>
	                            		<td>
	                            			<input type="password" id="password1" name="password1" value="${user.userNo }" class="form-control input-sm fl w15">
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>确认密码</td>
	                            		<td>
	                            			<input type="password" id="password2" name="password2 value="${user.userName }" class="form-control input-sm fl w15">
	                            		</td>
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