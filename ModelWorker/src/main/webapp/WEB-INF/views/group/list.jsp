<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="GroupList"/>
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
			location.href = "${rc.contextPath}/group/add";
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
				window.location.href = "${rc.contextPath}/group/edit?groupNo=" + ids[0];
			}
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
					  url:"${rc.contextPath}/group/delete",
					  type:"post",
					  dataType:"json",
					  data:{
						  groupNos :ids.toString()
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
		<%-- check所有用户 --%>
		$("#chkAllUser").click(function() {
			if ($(this).prop("checked")) {
				//check所有
				$.each($("#form1 table tbody input[type='checkbox']"), function() {
					$(this).prop("checked", true);
				});
			} else {
				//取消所有
				$.each($("#form1 table tbody input[type='checkbox']"), function() {
					$(this).prop("checked", false);
				});
			}
		});
		<%-- 单个checkbox事件 --%>
		$("#form1 table tbody input[type='checkbox']").click(function() {
			var allCheck = true;
			$.each($("#form1 table tbody input[type='checkbox']"), function() {
				if ($(this).prop("checked") == false) {
					allCheck = false;
					return false;
				}
			});
			if (allCheck) {
				$("#chkAllUser").prop("checked", true);
			} else {
				$("#chkAllUser").prop("checked", false);
			}
		});
		<%-- 加用户至工作组按钮 --%>
		$("#btn_user").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择工作组。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一个工作组！");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/group/getUser",
				type:"post",
				dataType:"json",
				data:{
					groupNo:ids[0]
				},
				success:function(data) {
					if (data.errType == "0") {
						$("#form1 table input[type='checkbox']").prop("checked", false);
						if (data.data != undefined && data.data != null && data.data != "") {
							var allCheck = true;
							$.each(data.data, function(n, user) {
								$.each($("#form1 table tbody input[type='checkbox']"), function() {
									if ($(this).val() == user.userNo) {
										$(this).prop("checked", true);
										return false;
									}
								});
								allCheck = false;
							});
						}
						if (allCheck) {
							$("#chkAllUser").prop("checked", true);
						} else {
							$("#chkAllUser").prop("checked", false);
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
		<%-- 加用户至工作组 --%>
		$("#btn-user-confirm").click(function() {
			var addUser = function(groupNo, users) {
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/group/addUser",
					type:"post",
					dataType:"json",
					data:{
						groupNo:groupNo,
						userNos:users.toString()
					},
					success:function(data){
						if (data.errType == "0") {
							MessageBox.processEnd("加入用户成功！", function() {
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
			var groupNos = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				groupNos.push($(this).val());				
			});
			if (groupNos.length == 0) {
				MessageBox.info("请选择工作组。");
				return;
			} else if (groupNos.length > 1) {
				MessageBox.info("请选择一个工作组！");
				return;
			}
			var userNos = [];
			$('#form1 table input[type="checkbox"]:checked').each(function(){
				userNos.push($(this).val());				
			});
			if (userNos.length == 0) {
				MessageBox.confirm("您没有选择任何用户，将清除该工作组中的所有用户，是否继续？", function() {
					addUser(groupNos[0], userNos);
				});
			} else {
				addUser(groupNos[0], userNos);
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
            <li><a href="#"><i class="fa fa-dashboard"></i>工作组管理</a></li>
            <li class="active">工作组列表</li>
          </ol>
 
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/group/list">
                	<div class="box_sreach clearfix">
	                	<div class="fl mr_30">
                        <label for="groupNo" class="label-header label-header1">组编码：</label>
                        <input id="groupNo" name="groupNo" class="form-control input-sm fl w15" value="${userNo }">
                  	</div>
                  		<div class="fl mr_30">
						<label for="groupName" class="label-header label-header1">组名称：</label>
						<input id="groupName" name="groupName" class="form-control input-sm fl w15" value="${userNo }">
	                </div>
                   <div class="fl mr_30">
                    	<hq:auth act="GroupList">
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
			          		<hq:auth act="GroupAdd">
			          			<button class="btn_header" id="btn_add" type="button">新增</button>
			          		</hq:auth>
			          		<hq:auth act="GroupEdit">
			          			<button class="btn_header" id="btn_edit" type="button">修改</button>
			          		</hq:auth>
			          		<hq:auth act="GroupAddUser">
			          			<button class="btn_header" id="btn_user" type="button">用户</button>
			          		</hq:auth>
			          		<hq:auth act="GroupDelete">
			          			<button class="btn_header" id="btn_delete" type="button">删除</button>
			          		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
			<!--           	<div class="btn-group"> -->
			<%--           		<hq:auth act="GroupList"> --%>
			<!--           			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
			<%--           		</hq:auth> --%>
			<!--           	</div> -->
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">组信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
				                   <th>组编码</th>
		                           <th>组名称</th>
								</tr>
						      	<c:forEach items="${data.groupList}" var="group">
						      	 <tbody>
						           <tr>
						           	 <td><input type="checkbox" value="${group.groupNo }"></td>
						             <td>${group.groupNo }</td>
						             <td>${group.groupName}</td>
						           </tr> 
						           </tbody>
						       </c:forEach>
                            </table>
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
                        <h4 class="modal-title" id="myModalLabel1">工作组用户</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/flowDesign/add" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                            	<thead>
		                            		<th><input type="checkbox" id="chkAllUser"></th>
		                            		<th>用户编码</th>
		                            		<th>用户名称</th>
		                            	</thead>
		                                <tbody>
		                                <c:forEach items="${data.userList }" var="user">
		                                	<tr>
		                                		<td><input type="checkbox" value="${user.userNo }"></td>
		                                		<td>${user.userNo }</td>
		                                		<td>${user.userName }</td>
		                                	</tr>
		                                </c:forEach>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-user-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>