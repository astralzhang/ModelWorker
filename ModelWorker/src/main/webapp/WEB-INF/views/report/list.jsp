<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="${data.report.no }Report"/>
	<script type="text/javascript" src="${rc.contextPath}/js/view/myreportlinker.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/js/view/myreport.js"></script>
	<script src="${rc.contextPath }/js/view/myfixedtable.js"></script>
	<script type="text/javascript">	
	$(function(){
		var fixRow = 0;
		var fixCol = 0;
		if (parseInt("${data.fixRow}") > 0) {
			fixRow = parseInt("${data.fixRow}");
		}
		if (parseInt("${data.fixCol}") > 0) {
			fixCol = parseInt("${data.fixCol}");
		}
		if (fixRow > 0 || fixCol > 0) {
			$("#table_scroll").myfixedtable({row:fixRow,col:fixCol});
		}
		var myreport = $("#searchForm").myreport(${data.report.viewScript}, "${rc.contextPath}", "${data.report.no}",eval('(${data.conditionData})'), eval('(${data.listData})'));
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		/*var download = function(id) {
			var param = Object;
			param.id = id;
			param.
			$.ajax({
				url:"${rc.contextPath}/report/download/${data.report.no}ReportDownload",
				type:"post",
				dataType:"json",
				data:{
					id:id
				},
				success:function(data) {
					
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		};
		//下载按钮
		$("#btn_download").click(function() {
			if ($("#myTemplateModal table tbody tr").length > 0) {
				var modal = $('#myTemplateModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			} else {
				myreport.download("");
			}
		});
		$("#btn-template-download").click(function() {
			var ids = [];
			$("#myTemplateModal table tbody tr input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
				}
			});
			if (ids.length <= 0) {
				MessageBox.confirm("没有选择下载模板，是否下载无模板文件！", function() {
					myreport.download("");
				});
			} else if (ids.length > 1) {
				MessageBox.info("请选择一份模板下载！");
				return;
			} else {
				myreport.download(ids[0]);
			}
		});*/
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
            <li><a href="#"><i class="fa fa-dashboard"></i>${data.module.name }</a></li>
            <li class="active">${data.report.name }</li>
          </ol>
 
        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow:hidden;">
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
				          	<hq:auth act="ReportDownload" view="${data.report.no }">
				      			<button class="btn_header" id="btn_download" type="button">下载</button>
				      		</hq:auth>
				          	</div>
				          	<div class="line_left"></div>
				          	<div class="btn-group">
				       		<hq:auth act="ReportSearch" view="${data.report.no }">
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
        <%-- 模板一栏 --%>
        <div class="modal fade" id="myTemplateModal" aria-labelledby="myTemplateModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myTemplateModalLabel">下载模板</h4>
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
	                                    <th>模板文件</th>
	                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${data.template }" var="template">
                                	<tr>
                                		<td><input type="checkbox" value="${template.id }"></td>
                                		<td>${template.name }</td>
                                	</tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                    	<button type="button" class="btn btn-default" id="btn-template-download">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>