<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="GroupList"/>
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
			if ($("#groupNo").val() == null || $("#groupNo").val() == "") {
				MessageBox.info("请输入工作组编码！");
				return;
			}
			if ($("#groupName").val() == null || $("#groupName").val() == "") {
				MessageBox.info("请输入工作组名称！");
				return;
			}
			if ($("#groupNo").val().length > 20) {
				MessageBox.info("请输入20位内工作组编码！");
				return;
			}
			if ($("#groupName").val().length > 100) {
				MessageBox.info("请输入100字以内的工作组名称！");
				return;
			}
			MessageBox.processStart();
			var postData = new Object;
			postData.groupNo = $("#groupNo").val();
			postData.groupName = $("#groupName").val();
			$.ajax({
				  url:"${rc.contextPath}/group/save",
				  type:"post",
				  dataType:"json",
				  contentType:'application/json',
				  data:JSON.stringify(postData),
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd("工作组保存成功！", function() {
							  location.href = "${rc.contextPath}/group/list";
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
            <li><a href="#"><i class="fa fa-dashboard"></i>工作组管理</a></li>
            <li class="active">工作组编辑</li>
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
				          		<hq:auth act="GroupSave">
				          			<button class="btn_header" id="btn_save" type="button">保存</button>
				          		</hq:auth>
				          	</div>
				          	<div class="line_left"></div>
				          	<div class="btn-group">
				          		<a class="btn_header" id="btn-back" href="${rc.contextPath }/group/list">返回</a>
				          	</div>
				          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">工作组编辑</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
                            	<tbody>
                            		<tr>
	                            		<td>工作组编码</td>
	                            		<td>
	                            			<input type="text" id="groupNo" name="groupNo" value="${data.groupNo }" class="form-control input-sm fl w15" <c:if test="${not empty data.groupNo }">readonly</c:if>>
	                            		</td>
	                            	</tr>
	                            	<tr>
	                            		<td>工作组名称</td>
	                            		<td>
	                            			<input type="text" id="groupName" name="groupName" value="${data.groupName }" class="form-control input-sm fl w15">
	                            		</td>
	                            	</tr>
								</tbody>
                            </table>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->
</body>
</html>