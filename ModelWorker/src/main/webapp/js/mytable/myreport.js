(function($){
	var MyReport = function(ele, con, appName, reportNo) {
		this.$element = ele;
		this.defaults = {
		};
		this.config = $.extend({}, this.defaults, con);
		this.appName = appName;
		this.reportNo = reportNo;
		//alert(JSON.stringify(this.config));
	};
	MyReport.prototype = {
		init : function() {
			//alert("init");
			$("#sheet table tbody").html("");
			$("#sheet table tbody input[type='checkbox']").unbind("click");
			$("#sheet table tbody input[type='checkbox']").bind("click", {myreport:this}, this.show_mapping);
			//设定数据源
			$("#btn-source").unbind("click");
			$("#btn-source").bind("click", {myreport:this}, this.set_source);
			//设定查询条件
			$("#btn-condition").unbind("click");
			$("#btn-condition").bind("click", {myreport:this}, this.set_condition);
			//设定显示项目
			$("#btn-show-field").unbind("click");
			$("#btn-show-field").bind("click", {myreport:this}, this.set_show_field);
			//上传模板
			$("#btn-upload").unbind("click");
			$("#btn-upload").bind("click", {myreport:this}, this.set_template);
			return this;
		},
		set_source : function(event) {
			var myReport = event.data.myreport;
			var config = myReport.config;
			$("#myReportModalLabel").html("数据源设定");
			var objForm = $("#myReportModal form");
			objForm.html("");
			var objTable = $("<table width='100%'></table>").appendTo(objForm);
			var tr = $("<tr></tr>").appendTo(objTable);
			var td = $("<td colspan='5'></td>").appendTo(tr);
			var objSql = $('<textarea placeholder="SQL文或存储过程" class="form-control input-sm fl w5" style="width:100%;"></textarea>').appendTo(td);
			td = $("<td></td>").appendTo(tr);
			$('<label>是否存储过程</label>').appendTo(td);
			var objType = $('<input type="checkbox">').appendTo(td);
			tr = $("<tr></tr>").appendTo(objTable);
			$("<td><label>执行时点</label></td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objRunTime = $('<select><option value="0">立即执行</option><option value="1" selected>查询后执行</option></select>').appendTo(td);
			$("<td><label>目标数据库</label></td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
			$("<td><label>固定行数</label></td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objFixRow = $("<input type='text' placeholder='固定行数' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(objTable);
			$("<td><label>固定列数</label></td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objFixCol = $("<input type='text' placeholder='固定列数' class='form-control input-sm fl w15'>").appendTo(td);
			$("#myReportDetail table thead").html("");
			$("#myReportDetail table tbody").html("");
			$("#btn-report-confirm").unbind("click");
			$("#btn-report-confirm").bind("click", function() {
				//alert(JSON.stringify(config));
				if ($(objSql).val() == null || $(objSql).val() == "") {
					alert("请输入SQL或存储过程！");
					return;
				}
				config.sql = encodeURIComponent($(objSql).val());
				if ($(objType).prop("checked")) {
					config.type = "P";
				} else {
					config.type = "S";
				}
				config.runTime = $(objRunTime).val();
				config.target = $(objTarget).val();
				config.fixRows = $(objFixRow).val() == "" ? "0" : $(objFixRow).val();
				config.fixCols = $(objFixCol).val() == "" ? "0" : $(objFixCol).val();
				//alert(JSON.stringify(config));
				$("#myReportModal").modal("hide");
			});
			if (config.sql != undefined) {
				$(objSql).val(decodeURIComponent(config.sql));
			}
			if (config.type == "P") {
				$(objType).prop("checked", true);
			} else {
				$(objType).prop("checked", false);
			}
			if (config.runTime != undefined) {
				$(objRunTime).val(config.runTime);
			}
			if (config.target != undefined) {
				$(objTarget).val(config.target);
			}
			if (config.fixRows != undefined) {
				$(objFixRow).val(config.fixRows);
			}
			if (config.fixCols != undefined) {
				$(objFixCol).val(config.fixCols);
			}
			var modal = $('#myReportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();		
		},
		set_condition : function(event) {
			var myReport = event.data.myreport;
			var config = myReport.config;
			$("#myReportModalLabel").html("条件设定");
			var objForm = $("#myReportModal form");
			$(objForm).html("");
			var table = $("<table width='100%'></table>").appendTo(objForm);
			var tr = $("<tr></tr>").appendTo(table);
			$("<td>显示文本</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objViewText = $('<input type="text" placeholder="显示文本" class="form-control input-sm fl w15">').appendTo(td);
			$("<td>字段</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objField = $('<input type="text" placeholder="字段" class="form-control input-sm fl w15">').appendTo(td);
			$("<td>字段别名</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objFieldAlias = $('<input type="text" placeholder="字段别名" class="form-control input-sm fl w15">').appendTo(td);
			tr = $("<tr></tr>").appendTo(table);
			$("<td>字段类型</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objEditType = $('<select id="editType" class="form-control2"></select>').appendTo(td);
			var options = "<option value='inputEditor'>单行输入框</option>";
			options += "<option value='selectEditor'>下拉框</option>";
			options += "<option value='openEditor'>选择框</option>";
			options += "<option value='checkEditor'>CheckBox</option>";
			options += "<option value='mutilInputEditor'>多行输入框</option>";
			options += "<option value='dateEditor'>日期</option>";
			$(objEditType).html(options);
			$("<td>比较方法</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objCompare = $('<select id="editType" class="form-control2"></select>').appendTo(td);
			options = "<option value='eq'>=</option>";
			options += "<option value='gt'>&gt;</option>";
			options += "<option value='get'>&gt;=</option>";
			options += "<option value='lt'>&lt;</option>";
			options += "<option value='let'>&lt;=</option>";
			options += "<option value='like'>包含</option>";
			options += "<option value='leftlike'>左包含</option>";
			options += "<option value='rightlike'>右包含</option>";
			$(objCompare).html(options);
			$("<td>开窗编码</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objOpenWindow = $('<input type="text" placeholder="开窗编码" class="form-control input-sm fl w15" disabled>').appendTo(td);
			tr = $("<tr></tr>").appendTo(table);
			$("<td>目标数据库</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTarget = $('<select id="target" class="form-control2" disabled><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(td);
			$("<td>默认值设定类型</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objDefaultType = $('<select id="defaultType" class="form-control2"><option value="SQL">SQL文</option><option value="PROC">存储过程</option><option value="SCRIPT">脚本</option></select>').appendTo(td);
			$("<td>默认值设定方法</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objDefault = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(table);
			$("<td>默认值目标数据库</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objDefaultTarget = $('<select id="target" class="form-control2"><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(td);
			$("<td>默认值映射字段</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objDefaultField = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(table);
			$("<td>开窗返回函数/SQL文</td>").appendTo(tr);
			td = $("<td colspan='5'></td>").appendTo(tr);
			var objSql = $('<textarea placeholder="开窗返回函数/SQL文" class="form-control" disabled></textarea>').appendTo(td);
			var objRowNo = $('<input type="hidden">').appendTo(objForm);
			//编辑类型选择
			$(objEditType).change(function() {
				if ($(objEditType).val() == "selectEditor") {
					//下拉框
					$(objOpenWindow).prop("disabled", true);
					$(objSql).prop("disabled", false);
					$(objTarget).prop("disabled", false);
				} else if ($(objEditType).val() == "openEditor") {
					//选择框
					$(objOpenWindow).prop("disabled", false);
					$(objSql).prop("disabled", false);
					$(objTarget).prop("disabled", true);
				} else {
					$(objOpenWindow).prop("disabled", true);
					$(objSql).prop("disabled", true);
					$(objTarget).prop("disabled", true);
				}
			});
			//默认值设定类型变更
			$(objDefaultType).change(function() {
				if ($(objDefaultType).val() == "SQL" || $(objDefaultType).val() == "PROC") {
					$(objDefaultTarget).prop("disabled", false);
					$(objDefaultField).prop("readonly", false);
				} else {
					$(objDefaultTarget).prop("disabled", true);
					$(objDefaultField).prop("readonly", true);
				}
			});
			//编辑条件
			var editCondition = function() {
    			if ($(this).prop("checked")) {
    				var ids = [];
    				$("#MyListTableData tbody input[type='checkbox']:checked").each(function() {
    					if ($(this).prop("checked")) {
    						ids.push($(this).val());
    					}
    				});
    				if (ids.length > 1) {
    					MessageBox.info("只能选择一条数据编辑！");
    					$("#myListModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
    					return;
    				}
    				$(objRowNo).val($(this).closest("tr").index());
    				$(objViewText).val($(this).parents("tr").find("td:eq(1)").html());
    				$(objField).val($(this).parents("tr").find("td:eq(2)").html());
    				$(objFieldAlias).val($(this).closest("tr").find("td:eq(3)").html());
    				$(objEditType).val($(this).parents("tr").find("td:eq(4)").html());
    				$(objCompare).val($(this).parents("tr").find("td:eq(5)").html());
    				$(objSql).prop("disabled", true);
					$(objOpenWindow).prop("disabled", true);
					$(objTarget).prop("disabled", true);
    				if ($(objEditType).val() == "selectEditor") {
    					$(objSql).prop("disabled", false);
    					$(objSql).val(decodeURIComponent($(this).parents("tr").find("td:eq(7)").html()));
    					$(objTarget).prop("disabled", false);
    					$(objTarget).val($(this).parents("tr").find("td:eq(8)").html());
    				} else if ($(objEditType).val() == "openEditor") {
    					$(objSql).prop("disabled", false);
    					$(objOpenWindow).prop("disabled", false);
    					$(objSql).val(decodeURIComponent($(this).parents("tr").find("td:eq(7)").html()));
    					$(objOpenWindow).val($(this).parents("tr").find("td:eq(6)").html());
    					$(objTarget).val("L");
    				}
    				var defaultType = $(this).parents("tr").find("td:eq(9)").html();
    				var defaultValue = $(this).parents("tr").find("td:eq(10)").html();
    				var defaultTarget = $(this).parents("tr").find("td:eq(11)").html();
    				var defaultField = $(this).parents("tr").find("td:eq(12)").html();
    				if (defaultType == undefined || defaultType == null || defaultType == "") {
    					$(objDefaultType).val("SQL");
    				} else {
    					$(objDefaultType).val(defaultType);
    				}
    				$(objDefaultType).trigger("change");
    				if (defaultValue == undefined || defaultValue == null || defaultValue == "") {
    					$(objDefault).val("");
    				} else {
    					$(objDefault).val(defaultValue);
    				}
    				if (defaultTarget == undefined || defaultTarget == null || defaultTarget == "") {
    					$(objDefaultTarget).val("L");
    				} else {
    					$(objDefaultTarget).val(defaultTarget);
    				}
    				if (defaultField == undefined || defaultField == null || defaultField == "") {
    					$(objDefaultField).val("");
    				} else {
    					$(objDefaultField).val(defaultField);
    				}
    			} else {
    				$(objRowNo).val("");
    				$(objViewText).val("");
    				$(objField).val("");
    				$(objEditType).val("inputEditor");
    				$(objCompare).val("eq");
    				$(objSql).val("");
    				$(objOpenWindow).val("");
    				$(objTarget).val("L");
    				$(objSql).prop("disabled", true);
					$(objOpenWindow).prop("disabled", true);
					$(objTarget).prop("disabled", true);
					$(objFieldAlias).val("");
					$(objDefaultType).val("SQL");
					$(objDefault).val("");
					$(objDefaultTarget).val("L");
					$(objDefaultTarget).prop("disabled", false);
					$(objDefaultField).val("");
					$(objDefaultField).prop("readonly", false);
    			}
    		};
    		//删除条件
    		var deleteCondition = function() {
    			if ($(objRowNo).val() == $(this).closest("tr").index()) {
    				$(objRowNo).val("");
    			} else if($(objRowNo).val() > $(this).closest("tr").index()) {
    				$(objRowNo).val($(objRowNo).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			$(objViewText).val("");
				$(objField).val("");
				$(objEditType).val("inputEditor");
				$(objCompare).val("eq");
				$(objTarget).val("L");
    		};
    		var addNewRow = function(label, field, alias, editType, compare, openWindowNo, sql, sqlProcFlag, defaultType, defaultValue, defaultTarget, defaultField, tr) {
    			var objTd = $("<td></td>").appendTo(tr);
    			$("<input type='checkbox'>").click(editCondition).appendTo(objTd);
    			//$("<td><input type='checkbox'></td>").appendTo(tr);
				$("<td>" + label + "</td>").appendTo(tr);
				$("<td>" + field + "</td>").appendTo(tr);
				$("<td>" + alias + "</td>").appendTo(tr);
				$("<td>" + editType + "</td>").appendTo(tr);
				$("<td>" + compare + "</td>").appendTo(tr);
				if (openWindowNo == undefined) {
					openWindowNo = "";
				}
				$("<td>" + openWindowNo + "</td>").appendTo(tr);
				if (sql == undefined) {
					sql = "";
				}
				$("<td width='50%' style='word-break:break-all;'>" + sql + "</td>").appendTo(tr);
				if (editType == "selectEditor") {
					$("<td>" + sqlProcFlag + "</td>").appendTo(tr);
				} else {
					$("<td></td>").appendTo(tr);
				}
				if (defaultValue == "") {
					$("<td></td><td></td><td></td><td></td>").appendTo(tr);
				} else {
					$("<td>" + defaultType + "</td>").appendTo(tr);
					$("<td>" + defaultValue + "</td>").appendTo(tr);
					$("<td>" + defaultTarget + "</td>").appendTo(tr);
					$("<td>" + defaultField + "</td>").appendTo(tr);
				}
				$("<td><input type='button' value='删除' class='btn btn-default'></td>").appendTo(tr);
    		}
    		//新增条件
    		var objAdd = $('<input type="button" value="新增↓" class="btn btn-success">').click(function() {
    			if ($(objViewText).val() == null || $(objViewText).val() == "") {
					alert("请输入显示文本！");
					return;
				}
				if ($(objField).val() == null || $(objField).val() == "") {
					alert("请输入数据表字段！");
					return;
				}
				if ($(objEditType).val() == "selectEditor") {
					//下拉框
					if ($(objSql).val() == null || $(objSql).val() == "") {
						alert("请输入下拉框所用SQL文件！");
						return;
					}
				} else if ($(objEditType).val == "openEditor") {
					//选择框
					if ($(objOpenWindow).val() == null || $(objOpenWindow).val() == "") {
						alert("请输入开窗编码！");
						return;
					}
					if ($(objSql).val() == null || $(objSql).val() == "") {
						alert("请输入开窗返回函数！");
						return;
					}
				}
				if ($(objRowNo).val() == null || $(objRowNo).val() == "") {
					//新增
					var tr = $("<tr></tr>").appendTo("#myReportDetail table tbody");
					addNewRow($(objViewText).val(), $(objField).val(), $(objFieldAlias).val(), $(objEditType).val(), $(objCompare).val(), $(objOpenWindow).val(), encodeURIComponent($(objSql).val()), $(objTarget).val(), $(objDefaultType).val(), $(objDefault).val(), $(objDefaultTarget).val(), $(objDefaultField).val(), tr);
				} else {
					//修改
        			var index = $(objRowNo).val();
        			var tr = $("#myReportDetail table tbody tr:eq(" + index + ")");
        			$(tr).html("");
        			addNewRow($(objViewText).val(), $(objField).val(), $(objFieldAlias).val(), $(objEditType).val(), $(objCompare).val(), $(objOpenWindow).val(), encodeURIComponent($(objSql).val()), $(objTarget).val(), $(objDefaultType).val(), $(objDefault).val(), $(objDefaultTarget).val(), $(objDefaultField).val(), tr);
        			$(objRowNo).val("");
				}
				$(objRowNo).val("");
				$(objViewText).val("");
				$(objField).val("");
				$(objFieldAlias).val("");
				$(objEditType).val("inputEditor");
				$(objCompare).val("eq");
				$(objTarget).val("L");
				$(objFieldAlias).val("");
				$(objDefaultType).val("SQL");
				$(objDefault).val("");
				$(objDefaultTarget).val("L");
				$(objDefaultField).val("");
				$(objOpenWindow).val("");
				$(objSql).val("");
				$(objDefaultType).trigger("change");
				//$("#myListModal table tbody input[type='checkbox']").unbind("click");
        		//$("#myListModal table tbody input[type='checkbox']").bind("click", editCondition);
        		$("#myReportDetail table tbody input[type='button']").unbind("click");
        		$("#myReportDetail table tbody input[type='button']").bind("click", deleteCondition);        		
    		}).appendTo(objForm);
    		//条件位置调整上
    		var objUp = $('<input type="button" value="向上" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myReportDetail table tbody input[type='checkbox']").each(function() {
					if ($(this).prop("checked")) {
						ids.push($(this).val());
						index = n;
					}
					n++;
				});
				if (ids.length > 1) {
					MessageBox.info("只能选择一条数据调整位置！");
					return;
				}
				if (index > 0) {
					var tr = $("#myReportDetail table tbody tr:eq(" + index + ")");
					var prevTr = $(tr).prev();
					prevTr.insertAfter(tr);
				}
    		}).appendTo(objForm);
    		$('<input type="button" value="向下" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myReportDetail table tbody input[type='checkbox']").each(function() {
					if ($(this).prop("checked")) {
						ids.push($(this).val());
						index = n;
					}
					n++;
				});
				if (ids.length > 1) {
					MessageBox.info("只能选择一条数据编辑！");
					return;
				}
				var tr = $("#MyListTableData tbody tr:eq(" + index + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
    		}).appendTo(objForm);
			//var sTh = "<tr><th>选择</th><th>显示文本</th><th>数据库字段</th><th>编辑类型</th><th>编辑框</th><th>比较方法</th><th>弹出窗口编码</th><th>返回函数/SQL文</th><th>处理</th></tr>";
    		var sTh = "<tr><th>选择</th><th>显示文本</th><th>数据库字段</th><th>字段别名</th><th>编辑类型</th><th>比较方法</th><th>弹出窗口编码</th><th>返回函数/SQL文</th><th>目标数据库</th><th>默认值设定类型</th><th>默认值设定方法</th><th>默认值设定目标数据库</th><th>默认值映射字段</th><th>处理</th></tr>";
			$("#myReportDetail table thead").html(sTh);
			$("#myReportDetail table tbody").html("");
			//alert(JSON.stringify(config.condition));
			if (config.condition != undefined && config.condition != null && config.condition != "") {
				$.each(config.condition, function(n, value) {
					var tr = $("<tr></tr>").appendTo($("#myReportDetail table tbody"));
					var target = value.target;
					if (target == undefined) {
						target = "";
					}
					var defaultType = value.defaultType;
					var defaultValue = value.defaultValue;
					var defaultTarget = value.defaultTarget;
					var defaultField = value.defaultField;
					if (defaultType == undefined) {
						defaultType = "";
					}
					if (defaultValue == undefined) {
						defaultValue = "";
					}
					if (defaultTarget == undefined) {
						defaultTarget = "";
					}
					if (defaultField == undefined) {
						defaultField = "";
					}
					if (defaultValue != "") {
						defaultValue = decodeURIComponent(defaultValue);
					}
					addNewRow(value.label, value.field, value.alias, value.editor, value.compare, value.openWinNo, value.sqlFunc, target, defaultType, defaultValue, defaultTarget, defaultField, tr);
				});
			}
			//$("#myListModal table tbody input[type='checkbox']").unbind("click");
			//$("#myListModal table tbody input[type='checkbox']").bind("click", editCondition);
			$("#myReportDetail table tbody input[type='button']").unbind("click");
			$("#myReportDetail table tbody input[type='button']").bind("click", deleteCondition);
			var modal = $('#myReportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
	    	//确定按钮
	    	$("#btn-report-confirm").unbind("click");
	    	$("#btn-report-confirm").bind("click", function() {
	    		if (config.condition == undefined || config.condition == null || config.condition == "") {
	    			config.condition = [];
	    		}
	    		config.condition.splice(0, config.condition.length);
	    		$("#myReportDetail table tbody tr").each(function() {
	    			var field = $(this).find("td:eq(2)").html();
	    			var alias = $(this).find("td:eq(3)").html();
	    			var viewText = $(this).find("td:eq(1)").html();
	    			var editType = $(this).find("td:eq(4)").html();
	    			var compare = $(this).find("td:eq(5)").html();
	    			var openWinNo = $(this).find("td:eq(6)").html();
	    			var sql = $(this).find("td:eq(7)").html();
	    			var target = $(this).find("td:eq(8)").html();
	    			var defaultType = $(this).find("td:eq(9)").html();
	    			var defaultValue = $(this).find("td:eq(10)").html();
	    			var defaultTarget = $(this).find("td:eq(11)").html();
	    			var defaultField = $(this).find("td:eq(12)").html();
	    			if (defaultValue != "") {
	    				defaultValue = encodeURIComponent(defaultValue);
	    			}
	    			config.condition.push({label:viewText,field:field,alias:alias,editor:editType,compare:compare,openWinNo:openWinNo,sqlFunc:sql,target:target,defaultType:defaultType,defaultValue:defaultValue,defaultTarget:defaultTarget,defaultField:defaultField});
				});
	    		alert(JSON.stringify(config));
	    		$("#myReportModal").modal("hide");
	    	});
		},
		set_show_field : function(event) {
			var myReport = event.data.myreport;
			var config = myReport.config;
			$("#myReportModalLabel").html("设定列表项目");
			var objForm = $("#myReportModal form");
			objForm.html("");			
			var labelField = $('<input type="text" placeholder="字段名称" class="form-control input-sm fl w15">').appendTo(objForm);
			var textField = $('<input type="text" placeholder="字段ID" class="form-control input-sm fl w15">').appendTo(objForm)
			var objViewNo = $('<input type="text" placeholder="画面编码" class="form-control input-sm fl w15" readonly>').appendTo(objForm);
			var objIdField = $('<input type="text" placeholder="数据ID的映射字段" class="form-control input-sm fl w15" readonly>').appendTo(objForm);
			var objLinkField = $('<input type="checkbox">').appendTo(objForm);
			$("<span>是否链接字段</span>").appendTo(objForm);
			var objIndex = $('<input type="hidden">').appendTo(objForm);
			//var objDiv = $('<div class="search"></div>').appendTo(objForm);
			var tHead = $("#myReportDetail table thead");
			$(tHead).html("");
			$(tHead).html("<tr><td></td><td>字段名称</td><td>字段ID</td><td>是否链接字段</td><td>画面编码</td><td>处理</td></tr>");
			var tbody = $("#myReportDetail table tbody");
			$(tbody).html("");
			//是否链接字段变更
			$(objLinkField).unbind("click");
			$(objLinkField).bind("click", function() {
				if ($(this).prop("checked")) {
					$(objViewNo).prop("readonly", false);
					$(objIdField).prop("readonly", false);
				} else {
					$(objViewNo).prop("readonly", true);
					$(objIdField).prop("readonly", true);
				}
			});
			//编辑字段
			var editField = function() {
				if ($(this).prop("checked")) {
    				var ids = [];
    				$("#myReportDetail table tbody input[type='checkbox']:checked").each(function() {
    					if ($(this).prop("checked")) {
    						ids.push($(this).val());
    					}
    				});
    				if (ids.length > 1) {
    					MessageBox.info("只能选择一条数据编辑！");
    					$("#myReportDetail table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
    					return;
    				}
    				$(objIndex).val($(this).closest("tr").index());
    				$(labelField).val($(this).parents("tr").find("td:eq(1)").html());
    				$(textField).val($(this).parents("tr").find("td:eq(2)").html());
    				var linkField = $(this).parents("tr").find("td:eq(3)").html();
    				if (linkField == "Y") {
    					$(objLinkField).prop("checked", true);
    					$(objViewNo).prop("readonly", false);    					
    					$(objViewNo).val($(this).parents("tr").find("td:eq(4)").html());
    					$(objIdField).prop("readonly", false);
    					$(objIdField).val($(this).parents("tr").find("td:eq(5)").html());
    				} else {
    					$(objLinkField).prop("checked", false);
    					$(objViewNo).prop("readonly", true);
    					$(objViewNo).val("");
    					$(objIdField).prop("readonly", true);
    					$(objIdField).val("");
    				}
    			} else {
    				$(labelField).val("");
    				$(textField).val("");
    				$(objViewNo).val("");
    				$(objIdField).val("")
    				$(objLinkField).prop("checked", false);
    				$(objViewNo).prop("readonly", true);
    				$(objIdField).prop("readonly", true);
    				$(objIndex).val("");
    			}
			};
			//删除字段
			var deleteField = function() {
				$(labelField).val("");
				$(textField).val("");
				$(objViewNo).val("");
				$(objIdField).val("");
				$(objLinkField).prop("checked", false);
				$(objViewNo).prop("readonly", true);
				$(objIdField).prop("readonly", true);
				$(this).closest("tr").remove();
				$(objIndex).val("");
			};
			var tableData = "";
			if (config.view != undefined && config.view != null && config.view != "") {
				$.each(config.view, function(n, value) {
					tableData += "<tr>";
					tableData += "<td><input type='checkbox' value='" + value.field + "'></td>";
					tableData += "<td>" + value.label + "</td>";
					tableData += "<td>" + value.field + "</td>";
					tableData += "<td>" + value.link + "</td>";
					tableData += "<td>" + value.viewNo + "</td>";
					tableData += "<td>" + value.idField + "</td>"
					tableData += "<td><input type='button' value='删除' class='btn btn-default'>" + "</td>";
					tableData += "</tr>";
				});
			}
			$(tbody).html(tableData);
			$("#myReportDetail table tbody input[type='checkbox']").unbind("click");
			$("#myReportDetail table tbody input[type='checkbox']").bind("click", editField);
			$("#myReportDetail table tbody input[type='button']").unbind("click");
			$("#myReportDetail table tbody input[type='button']").bind("click", deleteField);
			var objAddButton = $('<input type="button" class="btn btn-success" value="新增">').click(function() {
				if ($(labelField).val() == null || $(labelField).val() == "") {
					alert("请输入字段名称！");
					return;
				}
				if ($(textField).val() == null || $(textField).val() == "") {
					alert("请输入字段ID！");
					return;
				}
				var link = "";
				if ($(objLinkField).prop("checked")) {
					link = "Y";
					if ($(objViewNo).val() == "") {
						alert("请输入画面编码！");
						return;
					}
					if ($(objIdField).val() == "") {
						alert("请输入数据的ID字段！");
						return;
					}
				} else {
					link = "N";
				}
				if ($(objIndex).val() == null || $(objIndex).val() == "") {
					var tdata = "<tr>";
					tdata += "<td><input type='checkbox' value='" + $(textField).val() + "'></td>";
					tdata += "<td>" + $(labelField).val() + "</td>";
					tdata += "<td>" + $(textField).val() + "</td>";
					tdata += "<td>" + link + "</td>";
					if (link == "Y") {
						tdata += "<td>" + $(objViewNo).val() + "</td>";
						tdata += "<td>" + $(objIdField).val() + "</td>";
					} else {
						tdata += "<td></td>";
						tdata += "<td></td>";
					}
					tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
					tdata += "</tr>";
					$(tbody).append(tdata);
				} else {
					var index = $(objIndex).val();
        			var tr = $("#myReportDetail table tbody tr:eq(" + index + ")");
        			var tdata = "<td><input type='checkbox' value='" + $(textField).val() + "'></td>";
					tdata += "<td>" + $(labelField).val() + "</td>";
					tdata += "<td>" + $(textField).val() + "</td>";
					tdata += "<td>" + link + "</td>";
					if (link == "Y") {
						tdata += "<td>" + $(objViewNo).val() + "</td>";
						tdata += "<td>" + $(objIdField).val() + "</td>";
					} else {
						tdata += "<td></td>";
						tdata += "<td></td>";
					}
					tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
					$(tr).html(tdata);
				}
				$(labelField).val("");
				$(textField).val("");
				$(objViewNo).val("");
				$(objIdField).val("");
				$(objLinkField).prop("checked", false);
				$(objViewNo).prop("readonly", true);
				$(objIdField).prop("readonly", true);
				$(objIndex).val("");
				$("#myReportDetail table tbody input[type='checkbox']").unbind("click");
				$("#myReportDetail table tbody input[type='checkbox']").bind("click", editField);
				$("#myReportDetail table tbody input[type='button']").unbind("click");
				$("#myReportDetail table tbody input[type='button']").bind("click", deleteField);
			}).appendTo(objForm);
			//条件位置调整上
    		var objUp = $('<input type="button" value="向上" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myReportDetail table tbody input[type='checkbox']").each(function() {
					if ($(this).prop("checked")) {
						ids.push($(this).val());
						index = n;
					}
					n++;
				});
				if (ids.length > 1) {
					MessageBox.info("只能选择一条数据调整位置！");
					return;
				}
				if (index > 0) {
					var tr = $("#myReportDetail table tbody tr:eq(" + index + ")");
					var prevTr = $(tr).prev();
					prevTr.insertAfter(tr);
				}
    		}).appendTo(objForm);
    		$('<input type="button" value="向下" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myReportDetail table tbody input[type='checkbox']").each(function() {
					if ($(this).prop("checked")) {
						ids.push($(this).val());
						index = n;
					}
					n++;
				});
				if (ids.length > 1) {
					MessageBox.info("只能选择一条数据编辑！");
					return;
				}
				var tr = $("#myReportDetail table tbody tr:eq(" + index + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
    		}).appendTo(objForm);
			$("#btn-report-confirm").unbind("click");
			$("#btn-report-confirm").bind("click", function() {
				config.view = [];
				$("#myReportDetail table tbody tr").each(function() {
	    			var field = $(this).find("td:eq(2)").html();
	    			var viewText = $(this).find("td:eq(1)").html();
	    			var link = $(this).find("td:eq(3)").html();
	    			var viewNo = $(this).find("td:eq(4)").html();
	    			var idField = $(this).find("td:eq(5)").html();
	    			config.view.push({label:viewText,field:field,link:link,viewNo:viewNo,idField:idField});
				});
				//alert(JSON.stringify(view));
				$("#myReportModal").modal("hide");
			});
			var modal = $('#myReportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_template : function(event) {
			var myReport = event.data.myreport;
			var config = myReport.config;
			var appName = myReport.appName;
			var reportNo = myReport.reportNo;
			MessageBox.processStart();
			//取得模板数据
			$.ajax({
				url:appName + "/ReportDesign/getTemplate",
				type:"post",
				dataType:"json",
				data:{reportNo:reportNo},
				success:function(data){
					if (data.errType == "1") {
						MessageBox.processEnd("系统错误:" + data.errMessage);
						return;
					}
					MessageBox.processClose();
					//alert(JSON.stringify(data.data));
					var tbody = "";
					$.each(data.data, function(n, value) {
						tbody += "<tr>";
						tbody += "<td>" + value.fileName + "<input type='hidden' value='" + value.id + "'></td>";
						tbody += "<td><input type='button' value='删除'></td>";
						tbody += "</tr>";
					});
					$("#myReportDetail table tbody").html(tbody);
					$("#myReportDetail table tbody input[type='button']").unbind("click");
					$("#myReportDetail table tbody input[type='button']").bind("click", function() {
						var id = $(this).closest("tr").find("input[type='hidden']").val();
						MessageBox.confirm("您是否要删除改模板？", function() {
							MessageBox.processStart();
							$.ajax({
								url:appName + "/ReportDesign/deleteTemplate",
								type:"post",
								dataType:"json",
								data:{id:id},
								success:function(data){
									if (data.errType == "1") {
										MessageBox.processEnd(data.errMessage);
										return;
									}
									$("#btn-upload").trigger("click", {myreport:myReport});
								},
								error:function(XMLHttpRequest, data) {
									MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
								}
							});
						});
					});
					$("#myReportModalLabel").html("报表模板");
					var objBody = $("#myReportModalForm table tbody");
					$(objBody).html("");
					var tr = $("<tr></tr>").appendTo(objBody);
					var td = $("<td></td>").appendTo(tr);
					var objFile = $("<input type='file' name='UploadTemplateFile'>").appendTo(td);
					td = $("<td align='left'></td>").appendTo(tr);
					$("<input type='button' value='上传'>").appendTo(td).click(function() {
						if ($(objFile).val() == "") {
							alert("请选择文件后上传！");
							return;
						}
						var formData = new FormData($("#myReportModalForm")[0]);
						MessageBox.processStart();
						$.ajax({
							type:"POST",
							dataType:"json",
							data:formData,
							url:appName + "/ReportDesign/upload/" + reportNo,
							contentType:false,
							processData:false,
							success:function(data){
								//上传成功
								if (data.errType == "1") {
									MessageBox.processEnd(data.errMessage);
									return;
								}
								$("#btn-upload").trigger("click", {myreport:myReport});
							},
							error:function(XMLHttpRequest, data) {
								MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
							}
						});
					});
					$("#myReportDetail table thead").html("<tr><td>文件名</td><td>处理</td></tr>");
					$("#btn-report-confirm").unbind("click");
					$("#btn-report-confirm").bind("click", function() {
						$('#myReportModal').modal("hide");
					});
					var modal = $('#myReportModal').modal({backdrop: 'static', keyboard: false});
			    	modal.show();
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data + "status=" + XMLHttpRequest.status);
				}
			});
		}
		/*set_link_field : function(event) {
			var myReport = event.data.myreport;
			var config = myReport.config;
			if (config.view ==  undefined || config.view == null || config.view == "") {
				alert("请先设定显示项目！");
				return;
			}
			var linkFields = [];
			$.each(config.view, function(n, value) {
				if (value.link == "Y") {
					linkFields.push(value.field);
				}
			});
			if (linkFields.length <= 0) {
				alert("没有链接项目需要设定！");
				return;
			}
			$("#myReportModalLabel").html("链接项目设定");
			var objForm = $("#myReportModal form");
			$(objForm).html("");
			var table = $("<table width='100%'></table>").appendTo(objForm);
			var tr = $("<tr></tr>").appendTo(table);
			$("<td>链接字段</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objLinkField = $("<select class='form-control input-sm fl w15'></select>").appendTo(td);
			var options = "";
			$.each(linkFields, function(n, value){
				options += "<option value='" + value + "'>" + value + "</option>";
			});
			objLinkField.html(options);
			$("<td>参数ID</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objParam = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>参数来源字段</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objSourceField = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			td = $("<td></td>").appendTo(tr);
			var objBtnAdd = $("<input type='button' value='新增'>").appendTo(td);
			var objIndex = $("<input type='hidden'>").appendTo(td);
			$("#myReportDetail table thead tr").html("<th></th><th>参数ID</th><th>参数来源</th><th>处理</th>");
			$("#myReportDetail table tbody").html("");
			//新增按钮
			$(objBtnAdd).unbind("click");
			$(objBtnAdd).bind("click", function() {
				var tdata = "";
				tdata += "<td><input type='checkbox'></td>";
				tdata += "<td>" + $(objParam).val() + "</td>";
				tdata += "<td>" + $(objSourceField).val() + "</td>";
				tdata += "<td><input type='button' value='删除'></td>";
				if ($(objIndex).val() == "") {
					//新增
					$("#myReportDetail table tbody").append("<tr>" + tdata + "</tr>");
				} else {
					//修改
					var tr = $("#myReportDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tdata);
				}
				$(objParam).val("");
				$(objSourceField).val("");
				$(objIndex).val("");
				//注册事件
				$("#myReportDetail table tbody input[type='checkbox']").unbind("click");
				$("#myReportDetail table tbody input[type='checkbox']").bind("click", editParam);
				$("#myReportDetail table tbody input[type='button']").unbind("click");
				$("#myReportDetail table tbody input[type='button']").bind("click", deleteParam);
			});
			//编辑参数
			var editParam = function() {
				if($(this).prop("checked")) {
					//编辑
					if ($(objIndex).val() != "") {
						alert("只能选择一条数据编辑！");
						return;
					}
					var paramId = $(this).closest("tr").find("td:eq(1)").html();
					var sourceField = $(this).closest("tr").find("td:eq(2)").html();
					$(objParam).val(paramId);
					$(objSourceField).val(sourceField);
					$(objIndex).val($(this).closest("tr").index());
				} else {
					//取消编辑
					$(objParam).val("");
					$(objSourceField).val("");
					$(objIndex).val("");
				}
			};
			//删除参数
			var deleteParam = function() {
				if ($(objIndex).val() == "") {
					$(this).closest("tr").remove();
					return;
				}
				var index = $(this).closest("tr").index();
				if (index == $(objIndex).val()) {
					$(objParam).val("");
					$(objSourceField).val("");
					$(objIndex).val("");
				} else if (parseInt($(objIndex).val()) > index) {
					$(objIndex).val(parseInt($(objIndex).val()) - 1);
				}
				$(this).closest("tr").remove();
			};
			//链接对象修改事件
			var linkFieldChange = function() {
				var linkField = $(objLinkField).val();
				if (linkField == null || linkField == "") {
					return;
				}
				$("#myReportDetail table tbody").html("");
				if (config.link != undefined && config.link != null && config.link != "") {
					var objLink = null;
					$.each (config.link, function (n, value) {
						if (value.field == linkField) {
							objLink = value;
							return true;
						}
					});
					if (objLink != null && objLink.param != undefined && objLink.param != null && objLink.param != "") {
						var tbody = "";
						$.each(objLink.param, function(n, value) {
							tbody += "<tr>";
							tbody += "<td><input type='checkbox'></td>";
							tbody += "<td>" + value.paramId + "</td>";
							tbody += "<td>" + value.sourceField + "</td>";
							tbody += "<td><input type='button' value='删除'></td>";
							tbody += "</tr>";
						});
						$("#myReportDetail table tbody").html(tbody);
						//注册事件
						$("#myReportDetail table tbody input[type='checkbox']").unbind("click");
						$("#myReportDetail table tbody input[type='checkbox']").bind("click", editParam);
						$("#myReportDetail table tbody input[type='button']").unbind("click");
						$("#myReportDetail table tbody input[type='button']").bind("click", deleteParam);
					}
				}
			};
			//确定按钮
			$("#btn-report-confirm").unbind("click");
			$("#btn-report-confirm").bind("click", function() {
				var linkField = $(objLinkField).val();
				if (linkField == null || linkField == "") {
					return;
				}
				if (config.link == undefined || config.link == null || config.link == "") {
					config.link = [];
				}
				var objLink = null;
				$.each (config.link, function (n, value) {
					if (value.field == linkField) {
						objLink = value;
						return true;
					}
				});
				if (objLink == null) {
					objLink = {field:linkField,param:[]};
					config.link.push(objLink);
				}
				objLink.param.splice(0, objLink.param.length);
				$("#myReportDetail table tbody tr").each(function() {
					var paramId = $(this).find("td:eq(1)").html();
					var sourceField = $(this).find("td:eq(2)").html();
					objLink.param.push({paramId:paramId,sourceField:sourceField});
				});
				$('#myReportModal').modal("hide");
			});
			//注册链接项目修改change事件
			$(objLinkField).unbind("change");
			$(objLinkField).bind("change", linkFieldChange);
			$(objLinkField).trigger("change");
			var modal = $('#myReportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		}*/
	};
	$.fn.myreport = function(config, appName, reportNo) {
		var myreport = new MyReport(this, config, appName, reportNo);
		return myreport.init();
	}	
})(jQuery);