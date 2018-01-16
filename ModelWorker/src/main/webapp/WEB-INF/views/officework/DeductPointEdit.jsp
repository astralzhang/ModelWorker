<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="DeductList"/>
	<script type="text/javascript">
	var objUserNo;
	var objUserName;
	$(function(){
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
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
					if(code != ""){
						code = code+","+$(this).closest("tr").find("td:eq(1)").html();
						name = name+","+$(this).closest("tr").find("td:eq(2)").html();
					}else{
						code = $(this).closest("tr").find("td:eq(1)").html();
						name = $(this).closest("tr").find("td:eq(2)").html();
					}
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择工会！");
				return;
			} 
			$("#tradeCode").val(code);
			$("#tradeName").val(name);
			$("#myModal1").modal("hide");
		});
		<%-- 保存 --%>
		$("#btn_save").click(function() {
			var tradeCode = $("#tradeCode").val();
			if ($("#tradeCode").val() == null || $("#tradeCode").val() == "") {
				MessageBox.info("请选择工会！");
				return;
			}
			var deductPoint = $("#deductPoint").val();
			if ($("#deductPoint").val() == null || $("#deductPoint").val() == "") {
				MessageBox.info("请输入扣分值！");
				return;
			}
			if (isNaN(deductPoint)) {
				MessageBox.info("扣分值必须输入数字！");
				return;
			}
			var deductDate = $("#DeductDate").val();
			if (deductDate == null || deductDate == "") {
				MessageBox.info("请输入扣分日期！");
				return;
			}
			var deductCause = $("#deductCause").val();
			MessageBox.processStart();
			var postData = new Object;
			postData.tradeCode = tradeCode;
			postData.deductPoint = deductPoint;
			postData.deductCause = deductCause;
			postData.deductDate = deductDate;
			postData.id = $("#id").val();
			$.ajax({
				  url:"${rc.contextPath}/Deduct/save",
				  type:"post",
				  contentType:'application/json',
				  dataType:"json",
				  data:JSON.stringify(postData),
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd("保存成功！", function() {
							  location.href = "${rc.contextPath}/Deduct/list";
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
            <li><a href="#"><i class="fa fa-dashboard"></i>扣分管理</a></li>
            <li class="active">扣分编辑</li>
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
				          		<hq:auth act="DeductSave">
				          			<button class="btn_header" id="btn_save" type="button">保存</button>
				          		</hq:auth>
				          	</div>
				          	<div class="line_left"></div>
				          	<div class="btn-group">
				          		<a class="btn_header" id="btn-back" href="${rc.contextPath }/Deduct/list">返回</a>
				          	</div>
				          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">扣分编辑</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
                            	<tbody>
                            		<tr>
	                            		<td>工会名称</td>
	                            		<td>
	                            			<div>
		                            			<input type="text" id="tradeName" name="tradeName" value="${data.tradeName }" class="form-control input-sm fl w15" readonly>
		                            			
												<c:if test="${empty data.id}"><button type="button" class="btn btn-default" id="selTrade" style="float:left;height:30px;position:relative;left:-36px;border-radius:0 15px 15px 0;">...</button></c:if>
<!-- 		                            			<button type="button" class="btn btn-default" id="selTrade" style="float:left;">...</button> -->
		                            			<input type="hidden" id="tradeCode" name="tradeCode" value="${data.tradeCode }" >
		                            			<input type="hidden" id="id" value="${data.id }">
	                            			</div>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>扣分值</td>
	                            		<td>
	                            			<select id="deductPoint" name="deductPoint"  class="form-control2" required>
	                            			<option value="" <c:if test="${empty data.deductPoint}">selected</c:if>></option>
	                            			<option value=1 <c:if test="${data.deductPoint eq 1}">selected</c:if>>1</option>
	                            			<option value=2 <c:if test="${data.deductPoint eq 2}">selected</c:if>>2</option>
	                            			<option value=3 <c:if test="${data.deductPoint eq 3}">selected</c:if>>3</option>
	                            			<option value=4 <c:if test="${data.deductPoint eq 4}">selected</c:if>>4</option>
	                            			<option value=5 <c:if test="${data.deductPoint eq 5}">selected</c:if>>5</option>
	                            			<option value=6 <c:if test="${data.deductPoint eq 6}">selected</c:if>>6</option>
	                            			<option value=7 <c:if test="${data.deductPoint eq 7}">selected</c:if>>7</option>
	                            			<option value=8 <c:if test="${data.deductPoint eq 8}">selected</c:if>>8</option>
	                            			<option value=9 <c:if test="${data.deductPoint eq 9}">selected</c:if>>9</option>
	                            			<option value=10 <c:if test="${data.deductPoint eq 10}">selected</c:if>>10</option>
	                            			</select>
<%-- 	                            			<input type="text" id="deductPoint" name="deductPoint" value="${data.deductPoint }" class="form-control input-sm fl w15"> --%>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>扣分日期</td>
	                            		<td>
	                            			<div class="input-group date form_date fl w15">
												<input type="text" class="form-control input-sm" id="DeductDate"  name="DeductDate" placeholder="扣分日期" value="${data.deductDate }" readonly>
												<span class="input-group-addon">
													<span class="fa fa-calendar"></span>
												</span>
											</div>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>扣分理由</td>
	                            		<td>
	                            			<textarea id="deductCause" rows="8" class="form-control" style="width:90%;">${data.deductCause }</textarea>
	                            		</td>
	                            	</tr>
								</tbody>
                            </table>
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
    </div><!-- /.content-wrapper -->
</body>
</html>