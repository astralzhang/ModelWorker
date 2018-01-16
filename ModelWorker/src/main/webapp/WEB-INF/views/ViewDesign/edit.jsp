<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<html>
<head>
	<meta name="menuId" content="viewDesignList"/>
    <title></title>
    <link type="text/css" href="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet" />
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mytable.edit.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mytable.editor.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mytable.props.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mytable.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mydetail.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mylist.js"></script>
    <script type="text/javascript">
    function refresh() {
		$.ajax({
			url:"${rc.contextPath}/viewDesign/refresh",
			type:"post",
			dataType:"json",
			data:{},
			success:function(data){
				
			},
			error:function(XMLHttpRequest, data) {
				//MessageBox.processEnd("系统错误，详细信息：" + data);
			}
		});	
		setTimeout("refresh()", 10000);
	}
	$(document).ready(function() {
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		refresh();
		$("#btn-back").click(function() {
			location.href = "${rc.contextPath}/viewDesign/list?viewNo=${viewBean.no}";
		});
		var myTable = $("#headerTable").mytable(${viewBean.headScript});
		var myDetail = $("#detail").mydetail(${viewBean.detailScript});
		var myList = $("#myListModal").mylist(${viewBean.listScript});
		<%-- 保存 --%>
		$("#btn-save").click(function() {
			var headConfig = myTable.config;
			if (headConfig.table == undefined || headConfig.table == null || headConfig.table == "") {
				MessageBox.info("请输入表头数据表！");
				return;
			}
			if (myList.config.sql == undefined || myList.config.sql == null || myList.config.sql == "") {
				MessageBox.info("请设定列表信息！");
				return;
			}
			var nullDetail = true;
			$.each(myDetail.config, function(n, value) {
				if (value.table != undefined && value.table != null && value.table != "") {
					nullDetail = false;
					return false;
				}
			});
			if (nullDetail == true) {
				myDetail.config = [];
			}
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/viewDesign/save",
				  type:"post",
				  dataType:"json",
				  data:{
					  no : "${viewBean.no}",
					  name:"${viewBean.name}",
					  voucherType:"${viewBean.voucherType}",
					  headScript:JSON.stringify(headConfig),
					  detailScript:JSON.stringify(myDetail.config),
					  listScript:JSON.stringify(myList.config)
				  },
				  success:function(data){
					  if (data.errType == "1") {
						  MessageBox.processEnd("系统错误:" + data.errMessage);
						  return;
					  }
					  window.location.href = "${rc.contextPath}/viewDesign/list";
					  MessageBox.processClose();
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
	});
	//整数Check（必须大于0）
	function isInteger(data) {
		if (data == null || data == "") {
			return -1;
		}
		var ex = /^\d+$/;
		if (!ex.test(data)) {
			return -2;
		}
		if (data <= 0) {
			return -2;
		}
		return 0;
	}
    /**
    * 时间格式check
    */
	function hourMinuteCheck(){
    	var reg=/^([01]\d|2[01234]):([0-5]\d|60)$/;
		if($("#overTimeStart").val() != ""){
			var startTime = formatTime($("#overTimeStart").val());
			if(!reg.test(startTime)){
			  alert("请输入正确开始时间！");
			  return;
			 }
			$("#overTimeStart").val(startTime);
		}
		if($("#overTimeEnd").val() != ""){
			var endTime = formatTime($("#overTimeEnd").val());
			if(!reg.test(endTime)){
			  alert("请输入正确结束时间！");
			  return;
			 }
			$("#overTimeEnd").val(endTime);
		}
	}
	/**
	* 格式化时间格式
	*/
	function formatTime(time) {
		if (time == null || time == "") {
			return "";
		}
		time = time.replaceAll("：", ":");
		var arrTime = time.split(":");
		if (arrTime instanceof Array) {
			if (arrTime.length != 2) {
				return time;
			}
			var hours = arrTime[0];
			var minutes = arrTime[1];
			hours = ("0" + hours).substr(("0" + hours).length - 2);
			minutes = ("0" + minutes).substr(("0" + minutes).length - 2);
			return hours + ":" + minutes;
		}
		return time;
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
	<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="bb"/>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <ol class="breadcrumb">
                <li><a href="${rc.contextPath}/overTimeManage/init"><i class="fa fa-folder"></i>${viewBean.voucherTypeName }</a></li>
                <li class="active">${viewBean.name }</li>
            </ol>
            <div class="btn_box">
          	<div class="search">
          		<button class="btn_header" id="btn-save" type="button">保存</button>
          	</div>
          	<div class="line_left"></div>
          	<div class="search">
          		<button class="btn_header" id="btn-list-sql" type="button">列表SQL设定</button>
          		<button class="btn_header" id="btn-list-view" type="button">列表显示</button>
          		<button class="btn_header" id="btn-list-condition" type="button">列表条件</button>
          	</div>
          	<div class="line_left"></div>
          	<div class="search">
          		<button class="btn_header" id="btn-header" type="button">表头</button>
          		<button class="btn_header" id="btn-add-row" type="button">新增行</button>
          		<button class="btn_header" id="btn-add-col" type="button">新增列</button>
          		<button class="btn_header" id="btn-margin" type="button">合并单元格</button>
          		<button class="btn_header" id="btn-margin-cancel" type="button">取消合并</button>          			
          	</div>
          	<div class="line_left"></div>
          	<div class="search">
          		<button class="btn_header" id="btn_detail" type="button">表体</button>
          	</div>
          	<div class="line_left"></div>
          	<div class="search">
          		<button class="btn_header" id="btn_default_set" type="button">默认值</button>
          		<button class="btn_header" id="btn_save_proc" type="button">保存处理</button>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<button class="btn_header" id="btn-back" type="button">返回</button>
          	</div>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <!-- projectAdd -->
            <div id="header" class="box box-info product-info">
                <div class="box-header">
                    <h2 class="box-title">${voucherName }</h2>
                </div>
                <div class="box-body">
                	<form id="form1" enctype="multipart/form-data" method="post">
                    <table id="headerTable" border="1" width="100%">
                    </table>
                    </form>
                </div>
            </div><!-- /.overTime-info -->
            <br>
            <div id="detail" class="box box-warning cost-info">
                <div class="box-header">
                    <h2 class="box-title">${voucherName }</h2>
                </div>
                <%--
                <ul class="tab">
				      <li>最新</li>
				      <li class="cur">热门</li>
				      <li>新闻</li>
				</ul>
				<div class="tabContent">
					<table class="table table-hover" >
						<tr>
							<td>表体1</td>
						</tr>
                    </table>
				</div>
				<div class="tabContent">
					<table class="table table-hover" >
						<tr>
							<td>表体2</td>
						</tr>
                    </table>
				</div>
				<div class="tabContent">
					<table class="table table-hover" >
						<tr>
							<td>表体3</td>
						</tr>
                    </table>
				</div>
				--%>
            </div><!-- /.cost-info -->
            <div id="mytable_props" style="position: absolute; top: 110px; right: 50px; background-color: #fff; width: 300px; padding: 3px;z-index:99;display:none;" class="ui-widget-header">
				<div id="mytable_props_handle" class="ui-widget-header">属性</div>
				<table border="1" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td>aaa1</td>
					</tr>
					<tr>
						<td>aaa</td>
					</tr>
				</table>
				<div>&nbsp;</div>
			</div>
		</div>
        </section><!-- /.content -->
		<div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">表头设计</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/voucherDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>项目类型：</td>
		                                	<td>
		                                		<select class="form-control input-sm fl w15" id="fieldType" name="fieldType">													
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
                        <button type="button" class="btn btn-default" id="btn-header-design">确定</button>
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
                        <h4 class="modal-title" id="myModalLabel2">事件设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
								<input type="text" id="eventNo" placeholder="事件名称" class="form-control input-sm fl w1">&nbsp;
								<br />
                                <textarea id="eventFunc" placeholder="事件函数" class="form-control input-sm fl w1"></textarea>
                                <input type="hidden" id="rowIndex"/>
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-event">新增↓</button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>选择</th>
	                                    <th>事件</th>
	                                    <th>事件函数</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-event-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal3" aria-labelledby="myModalLabel3">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel3">单数明细设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                                <input type="text" id="search_deptNo" placeholder="部门编号" class="form-control input-sm fl w15">&nbsp;
                                <input type="text" id="search_deptName" placeholder="部门名称" class="form-control input-sm fl w15">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-dept-search"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
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
                        <button type="button" class="btn btn-default" id="btn-dept-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal4" aria-labelledby="myModalLabel4">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">单元格编辑</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/voucherDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>类型：</td>
		                                	<td>
		                                		<select id="editType" name="editType" class="form-control input-sm fl w15" style="width:30%;">
		                                			<option value="text">单行文本框</option>
		                                			<option value="textArea">多行文本框</option>
		                                			<option value="textDate">日期</option>
		                                			<option value="dataDropdown">下拉框</option>
		                                			<option vlaue="dataSelect">选择框</option>
												</select>
												<input id="hasLabel" type="checkbox" value="Y">带标签
												<input id="isReadonly" type="checkbox" value="Y">只读
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>标签：</td>
		                                	<td><input type="text" id="dataLabel" name="dataLabel" placeholder="显示标签：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>默认值：</td>
		                                	<td><input type="text" id="defaultValue" name="defaultValue" placeholder="默认值：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>数据字段：</td>
		                                	<td>
		                                		<select id="tableField" name="tableField" class="form-control input-sm fl w15" style="width:30%;">
		                                		</select>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>check函数：</td>
		                                	<td><textarea type="text" id="checkFunction" name="checkFunction" placeholder="数据提交时的Check函数：" class="form-control input-sm fl w5"></textarea></td>
		                                </tr>
		                                <tr>
		                                	<td>数据取得SQL：</td>
		                                	<td><textarea id="dataSql" name="dataSql" placeholder="数据取得SQL" class="form-control input-sm fl w5"></textarea></td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-header-design">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal5" aria-labelledby="myModalLabel5">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel5">合并单元格</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/voucherDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td colspan="2">开始</td>
		                                	<td colspan="2">结束</td>
		                                </tr>
		                                <tr>
		                                	<td>行：</td>
		                                	<td><input type="text" id="startRowNum" name="startRowNum" placeholder="行号：" class="form-control input-sm fl w15"></td>
		                                	<td>行：</td>
		                                	<td><input type="text" id="endRowNum" name="endRowNum" placeholder="行号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>列：</td>
		                                	<td><input type="text" id="startColNum" name="startColNum" placeholder="列号：" class="form-control input-sm fl w15"></td>
		                                	<td>列：</td>
		                                	<td><input type="text" id="endColNum" name="endColNum" placeholder="列号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-margin-ok">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal6" aria-labelledby="myModalLabel6">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel6">取消合并单元格</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/voucherDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>行：</td>
		                                	<td><input type="text" id="CancelRowNum" name="CancelRowNum" placeholder="行号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>列：</td>
		                                	<td><input type="text" id="CancelColNum" name="CancelColNum" placeholder="列号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-cancel-margin-ok">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myTableModal" aria-labelledby="myTableModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myTableModalTitle">自定义表格设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                                <input type="text" id="search_deptNo" placeholder="部门编号" class="form-control input-sm fl w15">&nbsp;
                                <input type="text" id="search_deptName" placeholder="部门名称" class="form-control input-sm fl w15">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-dept-search"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 200px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
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
                        <button type="button" class="btn btn-default" id="btn-myTable-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myDetailModal" aria-labelledby="myDetailModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myDetailModalTitle">自定义表格设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                                <input type="text" id="search_deptNo" placeholder="部门编号" class="form-control input-sm fl w15">&nbsp;
                                <input type="text" id="search_deptName" placeholder="部门名称" class="form-control input-sm fl w15">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-dept-search"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 200px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
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
                        <button type="button" class="btn btn-default" id="btn-myDetail-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myListModal" aria-labelledby="myListModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myListModalTitle">列表设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                            	<textarea id="listSql" class="form-control input-sm fl w5"></textarea>
                            	<input type="checkbox" id="listType">是否存储过程
                            	<hr />
                                <input type="text" id="listFieldName" placeholder="字段名称" class="form-control input-sm fl w15">&nbsp;
                                <input type="text" id="listFieldNo" placeholder="字段编码" class="form-control input-sm fl w15">
                                <select id="listEditor">
                                	<option value="inputEditor">单行文本框</option>
									<option value="mutilInputEditor">多行文本框</option>
									<option value="dateEditor">日期</option>
									<option value="dataDropdown">下拉框</option>
									<option vlaue="dataSelect">选择框</option>
                                </select>
                                <input type="text" id="listEditor" placeholder="条件编辑" class="form-control input-sm fl w15">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-dept-search"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 200px;overflow-y: auto;overflow-x: auto;">
                            <table id="MyListTableData" class="table table-bordered table-condensed">
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
                        <button type="button" class="btn btn-default" id="btn-myList-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>