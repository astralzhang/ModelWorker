<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="viewDesignList"/>
	<script type="text/javascript">	
	$(function(){
		//新增流程确定按钮
		$("#btn-voucher").click(function() {
			//document.getElementById("form1").submit();
			if ($("#viewNo").val() == null || $("#viewNo").val() == "") {
				MessageBox.info("请输入单据编号！");
				return;
			}
			if ($("#viewName").val() == null || $("#viewName").val() == "") {
				MessageBox.info("请输入单据名称！");
				return;
			}
			if ($("#voucherType").val() == null || $("#voucherType").val() == "") {
				MessageBox.info("请选择单据类型！");
				return;
			}
			$("#voucherTypeName").val($("#voucherType").find("option:selected").text());
			$("#form1").submit();
			//window.location.href = "${rc.contextPath}/flowDesign/add";
		});
		$("#btn-search").click(function() {
			$("#searchForm").submit();
		});
		
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		$("#btn_add").on('click', function() {
			var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		});
		<%-- 修改自定义画面 --%>
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
				//编辑自定义画面
				//alert(ids[0]);
				window.location.href = "${rc.contextPath}/viewDesign/edit?viewNo=" + ids[0];
			}
		});
		<%-- 发布按钮 --%>
		$("#btn_publish").click(function() {
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要审核的单据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.processEnd("请选择一个画面发布！");
				return;
			}
			$.ajax({
				  url:"${rc.contextPath}/viewDesign/getModule",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processClose();
						  $("#moduleNo").val(data.moduleNo);
						  var modal = $('#myModal2').modal({backdrop: 'static', keyboard: false});
						  modal.show();
					  } else {
						  MessageBos.processEnd(data.errMessage);
					  }
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
		<%-- 自定义画面发布 --%>
		$("#btn-publish-view").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要审核的单据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.processEnd("请选择一个画面发布！");
				return;
			}
			if ($("#moduleNo").val() == null || $("#moduleNo").val() == "") {
				MessageBox.info("请选择发布的模块！");
				return;
			}
			var modules = [];
			if ($("#chkAddModule").prop("checked")) {
				modules.push("add");
			}
			if ($("#chkEditModule").prop("checked")) {
				modules.push("edit");
			}
			if ($("#chkViewModule").prop("checked")) {
				modules.push("view");
			}
			if ($("#chkDeleteModule").prop("checked")) {
				modules.push("delete");
			}
			if ($("#chkSaveModule").prop("checked")) {
				modules.push("save");
			}
			if ($("#chkAuditModule").prop("checked")) {
				modules.push("audit");
			}
			if ($("#chkCancelAuditModule").prop("checked")) {
				modules.push("cancelAudit");
			}
			if ($("#chkAttachmentModule").prop("checked")) {
				modules.push("attachment");
			}
			if ($("#chkSubmitModule").prop("checked")) {
				modules.push("submit");
			}
			if ($("#chkCancelSubmitModule").prop("checked")) {
				modules.push("cancelSubmit");
			}
			if (modules.length == 0) {
				MessageBox.processEnd("请选择需要发布的机能！");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/viewDesign/publish",
				  type:"post",
				  dataType:"json",
				  data:{
					  viewNo :ids.toString(),
					  moduleNo:$("#moduleNo").val(),
					  modules:modules.toString()
				  },
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd("发布成功！", function() {
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
		//审核
		$("#btn_audit").click(function() {
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要审核的单据。");
				return;
			}else{				
			  $.ajax({
				  url:"${rc.contextPath}/overTimeManage/audit",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){					  
					  MessageBox.processEnd(data.errMsg, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		  }
		});
		//弃审		
		$("#btn_audit_cancel").click(function(){
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要弃审的单据。");
				return;
			}else{				
			  $.ajax({
				  url:"${rc.contextPath}/overTimeManage/audit_cancel",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){					  
					  MessageBox.processEnd(data.errMsg, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		  }
		});
		//查看
		$("#btn_view").click(function(){
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要查看的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据进行查看。");
				return;
			} else {
				//只有一条数据,被选择时
				window.location.href = "${rc.contextPath}/overTimeManage/viewOverTime?id=" + ids[0];
			}
		});
		
		//删除按钮按下
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
					  url:"${rc.contextPath}/overTimeManage/delete",
					  type:"post",
					  dataType:"json",
					  data:{
						  ids :ids.toString()
					  },
					  success:function(data){
						  MessageBox.processEnd(data.errMsg, function() {
							  if(data.errType == "0"){
								  $("#searchForm").submit();
							  }
						  });
					  },
					  error:function(XMLHttpRequest, data) {
						  MessageBox.processEnd("系统错误，详细信息：" + data);
					  }
				  });
			   });
		  }
		});
	});

	//获取所有checkbox选中的项目 
	function getId(){
		  var IdArr = [];
		  $("input[name='selectOverTime']").each(function(){
		 	  if( $(this).prop("checked")){
		 		 IdArr.push($(this).val());
		 		 }
		   });
		  return IdArr.join(",");
	}
	</script>
