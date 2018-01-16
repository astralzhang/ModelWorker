package cn.lmx.flow.utils;

public class AppConfiguration {
	//登录用URL
	private String loginUrl;
	//资源
	private String resourcePattern;
	//登录拦截器不拦截的URL
	private String loginInterceptorExclude;
	public AppConfiguration() {
	
	}
	public final String getLoginUrl() {
		return loginUrl;
	}
	public final void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public final String getResourcePattern() {
		return resourcePattern;
	}
	public final void setResourcePattern(String resourcePattern) {
		this.resourcePattern = resourcePattern;
	}
	public final String getLoginInterceptorExclude() {
		return loginInterceptorExclude;
	}
	public final void setLoginInterceptorExclude(String loginInterceptorExclude) {
		this.loginInterceptorExclude = loginInterceptorExclude;
	}
}
