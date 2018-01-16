(function($){
	var MySync = function(ele, con) {
		this.$element = ele;
		this.defaults = {
			source:[],
			table:[],
			proc:[]
		};
		this.config = $.extend({}, this.defaults, con);
		//alert(JSON.stringify(this.config));
	};
	MySync.prototype = {
		init : function() {
			//alert("init");
			$("#dbTable table tbody").html("");
			$.each(this.config.table, function(n, table) {
				var tbody = "<tr>";
				tbody += "<td><input type='checkbox' value='" + table.table + "'></td>";
				tbody += "<td>" + table.target + "</td>";
				tbody += "<td>" + table.table + "</td>";
				tbody += "</tr>";
				$("#dbTable table tbody").append(tbody);
			});
			$("#dbTable table tbody input[type='checkbox']").unbind("click");
			$("#dbTable table tbody input[type='checkbox']").bind("click", {myimport:this}, this.show_mapping);
			//设定Excel
			$("#btn-source").unbind("click");
			$("#btn-source").bind("click", {mysync:this}, this.set_source);
			//导入数据表
			$("#btn-table").unbind("click");
			$("#btn-table").bind("click", {mysync:this}, this.set_table);
			//字段映射
			$("#btn-mapping").unbind("click");
			$("#btn-mapping").bind("click", {mysync:this}, this.set_mapping);
			//存储过程设定
			$("#btn-proc").unbind("click");
			$("#btn-proc").bind("click", {mysync:this}, this.set_proc);
			return this;
		},
		set_source : function(event) {
			var mySync = event.data.mysync;
			var syncConfig = mySync.config;
			$("#mySyncModalLabel").html("数据取得源设定");
			var headBody = $("#mySyncModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>源编码</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objSourceNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>源数据库</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
			$("<td>是否主数据源</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objMaster = $("<input type='checkbox'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>数据取得SQL或存储过程</td>").appendTo(tr);
			td = $("<td colspan='3'></td>").appendTo(tr);
			var objSql = $("<textarea class='form-control input-sm fl'></textarea>").appendTo(td);
			var objIndex = $("<input type='hidden'>").appendTo(td);
			var edit_source = function() {
				var ids = [];
				$.each($("#mySyncDetail table tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						ids.push($(this).closest("tr").index());
					}
				});
				if (ids.length > 1) {
					$(this).prop("checked", false);
					alert("只能选择一条数据编辑！");
					return;
				} else if (ids.length <= 0) {
					$(objIndex).val("");
					$(objSourceNo).val("");
					$(objTarget).val("L");
					$(objSql).val("");
					$(objMaster).prop("checked", false);
				} else {
					var tr = $(this).closest("tr");
					$(objSourceNo).val($(tr).find("td:eq(1)").html());
					$(objTarget).val($(tr).find("td:eq(2)").html());
					$(objSql).val(decodeURIComponent($(tr).find("td:eq(3)").html()));
					var master = $(tr).find("td:eq(4)").html();
					if (master == "Y") {
						$(objMaster).prop("checked", true);
					} else {
						$(objMaster).prop("checked", false);
					}
					$(objIndex).val($(this).closest("tr").index());
				}
			};
			$("<input type='button' class='btn btn-default' value='新增'>").click(function() {
				if ($(objSourceNo).val() == "") {
					alert("请输入数据源编码！");
					return;
				}
				if ($(objSql).val() == "") {
					alert("请输入SQL或存储过程！");
					return;
				}
				var tbody = "";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + $(objSourceNo).val() + "</td>";
				tbody += "<td>" + $(objTarget).val() + "</td>";
				tbody += "<td>" + encodeURIComponent($(objSql).val()) + "</td>";
				if ($(objMaster).prop("checked")) {
					tbody += "<td>Y</td>";
				} else {
					tbody += "<td>N</td>";
				}
				if ($(objIndex).val() == "") {
					//新增
					$("#mySyncDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#mySyncDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objSourceNo).val("");
				$(objTarget).val("L");
				$(objSql).val("");
				$(objIndex).val("");
				$(objMaster).prop("checked", false);
				//增加事件处理
				$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
				$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_source);
			}).appendTo(td);
			$("#mySyncDetail table thead").html("<tr><th></th><th>数据源编码<th>目标数据库</th><th>SQL或存储过程</th><th>是否主数据源</th></tr>");
			$("#mySyncDetail table tbody").html("");
			$.each(syncConfig.source, function(n, value) {
				var tbody = "<tr>";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + value.no + "</td>"
				tbody += "<td>" + value.target + "</td>";
				tbody += "<td>" + value.sql + "</td>";
				tbody += "<td>" + value.master + "</td>";
				tbody += "</tr>";
				$("#mySyncDetail table tbody").append(tbody);
			});
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				syncConfig.source = [];
				var hasMaster = false;
				var isOk = true;
				$("#mySyncDetail table tbody tr").each(function() {
					var no = $(this).find("td:eq(1)").html();
					var target = $(this).find("td:eq(2)").html();
					var sql = $(this).find("td:eq(3)").html();
					var master = $(this).find("td:eq(4)").html();
					if (master == "Y") {
						if (hasMaster) {
							alert("只能选择一个主数据源！");
							syncConfig.source = [];
							isOk = false;
							return false;
						} else {
							hasMaster = true;
						}
					}
					syncConfig.source.push({no:no,target:target,sql:sql,master:master});
				});
				if (isOk) {
					$("#mySyncModal").modal("hide");
				}
			});
			$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
			$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_source);
			var modal = $('#mySyncModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_table : function(event) {
			var mySync = event.data.mysync;
			var syncConfig = mySync.config;
			$("#mySyncModalLabel").html("数据表设定");
			var headBody = $("#mySyncModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>目标数据库</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
			$("<td>数据表</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTable = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			td = $("<td></td>").appendTo(tr);
			var objIndex = $("<input type='hidden'>").appendTo(td);
			var edit_table = function() {
				var ids = [];
				$.each($("#mySyncDetail table tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						ids.push($(this).closest("tr").index());
					}
				});
				if (ids.length > 1) {
					$(this).prop("checked", false);
					alert("只能选择一条数据编辑！");
					return;
				} else if (ids.length <= 0) {
					$(objIndex).val("");
					$(objTarget).val("L");
					$(objTable).val("");
				} else {
					var tr = $(this).closest("tr");
					$(objTarget).val($(tr).find("td:eq(1)").html());
					$(objTable).val($(tr).find("td:eq(2)").html());
					$(objIndex).val($(this).closest("tr").index());
				}
			};
			//删除表
			var delete_table = function() {
				var tr = $(this).closest("tr");
				if ($(objIndex).val() == $(this).closest("tr").index()) {
    				$(objIndex).val("");
    			} else if($(objIndex).val() > $(this).closest("tr").index()) {
    				$(objIndex).val($(objIndex).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			if ($(objIndex).val() == "") {
    				$(objTarget).val("L");
					$(objTable).val("");
    			}
			}
			$("<input type='button' class='btn btn-default' value='新增'>").click(function() {
				if ($(objTable).val() == "") {
					alert("请输入数据表");
					return;
				}
				var tbody = "";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + $(objTarget).val() + "</td>";
				tbody += "<td>" + $(objTable).val() + "</td>";
				tbody += "<td><input type='button' value='删除'></td>";
				if ($(objIndex).val() == "") {
					//新增
					$("#mySyncDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#mySyncDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objTarget).val("L");
				$(objTable).val("");
				$(objIndex).val("");
				//增加事件处理
				$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
				$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_table);
				//增加事件处理
				$("#mySyncDetail table tbody input[type='button']").unbind("click");
				$("#mySyncDetail table tbody input[type='button']").bind("click", delete_table);
			}).appendTo(td);
			$("#mySyncDetail table thead").html("<tr><th></th><th>目标数据库</th><th>数据表</th></tr>");
			$("#mySyncDetail table tbody").html("");
			$.each(syncConfig.table, function(n, value) {
				var tbody = "<tr>";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + value.target + "</td>"
				tbody += "<td>" + value.table + "</td>";
				tbody += "<td><input type='button' value='删除'></td>";
				tbody += "</tr>";
				$("#mySyncDetail table tbody").append(tbody);
			});
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				var tableData = [];
				$("#mySyncDetail table tbody tr").each(function() {
					var target = $(this).find("td:eq(1)").html();
					var tableId = $(this).find("td:eq(2)").html();
					var hasData = false;
					$.each(syncConfig.table, function(n, table) {
						if (tableId == table.table) {
							tableData.push({target:target,table:tableId,mapping:table.mapping});
							hasData = true;
							return false;
						}
					});
					if (hasData == false) {
						tableData.push({target:target, table:tableId,mapping:[]});
					}
				});
				syncConfig.table = [];
				syncConfig.table = tableData;
				$("#mySyncModal").modal("hide");
				mySync.init();
			});
			$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
			$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_table);
			//增加事件处理
			$("#mySyncDetail table tbody input[type='button']").unbind("click");
			$("#mySyncDetail table tbody input[type='button']").bind("click", delete_table);
			var modal = $('#mySyncModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_mapping : function(event) {
			var mySync = event.data.mysync;
			var config = mySync.config;
			var ids = [];
			$.each($("#dbTable table tbody input[type='checkbox']"), function() {
				if ($(this).prop("checked")) {
					ids.push($(this).closest("tr").index());
				}
			});
			if (ids.length <= 0) {
				alert("请选择数据表后设定字段映射！");
				return;
			} else if (ids.length > 1) {
				alert("只能选择一个数据表进行字段映射！");
				return;
			}
			var table = config.table[ids[0]];
			$("#mySyncModalLabel").html("字段映射设定");
			var headBody = $("#mySyncModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>数据库字段</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objField = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>数据来源</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objSourceType = $("<select><option value='source'>数据源</option><option value='sqlFunc'>数据库函数</option><option value='sqlProc'>SQL或存储过程</option><option value='script'>脚本函数</option></select>").appendTo(td);
			$("<td>参数设定</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objParamenter = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>SQL/存储过程<br>脚本函数/数据库函数</td>").appendTo(tr);
			td = $("<td colspan='5'></td>").appendTo(tr);
			var objFunc = $("<textarea class='form-control input-sm fl'></textarea>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>SQL或存储过程目标数据库</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objMappingTarget = $("<select><option value='L'>本地</option><option value='R'>远程</option></select>").appendTo(td);
			$("<td>映射源编码</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objSourceNo = $("<select></select>").appendTo(td);
			var options = "";
			$.each(config.source, function(n,value) {
				options += "<option value='" + value.no + "'>" + value.no + "</option>";
			});
			$.each(config.proc, function(n, value) {
				if (value.type == "select") {
					options += "<option value='" + value.no + "'>" + value.no + "</option>";
				}
			});
			$(objSourceNo).html(options);
			$("<td>映射字段</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objMappingField = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			td = $("<td></td>").appendTo(tr);
			var objChkField = $("<input type='checkbox'>").appendTo(td);
			$("<span>数据重复check字段</span>").appendTo(td);
			td = $("<td></td>").appendTo(tr);
			var objIndex = $("<input type='hidden'>").appendTo(td);
			var objRpcField = $("<input type='checkbox'>").appendTo(td);
			$("<span>是否覆盖</span>").appendTo(td);
			var edit_mapping = function() {
				var ids = [];
				$.each($("#mySyncDetail table tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						ids.push($(this).closest("tr").index());
					}
				});
				if (ids.length <= 0) {
					$(objField).val("");
					$(objSourceType).val("excel");
					$(objParamenter).val("");
					$(objFunc).val("");
					$(objMappingTarget).val("L");
					$(objMappingField).val("");
					$(objSourceNo).val("");
					$(objChkField).prop("checked", false);
					$(objRpcField).prop("checked", true);
					$(objIndex).val("");
					return;
				} else if (ids.length > 1) {
					alert("只能选择一条数据编辑！");
					$(this).prop("checked", false);
					return;
				}
				var tr = $(this).closest("tr");
				var field = $(tr).find("td:eq(1)").html();
				var sourceType = $(tr).find("td:eq(2)").html();
				var parameter = $(tr).find("td:eq(3)").html();
				var func = decodeURIComponent($(tr).find("td:eq(4)").html());
				var mappingTarget = $(tr).find("td:eq(5)").html();
				var sourceNo = $(tr).find("td:eq(6)").html();
				var mappingField = $(tr).find("td:eq(7)").html();
				var chkField = $(tr).find("td:eq(8)").html();
				var rpcField = $(tr).find("td:eq(9)").html();
				if (chkField == "Y") {
					$(objChkField).prop("checked", true);
				} else {
					$(objChkField).prop("checked", false);
				}
				if (rpcField == "Y") {
					$(objRpcField).prop("checked", true);
				} else {
					$(objRpcField).prop("checked", false);
				}
				$(objField).val(field);
				$(objSourceType).val(sourceType);
				$(objParamenter).val(parameter);
				$(objFunc).val(func);
				$(objMappingField).val(mappingField);
				$(objIndex).val(ids[0]);
				$(objSourceType).trigger("change");
			};
			//删除映射
			var delete_mapping = function() {
				var tr = $(this).closest("tr");
				if ($(objIndex).val() == $(this).closest("tr").index()) {
    				$(objIndex).val("");
    			} else if($(objIndex).val() > $(this).closest("tr").index()) {
    				$(objIndex).val($(objIndex).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			if ($(objIndex).val() == "") {
    				$(objField).val("");
    				$(objSourceType).val("excel");
    				$(objParamenter).val("");
    				$(objFunc).val("");
    				$(objMappingTarget).val("L");
    				$(objSourceNo).val("");
    				$(objMappingField).val("");
    				$(objChkField).prop("checked", false);
    				$(objRpcField).prop("checked", true);
    			}
			}
			$("<input type='button' value='新增'>").click(function () {
				if ($(objField).val() == "") {
					alert("请输入数据库字段！");
					return;
				}
				var tbody = "";
				switch ($(objSourceType).val()) {
				case "source":
					//数据来源
					if ($(objMappingField).val() == "") {
						alert("请输入映射字段！");
						return;
					}
					if ($(objSourceNo).val() == "") {
						alert("请输入数据源编码！");
					}
					var field = $(objField).val();
					var sourceNo = $(objSourceNo).val();
					var mappingField = $(objMappingField).val();
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>source</td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td>" + sourceNo + "</td>";
					tbody += "<td>" + mappingField + "</td>";
					break;
				case "sqlFunc":
				case "script":
					//数据库函数  或  加班函数
					if ($(objFunc).val() == "") {
						alert("请输入数据函数或脚本函数！");
						return;
					}
					var field = $(objField).val();
					var parameter = $(objParamenter).val();
					var func = encodeURIComponent($(objFunc).val());
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>" + $(objSourceType).val() + "</td>";
					tbody += "<td>" + parameter + "</td>";
					tbody += "<td>" + func + "</td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					break;
				case "sqlProc":
					//SQL或存储过程
					if ($(objFunc).val() == "") {
						alert("请输入存储过程！");
						return;
					}
					if ($(objMappingField).val() == "") {
						alert("请输入映射字段！");
					}
					var field = $(objField).val();
					var parameter = $(objParamenter).val();
					var func = encodeURIComponent($(objFunc).val());
					var mappingTarget = $(objMappingTarget).val();
					var mappingField = $(objMappingField).val();
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>" + $(objSourceType).val() + "</td>";
					tbody += "<td>" + parameter + "</td>";
					tbody += "<td>" + func + "</td>";
					tbody += "<td>" + mappingTarget + "</td>";
					tbody += "<td></td>";
					tbody += "<td>" + mappingField + "</td>";
					break;
				default:
					var field = $(objField).val();
					var mappingField = $(objMappingField).val();
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>source</td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td>" + $(objSourceNo).val() + "</td>";
					tbody += "<td>" + mappingField + "</td>";
					break;
				}
				if ($(objChkField).prop("checked")) {
					tbody += "<td>Y</td>";
				} else {
					tbody += "<td>N</td>";
				}
				if ($(objRpcField).prop("checked")) {
					tbody += "<td>Y</td>";
				} else {
					tbody += "<td>N</td>";
				}
				tbody += "<td><input type='button' value='删除'></td>";
				if ($(objIndex).val() == "") {
					//新增
					$("#mySyncDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#mySyncDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objField).val("");
				$(objSourceType).val("excel");
				$(objParamenter).val("");
				$(objFunc).val("");
				$(objMappingTarget).val("L");
				$(objSourceNo).val("");
				$(objMappingField).val("");
				$(objChkField).prop("checked", false);
				$(objRpcField).prop("checked", true);
				$(objIndex).val("");
				//checkbox事件
				$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
				$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_mapping);
				//删除按钮
				$("#mySyncDetail table tbody input[type='button']").unbind("click");
				$("#mySyncDetail table tbody input[type='button']").bind("click", delete_mapping);
			}).appendTo(td);
			//部分控件控制
			$(objParamenter).prop("readonly", true);
			$(objFunc).prop("readonly", true);
			$(objMappingTarget).prop("disabled", true);
			$(objMappingField).prop("readonly", false);
			$(objSourceNo).prop("readonly", true);
			$(objSourceType).unbind("change");
			$(objSourceType).bind("change", function() {
				switch ($(objSourceType).val()) {
				case "source":
					//数据源
					$(objParamenter).prop("readonly", true);
					$(objFunc).prop("readonly", true);
					$(objMappingTarget).prop("disabled", true);
					$(objMappingField).prop("readonly", false);
					$(objSourceNo).prop("readonly", false);
					break;
				case "sqlFunc":
				case "script":
					//数据库函数  或  脚本函数
					$(objParamenter).prop("readonly", false);
					$(objFunc).prop("readonly", false);
					$(objMappingTarget).prop("disabled", true);
					$(objMappingField).prop("readonly", true);
					$(objSourceNo).prop("readonly", true);
					break;
				case "sqlProc":
					//SQL或存储过程
					$(objParamenter).prop("readonly", false);
					$(objFunc).prop("readonly", false);
					$(objMappingTarget).prop("disabled", false);
					$(objMappingField).prop("readonly", false);
					$(objSourceNo).prop("readonly", true);
					break;
				default:
					$(objParamenter).prop("readonly", true);
					$(objFunc).prop("readonly", true);
					$(objMappingTarget).prop("disabled", true);
					$(objMappingField).prop("readonly", true);
					$(objSourceNo).prop("readonly", true);
					break;
				}
			});
			$("#mySyncDetail table thead").html("<tr><th></th><th>数据库字段</th><th>数据来源</th><th>参数设定</th><th>SQL/存储过程<br>脚本函数/数据库函数</th><th>SQL或存储过程<br>目标数据库</th><th>数据源编码</th><th>映射字段</th><th>是否数据重复check字段</th><th>是否覆盖</th><th></th></tr>");
			$("#mySyncDetail table tbody").html("");
			if (table != undefined && table != null) {
				var tbody = "";
				$.each(table.mapping, function(n, value) {
					var param = "";
					if (value.parameter != undefined && value.parameter != null && value.parameter != "") {
						param = JSON.stringify(value.parameter);
					}
					if (param == "[]") {
						param = "";
					}
					tbody += "<tr>";
					tbody += "<td><input type='checkbox'></td>";
					tbody += "<td>" + value.targetField + "</td>"
					tbody += "<td>" + value.source + "</td>";
					tbody += "<td width='20'>" + param + "</td>";
					tbody += "<td>" + value.func + "</td>";
					tbody += "<td>" + value.target + "</td>";
					tbody += "<td>" + value.sourceNo + "</td>";
					tbody += "<td>" + value.sourceField + "</td>";
					tbody += "<td>" + value.keyField + "</td>";
					tbody += "<td>" + value.replace + "</td>";
					tbody += "<td><input type='button' value='删除'></td>";
					tbody += "</tr>";
				});
				$("#mySyncDetail table tbody").html(tbody);
				//设定checkbox事件
				$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
				$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_mapping);
				//删除按钮事件
				$("#mySyncDetail table tbody input[type='button']").unbind("click");
				$("#mySyncDetail table tbody input[type='button']").bind("click", delete_mapping);
				//确定按钮
				$("#btn-import-confirm").unbind("click");
				$("#btn-import-confirm").bind("click", function() {
					table.mapping = [];
					$.each($("#mySyncDetail table tbody tr"), function(n, tr) {
						var field = $(tr).find("td:eq(1)").html();
						var sourceType = $(tr).find("td:eq(2)").html();
						var parameter = $(tr).find("td:eq(3)").html();
						var func = $(tr).find("td:eq(4)").html();
						var mappingTarget = $(tr).find("td:eq(5)").html();
						var sourceNo = $(tr).find("td:eq(6)").html();
						var mappingField = $(tr).find("td:eq(7)").html();
						var keyField = $(tr).find("td:eq(8)").html();
						var keyReplace = $(tr).find("td:eq(9)").html();
						var param = [];
						if (parameter != "") {
							try {
								param = eval("(" + parameter + ")");
							} catch(ex) {
								alert(ex);
							}
						}
						table.mapping.push({targetField:field,source:sourceType,parameter:param,func:func,target:mappingTarget,sourceNo:sourceNo,sourceField:mappingField,keyField:keyField,replace:keyReplace});
					});
					$("#mySyncModal").modal("hide");
					myImport.init();
				});
			}
			var modal = $('#mySyncModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		show_mapping : function(event) {
			var myImport = event.data.myimport;
			var config = myImport.config;
			var ids = [];
			$.each($("#dbTable table tbody input[type='checkbox']"), function() {
				if ($(this).prop("checked")) {
					ids.push($(this).closest("tr").index());
				}
			});
			if (ids.length <= 0) {
				alert("请选择数据表后查看字段映射！");
				return;
			} else if (ids.length > 1) {
				alert("只能选择一个数据表查看字段映射！");
				return;
			}
			var table = config.table[ids[0]];
			if (table != undefined && table != null) {
				var tbody = "";
				$.each(table.mapping, function(n, mapping) {
					var param = JSON.stringify(mapping.parameter);
					if (param == "[]") {
						param = "";
					}
					tbody += "<tr>";
					tbody += "<td>" + mapping.targetField + "</td>"
					tbody += "<td>" + mapping.source + "</td>";
					tbody += "<td>" + param + "</td>";
					tbody += "<td>" + mapping.func + "</td>";
					tbody += "<td>" + mapping.target + "</td>";
					tbody += "<td>" + mapping.sourceField + "</td>";
					tbody += "</tr>";
				});
				$("#dbMapping tbody").html(tbody);
			}
		},
		set_proc : function(event) {
			var mySync = event.data.mysync;
			var config = mySync.config;
			$("#mySyncModalLabel").html("存储过程设定");
			var headBody = $("#mySyncModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>目标数据库</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
			$("<td>执行时点</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objRunTime = $("<select><option value='BefData'>每条数据同步前</option><option value='BakData'>每条数据同步后</option><option value='BefSync'>同步前</option><option value='BakSync'>同步后</option></select>").appendTo(td);
			$("<td>参数设定</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objParameter = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);	
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>别名</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objProcNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>处理类型</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objType = $("<select><option value='update'>修改数据</option><option value='select'>查询数据</option></select>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>SQL/存储过程</td>").appendTo(tr);
			td = $("<td colspan='5'></td>").appendTo(tr);
			var objProc = $("<textarea class='form-control input-sm fl'></textarea>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			td = $("<td></td>").appendTo(tr);
			var objIndex = $("<input type='hidden'>").appendTo(td);
			//编辑SQL
			var edit_proc = function() {
				var ids = [];
				$.each($("#mySyncDetail table tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						ids.push($(this).closest("tr").index());
					}
				});
				if (ids.length <= 0) {
					$(objTarget).val("L");
					$(objRunTime).val("BefData");
					$(objParameter).val("");
					$(objProc).val("");
					$(objProcNo).val("");
					$(objType).val("update");
					$(objIndex).val("");
					return;
				} else if (ids.length > 1) {
					alert("只能选择一条数据编辑！");
					$(this).prop("checked", false);
					return;
				}
				var tr = $("#mySyncDetail table tbody tr:eq(" + ids[0] + ")");
				$(objTarget).val($(tr).find("td:eq(1)").html());
				$(objRunTime).val($(tr).find("td:eq(2)").html())
				$(objParameter).val($(tr).find("td:eq(3)").html());
				$(objProcNo).val($(tr).find("td:eq(4)").html());
				$(objType).val($(tr).find("td:eq(5)").html());
				$(objProc).val(decodeURIComponent($(tr).find("td:eq(6)").html()));
				$(objIndex).val(ids[0]);
			};
			//删除SQL
			var delete_proc = function() {
				if ($(objIndex).val() == $(this).closest("tr").index()) {
    				$(objIndex).val("");
    			} else if($(objIndex).val() > $(this).closest("tr").index()) {
    				$(objIndex).val($(objIndex).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			if ($(objIndex).val() == "") {
	    			$(objTarget).val("L");
	    			$(objRunTime).val("BefData");
					$(objParameter).val("");
					$(objProcNo).val("");
					$(objType).val("update");
					$(objProc).val("");
    			}
			}
			$("<input type='button' value='新增'>").click(function () {
				if ($(objProcNo).val() == "") {
					alert("请输入别名！");
					return;
				}
				if ($(objProc).val() == "") {
					alert("请输入存储过程或SQL文！");
					return;
				}
				var target = $(objTarget).val();
				var runTime = $(objRunTime).val();
				var parameter = $(objParameter).val();
				var procNo = $(objProcNo).val();
				var type = $(objType).val();
				var proc = $(objProc).val();
				var tbody = "";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + target + "</td>";
				tbody += "<td>" + runTime + "</td>";
				tbody += "<td>" + parameter + "</td>";
				tbody += "<td>" + procNo + "</td>";
				tbody += "<td>" + type + "</td>";
				tbody += "<td>" + encodeURIComponent(proc) + "</td>";
				tbody += "<td><button class='btn btn-default'>删除</button></td>";
				if ($(objIndex).val() == "") {
					//新增
					$("#mySyncDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#mySyncDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objTarget).val("L");
				$(objRunTime).val("BefData");
				$(objParameter).val("");
				$(objProc).val("");
				$(objProcNo).val("");
				$(objType).val("update");
				$(objIndex).val("");
				//checkbox事件
				$("#mySyncDetail table tbody input[type='checkbox']").unbind("click");
				$("#mySyncDetail table tbody input[type='checkbox']").bind("click", edit_proc);
				//删除按钮
				$("#mySyncDetail table tbody button").unbind("click");
				$("#mySyncDetail table tbody button").bind("click", delete_proc);
			}).appendTo(td);
			$("<input type='button' value='向上'>").click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要调整位置的数据！");
					return;
				}
				if ($(objIndex).val() == "0") {
					return;
				}
				var tr = $("#mySyncDetail table tbody tr:eq(" + $(objIndex).val() + ")");
				var prevTr = $(tr).prev();
				prevTr.insertAfter(tr);
				$(objIndex).val(parseInt($(objIndex).val() - 1));
			}).appendTo(td);
			$("<input type='button' value='向下'>").click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要调整位置的数据！");
					return;
				}
				var size = $("#mySyncDetail table tbody tr").length - 1;
				if (parseInt($(objIndex).val()) >= size) {
					return;
				}
				var tr = $("#mySyncDetail table tbody tr:eq(" + $(objIndex).val() + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
				$(objIndex).val(parseInt($(objIndex).val() + 1));
			}).appendTo(td);
			//设定列表Header
			$("#mySyncDetail table thead").html("<tr><th></th><th>目标数据库</th><th>执行时点</th><th>参数</th><th>别名</th><th>处理类型</th><th>SQL/存储过程</th><th></th></tr>");
			//设定列表数据
			$("#mySyncDetail table tbody").html("");
			if (config.proc != undefined && config.proc != null) {
				var tbody = $("#mySyncDetail table tbody");
				$.each(config.proc, function(n, proc) {
					var tr = $("<tr></tr>").appendTo(tbody);
					var td = $("<td></td>").appendTo(tr);
					var chk = $("<input type='checkbox'>").appendTo(td);
					$(chk).bind("click", edit_proc);
					var param = "";
					if (proc.parameter != undefined && proc.parameter != null && proc.parameter != "") {
						param = JSON.stringify(proc.parameter);
					}
					$("<td>" + proc.target + "</td>").appendTo(tr);
					$("<td>" + proc.runTime + "</td>").appendTo(tr);
					$("<td>" + param + "</td>").appendTo(tr);
					$("<td>" + proc.no + "</td>").appendTo(tr);
					$("<td>" + proc.type + "</td>").appendTo(tr);
					$("<td>" + proc.proc + "</td>").appendTo(tr);
					td = $("<td></td>").appendTo(tr);
					var btn = $("<button class='btn btn-default' type='button'>删除</button>").appendTo(tr);
					$(btn).unbind("click");
					$(btn).bind("click", delete_proc);
				});
			}
			//确定按钮
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				var procs = [];
				var hasError = false;
				$.each($("#mySyncDetail table tbody tr"), function(n, tr) {
					var target = $(tr).find("td:eq(1)").html();
					var runTime = $(tr).find("td:eq(2)").html();
					var parameter = $(tr).find("td:eq(3)").html();
					var procNo = $(tr).find("td:eq(4)").html();
					var type = $(tr).find("td:eq(5)").html();
					var proc = $(tr).find("td:eq(6)").html();
					var hasNo = false;
					$.each(procs, function(n, value) {
						if (procNo == value.no) {
							hasNo = true;
							return false;
						}
					});
					if (hasNo) {
						alert("别名(" + procNo + ")重复！");
						hasError = true;
						return false;
					}
					$.each(config.source, function(n, value) {
						if (procNo == value.no) {
							hasNo = true;
							return false;
						}
					});
					if (hasNo) {
						alert("别名(" + procNo + ")与数据源中的别名重复！");
						hasError = true;
						return false;
					}
					var param = [];
					if (parameter != "") {
						try {
							param = eval("(" + parameter + ")");
						} catch(ex) {
							alert(ex);
						}
					}
					//alert(type);
					procs.push({target:target,runTime:runTime,parameter:param,no:procNo,type:type,proc:proc});
				});
				if (hasError == false) {
					config.proc = [];
					config.proc = procs;
					$("#mySyncModal").modal("hide");
				}
			});
			var modal = $('#mySyncModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		}
	};
	$.fn.mysync = function(config) {
		var mysync = new MySync(this, config);
		return mysync.init();
	}	
})(jQuery);