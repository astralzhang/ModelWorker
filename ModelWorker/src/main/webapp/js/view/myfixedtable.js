(function($){
	var MyFixedTable = function(ele, option) {
		this.$element = ele;
		this.defaults = {
			row:0,
			col:0
		};
		this.config = $.extend({}, this.defaults, option);
	};
	MyFixedTable.prototype = {
		init : function() {
			var ele = this.$element;
			$(ele).unbind("scroll");
			$(ele).bind("scroll", {table:this}, this.scroll_data);
		},
		scroll_data:function(event) {
			var table = event.data.table;
			var config = table.config;
			var element = table.$element;
			var row = config.row;
			var col = config.col;
			if (row <= 0 && col <= 0) {
				return;
			}
			var left = $("#table_scroll").scrollLeft();
			var top = $("#table_scroll").scrollTop();
			if (row > 0) {
				if (col > 0) {
					//固定列
					var tr = $(element).find("table tr:gt(" + (row - 1) + ")");
					for (var i = 0; i < col; i++) {
						$.each(tr, function() {
							$(this).children().eq(i).css({"position":"relative","top":"0px","left":left,"background-color":"white"});
						});
					}
					//固定行
					tr = $(element).find("table tr:lt(" + row + ")");
					$.each(tr, function() {
						for (var i = col; i < $(this).children().length; i++) {
							$(this).children().eq(i).css({"position":"relative","top":top,"left":"0px","background-color":"white"});
						}
					});
					//固定交叉行列
					tr = $(element).find("table tr:lt(" + row + ")");
					$.each(tr, function() {
						for (var i = 0; i < col; i++) {
							$(this).children().eq(i).css({"position":"relative","top":top,"left":left,"background-color":"white","z-index":"99"});
						}
					});			
				} else {
					//仅行固定
					$(element).find("table tr:lt(" + row + ")").children().css({"position":"relative","top":top,"left":"0px","background-color":"white"});
				}
			} else {
				//仅列固定
				for (var i = 0; i < col; i++) {
					var tr = $(element).find("table tr")
					$.each(tr, function() {
						$(this).children().eq(i).css({"position":"relative","top":"0px","left":left,"background-color":"white"});
					});
				}
			}
		}
	};
	$.fn.myfixedtable = function(config) {
		var myfixedtable = new MyFixedTable(this, config);
		return myfixedtable.init();
	}	
})(jQuery);