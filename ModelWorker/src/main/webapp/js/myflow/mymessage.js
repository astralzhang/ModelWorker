(function($){
	var MyMessage = function(ele, con) {
		this.$element = ele;
		this.defaults = [];
		this.config = $.extend([], this.defaults, con);
	};
	MyMessage.prototype = {
		init : function() {
			this.set_user_type();
			var tdata = "";
			$.each(this.config, function(n, value) {
				tdata += "<tr>";
				tdata += "<td><input type='checkbox'></td>";
				var voucherStatus = value.status;
				var userType = value.type;
				var voucherStatusName = "";
				var userTypeName = "";
				switch (voucherStatus) {
				case "Audited":
					voucherStatusName = "已审核的单据";
					switch (userType) {
					case "Cur":
						userTypeName = "未审核人";
						break;
					case "Pre":
						userTypeName = "已审核人";
						break;
					default:
						break;
					}
					break;
				case "Disagree":
					voucherStatusName = "被退回的单据";
					switch (userType) {
					case "Cur":
						userTypeName = "被退回人";
						break;
					case "Pre":
						userTypeName = "退回人";
						break;
					default:
						break;
					}
					break;
				case "Submited":
					voucherStatusName = "提交的单据";
					switch (userType) {
					case "Pre":
						userTypeName = "提交人";
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
				var messageType = value.messageType;
				var messageTypeName = "";
				if (messageType == "Script") {
					messageTypeName = "脚本函数";
				} else {
					messageTypeName = "简单模板";
				}
				var messageTemplate = decodeURIComponent(value.message);				
				tdata += "<td><input type='hidden' value='" + voucherStatus + "'><span>" + voucherStatusName + "</span></td>";
				tdata += "<td><input type='hidden' value='" + userType + "'><span>" + userTypeName + "</span></td>";
				tdata += "<td><input type='hidden' value='" + messageType + "'><span>" + messageTypeName + "</span></td>";
				tdata += "<td>" + messageTemplate + "</td>";
				tdata += "<td><input type='button' value='删除'></td>";
				tdata += "</tr>";
			});
			$("#messageTable tbody").html(tdata);
			$("#messageTable tbody input[type='checkbox']").unbind("click");
			$("#messageTable tbody input[type='checkbox']").bind("click", {obj:this}, this.check_message);
			$("#messageTable tbody input[type='button']").unbind("click");
			$("#messageTable tbody input[type='button']").bind("click", {obj:this}, this.delete_message);
			
			this.$element.unbind("click");
			this.$element.bind("click", {obj:this}, this.show);
	    	$("#btn-flow-message-confirm").unbind("click");
	    	$("#btn-flow-message-confirm").bind("click", {obj:this}, this.set_message);
	    	$("#VoucherStatus").unbind("change");
	    	$("#VoucherStatus").bind("change", {obj:this}, this.change_voucher_status);
	    	$("#AddMessageTemplate").unbind("click");
	    	$("#AddMessageTemplate").bind("click", {obj:this}, this.add_message_template);
	    	
	    	$("#VoucherStatus").val("Audited");
			$("#VoucherStatus").trigger("change");
			$("#MessageTemplate").val("");
			$("#RowNo").val("");
			return this;
		},
		show : function(event) {
			var obj = event.data.obj;
			obj.init();
			var modal = $('#myMessageModal').modal({backdrop: 'static', keyboard: false});
	    	modal.show();
		},
		set_user_type : function() {
			var voucherStatus = $("#VoucherStatus").val();
			var options = "";
			switch (voucherStatus) {
			case "Audited":
				options += "<option value='Cur'>未审核人</option><option value='Pre'>已审核人</option>";
				break;
			case "Disagree":
				options += "<option value='Cur'>被退回人</option><option value='Pre'>退回人</option>";
				break;
			case "Submited":
				options += "<option value='Pre'>提交人</option>";
				break;
			default:
				break;
			}
			$("#UserType").html(options);
		},
		add_message_template : function(event) {
			var obj = event.data.obj;
			if ($("#MessageTemplate").val() == "") {
				alert("请输入消息模板！");
				return;
			}
			var voucherStatus = $("#VoucherStatus").val();
			var voucherStatusName = $("#VoucherStatus").find("option[value='" + voucherStatus + "']").text();
			var userType = $("#UserType").val();
			var userTypeName = $("#UserType").find("option[value='" + userType + "']").text();
			var messageTemplate = $("#MessageTemplate").val();
			var messageType = $("#MessageType").val();
			var messageTypeName = $("#MessageType").find("option[value='" + messageType + "']").text();
			if ($("#RowNo").val() == "") {
				var tdata = "<tr>";
				tdata += "<td><input type='checkbox'></td>";
				tdata += "<td><input type='hidden' value='" + voucherStatus + "'><span>" + voucherStatusName + "</span></td>";
				tdata += "<td><input type='hidden' value='" + userType + "'><span>" + userTypeName + "</span></td>";
				tdata += "<td><input type='hidden' value='" + messageType + "'><span>" + messageTypeName + "</span></td>";
				tdata += "<td>" + messageTemplate + "</td>";
				tdata += "<td><input type='button' value='删除'></td>";
				tdata += "</tr>";
				$("#messageTable tbody").append(tdata);
			} else {
				var tdata = "";
				tdata += "<td><input type='checkbox'></td>";
				tdata += "<td><input type='hidden' value='" + voucherStatus + "'><span>" + voucherStatusName + "</span></td>";
				tdata += "<td><input type='hidden' value='" + userType + "'><span>" + userTypeName + "</span></td>";
				tdata += "<td><input type='hidden' value='" + messageType + "'><span>" + messageTypeName + "</span></td>";
				tdata += "<td>" + messageTemplate + "</td>";
				tdata += "<td><input type='button' value='删除'></td>";
				$("#messageTable tbody tr:eq(" + $("#RowNo").val() + ")").html(tdata);
				$("#RowNo").val("");
			}
			$("#VoucherStatus").val("Audited");
			$("#VoucherStatus").trigger("change");
			$("#MessageTemplate").val("");
			$("#MessageType").val("Simple");
			$("#messageTable tbody input[type='checkbox']").unbind("click");
			$("#messageTable tbody input[type='checkbox']").bind("click", {obj:obj}, obj.check_message);
			$("#messageTable tbody input[type='button']").unbind("click");
			$("#messageTable tbody input[type='button']").bind("click", {obj:obj}, obj.delete_message);
		},
		set_row_no : function() {
			var arrRow = [];
			$.each($("#messageTable tbody input[type='checkbox']"), function() {
				if ($(this).prop("checked")) {
					arrRow.push($(this).closest("tr").index());
				}
			});
			if (arrRow.length > 1) {
				alert("只能选择一个消息模板进行编辑！");
				return;
			}
			if (arrRow.length == 0) {
				$("#RowNo").val("");
				$("#VoucherStatus").val("Audited");
				$("#VoucherStatus").trigger("change");
				$("#MessageTemplate").val("");
				$("#MessageType").val("Simple");
				return;
			}
			$("#RowNo").val(arrRow[0]);
		},
		check_message : function(event) {
			var obj = event.data.obj;
			obj.set_row_no();
			if ($("#RowNo").val() == "") {
				return;
			}
			var tr = $("#messageTable tbody tr:eq(" + $("#RowNo").val() + ")");
			var voucherStatus = $(tr).find("td:eq(1)").find("input[type='hidden']").val();
			var userType = $(tr).find("td:eq(2)").find("input[type='hidden']").val();
			var messageType = $(tr).find("td:eq(3)").find("input[type='hidden']").val();
			var messageTemplate = $(tr).find("td:eq(4)").html();
			$("#VoucherStatus").val(voucherStatus);
			$("#VoucherStatus").trigger("change");
			$("#UserType").val(userType);
			$("#MessageType").val(messageType);
			$("#MessageTemplate").val(messageTemplate);
		},
		delete_message : function(event) {
			var obj = event.data.obj;
			var tr = $(this).closest("tr");
			$(tr).remove();
			obj.set_row_no();
		},
		set_message : function(event) {
			var obj = event.data.obj;
			obj.config = [];
			$.each($("#messageTable tbody tr"), function() {
				var voucherStatus = $(this).find("td:eq(1)").find("input[type='hidden']").val();
				var userType = $(this).find("td:eq(2)").find("input[type='hidden']").val();
				var messageType = $(this).find("td:eq(3)").find("input[type='hidden']").val();
				var messageTemplate = encodeURIComponent($(this).find("td:eq(4)").html());
				obj.config.push({status:voucherStatus,type:userType,messageType:messageType,message:messageTemplate});
			});
			$("#myMessageModal").modal('hide');
		},
		change_voucher_status : function(event) {
			var obj = event.data.obj;
			obj.set_user_type();
		},
		get_message_template : function() {
			return this.config;
		}
	};
	$.fn.mymessage = function(config) {
		var mymessage = new MyMessage(this, config);
		return mymessage.init();
	}	
})(jQuery);