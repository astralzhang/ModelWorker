<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="flowList"/>
	<script type="text/javascript">	
	$(function(){
		//新增流程确定按钮
		$("#btn-flow").click(function() {
			//document.getElementById("form1").submit();
			$("#form1").submit();
			//window.location.href = "${rc.contextPath}/flowDesign/add";
		});
		$("#btn-search").click(function() {
			$("#searchForm").submit();
		});
		
		$('.date').datepicker({
			 format: 'yyyy-mm-dd'
			}
		);
		$("#btn_add").on('click', function() {
			var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		});
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
				//编辑流程
				window.location.href = "${rc.contextPath}/flowDesign/edit?flowNo=" + ids[0];
			}
		});
		<%-- 流程发布 --%>
		$("#btn_publish").click(function() {
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要发布的流程。");
				return;
			} else if (ids.length > 1) {
				MessageBox.processEnd("请选择一个流程进行发布！");
				return;
			}	
			$.ajax({
				url:"${rc.contextPath}/flowDesign/publish",
				type:"post",
				dataType:"json",
				data:{
					id :ids.toString()
				},
				success:function(data){					  
					MessageBox.processEnd(data.errMessage, function(){
						if(data.errType == "0"){
							$("#searchForm").submit();
						}
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
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
            <li><a href="#"><i class="fa fa-dashboard"></i>流程设计</a></li>
            <li class="active">流程列表</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<hq:auth act="flowAdd">
          			<button class="btn_header" id="btn_add" type="button">新增</button>
          		</hq:auth>
          		<hq:auth act="flowEdit">
          			<button class="btn_header" id="btn_edit" type="button">修改</button>
          		</hq:auth>
          		<hq:auth act="flowDelete">
          			<button class="btn_header" id="btn_delete" type="button">删除</button>
          		</hq:auth>
          		<hq:auth act="flowPublish">
          			<button class="btn_header" id="btn_publish" type="button">发布</button>
          		</hq:auth>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<hq:auth act="flowList">
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
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/flowDesign/list">
                	<div class="fl">
	                  <label for="flowNo" class="label-header label-header1">流程编号：</label>
	                  <input type="text" id="flowNo" name="flowNo" value="${flowBean.no}" placeholder="流程编号：" class="form-control input-sm fl w15">
                  	</div>
                  <%--
                  	<div class="fl">
                        <label for="status" class="label-header label-header1">状态：</label>
                        <select id="status" name="status"  class="form-control input-sm fl w15">
                        	<option value=""></option>
                            <option value="0" <c:if test="${flowBean.status eq '0' || flowBean.status eq null}"> selected</c:if>>保存</option>
                            <option value="1" <c:if test="${flowBean.status eq '1' }"> selected</c:if>>已发布</option>
                        </select>
                  	</div>
                  --%>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">流程信息</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th></th>
		                           <th>流程编号</th>
		                           <th>流程名称</th>
		                           <th>单据类型</th>
		                           <th>状态</th>
								</tr>
						      	<c:forEach items="${flowList}" var="flow">
						      	 <tbody>
						           <tr>
						           	 <td><input type="checkbox" value="${flow.no }"></td>
						             <td>${flow.no }</td>
						             <td>${flow.name}</td>
						             <td>${flow.voucherTypeName}</td>
						             <td>
						             	<c:if test="${flow.status eq '0'}">保存</c:if>
						             	<c:if test="${flow.status eq '1'}">发布</c:if>
						             </td>
						           </tr> 
						           </tbody>
						       </c:forEach>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
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