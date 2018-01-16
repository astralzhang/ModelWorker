(function($){
	var MyOpenwin = function(ele, con, vi) {
		this.$element = ele;
		this.defaults = {
			condition:[],
			view:[]
		};
		var opt = {condition:con,view:vi};
		this.config = $.extend({}, this.defaults, opt);
	};
	MyOpenwin.prototype = {
		init : function() {
			//alert(JSON.stringify(this.config));
			//查询条件初始化
			var tbody = "";
			$.each(this.config.condition, function(n, value) {
				tbody += "<tr>";
				tbody += "<td><input type='checkbox' value='" + value.field + "'></td>";
				tbody += "<td>" + value.field + "</td>";
				tbody += "<td>" + value.label + "</td>";
				tbody += "<td>" + value.editor + "</td>";
				tbody += "<td>" + value.compare + "</td>";
				tbody += "<td>" + value.target + "</td>";
				tbody += "<td>" + value.sqlProc + "</td>";
				tbody += "<td>" + value.sql + "</td>";
				tbody += "<td><input type='button' value='删除' class='btn btn-default'></td>";
				tbody += "</tr>";
			});
			$("#condition tbody").html(tbody);
			$("#condition tbody input[type='checkbox']").unbind("click");
    		$("#condition tbody input[type='checkbox']").bind("click", this.select_condition);
    		$("#condition tbody input[type='button']").unbind("click");
    		$("#condition tbody input[type='button']").bind("click", this.delete_condition);
    		//列表初始化
    		var th = "";
    		var tb = "";
    		$.each(this.config.view, function(n, value) {
    			th += "<th>" + value.label + "</th>";
    			tb += "<td>" + value.field + "</td>";
    		});
    		$("#viewField thead").html("<tr>" + th + "</tr>");
    		$("#viewField tbody").html("<tr>" + tb + "</tr>");
			$("#btn-condition").unbind("click");
			$("#btn-condition").bind("click", {list:this}, this.set_condition);
			$("#btn-list").unbind("click");
			$("#btn-list").bind("click", {list:this}, this.set_list);
			return this;
		},
		get_condition : function() {
			var datas = [];
			$.each($("#condition tbody tr"), function () {
				var data = {label:$(this).find("td:eq(2)").html(),
						    field:$(this).find("td:eq(1)").html(),
						    editor:$(this).find("td:eq(3)").html(),
						    compare:$(this).find("td:eq(4)").html(),
						    target:$(this).find("td:eq(5)").html(),
						    sqlProc:$(this).find("td:eq(6)").html(),
						    sql:$(this).find("td:eq(7)").html()};
				datas.push(data);
			});
			return JSON.stringify(datas);
		},
		get_view : function() {
			//alert(JSON.stringify(this.config.view));
			return JSON.stringify(this.config.view);
		},
		set_condition : function(event) {
			var list = event.data.list;
			var config = list.config;
			$("#condition_TargetDatabase").prop("disabled", true);
			$("#condition_SqlProcFlag").prop("disabled", true);
			$("#condition_sql").prop("disabled", true);
			var index = -1;
			$.each($("#condition tbody input[type='checkbox']"), function() {
				if ($(this).prop("checked")) {
					index = $(this).closest("tr").index();
				}
				var tr = $("#condition tbody tr:eq(" + index + ")");
				if (index >= 0) {
					$("#fieldId").val($(tr).find("td:eq(1)").html());
					$("#viewName").val($(tr).find("td:eq(2)").html());
					$("#editorType").val($(tr).find("td:eq(3)").html());
					$("#compareTo").val($(tr).find("td:eq(4)").html());
					$("#condition_TargetDatabse").val($(tr).find("td:eq(5)").html());
					$("#condition_SqlProcFlag").val($(tr).find("td:eq(6)").html());
					$("#condition_sql").val($(tr).find("td:eq(7)").html());
					if ($("#editorType").val() == "selectEditor") {
						$("#condition_TargetDatabase").prop("disabled", false);
						$("#condition_SqlProcFlag").prop("disabled", false);
						$("#condition_sql").prop("disabled", false);
					}
				}
			});
			var modal = $('#myConditionModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
	    	//文本框
	    	$("#editorType").change(function() {
	    		if ($("#editorType").val() == "selectEditor") {
	    			//下拉框
	    			$("#condition_TargetDatabase").prop("disabled", false);
	    			$("#condition_SqlProcFlag").prop("disabled", false);
	    			$("#condition_sql").prop("disabled", false);
	    		} else {
	    			$("#condition_TargetDatabase").prop("disabled", true);
	    			$("#condition_SqlProcFlag").prop("disabled", true);
	    			$("#condition_sql").prop("disabled", true);
	    		}
	    	});
	    	//确定
	    	$("#btn-condition-confirm").unbind("click");
	    	$("#btn-condition-confirm").bind("click", function() {
	    		var index = -1;
	    		$("#condition input[type='checkbox']").each(function() {
	    			if ($(this).prop("checked")) {
	    				index = $(this).closest("tr").index();
	    				return false;
	    			}
	    		});
	    		if ($("#fieldId").val() == null || $("#fieldId").val() == "") {
	    			alert("请输入字段ID");
	    			return;
	    		}
	    		if ($("#viewName").val() == null || $("#viewName").val() == "") {
	    			alert("请输入显示名");
	    			return;
	    		}
	    		if ($("#editorType").val() == "selectEditor") {
	    			if ($("#condition_sql").val() == null || $("#condition_sql").val() == "") {
	    				alert("请输入SQL/存储过程！");
	    				return;
	    			}
	    		}
	    		if (index >= 0) {
	    			//修改
	    			var objTr = $("#condition tbody tr:eq(" + index + ")");
	    			tr += "<td><input type='checkbox' value='" + $("#fieldId").val() + "'></td>";
	    			tr += "<td>" + $("#fieldId").val() + "</td>";
	    			tr += "<td>" + $("#viewName").val() + "</td>";
	    			tr += "<td>" + $("#editorType").val() + "</td>";
	    			tr += "<td>" + $("#compareTo").val() + "</td>";
	    			tr += "<td>" + $("#condition_TargetDatabase").val() + "</td>";
	    			tr += "<td>" + $("#condition_SqlProcFlag").val() + "</td>";
	    			tr += "<td>" + $("#condition_sql").val() + "</td>";
	    			tr += "<td><input type='button' value='删除' class='btn btn-default'></td>";
	    			$(objTr).html(tr);
	    		} else {
	    			//新增
	    			var tr = "";
	    			tr += "<tr>";
	    			tr += "<td><input type='checkbox' value='" + $("#fieldId").val() + "'></td>";
	    			tr += "<td>" + $("#fieldId").val() + "</td>";
	    			tr += "<td>" + $("#viewName").val() + "</td>";
	    			tr += "<td>" + $("#editorType").val() + "</td>";
	    			tr += "<td>" + $("#compareTo").val() + "</td>";
	    			tr += "<td>" + $("#condition_TargetDatabase").val() + "</td>";
	    			tr += "<td>" + $("#condition_SqlProcFlag").val() + "</td>";
	    			tr += "<td>" + $("#condition_sql").val() + "</td>";
	    			tr += "<td><input type='button' value='删除' class='btn btn-default'></td>";
	    			tr += "</tr>";
	    			$("#condition tbody").append(tr);
	    		}
	    		$("#condition tbody input[type='checkbox']").unbind("click");
	    		$("#condition tbody input[type='checkbox']").bind("click", list.select_condition);
	    		$("#condition tbody input[type='button']").unbind("click");
	    		$("#condition tbody input[type='button']").bind("click", list.delete_condition);
	    		$("#fieldId").val("");
    			$("#viewName").val("");
    			$("#editorType").val("inputEditor");
    			$("#compareTo").val("eq");
    			$("#condition_TargetDatabase").val("L");
    			$("#condition_SqlProcFlag").val("S");
    			$("#condition_sql").val(""); 
	    		$("#myConditionModal").modal("hide");
	    	});
		},
		select_condition : function(event) {
			if ($(this).prop("checked")) {
    			var index = 0;
    			$.each($("#condition tbody input[type='checkbox']"), function() {
    				if ($(this).prop("checked")) {
    					index++;
    				}
    			});
    			if (index > 1) {
    				alert("只能选择条数据编辑！");
    				$(this).prop("checked", false);
    				return;
    			}
    		}
		},
		delete_condition : function(event) {
			var tr = $(this).closest("tr")
    		$(tr).remove();
		},
		set_list : function(event) {
			var list = event.data.list;
			var config = list.config;
			$("#myListModalTitle").html("设定列表项目");
			var objForm = $("#myListModal form");
			objForm.html("");			
			var labelField = $('<input type="text" placeholder="字段名称" class="form-control input-sm fl w15">').appendTo(objForm);
			var textField = $('<input type="text" placeholder="字段ID" class="form-control input-sm fl w15">').appendTo(objForm)
			var objIndex = $('<input type="hidden">').appendTo(objForm);
			//var objDiv = $('<div class="search"></div>').appendTo(objForm);
			var tHead = $("#myListModal table thead");
			$(tHead).html("");
			$(tHead).html("<tr><td></td><td>字段名称</td><td>字段ID</td><td>处理</td></tr>");
			var tbody = $("#myListModal table tbody");
			$(tbody).html("");
			//编辑字段
			var editField = function() {
				if ($(this).prop("checked")) {
    				var ids = [];
    				$("#myListModal table tbody input[type='checkbox']:checked").each(function() {
    					if ($(this).prop("checked")) {
    						ids.push($(this).val());
    					}
    				});
    				if (ids.length > 1) {
    					MessageBox.info("只能选择一条数据编辑！");
    					$(this).prop("checked", false);
    					return;
    				}
    				$(objIndex).val($(this).closest("tr").index());
    				$(labelField).val($(this).parents("tr").find("td:eq(1)").html());
    				$(textField).val($(this).parents("tr").find("td:eq(2)").html());
    			} else {
    				$(labelField).val("");
    				$(textField).val("");
    				$(objIndex).val("");
    			}
			};
			//删除字段
			var deleteField = function() {
				$(labelField).val("");
				$(textField).val("");
				$(this).closest("tr").remove();
			};
			var tableData = "";
			$.each(config.view, function(n, value) {
				tableData += "<tr>";
				tableData += "<td><input type='checkbox' value='" + value.field + "'></td>";
				tableData += "<td>" + value.label + "</td>";
				tableData += "<td>" + value.field + "</td>";
				tableData += "<td><input type='button' value='删除' class='btn btn-default'>" + "</td>";
				tableData += "</tr>";
			});
			$(tbody).html(tableData);
			$("#myListModal table tbody input[type='checkbox']").unbind("click");
			$("#myListModal table tbody input[type='checkbox']").bind("click", editField);
			$("#myListModal table tbody input[type='button']").unbind("click");
			$("#myListModal table tbody input[type='button']").bind("click", deleteField);
			var objAddButton = $('<input type="button" class="btn btn-success" value="新增">').click(function() {
				if ($(labelField).val() == null || $(labelField).val() == "") {
					alert("请输入字段名称！");
					return;
				}
				if ($(textField).val() == null || $(textField).val() == "") {
					alert("请输入字段ID！");
					return;
				}
				if ($(objIndex).val() == null || $(objIndex).val() == "") {
					var tdata = "<tr>";
					tdata += "<td><input type='checkbox' value='" + $(textField).val() + "'></td>";
					tdata += "<td>" + $(labelField).val() + "</td>";
					tdata += "<td>" + $(textField).val() + "</td>";
					tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
					tdata += "</tr>";
					$(tbody).append(tdata);
				} else {
					var index = $(objIndex).val();
        			var tr = $("#myListModal table tbody tr:eq(" + index + ")");
        			var tdata = "<td><input type='checkbox' value='" + $(textField).val() + "'></td>";
					tdata += "<td>" + $(labelField).val() + "</td>";
					tdata += "<td>" + $(textField).val() + "</td>";
					tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
					$(tr).html(tdata);
				}
				$(labelField).val("");
				$(textField).val("");
				$(objIndex).val("");
				$("#myListModal table tbody input[type='checkbox']").unbind("click");
				$("#myListModal table tbody input[type='checkbox']").bind("click", editField);
				$("#myListModal table tbody input[type='button']").unbind("click");
				$("#myListModal table tbody input[type='button']").bind("click", deleteField);
			}).appendTo(objForm);
			//条件位置调整上
    		var objUp = $('<input type="button" value="向上" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myListModal table tbody input[type='checkbox']").each(function() {
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
					var tr = $("#myListModal table tbody tr:eq(" + index + ")");
					var prevTr = $(tr).prev();
					prevTr.insertAfter(tr);
				}
    		}).appendTo(objForm);
    		$('<input type="button" value="向下" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myListModal table tbody input[type='checkbox']").each(function() {
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
				var tr = $("#myListModal table tbody tr:eq(" + index + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
    		}).appendTo(objForm);
			$("#btn-myList-confirm").unbind("click");
			$("#btn-myList-confirm").bind("click", function() {
				config.view = [];
				var th = "";
				var tb = "";
				$("#myListModal table tbody tr").each(function() {
	    			var field = $(this).find("td:eq(2)").html();
	    			var viewText = $(this).find("td:eq(1)").html();
	    			config.view.push({label:viewText,field:field});
	    			th += "<th>" + viewText + "</th>";
	    			tb += "<td>" + field + "</td>";
				});
				$("#viewField thead").html("<tr>" + th + "</tr>");
				$("#viewField tbody").html("<tr>" + tb + "</tr>");
				$("#myListModal").modal("hide");
			});
			var modal = $('#myListModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		}
	};
	$.fn.myopenwin = function(condition, view) {
		var myopenwin = new MyOpenwin(this, condition, view);
		return myopenwin.init();
	}	
})(jQuery);