<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="flowList"/>
	<link type="text/css" href="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet" />
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/raphael-min.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/myflow/myflow.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/myflow/myflow.jpdl4.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/myflow/myflow.editors.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/myflow/mymessage.js"></script>
	<script type="text/javascript">
	//用户控件
	var txtUser;
	//条件控件
	var txtCondition;
	$(function(){
		var mymessage = $("#btn-flow-message").mymessage(${MessageTemplate});
		$('#myflow').myflow({
			basePath : "",
			restore : eval("(${flowBean.flowScript})"),
			//restore : eval("({states:{rect1:{type:'start',text:{text:'开始'}, attr:{ x:279, y:50, width:50, height:50}, props:{id:{value:''},text:{value:'开始'}}}},paths:{},props:{props:{name:{value:'${flowName})'},key:{value:''},desc:{value:''}}}})"),
			//restore : eval("({states:{rect1:{type:\"start\",text:{text:\"开始22\"}, attr:{ x:283, y:33, width:50, height:50}, props:{id:{value:\"rect1\"},text:{value:\"开始\"}}},rect2:{type:\"task\",text:{text:\"审批\"}, attr:{ x:257, y:134, width:100, height:50}, props:{id:{value:\"rect2\"},text:{value:\"审批\"},desc:{value:\"\"},processor:{value:\"\"},proxyuser:{value:\"\"},proxyStartDate:{value:\"\"},proxyEndDate:{value:\"\"}}},rect3:{type:\"condition\",text:{text:\"条件\"}, attr:{ x:282, y:240, width:50, height:50}, props:{id:{value:\"rect3\"},text:{value:\"条件\"},desc:{value:\"\"},condition:{value:\"加班单单头.主键>'1111'\"}}},rect4:{type:\"task\",text:{text:\"审批\"}, attr:{ x:259, y:351, width:100, height:50}, props:{id:{value:\"rect6\"},text:{value:\"审批\"},desc:{value:\"\"},processor:{value:\"\"},proxyuser:{value:\"\"},proxyStartDate:{value:\"\"},proxyEndDate:{value:\"\"}}},rect5:{type:\"task\",text:{text:\"审批\"}, attr:{ x:395, y:342, width:100, height:50}, props:{id:{value:\"rect7\"},text:{value:\"审批\"},desc:{value:\"\"},processor:{value:\"\"},proxyuser:{value:\"\"},proxyStartDate:{value:\"\"},proxyEndDate:{value:\"\"}}},rect6:{type:\"end\",text:{text:\"结束\"}, attr:{ x:417, y:475, width:50, height:50}, props:{id:{value:\"rect9\"},text:{value:\"结束\"}}},rect7:{type:\"condition\",text:{text:\"条件\"}, attr:{ x:422, y:242, width:50, height:50}, props:{id:{value:\"rect7\"},text:{value:\"条件\"},desc:{value:\"条件ddd\"},condition:{value:\"\"}}},rect8:{type:\"task\",text:{text:\"审批\"}, attr:{ x:584, y:343, width:100, height:50}, props:{id:{value:\"rect8\"},text:{value:\"审批\"},desc:{value:\"\"},processor:{value:\"\"},proxyuser:{value:\"\"},proxyStartDate:{value:\"\"},proxyEndDate:{value:\"\"}}}},paths:{path9:{from:'rect1',to:'rect2', dots:[],text:{text:'TO 审批'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 审批\"}}},path10:{from:'rect2',to:'rect3', dots:[],text:{text:'TO 条件'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 条件\"}}},path11:{from:'rect3',to:'rect4', dots:[],text:{text:'TO 审批'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 审批\"},yesorno:{value:\"Y\"}}},path12:{from:'rect4',to:'rect6', dots:[],text:{text:'TO 结束'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 结束\"}}},path13:{from:'rect5',to:'rect6', dots:[],text:{text:'TO 结束'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 结束\"}}},path14:{from:'rect3',to:'rect7', dots:[],text:{text:'TO 条件'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 条件\"},yesorno:{value:\"N\"}}},path15:{from:'rect7',to:'rect5', dots:[],text:{text:'TO 审批'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 审批\"},yesorno:{value:\"Y\"}}},path16:{from:'rect7',to:'rect8', dots:[{x:632,y:267}],text:{text:'TO 审批'},textPos:{x:0,y:-10}, props:{text:{value:\"TO 审批\"},yesorno:{value:\"N\"}}},path17:{from:'rect8',to:'rect6', dots:[],text:{text:'TO 结束'},textPos:{x:0,y:-10}, props:{text:{value:\"\"}}}},props:{props:{no:{value:'HR.OverTime'},name:{value:'加班单流程'}}}})"),
			tools : {
				save : {
					onclick : function(data) {
						//$("#myflow").html(data);
						var disagreeAudit = $('input:radio[name="disagreeAudit"]:checked').val();
						MessageBox.processStart();
						$.ajax({
							  url:"${rc.contextPath}/flowDesign/save",
							  type:"post",
							  dataType:"json",
							  data:{
								  flowData : data,
								  flowNo   : "${flowNo}",
								  flowName : $("#txtFlowName").val(),
								  voucherType:"${voucherType}",
								  disagreeAudit:disagreeAudit,
								  auditType:$("#auditType").val(),
								  message:JSON.stringify(mymessage.get_message_template())
							  },
							  success:function(data){
								  if (data.errType == "1") {
									  MessageBox.processEnd("系统错误:" + data.errMessage);
									  return;
								  }
								  window.location.href = "${rc.contextPath}/flowDesign/list";
								  MessageBox.processClose();
							  },
							  error:function(XMLHttpRequest, data) {
								  MessageBox.processEnd("系统错误，详细信息：" + data);
							  }
						  });
					}
				}
			}
		});
		var myflow = $.myflow;
		$.extend(true,myflow.config.props.props,{
			no : {name:'no', label:'编码', editor:function(){return new myflow.editors.inputEditor();},value:'${flowNo}'},
			name : {name:'name', label:'名称', editor:function(){return new myflow.editors.inputEditor();},value:'${flowName}'}
		});
		//增加条件按钮点击
		$("#btn-add-condition").click(function(){
			if ($("#txtValue").val() == null || $("#txtValue").val() == "") {
				MessageBox.info("请输入数据！");
				return;
			}
			var sValue = $("#txtCondition").val();
			var sNewValue = "";
			if (sValue != null && sValue != "") {
				sNewValue += " " + $("#selRelation").val() + "\n";
			}
			//sNewValue += $("#selTableId").val();
			sNewValue += $("#selTableId").find("option:selected").text();
			sNewValue += ".";
			//sNewValue += $("#selFieldId").val();
			sNewValue += $("#selFieldId").find("option:selected").text();
			var sCompare = $("#selCompare").val();
			if (sCompare == "like") {
				sNewValue += ".indexOf('";
				sNewValue += $("#txtValue").val();
				sNewValue += "')>=0";
			} else if (sCompare == "leftlike") {
				sNewValue += ".startWith('";
				sNewValue += $("#txtValue").val();
				sNewValue += "')";
			} else if (sCompare == "rightlike") {
				sNewValue += ".endWith(''";
				sNewValue += $("#txtValue").val();
				sNewValue += "')";
			} else {
				var field = $("#selFieldId").val();
				if (field.charAt(field.length - 1) == "C") {
					sNewValue += $("#selCompare").val();
					sNewValue += "'";
					sNewValue += $("#txtValue").val();
					sNewValue += "'";
				} else {
					sNewValue += $("#selCompare").val();
					sNewValue += $("#txtValue").val();
				}
			}			
			sValue += sNewValue;
			$("#txtCondition").val(sValue);
			$("#txtValue").val("");
			parseCondition(sValue);
		});
		//字段选择变更
		$("#selFieldId").change(function(){
			//修改符号列表
			editCompare("");
			$("#txtValue").val("");
		});
		//数据表选择变更
		$("#selTableId").change(function(){
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowDesign/table/fields",
				  type:"post",
				  dataType:"json",
				  data:{
					  tableId :$("#selTableId").val()
				  },
				  success:function(data){
					  if (data.errType == "1") {
						  MessageBox.processEnd("系统错误:" + data.errMessage);
						  return;
					  }
					  $("#selFieldId").empty();
					  var options = "";
					  $.each(data.data, function(n, value){
						  options += "<option value='" + value.fieldId + "_" + value.fieldType + "'>" + value.fieldName + "</option>";
					  });
					  $("#selFieldId").append(options);
					  //修改符号列表
					  editCompare("");
					  MessageBox.processClose();
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
		//条件输入窗口确定按钮
		$("#btn-condition-confirm").click(function(){
			$(txtCondition).val($("#txtCondition").val().replaceAll("\n", "<br>"));
			$(txtCondition).trigger("change");
			$('#myModal1').modal('hide');
		});
		//用户条件增加按钮
		$("#btn-add-user").click(function(){
			if ($("#txtValue1").val() == null || $("#txtValue1").val() == "") {
				MessageBox.info("请输入数据！");
				return;
			}
			var sValue = $("#txtUserCondition").val();
			var sNewValue = "";
			if (sValue != null && sValue != "") {
				sNewValue += " " + $("#selRelation1").val() + "\n";
			}
			//sNewValue += $("#selTableId").val();
			sNewValue += $("#selTableId1").find("option:selected").text();
			sNewValue += ".";
			//sNewValue += $("#selFieldId").val();
			sNewValue += $("#selFieldId1").find("option:selected").text();
			var sCompare = $("#selCompare1").val();
			if (sCompare == "like") {
				sNewValue += " like '%";
				sNewValue += $("#txtValue1").val();
				sNewValue += "%''";
			} else if (sCompare == "leftlike") {
				sNewValue += " like '";
				sNewValue += $("#txtValue1").val();
				sNewValue += "%'";
			} else if (sCompare == "rightlike") {
				sNewValue += " like '%";
				sNewValue += $("#txtValue1").val();
				sNewValue += "'";
			} else {
				var field = $("#selFieldId1").val();
				if (field.charAt(field.length - 1) == "C") {
					sNewValue += $("#selCompare1").val();
					sNewValue += "'";
					sNewValue += $("#txtValue1").val();
					sNewValue += "'";
				} else {
					sNewValue += $("#selCompare1").val();
					sNewValue += $("#txtValue1").val();
				}
			}			
			sValue += sNewValue;
			$("#txtUserCondition").val(sValue);
			$("#txtValue1").val("");
		});
		//字段选择变更
		$("#selFieldId1").change(function(){
			//修改符号列表
			editCompare("1");
			$("#txtValue1").val("");
		});
		//数据表选择变更
		$("#selTableId1").change(function(){
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowDesign/table/fields",
				  type:"post",
				  dataType:"json",
				  data:{
					  tableId :$("#selTableId1").val()
				  },
				  success:function(data){
					  if (data.errType == "1") {
						  MessageBox.processEnd("系统错误:" + data.errMessage);
						  return;
					  }
					  $("#selFieldId1").empty();
					  var options = "";
					  $.each(data.data, function(n, value){
						  options += "<option value='" + value.fieldId + "_" + value.fieldType + "'>" + value.fieldName + "</option>";
					  });
					  $("#selFieldId1").append(options);
					  //修改符号列表
					  editCompare("1");
					  MessageBox.processClose();
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
		$(":radio").click(function(){
			if ($(this).val() == "C") {
				//条件
				$("#conditionDiv").show();
				$("#sqlDiv").hide();
			} else if ($(this).val() == "S") {
				//SQL文
				$("#conditionDiv").hide();
				$("#sqlDiv").show();
			} else if ($(this).val() == "W") {
				//工作流数据库访问
				
			} else if ($(this).val() == "B") {
				//业务数据库访问
			}
		});
		//新增用户确定按钮点击
		$("#btn-user-confirm").click(function(){
			var data = "";
			if ($("input[name='userConditionType']:checked").val() == "C") {
				data += "<type>C</type>";
			} else if ($("input[name='userConditionType']:checked").val() == "S") {
				data += "<type>S</type>";
			} else {
				MessageBox.info("请选择条件类型！");
				return;
			}
			if ($("input[name='databaseType']:checked").val() == "W") {
				data += "<dbtype>W</dbtype>";
			} else if ($("input[name='databaseType']:checked").val() == "B") {
				data += "<dbtype>B</dbtype>";
			} else {
				if ($("input[name='userConditionType']:checked").val() == "S") {
					MessageBox.info("请选择数据库类型！");
					return;
				}
			}
			data += "<condition><![CDATA[" + $("#txtUserCondition").val() + "]]></condition>";
			data += "<sql><![CDATA[" + $("#txtUserSql").val() + "]]></sql>";
			$(txtUser).val(data);
			$(txtUser).trigger("change");
			$('#myModal2').modal('hide');
		});
		<%-- 流程属性 --%>
		$("#btn-flow-props").click(function() {
			var modal = $('#myModal3').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		});
		<%-- 流程属性确定 --%>
		$("#btn-flow-props-confirm").click(function() {
			$('#myModal3').modal('hide');
		});
	})
	function parseCondition(sCondition) {
		//alert(sCondition);
	}
	//条件输入窗口
	function inputCondition(obj) {
		txtCondition = obj;
		$("#txtCondition").val($(txtCondition).val().replaceAll("<br>", "\n"));
		var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
    	modal.show();
	}
	//人员选择窗口
	function inputUser(obj) {
		txtUser = obj;
		if ($(txtUser).val()) {
			var data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data><processor>" + $(txtUser).val().replaceAll("<br>", "\n") + "</processor></data>";
			var xmlData = $.parseXML(data);
			var condition = $(xmlData).find("processor").find("condition").text();
			var sql = $(xmlData).find("processor").find("sql").text();
			var type = $(xmlData).find("processor").find("type").text();
			var dbType = $(xmlData).find("processor").find("dbtype").text();
			$("#txtUserCondition").val(condition);
			$("#txtUserSql").val(sql);
			$("input[name='userConditionType'][value=" + type + "]").prop("checked", true);
			$("input[name='databaseType'][value=" + dbType + "]").prop("checked", true);
			if (type == "C") {
				//条件
				$("#conditionDiv").show();
				$("#sqlDiv").hide();
			} else if (type == "S") {
				$("#conditionDiv").hide();
				$("#sqlDiv").show();
			}
		} else {
			$("#txtUserCondition").val("");
			$("#txtUserSql").val("");
			$("input[name='userConditionType'][value=C]").prop("checked", true);
			$("input[name='databaseType'][value=W]").prop("checked", true);
			$("#conditionDiv").show();
			$("#sqlDiv").hide();
		}
		var modal = $("#myModal2").modal({backdrop: 'static', keyboard: false});
		modal.show();
	}
	//修改符号下拉列表
	function editCompare(index) {
		var chrOption = false;
		for (var i = 0; i < $("#selCompare" + index).find("option").length; i++) {
			if ($("#selCompare" + index).get(0).options[i].value == "like") {
				chrOption = true;
				break;
			}
		}
		var field = $("#selFieldId" + index).val();
		if (field.charAt(field.length - 1) == "C") {
			//字符
			if (chrOption) {
				return;
			} else {
				$("#selCompare" + index).append("<option value=\"like\">包含</option>");
				$("#selCompare" + index).append("<option value=\"leftlike\">起始于</option>");
				$("#selCompare" + index).append("<option value=\"rightlike\">结束于</option>");
				return;
			}
		} else {
			//数值
			if (chrOption) {
				$("#selCompare" + index + " option[value='like']").remove();
				$("#selCompare" + index + " option[value='leftlike']").remove();
				$("#selCompare" + index + " option[value='rightlike']").remove();
				return;
			} else {
				return;
			}
		}
	}
	</script>
	<style type="text/css">
		body {
			margin: 0;
			pading: 0;
			line-height: 1.5;
		}
		.node {
			width: 70px;
			text-align: center;
			vertical-align: middle;
			border: 1px solid #fff;
		}
		.mover {
			border: 1px solid #ddd;
			background-color: #ddd;
		}
		.selected {
			background-color: #ddd;
		}
		.state {
			
		}
		#myflow_props table {
			
		}
		#myflow_props th {
			letter-spacing: 2px;
			text-align: left;
			padding: 6px;
			background: #ddd;
		}
		#myflow_props td {
			background: #fff;
			padding: 6px;
		}
		#pointer {
			background-repeat: no-repeat;
			background-position: center;
		}
		#path {
			background-repeat: no-repeat;
			background-position: center;
		}
		#task {
			background-repeat: no-repeat;
			background-position: center;
		}
		#state {
			background-repeat: no-repeat;
			background-position: center;
		}
	</style>
