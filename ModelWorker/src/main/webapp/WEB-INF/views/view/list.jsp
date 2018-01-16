<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="${data.item.itemNo }"/>
	<script type="text/javascript" src="${rc.contextPath}/js/view/mylist.js"></script>
	<script type="text/javascript">	
	$(function(){
		var listData = null;
		var conditionData = null;
		if ("${data.runTime}" != "0") {
			listData = [];
			conditionData = {};
		} else {
			listData = eval('(${data.data})');
			conditionData = eval('(${data.condition})');
		}
		var mylist = $("#searchForm").mylist(${data.viewBean.listScript}, "${rc.contextPath}", "${data.viewBean.no}",listData, conditionData);
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
	});

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
</head>
<body> 

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>${data.module.name }</a></li>
            <li class="active">${data.item.itemName }</li>
          </ol>

        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:hidden;overflow-x:hidden;">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/proxyManager/list">
                	
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                    	<div class="btn_box">
			          	<div class="search">
			          	<hq:auth act="Add" view="${data.viewBean.no}">
			      			<button class="btn_header" id="btn_add" type="button">新增</button>
			      		</hq:auth>
			      		<hq:auth act="Edit" view="${data.viewBean.no }">
			      			<button class="btn_header" id="btn_edit" type="button">修改</button>
			      		</hq:auth>
			      		<hq:auth act="Delete" view="${data.viewBean.no }">
			      			<button class="btn_header" id="btn_delete" type="button">删除</button>
			      		</hq:auth>
			      		<hq:auth act="View" view="${data.viewBean.no }">
			      			<button class="btn_header" id="btn_view" type="button">查看</button>
			      		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
			          	<div class="btn-group">
			       		<hq:auth act="" view="${data.viewBean.no }">
			       			<a class="btn_header" id="btn-search" href="#">搜索</a>
			       		</hq:auth>
			          	</div>
			          </div>
<!--                         <div class="box-header"> -->
<%--                             <h3 class="box-title">${data.item.itemName }</h3> --%>
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding" style="overflow:hidden;">
	                        <div id="table_scroll" style="overflow:auto;">
	                            <table class="table table-hover" id="dataTable">
	                            	<thead>
						            	<tr>
						                   <th></th>
						                   <th>单据类型</th>
				                           <th>开始日期</th>
				                           <th>结束日期</th>
				                           <th>代理人</th>
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
        </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myOpenWinModal" aria-labelledby="myOpenWinModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myOpenWinModalLabel">弹窗</h4>
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
                        <button type="button" class="btn btn-default" id="btn-myOpenWin-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">新增流程</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/flowDesign/add" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>流程编号：</td>
		                                	<td><input type="text" id="flowNo" name="flowNo" placeholder="流程编号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>流程名称：</td>
		                                	<td><input type="text" id="flowName" name="flowName" placeholder="流程名称：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>单据类型：</td>
		                                	<td>
												<select id="voucherType" name="voucherType" class="form-control input-sm fl w15" style="width:28%;">
													<c:forEach items="${voucherTypeList }" var="voucherType">
														<option value="${voucherType.no }">${voucherType.name }</option>
													</c:forEach>
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
                        <button type="button" class="btn btn-default" id="btn-flow">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>