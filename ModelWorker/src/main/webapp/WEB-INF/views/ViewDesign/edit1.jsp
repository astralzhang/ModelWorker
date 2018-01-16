<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="flowList"/>
	<link type="text/css" href="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet" />
	<link type="text/css" href="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.css" rel="stylesheet" />
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui-1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mytable.edit.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/mytable/mytable.js"></script>
    <script type="text/javascript">
	$(document).ready(function() {
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		$("#headerTable").mytable(({rows:4,cols:4,type:'S',margin:[{start:{row:1,col:1},end:{row:2,col:2}}]}));
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
          	<%-- 
          	<div class="line_left"></div>
          	<div class="btn-group">
          		
          	</div>
          	--%>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
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
					<div id="myflow_props" style="position: absolute; top: 50; right: 50; background-color: #fff; width: 300px; padding: 3px;">
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
    </div><!-- /.content-wrapper -->
</body>
</html>