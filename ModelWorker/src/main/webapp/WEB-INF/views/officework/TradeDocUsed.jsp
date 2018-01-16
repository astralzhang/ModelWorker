<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- <%@ taglib prefix="code" uri="/WEB-INF/tld/code4dropdown.tld"%> --%>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<html>
<head>
	<meta name="menuId" content="TradeDoc"/>
    <title></title>
    <script type="text/javascript">
    
	$(document).ready(function() {
		var arrChangeData = [];
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		
		$("#form1").validate({
			rules: {
			}
		});
		<%-- Checkbox事件 --%>
		$("#table2 tbody input[type='checkbox']").click(function() {
			var rowNo = $(this).closest("tr").index();
			var status = "N";
			if ($(this).prop("checked")) {
				status = "Y";
			}
			var hasData = false;
			$.each(arrChangeData, function(n, value) {
				if (value.rowNo == rowNo && value.status != status) {
					arrChangeData.splice(n, 1);
					hasData = true;
					return false;
				}
			});
			if (hasData == false) {
				arrChangeData.push({rowNo:rowNo,status:status});
			}
		});
		$("#btn-back").on('click', function() {
			window.location.href = "${rc.contextPath}/TradeDoc/list";
		});
 		$("#btn_save").on("click", function(){
 			if($("#form1").valid()){
 				MessageBox.processStart();
	 			var documentAcceptsBean = new Object;
	 			documentAcceptsBean.parentId = $("#id").val();
	 			var acceptList = [];
	 			$('#table2 tbody tr input[type="checkbox"]:checked').each(function(){
	 				var documentAccepts = new Object;
	 				documentAccepts.parentId= $("#id").val();
	 				documentAccepts.levelCode= $(this).closest("tr").find("input[name='levelCode1']:first").val();
	 				documentAccepts.levelName=$(this).closest("tr").find("input[name='levelName1']:first").val();
	 				documentAccepts.magazineCode=$(this).closest("tr").find("input[name='magazineCode1']:first").val();
	 				documentAccepts.magazineName= $(this).closest("tr").find("input[name='magazineName1']:first").val();
	 				documentAccepts.acceptType= $(this).closest("tr").find("input[name='acceptType1']:first").val();
	 				documentAccepts.acceptTypeName= $(this).closest("tr").find("input[name='acceptTypeName1']:first").val();
	 				documentAccepts.magazineYear = $(this).closest("tr").find("select:eq(0)").val();
	 				documentAccepts.period = $(this).closest("tr").find("select:eq(1)").val();
	 				acceptList.push(documentAccepts);
				});
	 			documentAcceptsBean.documentAcceptsList=acceptList;
	 			$.ajax({
					url:"${rc.contextPath}/TradeDoc/saveDocUser",
					type:"post",
					contentType:'application/json',
					dataType:"json",
					data:JSON.stringify(documentAcceptsBean),
					success:function(data){
						if(data.errType == '0'){
							MessageBox.processEnd(data.errMessage, function(){
								window.location.href="${rc.contextPath}/TradeDoc/list";
							});
						} else {
							MessageBox.processEnd(data.errMessage);
						}
					},
					error:function(XMLHttpRequest, textStatus, errorThrown) {
						MessageBox.processEnd("系统错误，详细信息：" + textStatus);
					}
				});
			};
		}); 
		<%-- 下载附件函数 --%>
		var downloadFunc = function() {
			var id = $(this).closest("tr").find("input[type='hidden']").val();
			location.href = "${rc.contextPath}/TradeDoc/download?id=" + id;
		};
		<%-- 添加附件下载事件 --%>
		$("#Attachment tbody input[name='btn_download']").unbind("click");
		$("#Attachment tbody input[name='btn_download']").bind("click", downloadFunc);
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
            <li class="active">公文采用信息</li>
            </ol>

        </section>
        <!-- Main content -->
        <section class="content">
        <div id="content" style="overflow-y:auto;">
            <!-- projectAdd -->
            <div class="box box-info product-info">
                <div class="btn_box">
		          	<div class="search">
	<%--           		<hq:auth act="dutyClassChangeSave"> --%>
		          		<button class="btn_header" id="btn_save" type="button" <c:if test="${data.tradeUnionInfo.tradeLevel != '01' }">disabled style="background:#666666;"</c:if>>采用</button>
	<%-- 	          	</hq:auth> --%>
		          	</div>
		          	<div class="line_left"></div>
		          	<div class="btn-group">
		          		<button class="btn_header" id="btn-back" type="button">返回</button>
		          	</div>
         		 </div>
<!--                 <div class="box-header"> -->
<!--                     <h2 class="box-title">公文采用信息</h2> -->
<!--                 </div> -->
                <div class="box-body">
                	<form id="form1" enctype="multipart/form-data" method="post">
                    <table class="table table-bordered">
                        <tr>
                            <td>标题：</td>
                            <td ><input type="text" id="Title" name="Title" readonly  value="${data.documents.title}" class="form-control input-sm fl w20" >
                           		<input type="hidden" name="id" id="id" value="${data.documents.id}" />  
                            </td>
                        </tr>
                         <tr>
                            <td>内容：</td>  
                            <td align="left" ><textarea Style="width:600Px" name="Description" cols="30" rows="4" readonly >${data.documents.description}</textarea>
                            
<%--                             <input type="textarea" id="Description" name="Description" readonly value="${data.dutyClassChange.Description}" class="form-control input-sm fl w20" > --%>
                            </td>
                        </tr>
                         <tr>
                            <td>信息分类：</td>  
                            <td >
								<select id="infoType" name="infoType"  class="form-control2" disabled="true">
								<c:forEach items="${data.infomationTypeList}" var="InfomationType">
									<option value="${InfomationType.code }" <c:if test="${data.documents.infoType eq InfomationType.code }">selected</c:if>>${InfomationType.name }</option>
								</c:forEach>
								</select>
<%--                             <input type="text" id="InfoType" name="InfoType" readonly value="${data.documents.infoType}" class="form-control input-sm fl w20" > --%>
                           		</td>
                       </tr>
                         <tr>
                            <td>附件信息：</td>  
                            <td>
                            	<table class="table table-bordered "  id="Attachment">
                        		<tr>
		                           <th>附件名称</th>
		                           <th>上传时间</th>
		                           <th>操作</th>
								</tr>
			                      <tbody>
			                      <c:forEach items="${data.docAttachList}" var="atta">
			                      	<tr>
			                      		<td><input type="hidden" value="${atta.id}">${atta.fileName }</td>
			                      		<td>${atta.uploadTime }</td>
			                      		<td><input type="button" value="下载" name="btn_download"></td>
			                      	</tr>
			                      </c:forEach>
			                      </tbody>
                        		</table>
                            </td>
                       </tr>
                    </table>
                    </form>
                </div>
            </div>
            <br>
            <div class="box box-warning cost-info">
                <div class="box-header">
                    <h2 class="box-title">录用情况</h2>
                </div>
                <div class="box-body" style=" overflow:scroll; ">
                    <table class="table table-bordered" id="table2">
                    	<thead>
	                        <tr>
	                            <th>所属市区</th>
	                            <th>期刊名称</th>
	                            <th>采用形式</th>
	                            <th>采用年</th>
	                            <th>采用期次</th>
	                            <th>是否采用</th>
	                        </tr>
                        </thead>
                        <tbody>
                      		<c:forEach items="${data.docAcceptList}" var="result2" >
			                   <tr>
					             <td>${result2.levelName}</td>
					             <td>${result2.magazineName}</td>
					             <td>${result2.acceptTypeName}</td>
					             <td align="center">
					             <c:if test="${not empty result2.periods }">
					             	<select class="form-control" style="width:100px;" <c:if test="${data.tradeUnionInfo.tradeLevel != '01' }">disabled</c:if>>
					            	<c:forEach items="${data.yearList}" var="year">
					             		<option value="${year }" <c:if test="${year eq result2.magazineYear }">selected</c:if>>${year }年</option>
					             	</c:forEach>
					             	</select>
					             </c:if>
					             </td>
					             <td align="center">
					             	<c:if test="${not empty result2.periods }">
					             	<select class="form-control" style="width:100px;" <c:if test="${data.tradeUnionInfo.tradeLevel != '01' }">disabled</c:if>>
					             	<c:forEach var="i" begin="1" end="${result2.periods }" step="1">
					             	<option value="${i }" <c:if test="${i eq result2.period }">selected</c:if>>${i }期</option>
					             	</c:forEach>
					             	</select>
					             	</c:if>
					             </td>
					             <td><input type="checkbox" name="selectDoc" <c:if test="${result2.status eq 'Y' }"> checked</c:if> <c:if test="${data.tradeUnionInfo.tradeLevel != '01' }">disabled</c:if>>
					             	<input type="hidden" id="levelCode1" name="levelCode1" value="${result2.levelCode}" >
					             	<input type="hidden" id="levelName1" name="levelName1" value="${result2.levelName}" >
					             	<input type="hidden" id="magazineCode1" name="magazineCode1" value="${result2.magazineCode}" >
					             	<input type="hidden" id="magazineName1" name="magazineName1" value="${result2.magazineName}" >
					             	<input type="hidden" id="acceptType1" name="acceptType1" value="${result2.acceptType}" >
					             	<input type="hidden" id="acceptTypeName1" name="acceptTypeName1" value="${result2.acceptTypeName}" >
					              </td>
			                   </tr>
			                </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div><!-- /.cost-info -->
         </div>
        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->
</body>
</html>