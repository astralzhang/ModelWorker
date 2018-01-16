<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<html>
<head>
	<meta name="menuId" content="system-rolemanage"/>
    <title></title>
    <link rel="stylesheet" href="${rc.contextPath}/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${rc.contextPath}/js/jquery.ztree.all-3.5.min.js"></script>
</head>
<body>
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>系统管理
            <small>角色管理</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>系统管理</a></li>
            <li class="active">角色管理</li>
        </ol>
    </section>
    <div class="boxmain">
        <li class="fl box-w">
            <ul id="treeRoles" class="ztree"></ul>
        </li>
        <!-- Main content -->
        <section class="content fl box-cont">
            <div>
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">角色基本信息</a></li>
                    <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">可操作的功能</a></li>
                    <li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">授权用户</a></li>
                </ul>
                <!-- Tab panes -->
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="home">
                        <!-- Main content -->
                        <section>
                            <!-- Horizontal Form -->
                            <div class="box box-info">
                                
                                <div class="box-header with-border header-ml">
                                    <h3 class="box-title">角色基本信息</h3>
                                    <div class="pull-right">
                                      <hq:auth act="addRole">
                                        <button type="button" id="addrole" class="btn btn-info">添加</button>
                                      </hq:auth>
                                      <hq:auth act="deleteRole">
                                        <button type="button" id="deleterole" class="btn btn-info">删除</button>
                                      </hq:auth>
                                    </div>
                                </div><!-- /.box-header -->
                                <!-- form start -->
                                
                                <form class="form-horizontal" id="roleinfoform" action="<c:url value="/role/addoreditrolemanage"/>" method="post">
                                    <div class="box-body">
                                        <div class="form-group txt-left">
                                            <label class="personnel-left control-label"><span class="required">*</span>角色名称：</label>
                                            <div class="personnel-right">
                                                <input type="text" class="form-control" name="roleName" id="roleName" required maxlength="100"/>
                                                <input type="hidden" name="roleId" id="roleId" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="personnel-left control-label">描述：</label>
                                            <div class="personnel-right">
                                                <textarea name="remark" id="remark" class="form-control" rows="3" cols="58"></textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="personnel-left control-label">类型：</label>
                                            <div class="personnel-right">
                                                <select name="roleType" id="roleType" class="form-control">
                                                	<option value="" ></option>
                                                	<option value="1" >PA中台</option>
                                                	<option value="2" >企业HR</option>
                                                	<option value="3" >一般用户</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div><!-- /.box-body -->
                                    <div class="box-footer">
                                      <hq:auth act="saveRole">
                                        <button type="button" class="btn btn-info btn-w" id="saverole">保存</button>
                                      </hq:auth>
                                    </div><!-- /.box-footer -->
                                </form>
                            </div><!-- /.box -->
                        </section><!-- /.content -->
                    </div><!-- 第一部分结束 -->
                    
                    <div role="tabpanel" class="tab-pane" id="profile">
                      <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">角色授权</h3>
                         </div><!-- /.box-header -->
                        <div class="profileleft">
                            <ul id="treeModule" class="ztree"></ul>
                        </div>
                        <div class="profileright">
                            <div class="profilebox">
                                <table class="table table-hover">
                                    <tr>
                                        <td><input id="checkAll" type="checkbox"></td>
                                        <td>权限名称</td>
                                    </tr>
                                    <tbody id="privilegemanagerlist-data">
                                    	<tr>
                                    		<td><input type="checkbox"></td>
                                    		<td>测试</td>
                                    	</tr>
                                    	<tr>
                                    		<td><input type="checkbox"></td>
                                    		<td>测试</td>
                                    	</tr>
                                    	<tr>
                                    		<td><input type="checkbox"></td>
                                    		<td>测试</td>
                                    	</tr>
                                    	<tr>
                                    		<td><input type="checkbox"></td>
                                    		<td>测试</td>
                                    	</tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="box-footer">
                              <hq:auth act="savePrivilege">
                                <button class="btn btn-info btn-w" id="privilegemanagersave">保存</button>
                              </hq:auth>
                            </div>
                        </div>
                      </div>
                    </div><!--第二部分结束-->
                        
                        <div role="tabpanel" class="tab-pane" id="messages">
                            <div class="box box-info" style="overflow:scroll;height:650px;">
                              <br>
                              <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="firstname" class="col-sm-1 label-header">账号:</label>
                                    <div class="fl w15">
                                       <input type="text" id="accountId" class="form-control input-sm">
                                    </div>
                                    
                                    <label for="firstname" class="col-sm-1 label-header">姓名:</label>
                                    <div class="fl w15">
                                       <input type="text" id="accountName" class="form-control input-sm">
                                    </div>
                  									<div class="search">
                  										<button class="btn btn-block btn-success fa fa-search" id="btn-search"><span class="search-btn">搜索</span></button>
                  									</div>
                              	</div>
                              </div>
	                            <div class="box-header">
	                                <h3 class="box-title">用户信息</h3>
	                            </div><!-- /.box-header -->
	                            <div class="box-body table-responsive no-padding" style="overflow:auto">
	                                <table id="userprivilegelist-data" class="table table-hover">
	                                	<thead>
	                                    <tr>
	                                        <th></th>
	                                        <th>账号</th>
	                                        <th>真实姓名</th>
	                                        <th>电话号码</th>
	                                        <th>邮箱</th>
	                                        <th>用户状态</th>
	                                    </tr>
	                                    </thead>
	                                    <tbody></tbody>
	                                </table>
	                            </div><!-- /.box-body -->
	                            <div class="box-footer">
	                              <hq:auth act="saveUserPrivilege">
	                                	<button type="button" class="btn btn-info btn-w" id="userprivilegesave">保存</button>
	                              </hq:auth>
								</div>
                            </div><!-- /.box -->
                </div>
			</div>
        </section>
    </div>
