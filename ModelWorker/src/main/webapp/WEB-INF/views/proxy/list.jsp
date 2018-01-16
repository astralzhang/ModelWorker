<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="proxyUserIndex"/>
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
			location.href = "${rc.contextPath}/proxyManager/add";
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
				window.location.href = "${rc.contextPath}/proxyManager/edit?id=" + ids[0];
			}
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
						  MessageBox.processEnd(data.errMessage, function() {
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
            <li><a href="#"><i class="fa fa-dashboard"></i>代理管理</a></li>
            <li class="active">代理用户列表</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<hq:auth act="proxyUserAdd">
          			<button class="btn_header" id="btn_add" type="button">新增</button>
          		</hq:auth>
          		<hq:auth act="proxyUserEdit">
          			<button class="btn_header" id="btn_edit" type="button">修改</button>
          		</hq:auth>
          		<hq:auth act="proxyUserDelete">
          			<button class="btn_header" id="btn_delete" type="button">删除</button>
          		</hq:auth>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<hq:auth act="flowList">
          			<a class="btn_header" id="btn-search" href="#">搜索</a>
          		</hq:auth>
          	</div>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/proxyManager/list">
                	<input type="hidden" id="editOtherUser" name="editOtherUser" value='<hq:auth act="editOtherUser">Y</hq:auth>'>
                	<div class="fl">
                        <label for="overTimeType" class="label-header label-header1">单据类型：</label>
                        <select  id="voucherType" name="voucherType" class="form-control input-sm fl w15">
                        	<option value=""></option>
                        <c:forEach items="${voucherTypes }" var="voucherTypeBean">
                        	<option value="${voucherTypeBean.no }" <c:if test="${voucherTypeBean.no eq voucherType }">selected</c:if>>${voucherTypeBean.name }</option>
                        </c:forEach>
                        </select>
                  	</div>
                  	<div class="fl">
						<label for="startDate" class="label-header label-header1">代理开始日期：</label>
						<div class="input-group date form_date fl w15">
							<input type="text" class="form-control input-sm" id="startDate" value="${startDate}"  name="startDate" placeholder="代理开始日期" readonly>
							<span class="input-group-addon">
								<span class="fa fa-calendar"></span>
							</span>
						</div>
	                </div>
	                <div class="fl">
                  		<label for="endDate" class="label-header label-header1">代理结束日期：</label>
	                  	<div class="input-group date fl w15">
	                      <input type="text" class="form-control input-sm" id="endDate" value="${endDate}"  name="endDate" placeholder="代理结束日期" readonly>
	                      <span class="input-group-addon">
	                      <i class="fa fa-calendar"></i>
	                      </span>
	                  	</div>
                  	</div>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">代理信息</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
				                   <th>单据类型</th>
		                           <th>开始日期</th>
		                           <th>结束日期</th>
		                           <th>代理人</th>
								</tr>
						      	<c:forEach items="${proxyUsers}" var="proxyUser">
						      	 <tbody>
						           <tr>
						           	 <td><input type="checkbox" value="${proxyUser.id }"></td>
						             <td>
						             <c:forEach items="${voucherTypes }" var="voucherTypeBean">
						             	<c:if test="${voucherTypeBean.no eq proxyUser.voucherType }">${voucherTypeBean.name }</c:if>
						             </c:forEach>
						             </td>
						             <td>${proxyUser.startDate}</td>
						             <td>${proxyUser.endDate}</td>
						             <td>${proxyUser.proxyUserName }</td>
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
                        <h4 class="modal-title" id="myModalLabel1">新增流程</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/flowDesign/add" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>流程编号：</td>
		                                	<td><input type="text" id="flowNo" name="flowNo" placeholder="流程编号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>流程名称：</td>
		                                	<td><input type="text" id="flowName" name="flowName" placeholder="流程名称：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>单据类型：</td>
		                                	<td>
												<select id="voucherType" name="voucherType" class="form-control input-sm fl w15" style="width:28%;">
													<c:forEach items="${voucherTypeList }" var="voucherType">
														<option value="${voucherType.no }">${voucherType.name }</option>
													</c:forEach>
												</select>
		                                	</td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-flow">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>