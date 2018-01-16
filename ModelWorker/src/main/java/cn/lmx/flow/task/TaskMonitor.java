package cn.lmx.flow.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.lmx.flow.bean.task.TaskProcessorBean;
import cn.lmx.flow.service.task.TaskProcessorService;

public class TaskMonitor implements TaskInterface {
	//线程池
	private ExecutorService executor = Executors.newCachedThreadPool();
	//任务列表
	private List<TaskProcessorBean> taskList;
	//任务处理
	private TaskProcessorService taskProcessorService;
	//正在执行的任务列表
	private List<String> processTaskList;
	//任务接收线程
	private Thread recvThread;
	//任务接收线程运行标志
	private boolean recvFlag;
	//任务完了check线程
	private Thread callbackThread;
	//任务完了check线程原型标志
	private boolean callbackFlag;
	//任务结果
	private Map<String, Future<String>> futures;
	//实例化
	private static TaskMonitor taskMonitor = null;
	/**
	 * 取得实例化对象
	 * @param taskProcessorService
	 * @return
	 */
	public static TaskMonitor getInstance(TaskProcessorService taskProcessorService) {
		if (taskMonitor == null) {
			taskMonitor = new TaskMonitor(taskProcessorService);
		}
		return taskMonitor;
	}
	/**
	 * 构造函数
	 */
	private TaskMonitor(TaskProcessorService taskProcessorService) {
		this.taskProcessorService = taskProcessorService;
		futures = new HashMap<String, Future<String>>();
		processTaskList = new ArrayList<String>();
	}
	/**
	 * 关闭线程
	 */
	public void stopThread() {
		// TODO Auto-generated method stub
		executor.shutdownNow();
		recvFlag = false;
		callbackFlag = false;
		System.out.println("close Thread");
	}
	/**
	 * 创建消息任务列表
	 */
	public synchronized void createTaskList() {
		try {
			if (taskList != null) {
				taskList.clear();
			}
			taskList = taskProcessorService.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startMonitor() {
		//建立消息任务列表
		createTaskList();
		//任务接收线程
		recvThread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Thread.currentThread().setName("主线程");
				while (recvFlag) {
					try {
						Calendar c = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
						if (taskList != null) {
							for (int i = 0; i < taskList.size(); i++) {
								TaskProcessorBean taskBean = taskList.get(i);
								if (sdf.format(c.getTime()).compareTo(taskBean.getProcessTime()) < 0) {
									continue;
								}
								if ("Y".equals(taskBean.getStartThread())) {
									//启用线程
									if (taskBean.getTaskId() != null && !"".equals(taskBean.getTaskId())) {
										boolean bProcessing = false;
										for (int j = 0; j < processTaskList.size(); j++) {
											if (taskBean.getTaskId().equals(processTaskList.get(j))) {
												//上一条任务尚未执行完成
												bProcessing = true;
												continue;
											}
										}
										if (bProcessing) {
											continue;
										}
									}
									Future<String> future = executor.submit(new Callable() {
										@Override
										public Object call() throws Exception {
											// TODO Auto-generated method stub
											return runTask(taskBean);
										}
										
									});
									if (taskBean.getTaskId() != null && !"".equals(taskBean.getTaskId())) {
										synchronized(futures) {
											futures.put(taskBean.getTaskId(), future);
											processTaskList.add(taskBean.getTaskId());
										}
									}
								} else {
									//不启用线程
									runTask(taskBean);
								}
							}
						}
						Thread.sleep(1000 * 60);
						System.out.println("主线程执行.......");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				super.run();
			}			
		};
		//任务完了check线程
		callbackThread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (callbackFlag) {
					try {
						synchronized(futures) {
							for (int i = 0; i < processTaskList.size(); i++) {
								Future<String> future = futures.get(processTaskList.get(i));
								String result = future.get();
								System.out.println("TaskId=" + processTaskList.get(i) + "的线程执行完了，result=" + result);
								futures.remove(processTaskList.get(i));
								processTaskList.remove(i);
								i--;
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				super.run();
			}			
		};
		//启动线程
		recvFlag = true;
		callbackFlag = true;
		recvThread.start();
		callbackThread.start();
	}
	/**
	 * 执行任务
	 * @param taskBean
	 * @return
	 */
	private String runTask(TaskProcessorBean taskBean) {
		try {
			taskProcessorService.runTask(taskBean.getId());
			return "Success";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Failed";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed";
		}
	}
	public static void main(String[] args) {
		TaskMonitor monitor = new TaskMonitor(null);
		monitor.startMonitor();
		try {
			Thread.sleep(1000 * 60 * 2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitor.stopThread();
	}
}
