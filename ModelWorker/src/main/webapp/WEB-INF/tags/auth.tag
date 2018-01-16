<%@tag import="java.util.List"%>
<%@tag import="cn.lmx.flow.bean.module.UserPermissionBean" %>
<%@tag import="cn.lmx.flow.utils.UserInfo" %>
<%@tag pageEncoding="UTF-8"%>
<%@attribute name="act" type="java.lang.String" required="true"%>
<%@attribute name="view" type="java.lang.String" required="false"%>
<%
	boolean isDisplay = false;
	String viewNo = (String)jspContext.getAttribute("view");
	System.out.println("viewNo=" + viewNo);
	String actionCode = (String)jspContext.getAttribute("act");
	if (viewNo != null && !"".equals(viewNo)) {
		actionCode = viewNo + actionCode;
	}
	System.out.println("actionCode=" + actionCode);
	List<UserPermissionBean> list = (List<UserPermissionBean>)UserInfo.get(UserPermissionBean.class.getName());
	if (list != null && list.size() > 0) {
		for (int i = 0; i < list.size(); i++) {
			UserPermissionBean bean = list.get(i);
//System.out.println(bean.getItemNo() + "=" + actionCode);			
			if (bean.getItemNo() != null && bean.getItemNo().equals(actionCode)) {
				isDisplay = true;
				break;
			}
		}
	}
	if (isDisplay) {
%>
<jsp:doBody/>
<%
    }
%>

