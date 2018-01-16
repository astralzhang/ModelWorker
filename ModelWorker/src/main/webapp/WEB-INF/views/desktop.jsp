<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<meta name="menuId" content=""/>
	<script type="text/javascript">	
	$(function(){
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		$("#add_module").click(function () {
			var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		});
		$("#btn-add-module").click(function () {
			var ids = [];
			$.each($("#myModal1 table tbody input[type='checkbox']"), function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
				}
			});
			if (ids.length <= 0) {
				MessageBox.info("请选择功能！");
				return;
			}
			MessageBox.processStart();
			$.ajax({
			  url:"${rc.contextPath}/desktop/save",
			  type:"post",
			  dataType:"json",
			  data:{
				  ids:ids.toString()
			  },
			  success:function(data){
				  if (data.errType != 0) {
					  MessageBox.processEnd(data.errMessage);
					  return;
				  }
				  MessageBox.processEnd("保存成功！", function() {
					  location.href = "${rc.contextPath}/desktop/desktop";
				  });
			  },
			  error:function(XMLHttpRequest, data) {
				  MessageBox.processEnd("系统错误，详细信息：" + data);
			  }
			});
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
            <li><a href="#"><i class="fa fa-dashboard"></i>我的桌面</a></li>
            <%-- <li class="active">导入中心</li> --%>
          </ol>
          <div class="btn_box">
          	
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
<!--                         <div class="box-header"> -->
<%--                             <h3 class="box-title"><img src="${rc.contextPath }/img/timg.jpg" width="20" height="20">我的桌面</h3> --%>
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                        <div id="table_scroll" style="overflow:auto;">
                           	<div>
	                            <c:forEach items="${data.desktop }" var="desktop">
	                            	<span style="padding-left:50px;font-size:23px;display:-moz-inline-box;display:inline-block;width:350px;"><a id="${desktop.itemNo}" href="${rc.contextPath }${desktop.actionUrl}"><img src="${rc.contextPath }${desktop.iconFile}" width="40" height="40">${desktop.itemName }</a></span>
	                            </c:forEach>
	                            <span style="padding-left:50px;font-size:23px;"><a id="add_module" href="#"><img src="${rc.contextPath }/img/index/Add.png" width="40" height="40">添加功能</a></span>
	                        </div>
                        </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
            <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">新增功能</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="#" method="post">
                            	
                            </form>
                        </div>
                        <div id="moduleDiv" style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th></th>
	                                    <th>模块</th>
	                                    <th>功能</th>
	                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${data.menu }" var="menu">
                                	<tr>
                                		<td><input type="checkbox" value="${menu.itemNo }"></td>
                                		<td>${menu.moduleName}</td>
                                		<td>${menu.itemName }</td>
                                	</tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-add-module">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        </section><!-- /.content -->
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>