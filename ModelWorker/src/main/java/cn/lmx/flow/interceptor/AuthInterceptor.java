package cn.lmx.flow.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.lmx.flow.bean.module.MenuBean;
import cn.lmx.flow.bean.module.PrivilegeBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.module.UserPermissionBean;
import cn.lmx.flow.service.module.PrivilegeService;
import cn.lmx.flow.utils.UserInfo;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	//
	@Resource(name ="PrivilegeService")
	private PrivilegeService privilegeService;
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		if (InterceptorUtil.preProcess(request)) {
			return true;
		}
		HttpSession session = request.getSession();
		UserBean userBean = (UserBean)session.getAttribute(UserBean.class.getName());
		if (userBean == null) {
			//没有登录，或Session已过期
			if (InterceptorUtil.isAjaxRequest(request)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("errType", "1");
				map.put("errMessage", "系统已过期，请重新登录！");
				InterceptorUtil.ajaxResponseData(response, map);
			} else {
				response.sendRedirect(InterceptorUtil.getLoginUrl(request));
			}
			return false;
		}
		//判断权限
		List<UserPermissionBean> list = (List<UserPermissionBean>)session.getAttribute(UserPermissionBean.class.getName());
		String ctx = request.getContextPath();
        String uri = request.getRequestURI();
        System.out.println(request.getRequestURL());
        uri = uri.substring(ctx.length());
//System.out.println("uri=" + uri);        
		if (hasPermission(uri, list)) {
			UserInfo.put(UserBean.class.getName(), userBean);
			UserInfo.put(UserPermissionBean.class.getName(), list);
			UserInfo.put(MenuBean.class.getName()	, session.getAttribute(MenuBean.class.getName()));
			UserInfo.put("DataPrivilege", session.getAttribute("DataPrivilege"));
			return true;
		}
		if (InterceptorUtil.isAjaxRequest(request)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("errType", "1");
			map.put("errMessage", "没有该功能访问权限！");
			InterceptorUtil.ajaxResponseData(response, map);
		} else {
			response.sendRedirect(InterceptorUtil.getLoginUrl(request));
		}
		return false;
	}
	/**
	 * 判断是否拥有访问权限
	 * @param uri
	 * @param list
	 * @return
	 */
	private boolean hasPermission(String uri, List<UserPermissionBean> list) {
		if (list == null || list.size() <= 0) {
			return false;
		}
		if (uri == null) {
			return false;
		}
		try {
			List<PrivilegeBean> privilegeList = privilegeService.listAll();
			boolean hasCheck = false;
			if (privilegeList != null) {
				for (int i = 0; i < privilegeList.size(); i++) {
					PrivilegeBean bean = privilegeList.get(i);
					if (uri.equals(bean.getActionUrl())) {
						hasCheck = true;
						break;
					}
				}
			}
			if (hasCheck) {
				for (int i = 0; i < list.size(); i++) {
					UserPermissionBean bean = list.get(i);
					String actionUrl = bean.getActionUrl() == null ? "" : bean.getActionUrl();
					int index = actionUrl.indexOf("?");
					if (index >= 0) {
						actionUrl = actionUrl.substring(0, index);
					}
//System.out.println("uri=" + uri + ";url=" + actionUrl);					
					if (uri.equals(actionUrl)) {
						return true;
					}
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
