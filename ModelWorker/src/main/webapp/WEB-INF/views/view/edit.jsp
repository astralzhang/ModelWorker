<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hq" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta name="menuId" content="${viewBean.no }"/>
    <title></title>
    <script type="text/javascript" src="${rc.contextPath }/js/view/jquery.freezeheader.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/js/view/mydetail.js"></script>
    <script type="text/javascript">
	$(function() {
		if ("${errType}" != "0") {
			MessageBox.info("${errMessage}", function() {
				location.href = "${rc.contextPath}/view/init/${viewBean.no}";
			});
			return;
		}
		//画面显示
		var headScript = ${viewBean.headScript};
		$("#headerTable").mydetail(headScript, ${viewBean.detailScript}, "${rc.contextPath}", "${viewBean.no}", "${id}", ${data}, ${attachment}, true);
		$('.date').datepicker({
			 format: 'yyyymmdd'
			}
		);
		$("#form1").validate({
			rules: {
				deptName:"required",
				overTimeDate:"required",
				overTimeStart:"required",
				overTimeEnd:"required",
				overTimeHours:"required",
        		overTimeReason:{
					maxlength:200
				},
				remark:{
					maxlength:200
				}
			}
		});
	});
</script>
<style type="text/css">
	#mytable_props table {
			
	}
	#mytable_props th {
		letter-spacing: 2px;
		text-align: left;
		padding: 6px;
		background: #ddd;
	}
	#mytable_props td {
		background: #fff;
		padding: 6px;
	}
	.tabContent{margin:0;padding:0;border:1px solid #ccc;display:none;}
	.tab{margin:0;padding:0;list-style:none;width:200px;overflow:hidden;}
	.tab li{float:left;width:60px;height:30px;background:#ccc;color:#fff;border:1px solid red; text-align:center;line-height:30px;cursor:pointer; }
	.on{display:block;}
	.tab li.cur{background:blue;}
</style>
</head>
<body>
	<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="bb"/>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <ol class="breadcrumb">
                <li><a href="${rc.contextPath}/overTimeManage/init"><i class="fa fa-folder"></i>${module.name }</a></li>
                <li class="active">${item.itemName }</li>
            </ol>

        </section>
        <!-- Main content -->
        <section class="content">
            <!-- projectAdd -->
            <div id="content" style="overflow-y:auto;overflow-x:hidden;">
	            <div id="header" class="box box-info product-info" style="overflow-x:auto;">
	                            <div class="btn_box">
			            <div class="search">
			            <hq:auth act="Add" view="${viewBean.no }">
			          		<button class="btn_header" id="btn_add" type="button">新增</button>
			          	</hq:auth>
			          	</div>
			          	<div class="search">
			          	<hq:auth act="Save" view="${viewBean.no }">
			       			<button class="btn_header" id="btn_save" type="button">保存</button>
			       		</hq:auth>
			          	</div>
			          	<div class="search">
			          	<hq:auth act="Submit" view="${viewBean.no }">
			       			<button class="btn_header" id="btn_submit" type="button">提交</button>
			       		</hq:auth>
			       		<hq:auth act="Audit" view="${viewBean.no }">
			       			<button class="btn_header" id="btn_audit" type="button">审核</button>
			       		</hq:auth>
			          	</div>
			          	<div class="search">
			          		<hq:auth act="Upload" view="${viewBean.no}">
			          			<button class="btn_header" id="btn_file" type="button">附件</button>
			          		</hq:auth>
			          	</div>
			          	<div class="line_left"></div>
			          	<div class="btn-group">
				          	<button class="btn_header" id="btn-back" type="button">返回</button>
			          	</div>
			          </div>
<!-- 	                <div class="box-header"> -->
<%-- 	                    <h2 class="box-title">${viewBean.name }</h2> --%>
<!-- 	                    <span style="float:right;margin-right:20px;color:blue;font-weight:900;font-style:italic;font-size:25px;" id="dataStatus"></span> -->
<!-- 	                    <span style="float:right;margin-right:100px;" id="attachmentShow"></span>                     -->
<!-- 	                </div> -->
	                <div class="box-body">
	                	<form id="form1" enctype="multipart/form-data" method="post">
	                	<input type="hidden" id="id" value="${id }">
	                    <table id="headerTable" width="100%">
	                    </table>
	                    </form>
	                </div>
	            </div><!-- /.overTime-info -->
	            <br>
	            <div id="detail" class="box box-warning cost-info">
	            <%-- 
	                <div class="box-header">
	                    <h2 class="box-title">${voucherName }</h2>
	                </div>
	            --%>
	                <%--
	                <ul class="tab">
					      <li>最新</li>
					      <li class="cur">热门</li>
					      <li>新闻</li>
					</ul>
					<div class="tabContent">
						<table class="table table-hover" >
							<tr>
								<td>表体1</td>
							</tr>
	                    </table>
					</div>
					<div class="tabContent">
						<table class="table table-hover" >
							<tr>
								<td>表体2</td>
							</tr>
	                    </table>
					</div>
					<div class="tabContent">
						<table class="table table-hover" >
							<tr>
								<td>表体3</td>
							</tr>
	                    </table>
					</div>
					--%>
	            </div><!-- /.cost-info -->
	        </div>
        </section><!-- /.content -->
		<div class="modal fade" id="myOpenWinModal" aria-labelledby="myOpenWinModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myOpenWinModalLabel">弹窗</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                            	<textarea id="listSql" class="form-control input-sm fl w5"></textarea>
                            	<input type="checkbox" id="listType">是否存储过程
                            	<hr />
                                <input type="text" id="listFieldName" placeholder="字段名称" class="form-control input-sm fl w15">&nbsp;
                                <input type="text" id="listFieldNo" placeholder="字段编码" class="form-control input-sm fl w15">
                                <select id="listEditor">
                                	<option value="inputEditor">单行文本框</option>
									<option value="mutilInputEditor">多行文本框</option>
									<option value="dateEditor">日期</option>
									<option value="dataDropdown">下拉框</option>
									<option vlaue="dataSelect">选择框</option>
                                </select>
                                <input type="text" id="listEditor" placeholder="条件编辑" class="form-control input-sm fl w15">
                                <div class="search">
                                    <button type="button" class="btn btn-block btn-success fa fa-search" id="btn-dept-search"><span class="search-btn">查找</span></button>
                                </div>
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>选择</th>
	                                    <th>部门编号</th>
	                                    <th>部门名称</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" id="btn-myOpenWin-confirm">确定</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- 附件一栏 --%>
        <div class="modal fade" id="myAttachmentModal" aria-labelledby="myAttachmentModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myAttachmentModalLabel">附件</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;">
                            <form role="form">
                            </form>
                        </div>
                        <div style="margin-top:20px;max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-bordered table-condensed">
                            	<thead>
	                                <tr>
	                                    <th>文件名</th>
	                                    <th>文件类型</th>
	                                    <th>删除</th>
	                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myAttachmentViewModal" aria-labelledby="myAttachmentViewModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myAttachmentViewModalLabel">附件</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-left: 0px;overflow-y:auto;">
                        </div>
                        <div id="PageButton" style="display:none; padding-left:45%">
                        	<input type="hidden" id="PageNo" value="1">
                        	<button type="button" id="PrevPage" class="btn btn-default">上一页</button>
                        	<span id="CurrentPageNo">1</span>/<span id="TotalPages">1</span>
                        	<button type="button" id="NextPage" class="btn btn-default">下一页</button>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.content-wrapper -->
    <!-- 产品分类选择 -->
</body>
</html>