<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="DeductList"/>
	<script type="text/javascript">	
	$(function(){
		<%-- 检索 --%>
		$("#btn-search").click(function() {
			var param = new Object;
			param.tradeCode = $("#tradeCode").val();
			param.year = $("#year").val();
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/Deduct/query",
				type:"post",
				dataType:"json",
				data:{
					tradeCode:$("#tradeCode").val(),
					year:$("#year").val()
				},
				success:function(data) {
					if (data.errType == "0") {
						var tbody = "";
						if (data.data != undefined && data.data != null) {
							$.each(data.data, function(n, value) {
								tbody += "<tr>";
								tbody += "<td><input type='checkbox' value='" + value.id + "'></td>";
								tbody += "<td>" + value.tradeName + "</td>";
								tbody += "<td>" + value.deductPoint + "</td>";
								tbody += "<td>" + value.deductYear + "</td>";
								if (value.deductCause.length > 10) {
									var cause = value.deductCause.substring(0, 10) + "...";
									tbody += "<td title='" + value.deductCause + "'>" + cause + "</td>";
								} else {
									tbody += "<td>" + value.deductCause + "</td>";
								}
								tbody += "</tr>";
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
		<%-- 新增 --%>
		$("#btn_add").on('click', function() {
			location.href = "${rc.contextPath}/Deduct/add";
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
				window.location.href = "${rc.contextPath}/Deduct/edit?id=" + ids[0];
			}
		});
		<%-- 删除按钮按下 --%>
		$('#btn_delete').click(function(){
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要删除的数据。");
				return;
			}else{
				MessageBox.confirm("确定将此记录删除?",function(){
				  MessageBox.processStart();
				  $.ajax({
					  url:"${rc.contextPath}/Deduct/delete",
					  type:"post",
					  dataType:"json",
					  data:{
						  ids :ids.toString()
					  },
					  success:function(data){
						  if (data.errType == "0") {
							  MessageBox.processEnd("删除成功", function() {
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
		<%-- 工会一栏选择 --%>
		$("#selTrade").click(function() {
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/Deduct/tradeList",
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
							if (value.showVirtual == "Y") {
								if (virtualCode != value.virtualUnionCode) {
									tbody += "<td></td><td><a href='#' ref='" + value.virtualUnionCode + "' openStatus='0'>+" + value.virtualUnionName + "</a></td><td></td>";
									virtualCode = value.virtualUnionCode;
								}
							} else {
								tbody += "<td><input type='checkbox' value='" + value.tradeUnionCode + "'></td>";
								tbody += "<td>" + value.tradeUnionCode + "</td>";
								tbody += "<td>" + value.tradeUnionName + "</td>";
							}
							tbody += "</tr>";
						});
						$("#myModal1 table tbody").html(tbody);
						$("#myModal1 table tbody a").click(function() {
							var virtualCode = $(this).attr("ref");
							var openStatus = $(this).attr("openStatus");
							if (openStatus == "0") {
								//关闭状态，打开处理
								var tr = $(this).closest("tr");
								var tbody = "";
								$.each(data.data, function(n, value) {
									if (value.virtualUnionCode != virtualCode) {
										return true;
									}
									tbody += "<tr virtualParent='" + virtualCode + "'>";
									tbody += "<td><input type='checkbox' value='" + value.tradeUnionCode + "'></td>";
									tbody += "<td>" + value.tradeUnionCode + "</td>";
									tbody += "<td>" + value.tradeUnionName + "</td>";
									tbody += "</tr>";
								});
								$(tr).after(tbody);
								$(this).attr("openStatus", "1");
								var sText = $(this).html();
								sText = "-" + sText.substr(1);
								$(this).html(sText);
							} else {
								//打开状态，关闭处理
								$("#myModal1 table tbody tr[virtualParent='" + virtualCode + "']").remove();
								$(this).attr("openStatus", "0");
								var sText = $(this).html(); 
								sText = "+" + sText.substr(1);
								$(this).html(sText);
							}
						});
						var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
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
		$("#btn-trade-confirm").click(function() {
			var ids = [];
			var code = "";
			var name = "";
			$("#myModal1 table tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					code = $(this).closest("tr").find("td:eq(1)").html();
					name = $(this).closest("tr").find("td:eq(2)").html();
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择工会！");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("不可选择多个工会！");
				return;
			}
			$("#tradeCode").val(code);
			$("#tradeName").val(name);
			$("#myModal1").modal("hide");
		});
		//查询数据
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
            <li><a href="#"><i class="fa fa-dashboard"></i>扣分管理</a></li>
            <li class="active">扣分管理</li>
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
	                        <label for="tradeName" class="label-header label-header1">选择工会：</label>
	                        <input id="tradeName" name="tradeName" class="form-control input-sm w15 fl" readonly>
	                        <button type="button" class="btn btn-default" id="selTrade" style="height:30px;position:relative;left:-30px;border-radius: 0 15px 15px 0;">...</button>
	                        <input type="hidden" id="tradeCode">
	                  	</div>
	                  	<div class="fl mr_30">
							<label for="userName" class="label-header label-header1">扣分年度：</label>
							<select id="year" name="year" class="form-control input-sm w15">
							<c:forEach items="${data.yearList }" var="year">
								<option value="${year.year }" <c:if test="${year.defaultData eq 'Y' }">selected</c:if>>${year.year }</option>
							</c:forEach>
							</select>
		                </div>
                    <div class="fl mr_30">
		                <a class="btn_sreach fr" id="btn-search" href="#">搜 &nbsp;索</a>
                    </div>
		              </div>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                    	<div class="btn_box">
				          	<div class="search">
				          		<hq:auth act="DeductAdd">
				          			<button class="btn_header" id="btn_add" type="button">新增</button>
				          		</hq:auth>
				          		<hq:auth act="DeductEdit">
				          			<button class="btn_header" id="btn_edit" type="button">修改</button>
				          		</hq:auth>
				          		<hq:auth act="DeductDelete">
				          			<button class="btn_header" id="btn_delete" type="button">删除</button>
				          		</hq:auth>
				          	</div>
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">扣分信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                        
                        <div id="table_scroll" style="overflow:auto;">
                            <table class="table table-hover" id="dataTable">
                            <thead>
				            	<tr>
				                   <th></th>
				                   <th>扣分工会</th>
		                           <th>扣分值</th>
		                           <th>扣分年度</th>
		                           <th>扣分原因</th>
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
        <%-- 工会一栏 --%>
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">工会一栏</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/flowDesign/add" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                            	<thead>
		                            		<th></th>
		                            		<th>工会编码</th>
		                            		<th>工会名称</th>
		                            	</thead>
		                                <tbody>
		                                
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-trade-confirm">确定</button>
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