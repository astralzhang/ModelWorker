package cn.lmx.flow.service.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.lmx.flow.bean.task.TaskProcessorBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.task.TaskDao;
import cn.lmx.flow.dao.task.TaskProcessErrorDao;
import cn.lmx.flow.dao.task.TaskProcessHistoryDao;
import cn.lmx.flow.dao.task.TaskProcessorDao;
import cn.lmx.flow.entity.task.TaskProcessError;
import cn.lmx.flow.entity.task.TaskProcessHistory;
import cn.lmx.flow.entity.task.TaskProcessor;
import cn.lmx.flow.entity.task.Tasks;
import cn.lmx.flow.task.TaskMonitor;

@Repository("TaskProcessorService")
public class TaskProcessorService {
	//任务设定
	@Resource(name="TaskDao")
	private TaskDao taskDao;
	//任务执行Dao
	@Resource(name="TaskProcessorDao")
	private TaskProcessorDao taskProcessorDao;
	//任务执行履历
	@Resource(name="TaskProcessHistoryDao")
	private TaskProcessHistoryDao taskProcessHistoryDao;
	//任务执行异常
	@Resource(name="TaskProcessErrorDao")
	private TaskProcessErrorDao taskProcessErrorDao;
	//本地数据库SQL
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//远程数据库SQL
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSqlDao;
	/**
	 * 取得所有需要执行的任务
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<TaskProcessorBean> list() throws Exception {
		try {
			List<?> taskList = taskProcessorDao.listAll();
			if (taskList == null || taskList.size() <= 0) {
				return null;
			}
			List<TaskProcessorBean> list = new ArrayList<TaskProcessorBean>();
			for (int i = 0; i < taskList.size(); i++) {
				TaskProcessor task = (TaskProcessor)taskList.get(i);
				TaskProcessorBean taskBean = new TaskProcessorBean();
				BeanUtils.copyProperties(task, taskBean);
				list.add(taskBean);
			}
			return list;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 运行任务
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void runTask(String id) throws Exception {
		String taskId = null;
		try {
			TaskProcessor task = taskProcessorDao.getById(id);
			if (task == null) {
				return;
			}
			taskId = task.getTaskId();
			Calendar c1 = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			if ("0".equals(task.getTaskType())) {
				//定时任务
				try {
					if ("S".equals(task.getUrlSqlFlag()) || "P".equals(task.getUrlSqlFlag())) {
						//SQL文 或 存储过程
						runSql(task);
					} else if ("U".equals(task.getUrlSqlFlag())) {
						//URL
						runUrl(task);
					} else {
						return;
					}
				} catch (Exception e) {
					Calendar c = Calendar.getInstance();
					if (e.getCause() != null) {
						TaskProcessError error = new TaskProcessError();
						error.setId(UUID.randomUUID().toString());
						error.setTaskId(taskId);
						error.setProcessId(id);
						error.setErrorMessage(new StringBuffer("")
											.append("任务执行异常，原因：")
											.append(e.getCause().getMessage()).toString());
						error.setCreateTime(sdf.format(c));
						taskProcessErrorDao.add(error);
					}
					TaskProcessError error = new TaskProcessError();
					error.setId(UUID.randomUUID().toString());
					error.setTaskId(taskId);
					error.setProcessId(id);
					error.setErrorMessage(new StringBuffer("")
										.append("任务执行异常，原因：")
										.append(e.getMessage()).toString());
					error.setCreateTime(sdf.format(c.getTime()));
					taskProcessErrorDao.add(error);
					return;
				}
				//写任务执行履历
				TaskProcessHistory history = new TaskProcessHistory();
				BeanUtils.copyProperties(task, history);
				history.setBeginTime(sdf.format(c1.getTime()));
				Calendar c2 = Calendar.getInstance();
				history.setEndTime(sdf.format(c2.getTime()));
				history.setCreateTime(sdf.format(c2.getTime()));
				history.setCreateUser("SUPER");
				history.setUpdateUser(null);
				history.setUpdateTime(null);
				history.setThread(Thread.currentThread().getName());
				history.setMessage(task.getMessageTemplate());
				taskProcessHistoryDao.add(history);
				//取得任务设定信息
				if (task.getTaskId() != null && !"".equals(task.getTaskId())) {
					Tasks taskSet = taskDao.getById(task.getTaskId());
					if (taskSet != null) {
						Calendar c = Calendar.getInstance();
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
						Calendar processTime = Calendar.getInstance();
						processTime.setTime(sdf1.parse(task.getProcessTime()));
						int unit = -1;
						if ("Y".equals(taskSet.getUnit())) {
							unit = Calendar.YEAR;
						} else if ("M".equals(taskSet.getUnit())) {
							unit = Calendar.MONTH;
						} else if ("D".equals(taskSet.getUnit())) {
							unit = Calendar.DAY_OF_MONTH;
						} else if ("H".equals(taskSet.getUnit())) {
							unit = Calendar.HOUR_OF_DAY;
						} else if ("m".equals(taskSet.getUnit())) {
							unit = Calendar.MINUTE;
						}
						Calendar startTime = Calendar.getInstance();
						startTime.setTime(sdf1.parse(taskSet.getStartTime()));
						Calendar endTime = Calendar.getInstance();
						if (taskSet.getEndTime() != null && !"".equals(taskSet.getEndTime())) {
							endTime.setTime(sdf1.parse(taskSet.getEndTime()));
						} else {
							endTime.setTime(sdf1.parse("999912310000"));
						}
						if ("Y".equals(task.getCompleted())) {
							taskSet.setCompleteMiss("N");
						}
						if ("Y".equals(taskSet.getCompleteMiss())) {
							//需要补全没有执行的任务
							if (unit != -1) {
								processTime.add(unit, taskSet.getFrequency().intValue());								
								while (processTime.compareTo(startTime) < 0) {
									processTime.add(unit, taskSet.getFrequency().intValue());
								}
								Calendar loopCalendar = null;
								if (endTime.compareTo(c) > 0) {
									loopCalendar = c;
								} else {
									loopCalendar = endTime;
								}
								while (processTime.compareTo(loopCalendar) <= 0) {
									//取得时间
									List<?> tempList = taskProcessorDao.getByTaskTime(taskSet.getId(), sdf1.format(processTime.getTime()));
									if (tempList == null || tempList.size() <= 0) {
										TaskProcessor taskProcessor = new TaskProcessor();
										BeanUtils.copyProperties(taskSet, taskProcessor);
										taskProcessor.setId(UUID.randomUUID().toString());
										taskProcessor.setTaskId(taskSet.getId());
										taskProcessor.setProcessTime(sdf1.format(processTime.getTime()));
										taskProcessor.setCreateUser("SUPER");
										taskProcessor.setCreateTime(sdf.format(c.getTime()));
										taskProcessor.setUpdateTime(null);
										taskProcessor.setUpdateUser(null);
										taskProcessor.setCompleted("Y");
										taskProcessorDao.add(taskProcessor);
										processTime.add(unit, taskSet.getFrequency().intValue());
									}
								}
								if (endTime.compareTo(c) > 0) {
									//新增一条大于当前时间的任务
									List<?> tempList = taskProcessorDao.getByTaskTime(taskSet.getId(), sdf1.format(processTime.getTime()));
									if (tempList == null || tempList.size() <= 0) {
										TaskProcessor taskProcessor = new TaskProcessor();
										BeanUtils.copyProperties(taskSet, taskProcessor);
										taskProcessor.setId(UUID.randomUUID().toString());
										taskProcessor.setTaskId(taskSet.getId());
										taskProcessor.setProcessTime(sdf1.format(processTime.getTime()));
										taskProcessor.setCreateUser("SUPER");
										taskProcessor.setCreateTime(sdf.format(c.getTime()));
										taskProcessor.setUpdateTime(null);
										taskProcessor.setUpdateUser(null);
										taskProcessor.setCompleted("N");
										taskProcessorDao.add(taskProcessor);
									}
								}
							}
						} else {
							//不补漏
							if (unit != -1) {
								processTime.add(unit, taskSet.getFrequency().intValue());
								while (true) {									
									if (processTime.compareTo(startTime) < 0) {
										processTime.add(unit, taskSet.getFrequency().intValue());
										continue;
									}
									if (processTime.compareTo(c) < 0) {
										processTime.add(unit, taskSet.getFrequency().intValue());
										continue;
									}
									if (processTime.compareTo(endTime) < 0) {
										//新增一条任务
										List<?> tempList = taskProcessorDao.getByTaskTime(taskSet.getId(), sdf1.format(processTime.getTime()));
										if (tempList == null || tempList.size() <= 0) {
											TaskProcessor taskProcessor = new TaskProcessor();
											BeanUtils.copyProperties(taskSet, taskProcessor);
											taskProcessor.setId(UUID.randomUUID().toString());
											taskProcessor.setTaskId(taskSet.getId());
											taskProcessor.setProcessTime(sdf1.format(processTime.getTime()));
											taskProcessor.setCreateUser("SUPER");
											taskProcessor.setCreateTime(sdf.format(c.getTime()));
											taskProcessor.setUpdateTime(null);
											taskProcessor.setUpdateUser(null);
											taskProcessor.setCompleted("N");
											taskProcessorDao.add(taskProcessor);
										}
										break;
									}
									break;
								}
							}
						}
					}
				}
				//删除已运行完成的任务
				taskProcessorDao.delete(task);
				//重建任务列表
				TaskMonitor monitor = TaskMonitor.getInstance(null);
				monitor.createTaskList();
			} else if ("1".equals(task.getTaskType())) {
				//消息
			} else if ("2".equals(task.getTaskType())) {
				//邮件提醒
			} else {
				//非法
				return;
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getMessage());
			}
			throw e;
		}
	}
	/**
	 * 执行SQL
	 * @param task
	 * @throws Exception
	 */
	private void runSql(TaskProcessor task) throws Exception {
		try {
			String sql = task.getUrlOrSql();
			if (sql == null || "".equals(sql)) {
				return;
			}
			Map<String, Object> map = null;
			if (task.getParameter() != null && !"".equals(task.getParameter())) {
				Gson gson = new Gson();
				map = gson.fromJson(task.getParameter(), new TypeToken<Map<String, Object>>(){}.getType());
			}
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			if ("R".equals(task.getTarget())) {
				//远程数据库
				businessSqlDao.update(sql, map);
			} else {
				sqlDao.update(sql, map);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 执行URL
	 * @param task
	 * @throws Exception
	 */
	private void runUrl(TaskProcessor task) throws Exception {
		BufferedReader in = null;
		try {
			String sUrl = task.getUrlOrSql();
			URL realUrl = new URL(sUrl);
			URLConnection connection = realUrl.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
			String paramData = "";
			String postData = task.getParameter();
			if (postData != null && !"".equals(postData)) {
				Gson gson = new Gson();
				Map<String, Object> map = gson.fromJson(task.getParameter(), new TypeToken<Map<String, Object>>(){}.getType());
				if (map != null) {
					Iterator<Entry<String, Object>> it = map.entrySet().iterator();
					StringBuffer sb = new StringBuffer("");
					while (it.hasNext()) {
						Entry<String, Object> entry = it.next();
						if (sb.length() > 0) {
							sb.append("&");
						}
						sb.append(entry.getKey()).append("=");
						if (entry.getValue() != null) {							
							sb.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
						}
					}
					paramData = sb.toString();
				}
			}
			// 设置通用的请求属性
			httpURLConnection.setDoOutput(true);
	        httpURLConnection.setRequestMethod("POST");
	        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        	httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");	        	
	        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(paramData.length()));
	        httpURLConnection.setRequestProperty("accept", "*/*");
	        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
	        httpURLConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        
	        OutputStream outputStream = null;
	        OutputStreamWriter outputStreamWriter = null;
	        InputStream inputStream = null;
	        InputStreamReader inputStreamReader = null;
	        BufferedReader reader = null;
	        StringBuffer resultBuffer = new StringBuffer();
	        String tempLine = null;	        
	        try {
	            outputStream = httpURLConnection.getOutputStream();
	            outputStreamWriter = new OutputStreamWriter(outputStream);
	            
	            outputStreamWriter.write(paramData.toString());
	            outputStreamWriter.flush();
	            
	            if (httpURLConnection.getResponseCode() >= 300) {
	                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
	            }
	            
	            inputStream = httpURLConnection.getInputStream();
	            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
	            reader = new BufferedReader(inputStreamReader);
	            
	            while ((tempLine = reader.readLine()) != null) {
	                resultBuffer.append(tempLine);
	            }
	        } catch (Exception e) {
	        	if (e.getCause() != null) {
	        		throw new Exception(e.getCause().getMessage());
	        	}
	        	throw e;
	        } finally {
	            
	            if (outputStreamWriter != null) {
	                outputStreamWriter.close();
	            }
	            
	            if (outputStream != null) {
	                outputStream.close();
	            }
	            
	            if (reader != null) {
	                reader.close();
	            }
	            
	            if (inputStreamReader != null) {
	                inputStreamReader.close();
	            }
	            
	            if (inputStream != null) {
	                inputStream.close();
	            }
	            
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            	throw e2;
            }
		}
	}
}
