<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="DutyClassDayResult"/>
	<script src="${rc.contextPath }/js/view/mymenutable.js"></script>
	<script src="${rc.contextPath }/js/view/myfixedtable.js"></script>
	<script type="text/javascript">	
	$(function(){
		var myMenuTable = null;
		//固定1行3列
		$("#table_scroll").myfixedtable({row:1,col:3});
		$(document).click(function (e) {
			if (e.which == 3) {
				//兼容火狐
				return;
			}
			$("#rightmenu").css({"display":"none"});
		});
		var searchDayResult = function(dutyClass, yearMonth) {
			MessageBox.processStart();
			$.ajax({
			  url:"${rc.contextPath}/dutyClassDayResult/listData",
			  type:"post",
			  dataType:"json",
			  data:{
				  yearMonth :yearMonth,
				  dutyClass:dutyClass
			  },
			  success:function(data){
				  if(data.errType == "0"){
					  var tbody = "";
					  var workHours = [];
					  $.each(data.data.DutyClassDayResult, function(n, dutyClass) {
						  var workHour = 0;
						  tbody += "<tr>";
						  tbody += "<td>" + dutyClass.dutyClass + "</td>"
						  tbody += "<td>" + dutyClass.dutyClassName + "</td>";
						  tbody += "<td>" + dutyClass.workHours + "</td>";
						  $.each(dutyClass.list, function(m, value) {
							  workHour += value.workHours;
							  if (value.dateProperty == "1") {
								  tbody += "<td ref='1' dateProperty='" + value.dateProperty + "' style='background-color:#FFD700;'>" + value.dutyCode + "</td>";
							  } else if (value.dateProperty == "2") {
								  tbody += "<td ref='1' dateProperty='" + value.dateProperty + "' style='background-color:#FFA500;'>" + value.dutyCode + "</td>";
							  } else {
							  	tbody += "<td ref='1' dateProperty='" + value.dateProperty + "' style='background-color:#FFFFFF;'>" + value.dutyCode + "</td>";
							  }
						  });
						  tbody += "</tr>";
						  workHours.push(workHour);
					  });					  
					  $("#dataTable tbody").html(tbody);
					  var index = 0;
					  $.each(workHours, function(n, value) {
						  //alert($(this));
						  var tr = $("#dataTable tbody tr:eq(" + index + ")");
						  tr.find("td:eq(2)").html(value);
						  index++;
					  });
					  //创建右键菜单
					  var lis = "";
					  $.each(data.data.DutyBasic, function(n, value) {
						  lis += "<li><a href='#' ref='" + value.code + "'>" + value.name + "(" + value.code + ")</a></li>";
					  });
					  $("#rightmenu ul").html(lis);
					  //设定隐藏年月
					  $("#yearMonth").val(yearMonth);
				  }
				  MessageBox.processClose();
				  $("#myModal1").modal("hide");
			  },
			  error:function(XMLHttpRequest, data) {
				  MessageBox.processEnd("系统错误，详细信息：" + data);
			  }
		  });
		};
		//检索
		$("#btn-search").click(function() {
			if ($("#dutyClass").val() == "") {
				MessageBox.info("请输入班组！", function() {
					$("#dutyClass").focus();
				});				
				return;
			}
			var seachData = function() {
				var year = $("#year").val();
				var month = $("#month").val();
				var yearMonth = "";
				if (parseInt(month) < 10) {
					yearMonth = year + "0" + month;
				} else {
					yearMonth = year + month;
				}
				searchDayResult($("#dutyClass").val(), yearMonth);
			};
			if (myMenuTable != null) {
				var editedData = myMenuTable.getEditData();
				if (editedData != null && editedData.length > 0) {
					MessageBox.confirm("画面数据已修改，您是否放弃本次修改！", function () {
						seachData();
					});
					return;
				}
			}
			seachData();
		});
		//修改按钮
		$("#btn_edit").click(function() {
			myMenuTable = $("#dataTable").mymenutable("rightmenu", {property:"dateProperty", color:[{key:'0',bgcolor:"#FFFFFF"},{key:"1",bgcolor:"#FFD700"},{key:"2",bgcolor:"#FFA500"}]});
		});
		//保存按钮
		$("#btn_save").click(function() {
			if (myMenuTable == null) {
				MessageBox.info("请点击修改按钮，修改排班数据后保存！");
				return;
			}
			if (myMenuTable != null) {
				var editedData = myMenuTable.getEditData();
				if (editedData == null || editedData.length <= 0) {
					MessageBox.info("没有需要保存的数据！");
					return;
				}
				var postData = [];
				$.each(editedData, function(n, value) {
					var rowNo = value.row;
					var colNo = value.col;
					var tr = $("#dataTable tbody tr:eq(" + rowNo + ")");
					var dutyClass = $(tr).find("td:eq(0)").html();
					tr = $("#dataTable thead tr:eq(0)");
					var day = $(tr).find("th:eq(" + value.col + ")").html();
					var dutyDate = "";
					if (parseInt(day) < 10) {
						dutyDate = $("#yearMonth").val() + "0" + day;
					} else {
						dutyDate = $("#yearMonth").val() + day;
					}
					var tempData = {dutyClass:dutyClass,dutyDate:dutyDate,dutyCode:value.newDuty,dateProperty:value.newProperty,shiftSource:"2"};
					postData.push(tempData);
				});
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/dutyClassDayResult/save",
					type:"post",
					dataType:"json",
					contentType: "application/json",
					data:JSON.stringify(postData),
					success:function(data) {
						if (data.errType == "1") {
							MessageBox.processEnd(data.errMessage);
							return;
						}
						MessageBox.processEnd("保存成功！");
						myMenuTable.clearData();
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			}
		});
		//生成排班
		$("#btn_create").click(function() {
			if (myMenuTable != null) {
				var editedData = myMenuTable.getEditData();
				if (editedData != null && editedData.length > 0) {
					MessageBox.confirm("画面数据已修改，您是否放弃本次修改！", function () {
						var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
						modal.show();
					});
					return;
				}
			}
			var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
			modal.show();
		});
		//生成排班确定
		$("#btn-duty-confirm").click(function() {
			var createData = function(dutyClass, yearMonth, replaceManual) {
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/dutyClassDayResult/create",
					type:"post",
					dataType:"json",
					data:{
						yearMonth :yearMonth,
						dutyClass:dutyClass,
						replaceManual:replaceManual
					},
					success:function(data) {
						if (data.errType == "1") {
							MessageBox.processEnd(data.errMessage);
							return;
						}
						MessageBox.processEnd("生成成功！", function () {
							searchDayResult($("#dutyClassCode").val(), yearMonth);
						});
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			};
			if ($("#dutyClassCode").val() == "") {
				MessageBox.info("请输入班组！");
				return;
			}
			var year = $("#dutyYear").val();
			var month = $("#dutyMonth").val();
			var yearMonth = "";
			if (parseInt(month) < 10) {
				yearMonth = year + "0" + month;
			} else {
				yearMonth = year + month;
			}
			var replaceManual = "N";
			if ($("#replaceManual").prop("checked")) {
				replaceManual = "Y";
			}
			createData($("#dutyClassCode").val(), yearMonth, replaceManual);
		});
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
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
            <li><a href="#"><i class="fa fa-dashboard"></i>班组排班</a></li>
            <li class="active">班组排班计划</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          	<hq:auth act="DutyClassDayResultSave">
      			<button class="btn_header" id="btn_save" type="button">保存</button>
      		</hq:auth>
      		<hq:auth act="DutyClassDayResultEdit">
      			<button class="btn_header" id="btn_edit" type="button">修改</button>
      		</hq:auth>
      		<hq:auth act="DutyClassDayResultCreate">
      			<button class="btn_header" id="btn_create" type="button">生成排班</button>
      		</hq:auth>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
       		<hq:auth act="DutyClassDayResultSearch">
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
                <form role="form" id="searchForm" method="post"  action="${rc.contextPath}/dutyClassDayResult/list">
                	<input type="hidden" id="yearMonth">
                	<div class="fl">
						<label for="year" class="label-header label-header">年：</label>
						<select id="year" class="form-control input-sm fl w15"">
						 <c:forEach items="${year }" var="sYear">
                        	<option value="${sYear.data }" <c:if test="${sYear.flag eq 1 }">selected</c:if>>${sYear.data }</option>
                        </c:forEach>
						</select>
	                </div>
	                <div class="fl">
                  		<label for="month" class="label-header label-header">月：</label>
	                  	<select id="month" class="form-control input-sm fl w15"">
						   	<option value="1"  selected>1月</option>
						   	<option value="2" >2月</option>
						   	<option value="3" >3月</option>
						   	<option value="4" >4月</option>
						   	<option value="5" >5月</option>
						   	<option value="6" >6月</option>
						   	<option value="7" >7月</option>
						   	<option value="8" >8月</option>
						   	<option value="9" >9月</option>
						   	<option value="10" >10月</option>
						   	<option value="11" >11月</option>
						   	<option value="12" >12月</option>
						</select>
                  	</div>
                  	<div class="fl">
						<label for="dutyClass" class="label-header label-header">班组：</label>
						<input type="text" id="dutyClass" name="dutyClass" class="form-control input-sm fl w15" value="${dutyClass }">
	                </div>
	                <input type="hidden" id="yearMonth" name="yearMonth"/>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">班组排班计划</h3>
                            <hq:auth act="PersonDayResultEdit">
                            <span style="float:right;margin-right:20px;color:blue;font-weight:900;font-style:italic;font-size:20px;">点击修改可右键手动调整排班</span>
                            </hq:auth>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding" style="overflow:hidden;">
                        <div id="table_scroll" style="overflow-x:auto;">
                            <table class="table table-hover" id="dataTable">
                            	<thead>
					            	<tr>
					                   <th>班组编码</th>
					                   <th>班组名称</th>
			                           <th>计划工时</th>
			                           <th>1</th>
			                           <th>2</th>
			                           <th>3</th>
			                           <th>4</th>
			                           <th>5</th>
			                           <th>6</th>
			                           <th>7</th>
			                           <th>8</th>
			                           <th>9</th>
			                           <th>10</th>
			                           <th>11</th>
			                           <th>12</th>
			                           <th>13</th>
			                           <th>14</th>
			                           <th>15</th>
			                           <th>16</th>
			                           <th>17</th>
			                           <th>18</th>
			                           <th>19</th>
			                           <th>20</th>
			                           <th>21</th>
			                           <th>22</th>
			                           <th>23</th>
			                           <th>24</th>
			                           <th>25</th>
			                           <th>26</th>
			                           <th>27</th>
			                           <th>28</th>
			                           <th>29</th>
			                           <th>30</th>
			                           <th>31</th>
									</tr>
								</thead>
						      	<tbody>
						        </tbody>
                            </table>
                        </div>
                        </div><!-- /.box-body -->
                    </div><!-- /.box -->
                </div>
            </div>
            <div class="panel panel-default" style="position:absolute;display:none;z-index:999;padding:0;margin:0;line-height:0" id="rightmenu">
   				<div class="panel-body" style="margin:0;padding:0;">
		            <ul class="nav nav-pills nav-stacked">
		                <li><a href="#">听说我是第一个菜单项</a></li>
		                <li><a href="#">我是第二个菜单项</a></li>
		                <li><a href="#">我是最后一个咯</a></li>
		            </ul>
			    </div>
			</div>
        </section><!-- /.content -->
        <%-- 排班选择 --%>
        <div class="modal fade" id="myModal1" aria-labelledby="myModalLabel1">
            <div class="modal-dialog modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel1">排班设定</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form" id="dutySearchForm">
	                            <table>
	                            	<tr>
	                            		<td><label for="dutyYear" class="label-header label-header">年：</label></td>
	                            		<td>
	                            			<select id="dutyYear" class="form-control input-sm fl w15">
											<c:forEach items="${year }" var="sYear">
					                        	<option value="${sYear.data }" <c:if test="${sYear.flag eq 1 }">selected</c:if>>${sYear.data }</option>
					                        </c:forEach>
											</select>
										</td>
										<td><label for="dutyMonth" class="label-header label-header">月：</label></td>
										<td>
											<select id="dutyMonth" class="form-control input-sm fl w15">
											   	<option value="1"  selected>1月</option>
											   	<option value="2" >2月</option>
											   	<option value="3" >3月</option>
											   	<option value="4" >4月</option>
											   	<option value="5" >5月</option>
											   	<option value="6" >6月</option>
											   	<option value="7" >7月</option>
											   	<option value="8" >8月</option>
											   	<option value="9" >9月</option>
											   	<option value="10" >10月</option>
											   	<option value="11" >11月</option>
											   	<option value="12" >12月</option>
											</select>
										</td>
										<td><label for="dutyClassCode" class="label-header label-header">班组：</label></td>
										<td><input type="text" id="dutyClassCode" name="dutyClassCode" class="form-control input-sm fl w15" value="${dutyClass }"></td>
	                            	</tr>
	                            	<tr>
	                            		<td><input type="checkbox" id="replaceManual">覆盖手动排班</td>
	                            	</tr>
	                            </table>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table id="userTable" class="table table-bordered table-condensed">
                            	<thead>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-duty-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
</body>
</html>