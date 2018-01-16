<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<jsp:useBean id="now" class="java.util.Date" />
<html>
<head>
	<meta name="menuId" content="openWindowList"/>
	<script type="text/javascript" src="${rc.contextPath}/js/mytable/myopenwin.js"></script>
    <title></title>
    <script type="text/javascript">    
	$(document).ready(function() {
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		var myopenwin = $("#form1").myopenwin(${data.condition},${data.viewField});
		$("#btn_save").click(function () {
			//alert(myopenwin.get_condition());
			var data = new Object;
			data.no = $("#no").val();
			data.name = $("#name").val();
			data.sql = $("#sql").val();
			data.sqlProcFlag = $("#sqlProcFlag").val();
			data.targetDatabase = $("#targetDatabase").val();
			data.condition = myopenwin.get_condition();
			data.viewField = myopenwin.get_view();
			alert(JSON.stringify(data));
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/openWinDesign/save",
				type:"post",
				dataType:"json",
				data:JSON.stringify(data),
				contentType: "application/json",
				success:function(data){
					if (data.errType == "1") {
						MessageBox.processEnd("系统错误:" + data.errMessage);
						return;
					}
					MessageBox.processEnd("保存成功！", function() {
						window.location.href = "${rc.contextPath}/openWinDesign/list";	
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
				}
			});
		});
		$("#form1").validate({
			rules: {
				deptName:"required",
				overTimeDate:"required",
				overTimeStart:"required",
				overTimeEnd:"required",
				overTimeHours:"required",
        		overTimeReason:{
					maxlength:200
				},
				remark:{
					maxlength:200
				}
			}
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
                <li><a href="${rc.contextPath}/openWinDesign/init"><i class="fa fa-folder"></i>画面设计</a></li>
                <li class="active">弹窗设计</li>
            </ol>
            <div class="btn_box">
          	<div class="search">
          		<hq:auth act="openWinSave">
          			<button class="btn_header" id="btn_save" type="button">保存</button>
          		</hq:auth>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<a class="btn_header" id="btn-condition" href="#">查询条件</a>
          		<a class="btn_header" id="btn-list" href="#">显示字段</a>
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
                    <h2 class="box-title">弹窗信息</h2>
                </div>
                <div class="box-body">
                	<form id="form1" enctype="multipart/form-data" method="post">
                    <table class="table table-bordered">
                        <tr>
                            <td>弹窗编号</td>
                            <td><input id="no" type="text" name="no" maxlength="100" value="${data.no}" class="form-control2"></td>
                            <td>弹窗名称</td>
                            <td><input type="text" id="name" name="name"  value="${data.name}" class="form-control2" placeholder="弹窗名称"></td>
                        </tr>
                        <tr>
                            <td>目标数据库</td>
                            <td>
                            	<select id="targetDatabase" name="targetDatabase"  class="form-control2" required>
                            		<option value="L" <c:if test="${data.targetDatabase eq 'L' }">selected</c:if>>本地数据库</option>
                            		<option value="R" <c:if test="${data.targetDatabase eq 'R' }">selected</c:if>>远程数据库</option>
                            	</select>
                            </td>
                            <td>SQL类型</td>
                            <td>
                            	<select id="sqlProcFlag" name="sqlProcFlag"  class="form-control2" required>
                            		<option value="S" <c:if test="${data.sqlProcFlag eq 'S' }">selected</c:if>>SQL文</option>
                            		<option value="P" <c:if test="${data.sqlProcFlag eq 'P' }">selected</c:if>>存储过程</option>
                            	</select>
                            </td>   
                        </tr>
						<tr>
                            <td>SQL文/存储过程</td>
                            <td colspan="3">
                            <textarea name="sql" id="sql" class="form-control input-sm fl">${data.sql}</textarea>
                            </td>                            
                        </tr>
                    </table>
                    </form>
                </div>
            </div><!-- /.overTime-info -->
            <br>
            <div class="box box-warning cost-info">
                <div class="box-header">
                    <h2 class="box-title">查询条件</h2>
                </div>
                <div class="box-body" style=" overflow:scroll; ">
                    <table class="table table-bordered" id="condition">
                    	<thead>
	                        <tr>
	                            <th></th>
	                            <th>字段Id</th>
	                            <th>显示名称</th>
	                            <th>编辑框类型</th>
	                            <th>比较方法</th>
	                            <th>目标数据库</th>
	                            <th>SQL/Proc</th>
	                            <th>SQL</th>
	                            <th>操作</th>
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
                    <h2 class="box-title">数据列表</h2>
                </div>
                <div class="box-body" style=" overflow:scroll; ">
                    <table class="table table-bordered" id="viewField">
                    	<thead>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div><!-- /.cost-info -->
        </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myConditionModal" aria-labelledby="myConditionModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myConditionModalLabel">条件设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>字段ID</td>
		                                	<td><input id="fieldId" name="fieldId" class="form-control input-sm fl w15" ></td>
		                                	<td>显示名</td>
		                                	<td><input id="viewName" name="viewName" class="form-control input-sm fl w15" ></td>
		                                </tr>
		                                <tr>
		                                	<td>编辑框</td>
		                                	<td>
		                                		<select id="editorType" name="editorType" class="form-control input-sm fl w15" >
		                                			<option value="inputEditor">文本框</option>
		                                			<option value="dateEditor">日期</option>
		                                			<option value="selectEditor">下拉框</option>
		                                			<option value="checkEditor">CheckBox</option>
		                                		</select>
		                                	</td>
		                                	<td>比较方法</td>
		                                	<td>
		                                		<select id="compareTo" name="compareTo" class="form-control input-sm fl w15" >
		                                			<option value="eq">=</option>
		                                			<option value="neq">&gt;&lt;</option>
		                                			<option value="gt">&gt;</option>
		                                			<option value="get">&gt;=</option>
		                                			<option value="lt">&lt;</option>
		                                			<option value="let">&lt;=</option>
		                                			<option value="like">包含</option>
		                                			<option value="leftlike">左包含</option>
		                                			<option value="rightlike">右包含</option>
		                                		</select>
		                                	</td>
		                                </tr>
		                                 <tr>
		                                	<td>目标数据库</td>
		                                	<td>
		                                		<select id="condition_TargetDatabase" name="condition_TargetDatabase" class="form-control input-sm fl w15" disabled>
		                                			<option value="L">本地数据库</option>
		                                			<option value="R">远程数据库</option>
		                                		</select>
		                                	</td>
		                                	<td>SQL/存储过程</td>
		                                	<td>
		                                		<select id="condition_SqlProcFlag" name="condition_SqlProcFlag" class="form-control input-sm fl w15" disabled>
		                                			<option value="S">SQL文</option>
		                                			<option value="P">存储过程</option>
		                                		</select>
		                                	</td>
		                                </tr>
		                                <tr>
		                                	<td>目标数据库</td>
		                                	<td colspan="3"><textarea id="condition_sql" name="condition_sql" class="form-control input-sm fl w5" disabled></textarea></td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-condition-confirm">确定</button>
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
                        <button type="button" class="btn btn-default" id="btn-myList-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>