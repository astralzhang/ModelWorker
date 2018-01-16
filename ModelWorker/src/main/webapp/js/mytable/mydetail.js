(function($){
	var MyDetail = function(ele, opt) {
		this.$element = ele;
		this.defaults = [{
			label:'',
			table:'',
			process:{
				type:'S',
				sql:'',
				fields:[]
			}
		}];
		this.config = $.extend([], this.defaults, opt);
		this.tools = {
			editors : {},
			props : {}
		};
		var myeditor = $.myeditor;
		$.extend(true, this.tools.editors, myeditor);
		var myprops = $.myprops;
		$.extend(this.tools.props, myprops);
	};
	MyDetail.prototype = {
		init : function(n) {
			//alert("init=" + n + ":" + JSON.stringify(this.config));
			var tabIndex = n;
			var objUl = $("<ul class='tab'></ul>").appendTo($("#detail"));
			$.each(this.config, function(n, tab) {
				$("<li>" + tab.label + "</li>").appendTo(objUl);
			});
			var detail = this;
			$(objUl).find("li:eq(" + n + ")").attr("class", "cur");
			$.each(this.config, function(n, tab) {
				var objDiv = $("<div class='tabContent' style='display:none;overflow-x:auto;'></div>").appendTo($("#detail"));
				var objTable = $("<table class='table table-hover'></table>").appendTo(objDiv);
				var objTHead = $("<thead></thead>").appendTo($(objTable));
				var objTBody = $("<tbody></tbody>").appendTo($(objTable));
				var objTh = $("<tr></tr>").appendTo($(objTHead));
				var objTd = $("<tr></tr>").appendTo($(objTBody));
				var editor = null;
				$.each(tab.process.fields, function(n, field) {
					$("<th>" + field.label + "</th>").appendTo($(objTh));
					switch (field.editor) {
					case "label":
						var td = $("<td></td>").appendTo(objTd);
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $("<label " + field.props.controlProp + ">" +  field.props.field + "</label>").appendTo(td);
						} else {
							editor = $("<label>" +  field.props.field + "</label>").appendTo(td);
						}
						//editor = $("<label>" + field.props.field + "</label>").appendTo($(td));
						break;
					case "inputEditor":
						var td = $("<td></td>").appendTo(objTd);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "form-control input-sm fl w15";
						} else {
							css = value.props.css;
						}
						var readonlyType = "";
						if (field.props.readonly != undefined) {
							if (field.props.readonly == "Y") {
								readonlyType = "readonly";
							}
						}
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $("<input type='text' class='" + css + "' " + field.props.controlProp + " value='" + field.props.field + "' " + readonlyType + ">").appendTo(td);
						} else {
							editor = $("<input type='text' class='" + css + "' value='" + field.props.field + "' " + readonlyType + ">").appendTo(td);
						}
						//editor = $("<input type='text' value='" + field.props.field + "'></td>").appendTo(td);
						break;
					case 'selectEditor':
						//下拉框处理
						var td = $("<td></td>").appendTo(objTd);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "form-control2";
						} else {
							css = value.props.css;
						}
						var disabledType = "";
						/*if (field.props.readonly != undefined) {
							if (field.props.readonly == "Y") {
								disabledType = "disabled";
							}
						}*/
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $("<select class='" + css + "' " + field.props.controlProp + " " + disabledType + "></select>").appendTo(td);
						} else {
							editor = $("<select class='" + css + "' " + disabledType + "></select>").appendTo(td);
						}
						//editor = $("<select class='form-control2'></select>").appendTo(td);
						break;
					case 'openEditor':
						//选择框
						var td = $("<td nowrap></td>").appendTo(objTd);
						var div = $("<div></div>").appendTo(td);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "form-control input-sm fl w15";
						} else {
							css = field.props.css;
						}
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $('<input type="text" class="' + css + '" ' + field.props.controlProp + ' value="' + field.props.nameField + '" readonly">').appendTo(div);
						} else {
							editor = $('<input type="text" class="' + css + '" value="' + field.props.nameField + '" readonly>').appendTo(div);
						}
						//editor = $("<input type='text'>").appendTo(td);
						var disabledType = "";
						if (field.props.readonly != undefined) {
							if (field.props.readonly == "Y") {
								disabledType = "disabled";
							}
						}
						var w = $(editor).width();
						if (w <= 0) {
							w = 0;
						}
						$(div).css("float", "left");
						$(div).css("width", w + 30);
						$(editor).css("float", "left");
						$(editor).css("width", w);
						$("<input type='button' style='width:30px;' value='...' " + disabledType + ">").appendTo(div);
						break;
					case 'checkEditor':
						//checkBox
						var td = $("<td></td>").appendTo(objTd);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "";
						} else {
							css = field.props.css;
						}
						var readonlyType = "";
						if (field.props.readonly != undefined) {
							if (field.props.readonly == "Y") {
								readonlyType = "disabled";
							}
						}
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $("<input type='checkbox' class='" + css + "' " + field.props.controlProp + " " + readonlyType + ">").appendTo(td);
						} else {
							editor = $("<input type='checkbox' class='" + css + "' " + readonlyType + ">").appendTo(td);
						}
						//editor = $("<input type='checkbox'>").appendTo(td);
						break;
					case 'mutilInputEditor':
						//多行输入框
						var td = $("<td></td>").appendTo(objTd);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "form-control input-sm fl";
						} else {
							css = field.props.css;
						}
						var readonlyType = "";
						if (field.props.readonly != undefined) {
							if (field.props.readonly == "Y") {
								readonlyType = "disabled";
							}
						}
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $("<textarea class='" + css + "' " + field.props.controlProp + " " + readonlyType + ">" + field.props.field + "</textarea>").appendTo(td);
						} else {
							editor = $("<textarea class='" + css + "' " + readonlyType + ">" + field.props.field + "</textarea>").appendTo(td);
						}
						//editor = $("<textarea>" + field.props.field + "</textarea>").appendTo(td);
						break;
					case 'dateEditor':
						//日期控件
						var td = $("<td></td>").appendTo(objTd);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "form-control input-sm";
						} else {
							css = field.props.css;
						}
						var objDiv = $('<div class="input-group date fl w15"></div>').appendTo(td);
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp != "") {
							editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly ' + field.props.controlProp + ' value="' + field.props.field + '">').appendTo(objDiv);
						} else {
							editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly value="' + field.props.field + '">').appendTo(objDiv);
						}
						//editor = $('<input type="text" class="form-control input-sm" value="" placeholder="请选择日期" readonly>').appendTo(objDiv);
	                    var objSpan = $('<span class="input-group-addon"></span>').appendTo(objDiv);
	                    $('<i class="fa fa-calendar"></i>').appendTo(objSpan);
	                    break;
					case 'image':
						//图像
						var td = $("<td></td>").appendTo(objTd);
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css == "") {
							css = "";
						} else {
							css = field.props.css;
						}
						editor = $('<img class="' + css + '" src="../img/default.jpg">').appendTo(td);
						//editor = $("<img src='../img/default.jpg' height='100'>").appendTo(td);
						break;
					default:
						break;
					}
					if (editor != null) {
						$(editor).bind("click", {field:field, detail:detail,tabIndex:tabIndex}, detail.show_prop);
					}
				});
			});
			$.each($("#detail div[class='tabContent']"),function(){$(this).hide();});
			//$("#detail div[class='tabContent']").eq(0).show();
			$("#btn_detail").unbind("click");
			$("#btn_detail").bind("click", {detail:this}, this.add_tab);
			//页签切换
			if ($(objUl).find("li")) {
				var index = $(objUl).find("li[class='cur']").index();
				$("#detail div[class='tabContent']").eq(index).show();
				$(objUl).find("li").unbind("click");
		        $(objUl).find("li").bind("click", function(){
			        $(objUl).find("li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
					$("#detail div[class='tabContent']").hide().eq($(this).index()).show();
			    });
			}
			//增加字段
			$("#detail div[class='tabContent']").unbind("dblclick");
			$("#detail div[class='tabContent']").bind("dblclick", {detail:this}, this.add_field);
			return this;
		},
		add_tab : function(event) {
			$("#myDetailModalTitle").html("页签管理");
			var detail = event.data.detail;
			var config = detail.config;
			var objForm = $("#myDetailModal form");
			$(objForm).html("");
			var objViewText = $('<input type="text" placeholder="页签显示文本" class="form-control input-sm fl w15">').appendTo(objForm);
			var objTable = $('<input type="text" placeholder="数据库表" class="form-control input-sm fl w15">').appendTo(objForm);
			var objProcessSQL = $('<input type="text" placeholder="取得SQL或存储过程" class="form-control input-sm fl w15">').appendTo(objForm);
			var objProcessType = $('<select class="form-control2"><option value="S">SQL文</option><option value="P">存储过程</option></select>').appendTo(objForm);
			var objTarget = $('<select class="form-control2"><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(objForm);
			var objRowNo = $('<input type="hidden">').appendTo(objForm);
			//编辑页签
			var editTab = function() {
    			if ($(this).prop("checked")) {
    				var ids = [];
    				$("#myDetailModal table tbody input[type='checkbox']:checked").each(function() {
    					if ($(this).prop("checked")) {
    						ids.push($(this).val());
    					}
    				});
    				if (ids.length > 1) {
    					MessageBox.info("只能选择一条数据编辑！");
    					$("#myDetailModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
    					return;
    				}
    				$(objRowNo).val($(this).closest("tr").index());
    				$(objViewText).val($(this).parents("tr").find("td:eq(1)").html());
    				$(objTable).val($(this).parents("tr").find("td:eq(2)").html());
    				$(objProcessSQL).val($(this).parents("tr").find("td:eq(3)").html());
    				$(objProcessType).val($(this).parents("tr").find("td:eq(4)").html());
    				$(objTarget).val($(this).parents("tr").find("td:eq(5)").html());
    			} else {
    				$(objRowNo).val("");
    				$(objViewText).val("");
    				$(objTable).val("");
    				$(objProcessSQL).val("");
    				$(objProcessType).val("S");
    				$(objTarget).val("L");
    			}
    		};
    		//删除页签
    		var deleteTab = function() {
    			if ($(objRowNo).val() == $(this).closest("tr").index()) {
    				$(objRowNo).val("");
    			} else if($(objRowNo).val() > $(this).closest("tr").index()) {
    				$(objRowNo).val($(objRowNo).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			$(objViewText).val("");
				$(objTable).val("");
				$(objProcessSQL).val("");
				$(objProcessType).val("S");
				$(objTarget).val("L")
    		};
			var objAdd = $('<input type="button" value="新增↓" class="btn btn-success">').click(function() {
				if ($(objViewText).val() == null || $(objViewText).val() == "") {
					alert("请输入标签显示文本！");
					return;
				}
				if ($(objTable).val() == null || $(objTable).val() == "") {
					alert("请输入数据表！");
					return;
				}
				/*if ($(objProcessSQL).val() == null || $(objProcessSQL).val() == "") {
					alert("请输入取得SQL文或存储过程！");
					return;
				}*/
				var tdata = "";
				if ($(objRowNo).val() == null || $(objRowNo).val() == "") {
					//新增
					tdata = "<tr>";
					tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objViewText).val() + "</td>";
					tdata += "<td>" + $(objTable).val() + "</td>";
					tdata += "<td>" + $(objProcessSQL).val() + "</td>";
					tdata += "<td>" + $(objProcessType).val() + "</td>";
					tdata += "<td>" + $(objTarget).val() + "</td>";
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
					tdata += "</tr>";
					$("#myDetailModal table tbody").append(tdata);
				} else {
					//修改
        			var index = $(objRowNo).val();
        			var tr = $("#myDetailModal table tbody tr:eq(" + index + ")");
        			tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objViewText).val() + "</td>";
					tdata += "<td>" + $(objTable).val() + "</td>";
					tdata += "<td>" + $(objProcessSQL).val() + "</td>";
					tdata += "<td>" + $(objProcessType).val() + "</td>";
					tdata += "<td>" + $(objTarget).val() + "</td>";
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
        			$(tr).html(tdata);
        			$(objRowNo).val("");
				}
				$("#myDetailModal table tbody input[type='checkbox']").unbind("click");
        		$("#myDetailModal table tbody input[type='checkbox']").bind("click", editTab);
        		$("#myDetailModal table tbody input[type='button']").unbind("click");
        		$("#myDetailModal table tbody input[type='button']").bind("click", deleteTab);
        		$(objRowNo).val("");
				$(objViewText).val("");
				$(objTable).val("");
				$(objProcessSQL).val("");
				$(objProcessType).val("S");
				$(objTarget).val("L");
			}).appendTo(objForm);
			var sTh = "<tr><th>选择</th><th>页签显示文本</th><th>数据库表</th><th>取得SQL或存储过程</th><th>处理类型</th><th>目标数据库</th><th>处理</th></tr>";
			$("#myDetailModal table thead").html(sTh);
			$("#myDetailModal table tbody").html("");
			$.each(config, function(n, value) {
				var tbody = "<tr>";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + value.label + "</td>";
				tbody += "<td>" + value.table + "</td>";
				tbody += "<td>" + value.process.sql + "</td>";
				tbody += "<td>" + value.process.type + "</td>";
				tbody += "<td>" + value.process.target + "</td>";
				tbody += "<td><input type='button' value='删除' class='btn btn-default'></td>";
				tbody += "</tr>";
				$("#myDetailModal table tbody").append(tbody);
			});
			$("#myDetailModal table tbody input[type='checkbox']").unbind("click");
    		$("#myDetailModal table tbody input[type='checkbox']").bind("click", editTab);
    		$("#myDetailModal table tbody input[type='button']").unbind("click");
    		$("#myDetailModal table tbody input[type='button']").bind("click", deleteTab);
			var modal = $('#myDetailModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
	    	//确定按钮
	    	$("#btn-myDetail-confirm").unbind("click");
	    	$("#btn-myDetail-confirm").bind("click", function() {
	    		var data = [];
	    		var indexTable = 0;
	    		$("#myDetailModal table tbody tr").each(function() {
	    			var table = $(this).find("td:eq(2)").html();
	    			var viewText = $(this).find("td:eq(1)").html();
	    			var processSql = $(this).find("td:eq(3)").html();
	    			var processType = $(this).find("td:eq(4)").html();
	    			var target = $(this).find("td:eq(5)").html();
	    			if (config[indexTable].table == table) {
	    				config[indexTable].label = viewText;
	    				config[indexTable].process.type = processType;
	    				config[indexTable].process.sql = processSql;
	    				config[indexTable].process.target = target;
	    			} else {
	    				config.splice(indexTable, 0, {label:viewText,table:table,process:{type:processType,sql:processSql,target:target,fields:[]}});
	    			}
	    			indexTable++;
	    			/*var processed = false;
	    			$.each(config, function(n, value) {
	    				if (value.table == table) {
	    					value.label = viewText;
	    					value.process.type = processType;
	    					value.process.sql = processSql;
	    					processed = true;
	    					data.push(value);
	    				}
	    			});
	    			if (processed == false) {
	    				data.push({label:viewText,table:table,process:{type:processType,sql:processSql,fields:[]}});
	    			}*/
				});
	    		if (indexTable < config.length) {
	    			config.splice(indexTable, config.length - indexTable);
	    		}
	    		//config.splice(0, config.length);
	    		//$.extend(config, data);
	    		//alert(JSON.stringify(config));
	    		$("#detail ul").remove();
	    		$("#detail div[class='tabContent']").remove();
	    		$("#myDetailModal").modal("hide");
	    		detail.init(0);
	    	});
		},
		add_field : function(event) {
			$("#myDetailModalTitle").html("字段设定");
			var detail = event.data.detail;
			var config = detail.config;
			var index = $("#detail ul li[class='cur']").index();
			var tab = config[index];
			var objForm = $("#myDetailModal form");
			$(objForm).html("");
			var objViewText = $('<input type="text" placeholder="显示文本" class="form-control input-sm fl w15">').appendTo(objForm);
			var objEditType = $('<select id="editType" class="form-control2"></select>').appendTo(objForm);
			var options = "<option value='label'>标签</option>";
			options += "<option value='inputEditor'>单行输入框</option>";
			options += "<option value='selectEditor'>下拉框</option>";
			options += "<option value='openEditor'>选择框</option>";
			options += "<option value='checkEditor'>CheckBox</option>";
			options += "<option value='mutilInputEditor'>多行输入框</option>";
			options += "<option value='dateEditor'>日期</option>";
			$(objEditType).html(options);
			$('<label>是否需要提交保存</label>').appendTo(objForm);
			var objSubmit = $('<input type="checkbox" value="Y">').appendTo(objForm);
			var objRowNo = $('<input type="hidden">').appendTo(objForm);
			//编辑字段
			var editField = function() {
    			if ($(this).prop("checked")) {
    				var ids = [];
    				$("#myDetailModal table tbody input[type='checkbox']:checked").each(function() {
    					if ($(this).prop("checked")) {
    						ids.push($(this).val());
    					}
    				});
    				if (ids.length > 1) {
    					MessageBox.info("只能选择一条数据编辑！");
    					$("#myDetailModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
    					return;
    				}
    				$(objRowNo).val($(this).closest("tr").index());
    				$(objViewText).val($(this).parents("tr").find("td:eq(1)").html());
    				$(objEditType).val($(this).parents("tr").find("td:eq(2)").html());
    				if ($(this).parents("tr").find("td:eq(3)").html() == "Y") {
    					$(objSubmit).prop("checked", true);
    				} else {
    					$(objSubmit).prop("checked", false);
    				}
    			} else {
    				$(objRowNo).val("");
    				$(objViewText).val("");
    				$(objEditType).val("label");
    				$(objSubmit).prop("checked", false);
    			}
    		};
    		//删除字段
    		var deleteField = function() {
    			if ($(objRowNo).val() == $(this).closest("tr").index()) {
    				$(objRowNo).val("");
    			} else if($(objRowNo).val() > $(this).closest("tr").index()) {
    				$(objRowNo).val($(objRowNo).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			$(objViewText).val("");
				$(objEditType).val("label");
				$(objSubmit).prop("checked", false);
    		};
    		//新增字段
    		var objAdd = $('<input type="button" value="新增↓" class="btn btn-success">').click(function() {
    			if ($(objViewText).val() == null || $(objViewText).val() == "") {
					alert("请输入显示文本！");
					return;
				}
				if ($(objEditType).val() == null || $(objEditType).val() == "") {
					alert("请输入编辑类型！");
					return;
				}
				var tdata = "";
				if ($(objRowNo).val() == null || $(objRowNo).val() == "") {
					//新增
					tdata = "<tr>";
					tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objViewText).val() + "</td>";
					tdata += "<td>" + $(objEditType).val() + "</td>";
					if ($(objSubmit).prop("checked")) {
						tdata += "<td>Y</td>";
					} else {
						tdata += "<td>N</td>";
					}
					tdata += "<td><input type='button' value='删除' class='btn btn-default'><input type='hidden' value=''></td>";
					tdata += "</tr>";
					$("#myDetailModal table tbody").append(tdata);
				} else {
					//修改
        			var index = $(objRowNo).val();
        			var tr = $("#myDetailModal table tbody tr:eq(" + index + ")");
        			tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objViewText).val() + "</td>";
					tdata += "<td>" + $(objEditType).val() + "</td>";
					if ($(objSubmit).prop("checked")) {
						tdata += "<td>Y</td>";
					} else {
						tdata += "<td>N</td>";
					}
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
        			$(tr).html(tdata);
        			$(objRowNo).val("");
				}
				$("#myDetailModal table tbody input[type='checkbox']").unbind("click");
        		$("#myDetailModal table tbody input[type='checkbox']").bind("click", editField);
        		$("#myDetailModal table tbody input[type='button']").unbind("click");
        		$("#myDetailModal table tbody input[type='button']").bind("click", deleteField);
        		$(objRowNo).val("");
				$(objViewText).val("");
				$(objEditType).val("label");
				$(objSubmit).prop("checked", false);
    		}).appendTo(objForm);
    		//字段位置调整上
    		var objUp = $('<input type="button" value="向上" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myDetailModal table tbody input[type='checkbox']").each(function() {
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
				if (index > 0) {
					var tr = $("#myDetailModal table tbody tr:eq(" + index + ")");
					var prevTr = $(tr).prev();
					prevTr.insertAfter(tr);
				}
    		}).appendTo(objForm);
    		$('<input type="button" value="向下" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myDetailModal table tbody input[type='checkbox']").each(function() {
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
				var tr = $("#myDetailModal table tbody tr:eq(" + index + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
    		}).appendTo(objForm);
			var sTh = "<tr><th>选择</th><th>显示文本</th><th>编辑类型</th><th>是否需要提交保存</th><th>处理</th></tr>";
			$("#myDetailModal table thead").html(sTh);
			$("#myDetailModal table tbody").html("");
			var tbody = "";
			$.each(tab.process.fields, function(n, value) {
				var fieldName = "";
				if (value.editor == "openEditor") {
					if (value.editor.noField != undefined && value.editor.noField != null && value.editor.noField != "") {
						fieldName = value.props.noField;
					} else {
						fieldName = value.props.nameField;
					}
				} else {
					fieldName = value.props.field;
				}
				tbody += "<tr>";
				tbody += "<td><input type='checkbox'></td>";
				tbody += "<td>" + value.label + "</td>";
				tbody += "<td>" + value.editor + "</td>";
				tbody += "<td>" + value.submit + "</td>";
				tbody += "<td><input type='button' value='删除' class='btn btn-default'><input type='hidden' value='" + fieldName + "'>" +  "</td>";
				tbody += "</tr>";
			});
			$("#myDetailModal table tbody").html(tbody);
			$("#myDetailModal table tbody input[type='checkbox']").unbind("click");
			$("#myDetailModal table tbody input[type='checkbox']").bind("click", editField);
			$("#myDetailModal table tbody input[type='button']").unbind("click");
			$("#myDetailModal table tbody input[type='button']").bind("click", deleteField);
			var modal = $('#myDetailModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
	    	//确定按钮
	    	$("#btn-myDetail-confirm").unbind("click");
	    	$("#btn-myDetail-confirm").bind("click", function() {
	    		//tab.process.fields = [];
	    		var len = tab.process.fields.length;
	    		//alert("len=" + len);
	    		//var indexField = 0;
	    		var fieldData = [];
	    		$("#myDetailModal table tbody tr").each(function() {
	    			//alert("1");
	    			var viewText = $(this).find("td:eq(1)").html();
	    			var editType = $(this).find("td:eq(2)").html();
	    			var submit = $(this).find("td:eq(3)").html();
	    			var fieldName = $(this).find("input[type='hidden']").val();
	    			//alert(fieldName);
	    			if (fieldName == "") {
	    				fieldData.push({label:viewText,editor:editType,submit:submit,props:{}});
	    			} else {
	    				//查找原有的field
	    				var hasData = false;
	    				$.each(tab.process.fields, function(n, value) {
	    					//alert(value.editor);
	    					if (value.editor == "openEditor") {
	    						if (value.props.noField != undefined && value.props.noField != null && value.props.noField != "") {
	    							if (value.props.noField == fieldName) {
	    								fieldData.push({label:viewText,editor:editType,submit:submit,props:value.props});
	    								hasData = true;
	    								return false;
	    							}
	    						} else {
	    							if (value.props.nameField == fieldName) {
	    								fieldData.push({label:viewText,editor:editType,submit:submit,props:value.props});
	    								hasData = true;
	    								return false;
	    							}
	    						}
	    					} else {
	    						if (value.props.field == fieldName) {
	    							fieldData.push({label:viewText,editor:editType,submit:submit,props:value.props});
	    							hasData = true;
	    							return false;
	    						}
	    					}
	    				});
	    				if (hasData == false) {
	    					fieldData.push({label:viewText,editor:editType,submit:submit,props:{}});
	    				}
	    			}
				});
	    		tab.process.fields = fieldData;
	    		$("#detail ul").remove();
	    		$("#detail div[class='tabContent']").remove();
	    		$("#myDetailModal").modal("hide");
	    		detail.init(index);
	    	});
		},
		show_prop : function(event) {
			var props = $("#mytable_props").show().draggable();
			var prop_table = props.find("table");
			prop_table.empty();
			var field = event.data.field;
			var detail = event.data.detail;
			var fieldType = field.editor;
			var fieldProps = detail.tools.props[fieldType];
			var tabIndex = event.data.tabIndex;
			var obj = event.target;
			if (field.props == undefined) {
				field.props = {};
			}
			$.each(fieldProps, function(n, value) {
				prop_table.append("<tr><th>" + value.label + "</th><td><div id='P" + n + "'></div></td></tr>");
				if (typeof(field.props[n]) == "undefined") {
					field.props[n] = "";
				}
				value.editor().init(field.props, n, obj);
			});
			$(obj).unbind("update");
			$(obj).bind("update", function() {
				$("#detail ul").remove();
				$("div .tabContent").remove();
				detail.init(tabIndex);
			});
		}
	};
	$.fn.mydetail = function(options) {
		var mydetail = new MyDetail(this, options);
		return mydetail.init(0);
	}	
})(jQuery);