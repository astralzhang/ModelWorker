(function($){
	var MyReport = function(ele, viewScript, appName, reportNo, conditionData, listData) {
		this.$element = ele;
		this.defaults = {

		};
		this.config = $.extend({}, this.defaults, viewScript);
		this.appName = appName;
		this.reportNo = reportNo;
		this.conditionData = conditionData;
		this.listData = listData;
		//alert(JSON.stringify(conditionData));
		//alert(reportNo);
	};
	MyReport.prototype = {
		init : function() {
			var form = $("#searchForm");
			//alert(JSON.stringify(this.config.viewScript.condition));
			var appName = this.appName;
			var listData = this.data;
			var config = this.config;
			var conditionData = this.conditionData;
			var openWindow = function(param, controler, redrawCondition) {
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
								var field = condition.field;
								field = field.replaceAll("\\.", "_");
								switch(condition.editor) {
								case "inputEditor":
									$('<input id="search_openWin_' + field + '" class="form-control input-sm fl w15" placeholder="' + condition.label + '">').appendTo(objForm);
									break;
								case "checkEditor":
									$('<input type="checkbox" id="search_openWin_' + field + '">').appendTo(objForm);
									$('<label>' + condition.label + '</label>').appendTo(objForm);
									break;
								case "dateEditor":
									//日期
									var tempDiv = $('<div class="input-group date form_date fl w15"></div>').appendTo(objForm);
									$('<input type="text" class="form-control input-sm" id="search_openWin_' + field + '" placeholder="' + condition.label + '" readonly>').appendTo(tempDiv);
									var objSpan = $('<span class="input-group-addon"></span>').appendTo(tempDiv);
									$('<span class="fa fa-calendar"></span>').appendTo(objSpan);
									break;
								case "mutilInputEditor":
									//多行输入框
									$('<textarea id="search_openWin_' + field + '" placeholder="' + condition.label + '"></textarea>').appendTo(objForm);
									break;
								default:
									$('<input id="search_openWin_' + field + '" class="form-control input-sm fl w15" placeholder="' + condition.label + '">').appendTo(objForm);
									break;
								}
							});
							var searchDiv = $('<div class="search"></div>').appendTo(objForm);
							//查询按钮
	                        $('<button type="button" class="btn btn-block btn-success fa fa-search"><span class="search-btn">查找</span></button>').click(function() {
	                        	var objParam = new Object;
	                        	$.each(eval(data.openWin.condition), function(n, condition) {
	                        		var field = condition.field;
	                        		field = field.replaceAll("\\.", "_");
	                        		if (condition.editor == "checkEditor") {
	                        			if ($("#search_openWin_" + field).prop("checked")) {
	                        				objParam[field] = "Y";
	                        			} else {
	                        				objParam[field] = "N";
	                        			}
	                        		} else {
	                        			//alert($("#search_openWin_UserNo").val());
	                        			objParam[field] = $("#search_openWin_" + field).val();
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
			//alert(JSON.stringify(this.config.viewScript.condition));
			$.each(this.config.condition, function(n, value) {
				//alert(JSON.stringify(value));
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
					$('<input id="' + field + '" name="' + field + '" type="text" class="form-control input-sm fl w15" value="' + valueData + '">').appendTo(objDiv);
					break;
				case "selectEditor":
					//下拉框
					//alert(JSON.stringify(value));
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					var objSelect = $('<select id="' + field + '" name="' + field + '" class="form-control input-sm fl w15"></select>').appendTo(objDiv);
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
					var objFieldId = $('<input type="hidden" id="' + field + '" name="' + field + '">').appendTo(objDiv);
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
						$('<input type="checkbox" id="' + field + '" name="' + field + '" checked>').appendTo(objDiv);
					} else {
						$('<input type="checkbox" id="' + field + '" name="' + field + '">').appendTo(objDiv);
					}
					break;
				case "dateEditor":
					//日期
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					var tempDiv = $('<div class="input-group date form_date fl w15"></div>').appendTo(objDiv);
					$('<input type="text" class="form-control input-sm" id="' + field + '" name="' + field + '" placeholder="' + value.label + '" value="' + valueData + '" readonly>').appendTo(tempDiv);
					var objSpan = $('<span class="input-group-addon"></span>').appendTo(tempDiv);
					$('<span class="fa fa-calendar"></span>').appendTo(objSpan);
					break;
				case "mutilInputEditor":
					//多行输入框
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					$('<textarea id="' + field + '" name="' + field + '">' + valueData + '</textarea>').appendTo(objDiv);
					break;
				default:
					//文本框
					var valueData = conditionData[field];
					if (valueData == undefined) {
						valueData = "";
					}
					$('<input id="' + field + '" name="' + field + '" type="text" class="form-control input-sm fl w15" value="' + valueData + '">').appendTo(objDiv);
					break;
				}
			});
			//生成列表
			var thead = "<tr>";
			//alert(JSON.stringify(this.config.view));
			$.each(this.config.view, function(n, value) {
				thead += "<th>" + value.label + "</th>"; 
			});
			thead += "</tr>";
			$("#dataTable thead").html(thead);
			$(this).unbind("showData");
			$(this).bind("showData", {report:this,data:this.listData}, this.show_data);
			$(this).trigger("showData");
			//设定查询按钮事件
			$("#btn-search").unbind("click");
			$("#btn-search").bind("click", {report:this,appName:appName, reportNo:this.reportNo}, this.search_data);
			//下载按钮
			$("#btn_download").unbind("click");
			$("#btn_download").bind("click", {report:this,appName:appName}, this.download);
			//下载处理
			$("#btn-template-download").unbind("click");
			$("#btn-template-download").bind("click", {report:this,templateId:""}, this.downloadFile);
			return this;
		},
		show_data : function(event) {
			var objReport = event.data.report;
			var config = objReport.config;
			var listData = event.data.data;
			var tbody = "";
			//alert(JSON.stringify(config.viewScript.link));
			$.each(listData, function(n, value) {
				tbody += "<tr>";
				$.each(config.view, function(n, view) {
					if (value[view.field] == undefined) {
						tbody += "<td></td>";
					} else {
						if (view.link == "Y") {
							tbody += "<td><a href='#'>";
							tbody += value[view.field];
							tbody += "</a></td>";
						} else {
							tbody += "<td>" + value[view.field] + "</td>";
						}
					}
				});
				tbody += "</tr>";
			});
			$("#dataTable tbody").html(tbody);
			$("#dataTable tbody a").unbind("click");
			$("#dataTable tbody a").bind("click", {report:objReport, data:listData}, objReport.show_link_window);
			$(window).resize();
		},
		search_data : function(event) {
			var objReport = event.data.report;
			var config = objReport.config;
			var appName = event.data.appName;
			var reportNo = event.data.reportNo;
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
			reportNo += "Report";
			MessageBox.processStart();
			$.ajax({
				url:appName + "/report/search/" + reportNo,
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					$(objReport).unbind("showData");
					$(objReport).bind("showData", {report:objReport,data:data.data}, objReport.show_data);
					$(this).trigger("showData");
					$(objReport).trigger("showData");
					MessageBox.processClose();
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		},
		show_link_window:function(event) {
			var objReport = event.data.report;
			var config = objReport.config;
			var listData = event.data.data;
			var rowIndex = $(this).closest("tr").index();
			var colIndex = $(this).closest("td").index();
			if (rowIndex < 0 || colIndex < 0) {
				return;
			}
			if (listData == undefined || listData == null || listData === "") {
				return;
			}
			if (config.view[colIndex] == undefined || config.view[colIndex] == null || config.view[colIndex] === "") {
				return;
			}
			var fieldName = config.view[colIndex].field;
			var rowData = listData[rowIndex];
			if (rowData == undefined || rowData == null || rowData === "") {
				return;
			}
			var viewNo = config.view[colIndex].viewNo;
			if (viewNo == undefined || viewNo == null || viewNo === "") {
				return;
			}
			
			var idField = config.view[colIndex].idField;
			if (idField == undefined || idField == null || idField === "") {
				return;
			}
			var id = rowData[idField];
			if (id == undefined || id == null || id === "") {
				return;
			}
			$("#viewData").html('<span style="float:right;" id="attachmentShow"></span>');
			$("#viewData").myview(objReport.appName, id, viewNo);
			//$("#myModal1").modal({remote:"/flow/view/view/OverTimeJL?id=c58b84f8-8265-415c-baf9-fec452fb43a4&NO_SITE_MESH=1"});
		},
		download : function(event) {
			var objReport = event.data.report;
			var config = objReport.config;
			if ($("#myTemplateModal table tbody tr").length > 0) {
				var modal = $('#myTemplateModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			} else {
				$("#btn-template-download").trigger("click", {report:objReport,templateId:"",noTemplate:"Y"});
			}
		},
		downloadFile : function(event) {
			var objReport = event.data.report;
			var templateId = event.data.templateId;
			var noTemplate = event.data.noTemplate;
			var config = objReport.config;
			var appName = objReport.appName;
			var reportNo = objReport.reportNo;
			if (noTemplate == undefined) {
				var ids = [];
				$("#myTemplateModal table tbody tr input[type='checkbox']").each(function() {
					if ($(this).prop("checked")) {
						ids.push($(this).val());
					}
				});
				if (ids.length <= 0) {
					MessageBox.confirm("您没有选择模板，是否下载无模板文件！", function() {
						$("#searchForm").attr("action", appName + "/report/download/" + reportNo + "ReportDownload?templateId=");
						$("#searchForm").submit();
					})
				} else if (ids.length > 1) {
					MessageBox.info("不能选择多个模板下载文件，请选择一个模板文件！");
					return;
				} else {
					$("#searchForm").attr("action", appName + "/report/download/" + reportNo + "ReportDownload?templateId=" + ids[0]);
					$("#searchForm").submit();
				}
			} else {
				$("#searchForm").attr("action", appName + "/report/download/" + reportNo + "ReportDownload?templateId=" + templateId);
				$("#searchForm").submit();
			}
		}
	};
	$.fn.myreport = function(viewScript, appName, reportNo, conditionData, listData) {
		var myreport = new MyReport(this, viewScript, appName, reportNo, conditionData, listData);
		return myreport.init();
	}	
})(jQuery);