(function($){
	var MyList = function(ele, opt, appName, viewNo, data, conditionData, token) {
		//alert(appName);
		this.$element = ele;
		this.defaults = {
			type:'S',
			sql:'',
			condition:[],
			view:[]
		};
		this.config = $.extend({}, this.defaults, opt);
		this.appName = appName;
		this.viewNo = viewNo;
		this.data = data;
		this.conditionData = conditionData;
		this.token = token;
		//alert(JSON.stringify(data));
	};
	MyList.prototype = {
		init : function() {
			var form = $("#searchForm");
			//alert(JSON.stringify(this.config.condition));
			var appName = this.appName;
			var listData = this.data;
			var config = this.config;
			var conditionData = this.conditionData;
			var openWindow = function(param, controler, redrawCondition) {
				var paramCondition = {};
				$.each(config.condition, function(n, condition) {
					//alert(condition.editor);
					var field = condition.field;
					if (condition.alias != undefined && condition.alias != null && condition.alias != "") {
						field = condition.alias;
					}
					switch (condition.editor) {
					case "inputEditor":
					case "selectEditor":
					case "openEditor":
					case "dateEditor":
					case "mutilInputEditor":
						paramCondition["list_" + field] = $("#" + field).val();
						break;
					case "checkEditor":
						if ($("#" + field).prop("checked")) {
							paramCondition["list_" + field] = "Y";
						} else {
							paramCondition["list_" + field] = "N";
						}
						break;
					default:
						paramCondition["list_" + field] = $("#" + field).val();
						break;
					}
				});
				param.viewData = paramCondition;
				//alert(JSON.stringify(param));
				MessageBox.processStart();
				$.ajax({
					url:appName + "/OpenWindow/getWindow",
					type:"post",
					dataType:"json",
					contentType: "application/json",
					data:JSON.stringify(param),
					success:function(data) {
						if (data.errType == "1") {
							MessageBox.processEnd(data.errMessage);
							return;
						}
						MessageBox.processClose();
						if (redrawCondition) {
							$("#myOpenWinModalLabel").html(data.openWin.name);
							var objForm = $("#myOpenWinModal form");
							$(objForm).html("");
							$.each(eval(data.openWin.condition), function (n, condition) {
								switch(condition.editor) {
								case "inputEditor":
									$('<input id="search_openWin_' + n + '" class="form-control input-sm fl w15" placeholder="' + condition.label + '">').appendTo(objForm);
									break;
								case "checkEditor":
									$('<input type="checkbox" id="search_openWin_' + n + '">').appendTo(objForm);
									$('<label>' + condition.label + '</label>').appendTo(objForm);
									break;
								case "dateEditor":
									//日期
									var tempDiv = $('<div class="input-group date form_date fl w15"></div>').appendTo(objForm);
									$('<input type="text" class="form-control input-sm" id="search_openWin_' + n + '" placeholder="' + condition.label + '" readonly>').appendTo(tempDiv);
									var objSpan = $('<span class="input-group-addon"></span>').appendTo(tempDiv);
									$('<span class="fa fa-calendar"></span>').appendTo(objSpan);
									break;
								case "mutilInputEditor":
									//多行输入框
									$('<textarea id="search_openWin_' + n + '" placeholder="' + condition.label + '"></textarea>').appendTo(objForm);
									break;
								default:
									$('<input id="search_openWin_' + n + '" class="form-control input-sm fl w15" placeholder="' + condition.label + '">').appendTo(objForm);
									break;
								}
							});
							var searchDiv = $('<div class="search"></div>').appendTo(objForm);
							//查询按钮
	                        $('<button type="button" class="btn btn-block btn-success fa fa-search"><span class="search-btn">查找</span></button>').click(function() {
	                        	var objParam = new Object;
	                        	$.each(eval(data.openWin.condition), function(n, condition) {
	                        		var sField = condition.field;
	                        		sField = sField.replaceAll("\\.", "_");
	                        		if (condition.editor == "checkEditor") {
	                        			if ($("#search_openWin_" + n).prop("checked")) {
	                        				objParam[sField] = "Y";
	                        			} else {
	                        				objParam[sField] = "N";
	                        			}
	                        		} else {
	                        			//alert($("#search_openWin_UserNo").val());
	                        			objParam[sField] = $("#search_openWin_" + n).val();
	                        		}                        		
	                        	});
	                        	objParam.openWinNo = param.openWinNo;
	                        	//alert(JSON.stringify(objParam));
	                        	openWindow(objParam, controler, false);
	                        }).appendTo(searchDiv);
						}
                        //显示表头数据
                        var th = "<tr><th></th>";
                        $.each(eval(data.openWin.viewField), function(n, view) {
                        	th += "<th>" + view.label + "</th>";
                        });
                        th += "</tr>";
                        $("#myOpenWinModal table thead").html(th);
                        //显示数据
                        var tbody = "";
                        $.each(data.data, function(n, value) {
                        	tbody += "<tr>";
                        	tbody += "<td><input type='checkbox'></td>";
                        	$.each(eval(data.openWin.viewField), function(n, view) {
                        		tbody += "<td>" + value[view.field] + "</td>";
                        	});
                        	tbody += "</tr>";
                        });
                        $("#myOpenWinModal table tbody").html(tbody);
                        //设定确定按钮事件
    					$("#btn-myOpenWin-confirm").unbind("click");
    					$("#btn-myOpenWin-confirm").bind("click", function() {
    						var ids = [];
    						$("#myOpenWinModal table tbody input[type='checkbox']").each(function() {
    							if ($(this).prop("checked")) {
    								ids.push($(this).closest("tr").index());
    							}
    						});
    						if (ids.length > 1) {
    							MessageBox.info("请仅选择一条数据！");
    							return;
    						}
    						var indexNo = -1;
    						var indexName = -1;
    						$.each(eval(data.openWin.viewField), function(n, view) {
    							if (view.field == "no") {
    								indexNo = n;
    							} else if (view.field == "name") {
    								indexName = n;
    							}
    						});
    						indexNo++;
    						indexName++;
							var tr = $("#myOpenWinModal table tbody tr").eq(ids[0]);
							var objNo = controler.objNo;
							$(objNo).val($(tr).find("td:eq(" + indexNo + ")").html());
							var objName = controler.objName;
							$(objName).val($(tr).find("td:eq(" + indexName + ")").html());
			
    						$("#myOpenWinModal").modal("hide");
    					});
						var modal = $('#myOpenWinModal').modal({backdrop: 'static', keyboard: false});
				    	modal.show();
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			};
			//生成查询条件
			$.each(this.config.condition, function(n, value) {
				var objDiv = $('<div class="fl"></div>').appendTo(form);
				var field = value.field;
				if (value.alias != undefined && value.alias != null && value.alias != "") {
					field = value.alias;
				}
				$('<label for="' + field + '" class="label-header label-header1">' + value.label + '：</label>').appendTo(objDiv);
				switch (value.editor) {
				case "inputEditor":
					//文本框
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					$('<input id="' + field + '" type="text" class="form-control input-sm fl w15" value="' + valueData + '">').appendTo(objDiv);
					break;
				case "selectEditor":
					//下拉框
					//alert(JSON.stringify(value));
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					var objSelect = $('<select id="' + field + '" class="form-control input-sm fl w15"></select>').appendTo(objDiv);
					$.ajax({
						url:appName + "/view/dropdown",
						type:"post",
						dataType:"json",
						data:{
							sql:decodeURIComponent(value.sqlFunc),
							target:value.target
						},
						success:function(data) {
							if (data.errType == "1") {
								MessageBox.info(data.errMessage);
								return;
							}
							var options = "";
							$.each(data.data, function(n, value) {
								options += "<option value='" + value.no + "'>" + value.name + "</option>";
							});
							$(objSelect).html(options);
							if (valueData != null && valueData != "") {
								$(objSelect).val(valueData);
							}
						},
						error:function(XMLHttpRequest, data) {
							MessageBox.processEnd("系统错误，详细信息：" + data);
						}
					});
					break;
				case "openEditor":
					//开窗选择框
					var objFieldName = $('<input id="' + field + '_name" type="text class="form-control input-sm fl w15" readonly>').appendTo(objDiv);
					var objFieldId = $('<input type="hidden" id="' + field + '">').appendTo(objDiv);
					$('<input type="button" value="...">').click(function() {
						//开窗处理
						openWindow({openWinNo:value.openWinNo}, {objNo:objFieldId, objName:objFieldName}, true);
					}).appendTo(objDiv);
					break;
				case "checkEditor":
					//checkBox
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "N";
					}
					if (valueData == "Y") {
						$('<input type="checkbox" id="' + field + '" checked>').appendTo(objDiv);
					} else {
						$('<input type="checkbox" id="' + field + '">').appendTo(objDiv);
					}
					break;
				case "dateEditor":
					//日期
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					var tempDiv = $('<div class="input-group date form_date fl w15"></div>').appendTo(objDiv);
					$('<input type="text" class="form-control input-sm" id="' + field + '" placeholder="' + value.label + '" value="' + valueData + '" readonly>').appendTo(tempDiv);
					var objSpan = $('<span class="input-group-addon"></span>').appendTo(tempDiv);
					$('<span class="fa fa-calendar"></span>').appendTo(objSpan);
					break;
				case "mutilInputEditor":
					//多行输入框
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					$('<textarea id="' + field + '">' + valueData + '</textarea>').appendTo(objDiv);
					break;
				default:
					//文本框
					var valueData = conditionData[field];
				if (valueData == undefined) {
					valueData = "";
				}
					$('<input id="' + field + '" type="text" class="form-control input-sm fl w15" value="' + valueData + '">').appendTo(objDiv);
					break;
				}
			});
			//生成列表
			var thead = "<tr><th></th>";
			$.each(this.config.view, function(n, value) {
				thead += "<th>" + value.label + "</th>"; 
			});
			thead += "</tr>";
			$("#dataTable thead").html(thead);
			//生成列表数据
			var tbody = "";
			$.each(listData, function(n, value) {
				tbody += "<tr>";
				tbody += "<td><input type='checkbox' value='" + value.id + "'><input type='hidden' value='" + value.status + "'></td>";
				$.each(config.view, function(n, view) {
					if (value[view.field] == undefined) {
						tbody += "<td></td>";
					} else {
						tbody += "<td>" + value[view.field] + "</td>";
					}
				});
				tbody += "</tr>";
			});
			$("#dataTable tbody").html(tbody);
			//设定查询按钮事件
			$("#btn-search").unbind("click");
			$("#btn-search").bind("click", {config:this.config,appName:appName, viewNo:this.viewNo}, this.search_data);
			//新增按钮事件
			$("#btn_add").unbind("click");
			$("#btn_add").bind("click", {config:this.config,appName:appName, viewNo:this.viewNo}, this.add_data);
			//修改按钮事件
			$("#btn_edit").unbind("click");
			$("#btn_edit").bind("click", {config:this.config,appName:appName, viewNo:this.viewNo}, this.edit_data);
			//删除按钮事件
			$("#btn_delete").unbind("click");
			$("#btn_delete").bind("click", {config:this.config, appName:appName, viewNo:this.viewNo}, this.delete_data);
			//查看按钮事件
			$("#btn_view").unbind("click");
			$("#btn_view").bind("click", {config:this.config, appName:appName, viewNo:this.viewNo}, this.view_data);
			//提交按钮事件
			$("#btn-submit").unbind("click");
			$("#btn-submit").bind("click", {view:this}, this.submit_data);
			$(window).resize();
			//撤销按钮事件
			$("#btn_cancel_submit").unbind("click");
			$("#btn_cancel_submit").bind("click", {view:this}, this.cancelSubmit_data);
			//查审按钮事件
			$("#btn_view_audit").unbind("click");
			$("#btn_view_audit").bind("click", {view:this}, this.viewAudit);			
			return this;
		},
		search_data : function(event) {
			var config = event.data.config;
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
			var param = new Object;
			$.each(config.condition, function(n, condition) {
				//alert(condition.editor);
				var field = condition.field;
				if (condition.alias != undefined && condition.alias != null && condition.alias != "") {
					field = condition.alias;
				}
				switch (condition.editor) {
				case "inputEditor":
				case "selectEditor":
				case "openEditor":
				case "dateEditor":
				case "mutilInputEditor":
					param[field] = $("#" + field).val();
					break;
				case "checkEditor":
					if ($("#" + field).prop("checked")) {
						param[field] = "Y";
					} else {
						param[field] = "N";
					}
					break;
				default:
					param[field] = $("#" + field).val();
					break;
				}
			});
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/list/" + viewNo,
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					var tbody = "";
					$.each(data.data, function(n, value) {
						tbody += "<tr>";
						tbody += "<td><input type='checkbox' value='" + value.id + "'><input type='hidden' value='" + value.status + "'></td>";
						$.each(config.view, function(n, view) {
							if (value[view.field] == undefined) {
								tbody += "<td></td>";
							} else {
								tbody += "<td>" + value[view.field] + "</td>";
							}
						});
						tbody += "</tr>";
					});
					//alert(tbody);
					$("#dataTable tbody").html(tbody);
					MessageBox.processClose();
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		},
		add_data : function(event) {
			var config = event.data.config;
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
			var sUrl = appName + "/view/add/" + viewNo;// + "?id="
			window.location.href = sUrl;
		},
		edit_data : function(event) {
			var config = event.data.config;
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
			var ids = [];
			var dataStatus = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					dataStatus.push($(this).closest("tr").find("input[type='hidden']").val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择需要编辑的数据！");
				return;
			}
			if (ids.length > 1) {
				MessageBox.info("只能选择一条数据进行编辑！");
				return;
			}
			if (dataStatus[0] != "0") {
				if (dataStatus[0] == "1") {
					MessageBox.info("您选择的单据正在审核中，不能修改！");
					return;
				}
				MessageBox.info("您选择的单据审核完成，不能修改！");
				return;
			}
			var sUrl = appName + "/view/edit/" + viewNo + "?id=" + ids[0];
			window.location.href = sUrl;
		},
		delete_data : function(event) {
			var config = event.data.config;
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
			var ids = [];
			var dataStatus = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					dataStatus.push($(this).closest("tr").find("input[type='hidden']").val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要删除的数据！");
				return;
			}
			var bError = false;
			$.each(dataStatus, function(n, value) {
				if (value != "0") {
					if (value == "1") {
						MessageBox.info("您选择的数据已在审核中，不可以删除！");
						bError = true;
						return false;
					} else {
						MessageBox.info("您选择的数据已审核，不可以删除！");
						bError = true;
						return false;
					}
				}
			});
			if (bError) {
				return;
			}
			MessageBox.confirm("您真到要删除选中的数据吗？", function(){
				MessageBox.processStart();
				$.ajax({
					url:appName + "/view/delete/" + viewNo,
					type:"post",
					dataType:"json",
					data:{
						ids:ids.toString()
					},
					success:function(data) {
						if (data.errType == "1") {
							MessageBox.processEnd(data.errMessage);
							return;
						}
						MessageBox.processEnd("删除成功！", function() {
							$("#btn-search").trigger("click");
						});						
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			});
		},
		view_data : function(event) {
			var config = event.data.config;
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
			var ids = [];
			var dataStatus = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					dataStatus.push($(this).closest("tr").find("input[type='hidden']").val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择需要编辑的数据！");
				return;
			}
			if (ids.length > 1) {
				MessageBox.info("只能选择一条数据进行编辑！");
				return;
			}
			var sUrl = appName + "/view/view/" + viewNo + "?id=" + ids[0];
			window.location.href = sUrl;
		},
		cancelSubmit_data : function(event) {
			var list = event.data.view;
			var config = list.config;
			var appName = list.appName;
			var viewNo = list.viewNo;
			var token = list.token;
			var ids = [];
			var dataStatus = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					dataStatus.push($(this).closest("tr").find("input[type='hidden']").val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要提交的数据！");
				return;
			}
			var bError = false;
			$.each(dataStatus, function(n, value) {
				if (value != "1") {
					if (value == "0") {
						MessageBox.info("您选择的数据尚未提交，不可以撤销！");
						bError = true;
						return false;
					} else {
						MessageBox.info("您选择的数据已审核，不可以撤销！");
						bError = true;
						return false;
					}
				}
			});
			if (bError) {
				return;
			}
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/cancelSubmit/" + viewNo,
				type:"post",
				dataType:"json",
				data:{
					ids:ids.toString(),
					token:token
				},
				success:function(data) {
					if (data.token != undefined && data.token != null && data.token !== "") {
						list.token = data.token;
					}
					if (data.errType == "0") {
						MessageBox.processEnd("撤销成功！", function() {
							$("#btn-search").trigger("click");
						});
						return;
					} else {
						MessageBox.processEnd(data.errMessage);
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，信息信息：" + data);
				}
			});
		},
		viewAudit:function(event){
			var list = event.data.view;
			var config = list.config;
			var appName = list.appName;
			var viewNo = list.viewNo;
			var token = list.token;
			var ids = [];
			var dataStatus = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					dataStatus.push($(this).closest("tr").find("input[type='hidden']").val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要提交的数据！");
				return;
			} else if (ids.length > 1) {
				MessageBox.info("请选择一条数据查审！");
				return;
			}
			var bError = false;
			$.each(dataStatus, function(n, value) {
				if (value == "0") {
					MessageBox.info("您选择的数据没有提交流程，不可以查审！");
					bError = true;
					return false;
				}
			});
			if (bError) {
				return;
			}
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/viewAudit/" + viewNo,
				type:"post",
				dataType:"json",
				data:{
					id:ids.toString()
				},
				success:function(data) {
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					var tbody = "";
					$.each(data.data, function(n, value) {
						tbody += "<tr>";
						tbody += "<td>" + value.userName + "</td>";
						var realUserName = "";
						if (value.realUserName != undefined && value.realUserName != null) {
							realUserName = value.realUserName;
						}
						tbody += "<td>" + realUserName + "</td>";
						var auditStatus = "";
						if (value.auditStatus == "0") {
							auditStatus = "未审核";
						} else if (value.auditStatus == "1") {
							auditStatus = "不同意";
						} else if (value.auditStatus == "2") {
							auditStatus = "已审核";
						} else if (value.auditStatus == "3") {
							auditStatus = "已弃审";
						}
						tbody += "<td>" + auditStatus + "</td>";
						tbody += "<td>" + value.auditTime + "</td>";
						tbody += "</tr>";
					});
					$("#viewAuditTable tbody").html(tbody);
					MessageBox.processClose();
					var modal = $('#myViewAuditModal').modal({backdrop: 'static', keyboard: false});
			    	modal.show();					
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		},
		submit_data : function(event) {
			var list = event.data.view;
			var config = list.config;
			var appName = list.appName;
			var viewNo = list.viewNo;
			var token = list.token;
			var ids = [];
			var dataStatus = [];
			$("#dataTable tbody input[type='checkbox']").each(function() {
				if ($(this).prop("checked")) {
					ids.push($(this).val());
					dataStatus.push($(this).closest("tr").find("input[type='hidden']").val());
				}
			});
			if (ids.length == 0) {
				MessageBox.info("请选择要提交的数据！");
				return;
			}
			var bError = false;
			$.each(dataStatus, function(n, value) {
				if (value != "0") {
					if (value == "1") {
						MessageBox.info("您选择的数据已在审核中，不可以提交！");
						bError = true;
						return false;
					} else {
						MessageBox.info("您选择的数据已审核，不可以提交！");
						bError = true;
						return false;
					}
				}
			});
			if (bError) {
				return;
			}
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/submit/" + viewNo,
				type:"post",
				dataType:"json",
				data:{
					ids:ids.toString(),
					token:token
				},
				success:function(data) {
					if (data.token != undefined && data.token != null && data.token !== "") {
						list.token = data.token;
					}
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					MessageBox.processEnd("提交成功！", function() {
						$("#btn-search").trigger("click");
					});						
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		}
	};
	$.fn.mylist = function(options, appName, viewNo, data, conditionData, token) {
		var mylist = new MyList(this, options, appName, viewNo, data, conditionData, token);
		return mylist.init();
	}	
})(jQuery);