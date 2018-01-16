<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge, Chrome=1" />
    <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
    <title>X平台 - Login</title>
    <link href="${rc.contextPath}/vendor/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${rc.contextPath}/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="${rc.contextPath}/vendor/animate.css/animate.min.css" rel="stylesheet" type="text/css">
    <link href="${rc.contextPath}/dist/fa/css/login.css" rel="stylesheet" type="text/css">
	<!-- jQuery 2.1.4 -->
	<script src="${rc.contextPath}/vendor/jquery/dist/jquery.min.js"></script>
	<!-- Bootstrap 3.3.5 -->
	<script src="${rc.contextPath}/vendor/bootstrap/dist/js/bootstrap.js"></script>
	<script src="${rc.contextPath}/vendor/requirejs/require.js"></script>
	<script src="${rc.contextPath}/js/common.js"></script>
<style type="text/css">
	.err-tips {
		position: absolute;
		margin: 24px;
		color: #d0212d;
		font-size: 14px;
	}
</style>
</head>
<body class="expanded skin-blue sidebar-mini">
	<div class="container">
		<div class="login-container">
			<div class="title">人事管理辅助系统</div>
			<br>
			<c:if test="${not empty error}">
				<div class="err-tips">
					${error}
				</div>
			</c:if>
			<br>
			<div class="login-box shadow">
				<form method="post" id="myform" action="${rc.contextPath}/user/login">
					<div class="form-group">
						<label for="accountId">用户名</label><input type="text"
							name="accountId" class="form-control" value="${accountId}" placeholder="用户名"
							required> <span class="glyphicon glyphicon-user"
							aria-hidden="true"></span>
					</div>
					<div class="form-group">
						<label for="password">密码</label> <input type="password"
							name="password" class="form-control" placeholder="密码" value="" required> <span
							class="glyphicon glyphicon-lock" aria-hidden="true"></span>
					</div>
					<input type="hidden" name="_token"
						value="mO1UFtAWHhPRrD6kwDCBqunqQ05N3LQhgOnEAZ4r">
					<button type="submit" class="btn btn-lg btn-success" id="login">
						登 录</button>
				</form>

