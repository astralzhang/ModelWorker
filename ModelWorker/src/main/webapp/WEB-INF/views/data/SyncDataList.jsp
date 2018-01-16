<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="dataSync"/>
	<script type="text/javascript">	
	$(function(){
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		$("#table_scroll a").click(function() {
			var id = $(this).attr("id");
			var name = $(this).html();
			//文件上传
			MessageBox.processStart();
			$.ajax({
				type:"POST",
				dataType:"json",
				data:{},
				url:"${rc.contextPath}/dataSync/sync/" + id,
			}).success(function(data){
				if (data.errType == "0") {
					MessageBox.processEnd(name + "同步成功！");
				} else {
					MessageBox.processEnd(data.errMessage);
				}				
			}).error(function(data){
				MessageBox.processEnd(data);
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
            <li><a href="#"><i class="fa fa-dashboard"></i>同步中心</a></li>
            <li class="active">同步中心</li>
          </ol>
          <div class="btn_box">
          	
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">同步中心</h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                        <div id="table_scroll" style="overflow:auto;">
                        <c:forEach items="${data }" var="group">
                        	<div>
	                            <div style="padding-left:10px;border:1px;font-size:20px;">※${group.name }</div>
	                            <c:forEach items="${group.syncList }" var="syncBean">
	                            	<span style="padding-left:50px;display:-moz-inline-box;display:inline-block;width:200px;"><a id="${syncBean.no }" href="#">${syncBean.name }</a></span>
	                            </c:forEach>
	                        </div>
                        </c:forEach>
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
                        <h4 class="modal-title" id="myModalLabel1">新增流程</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form id="form1" role="form1" action="#" method="post">
                            	
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
        </section><!-- /.content -->
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>