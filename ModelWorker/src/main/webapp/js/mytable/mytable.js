(function($){
	var MyTable = function(ele, opt) {
		this.$element = ele;
		this.defaults = {
			rows:1,
			cols:1,
			table:'',
			cells:[],
			margin:[]			
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
		//alert(this.tools.props.inputEditor[0].name);
	};
	MyTable.prototype = {
		init : function() {
			//alert("init=" + JSON.stringify(this.config));
			var objParent = this.$element;
			for (var i = 1; i <= this.config.rows; i++) {
				var tr = $("<tr></tr>").appendTo(this.$element);
				for (var j = 1; j <= this.config.cols; j++) {
					var td = null;
					var hasOut = true;
					if (this.config.margin && this.config.margin.length > 0) {
						for (var m = 0; m < this.config.margin.length; m++) {
							var data = this.config.margin[m];
							if (i == data.start.row && j == data.start.col) {
								var rows = data.end.row - data.start.row + 1;
								var cols = data.end.col - data.start.col + 1;
								var props = "";
								if (rows > 1) {
									props += " rowspan=" + rows;
								}
								if (cols > 1) {
									props += " colspan=" + cols;
								}
								td = $("<td" + props + ">&nbsp;</td>");
							} else if (i >= data.start.row && i <= data.end.row && j >= data.start.col && j <= data.end.col) {
								hasOut = false;
								break;
							}
						}
					}
					if (hasOut == false) {
						continue;
					}
					if (td) {
						td.appendTo(tr);
					} else {
						td = $("<td>&nbsp;</td>").appendTo(tr);
					}
					var editor = null;
					var fieldType = null;
					$.each(this.config.cells, function(n, value) {
						if (value.row == i && value.col == j) {
							fieldType = value.type;
							switch (fieldType) {
							case 'label':
								//alert(value.props.value);
								var showData = "标签";
								if (typeof(value.props.value) != "undefined") {
									if (value.props.value != "") {
										showData = value.props.value;
									}										
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $("<label " + value.props.controlProp + ">" + showData + "</label>").appendTo(td);
								} else {
									editor = $("<label>" + showData + "</label>").appendTo(td);
								}			
								//alert(showData);
								//editor = $("<label>" + showData + "</label>").appendTo(td);
								return false;
							case 'inputEditor':
								var showData = "";
								if (typeof(value.props.field) != "undefined") {
									showData = value.props.field;
								}
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "form-control input-sm fl w15";
								} else {
									css = value.props.css;
								}
								var readonlyType = "";
								if (value.props.readonly == "Y") {
									readonlyType = "readonly";
								}
								//alert(JSON.stringify(value));
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $("<input type='text' class='" + css + "' value='" + showData + "' " + value.props.controlProp + " " + readonlyType + ">").appendTo(td);
								} else {
									editor = $("<input type='text' class='" + css + "' value='" + showData + "' " + readonlyType + ">").appendTo(td);
								}
								//editor = $("<input type='text' value='" + showData + "'>").appendTo(td);									
								return false;
							case 'selectEditor':
								//下拉框处理
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "form-control2";
								} else {
									css = value.props.css;
								}
								var disableType = "";
								/*if (value.props.readonly == "Y") {
									disableType = "disabled";
								}*/
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $("<select " + value.props.controlProp + " class='" + css + "' " + disableType + "></select>").appendTo(td);
								} else {
									editor = $("<select class='" + css + "' " + disableType + "></select>").appendTo(td);
								}
								//editor = $("<select></select>").appendTo(td);
								return false;
							case 'openEditor':
								//选择框
								var div = $("<div></div>").appendTo(td);
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "form-control input-sm fl w15";
								} else {
									css = value.props.css;
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $('<input type="text" class="' + css + '" value="' + value.props.field + '" ' + value.props.controlProp + ' ">').appendTo(div);
								} else {
									editor = $('<input type="text" class="' + css + '" value="' + value.props.field + '">').appendTo(div);
								}
								//editor = $("<input type='text'>").appendTo(td);
								if (value.props.readonly == "Y") {
									disableType = "disabled";
								}
								var w = $(editor).width();
								if (w <= 0) {
									w = 0;
								}
								$(div).css("float", "left");
								$(div).css("width", w + 30);
								$(editor).css("float", "left");
								$(editor).css("width", w);
								$("<input type='button' value='...' style='width:30px;' " + disableType + ">").appendTo(div);
								return false;
							case 'checkEditor':
								//checkBox
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "";
								} else {
									css = value.props.css;
								}
								var readonlyType = "";
								if (value.props.readonly == "Y") {
									readonlyType = "disabled";
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $("<input type='checkbox' class='" + css + "' " + value.props.controlProp + " " + readonlyType + ">").appendTo(td);
								} else {
									editor = $("<input type='checkbox' class='" + css + "' " + readonlyType + ">").appendTo(td);
								}
								//editor = $("<input type='checkbox'>").appendTo(td);
								return false;
							case 'mutilInputEditor':
								//多行输入框
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "form-control input-sm fl";
								} else {
									css = value.props.css;
								}
								var readonlyType = "";
								if (value.props.readonly == "Y") {
									readonlyType = "readonly";
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $("<textarea class='" + css + "' " + value.props.controlProp + " " + readonlyType + ">" + value.props.field + "</textarea>").appendTo(td);
								} else {
									editor = $("<textarea class='" + css + "' " + readonlyType + ">" + value.props.field + "</textarea>").appendTo(td);
								}
								//editor = $("<textarea></textarea>").appendTo(td);
								return false;
							case 'dateEditor':
								//日期控件
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "form-control input-sm";
								} else {
									css = value.props.css;
								}
								var objDiv = $('<div class="input-group date fl w15"></div>').appendTo(td);
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly ' + value.props.controlProp + ' >').appendTo(objDiv);
								} else {
									editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly>').appendTo(objDiv);
								}
								//editor = $('<input type="text" class="form-control input-sm" id="overTimeDate" value=""  name="overTimeDate" placeholder="请选择日期" readonly>').appendTo(objDiv);
			                    var objSpan = $('<span class="input-group-addon"></span>').appendTo(objDiv);
			                    $('<i class="fa fa-calendar"></i>').appendTo(objSpan);
			                    return false;
							case 'image':
								//图像
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css == "") {
									css = "";
								} else {
									css = value.props.css;
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									editor = $("<img src='../img/default.jpg' " + value.props.controlProp + ">").appendTo(td);
								} else {
									editor = $("<img src='../img/default.jpg' height='100'>").appendTo(td);
								}
								return false;
							default:
								return false;
							}
						}
					});
					if (editor != null) {
						$(editor).bind("click", {row:i,col:j,fieldType:fieldType,table:this}, this.show_prop);
					}
					$(td).bind("dblclick", {obj : this, row:i, col:j, config:this.config}, this.td_dblclick);
					//$(td).bind("dblclick", this.td_dblclick);
					$(td).bind('mouseover', this.td_mouseover);
					$(td).bind('mouseout', {background:'white'}, this.td_mouseout);
					var hasData = false;
					$.each(this.config.cells, function(n, value) {
						if (value.row == i && value.col == j) {
							hasData = true;
							return false;
						}
					});
					if (hasData == false) {
						this.config.cells.push({row:i,col:j,props:{}});
					}
				}
			}
			//alert(JSON.stringify(this.config));
			$("#btn-header").unbind("click");
			$("#btn-margin").unbind("click");
			$("#btn-margin-ok").unbind("click");
			$("#btn-margin-cancel").unbind("click");
			$("#btn-cancel-margin-ok").unbind("click");
			$("#btn-add-row").unbind("click");
			$("#btn-add-col").unbind("click");
			$("#fieldType").unbind("change");
			$("#btn_save_proc").unbind("click");
			$("#btn_default_set").unbind("click");
			$("#btn_define_button").unbind("click");
			
			$("#btn-header").bind("click", {table:this}, this.set_table);
			$("#btn-margin").bind("click", this.margin_cell_view);
			$("#btn-margin-ok").bind("click", {table:this}, this.margin_cell);
			$("#btn-margin-cancel").bind("click", this.cancelMargin_cell_view);
			$("#btn-cancel-margin-ok").bind("click", {config:this.config, table:this}, this.cancelMargin_cell);
			$("#btn-add-row").bind("click", {table:this}, this.add_row);
			$("#btn-add-col").bind("click", {table:this}, this.add_col);
			$("#fieldType").bind("change", {table:this}, this.fieldType_change);
			$("#btn_save_proc").bind("click", {config:this.config}, this.save_proc);
			$("#btn_default_set").bind("click", {config:this.config}, this.default_set);
			$("#btn_define_button").bind("click",{view:this}, this.add_button);
			
			return this;
		},
		isInteger:function(data) {
			if (data == null || data == "") {
				return -1;
			}
			var ex = /^\d+$/;
			if (!ex.test(data)) {
				return -2;
			}
			if (data <= 0) {
				return -2;
			}
			return 0;
		},
		set_table :function(event) {
			var table = event.data.table;
			var config = table.config;
			$("#myTableModalTitle").html("设定Head的数据表");
			var objForm = $("#myTableModal form");
			$(objForm).html("");
			var objTable = $('<input type="text" placeholder="数据表" class="form-control input-sm fl w15">').val(config.table).appendTo(objForm);
			if (config.tableKey == undefined || config.tableKey == "" || config.tableKey == null) {
				config.tableKey = "ID";
			}
			var objTableKey = $('<input type="text" placeholder="数据表主键" class="form-control input-sm fl w15">').val(config.tableKey).appendTo(objForm);
			var objTarget = $('<select class="form-control2"><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').val(config.target).appendTo(objForm);
			if (config.sql == undefined || config.sql == null) {
				config.sql = "";
			}
			var objSqlProc = $('<select class="form-control2"><option value="S">SQL文</option><option value="P">存储过程</option></select>').val(config.sqlProc).appendTo(objForm);
			$("<label>SQL文或存储过程</label>").appendTo(objForm);
			var objSql = $('<textarea calss="form-control input-sl fl w5" style="width:90%;" placeholder="SQL文/存储过程"></textarea>').val(decodeURIComponent(config.sql)).appendTo(objForm);
			var objTHead = $("#myTableModal table thead");
			$(objTHead).html("");
			var objTBody = $("#myTableModal table tbody");
			$(objTBody).html("");
			$("#btn-myTable-confirm").unbind("click");
			$("#btn-myTable-confirm").bind("click", function() {
				if ($(objTable).val() == "" || $(objTable).val == null) {
					alert("请输入数据表！");
					return;
				}
				if ($(objTableKey).val() == null || $(objTableKey).val() == "") {
					alert("请输入数据表主键！");
					return;
				}
				config.table = $(objTable).val();
				config.tableKey = $(objTableKey).val();
				config.target = $(objTarget).val();
				config.sql = encodeURIComponent($(objSql).val());
				config.sqlProc = $(objSqlProc).val();
				$("#myTableModal").modal("hide");
			});
			var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		},
		add_row : function(event) {
			var table = event.data.table;
			var config = table.config;
			var cols = config.cols;
			$("#myTableModalTitle").html("新增行");
			$("#myTableModal form").html("");
			$("#myTableModal table tbody").html("");
			$("#myTableModal table thead").html("");			
			var thead = $("#myTableModal table thead");
			var tr = $("<tr></tr>").appendTo(thead);
			$("<th>插入的行号</th>").appendTo(tr);
			var th = $("<th></th>").appendTo(tr);
			var objStartRow = $("<input type='textbox' value='" + (config.rows + 1) + "'>").appendTo(th);
			var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
        	modal.show();
        	$("#btn-myTable-confirm").unbind("click");
        	$("#btn-myTable-confirm").bind("click", function() {
        		config.rows = config.rows + 1;
    			var startRow = parseInt($(objStartRow).val());
    			//修改startRow以后的行号
    			$.each(config.cells, function(n, value){
    				if (value.row >= startRow) {
    					value.row = value.row + 1;
    				}
    			});
    			//修改所有合并单元格
    			$.each(config.margin, function(n, value) {
    				if (value.start.row >= startRow) {
    					value.start.row = value.start.row + 1;
    				}
    				if (value.end.row >= startRow) {
    					value.end.row = value.end.row + 1;
    				}
    			});
    			var cells = config.cells;
    			for (var i = cols; i > 0; i--) {
    				cells.splice(startRow, 0, {row:config.rows,col:i+1,props:{}});
    			}
    			$("#headerTable").html("");
    			table.init();
    			$('#myTableModal').modal('hide');
        	});
		},
		add_col : function(event) {
			var table = event.data.table;
			var config = table.config;
			var rows = config.rows;
			config.cols = config.cols + 1;
			var rowNum = 1;
			for (var i = 1; i <= rows; i++) {
				$.each(config.cells, function(n, value){
					if (value.row > i) {
						config.cells.splice(n, 0, {row:i,col:config.cols,props:{}});
						return false;
					}
				});
			}
			config.cells.push({row:rows,col:config.cols,props:{}});
			//alert(JSON.stringify(config));
			$("#headerTable").html("");
			table.init();
		},
		td_dblclick : function(event) {
			var objTable = event.data.obj;
			var rowNum = event.data.row;
			var colNum = event.data.col;
			var config = event.data.config;
			//alert("row=" + rowNum + ";col=" + colNum);
			//alert(JSON.stringify(config));
			var cell = null;
			$.each(config.cells, function (n, value) {
				if (value.row == rowNum && value.col == colNum) {
					cell = value;
					return false;
				}
			});
			var fieldType = "";
			if (cell != null) {
				fieldType = cell.type;
			}
			var options = "";
			options += "<option value='all'></option>";
			if (fieldType == "label") {
				options += "<option value='label' selected>标签</option>";
			} else {
				options += "<option value='label'>标签</option>"
			}
			if (fieldType == "inputEditor") {
				options += "<option value='inputEditor' selected>单行输入框</option>";
			} else {
				options += "<option value='inputEditor'>单行输入框</option>";
			}
			if (fieldType == "selectEditor") {
				options += "<option value='selectEditor' selected>下拉框</option>";
			} else {
				options += "<option value='selectEditor'>下拉框</option>";
			}
			if (fieldType == "openEditor") {
				options += "<option value='openEditor' selected>选择框</option>";	
			} else {
				options += "<option value='openEditor'>选择框</option>";
			}
			if (fieldType == "checkEditor") {
				options += "<option value='checkEditor' selected>CheckBox</option>";
			} else {
				options += "<option value='checkEditor'>CheckBox</option>";
			}
			if (fieldType == "mutilInputEditor") {
				options += "<option value='mutilInputEditor' selected>多行输入框</option>";
			} else {
				options += "<option value='mutilInputEditor'>多行输入框</option>";
			}
			if (fieldType == "dateEditor") {
				options += "<option value='dateEditor' selected>日期</option>";
			} else {
				options += "<option value='dateEditor'>日期</option>";
			}
			if (fieldType == "image") {
				options += "<option value='image' selected>图像</option>";
			} else {
				options += "<option value='image'>图像</option>";
			}
			$("#fieldType").html(options);
			var modal = $('#myModal1').modal({backdrop: 'static', keyboard: false});
        	modal.show();
        	$("#btn-header-design").unbind("click");
        	$("#btn-header-design").bind("click", {row:rowNum,col:colNum,table:objTable}, objTable.fieldType_ok);
		},
		fieldType_ok : function (event) {
			var rowNum = event.data.row;
			var colNum = event.data.col;
			var table = event.data.table;
			var config = event.data.table.config;
			$.each(config.cells, function(n, value) {
				if (value.row == rowNum && value.col == colNum) {
					if ($("#fieldType").val() == "all") {
						value.type = "";
						value.props = {};
					} else {
						value.type = $("#fieldType").val();
					}
					return false;
				}
			});
			//alert(JSON.stringify(config));
			$("#headerTable").html("");
			table.init();
			$('#myModal1').modal('hide');
		},
		show_prop : function(event) {
			var props = $("#mytable_props").show().draggable();
			var prop_table = props.find("table");
			prop_table.empty();
			var table = event.data.table;
			var config = table.config;
			var row = event.data.row;
			var col = event.data.col;
			var fieldType = event.data.fieldType;
			var fieldProps = table.tools.props[fieldType];
			var cell = null;
			var obj = event.target;
			$.each(config.cells, function(n, value) {
				if (value.row == row && value.col == col) {
					cell = value;
					return false;
				}
			});
			$.each(fieldProps, function(n, value) {
				prop_table.append("<tr><th>" + value.label + "</th><td><div id='P" + n + "'></div></td></tr>");
				if (typeof(cell.props[n]) == "undefined") {
					cell.props[n] = "";
				}
				value.editor().init(cell.props, n, obj);
			});
			$(obj).unbind("update");
			$(obj).bind("update", function() {
				$("#headerTable").html("");
				table.init();
			});
			//alert(JSON.stringify(config));
		},
		fieldType_change : function (event) {
			var table = event.data.table;
			//alert(table.tools.props);
			//alert("changed");
		},
		td_mouseover : function() {
			$(this).css("background-color", "#0fe")
		},
		td_mouseout : function(event) {
			var cl = event.data.background;
			if (cl) {
				$(this).css("background-color", cl);
			} else {
				$(this).css("background-color", "white");
			}			
		},
		margin_cell_view:function(event) {
			var modal = $('#myModal5').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		},
		margin_cell:function(event) {
			var table = event.data.table;
			var config = table.config;
			var rows = config.rows;
			var cols = config.cols;
			switch (event.data.table.isInteger($("#startRowNum").val())) {
			case -1:
				MessageBox.info("请输入起始行号！");
				return;
			case -2:
				MessageBox.info("起始行号必须为正整数！");
				return;
			}
			switch (event.data.table.isInteger($("#startColNum").val())) {
			case -1:
				MessageBox.info("请输入起始列号！");
				return;
			case -2:
				MessageBox.info("起始列号必须为正整数！");
				return;
			}
			switch (event.data.table.isInteger($("#endRowNum").val())) {
			case -1:
				MessageBox.info("请输入结束行号！");
				return;
			case -2:
				MessageBox.info("结束行号必须为正整数！");
				return;
			}
			switch (event.data.table.isInteger($("#endColNum").val())) {
			case -1:
				MessageBox.info("请输入结束列号！");
				return;
			case -2:
				MessageBox.info("结束列号必须为正整数！");
				return;
			}
			if (parseInt($("#startRowNum").val()) > parseInt($("#endRowNum").val())) {
				MessageBox.info("起始行号不能大于结束行号！");
				return;
			}
			if (parseInt($("#startColNum").val()) > parseInt($("#endColNum").val())) {
				MessageBox.info("起始列号不能大于结束列号！");
				return;
			}
			//起始单元格
			var startRow = parseInt($("#startRowNum").val());
			var startCol = parseInt($("#startColNum").val());
			var endRow = parseInt($("#endRowNum").val());
			var endCol = parseInt($("#endColNum").val());
			var delTds = [];
			$.each(config.cells, function(n, value) {
				if (value.row == startRow && value.col == startCol) {
					return;
				} else {
					if (value.row >= startRow && value.row <= endRow &&
							value.col >= startCol && value.col <= endCol) {
						delTds.push(n);
					}
				}
			});
			//合并单元格
			delTds.reverse();
			$.each(delTds, function(n, value) {
				config.cells.splice(value, 1);
			});
			config.margin.push({start:{row:startRow,col:startCol},end:{row:endRow,col:endCol}});
			$("#headerTable").html("");
			//alert("合并单元格后=" + JSON.stringify(config));
			event.data.table.init();
			$('#myModal5').modal('hide');
		},
		cancelMargin_cell_view:function(event) {
			var modal = $('#myModal6').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		},
		//取消合并单元格
		cancelMargin_cell:function (event) {
			var table = event.data.table;
			var config = table.config;
			switch (isInteger($("#CancelRowNum").val())) {
			case -1:
				MessageBox.info("请输入行号！");
				return;
			case -2:
				MessageBox.info("行号必须为正整数！");
				return;
			}
			switch (isInteger($("#CancelColNum").val())) {
			case -1:
				MessageBox.info("请输入列号！");
				return;
			case -2:
				MessageBox.info("列号必须为正整数！");
				return;
			}
			var rowNum = parseInt($("#CancelRowNum").val());
			var colNum = parseInt($("#CancelColNum").val());
			var delMarginIndex = -1;
			var marginData = null;
			$.each(config.margin, function(n, value) {
				if (value.start.row == rowNum && value.start.col == colNum) {
					marginData = value;
					delMarginIndex = n;
					return false;
				}
			});
			if (delMarginIndex == -1) {
				MessageBox.info("输入的合并单元格行列号不正确！");
				return;
			}
			var index = -1;
			$.each(config.cells, function(n, value){
				if (value.row == rowNum && value.col == colNum) {
					index = n;
					return false;
				}
			});
			if (index == -1) {
				MessageBox.info("输入的合并单元格行列号不正确！");
				return;
			}
			config.margin.splice(delMarginIndex, 1);
			index++;
			if (marginData.end.row - marginData.start.row <= 0) {
				for (var i = marginData.end.col; i > marginData.start.col; i--) {
					config.cells.splice(index, 0 , {row:rowNum,col:i,props:{}});
				}				
			} else {
				for (var i = marginData.start.row; i <= marginData.end.row; i++) {
					for (var j = marginData.end.col; j >= marginData.start.col; j--) {
						if (i == marginData.start.row && j == marginData.start.col) {
							break;
						}
						config.cells.splice(index, 0 , {row:i,col:j,props:{}});
					}
					if (i + 1 >= marginData.endRow) {
						break;
					}
					$.each(config.cells, function(n, value) {
						index = -1;
						if (value.row == i + 1 && value.col == colNum) {
							index = n + 1;
							return false;
						} else if (value.row == i + 1 && value.col > colNum) {
							index = n;
							return false;
						}
					});
					if (index == -1) {
						break;
					}
				}
			}
			//alert("取消合并后=" + JSON.stringify(config));
			$("#headerTable").html("");
			table.init();
			$('#myModal6').modal('hide');
		},
		save_proc : function(event) {
			var config = event.data.config;
			$("#myTableModalTitle").html("设定保存处理");
			var objForm = $("#myTableModal form");
			$(objForm).html("");
			var objTarget = $('<select class="form-control2"><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(objForm);
			var objRunTime = $('<select class="form-control2"><option value="BeforeSave">保存前</option><option value="BackSave">保存后</option><option value="BeforeDelete">删除前</option><option value="BackDelete">删除后</option><option value="BeforeSubmit">提交前</option><option value="BackSubmit">提交后</option><option value="BeforeCancelSubmit">取消提交前</option><option value="BackCancelSubmit">取消提交后</option><option value="BeforeAudit">审核前</option><option value="BackAudit">审核后</option><option value="BeforeCancelAudit">撤审前</option><option value="BackCancelAudit">撤审后</option></select>').appendTo(objForm);
			var objDetailTable = null;
			var objParam = $('<select class="form-control2"><option value="HeadOnly">仅表头</option><option value="DetailLoop">每条明细循环</option><option value="DetailTop">明细第一条</option><option value="DetailLast">明细最后一条</option></select>').change(function() {
				if ($(this).val() == "HeadOnly") {
					$(objDetailTable).prop("readonly", true);
				} else {
					$(objDetailTable).prop("readonly", false);
				}
			}).appendTo(objForm);
			objDetailTable = $('<input type="text" class="form-control input-sl fl w15" placeholder="请输入明细表(多表用;隔开)" readonly>').appendTo(objForm);
			var objProc = $("<input type='text' class='form-control input-sl fl w15' placeholder='请输入存储过程'>").appendTo(objForm);
			var objRowNo = $('<input type="hidden">').appendTo(objForm);
			//编辑存储过程
			var editProc = function() {
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
    				$(objRowNo).val($(this).closest("tr").index());
    				$(objTarget).val($(this).parents("tr").find("td:eq(1)").html());
    				$(objRunTime).val($(this).parents("tr").find("td:eq(2)").html());
    				$(objParam).val($(this).parents("tr").find("td:eq(3)").html());
    				$(objParam).trigger("change");
    				$(objDetailTable).val($(this).parents("tr").find("td:eq(4)").html());
    				$(objProc).val($(this).parents("tr").find("td:eq(5)").html());
    			} else {
    				$(objRowNo).val("");
    				$(objTarget).val("L");
    				$(objRunTime).val("BeforeSave");
    				$(objParam).val("HeadOnly");
    				$(objDetailTable).val("");
    				$(objParam).trigger("change");
    				$(objProc).val("");
    			}
    		};
    		//删除存储过程
    		var deleteProc = function() {
    			if ($(objRowNo).val() == $(this).closest("tr").index()) {
    				$(objRowNo).val("");
    			} else if($(objRowNo).val() > $(this).closest("tr").index()) {
    				$(objRowNo).val($(objRowNo).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			$(objTarget).val("L");
				$(objRunTime).val("BeforeSave");
				$(objParam).val("HeadOnly");
				$(objDetailTable).val("");
				$(objParam).trigger("change");
				$(objProc).val("");
    		};
			//新增存储过程
    		var objAdd = $('<input type="button" value="新增↓" class="btn btn-success">').click(function() {
    			if ($(objProc).val() == null || $(objProc).val() == "") {
					alert("请输入存储过程！");
					return;
				}
    			if ($(objParam).val() != "HeadOnly") {
    				if ($(objDetailTable).val() == null || $(objDetailTable).val() == "") {
    					alert("请输入明细表！");
    					return;
    				}
    			}
				var tdata = "";
				if ($(objRowNo).val() == null || $(objRowNo).val() == "") {
					//新增
					tdata = "<tr>";
					tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objTarget).val() + "</td>";
					tdata += "<td>" + $(objRunTime).val() + "</td>";
					tdata += "<td>" + $(objParam).val() + "</td>";
					tdata += "<td>" + $(objDetailTable).val() + "</td>";
					tdata += "<td>" + $(objProc).val() + "</td>";
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
					tdata += "</tr>";
					$("#myTableModal table tbody").append(tdata);
				} else {
					//修改
        			var index = $(objRowNo).val();
        			var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
        			tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objTarget).val() + "</td>";
					tdata += "<td>" + $(objRunTime).val() + "</td>";
					tdata += "<td>" + $(objParam).val() + "</td>";
					tdata += "<td>" + $(objDetailTable).val() + "</td>";
					tdata += "<td>" + $(objProc).val() + "</td>";
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
        			$(tr).html(tdata);
        			$(objRowNo).val("");
				}
				$("#myTableModal table tbody input[type='checkbox']").unbind("click");
        		$("#myTableModal table tbody input[type='checkbox']").bind("click", editProc);
        		$("#myTableModal table tbody input[type='button']").unbind("click");
        		$("#myTableModal table tbody input[type='button']").bind("click", deleteProc);
        		$(objRowNo).val("");
				$(objProc).val("");
				$(objTarget).val("L");
    		}).appendTo(objForm);
    		//存储过程位置调整上
    		var objUp = $('<input type="button" value="向上" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myTableModal table tbody input[type='checkbox']").each(function() {
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
					var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
					var prevTr = $(tr).prev();
					prevTr.insertAfter(tr);
				}
    		}).appendTo(objForm);
    		//存储过程位置向下
    		$('<input type="button" value="向下" class="btn btn-success">').click(function() {
    			var ids = [];
    			var index = 0;
    			var n = 0;
				$("#myTableModal table tbody input[type='checkbox']").each(function() {
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
				var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
				var nextTr = $(tr).next();
				if (nextTr.length > 0) {
					nextTr.insertBefore(tr);
				}
    		}).appendTo(objForm);
    		var th = "<tr><th></th><th>目标数据库</th><th>执行时点</th><th>参数类型</th><th>明细表</th><th>存储过程</th><th></th>";
			var objTHead = $("#myTableModal table thead");
			$(objTHead).html(th);
			var objTBody = $("#myTableModal table tbody");
			$(objTBody).html("");
			var tbody = "";
			if (config.saveProc != undefined && config.saveProc != null && config.saveProc != "") {
				$.each(config.saveProc, function(n, value) {
					tbody += "<tr>";
					tbody += "<td><input type='checkbox'></td>";
					tbody += "<td>" + value.target + "</td>";
					tbody += "<td>" + value.runTime + "</td>";
					tbody += "<td>" + value.paramType + "</td>";
					tbody += "<td>" + value.table + "</td>"
					tbody += "<td>" + value.proc + "</td>";
					tbody += "<td><input type='button' value='删除' class='btn btn-default'></td>";
					tbody += "</tr>";
				});
			}
			$("#myTableModal table tbody").html(tbody);
			$("#myTableModal table tbody input[type='checkbox']").unbind("click");
			$("#myTableModal table tbody input[type='checkbox']").bind("click", editProc);
			$("#myTableModal table tbody input[type='button']").unbind("click");
			$("#myTableModal table tbody input[type='button']").bind("click", deleteProc);
			$("#btn-myTable-confirm").unbind("click");
			$("#btn-myTable-confirm").bind("click", function() {
				config.saveProc = [];
	    		$("#myTableModal table tbody tr").each(function() {
	    			var target = $(this).find("td:eq(1)").html();
	    			var runTime = $(this).find("td:eq(2)").html();
	    			var paramType = $(this).find("td:eq(3)").html();
	    			var table = $(this).find("td:eq(4)").html();
	    			var proc = $(this).find("td:eq(5)").html();
	    			config.saveProc.push({target:target,runTime:runTime,paramType:paramType,table:table,proc:proc});
				});
				$("#myTableModal").modal("hide");
			});
			var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		},
		default_set : function(event) {
			var config = event.data.config;
			$("#myTableModalTitle").html("数据新增时默认值设定");
			var objForm = $("#myTableModal form");
			$(objForm).html("");
			var objTarget = $('<select class="form-control2" style="height:30px;"><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(objForm);
			var objProc = $("<input type='text' class='form-control input-sl fl w5' placeholder='请输入SQL文/存储过程'>").appendTo(objForm);
			var objTable = $("<input type='text' class='form-control input-sl fl w15' placeholder='请输入数据表'>").appendTo(objForm);
			var objSqlField = $("<input type='text' class='form-control input-sl fl w15' placeholder='请输入SQL文中字段'>").appendTo(objForm);
			var objViewField = $("<input type='text' class='form-control input-sl fl w15' placeholder='请输入画面字段'>").appendTo(objForm);
			var objRowNo = $('<input type="hidden">').appendTo(objForm);
			//编辑默认值
			var editDefault = function() {
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
    				$(objRowNo).val($(this).closest("tr").index());
    				$(objTable).val($(this).parents("tr").find("td:eq(1)").html());
    				$(objSqlField).val($(this).parents("tr").find("td:eq(2)").html());
    				$(objViewField).val($(this).parents("tr").find("td:eq(3)").html());
    			} else {
    				$(objRowNo).val("");
    				$(objTable).val("head");
    				$(objSqlField).val("");
    				$(objViewField).val("");
    			}
    		};
    		//删除默认值
    		var deleteDefault = function() {
    			if ($(objRowNo).val() == $(this).closest("tr").index()) {
    				$(objRowNo).val("");
    			} else if($(objRowNo).val() > $(this).closest("tr").index()) {
    				$(objRowNo).val($(objRowNo).val() - 1);
    			}
    			$(this).closest("tr").remove();
    			$(objRowNo).val("");
				$(objTable).val("head");
				$(objSqlField).val("");
				$(objViewField).val("");
    		};
			//新增默认值
    		var objAdd = $('<input type="button" value="新增↓" class="btn btn-success">').click(function() {
    			if ($(objTable).val() == null || $(objTable).val() == "") {
					alert("请输入数据表！");
					return;
				}
    			if ($(objSqlField).val() == null || $(objSqlField).val() == "") {
    				alert("请输入SQL字段");
    				return;
    			}
    			if ($(objViewField).val() == null || $(objViewField).val() == "") {
    				alert("请输入画面字段");
    				return;
    			}
				var tdata = "";
				if ($(objRowNo).val() == null || $(objRowNo).val() == "") {
					//新增
					tdata = "<tr>";
					tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objTable).val() + "</td>";
					tdata += "<td>" + $(objSqlField).val() + "</td>";
					tdata += "<td>" + $(objViewField).val() + "</td>";
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
					tdata += "</tr>";
					$("#myTableModal table tbody").append(tdata);
				} else {
					//修改
        			var index = $(objRowNo).val();
        			var tr = $("#myTableModal table tbody tr:eq(" + index + ")");
        			tdata += "<td><input type='checkbox'></td>";
					tdata += "<td>" + $(objTable).val() + "</td>";
					tdata += "<td>" + $(objSqlField).val() + "</td>";
					tdata += "<td>" + $(objViewField).val() + "</td>";
					tdata += "<td><input type='button' value='删除' class='btn btn-default'></td>";
        			$(tr).html(tdata);
        			$(objRowNo).val("");
				}
				$("#myTableModal table tbody input[type='checkbox']").unbind("click");
        		$("#myTableModal table tbody input[type='checkbox']").bind("click", editDefault);
        		$("#myTableModal table tbody input[type='button']").unbind("click");
        		$("#myTableModal table tbody input[type='button']").bind("click", deleteDefault);
        		$(objRowNo).val("");
				$(objTale).val("");
				$(objSqlField).val("");
				$(objViewField).val("");
    		}).appendTo(objForm);
    		var th = "<tr><th></th><th>数据表</th><th>SQL文字段</th><th>画面字段</th><th></th>";
			var objTHead = $("#myTableModal table thead");
			$(objTHead).html(th);
			var objTBody = $("#myTableModal table tbody");
			$(objTBody).html("");
			var tbody = "";
			if (config.defaultValue != undefined && config.defaultValue != null && config.defaultValue != "") {
				$(objTarget).val(config.defaultValue.target);
				$(objProc).val(decodeURIComponent(config.defaultValue.sql));
				if (config.defaultValue.mapping != undefined && config.defaultValue.mapping != null && config.defaultValue.mapping != "") {
					$.each(config.defaultValue.mapping, function(n, value) {
						tbody += "<tr>";
						tbody += "<td><input type='checkbox'></td>";
						tbody += "<td>" + value.table + "</td>";
						tbody += "<td>" + value.sqlField + "</td>";
						tbody += "<td>" + value.viewField + "</td>";
						tbody += "<td><input type='button' value='删除' class='btn btn-default'></td>";
						tbody += "</tr>";
					});
				}
			}
			$("#myTableModal table tbody").html(tbody);
			$("#myTableModal table tbody input[type='checkbox']").unbind("click");
			$("#myTableModal table tbody input[type='checkbox']").bind("click", editDefault);
			$("#myTableModal table tbody input[type='button']").unbind("click");
			$("#myTableModal table tbody input[type='button']").bind("click", deleteDefault);
			$("#btn-myTable-confirm").unbind("click");
			$("#btn-myTable-confirm").bind("click", function() {
				if ($("#myTableModal table tbody tr").length > 0) {
					if ($(objProc).val() == null || $(objProc).val() == "") {
						alert("请输入SQL文/存储过程");
						return;
					}
				} else {
					config.defaultValue = {};
					$("#myTableModal").modal("hide");
					return;
				}
				config.defaultValue = {};
				config.defaultValue.target = $(objTarget).val();
				config.defaultValue.sql = encodeURIComponent($(objProc).val());
				config.defaultValue.mapping = [];
	    		$("#myTableModal table tbody tr").each(function() {
	    			var table = $(this).find("td:eq(1)").html();
	    			var sqlField = $(this).find("td:eq(2)").html();
	    			var viewField = $(this).find("td:eq(3)").html();
	    			config.defaultValue.mapping.push({table:table,sqlField:sqlField,viewField:viewField});
				});
				$("#myTableModal").modal("hide");
			});
			var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
        	modal.show();
		},
		add_button : function(event){
			var view = event.data.view;
			var config = view.config;
			$("#myTableModalTitle").html("自定义按钮一栏");
			var objForm = $("#myTableModal form");
			$(objForm).html("");
			var objBtnId = $('<input type="text" placeholder="按钮ID" class="form-control input-sm fl w15">').appendTo(objForm);
			var objBtnName = $('<input type="text" placeholder="按钮名称" class="form-control input-sm fl w15">').appendTo(objForm);
			var objBtnPos = $('<input type="text" placeholder="按钮位置" class="form-control input-sm fl w15">').appendTo(objForm);
			var objBtnUnit = $('<select><option value="bef">之前</option><option value="bak">之后</option></select>').appendTo(objForm);
			var objBtnType = $('<select><option value="yes">必须执行后保存</option><option value="no">不执行可以保存</option></select>').appendTo(objForm);
			var objBtnAdd = $('<input type="button" value="新增" class="btn btn-primary">').appendTo(objForm);
			var objIndex = $("<input type='hidden'>").appendTo(objForm);
			//删除按钮
			var deleteButton = function() {
				var id = $(this).closest("tr").find("td:eq(1)").html();
				$.each(config.buttons, function(n, value) {
					if (id == value.id) {
						config.buttons.splice(n, 1);
						return false;
					}
				});
				$(this).closest("tr").remove();
			};
			//Checkbox
			var checkButton = function() {
				if ($(this).prop("checked")) {
					//选择
					var ids = [];
					$("#myTableModal table tbody input[type='checkbox']").each(function() {
						if ($(this).prop("checked")) {
							ids.push($(this).closest("tr").index());
						}
					});
					if (ids.length > 1) {
						alert("请选择一条数据编辑！");
						$(this).prop("checked", false);
						return;
					}
					var id = $(this).closest("tr").find("td:eq(1)").html();
					var name = $(this).closest("tr").find("td:eq(2)").html();
					var buttonId = $(this).closest("tr").find("td:eq(3)").html();
					var position = $(this).closest("tr").find("td:eq(4)").html();
					var saveControl = $(this).closest("tr").find("td:eq(5)").html();
					$(objBtnId).val(id);
					$(objBtnName).val(name);
					$(objBtnPos).val(buttonId);
					$(objBtnUnit).val(position);
					$(objBtnType).val(saveControl);
					$(objIndex).val(ids[0]);
				} else {
					//取消选择
					$(objBtnId).val("");
					$(objBtnName).val("");
					$(objBtnPos).val("");
					$(objBtnUnit).val("bef");
					$(objBtnType).val("yes");
					$(objIndex).val("");
				}
			}
			//生成按钮列表
			var createList = function() {
				var tbody = "";
				$.each(config.buttons, function(n, value) {
					tbody += "<tr>";
					tbody += "<td><input type='checkbox' value='" + value.id + "'></td>";
					tbody += "<td>" + value.id + "</td>";
					tbody += "<td>" + value.name + "</td>";
					tbody += "<td>" + value.button + "</td>";
					tbody += "<td>" + value.position + "</td>";
					tbody += "<td>" + value.save + "</td>";
					tbody += "<td><input type='button' value='删除'></td>";
					tbody += "</tr>";
				});
				$("#myTableModal table tbody").html(tbody);
				$("#myTableModal table tbody input[type='button']").unbind("click");
				$("#myTableModal table tbody input[type='button']").bind("click", deleteButton);
				$("#myTableModal table tbody input[type='checkbox']").unbind("click");
				$("#myTableModal table tbody input[type='checkbox']").bind("click", checkButton);
			}
			$(objBtnAdd).unbind("click");
			$(objBtnAdd).bind("click", function() {
				if ($(objBtnId).val() == "") {
					alert("请输入按钮ID！");
					return;
				}
				if ($(objBtnName).val() == "") {
					alert("请输入按钮名称！");
					return;
				}
				if ($(objBtnPos).val() == "") {
					alert("请输入按钮位置！");
					return;
				}
				var id = $(objBtnId).val();
				var name = $(objBtnName).val();
				var buttonId = $(objBtnPos).val();
				var position = $(objBtnUnit).val();
				var save = $(objBtnType).val();
				if ($(objIndex).val() == "") {
					//新增
					var hasData = false;
					$.each(config.buttons, function(n, value) {
						if (id == value.id) {
							hasData = true;
							return false;
						}
					});
					if (hasData) {
						alert("按钮ID已存在，请重新输入！");
						$(objBtnId).select();
						return;
					}
					config.buttons.push({id:id,name:name,button:buttonId,position:position,save:save});
				} else {
					var index = $(objIndex).val();
					config.buttons[index].id = id;
					config.buttons[index].name = name;
					config.buttons[index].button = buttonId;
					config.buttons[index].position = position;
					config.buttons[index].save = save;
					$(objIndex).val("");
				}
				createList();
				$(objBtnId).val("");
				$(objBtnName).val("");
				$(objBtnPos).val("");
				$(objBtnUnit).val("bef");
				$(objBtnType).val("yes");
			});
			var objBtnSet = $('<input type="button" value="设定" class="btn btn-primary">').appendTo(objForm);
			$(objBtnSet).unbind("click");
			$(objBtnSet).bind("click", {view:view}, view.set_button);
			if (config.buttons == undefined || config.buttons == null) {
				config.buttons = [];
			}
			var thead = "<tr><th></th><th>按钮ID</th><th>按钮名称</th><th>按钮位置参照按钮</th><th>位置参照方式</th><th>是否控制保存</th><th>操作</th></tr>";
			$("#myTableModal table thead").html(thead);
			createList();
			var modal = $('#myTableModal').modal({backdrop: 'static', keyboard: false});
        	modal.show();
        	$("#btn-myTable-confirm").unbind("click");
			$("#btn-myTable-confirm").bind("click", function() {
				$("#myTableModal").modal("hide");
			});
		},
		set_button : function(event) {
			var view = event.data.view;
			var config = view.config;
			var ids = [];
			$("#myTableModal table tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).closest("tr").find("td:eq(1)").html());
				}
			});
			if (ids.length <= 0) {
				alert("请选择按钮设定！");
				return;
			} else if (ids.length > 1) {
				alert("只能选择一个按钮进行设定！");
				return;
			}
			$(view).unbind("showButtonProp");
			$(view).bind("showButtonProp", {view:view,id:ids[0]}, view.showButtonProp);
			$(view).trigger("showButtonProp");
		},
		showButtonProp:function(event) {
			var view = event.data.view;
			var config = view.config;
			var id = event.data.id;
			var button = null;
			$.each(config.buttons, function(n, value) {
				if (id == value.id) {
					button = value;
					return false;
				}
			});
			if (button == null) {
				alert("没有找到对应的按钮，请确认！");
				return;
			}
			$("#myTableModalTitle").html("自定义按钮属性");
			var objForm = $("#myTableModal form");
			$(objForm).html("");
			var div = $("<div></div>").appendTo(objForm);
			var objBtnId = $('<input type="text" placeholder="按钮ID" class="form-control input-sm fl w15" readonly>').appendTo(div);
			$(objBtnId).val(button.id);
			var objBtnName = $('<input type="text" placeholder="按钮名称" class="form-control input-sm fl w15" readonly>').appendTo(div);
			$(objBtnName).val(button.name);
			var objTarget = $('<select><option value="L">本地数据库</option><option value="R">远程数据库</option></select>').appendTo(div);
			var objProcessType = $('<select><option value="update">数据更新</option><option value="select">数据查询</option></select>').appendTo(div);
			var objBackType = $('<select><option value="edit">处理完后保留在修改画面</option><option value="list">处理完返回列表画面</option></select>').appendTo(div);
			$("<br>").appendTo(objForm);
			div = $("<div></div>").appendTo(objForm);
			var objSql = $('<textarea class="form-control input-sm fl w15" placeholder="SQL文/存储过程" style="width:90%;"></textarea>').appendTo(div);
			$("<br>").appendTo(objForm);
			div = $("<div></div>").appendTo(objForm);
			var objBtnAdd = $('<input type="button" value="SQL/存储过程新增" class="btn btn-primary">').appendTo(div);
			var objBtnUp = $('<input type="button" value="向上" class="btn btn-primary">').appendTo(div);
			var objBtnDown = $('<input type="button" value="向下" class="btn btn-primary">').appendTo(div);
			var objBtnMapping = $('<input type="button" value="数据返回设定" class="btn btn-primary">').appendTo(div);
			var objIndex = $("<input type='hidden'>").appendTo(objForm);
			//check
			var checkProc = function() {
				if ($(this).prop("checked")) {
					//选择
					var ids = [];
					$("#myTableModal table tbody input[type='checkbox']").each(function() {
						if ($(this).prop("checked")) {
							ids.push($(this).closest("tr").index());
						}
					});
					if (ids.length > 1) {
						alert("请选择一条数据编辑！");
						return;
					}
					$(objIndex).val(ids[0]);
					var target = $(this).closest("tr").find("td:eq(1)").html();
					var proc = decodeURIComponent($(this).closest("tr").find("td:eq(2)").html());
					var process = $(this).closest("tr").find("td:eq(3)").html();
					var back = $(this).closest("tr").find("td:eq(4)").html();
					$(objTarget).val(target);
					$(objSql).val(proc);
					$(objProcessType).val(process);
					$(objBackType).val(back);
				} else {
					//选择取消
					$(objSql).val("");
					$(objTarget).val("L");
					$(objProcessType).val("update");
					$(objBackType).val("edit");
					$(objIndex).val("");
				}
			};
			//删除数据
			var deleteProc = function() {
				var index = $(this).closest("tr").index();
				button.proc.splice(index, 1);
				$(objSql).val("");
				$(objTarget).val("L");
				$(objProcessType).val("update");
				$(objBackType).val("edit");
				$(objIndex).val("");
				createList();
			};
			//创建列表
			var createList = function() {
				var thead = "<tr><th></th><th>目标数据库</th><th>SQL/存储过程</th><th>处理类型</th><th>执行后处理</th><th>操作</th></tr>";
				if (button.proc == undefined || button.proc == null) {
					button.proc = [];
				}
				var tbody = "";
				$.each(button.proc, function(n, value) {
					tbody += "<tr>";
					tbody += "<td><input type='checkbox'></td>";
					tbody += "<td>" + value.target + "</td>";
					tbody += "<td>" + value.proc + "</td>";
					tbody += "<td>" + value.process + "</td>";
					tbody += "<td>" + value.back + "</td>";
					tbody += "<td><input type='button' value='删除'></td>";
					tbody += "</tr>";
				});
				$("#myTableModal table thead").html(thead);
				$("#myTableModal table tbody").html(tbody);
				$("#myTableModal table tbody input[type='checkbox']").unbind("click");
				$("#myTableModal table tbody input[type='checkbox']").bind("click", checkProc);
				$("#myTableModal table tbody input[type='button']").unbind("click");
				$("#myTableModal table tbody input[type='button']").bind("click", deleteProc);
			};
			createList();
			//新增
			$(objBtnAdd).click(function() {
				if ($(objSql).val() == "") {
					alert("请输入SQL文/存储过程！");
					return;
				}
				var proc = encodeURIComponent($(objSql).val());
				var target = $(objTarget).val();
				var process = $(objProcessType).val();
				var back = $(objBackType).val();
				if ($(objIndex).val() == "") {
					//新增					
					if (button.proc == undefined || button.proc == null) {
						button.proc = [];
					}
					button.proc.push({proc:proc,target:target,process:process,back:back});
				} else {
					//修改
					var objProc = button.proc[$(objIndex).val()];
					objProc.proc = proc;
					objProc.target = target;
					objProc.process = process;
					objProc.back = back;
					$(objIndex).val("");
				}
				$(objSql).val("");
				$(objTarget).val("L");
				$(objProcessType).val("update");
				$(objBackType).val("edit");
				createList();
			});
			//向上
			$(objBtnUp).click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要调整顺序的数据！");
					return;
				}
				var index = parseInt($(objIndex).val());
				if (index <= 0) {
					return;
				}
				var temp = button.proc[index];
				button.proc.splice(index, 1);
				index--;
				button.proc.splice(index, 0, temp);
				$(objIndex).val(index);
				createList();
				$("#myTableModal table tbody tr:eq(" + index + ")").find("input[type='checkbox']").prop("checked", true);
			});
			//向下
			$(objBtnDown).click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要调整顺序的数据！");
					return;
				}
				var index = parseInt($(objIndex).val());
				var len = $("#myTableModal table tbody tr").length - 1;
				if (index >= len) {
					return;
				}
				var temp = button.proc[index];
				button.proc.splice(index, 1);
				index++;
				button.proc.splice(index, 0, temp);
				$(objIndex).val(index);
				createList();
				$("#myTableModal table tbody tr:eq(" + index + ")").find("input[type='checkbox']").prop("checked", true);
			});
			//数据返回设定
			$(objBtnMapping).click(function() {
				if ($(objIndex).val() == "") {
					alert("请选择需要设定的SQL/存储过程！");
					return;
				}
				var index = parseInt($(objIndex).val());
				var objProc = button.proc[index];
				if (objProc.process != "select") {
					alert("您选择的存储过程没有返回结果集！");
					return;
				}
				$(view).unbind("showProcProp");
				$(view).bind("showProcProp", {view:view,proc:objProc,id:id}, view.showProcProp);
				$(view).trigger("showProcProp");
			});
			$("#btn-myTable-confirm").unbind("click");
			$("#btn-myTable-confirm").bind("click", function() {
				$("#btn_define_button").trigger("click", {view:view});
			});
		},
		showProcProp : function(event) {
			var view = event.data.view;
			var objProc = event.data.proc;
			var id = event.data.id;
			$("#myTableModalTitle").html("字段映射设定");
			var objForm = $("#myTableModal form");
			$(objForm).html("");
			var objSourceField = $('<input type="text" placeholder="SQL/存储过程返回字段" class="form-control input-sm fl w15">').appendTo(objForm);
			var objTargetType = $('<select><option value="head">Head字段</option><option value="detail">明细字段</option></select>').appendTo(objForm);
			var objTargetField = $('<input type="text" placeholder="画面字段" class="form-control input-sm fl w15">').appendTo(objForm);
			var objBtnAdd = $('<input type="button" value="新增" class="btn btn-primary">').appendTo(objForm);
			var objIndex = $("<input type='hidden'>").appendTo(objForm);
			//创建List
			var createList = function() {
				var thead = "<tr><th></th><th>SQL/存储过程返回字段</th><th>字段类型</th><th>画面字段</th><th></th></tr>";
				var tbody = "";
				if (objProc.mapping == undefined || objProc.mapping == null) {
					objProc.mapping = [];
				}
				$.each(objProc.mapping, function(n, value) {
					tbody += "<tr>";
					tbody += "<td><input type='checkbox'></td>";
					tbody += "<td>" + value.sourceField + "</td>";
					tbody += "<td>" + value.targetType + "</td>";
					tbody += "<td>" + value.targetField + "</td>";
					tbody += "<td><input type='button' value='删除'></td>";
					tbody += "</tr>";
				});
				$("#myTableModal table thead").html(thead);
				$("#myTableModal table tbody").html(tbody);
				$("#myTableModal table tbody input[type='checkbox']").unbind("click");
				$("#myTableModal table tbody input[type='checkbox']").bind("click", checkMapping);
				$("#myTableModal table tbody input[type='button']").unbind("click");
				$("#myTableModal table tbody input[type='button']").bind("click", deleteMapping);
			};
			//选择数据
			var checkMapping = function() {
				if ($(this).prop("checked")) {
					//选择
					var indexs = [];
					$("#myTableModal table tbody input[type='checkbox']").each(function() {
						if ($(this).prop("checked")) {
							indexs.push($(this).closest("tr").index());
						}
					});
					if (indexs.length > 1) {
						alert("只能选择一条数据修改！");
						return;
					}
					var sourceField = $(this).closest("tr").find("td:eq(1)").html();
					var targetType = $(this).closest("tr").find("td:eq(2)").html();
					var targetField = $(this).closest("tr").find("td:eq(3)").html();
					$(objSourceField).val(sourceField);
					$(objTargetType).val(targetType);
					$(objTargetField).val(targetField);
					$(objIndex).val(indexs[0]);
				} else {
					//取消选择
					$(objSourceField).val("");
					$(objTargetType).val("head");
					$(objTargetField).val("");
					$(objIndex).val("");
				}
			};
			//删除
			var deleteMapping = function() {
				var index = $(this).closest("tr").index();
				objProc.mapping.splice(index, 1);
				$(objSourceField).val("");
				$(objTargetType).val("head");
				$(objTargetField).val("");
				$(objIndex).val("");
				createList();
			};
			//新增按钮
			$(objBtnAdd).click(function() {
				if ($(objSourceField).val() == "") {
					alert("请输入SQL/存储过程字段！");
					return;
				}
				if ($(objTargetField).val() == "") {
					alert("请输入画面字段！");
					return;
				}
				var sourceField = $(objSourceField).val();
				var targetType = $(objTargetType).val();
				var targetField = $(objTargetField).val();
				if ($(objIndex).val() == "") {
					//新增
					objProc.mapping.push({sourceField:sourceField,targetType:targetType,targetField:targetField});
				} else {
					//修改
					var index = parseInt($(objIndex).val());
					var objMapping = objProc.mapping[index];
					objMapping.sourceField = sourceField;
					objMapping.targetType = targetType;
					objMapping.targetField = targetField;
				}
				$(objSourceField).val("");
				$(objTargetType).val("head");
				$(objTargetField).val("");
				$(objIndex).val("");
				createList();
			});
			createList();
			$("#btn-myTable-confirm").unbind("click");
			$("#btn-myTable-confirm").bind("click", function() {
				$(view).trigger("showButtonProp", {view:view,id:id});
			});
		}
	};
	$.fn.mytable = function(options) {
		var mytable = new MyTable(this, options);
		return mytable.init();
	}	
})(jQuery);