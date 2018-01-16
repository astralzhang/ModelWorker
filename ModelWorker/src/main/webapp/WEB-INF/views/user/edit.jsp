<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="systemUserList"/>
	<script type="text/javascript">
	var objUserNo;
	var objUserName;
	$(function(){
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		<%-- 保存 --%>
		$("#btn_save").click(function() {
			if ($("#userNo").val() == null || $("#userNo").val() == "") {
				MessageBox.info("请输入用户编码！");
				return;
			}
			if ($("#userName").val() == null || $("#userName").val() == "") {
				MessageBox.info("请输入用户名称！");
				return;
			}
			MessageBox.processStart();
			var postData = new Object;
			postData.userNo = $("#userNo").val();
			postData.userName = $("#userName").val();
			postData.mailAddress = $("#mailAddress").val();
			$.ajax({
				  url:"${rc.contextPath}/user/save",
				  type:"post",
				  contentType:'application/json',
				  dataType:"json",
				  data:JSON.stringify(postData),
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd("保存成功！", function() {
							  location.href = "${rc.contextPath}/user/list";
						  });
					  } else {
						  MessageBox.processEnd(data.errMessage);
					  }
				  },
				  error:function(XMLHttpRequest, txtStatus, data) {
					  MessageBox.processEnd("系统错误，详细信息：" + data);
				  }
			  });
		});
	});
	</script>
</head>
<body> 
    <!-- Content Wrapper. Contains page content -->
    <fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyyMMdd" var="bb"/>
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i>用户管理</a></li>
            <li class="active">用户编辑</li>
          </ol>
 
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                         <div class="btn_box">
				          	<div class="search">
				          		<hq:auth act="SystemUserSave">
				          			<button class="btn_header" id="btn_save" type="button">保存</button>
				          		</hq:auth>
				          	</div>
				          	<div class="line_left"></div>
				          	<div class="btn-group">
				          		<a class="btn_header" id="btn-back" href="${rc.contextPath }/user/list">返回</a>
				          	</div>
				          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">用户编辑</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
                            	<tbody>
                            		<tr>
	                            		<td>用户编码</td>
	                            		<td>
	                            			<input type="text" id="userNo" name="userNo" value="${user.userNo }" class="form-control input-sm fl w15" <c:if test="${not empty user.userNo }">readonly</c:if>>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>用户名称</td>
	                            		<td>
	                            			<input type="text" id="userName" name="userName" value="${user.userName }" class="form-control input-sm fl w15">
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>用户邮箱</td>
	                            		<td>
	                            			<input type="text" id="mailAddress" name="mailAddress" value="${user.mailAddress }" class="form-control input-sm fl w15">
	                            		</td>
	                            	</tr>
								</tbody>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
        <%-- 用户选择 --%>
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">工作组</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form" id="userSearchForm">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-user-search" onclick="showAddAudit();"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table id="userTable" class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>选择</th>
	                                    <th>组编号</th>
	                                    <th>组名称</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-group-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>