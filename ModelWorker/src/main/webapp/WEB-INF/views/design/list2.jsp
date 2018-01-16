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
				alert(ids[0]);
				window.location.href = "${rc.contextPath}/flowDesign/add?flowNo=" + ids[0];
			}
		});
		//审核
		$("#btn_audit").click(function() {
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要审核的单据。");
				return;
			}else{				
			  $.ajax({
				  url:"${rc.contextPath}/overTimeManage/audit",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){					  
					  MessageBox.processEnd(data.errMsg, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		  }
		});
		//弃审		
		$("#btn_audit_cancel").click(function(){
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要弃审的单据。");
				return;
			}else{				
			  $.ajax({
				  url:"${rc.contextPath}/overTimeManage/audit_cancel",
				  type:"post",
				  dataType:"json",
				  data:{
					  ids :ids.toString()
				  },
				  success:function(data){					  
					  MessageBox.processEnd(data.errMsg, function(){
						  if(data.errType == "0"){
							  $("#searchForm").submit();
						  }
					  });
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		  }
		});
		//查看
		$("#btn_view").click(function(){
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要查看的数据。");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据进行查看。");
				return;
			} else {
				//只有一条数据,被选择时
				window.location.href = "${rc.contextPath}/overTimeManage/viewOverTime?id=" + ids[0];
			}
		});
		
		//删除按钮按下
		$('#btn_delete').click(function(){
			MessageBox.processStart();
			var ids = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要删除的数据。");
				return;
			}else{
				MessageBox.processClose();
				MessageBox.confirm("确定将此记录删除?",function(){
				  MessageBox.processStart();
				  $.ajax({
					  url:"${rc.contextPath}/overTimeManage/delete",
					  type:"post",
					  dataType:"json",
					  data:{
						  ids :ids.toString()
					  },
					  success:function(data){
						  MessageBox.processEnd(data.errMsg, function() {
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
		  }
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
            <li><a href="#"><i class="fa fa-dashboard"></i>上料管理</a></li>
            <li class="active">物料清单</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		
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
                	<%--<div class="fl">
	                  <label for="flowNo" class="label-header label-header1">流程编号：</label>
	                  <input type="text" id="flowNo" name="flowNo" value="${flowBean.no}" placeholder="流程编号：" class="form-control input-sm fl w15">
                  	</div>
                  
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
                            <h3 class="box-title">物料清单</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th>物料编码</th>
		                           <th>物料名称</th>
		                           <th>上料器号</th>
		                           <th>需求量</th>
		                           <th>已上料量</th>
		                           <th>状态</th>
								</tr>

						           <tr>
						           	 <td>Y000123</td>
						             <td>TF213 V59</td>
						             <td>45</td>
						             <td>785</td>
						             <td>100</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>X000323</td>
						             <td>GY453 V57</td>
						             <td>45</td>
						             <td>345</td>
						             <td>125</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>N400123</td>
						             <td>GY422 V67</td>
						             <td>45</td>
						             <td>100</td>
						             <td>80</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>H442351</td>
						             <td>TH2208 V7</td>
						             <td>45</td>
						             <td>785</td>
						             <td>200</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>Y000354</td>
						             <td>GYYHD V57</td>
						             <td>45</td>
						             <td>772</td>
						             <td>200</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>B000093</td>
						             <td>GY795 V3</td>
						             <td>45</td>
						             <td>710</td>
						             <td>150</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>X900821</td>
						             <td>GY432 V57</td>
						             <td>42</td>
						             <td>70</td>
						             <td>80</td>
						             <td>正常料</td>
						           </tr>
						           <tr>
						           	 <td>Y998082</td>
						             <td>GY432 V3T</td>
						             <td>42</td>
						             <td>708</td>
						             <td>200</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>Y008334</td>
						             <td>GY343 N8</td>
						             <td>42</td>
						             <td>700</td>
						             <td>120</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>Y098521</td>
						             <td>GY974 V57</td>
						             <td>42</td>
						             <td>993</td>
						             <td>300</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>Y342432</td>
						             <td>GY938 V57</td>
						             <td>45</td>
						             <td>785</td>
						             <td>400</td>
						             <td>正常</td>
						           </tr>
						           <tr>
						           	 <td>Y342643</td>
						             <td>GY213 V57</td>
						             <td>45</td>
						             <td>685</td>
						             <td>75</td>
						             <td>正常</td>
						           </tr>
						           
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