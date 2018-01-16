<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="TradeDoc"/>
	<script type="text/javascript">	
	$(function(){
		<%-- 检索 --%>
		$("#btn-search").click(function() {
			$("#searchForm").submit();
		});
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		<%-- 文件选择按钮 --%>
		$("#selectFile").click(function() {
			$("#uploadFile").trigger("click");
		});
		<%-- 文件选择变化 --%>
		$("#uploadFile").change(function() {
			if ($("#uploadFile").val() == "") {
				$("#uploadFileName").html("没有文件选择");
				return;
			}
			var fileName = $("#uploadFile").val();
			if (fileName.length > 10) {
				fileName = fileName.substr(0, 10) + "...";
			}
			$("#uploadFileName").html(fileName);
		});
		<%-- 上传文件名显示 --%>
		$("#uploadFileName").mouseover(function() {
			if ($("#uploadFile").val() != "") {
				$("#uploadFileName").attr("title", $("#uploadFile").val());
			} else {
				$("#uploadFileName").attr("title", "");
			}
		});
		<%-- 下载附件函数 --%>
		var downloadFunc = function() {
			var id = $(this).closest("tr").find("input[type='hidden']").val();
			location.href = "${rc.contextPath}/TradeDoc/download?id=" + id;
		};
		<%-- 删除附件函数 --%>
		var deleteFunc = function() {
			var tr = $(this).closest("tr");
			var id = $(tr).find("input[type='hidden']").val();
			//删除处理
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/deleteAtta",
				type:"post",
				dataType:"json",
				data:{
					id:id
				},
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processEnd("删除成功！", function() {
							$(tr).remove();
						});
						return;
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		};
		<%-- 上传按钮 --%>
		$("#btn_upload").click(function () {
			if ($("#uploadFile").val() == "") {
				MessageBox.info("请选择需要上传的文件！");
				return;
			}
			MessageBox.processStart();
			var formData = new FormData($("#uploadForm")[0]);	
			$.ajax({
				type:"POST",
				dataType:"json",
				data:formData,
				url:"${rc.contextPath}/TradeDoc/upload",
				contentType:false,
				processData:false
			}).success(function(data){
				if (data.errType == "0") {
					var tbody = "";
					$.each(data.data, function(n, value) {
						tbody += "<tr>";
						tbody += "<td><input type='hidden' value='" + value.id + "'>" + value.fileName + "</td>";
						tbody += "<td>" + value.uploadTime + "</td>";
						tbody += "<td><input type='button' value='下载' name='btn_download'></td>";
						tbody += "<td><input type='button' value='删除' name='btn_delete'></td>";
						tbody += "</tr>";
					});
					$("#Attachment tbody").html(tbody);
					$("#Attachment tbody input[name='btn_download']").unbind("click");
					$("#Attachment tbody input[name='btn_download']").bind("click", downloadFunc);
					$("#Attachment tbody input[name='btn_delete']").unbind("click");
					$("#Attachment tbody input[name='btn_delete']").bind("click", deleteFunc);
					$("#id").val(data.id);
					$("#documentId").val(data.id);
					<%-- 清空选择的文件 --%>
					$("#uploadFile").val("");
					$("#uploadFile").change();
					MessageBox.processEnd("上传成功！");
				} else {
					MessageBox.processEnd(data.errMessage);
				}				
			}).error(function(data){
				MessageBox.processEnd(data);
			});
		});
		<%-- 本页面离开时的处理 --%>
		$(window).unload(function () {
			var id = $("#id").val();
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/close",
				type:"post",
				dataType:"json",
				data:{
					id:id
				},
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processEnd("删除成功！", function() {
							$(tr).remove();
						});
						return;
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		});
		<%-- 保存 --%>
		$("#btn_save").click(function () {
			if ($("#docTitle").val() == "") {
				MessageBox.info("请输入标题！");
				return;
			}
			var param = new Object;
			param.id = $("#id").val();
			param.title = $("#docTitle").val();
			param.description = $("#description").val();
			param.infoType = $("#infoType").val();
			MessageBox.processStart();
			$.ajax({
				url:"${rc.contextPath}/TradeDoc/save",
				type:"post",
				contentType:"application/json",
				dataType:"json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.errType == "0") {
						MessageBox.processEnd("保存成功！", function() {
							location.href = "${rc.contextPath}/TradeDoc/list";
						});
						return;
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		});
		<%-- 返回 --%>
		$("#btn-back").click(function() {
			location.href = "${rc.contextPath}/TradeDoc/list";
		});
		<%-- 添加附件下载事件 --%>
		$("#Attachment tbody input[name='btn_download']").unbind("click");
		$("#Attachment tbody input[name='btn_download']").bind("click", downloadFunc);
		<%-- 添加附件删除事件 --%>
		$("#Attachment tbody input[name='btn_delete']").unbind("click");
		$("#Attachment tbody input[name='btn_delete']").bind("click", deleteFunc);
	});
	</script>
</head>
<body>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>办公工作</a></li>
            <li class="active">公文信息编辑</li>
          </ol>
 
        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <div class="box box-info product-info"><%-- 公文基本信息 --%>
	           	<div class="btn_box">
		          	<div class="search">
		          		<hq:auth act="TradeDocSave">
		          			<button class="btn_header" id="btn_save" type="button">保存</button>
		          		</hq:auth>
		          	</div>
		          	<div class="line_left"></div>
		          	<div class="btn-group">
		          		<hq:auth act="TradeDoc">
		          			<a class="btn_header" id="btn-back" href="#">返回</a>
		          		</hq:auth>
		          	</div>
	          </div>
<!--                 <div class="box-header"> -->
<!--                     <h2 class="box-title">公文基本信息</h2> -->
<!--                 </div> -->
                <div class="box-body">
                	<form id="form1" enctype="multipart/form-data" method="post">
                    <table class="table table-bordered">
                        <tr>
                            <td>标题</td>
                            <td><input id="docTitle" type="text" name="docTitle" maxlength="100" value="${Document.title }" class="form-control2">
								<input type="hidden" name="id" id="id" value="${Document.id }" />
                            </td>
						</tr>
						<tr>
                            <td>正文</td>
                            <td>
                               <textarea name="description" id="description" maxlength="2000" class="form-control input-sm fl" style="height:200px;">${Document.description }</textarea>
                            </td>
                        </tr>
						<tr>
                            <td>信息分类</td>
                            <td>
								<select id="infoType" name="infoType"  class="form-control2" required>
								<c:forEach items="${InfomationTypeList }" var="InfomationType">
									<option value="${InfomationType.code }" <c:if test="${Document.infoType eq InfomationType.code }">selected</c:if>>${InfomationType.name }</option>
								</c:forEach>
								</select>
                            </td>
						</tr>
                    </table>
                    </form>
                </div>
            </div><%-- 公文基本信息 --%>
			<div class="box box-warning cost-info"><%-- 附件信息 --%>
                <div class="box-header">
                    <h2 class="box-title">附件信息</h2>
                </div>
                <hq:auth act="TradeDocUpload">
                <div>
                	<input type="button" value="选择附件" id="selectFile" class="btn_templet">
                	<span id="uploadFileName" style="display:-moz-inline-box;display:inline-block;width:150px;">没有选择文件</span>
                	<button type="button" id="btn_upload" class="btn_templet">上传</button>
                </div>
                </hq:auth>
                <div class="box-body" style="overflow-x:auto;overflow-y:hidden">
                    <table class="table table-bordered" id="Attachment">
                    	<thead>
	                        <tr>
	                            <th>文件名称</th>
	                            <th>上传时间</th>
	                            <th colspan="2">操作</th>
	                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${DocumentAttachment }" var="atta">
                        	<tr>
                        		<td><input type="hidden" value="${atta.id}">${atta.fileName }</td>
                        		<td>${atta.uploadTime }</td>
                        		<td><input type="button" value="下载" name="btn_download" class="btn_templet"></td>
                        		<td><input type="button" value="删除" name="btn_delete" class="btn_templet"></td>
                        	</tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div><%-- 附件信息 --%>
		</div>
        </section><!-- /.content -->
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">用户组</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="uploadForm" role="uploadForm" action="${rc.contextPath}/TradeDoc/upload" method="post">
                            	<input type="file" id="uploadFile" name="uploadFile">
                            	<input type="hidden" name="id" id="documentId" value="${Document.id }">
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-group-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal2" aria-labelledby="myModalLabel2">
            <div class="modal-dialog modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel2">重置密码</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form2" role="form2" action="#" method="post">
                            	<div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
		                            <table class="table table-bordered table-condensed">
		                                <tbody>
		                                	<tr>
		                                		<td>新密码：</td>
		                                		<td><input type="password" id="newPassword1" class="form-control input-sm fl w15"></td> 
		                                	</tr>
		                                	<tr>
		                                		<td>确认密码：</td>
		                                		<td><input type="password" id="newPassword2" class="form-control input-sm fl w15"></td> 
		                                	</tr>
		                                </tbody>
		                            </table>
		                        </div>
                            </form>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-resetPwd-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>