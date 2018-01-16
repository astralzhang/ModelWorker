(function($){
	var MyView = function(ele, appName, id, status) {
		//alert(eval(headData));
		this.$element = ele;
		this.appName = appName;
		this.id = id;
		this.status = status;
	};
	MyView.prototype = {
		init : function() {
			//alert(JSON.stringify(this.config.condition));
			var appName = this.appName;
			var ele = this.$element;
			//$(ele).html("");
			var objView = this;
			//获取数据
			var getData = function(id, status) {
				//alert(appName + "/flowProcessor/view");
				MessageBox.processStart();
				$.ajax({
					url:appName + "/flowProcessor/view",
					type:"post",
					dataType:"json",
					data:{
						id:id,
						status:status
					},
					success:function(data) {
						if (data.errType == "1") {
							MessageBox.processEnd(data.errMessage);
							return;
						}
						//alert(data.view);
						//alert(JSON.stringify(data));
						var head = data.head;
						//lert($(this.$element).html());
						//alert(head.rows);
						//生成Head
						var headData = eval("(" + data.data.headData + ")");
						//alert(JSON.stringify(data.data.headData));
						var table = $("<table width='100%'></table>").appendTo(ele);
						for (var i = 1; i <= head.rows; i++) {
							var tr = $("<tr style='height:50px;'></tr>").appendTo(table);
							for (var j = 1; j <= head.cols; j++) {
								var td = null;
								var hasOut = true;
								if (head.margin && head.margin.length > 0) {
									for (var m = 0; m < head.margin.length; m++) {
										var data1 = head.margin[m];
										if (i == data1.start.row && j == data1.start.col) {
											var rows = data1.end.row - data1.start.row + 1;
											var cols = data1.end.col - data1.start.col + 1;
											var props = "";
											if (rows > 1) {
												props += " rowspan=" + rows;
											}
											if (cols > 1) {
												props += " colspan=" + cols;
											}
											td = $("<td" + props + ">&nbsp;</td>");
										} else if (i >= data1.start.row && i <= data1.end.row && j >= data1.start.col && j <= data1.end.col) {
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
								$.each(head.cells, function(n, value) {
									if (value.row == i && value.col == j) {
										fieldType = value.type;
												switch (fieldType) {
										case 'label':
											//alert(value.props.value);
											var showData = "标签";
											if (typeof(value.props.value) != "undefined") {
												if (value.value != "") {
													showData = value.props.value;
												}										
											}
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												var editor = $("<label " + value.props.controlProp + ">" + showData + "</label>").appendTo(td);
											} else {
												var editor = $("<label>" + showData + "</label>").appendTo(td);
											}
											$(td).attr("nowrap", "nowrap");
											return false;
										case 'inputEditor':
											var showData = "";
											var css = "";
											if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
												css = "form-control input-sm fl w15";
											} else {
												css = value.props.css;
											}
											if (headData[value.props.field] != undefined) {
												showData = headData[value.props.field];
											}
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												var editor = $("<input type='text' id='head_" + value.props.field + "' class='" + css + "' value='" + showData + "' " + value.props.controlProp + " readonly>").appendTo(td);
											} else {
												var editor = $("<input type='text' id='head_" + value.props.field + "' class='" + css + "' value='" + showData + "' readonly>").appendTo(td);
											}
											//$(editor).click(eval("(function() {alert('aaa');})"));
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
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												editor = $("<select id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " disabled></select>").appendTo(td);
											} else {
												editor = $("<select id='head_" + value.props.field + "' class='" + css + "' disabled></select>").appendTo(td);
											}
											var objSql = null;
											try {
												objSql = eval("(" + value.props.sql + ")");
												if (objSql.type == "SQL") {
													$.ajax({
														url:appName + "/view/dropdown",
														type:"post",
														dataType:"json",
														data:{
															sql:decodeURIComponent(objSql.data),
															target:objSql.target
														},
														success:function(selectData) {
															if (selectData.errType == "1") {
																MessageBox.info(selectData.errMessage);
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
															$.each(selectData.data, function(n, value) {
																if (valueData == "") {
																	valueData = value.no;
																}
																options += "<option value='" + value.no + "'>" + value.name + "</option>";
															});
															$(editor).html(options);										
															if (headData[value.props.field] != undefined && headData[value.props.field] != null) {
																valueData = headData[value.props.field];
															}
															$(editor).val(valueData);
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
													$.each(objSql.data, function(m, textData) {
														if (valueData == "") {
															valueData = textData.no;
														}
														options += "<option value='" + textData.no + "'>" + textData.name + "</option>";
													});
													$(editor).html(options);										
													if (headData[value.props.field] != undefined && headData[value.props.field] != null && headData[value.props.field] !== "") {
														valueData = headData[value.props.field];
													}
													$(editor).val(valueData);
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
											var objFieldName = null;
											var showData = "";
											if (headData[value.props.nameField] != undefined) {
												showData = headData[value.props.nameField];
											}
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												objFieldName = $('<input id="head_' + value.props.nameField + '" type="text" class="' + css + '" value="' + showData + '" ' + value.props.controlProp + ' readonly>').appendTo(td);
											} else {
												objFieldName = $('<input id="head_' + value.props.nameField + '" type="text" class="' + css + '" value="' + showData + '" readonly>').appendTo(td);
											}
											var objFieldId = $('<input type="hidden" id="head_' + value.props.noField + '">').appendTo(td);
											break;
											return false;
										case 'checkEditor':
											//checkBox
											var css = "";
											if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
												css = "";
											} else {
												css = value.props.css;
											}
											var checked = "";
											if(headData[value.props.field] != undefined) {
												if ("Y" == headData[value.props.field]) {
													checked = "checked";
												}
											}
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												var editor = $("<input type='checkbox' id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " " + checked + " disabled>").appendTo(td);
											} else {
												var editor = $("<input type='checkbox' id='head_" + value.props.field + "' class='" + css + "' " + checked + " disabled>").appendTo(td);
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
											var showData = "";
											if (headData[value.props.field] != undefined) {
												showData = headData[value.props.field];
											}
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												var editor = $("<textarea id='head_" + value.props.field + "' class='" + css + "' " + value.props.controlProp + " readonly>" + showData + "</textarea>").appendTo(td);
											} else {
												var editor = $("<textarea id='head_" + value.props.field + "' class='" + css + "' readonly>" + showData + "</textarea>").appendTo(td);
											}
											return false;
										case 'dateEditor':
											//日期控件
											var css = "";
											if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
												css = "form-control input-sm";
											} else {
												css = value.props.css;
											}
											var showData = "";
											if (headData[value.props.field] != undefined) {
												showData = headData[value.props.field];
											}
											var objDiv = $('<div class="input-group date fl w15"></div>').appendTo(td);
											var editor = null;
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												editor = $('<input type="text" class="' + css + '" id="head_' + value.props.field + '" value="' + showData + '" placeholder="请选择日期" readonly ' + value.props.controlProp + '>').appendTo(objDiv);
											} else {
												editor = $('<input type="text" class="' + css + '" id="head_' + value.props.field + '" value="' + showData + '" placeholder="请选择日期" readonly>').appendTo(objDiv);
											}
						                    var objSpan = $('<span class="input-group-addon"></span>').appendTo(objDiv);
						                    $('<i class="fa fa-calendar"></i>').appendTo(objSpan);
						                    return false;
										case "image":
											//图像
											var css = "";
											if (value.props.css == undefined || value.props.css == null || value.props.css === "") {
												css = "";
											} else {
												css = value.props.css;
											}
											if (value.props.controlProp != undefined && value.props.controlProp != null && value.props.controlProp !== "") {
												$('<img class="' + css + '" id="head_' + value.props.field + '"  ' + value.props.controlProp + '>').appendTo(td);
											} else {
												$('<img class="' + css + '" id="head_' + value.props.field + '">').appendTo(td);
											}
											return false;
										default:
											return false;
										}
									}
								});
							}
						}
						//做成Detail数据
						var detail = data.detail;
						//alert(JSON.stringify(detail));
						if (detail.length > 0) {
							var detailDiv = $('<div class="box box-warning cost-info"></div>').appendTo(ele);
							var objUl = $("<ul class='tab'></ul>").appendTo($(detailDiv));							
							$.each(data.detail, function(m, tab) {
								//alert(JSON.stringify(tab));
								$("<li>" + tab.label + "</li>").appendTo(objUl);
							});
							$(objUl).find("li:eq(0)").attr("class", "cur");
							//新增明细行
							var addRow = function(tr, fieldConfig, detailData) {
								$.each(fieldConfig, function(n, field) {
									//alert(JSON.stringify(field));
									switch (field.editor) {
									case 'label':
										var showData = "";
										if (typeof(field.props.value) != "undefined") {
											showData = field.props.value;										
										}
										if (detailData != null) {
											showData = detailData[field.props.field];
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
										if (detailData != null) {
											if (detailData[field.props.field] != undefined && detailData[field.props.field] != null && detailData[field.props.field] !== "") {
												valueData = detailData[field.props.field];
											}
										}
										var td = $("<td></td>").appendTo($(tr));
										var editor = null;
										if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
											editor = $("<input type='text' class='" + css + "' " + field.props.controlProp + " value='" + valueData + "' readonly>").appendTo(td);
										} else {
											editor = $("<input type='text' class='" + css + "' value='" + valueData + "' readonly>").appendTo(td);
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
										var td = $("<td></td>").appendTo($(tr));
										if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
											editor = $("<select class='" + css + "' " + field.props.controlProp + " disabled></select>").appendTo(td);
										} else {
											editor = $("<select class='" + css + "' " + disableType + " disabled></select>").appendTo(td);
										}
										var objSql = null;
										try {
											objSql = eval("(" + field.props.sql + ")");
											if (objSql.type == "SQL") {
												$.ajax({
													url:appName + "/view/dropdown",
													type:"post",
													dataType:"json",
													data:{
														sql:decodeURIComponent(objSql.data),
														target:objSql.target
													},
													success:function(selectData) {
														if (selectData.errType == "1") {
															MessageBox.info(selectData.errMessage);
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
														$.each(selectData.data, function(n, value) {
															if (valueData === "") {
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
															if (detailData[field.props.field] != undefined && detailData[field.props.field] != null) {
																valueData = detailData[field.props.field];
															}
															$(editor).val(valueData);
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
												$.each(objSql.data, function(m, textData) {
													if (valueData == "") {
														valueData = textData.no;
													}
													options += "<option value='" + textData.no + "'>" + textData.name + "</option>";
												});
												$(editor).html(options);
												if (detailData != null) {
													if (detailData[field.props.field] != undefined && detailData[field.props.field] != null && detailData[field.props.field] !== "") {
														valueData = detailData[field.props.field];
													}
												}
												$(editor).val(valueData);
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
										var valueData2 = "";
										if (detailData != null) {
											if (detailData[field.props.nameField] != undefined && detailData[field.props.nameField] != null && detailData[field.props.nameField] !== "") {
												valueData2 = detailData[field.props.nameField];
											}
										}
										var td = $("<td nowrap></td>").appendTo($(tr));
										var objFieldName = null;
										if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
											objFieldName = $('<input type="text" class="' + css + '" ' + field.props.controlProp + ' value="' + valueData2 + '" readonly>').appendTo(td);
										} else {
											objFieldName = $('<input type="text" class="' + css + '" value="' + valueData2 + '" readonly>').appendTo(td);
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
										var td = $("<td></td>").appendTo($(tr));
										if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
											editor = $("<input type='checkbox' class='" + css + "' " + field.props.controlProp + " readonly>").appendTo(td);
										} else {
											editor = $("<input type='checkbox' class='" + css + "' readonly>").appendTo(td);
										}
										var valueData = "";
										if (detailData != null) {
											if (detailData[field.props.value] != undefined && detailData[field.props.value] != null && detailData[field.props.value] !== "") {
												valueData = detailData[field.props.value];
											}
										}
										if (valueData == "Y") {
											$(editor).prop("checked", true);
										} else {
											$(editor).prop("checked", false);
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
										if (detailData != null) {
											if (detailData[field.props.field] != undefined && detailData[field.props.field] != null && detailData[field.props.field] !== "") {
												valueData = detailData[field.props.field];
											}
										}
										var td = $("<td></td>").appendTo($(tr));
										var editor = null;
										if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
											editor = $("<textarea class='" + css + "' " + field.props.controlProp + " readonly>" + valueData + "</textarea>").appendTo(td);
										} else {
											editor = $("<textarea class='" + css + "' readonly>" + valueData + "</textarea>").appendTo(td);
										}
										return false;
									case 'dateEditor':
										//日期控件
										var css = "";
										if (field.props.css == undefined || field.props.css == null || field.props.css === "") {
											css = "form-control input-sm fl w15";
										} else {
											css = field.props.css;
										}
										var valueData = "";
										if (detailData != null) {
											if (detailData[field.props.field] != undefined && detailData[field.props.field] != null && detailData[field.props.field] !== "") {
												valueData = detailData[field.props.field];
											}
										}
										var td = $("<td></td>").appendTo($(tr));
										if (field.props.controlProp != undefined && field.props.controlProp != null && field.props.controlProp !== "") {
											var editor = $("<input type='text' class='" + css + "' " + field.props.controlProp + " value='" + valueData + "' disabled>").appendTo(td);
										} else {
											var editor = $("<input type='text' class='" + css + "' value='" + valueData + "' disabled>").appendTo(td);
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
											if (detailData[field.props.field] != undefined && detailData[field.props.field] != null && detailData[field.props.field] !== "") {
												objImg = $('<img class="' + css + '" ' + field.props.controlProp + ' src="' + detailData[field.props.field] + '">').appendTo(td);
											} else {
												var valueData = "";
												if (field.props.value != undefined && field.props.value != null && field.props.value !== "") {
													try {
														valueData = eval("(" + field.props.value + ")");
													} catch (ex) {
														alert(ex);
													}
												}
												objImg = $('<img class="' + css + '" ' + field.props.controlProp + ' src="' + valueData + '">').appendTo(td);
											}
										} else {
											if (detailData[field.props.field] != undefined && detailData[field.props.field] != null && detailData[field.props.field] !== "") {
												objImg = $('<img class="' + css + '" src="' + detailData[field.props.field] + '">').appendTo(td);
											} else {
												var valueData = "";
												if (field.props.value != undefined && field.props.value != null && field.props.value != "") {
													try {
														valueData = eval("(" + field.props.value + ")");
													} catch (ex) {
														alert(ex);
													}
												}
												objImg = $('<img class="' + css + '" src="' + valueData + '">').appendTo(td);
											}									
										}
										break;
									default:
										break;
									}
								});
							};
							$.each(detail, function(n, tab) {
								var objDiv = $("<div class='tabContent' style='overflow-x:auto;overflow-y:auto;position:relative;'></div>").appendTo($(detailDiv));
								var objTable = $("<table class='table table-hover'></table>").appendTo(objDiv);
								var objTHead = $("<thead></thead>").appendTo($(objTable));
								var objTBody = $("<tbody></tbody>").appendTo($(objTable));
								var objTh = $("<tr></tr>").appendTo($(objTHead));
								$.each(tab.process.fields, function(n, value) {
									$("<th>" + value.label + "</th>").appendTo(objTh);
								});
								$("<th></th>").appendTo(objTh);
								//创建一行数据
								var data1 = eval("(" + data.data[tab.table] + ")");
								//alert("data1=" + JSON.stringify(data1));
								if (data1 != undefined && data1.length > 0) {
									$.each(data1, function(n,value) {
										var objTd = $("<tr></tr>").appendTo($(objTBody));
										addRow(objTd, tab.process.fields, value);
									});					
								} else {
									var objTd = $("<tr></tr>").appendTo($(objTBody));
									addRow(objTd, tab.process.fields, null);
								}
								//$(objTable).freezeHeader();
							});
							$.each($(detailDiv).find("div[class='tabContent']"),function(){$(this).hide();});
							//页签切换
							if ($(objUl).find("li")) {
								var index = $(objUl).find("li[class='cur']").index();
								$(detailDiv).find("div[class='tabContent']").eq(index).show();
								$(objUl).find("li").unbind("click");
						        $(objUl).find("li").bind("click", function(){
							        $(objUl).find("li").eq($(this).index()).addClass("cur").siblings().removeClass('cur');
									$(detailDiv).find("div[class='tabContent']").hide();
									$(detailDiv).find("div[class='tabContent']").eq($(this).index()).show();
							    });
							}
						}
						//附件数据生成
						if (data.attachment != undefined) {
							var tbody = $("#myAttachmentModal table tbody");
							var tdata = "";
							$.each(data.attachment, function(n, atta) {
								tdata += "<tr>";
								tdata += "<td><input type='hidden' value='" + atta.id + "'><a href='#'>" + atta.fileName + "</a></td>";
								tdata += "<td>" + atta.fileExtend + "</td>";
								tdata += "</tr>";
							});
							tbody.html(tdata);
							$("#myAttachmentModal table tbody a").unbind("click");
							$("#myAttachmentModal table tbody a").bind("click", {appName:appName,viewNo:data.viewNo}, objView.viewFile);
							if ($("#myAttachmentModal table tbody tr").length > 0) {
								$("#attachmentShow").html("");
								$("<button class='btn'><span class='glyphicon glyphicon-file'></span>附件</button>").click(function() {
									var modal = $('#myAttachmentModal').modal({backdrop: 'static', keyboard: false});
							    	modal.show();
								}).appendTo($("#attachmentShow"));
							}
						}
						//设定各控件状态
						if (status != "" && status != "0") {
							if ($("#processContent2")[0]) {
								$("#processContent2").val(data.content);
							} else {
								$("<img src='" + data.content + "' height='100'>").appendTo($("#processContent3"));
							}
							$("#btn-view-processOk").attr("disabled", true);
							$("#btn-view-processDisagree").attr("disabled", true);
						} else {
							$("#btn-view-processOk").attr("disabled", false);
							$("#btn-view-processDisagree").attr("disabled", false);
						}
						MessageBox.processClose();
						var modal = $('#myModal2').modal({backdrop: 'static', keyboard: false});
						modal.show();						
					},
					error:function(XMLHttpRequest, data) {
						MessageBox.processEnd("系统错误，详细信息：" + data);
					}
				});
			}
			getData(this.id, this.status);
			return this;
		},
		viewFile : function(event) {
			var appName = event.data.appName;
			var viewNo = event.data.viewNo;
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
		}
	};
	$.fn.myview = function(appName, id, status) {
		var myview = new MyView(this, appName, id, status);
		return myview.init();
	}	
})(jQuery);