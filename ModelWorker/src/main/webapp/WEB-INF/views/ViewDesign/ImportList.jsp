<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="importDesignList"/>
	<script type="text/javascript">	
	$(function(){
		
		$("#btn-search").click(function() {
			$("#searchForm").submit();
		});
		
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		$("#btn_add").on('click', function() {
			location.href = "${rc.contextPath}/ImportDesign/add";
		});
		<%-- 修改导入设定 --%>
		$("#btn_edit").click(function(){
			var ids = [];
			var statuses = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要修改的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据进行修改。");
				return;
			} else {
				//编辑自定义画面
				//alert(ids[0]);
				window.location.href = "${rc.contextPath}/ImportDesign/edit?no=" + ids[0];
			}
		});
		<%-- 导入设定发布按钮  --%>
		$("#btn_publish").click(function() {
			var ids = [];
			var statuses = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要发布的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条进行发布。");
				return;
			}
			var modal = $('#myModal2').modal({backdrop: 'static', keyboard: false});
	    	modal.show();			
		});
		<%-- 导入发布  --%>
		$("#btn-publish-import").click(function() {
			var ids = [];
			var statuses = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
				
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要发布的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条进行发布。");
				return;
			}
			//发布
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/ImportDesign/publish",
				type:"post",
				dataType:"json",
				data:{
					no:ids[0],
					groupNo:$("#moduleNo").val()
				},
				success:function(data){
					if (data.errType == "1") {
						MessageBox.processEnd("系统错误:" + data.errMessage);
						return;
					}
					MessageBox.processEnd("发布成功！", function() {
						window.location.href = "${rc.contextPath}/ImportDesign/list";	
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
				}
			});
		});
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
            <li><a href="#"><i class="fa fa-dashboard"></i>画面设计</a></li>
            <li class="active">导入设定列表</li>
          </ol>

        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/ImportDesign/list">
                	<div class="box_sreach clearfix">
	                	<div class="fl mr_30">
	                  <label for="no" class="label-header label-header1">导入设定编码：</label>
	                  <input type="text" id="no" name="no" value="${no}" placeholder="导入设定编码：" class="form-control input-sm fl w15">
                  	</div>
                  	<div class="fl mr_30">
	                  <label for="name" class="label-header label-header1">导入设定名称：</label>
	                  <input type="text" id="name" name="name" value="${name}" placeholder="弹窗名称：" class="form-control input-sm fl w15">
                  	</div>
                   <div class="fl mr_30">
			          		<hq:auth act="ImportDesignList">
			          			<a class="btn_sreach fr" id="btn-search" href="#">搜 &nbsp;索</a>
			          		</hq:auth>
			        </div>
                  	</div>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                    	          <div class="btn_box">
          	<div class="search">
          		<hq:auth act="ImportDesignAdd">
          			<button class="btn_header" id="btn_add" type="button">新增</button>
          		</hq:auth>
          		<hq:auth act="ImportDesignEdit">
          			<button class="btn_header" id="btn_edit" type="button">修改</button>
          		</hq:auth>
          		<hq:auth act="ImportDesignPublish">
          			<button class="btn_header" id="btn_publish" type="button">发布</button>
          		</hq:auth>
          		<hq:auth act="ImportDesignDelete">
          			<button class="btn_header" id="btn_delete" type="button">删除</button>
          		</hq:auth>
          	</div>
          	<div class="line_left"></div>
<!--           	<div class="btn-group"> -->
<%--           		<hq:auth act="ImportDesignList"> --%>
<!--           			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
<%--           		</hq:auth> --%>
<!--           	</div> -->
          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">导入设定信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
		                           <th>导入设定编码</th>
		                           <th>导入设定名称</th>
		                           <th>导入模式</th>
								</tr>
						      	<c:forEach items="${data.Design}" var="importDesign">
						      	 <tbody>
						           <tr>
						           	 <td><input type="checkbox" value="${importDesign.no }"></td>
						             <td>${importDesign.no }</td>
						             <td>${importDesign.name}</td>
						             <td>${importDesign.model}</td>
						           </tr> 
						           </tbody>
						       </c:forEach>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </div>
        </section><!-- /.content -->
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">新增画面</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>画面编号：</td>
		                                	<td><input type="text" id="viewNo" name="viewNo" placeholder="画面编号：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>画面名称：</td>
		                                	<td><input type="text" id="viewName" name="viewName" placeholder="画面名称：" class="form-control input-sm fl w15"></td>
		                                </tr>
		                                <tr>
		                                	<td>单据类型：</td>
		                                	<td>
												<select id="voucherType" name="voucherType" class="form-control input-sm fl w15" style="width:28%;">
													<option value=""></option>
													<c:forEach items="${voucherTypeList }" var="voucherType">
														<option value="${voucherType.no }">${voucherType.name }</option>
													</c:forEach>
												</select>
												<input type="hidden" id="voucherTypeName" name="voucherTypeName"/>
		                                	</td>
		                                </tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-voucher">确定</button>
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
                        <h4 class="modal-title" id="myModalLabel2">发布画面</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="${rc.contextPath}/viewDesign/edit" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                <tr>
		                                	<td>所属模块：</td>
		                                	<td>
												<select id="moduleNo" name="moduleNo" class="form-control input-sm fl w15" style="width:28%;">
													<option value=""></option>
													<c:forEach items="${data.Group }" var="module">
														<option value="${module.no }">${module.name }</option>
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
                        <button type="button" class="btn btn-default" id="btn-publish-import">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>