$(document).ready(function() {
	var resize = function() {
		if ($("div .tabContent")) {
			var windowWidth = $(document).width();
			var windowHeight = $(window).height();
			var parentWidth = $("div .tabContent").parent().width();
			var offset = $("div .tabContent").offset();
			var tableWidth = parentWidth ? parentWidth : windowWidth;
			var left = offset == null ? 0 : offset.left,
				top = offset == null ? 0 : offset.top;
			$("div .tabContent").width(tableWidth);
		}
		if ($("#leftMenuBar")) {
			var windowHeight = $(window).height();
			var offset = $("#leftMenuBar").offset();
			var top = offset == null ? 0 : offset.top;
			var left = offset == null ? 0 : offset.left;
			$("#leftMenuBar").height(windowHeight - top - 45);
		}
		if ($("#content")) {
			var windowWidth = $(document).width();
			var windowHeight = $(window).height();
			var parentWidth = $("#content").parent().width();
			var contentWidth = parentWidth ? parentWidth : windowWidth;
			var offset = $("#content").offset();
			var top = offset == null ? 0 : offset.top;
			var left = offset == null ? 0 : offset.left;
			$("#content").height(windowHeight - top - 20);
			$("#content").width(contentWidth);
		}
		if($("#table_scroll")) {
			var windowWidth = $(document).width();
			var windowHeight = $(window).height();
			var parentWidth = $("#table_scroll").parent().width();
			var offset = $("#table_scroll").offset();
			var tableWidth = parentWidth ? parentWidth : windowWidth;
			var left = offset == null ? 0 : offset.left,
				top = offset == null ? 0 : offset.top;
			$("#table_scroll").width(tableWidth - 0);
			$("#table_scroll").height(windowHeight - top - 40);
		}
	};
	$(window).resize(resize);
	$(window).resize();
});
