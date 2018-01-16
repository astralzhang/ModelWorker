<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html>
<!-- BEGIN HEAD -->
<head>
	<meta charset="utf-8" />
	<title>FESCO Adecco 福利平台管理系统</title>
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta content='<decorator:getProperty property="description"/>' name="description" />
	<meta content="heinqi" name="author" />
	<jsp:include page="import-res.jsp"/>
	<jsp:include page="core-plugin.jsp"/>
	<%--<link rel="shortcut icon" href="media/image/favicon.ico" />--%>
	<!-- BEGIN PAGE LEVEL STYLES -->
	<decorator:head/>
	<!-- END PAGE LEVEL STYLES -->
</head>

<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="hold-transition skin-red sidebar-mini">
  <div class="wrapper">
	<!-- BEGIN HEADER -->
	<jsp:include page="header.jsp"/>
	<!-- END HEADER -->
	<jsp:include page="left-menu-personal.jsp"/>
	<!-- Content Wrapper. Contains page content -->
	<decorator:body/>
	
	<jsp:include page="footer.jsp"/>
	
  </div><!-- ./wrapper -->
</body>
</html>