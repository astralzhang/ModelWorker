package cn.lmx.flow.task;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;

import cn.lmx.flow.service.task.TaskProcessorService;

public class TaskServlet extends HttpServlet {
	private TaskMonitor task = null;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		if (task != null) {
			task.stopThread();
		}
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		if (task != null) {
			task.stopThread();
		}
		ServletContext sc = getServletContext();
		WebApplicationContext wac = (WebApplicationContext)sc.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.appServlet");
		TaskProcessorService taskProcessorService = (TaskProcessorService)wac.getBean("TaskProcessorService");
		task = TaskMonitor.getInstance(taskProcessorService);
		task.startMonitor();
		super.init();
	}

}
