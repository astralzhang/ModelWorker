<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<html>
<head>
	<meta name="menuId" content="SyncDesignList"/>
	<script type="text/javascript" src="${rc.contextPath}/js/mytable/mysync.js"></script>
    <title></title>
    <script type="text/javascript">    
	$(document).ready(function() {
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		var mysync = $("#form1").mysync(${data.script});
		$("#btn_save").click(function () {
			if ($("#no").val() == "") {
				MessageBox.info("请输入导入编号！");
				return;
			}
			if ($("#name").val() == "") {
				MessageBox.info("请输入导入名称！");
				return;
			}
			var data = new Object;
			data.no = $("#no").val();
			data.name = $("#name").val();
			data.model = $("#syncModel").val();
			data.script = JSON.stringify(mysync.config);
			alert(JSON.stringify(data));
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/SyncDesign/save",
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
						window.location.href = "${rc.contextPath}/SyncDesign/list";	
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
				}
			});
		});
		$("#btn-back").click(function() {
			location.href = "${rc.contextPath}/SyncDesign/list";
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
                <li><a href="${rc.contextPath}/SyncDesign/list"><i class="fa fa-folder"></i>画面设计</a></li>
                <li class="active">同步设定</li>
            </ol>
            <div class="btn_box">
          	<div class="search">
       			<button class="btn_header" id="btn_save" type="button">保存</button>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<a class="btn_header" id="btn-source" href="#">源表设定</a>
          		<a class="btn_header" id="btn-table" href="#">导入数据表</a>
          		<a class="btn_header" id="btn-mapping" href="#">字段映射</a>
          		<a class="btn_header" id="btn-proc" href="#">存储过程</a>
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
                    <h2 class="box-title">同步设定</h2>
                </div>
                <div class="box-body">
                	<form id="form1" enctype="multipart/form-data" method="post">
                    <table class="table table-bordered">
                        <tr>
                            <td>同步设定编号</td>
                            <td><input id="no" type="text" name="no" maxlength="100" value="${data.no}" class="form-control input-sm fl w15" placeholder="同步设定编号"></td>
                            <td>同步设定名称</td>
                            <td><input type="text" id="name" name="name"  value="${data.name}" class="form-control input-sm fl w15" placeholder="同步设定名称"></td>
                            <td>同步模式</td>
                            <td>
                            	<select id="syncModel" name="syncModel"  class="form-control2" required>
                            		<option value="add" <c:if test="${data.model eq 'add' }">selected</c:if>>新增</option>
                            		<option value="replace" <c:if test="${data.model eq 'replace' }">selected</c:if>>覆盖</option>
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
                    <h2 class="box-title">数据表</h2>
                </div>
                <div id="dbTable" class="box-body" style=" overflow:scroll; ">
                    <table class="table table-bordered" id="condition">
                    	<thead>
	                        <tr>
	                            <th></th>
	                            <th>目标数据库</th>
	                            <th>数据表</th>
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
                    <h2 class="box-title">字段映射</h2>
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
        <div class="modal fade" id="mySyncModal" aria-labelledby="mySyncModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="mySyncModalLabel">条件设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="mySyncModalForm" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                        <div id="mySyncDetail" style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: auto;">
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
                        <button type="button" class="btn btn-default" id="btn-import-confirm">确定</button>
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