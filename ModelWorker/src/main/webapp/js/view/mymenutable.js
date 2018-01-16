(function($){
	var MyMenuTable = function(ele, menuId, option) {
		this.$element = ele;
		this.menuId = menuId;
		this.data = [];
		this.defaults = {
			property:"dateProperty",
			color:[
					{key:'0',bgcolor:"#FFFFFF"},
					{key:"1",bgcolor:"#FFD700"},
					{key:"2",bgcolor:"#FFA500"}
			]
		};
		this.config = $.extend({}, this.defaults, option);
	};
	MyMenuTable.prototype = {
		init : function() {
			var menuId = this.menuId;
			var ele = this.$element;
			$(ele).unbind("contextmenu");
			$(ele).bind("contextmenu", function(e) {return false;});
			$(ele).find("tr td[ref='1']").unbind("mousedown");
			$(ele).find("tr td[ref='1']").bind("mousedown", {myMenuTable:this}, this.showMenu);
			return this;
		},
		showMenu:function(e) {
			if (e.which == 3) {
				//右击
				var myMenuTable = e.data.myMenuTable;
				var menuId = myMenuTable.menuId;
				$("#" + menuId).css({"left":e.clientX,"top":e.clientY,"display":"block"});
				//设定菜单事件
				$("#" + menuId).find("li a").unbind("click");
				$("#" + menuId).find("li a").bind("click", {myMenuTable:myMenuTable, td:this}, myMenuTable.menuClick);
			}
		},
		menuClick : function(e) {
			var myMenuTable = e.data.myMenuTable;
			var config = myMenuTable.config;
			var td = e.data.td;
			var oldDuty = $(td).html();
			var newDuty = $(this).attr("ref");
			var newDutyName = $(this).attr("ref2");
			if (oldDuty == newDutyName) {
				//没有修改
				return;
			}
			var rowNo = $(td).closest("tr").index();
			var colNo = $(td).index();
			var property = $(td).attr(config.property);
			var newProperty = "";
			if (newDuty == "PH") {
				if (property == "2") {
					newProperty = "2";
				} else {
					newProperty = "1";
				}
			} else {
				if (property == "2") {
					newProperty = "2";
				} else {
					newProperty = "0";
				}
			}
			var tempData = {row:rowNo, col:colNo, oldDuty:oldDuty, newDuty:newDuty,oldProperty:property,newProperty:newProperty};
			var data = null;
			var index = -1;
			$.each(myMenuTable.data, function(n, value) {
				if (value.row == rowNo && value.col == colNo) {
					data = value;
					index = n;
					return false;
				}
			});
			$.each(config.color, function(n, color) {
				if (color.key == tempData.newProperty) {
					$(td).css({background:color.bgcolor});
					return false;
				}
			});
			if (data == null) {
				myMenuTable.data.push(tempData);
				$(td).html(newDutyName);
			} else {
				if (data.newDuty == tempData.newDuty) {
					//数据和上一次执行相同
					return;
				}
				$(td).html(newDutyName);
				if (data.oldDuty == tempData.newDuty) {
					//数据被修改回去,从数据删除改数据
					myMenuTable.data.splice(index, 1);
					return;
				}
				data.newDuty = tempData.newDuty;
				data.newProperty = tempData.newProperty;
			}
		},
		getEditData: function() {
			return this.data;
		},
		clearData: function() {
			this.data = [];
		}
	};
	$.fn.mymenutable = function(menuId, config) {
		var mymenutable = new MyMenuTable(this, menuId, config);
		return mymenutable.init();
	}	
})(jQuery);