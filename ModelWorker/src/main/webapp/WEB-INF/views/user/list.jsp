<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="systemUserList"/>
	<script type="text/javascript">	
	$(function(){
		<%-- 检索 --%>
		$("#btn-search").click(function() {
			$("#searchForm").submit();
		});
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		<%-- 新增 --%>
		$("#btn_add").on('click', function() {
			location.href = "${rc.contextPath}/user/add";
		});
		<%-- 修改 --%>
		$("#btn_edit").click(function(){
			var ids = [];
			var statuses = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要修改的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据进行修改。");
				return;
			} else {
				<%-- 编辑代理人 --%>
				window.location.href = "${rc.contextPath}/user/edit?userNo=" + ids[0];
			}
		});
		<%-- 重置密码按钮 --%>
		$("#btn_resetPwd").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择需要重置密码的用户。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一个用户重置密码。");
				return;
			}
			$("#newPassword1").val("");
			$("#newPassword2").val("");
			var modal = $('#myModal2').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		});
		<%-- 重置密码 --%>
		$("#btn-resetPwd-confirm").click(function() {
			var password1 = $("#newPassword1").val();
			var password2 = $("#newPassword2").val();
			if (password1 == "") {
				MessageBox.info("请输入新密码！");
				return;
			}
			if (password1.length > 20) {
				MessageBox.info("请输入20位以内的密码！");
				return;
			}
			if (password1 != password2) {
				MessageBox.info("两次输入的密码不一致！");
				return;
			}
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择需要重置密码的用户。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一个用户重置密码。");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/user/resetPassword",
				type:"post",
				dataType:"json",
				data:{
					userNo:ids[0],
					newPassword:password1,
					confirmPassword:password2
				},
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processEnd("密码重置成功！", function() {
							$('#myModal2').modal("hide");
							$("#searchForm").submit();
						});
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		});
		<%-- 删除按钮按下 --%>
		$('#btn_delete').click(function(){
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要删除的数据。");
				return;
			}else{
				MessageBox.processClose();
				MessageBox.confirm("确定将此记录删除?",function(){
				  MessageBox.processStart();
				  $.ajax({
					  url:"${rc.contextPath}/user/delete",
					  type:"post",
					  dataType:"json",
					  data:{
						  ids :ids.toString()
					  },
					  success:function(data){
						  if (data.errType == "0") {
							  MessageBox.processEnd("删除成功", function() {
								  $("#searchForm").submit();
							  });
						  } else {
							  MessageBox.processEnd(data.errMessage);
						  }
					  },
					  error:function(XMLHttpRequest, data) {
						  MessageBox.processEnd("系统错误，详细信息：" + data);
					  }
				  });
			   });
		  }
		});
		<%-- 加入工作组按钮 --%>
		$("#btn_group").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要加入工作组的用户。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一个用户加入工作组！");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/user/getGroup",
				type:"post",
				dataType:"json",
				data:{
					userNo:ids[0]
				},
				success:function(data) {
					if (data.errType == "0") {
						$("#form1 table input[type='checkbox']").prop("checked", false);
						if (data.data != undefined && data.data != null && data.data != "") {
							$.each(data.data, function(n, group) {
								$.each($("#form1 table input[type='checkbox']"), function() {
									if ($(this).val() == group.groupNo) {
										$(this).prop("checked", true);
										return false;
									}
								});
							});
						}
						MessageBox.processClose();
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
			var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		});
		<%-- 加入工作组 --%>
		$("#btn-group-confirm").click(function() {
			var addGroup = function(userNo, groups) {
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/user/addGroup",
					type:"post",
					dataType:"json",
					data:{
						userNo:userNo,
						groups:groups.toString()
					},
					success:function(data){
						if (data.errType == "0") {
							MessageBox.processEnd("加入工作组成功！", function() {
								$("#myModal1").modal("hide");
							});
						} else {
							MessageBox.processEnd(data.errMessage);
						}
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			}
			var userNos = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				userNos.push($(this).val());
				
			});
			if (userNos.length == 0) {
				MessageBox.info("请选择要加入工作组的用户。");
				return;
			} else if (userNos.length > 1) {
				MessageBox.info("请选择一个用户加入工作组！");
				return;
			}
			var groups = [];
			$('#form1 table input[type="checkbox"]:checked').each(function(){
				groups.push($(this).val());				
			});
			if (groups.length == 0) {
				MessageBox.confirm("您没有选择任何工作组，将清除该用户的所有工作组，是否继续？", function() {
					addGroup(userNos[0], groups);
				});
			} else {
				addGroup(userNos[0], groups);
			}
		});
	});
	</script>
