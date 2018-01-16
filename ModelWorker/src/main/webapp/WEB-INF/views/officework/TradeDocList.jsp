<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="TradeDoc"/>
	<script src="${rc.contextPath }/js/view/myfixedtable.js"></script>
	<script type="text/javascript">	
	$(function(){
		//固定1行5列
		$("#table_scroll").myfixedtable({"row":"2","col":"5"});
		<%-- 检索 --%>
		$("#btn-search").click(function() {
			var param = new Object;
			param.TradeCode = $("#TradeCode").val();
			param.InfomationType = $("#InfomationType").val();
			param.Magazine = $("#Magazine").val();
			param.StartDate = $("#StartDate").val();
			param.EndDate = $("#EndDate").val();
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/query",
				type:"post",
				contentType:"application/json",
				dataType:"json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.errType == "0") {
						var tbody = "";
						if (data.data != undefined && data.data != null) {
							var monthFlag = "";
							$.each(data.data, function(m, doc) {
								if (doc.monthFlag !== monthFlag) {
									var month = doc.submitDate.substring(0, 4) + "年" + doc.submitDate.substring(4, 6) + "月";
									tbody += "<tr>";
									tbody += "<td colspan='4' align='left'><font color='red'>" + month + "</font></td>";
									tbody += "</tr>";
									monthFlag = doc.monthFlag;
								}
								tbody += "<tr>";
								tbody += "<td><input type='checkbox' value='" + doc.id + "'></td>"
								var title = doc.title;
								if (title.length > 10) {
									tbody += "<td nowrap><a  href='${rc.contextPath}/TradeDoc/docUseView?id="+doc.id+"' title='" + title + "'>" + title.substring(0, 10) + "...</a></td>";
								} else {
									tbody += "<td nowrap><a  href='${rc.contextPath}/TradeDoc/docUseView?id="+doc.id+"'>" + title + "</a></td>";
								}
								tbody += "<td>" + doc.infoTypeName + "</td>";
								tbody += "<td>" + doc.tradeUnionName + "</td>";
								tbody += "<td>" + doc.submitDate + "</td>";
								$.each(doc.magazine, function(n, magazine) {
									if (magazine.status == "0") {
										tbody += "<td></td>";
									} else {
										var tempData = "已录用";
										if (magazine.submitDate != undefined && magazine.submitDate != null && magazine.submitDate !== "") {
											tempData += "<br>";
											tempData += magazine.submitDate;
										}
										tbody += "<td>" + tempData + "</td>";
									}
								});
								tbody += "<td>" + doc.point + "</td>";
								tbody += "</tr>"
							});
						}
						$("#dataTable tbody").html(tbody);
						MessageBox.processClose();
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		});
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		<%-- 新增按钮处理 --%>
		$("#btn_add").click(function() {
			location.href = "${rc.contextPath}/TradeDoc/add";
		});
		<%-- 修改按钮 --%>
		$("#btn_edit").click(function () {
			var ids = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
				}
			});
			if (ids.length == 0) {
				alert("eeee");
				MessageBox.info("请选择需要修改的信息！");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条信息进行修改！");
				return;
			}
			MessageBox.processStart();
			<%-- check该公文是否可以修改 --%>
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/check",
				type:"post",
				dataType:"json",
				data:{
					id:ids[0]
				},
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processClose();
						location.href = "${rc.contextPath}/TradeDoc/edit?id=" + ids[0];
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		});
		<%-- 公文上报 --%>
		$("#btn_submit").click(function() {
			var ids = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择需要上报的信息！");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条信息进行上报！");
				return;
			}
			<%-- 上报处理 --%>
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/submit",
				type:"post",
				dataType:"json",
				data:{
					id:ids[0]
				},
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processEnd("上报成功！", function () {
							$("#btn-search").trigger("click");
						});
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		});
		<%-- 积分排名 --%>
		$("#btn-point").click(function() {
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/pointList",
				type:"post",
				dataType:"json",
				data:{
					tradeLevel:'02'
				},
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processClose();
						var tbody = "";
						var virtualCode = "";
						$.each(data.data, function(n, value) {
							tbody += "<tr>";
							tbody += "<td>" + (n + 1) + "</td>";
							tbody += "<td>" + value.tradeUnionName + "</td>";
							tbody += "<td>" + value.point + "</td>";
							tbody += "</tr>";
						});
						$("#myModal2 table tbody").html(tbody);
						var modal = $('#myModal2').modal({backdrop: 'static', keyboard: false});
				    	modal.show();
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});			
		});
		//删除按钮按下
		$('#btn_delete').click(function(){
			var ids = [];
			var status = "";
			var errorMsg="";
			$('#dataTable tbody input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要删除的数据。");
				return;
			}else{
				MessageBox.processClose();
				MessageBox.confirm("确定将此记录删除?",function(){
				  MessageBox.processStart();
				  $.ajax({
					  url:"${rc.contextPath}/TradeDoc/deleteDoc",
					  type:"post",
					  dataType:"json",
					  data:{
						  ids :ids.toString()
					  },
					  success:function(data){
						if (data.errType == "0") {
							MessageBox.processEnd(data.errMessage, function () {
								$("#btn-search").trigger("click");
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
		<%-- 驳回按钮 --%>
		$("#btn_return").click(function () {
			var ids = [];
			var status = "";
			var errorMsg="";
			$('#dataTable tbody input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要驳回的数据。");
				return;
			}else{
				$("#returnCause").val("");
				var modal = $('#myReturnModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}
		});
		<%-- 驳回处理 --%>
		$("#btn-return-confirm").click(function() {
			var ids = [];
			var status = "";
			var errorMsg="";
			$('#dataTable tbody input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			//alert(ids);
			if (ids.length == 0) {
				MessageBox.info("请选择要驳回的数据。");
				return;
			}else{
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/TradeDoc/return",
					type:"post",
					dataType:"json",
					data:{
						ids :ids.toString(),
						cause:$("#returnCause").val()
					},
					success:function(data){
						if (data.errType == "0") {
							MessageBox.processEnd("驳回成功！", function () {
								$("#btn-search").trigger("click");
								$('#myReturnModal').modal("hide");
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
		});
		<%-- 新增默认查询功能 --%>
		$("#btn-search").trigger("click");
	});
	</script>
</head>
<body>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>办公工作</a></li>
            <li class="active">公文信息</li>
          </ol>
<!--           <div class="btn_box"> -->
<!--           	<div class="search"> -->
<%--           		<hq:auth act="TradeDocAdd"> --%>
<!--           			<button class="btn_header" id="btn_add" type="button">新增</button> -->
<%--           		</hq:auth> --%>
<%--           		<hq:auth act="SystemUserEdit"> --%>
<!--           			<button class="btn_header" id="btn_edit" type="button">修改</button> -->
<%--           		</hq:auth> --%>
<%--           		<hq:auth act="SystemUserDelete"> --%>
<!--           			<button class="btn_header" id="btn_delete" type="button">删除</button> -->
<%--           		</hq:auth> --%>
<%--           		<hq:auth act="SystemUserDelete"> --%>
<!--           			<button class="btn_header" id="btn_submit" type="button">上报</button> -->
<%--           		</hq:auth> --%>
<%--           		<hq:auth act="SystemUserDelete"> --%>
<!--           			<button class="btn_header" id="btn_return" type="button">驳回</button> -->
<%--           		</hq:auth> --%>
<!--           	</div> -->
<!--           	<div class="line_left"></div> -->
<!--           	<div class="btn-group"> -->
<%--           		<hq:auth act="systemUserList"> --%>
<!--           			<a class="btn_header" id="btn-point" href="#">查看积分排名</a> -->
<%--           		</hq:auth> --%>
<%--           		<hq:auth act="systemUserList"> --%>
<!--           			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
<%--           		</hq:auth> --%>
<!--           	</div> -->
<!--           </div> -->
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post" action="${rc.contextPath}/user/list">
                	<div class="box_sreach clearfix">
	                	<div class="fl mr_30">
	                        <label for="TradeCode" class="label-header label-header1">报送工会：</label>
	                        <select name="TradeCode" class="form-control input-sm w15" id="TradeCode">
	                        	<option value=""></option>
	                        <c:forEach items="${data.TradeInfoList }" var="TradeInfo">
	                        	<option value="${TradeInfo.code }">${TradeInfo.name }</option>
	                        </c:forEach>
	                        </select>
	                  	</div>
	               		<div class="fl mr_30">
							<label for="InfomationType" class="label-header label-header1">信息分类：</label>
							<select id="InfomationType" name="InfomationType" class="form-control input-sm w15">
								<option value=""></option>
							<c:forEach items="${data.InfomationTypeList }" var="InfomationType">
								<option value="${InfomationType.code }">${InfomationType.name }</option>
							</c:forEach>
							</select>
		                </div>
		                <div class="fl mr_30">
							<label for="Magazine" class="label-header label-header1">录用情况：</label>
							<select id="Magazine" name="Magazine" class="form-control input-sm w15">
								<option value=""></option>
							<c:forEach items="${data.magazineList }" var="Magazine">
								<option value="${Magazine.code }">${Magazine.name }</option>
							</c:forEach>
							</select>
	                	</div>
	                	<div class="fl mr_30">
							<label for="StartDate" class="label-header label-header1">报送开始日期：</label>
							<div class="input-group date form_date fl w15">
								<input type="text" class="form-control input-sm" id="StartDate"  name="StartDate" placeholder="报送开始日期" value="${data.StartSubmitDate }" readonly>
								<span class="input-group-addon">
									<span class="fa fa-calendar"></span>
								</span>
							</div>
		                </div>
	                	<div class="fl mr_30">
							<label for="EndDate" class="label-header label-header1">报送结束日期：</label>
							<div class="input-group date form_date fl w15">
								<input type="text" class="form-control input-sm" id="EndDate"  name="EndDate" placeholder="报送结束日期" value="${data.EndSubmitDate }" readonly>
								<span class="input-group-addon">
									<span class="fa fa-calendar"></span>
								</span>
							</div>
		                </div>
                   <div class="fl mr_30">
		          		<hq:auth act="TradeDoc">
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
			          		<hq:auth act="TradeDocAdd">
			          			<button class="btn_header" id="btn_add" type="button">新增</button>
			          		</hq:auth>
			          		<hq:auth act="TradeDocEdit">
			          			<button class="btn_header" id="btn_edit" type="button">修改</button>
			          		</hq:auth>
			          		<hq:auth act="TradeDocDelete">
			          			<button class="btn_header" id="btn_delete" type="button">删除</button>
			          		</hq:auth>
			          		<hq:auth act="TradeDocSubmit">
			          			<button class="btn_header" id="btn_submit" type="button">上报</button>
			          		</hq:auth>
			          		<hq:auth act="TradeDocReturn">
			          			<button class="btn_header" id="btn_return" type="button">驳回</button>
			          		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
			          	<div class="btn-group">
			          		<hq:auth act="ViewPointList">
			          			<a class="btn_header" id="btn-point" href="#">查看积分排名</a>
			          		</hq:auth>
<%-- 			          		<hq:auth act="systemUserList"> --%>
<!-- 			          			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
<%-- 			          		</hq:auth> --%>
			          	</div>
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">公文信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                        <div id="table_scroll" style="overflow:auto;width:100%" >
<!--                         <div id="table_scroll" style="overflow:auto;"> -->
                            <table class="table table-hover" id="dataTable">
                            <thead>
				            	<tr>
				                  	<th></th>
				                   	<th></th>
									<th></th>
									<th></th>
									<th></th>
								<c:forEach items="${data.AcceptLevelList }" var="AcceptLevel">
								<c:if test="${AcceptLevel.count > 0}">
									<th colspan="${AcceptLevel.count }">${AcceptLevel.name }</th>
								</c:if>
								<c:if test="${AcceptLevel.count eq 0}">
									<th rowspan="2">${AcceptLevel.name }</th>
								</c:if>
								</c:forEach>
		                           <th rowspan="2" style="text-align:center;vertical-align:middle;">积分</th>
								</tr>
								<tr>
									<th></th>
									<th style="text-align:center;vertical-align:middle;">公文标题</th>
		                            <th style="text-align:center;vertical-align:middle;">公文类别</th>
		                            <th style="text-align:center;vertical-align:middle;">报送工会</th>
		                            <th style="text-align:center;vertical-align:middle;">上报日期</th>
								<c:forEach items="${data.AcceptLevelList }" var="AcceptLevel">
									<c:forEach items="${AcceptLevel.magazineList }" var="Magazine">
										<th>${Magazine.name }</th>
									</c:forEach>
								</c:forEach>
								</tr>
							</thead>
							<tbody>
							</tbody>
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
                            	<div style="margin-top:20px;max-height:350px;overflow-y:auto;overflow-x:hidden;">
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
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 工会排名 --%>
        <div class="modal fade" id="myModal2" aria-labelledby="myModalLabel2">
            <div class="modal-dialog modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel2">积分排名</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                           	<div style="margin-top:20px;max-height:350px;overflow-y:auto;overflow-x:hidden;">
	                            <table class="table table-bordered table-condensed" style="width:95%;">
	                            	<thead>
	                            		<tr>
	                            			<th>名次</th>
	                            			<th>工会名称</th>
	                            			<th>积分</th>
	                            		</tr>
	                            	</thead>
	                                <tbody>
	                                </tbody>
	                            </table>
							</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-resetPwd-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 驳回窗口 --%>
        <div class="modal fade" id="myReturnModal" aria-labelledby="myReturnModalLabel">
            <div class="modal-dialog modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myReturnModalLabel">驳回处理</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                           	<form role="form">
                           		<div><label for="returnCause">驳回原由:</label></div>
                            	<textarea id="returnCause" name="returnCause" class="form-control input-sm fl" style="width:500px;"></textarea>
							</form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-return-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>