<!-- 				<div class="links"> -->
<!--                     <hr> -->
<!--                     <a href="javascript:void(0)" id="forgetPassword">忘记密码？</a> -->
<!--                 </div> -->
			</div>
		</div>
	</div>

	<!-- 忘记密码 弹出框 -->
    <div id="forgetBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title">忘记密码</h4>
                </div>
                <form class="form-horizontal" id="findPassword">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="typeNum" class="col-sm-3 control-label">用户名</label>
                            <div class="col-sm-6"><input type="text" class="form-control" name="accountId" required></div>
                        </div>
                        <div class="form-group">
                            <label for="typeName" class="col-sm-3 control-label">找回方式</label>
                            <div class="col-sm-6">
                                <select class="form-control" name="verificationType">
                                    <option value="1">手机找回</option>
                                    <option value="2">邮箱找回</option>
                                </select>
                            </div>
                            <button type="button" class="btn btn-success" id="sendbtn" onclick="sendCode()">发送验证码</button>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">验证码</label>
                            <div class="col-sm-6"><input type="text" class="form-control" name="code" id="code"></div>
                            <label class="col-sm-2 control-label" id="overtime"></label>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">新密码</label>
                            <div class="col-sm-6"><input type="password" class="form-control" name="newpassword" id="newpassword"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">确认密码</label>
                            <div class="col-sm-6"><input type="password" class="form-control" name="confirmpassword" id="confirmpassword"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" id="setpass" onclick="resetPassword()">重设密码</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="modal fade" id="infoModal" tabindex="-1" role="dialog"
		aria-labelledby="mySmallModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="exampleModalLabel">消息</h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="EMAIL_VALID_PERIOD" value="${EMAIL_VALID_PERIOD }"/>
	<input type="hidden" id="PHONE_VALID_PERIOD" value="${PHONE_VALID_PERIOD }"/>
    <script type="text/javascript">
	var InterValObj; 
	var count = parseInt($("#PHONE_VALID_PERIOD").val(),10) * 60; 
	var curCount;
	$("#accountId").val("${accountId}");
	$(function(){		
		$("#forgetPassword").click(function(){
			$("#forgetBox").modal("show");
			
			//$("#findPassword").reset();
			window.clearInterval(InterValObj);
			$("#sendbtn").removeAttr("disabled");
			$("#sendbtn").html("发送验证码");
			$("#findPassword [name='accountId']").val("");
			$("#findPassword [name='verificationType']").val("1");
			$("#findPassword [name='code']").val("");
			$("#findPassword [name='newpassword']").val("");
			$("#findPassword [name='confirmpassword']").val("");
			count = parseInt($("#PHONE_VALID_PERIOD").val(),10) * 60; 
			curCount = 0;
		});
	});
	
	function sendCode(){
		if ("" ==  $("#findPassword [name='accountId']").val().trim()) {
			MessageBox.info("请输入用户名");
			return;
		}
		
		if ("2" == $("#findPassword [name='verificationType']").val()) {
			count =  parseInt($("#EMAIL_VALID_PERIOD").val(),10) * 60;
		}
        
		$.ajax({
    		url:"${rc.contextPath}/sendcode",
			type:"post",
			dataType:"json",
			data:{
				account: $("#findPassword [name='accountId']").val(),
				type: $("#findPassword [name='verificationType']").val()
			},
			success:function(data){
				if ("1" ==  data.errType) {
					MessageBox.info(data.errMsg);
				} else if ("0" ==  data.errType) {
					curCount = count;
					$("#sendbtn").attr("disabled", "true");
					$("#sendbtn").html(curCount + "秒后重发");  
					InterValObj = window.setInterval(SetRemainTime, 1000); 
				}
			}
    	});
	}
	function SetRemainTime() {
		if (curCount == 0) {
			window.clearInterval(InterValObj);
			$("#sendbtn").removeAttr("disabled");
			$("#sendbtn").html("重新发送验证码");
			$("#code").val("");
		} else {
			curCount--;
			$("#sendbtn").html(curCount + "秒后重发");
		}
	}
	
	function resetPassword(){
		if ("" ==  $("#findPassword [name='accountId']").val().trim()) {
			MessageBox.info("请输入用户名");
			return;
		}
		
		if ("" ==  $("#findPassword [name='code']").val().trim()) {
			MessageBox.info("请输入验证码");
			return;
		}
		
		if(validatePassword()){
			if(checkPassowrdIsMatched()){
				$.ajax({
					type:"post",
					url:"${rc.contextPath}/resetPassword",
					data:{
						code:$("#code").val(),
						accountId: $("#findPassword [name='accountId']").val(),
						password: $("#newpassword").val()
					},
					dataType:"json",
					async:false,
					success:function(data){
						if(data.errType == "1"){
							MessageBox.info(data.errMsg);
						} else{
							$("#forgetBox").modal("hide");
						}
					}
				});
			}
		}
	}
	
	function validatePassword() {
		if ($("#newpassword").val().trim().length == 0) {
			MessageBox.info("请输入密码");
			return false;
		} else {
			var flag = checkPasswordIsValidate();
			return flag;
		}
	}

	function checkPasswordIsValidate() {
		var reg = /^(?!\D+$)(?![^a-zA-Z]+$)\S{6,10}$/;
		var pwd = $("#newpassword").val();
		if(!reg.test(pwd)) {
			MessageBox.info("密码至少包含数字、字母，长度6－10");
			return false;
		}
		else{
			return true;
		}
	}
	
	function checkPassowrdIsMatched() {
		var pwd = $("#newpassword").val();
		var pwd1 = $("#confirmpassword").val();
		if(pwd1.length == 0)
		{
			MessageBox.info("请输入确认密码");
			return false;
		}
		else if (pwd != pwd1) {
			MessageBox.info("两次输入的密码不匹配");
			return false;
		} 
		else {
			return true;
		}
	}
</script>
</body>
</html>