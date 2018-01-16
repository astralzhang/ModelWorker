<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<html>
<head>
	<meta name="menuId" content="ReportDesignList"/>
	<script type="text/javascript" src="${rc.contextPath}/js/mytable/myreport.js"></script>
    <title></title>
    <script type="text/javascript">    
	$(document).ready(function() {
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		var myreport = $("#form1").myreport(${data.report.viewScript},"${rc.contextPath}", "${reportNo}");
		$("#btn_save").click(function () {
			var data = new Object;
			data.id = "${data.report.id}";
			data.no = $("#no").val();
			data.name = $("#name").val();
			data.moduleNo = $("#moduleNo").val();
			data.viewScript = JSON.stringify(myreport.config);
			//alert(JSON.stringify(data));
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/ReportDesign/save",
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(data),
				success:function(data){
					if (data.errType == "1") {
						MessageBox.processEnd("系统错误:" + data.errMessage);
						return;
					}
					MessageBox.processEnd("保存成功！", function() {
						window.location.href = "${rc.contextPath}/ReportDesign/list";	
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
				}
			});
		});
		$("#btn-back").click(function() {
			location.href = "${rc.contextPath}/ReportDesign/list";
		});
	});	
</script>
</head>
<body>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <ol class="breadcrumb">
                <li><a href="${rc.contextPath}/ReportDesign/list"><i class="fa fa-folder"></i>画面设计</a></li>
                <li class="active">报表设计</li>
            </ol>
            <div class="btn_box">
          	<div class="search">
       			<button class="btn_header" id="btn_save" type="button">保存</button>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn_group">
          		<a class="btn_header" id="btn-source" href="#">数据源设定</a>
          		<a class="btn_header" id="btn-condition" href="#">查询条件</a>
          		<a class="btn_header" id="btn-show-field" href="#">画面项目</a>
          		<a class="btn_header" id="btn-link-field" href="#">链接项目</a>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<a class="btn_header" id="btn-upload" href="#">上传模板</a>
          		<a class="btn_header" id="btn-sheet" href="#">Sheet设定</a>
          		<a class="btn_header" id="btn-fix-mapping" href="#">固定字段映射</a>
          		<a class="btn_header" id="btn-detail-mapping" href="#">明细字段映射</a>
	          	<button class="btn_header" id="btn-back" type="button">返回</button>
          	</div>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <!-- projectAdd -->
            <div class="box box-info product-info">
                <div class="box-header">
                    <h2 class="box-title">报表设定</h2>
                </div>
                <div class="box-body">
                	<form id="form1" enctype="multipart/form-data" method="post">
                    <table class="table table-bordered">
                        <tr>
                            <td>报表编号</td>
                            <td><input id="no" type="text" name="no" maxlength="100" value="${reportNo}" class="form-control input-sm fl w15" placeholder="报表编号" readonly></td>
                            <td>报表名称</td>
                            <td><input type="text" id="name" name="name"  value="${reportName}" class="form-control input-sm fl w15" placeholder="报表名称" readonly></td>
                            <td>所属模块</td>
                            <td>
                            	<select id="moduleNo" name="moduleNo" class="form-control input-sm fl w15" disabled>
									<c:forEach items="${data.module }" var="moduleBean">
										<option value="${moduleBean.no }" <c:if test="${moduleBean.no eq moduleNo }">selected</c:if>>${moduleBean.name }</option>
									</c:forEach>
								</select>
                            </td>
                        </tr>
                    </table>
                    </form>
                </div>
            </div><!-- /.overTime-info -->
            <br>
            <div class="box box-warning cost-info">
                <div class="box-header">
                    <h2 class="box-title">Sheet页</h2>
                </div>
                <div id="sheet" class="box-body" style=" overflow:scroll; ">
                    <table class="table table-bordered" id="condition">
                    	<thead>
	                        <tr>
	                            <th></th>
	                            <th>Sheet序号</th>
	                            <th>是否自动创建Sheet</th>
	                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div><!-- /.cost-info -->
            <br>
            <div class="box box-warning cost-info">
                <div class="box-header">
                    <h2 class="box-title">数据映射</h2>
                </div>
                <div class="box-body" style=" overflow:scroll; ">
                    <table id="dbMapping" class="table table-bordered" id="viewField">
                    	<thead>
                    		<tr>
                    			<th>数据表字段</th>
                    			<th>数据来源</th>
                    			<th>参数设定</th>
                    			<th>SQL/存储过程<br>脚本函数/数据库函数</th>
                    			<th>SQL/存储过程<br>目标数据库</th>
                    			<th>映射字段</th>
                    			<th>是否数据重复check字段</th>
                    		</tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div><!-- /.cost-info -->
        </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myReportModal" aria-labelledby="myReportModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myReportModalLabel">条件设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="myReportModalForm" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                        <div id="myReportDetail" style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: auto;">
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
                        <button type="button" class="btn btn-default" id="btn-report-confirm">确定</button>
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
                            	
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: auto;">
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
                        <button type="button" class="btn btn-default" id="btn-myList-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>