</head>
<body>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>用户管理</a></li>
            <li class="active">用户列表</li>
          </ol>

        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/user/list">
                	<div class="box_sreach clearfix">
	                	<div class="fl mr_30">
                        <label for="userNo" class="label-header label-header1">用户编码：</label>
                        <input id="userNo" name="userNo" class="form-control input-sm fl w15" value="${userNo }">
                  	</div>
                  	<div class="fl mr_30">
						<label for="userName" class="label-header label-header1">用户名称：</label>
						<input id="userName" name="userName" class="form-control input-sm fl w15" value="${userName }">
	                </div>
                   <div class="fl mr_30">
		          		<hq:auth act="systemUserList">
		          			<a class="btn_sreach fr" id="btn-search" href="#">搜 &nbsp;索</a>
		          		</hq:auth>
                    </div>
	                </div>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                    <div class="btn_box">
			          	<div class="search">
			          		<hq:auth act="SystemUserAdd">
			          			<button class="btn_header" id="btn_add" type="button">新增</button>
			          		</hq:auth>
			          		<hq:auth act="SystemUserEdit">
			          			<button class="btn_header" id="btn_edit" type="button">修改</button>
			          		</hq:auth>
			          		<hq:auth act="SystemUserResetPwd">
			          			<button class="btn_header" id="btn_resetPwd" type="button">重置密码</button>
			          		</hq:auth>
			          		<hq:auth act="SystemUserAddGroup">
			          			<button class="btn_header" id="btn_group" type="button">工作组</button>
			          		</hq:auth>
			          		<hq:auth act="SystemUserDelete">
			          			<button class="btn_header" id="btn_delete" type="button">删除</button>
			          		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
<!-- 			          	<div class="btn-group"> -->
<%-- 			          		<hq:auth act="systemUserList"> --%>
<!-- 			          			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
<%-- 			          		</hq:auth> --%>
<!-- 			          	</div> -->
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">用户信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                        
                        <div id="table_scroll" style="overflow:auto;">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
				                   <th>用户编码</th>
		                           <th>用户名称</th>
		                           <th>邮箱</th>
								</tr>
						      	<c:forEach items="${data.userList}" var="user">
						      	 <tbody>
						           <tr>
						           	 <td><input type="checkbox" value="${user.userNo }"></td>
						             <td>${user.userNo }</td>
						             <td>${user.userName}</td>
						             <td>${user.mailAddress}</td>
						           </tr> 
						           </tbody>
						       </c:forEach>
                            </table>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">用户组</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/flowDesign/add" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                            	<thead>
		                            		<th></th>
		                            		<th>用户组编码</th>
		                            		<th>用户组名称</th>
		                            	</thead>
		                                <tbody>
		                                <c:forEach items="${data.groupList }" var="group">
		                                	<tr>
		                                		<td><input type="checkbox" value="${group.groupNo }"></td>
		                                		<td>${group.groupNo }</td>
		                                		<td>${group.groupName }</td>
		                                	</tr>
		                                </c:forEach>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-group-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal2" aria-labelledby="myModalLabel2">
            <div class="modal-dialog modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel2">重置密码</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form2" role="form2" action="#" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                	<tr>
		                                		<td>新密码：</td>
		                                		<td><input type="password" id="newPassword1" class="form-control input-sm fl w15"></td> 
		                                	</tr>
		                                	<tr>
		                                		<td>确认密码：</td>
		                                		<td><input type="password" id="newPassword2" class="form-control input-sm fl w15"></td> 
		                                	</tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-resetPwd-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>