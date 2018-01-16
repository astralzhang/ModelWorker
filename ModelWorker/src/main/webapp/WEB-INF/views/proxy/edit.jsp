<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="proxyManager"/>
	<script type="text/javascript">
	var objUserNo;
	var objUserName;
	$(function(){
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		<%-- 被代理人用户选择 --%>
		$("#btn-user").click(function() {
			showUserList();
			objUserNo = $("#userNo");
			objUserName = $("#userName");
		});
		<%-- 代理用户选择 --%>
		$("#btn-proxyUser").click(function() {
			showUserList();
			objUserNo = $("#proxyUserNo");
			objUserName = $("#proxyUserName");
		});
		<%-- 用户选择确定 --%>
		$("#btn-user-confirm").click(function() {
			var arrUser = [];
			$('#userTable input[type="checkbox"]:checked').each(function(){
				var objUser = new Object;
				objUser.userNo = $(this).val();
				objUser.userName = $(this).closest("tr").find("td").eq(2).html();
				arrUser.push(objUser);
			});
			if (arrUser.length > 1) {
				MessageBox.info("请选择一个用户！");
				return;
			}
			objUserNo.val(arrUser[0].userNo);
			objUserName.val(arrUser[0].userName);
			$("#myModal1").modal("hide");
		});
		<%-- 清空代理人 --%>
		$("#btn-clear-proxyUser").click(function() {
			$("#proxyUserNo").val("");
			$("#proxyUserName").val("");
		});
		<%-- 保存 --%>
		$("#btn_save").click(function() {
			if ($("#voucherType").val() == null || $("#voucherType").val() == "") {
				MessageBox.info("请选择单据类型！");
				return;
			}
			if ($("#userNo").val() == null || $("#userNo").val() == "") {
				MessageBox.info("请选择被代理人！");
				return;
			}
			if ($("#proxyUserNo").val() == null || $("#proxyUserNo").val() == "") {
				MessageBox.info("请选择代理人！");
				return;
			}
			if ($("#startDate").val() == null || $("#startDate").val() == "") {
				MessageBox.info("请选择代理开始日期！");
				return;
			}
			if ($("#endDate").val() == null || $("#endDate").val() == "") {
				MessageBox.info("请选择代理结束日期！");
				return;
			}
			MessageBox.processStart();
			var postData = new Object;
			postData.id = $("#id").val();
			postData.voucherType = $("#voucherType").val();
			postData.userNo = $("#userNo").val();
			postData.userName = $("#userName").val();
			postData.proxyUserNo = $("#proxyUserNo").val();
			postData.proxyUserName = $("#proxyUserName").val();
			postData.startDate = $("#startDate").val();
			postData.endDate = $("#endDate").val();
			$.ajax({
				  url:"${rc.contextPath}/proxyManager/proxyUserSave",
				  type:"post",
				  contentType:'application/json',
				  dataType:"json",
				  data:JSON.stringify(postData),
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd(data.errMessage, function() {
							  location.href = "${rc.contextPath}/proxyManager/list";
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
	<%-- 人员一栏画面显示 --%>
	function showUserList() {
		var postData = new Object;
		$('#userSearchForm input[type="text"]').each(function() {
			postData[this.id] = $(this).val();
		});
		MessageBox.processStart();
		$.ajax({
			  url:"${rc.contextPath}/proxyManager/userList",
			  type:"post",
			  dataType:"json",
			  data:postData,
			  success:function(data){
				  if(data.errType == "0"){
					  var tableData = "";
					  var index = 0;
					  tableData += "<tr>";
					  tableData += "<th>&nbsp;</th>";
					  $.each(data.showFields, function(n, value) {
						  tableData += "<th>";
						  tableData += value.name;
						  tableData += "</th>";
					  });
					  tableData += "</tr>";
					  $.each(data.data, function(n, value) {
						  tableData += "<tr>";
						  tableData += "<td>";
						  tableData += "<input type='checkbox' value='" + value["dataKey"] + "'>";
						  tableData += "</td>"
						  $.each(data.showFields, function(m, value2){
							  tableData += "<td>";
							  tableData += value[value2.id];
							  tableData += "</td>";
						  });
						  tableData += "</tr>";
					  });
					  $("#userTable").html(tableData);
					  <%-- 设定检索条件 --%>
					  var searchCond = "";
					  $.each(data.conditionFields, function(m, value){
						  var valueData;
						  if (typeof(data[value.id]) == "undefined") {
							  valueData = "";
						  } else {
							  valueData = data[value.id];
						  }
						  searchCond += "<input type=\"text\" id=\"" + value.id + "\" placeholder=\"" + value.name + "\" value=\"" + valueData + "\" class=\"form-control input-sm fl w15\">&nbsp;";
					  });
					  searchCond += "<div class=\"search\">";
					  searchCond += "<button type=\"button\" class=\"btn btn-block btn-success fa fa-search\" id=\"btn-user-search\" onclick=\"showUserList();\"><span class=\"search-btn\">查找</span></button>";
					  searchCond += "</div>";
					  $("#userSearchForm").html(searchCond);
					  var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
					  modal.show();
					  MessageBox.processClose();
				  } else {
					  MessageBox.processEnd(data.errMessage);
				  }
			  },
			  error:function(XMLHttpRequest, data) {
				  MessageBox.processEnd("系统错误，详细信息：" + data);
			  }
		  });
	}
	</script>
</head>
<body> 
    <!-- Content Wrapper. Contains page content -->
    <fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyyMMdd" var="bb"/>
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>代理用户管理</a></li>
            <li class="active">代理用户</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<hq:auth act="proxyUserSave">
          			<button class="btn_header" id="btn_save" type="button">保存</button>
          		</hq:auth>
          	</div>
          	<!--  <div class="line_left"></div> -->
          	<div class="btn-group">
          		<a class="btn_header" id="btn-back" href="${rc.contextPath }/proxyManager/list">返回</a>
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
                            <h3 class="box-title">代理用户</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
                            	<tbody>
	                            	<tr>
	                            		<td>单据类型</td>
	                            		<td>
	                            			<input type="hidden" id="id" name="id" value="${data.id }">
					                        <select  id="voucherType" name="voucherType" class="form-control input-sm fl w15">
					                        	<option value=""></option>
					                        <c:forEach items="${voucherTypes }" var="voucherTypeBean">
					                        	<option value="${voucherTypeBean.no }" <c:if test="${voucherTypeBean.no eq data.voucherType }">selected</c:if>>${voucherTypeBean.name }</option>
					                        </c:forEach>
					                        </select>
										</td>
	                            	</tr>
	                            	<tr>
	                            		<td>被代理人</td>
	                            		<td>
	                            			<input type="text" id="userName" name="userName" value='<c:choose><c:when test="${empty data.userName }">${user.userName }</c:when><c:otherwise>${data.userName }</c:otherwise></c:choose>' placeholder="被代理人" class="form-control input-sm fl w15" readonly>
	                            			<hq:auth act="editOtherUser">
		                            			<button type="button" class="btn btn-default" id="btn-user" style="float:left;">选择人员</button>
                               				</hq:auth>
                               				<input type="hidden" id = "userNo" name="userNo" value='<c:choose><c:when test="${empty data.proxyUserNo }">${user.userNo }</c:when><c:otherwise>${data.userNo }</c:otherwise></c:choose>'>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>代理人</td>
	                            		<td>
	                            			<input type="text" id="proxyUserName" name="proxyUserName" value="${data.proxyUserName}" placeholder="代理人" class="form-control input-sm fl w15" readonly>
	                            			<button type="button" class="btn btn-default" id="btn-proxyUser" style="float:left;">选择人员</button>
                               				<button type="button" class="btn btn-default" id="btn-clear-proxyUser" style="float:left;">清空人员</button>
                               				<input type="hidden" id = "proxyUserNo" name="proxyUserNo" value="${data.proxyUserNo }">
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>代理开始日期</td>
	                            		<td>
											<div class="input-group date fl w15">
												<input type="text" class="form-control input-sm" id="startDate" value='<c:choose><c:when test="${empty data.startDate}">${bb }</c:when><c:otherwise>${data.startDate } </c:otherwise></c:choose>'  name="startDate" placeholder="请选择日期" readonly>
												<span class="input-group-addon">
													<i class="fa fa-calendar"></i>
												</span>
											</div>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>代理结束日期</td>
	                            		<td>
											<div class="input-group date fl w15">
												<input type="text" class="form-control input-sm" id="endDate" value='<c:choose><c:when test="${empty data.endDate}">${bb }</c:when><c:otherwise>${data.endDate } </c:otherwise></c:choose>'  name="startDate" placeholder="请选择日期" readonly>
												<span class="input-group-addon">
													<i class="fa fa-calendar"></i>
												</span>
											</div>
	                            		</td>
	                            	</tr>
								</tbody>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
        <%-- 用户选择 --%>
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">用户选择</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form" id="userSearchForm">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-user-search" onclick="showAddAudit();"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table id="userTable" class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>选择</th>
	                                    <th>部门编号</th>
	                                    <th>部门名称</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
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