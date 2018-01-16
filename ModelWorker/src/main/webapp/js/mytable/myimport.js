(function($){
	var MyImport = function(ele, con) {
		this.$element = ele;
		this.defaults = {
			type:"excel",
			text:{},
			excel:{},
			table:[],
			proc:[]
		};
		this.config = $.extend({}, this.defaults, con);
	};
	MyImport.prototype = {
		init : function() {
			//alert(JSON.stringify(this.config.table));
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
			//文件类型
			$("#btn-file").unbind("click");
			$("#btn-file").bind("click", {myimport:this}, this.set_file);
			if (this.config.type == 'excel') {
				//设定Excel
				$("#btn-excel").unbind("click");
				$("#btn-excel").bind("click", {myimport:this}, this.set_excel);
				//设定文本文件
				$("#btn-text").unbind("click");
			} else {
				//设定Excel
				$("#btn-excel").unbind("click");
				//设定文本文件
				$("#btn-text").unbind("click");
				$("#btn-text").bind("click", {myimport:this}, this.set_text);
			}
			//导入数据表
			$("#btn-table").unbind("click");
			$("#btn-table").bind("click", {myimport:this}, this.set_table);
			//字段映射
			$("#btn-mapping").unbind("click");
			$("#btn-mapping").bind("click", {myimport:this}, this.set_mapping);
			//存储过程设定
			$("#btn-proc").unbind("click");
			$("#btn-proc").bind("click", {myimport:this}, this.set_proc);
			//空行处理
			$("#btn-SpaceRow").unbind("click");
			$("#btn-SpaceRow").bind("click", {myimport:this}, this.set_SpaceRow);
			return this;
		},
		set_file : function(event) {
			var myImport = event.data.myimport;
			var importConfig = myImport.config;
			$("#myImportModalLabel").html("文件类型设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>文件类型</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objFile = $("<select><option value='excel'>Excel文件</option><option value='text'>文本文件</option></select>").appendTo(td);
			$(objFile).val(importConfig.type);
			$("#myImportDetail table thead").html("");
			$("#myImportDetail table tbody").html("");
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				importConfig.type = $(objFile).val();
				$("#myImportModal").modal("hide");
				if ($(objFile).val() == 'excel') {
					//设定Excel
					$("#btn-excel").unbind("click");
					$("#btn-excel").bind("click", {myimport:myImport}, myImport.set_excel);
					//设定文本文件
					$("#btn-text").unbind("click");
				} else {
					//设定Excel
					$("#btn-excel").unbind("click");
					//设定文本文件
					$("#btn-text").unbind("click");
					$("#btn-text").bind("click", {myimport:myImport}, myImport.set_text);
				}
			});
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_excel : function(event) {
			var myImport = event.data.myimport;
			var importConfig = myImport.config;
			$("#myImportModalLabel").html("Excel文件设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			$("<tr><td colspan='4' align='left'>Excel表头</td></tr>").appendTo(headBody);
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>起始行</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objHeadStartRowNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>结束行</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objHeadEndRowNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$(objHeadStartRowNo).unbind("change");
			$(objHeadStartRowNo).bind("change", function() {
				if ($(this).val() != "") {
					$(objHeadEndRowNo).val($(this).val());
				}
			});
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td colspan='4' align='left'>Excel明细</td>").appendTo(tr);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>明细标题起始行</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTitleStartRowNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>明细标题结束行</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTitleEndRowNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$(objTitleStartRowNo).unbind("change");
			$(objTitleStartRowNo).bind("change", function() {
				if ($(this).val() != "") {
					$(objTitleEndRowNo).val($(this).val());
					$(objTitleEndRowNo).trigger("change");
				}
			});
			$(objTitleEndRowNo).unbind("change");
			$(objTitleEndRowNo).bind("change", function() {
				if ($(this).val() != "") {
					$(objDetailStartRowNo).val(parseInt($(this).val()) + 1);
				}
			});
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>明细数据起始行</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objDetailStartRowNo = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			//设定初始值
			if (importConfig.excel.headStartRowNo != undefined && importConfig.excel.headStartRowNo != null && importConfig.excel.headStartRowNo != "") {
				$(objHeadStartRowNo).val(importConfig.excel.headStartRowNo);
			}
			if (importConfig.excel.headEndRowNo != undefined && importConfig.excel.headEndRowNo != null && importConfig.excel.headEndRowNo != "") {
				$(objHeadEndRowNo).val(importConfig.excel.headEndRowNo);
			}
			if (importConfig.excel.titleStartRowNo != undefined && importConfig.excel.titleStartRowNo != null && importConfig.excel.titleStartRowNo != "") {
				$(objTitleStartRowNo).val(importConfig.excel.titleStartRowNo);
			} else {
				$(objTitleStartRowNo).val("1");
			}
			if (importConfig.excel.titleEndRowNo != undefined && importConfig.excel.titleEndRowNo != null && importConfig.excel.titleEndRowNo != "") {
				$(objTitleEndRowNo).val(importConfig.excel.titleEndRowNo);
			} else {
				$(objTitleEndRowNo).val($(objTitleStartRowNo).val());
			}
			if (importConfig.excel.detailStartRowNo != undefined && importConfig.excel.detailStartRowNo != null && importConfig.excel.detailStartRowNo != "") {
				$(objDetailStartRowNo).val(importConfig.excel.detailStartRowNo);
			} else {
				$(objDetailStartRowNo).val("2");
			}
			$("#myImportDetail table thead").html("");
			$("#myImportDetail table tbody").html("");
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				if ($(objDetailStartRowNo).val() == null || $(objDetailStartRowNo).val() == "") {
					alert("请输入明细数据起始行行号！");
					return;
				}
				importConfig.excel.headStartRowNo = $(objHeadStartRowNo).val();
				importConfig.excel.headEndRowNo = $(objHeadEndRowNo).val();
				importConfig.excel.titleStartRowNo = $(objTitleStartRowNo).val();
				importConfig.excel.titleEndRowNo = $(objTitleEndRowNo).val();
				importConfig.excel.detailStartRowNo = $(objDetailStartRowNo).val();
				importConfig.excel.detailStep = $(objDetailStep).val();
				$("#myImportModal").modal("hide");
			});
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_text : function(event) {
			var myImport = event.data.myimport;
			var importConfig = myImport.config;
			$("#myImportModalLabel").html("文本文件设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>文件编码</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objEncode = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>标题行号(0:没有标题)</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTitle = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>数据起始行</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objStartRow = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>项目区分符号</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objSeparator = $("<select><option value='tab'>TAB</option><option value='comma'>逗号</option></select>").appendTo(td);
			//设定初始值
			var encode = "GB2312";
			if (importConfig.text.encode != undefined && importConfig.text.encode != null && importConfig.text.encode != "") {
				encode = importConfig.text.encode;
			}
			$(objEncode).val(encode);
			var title = 0;
			if (importConfig.text.title != undefined && importConfig.text.title != null && importConfig.text.title != "") {
				title = importConfig.text.title;
			}
			$(objTitle).val(title);
			var startRow = 1;
			if (importConfig.text.startRow != undefined && importConfig.text.startRow != null && importConfig.text.startRow != "") {
				startRow = importConfig.text.startRow;
			}
			$(objStartRow).val(startRow);
			var separator = "tab";
			if (importConfig.text.separator != undefined && importConfig.text.separator != null && importConfig.text.separator != "") {
				separator = importConfig.text.separator;
			}
			$(objSeparator).val(separator);
			$("#myImportDetail table thead").html("");
			$("#myImportDetail table tbody").html("");
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				if ($(objEncode).val() == null || $(objEncode).val() == "") {
					alert("请输入文件编码！");
					return;
				}
				if ($(objStartRow).val() == null || $(objStartRow).val() == "") {
					alert("请输入明细数据起始行行号！");
					return;
				}
				importConfig.text.encode = $(objEncode).val();
				importConfig.text.title = $(objTitle).val();
				importConfig.text.startRow = $(objStartRow).val();
				importConfig.text.separator = $(objSeparator).val();
				$("#myImportModal").modal("hide");
			});
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_table : function(event) {
			var myImport = event.data.myimport;
			var importConfig = myImport.config;
			$("#myImportModalLabel").html("数据表设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>目标数据库</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option vlaue='R'>远程数据库</option></select>").appendTo(td);
			$("<td>数据表</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTable = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			td = $("<td></td>").appendTo(tr);
			var objIndex = $("<input type='hidden'>").appendTo(td);
			var edit_table = function() {
				var ids = [];
				$.each($("#myImportDetail table tbody input[type='checkbox']"), function() {
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
			$("<input type='button' class='btn btn-default' value='新增'>").click(function() {
				if ($(objTable).val() == "") {
					alert("请输入数据表");
					return;
				}
				var tbody = "";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + $(objTarget).val() + "</td>";
				tbody += "<td>" + $(objTable).val() + "</td>";
				if ($(objIndex).val() == "") {
					//新增
					$("#myImportDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#myImportDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objTarget).val("L");
				$(objTable).val("");
				$(objIndex).val("");
				//增加事件处理
				$("#myImportDetail table tbody input[type='checkbox']").unbind("click");
				$("#myImportDetail table tbody input[type='checkbox']").bind("click", edit_table);
				$("#myImportDetail table tbody")
			}).appendTo(td);
			$("#myImportDetail table thead").html("<tr><th></th><th>目标数据库</th><th>数据表</th></tr>");
			$("#myImportDetail table tbody").html("");
			$.each(importConfig.table, function(n, value) {
				var tbody = "<tr>";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + value.target + "</td>"
				tbody += "<td>" + value.table + "</td>";
				tbody += "</tr>";
				$("#myImportDetail table tbody").append(tbody);
			});
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				var tableData = [];
				$("#myImportDetail table tbody tr").each(function() {
					var target = $(this).find("td:eq(1)").html();
					var tableId = $(this).find("td:eq(2)").html();
					var hasData = false;
					$.each(importConfig.table, function(n, table) {
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
				importConfig.table = [];
				importConfig.table = tableData;
				$("#myImportModal").modal("hide");
				myImport.init();
			});
			$("#myImportDetail table tbody input[type='checkbox']").unbind("click");
			$("#myImportDetail table tbody input[type='checkbox']").bind("click", edit_table);
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_mapping : function(event) {
			var myImport = event.data.myimport;
			var config = myImport.config;
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
			$("#myImportModalLabel").html("字段映射设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>数据库字段</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objField = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>数据来源</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objSourceType = $("<select><option value='excel'>Excel文件</option><option value='sqlFunc'>数据库函数</option><option value='sqlProc'>SQL或存储过程</option><option value='script'>脚本函数</option></select>").appendTo(td);
			$("<td>参数设定</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objParamenter = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>SQL/存储过程<br>脚本函数/数据库函数</td>").appendTo(tr);
			td = $("<td colspan='5'></td>").appendTo(tr);
			var objFunc = $("<textarea class='form-control input-sm fl'></textarea>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>映射类型</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objMappingType = $("<select><option value='title'>明细标题</option><option value='column'>明细列</option><option value='header'>表头位置</option></select>").appendTo(td);
			$("<td>映射字段</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objMappingField = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);
			$("<td>SQL或存储过程<br>目标数据库</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
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
				$.each($("#myImportDetail table tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						ids.push($(this).closest("tr").index());
					}
				});
				if (ids.length <= 0) {
					$(objField).val("");
					$(objSourceType).val("excel");
					$(objParamenter).val("");
					$(objFunc).val("");
					$(objMappingType).val("title");
					$(objMappingField).val("");
					$(objChkField).prop("checked", false);
					$(objRpcField).prop("checked", true);
					$(objTarget).val("L");
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
				var mappingType = $(tr).find("td:eq(5)").html();
				var mappingField = $(tr).find("td:eq(6)").html();
				var chkField = $(tr).find("td:eq(7)").html();
				var rpcField = $(tr).find("td:eq(8)").html();
				var target = $(tr).find("td:eq(9)").html();
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
				if (mappingType != "") {
					$(objMappingType).val(mappingType);
				} else {
					$(objMappingType).val("title");
				}
				$(objMappingField).val(mappingField);
				if (target != "") {
					$(objTarget).val(target);
				} else {
					$(objTarget).val("L");
				}
				$(objIndex).val(ids[0]);
				$(objSourceType).trigger("change");
			};
			$("<input type='button' value='新增'>").click(function () {
				if ($(objField).val() == "") {
					alert("请输入数据库字段！");
					return;
				}
				var tbody = "";
				switch ($(objSourceType).val()) {
				case "excel":
					//Excel文件
					if ($(objMappingField).val() == "") {
						alert("请输入映射字段！");
						return;
					}
					var field = $(objField).val();
					var mappingType = $(objMappingType).val();
					var mappingField = $(objMappingField).val();
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>excel</td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td>" + mappingType + "</td>";
					tbody += "<td>" + mappingField + "</td>";
					break;
				case "sqlFunc":
				case "script":
					//数据库函数  或  数据库函数
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
					var mappingField = $(objMappingField).val();
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>" + $(objSourceType).val() + "</td>";
					tbody += "<td>" + parameter + "</td>";
					tbody += "<td>" + func + "</td>";
					tbody += "<td></td>";
					tbody += "<td>" + mappingField + "</td>";
					break;
				default:
					var field = $(objField).val();
					var mappingType = $(objMappingType).val();
					var mappingField = $(objMappingField).val();
					tbody = "<td><input type='checkbox'></td>";
					tbody += "<td>" + field + "</td>";
					tbody += "<td>excel</td>";
					tbody += "<td></td>";
					tbody += "<td></td>";
					tbody += "<td>" + mappingType + "</td>";
					tbody += "<td>" + mappingField + "</td>";
					tbody += "<td></td>";
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
				if ($(objSourceType).val() == "sqlProc") {
					tbody += "<td>" + $(objTarget).val() + "</td>";
				} else {
					tbody += "<td></td>";
				}
				if ($(objIndex).val() == "") {
					//新增
					$("#myImportDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#myImportDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objField).val("");
				$(objSourceType).val("excel");
				$(objParamenter).val("");
				$(objFunc).val("");
				$(objMappingType).val("title");
				$(objMappingField).val("");
				$(objChkField).prop("checked", false);
				$(objRpcField).prop("checked", true);
				$(objTarget).val("L");
				$(objIndex).val("");
				//checkbox事件
				$("#myImportDetail table tbody input[type='checkbox']").unbind("click");
				$("#myImportDetail table tbody input[type='checkbox']").bind("click", edit_mapping);
			}).appendTo(td);
			//部分控件控制
			$(objParamenter).prop("readonly", true);
			$(objFunc).prop("readonly", true);
			$(objMappingType).prop("disabled", false);
			$(objMappingField).prop("readonly", false);
			$(objTarget).prop("disabled", true);
			$(objSourceType).unbind("change");
			$(objSourceType).bind("change", function() {
				switch ($(objSourceType).val()) {
				case "excel":
					//Excel文件
					$(objParamenter).prop("readonly", true);
					$(objFunc).prop("readonly", true);
					$(objMappingType).prop("disabled", false);
					$(objMappingField).prop("readonly", false);
					$(objTarget).prop("disabled", true);
					break;
				case "sqlFunc":
				case "script":
					//数据库函数  或  数据库函数
					$(objParamenter).prop("readonly", false);
					$(objFunc).prop("readonly", false);
					$(objMappingType).prop("disabled", true);
					$(objMappingField).prop("readonly", true);
					$(objTarget).prop("disabled", true);
					break;
				case "sqlProc":
					//SQL或存储过程
					$(objParamenter).prop("readonly", false);
					$(objFunc).prop("readonly", false);
					$(objMappingType).prop("disabled", true);
					$(objMappingField).prop("readonly", false);
					$(objTarget).prop("disabled", false);
					break;
				default:
					$(objParamenter).prop("readonly", true);
					$(objFunc).prop("readonly", true);
					$(objMappingType).prop("disabled", true);
					$(objMappingField).prop("readonly", true);
					$(objTarget).prop("disabled", true);
					break;
				}
			});
			$("#myImportDetail table thead").html("<tr><th></th><th>数据库字段</th><th>数据来源</th><th>参数设定</th><th>SQL/存储过程<br>脚本函数/数据库函数</th><th>映射类型</th><th>映射字段</th><th>是否数据重复check字段</th><th>是否覆盖</th><th>SQL或存储过程<br>目标数据库</th></tr>");
			$("#myImportDetail table tbody").html("");
			if (table != undefined && table != null) {
				var tbody = "";
				$.each(table.mapping, function(n, value) {
					var param = JSON.stringify(value.parameter);
					if (param == "[]") {
						param = "";
					}
					var target = "";
					if (value.target != undefined && value.target != null && value.target != "") {
						target = value.target;
					}
					tbody += "<tr>";
					tbody += "<td><input type='checkbox'></td>";
					tbody += "<td>" + value.targetField + "</td>"
					tbody += "<td>" + value.source + "</td>";
					tbody += "<td width='20'>" + param + "</td>";
					tbody += "<td>" + value.func + "</td>";
					tbody += "<td>" + value.mapping + "</td>";
					tbody += "<td>" + value.sourceField + "</td>";
					tbody += "<td>" + value.keyField + "</td>";
					tbody += "<td>" + value.replace + "</td>";
					tbody += "<td>" + target + "</td>";
					tbody += "</tr>";
				});
				$("#myImportDetail table tbody").html(tbody);
				//设定checkbox事件
				$("#myImportDetail table tbody input[type='checkbox']").unbind("click");
				$("#myImportDetail table tbody input[type='checkbox']").bind("click", edit_mapping);
				//确定按钮
				$("#btn-import-confirm").unbind("click");
				$("#btn-import-confirm").bind("click", function() {
					table.mapping = [];
					$.each($("#myImportDetail table tbody tr"), function(n, tr) {
						var field = $(tr).find("td:eq(1)").html();
						var sourceType = $(tr).find("td:eq(2)").html();
						var parameter = $(tr).find("td:eq(3)").html();
						var func = $(tr).find("td:eq(4)").html();
						var mappingType = $(tr).find("td:eq(5)").html();
						var mappingField = $(tr).find("td:eq(6)").html();
						var keyField = $(tr).find("td:eq(7)").html();
						var keyReplace = $(tr).find("td:eq(8)").html();
						var target = $(tr).find("td:eq(9)").html();
						var param = [];
						if (parameter != "") {
							try {
								param = eval("(" + parameter + ")");
							} catch(ex) {
								alert(ex);
							}
						}
						table.mapping.push({targetField:field,source:sourceType,parameter:param,func:func,mapping:mappingType,sourceField:mappingField,keyField:keyField,replace:keyReplace,target:target});
					});
					$("#myImportModal").modal("hide");
					myImport.init();
				});
			}
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
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
					tbody += "<td>" + mapping.mapping + "</td>";
					tbody += "<td>" + mapping.sourceField + "</td>";
					tbody += "</tr>";
				});
				$("#dbMapping tbody").html(tbody);
			}
		},
		set_proc : function(event) {
			var myImport = event.data.myimport;
			var config = myImport.config;
			$("#myImportModalLabel").html("存储过程设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>目标数据库</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
			$("<td>执行时点</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objRunTime = $("<select><option value='BefData'>每条数据导入前</option><option value='BakData'>每条数据导入后</option><option value='BefImport'>导入前</option><option value='BakImport'>导入后</option></select>").appendTo(td);
			$("<td>参数设定</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objParameter = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);			
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
				$.each($("#myImportDetail table tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						ids.push($(this).closest("tr").index());
					}
				});
				if (ids.length <= 0) {
					$(objTarget).val("L");
					$(objRunTime).val("BefData");
					$(objParameter).val("");
					$(objProc).val("");
					$(objIndex).val("");
					return;
				} else if (ids.length > 1) {
					alert("只能选择一条数据编辑！");
					$(this).prop("checked", false);
					return;
				}
				var tr = $("#myImportDetail table tbody tr:eq(" + ids[0] + ")");
				$(objTarget).val($(tr).find("td:eq(1)").html());
				$(objRunTime).val($(tr).find("td:eq(2)").html())
				$(objParameter).val($(tr).find("td:eq(3)").html());
				$(objProc).val(decodeURIComponent($(tr).find("td:eq(4)").html()));
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
    			$(objTarget).val("L");
    			$(objRunTime).val("BefData");
				$(objParameter).val("");
				$(objProc).val("");
			}
			$("<input type='button' value='新增'>").click(function () {
				if ($(objProc).val() == "") {
					alert("请输入存储过程或SQL文！");
					return;
				}
				var target = $(objTarget).val();
				var runTime = $(objRunTime).val();
				var parameter = $(objParameter).val();
				var proc = $(objProc).val();
				var tbody = "";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + target + "</td>";
				tbody += "<td>" + runTime + "</td>";
				tbody += "<td>" + parameter + "</td>";
				tbody += "<td>" + encodeURIComponent(proc) + "</td>";
				tbody += "<td><button class='btn btn-default'>删除</button></td>";
				if ($(objIndex).val() == "") {
					//新增
					$("#myImportDetail table tbody").append("<tr>" + tbody + "</tr>");
				} else {
					//修改
					var tr = $("#myImportDetail table tbody tr:eq(" + $(objIndex).val() + ")");
					$(tr).html(tbody);
				}
				$(objTarget).val("L");
				$(objRunTime).val("BefData");
				$(objParameter).val("");
				$(objProc).val("");
				$(objIndex).val("");
				//checkbox事件
				$("#myImportDetail table tbody input[type='checkbox']").unbind("click");
				$("#myImportDetail table tbody input[type='checkbox']").bind("click", edit_proc);
				//删除按钮
				$("#myImportDetail table tbody button").unbind("click");
				$("#myImportDetail table tbody button").bind("click", delete_proc);
			}).appendTo(td);
			$("<input type='button' value='向上'>").click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要调整位置的数据！");
					return;
				}
				if ($(objIndex).val() == "0") {
					return;
				}
				var tr = $("#myImportDetail table tbody tr:eq(" + $(objIndex).val() + ")");
				var prevTr = $(tr).prev();
				prevTr.insertAfter(tr);
				$(objIndex).val(parseInt($(objIndex).val() - 1));
			}).appendTo(td);
			$("<input type='button' value='向下'>").click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要调整位置的数据！");
					return;
				}
				var size = $("#myImportDetail table tbody tr").length - 1;
				if (parseInt($(objIndex).val()) >= size) {
					return;
				}
				var tr = $("#myImportDetail table tbody tr:eq(" + $(objIndex).val() + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
				$(objIndex).val(parseInt($(objIndex).val() + 1));
			}).appendTo(td);
			//设定列表Header
			$("#myImportDetail table thead").html("<tr><th></th><th>目标数据库</th><th>执行时点</th><th>参数</th><th>SQL/存储过程</th><th></th></tr>");
			//设定列表数据
			$("#myImportDetail table tbody").html("");
			if (config.proc != undefined && config.proc != null) {
				var tbody = $("#myImportDetail table tbody");
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
					$("<td>" + proc.proc + "</td>").appendTo(tr);
					td = $("<td></td>").appendTo(tr);
					var btn = $("<button class='btn btn-default' type='button'>删除</button>").appendTo(td);
					$(btn).unbind("click");
					$(btn).bind("click", delete_proc);
				});
			}
			//确定按钮
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				config.proc = [];
				$.each($("#myImportDetail table tbody tr"), function(n, tr) {
					var target = $(tr).find("td:eq(1)").html();
					var runTime = $(tr).find("td:eq(2)").html();
					var parameter = $(tr).find("td:eq(3)").html();
					var proc = $(tr).find("td:eq(4)").html();
					var param = [];
					if (parameter != "") {
						try {
							param = eval("(" + parameter + ")");
						} catch(ex) {
							alert(ex);
						}
					}
					config.proc.push({target:target,runTime:runTime,parameter:param,proc:proc});
				});
				$("#myImportModal").modal("hide");
			});
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_SpaceRow : function(event) {
			alert("1");
			var myImport = event.data.myimport;
			var config = myImport.config;
			$("#myImportModalLabel").html("空行处理设定");
			var headBody = $("#myImportModal form table tbody");
			headBody.html("");
			var tr = $("<tr></tr>").appendTo(headBody);
			$("<td>处理方法</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objMethod = $("<select><option value='error'>报错</option><option value='skip'>跳过</option><option value='normal'>正常处理</option></select>").appendTo(td);
			$("<td>目标数据库</td>").appendTo(tr);
			var td = $("<td></td>").appendTo(tr);
			var objTarget = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td);
			$("<td>参数设定</td>").appendTo(tr);
			td = $("<td></td>").appendTo(tr);
			var objParameter = $("<input type='text' class='form-control input-sm fl w15'>").appendTo(td);			
			tr = $("<tr></tr>").appendTo(headBody);
			$("<td>空行判定存储过程</td>").appendTo(tr);
			td = $("<td colspan='5'></td>").appendTo(tr);
			var objProc = $("<textarea class='form-control input-sm fl'></textarea>").appendTo(td);
			tr = $("<tr></tr>").appendTo(headBody);
			td = $("<td></td>").appendTo(tr);
			$("#myImportDetail table thead").html("");
			$("#myImportDetail table tbody").html("");
			alert(JSON.stringify(config.spaceRow));
			//设定初始值
			if (config.spaceRow != undefined && config.spaceRow != null) {
				alert(config.spaceRow.parameter);
				$(objMethod).val(config.spaceRow.method);
				$(objTarget).val(config.spaceRow.target);
				$(objParameter).val(JSON.stringify(config.spaceRow.parameter));
				$(objProc).val(decodeURIComponent(config.spaceRow.proc));
			}
			//确定按钮
			$("#btn-import-confirm").unbind("click");
			$("#btn-import-confirm").bind("click", function() {
				var parameter = $(objParameter).val();
				var param = [];
				if (parameter != null && parameter != "") {
					param = eval(parameter);
				}
				config.spaceRow = {method:$(objMethod).val(),target:$(objTarget).val(),parameter:param,proc:encodeURIComponent($(objProc).val())};
				$("#myImportModal").modal("hide");
			});
			var modal = $('#myImportModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		}
	};
	$.fn.myimport = function(config) {
		var myimport = new MyImport(this, config);
		return myimport.init();
	}	
})(jQuery);