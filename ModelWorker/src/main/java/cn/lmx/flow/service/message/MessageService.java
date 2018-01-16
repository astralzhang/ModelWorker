package cn.lmx.flow.service.message;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.lmx.flow.bean.design.json.AuditMessage;
import cn.lmx.flow.bean.message.AuditMessageBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.view.json.detail.DetailBean;
import cn.lmx.flow.bean.view.json.head.HeadBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.AuditDao;
import cn.lmx.flow.dao.flow.AuditHistoryDao;
import cn.lmx.flow.dao.flow.FlowInstanceDao;
import cn.lmx.flow.dao.flow.ProxyUserDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.module.UserDao;
import cn.lmx.flow.dao.view.PublishedViewDao;
import cn.lmx.flow.entity.flow.FlowInstance;
import cn.lmx.flow.entity.module.User;
import cn.lmx.flow.entity.proxy.ProxyUsers;
import cn.lmx.flow.entity.view.PublishedViews;
import cn.lmx.flow.utils.AuditMessageUtils;
import cn.lmx.flow.utils.SqlUtils;

@Repository("MessageService")
public class MessageService {
	//代理人
	@Resource(name="ProxyUserDao")
	private ProxyUserDao proxyUserDao;
	//审批人
	@Resource(name="AuditDao")
	private AuditDao auditDao;
	//审批历史
	@Resource(name="AuditHistoryDao")
	private AuditHistoryDao auditHistoryDao;
	//流程实例
	@Resource(name="FlowInstanceDao")
	private FlowInstanceDao flowInstanceDao;
	//画面显示
	@Resource(name="PublishedViewDao")
	private PublishedViewDao publishedViewDao;
	//BusinessSql执行
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSQLDao;
	//SQL文执行
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//用户
	@Resource(name="UserDao")
	private UserDao userDao;
	/**
	 * 取得需审批一栏
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<AuditMessageBean> getAuditList(String userNo, String voucherType, String status, String auditMessage) throws Exception {
		//取得被代理人
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (status == null || "".equals(status)) {
			status = "0";
		}
		if (!"0".equals(status)) {
			return null;
		}
		try {
			//取得用户一栏
			List<?> userList = userDao.list();
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			if (userList != null) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User)userList.get(i);
					UserBean userBean = new UserBean();
					BeanUtils.copyProperties(user, userBean);
					userBeanList.add(userBean);
				}
			}
			AuditMessageUtils messageUtils = new AuditMessageUtils(userBeanList);
			List<?> proxyList = proxyUserDao.getUserByProxyUser(userNo, sdf.format(c.getTime()));
			List<AuditMessageBean> auditList = new ArrayList<AuditMessageBean>();
			if (proxyList != null && proxyList.size() > 0) {
				//有被代理人
				for (int i = 0; i < proxyList.size(); i++) {
					ProxyUsers proxyUser = (ProxyUsers)proxyList.get(i);
					if (voucherType != null && !"".equals(voucherType)) {
						if (!voucherType.equals(proxyUser.getVoucherType())) {
							//单据类型不符合
							continue;
						}
					}
					List<?> tempList = auditDao.getAuditByUserNo(proxyUser.getUserNo(), proxyUser.getVoucherType());
					if (tempList == null || tempList.size() <= 0) {
						continue;
					}
					for (int j = 0; j < tempList.size(); j++) {
						AuditMessageBean audits = (AuditMessageBean)tempList.get(j);
						FlowInstance flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
						String message = createMessage(flowInstance, "Audited", "Cur", userNo, messageUtils);
						if (auditMessage != null && !"".equals(auditMessage)) {
							if (message.indexOf(auditMessage) < 0) {
								continue;
							}
						}
						audits.setMessage(message);
						auditList.add(audits);
					}					
				}
			}
			//取得需要审批一栏
			List<?> tempList = auditDao.getAuditByUserNo(userNo, voucherType);
			if (tempList != null && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					AuditMessageBean audits = (AuditMessageBean)tempList.get(i);
					FlowInstance flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
					String message = createMessage(flowInstance, "Audited", "Cur", userNo, messageUtils);
					if (auditMessage != null && !"".equals(auditMessage)) {
						if (message.indexOf(auditMessage) < 0) {
							continue;
						}
					}
					audits.setMessage(message);
					auditList.add(audits);
				}
			}
			return auditList;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 取得处理过的信息
	 * @param userNo
	 * @param voucherType
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<AuditMessageBean> getAuditedList(String userNo, String voucherType, String status, String startDate, String endDate, String auditMessage) throws Exception {
		List<AuditMessageBean> list = new ArrayList<AuditMessageBean>();
		try {
			if ("3".equals(status)) {
				//否决
				status = "1";
			}
			List<?> tempList = auditHistoryDao.getAuditsHistoryByUser(userNo, voucherType, status, startDate, endDate);
			if (tempList == null || tempList.size() <= 0) {
				return list;
			}
			//取得用户一栏
			List<?> userList = userDao.list();
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			if (userList != null) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User)userList.get(i);
					UserBean userBean = new UserBean();
					BeanUtils.copyProperties(user, userBean);
					userBeanList.add(userBean);
				}
			}
			AuditMessageUtils messageUtils = new AuditMessageUtils(userBeanList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < tempList.size(); i++) {
				AuditMessageBean bean = (AuditMessageBean)tempList.get(i);
				Date date = sdf.parse(bean.getProcessTime());
				FlowInstance flowInstance = flowInstanceDao.getInstanceById(bean.getInstanceId());
				String message = null;
				if ("1".equals(status)) {
					//否决
					message = createMessage(flowInstance, "Disagree", "Pre", userNo, messageUtils);
				} else {
					//同意
					message = createMessage(flowInstance, "Audited", "Pre", userNo, messageUtils);
				}
				if (auditMessage != null && !"".equals(auditMessage)) {
					if (message.indexOf(auditMessage) < 0) {
						continue;
					}
				}
				bean.setMessage(message);
				bean.setProcessTime(sdf2.format(date));
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 取得提交一栏
	 * @param userNo
	 * @param voucherType
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<AuditMessageBean> getSubmitedList(String userNo, String voucherType, String startDate, String endDate, String auditMessage) throws Exception {
		try {
			List<?> instanceList = flowInstanceDao.getSubmitedList(userNo, voucherType, startDate, endDate, false);
			if (instanceList == null || instanceList.size() <= 0) {
				return null;
			}
			//取得用户一栏
			List<?> userList = userDao.list();
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			if (userList != null) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User)userList.get(i);
					UserBean userBean = new UserBean();
					BeanUtils.copyProperties(user, userBean);
					userBeanList.add(userBean);
				}
			}
			AuditMessageUtils messageUtils = new AuditMessageUtils(userBeanList);
			List<AuditMessageBean> list = new ArrayList<AuditMessageBean>();
			for (int i = 0; i < instanceList.size(); i++) {
				FlowInstance flowInstance = (FlowInstance)instanceList.get(i);
				try {
					String message = createMessage(flowInstance, "Submited", "Pre", userNo, messageUtils);
					AuditMessageBean messageBean = new AuditMessageBean();
					BeanUtils.copyProperties(flowInstance, messageBean);
					if (auditMessage != null && !"".equals(auditMessage)) {
						if (message.indexOf(auditMessage) < 0) {
							continue;
						}
					}
					messageBean.setMessage(message);
					messageBean.setProcessTime(flowInstance.getCreateTime().substring(0, 8));
					list.add(messageBean);
				} catch (Exception e) {
					e.printStackTrace();
				}				
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
	 * 创建消息
	 * @param flowInstance
	 * @param status
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private String createMessage(FlowInstance flowInstance, String status, String type, String systemUser, AuditMessageUtils messageUtils) throws Exception {
		if (flowInstance == null) {
			return "";
		}
		if (status == null || "".equals(status)) {
			return "";
		}
		if (type == null || "".equals(type)) {
			return "";
		}
		Gson gson = new Gson();
		List<AuditMessage> messageList = gson.fromJson(flowInstance.getMessage(), new TypeToken<List<AuditMessage>>(){}.getType());
		if (messageList == null || messageList.size() <= 0) {
			return "";
		}
		String sMessage = null;
		String messageType = null;
		for (int i = 0; i < messageList.size(); i++) {
			AuditMessage message = messageList.get(i);
			if (status.equals(message.getStatus()) && type.equals(message.getType())) {
				sMessage = message.getMessage();
				messageType = message.getMessageType();
				break;
			}
		}
		if (sMessage == null || "".equals(sMessage)) {
			return "";
		}
		sMessage = URLDecoder.decode(sMessage, "UTF-8");
		PublishedViews publishedView = publishedViewDao.getViewByNo(flowInstance.getViewNo());
		if (publishedView == null) {
			throw new Exception("对应画面数据不存在！");
		}
		//取得HeadScript
		String headScript = publishedView.getHeadScript();
		if (headScript == null || "".equals(headScript)) {
			throw new Exception("画面设定不正确！");
		}
		HeadBean headBean = gson.fromJson(headScript, HeadBean.class);
		if (headBean == null) {
			throw new Exception("画面设定不正确！");
		}
		String detailScript = publishedView.getDetailScript();
		if (detailScript == null || "".equals(detailScript)) {
			throw new Exception("画面设定不正确！");
		}
		List<DetailBean> detailBeanList = gson.fromJson(publishedView.getDetailScript(), new TypeToken<List<DetailBean>>(){}.getType());
		if (detailBeanList == null) {
			throw new Exception("画面设定不正确！");
		}
		//取得单据数据
		Map<String, Object> formData = SqlUtils.getDbData(headBean, detailBeanList, flowInstance.getDataKey(), businessSQLDao, sqlDao);
		Map<String, Object> headDataMap = null;
		Map<String, List<Map<String, Object>>> detailDataMap = null;
		if (formData == null || formData.size() <= 0) {
			headDataMap = new HashMap<String, Object>();
			detailDataMap = new HashMap<String, List<Map<String, Object>>>();
		} else {
			headDataMap = (Map<String, Object>)formData.get("headData");
			detailDataMap = (Map<String, List<Map<String, Object>>>)formData.get("detailData");
		}
		if (headDataMap == null || detailDataMap == null) {
			throw new Exception("单据已被删除！");
		}
		//取得审批历史
		String sql = "select * from FlowInstance where id=:id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", flowInstance.getId());
		List<?> flowInstanceList = sqlDao.select(sql, paramMap);
		Map<String, Object> flowInstanceMap = null;
		if (flowInstanceList == null || flowInstanceList.size() <= 0) {
			flowInstanceMap = new HashMap<String, Object>();
		} else {
			flowInstanceMap = (Map<String, Object>)flowInstanceList.get(0);
		}
		sql = "select * from AuditsHistory where InstanceId=:instanceId order by CreateTime desc";
		paramMap = new HashMap<String, Object>();
		paramMap.put("instanceId", flowInstance.getId());
		List<?> historyList = sqlDao.select(sql, paramMap);
		Map<String, Object> historyMap = null;
		if (historyList == null || historyList.size() <= 0) {
			historyMap = new HashMap<String, Object>();
		} else {
			historyMap = (Map<String, Object>)historyList.get(0);
		}
		//去除所有的服务端数据
		int iPos = sMessage.indexOf("${");
		while (iPos >= 0) {
			int lastIndex = sMessage.indexOf("}", iPos);
			if (lastIndex > 0) {
				String befData = sMessage.substring(0, iPos);
				String data = sMessage.substring(iPos + "${".length(), lastIndex);
				String bakData = sMessage.substring(lastIndex + 1);
				int dotIndex = data.indexOf(".");
				if (dotIndex < 0) {
					sMessage = new StringBuffer("")
							.append(befData)
							.append(data)
							.append(bakData).toString();
				} else {
					String tableId = data.substring(0, dotIndex);
					String fieldId = data.substring(dotIndex + 1);
					if (tableId == null || "".equals(tableId)) {
						sMessage = new StringBuffer("")
								.append(befData)
								.append(data)
								.append(bakData).toString();
					} else if (fieldId == null || "".equals(fieldId)) {
						sMessage = new StringBuffer("")
								.append(befData)
								.append(data)
								.append(bakData).toString();
					} else {
						if (tableId.equals(headBean.getTable())) {
							sMessage = new StringBuffer("")
									.append(befData)
									.append(headDataMap.get(fieldId) == null ? "" : headDataMap.get(fieldId))
									.append(bakData).toString();
						} else if ("FlowInstance".equals(tableId)) {
							sMessage = new StringBuffer("")
									.append(befData)
									.append(flowInstanceMap.get(fieldId) == null ? "" : flowInstanceMap.get(fieldId))
									.append(bakData).toString();
						} else if ("AuditsHistory".equals(tableId)) {
							sMessage = new StringBuffer("")
									.append(befData)
									.append(flowInstanceMap.get(fieldId) == null ? "" : historyMap.get(fieldId))
									.append(bakData).toString();
						} else {
							List<?> detailDataList = detailDataMap.get(tableId);
							if (detailDataList == null || detailDataList.size() <= 0) {
								sMessage = new StringBuffer("")
										.append(befData)
										.append(data)
										.append(bakData).toString();
								iPos = sMessage.indexOf("${");
								continue;
							}
							Map<String, Object> detailMap = (Map<String, Object>)detailDataList.get(0);
							if (detailMap == null || detailMap.size() <= 0) {
								sMessage = new StringBuffer("")
										.append(befData)
										.append(data)
										.append(bakData).toString();
								iPos = sMessage.indexOf("${");
								continue;
							}
							sMessage = new StringBuffer("")
									.append(befData)
									.append(detailMap.get(fieldId) == null ? "" : detailMap.get(fieldId))
									.append(bakData).toString();
						}
					}
				}
			} else {
				return sMessage;
			}
			iPos = sMessage.indexOf("${");
		}
		if (!"Script".equals(messageType)) {
			return sMessage;
		}
		try {
			//执行脚本
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			engine.put("MessageUtils", messageUtils);
			String functionName = new StringBuffer("")
					.append("messageFunc_")
					.append(UUID.randomUUID().toString().replaceAll("-", "")).toString();
			String funcScript = new StringBuffer("")
					.append("function ")
					.append(functionName)
					.append("(){")
					.append(sMessage)
					.append("}").toString();
			engine.eval(funcScript);
			if (engine instanceof Invocable) {
				Invocable invocable = (Invocable)engine;
				Object result = invocable.invokeFunction(functionName);
				if (result == null) {
					return sMessage;
				}
				return String.valueOf(result);
			}
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sMessage;
		}
		return sMessage;
	}
}
