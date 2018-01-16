package cn.lmx.flow.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import cn.lmx.flow.utils.DataFilter;

public class DataInterceptor extends HandlerInterceptorAdapter {
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		if (handler instanceof ResourceHttpRequestHandler) {
			String uri = request.getRequestURI();
			System.out.println(uri);
			return;
		}
		HttpSession session = request.getSession();
		Map<String, List<String>> dataPrivilegeMap = (Map<String, List<String>>)session.getAttribute("DataPrivilege");
		
		if (InterceptorUtil.isAjaxRequest(request)) {
			//Ajax请求，不做处理
			return;
		} else {
			//非Ajax请求
			if (modelAndView == null) {
				//没有返回数据
				return;
			}
			Map<String, Object> map = modelAndView.getModel();
			//权限处理
			DataFilter.filter(map, dataPrivilegeMap);
		}	
		super.postHandle(request, response, handler, modelAndView);
	}
}
