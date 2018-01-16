(function($){
var myflow = $.myflow;

$.extend(true, myflow.editors, {
	//只读输入框
	idEditor:function() {
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			var idTxt = $('<input readonly="readonly" style="width:100%;"/>').appendTo('#' + _div);
			$(idTxt).val(props[_k].value);
			if ($(idTxt).val() == "") {
				$(idTxt).val(src.getId());
			}
			$(idTxt).change(function(){
				props[_k].value = $(this).val();
			});			
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		}
	},
	//输入框
	inputEditor : function(){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			$('<input style="width:100%;"/>').val(props[_k].value).change(function(){
				alert($(this).val());
				props[_k].value = $(this).val();
			}).appendTo('#'+_div);
			
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		}
	},
	//日期控件
	dateEditor:function() {
		var _props,_k,_div,_src,_r;
		var txtDate;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			txtDate = $('<input style="width:100%;" placeholder="选择时间" readonly/>').appendTo('#' + _div);
			$(txtDate).change(function(){
				props[_k].value = $(this).val();
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
	//条件编辑
	conditionEditor:function(){
		var _props, _k, _div, _src, _r;
		var hid;
		this.init = function(props, k, div, src, r) {
			_props=props; _k=k; _div=div; _src=src; _r=r;
			hid = $('<input type="hidden"/>').change(function(){
				props[_k].value = $(this).val();
			}).appendTo('#' + _div);
			if (typeof props[_k].value === 'string') {
				hid.val(props[_k].value);
			} else {
				hid.val(props[_k].value);
			}			
			var btn = $('<input type="button" value="条件" style="width:100%;"/>').click(function(){
				hid.val(hid.val());
				inputCondition(hid);
			}).appendTo('#'+_div);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(hid).val();
			});
		}
	},
	//审核人编辑
	userEditor:function(arg) {
		var _props,_k,_div,_src,_r;
		var txt;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			var span = $('<span style="width:100%;" />').appendTo('#' + _div);
			showConditionTxt = $('<input type="text" readonly="readonly" style="width:80%;"/>').appendTo($(span));
			txt = $('<input type="hidden" readonly="readonly"/>').change(function(){
				props[_k].value = $(this).val().replaceAll("\n","<br>");
				var data = $(this).val() ? $(this).val().replaceAll("<br>", "\n") : "";
				if (data != "") {
					data = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data><processor>" + data + "</processor></data>";
					var xmlData = $.parseXML(data);
					var type = $(xmlData).find("processor").find("type").text();
					if (type == "C") {
						$(showConditionTxt).val($(xmlData).find("processor").find("condition").text());
					} else if (type == "S") {
						$(showConditionTxt).val($(xmlData).find("processor").find("sql").text());
					} else {
						$(showConditionTxt).val("");
					}					
				}
			});			
			var btn = $('<input type="button" value="..." style="width:20%;" />');			
			$(txt).appendTo($(span));
			$(showConditionTxt).appendTo($(span));
			$(btn).appendTo($(span));
			$(txt).val(props[_k] ? props[_k].value : "");
			$(txt).trigger("change");
			$(btn).click(function() {
				//按钮点击事件
				inputUser(txt);
			});
		};
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(txt).val();
			});
		};
	},
	sqlEditor : function() {
		var _props, _k, _div, _src, _r;
		var hid;
		this.init = function(props, k, div, src, r) {
			_props=props; _k=k; _div=div; _src=src; _r=r;
			hid = $('<input type="hidden"/>').change(function(){
				props[_k].value = $(this).val();
			}).appendTo('#' + _div);
			if (typeof props[_k].value === 'string') {
				hid.val(props[_k].value);
			} else {
				hid.val(props[_k].value);
			}	
			var selectSql = function() {
				var index = -1;
				var bError = false;
				$.each($("#sqlTable tbody input[type='checkbox']"), function() {
					if ($(this).prop("checked")) {
						if (index != -1) {
							MessageBox.info("只能选择一条数据编辑!");
							$(this).prop("checked", false);
							bError = true;
							return false;
						} else {
							index = $(this).closest("tr").index();
						}
					}
				});
				if (bError) {
					return;
				}
				if (index == -1) {
					$("#targetDatabase").val("W");
					$("#sqlType").val("S");
					$("#processSection").val("BeforeAudit");
					$("#sqlProc").val("");
					return;
				}
				$("#sqlIndex").val(index);
				var tr = $("#sqlTable tbody tr:eq(" + index + ")");
				var td = $(tr).find("td:eq(1)");
				var target = $(td).find("input").val();
				$("#targetDatabase").val(target);
				td = $(tr).find("td:eq(2)");
				var sqlType = $(td).find("input").val();
				$("#sqlType").val(sqlType);
				td = $(tr).find("td:eq(3)");
				var processSection = $(td).find("input").val();
				$("#processSection").val(processSection);
				td = $(tr).find("td:eq(4)");
				$("#sqlProc").val(decodeURIComponent($(td).html()));
			};
			var btn = $('<input type="button" value="设定" style="width:100%;"/>').click(function(){
				$("#sqlTable tbody").html("");
				if (hid.val() != "") {
					var data = decodeURIComponent(hid.val());
					var obj = eval("(" + data + ")");
					var tdata = "";
					$.each(obj, function(n, data) {
						tdata += "<tr>";
						tdata += "<td><input type='checkbox'></td>";
						tdata += "<td>";
						if (data.target == "W") {
							tdata += "工作流";
							tdata += "<input type='hidden' value='W'>";
						} else {
							tdata += "业务";
							tdata += "<input type='hidden' value='B'>";
						}
						tdata += "</td>";
						tdata += "<td>";
						if (data.sqlType == "S") {
							tdata += "SQL文";
							tdata += "<input type='hidden' value='S'>";
						} else {
							tdata += "存储过程";
							tdata += "<input type='hidden' value='P'>";
						}
						tdata += "</td>";
						tdata += "<td>";
						switch(data.section) {
						case "BeforeAudit":
							tdata += "审核前";
							tdata += "<input type='hidden' value='BeforeAudit'>";
							break;
						case "AfterAudit":
							tdata += "审核后";
							tdata += "<input type='hidden' value='AfterAudit'>";
							break;
						case "BeforeCancelAudit":
							tdata += "撤审前";
							tdata += "<input type='hidden' value='BeforeCancelAudit'>";
							break;
						case "AfterCancelAudit":
							tdata += "撤审后";
							tdata += "<input type='hidden' value='AfterCancelAudit'>";
							break;
						}
						tdata += "</td>";
						tdata += "<td style='word-wrap:break-word;word-break:break-all;'>" + encodeURIComponent(data.sql) + "</td>";
						tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
						tdata += "</tr>";
					});
					$("#sqlTable tbody").html(tdata);
					$("#sqlTable tbody input[type='checkbox']").unbind("click");
					$("#sqlTable tbody input[type='checkbox']").bind("click", selectSql);
				}				
				var modal = $('#mySqlModal').modal({backdrop: 'static', keyboard: false});
		    	modal.show();
			}).appendTo('#'+_div);			
			$("#btnAddSql").click(function() {
				if ($("#sqlProc").val() == null || $("#sqlProc").val() == "") {
					MessageBox.info("请输入处理SQL！");
					return;
				}
				var tdata = "<tr>";
				tdata += "<td><input type='checkbox'></td>";
				tdata += "<td>";
				if ($("#targetDatabase").val() == "W") {
					tdata += "工作流";
					tdata += "<input type='hidden' value='W'>";
				} else {
					tdata += "业务";
					tdata += "<input type='hidden' value='B'>";
				}
				tdata += "</td>";
				tdata += "<td>";
				if ($("#sqlType").val() == "S") {
					tdata += "SQL文";
					tdata += "<input type='hidden' value='S'>";
				} else {
					tdata += "存储过程";
					tdata += "<input type='hidden' value='P'>";
				}
				tdata += "</td>";
				tdata += "<td>";
				switch($("#processSection").val()) {
				case "BeforeAudit":
					tdata += "审核前";
					tdata += "<input type='hidden' value='BeforeAudit'>";
					break;
				case "AfterAudit":
					tdata += "审核后";
					tdata += "<input type='hidden' value='AfterAudit'>";
					break;
				case "BeforeCancelAudit":
					tdata += "撤审前";
					tdata += "<input type='hidden' value='BeforeCancelAudit'>";
					break;
				case "AfterCancelAudit":
					tdata += "撤审后";
					tdata += "<input type='hidden' value='AfterCancelAudit'>";
					break;
				}
				tdata += "</td>";
				tdata += "<td style='word-wrap:break-word;word-break:break-all;'>" + encodeURIComponent($("#sqlProc").val()) + "</td>";
				tdata += "<td><input type='button' class='btn btn-default' value='删除'></td>";
				tdata += "</tr>";
				$("#sqlTable tbody").append(tdata);
				$("#targetDatabase").val("W");
				$("#sqlType").val("S");
				$("#processSection").val("BeforeAudit");
				$("#sqlProc").val("");
				$("#sqlTable tbody input[type='checkbox']").unbind("click");
				$("#sqlTable tbody input[type='checkbox']").bind("click", selectSql);
			});			
			$("#btn-flow-process-confirm").click(function () {
				var data = [];
				$.each($("#sqlTable tbody tr"), function(n, tr) {
					var td = $(tr).find("td:eq(1)");
					var target = $(td).find("input").val();
					td = $(tr).find("td:eq(2)");
					var sqlType = $(td).find("input").val();
					td = $(tr).find("td:eq(3)");
					var processSection = $(td).find("input").val();
					td = $(tr).find("td:eq(4)");
					var sql = decodeURIComponent($(td).html());
					data.push({target:target,sqlType:sqlType,section:processSection,sql:sql});
				});
				var sData = encodeURIComponent(JSON.stringify(data));
				$(hid).val(sData);
				$(hid).trigger("change");
				$('#mySqlModal').modal("hide");
			});
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(hid).val();
			});
		}
	},
	selectEditor : function(arg){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			//props：当前属性
			//k:当前对象
			//src:源
			_props=props; _k=k; _div=div; _src=src; _r=r;

			var sle = $('<select  style="width:100%;"/>').val(props[_k].value).change(function(){
				props[_k].value = $(this).val();
			}).appendTo('#'+_div);
			
			if(typeof arg === 'string'){
				$.ajax({
				   type: "GET",
				   url: arg,
				   success: function(data){
					  var opts = eval(data);
					 if(opts && opts.length){
						for(var idx=0; idx<opts.length; idx++){
							sle.append('<option value="'+opts[idx].value+'">'+opts[idx].name+'</option>');
						}
						sle.val(_props[_k].value);
					 }
				   }
				});
			}else {
				for(var idx=0; idx<arg.length; idx++){
					sle.append('<option value="'+arg[idx].value+'">'+arg[idx].name+'</option>');
				}
				sle.val(_props[_k].value);
			}
			
			$('#'+_div).data('editor', this);
		};
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		};
	}
});

})(jQuery);