</div><!-- content-wrapper -->

<script type="text/javascript">
var table = $('#userprivilegelist-data').dataTable({
	"language": tableLanguage,
	"bPaginate": true,  //翻页功能
	"bLengthChange": false, //改变每页显示数据数量
	"bFilter": false, //过滤功能
	"bSort": false, //排序功能
	"bInfo": true,//页脚信息
	"bAutoWidth": true, //自动宽度
	"bProcessing": true, //当datatable获取数据时候是否显示正在处理提示信息。
	"sServerMethod": "POST",
	"sAjaxSource": '${rc.contextPath}/role/getaccountmanagebyid',
	"fnServerParams": function(aoData){
		aoData.push(
			{"name":"rolemanageid","value":$('#roleId').val()},
			{"name":"accountId","value":$('#accountId').val()},
			{"name":"accountName","value":$('#accountName').val()}
		);
	},
	"bServerSide" : true,
	"columns": [
		{
			createdCell : function(td, cellData, rowData, row, col) {
				if (rowData.checked == 1) {
					$(td).html('<input type="checkbox" name="selectUser" checked="checked" value='+rowData.id+'>');
				} else {
					$(td).html('<input type="checkbox" name="selectUser" value='+rowData.id+'>');
				}
				
			},
			data : null
		},
		{ data: 'accountId'},
		{ data: 'accountName'},
		{ data: 'mobile'},
		{ data: 'email'},
		{ 
			data: 'checked',
			render : function(data, type, full) {
				if (data == 1) {
					return "使用中";
				} 
				return "";
			}
		}]
	});

		String.prototype.replaceAll = function(s1, s2) {      
		    return this.replace(new RegExp(s1, "gm"), s2); //g全局     
		}
		
	    var settingRole = {
			view:{
				showLine:true,
				selectedMulti: false
			},
			data:{
				simpleData:{
					enable:true, 
					idkey: "roleId", 
					pidkey: "pId", 
					rootpid: null
				}
			},
			callback: {
				onClick: zTreeRoleOnClick
			}
		};
	    
	    //角色菜单的点击事件
		function zTreeRoleOnClick(event, treeId, treeNode) {
			var tId = treeNode.tId;
			var id = tId.split("_")[1];
			//console.log(treeNode.id);
			if(treeNode.roleId != "0"){
				$.ajax({
					type: "post",
		            url: "${rc.contextPath}/role/getrolemanagebyid",
		            data: {"rolemanageid":treeNode.roleId},
		            dateType:"json",
		            success: function(data){
		            	eval( "datas = " + data );
		            	$('#roleName').val(datas.name);
		            	$('#roleId').val(datas.id);
		            	$('#roleType').val(datas.roleType);
		            	if('' != datas.remark && 'null' != datas.remark)
		            		$('#remark').val(datas.remark);
		            	else
		            		$('#remark').val('');
		            	//console.log(datas);
		            	table.fnDraw();
		            },
		            error:function(err){
		            	//console.log(err.status);
		            }
				});
			}else{
				$('#roleName').val('');
            	$('#id').val('');
            	$('#roleType').val('');
           		$('#remark').val('');
			}
		};
		
		var settingM = {
				view:{
					showLine:true,
					selectedMulti: false
				},
				data:{
					simpleData:{
						enable:true
					}
				},
				callback: {
					onClick: zTreeMOnClick
				}
			};
		    
		    //角色菜单的点击事件
			function zTreeMOnClick(event, treeId, treeNode) {
				var tId = treeNode.tId;
				var id = tId.split("_")[1];
				if(treeNode.isParent) return;
				if($('#roleId').val() == '') return;
				$.ajax({
					type: "post",
		            url: "<c:url value='/role/getprivilegemanagebyid'/>",
		            data: {
		            	"rolemanageid":$('#roleId').val(),
		            	"moduleid":treeNode.id
	            	},
		            dataType: "json",
		            success: function(data){
		            	//console.log(data);
		            	var dataHtml = '';
		            	for(var i = 0; i < data.length; i++){
			            	dataHtml = dataHtml +'<tr>';
			            	if(data[i].checked == 0)
		            			dataHtml = dataHtml + '<td><input name="selectAction" type="checkbox" value="' + data[i].id + '"/></td>';
	            			else
	            				dataHtml = dataHtml + '<td><input name="selectAction" checked="checked" type="checkbox" value="' + data[i].id + '"/></td>';
		            		dataHtml = dataHtml + '<td>' + data[i].priName + '</td>';
		            		/*dataHtml = dataHtml + '<td>' + data[i].priName + '</td>';
		            		dataHtml = dataHtml + '<td>' + data[i].priName + '</td>';*/
			            	dataHtml = dataHtml + '</tr>';
		            	}
		            	$("#checkAll").prop("checked",false);
		            	$("#privilegemanagerlist-data").html(dataHtml);
		            }
				})
			};
		
		/*table 中的checkbox 的全选功能  */
		  $("#checkAll").click(function(){
			 if($("#checkAll").prop("checked")){
				 $("input[name='selectAction']").each(function(){
		               $(this).prop("checked",true);			 
				 })
			 }else{
				 $("input[name='selectAction']").each(function(){
		             $(this).prop("checked",false);			 
				 })
			 } 
		  });
	    	
	    	//加载角色树状菜单--Start
			var nodesVal = '${nodesRole}';
			var pid = '{"id":"0","name":"角色名称","pId":null}';
			var jsonArr = eval('(' + nodesVal.replaceAll('roleName','pId":"0","name') + ')');
			//console.log(jsonArr);
			jsonArr.push(JSON.parse(pid));
			
			$.fn.zTree.init($("#treeRoles"), settingRole, jsonArr);
			var treeObj = $.fn.zTree.getZTreeObj("treeRoles");
			//菜单全部展开
			treeObj.expandAll(true);
			var nodes = treeObj.getNodes();
			//console.log(nodes[0].children[0].id)
			if (nodes.length>1) {
				treeObj.selectNode(nodes[0].children[0]);	
				$.ajax({
					type: "post",
		            url: "${rc.contextPath}/role/getrolemanagebyid",
		            data: {"rolemanageid":nodes[0].children[0].id},
		            dateType:"json",
		            success: function(data){
		            	eval( "datas = " + data );
		            	$('#roleName').val(datas.name);
		            	$('#roleId').val(datas.roleId);
		            	if('' != datas.remark && 'null' != datas.remark)
		            		$('#remark').val(datas.remark);
		            	else
		            		$('#remark').val('');
		            	//console.log(datas);
		            },
		            error:function(err){
		            	//console.log(err.status);
		            }
				});
			}
			//加载角色树状菜单--End
			//console.log(nodesVal)
			
			var nodesValA = '${nodesApplication}';
			var jsonArrA = eval('(' + nodesValA.replaceAll('appName','name').replaceAll('datas','children').replaceAll('moduleName','name').replaceAll('parentId','pId').replaceAll('moduleId','id') + ')');
			$.fn.zTree.init($("#treeModule"), settingM, jsonArrA);
			var treeObjM = $.fn.zTree.getZTreeObj("treeModule");
			//菜单全部展开
			treeObjM.expandAll(true);
			//console.log(nodesValA.replaceAll('appName','name'))
			$("#roleinfoform").validate({
				rules: {
					remark: {
						maxlength:200
					}
				}
			});
			
			$('#saverole').on('click',function(){
				if($("#roleinfoform").valid()){
					$('#roleinfoform').submit();
				}
			});
			
			$('#addrole').on('click',function(){
				$('#roleName').val('');
            	$('#roleId').val('');
            	$('#roleType').val('');
           		$('#remark').val('');
			});
			
			
			$('#deleterole').on('click',function(){
				var id = $('#roleId').val();
				if('' == id){
					MessageBox.info('未选中角色，无法进行删除操作');
				} else if ('99999999' == id) {
					MessageBox.info('不能删除此角色');
				} else{
					$.ajax({
						url:"${rc.contextPath}/role/judgeCancel",
						type:"post",
						dataType:"json",
						data:{
							rolemanageid:id
						},
						success:function(data){
							if(data.errType != '0'){
								MessageBox.error('角色下存在对应的用户与功能，无法进行删除操作');
								return;
							}else{
								MessageBox.confirm("确认删除该角色?",function(){
									$.ajax({
										type: "post",
							            url: "${rc.contextPath}/role/delrolemanagebyid",
							            data: {"rolemanageid":id},
							            success: function(data){
							            	//console.log(data);
							            	window.location.reload();
							            },
									});
								});
							}
						}
					});
				}
			});
			
	    $('#privilegemanagersave').on('click',function(){
	    	if('' == $('#roleId').val()){
				MessageBox.info('未选中角色，无法进行操作');
				return;
			}
			var privileges = "";
			var privilegesAll = "";
	    	$("input[name='selectAction']").each( function(){
				if( $(this).prop("checked")){
					privileges += $(this).val() + ";";
				}
				privilegesAll += $(this).val() + ";";
			});
	    	if(privilegesAll == ''){
	    		MessageBox.info('权限列表中无数据,无法进行授权操作');
	    		return false;
	    	}
	    	
	    	$.ajax({ 
	    		type: "post",
	    		dataType:"json",
				url: '${rc.contextPath}/role/saveprivilegemanage', 
				data: {"rolemanageid" : $('#roleId').val(), "privilegeIds" : privileges, "privilegeIdsAll" : privilegesAll}, 
				success: function(){
					
		      	}
			});
	    });
	    
	    $('#userprivilegesave').on('click',function(){
			if('' == $('#roleId').val()){
				MessageBox.info('未选中角色，无法进行操作');
				return;
			}
			
			var userprivilege = "";
			var userprivilegeAll = "";
	    	$("input[name='selectUser']").each( function(){
				if( $(this).prop("checked")){
					userprivilege += $(this).val() + ";";
				}
				userprivilegeAll += $(this).val() + ";";
			});
	    	if(userprivilegeAll == ''){
                MessageBox.info('权限列表中无数据,无法进行授权操作');
	    		return false;
	    	}
	    	$.ajax({ 
				url: '${rc.contextPath}/role/saveuserprivilege', 
	    		type: "post",
	    		dataType:"json",
				data: {"rolemanageid" : $('#roleId').val(), "userprivilege" : userprivilege, "userprivilegeAll" : userprivilegeAll}, 
				success: function(data){
					if (data == "success") {
						table.fnDraw();
					} else {
						MessageBox.error('保存失败');
					}
		      	}
			});
	    });
	    
		$('#btn-search').on('click', function () {
			table.fnDraw();
		});
	</script>
</body>
</html>
