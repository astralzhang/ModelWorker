
var tableLanguage = {
    "sProcessing": "处理中...",
    "sLengthMenu": "显示 _MENU_ 项结果",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上一页",
        "sNext": "下一页",
        "sLast": "末页"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
}

String.prototype.replaceAll = function(s1, s2) {      
    return this.replace(new RegExp(s1, "gm"), s2); //g全局     
}

$(document).ready(function() {
	/*table 中的checkbox 的全选功能  */
	$("#allCheck").click(function() {

		var selected = $(this).prop("checked"); 
		$(".dataTable tbody input[type='checkbox']").prop("checked", selected);
		
		var rows = $('.dataTable tbody tr');
		$('.dataTable tbody tr').each(function(){
			if($(this).find(":checkbox").prop("checked") == true){
				$(this).addClass('selected');
			}
			
			if($(this).find(":checkbox").prop("checked") == false){
				$(this).removeClass('selected');
			}  
		});
	});
});

var MessageBox = {
		info : function(message, callback) {
			$("#infoModal .modal-body").html(message);
			$("#infoModal .modal-footer .btn").unbind("click");
			$("#infoModal .modal-footer .btn").bind("click", function() {
				if (callback) {
					callback();
				}
			});
			$("#infoModal .modal-footer .btn").removeAttr("disabled");
			var modal = $("#infoModal").modal({backdrop: 'static', keyboard: false});
			modal.show();
		},
		error : function(message, callback) {
			$("#errorModal .modal-body").text(message);
			$("#errorModal .modal-footer .btn").unbind("click");
			$("#errorModal .modal-footer .btn").bind("click", function() {
				if (callback) {
					callback();
				}
			});
			var modal = $("#errorModal").modal({backdrop: 'static', keyboard: false});
			modal.show();
		},
		confirm : function(message, callbackYes, callbackNo) {
			$("#confirmModal .modal-body").html(message);
			$("#confirmModal .modal-footer .btn-default").unbind("click");
			$("#confirmModal .modal-footer .btn-default").bind("click", function() {
				if (callbackNo) {
					callbackNo();
				}
			});
			$("#confirmModal .modal-footer .btn-primary").unbind("click");
			$("#confirmModal .modal-footer .btn-primary").bind("click", function() {
				if (callbackYes) {
					callbackYes();
				}
			});
			var modal = $("#confirmModal").modal({backdrop: 'static', keyboard: false});
			modal.show();
		},
		processStart : function() {
			$("#processBar .modal-body").html("系统处理中，请等待！");
			$("#processBar :button").hide();
			var modal = $("#processBar").modal({backdrop: 'static', keyboard: false});
			modal.show();
		},
		processEnd : function(message, callback) {
			$("#processBar :button").show();
			$("#processBar .modal-body").html(message);
			$("#processBar .modal-footer .btn").unbind("click");
			$("#processBar .modal-footer .btn").bind("click", function() {
				if (callback) {
					callback();
				}
			});
			$("#processBar .modal-footer .btn").removeAttr("disabled");
		},
		processClose : function() {
			$("#processBar").modal('hide');
		}
	}

$.fn.reset = function() {      
    this.validate().resetForm(); 
    this.find(".error").removeClass("error");
}

$.fn.serializeJson=function(){  
    var serializeObj={};  
    $(this.serializeArray()).each(function(){  
        serializeObj[this.name]=this.value;  
    });  
    return serializeObj;  
};  


//验证数值输入（可带小数）
function checkDecimal(param1, param2, value) {
 if (new RegExp("^-?\\d{1," + param1 + "}(\\.\\d{1," + param2 + "})?$").test(value)) {
     return true;
 } else {
     return false;
 }
}

var Formatter = {
		formatDate : function(date) {
			if (date != null && date != '' && date != 'undefined' && date != 'null') {
				return new Date(date).format("yyyy-MM-dd");
			}
			return '';
		},
		formatDateTime : function(date) {
			if (date != null && date != '' && date != 'undefined' && date != 'null') {
				return new Date(date).format("yyyy-MM-dd hh:mm:ss");
			}
			return '';
		},
		
		formatNumber : function(num) {
			if (num != null && num != '' && num != 'undefined' && num != 'null') {
				return new Number(num).formatMoney(2, ".", ",");
			}
			return "";
		},
		formatMoney : function(num) {
			if (num != null && num != '' && num != 'undefined' && num != 'null') {
				return new Number(num).formatMoney(0, ".", ",");
			}
			return num;
		}
	};

//$.fn.serializeObject = function() {
//	var o = {};
//	var a = this.serializeArray();
//	$.each(a, function() {
//		if (o[this.name] !== undefined) {
//			if (!o[this.name].push) {
//				o[this.name] = [ o[this.name] ];
//			}
//			o[this.name].push(this.value || '');
//		} else {
//			o[this.name] = this.value || '';
//		}
//	});
//	return o;
//};
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
Date.prototype.format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

Number.prototype.formatMoney = function(c, d, t) {
	var n = this, c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "."
			: d, t = t == undefined ? "," : t, s = n < 0 ? "-" : "", i = parseInt(n = Math
			.abs(+n || 0).toFixed(c))
			+ "", j = (j = i.length) > 3 ? j % 3 : 0;
	return s + (j ? i.substr(0, j) + t : "")
			+ i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t)
			+ (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};
var RunClientExe = {
    	run : function(exePath) {
    		alert(exePath);
    		try {
    			var objShell = new ActiveXObject("wscript.shell");
    			objShell.Run(exePath);
    			objShell = null;
    		} catch (e) {
    			alert(e.message);
    			alert('找不到文件"'+exePath+'"(或它的组件之一)。请确定路径和文件名是否正确，而且所需的库文件均可用。');
    		}
    	}
};