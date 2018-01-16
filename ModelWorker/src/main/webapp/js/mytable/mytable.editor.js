(function($){
var myeditor = {};
$.extend(true, myeditor, {
	//标签
	label : function() {
		var _props,_div,_name;
		this.init = function(props, name, obj) {
			_props=props;
			_div = "P" + name;
			_name = name;
			_obj = obj;
			$('<input style="width:100%;"/>').val(props[name]).change(function(){
				if ($(this).val() == null || $(this).val() == "") {
					$(_obj).html("标签");
				} else {
					$(_obj).html($(this).val());
				}
				props[name] = $(this).val();
				$(_obj).trigger("update");
			}).appendTo('#P'+ name);
		}
		this.destroy = function() {
			$('#'+_div+' input').each(function(){
				_props[_name] = $(this).val();
				$(_obj).html(_props[_name]);
			});
		}
	},
	//输入框
	inputEditor : function(){
		var _props,_div,_name,_obj;
		this.init = function(props, name, obj){
			_props=props;
			_div = "P" + name;
			_name = name;
			_obj = obj;
			$('<input style="width:100%;"/>').val(props[name]).change(function(){
				props[name] = $(this).val();
				switch ($(_obj).attr("type")) {
				case "checkbox":
					if (name == "viewText") {
						$("<span>" + props[name] + "</span>").appendTo($(_obj).closest("td"));
					}
					break;
				default:
					if (name == "field") {
						$(_obj).val(props[name]);
					}
					break;
				}
				$(_obj).trigger("update");
			}).appendTo('#P'+ name);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_name] = $(this).val();
			});
		}
	},
	//checkBox
	checkEditor : function() {
		var _props, _div, _name, _obj;
		this.init = function(props, name, obj) {
			_props = props;
			_div = "P" + name;
			_name = name;
			_obj = obj;
			var objCheckBox = null;
			//alert(_div);
			if (props[name] == "Y") {
				objCheckBox = $('<input type="checkbox" checked />').appendTo("#" + _div);
			} else {
				objCheckBox = $('<input type="checkbox" />').appendTo("#" + _div);
			}
			$(objCheckBox).click(function() {
				if ($(this).prop("checked")) {
					props[name] = "Y";
				} else {
					props[name] = "N";
				}
				$(_obj).trigger("update");
			});
			
		}
		this.destroy = function() {
			$("#" + _div + " input").each(function () {
				if ($(this).type == "checkbox") {
					if ($(this).prop("checked")) {
						_props[_name] = "Y";
					} else {
						_props[_name] = "N";
					}
				} else {
					_props[_name] = $(this).val();
				}
			});
		}
	},
	//日期控件
	dateEditor:function() {
		var _props, _div, _name, _obj;
		var txtDate;
		this.init = function(props, name, obj){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			txtDate = $('<input style="width:100%;" placeholder="选择时间" readonly/>').appendTo('#' + _div);
			$(txtDate).change(function(){
				props[_k].value = $(this).val();
				$(_obj).trigger("update");
			});
			$(txtDate).datepicker();
			$(txtDate).datepicker("option", "dateFormat", "yy-mm-dd");
			$(txtDate).val(props[_k].value);
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(txtDate).val();
			});
		}
	},
	//事件控件
	eventEditor:function(){
		var _props, _name, _div, _obj;
		this.init = function(props, name, obj) {
			_props = props;
			_div = "P" + name;
			_name = name;
			_obj = obj;
			var hid = $('<input type="hidden"/>').change(function(){
				props[name] = $(this).val();
			}).appendTo('#' + _div);
			$(hid).val(props[name]);
			var btn = $('<input type="button" value="事件" style="width:100%;"/>').click(function(){
				$(hid).val($(hid).val());
				$("#myTableModalTitle").html("事件设定");
				//表单部分数据做成
				var objForm = $("#myTableModal form");
				$(objForm).html("");
				var objEventNo = $('<input type="text" id="eventNo" placeholder="事件名称" class="form-control input-sm fl w1">').appendTo(objForm);
				$('<br />').appendTo(objForm);
                var objEventFunc = $('<textarea id="eventFunc" placeholder="事件函数" class="form-control input-sm fl w1"></textarea>').appendTo(objForm);
                var objRowIndex = $('<input type="hidden" id="rowIndex"/>').appendTo(objForm);
                var objDiv = $('<div class="search"></div>').appendTo(objForm);
                //事件编辑函数
                var editEvent = function() {
        			if ($(this).prop("checked")) {
        				var ids = [];
        				$("#myTableModal table tbody input[type='checkbox']:checked").each(function() {
        					if ($(this).prop("checked")) {
        						ids.push($(this).val());
        					}
        				});
        				if (ids.length > 1) {
        					MessageBox.info("只能选择一条数据编辑！");
        					$("#myTableModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
        					return;
        				}
        				$(objRowIndex).val($(this).closest("tr").index());
        				$(objEventNo).val($(this).val());
        				$(objEventFunc).val($(this).parents("tr").find("td:eq(2)").html());
        			} else {
        				$(objEventNo).val("");
        				$(objEventFunc).val("");
        				$(objRowIndex).val("");
        			}
        		};
                //事件删除函数
                var deleteEvent = function() {
                	if ($(objRowIndex).val() == $(this).closest("tr").index()) {
        				$(objRowIndex).val("");
        			} else if($(objRowIndex).val() > $(this).closest("tr").index()) {
        				$(objRowIndex).val($(objRowIndex).val() - 1);
        			}
                	$(this).closest("tr").remove();
                	$(objEventNo).val("");
                	$(objEventFunc).val("");
                };
                //新增按钮
                $('<button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-event">新增↓</button>').appendTo(objDiv).click(function() {
            		var no = $(objEventNo).val();
            		var func = $(objEventFunc).val();
            		if (no == null || no == "") {
            			alert("请输入事件名称！");
            			return;
            		}
            		if (func == null || func == "") {
            			alert("请输入事件处理过程！");
            			return;
            		}
            		if ($(objRowIndex).val() == "" || $(objRowIndex).val() == null) {
            			//新增
            			var data = "<tr>";
            			data += "<td><input type='checkbox' value='" + no + "'></td>";
            			data += "<td>" + no + "</td>";
            			data += "<td>" + func + "</td>";
            			data += "<td><input type='button' value='删除' class='btn btn-default'></td>"
            			data += "</tr>";
            			$("#myTableModal table tbody").append(data);
            		} else {
            			//修改
            			var index = $(objRowIndex).val();
            			var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
            			var data = "<td><input type='checkbox' value='" + no + "'></td>";
            			data += "<td>" + no + "</td>";
            			data += "<td>" + func + "</td>";
            			data += "<td><input type='button' value='删除' class='btn btn-default'></td>"
            			$(tr).html(data);
            			$(objRowIndex).val("");
            		}
            		$("#myTableModal table tbody input[type='checkbox']").unbind("click");
            		$("#myTableModal table tbody input[type='checkbox']").bind("click", editEvent);
            		$("#myTableModal table tbody input[type='button']").unbind("click");
            		$("#myTableModal table tbody input[type='button']").bind("click", deleteEvent);
            		$(objEventNo).val("");
            		$(objEventFunc).val("");
                });
                var objHeader = $("#myTableModal table thead");
                $(objHeader).html("<tr><th>选择</th><th>事件</th><th>事件函数</th><th>删除</th></tr>");
                var objBody = $("#myTableModal table tbody");
                $(objBody).html("");
                //生成已有函数
                if ($(hid).val() != null && $(hid).val() != "") {
                	var data = eval("(" + $(hid).val() + ")");
        			var tdata = "";
        			$.each(data, function(n, value) {
        				tdata += "<tr>";
        				tdata += "<td><input type='checkbox' value='" + value.eventNo + "'></td>";
        				tdata += "<td>" + value.eventNo + "</td>";
        				tdata += "<td>" + decodeURIComponent(value.eventFunc) + "</td>";
        				tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
        				tdata += "</tr>";
        			});                    
                    $(objBody).html(tdata);
                    $("#myTableModal table tbody input[type='checkbox']").unbind("click");
            		$("#myTableModal table tbody input[type='checkbox']").bind("click", editEvent);
            		$("#myTableModal table tbody input[type='button']").unbind("click");
            		$("#myTableModal table tbody input[type='button']").bind("click", deleteEvent);
                }
                //确定按钮事件
                $("#btn-myTable-confirm").unbind("click");
				$("#btn-myTable-confirm").bind("click", function() {
					var data = "";
					$("#myTableModal table tbody tr").each(function() {
						data += "{eventNo:\"" + $(this).find("td:eq(1)").html() + "\",";
						data += "eventFunc:\"" + encodeURIComponent($(this).find("td:eq(2)").html()) + "\"},"
					});
					if (data.length > 0) {
						data = data.substr(0, data.length - 1);
					}
					$(hid).val("[" + data + "]");
					$(hid).trigger("change");
					$('#myTableModal').modal('hide');
				});
                var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}).appendTo('#'+_div);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_name].value = $(hid).val();
			});
		}
	},
	//sql或存储过程编辑
	sqlEditor:function() {
		var _props, _name, _div, _obj;
		this.init = function(props, name, obj) {
			_props = props;
			_name = name;
			_obj = obj;
			_div = "P" + name;
			var hid = null;
			hid = $("<input type='hidden'>").val(props[name]).change(function() {
				props[name] = $(this).val();
			}).appendTo("#" + _div);
			$('<input type="button" value="SQL编辑" style="width:100%;"/>').click(function() {
				$("#myTableModalTitle").html("SQL设定");
				$("#myTableModal form").html("");
				var sData = $(hid).val();
				var data = null;
				if (sData == null || sData == "") {
					data = {type:"",sql:""};
				} else {
					data = eval("(" + $(hid).val() + ")");
				}
				var objTable = $('<table width="100%"></table>').appendTo($("#myTableModal form"));
				var tr1 = $("<tr></tr>").appendTo(objTable);
				var td1_1 = $("<td align='left'></td>").appendTo(tr1);
				var viewData = "";
				if (data.type == "SQL") {
					viewData = '类型：<input type="radio" name="sqlProcType" value="SQL" checked>SQL文<input type="radio" name="sqlProcType" value="PROC">存储过程';
				} else if (data.type == "PROC") {
					viewData = '类型：<input type="radio" name="sqlProcType" value="SQL">SQL文<input type="radio" name="sqlProcType" value="PROC" checked>存储过程';
				} else {
					viewData = '类型：<input type="radio" name="sqlProcType" value="SQL">SQL文<input type="radio" name="sqlProcType" value="PROC">存储过程';
				}
				$(td1_1).html(viewData);
				var tr2 = $("<tr></tr>").appendTo(objTable);
				var td2_1 = $("<td></td>").appendTo(tr2);
				var txtSql = $('<textarea id="sqlProc" placeholder="SQL文或存储过程" class="form-control input-sm fl w1"></textarea>').appendTo($(td2_1));
				$(txtSql).val(data.sql);
				//var objDiv = $('<div class="search">').appendTo($("#myTableModal form"));
				//var btnAdd = $('<button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-event">新增↓</button>').appendTo(objDiv);
				$("#myTableModal table:eq(1)").find("thead").html("");
				$("#myTableModal table:eq(1)").find("tbody").html("");
				$("#btn-myTable-confirm").unbind("click");
				$("#btn-myTable-confirm").bind("click", function(){
					var sqlProcType = $("#myTableModal form input[name='sqlProcType']:checked").val();
					if (sqlProcType == null || sqlProcType == "") {
						MessageBox.info("请选择类型！");
						return;
					}
					if ($(txtSql).val() == "") {
						MessageBox.info("请输入SQL文或存储过程！");
						return;
					}
					var data = "{type:\"" + sqlProcType + "\",sql:\"" + $(txtSql).val() + "\"}";
					$(hid).val(data);
					$(hid).trigger("change");
					$("#myTableModal").modal("hide");
				});
				var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}).appendTo("#" + _div);
		},
		this.destroy = function() {
			$('#'+_div+' input').each(function(){
				_props[_name].value = $(hid).val();
			});
		};
	},
	//Server事件编辑
	serverEditor:function() {
		var _props, _name, _div, _obj;
		this.init = function(props, name, obj) {
			_props = props;
			_name = name;
			_obj = obj;
			_div = "P" + name;
			var hid = null;
			hid = $("<input type='hidden'>").val(props[name]).change(function() {
				props[name] = $(this).val();
			}).appendTo("#" + _div);
			$('<input type="button" value="服务器事件" style="width:100%;"/>').click(function() {
				$("#myTableModalTitle").html("服务器事件设定");
				$("#myTableModal form").html("");
				var sData = $(hid).val();
				var data = null;
				if (sData == null || sData == "") {
					data = {type:"",sql:""};
				} else {
					data = eval("(" + $(hid).val() + ")");
				}
				var objTable = $('<table width="100%"></table>').appendTo($("#myTableModal form"));
				var tr1 = $("<tr></tr>").appendTo(objTable);
				var td1_1 = $("<td align='left'></td>").appendTo(tr1);
				var objEvent = $("<input type='text' placeholder='事件名称'>").appendTo(td1_1);
				if (data.event != undefined && data.event != null && data.event != "") {
					$(objEvent).val(data.event);
				}
				var dbType = $("<select><option value='L'>本地数据库</option><option value='R'>远程数据库</option></select>").appendTo(td1_1);
				if (data.target != undefined && data.target != null && data.target != "") {
					$(dbType).val(data.target);
				}
				var objDetailParam = $("<input type='text' placeholder='参数的明细表'>").appendTo(td1_1);
				if (data.detail != undefined && data.detail != null && data.detail != "") {
					$(objDetailParam).val(data.detail);
				}
				var tr2 = $("<tr></tr>").appendTo(objTable);
				var td2_1 = $("<td></td>").appendTo(tr2);
				var txtSql = $('<textarea id="sqlProc" placeholder="SQL文或存储过程" class="form-control input-sm fl w1"></textarea>').appendTo($(td2_1));
				if (data.sql != undefined && data.sql != null) {
					$(txtSql).val(decodeURIComponent(data.sql));
				}
				var tr3 = $("<tr></tr>").appendTo(objTable);
				var td3_1 = $("<td></td>").appendTo(tr3);
				var txtFunc = $('<textarea placeholder="存储过程执行前check函数，返回true执行、返回false不执行" class="form-control input-sm fl w1"></textarea>').appendTo(td3_1);
				if (data.checkFunc != undefined && data.checkFunc != null && data.checkFunc != "") {
					$(txtFunc).val(decodeURIComponent(data.checkFunc));
				}
				var objRowNo = $("<input type='hidden'>");
				var objDiv = $('<div class="search">').appendTo($("#myTableModal form"));
				var objSourceField = $("<input type='text' placeholder='SQL字段名称'>").appendTo(objDiv);
				var objTargetFlag = $("<select><option value='head'>表头</option><option value='detail'>表体</option></select>").appendTo(objDiv);
				var objDetailTable = $("<input type='text' placeholder='设定明细表' disabled>").appendTo(objDiv);
				var objTargetField = $("<input type='text' placeholder='字段名称'>").appendTo(objDiv);
				var objDetailSet = $("<select disabled><option value='current'>设定当前明细行</option><option value='all'>设定所有明细行</option><option value='first'>设定明细第一行</option><option value='last'>设定明细最后一行</option></select>").appendTo(objDiv);
				$(objTargetFlag).change(function() {
					if ($(objTargetFlag).val() == "detail") {
						$(objDetailTable).prop("disabled", false);
						$(objDetailSet).prop("disabled", false);
					} else {
						$(objDetailTable).prop("disabled", true);
						$(objDetailSet).prop("disabled", true);
					}
				});
				var editServer = function() {
					var ids = [];
					$.each($("#myTableModal table:eq(1) tbody tr input[type='checkbox']:checked"), function() {
						ids.push($(this).closest("tr").index());
					});
					if (ids.length <= 0) {
						$(objSourceField).val("");
						$(objTargetField).val("");
						$(objTargetFlag).val("head");
						$(objDetailSet).prop("disabled", true);
						$(objDetailTable).prop("disabled", true);
						return;
					}
					if (ids.length > 1) {
						$(this).prop("checked", false);
						MessageBox.info("只能选择一条数据编辑！");
						return;
					}
					$(objRowNo).val(ids[0]);
					var tr = $("#myTableModal table:eq(1)").find("tbody tr:eq(" + ids[0] + ")");
					$(objSourceField).val($(tr).find("td:eq(1)").html());
					$(objTargetFlag).val($(tr).find("td:eq(2)").html());
					$(objTargetField).val($(tr).find("td:eq(4)").html());
					if ($(objTargetFlag).val() == "detail") {
						$(objDetailSet).val($(tr).find("td:eq(5)").html());
						$(objDetailSet).prop("disabled", false);
						$(objDetailTable).val($(tr).find("td:eq(3)").html());
						$(objDetailTable).prop("disabled", false);
					} else {
						$(objDetailSet).prop("disabled", true);
						$(objDetailTable).val("");
						$(objDetailTable).prop("disabled", true);
					}
				};
				var deleteServer = function() {
					var tr = $(this).closest("tr");
					tr.remove();
				};
				var btnAdd = $('<button type="button" class="btn btn-success fa fa-search" id="btn-add-event">新增↓</button>').click(function() {
					if ($(objSourceField).val() == null || $(objSourceField).val() == "") {
						MessageBox.info("请输入SQL字段名称！");
						return;
					}
					if ($(objTargetField).val() == null || $(objTargetField).val() == "") {
						MessageBox.info("请输入目标字段名称！");
						return;
					}
					if ($(objTargetFlag).val() == "detail") {
						if ($(objDetailTable).val() == "") {
							MessageBox.info("目标为明细时必须输入明细表！");
							return;
						}
					}
					var table = $("#myTableModal table:eq(1)");
					var tbody = table.find("tbody");
					var tr = null;
					if ($(objRowNo).val() != null && $(objRowNo).val() != "") {
						tr = $(tbody).find("tr:eq(" + $(objRowNo).val() + ")");
						$(tr).html("");
						$(objRowNo).val("");
					} else {
						tr = $("<tr></tr>").appendTo(tbody);
					}
					$("<td><input type='checkbox'></td>").appendTo(tr);
					$("<td>" + $(objSourceField).val() + "</td>").appendTo(tr);
					$("<td>" + $(objTargetFlag).val() + "</td>").appendTo(tr);
					if ($(objTargetFlag).val() == "detail") {
						$("<td>" + $(objDetailTable).val() + "</td>").appendTo(tr);
					} else {
						$("<td></td>").appendTo(tr);
					}
					$("<td>" + $(objTargetField).val() + "</td>").appendTo(tr);
					if ($(objTargetFlag).val() == "detail") {
						$("<td>" + $(objDetailSet).val() + "</td>").appendTo(tr);
					} else {
						$("<td></td>").appendTo(tr);
					}
					$("<td><input type='button' class='btn btn-default' value='删除'></td>").appendTo(tr);
					$(objSourceField).val("");
					$(objTargetFlag).val("head");
					$(objTargetField).val("");
					$(objDetailTable).val("");
					$(objDetailTable).prop("disabled", true);
					$(objDetailSet).val("current");
					$(objDetailSet).prop("disabled", true);
					table.find("tbody input[type='checkbox']").unbind("click");
					table.find("tbody input[type='checkbox']").bind("click", editServer);
					table.find("tbody input[type='button']").unbind("click");
					table.find("tbody input[type='button']").bind("click", deleteServer);
				}).appendTo(objDiv);
				var btnClear = $('<button type="button" class="btn btn-success fa fa-search" id="btn-add-event">清空事件</button>').click(function() {
					var data = {};
					$(hid).val(JSON.stringify(data));
					$(hid).trigger("change");
					$("#myTableModal").modal("hide");
				}).appendTo(objDiv);
				$("#myTableModal table:eq(1)").find("thead").html("<tr><th></th><th>SQL字段</th><th>目标</th><th>明细表</th><th>目标字段</th><th>明细设定方法</th><th></th></tr>");
				$("#myTableModal table:eq(1)").find("tbody").html("");
				if (data.mapping != undefined && data.mapping != null) {
					var tdata = "";
					$.each(data.mapping, function(n, value) {
						tdata += "<tr>";
						tdata += "<td><input type='checkbox'></td>";
						tdata += "<td>" + value.sourceField + "</td>";
						tdata += "<td>" + value.targetType + "</td>";
						tdata += "<td>" + value.table + "</td>";
						tdata += "<td>" + value.targetField + "</td>";
						tdata += "<td>" + value.rule + "</td>";
						tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
						tdata += "</tr>";
					});
					$("#myTableModal table:eq(1)").find("tbody").html(tdata);
				}
				var table = $("#myTableModal table:eq(1)");
				table.find("tbody input[type='checkbox']").unbind("click");
				table.find("tbody input[type='checkbox']").bind("click", editServer);
				table.find("tbody input[type='button']").unbind("click");
				table.find("tbody input[type='button']").bind("click", deleteServer);
				$("#btn-myTable-confirm").unbind("click");
				$("#btn-myTable-confirm").bind("click", function(){
					if ($(objEvent).val() == null || $(objEvent).val() == "") {
						MessageBox.info("请输入事件名称！");
						return;
					}
					if ($(txtSql).val() == "") {
						MessageBox.info("请输入SQL文或存储过程！");
						return;
					}
					var data = {};
					data.event = $(objEvent).val();
					data.sql = encodeURIComponent($(txtSql).val());
					data.target = $(dbType).val();
					data.detail = $(objDetailParam).val();
					data.checkFunc = encodeURIComponent($(txtFunc).val());
					data.mapping = [];
					var table = $("#myTableModal table:eq(1)");
					var tbody = table.find("tbody");
					$.each($(tbody).find("tr"), function() {
						var data2 = {};
						data2.sourceField = $(this).find("td:eq(1)").html();
						data2.targetType = $(this).find("td:eq(2)").html();
						data2.table = $(this).find("td:eq(3)").html();
						data2.targetField = $(this).find("td:eq(4)").html();
						data2.rule = $(this).find("td:eq(5)").html();
						data.mapping.push(data2);
					});
					$(hid).val(JSON.stringify(data));
					$(hid).trigger("change");
					$("#myTableModal").modal("hide");
				});
				var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}).appendTo("#" + _div);
		},
		this.destroy = function() {
			$('#'+_div+' input').each(function(){
				_props[_name].value = $(hid).val();
			});
		};
	},
	//条件编辑
	conditionEditor:function() {
		var _props, _name, _div, _obj;
		this.init = function(props, name, obj) {
			_props = props;
			_div = "P" + name;
			_name = name;
			_obj = obj;
			var hid = $('<input type="hidden"/>').change(function(){
				props[name] = $(this).val();
			}).appendTo('#' + _div);
			$(hid).val(props[name]);
			var btn = $('<input type="button" value="条件" style="width:100%;"/>').click(function(){
				$(hid).val($(hid).val());
				$("#myTableModalTitle").html("条件设定");
				//表单部分数据做成
				var objForm = $("#myTableModal form");
				$(objForm).html("");
				var objShowLabel = $('<input type="text" placeholder="显示名称" class="form-control input-sm fl w15">').appendTo(objForm);
                var objField = $('<input type="text" placeholder="字段" class="form-control input-sm fl w15">').appendTo(objForm);
                var objCompare = $('<select class="form-control2"></select>').appendTo(objForm);
                var objRowIndex = $('<input type="hidden" id="rowIndex"/>').appendTo(objForm);
               	var options = "<option value='eq'>=</opion>";
               	options += "<option value='gt'>&gt</option>";
               	options += "<option value='get'>&gt=</option>";
               	options += "<option value='lt'>&lt</option>";
               	options += "<option value='let'>&lt=</option>";
               	options += "<option value='like'>包含</option>";
               	options += "<option value='leftlike'>起始于</option>";
               	options += "<option value='rightlike'>结束于</option>";
               	$(objCompare).html(options);
                var objDiv = $('<div class="search"></div>').appendTo(objForm);
                //条件编辑函数
                var editCondition = function() {
        			if ($(this).prop("checked")) {
        				var ids = [];
        				$("#myTableModal table tbody input[type='checkbox']:checked").each(function() {
        					if ($(this).prop("checked")) {
        						ids.push($(this).val());
        					}
        				});
        				if (ids.length > 1) {
        					MessageBox.info("只能选择一条数据编辑！");
        					$("#myTableModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
        					return;
        				}
        				$(objRowIndex).val($(this).closest("tr").index());
        				$(objCompare).val($(this).val());
        				$(objShowLabel).val($(this).parents("tr").find("td:eq(1)").html());
        				$(objField).val($(this).parents("tr").find("td:eq(2)").html());
        			} else {
        				$(objCompare).val("");
        				$(objShowLabel).val("");
        				$(objField).val("");
        				$(objRowIndex).val("");
        			}
        		};
                //条件删除函数
                var deleteCondition = function() {
                	if ($(objRowIndex).val() == $(this).closest("tr").index()) {
        				$(objRowIndex).val("");
        			} else if($(objRowIndex).val() > $(this).closest("tr").index()) {
        				$(objRowIndex).val($(objRowIndex).val() - 1);
        			}
                	$(this).closest("tr").remove();
                	$(objCompare).val("");
    				$(objShowLabel).val("");
    				$(objField).val("");
                };
                //新增按钮
                $('<button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-event">新增↓</button>').appendTo(objDiv).click(function() {
            		var showLabel = $(objShowLabel).val();
            		var field = $(objField).val();
            		var comp = $(objCompare).val();
            		if (showLabel == null || showLabel == "") {
            			alert("请输入显示名称！");
            			return;
            		}
            		if (field == null || field == "") {
            			alert("请输入字段名！");
            			return;
            		}
            		var comp2 = "";
    				switch (comp) {
    				case 'eq':
    					comp2 = "=";
    					break;
    				case 'like':
    				case 'leftlike':
    				case 'rightlike':
    					comp2 = comp;
    					break;
    				case 'let':
    					comp2 = "&lt=";
    					break;
    				case 'get':
    					comp2 = "&gt=";
    					break;
    				default:
    					comp2 = "&" + comp;
    					break;
    				}
            		if ($(objRowIndex).val() == "" || $(objRowIndex).val() == null) {
            			//新增
            			var data = "<tr>";
            			data += "<td><input type='checkbox' value='" + comp + "'></td>";
            			data += "<td>" + showLabel + "</td>";
            			data += "<td>" + field + "</td>";
            			data += "<td>" + comp2 + "</td>";
            			data += "<td><input type='button' value='删除' class='btn btn-default'></td>";
            			data += "</tr>";
            			$("#myTableModal table tbody").append(data);
            		} else {
            			//修改
            			var index = $(objRowIndex).val();
            			var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
            			var data = "<td><input type='checkbox' value='" + comp + "'></td>";
            			data += "<td>" + showLabel + "</td>";
            			data += "<td>" + field + "</td>";
            			data += "<td>" + comp2 + "</td>";
            			data += "<td><input type='button' value='删除' class='btn btn-default'></td>";
            			$(tr).html(data);
            			$(objRowIndex).val("");
            		}
            		$("#myTableModal table tbody input[type='checkbox']").unbind("click");
            		$("#myTableModal table tbody input[type='checkbox']").bind("click", editCondition);
            		$("#myTableModal table tbody input[type='button']").unbind("click");
            		$("#myTableModal table tbody input[type='button']").bind("click", deleteCondition);
            		$(objEventNo).val("");
            		$(objEventFunc).val("");
                });
                var objHeader = $("#myTableModal table thead");
                $(objHeader).html("<tr><th>选择</th><th>显示名</th><th>字段</th><th>比较符号</th><th>删除</th></tr>");
                var objBody = $("#myTableModal table tbody");
                $(objBody).html("");
                //alert($(hid).val());
                //生成已有条件
                if ($(hid).val() != null && $(hid).val() != "") {
                	var data = eval("(" + $(hid).val() + ")");
                	//alert(data);
        			var tdata = "";
        			$.each(data, function(n, value) {
        				tdata += "<tr>";
        				tdata += "<td><input type='checkbox' value='" + value.compareType + "'></td>";
        				tdata += "<td>" + value.viewText + "</td>";
        				tdata += "<td>" + value.field + "</td>";
        				var comp = "";
        				switch (value.compareType) {
        				case 'eq':
        					comp = "=";
        					break;
        				case 'like':
        				case 'leftlike':
        				case 'rightlike':
        					comp = value.compareType;
        					break;
        				case 'let':
        					comp = "&lt=";
        					break;
        				case 'get':
        					comp = "&gt=";
        					break;
        				default:
        					comp = "&" + value.compareType;
        					break;
        				}
        				tdata += "<td>" + comp + "</td>"
        				tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>"
        				tdata += "</tr>";
        			});
                    $(objBody).html(tdata);
                    $("#myTableModal table tbody input[type='checkbox']").unbind("click");
            		$("#myTableModal table tbody input[type='checkbox']").bind("click", editCondition);
            		$("#myTableModal table tbody input[type='button']").unbind("click");
            		$("#myTableModal table tbody input[type='button']").bind("click", deleteCondition);
                }
                //确定按钮事件
                $("#btn-myTable-confirm").unbind("click");
				$("#btn-myTable-confirm").bind("click", function() {
					var data = "";
					$("#myTableModal table tbody tr").each(function() {
						data += "{viewText:\"" + $(this).find("td:eq(1)").html() + "\",";
						data += "field:\"" +$(this).find("td:eq(2)").html() + "\",";
						data += "compareType:\"" + $(this).find("input").val() + "\"},";
					});
					if (data.length > 0) {
						data = data.substr(0, data.length - 1);
					}
					$(hid).val("[" + data + "]");
					$(hid).trigger("change");
					$('#myTableModal').modal('hide');
				});
                var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}).appendTo('#'+_div);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_name].value = $(hid).val();
			});
		};
	},
	//开窗编辑
	openEditor:function() {
		var _props, _name, _div, _obj;
		this.init = function(props, name, obj) {
			_props = props;
			_div = "P" + name;
			_name = name;
			_obj = obj;
			var hid = $('<input type="hidden"/>').change(function(){
				props[name] = $(this).val();
			}).appendTo('#' + _div);
			$(hid).val(props[name]);
			var btn = $('<input type="button" value="开窗返回设定" style="width:100%;"/>').click(function(){
				$(hid).val($(hid).val());
				$("#myTableModalTitle").html("开窗返回设定");
				//表单部分数据做成
				var objForm = $("#myTableModal form");
				$(objForm).html("");
				var objProc = $('<input type="text" placeholder="存储过程" class="form-control input-sm fl w15">').appendTo(objForm);
                var objTarget = $('<select class="form-control2"><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(objForm);
                var objNewRow = $('<select class="form-control2"><option value="Y">明细行不足时新增明细</option><option value="N">明细行不足时停止设定</option></select>').appendTo(objForm);
                $("<div style='width:500px;height:30px;'></div>").appendTo(objForm);                
                var objSourceFlag = $('<select class="form-control2"><option value="head">表头</option><option value="open">开窗数据</option><option value="proc">存储过程</option></select>').appendTo(objForm);
                var objSourceField = $('<input type="text" placeholder="来源字段" class="form-control input-sm fl w15">').appendTo(objForm);
                //var objTargetFlag = $('<select class="form-control2"><option value="head">表头</option><option value="detail">表体</option></select>').appendTo(objForm);
                var objTargetField = $('<input type="text" placeholder="目标字段" class="form-control input-sm fl w15">').appendTo(objForm);
                var objRowIndex = $('<input type="hidden" id="rowIndex"/>').appendTo(objForm);
                var objDiv = $('<div class="search"></div>').appendTo(objForm);
                //编辑映射
                var editMapping = function() {
        			if ($(this).prop("checked")) {
        				var ids = [];
        				$("#myTableModal table tbody input[type='checkbox']:checked").each(function() {
        					if ($(this).prop("checked")) {
        						ids.push($(this).closest("tr").index());
        					}
        				});
        				if (ids.length > 1) {
        					MessageBox.info("只能选择一条数据编辑！");
        					$("#myTableModal table tbody input:eq(" + $(this).closest("tr").index() + ")").prop("checked", false);
        					return;
        				}
        				$(objRowIndex).val($(this).closest("tr").index());
        				var tr = $(this).closest("tr");
        				var index = $(this).closest("tr").index();
        				$(objSourceFlag).val($(tr).find("td:eq(1)").html());
        				$(objSourceField).val($(tr).find("td:eq(2)").html());
        				//$(objTargetFlag).val($(tr).find("td:eq(3)").html());
        				$(objTargetField).val($(tr).find("td:eq(3)").html());
        			} else {
        				$(objSourceFlag).val("head");
        				$(objSourceField).val("");
        				//$(objTargetFlag).val("head");
        				$(objTargetField).val("");
        				$(objRowIndex).val("");
        			}
        		};
                //映射删除函数
                var deleteMapping = function() {
                	if ($(objRowIndex).val() == $(this).closest("tr").index()) {
        				$(objRowIndex).val("");
        			} else if($(objRowIndex).val() > $(this).closest("tr").index()) {
        				$(objRowIndex).val($(objRowIndex).val() - 1);
        			}
                	$(this).closest("tr").remove();
                	$(objSourceFlag).val("head");
    				$(objSourceField).val("");
    				//$(objTargetFlag).val("head");
    				$(objTargetField).val("");
                };
                //新增按钮
                $('<button type="button" class="btn btn-block btn-success fa fa-search" id="btn-add-event">新增↓</button>').appendTo(objDiv).click(function() {
            		if ($(objSourceField).val() == null || $(objSourceField).val() == "") {
            			alert("请输入来源字段！");
            			return;
            		}
            		if ($(objTargetField).val() == null || $(objTargetField).val() == "") {
            			alert("请输入目标字段！");
            			return;
            		}
            		if ($(objRowIndex).val() == "" || $(objRowIndex).val() == null) {
            			//新增
            			var data = "<tr>";
            			data += "<td><input type='checkbox'></td>";
            			data += "<td>" + $(objSourceFlag).val() + "</td>";
            			data += "<td>" + $(objSourceField).val() + "</td>";
            			//data += "<td>" + $(objTargetFlag).val() + "</td>";
            			data += "<td>" + $(objTargetField).val() + "</td>";
            			data += "<td><input type='button' value='删除' class='btn btn-default'></td>";
            			data += "</tr>";
            			$("#myTableModal table tbody").append(data);
            		} else {
            			//修改
            			var index = $(objRowIndex).val();
            			var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
            			var data = "<td><input type='checkbox'></td>";
            			data += "<td>" + $(objSourceFlag).val() + "</td>";
            			data += "<td>" + $(objSourceField).val() + "</td>";
            			//data += "<td>" + $(objTargetFlag).val() + "</td>";
            			data += "<td>" + $(objTargetField).val() + "</td>";
            			data += "<td><input type='button' value='删除' class='btn btn-default'></td>";
            			$(tr).html(data);
            			$(objRowIndex).val("");
            		}
            		$("#myTableModal table tbody input[type='checkbox']").unbind("click");
            		$("#myTableModal table tbody input[type='checkbox']").bind("click", editMapping);
            		$("#myTableModal table tbody input[type='button']").unbind("click");
            		$("#myTableModal table tbody input[type='button']").bind("click", deleteMapping);
            		$(objSourceFlag).val("head");
            		$(objSourceField).val("");
            		//$(objTargetFlag).val("head");
            		$(objTargetField).val("");
                });
                var objHeader = $("#myTableModal table thead");
                $(objHeader).html("<tr><th>选择</th><th>来源类型</th><th>来源字段</th><th>目标字段</th><th>删除</th></tr>");
                var objBody = $("#myTableModal table tbody");
                $(objBody).html("");
                //alert($(hid).val());
                //生成已有条件
                if ($(hid).val() != null && $(hid).val() != "") {
                	try {
	                	var data = eval("(" + decodeURIComponent($(hid).val()) + ")");
	                	$(objProc).val(data.proc);
	                	$(objTarget).val(data.target);
	                	$(objNewRow).val(data.addRow);
	        			var tdata = "";
	        			$.each(data.mapping, function(n, value) {
	        				tdata += "<tr>";
	        				tdata += "<td><input type='checkbox'></td>";
	        				tdata += "<td>" + value.sourceFlag + "</td>";
	        				tdata += "<td>" + value.source + "</td>";
	        				//tdata += "<td>" + value.targetFlag + "</td>";
	        				tdata += "<td>" + value.target + "</td>";
	        				tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>"
	        				tdata += "</tr>";
	        			});
	                    $(objBody).html(tdata);
                	} catch (ex) {
                		alert(ex);
                	}
                    $("#myTableModal table tbody input[type='checkbox']").unbind("click");
            		$("#myTableModal table tbody input[type='checkbox']").bind("click", editMapping);
            		$("#myTableModal table tbody input[type='button']").unbind("click");
            		$("#myTableModal table tbody input[type='button']").bind("click", deleteMapping);
                }
                //确定按钮事件
                $("#btn-myTable-confirm").unbind("click");
				$("#btn-myTable-confirm").bind("click", function() {
					var data = "";
					data += "{proc:\"" + $(objProc).val() + "\",";
					data += "target:\"" + $(objTarget).val() + "\",";
					data += "addRow:\"" + $(objNewRow).val() + "\",";
					data += "mapping:[";
					$("#myTableModal table tbody tr").each(function() {
						data += "{sourceFlag:\"" + $(this).find("td:eq(1)").html() + "\",";
						data += "source:\"" +$(this).find("td:eq(2)").html() + "\",";
						//data += "targetFlag:\"" +$(this).find("td:eq(3)").html() + "\",";
						data += "target:\"" + $(this).find("td:eq(3)").html() + "\"},";
					});
					if (data.length > 0) {
						data = data.substr(0, data.length - 1);
					}
					data += "]}";
					$(hid).val(encodeURIComponent(data));
					$(hid).trigger("change");
					$('#myTableModal').modal('hide');
				});
                var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}).appendTo('#'+_div);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_name].value = $(hid).val();
			});
		};
	}
});
$.myeditor = myeditor;
})(jQuery);