</head>
<body> 

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>画面设计</a></li>
            <li class="active">自定义画面列表</li>
          </ol>

        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/viewDesign/list">
                	<div class="box_sreach clearfix">
                	<div class="fl mr_30">
	                  <label for="viewNo" class="label-header label-header1">自定义画面：</label>
	                  <input type="text" id="search_viewNo" name="viewNo" value="${viewBean.no}" placeholder="自定义画面编号：" class="form-control input-sm fl w15">
                  	</div>
                  <%--
                  	<div class="fl mr_30">
                        <label for="status" class="label-header label-header1">状态：</label>
                        <select id="status" name="status"  class="form-control input-sm fl w15">
                        	<option value=""></option>
                            <option value="0" <c:if test="${flowBean.status eq '0' || flowBean.status eq null}"> selected</c:if>>保存</option>
                            <option value="1" <c:if test="${flowBean.status eq '1' }"> selected</c:if>>已发布</option>
                        </select>
                  	</div>
                  --%>
                   <div class="fl mr_30">
		          		<hq:auth act="flowList">
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
			          		<hq:auth act="viewAdd">
			          			<button class="btn_header" id="btn_add" type="button">新增</button>
			          		</hq:auth>
			          		<hq:auth act="viewEdit">
			          			<button class="btn_header" id="btn_edit" type="button">修改</button>
			          		</hq:auth>
			          		<hq:auth act="viewDelete">
			          			<button class="btn_header" id="btn_delete" type="button">删除</button>
			          		</hq:auth>
			          		<hq:auth act="viewPublish">
			          			<button class="btn_header" id="btn_publish" type="button">发布</button>
			          		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
			          	<div class="btn-group">
			          		<hq:auth act="flowList">
			          			<a class="btn_header" id="btn-search" href="#">搜索</a>
			          		</hq:auth>
			          	</div>
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">自定义画面信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
		                           <th>画面编号</th>
		                           <th>画面名称</th>
		                           <th>单据类型</th>
		                           <th>状态</th>
								</tr>
						      	<c:forEach items="${resultList}" var="view">
						      	 <tbody>
						           <tr>
						           	 <td><input type="checkbox" value="${view.no }"></td>
						             <td>${view.no }</td>
						             <td>${view.name}</td>
						             <td>${view.voucherTypeName}</td>
						             <td>
						             	<c:if test="${view.status eq '0'}">保存</c:if>
						             	<c:if test="${view.status eq '1'}">发布</c:if>
						             </td>
						           </tr> 
						           </tbody>
						       </c:forEach>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">新增画面</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>画面编号：</td>
		                                	<td><input type="text" id="viewNo" name="viewNo" placeholder="画面编号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>画面名称：</td>
		                                	<td><input type="text" id="viewName" name="viewName" placeholder="画面名称：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>单据类型：</td>
		                                	<td>
												<select id="voucherType" name="voucherType" class="form-control input-sm fl w15" style="width:28%;">
													<c:forEach items="${voucherTypeList }" var="voucherType">
														<option value="${voucherType.no }">${voucherType.name }</option>
													</c:forEach>
												</select>
												<input type="hidden" id="voucherTypeName" name="voucherTypeName"/>
		                                	</td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-voucher">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal2" aria-labelledby="myModalLabel2">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel2">发布画面</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>所属模块：</td>
		                                	<td>
												<select id="moduleNo" name="moduleNo" class="form-control input-sm fl w15" style="width:28%;">
													<c:forEach items="${moduleList }" var="module">
														<option value="${module.no }">${module.name }</option>
													</c:forEach>
												</select>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>新增机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkAddModule" checked>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>修改机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkEditModule" checked>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>查看机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkViewModule" checked>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>删除机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkDeleteModule" checked>
		                                	</td>
		                                </tr>
		                                 <tr>
		                                	<td>保存机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkSaveModule" checked>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>审核机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkAuditModule">
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>撤销审核：</td>
		                                	<td align="left">
		                                		<input type="checkbox" id="chkCancelAuditModule">
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>附件机能：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkAttachmentModule" checked>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>提交工作流：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkSubmitModule" checked>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>撤销工作流：</td>
		                                	<td align="left">
												<input type="checkbox" id="chkCancelSubmitModule" checked>
		                                	</td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-publish-view">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>