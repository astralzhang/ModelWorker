<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="PersonDayResult"/>
	<script src="${rc.contextPath }/js/view/mymenutable.js"></script>
	<script src="${rc.contextPath }/js/view/myfixedtable.js"></script>
	<script type="text/javascript">	
	$(function(){
		var myMenuTable = null;
		//固定1行5列
		$("#table_scroll").myfixedtable({row:1,col:5});
		$(document).click(function (e) {
			if (e.which == 3) {
				//兼容火狐
				return;
			}
			$("#rightmenu").css({"display":"none"});
		});
		var searchDayResult = function(dutyDept, startDutyPerson, endDutyPerson, yearMonth) {
			MessageBox.processStart();
			$.ajax({
			  url:"${rc.contextPath}/personDayResult/listData",
			  type:"post",
			  dataType:"json",
			  data:{
				  yearMonth :yearMonth,
				  dutyDept:dutyDept,
				  startDutyPerson:startDutyPerson,
				  endDutyPerson:endDutyPerson
			  },
			  success:function(data){
				  if(data.errType == "0"){
					  //设定标题
					  var thead = "<tr><th>部门</th><th>班组</th><th>人员编码</th><th>人员名称</th><th>计划工时</th>";
					  $.each(data.data.title, function(n, title) {
						  thead += "<th>" + title.date + "<br/>";
						  switch(title.week) {
						  case 1:
							  thead += "星期日";
							  break;
						  case 2:
							  thead += "星期一";
							  break;
						  case 3:
							  thead += "星期二";
							  break;
						  case 4:
							  thead += "星期三";
							  break;
						  case 5:
							  thead += "星期四";
							  break;
						  case 6:
							  thead += "星期五";
							  break;
						  case 7:
							  thead += "星期六";
							  break;
						  default:
							  break;
						  }
						  thead += "<input type='hidden' value='" + title.dutyDate + "'></th>";
					  });
					  thead += "</tr>";
					  $("#dataTable thead").html(thead);
					  var tbody = "";
					  $.each(data.data.PersonDayResult, function(n, dutyPerson) {
						  tbody += "<tr>";
						  tbody += "<td>" + dutyPerson.deptCode + "</td>"
						  tbody += "<td>" + dutyPerson.dutyClass + "</td>";
						  tbody += "<td>" + dutyPerson.userNo + "</td>"
						  tbody += "<td>" + dutyPerson.userName + "</td>";
						  tbody += "<td>" + dutyPerson.workHours + "</td>";
						  $.each(dutyPerson.list, function(m, value) {
							  if (value == undefined || value == null || value === "") {
								  tbody += "<td ref='1' dateProperty='0' style='background-color:#FFFFFF;'></td>";
								  return true;
							  }
							  if (value.dateProperty == "1") {
								  tbody += "<td ref='1' dateProperty='" + value.dateProperty + "' style='background-color:#FFD700;'>" + value.dutyCode + "</td>";
							  } else if (value.dateProperty == "2") {
								  tbody += "<td ref='1' dateProperty='" + value.dateProperty + "' style='background-color:#FFA500;'>" + value.dutyCode + "</td>";
							  } else {
							  	tbody += "<td ref='1' dateProperty='" + value.dateProperty + "' style='background-color:#FFFFFF;'>" + value.dutyCode + "</td>";
							  }
						  });
						  tbody += "</tr>";
					  });
					  $("#dataTable tbody").html(tbody);
					  //创建右键菜单
					  var lis = "";
					  $.each(data.data.DutyBasic, function(n, value) {
						  lis += "<li><a href='#' ref='" + value.code + "'>" + value.name + "</a></li>";
					  });
					  $("#rightmenu ul").html(lis);
					  //设定隐藏年月
					  $("#yearMonth").val(yearMonth);
					  MessageBox.processClose();
				  } else {
					  MessageBox.processEnd(data.errMessage);
				  }
				  $("#myModal1").modal("hide");
			  },
			  error:function(XMLHttpRequest, data) {
				  MessageBox.processEnd("系统错误，详细信息：" + data);
			  }
		  });
		};
		//检索
		$("#btn-search").click(function() {
			//RunClientExe.run("notepad.exe");
			//return;
			if ($("#dutyDept").val() == "" && $("#startDutyPerson").val() == "" && $("#endDutyPerson").val() == "") {
				MessageBox.info("请输入部门或需要排班人员编码！");				
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
				searchDayResult($("#dutyDept").val(), $("#startDutyPerson").val(), $("#endDutyPerson").val(), yearMonth);
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
					var personNo = $(tr).find("td:eq(2)").html();
					tr = $("#dataTable thead tr:eq(0)");
					//var day = $(tr).find("th:eq(" + value.col + ")").html();
					var dutyDate = $(tr).find("th:eq(" + value.col + ")").find("input[type='hidden']").val();
					var tempData = {userNo:personNo,dutyDate:dutyDate,dutyCode:value.newDuty,dateProperty:value.newProperty,shiftSource:"2"};
					postData.push(tempData);
				});
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/personDayResult/save",
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
			var createData = function(dutyDept, yearMonth, startPersonNo, endPersonNo, replaceManual) {
				MessageBox.processStart();
				$.ajax({
					url:"${rc.contextPath}/personDayResult/create",
					type:"post",
					dataType:"json",
					data:{
						yearMonth :yearMonth,
						dutyDept:dutyDept,
						startPersonNo:startPersonNo,
						endPersonNo:endPersonNo,
						replaceManual:replaceManual
					},
					success:function(data) {
						if (data.errType == "1") {
							MessageBox.processEnd(data.errMessage);
							return;
						}
						MessageBox.processEnd("生成成功！", function () {
							searchDayResult($("#dutyDeptCode").val(), $("#startPersonNo").val(), $("#endPersonNo").val(), yearMonth);
						});
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			};
			if ($("#dutyDeptCode").val() == "" && $("#startPersonNo").val() == "" && $("#endPersonNo").val() == "") {
				MessageBox.info("请输入部门或人员编码！");
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
			createData($("#dutyDeptCode").val(), yearMonth, $("#startPersonNo").val(), $("#endPersonNo").val(), replaceManual);
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
            <li><a href="#"><i class="fa fa-dashboard"></i>人员排班</a></li>
            <li class="active">人员排班计划</li>
          </ol>
          <div class="btn_box">
          	<div class="search">
          	<hq:auth act="PersonDayResultSave">
      			<button class="btn_header" id="btn_save" type="button">保存</button>
      		</hq:auth>
      		<hq:auth act="PersonDayResultEdit">
      			<button class="btn_header" id="btn_edit" type="button">修改</button>
      		</hq:auth>
      		<hq:auth act="PersonDayResultCreate">
      			<button class="btn_header" id="btn_create" type="button">生成排班</button>
      		</hq:auth>
          	</div>
          	<div class="line_left"></div>
          	<div class="btn-group">
       		<hq:auth act="PersonDayResultSearch">
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
						<label for="year" class="label-header label-header">考勤年：</label>
						<select id="year" class="form-control input-sm fl w15"">
						 <c:forEach items="${year }" var="sYear">
                        	<option value="${sYear.data }" <c:if test="${sYear.flag eq 1 }">selected</c:if>>${sYear.data }</option>
                        </c:forEach>
						</select>
	                </div>
	                <div class="fl">
                  		<label for="month" class="label-header label-header">考勤月：</label>
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
						<label for="dutyClass" class="label-header label-header">部门：</label>
						<input type="text" id="dutyDept" name="dutyDept" class="form-control input-sm fl w15" value="${dutyDept }">
	                </div>
	                <div class="fl">
						<label for="dutyClass" class="label-header label-header">人员编码：</label>
						<input type="text" id="startDutyPerson" name="startDutyPerson" class="form-control input-sm fl w15" value="${startDutyPerson }">
						<label style="float:left;">-</label>
						<input type="text" id="endDutyPerson" name="endDutyPerson" class="form-control input-sm fl w15" value="${endDutyPerson }">
	                </div>
	                <input type="hidden" id="yearMonth" name="yearMonth"/>
                </form>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">人员排班计划</h3>
                            <hq:auth act="PersonDayResultEdit">
                            <span style="float:right;margin-right:20px;color:blue;font-weight:900;font-style:italic;font-size:20px;">点击修改可右键手动调整排班</span>
                            </hq:auth>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding" style="overflow:hidden;">
                        <div id="table_scroll" style="overflow:auto;">
                            <table class="table table-hover" id="dataTable">
                            	<thead>
					            	<tr>
					                   <th>部门</th>
					                   <th>班组</th>
					                   <th>人员编码</th>
					                   <th>人员姓名</th>
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
	                            	</tr>
	                            	<tr>
	                            		<td><label for="dutyDeptCode" class="label-header label-header">部门：</label></td>
										<td><input type="text" id="dutyDeptCode" name="dutyDeptCode" class="form-control input-sm fl w15" value="${dutyDeptode }"></td>
	                            	</tr>
	                            	<tr>
	                            		<td><label for="startPersonNo" class="label-header label-header">人员编码</label></td>
	                            		<td><input type="text" id="startPersonNo" class="form-control input-sm fl w15" value="${startPersonNo }"></td>
	                            		<td>-</td>
	                            		<td><input type="text" id="endPersonNo" class="form-control input-sm fl w15" value="${endPersonNo }"></td>
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