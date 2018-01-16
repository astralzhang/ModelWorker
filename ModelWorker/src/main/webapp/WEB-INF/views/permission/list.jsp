<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="PermissionList"/>
	<link rel="stylesheet" href="${rc.contextPath }/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${rc.contextPath }/js/jquery.ztree.all-3.5.min.js"></script>
	<script type="text/javascript">
	$(function(){
		var selectedModule = [];
		<%-- 功能树大小设定 --%>
		var moduleResizeFunc = function() {
			var obj = $("#module").closest("div");
			var windowHeight = $(window).height();
			var offset = $(obj).offset();
			var top = offset == null ? 0 : offset.top;
			$(obj).css({"height":windowHeight - top - 50,"overflow-y":"auto"});
		}
		moduleResizeFunc();
		<%-- 用户树大小设定 --%>
		var userResizeFunc = function() {
			var obj = $("#user").closest("div");
			var windowHeight = $(window).height();
			var offset = $(obj).offset();
			var top = offset == null ? 0 : offset.top;
			$(obj).css({"height":windowHeight - top - 50,"overflow-y":"auto"});
		}
		userResizeFunc();
		<%-- 窗口大小改变 --%>
		$(window).resize(function() {
			userResizeFunc();
			moduleResizeFunc();
		});
		<%-- 选择用户 --%>
		function checkUser(event, treeId, treeNode) {
			var setModuleStatus = function(type, userNo) {
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/permission/getPermission",
					type:"post",
					dataType:"json",
					data:{
						type:type,
						userNo:userNo
					},
					success:function(data){
						if (data.errType == "0") {
							var moduleTree = $.fn.zTree.getZTreeObj("module");
							moduleTree.checkAllNodes(false);
							$.each(data.data, function(n, permission){
								var nodes = moduleTree.getNodesByParam("id", permission.itemNo, null);
								if (nodes == null) {
									return true;
								}
								if (nodes.length > 0) {
									moduleTree.checkNode(nodes[0], true, true);
								}								
							});
							MessageBox.processClose();
						} else {
							MessageBox.processEnd(data.errMessage);
						}
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			};
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var allCheckedNodes = treeObj.getCheckedNodes(true);
			if (treeNode.checked) {
				<%-- 勾选 --%>
				var preNode = null;
				$.each(allCheckedNodes, function(n, node) {
					if (node.id != treeNode.id) {
						preNode = node;
						return false;
					}
				});
				if (preNode == null) {
					<%-- 原来没有选择用户 --%>
					selectedModule = [];
					var parentNode = treeNode.getParentNode();
					if (parentNode == null) {
						<%-- 清空所有的模块选择 --%>
						var moduleTree = $.fn.zTree.getZTreeObj("module");
						moduleTree.checkAllNodes(false);
					} else {
						<%-- 取得新用户的权限状态 --%>
						setModuleStatus(parentNode.id, treeNode.id);
					}
				} else {
					<%-- 原来有选择用户 --%>
					//if (selectedModule.length > 0) {
						MessageBox.confirm("选择切换用户，将导致您保存前的设定失效，是否继续？", function() {
							treeObj.checkNode(preNode, false);
							selectedModule = [];
							var parentNode = treeNode.getParentNode();
							if (parentNode == null) {
								<%-- 清空所有的模块选择 --%>
								var moduleTree = $.fn.zTree.getZTreeObj("module");
								moduleTree.checkAllNodes(false);
							} else {
								<%-- 取得新用户的权限状态 --%>
								setModuleStatus(parentNode.id, treeNode.id);
							}								
						}, function() {
							treeObj.checkNode(treeNode, false, true);
						});
					/*} else {
						selectedModule = [];
						treeObj.checkNode(preNode, false, true);
						var parentNode = treeNode.getParentNode();
						if (parentNode == null) {
							<%-- 清空所有的模块选择 --%>
							var moduleTree = $.fn.zTree.getZTreeObj("module");
							moduleTree.checkAllNodes(false);
						} else {
							<%-- 取得新用户的权限状态 --%>
							setModuleStatus(parentNode.id, treeNode.id);
						}
					}*/
				}
			} else {
				<%-- 取消勾选  --%>
				if (selectedModule.length > 0) {
					MessageBox.confirm("取消用户选择，将导致您保存前的设定失效，是否继续？", function() {
						selectedModule = [];
						<%-- 清空所有的模块选择 --%>
						var moduleTree = $.fn.zTree.getZTreeObj("module");
						moduleTree.checkAllNodes(false);
					}, function() {
						treeObj.checkNode(treeNode, true, true);
					});
				} else {
					selectedModule = [];
					<%-- 清空所有的模块选择 --%>
					var moduleTree = $.fn.zTree.getZTreeObj("module");
					moduleTree.checkAllNodes(false);
				}
			}
		}
		<%-- 选择功能 --%>
		function checkModule(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var hasData = false;
			$.each(selectedModule, function(n, value) {
				if (value.id == treeNode.id) {
					selectedModule.splice(n, 1);
					hasData = true;
					return false;
				}
			});
			if (hasData == false) {
				if (treeNode.checked) {
					selectedModule.push({id:treeNode.id, check:"Y"});
				} else {
					selectedModule.push({id:treeNode.id, check:"N"});
				}
			}
		}
		var user_setting = {
			view: {
				selectedMulti: false, //设置是否能够同时选中多个节点
				showIcon: true, //设置是否显示节点图标
				showLine: true, //设置是否显示节点与节点之间的连线
				showTitle: true //设置是否显示节点的title提示信息
			},
			data: {
				simpleData: {
					enable: true, //设置是否启用简单数据格式（zTree支持标准数据格式跟简单数据格式，上面例子中是标准数据格式）
					idKey: "id", //设置启用简单数据格式时id对应的属性名称
					pidKey: "pId" //设置启用简单数据格式时parentId对应的属性名称,ztree根据id及pid层级关系构建树结构
				}
			},
			check:{
				chkStyle:"checkbox",
				enable: true,  //设置是否显示checkbox复选框
				checkboxType:{"Y":"","N":""}
			},
			callback:{
				onCheck:checkUser
			}
		};
		$.fn.zTree.init($("#user"), user_setting, ${data.user});
		var module_setting = {
			view: {
				selectedMulti: true, //设置是否能够同时选中多个节点
				showIcon: true, //设置是否显示节点图标
				showLine: true, //设置是否显示节点与节点之间的连线
				showTitle: true //设置是否显示节点的title提示信息
			},
			data: {
				simpleData: {
					enable: true, //设置是否启用简单数据格式（zTree支持标准数据格式跟简单数据格式，上面例子中是标准数据格式）
					idKey: "id", //设置启用简单数据格式时id对应的属性名称
					pidKey: "pId" //设置启用简单数据格式时parentId对应的属性名称,ztree根据id及pid层级关系构建树结构
				}
			},
			check:{
				chkStyle:"checkbox",
				enable: true  //设置是否显示checkbox复选框
			},
			callback:{
				onCheck:checkModule
			}
		};
		$.fn.zTree.init($("#module"), module_setting, ${data.module});//初始化zTree树
		//检索按钮
		$("#btn-search").click(function() {
			if (selectedModule.length > 0) {
				MessageBox.confirm("权限已发生变动，重新搜索将导致您保存前的设定失效，是否继续？", function() {
					$("#searchForm").submit();
				});
			} else {
				$("#searchForm").submit();
			}
		});
		//保存按钮
		$("#btn_save").click(function() {
			/*if (selectedModule.length <= 0) {
				MessageBox.info("权限没有发生变动，无需保存！");
				return;
			}*/
			var userTree = $.fn.zTree.getZTreeObj("user");
			if (userTree == null) {
				return;
			}
			var userNodes = userTree.getCheckedNodes(true);
			if (userNodes == null || userNodes.length <= 0) {
				MessageBox.info("没有选择需要设定权限的用户！");
				return;
			}
			var userNo = userNodes[0].id;
			var parentNode = userNodes[0].getParentNode();
			if (parentNode == null) {
				MessageBox.info("没有选择需要设定权限的用户！");
				return;
			}
			var userType = parentNode.id;
			//取得功能树
			var moduleTree = $.fn.zTree.getZTreeObj("module");
			if (moduleTree == null) {
				return;
			}
			var moduleNodes = moduleTree.getCheckedNodes(true);
			var modules = [];
			if (moduleNodes != null) {
				$.each(moduleNodes, function(n, module) {
					if (module.isParent) {
						return true;
					}
					modules.push(module.id);
				});
			}
			MessageBox.processStart();
			//数据传输
			$.ajax({
				url:"${rc.contextPath}/permission/save",
				type:"post",
				dataType:"json",
				data:{
					userType:userType,
					userNo:userNo,
					modules:modules.toString()
				},
				success:function(data){
					if (data.errType == "0") {
						MessageBox.processEnd("保存成功！", function() {
							location.href = "${rc.contextPath}/permission/list";
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
	});
	</script>
</head>
<body>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>权限管理</a></li>
            <li class="active">权限设定</li>
          </ol>

        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/permission/list">
                	<div class="box_sreach clearfix">
	                	<div class="fl mr_30">
                        <label for="userNo" class="label-header label-header1">用户类别：</label>
                        <select id="userType" name="userType" class="form-control input-sm fl w15">
                        	<option value="">全部</option>
                        	<option value="Group" <c:if test="${userType eq 'Group' }">selected</c:if>>工作组</option>
                        	<option value="User" <c:if test="${userType eq 'User' }">selected</c:if>>用户</option>
                        </select>
                  	</div>
                	<div class="fl mr_30">
                        <label id="lblUserNo" for="userNo" class="label-header label-header1">用户编码：</label>
                        <input id="userNo" name="userNo" class="form-control input-sm fl w15" value="${userNo }">
                  	</div>
                  	<div class="fl mr_30">
						<label id="lblUserName" for="userName" class="label-header label-header1">用户名称：</label>
						<input id="userName" name="userName" class="form-control input-sm fl w15" value="${userName }">
	                </div>
                    <div class="fl mr_30">
			          	 <hq:auth act="PermissionList">
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
			          		<hq:auth act="PermissionSave">
			          			<button class="btn_header" id="btn_save" type="button">保存</button>
			          		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
<!-- 			          	<div class="btn-group"> -->
<%-- 			          		<hq:auth act="PermissionList"> --%>
<!-- 			          			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
<%-- 			          		</hq:auth> --%>
<!-- 			          	</div> -->
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">权限信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                        <div class="profileleft" style="padding-top:20px;">
                        	<span style="padding:50px;">用户一栏</span>
                        	<div class="profilebox">
                            	<ul id="user" class="ztree"></ul>
                            </div>
                        </div>
                        <div class="profileright" style="padding-top:20px;">
                        	<span style="padding-left:100px;">功能一栏</span>
                            <div class="profilebox">
                                <ul id="module" class="ztree"></ul>
                            </div>
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