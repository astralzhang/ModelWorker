<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="auditMessage"/>
	<script type="text/javascript" src="${rc.contextPath }/js/view/jquery.freezeheader.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/view/myview.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/view/writeboard.js"></script>
	<script type="text/javascript">	
	$(function(){
		//搜索
		$("#btn-search").click(function() {
			$("#searchForm").submit();
		});
		
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		var auditNext = function() {
			//MessageBox.processStart();
			//MessageBox.processClose();
			$.ajax({
				  url:"${rc.contextPath}/message/auditListByAjax",
				  type:"post",
				  dataType:"json",
				  data:{
					  voucherType :"${voucherType}"
				  },
				  success:function(data){
					  if (data.errType != "0") {
						  MessageBox.info(data.errMessage);
						  return;
					  }
					  //MessageBox.processClose();
					  $("#dataTable").html("");
					  var thead = "<tr><th></th><th>单据类型</th><th>消息</th><th>前一审批人意见</th><th>状态</th></tr>";
					  var tbody = "";
					  var index = 0;
					  $.each(data.data, function(n, value) {
						  tbody += "<tr>";
						  if (index == 0) {
							  tbody += '<td><input type="checkbox" value="' + value.id + '" ref="0" checked><input type="hidden" value="' + value.auditType + '"></td>';
						  } else {
							  tbody += '<td><input type="checkbox" value="' + value.id + '" ref="0"><input type="hidden" value="' + value.auditType + '"></td>';
						  }
						  tbody += "<td>" + value.voucherTypeName + "</td>";
						  tbody += "<td>" + value.message + "</td>";
						  if (value.auditType == undefined || value.auditType == "" || value.auditType == "0") {
							  tbody += "<td>" + value.beforeContent + "</td>";
						  } else {
							  tbody += "<td><img src='" + value.beforeContent + "' height='30'></td>";
						  }
						  tbody += "</tr>";
						  index++;
					  });
					  $("#dataTable").html(thead + tbody);
					  if (index > 0) {
					  	$("#btn_view").trigger("click");
					  } else {
						  MessageBox.info("所有单据已审核完成！");
					  }
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.info("系统错误，详细信息：" + data);
				  }
			  });
		};
		<%-- 加签 --%>
		$("#btn_addAudit").click(function(){
			var ids = [];
			var statuses = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要加签的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据进行加签。");
				return;
			} else {
				//加签画面
				showAddAudit();
			}
		});
		<%-- 加签确定按钮 --%>
		$("#btn-add-audit-confirm").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			var userNos =[];
			$('#userTable tr input[type="checkbox"]:checked').each(function() {
				userNos.push($(this).val());
			});
			if (userNos.length == 0) {
				MessageBox.info("请选择加签人员！");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/addAuditUser",
				  type:"post",
				  dataType:"json",
				  data:{
					  id :ids[0],
					  userNo:userNos.toString()
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  $("#myModal3").modal('hide');
						  if(data.errType == "0"){
							  MessageBox.info("加签成功！");
							  return;
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
		<%-- 查审 --%>
		$("#btn_viewAudit").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择单据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一张单据进行查审。");
				return;
			} else {
				<%-- 只有一条数据,被选择时 --%>
				MessageBox.processStart();
				$.ajax({
					  url:"${rc.contextPath}/flowProcessor/viewAudit",
					  type:"post",
					  dataType:"json",
					  data:{
						  id :ids[0]
					  },
					  success:function(data){
							if (data.errType == "0") {
								var viewAuditData = "";
								$.each(data.data, function(n,value){
									viewAuditData += "<tr><td align='center'><table border='1'><tr>";
									$.each(value, function(m, value2) {										
										if (value2.auditStatus == "0") {
											viewAuditData += "<td style='background-color:#fff;font-weight:900;color:#000;'>";
											viewAuditData += value2.userNo;
											viewAuditData += "  未审核";
										} else if (value2.auditStatus == "1") {
											viewAuditData += "<td style='background-color:#f00;font-weight:900;color:#000;'>";
											viewAuditData += value2.userNo;
											viewAuditData += "  已否决";
										} else if (value2.auditStatus == "2") {
											viewAuditData += "<td style='background-color:#00f;font-weight:900;color:#000;'>";
											viewAuditData += value2.userNo;
											viewAuditData += "  已同意";
										} else if (value2.auditStatus == "4") {
											viewAuditData += "<td style='background-color:#eee;font-weight:900;color:#000;'>";
											viewAuditData += value2.userNo;
											viewAuditData += "  已撤审";
										}
										viewAuditData += "</td>";
									});
									viewAuditData += "</tr></table></td></tr>";
								});
								$("#viewAuditTable tbody").html(viewAuditData);
								MessageBox.processClose();
								var modal = $('#myModal4').modal({backdrop: 'static', keyboard: false});
						    	modal.show();
							} else {
								MessageBox.processEnd(data.errMessage)
							}					  	
					  },
					  error:function(XMLHttpRequest, data) {
						  MessageBox.processEnd("系统错误，详细信息：" + data);
					  }
				  });
			}
		});
		<%-- 审核按钮 --%>
		$("#btn_audit").click(function() {
			//MessageBox.processStart();
			var auditType = "";
			var bError = false;
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				var tempType = $(this).closest("tr").find("input[type='hidden']").val();
				if (auditType == "") {
					auditType = tempType;
				} else {
					if (auditType != tempType) {
						MessageBox.info("必须选中相同签核模式的数据一起签核！");
						bError = true;
						return false;
					}
				}
			});
			if (bError) {
				return;
			}
			if (ids.length == 0) {
				MessageBox.info("请选择要审核的单据。");
				return;
			}else{
				if (auditType == "1") {
					$("#processContent1").css("border", "solid 1px");
					$("#processContent1").html("");
					$("#processContent1").unbind("click");
					$("#processContent1").bind("click", {index:1}, showSign);
				} else {
					$("#processContent1").css("border", "");
					$("#processContent1").html("");
					$('<textarea id="processContent" maxlength="200" class="form-control input-sm fl" style="width:95%;height:120px;margin-left:2px;"></textarea>').appendTo($("#processContent1"));
					$("#processContent1").unbind("click");
				}
				var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
		  }
		});
		<%-- 审核同意 --%>
		$("#btn-processOk").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要审核的单据。");
				return;
			}
			var content = "";
			if ($("#processContent").val() != undefined && $("#processContent").val() != null && $("#processContent").val() != "") {
				content = $("#processContent").val();
			} else {
				content = $("#processContent1 img").attr("src");
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/audit",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString(),
					  processContent:content
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
							  return;
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
			//MessageBox.processClose();
			$("#myModal1").modal("hide");
		});
		<%-- 审核不同意处理 --%>
		$("#btn-processDisagree").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要审核的单据。");
				return;
			}
			var content = "";
			if ($("#processContent").val() != undefined && $("#processContent").val() != null && $("#processContent").val() != "") {
				content = $("#processContent").val();
			} else {
				content = $("#processContent1 img").attr("src");
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/auditDisagree",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString(),
					  processContent:content
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  $("#myModal1").modal('hide');
						  if(data.errType == "0"){
							  $("#searchForm").submit();
							  return;
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
			$("#myModal1").modal("hide");
		});
		<%-- 审核取消 --%>
		$("#btn_auditCancel").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要弃审的单据。");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/auditCancel",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
							  return;
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
		<%-- 撤销提交 --%>
		$("#btn_submitCancel").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要撤销的单据。");
				return;
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/submitCancel",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
							  return;
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
		<%-- 查看--%>
		$("#btn_view").click(function(){
			var ids = [];
			var index = -1;
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				index = $(this).closest("tr").index();
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要查看的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据进行查看。");
				return;
			} else {
				var auditType = $('#dataTable tbody tr input[type="hidden"]').val();
				if (auditType == "1") {
					$("#processContent3").css("border", "solid 1px");
					$("#processContent3").html("");
					if ("${status}" == "" || "${status}" == "0") {
						$("#processContent3").unbind("click");
						$("#processContent3").bind("click", {index:3}, showSign);
					}
				} else {
					$("#processContent3").css("border", "");
					$("#processContent3").html("");
					if ("${status}" == "" || "${status}" == "0") {
						$('<textarea id="processContent2" maxlength="200" class="form-control input-sm fl" style="width:95%;height:120px;margin-left:2px;"></textarea>').appendTo($("#processContent3"));
					} else {
						$('<textarea id="processContent2" maxlength="200" class="form-control input-sm fl" style="width:95%;height:120px;margin-left:2px;" readonly></textarea>').appendTo($("#processContent3"));
					}
					$("#processContent3").unbind("click");
				}
				//$("#processContent3").html("");
				$("#viewData").html('<span style="float:right;" id="attachmentShow"></span>');
				$("#viewData").myview("${rc.contextPath}", ids[0], "${status}");
				<%-- 只有一条数据,被选择时 --%>
				/*MessageBox.processStart();
				$.ajax({
					  url:"${rc.contextPath}/flowProcessor/view",
					  type:"post",
					  dataType:"json",
					  data:{
						  id :ids[0]
					  },
					  success:function(data){
							$("#viewData").html(data.view);
							var modal = $('#myModal2').modal({backdrop: 'static', keyboard: false});
							modal.show();
						  	MessageBox.processClose();
					  },
					  error:function(XMLHttpRequest, data) {
						  MessageBox.processEnd("系统错误，详细信息：" + data);
					  }
				  });*/
			}
		});
		<%-- 审核同意 --%>
		$("#btn-view-processOk").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要审核的单据。");
				return;
			}
			var content = "";
			if ($("#processContent2").val() != undefined && $("#processContent2").val() != null && $("#processContent").val() != "") {
				content = $("#processContent2").val();
			} else {
				content = $("#processContent3 img").attr("src");
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/audit",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString(),
					  processContent:content
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  if(data.errType == "0"){
							  $("#myModal2").unbind("hidden.bs.modal");
							  $("#myModal2").bind("hidden.bs.modal", function() {
								  auditNext();
								  $("#myModal2").unbind("hidden.bs.modal");
							  });
							  $("#myModal2").modal("hide");
							  return;
						  }
						  $("#myModal2").modal("hide");
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data, function() {
						  $("#myModal2").modal("hide");
					  });
				  }
			  });
		});
		<%-- 审核不同意处理 --%>
		$("#btn-view-processDisagree").click(function() {
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要审核的单据。");
				return;
			}
			var content = "";
			if ($("#processContent2").val() != undefined && $("#processContent2").val() != null && $("#processContent").val() != "") {
				content = $("#processContent2").val();
			} else {
				content = $("#processContent3 img").attr("src");
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/auditDisagree",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString(),
					  processContent:content
				  },
				  success:function(data){
					  MessageBox.processEnd(data.errMessage, function(){
						  if(data.errType == "0"){
							  $("#myModal2").unbind("hidden.bs.modal");
							  $("#myModal2").bind("hidden.bs.modal", function() {
								  auditNext();
								  $("#myModal2").unbind("hidden.bs.modal");
							  });
							  $("#myModal2").modal("hide");
							  return;
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
			$("#myModal2").modal("hide");
		});
	});
	<%-- 显示签核数据 --%>
	function showSign(event) {
		var index = event.data.index;
		$("#myModal5").css("top", "0px;");
		$("#myModal5").height($(window).height());
		$("#myModal5").width($(window).width());
		if ($("#processContent" + index).html() != null && $("#processContent" + index).html() != "") {
			MessageBox.confirm("您是否要重新签名！", function () {
				$("#processContent" + index).html("");
				writeboard($("#divCanvas"),null,null,null,null,function(x,y,n,t,imgurl){//callback 数据
					//alert(imgurl);//base64 图片，用于存放至服务器
					//alert(x,y,n,t)//绘制数据，
						//$("#showimg").attr('src',imgurl);
				},1,{"font":""},index);
			})
		} else {
			writeboard($("#divCanvas"),null,null,null,null,function(x,y,n,t,imgurl){//callback 数据
				//alert(imgurl);//base64 图片，用于存放至服务器
				//alert(x,y,n,t)//绘制数据，
					//$("#showimg").attr('src',imgurl);
			},1,{"font":""},index);
		}		
		var modal = $('#myModal5').modal({backdrop: 'static', keyboard: false});
		modal.show();
	}
	<%-- 加签画面显示 --%>
	function showAddAudit() {
		var ids = [];
		var statuses = [];
		$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
			ids.push($(this).val());			
		});
		if (ids.length == 0) {
			MessageBox.info("请选择要加签的数据。");
			return;
		} else if (ids.length > 1) {
			MessageBox.info("请选择一条数据进行加签。");
			return;
		}
		var postData = new Object;
		postData["id"] = ids.toString();
		$('#userSearchForm input[type="text"]').each(function() {
			postData[this.id] = $(this).val();
		});
		MessageBox.processStart();
		$.ajax({
			  url:"${rc.contextPath}/flowProcessor/userList",
			  type:"post",
			  dataType:"json",
			  data:postData,
			  success:function(data){
				  if(data.errType == "0"){
					  var tableData = "";
					  var index = 0;
					  tableData += "<tr>";
					  tableData += "<th><input type=\"checkbox\"></th>";
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
					  searchCond += "<button type=\"button\" class=\"btn btn-block btn-success fa fa-search\" id=\"btn-user-search\" onclick=\"showAddAudit();\"><span class=\"search-btn\">查找</span></button>";
					  searchCond += "</div>";
					  $("#userSearchForm").html(searchCond);
					  var modal = $('#myModal3').modal({backdrop: 'static', keyboard: false});
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
		//MessageBox.processClose();
		$("#myModal1").modal("hide");
	}
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
	<style type="text/css">
		#mytable_props table {
				
		}
		#mytable_props th {
			letter-spacing: 2px;
			text-align: left;
			padding: 6px;
			background: #ddd;
		}
		#mytable_props td {
			background: #fff;
			padding: 6px;
		}
		.tabContent{margin:0;padding:0;border:1px solid #ccc;display:none;}
		.tab{margin:0;padding:0;list-style:none;width:200px;overflow:hidden;}
		.tab li{float:left;width:60px;height:30px;background:#ccc;color:#fff;border:1px solid red; text-align:center;line-height:30px;cursor:pointer; }
		.on{display:block;}
		.tab li.cur{background:blue;}
	</style>
</head>
<body> 

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>消息中心</a></li>
            <li class="active">审批消息</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<c:choose>
					<c:when test="${empty status || status eq '0' }">
						<%-- 
						<hq:auth act="voucherAudit">
          					<button class="btn_header" id="btn_audit" type="button">审核</button>
          				</hq:auth>
          				--%> 
          				<hq:auth act="voucherView">
          					<button class="btn_header" id="btn_view" type="button">查看</button>
          				</hq:auth>
          				<hq:auth act="addAuditUser">
          					<button class="btn_header" id="btn_addAudit" type="button">加签</button>
          				</hq:auth>
					</c:when>
					<c:when test="${status eq '2' }">
						<hq:auth act="voucherAuditCancel">
							<button class="btn_header" id="btn_auditCancel" type="button">弃审</button>
						</hq:auth> 
          				<hq:auth act="voucherView">
          					<button class="btn_header" id="btn_view" type="button">查看</button>
          				</hq:auth>
					</c:when>
					<c:when test="${status eq '3' }">
          				<hq:auth act="voucherView">
          					<button class="btn_header" id="btn_view" type="button">查看</button>
          				</hq:auth>
					</c:when>
					<c:when test="${status eq '1' }">
						<hq:auth act="voucherSubmitCancel">
							<button class="btn_header" id="btn_submitCancel" type="button">撤销</button>
						</hq:auth>
						<hq:auth act="voucherViewAudit">
							<button class="btn_header" id="btn_viewAudit" type="button">查审</button>
						</hq:auth>
          				<hq:auth act="voucherView">
          					<button class="btn_header" id="btn_view" type="button">查看</button>
          				</hq:auth>
					</c:when>
					<c:otherwise>
						<hq:auth act="flowView">
          					<button class="btn_header" id="btn_view" type="button">查看</button>
          				</hq:auth>
					</c:otherwise>
				</c:choose>	
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<hq:auth act="auditMessage">
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
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/message/auditList">
                	<div class="fl">
	                  <label for="voucherType" class="label-header label-header1">单据类型：</label>
	                  <select class="form-control input-sm fl w15" id="voucherType" name="voucherType">
	                  	<option value="">全部</option>
	                  	<c:forEach items="${voucherTypeList }" var="type">
	                  		<option value="${type.no }" <c:if test="${type.no == voucherType }">selected</c:if>>${type.name }</option>
	                  	</c:forEach>
	                  </select>
	               </div>
	               <div class="fl">
	                  <label for="status" class="label-header label-header1">审核状态：</label>
	                  <select class="form-control input-sm fl w15" id="status" name="status">
	                  	<option value="0" <c:if test="${status eq '0' }">selected</c:if>>未审核</option>
	                  	<option value="2" <c:if test="${status eq '2' }">selected</c:if>>我同意</option>
	                  	<option value="3" <c:if test="${status eq '3' }">selected</c:if>>我否决</option>
	                  	<option value="1" <c:if test="${status eq '1' }">selected</c:if>>我提交</option>
	                  </select>
                  	</div>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">审批信息</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding" id="table_scroll">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
		                           <th>单据类型</th>
		                           <th>消息</th>
		                        <c:choose>
		                        	<c:when test="${empty status || status eq '0' }"><th>前一审批人意见</th></c:when>
		                        	<c:when test="${status eq '2' }">
		                        		<th>审批意见</th>
		                        		<th>审批时间</th>
		                        	</c:when>
		                        	<c:when test="${status eq '3' }">
		                        		<th>退回意见</th>
		                        		<th>退回时间</th>
		                        	</c:when>
		                        	<c:otherwise>
		                        		<th>提交时间</th>
		                        	</c:otherwise>
		                        </c:choose>	
		                           <th>状态</th>
								</tr>
						      	 <tbody>
						      	 <c:forEach items="${data}" var="message">
						           <tr>
						           	 <td><input type="checkbox" value="${message.id }" ref="${status }"><input type="hidden" value="${message.auditType }"></td>
						             <td>${message.voucherTypeName }</td>
						        <c:choose>
						        	<%-- 未审核 --%>
		                        	<c:when test="${empty status || status eq '0' }">
		                        		<td>${message.message }</td>
		                        		<td>
			                        		<c:if test="${empty message.auditType || message.auditType eq '0'}">${message.beforeContent}</c:if>
			                        		<c:if test="${message.auditType eq '1'}"><img src="${message.beforeContent}"  height="30"></c:if>
										</td>
		                        	</c:when>
		                        	<%-- 我同意 --%>
		                        	<c:when test="${status eq '1' }">
		                        		<td>${message.message }</td>
		                        		<td>${message.processTime }</td>
		                        	</c:when>
		                        	<%-- 我提交 --%>
		                        	<c:otherwise>
		                        		<td>${message.message }</td>
		                        		<td>
		                        			<c:if test="${empty message.auditType || message.auditType eq '0'}">${message.content}</c:if>
		                        			<c:if test="${message.auditType eq '1'}"><img src="${message.content}"  height="30"></c:if>
		                        		</td>
		                        		<td>${message.processTime }</td>
		                        	</c:otherwise>
		                        </c:choose>
		                        	 <td>
		                        	 <c:choose>
		                        	 	<c:when test="${empty message.status || message.status eq 'N' }">审核中</c:when>
		                        	 	<c:when test="${message.status eq 'Y' }">审核完成</c:when>
		                        	 	<c:otherwise>已取消</c:otherwise>
		                        	 </c:choose>
		                        	 </td>                           
						           </tr>
						           </c:forEach>
						           </tbody>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
        <%-- 审核画面 --%>
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">审批意见</h4>
                    </div>
                    <div class="row" style="margin-left: 0px;">
                        <form role="form">
                            <div id="processContent1" style="width:95%;height:120px;"></div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-processOk">同意</button>
                        <button type="button" class="btn btn-default" id="btn-processDisagree">不同意</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 查看画面 --%>
        <div class="modal fade" id="myModal2" aria-labelledby="myModalLabel2">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">审批意见</h4>
                    </div>
                    <div id="viewData" class="row" style="margin-left: 0px; height:300px; width:900px; overflow-y:auto; overflow-x:auto;">
						<span style="float:right;" id="attachmentShow"></span>
                    </div>
                    <div class="row" style="margin-left: 0px;">
                        <form role="form">
                            审核意见:<div id="processContent3" style="width:95%;height:120px;"></div>
                        </form>
                    </div>
                    <div class="modal-footer">
		                <button type="button" class="btn btn-default" id="btn-view-processOk">同意</button>
                        <button type="button" class="btn btn-default" id="btn-view-processDisagree">不同意</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 用户一栏 --%>
        <div class="modal fade" id="myModal3" aria-labelledby="myModalLabel3">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel3">用户一栏</h4>
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
                        <button type="button" class="btn btn-default" id="btn-add-audit-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 查审 --%>
        <div class="modal fade" id="myModal4" aria-labelledby="myModalLabel4">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel4">查审</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table id="viewAuditTable" class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>审批状况</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-add-audit-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 签名 --%>
        <div class="modal fade" id="myModal5" aria-labelledby="myModalLabel5" style="width:1000px;height:600px;overflow-y:hidden;">
            <div class="modal-dialog modal-sm" style="width:100%;height:100%;overflow-y:hidden;">
                <div class="modal-content" style="width:100%;height:100%;overflow-y:hidden;">
                    <div class="modal-header" style="width:100%;height:10%;">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel5">请签名</h4>
                    </div>
                    <div class="modal-body" style="width:100%;height:70%;">
                        <div id="divCanvas" class="row" style="margin-left: 0px;width:100%;height:100%;">
                            <canvas style="position:absolute; left:0px; top:0px; z-index:999;"></canvas>
                        </div>
                    </div>
                    <div class="modal-footer" style="width:100%;height:20%;">
                        <button type="button" class="btn btn-default" id="btn-sign-confirm">确定</button>
                        <button type="button" class="btn btn-default" id="btn-redraw">重写</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 附件一栏 --%>
        <div class="modal fade" id="myAttachmentModal" aria-labelledby="myAttachmentModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myAttachmentModalLabel">附件</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>文件名</th>
	                                    <th>文件类型</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myAttachmentViewModal" aria-labelledby="myAttachmentViewModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myAttachmentViewModalLabel">附件</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;overflow-y:auto;">
                        </div>
                        <div id="PageButton" style="display:none; padding-left:45%">
                        	<input type="hidden" id="PageNo" value="1">
                        	<button type="button" id="PrevPage" class="btn btn-default">上一页</button>
                        	<span id="CurrentPageNo">1</span>/<span id="TotalPages">1</span>
                        	<button type="button" id="NextPage" class="btn btn-default">下一页</button>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>