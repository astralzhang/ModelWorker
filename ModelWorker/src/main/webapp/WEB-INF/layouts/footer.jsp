<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>

<!-- Main Footer -->
<footer class="main-footer" style="padding:2px 0 2px 0;width:100%;position:fixed;left:0;bottom:0;">
  <!-- To the right -->
<!--   <div class="pull-right hidden-xs"> -->
<!--     Anything you want -->
<!--   </div> -->
  <!-- Default to the left -->
<!--   <strong>Copyright &copy; 2016 <a href="#">limuxin</a>.</strong> All rights reserved. -->
</footer>
      
<script type="text/javascript">

$(function(){
    var menuItem = $('meta[name="menuId"]').attr('content')||'';
    $('.sidebar-menu').find('li').removeClass('active');
    if(''!==menuItem){
        $('.sidebar-menu').find('#'+menuItem).addClass('active').parents('li').addClass('active');
    }
});
</script>

<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
			<%--
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			--%>
				<h4 class="modal-title" id="exampleModalLabel">操作提示</h4>
			</div>
			<div class="modal-body">确认继续吗？</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="infoModal" tabindex="-1" role="dialog"
	aria-labelledby="mySmallModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
			<%-- 
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			--%>
				<h4 class="modal-title" id="exampleModalLabel">消息</h4>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="errorModal" tabindex="-1" role="dialog"
	aria-labelledby="mySmallModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
			<%-- 
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			--%>
				<h4 class="modal-title" id="exampleModalLabel">系统错误</h4>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="processBar" tabindex="-1" role="dialog"
	aria-labelledby="mySmallModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
			<%-- 
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			--%>
				<h4 class="modal-title" id="exampleModalLabel">消息</h4>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>