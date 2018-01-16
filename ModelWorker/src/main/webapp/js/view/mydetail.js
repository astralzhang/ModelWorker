(function($){
	var MyDetail = function(ele, opt, opt2, appName, viewNo, id, data, attachment, edit) {
		//alert(appName);
		this.$element = ele;
		this.defaults1 = {

		};
		this.defaults2 = [];
		this.head = $.extend({}, this.defaults1, opt);
		this.detail = $.extend([], this.defaults2, opt2)
		this.appName = appName;
		this.viewNo = viewNo;
		this.id = id;
		//alert(data["headData"]);
		this.headData = eval("(" + data["headData"] + ")");
		//this.headData = data["headData"];
		this.edit = edit;
		this.data = data;
		this.attachment = attachment;
		this.token = data.token;
		//alert(JSON.stringify(this.head.buttons));
		this.changeData = [];
	};
	MyDetail.prototype = {
		init : function() {
			var form = $("#form");
			//alert(JSON.stringify(this.config.condition));
			var appName = this.appName;
			var headData = this.headData;
			var edit = this.edit;
			var head = this.head;
			var detail = this.detail;
			var detailData = this.data;
			var viewNo = this.viewNo;
			var changeData = this.changeData;
			//画面
			var myWindow = this;
			var createOpenWindowCondition = function(controler) {
				//取得数据
				var param = {};
				$.each(head.cells, function(n, cell) {
					//alert(condition.editor);
					switch (cell.type) {
					case "label":
						if (cell.props.field != undefined && cell.props.field != null && cell.props.field !== "") {
							param["head_" + cell.props.field] = $("#head_" + cell.props.field).html();
						}
						break;
					case "inputEditor":
						if (cell.props.hidden != undefined && cell.props.hidden != null && cell.props.hidden !== "") {
							try {
								var hidden = eval("(" + cell.props.hidden + ")");
								if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
									param["head_" + hidden.field] = $("#head_" + hidden.field).val();
								}
							} catch (ex) {
								alert(ex);
							}
						}
					case "selectEditor":
					case "dateEditor":
					case "mutilInputEditor":
						param["head_" + cell.props.field] = $("#head_" + cell.props.field).val();
						break;
					case "openEditor":
						if (cell.props.noField != undefined && cell.props.noField != null && cell.props.noField !== "") {
							param["head_" + cell.props.noField] = $("#head_" + cell.props.noField).val();
						}
						if (cell.props.nameField != undefined && cell.props.nameField != null && cell.props.nameField !== "") {
							param["head_" + cell.props.nameField] = $("#head_" + cell.props.nameField).val();
						}
						break;
					case "checkEditor":
						if ($("#head_" + cell.props.field).prop("checked")) {
							param["head_" + cell.props.field] = "Y";
						} else {
							param["head_" + cell.props.field] = "N";
						}
						break;
					default:
						param["head_" + cell.props.field] = $("#head_" + cell.props.field).val();
						break;
					}

				});
				//明细数据做成
				if (controler == null) {
					return param;
				}
				var fieldConfig = controler.config;
				if (fieldConfig == undefined || fieldConfig == null || fieldConfig === "") {
					return param;
				}
    			var objTr = controler.control;
    			if (objTr == undefined || objTr == null || objTr == "") {
    				return param;
    			}
    			$.each(fieldConfig, function(n, value) {
					//查找对应数据
					var td = $(objTr).find("td:eq(" + n + ")");
					if (td == null) {
						return false;
					}
					switch (value.editor) {
					case "label":
    					param["detail_" + value.props.field] = $(td).find("label").html();
						break;
					case "inputEditor":
						param["detail_" + value.props.field] = $(td).find("input[type='text']").val();
						if (value.props.hidden != undefined && value.props.hidden != null && value.props.hidden !== "") {
							try {
								var hidden = eval("(" + value.props.hidden + ")");
								param["detail_" + hidden.field] = $(td).find("input[type='hidden']").val();
							} catch (ex) {
								alert(ex);
							}
						}
						break;
					case "selectEditor":
						param["detail_" + value.props.field] = $(td).find("select").val();
						break;
					case "openEditor":
						if (value.props.noField != undefined && value.props.noField != null && value.props.noField !== "") {
							param["detail_" + value.props.noField] = $(td).find("input[type='hidden']").val();
						}
						if (value.props.nameField != undefined && value.props.nameField != null && value.props.nameField !== "") {
							param["detail_" + value.props.nameField] = $(td).find("input[type='text']").val();
						}
						break;
					case "checkEditor":
						if ($(td).find("input[type='checkbox']").prop("checked")) {
							param["detail_" + value.props.field] = "Y";
						} else {
							param["detail_" + value.props.field] = "N";
						}
						break;
					case "mutilInputEditor":
						param["detail_" + value.props.field] = $(td).find("textarea").val();
						break;
					case "dateEditor":
						param["detail_" + value.props.field] = $(td).find("input[type='text']").val();
						break;
					default:
						break;
					}
    			});
    			return param;
			}
			//开窗返回数据
			var openWindowResult = null;
			//开窗
			var openWindow = function(param, controler, redrawCondition) {
				MessageBox.processStart();
				//生成查询条件
				param.viewData = createOpenWindowCondition(controler);
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
	                        	$.each(eval(data.openWin.condition), function(n, condition) {
	                        		var field = condition.field;
	                        		field = field.replaceAll("\\.", "_");
	             		            if (condition.editor == "checkEditor") {
	                        			if ($("#search_openWin_" + field).prop("checked")) {
	                        				param[field] = "Y";
	                        			} else {
	                        				param[field] = "N";
	                        			}
	                        		} else {
	                        			param[field] = $("#search_openWin_" + field).val();
	                        		}
	                        	});
	                        	param.viewData = createOpenWindowCondition(controler);
	                        	//objParam.openWinNo = param.openWinNo;
	                        	//alert(JSON.stringify(objParam));
	                        	openWindow(param, controler, false);
	                        }).appendTo(searchDiv);
						}
                        //显示表头数据
                        var th = "<tr><th></th>";
                        $.each(eval(data.openWin.viewField), function(n, view) {
                        	th += "<th>" + view.label + "</th>";
                        });
                        th += "</tr>";
                        $("#myOpenWinModal table thead").html(th);
                        if (controler.multiSelect != undefined && controler.multiSelect == true) {
                        	$('<input type="checkbox">').click(function() {
                        		if ($(this).prop("checked")) {
                        			$("#myOpenWinModal table tbody input[type='checkbox']").prop("checked", true);
                        		} else {
                        			$("#myOpenWinModal table tbody input[type='checkbox']").prop("checked", false);
                        		}
                        	}).appendTo($("#myOpenWinModal table thead th:eq(0)"));
                        }
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
    						if (controler.multiSelect == undefined || controler.multiSelect == false) {
	    						if (ids.length > 1) {
	    							MessageBox.info("请仅选择一条数据！");
	    							return;
	    						}
    						}
    						var returnData = [];
    						for (var i = 0; i < ids.length; i++) {
    							var tr = $("#myOpenWinModal table tbody tr").eq(ids[i]);
    							var sData = "{";
    							$.each(eval(data.openWin.viewField), function(n, view) {
    								sData += view.field + ":\"";
    								sData += $(tr).find("td:eq(" + (n + 1) + ")").html();
    								sData += "\",";
    	                        });
    							sData = sData.substring(0, sData.length - 1);
    							sData += "}";
    							returnData.push(eval("(" + sData + ")"));
    						}
    						if (controler.confirmFunc != undefined && controler.confirmFunc != null && controler.confirmFunc !== "") {
    							controler.confirmFunc(controler.config, controler.control, controler.viewOk, returnData);
    						}	
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
			//开窗回调函数
			var headConfirmFunc = function(config, control, viewOk, data) {
				try {
					var mappingConfig = eval("(" + decodeURIComponent(viewOk) + ")");
					if (mappingConfig.proc != undefined && mappingConfig.proc != null && mappingConfig.proc !== "") {
						//需要执行存储过程，取得表头所有数据
						var paramData = new Object;
						$.each(config, function(n, cell) {
							switch (cell.type) {
							case "label":
								if (cell.props.field != undefined && cell.props.field != null && cell.props.field !== "") {
									paramData["head_" + cell.props.field] = $("#head_" + cell.props.field).html();
								}
								break;
							case "inputEditor":
								if (cell.props.hidden != undefined && cell.props.hidden != null && cell.props.hidden !== "") {
									try {
										var hidden = eval("(" + cell.props.hidden + ")");
										if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
											if (hidden.save == "Y") {
												paramData["head_" + hidden.field] = $("#head_" + hidden.field).val();
											}
										}
									} catch (ex) {
										alert(ex);
									}
								}
							case "selectEditor":
							case "dateEditor":
							case "mutilInputEditor":
								paramData["head_" + cell.props.field] = $("#head_" + cell.props.field).val();
								break;
							case "openEditor":
								paramData["head_" + cell.props.noField] = $("#head_" + cell.props.noField).val();
								paramData["head_" + cell.props.nameField] = $("#head_" + cell.props.nameField).val();
								break;
							case "checkEditor":
								if ($("#head_" + cell.props.field).prop("checked")) {
									paramData["head_" + cell.props.field] = "Y";
								} else {
									paramData["head_" + cell.props.field] = "N";
								}
								break;
							default:
								break;
							}
						});
						//取得弹出窗口数据
						$.each(data, function (n, value) {
							$.each(value, function(m, valueData) {
								if (paramData["open_" + m] == undefined || paramData["open_" + m] == null || paramData["open_" + m] === "") {
									paramData["open_" + m] = valueData;
								} else {
									paramData["open_" + m] += "," + valueData;
								}
							});
						});
						paramData.id = $("#id").val();
						var param = new Object;
						param.proc = encodeURIComponent(mappingConfig.proc);
						param.target = mappingConfig.target;
						param.data = paramData;
						//执行存储过程
						$.ajax({
							url:appName + "/view/proc",
							type:"post",
							dataType:"json",
							contentType: "application/json",
							data:JSON.stringify(param),
							success:function(procData) {
								if (procData.errType == "1") {
									MessageBox.info(data.errMessage);
									return;
								}
								var newRow = false;
								$.each(data, function(n, value) {
									$.each(mappingConfig.mapping, function(n, mapping) {
										var source = mapping.source;
										var target = mapping.target;
										var colType = "";
											//取得设定对象所在列
										$.each(config, function(m, field) {
											if (field.type == "openEditor") {
												if (target == field.props.noField) {
													colType = field.type;
													return false;
												} else if (target == field.props.nameField) {
													colType = field.type;
													return false;
												}
											} else {
												if (target == field.props.field) {
													colType = field.type;
													return false;
												}
											}
										});
										if (colType != "") {
											switch(colType) {
											case 'label':
												var editor = $("#head_" + target);
												if (mapping.sourceFlag == "head") {
													$(editor).html($("#head_" + source).val());
												} else if (mapping.sourceFlag == "open") {
													$(editor).html(value[source]);
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).html(procData.data[0][source]);
													} else {
														$(editor).html(procData.data[n][source]);
													}
												}
												break;
											case "openEditor":
											case "inputEditor":
											case "dateEditor":
											case "mutilInputEditor":
											case "selectEditor":
												var editor = $("#head_" + target);
												if (mapping.sourceFlag == "head") {
													$(editor).val($("#head_" + source).val());
												} else if (mapping.sourceFlag == "open") {
													$(editor).val(value[source]);
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).val(procData.data[0][source]);
													} else {
														$(editor).val(procData.data[n][source]);
													}
												}
												break;
											case "checkEditor":
												var editor = $("#head_" + target);
												if (mapping.sourceFlag == "head") {
													if ($("#head_" + source).val() == "Y") {
														$(editor).prop("checked", true);
													} else {
														$(editor).prop("checked", false);
													}
												} else if (mapping.sourceFlag == "open") {
													if (value[source] == "Y") {
														$(editor).prop("checked", true);
													} else {
														$(editor).prop("checked", false);
													}
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data[0][source] == "Y") {
														$(editor).prop("checked", true);
													} else {
														$(editor).prop("checked", false);
													}
												}
												break;
											default:
												break;
											}
										}									
									});
									index++;
								});
							},
							error:function(XMLHttpRequest, data) {
								MessageBox.info("系统错误，详细信息：" + data);
							}
						});
					} else {
						//不需要执行存储过程
						$.each(data, function(m, value) {
							$.each(mappingConfig.mapping, function(n, mapping) {
								var source = mapping.source;
								var target = mapping.target;
								//取得设定对象类型
								var colType = "";
								$.each(config, function(m, field) {
									if (field.type == "openEditor") {
										if (target == field.props.noField) {
											colType = field.type;
											return false;
										} else if (target == field.props.nameField) {
											colType = field.type;
											return false;
										}
									} else {
										if (target == field.props.field) {
											colType = field.type;
											return false;
										}
									}
								});
								if (colType != "") {
									switch(colType) {
									case 'label':
										var editor = $("#head_" + target);
										if (mapping.sourceFlag == "head") {
											$(editor).html($("#head_" + source).val());
										} else if (mapping.sourceFlag == "open") {
											if (m <= 0) {
												$(editor).html("");
											}
											if ($(editor).val() != "") {
												var tempData = $(editor).val() + "," + value[source];
												$(editor).html(tempData);
											} else {
												$(editor).html(value[source]);
											}
										}
										break;
									case "openEditor":
									case "inputEditor":
									case "dateEditor":
									case "mutilInputEditor":
									case "selectEditor":
										var editor = $("#head_" + target);
										if (mapping.sourceFlag == "head") {
											$(editor).val($("#head_" + source).val());
										} else if (mapping.sourceFlag == "open") {
											if (m <= 0) {
												$(editor).val("");
											}
											if ($(editor).val() != "") {
												var tempData = $(editor).val() + "," + value[source];
												$(editor).val(tempData);
											} else {
												$(editor).val(value[source]);
											}
										}
										break;
									case "checkEditor":
										var editor = $("#head_" + target);
										if (mapping.sourceFlag == "head") {
											if ($("#head_" + source).val() == "Y") {
												$(editor).prop("checked", true);
											} else {
												$(editor).prop("checked", false);
											}
										} else if (mapping.sourceFlag == "open") {
											if (value[source] == "Y") {
												$(editor).prop("checked", true);
											} else {
												$(editor).prop("checked", false);
											}
										}
										break;
									default:
										break;
									}
								} else {
									var editor = $("#head_" + target);
									if (editor.length > 0) {
										if (mapping.sourceFlag == "head") {
											$(editor).val($("#head_" + source).val());
										} else if (mapping.sourceFlag == "open") {
											if (m <= 0) {
												$(editor).val("");
											}
											if ($(editor).val() != "") {
												var tempData = $(editor).val() + "," + value[source];
												$(editor).val(tempData);
											} else {
												$(editor).val(value[source]);
											}
										}
									}
								}
							});
							index++;
						});
					}
				} catch (ex) {
					alert(ex);
				}
			};
			var get_attachment = function (view) {
				//附件数据生成
				var thead = $("#myAttachmentModal table thead");
				if (edit) {
					$(thead).html("<tr><th>文件名</th><th>文件类型</th><th>删除</th></tr>");
				} else {
					$(thead).html("<tr><th>文件名</th><th>文件类型</th></tr>");
				}
				var tbody = $("#myAttachmentModal table tbody");
				var tdata = "";
				$.each(view.attachment, function(n, atta) {
					tdata += "<tr>";
					tdata += "<td><input type='hidden' value='" + atta.id + "'><a href='#'>" + atta.fileName + "</a></td>";
					tdata += "<td>" + atta.fileExtend + "</td>";
					if (edit) {
						tdata += "<td><button type='button' class='btn'><span class='glyphicon glyphicon-remove'></span>删除</button></td>";
					}
					tdata += "</tr>";
				});
				tbody.html(tdata);
				$("#myAttachmentModal table tbody button").unbind("click");
				$("#myAttachmentModal table tbody button").bind("click", {view:view}, view.deleteFile);
				$("#myAttachmentModal table tbody a").unbind("click");
				$("#myAttachmentModal table tbody a").bind("click", {view:view}, view.viewFile);
				if ($("#myAttachmentModal table tbody tr").length > 0) {
					$("<button class='btn'><span class='glyphicon glyphicon-file'></span>附件</button>").click(function() {
						var modal = $('#myAttachmentModal').modal({backdrop: 'static', keyboard: false});
				    	modal.show();
					}).appendTo($("#attachmentShow"));
				}
			};
			//创建Head
			for (var i = 1; i <= this.head.rows; i++) {
				//$("<input type='hidden' id='id'>").appendTo("#form1");
				var tr = $("<tr style='height:50px;'></tr>").appendTo(this.$element);
				for (var j = 1; j <= this.head.cols; j++) {
					var td = null;
					var hasOut = true;
					if (this.head.margin && this.head.margin.length > 0) {
						for (var m = 0; m < this.head.margin.length; m++) {
							var data = this.head.margin[m];
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
					var fieldType = null;
					$.each(this.head.cells, function(n, value) {
						if (value.row == i && value.col == j) {
							fieldType = value.type;
							switch (fieldType) {
							case 'label':
								//alert(value.props.value);
								$(td).attr("nowrap", true);
								var showData = "标签";
								if (typeof(value.props.value) != "undefined") {
									if (value.value != "") {
										showData = value.props.value;
									}										
								}
								if (value.props.field != undefined && value.props.field != null && value.props.field !== "") {
									if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
										showData = headData[value.props.field];									
									}
								}
								var field = "";
								if (value.props.field != undefined && value.props.field != null && value.props.field != "") {
									field = value.props.field;
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
									if (field != "") {
										var editor = $("<label " + value.props.controlProp + " id='head_" + field + "'>" + showData + "</label>").appendTo(td);
									} else {
										var editor = $("<label " + value.props.controlProp + ">" + showData + "</label>").appendTo(td);
									}
								} else {
									if (field != "") {
										var editor = $("<label id='head_" + field + "'>" + showData + "</label>").appendTo(td);
									} else {
										var editor = $("<label>" + showData + "</label>").appendTo(td);
									}
								}
								return false;
							case 'inputEditor':
								var showData = "";
								if (typeof(value.props.field) != "undefined") {
									showData = value.props.field;
								}
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
									css = "form-control input-sm fl w15";
								} else {
									css = value.props.css;
								}
								var valueData = "";
								if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
									try {
										valueData = eval("(" + value.props.value + ")");
									} catch (ex) {
										alert(ex);
									}
								}
								if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
									valueData = headData[value.props.field];									
								}
								
								var readonlyType = "";
								if (!edit) {
									readonlyType = "readonly";
								}
								if (value.props.readonly == "Y") {
									readonlyType = "readonly";
								}
								//alert(JSON.stringify(value));
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
									var editor = $("<input type='text' id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " value='" + valueData + "' " + readonlyType + ">").appendTo(td);
								} else {
									var editor = $("<input type='text' id='head_" + value.props.field + "' class='" + css + "' value='" + valueData + "' " + readonlyType + ">").appendTo(td);
								}
								if (edit) {
									$(editor).unbind("change");
									$(editor).bind("change", function() {
										var row = value.row;
										var col = value.col;
										myWindow.changeData.push({type:"head",row:row,col:col});
										$(this).trigger("DataChanged");
									});
									//新增事件处理
									if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
										var eventFunc = eval("(" + value.props.eventFunc + ")");
										$.each(eventFunc, function(m, eventFunction) {
											if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
												if (eventFunction.eventNo === "change") {
													eventFunction.eventNo = "DataChanged";
												}
												if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
													$(editor).unbind(eventFunction.eventNo);
													$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
												}
											}
										});									
									}
									//新增Server事件处理
									if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
										var eventFunc = eval("(" + value.props.serverFunc + ")");
										if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
											&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
											&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
											if (eventFunc.event === "change") {
												eventFunc.event = "DataChanged";
											}
											$(editor).unbind(eventFunc.event);
											$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
										}
									}
								}
								if (value.props.hidden != undefined && value.props.hidden != null && value.props.hidden !== "") {
									try {
										var hidden = eval("(" + value.props.hidden + ")");
										if (hidden.field != undefined && hidden.field != null && hidden.field != "") {
											if (headData[hidden.field] != undefined && headData[hidden.field] != null && headData[hidden.field] !== "") {
												$("<input type='hidden' id='head_" + hidden.field + "' value='" + headData[hidden.field] + "'>").appendTo(td);
											} else {
												$("<input type='hidden' id='head_" + hidden.field + "' value=''>").appendTo(td);
											}
										}
									} catch (ex) {
										alert(ex);
									}
								}
								return false;
							case 'selectEditor':
								//下拉框处理
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
									css = "form-control2";
								} else {
									css = value.props.css;
								}
								var editor = null;
								var disableType = "";
								if (!edit) {
									disableType = "disabled";
								} else {
									if (value.props.readonly == "Y") {
										disableType = "disabled";
									}
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
									editor = $("<select " + value.props.controlProp + " id='head_" + value.props.field + "' class='" + css + "' " + disableType + "></select>").appendTo(td);
								} else {
									editor = $("<select id='head_" + value.props.field + "' class='" + css + "' " + disableType + "></select>").appendTo(td);
								}
								var objSql = null;
								try {
									var jsonData = "{}";
									if (value.props.sql != undefined && value.props.sql != null && value.props.sql !== "") {
										jsonData = value.props.sql;
									}
									objSql = eval("(" + jsonData + ")");
									if (objSql.type == "SQL") {
										$.ajax({
											url:appName + "/view/dropdown",
											type:"post",
											dataType:"json",
											data:{
												sql:decodeURIComponent(objSql.data),
												target:objSql.target
											},
											success:function(data) {
												if (data.errType == "1") {
													MessageBox.info(data.errMessage);
													return;
												}
												var options = "";
												var valueData = "";
												if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
													try {
														valueData = eval("(" + value.props.value + ")");
													} catch (ex) {
														alert(ex);
													}
												}
												$.each(data.data, function(n, dropData) {
													options += "<option value='" + dropData.no + "'>" + dropData.name + "</option>";
													if (valueData == "") {
														if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
															return true;
														}
														valueData = dropData.no;
													}
												});
												$(editor).html(options);										
												if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
													valueData = headData[value.props.field];
												}
												$(editor).val(valueData);
												$(editor).attr("BeforeData", valueData);
												if (edit) {
													$(editor).unbind("change");
													$(editor).bind("change", function() {
														var row = value.row;
														var col = value.col;
														myWindow.changeData.push({type:"head",row:row,col:col});
														$(this).trigger("DataChanged");
													});
													//新增事件处理
													if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
														var eventFunc = eval("(" + value.props.eventFunc + ")");
														$.each(eventFunc, function(m, eventFunction) {
															if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
																if (eventFunction.eventNo === "change") {
																	eventFunction.eventNo = "DataChanged";
																}
																$(editor).unbind(eventFunction.eventNo);
																if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
																	$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
																}
															}
														});									
													}
													//新增Server事件处理
													if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
														var eventFunc = eval("(" + value.props.serverFunc + ")");
														if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
															&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
															&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
															if (eventFunc.event === "change") {
																eventFunc.event = "DataChanged";
															}
															$(editor).unbind(eventFunc.event);
															$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
														}
													}
												}
											},
											error:function(XMLHttpRequest, data) {
												MessageBox.processEnd("系统错误，详细信息：" + data);
											}
										});
									} else {
										var options = "";
										var valueData = "";
										if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
											try {
												valueData = eval("(" + value.props.value + ")");
											} catch (ex) {
												alert(ex);
											}
										}
										if (objSql.data != undefined && objSql.data != null && objSql.data != "") {
											$.each(objSql.data, function(m, textData) {
												options += "<option value='" + textData.no + "'>" + textData.name + "</option>";
												if (valueData == "") {
													if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
														return true;
													}
													valueData = textData.no;
												}
											});
										}
										$(editor).html(options);										
										if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
											valueData = headData[value.props.field];
										}
										$(editor).val(valueData);
										if (edit) {
											$(editor).unbind("change");
											$(editor).bind("change", function() {
												var row = value.row;
												var col = value.col;
												myWindow.changeData.push({type:"head",row:row,col:col});
												$(this).trigger("DataChanged");
											});
											//新增事件处理
											if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
												var eventFunc = eval("(" + value.props.eventFunc + ")");
												$.each(eventFunc, function(m, eventFunction) {
													if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
														if (eventFunction.eventNo === "change") {
															eventFunction.eventNo = "DataChanged";
														}
														$(editor).unbind(eventFunction.eventNo);
														if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
															$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
														}
													}
												});									
											}
											//新增Server事件处理
											if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
												var eventFunc = eval("(" + value.props.serverFunc + ")");
												if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
													&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
													&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
													if (eventFunc.event === "change") {
														eventFunc.event = "DataChanged";
													}
													$(editor).unbind(eventFunc.event);
													$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
												}
											}
										}
									}
								} catch(ex) {
									alert(ex);
								}
								
								return false;
							case 'openEditor':
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
									css = "form-control input-sm fl w15";
								} else {
									css = value.props.css;
								}
								//选择框
								var valueData1 = "";
								if (headData[value.props.noField] != undefined && headData[value.props.noField] != null && headData[value.props.noField] !== "") {
									valueData1 = headData[value.props.noField];
								}
								var valueData2 = "";
								if (headData[value.props.nameField] != undefined && headData[value.props.nameField] != null && headData[value.props.nameField] !== "") {
									valueData2 = headData[value.props.nameField];
								}
								var objDiv = $("<div></div>").appendTo(td);
								var objFieldName = null;
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp != "") {
									objFieldName = $('<input id="head_' + value.props.nameField + '" type="text" class="' + css + '" ' + value.props.controlProp + ' value="' + valueData2 + '" readonly>').appendTo(objDiv);
								} else {
									objFieldName = $('<input id="head_' + value.props.nameField + '" type="text" class="' + css + '" value="' + valueData2 + '" readonly>').appendTo(objDiv);
								}
								$(objFieldName).unbind("change");
								$(objFieldName).bind("change", function() {
									var row = value.row;
									var col = value.col;
									myWindow.changeData.push({type:"head",row:row,col:col});
									$(this).trigger("DataChanged");
								});
								var objFieldId = null;
								if (value.props.noField != undefined && value.props.noField != null && value.props.noField !== "") {
									objFieldId = $('<input type="hidden" id="head_' + value.props.noField + '" value="' + valueData1 + '">').appendTo(objDiv);
									$(objFieldId).unbind("change");
									$(objFieldId).bind("change", function() {
										var row = value.row;
										var col = value.col;
										myWindow.changeData.push({type:"head",row:row,col:col});
										$(this).trigger("DataChanged");
									});
								}
								var disableType = "";
								if (!edit) {
									disableType = "disabled";
								} else {
									if (value.props.readonly == "Y") {
										disableType = "disabled";
									}
								}
								var w = $(objFieldName).width();
								if (w <= 0) {
									w = 0;
								}
								$(objDiv).css("float", "left");
								$(objDiv).css("width", w + 30);
								$(objFieldName).css("float", "left");
								$(objFieldName).css("width", w);
								var btnOpen = $('<input type="button" value="..." ' + disableType + ' style="width:30px;">').click(function() {
									//开窗处理
									var viewOk = "";
									if (value.props.viewOk != undefined && value.props.viewOk != null && value.props.viewOk != "") {
										viewOk = value.props.viewOk;
									}
									var mutilSelect = false;
									if (value.props.mutilSelect != undefined && value.props.mutilSelect != null && value.props.mutilSelect == "Y") {
										mutilSelect = true;
									}
									openWindow({openWinNo:value.props.openWinNo}, {config:head.cells, control:$(this), viewOk:viewOk, multiSelect:mutilSelect,confirmFunc:headConfirmFunc}, true);
								}).appendTo(objDiv);
								return false;
							case 'checkEditor':
								//checkBox
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
									css = "";
								} else {
									css = value.props.css;
								}
								var editor = null;
								var readonlyType = "";
								if (!edit) {
									readonlyType = "disabled";
								} else {
									if (value.props.readonly == "Y") {
										readonlyType = "disabled";
									}
								}
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
									editor = $("<input type='checkbox' id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " " + readonlyType + ">").appendTo(td);
								} else {
									editor = $("<input type='checkbox' id='head_" + value.props.field + "' class='" + css + "' " + readonlyType + ">").appendTo(td);
								}
								var valueData = "";
								if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
									try {
										valueData = eval("(" + value.props.value + ")");
									} catch (ex) {
										alert(ex);
									}
								}
								if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
									valueData = headData[value.props.field];
								}
								if (valueData == "Y") {
									$(editor).prop("checked", true);
								} else {
									$(editor).prop("checked", false);
								}
								if (edit) {
									$(editor).unbind("click");
									$(editor).bind("click", function() {
										var row = value.row;
										var col = value.col;
										myWindow.changeData.push({type:"head",row:row,col:col});
										$(this).trigger("DataChanged");
									});
									//新增事件处理
									if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
										var eventFunc = eval("(" + value.props.eventFunc + ")");
										$.each(eventFunc, function(m, eventFunction) {
											if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
												if (eventFunction.eventNo === "click") {
													eventFunction.eventNo = "DataChanged";
												}
												$(editor).unbind(eventFunction.eventNo);
												if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
													$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
												}
											}
										});									
									}
									//新增Server事件处理
									if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
										var eventFunc = eval("(" + value.props.serverFunc + ")");
										if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
											&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
											&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
											if (eventFunc.event === "click") {
												eventFunc.event = "DataChanged";
											}
											$(editor).unbind(eventFunc.event);
											$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
										}
									}
								}
								return false;
							case 'mutilInputEditor':
								//多行输入框
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
									css = "form-control input-sm fl";
								} else {
									css = value.props.css;
								}
								var valueData = "";
								if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
									try {
										valueData = eval("(" + value.props.value + ")");
									} catch (ex) {
										alert(ex);
									}
								}
								if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
									valueData = headData[value.props.field];
								}
								var readonlyType = "";
								if (!edit) {
									readonlyType = "readonly";
								} else {
									if (value.props.readonly == "Y") {
										readonlyType = "readonly";
									}
								}
								var editor = null;
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
									editor = $("<textarea id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " " + readonlyType + ">" + valueData + "</textarea>").appendTo(td);
								} else {
									editor = $("<textarea id='head_" + value.props.field + "' class='" + css + "' " + readonlyType + ">" + valueData + "</textarea>").appendTo(td);
								}
								if (edit) {
									$(editor).unbind("change");
									$(editor).bind("change", function() {
										var row = value.row;
										var col = value.col;
										myWindow.changeData.push({type:"head",row:row,col:col});
										$(this).trigger("DataChanged");
									});
									//新增事件处理
									if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
										var eventFunc = eval("(" + value.props.eventFunc + ")");
										$.each(eventFunc, function(m, eventFunction) {
											if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
												if (eventFunction.eventNo === "change") {
													eventFunction.eventNo = "DataChanged";
												}
												$(editor).unbind(eventFunction.eventNo);
												if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
													$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
												}
											}
										});									
									}
									//新增Server事件处理
									if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
										var eventFunc = eval("(" + value.props.serverFunc + ")");
										if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
											&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
											&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
											if (eventFunc.event === "change") {
												eventFunc.event = "DataChanged";
											}
											$(editor).unbind(eventFunc.event);
											$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
										}
									}
								}
								return false;
							case 'dateEditor':
								//日期控件
								var css = "";
								if (edit) {
									if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
										css = "form-control input-sm";
									} else {
										css = value.props.css;
									}
									var objDiv = $('<div class="input-group date fl w15"></div>').appendTo(td);
									var valueData = "";
									if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
										try {
											valueData = eval("(" + value.props.value + ")");
										} catch (ex) {
											alert(ex);
										}
									}
									if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
										valueData = headData[value.props.field];
									}
									var editor = null;
									var disableType = ""
									if (value.props.readonly == "Y") {
										disableType = "disabled";
									}
									if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
										editor = $('<input type="text" class="' + css + '" id="head_' + value.props.field + '" value="" placeholder="请选择日期" readonly ' + value.props.controlProp + ' value="' + valueData + '" ' + disableType + '>').appendTo(objDiv);
									} else {
										editor = $('<input type="text" class="' + css + '" id="head_' + value.props.field + '" value="" placeholder="请选择日期" readonly value="' + valueData + '" ' + disableType + '>').appendTo(objDiv);
									}
									$(editor).unbind("change");
									$(editor).bind("change", function() {
										var row = value.row;
										var col = value.col;
										myWindow.changeData.push({type:"head",row:row,col:col});
										$(this).trigger("DataChanged");
									});
									//新增事件处理
									if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
										var eventFunc = eval("(" + value.props.eventFunc + ")");
										$.each(eventFunc, function(m, eventFunction) {
											if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
												if (eventFunction.eventNo === "change") {
													eventFunction.eventNo = "DataChanged";
												}
												$(editor).unbind(eventFunction.eventNo);
												if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
													$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
												}
											}
										});									
									}
									//新增Server事件处理
									if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
										var eventFunc = eval("(" + value.props.serverFunc + ")");
										if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
											&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
											&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql !== "") {
											if (eventFunc.event === "change") {
												eventFunc.event = "DataChanged";
											}
											$(editor).unbind(eventFunc.event);
											$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
										}
									}
									$(editor).val(valueData);
				                    var objSpan = $('<span class="input-group-addon"></span>').appendTo(objDiv);
				                    $('<i class="fa fa-calendar"></i>').appendTo(objSpan);
								} else {
									var showData = "";
									if (typeof(value.props.field) != "undefined") {
										showData = value.props.field;
									}
									var css = "";
									if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
										css = "form-control input-sm fl w15";
									} else {
										css = value.props.css;
									}
									var valueData = "";
									if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
										try {
											valueData = eval("(" + value.props.value + ")");
										} catch (ex) {
											alert(ex);
										}
									}
									if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
										valueData = headData[value.props.field];
									}
									if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
										var editor = $("<input type='text' id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " value='" + valueData + "' readonly>").appendTo(td);
									} else {
										var editor = $("<input type='text' id='head_" + value.props.field + "' class='" + css + "' value='" + valueData + "' readonly>").appendTo(td);
									}
								}
			                    return false;
							case "image":
								//图像
								var css = "";
								if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
									css = "";
								} else {
									css = value.props.css;
								}
								var objImg = null;
								if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
									if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
										objImg = $('<img class="' + css + '" id="head_' + value.props.field + '"  ' + value.props.controlProp + ' src="' + headData[value.props.field] + '">').appendTo(td);
									} else {
										var valueData = "";
										if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
											try {
												valueData = eval("(" + value.props.value + ")");
											} catch (ex) {
												alert(ex);
											}
										}
										objImg = $('<img class="' + css + '" id="head_' + value.props.field + '"  ' + value.props.controlProp + ' src="' + valueData + '">').appendTo(td);
									}
								} else {
									if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
										objImg = $('<img class="' + css + '" id="head_' + value.props.field + '" src="' + headData[value.props.field] + '">').appendTo(td);
									} else {
										var valueData = "";
										if (value.props.value != undefined && value.props.value != null && value.props.value !== "") {
											try {
												valueData = eval("(" + value.props.value + ")");
											} catch (ex) {
												alert(ex);
											}
										}
										objImg = $('<img class="' + css + '" id="head_' + value.props.field + '" src="' + valueData + '">').appendTo(td);
									}									
								}
								if (edit) {
									//新增事件处理
									if (value.props.eventFunc != undefined && value.props.eventFunc != null && value.props.eventFunc !== "") {
										var eventFunc = eval("(" + value.props.eventFunc + ")");
										$.each(eventFunc, function(m, eventFunction) {
											if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
												$(objImg).unbind(eventFunction.eventNo);
												if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
													$(objImg).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
												}
											}
										});									
									}
									//新增Server事件处理
									if (value.props.serverFunc != undefined && value.props.serverFunc != null && value.props.serverFunc !== "") {
										var eventFunc = eval("(" + value.props.serverFunc + ")");
										if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
											&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
											&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
											$(editor).unbind(eventFunc.event);
											$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo}, myWindow.runSql);
										}
									}
								}
								return false;
							default:
								return false;
							}
						}
					});
				}
			}
			if (headData.Status == undefined || headData.Status == null || headData.Status === "") {
				if (headData.status == undefined || headData.status == null || headData.status === "") {
					$("#dataStatus").html("");
				} else {
					switch (headData.status) {
					case "0":
						$("#dataStatus").html("保存");
						var cancelSubmit = $("#btn_cancel_submit");
						if (cancelSubmit[0]) {
							$("#btn_cancel_submit").remove();
						}
						break;
					case "1":
						$("#dataStatus").html("提交");
						break;
					case "2":
						$("#dataStatus").html("审核");
						break;
					default:
						$("#dataStatus").html("");
						break;
					}
				}
			} else {
				switch (headData.Status) {
				case "0":
					$("#dataStatus").html("保存");
					var cancelSubmit = $("#btn_cancel_submit");
					if (cancelSubmit[0]) {
						$("#btn_cancel_submit").remove();
					}
					break;
				case "1":
					$("#dataStatus").html("提交");
					break;
				case "2":
					$("#dataStatus").html("审核");
					break;
				default:
					if (headData.status == undefined || headData.status == null || headData.status === "") {
						$("#dataStatus").html("");
					} else {
						switch (headData.status) {
						case "0":
							$("#dataStatus").html("保存");
							var cancelSubmit = $("#btn_cancel_submit");
							if (cancelSubmit[0]) {
								$("#btn_cancel_submit").remove();
							}
							break;
						case "1":
							$("#dataStatus").html("提交");
							break;
						case "2":
							$("#dataStatus").html("审核");
							break;
						default:
							$("#dataStatus").html("");
							break;
						}
					}
					break;
				}
			}
			//生成明细
			if (this.detail.length <= 0) {
				//生成附件
				get_attachment(this);
				$("#detail").remove();
				//新增
				$("#btn_add").unbind("click");
				$("#btn_add").bind("click", {view:this}, this.add_data);
				//设定保存按钮、返回按钮处理
				$("#btn_save").unbind("click");
				$("#btn_save").bind("click", {view:this}, this.save_data);
				//提交按钮
				$("#btn_submit").unbind("click");
				$("#btn_submit").bind("click", {view:this}, this.submit_data);
				//撤销提交按钮
				$("#btn_cancel_submit").unbind("click");
				$("#btn_cancel_submit").bind("click", {view:this}, this.cancelSubmit_data);
				//查审按钮
				$("#btn_view_audit").unbind("click");
				$("#btn_view_audit").bind("click", {view:this}, this.viewAudit);
				//审核按钮
				$("#btn_audit").unbind("click");
				$("#btn_audit").bind("click", {view:this}, this.audit_data);
				//撤销审核按钮
				$("#btn_cancel_audit").unbind("click");
				$("#btn_cancel_audit").bind("click", {view:this}, this.cancelAudit_data);
				//附件按钮
				$("#btn_file").unbind("click");
				$("#btn_file").bind("click", {view:this}, this.upload_file);
				//返回
				$("#btn-back").unbind("click");
				$("#btn-back").bind("click", {view:this}, this.back_list);
				//创建自定义按钮
				var createButton = function(view) {
					if (head.buttons == undefined || head.buttons == null) {
						return;
					}
					$.each(head.buttons, function(n, value) {
						var buttonId = value.button;
						var position = value.position;
						var div = $("#" + buttonId).closest("div");
						var divText = "<div class='search'><button class='btn_header' id='" + value.id + "' type='button'>" + value.name + "</button></div>";
						if (position == "bak") {
							$(div).after(divText);
						} else {
							$(div).before(divText);
						}
						//设定自定义按钮事件
						$("#" + value.id).unbind("click");
						$("#" + value.id).bind("click", {view:view,button:value}, view.button_click);
					});
				};
				createButton(this);
				return this;
			}
			//alert(JSON.stringify(this.detail));
			var objUl = $("<ul class='tab'></ul>").appendTo($("#detail"));
			$.each(this.detail, function(n, tab) {
				$("<li>" + tab.label + "</li>").appendTo(objUl);
			});
			$(objUl).find("li:eq(0)").attr("class", "cur");
			//新增明细行
			var addRow = function(tr, fieldConfig, data) {
				$.each(fieldConfig, function(n, field) {
					//alert(JSON.stringify(field));
					switch (field.editor) {
					case 'label':
						var showData = "";
						if (typeof(field.props.value) != "undefined") {
							showData = field.props.value;										
						}
						if (data != null) {
							showData = data[field.props.field];
						}
						var td = $("<td></td>").appendTo($(tr));
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							var editor = $("<label " + field.props.controlProp + ">" + showData + "</label>").appendTo(td);
						} else {
							var editor = $("<label>" + showData + "</label>").appendTo(td);
						}
						break;
					case 'inputEditor':
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
							css = "form-control input-sm fl w15";
						} else {
							css = value.props.css;
						}
						var valueData = "";
						if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
							try {
								valueData = eval("(" + field.props.value + ")");
							} catch (ex) {
								alert(ex);
							}
						}
						if (data != null) {
							if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
								valueData = data[field.props.field];
							}
						}
						var readonlyType = "";
						if (!edit) {
							readonlyType = "readonly";
						} else {
							if (field.props.readonly == "Y") {
								readonlyType = "readonly";
							}
						}
						var td = $("<td></td>").appendTo($(tr));
						var editor = null;
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							editor = $("<input type='text' class='" + css + "' " + field.props.controlProp + " value='" + valueData + "' " + readonlyType + ">").appendTo(td);
						} else {
							editor = $("<input type='text' class='" + css + "' value='" + valueData + "' " + readonlyType + ">").appendTo(td);
						}
						if (edit) {
							$(editor).unbind("change");
							$(editor).bind("change", function() {
								var row = (tr).index();
								var col = n;
								myWindow.changeData.push({type:"detail",row:row,col:col});
								$(this).trigger("DataChanged");
							});
							//新增事件处理
							if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
								var eventFunc = eval("(" + field.props.eventFunc + ")");
								$.each(eventFunc, function(m, eventFunction) {
									if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
										if (eventFunction.eventNo === "change") {
											eventFunction.eventNo = "DataChanged";
										}
										$(editor).unbind(eventFunction.eventNo);
										if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
											$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
										}
									}
								});									
							}
							//新增Server事件处理
							if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
								var eventFunc = eval("(" + field.props.serverFunc + ")");
								if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
									&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
									&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
									var rowNo = $(editor).closest("tr").index();
									if (eventFunc.event === "change") {
										eventFunc.event = "DataChanged";
									}
									$(editor).unbind(eventFunc.event);
									$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
								}
							}
						}
						if (field.props.hidden != undefined && field.props.hidden != null && field.props.hidden !== "") {
							try {
								var hidden = eval("(" + field.props.hidden + ")");
								if (data != null) {
									if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
										if (data[hidden.field] != undefined && data[hidden.field] != null && data[hidden.field] !== "") {
											$("<input type='hidden' value='" + data[hidden.field] + "'>").appendTo(td);
										} else {
											$("<input type='hidden' value=''>").appendTo(td);
										}
									}
								} else {
									$("<input type='hidden' value=''>").appendTo(td);
								}
							} catch (ex) {
								alert(ex);
							}
						}
						break;
					case 'selectEditor':
						//下拉框处理
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
							css = "form-control2";
						} else {
							css = value.props.css;
						}
						var editor = null;
						var disableType = "";
						if (!edit) {
							disableType = "disabled";
						} else {
							if (field.props.readonly == "Y") {
								disableType = "disabled";
							}
						}
						var td = $("<td></td>").appendTo($(tr));
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							editor = $("<select class='" + css + "' " + field.props.controlProp + " " + disableType + "></select>").appendTo(td);
						} else {
							editor = $("<select class='" + css + "' " + disableType + "></select>").appendTo(td);
						}
						var objSql = null;
						try {
							var jsonData = "{}";
							if (field.props.sql != undefined && field.props.sql != null && field.props.sql !== "") {
								jsonData = field.props.sql;
							}
							objSql = eval("(" + jsonData + ")");
							if (objSql.type == "SQL") {
								$.ajax({
									url:appName + "/view/dropdown",
									type:"post",
									dataType:"json",
									data:{
										sql:decodeURIComponent(objSql.data),
										target:objSql.target
									},
									success:function(dataSelect) {
										if (dataSelect.errType == "1") {
											MessageBox.info(dataSelect.errMessage);
											return;
										}
										var options = "";
										var valueData = "";
										if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
											try {
												valueData = eval("(" + field.props.value + ")");
											} catch (ex) {
												alert(ex);
											}
										}
										//alert($(editor).val());
										$.each(dataSelect.data, function(n, value) {
											if (valueData === "") {
												if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
													options += "<option value='" + value.no + "'>" + value.name + "</option>";
													return true;
												}
												valueData = value.no;
											}
											if (valueData == value.no) {
												options += "<option value='" + value.no + "' selected>" + value.name + "</option>";
											} else {
												options += "<option value='" + value.no + "'>" + value.name + "</option>";
											}
										});
										$(editor).html(options);
										$(editor).trigger("SetValue");
										if (data != null) {
											if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
												valueData = data[field.props.field];
											}
											$(editor).val(valueData);
										}
										if (edit) {
											$(editor).unbind("change");
											$(editor).bind("change", function() {
												var row = (tr).index();
												var col = n;
												myWindow.changeData.push({type:"detail",row:row,col:col});
												$(this).trigger("DataChanged");
											});
											//新增事件处理
											if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
												var eventFunc = eval("(" + field.props.eventFunc + ")");
												$.each(eventFunc, function(m, eventFunction) {
													if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
														if (eventFunction.eventNo === "change") {
															eventFunction.eventNo = "DataChanged";
														}
														$(editor).unbind(eventFunction.eventNo);
														if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
															$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
														}
													}
												});									
											}
											//新增Server事件处理
											if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
												var eventFunc = eval("(" + field.props.serverFunc + ")");
												if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event != ""
													&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
													&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
													var rowNo = $(editor).closest("tr").index();
													if (eventFunc.event === "change") {
														eventFunc.event = "DataChanged";
													}
													$(editor).unbind(eventFunc.event);
													$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
												}
											}
										}
									},
									error:function(XMLHttpRequest, data) {
										MessageBox.info("系统错误，详细信息：" + data);
									}
								});
							} else {
								var options = "";
								var valueData = "";
								if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
									try {
										valueData = eval("(" + field.props.value + ")");
									} catch (ex) {
										alert(ex);
									}
								}
								if (objSql.data != undefined && objSql.data != null && objSql.data != "") {
									$.each(objSql.data, function(m, textData) {
										options += "<option value='" + textData.no + "'>" + textData.name + "</option>";
										if (valueData == "") {
											if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
												return true;
											}
											valueData = textData.no;
										}
									});
								}
								$(editor).html(options);
								if (data != null) {
									if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
										valueData = data[field.props.field];
									}
								}
								$(editor).val(valueData);
								if (edit) {
									$(editor).unbind("change");
									$(editor).bind("change", function() {
										var row = (tr).index();
										var col = n;
										myWindow.changeData.push({type:"detail",row:row,col:col});
										$(this).trigger("DataChanged");
									});
									//新增事件处理
									if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
										var eventFunc = eval("(" + field.props.eventFunc + ")");
										$.each(eventFunc, function(m, eventFunction) {
											if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
												if (eventFunction.eventNo === "change") {
													eventFunction.eventNo = "DataChanged";
												}
												$(editor).unbind(eventFunction.eventNo);
												if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
													$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
												}
											}
										});									
									}
									//新增Server事件处理
									if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
										var eventFunc = eval("(" + field.props.serverFunc + ")");
										if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
											&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
											&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
											var rowNo = $(editor).closest("tr").index();
											if (eventFunc.event === "change") {
												eventFunc.event = "DataChanged";
											}
											$(editor).unbind(eventFunc.event);
											$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
										}
									}
								}
							}
						} catch (ex) {
							alert(ex);
						}
						break;
					case 'openEditor':
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
							css = "form-control input-sm fl w15";
						} else {
							css = field.props.css;
						}
						//选择框
						var valueData1 = "";
						if (data != null) {
							if (data[field.props.noField] != undefined && data[field.props.noField] != null && data[field.props.noField] !== "") {
								valueData1 = data[field.props.noField];
							}
						}
						var valueData2 = "";
						if (data != null) {
							if (data[field.props.nameField] != undefined && data[field.props.nameField] != null && data[field.props.nameField] !== "") {
								valueData2 = data[field.props.nameField];
							}
						}
						var td = $("<td nowrap></td>").appendTo($(tr));
						var objFieldName = null;
						var objDiv = $("<div></div>").appendTo(td);
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							objFieldName = $('<input type="text" class="' + css + '" ' + field.props.controlProp + ' value="' + valueData2 + '" readonly>').appendTo(objDiv);
						} else {
							objFieldName = $('<input type="text" class="' + css + '" value="' + valueData2 + '" readonly>').appendTo(objDiv);
						}
						var disableType = "";
						if (!edit) {
							disableType = "disabled";
						}
						var w = $(objFieldName).width();
						if (w <= 0) {
							w = 0;
						}
						$(objDiv).css("float", "left");
						$(objDiv).css("width", w + 30);
						$(objFieldName).css("float", "left");
						$(objFieldName).css("width", w);
						$('<input type="button" value="..." ' + disableType + ' style="width:30px;">').click(function() {
							//开窗处理							
							var viewOk = "";
							if (field.props.viewOk != undefined && field.props.viewOk != null && field.props.viewOk != "") {
								viewOk = field.props.viewOk;
							}
							var objTr = $(this).closest("tr");
							openWindow({openWinNo:field.props.openWinNo}, {config:fieldConfig, control:objTr, viewOk:viewOk, multiSelect:true,confirmFunc:detailConfirmFunc}, true);
						}).appendTo(objDiv);
						if (edit) {
							$(objFieldName).unbind("change");
							$(objFieldName).bind("change", function() {
								var row = (tr).index();
								var col = n;
								myWindow.changeData.push({type:"detail",row:row,col:col});
								$(this).trigger("DataChanged");
							});
							//新增事件处理
							//alert(field.props.eventFunc);
							if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
								var eventFunc = eval("(" + field.props.eventFunc + ")");
								$.each(eventFunc, function(m, eventFunction) {
									if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
										if (eventFunction.eventNo === "change") {
											eventFunction.eventNo = "DataChanged";
										}
										$(objFieldName).unbind(eventFunction.eventNo);
										if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
											try {
												$(objFieldName).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
											} catch (er) {
												alert(er);
											}
										}
									}
								});									
							}
							//新增Server事件处理
							if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
								var eventFunc = eval("(" + field.props.serverFunc + ")");
								if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
									&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
									&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
									var rowNo = $(editor).closest("tr").index();
									if (eventFunc.event === "change") {
										eventFunc.event = "DataChanged";
									}
									$(editor).unbind(eventFunc.event);
									$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
								}
							}
						}
						break;
					case 'checkEditor':
						//checkBox
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
							css = "";
						} else {
							css = field.props.css;
						}
						var editor = null;
						var readonlyType = "";
						if (!edit) {
							readonlyType = "disabled";
						} else {
							if (field.props.readonly == "Y") {
								readonlyType = "disabled";
							}
						}
						var td = $("<td></td>").appendTo($(tr));
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							editor = $("<input type='checkbox' class='" + css + "' " + field.props.controlProp + " " + readonlyType + ">").appendTo(td);
						} else {
							editor = $("<input type='checkbox' class='" + css + "' " + readonlyType + ">").appendTo(td);
						}
						var valueData = "";
						if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
							try {
								valueData = eval("(" + field.props.value + ")");
							} catch (ex) {
								alert(ex);
							}
						}
						if (data != null) {
							if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
								valueData = data[field.props.field];
							}
						}
						if (valueData == "Y") {
							$(editor).prop("checked", true);
						} else {
							$(editor).prop("checked", false);
						}
						if (edit) {
							$(editor).unbind("change");
							$(editor).bind("change", function() {
								var row = (tr).index();
								var col = n;
								myWindow.changeData.push({type:"detail",row:row,col:col});
								$(this).trigger("DataChanged");
							});
							//新增事件处理
							if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
								var eventFunc = eval("(" + field.props.eventFunc + ")");
								$.each(eventFunc, function(m, eventFunction) {
									if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
										if (eventFunction.eventNo === "change") {
											eventFunction.eventNo = "DataChanged";
										}
										$(editor).unbind(eventFunction.eventNo);
										if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
											$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
										}
									}
								});
							}
							//新增Server事件处理
							if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
								var eventFunc = eval("(" + field.props.serverFunc + ")");
								if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
									&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
									&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
									var rowNo = $(editor).closest("tr").index();
									if (eventFunc.event === "change") {
										eventFunc.event = "DataChanged";
									}
									$(editor).unbind(eventFunc.event);
									$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
								}
							}
						}
						break;
					case 'mutilInputEditor':
						//多行输入框
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
							css = "form-control input-sm fl";
						} else {
							css = field.props.css;
						}
						var valueData = "";
						if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
							try {
								valueData = eval("(" + field.props.value + ")");
							} catch (ex) {
								alert(ex);
							}
						}
						if (data != null) {
							if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
								valueData = data[field.props.field];
							}
						}
						var readonlyType = "";
						if (!edit) {
							readonlyType = "readonly";
						} else {
							if (field.props.readonly == "Y") {
								readonlyType = "readonly";
							}
						}
						var td = $("<td></td>").appendTo($(tr));
						var editor = null;
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							editor = $("<textarea class='" + css + "' " + field.props.controlProp + " " + readonlyType + ">" + valueData + "</textarea>").appendTo(td);
						} else {
							editor = $("<textarea class='" + css + "' " + readonlyType + ">" + valueData + "</textarea>").appendTo(td);
						}
						if (edit) {
							//新增事件处理
							$(editor).unbind("change");
							$(editor).bind("change", function() {
								var row = (tr).index();
								var col = n;
								myWindow.changeData.push({type:"detail",row:row,col:col});
								$(this).trigger("DataChanged");
							});
							if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
								var eventFunc = eval("(" + field.props.eventFunc + ")");
								$.each(eventFunc, function(m, eventFunction) {
									if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
										if (eventFunction.eventNo === "change") {
											eventFunction.eventNo = "DataChanged";
										}
										$(editor).unbind(eventFunction.eventNo);
										if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
											$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
										}
									}
								});									
							}
							//新增Server事件处理
							if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
								var eventFunc = eval("(" + field.props.serverFunc + ")");
								if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
									&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
									&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
									var rowNo = $(editor).closest("tr").index();
									if (eventFunc.event === "change") {
										eventFunc.event = "DataChanged";
									}
									$(editor).unbind(eventFunc.event);
									$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
								}
							}
						}
						return false;
					case 'dateEditor':
						//日期控件
						var css = "";
						if (edit) {
							if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
								css = "form-control input-sm";
							} else {
								css = field.props.css;
							}
							var td = $("<td></td>").appendTo($(tr));
							var objDiv = $('<div class="input-group date fl w15"></div>').appendTo(td);
							var valueData = "";
							if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
								try {
									valueData = eval("(" + field.props.value + ")");
								} catch (ex) {
									alert(ex);
								}
							}
							if (data != null) {
								if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
									valueData = data[field.props.field];
								}
							}
							var editor = null;
							var readonlyType = "";
							if (field.props.readonly == "Y") {
								readonlyType = "disabled";
							}
							if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
								//editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly ' + field.props.controlProp + ' value="' + valueData + '" ' + readonlyType + '>').appendTo(objDiv);
								editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly ' + field.props.controlProp + ' ' + readonlyType + '>').appendTo(objDiv);
							} else {
								//editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly value="' + valueData + '" ' + readonlyType + '>').appendTo(objDiv);
								editor = $('<input type="text" class="' + css + '" value="" placeholder="请选择日期" readonly ' + readonlyType + '>').appendTo(objDiv);
							}
							$(editor).datepicker({
								 format: 'yyyymmdd',
								 defaultDate:new Date(valueData)
							});
							$(editor).datepicker(valueData);
							//新增事件处理
							$(editor).unbind("change");
							$(editor).bind("change", function() {
								var row = (tr).index();
								var col = n;
								myWindow.changeData.push({type:"detail",row:row,col:col});
								$(this).trigger("DataChanged");
							});
							if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
								var eventFunc = eval("(" + field.props.eventFunc + ")");
								$.each(eventFunc, function(m, eventFunction) {
									if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
										if (eventFunction.eventNo === "change") {
											eventFunction.eventNo = "DataChanged";
										}
										$(editor).unbind(eventFunction.eventNo);
										if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
											$(editor).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
										}
									}
								});									
							}
							//新增Server事件处理
							if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
								var eventFunc = eval("(" + field.props.serverFunc + ")");
								if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
									&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
									&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
									var rowNo = $(editor).closest("tr").index();
									if (eventFunc.event === "change") {
										eventFunc.event = "DataChanged";
									}
									$(editor).unbind(eventFunc.event);
									$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
								}
							}
							$(editor).val(valueData);
		                    var objSpan = $('<span class="input-group-addon"></span>').appendTo(objDiv);
		                    $('<i class="fa fa-calendar"></i>').appendTo(objSpan);
						} else {
							var css = "";
							if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
								css = "form-control input-sm fl w15";
							} else {
								css = field.props.css;
							}
							var valueData = "";
							if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
								try {
									valueData = eval("(" + field.props.value + ")");
								} catch (ex) {
									alert(ex);
								}
							}
							if (data != null) {
								if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
									valueData = data[field.props.field];
								}
							}
							var td = $("<td></td>").appendTo($(tr));
							if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
								var editor = $("<input type='text' class='" + css + "' " + field.props.controlProp + " value='" + valueData + "' disabled>").appendTo(td);
							} else {
								var editor = $("<input type='text' class='" + css + "' value='" + valueData + "' disabled>").appendTo(td);
							}
						}
	                    break;
					case "image":
						//图像
						var css = "";
						if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
							css = "";
						} else {
							css = field.props.css;
						}
						var objImg = null;
						var td = $("<td></td>").appendTo($(tr));
						if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
							if (headData[field.props.field] != undefined && headData[field.props.field] != null && headData[field.props.field] !== "") {
								objImg = $('<img class="' + css + '" ' + field.props.controlProp + ' src="' + headData[field.props.field] + '">').appendTo(td);
							} else {
								var valueData = "";
								if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
									try {
										valueData = eval("(" + field.props.value + ")");
									} catch (ex) {
										alert(ex);
									}
								}
								if (data != null) {
									if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
										valueData = data[field.props.field];
									}
								}
								objImg = $('<img class="' + css + '" ' + field.props.controlProp + ' src="' + valueData + '">').appendTo(td);
							}
						} else {
							if (headData[field.props.field] != undefined && headData[field.props.field] != null && headData[field.props.field] !== "") {
								objImg = $('<img class="' + css + '" src="' + headData[field.props.field] + '">').appendTo(td);
							} else {
								var valueData = "";
								if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
									try {
										valueData = eval("(" + field.props.value + ")");
									} catch (ex) {
										alert(ex);
									}
								}
								if (data != null) {
									if (data[field.props.field] != undefined && data[field.props.field] != null && data[field.props.field] !== "") {
										valueData = data[field.props.field];
									}
								}
								objImg = $('<img class="' + css + '" src="' + valueData + '">').appendTo(td);
							}									
						}
						if (edit) {
							//新增事件处理
							if (field.props.eventFunc != undefined && field.props.eventFunc != null && field.props.eventFunc !== "") {
								var eventFunc = eval("(" + field.props.eventFunc + ")");
								$.each(eventFunc, function(m, eventFunction) {
									if (eventFunction.eventNo != undefined && eventFunction.eventNo != null && eventFunction.eventNo !== "") {
										$(objImg).unbind(eventFunction.eventNo);
										if (eventFunction.eventFunc != undefined && eventFunction.eventFunc != null && eventFunction.eventFunc !== "") {
											$(objImg).bind(eventFunction.eventNo, eval("(" + decodeURIComponent(eventFunction.eventFunc) + ")"));
										}
									}
								});									
							}
							//新增Server事件处理
							if (field.props.serverFunc != undefined && field.props.serverFunc != null && field.props.serverFunc !== "") {
								var eventFunc = eval("(" + field.props.serverFunc + ")");
								if (eventFunc.event != undefined && eventFunc.event != null && eventFunc.event !== ""
									&& eventFunc.target != undefined && eventFunc.target != null && eventFunc.target !== ""
									&& eventFunc.sql != undefined && eventFunc.sql != null && eventFunc.sql != "") {
									var rowNo = $(editor).closest("tr").index();
									$(editor).unbind(eventFunc.event);
									$(editor).bind(eventFunc.event, {eventFunc:eventFunc, head:head, detail:detail,appName:appName,viewNo:viewNo,rowNo:rowNo}, myWindow.runSql);
								}
							}
						}
						break;
					default:
						break;
					}
				});
				var td = $("<td></td>").appendTo(tr);
				var id = "";
				if (data != null) {
					if (data.id != undefined && data.id != null && data.id != "") {
						id = data.id;
					}
				}
				$('<input type="hidden" value="' + id + '">').appendTo(td);
				$("<button type='button' class='btn btn-default'>删除</button>").click(function() {
					if ($(tr).index() == $(tr).closest("tbody").find("tr").length - 1) {
						MessageBox.info("不能删除最后一行！");
						return;
					}
					if ($(tr).closest("tbody").find("tr").length <= 1) {
						MessageBox.info("不能删除最后一行数据！");
						return;
					}
					$(tr).remove();
				}).appendTo(td);
			};
			$.each(this.detail, function(n, tab) {
				var objDiv = $("<div class='tabContent' style='display:none;overflow-x:auto;overflow-y:auto;position:relative;'></div>").appendTo($("#detail"));
				var objTable = $("<table class='table table-hover'></table>").appendTo(objDiv);
				var objTHead = $("<thead></thead>").appendTo($(objTable));
				var objTBody = $("<tbody></tbody>").appendTo($(objTable));
				var objTh = $("<tr></tr>").appendTo($(objTHead));
				$.each(tab.process.fields, function(n, value) {
					$("<th>" + value.label + "</th>").appendTo(objTh);
				});
				$("<th></th>").appendTo(objTh);
				//创建一行数据
				var data = eval("(" + detailData[tab.table] + ")");
				if (data != undefined && data.length > 0) {
					$.each(data, function(n,value) {
						var objTd = $("<tr></tr>").appendTo($(objTBody));
						addRow(objTd, tab.process.fields, value);
					});
				}
				if (edit) {
					//创建一条空行
					if (detailData[tab.table] != "[{}]") {
						var objTd = $("<tr></tr>").appendTo($(objTBody));
						addRow(objTd, tab.process.fields, null);
					}
				}
			});
			//明细中开窗之后的回调函数
			var detailConfirmFunc = function(config, controler, viewOk, data) {
				var index = $(controler).index();
				var tbody = $(controler).closest("tbody");
				var rows = $(tbody).find("tr").length;
				var mappingConfig = null;
				try {
					mappingConfig = eval("(" + decodeURIComponent(viewOk) + ")");
					var paramData = new Object;
					if (mappingConfig.proc != undefined && mappingConfig.proc != null && mappingConfig.proc != "") {
						//需要执行存储过程，取得表头所有数据
						$.each(head.cells, function(n, cell) {
							switch (cell.type) {
							case "label":
								if (cell.props.field != undefined && cell.props.field != null && cell.props.field !== "") {
									paramData["head_" + cell.props.field] = $("#head_" + cell.props.field).html();
								}
								break;
							case "inputEditor":
								if (cell.props.hidden != undefined && cell.props.hidden != null && cell.props.hidden !== "") {
									try {
										var hidden = eval("(" + cell.props.hidden + ")");
										if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
											if (hidden.save == "Y") {
												paramData["head_" + hidden.field] = $("#head_" + hidden.field).val();
											}
										}
									} catch (ex) {
										alert(ex);
									}
								}
							case "selectEditor":
							case "dateEditor":
							case "mutilInputEditor":
								paramData["head_" + cell.props.field] = $("#head_" + cell.props.field).val();
								break;
							case "openEditor":
								paramData["head_" + cell.props.noField] = $("#head_" + cell.props.noField).val();
								paramData["head_" + cell.props.nameField] = $("#head_" + cell.props.nameField).val();
								break;
							case "checkEditor":
								if ($("#head_" + cell.props.field).prop("checked")) {
									paramData["head_" + cell.props.field] = "Y";
								} else {
									paramData["head_" + cell.props.field] = "N";
								}
								break;
							default:
								break;
							}
						});
						//取得弹出窗口数据
						$.each(data, function (n, value) {
							$.each(value, function(m, valueData) {
								if (paramData["open_" + m] == undefined || paramData["open_" + m] == null || paramData["open_" + m] === "") {
									paramData["open_" + m] = valueData;
								} else {
									paramData["open_" + m] += "," + valueData;
								}
							});
						});
						paramData.id = $("#id").val();
						var param = new Object;
						param.proc = encodeURIComponent(mappingConfig.proc);
						param.target = mappingConfig.target;
						param.data = paramData;
						//执行存储过程
						MessageBox.processClose();
						MessageBox.processStart();
						$.ajax({
							url:appName + "/view/proc",
							type:"post",
							dataType:"json",
							contentType: "application/json",
							data:JSON.stringify(param),
							success:function(procData) {
								if (procData.errType == "1") {
									MessageBox.processEnd(procData.errMessage);
									return;
								}
								var newRow = false;
								$.each(data, function(n, value) {
									var tr = null;
									if (index >= $(tbody).find("tr").length - 1) {
										//新增行
										tr = $("<tr></tr>").appendTo(tbody);
										addRow(tr, config, null);
										newRow = true;
									}
									tr = $(tbody).find("tr:eq(" + index + ")");
									//newRow = false;
									//alert("mappingConfig=" + JSON.stringify(mappingConfig));
									$.each(mappingConfig.mapping, function(m, mapping) {
										var source = mapping.source;
										var target = mapping.target;
										var colIndex = -1;
										var colType = "";
										var noOrName = "0";
										//取得设定对象所在列
										$.each(config, function(q, field) {
											if (field.editor == "openEditor") {
												if (target == field.props.noField) {
													colIndex = q;
													colType = field.editor;
													noOrName = "0";
													return false;
												} else if (target == field.props.nameField) {
													colIndex = q;
													colType = field.editor;
													noOrName = "1";
													return false;
												}
											} else {
												if (target == field.props.field) {
													colIndex = q;
													colType = field.editor;
													noOrName = "1";
													return false;
												} else {
													if (field.props.hidden != undefined && field.props.hidden != null && field.props.hidden !== "") {
														try {
															var hidden = eval("(" + field.props.hidden + ")");
															if (target == hidden.field) {
																colIndex = q;
																colType = field.editor;
																noOrName = "0";
																return false;
															}
														} catch (ex) {
															alert(ex);
														}
													}
												}
											}
										});
										if (colIndex >= 0) {
											var td = $(tr).find("td:eq(" + colIndex + ")");
											switch(colType) {
											case 'label':								
												var editor = $(td).find("label");								
												if (mapping.sourceFlag == "head") {
													$(editor).html($("#head_" + source).val());
												} else if (mapping.sourceFlag == "open") {
													$(editor).html(value[source]);
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).html(procData.data[0][source]);
													} else {
														$(editor).html(procData.data[n][source]);
													}
												}
												break;
											case "openEditor":
												if (noOrName == "0") {
													//编码字段
													var editor = $(td).find("input[type='hidden']");
													if (mapping.sourceFlag == "head") {
														$(editor).val($("#head_" + source).val());
													} else {
														$(editor).val(value[source]);
													}
													break;
												} else if (noOrName == "1") {
													//名称字段
													var editor = $(td).find("input[type='text']");
													if (mapping.sourceFlag == "head") {
														$(editor).val($("#head_" + source).val());
													} else if (mapping.sourceFlag == "open") {
														$(editor).val(value[source]);
													} else if (mapping.sourceFlag == "proc") {
														if (procData.data == undefined || procData.data == null || procData.data === "") {
															break;
														}
														if (procData.data.length <= n) {
															$(editor).val(procData.data[0][source]);
														} else {
															$(editor).val(procData.data[n][source]);
														}
													}
													break;
												}
												break;
											case "inputEditor":
												var editor = null;
												if (noOrName == "0") {
													//设定Hidden
													editor = $(td).find("input[type='hidden']");													
												} else {
													editor = $(td).find("input[type='text']");
												}
												if (mapping.sourceFlag == "head") {
													$(editor).val($("#head_" + source).val());
												} else if (mapping.sourceFlag == "open") {
													$(editor).val(value[source]);
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).val(procData.data[0][source]);
													} else {
														$(editor).val(procData.data[n][source]);
													}
												}
												break;
											case "dateEditor":
												var editor = $(td).find("input[type='text']");
												if (mapping.sourceFlag == "head") {
													$(editor).val($("#head_" + source).val());
												} else if (mapping.sourceFlag == "open") {
													$(editor).val(value[source]);
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).val(procData.data[0][source]);
													} else {
														$(editor).val(procData.data[n][source]);
													}
												}
												break;
											case "mutilInputEditor":
												var editor = $(td).find("areatext");
												if (mapping.sourceFlag == "head") {
													$(editor).val($("#head_" + source).val());
												} else if (mapping.sourceFlag == "open") {
													$(editor).val(value[source]);
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).val(procData.data[0][source]);
													} else {
														$(editor).val(procData.data[n][source]);
													}
												}
												break;
											case "selectEditor":
												var editor = $(td).find("select");
												if (mapping.sourceFlag == "head") {
													//alert(source + "=" + $("#head_" + source).val());
													$(editor).val($("#head_" + source).val());
													if (newRow) {
														$(editor).bind("SetValue", function() {
															$(editor).val($("#head_" + source).val());
															$(editor).unbind("SetValue");
														});
													}
												} else if (mapping.sourceFlag == "open") {
													$(editor).val(value[source]);
													if (newRow) {
														$(editor).bind("SetValue", function() {
															$(editor).val(value[source]);
															$(editor).unbind("SetValue");
														});
													}
												} else if (mapping.sourceFlag == "proc") {
													if (procData.data == undefined || procData.data == null || procData.data === "") {
														break;
													}
													if (procData.data.length <= n) {
														$(editor).val(procData.data[0][source]);
													} else {
														$(editor).val(procData.data[n][source]);
													}
													if (newRow) {
														$(editor).bind("SetValue", function() {
															$(editor).val(value[source]);
															$(editor).unbind("SetValue");
														});
													}
												}												
												break;
											case "checkEditor":
												var editor = $(td).find("input[type='checkbox']");
												if (mapping.sourceFlag == "head") {
													if ($("#head_" + source).prop("checked")) {
														$(editor).prop("checked", true);
													} else {
														$(editor).prop("checked", false);
													}
												} else {
													if (value[source] == "Y") {
														$(editor).prop("checked", true);
													} else {
														$(editor).prop("checked", false);
													}
												}
												break;
											default:
												break;
											}
										}									
									});
									index++;
									if (mappingConfig.addRow != "Y") {
										//不能增行
										if (index >= $(tbody).find("tr").length - 1) {
											//结束循环
											return false;
										}
									}
								});
								MessageBox.processClose();
							},
							error:function(XMLHttpRequest, data) {
								MessageBox.processEnd("系统错误，详细信息：" + data);
							}
						});
					} else {
						//不需要执行存储过程
						var newRow = false;
						$.each(data, function(n, value) {
							var tr = null;
							if (index >= $(tbody).find("tr").length - 1) {
								//没有行，新增行
								tr = $("<tr></tr>").appendTo(tbody);
								addRow(tr, config, null);
								newRow = true;
							}
							tr = $(tbody).find("tr:eq(" + index + ")");
							$.each(mappingConfig.mapping, function(n, mapping) {
								var source = mapping.source;
								var target = mapping.target;
								var colIndex = -1;
								var colType = "";
								var noOrName = "0";
								//取得设定对象所在列
								$.each(config, function(n, field) {
									if (field.editor == "openEditor") {
										if (target == field.props.noField) {
											colIndex = n;
											colType = field.editor;
											noOrName = "0";
											return false;
										} else if (target == field.props.nameField) {
											colIndex = n;
											colType = field.editor;
											noOrName = "1";
											return false;
										}
									} else {
										if (target == field.props.field) {
											colIndex = n;
											colType = field.editor;
											return false;
										}
									}
								});
								if (colIndex >= 0) {
									var td = $(tr).find("td:eq(" + colIndex + ")");
									switch(colType) {
									case 'label':								
										var editor = $(td).find("label");								
										if (mapping.sourceFlag == "head") {
											$(editor).html($("#head_" + source).val());
										} else {
											$(editor).html(value[source]);
										}
										break;
									case "openEditor":
										if (noOrName == "0") {
											//编码字段
											var editor = $(td).find("input[type='hidden']");
											if (mapping.sourceFlag == "head") {
												$(editor).val($("#head_" + source).val());
											} else {
												$(editor).val(value[source]);
											}
											break;
										} else if (noOrName == "1") {
											//名称字段
											var editor = $(td).find("input[type='text']");
											if (mapping.sourceFlag == "head") {
												$(editor).val($("#head_" + source).val());
											} else {
												$(editor).val(value[source]);
											}
											break;
										}
										break;
									case "inputEditor":
									case "dateEditor":
										var editor = $(td).find("input[type='text']");
										if (mapping.sourceFlag == "head") {
											$(editor).val($("#head_" + source).val());
										} else {
											$(editor).val(value[source]);
										}
										break;
									case "mutilInputEditor":
										var editor = $(td).find("areatext");
										if (mapping.sourceFlag == "head") {
											$(editor).val($("#head_" + source).val());
										} else {
											$(editor).val(value[source]);
										}
										break;
									case "selectEditor":
										var editor = $(td).find("select");
										if (mapping.sourceFlag == "head") {
											//alert(source + "=" + $("#head_" + source).val());
											$(editor).val($("#head_" + source).val());
											if (newRow) {
												$(editor).bind("SetValue", function() {
													$(editor).val($("#head_" + source).val());
													$(editor).unbind("SetValue");
												});
											}
										} else {
											$(editor).val(value[source]);
											if (newRow) {
												$(editor).bind("SetValue", function() {
													$(editor).val(value[source]);
													$(editor).unbind("SetValue");
												});
											}
										}
										break;
									case "checkEditor":
										var editor = $(td).find("input[type='checkbox']");
										if (mapping.sourceFlag == "head") {
											if ($("#head_" + source).prop("checked")) {
												$(editor).prop("checked", true);
											} else {
												$(editor).prop("checked", false);
											}
										} else {
											if (value[source] == "Y") {
												$(editor).prop("checked", true);
											} else {
												$(editor).prop("checked", false);
											}
										}
										break;
									default:
										break;
									}
								}
							});
							index++;
							if (mappingConfig.addRow != "Y") {
								//不能增行
								if (index >= $(tbody).find("tr").length - 1) {
									//结束循环
									return false;
								}
							}
						});
					}
				} catch (ex) {
					alert(ex);
				}
				/**/
			};
			$.each($("#detail div[class='tabContent']"),function(){$(this).hide();});
			//页签切换
			if ($(objUl).find("li")) {
				var index = $(objUl).find("li[class='cur']").index();
				$("#detail div[class='tabContent']").eq(index).show();
				$(objUl).find("li").unbind("click");
		        $(objUl).find("li").bind("click", function(){
			        $(objUl).find("li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
					$("#detail div[class='tabContent']").hide();
					$("#detail div[class='tabContent']").eq($(this).index()).show();
			    });
			}
			//附件数据生成
			get_attachment(this);
			//新增
			$("#btn_add").unbind("click");
			$("#btn_add").bind("click", {view:this}, this.add_data);
			//设定保存按钮、返回按钮处理
			$("#btn_save").unbind("click");
			$("#btn_save").bind("click", {view:this}, this.save_data);
			//提交按钮
			$("#btn_submit").unbind("click");
			$("#btn_submit").bind("click", {view:this}, this.submit_data);
			//撤销提交按钮
			$("#btn_cancel_submit").unbind("click");
			$("#btn_cancel_submit").bind("click", {view:this}, this.cancelSubmit_data);
			//查审按钮
			$("#btn_view_audit").unbind("click");
			$("#btn_view_audit").bind("click", {view:this}, this.viewAudit);
			//审核按钮
			$("#btn_audit").unbind("click");
			$("#btn_audit").bind("click", {view:this}, this.audit_data);
			//撤销审核按钮
			$("#btn_cancel_audit").unbind("click");
			$("#btn_cancel_audit").bind("click", {view:this}, this.cancelAudit_data);
			//附件按钮
			$("#btn_file").unbind("click");
			$("#btn_file").bind("click", {view:this}, this.upload_file);
			//返回
			$("#btn-back").unbind("click");
			$("#btn-back").bind("click", {view:this}, this.back_list);
			//创建自定义按钮
			var createButton = function(view) {
				if (head.buttons == undefined || head.buttons == null) {
					return;
				}
				$.each(head.buttons, function(n, value) {
					var buttonId = value.button;
					var position = value.position;
					var div = $("#" + buttonId).closest("div");
					var divText = "<div class='search'><button class='btn_header' id='" + value.id + "' type='button'>" + value.name + "</button></div>";
					if (position == "bak") {
						$(div).after(divText);
					} else {
						$(div).before(divText);
					}
					//设定自定义按钮事件
					$("#" + value.id).unbind("click");
					$("#" + value.id).bind("click", {view:view,button:value}, view.button_click);
				});
			};
			createButton(this);
			return this;
		},
		get_viewData : function(head, detail, viewNo) {
			var param = new Object;
			param.headData = {};
			//Head数据做成
			$.each(head.cells, function(n, cell) {
				//alert(condition.editor);
				switch (cell.type) {
				case "label":
					if (cell.props.field != undefined && cell.props.field != null && cell.props.field != "") {
						if (cell.props.save != undefined && cell.props.save != null && cell.props.save == "Y") {
							param.headData[cell.props.field] = $("#head_" + cell.props.field).html();
						}
					}
					break;
				case "inputEditor":
					if (cell.props.hidden != undefined && cell.props.hidden != null && cell.props.hidden !== "") {
						try {
							var hidden = eval("(" + cell.props.hidden + ")");
							if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
								if (hidden.save == "Y") {
									param.headData[hidden.field] = $("#head_" + hidden.field).val();
								}
							}
						} catch (ex) {
							alert(ex);
						}
					}
				case "selectEditor":
				case "dateEditor":
				case "mutilInputEditor":
					if (cell.props.save == "Y") {
						param.headData[cell.props.field] = $("#head_" + cell.props.field).val();
					}
					break;
				case "openEditor":
					if (cell.props.save == "Y") {
						if (cell.props.noField != undefined && cell.props.noField != null && cell.props.noField !== "") {
							param.headData[cell.props.noField] = $("#head_" + cell.props.noField).val();
						}
						if (cell.props.nameField != undefined && cell.props.nameField != null && cell.props.nameField !== "") {
							param.headData[cell.props.nameField] = $("#head_" + cell.props.nameField).val();
						}
					}
					break;
				case "checkEditor":
					if (cell.props.save == "Y") {
						if ($("#head_" + cell.props.field).prop("checked")) {
							param.headData[cell.props.field] = "Y";
						} else {
							param.headData[cell.props.field] = "N";
						}
					}
					break;
				default:
					if (cell.props.save == "Y") {
						param.headData[cell.props.field] = $("#head_" + cell.props.field).val();
					}
					break;
				}

			});
			//明细数据做成
			param.detailData = {};
			$.each(detail, function(n, tab) {
				//标签页
				var objTab = $(".tabContent")[n];
				//数据表
				var dbTable = tab.table;
				//alert(JSON.stringify(tab.process.fields));
				if (tab.process == undefined) {
					return true;
				}
				if (tab.process.fields == undefined) {
					return true;
				}
				//循环取得数据
				//alert($(objTab).html());
				var tabData = [];
				$.each($(objTab).find("tbody tr"), function(m, tr) {
					//alert($(tr).html());
					if ($(tr).index() >= $(objTab).find("tbody tr").length - 1) {
						//不需要读取最后一条空行数据
						return false;
					}
					var rowData = {};
					var tdIndex = $(tr).find("td").length;
					if (tdIndex > 0) {
						tdIndex = tdIndex - 1;
						var td = $(tr).find("td:eq(" + tdIndex + ")");
						var editor = $(td).find("input[type='hidden']");
						rowData.id = $(editor).val();
					}
					$.each(tab.process.fields, function(fieldIndex, field) {
						//alert(JSON.stringify(field));
						if (field.submit != "Y") {
							//结束本次循环，进入下一循环
							return true;
						}
						//取得数据
						var td = $(tr).find("td:eq(" + fieldIndex + ")");
						if (td == null) {
							//结束本次循环，进入下一循环
							return true;
						}
						switch (field.editor) {
						case "label":
							if (field.props.field != undefined && field.props.field != null && field.props.field !== "") {
								if (field.props.save === "Y") {
									var objLabel = $(td).find("label");
									rowData[field.props.field] = $(objLabel).html();
								}
							}
							break;
						case "inputEditor":
							if (field.props.hidden != undefined && field.props.hidden != null && field.props.hidden !== "") {
								try {
									var hidden = eval("(" + field.props.hidden + ")");
									if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
										if (hidden.save == "Y") {
											var objHidden = $(td).find("input[type='hidden']");
											rowData[hidden.field] = $(objHidden).val();
										}
									}
								} catch (ex) {
									alert(ex);
								}
							}
						case "dateEditor":
							if (field.props.save == "Y") {
								var objText = $(td).find("input[type='text']");
								rowData[field.props.field] = $(objText).val();
							}
							break;
						case "selectEditor":
							if (field.props.save == "Y") {
								var objSelect = $(td).find("select");
								rowData[field.props.field] = $(objSelect).val();
							}
							break;						
						case "mutilInputEditor":
							if (field.props.save == "Y") {
								var objText = $(td).find("areatext");
								rowData[field.props.field] = $(objText).val();
							}
							break;	
						case "openEditor":
							if (field.props.save == "Y") {
								if (field.props.noField != undefined && field.props.noField != null && field.props.noField !== "") {
									var objText = $(td).find("input[type='hidden']");
									rowData[field.props.noField] = $(objText).val();
								}
								if (field.props.nameField != undefined && field.props.nameField != null && field.props.nameField !== "") {
									var objText = $(td).find("input[type='text']");
									rowData[field.props.nameField] = $(objText).val();
								}
							}
							break;
						case "checkEditor":
							if (field.props.save == "Y") {
								var objCheck = $(td).find("input[type='checkbox']");
								if ($(objCheck).prop("checked")) {
									rowData[field.props.field] = "Y";
								} else {
									rowData[field.props.field] = "N";
								}
							}
							break;
						default:
							break;
						}
					});
					tabData.push(rowData);
				});
				param.detailData[dbTable] = tabData;
			});
			param.id = $("#id").val();
			//附件数据
			param.attachment = [];
			$.each($("#myAttachmentModal table tbody input[type='hidden']"), function() {
				param.attachment.push($(this).val());
			});
			return param;
		},
		add_data : function(event) {
			var view = event.data.view;
			var appName = view.appName;
			var viewNo = view.viewNo;
			location.href = appName + "/view/add/" + viewNo;
		},
		save_data : function(event) {
			var view = event.data.view;
			var head = view.head;
			var detail = view.detail;
			var appName = view.appName;
			var viewNo = view.viewNo;
			//取得自定义按钮
			if (head.buttons != undefined && head.buttons != null) {
				var hasError = false;
				$.each(head.buttons, function(n, button) {
					if (button.save =="yes") {
						if (view.changeData.length > 0) {
							MessageBox.info("数据已修改，请执行" + button.name + "!");
							hasError = true;
							return false;
						}
					}
				});
				if (hasError) {
					return;
				}
			}
			//画面数据取得
			var param = view.get_viewData(head, detail, viewNo);
			param.token = view.token;
			//alert(JSON.stringify(param));
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/save/" + viewNo,
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.token != undefined && data.token != null && data.token !== "") {
						view.token = data.token;
					}
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					MessageBox.processEnd(data.errMessage, function() {
						//window.location.href = appName + "/view/init/" + viewNo;
						$("#dataStatus").html("保存");
						$("#id").val(data.id);
						var sUrl = appName + "/view/edit/" + viewNo + "?id=" + data.id;
						window.location.href = sUrl;
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		},
		submit_data : function(event) {
			var view = event.data.view;
			var head = view.head;
			var detail = view.detail;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var token = view.token;
			//画面数据取得
			var param = view.get_viewData(head, detail, viewNo);
			param.token = token;
			//alert(JSON.stringify(param));
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/save/" + viewNo,
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.token != undefined && data.token != null && data.token !== "") {
						view.token = data.token;
					}
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					//提交处理
					$("#id").val(data.id);
					$("#dataStatus").html("保存");
					var ids = [];
					ids.push(data.id);
					$.ajax({
						url:appName + "/view/submit/" + viewNo,
						type:"post",
						dataType:"json",
						data:{
							ids:ids.toString(),
							token:data.token
						},
						success:function(auditData) {
							if (auditData.token != undefined && auditData.token != null && auditData.token !== "") {
								view.token = auditData.token;
							}
							if (auditData.errType == "0") {
								MessageBox.processEnd("提交成功！", function() {
									location.href= appName + "/view/view/" + viewNo + "?id=" + data.id;
								});
								return;
							} else {
								MessageBox.processEnd(auditData.errMessage);
							}
						},
						error:function(XMLHttpRequest, auditData) {
							MessageBox.processEnd("系统错误，信息信息：" + auditData);
						}
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		},
		cancelSubmit_data : function(event) {
			var view = event.data.view;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var ids = [];
			ids.push($("#id").val());
			var token = view.token;
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/cancelSubmit/" + viewNo,
				type:"post",
				dataType:"json",
				data:{
					ids:ids.toString(),
					token:token
				},
				success:function(submitData) {
					if (submitData.token != undefined && submitData.token != null && submitData.token !== "") {
						view.token = submitData.token;
					}
					if (submitData.errType == "0") {
						MessageBox.processEnd("撤销成功！", function() {
							location.href= appName + "/view/edit/" + viewNo + "?id=" + $("#id").val();
						});
						return;
					} else {
						MessageBox.processEnd(submitData.errMessage);
					}
				},
				error:function(XMLHttpRequest, submitData) {
					MessageBox.processEnd("系统错误，信息信息：" + submitData);
				}
			});
		},
		viewAudit : function(event) {
			var view = event.data.view;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var ids = [];
			ids.push($("#id").val());
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
				error:function(XMLHttpRequest, submitData) {
					MessageBox.processEnd("系统错误，信息信息：" + submitData);
				}
			});
		},
		audit_data : function(event) {
			var view = event.data.view;
			var head = view.head;
			var detail = view.detail;
			var appName = view.appName;
			var viewNo = view.viewNo;
			//画面数据取得
			var param = view.get_viewData(head, detail, viewNo);
			param.token = view.token;
			//alert(JSON.stringify(param));
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/save/" + viewNo,
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.token != undefined && data.token != null && data.token !== "") {
						view.token = data.token;
					}
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					//审核处理
					var ids = [];
					ids.push(data.id);
					$.ajax({
						url:appName + "/view/audit/" + viewNo,
						type:"post",
						dataType:"json",
						data:{
							ids:ids.toString(),
							token:data.token
						},
						success:function(auditData) {
							if (auditData.token != undefined && auditData.token != null && auditData.token !== "") {
								view.token = auditData.token;
							}
							if (auditData.errType == "0") {
								MessageBox.processEnd("审核成功！", function() {
									location.href= appName + "/view/view/" + viewNo + "?id=" + data.id;
								});
								return;
							} else {
								MessageBox.processEnd(auditData.errMessage);
							}
						},
						error:function(XMLHttpRequest, auditData) {
							MessageBox.processEnd("系统错误，信息信息：" + auditData);
						}
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		},
		cancelAudit_data : function(event) {
			var view = event.data.view;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var ids = [];
			ids.push($("#id").val());
			var token = view.token;
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/cancelAudit/" + viewNo,
				type:"post",
				dataType:"json",
				data:{
					ids:ids.toString(),
					token:token
				},
				success:function(auditData) {
					if (auditData.token != undefined && auditData.token != null && auditData.token !== "") {
						view.token = auditData.token;
					}
					if (auditData.errType == "0") {
						MessageBox.processEnd("撤审成功！", function() {
							location.href= appName + "/view/edit/" + viewNo + "?id=" + $("#id").val();
						});
						return;
					} else {
						MessageBox.processEnd(auditData.errMessage);
					}
				},
				error:function(XMLHttpRequest, auditData) {
					MessageBox.processEnd("系统错误，信息信息：" + auditData);
				}
			});
		},
		back_list : function(event) {
			var view = event.data.view;			
			var appName = view.appName;
			var viewNo = view.viewNo;
			var sUrl = appName + "/view/init/" + viewNo;
			window.location.href = sUrl;
		},
		upload_file : function(event) {
			var view = event.data.view;
			var head = view.head;
			var detail = view.detail;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var dataId = view.id
			var token = view.token;
			var objForm = $("#myAttachmentModal form");
			$(objForm).html("");
			var objFile = $("<input type='file' name='importFile'>").appendTo(objForm);
			$("<input type='hidden' value='" + dataId + "' name='dataId'>").appendTo(objForm);
			$("<input type='hidden' value='' name='id'>").appendTo(objForm);
			var maxSeq = $("#myAttachmentModal table tbody tr").length;
			$("<input type='hidden' value='" + maxSeq + "' name='maxSeqNo'>").appendTo(objForm);
			$("<input type='hidden' value='" + token + "' name='token'>").appendTo(objForm);
			$(objFile).change(function() {
				//文件上传
				MessageBox.processStart();
				var formData = new FormData($(objForm)[0]);	
				$.ajax({
					type:"POST",
					dataType:"json",
					data:formData,
					url:appName + "/view/upload/" + viewNo,
					contentType:false,
					processData:false
				}).success(function(data){
					if (data.token != undefined && data.token != null && data.token !== "") {
						view.token = data.token;
					}
					$("#import-message").html("");
					if (data.errType == "0") {
						MessageBox.processEnd("上传成功！");
						$("#attachmentShow").html("");
						$(objForm).html("");
						var tbody = $("#myAttachmentModal table tbody");
						var tr = $("<tr></tr>").appendTo(tbody);
						$("<td><input type='hidden' value='" + data.data.id + "'><a href='#'>" + data.data.fileName + "</a></td>").appendTo(tr);
						$("<td>" + data.data.fileExtend + "</td>").appendTo(tr);
						var td = $("<td></td>").appendTo(tr);
						$("<button type='button' class='btn'><span class='glyphicon glyphicon-remove'></span>删除</button>").appendTo(td);
						$("<button class='btn'><span class='glyphicon glyphicon-file'></span>附件</button>").click(function() {
							var modal = $('#myAttachmentModal').modal({backdrop: 'static', keyboard: false});
					    	modal.show();
						}).appendTo($("#attachmentShow"));
						$("#myAttachmentModal table tbody button").unbind("click");
						$("#myAttachmentModal table tbody button").bind("click", {view:view}, view.deleteFile);
						$("#myAttachmentModal table tbody a").unbind("click");
						$("#myAttachmentModal table tbody a").bind("click", {view:view}, view.viewFile);
					} else {
						MessageBox.processEnd(data.errMessage);
					}				
				}).error(function(data){
					MessageBox.processEnd(data);
				});
			});
			$(objFile).trigger("click");
		},
		deleteFile: function(event) {
			var view = event.data.view;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var id = $(this).closest("tr").find("input[type='hidden']").val();
			var tr = $(this).closest("tr");
			MessageBox.confirm("是否要删除附件！", function() {
				MessageBox.processStart();
				$.ajax({
					type:"post",
					dataType:"json",
					data:{
						id:id
					},
					url:appName + "/view/deleteAttachment/" + viewNo
				}).success(function(data) {
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					MessageBox.processEnd("删除成功！", function() {
						$(tr).remove();
						if ($("#myAttachmentModal table tbody tr").length <= 0) {
							$("#attachmentShow").html("");
						}
					});
				}).error(function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
				});
			});
		},
		viewFile : function(event) {
			//alert(JSON.stringify(event.data));
			var view = event.data.view;
			var appName = view.appName;
			var viewNo = view.viewNo;
			var tr = $(this).closest("tr");
			var fileType = $(tr).find("td:eq(1)").html();
			var id = $(tr).find("input[type='hidden']").val();
			var imgData = {};
			var viewFile2 = function(pageNo) {
				$.ajax({
					type:"post",
					dataType:"json",
					data:{
						id:id,
						pageNo:pageNo
					},
					url:appName + "/view/viewAttachment/" + viewNo,
					success : function(data) {
						imgData = data;
						$("#myAttachmentViewModal").trigger("show_image");
					},
					error : function(XMLHttpRequest, data) {					
						imgData = {};
						$("#myAttachmentViewModal").trigger("show_image");
					}
				});
			};
			if (fileType != undefined && fileType != null) {
				switch (fileType.toLocaleLowerCase()) {
				case "pdf":
					var pageNo = $("#PageNo").val();
					$("#PageButton").show();
					$("#myAttachmentViewModal").unbind("show_image");
					$("#myAttachmentViewModal").bind("show_image", function() {
						if (imgData.errType == undefined) {
							MessageBox.info("系统错误！");
							return;
						}
						if (imgData.errType == "1") {
							MessageBox.Info(imgData.errMessage);
							return;
						}
						var objDiv = $("#myAttachmentViewModal div[class='row']");
						$(objDiv).scrollTop(0);
						var height = parseInt($(window).height() * 0.6);
						$(objDiv).height(height);
						$(objDiv).width(900);
						$(objDiv).html("");
						var img = $("<img src='data:image/" + fileType.toLocaleLowerCase() + ";base64," + imgData.fileContent + "' width='800'>").load(function() {
							
						}).appendTo(objDiv);
						if (imgData.totalPage != undefined && imgData.totalPage != null) {
							if (imgData.pageNo != undefined && imgData.pageNo != null) {
								$("#PageNo").val(imgData.pageNo);
								if (parseInt(imgData.pageNo) <= 1) {
									$("#PrevPage").prop("disabled", true);
									if (parseInt(imgData.pageNo) >= parseInt(imgData.totalPage)) {
										$("#NextPage").prop("disabled", true);
									} else {
										$("#NextPage").prop("disabled", false);
									}
								} else if (parseInt(imgData.pageNo) >= parseInt(imgData.totalPage)) {
									$("#PageNo").val(imgData.totalPage);
									$("#PrevPage").prop("disabled", false);
									$("#NextPage").prop("disabled", true);
								} else {
									$("#PrevPage").prop("disabled", false);
									$("#NextPage").prop("disabled", false);
								}
								$("#CurrentPageNo").html($("#PageNo").val());
								$("#TotalPages").html(imgData.totalPage);
							}
						}
					});
					viewFile2(pageNo);					
					var modal = $('#myAttachmentViewModal').modal({backdrop: 'static', keyboard: false});
			    	modal.show();
					$("#PrevPage").unbind("click");
					$("#PrevPage").bind("click", function() {
						imgData = {};
						var page = parseInt($("#PageNo").val()) - 1;
						if (page <= 0) {
							page = 1;
						}
						$("#PageNo").val(page);
						viewFile2(page);
						var objDiv = $("#myAttachmentViewModal div[class='row']");
						$(objDiv).scrollTop(0);
					});
					$("#NextPage").unbind("click");
					$("#NextPage").bind("click", function() {
						imgData = {};
						var page = parseInt($("#PageNo").val()) + 1;
						if (page >= parseInt(imgData.totalPage)) {
							page = imgData.totalPage;
						}
						$("#PageNo").val(page);
						viewFile2(page);
						var objDiv = $("#myAttachmentViewModal div[class='row']");
						$(objDiv).scrollTop(0);
					});
					break;
				case "png":
				case "jpg":
				case "gif":
					$("#PageButton").hide();
					viewFile2(1);
					var modal = $('#myAttachmentViewModal').modal({backdrop: 'static', keyboard: false});
			    	modal.show();
			    	$("#myAttachmentViewModal").unbind("show_image");
					$("#myAttachmentViewModal").bind("show_image", function() {
						if (imgData.errType == undefined) {
							MessageBox.info("系统错误！");
							return;
						}
						if (imgData.errType == "1") {
							MessageBox.Info(imgData.errMessage);
							return;
						}
						var objDiv = $("#myAttachmentViewModal div[class='row']");
						$(objDiv).scrollTop(0);
						var height = parseInt($(window).height() * 0.6);
						$(objDiv).height(height);
						$(objDiv).width(900);
						$(objDiv).html("");
						var img = $("<img src='data:image/" + fileType.toLocaleLowerCase() + ";base64," + imgData.fileContent + "' width='800'>").load(function() {
							
						}).appendTo(objDiv);
					});
					break;
				default:
					//下载
					location.href = appName + "/view/viewAttachment/" + viewNo + "?pageNo=0&id=" + id;
					break;
				}
			}
			
		},
		runSql : function(event) {
			var head = event.data.head;
			var detail = event.data.detail;
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
			var eventFunc = event.data.eventFunc;
			var rowNo = null;
			if (event.data.rowNo == undefined || event.data.rowNo == null || event.data.rowNo === "") {
				rowNo = 0;
			} else {
				rowNo = $(this).closest("tr").index();
			}
			var befData = "";
			if ($(this)[0].tagName.toUpperCase() == "SELECT") {
				befData = $(this).attr("BeforeData");
			}
			if (eventFunc.checkFunc != undefined && eventFunc.checkFunc != null && eventFunc.checkFunc != "") {
				var checkFunc = eval("(" + decodeURIComponent(eventFunc.checkFunc) + ")");
				if (checkFunc(befData, $(this)) == false) {
					return;
				}
			}
			if ($(this)[0].tagName.toUpperCase() == "SELECT") {
				if ($(this).val() != befData) {
					$(this).attr("BeforeData", $(this).val());
				}
			}
			//alert(rowNo);
			//取得数据
			var param = {};
			//Head数据做成
			$.each(head.cells, function(n, cell) {
				//alert(condition.editor);
				//if (cell.props.save == "Y") {
				switch (cell.type) {
				case "label":
					if (cell.props.field != undefined && cell.props.field != null && cell.props.field !== "") {
						param["head_" + cell.props.field] = $("#head_" + cell.props.field).html();
					}
					break;
				case "inputEditor":
					if (cell.props.hidden != undefined && cell.props.hidden != null && cell.props.hidden !== "") {
						try {
							var hidden = eval("(" + cell.props.hidden + ")");
							if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
								//alert(hidden.field);
								//if (hidden.save == "Y") {
								param["head_" + hidden.field] = $("#head_" + hidden.field).val();
								//}
							}
						} catch (ex) {
							alert(ex);
						}
					}
				case "selectEditor":
				case "dateEditor":
				case "mutilInputEditor":
					param["head_" + cell.props.field] = $("#head_" + cell.props.field).val();
					break;
				case "openEditor":
					param["head_" + cell.props.noField] = $("#head_" + cell.props.noField).val();
					param["head_" + cell.props.nameField] = $("#head_" + cell.props.nameField).val();
					break;
				case "checkEditor":
					if ($("#head_" + cell.props.field).prop("checked")) {
						param["head_" + cell.props.field] = "Y";
					} else {
						param["head_" + cell.props.field] = "N";
					}
					break;
				default:
					param["head_" + cell.props.field] = $("#head_" + cell.props.field).val();
					break;
				}
				//}
			});
			//明细数据做成
			if (eventFunc.detail != undefined && eventFunc.detail != null && eventFunc.detail !== "") {				
				$.each(detail, function(n, tab) {
					//标签页
					var objTab = $(".tabContent")[n];
					if (tab.table != eventFunc.detail) {
						//结束循环执行下一条
						return true;
					}
					//数据表
					var dbTable = tab.table;
					//alert(JSON.stringify(tab.process.fields));
					if (tab.process == undefined) {
						return true;
					}
					if (tab.process.fields == undefined) {
						return true;
					}
					//取得明细数据
					//alert($(objTab).html());
					var trIndex = 0;
					if (rowNo != undefined && rowNo != null && rowNo) {
						trIndex = rowNo;
					}
					var tr = $(objTab).find("tbody tr:eq(" + trIndex + ")");
					var tdIndex = $(tr).find("td").length;
					if (tdIndex > 0) {
						tdIndex = tdIndex - 1;
						var td = $(tr).find("td:eq(" + tdIndex + ")");
						var editor = $(td).find("input[type='hidden']");
						param.detail_id = $(editor).val();
					}
					$.each(tab.process.fields, function(fieldIndex, field) {
						//取得数据
						var td = $(tr).find("td:eq(" + fieldIndex + ")");
						if (td == null) {
							//结束本次循环，进入下一循环
							return true;
						}
						switch (field.editor) {
						case "label":
							if (field.props.field != undefined && field.props.field != null && field.props.field !== "") {
								var objLabel = $(td).find("label");
								param["detail_" + field.props.field] = $(objLabel).html();
							}
							break;
						case "inputEditor":
							if (field.props.hidden != undefined && field.props.hidden != null && field.props.hidden !== "") {
								try {
									var hidden = eval("(" + field.props.hidden + ")");
									if (hidden.field != undefined && hidden.field != null && hidden.field != "") {
										var objHidden = $(td).find("input[type='hidden']");
										param["detail_" + hidden.field] = $(objHidden).val();
									}
								} catch (ex) {
									alert(ex);
								}
							}
						case "dateEditor":
							var objText = $(td).find("input[type='text']");
							param["detail_" + field.props.field] = $(objText).val();
							break;
						case "selectEditor":
							var objSelect = $(td).find("select");
							param["detail_" + field.props.field] = $(objSelect).val();
							break;						
						case "mutilInputEditor":
							var objText = $(td).find("areatext");
							param["detail_" + field.props.field] = $(objText).val();
							break;	
						case "openEditor":
							if (field.props.noField != undefined && field.props.noField != null && field.props.noField !== "") {
								var objText = $(td).find("input[type='hidden']");
								param["detail_" + field.props.noField] = $(objText).val();
							}
							if (field.props.nameField != undefined && field.props.nameField != null && field.props.nameField !== "") {
								var objText = $(td).find("input[type='text']");
								param["detail_" + field.props.nameField] = $(objText).val();
							}
							break;
						case "checkEditor":
							var objCheck = $(td).find("input[type='checkbox']");
							if ($(objCheck).prop("checked")) {
								param["detail_" + field.props.field] = "Y";
							} else {
								param["detail_" + field.props.field] = "N";
							}
							break;
						default:
							break;
						}
					});
				});
			}
			param.id = $("#id").val();
			var paramData = {};
			paramData.data = param;
			paramData.proc = eventFunc.sql;
			paramData.target = eventFunc.target;
			var obj = this;
			$.ajax({
				url:appName + "/view/proc",
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(paramData),
				success:function(data) {
					//alert(JSON.stringify(data));
					if (data.errType == "1") {
						//alert(data.errMessage);
						if ($(obj).is("input[type='checkbox']")) {
							
						} else {
							//$(obj).val("");
						}
						MessageBox.info(data.errMessage);
						return;
					}
					if (eventFunc.mapping == undefined || eventFunc.mapping == null || eventFunc.mapping === "") {
						return;
					}
					//alert(JSON.stringify(data.data));
					$.each(data.data, function(n, value) {
						$.each(eventFunc.mapping, function(m, mapping) {
							//alert(JSON.stringify(mapping));
							if (mapping.targetType == "detail") {
								//明细
								$.each(detail, function(n, tab) {
									//标签页
									var objTab = $(".tabContent")[n];
									if (tab.table != mapping.table) {
										//结束循环执行下一条
										return true;
									}
									//数据表
									var dbTable = tab.table;
									//alert(JSON.stringify(tab.process.fields));
									if (tab.process == undefined) {
										return true;
									}
									if (tab.process.fields == undefined) {
										return true;
									}
									//设定明细数据
									if (mapping.rule == "all") {
										var rows = $(objTab).find("tbody tr").length - 1;
										$.each($(objTab).find("tbody tr"), function(n, tr) {
											if ($(tr).index() >= rows) {
												//最后一行
												return false;
											}
											//循环tr
											$.each(tab.process.fields, function(fieldIndex, field) {
												//循环字段设定数据
												var td = $(tr).find("td:eq(" + fieldIndex + ")");
												if (td == null) {
													//结束本次循环，进入下一循环
													return true;
												}
												switch (field.editor) {
												case "label":
													if (field.props.field == mapping.targetField) {
														$(td).find("label").html(value[mapping.sourceField]);
													}
													break;
												case "inputEditor":
													if (field.props.hidden != undefined && field.props.hidden != null && field.props.hidden !== "") {
														try {
															var hidden = eval("(" + field.props.hidden + ")");
															if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
																if (hidden.field == mapping.targetField) {
																	$(td).find("input[type='hidden']").val(value[mapping.sourceField]);
																}
															}
														} catch (ex) {
															alert(ex);
														}
													}
												case "dateEditor":
													if (field.props.field == mapping.targetField) {
														var backData = $(td).find("input[type='text']").val();
														$(td).find("input[type='text']").val(value[mapping.sourceField]);
														if (backData != value[mapping.sourceField]) {
															$(td).find("input[type='text']").trigger("change");
														}
													}
													break;
												case "selectEditor":
													if (field.props.field == mapping.targetField) {
														var backData = $(td).find("select").val();
														$(td).find("select").val(value[mapping.sourceField]);
														if (backData != value[mapping.sourceField]) {
															$(td).find("select").trigger("change");
														}
													}
													break;						
												case "mutilInputEditor":
													if (field.props.field == mapping.targetField) {
														var backData = $(td).find("areatext").val();
														$(td).find("areatext").val(value[mapping.sourceField]);
														if (backData != value[mapping.sourceField]) {
															$(td).find("areatext").trigger("change");
														}
													}
													break;	
												case "openEditor":
													if (field.props.noField == mapping.targetField) {
														$(td).find("input[type='hidden']").val(value[mapping.sourceField]);
													} else if (field.props.nameField == mapping.targetField) {
														$(td).find("input[type='text']").val(value[mapping.sourceField]);
													}
													break;
												case "checkEditor":
													if (field.props.field == mapping.targetField) {
														var objCheck = $(td).find("input[type='checkbox']");
														var backData = $(objCheck).prop("checked") ? "Y" : "N";
														if (value[mapping.sourceField] == "Y") {
															$(objCheck).prop("checked", true);
														} else {
															$(objCheck).prop("checked", false);
														}
														var newData = $(objCheck).prop("checked") ? "Y" : "N";
														if (newData != backData) {
															$(objCheck).trigger("change");
														}
													}
													break;
												default:
													break;
												}
											});
										});
									} else {
										var trIndex = 0;
										if (mapping.rule == "first") {
											//第一行
											trIndex = 0;
										} else if (mapping.rule == "last") {
											//最后一行
											trIndex = $(objTab).find("tbody tr").length - 1;
										} else if (mapping.rule == "current") {
											//当前行
											trIndex = rowNo;
										}
										var tr = $(objTab).find("tbody tr:eq(" + trIndex + ")");
										$.each(tab.process.fields, function(fieldIndex, field) {
											//循环字段设定数据
											var td = $(tr).find("td:eq(" + fieldIndex + ")");
											if (td == null) {
												//结束本次循环，进入下一循环
												return true;
											}
											switch (field.editor) {
											case "label":
												if (field.props.field == mapping.targetField) {
													$(td).find("label").html(value[mapping.sourceField]);
												}
												break;
											case "inputEditor":
												if (field.props.hidden != undefined && field.props.hidden != null && field.props.hidden !== "") {
													try {
														var hidden = eval("(" + field.props.hidden + ")");
														if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
															if (hidden.field == mapping.targetField) {
																$(td).find("input[type='hidden']").val(value[mapping.sourceField]);
															}
														}
													} catch (ex) {
														alert(ex);
													}
												}
											case "dateEditor":
												if (field.props.field == mapping.targetField) {
													var backData = $(td).find("input[type='text']").val();
													$(td).find("input[type='text']").val(value[mapping.sourceField]);
													if (backData != value[mapping.sourceField]) {
														$(td).find("input[type='text']").trigger("change");
													}
												}
												break;
											case "selectEditor":
												if (field.props.field == mapping.targetField) {
													var backData = $(td).find("select").val();
													$(td).find("select").val(value[mapping.sourceField]);
													if (backData != value[mapping.sourceField]) {
														$(td).find("select").trigger("change");
													}
												}
												break;						
											case "mutilInputEditor":
												if (field.props.field == mapping.targetField) {
													var backData = $(td).find("areatext").val();
													$(td).find("areatext").val(value[mapping.sourceField]);
													if (backData != value[mapping.sourceField]) {
														$(td).find("areatext").trigger("change");
													}
												}
												break;	
											case "openEditor":
												if (field.props.noField == mapping.targetField) {
													$(td).find("input[type='hidden']").val(value[mapping.sourceField]);
												} else if (field.props.nameField == mapping.targetField) {
													$(td).find("input[type='text']").val(value[mapping.sourceField]);
												}
												break;
											case "checkEditor":
												if (field.props.field == mapping.targetField) {
													var objCheck = $(td).find("input[type='checkbox']");
													var backData = $(objCheck).prop("checked") ? "Y" : "N";
													if (value[mapping.sourceField] == "Y") {
														$(objCheck).prop("checked", true);
													} else {
														$(objCheck).prop("checked", false);
													}
													if (backData != value[mapping.sourceField]) {
														$(objCheck).trigger("change");
													}
												}
												break;
											default:
												break;
											}
										});
									}
								});
							} else {
								//设定Head数据
								$.each(head.cells, function(n, cell) {									
									switch (cell.type) {
									case "label":
										if (cell.props.field == mapping.targetField) {
											$("#head_" + cell.props.field).html(value[mapping.sourceField]);
										}
										break;
									case "inputEditor":
										if (cell.props.hidden != undefined && cell.props.hidden != null && cell.props.hidden !== "") {
											try {
												var hidden = eval("(" + cell.props.hidden + ")");
												if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
													if (hidden.field == mapping.targetField) {
														$("#head_" + hidden.field).val(value[mapping.sourceField]);
													}
												}
											} catch (ex) {
												alert(ex);
											}
										}
									case "selectEditor":
									case "dateEditor":
									case "mutilInputEditor":
										if (cell.props.field == mapping.targetField) {
											var backData = $("#head_" + cell.props.field).val();
											$("#head_" + cell.props.field).val(value[mapping.sourceField]);
											if (backData != value[mapping.sourceField]) {
												$("#head_" + cell.props.field).trigger("change");
											}
										}
										break;
									case "openEditor":
										if (cell.props.noField == mapping.targetField) {
											$("#head_" + cell.props.noField).val(value[mapping.sourceField]);
										}
										if (cell.props.nameField == mapping.targetField) {
											$("#head_" + cell.props.nameField).val(value[mapping.sourceField]);
										}
										break;
									case "checkEditor":
										if (cell.props.field == mapping.targetField) {
											var backData = $("#head_" + cell.props.field).prop("checked");
											if (value[mapping.sourceField] == "Y") {
												$("#head_" + cell.props.field).prop("checked", true);
											} else {
												$("#head_" + cell.props.field).prop("checked", false);
											}
											if (backData != value[mapping.sourceField]) {
												$("#head_" + cell.props.field).trigger("change");
											}
										}
										break;
									default:
										if (cell.props.field == mapping.targetField) {
											$("#head_" + cell.props.field).val(value[mapping.sourceField]);
										}
										break;
									}
								});
							}
						});
					});
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.info("系统错误，详细信息：" + data);
				}
			});
		},
		button_click : function(event) {
			//自定义按钮处理
			var view = event.data.view;
			var button = event.data.button;
			var head = view.head;
			var detail = view.detail;
			var viewNo = view.viewNo;
			var appName = view.appName;
			//画面数据取得
			var param = view.get_viewData(head, detail, viewNo);
			param.token = view.token;
			param.userDefinedButton = button.id;
			MessageBox.processStart();
			$.ajax({
				url:appName + "/view/process_button/" + viewNo,
				type:"post",
				dataType:"json",
				contentType: "application/json",
				data:JSON.stringify(param),
				success:function(data) {
					if (data.token != undefined && data.token != null && data.token !== "") {
						view.token = data.token;
					}
					if (data.errType == "1") {
						MessageBox.processEnd(data.errMessage);
						return;
					}
					var resultFunc = function() {
						$.each(button.proc, function(n, proc) {
							if ("select" != proc.process) {
								return true;
							}
							$.each(proc.mapping, function(m, mapping) {
								$.each(data.data["" + n], function(p, value){
									if (mapping.targetType == "head") {
										if ($("head_" + mapping.targetField).attr("type") == "checkbox") {
											if (value[mapping.sourceField] == "Y") {
												$("head_" + mapping.targetField).prop("checked", true);
											} else {
												$("head_" + mapping.targetField).prop("checked", false);
											}
										} else {
											$("head_" + mapping.targetField).val(value[mapping.sourceField]);
										}
									} else {
										//明细
										var objTab = $(".tabContent")[0];
										if (p >= $(objTab).find("tbody tr").length - 1) {
											//最后一条没有设定的数据
											return true;
										}
										var tr = $(objTab).find("tbody tr:eq(" + p + ")");
										$.each(detail, function(q, detailTable) {
											$.each(detailTable.process.fields, function(u, detailField) {
												//循环字段设定数据
												var td = $(tr).find("td:eq(" + u + ")");
												if (td == null) {
													//结束本次循环，进入下一循环
													return true;
												}
												switch (detailField.editor) {
												case "label":
													if (detailField.props.field == mapping.targetField) {
														$(td).find("label").html(value[mapping.sourceField]);
													}
													break;
												case "inputEditor":
													if (detailField.props.hidden != undefined && detailField.props.hidden != null && detailField.props.hidden !== "") {
														try {
															var hidden = eval("(" + detailField.props.hidden + ")");
															if (hidden.field != undefined && hidden.field != null && hidden.field !== "") {
																if (hidden.field == mapping.targetField) {
																	$(td).find("input[type='hidden']").val(value[mapping.sourceField]);
																}
															}
														} catch (ex) {
															alert(ex);
														}
													}
												case "dateEditor":
													if (detailField.props.field == mapping.targetField) {
														var backData = $(td).find("input[type='text']").val();
														$(td).find("input[type='text']").val(value[mapping.sourceField]);
														if (backData != value[mapping.sourceField]) {
															$(td).find("input[type='text']").trigger("change");
														}
													}
													break;
												case "selectEditor":
													if (detailField.props.field == mapping.targetField) {
														var backData = $(td).find("select").val();
														$(td).find("select").val(value[mapping.sourceField]);
														if (backData != value[mapping.sourceField]) {
															$(td).find("select").trigger("change");
														}
													}
													break;						
												case "mutilInputEditor":
													if (detailField.props.field == mapping.targetField) {
														var backData = $(td).find("areatext").val();
														$(td).find("areatext").val(value[mapping.sourceField]);
														if (backData != value[mapping.sourceField]) {
															$(td).find("areatext").trigger("change");
														}
													}
													break;	
												case "openEditor":
													if (detailField.props.noField == mapping.targetField) {
														$(td).find("input[type='hidden']").val(value[mapping.sourceField]);
													} else if (detailField.props.nameField == mapping.targetField) {
														$(td).find("input[type='text']").val(value[mapping.sourceField]);
													}
													break;
												case "checkEditor":
													if (detailField.props.field == mapping.targetField) {
														var objCheck = $(td).find("input[type='checkbox']");
														var backData = $(objCheck).prop("checked") ? "Y" : "N";
														if (value[mapping.sourceField] == "Y") {
															$(objCheck).prop("checked", true);
														} else {
															$(objCheck).prop("checked", false);
														}
														if (backData != value[mapping.sourceField]) {
															$(objCheck).trigger("change");
														}
													}
													break;
												default:
													break;
												}
											});
										});
									}
								});								
							});
						});
					};
					var allUpdate = true;
					$.each(button.proc, function(n, proc) {
						if (proc.process == "select") {
							allUpdate = false;
						}
					});
					if (allUpdate == true) {
						MessageBox.processEnd(button.name + "成功！", resultFunc);
					} else {
						resultFunc();
						MessageBox.processClose();
					}
					if (button.save == "yes") {
						view.changeData = [];
					}
				},
				error:function(XMLHttpRequest, data) {
					MessageBox.processEnd("系统错误，详细信息：" + data);
				}
			});
		}
	};
	$.fn.mydetail = function(options, details, appName, viewNo, id, data, attachment, edit) {
		var mylist = new MyDetail(this, options, details, appName, viewNo, id, data, attachment, edit);
		return mylist.init();
	}	
})(jQuery);