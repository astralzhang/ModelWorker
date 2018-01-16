package cn.lmx.flow.interceptor;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.lmx.flow.utils.AppConfiguration;

public class InterceptorUtil {
	private static AppConfiguration CONF;
	@Autowired
	@Qualifier("appConfiguration")
	public void init(AppConfiguration configuration) {
		CONF = configuration;
	}
	/**
	 * 过滤资源URL和不需要拦截的URL
	 * @param request
	 * @return
	 */
	public static boolean preProcess(HttpServletRequest request) {
		if(CONF.getResourcePattern() == null || "".equals(CONF.getResourcePattern())){
			//没有指定资源
            return true;
        }
		String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String path = "";
        if(""!=ctx){
            int pos = uri.indexOf(ctx);
            path = uri.substring(pos+ctx.length());
        }
        if(""!= path){
            if (path.matches(CONF.getResourcePattern())) {
            	return true;
            }
        }
        String excludeUrl = CONF.getLoginInterceptorExclude();
        String[] arrUrl = excludeUrl.split(";");
        if (arrUrl == null || arrUrl.length <= 0) {
        	return true;
        }
        String url = uri.substring(ctx.length());
        for (int i = 0; i < arrUrl.length; i++) {
        	if (url.equals(arrUrl[i])) {
        		return true;
        	}
        }
        return false;
	}
	/**
     * 判断当前是否为异步请求
     * @param request
     * @return true,当前为异步请求;false，非异步请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-with");
        return requestType != null && requestType.equals("XMLHttpRequest");
    }
    /**
     * 取得登录用URL
     * @param request
     * @return
     */
    public static String getLoginUrl(HttpServletRequest request) {
    	String loginUrl = CONF.getLoginUrl();
    	if (loginUrl == null) {
    		return request.getContextPath();
    	}
    	return request.getContextPath() + loginUrl;
    }
    /**
     * 异步请求时 data json写出
     * @param response
     */
    public static void ajaxResponseData(HttpServletResponse response, Map<String, String> data){
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("text/plain;charset=UTF-8");
        try{
            PrintWriter writer = response.getWriter();
            JsonGenerator gen = new JsonFactory().createJsonGenerator(writer);
            mapper.writeValue(gen,data);
            writer.flush();
            writer.close();
            gen.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
