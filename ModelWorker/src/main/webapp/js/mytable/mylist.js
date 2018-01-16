(function($){
	var MyList = function(ele, opt) {
		this.$element = ele;
		this.defaults = {
			type:'S',
			sql:'',
			condition:[],
			view:[]
		};
		this.tools = {
			editors : {},
			props : {}
		};
		this.config = $.extend({}, this.defaults, opt);
		var myeditor = $.myeditor;
		$.extend(true, this.tools.editors, myeditor);
		var myprops = $.myprops;
		$.extend(this.tools.props, myprops);
		//this.config = $.extend({}, this.defaults, opt);
	};
	MyList.prototype = {
		init : function() {
			//alert(JSON.stringify(this.config));
			$("#btn-list-sql").unbind("click");
			$("#btn-list-sql").bind("click", {list:this}, this.set_list_sql);
			$("#btn-list-view").unbind("click");
			$("#btn-list-view").bind("click", {list:this}, this.set_list_view);
			$("#btn-list-condition").unbind("click");
			$("#btn-list-condition").bind("click", {list:this}, this.set_list_condition);
			return this;
		},
		set_list_sql : function(event) {
			//alert("S");
			var list = event.data.list;
			var config = list.config;
			$("#myListModalTitle").html("列表SQL设定");
			var objForm = $("#myListModal form");
			objForm.html("");			
			var objSql = $('<textarea placeholder="SQL文或存储过程" class="form-control input-sm fl w5" style="width:100%;"></textarea>').appendTo(objForm);
			$('<label>是否存储过程</label>').appendTo(objForm);
			var objType = $('<input type="checkbox">').appendTo(objForm);
			
			$("<label>&nbsp;&nbsp;&nbsp;&nbsp;执行时点</label>").appendTo(objForm);
			var objRunTime = $('<select><option value="0">立即执行</option><option value="1">查询后执行</option></select>').appendTo(objForm);
			$("#myListModal table thead").html("");
			$("#myListModal table tbody").html("");
			$("#btn-myList-confirm").unbind("click");
			$("#btn-myList-confirm").bind("click", function() {
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
				//alert(JSON.stringify(config));
				$("#myListModal").modal("hide");
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
			var modal = $('#myListModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_list_view : function(event) {
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
    					$("#myListModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
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
				$("#myListModal table tbody tr").each(function() {
	    			var field = $(this).find("td:eq(2)").html();
	    			var viewText = $(this).find("td:eq(1)").html();
	    			config.view.push({label:viewText,field:field});
				});
				//alert(JSON.stringify(view));
				$("#myListModal").modal("hide");
			});
			var modal = $('#myListModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_list_condition : function(event) {			
			$("#myListModalTitle").html("条件设定");
			var list = event.data.list;
			var config = list.config;
			var objForm = $("#myListModal form");
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
					var tr = $("<tr></tr>").appendTo("#MyListTableData tbody");
					addNewRow($(objViewText).val(), $(objField).val(), $(objFieldAlias).val(), $(objEditType).val(), $(objCompare).val(), $(objOpenWindow).val(), encodeURIComponent($(objSql).val()), $(objTarget).val(), $(objDefaultType).val(), $(objDefault).val(), $(objDefaultTarget).val(), $(objDefaultField).val(), tr);
				} else {
					//修改
        			var index = $(objRowNo).val();
        			var tr = $("#MyListTableData tbody tr:eq(" + index + ")");
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
        		$("#MyListTableData tbody input[type='button']").unbind("click");
        		$("#MyListTableData tbody input[type='button']").bind("click", deleteCondition);        		
    		}).appendTo(objForm);
    		//条件位置调整上
    		var objUp = $('<input type="button" value="向上" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#MyListTableData tbody input[type='checkbox']").each(function() {
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
					var tr = $("#MyListTableData tbody tr:eq(" + index + ")");
					var prevTr = $(tr).prev();
					prevTr.insertAfter(tr);
				}
    		}).appendTo(objForm);
    		$('<input type="button" value="向下" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#MyListTableData tbody input[type='checkbox']").each(function() {
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
			$("#MyListTableData thead").html(sTh);
			$("#MyListTableData tbody").html("");
			//alert(JSON.stringify(config.condition));
			$.each(config.condition, function(n, value) {
				var tr = $("<tr></tr>").appendTo($("#MyListTableData tbody"));
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
			//$("#myListModal table tbody input[type='checkbox']").unbind("click");
			//$("#myListModal table tbody input[type='checkbox']").bind("click", editCondition);
			$("#MyListTableData tbody input[type='button']").unbind("click");
			$("#MyListTableData tbody input[type='button']").bind("click", deleteCondition);
			var modal = $('#myListModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
	    	//确定按钮
	    	$("#btn-myList-confirm").unbind("click");
	    	$("#btn-myList-confirm").bind("click", function() {
	    		config.condition.splice(0, config.condition.length);
	    		$("#MyListTableData tbody tr").each(function() {
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
	    		//alert(JSON.stringify(config.condition));
	    		$("#myListModal").modal("hide");
	    	});
		}
	};
	$.fn.mylist = function(options) {
		var mylist = new MyList(this, options);
		return mylist.init();
	}	
})(jQuery);