</head>
<body>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>流程设计</a></li>
            <li class="active">流程编辑</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<button class="btn_header" id="btn-flow-props" type="button">流程属性</button>
          	</div>
          	
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<a class="btn_header" id="btn-flow-message" href="#">消息设定</a>
          	</div>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="flowForm" method="post"  action="${rc.contextPath}/flowDesign/save">
	                <div id="myflow_tools" style="position: absolute; top: 10; left: 10; background-color: #fff; width: 80px; cursor: default; padding: 1px;" class="ui-widget-content">
						<div id="myflow_tools_handle" style="text-align: center;" class="ui-widget-header">工具集</div>
						<div class="node" id="myflow_save"><img src="${rc.contextPath}/img/save.gif" />&nbsp;&nbsp;保存</div>
						<div>
							<hr style="margin-top: 2px;margin-bottom: 2px;" />
						</div>
						<div class="node selectable" id="pointer"><img
							src="${rc.contextPath}/img/select16.gif" />&nbsp;&nbsp;选择</div>
						<div class="node selectable" id="path"><img src="${rc.contextPath}/img/16/flow_sequence.png" />&nbsp;&nbsp;转换</div>
						<div>
							<hr style="margin-top: 2px;margin-bottom: 2px;" />
						</div>
						<div class="node state" id="start" type="start"><img src="${rc.contextPath}/img/16/start_event_empty.png" />&nbsp;&nbsp;开始</div>
						<div class="node state" id="task" type="task"><img src="${rc.contextPath}/img/16/task_empty.png" />&nbsp;&nbsp;审批</div>
						<div class="node state" id="condition" type="condition"><img src="${rc.contextPath}/img/16/gateway_parallel.png" />&nbsp;&nbsp;条件</div>
						<div class="node state" id="end" type="end"><img src="${rc.contextPath}/img/16/end_event_terminate.png" />&nbsp;&nbsp;结束</div>
					</div>
					<div id="flowName" style="color:red;text-align:center;width:100%;"><strong>${flowName }</strong></div>
					<div id="myflow_props" style="position: absolute; top: 50; right: 50; background-color: #fff; width: 300px; padding: 3px;" class="ui-widget-content">
						<div id="myflow_props_handle" class="ui-widget-header">属性</div>
						<table border="1" width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td>aaa</td>
							</tr>
							<tr>
								<td>aaa</td>
							</tr>
						</table>
						<div>&nbsp;</div>
					</div>
					
					<div id="myflow"></div>
					<input id="flowData" name="flowData" type="hidden" value="">
                </form>
            </div>
        </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content" style="width:680px;height:350px;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">设定条件</h4>
                    </div>
                    <div class="modal-body" style="height:200px;">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form1">
                            	<div class="fl">
	                            	<select id="selTableId" name="selTableId" class="form-control input-sm fl w15">
										<c:forEach items="${tableList }" var="table">
											<c:if test="${table.tableType eq 'B' }">
											<option value="${table.tableId }">${table.tableName }</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<div class="fl">
	                            	<select id="selFieldId" name="selFieldId" class="form-control input-sm fl w15">
										<c:forEach items="${fieldList }" var="field">
											<option value="${field.fieldId }_${field.fieldType}">${field.fieldName }</option>
										</c:forEach>
									</select>
								</div>
								<div class="fl">
	                            	<select id="selCompare" name="selCompare" class="form-control input-sm fl w15">
	                            		<option value="&gt;">大于</option>
	                            		<option value="&gt;=">大于等于</option>
	                            		<option value="&lt;">小于</option>
	                            		<option value="&lt;=">小于等于</option>
	                            		<option value="=">等于</option>
	                            		<option value="like">包含</option>
	                            		<option value="leftlike">起始于</option>
	                            		<option value="rightlike">结束于</option>
									</select>
								</div>
                                <div class="fl">
	                            	<input type="text" id="txtValue" name="txtValue" class="form-control input-sm fl w15" style="height:34px;">
								</div>
								<div class="fl">
	                            	<select id="selRelation" name="selRelation" class="form-control input-sm fl w15">
	                            		<option value="AND">并且</option>
	                            		<option value="OR">或者</option>
									</select>
								</div>
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-condition" style="height:34px;"><span class="search-btn">添加</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <textarea id="txtCondition" class="form-control input-sm fl w15" style="width:100%;height:110px;"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-condition-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal2" aria-labelledby="myModalLabel2">
            <div class="modal-dialog modal-lg">
                <div class="modal-content" style="width:680px;height:350px;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">设定人员</h4>
                    </div>
                    <div class="modal-body" style="height:200px;">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form1">
                            	<div class="fl">
	                            	<select id="selTableId1" name="selTableId1" class="form-control input-sm fl w15">
										<c:forEach items="${tableList }" var="table">
											<option value="${table.tableId }">${table.tableName }</option>
										</c:forEach>
									</select>
								</div>
								<div class="fl">
	                            	<select id="selFieldId1" name="selFieldId1" class="form-control input-sm fl w15">
										<c:forEach items="${fieldList }" var="field">
											<option value="${field.fieldId }_${field.fieldType}">${field.fieldName }</option>
										</c:forEach>
									</select>
								</div>
								<div class="fl">
	                            	<select id="selCompare1" name="selCompare1" class="form-control input-sm fl w15">
	                            		<option value="&gt;">大于</option>
	                            		<option value="&gt;=">大于等于</option>
	                            		<option value="&lt;">小于</option>
	                            		<option value="&lt;=">小于等于</option>
	                            		<option value="=">等于</option>
	                            		<option value="like">包含</option>
	                            		<option value="leftlike">起始于</option>
	                            		<option value="rightlike">结束于</option>
									</select>
								</div>
                                <div class="fl">
	                            	<input type="text" id="txtValue1" name="txtValue1" class="form-control input-sm fl w15" style="height:34px;">
								</div>
								<div class="fl">
	                            	<select id="selRelation1" name="selRelation1" class="form-control input-sm fl w15">
	                            		<option value="AND">并且</option>
	                            		<option value="OR">或者</option>
									</select>
								</div>
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-user" style="height:34px;"><span class="search-btn">添加</span></button>
                                </div>
                                <div class="fl">
									<input type="radio" name="userConditionType" value="C">条件
									<input type="radio" name="userConditionType" value="S">SQL语句
								</div>
                            </form>
                        </div>
                        <div id="conditionDiv" style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <textarea id="txtUserCondition" class="form-control input-sm fl w15" style="width:100%;height:110px;"></textarea>
                        </div>
                        <div id="sqlDiv" style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden; display:none;">
							<div class="fl" style="width:100%;">
                            	<textarea id="txtUserSql" class="form-control input-sm fl w15" style="width:100%;height:110px;"></textarea>
                            </div>
                            <div class="fl">
								<input type="radio" name="databaseType" value="W">工作流端
								<input type="radio" name="databaseType" value="B">业务端
							</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-user-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 流程属性 --%>
        <div class="modal fade" id="myModal3" aria-labelledby="myModalLabel3">
            <div class="modal-dialog modal-lg">
                <div class="modal-content" style="width:680px;height:350px;">
                    <div class="modal-header">
                        <h4 class="modal-title" id="myModalLabel3">流程属性</h4>
                    </div>
                    <div class="modal-body" style="height:200px;">
                        <div class="row" style="margin-left: 0px;">
                        	<form role="form1">
                        		<table width="100%" style="height:150px;">                        			
                        			<tr>
                        				<td>流程编码:</td>
                        				<td><input type="text" id="flowNo" value="${flowNo }" class="form-control input-sm fl w15" readonly></td>
                        				<td>流程名称:</td>
                        				<td><input type="text" id="txtFlowName" value="${flowName }" class="form-control input-sm fl w15"></td>
                        			</tr>
                        			<tr>
                        				<td>不同意处理:</td>
                        				<td colspan="3" align="left">
                        					<input type="radio" name="disagreeAudit" value="0" <c:if test="${empty flowBean.disagreeAudit || flowBean.disagreeAudit eq '0' }">checked</c:if>>直接返回给提交人
                        					<input type="radio" name="disagreeAudit" value="1" <c:if test="${flowBean.disagreeAudit eq '1' }">checked</c:if>>返回上一位审核人
                        				</td>
                        			</tr>
                        			<tr>
                        				<td>签核模式:</td>
                        				<td colspan="3" align="left">
                        					<select id="auditType" name="auditType">
                        						<option value="0" <c:if test="${empty flowBean.auditType || flowBean.auditType eq '0' }">selected</c:if>>普通签核</option>
                        						<option value="1" <c:if test="${flowBean.auditType eq '1' }">selected</c:if>>手写板签核</option>
                        					</select>
                        				</td>
                        			</tr>
                        		</table>
							</form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-flow-props-confirm">确定</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- SQL处理 --%>
        <div class="modal fade" id="mySqlModal" aria-labelledby="mySqlModalLabel">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                     	<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="mySqlModalLabel">SQL设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                        	<form role="form1">
                        		<table width="100%">
                        			<tr>
                        				<td>目标数据库:</td>
                        				<td>
                        					<select id="targetDatabase" class="form-control2">
                        						<option value="W">工作流</option>
                        						<option value="B">业务</option>
                        					</select>
                        				</td>
                        				<td>处理类型:</td>
                        				<td>
                        					<select id="sqlType" class="form-control2">
                        						<option value="S">SQL文</option>
                        						<option value="P">存储过程</option>
                        					</select>
                        				</td>
                        			</tr>
                        			<tr>
                        				<td>处理时点:</td>
                        				<td>
                        					<select id="processSection" class="form-control2">
                        						<option value="BeforeAudit">审核同意前</option>
                        						<option value="AfterAudit">审核同意后</option>
                        						<option value="BeforeDisagree">审核不同意前</option>
                        						<option value="AfterDisagree">审核不同意后</option>
                        						<option value="BeforeCancelAudit">撤审前</option>
                        						<option value="AfterCancelAudit">撤审后</option>
                        					</select>
                        				</td>
                        				<td></td>
                        				<td></td>
                        			</tr>
                        			<tr>
                        				<td>SQL文/存储过程:</td>
                        				<td colspan="3" align="left">
                        					<textarea id="sqlProc" class="form-control input-sm fl"></textarea>
                        				</td>
                        			</tr>
                        			<tr>
                        				<td></td>
                        				<td></td>
                        				<td></td>
                        				<td>
                        					<input id="btnAddSql" type="button" value="新增" class="btn btn-default">
                        					<input id="sqlIndex" type="hidden">
                        				</td>
                        			</tr>
                        		</table>
							</form>
                        </div>
                        <div id="sqlTable" style="margin-top:20px;max-height: 250px;overflow-y: auto;overflow-x: auto;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>选择</th>
	                                    <th>目标数据库</th>
	                                    <th>处理类型</th>
	                                    <th>处理时点</th>
	                                    <th width="50%">处理SQL</th>
	                                    <th></th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-flow-process-confirm">确定</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 流程属性 --%>
        <div class="modal fade" id="myMessageModal" aria-labelledby="myMessageModalLabel">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myMessageModalLabel">消息模板</h4>
                    </div>
                    <div class="modal-body" style="height:400px;">
                        <div class="row" style="margin-left: 0px;">
                        	<form role="form1">
                        		<table width="90%" style="height:150px;">                     			
                        			<tr>
                        				<td>单据状态:</td>
                        				<td>
                        					<select id="VoucherStatus" class="form-control input-sm fl w15">
                        						<option value="Audited" selected>已审批的单据</option>
                        						<option value="Disagree">被退回的单据</option>
                        						<option value="Submited">提交的单据</option>
                        					</select>
                        				</td>
                        				<td>处理人:</td>
                        				<td>
	                        				<select id="UserType" class="form-control input-sm fl w15">
	                        					<option value="Cur">当前处理人</option>
	                        					<option value="Pre">已处理人</option>
	                        				</select>
	                        			</td>
                        			</tr>
                        			<tr>
                        				<td>消息模板:</td>
                        				<td colspan="3" align="left">
                        					<textarea id="MessageTemplate" class="form-control"></textarea>
                        				</td>
                        			</tr>
                        			<tr>
                        				<td>模板类型</td>
                        				<td>
                        					<select id="MessageType" class="form-control input-sm fl w15">
                        						<option value="Simple">简单模板</option>
                        						<option value="Script">脚本函数</option>
                        					</select>
                        				</td>
                        				<td>
                        					<input type="button" value="新增" id="AddMessageTemplate">
                        					<input type="hidden" id="RowNo">
                        				</td>
                        			</tr>
                        		</table>
							</form>
                        </div>
                        <div id="messageTable" style="margin-top:20px;max-height: 200px;overflow-y: auto;overflow-x: auto;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>选择</th>
	                                    <th>单据类型</th>
	                                    <th>处理人</th>
	                                    <th>模板类型</th>
	                                    <th>消息模板</th>
	                                    <th></th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-flow-message-confirm">确定</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>