package cn.lmx.flow.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.lmx.flow.interceptor.InterceptorUtil;
import cn.lmx.flow.interceptor.ResponseWrapper;

public class DataPrivilegeFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest)request;
		if (InterceptorUtil.isAjaxRequest(req)) {
			//System.out.println("Ajax");
			request.setCharacterEncoding("UTF-8");
			ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse)response);
			chain.doFilter(req, wrapper);
			String result = new String(wrapper.getResult(), response.getCharacterEncoding());
			result = getDealResult((HttpServletRequest)request, result);
			response.setContentLength(-1);
			ServletOutputStream out = response.getOutputStream();
			out.write(result.getBytes(response.getCharacterEncoding()));
			out.flush();			
			return;
		}
		chain.doFilter(request, response);
	}
	/**
	 * 转换数据
	 * @param request
	 * @param data
	 * @return
	 */
	private String getDealResult(HttpServletRequest request, String data) {
		if (data == null || "".equals(data)) {
			return data;
		}
		HttpSession session = request.getSession();
		Map<String, List<String>> dataPrivilegeMap = (Map<String, List<String>>)session.getAttribute("DataPrivilege");
		if (dataPrivilegeMap == null || dataPrivilegeMap.size() <= 0) {
			return data;
		}
		try {
			Gson gson = new Gson();
			//转换为List
			try {
				List<Map<String, Object>> list = gson.fromJson(data, new TypeToken<List<Map<String, Object>>>(){}.getType());
				if (list == null) {
					Map<String, Object> map = gson.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
					if (map == null) {
						return data;
					}
					DataFilter.filter(map, dataPrivilegeMap);
					return gson.toJson(map);
				} else {
					DataFilter.filter(list, dataPrivilegeMap);
					return gson.toJson(list);
				}
			} catch (Exception e) {
				Map<String, Object> map = gson.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
				if (map == null) {
					return data;
				}
				DataFilter.filter(map, dataPrivilegeMap);
				return gson.toJson(map);
			}
		} catch (Exception e) {
			//转换出错，返回转换前数据
			return data;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
