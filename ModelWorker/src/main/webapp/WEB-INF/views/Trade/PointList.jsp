<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- <%@ taglib prefix="code" uri="/WEB-INF/tld/code4dropdown.tld"%> --%>
<html>
<head>
	<meta name="menuId" content="PointManager"/>
	<script type="text/javascript">	
	$(function(){
		$('#btn-cancel').on('click', function () {
			$("#searchForm input").val("");
			$("#searchForm select").val("");
		});
		$("#btn-search").click(function() {
			MessageBox.processStart();
			$("#searchForm").submit();
		});
		
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		//确认
		$("#btn_save").click(function() {
			MessageBox.processStart();
			var documentAcceptsBean = new Object;
			var pointList = [];
			var ids = [];
			$('#dataTable tbody tr input[name="selectPoint"]:checked').each(function(){
				ids.push($(this).val());
				pointList.push(getConfirmData(this));
			});
			if (ids.length == 0) {
				MessageBox.processEnd("请选择要保存的积分数据。");
				return;
			}else{		
			  documentAcceptsBean.documentAcceptsList=pointList;
			  $.ajax({
				  url:"${rc.contextPath}/Point/save",
				  type:"post",
				  contentType:'application/json',
				  dataType:"json",
				  data:JSON.stringify(documentAcceptsBean),
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
		  }
		});
		
		 	
		/*table 中的checkbox 的全选功能  */
		$("#allCheck").click(function() {
			//console.log("select all");
			if ($("#allCheck").prop("checked")) {
				$("input[name='selectPoint']").each(function() {
					$(this).prop("checked", true);
				})
			} else {
				$("input[name='selectPoint']").each(function() {
					$(this).prop("checked", false);
				})
			}
		});
	});
	
    function getConfirmData(obj){
    	var documentAcceptsBean = new Object;
    	documentAcceptsBean.id =  $(obj).closest("tr").find("input[name='checkId']:first").val();
    	documentAcceptsBean.point =  $(obj).closest("tr").find("input[name='point']:first").val();
    	documentAcceptsBean.levelCode =  $(obj).closest("tr").find("input[name='levelCode']:first").val();
    	documentAcceptsBean.magazineCode =  $(obj).closest("tr").find("input[name='magazineCode']:first").val();
    	documentAcceptsBean.acceptType =  $(obj).closest("tr").find("input[name='acceptType']:first").val();
    	documentAcceptsBean.infoType =  $(obj).closest("tr").find("input[name='infoType']:first").val();
    	return documentAcceptsBean;
    }
	//获取所有checkbox选中的项目 
	function getId(){
		  var IdArr = [];
		  $("input[name='selectPoint']").each(function(){
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
            <li><a href="#"><i class="fa fa-dashboard"></i>积分管理</a></li>
            <li class="active">期刊积分信息</li>
          </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <!-- form start -->
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/Point/query">
	               <div class="box_sreach clearfix">
	                <div class="fl mr_30">
                        <label for="search_level" class="label-header label-header1">采用级别：</label>
                        <select id="search_level" name="search_level" class="form-control input-sm fl w15"">
                        <option value=""></option>
                        <c:forEach items="${data.levelList}" var="levelInfo">
                        	<option value="${levelInfo.code }" <c:if test="${levelInfo.code eq data.search_level }"> selected</c:if>>${levelInfo.name }</option>
                        </c:forEach>
                        </select>
                        </div>
                    <div class="fl mr_30">
                        <label for="search_magazines" class="label-header label-header1">期刊：</label>
                        <select id="search_magazines" name="search_magazines" class="form-control input-sm fl w15"">
                        <option value=""></option>
                        <c:forEach items="${data.magazineList}" var="magazinesInfo">
                        	<option value="${magazinesInfo.code }" <c:if test="${magazinesInfo.code eq data.search_magazines }">selected</c:if>>${magazinesInfo.name }</option>
                        </c:forEach>
                        </select>
                        </div>
                  <div class="fl mr_30">
                        <label for="search_accept" class="label-header label-header1">采用类型：</label>
                        <select id="search_accept" name="search_accept" class="form-control input-sm fl w15"">
                        <option value=""></option>
                        <c:forEach items="${data.acceptList }" var="acceptfo">
                        	<option value="${acceptfo.code }" <c:if test="${acceptfo.code eq data.search_accept }">selected</c:if>>${acceptfo.name }</option>
                        </c:forEach>
                        </select>
                        </div>
                  <div class="fl mr_30">
                        <label for="search_info" class="label-header label-header1">信息类别：</label>
                        <select id="search_info" name="search_info" class="form-control input-sm fl w15"">
                        <option value=""></option>
                        <c:forEach items="${data.infoList }" var="info">
                        	<option value="${info.code }" <c:if test="${info.code eq data.search_info }">selected</c:if>>${info.name }</option>
                        </c:forEach>
                        </select>
                        </div>
                  <div class="fl mr_30">
<%-- 		          		<hq:auth act="systemUserList"> --%>
		          			<a class="btn_sreach fr" id="btn-search" href="#">搜 &nbsp;索</a>
<%-- 		          		</hq:auth> --%>
		          	</div>
                   </div>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
			          <div class="btn_box">
			          	<div class="search">
			<%--           		<hq:auth act="pointSave"> --%>
			          		<button class="btn_header" id="btn_save" type="button">保存</button>
			<%--           		</hq:auth> --%>
			          	</div>
			          	<div class="line_left"></div>
			          	<div class="btn-group">
			<%--           		<hq:auth act="pointList"> --%>
<!-- 			          			<a class="btn_header" id="btn-search" href="#">搜索</a> -->
			<%--           		</hq:auth> --%>
			<!--           		<a class="btn_header" id="btn-cancel" href="#">重置</a> -->
			          	</div>
			          </div>
<!--                         <div class="box-header"> -->
<!--                             <h3 class="box-title">期刊积分信息</h3> -->
<!--                         </div>/.box-header -->
                        <div class="box-body table-responsive no-padding">
                        <div id="table_scroll" style="overflow:auto;">
                            <table class="table table-hover" id="dataTable">
				            	<tr>
				                   <th><input type="checkbox" id="allCheck"></th>
		                           <th>采用级别</th>
		                           <th>期刊名称</th>
		                           <th>采用类别</th>
		                           <th>信息类别</th>
		                           <th >积分</th>
								</tr>
						      	<c:forEach items="${data.rstList}" var="result">
						      	 <tbody>
						           <tr>
						             <td><input type="checkbox" name="selectPoint" value="${result.infoType}" > </td>
						             <td>${result.levelName}</td>
						             <td>${result.magazineName}</td>
						             <td>${result.acceptTypeName}</td>
						             <td>${result.infoTypeName}</td>
		                           	 <td align="center"><input type="text" name="point" id="point" class="form-control input-sm fl w15" value="${result.point }">
		                           	 	<input type="hidden" name="levelCode" value="${result.levelCode }">
		                           	 	<input type="hidden" name="magazineCode" value="${result.magazineCode }">
		                           	 	<input type="hidden" name="acceptType" value="${result.acceptType }">
		                           	 	<input type="hidden" name="infoType" value="${result.infoType }">
		                           	 	<input type="hidden" name="checkId" value="${result.id }">
		                           	 </td>
						           </tr> 
						           </tbody>
						       </c:forEach>
                            </table>
                            </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->
</body>
</html>