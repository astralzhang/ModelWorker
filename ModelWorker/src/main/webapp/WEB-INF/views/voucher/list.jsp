<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="${menuId}"/>
	<script type="text/javascript">	
	$(function(){
		$.ajax({
			  url:"${rc.contextPath}/voucher/getData",
			  type:"post",
			  dataType:"json",
			  data:{
				  type :"${voucherType}"
			  },
			  success:function(data){
				  if(data.errType == "0"){
					  //数据取得
					  $("#voucherTitle").html(data.title);
					  $("#tableName").html(data.title);
					  //设定表格标题
					  var tableTitle = "<tr><th>选择</th>";
					  $.each(data.data_header, function(n, value){
						  tableTitle += "<th>" + value + "</th>";
					  });
					  tableTitle += "</tr>";
					  $("#dataTable thead").html(tableTitle);
					  //设定表格数据
					  var tableBody = "";
					  $.each(data.data, function(n, value) {
						  tableBody += "<tr>";
						  tableBody += "<td><input type='checkbox' value='" + value["dataKey"] + "'></td>";
						  $.each(data.data_header, function(name, valueData){
							  tableBody += "<td>";
							  tableBody += value[valueData];
							  tableBody += "</td>";
						  });
						  tableBody += "</tr>";
					  });
					  $("#dataTable tbody").html(tableBody);
				  } else {
					  MessageBox.info(data.errMessage);
				  }
			  },
			  error:function(XMLHttpRequest, data) {
				  MessageBox.processEnd("系统错误，详细信息：" + data);
			  }
		  });
		//提交
		$("#btn_submit").click(function() {
			var ids = [];
			var statuses = [];
			$('#dataTable tbody tr input[type="checkbox"]:checked').each(function(){
				ids.push($(this).val());				
			});
			MessageBox.processStart();
			$.ajax({
				  url:"${rc.contextPath}/flowProcessor/submit",
				  type:"post",
				  dataType:"json",
				  data:{
					  voucherType :"${voucherType}",
					  dataKey:ids.toString()
				  },
				  success:function(data){
					  if(data.errType == "0"){
						  MessageBox.processEnd(data.errMessage, function(){
							  location.href = "${rc.contextPath}/voucher/list?type=${voucherType}";
						  });
					  } else {
						  MessageBox.processEnd(data.errMessage);
					  }
				  },
				  error:function(XMLHttpRequest, data) {
					  MessageBox.processClose("系统错误，详细信息：" + data);
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
            <li><a href="#"><i class="fa fa-dashboard"></i>单据中心</a></li>
            <li class="active" id="voucherTitle">单据中心</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          		<%--  <hq:auth act="flowAdd"> --%>
          			<button class="btn_header" id="btn_submit" type="button">提交</button>
          		<%-- </hq:auth> --%>
          	</div>
          	<%-- 
          	<div class="line_left"></div>
          	<div class="btn-group">
          		<hq:auth act="flowList">
          			<a class="btn_header" id="btn-search" href="#">搜索</a>
          		</hq:auth>
          	</div>
          	--%>
          </div>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <%--
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/flowDesign/list">
                	<div class="fl">
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
                </form>
                --%>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title" id="tableName"></h3>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover" id="dataTable">
								<thead>
                            	</thead>
								<tbody>
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