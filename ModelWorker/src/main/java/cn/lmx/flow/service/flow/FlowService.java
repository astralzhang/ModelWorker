package cn.lmx.flow.service.flow;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
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
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.lmx.flow.bean.SQL.FieldBean;
import cn.lmx.flow.bean.SQL.SqlProcBean;
import cn.lmx.flow.bean.design.flow.AuditNode;
import cn.lmx.flow.bean.design.flow.ConditionNode;
import cn.lmx.flow.bean.design.flow.EndNode;
import cn.lmx.flow.bean.design.flow.Node;
import cn.lmx.flow.bean.design.json.Flow;
import cn.lmx.flow.bean.design.json.ProcessSql;
import cn.lmx.flow.bean.flow.AuditUserBean;
import cn.lmx.flow.bean.flow.DatabaseBean;
import cn.lmx.flow.bean.message.AuditMessageBean;
import cn.lmx.flow.bean.view.AttachmentBean;
import cn.lmx.flow.bean.view.json.detail.DetailBean;
import cn.lmx.flow.bean.view.json.detail.PropertyBean;
import cn.lmx.flow.bean.view.json.head.CellBean;
import cn.lmx.flow.bean.view.json.head.CellPropertyBean;
import cn.lmx.flow.bean.view.json.head.HeadBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.AuditDao;
import cn.lmx.flow.dao.flow.AuditHistoryDao;
import cn.lmx.flow.dao.flow.FlowInstanceDao;
import cn.lmx.flow.dao.flow.PublishedFlowDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.flow.TableFieldDao;
import cn.lmx.flow.dao.flow.VoucherTableDao;
import cn.lmx.flow.dao.flow.VoucherTypeDao;
import cn.lmx.flow.dao.module.UserDao;
import cn.lmx.flow.dao.view.AttachmentDao;
import cn.lmx.flow.dao.view.PublishedViewDao;
import cn.lmx.flow.entity.flow.Audits;
import cn.lmx.flow.entity.flow.AuditsHistory;
import cn.lmx.flow.entity.flow.FlowInstance;
import cn.lmx.flow.entity.flow.Flows;
import cn.lmx.flow.entity.flow.PublishedFlows;
import cn.lmx.flow.entity.flow.TableFields;
import cn.lmx.flow.entity.flow.VoucherTables;
import cn.lmx.flow.entity.flow.VoucherTypes;
import cn.lmx.flow.entity.module.User;
import cn.lmx.flow.entity.view.Attachments;
import cn.lmx.flow.entity.view.PublishedViews;
import cn.lmx.flow.utils.FlowUtils;
import cn.lmx.flow.utils.SqlUtils;
@Repository("FlowService")
public class FlowService {
	//FlowDao
	/*
	@Resource(name="FlowDao")
	private FlowDao flowDao;
	*/
	@Resource(name="PublishedFlowDao")
	private PublishedFlowDao publishedFlowDao;
	//单据类型
	@Resource(name="VoucherTypeDao")
	private VoucherTypeDao voucherTypeDao;
	//单据对应表
	@Resource(name="VoucherTableDao")
	private VoucherTableDao voucherTableDao;
	//表对应字段
	@Resource(name="TableFieldDao")
	private TableFieldDao tableFieldDao;
	//审核人
	@Resource(name="AuditDao")
	private AuditDao auditDao;
	//流程实例
	@Resource(name="FlowInstanceDao")
	private FlowInstanceDao flowInstanceDao;
	//审批履历
	@Resource(name="AuditHistoryDao")
	private AuditHistoryDao auditHistoryDao;
	//画面显示
	@Resource(name="PublishedViewDao")
	private PublishedViewDao publishedViewDao;
	//附件
	@Resource(name="AttachmentDao")
	private AttachmentDao attachmentDao;
	//BusinessSql执行
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSQLDao;
	//SQL文执行
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//用户一栏
	@Resource(name="UserDao")
	private UserDao userDao;
	/**
	 * 单据提交
	 * @param voucherType
	 * @param dataKey
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> submit(String systemUser, String voucherType, String[] dataKeys, String viewNo) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			if (dataKeys == null || dataKeys.length <= 0) {
				return resultMap;
			}
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(viewNo);
			if (publishedView == null) {
				throw new Exception("对应画面数据不存在！");
			}
			//取得HeadScript
			String headScript = publishedView.getHeadScript();
			if (headScript == null || "".equals(headScript)) {
				throw new Exception("画面设定不正确！");
			}
			Gson gson = new Gson();
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
			for (int i = 0; i < dataKeys.length; i++) {
				String dataKey = dataKeys[i];
				if (dataKey == null || "".equals(dataKey)) {
					//没有指定数据主键
					continue;
				}
				//取得流程实例
				List<?> flowInstanceList = flowInstanceDao.getInstanceByDataKey(dataKey);
				if (flowInstanceList != null && flowInstanceList.size() > 0) {
					throw new Exception("流程已提交，请勿重复提交！");
				}
				//取得单据数据
				Map<String, Object> formData = SqlUtils.getDbData(headBean, detailBeanList, dataKey, businessSQLDao, sqlDao);
				//执行提交前存储过程
				SqlUtils.runProc(systemUser, headBean, formData, "BeforeSubmit", businessSQLDao, sqlDao);
				//取得单据类型数据
				VoucherTypes objVoucherType = voucherTypeDao.getVoucherTypeById(voucherType);
				//将单据数据复制导本地
				Map<String, String> tableMap = new HashMap<String, String>();
				Map<String, Map<String, Object>> dataMap = createLocalVoucherData(objVoucherType, dataKey, tableMap);
				if (dataMap == null || dataMap.size() <= 0) {
					dataMap = new HashMap<String, Map<String, Object>>();
					createDataMap(dataMap, formData);
				}
				//取得流程数据
				Flows flows = new Flows();
				Node startNode = getFlowNode(objVoucherType, flows);
				//生成流程实例ID
				String instanceId = UUID.randomUUID().toString();
				//执行流程
				processFlow(startNode, objVoucherType, dataKey, tableMap, dataMap, instanceId, new BigDecimal(1), systemUser);
				//提交处理SQL
				processSubmitSQL(systemUser, objVoucherType, dataMap);
				//保存流程实例
				saveFlowInstance(systemUser, instanceId, dataKey, flows, objVoucherType, viewNo);
				//执行提交后存储过程
				SqlUtils.runProc(systemUser, headBean, formData, "BackSubmit", businessSQLDao, sqlDao);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
		return resultMap;		
	}
	/**
	 * 取得数据
	 * @param dataMap
	 * @param formData
	 */
	private void createDataMap(Map<String, Map<String, Object>> dataMap, Map<String, Object> formData) {
		if (dataMap == null) {
			return;
		}
		if (formData == null || formData.size() <= 0) {
			return;
		}
		Map<String, Object> headData = (Map<String, Object>)formData.get("headData");
		if (headData == null || headData.size() <= 0) {
			return;
		}
		Iterator<Entry<String, Object>> it = headData.entrySet().iterator();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			tempMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		dataMap.put("headData", tempMap);
		//取明细数据
		Map<String, Object> detailData = (Map<String, Object>)formData.get("detailData");
		if (detailData == null || detailData.size() <= 0) {
			return;
		}
		String detailTable = null;
		Map<String, Object> detailDataMap = null;
		it = detailData.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			detailTable = entry.getKey();
			List<Map<String, Object>> list = (List<Map<String, Object>>)entry.getValue();
			if (list == null || list.size() <= 0) {
				continue;
			}
			detailDataMap = list.get(0);
			if (detailDataMap == null || detailDataMap.size() <= 0) {
				continue;
			}
			Iterator<Entry<String, Object>> it2 = detailDataMap.entrySet().iterator();
			tempMap = new HashMap<String, Object>();
			while (it2.hasNext()) {
				Entry<String, Object> entry2 = it2.next();
				tempMap.put(entry2.getKey().toLowerCase(), entry2.getValue());
			}
			dataMap.put(detailTable.toLowerCase(), tempMap);
		}
	}
	/**
	 * 根据实例ID取得查审数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<AuditUserBean> viewAudit(String id, String idType) throws Exception {
		//取得实例数据
		FlowInstance flowInstance = null;
		if (idType == null || "".equals(idType) || "message".equals(idType)) {
			flowInstance = flowInstanceDao.getInstanceById(id);
		} else {
			List<?> flowInstanceList = flowInstanceDao.getInstanceByDataKey(id);
			if (flowInstanceList == null || flowInstanceList.size() <= 0) {
				throw new Exception("流程没有实例化！");
			}
			flowInstance = (FlowInstance)flowInstanceList.get(0);
		}
		if (flowInstance == null) {
			throw new Exception("没有找到流程实例！");
		}
		//取得审核人员一栏
		List<?> userList = userDao.list();
		if (userList == null || userList.size() <= 0) {
			throw new Exception("没有系统用户！");
		}
		List<?> historyList = auditHistoryDao.getAuditedListByInstance(flowInstance.getId());
		//审批列表
		List<?> auditList = auditDao.getAuditListByInstance(flowInstance.getId(), null);
		if (auditList == null || auditList.size() <= 0) {
			throw new Exception("没有取得审核人员！");
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		List<AuditUserBean> list = new ArrayList<AuditUserBean>();
		//处理已审批列表
		if (historyList != null && historyList.size() > 0) {
			for (int i = 0; i < historyList.size(); i++) {
				AuditsHistory history = (AuditsHistory)historyList.get(i);
				//查找相同Level的所有审批人
				String auditId = history.getAuditsId();
				if (auditId == null || "".equals(auditId)) {
					//没有审批ID
					continue;
				}
				Audits audit = null;
				for (int j = 0; j < auditList.size(); j++) {
					Audits audit1 = (Audits)auditList.get(j);
					if (auditId.equals(audit1.getId())) {
						audit = audit1;
						break;
					}
				}
				if (audit == null) {
					//没有找到对应的审批人
					continue;
				}
				if (audit.getLevel() == null) {
					continue;
				}
				AuditUserBean userBean = new AuditUserBean();
				for (int j = 0; j < auditList.size(); j++) {
					Audits audit1 = (Audits)auditList.get(j);
					if (audit1.getLevel() == null) {
						continue;
					}
					if (audit.getLevel().intValue() != audit1.getLevel().intValue()) {
						//非相同阶层的审核
						continue;
					}
					userBean.setUserNo(userBean.getUserNo() == null ? audit1.getUserNo() : userBean.getUserNo() + "," + audit1.getUserNo());
					if (userBean.getRealUserNo() == null || "".equals(userBean.getRealUserNo())) {
						userBean.setRealUserNo(history.getRealUserNo());
					}
					if ("0".equals(history.getStatus())) {
						//审核取消
						userBean.setAuditStatus("3");
						userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
					} else if ("1".equals(history.getStatus())) {
						userBean.setAuditStatus("1");
						userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
					} else if ("2".equals(history.getStatus())) {
						userBean.setAuditStatus("2");
						userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
					} else if ("3".equals(history.getStatus())) {
						userBean.setAuditStatus("3");
						userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
					}
					userBean.setUserStatus(history.getAuditProp());					
				}
				list.add(userBean);
			}
		}
		//处理未审批列表
		int level = -1;
		for (int i = 0; i < auditList.size(); i++) {
			Audits audit = (Audits)auditList.get(i);
			if (audit == null) {
				continue;
			}
			if (!"0".equals(audit.getStatus())) {
				//非未审核数据跳过
				continue;
			}
			BigDecimal auditLevel = audit.getLevel();
			if (auditLevel == null) {
				continue;
			}
			if (level < 0) {
				level = auditLevel.intValue();
				list.add(new AuditUserBean());
			}
			if (level == auditLevel.intValue()) {
				//相同审批层
				AuditUserBean userBean = list.get(list.size() - 1);
				userBean.setUserNo(userBean.getUserNo() == null ? audit.getUserNo() : userBean.getUserNo() + "," + audit.getUserNo());
				if (audit.getRealUserNo() != null) {
					userBean.setRealUserNo(audit.getRealUserNo());
				}
				if ("0".equals(audit.getStatus())) {
					userBean.setAuditStatus("0");
					userBean.setAuditTime("");
				} else if ("1".equals(audit.getStatus())) {
					userBean.setAuditStatus("1");
					for (int j = 0; j < historyList.size(); j++) {
						AuditsHistory history = (AuditsHistory)historyList.get(j);
						BigDecimal historyLevel = history.getLevel();
						if (historyLevel == null) {
							continue;
						}
						if (level == historyLevel.intValue()) {
							userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
							break;
						}
					}
				} else if ("2".equals(audit.getStatus())) {
					userBean.setAuditStatus("2");
					for (int j = 0; j < historyList.size(); j++) {
						AuditsHistory history = (AuditsHistory)historyList.get(j);
						BigDecimal historyLevel = history.getLevel();
						if (historyLevel == null) {
							continue;
						}
						if (level == historyLevel.intValue()) {
							userBean.setAuditTime(history.getCreateTime());
							break;
						}
					}
				}
				userBean.setUserStatus(audit.getAuditProp());
			} else {
				AuditUserBean userBean = new AuditUserBean();
				userBean.setUserNo(audit.getUserNo());
				if (audit.getRealUserNo() != null) {
					userBean.setRealUserNo(audit.getRealUserNo());
				}
				if ("0".equals(audit.getStatus())) {
					userBean.setAuditStatus("0");
					userBean.setAuditTime("");
				} else if ("1".equals(audit.getStatus())) {
					userBean.setAuditStatus("1");
					for (int j = 0; j < historyList.size(); j++) {
						AuditsHistory history = (AuditsHistory)historyList.get(j);
						BigDecimal historyLevel = history.getLevel();
						if (historyLevel == null) {
							continue;
						}
						if (level == historyLevel.intValue()) {
							userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
							break;
						}
					}
				} else if ("2".equals(audit.getStatus())) {
					userBean.setAuditStatus("2");
					for (int j = 0; j < historyList.size(); j++) {
						AuditsHistory history = (AuditsHistory)historyList.get(j);
						BigDecimal historyLevel = history.getLevel();
						if (historyLevel == null) {
							continue;
						}
						if (level == historyLevel.intValue()) {
							userBean.setAuditTime(sdf2.format(sdf1.parse(history.getCreateTime())));
							break;
						}
					}
				}
				userBean.setUserStatus(audit.getAuditProp());
				list.add(userBean);
			}
		}
		//设定用户名称
		for (int i = 0; i < list.size(); i++) {
			AuditUserBean userBean = list.get(i);
			String userNo = userBean.getUserNo();
			if (userNo == null || "".equals(userNo)) {
				continue;
			}
			String[] arrUserNo = userNo.split(",");
			StringBuffer sb = new StringBuffer("");
			for (int j = 0; j < arrUserNo.length; j++) {
				String sNo = arrUserNo[j];
				if (sNo == null || "".equals(sNo)) {
					continue;
				}
				for (int m = 0; m < userList.size(); m++) {
					User user = (User)userList.get(m);
					if (sNo.equals(user.getUserNo())) {
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(user.getUserName());
						break;
					}
				}
			}
			userBean.setUserName(sb.toString());
			//设定实际处理用户名称
			String realUserNo = userBean.getRealUserNo();
			if (realUserNo == null || "".equals(realUserNo)) {
				continue;
			}
			for (int j = 0; j < userList.size(); j++) {
				User user = (User)userList.get(j);
				if (realUserNo.equals(user.getUserNo())) {
					userBean.setRealUserName(user.getUserName());
					break;
				}
			}
		}
		/*List<?> auditList = null;
		List<List<AuditUserBean>> list = new ArrayList<List<AuditUserBean>>();
		if (historyList == null || historyList.size() <= 0) {
			//没有已审批消息
			auditList = auditDao.getAuditListByInstance(id, null);
		} else {
			auditList = auditDao.getAuditListByInstance(id, flowInstance.getCurrentLevel());
		}
		if (historyList != null) {
			for (int i = 0; i < historyList.size(); i++) {
				AuditsHistory history = (AuditsHistory)historyList.get(i);
				AuditUserBean userBean = new AuditUserBean();
				userBean.setUserNo(history.getUserNo());
				userBean.setRealUserNo(history.getRealUserNo());
				userBean.setUserStatus(history.getAuditProp());
				userBean.setAuditStatus("0".equals(history.getStatus()) ? "4" : history.getStatus());
				userBean.setAuditTime(history.getCreateTime());
				List<AuditUserBean> tempList = new ArrayList<AuditUserBean>();
				tempList.add(userBean);
				list.add(tempList);
			}
		}
		if (auditList != null) {
			BigDecimal level = null;
			List<AuditUserBean> tempList = null;
			for (int i = 0; i < auditList.size(); i++) {
				Audits audit = (Audits)auditList.get(i);
				if (level == null || level.compareTo(audit.getLevel()) != 0) {
					tempList = new ArrayList<AuditUserBean>();
					list.add(tempList);
					AuditUserBean userBean = new AuditUserBean();
					userBean.setUserNo(audit.getUserNo());
					userBean.setUserStatus(audit.getAuditProp());
					userBean.setAuditStatus("0");
					tempList.add(userBean);
					level = new BigDecimal(audit.getLevel().intValue());
				} else {
					AuditUserBean userBean = new AuditUserBean();
					userBean.setUserNo(audit.getUserNo());
					userBean.setUserStatus(audit.getAuditProp());
					userBean.setAuditStatus("0");
					tempList.add(userBean);
				}
			}
		}*/
		return list;
	}
	/**
	 * 画面查看
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> view(String id, String status) throws Exception {
		if (id == null || "".equals(id)) {
			return null;
		}
		//取得流程实例
		String content = "";
		FlowInstance flowInstance = null;
		if (status == null || "".equals(status) || "0".equals(status)) {
			//未审核
			Audits audits = auditDao.getAuditById(id);
			if (audits == null) {
				throw new Exception("没有找到单据！");
			}
			content = audits.getBeforeContent();
			flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
		} else if ("1".equals(status)) {
			//我提交
			flowInstance = flowInstanceDao.getInstanceById(id);
			if (flowInstance == null) {
				throw new Exception("没有找到对应的工作流实例！");
			}
			List<?> historyList = auditHistoryDao.getAuditedListByInstance(flowInstance.getId());
			if (historyList != null && historyList.size() > 0) {
				AuditsHistory history = (AuditsHistory)historyList.get(historyList.size() - 1);
				if (history != null) {
					content = history.getContent();
				}
			}
		} else if ("2".equals(status) || "3".equals(status)) {
			//我同意或我否决
			AuditsHistory history = auditHistoryDao.getAuditsHistoryById(id);
			if (history == null) {
				throw new Exception("没有找到单据！");
			}
			content = history.getContent();
			flowInstance = flowInstanceDao.getInstanceById(history.getInstanceId());
		}
		if (flowInstance == null) {
			throw new Exception("没有找到对应的工作流实例！");
		}
		/*VoucherTypes objVoucherType = voucherTypeDao.getVoucherTypeById(audits.getVoucherType());
		if (objVoucherType == null) {
			throw new Exception("没有找到对应的单据类型！");
		}
		String viewScript = objVoucherType.getViewScript();
		if (viewScript == null || "".equals(viewScript)) {
			throw new Exception("没有设定单据显示脚本！");
		}*/
		String viewNo = flowInstance.getViewNo();
		if (viewNo == null || "".equals(viewNo)) {
			//没有设定画面编码
			throw new Exception("画面显示方法没有设定！");
		}
		PublishedViews publishedView = publishedViewDao.getViewByNo(viewNo);
		if (publishedView == null) {
			//没有设定画面编码
			throw new Exception("画面显示方法没有设定！");
		}
		/*
		Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
		if (publishedView.getHeadScript() == null || "".equals(publishedView.getHeadScript())) {
			throw new Exception("画面显示方法设定不正确！");
		}

		HeadBean headBean = gson.fromJson(publishedView.getHeadScript(), HeadBean.class);
		if (headBean == null) {
			throw new Exception("单据显示设定不正确！");
		}
		result.put("head", headBean);
		if (publishedView.getDetailScript() != null && !"".equals(publishedView.getDetailScript())) {
			DetailBean detailBean = gson.fromJson(publishedView.getDetailScript(), DetailBean.class);
			result.put("detail", detailBean);
		}
		*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("viewNo", viewNo);
		map.put("content", content);
		//查询Head数据
		String headScript = publishedView.getHeadScript();
		if (headScript == null || "".equals(headScript)) {
			throw new Exception("画面设定错误，没有指定Header！");
		}
		Gson gson = new Gson();
		HeadBean headBean = gson.fromJson(headScript, HeadBean.class);
		if (headBean == null) {
			throw new Exception("画面设定错误，Header不正确！");
		}
		map.put("head", headBean);
		List<?> headDataList = null;
		//做成Head用SQL
		if (headBean.getSql() == null || "".equals(headBean.getSql())) {
			//没有设定SQL文，自己创建
			StringBuffer fields = new StringBuffer("");
			if (headBean.getTable() == null || "".equals(headBean.getTable())) {
				throw new Exception("画面Header设定错误，没有指定数据表！");
			}
			List<CellBean> cellList = headBean.getCells();
			if (cellList == null || cellList.size() <= 0) {
				throw new Exception("画面Header设定错误，没有设定字段！");
			}
			for (int i = 0; i < cellList.size(); i++) {
				CellBean cellBean = cellList.get(i);
				if (cellBean == null) {
					continue;
				}
				CellPropertyBean propBean = cellBean.getProps();
				if (propBean == null) {
					continue;
				}
				if ("openEditor".equals(cellBean.getType())) {
					if (propBean.getNoField() != null && !"".equals(propBean.getNoField())) {
						fields.append(propBean.getNoField()).append(",");
					}
					if (propBean.getNameField() != null && !"".equals(propBean.getNameField())) {
						fields.append(propBean.getNameField()).append(",");
					}
				} else {
					if (propBean.getField() == null || "".equals(propBean.getField())) {
						continue;
					}
					fields.append(propBean.getField())
						.append(",");
				}
			}
			if (fields.length() <= 0) {
				throw new Exception("画面Header设定错误，没有设定字段！");
			}
			String tableKey = headBean.getTableKey();
			if (tableKey == null || "".equals(tableKey)) {
				tableKey = "ID";
			}
			StringBuffer sql = new StringBuffer("")
						.append("select\n")
						.append(fields.substring(0, fields.length() - 1)).append("\n")
						.append("from\n")
						.append(headBean.getTable()).append("\n")
						.append("where\n")
						.append(tableKey).append("=:id\n");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", flowInstance.getDataKey());			
			if ("R".equals(headBean.getTarget())) {
				headDataList = businessSQLDao.select(sql.toString(), paramMap);
			} else {
				headDataList = sqlDao.select(sql.toString(), paramMap);
			}
			if (headDataList == null || headDataList.size() <= 0) {
				throw new Exception("您指定的数据不存在，或已被他人修改，请搜索后重试！");
			}
		} else {
			//设定有SQL文
			String sql = URLDecoder.decode(headBean.getSql(), "UTF-8");
			//if ("S".equals(headBean.getSql())) {
				//SQL文
			String tableKey = headBean.getTableKey();
			if (tableKey == null || "".equals(tableKey)) {
				tableKey = "ID";
			}
			sql = sql.replaceAll("\\$condition\\$", tableKey + "=:id");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", flowInstance.getDataKey());
			if ("R".equals(headBean.getTarget())) {
				//远程数据库
				headDataList = businessSQLDao.select(sql, paramMap);
				if (headDataList == null || headDataList.size() <= 0) {
					throw new Exception("您指定的数据不存在，或已被他人修改，请搜索后重试！");
				}
			} else {
				//本地数据库
				headDataList = sqlDao.select(sql, paramMap);
				if (headDataList == null || headDataList.size() <= 0) {
					throw new Exception("您指定的数据不存在，或已被他人修改，请搜索后重试！");
				}
			}
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("headData", gson.toJson(headDataList.get(0)));
		//取得附件信息
		List<?> attaList = attachmentDao.list(viewNo, flowInstance.getDataKey());
		if (attaList == null) {
			map.put("attachment", null);
		} else {
			List<AttachmentBean> list = new ArrayList<AttachmentBean>();
			for (int i = 0; i < attaList.size(); i++) {
				Attachments atta = (Attachments)attaList.get(i);
				AttachmentBean attaBean = new AttachmentBean();
				BeanUtils.copyProperties(atta, attaBean);
				list.add(attaBean);
			}
			map.put("attachment", list);
		}
		//明细处理
		String detailScript = publishedView.getDetailScript();
		if (detailScript == null || "".equals(detailScript)) {
			map.put("data", dataMap);
			return map;
		}
		List<DetailBean> detailList = gson.fromJson(detailScript, new TypeToken<ArrayList<DetailBean>>(){}.getType());
		if (detailList == null) {
			map.put("data", dataMap);
			return map;
		}
		map.put("detail", detailList);
		Map<String, Object> detailDataMap = getDetailData(detailList, flowInstance.getDataKey(), (Map<String, Object>)headDataList.get(0), headBean.getTable());
		if (detailDataMap == null || detailDataMap.size() <= 0) {
			map.put("data", dataMap);
			return map;
		}
		Iterator<Entry<String, Object>> it = detailDataMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			if (entry.getValue() != null) {
				dataMap.put(entry.getKey(), gson.toJson(entry.getValue()));
			}
		}
		map.put("data", dataMap);
		return map;
	}
	/**
	 * 明细数据取得
	 * @param detailList
	 * @param id
	 * @param headData
	 * @param headTableId
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getDetailData(List<DetailBean> detailList, String id, Map<String, Object> headData, String headTableId) throws Exception {
		if (detailList == null || detailList.size() <= 0) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (int i = 0; i < detailList.size(); i++) {
			DetailBean detailBean = detailList.get(i);
			if (detailBean.getProcess() == null) {
				throw new Exception("画面明细设定错误，没有指定处理信息！");
			}
			String sql = detailBean.getProcess().getSql();
			if (sql == null || "".equals(sql)) {
				//没设定SQL文，自己创建SQL文
				StringBuffer fields = new StringBuffer("");
				if (detailBean.getTable() == null || "".equals(detailBean.getTable())) {
					throw new Exception("画面明细设定错误，没有指定数据表！");
				}
				List<cn.lmx.flow.bean.view.json.detail.FieldBean> fieldList = detailBean.getProcess().getFields();
				if (fieldList == null || fieldList.size() <= 0) {
					throw new Exception("画面明细设定错误，没有设定字段！");
				}
				for (int j = 0; j < fieldList.size(); j++) {
					cn.lmx.flow.bean.view.json.detail.FieldBean fieldBean = fieldList.get(j);
					if (fieldBean == null) {
						continue;
					}
					PropertyBean propBean = fieldBean.getProps();
					if (propBean == null) {
						continue;
					}
					if ("openEditor".equals(fieldBean.getEditor())) {
						if (propBean.getNoField() != null && !"".equals(propBean.getNoField())) {
							fields.append(propBean.getNoField()).append(",");
						}
						if (propBean.getNameField() != null && !"".equals(propBean.getNameField())) {
							fields.append(propBean.getNameField()).append(",");
						}
					} else {
						if (propBean.getField() == null || "".equals(propBean.getField())) {
							continue;
						}
						fields.append(propBean.getField())
							.append(",");
					}
				}
				if (fields.length() <= 0) {
					throw new Exception("画面明细设定错误，没有设定字段！");
				}
				StringBuffer sbSql = new StringBuffer("")
							.append("select\n")
							.append(fields)
							.append("ID as id\n")
							.append("from\n")
							.append(detailBean.getTable()).append("\n")
							.append("where\n")
							.append("ParentId=:id\n");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", id);
				List<?> detailDataList = null;
				if ("R".equals(detailBean.getProcess().getTarget())) {
					detailDataList = businessSQLDao.select(sbSql.toString(), paramMap);
				} else {
					detailDataList = sqlDao.select(sbSql.toString(), paramMap);
				}
				if (detailDataList == null || detailDataList.size() <= 0) {
					throw new Exception("您指定的数据不存在，或已被他人修改，请搜索后重试！");
				}
				resultMap.put(detailBean.getTable(), detailDataList);
			} else {
				//指定SQL文
				sql = URLDecoder.decode(sql, "UTF-8");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				Iterator<Entry<String, Object>> it = headData.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if (entry.getValue() != null) {
						paramMap.put(headTableId + "_" + entry.getKey(), entry.getValue());
					}
				}
				paramMap.put("parentId", id);
				if ("S".equals(detailBean.getProcess().getType())) {
					//SQL文
					sql = sql.replaceAll("\\$condition\\$", "ParentId=:parentId");
				}
				List<?> detailDataList = null;
				if ("R".equals(detailBean.getTable())) {
					detailDataList = businessSQLDao.select(sql, paramMap);
				} else {
					detailDataList = sqlDao.select(sql, paramMap);
				}
				resultMap.put(detailBean.getTable(), detailDataList);
			}
		}
		return resultMap;
	}
	/**
	 * 审批通过处理
	 * @param systemUser
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void audit(String systemUser, String ids, String processContent) throws Exception {
		if (ids == null || "".equals(ids)) {
			return;
		}
		String[] arrId = ids.split(",");
		if (arrId.length <= 0) {
			return;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		for (int i = 0; i < arrId.length; i++) {
			String id = arrId[i];
			//取得审核数据
			Audits audits = auditDao.getAuditById(id);
			if (audits == null) {
				throw new Exception("您没有该单据的审核权限！");
			}
			//取得实例数据
			FlowInstance flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
			if (flowInstance == null) {
				throw new Exception("流程实例不存在，请确认！");
			}
			if (audits.getLevel().compareTo(flowInstance.getCurrentLevel()) > 0) {
				throw new Exception("流程您已审核完成，无需再次审核！");
			} else if (audits.getLevel().compareTo(flowInstance.getCurrentLevel()) < 0) {
				throw new Exception("流程尚未到达您处审核！");
			}
			//取得单据类型
			VoucherTypes voucherType = voucherTypeDao.getVoucherTypeById(audits.getVoucherType());
			if (voucherType == null) {
				throw new Exception("单据类型不正确！");
			}
			//取得数据
			Map<String, Map<String, Object>> dataMap = getDataMap(voucherType, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
			//处理审核前SQL
			processAuditSQL(systemUser, audits.getProcessSql(), "BeforeAudit", dataMap);
			//取消同阶层的审核人员
			auditDao.editAuditStatusByInstanceLevel(audits.getInstanceId(), audits.getLevel(), "3", systemUser, sdf.format(c.getTime()));			
			//取消所有未审核的加签数据
			auditDao.cancelUnauditAddSignByLevel(audits.getInstanceId(), audits.getLevel(), systemUser, sdf.format(c.getTime()));
			//更新当前审核数据
			audits.setRealUserNo(systemUser);
			audits.setContent(processContent);
			audits.setStatus("2");
			audits.setUpdateTime(sdf.format(c.getTime()));
			audits.setUpdateUser(systemUser);
			auditDao.edit(audits);
			//新增审核履历
			AuditsHistory auditsHistory = new AuditsHistory();
			BeanUtils.copyProperties(audits, auditsHistory);
			auditsHistory.setAuditsId(audits.getId());
			auditsHistory.setId(UUID.randomUUID().toString());
			auditsHistory.setCreateUser(systemUser);
			auditsHistory.setCreateTime(sdf.format(c.getTime()));
			auditHistoryDao.add(auditsHistory);
			//取得下一条非加签的处理人
			List<?> auditList = auditDao.getNextNormalByLevel(audits.getInstanceId(), audits.getLevel());
			if (auditList == null || auditList.size() <= 0) {							
				//更新实例数据为完成
				flowInstance.setStatus("Y");
				flowInstance.setUpdateTime(sdf.format(c.getTime()));
				flowInstance.setUpdateUser(systemUser);
				flowInstanceDao.update(flowInstance);
				//终审
				if (voucherType.getCompleteSql() == null || "".equals(voucherType.getCompleteSql())) {
					continue;
				}
				//取得数据
				dataMap = getDataMap(voucherType, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
				//处理审核后SQL
				processAuditSQL(systemUser, audits.getProcessSql(), "AfterAudit", dataMap);
				//执行终审SQL
				this.processCompleteSQL(systemUser, voucherType, dataMap);
			} else {
				//非终审
				BigDecimal level = ((Audits)auditList.get(0)).getLevel();
				if (auditDao.editAddSignContentByLevel(audits.getInstanceId(), audits.getLevel(), level, processContent, systemUser, sdf.format(c.getTime())) <= 0) {
					//更新下一条数据的处理状态和本次的审核意见
					auditDao.editAuditContentByInstanceLevel(audits.getInstanceId(), level, processContent, "0", systemUser, sdf.format(c.getTime()));
				}
				//更新实例的当前处理层
				flowInstance.setCurrentLevel(level);
				flowInstance.setUpdateTime(sdf.format(c.getTime()));
				flowInstance.setUpdateUser(systemUser);
				flowInstanceDao.update(flowInstance);
				//取得数据
				dataMap = getDataMap(voucherType, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
				//处理审核后SQL
				processAuditSQL(systemUser, audits.getProcessSql(), "AfterAudit", dataMap);
			}
		}
	}
	/**
	 * 审批不同意
	 * @param systemUser
	 * @param ids
	 * @param processContent
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void auditDisagree(String systemUser, String ids, String processContent) throws Exception {
		if (ids == null || "".equals(ids)) {
			return;
		}
		String[] arrId = ids.split(",");
		if (arrId.length <= 0) {
			return;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		for (int i = 0; i < arrId.length; i++) {
			String id = arrId[i];
			//取得审核数据
			Audits audits = auditDao.getAuditById(id);
			//取得流程实例
			FlowInstance flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
			if (flowInstance == null) {
				//没有对应的流程实例
				//throw new Exception("单据（" + id + "）没有流程实例数据！");
				throw new Exception("单据没有流程实例数据！");
			}
			//取得单据类型
			VoucherTypes voucherTypes = voucherTypeDao.getVoucherTypeById(audits.getVoucherType());
			//取得业务数据
			Map<String, Map<String, Object>> dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
			//审核前处理SQL
			processAuditSQL(systemUser, audits.getProcessSql(), "BeforeDisagree", dataMap);
			//取消同阶层的审核人员
			auditDao.editAuditStatusByInstanceLevel(audits.getInstanceId(), audits.getLevel(), "3", systemUser, sdf.format(c.getTime()));
			//更新当前审核数据
			audits.setRealUserNo(systemUser);
			audits.setContent(processContent);
			audits.setStatus("1");
			audits.setUpdateTime(sdf.format(c.getTime()));
			audits.setUpdateUser(systemUser);
			auditDao.edit(audits);
			//新增审核履历
			AuditsHistory auditsHistory = new AuditsHistory();
			BeanUtils.copyProperties(audits, auditsHistory);
			auditsHistory.setAuditsId(audits.getId());
			auditsHistory.setId(UUID.randomUUID().toString());
			auditsHistory.setCreateUser(systemUser);
			auditsHistory.setCreateTime(sdf.format(c.getTime()));
			auditHistoryDao.add(auditsHistory);			
			//取得流程数据
			PublishedFlows flows = publishedFlowDao.getFlowByVoucherTypeVersion(flowInstance.getVoucherType(), flowInstance.getFlowVersion());
			//Flows flows = flowDao.getFlowsByVersion(flowInstance.getFlowNo(), flowInstance.getFlowVersion());
			if (flows == null) {
				//没有找到对应版本的流程
				String message = new StringBuffer("")
						.append("工作流（")
						.append(flowInstance.getFlowNo())
						.append(")的")
						.append(flowInstance.getFlowVersion())
						.append("版本不存在，请确认！").toString();
				throw new Exception(message);
			}
			if ("0".equals(audits.getAuditProp())) {
				if ("0".equals(flows.getDisagreeAudit())) {
					//直接返回给提交人
					flowInstance.setStatus("V");
					flowInstance.setUpdateUser(systemUser);
					flowInstance.setUpdateTime(sdf.format(c.getTime()));
					flowInstanceDao.update(flowInstance);
					//修改所有之前的未审核的加签数据
					auditDao.cancelUnauditAddSignByLevel(audits.getInstanceId(), audits.getLevel(), systemUser, sdf.format(c.getTime()));
					//取得数据
					dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
					//审核后处理SQL
					processAuditSQL(systemUser, audits.getProcessSql(), "BackDisagree", dataMap);
					//取消提交处理
					processSubmitCancelSQL(systemUser, voucherTypes, dataMap);
				} else if ("1".equals(flows.getDisagreeAudit())) {
					//逐级返回
					List<?> auditList = auditDao.getPreNormalAuditByInstanceLevel(audits.getInstanceId(), audits.getLevel().subtract(new BigDecimal(1)));
					if (auditList == null || auditList.size() <= 0) {
						//没有上一级审核人，已经到提交阶段
						flowInstance.setStatus("V");
						flowInstance.setUpdateUser(systemUser);
						flowInstance.setUpdateTime(sdf.format(c.getTime()));
						flowInstanceDao.update(flowInstance);
						//取消所有未审核的加签数据
						auditDao.cancelUnauditAddSignByLevel(audits.getInstanceId(), audits.getLevel(), systemUser, sdf.format(c.getTime()));
						//取得数据
						dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
						//审核后处理SQL
						processAuditSQL(systemUser, audits.getProcessSql(), "BackDisagree", dataMap);
						//取消提交处理
						processSubmitCancelSQL(systemUser, voucherTypes, dataMap);
					} else {
						//修改上一级审核人为审核中,同时修改本次的不同意理由
						BigDecimal level = ((Audits)auditList.get(0)).getLevel();
						String content = systemUser + "审核不同意，理由：" + processContent;
						auditDao.editAuditContentByInstanceLevel(audits.getInstanceId(), level, content, "0", systemUser, sdf.format(c.getTime()));
						//设定流程实例的当前审核阶层
						flowInstance.setCurrentLevel(level);
						flowInstance.setUpdateUser(systemUser);
						flowInstance.setUpdateTime(sdf.format(c.getTime()));
						flowInstanceDao.update(flowInstance);
						//取消所有未审核的加签数据
						auditDao.cancelUnauditAddSignByLevel(audits.getInstanceId(), audits.getLevel(), systemUser, sdf.format(c.getTime()));
						//取得数据
						dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
						//审核后处理SQL
						processAuditSQL(systemUser, audits.getProcessSql(), "BackDisagree", dataMap);
					}
					
				} else {
					//类型设定不正确
					continue;
				}
			} else {
				//加签人员
				//取得下一条非加签的处理人
				List<?> auditList = auditDao.getNextNormalByLevel(audits.getInstanceId(), audits.getLevel());
				if (auditList == null || auditList.size() <= 0) {
					throw new Exception("审批人列表不正确！");
				}
				BigDecimal level = ((Audits)auditList.get(0)).getLevel();
				String content = systemUser + "审核不同意，理由：" + processContent;
				if (auditDao.editAddSignContentByLevel(audits.getInstanceId(), audits.getLevel(), level, content, systemUser, sdf.format(c.getTime())) <= 0) {
					//更新下一条数据的处理状态和本次的审核意见
					auditDao.editAuditContentByInstanceLevel(audits.getInstanceId(), level, content, "0", systemUser, sdf.format(c.getTime()));
				}
				//取得数据
				dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
				//审核后处理SQL
				processAuditSQL(systemUser, audits.getProcessSql(), "BackDisagree", dataMap);
			}
		}
	}
	/**
	 * 取消审核
	 * @param systemUser
	 * @param ids
	 * @param processContent
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void auditCancel(String systemUser, String ids) throws Exception {
		if (ids == null || "".equals(ids)) {
			return;
		}
		String[] arrId = ids.split(",");
		if (arrId.length <= 0) {
			return;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		for (int i = 0; i < arrId.length; i++) {
			String id = arrId[i];
			AuditsHistory auditsHistory = auditHistoryDao.getAuditsHistoryById(id);
			if (auditsHistory == null) {
				throw new Exception("系统错误！");
			}
			//取得最后的审批消息
			AuditMessageBean lastMessageBean = auditHistoryDao.getLastAuditByInstance(auditsHistory.getInstanceId());
			if (!lastMessageBean.getId().equals(auditsHistory.getId())) {
				throw new Exception("您不是最后审批人，无法撤销审核！");
			}
			//取得审核数据
			Audits audits = auditDao.getAuditById(auditsHistory.getAuditsId());
			if (audits == null) {
				throw new Exception("系统错误！");
			}
			//取得流程实例
			FlowInstance flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
			if (flowInstance == null) {
				//没有对应的流程实例
				throw new Exception("没有流程实例数据！");
			}
			if ("V".equals(flowInstance.getStatus())) {
				//流程已取消
				throw new Exception("流程已取消，无法弃审！");
			}
			//取得单据类型
			VoucherTypes voucherTypes = voucherTypeDao.getVoucherTypeById(audits.getVoucherType());
			//取得业务数据
			Map<String, Map<String, Object>> dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
			//执行取消前SQL
			processAuditSQL(systemUser, audits.getProcessSql(), "BeforeCancelAudit", dataMap);
			//设定同阶层的审核人员为未审核
			auditDao.editAuditStatusByInstanceLevel(audits.getInstanceId(), audits.getLevel(), "0", systemUser, sdf.format(c.getTime()));
			//更新当前审核数据为未审核
			audits.setRealUserNo(systemUser);
			audits.setContent("审核取消");
			audits.setStatus("0");
			audits.setUpdateTime(sdf.format(c.getTime()));
			audits.setUpdateUser(systemUser);
			auditDao.edit(audits);			
			//新增审核履历
			AuditsHistory paramAuditsHistory = new AuditsHistory();
			BeanUtils.copyProperties(audits, paramAuditsHistory);
			//审核ID设定为元履历ID
			paramAuditsHistory.setAuditsId(auditsHistory.getAuditsId());
			paramAuditsHistory.setId(UUID.randomUUID().toString());
			paramAuditsHistory.setCreateUser(systemUser);
			paramAuditsHistory.setCreateTime(sdf.format(c.getTime()));
			auditHistoryDao.add(paramAuditsHistory);
			//取得流程数据
			//Flows flows = flowDao.getFlowsByVersion(flowInstance.getFlowNo(), flowInstance.getFlowVersion());
			PublishedFlows flows = publishedFlowDao.getFlowByVoucherTypeVersion(flowInstance.getVoucherType(), flowInstance.getFlowVersion());
			if (flows == null) {
				//没有找到对应版本的流程
				String message = new StringBuffer("")
						.append("工作流（")
						.append(flowInstance.getFlowNo())
						.append(")的")
						.append(flowInstance.getFlowVersion())
						.append("版本不存在，请确认！").toString();
				throw new Exception(message);
			}
			//取得下一个非加签的处理人,判定是否终审
			List<?> nextNormalList = auditDao.getNextNormalByLevel(audits.getInstanceId(), audits.getLevel());
			//修改实例当前处理阶层
			flowInstance.setCurrentLevel(lastMessageBean.getLevel());
			flowInstance.setStatus("N");
			flowInstance.setUpdateUser(systemUser);
			flowInstance.setUpdateTime(sdf.format(c.getTime()));
			flowInstanceDao.update(flowInstance);
			//取得数据
			dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
			//执行取消后SQL
			processAuditSQL(systemUser, audits.getProcessSql(), "BackCancelAudit", dataMap);
			if (nextNormalList == null || nextNormalList.size() <= 0) {
				//终审取消处理
				processCompleteCancelSQL(systemUser, voucherTypes, dataMap);						
			}
		}
	}
	/**
	 * 撤销提交(根据消息ID撤销提交)
	 * @param systemUser
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void submitCancel(String systemUser, String ids, String idType) throws Exception {
		if (ids == null || ids.length() <= 0) {
			return;
		}
		String[] arrId = ids.split(",");
		if (arrId.length <= 0) {
			return;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		for (int i = 0; i < arrId.length; i++) {
			String id = arrId[i];
			FlowInstance flowInstance = null;
			if (idType == null || "".equals(idType) || "message".equals(idType)) {
				//取得流程实例
				flowInstance = flowInstanceDao.getInstanceById(id);
				if (flowInstance == null) {
					throw new Exception("流程没有实例化！");
				}
			} else {
				List<?> flowInstanceList = flowInstanceDao.getInstanceByDataKey(id);
				if (flowInstanceList == null || flowInstanceList.size() <= 0) {
					throw new Exception("流程没有实例化！");
				}
				flowInstance = (FlowInstance)flowInstanceList.get(0);
			}
			List<?> preNormalAuditList = auditDao.getPreNormalAuditByInstanceLevel(flowInstance.getId(), flowInstance.getCurrentLevel().subtract(new BigDecimal(1)));
			if (preNormalAuditList != null && preNormalAuditList.size() > 0) {
				//已有人员审批，无法撤销
				//throw new Exception(flowInstance.getVoucherTypeName() + "(" + flowInstance.getDataKey() + ")审批流程已开始，无法撤销！");
				throw new Exception(flowInstance.getVoucherTypeName() + "审批流程已开始，无法撤销！");
			}
			//取得当前审核节点
			List<?> auditsList = auditDao.getNextNormalByLevel(flowInstance.getId(), new BigDecimal(0));
			if (auditsList == null || auditsList.size() <= 0) {
				//throw new Exception(flowInstance.getVoucherTypeName() + "(" + flowInstance.getDataKey() + ")没有审核人员！");
				throw new Exception(flowInstance.getVoucherTypeName() + "没有审核人员！");
			}
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(flowInstance.getViewNo());
			if (publishedView == null) {
				throw new Exception("对应画面数据不存在！");
			}
			//取得HeadScript
			String headScript = publishedView.getHeadScript();
			if (headScript == null || "".equals(headScript)) {
				throw new Exception("画面设定不正确！");
			}
			Gson gson = new Gson();
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
			//执行提交取消前存储过程
			SqlUtils.runProc(systemUser, headBean, formData, "BeforeCancelSubmit", businessSQLDao, sqlDao);
			Audits audits = (Audits)auditsList.get(0);
			//取得单据类型
			VoucherTypes voucherTypes = voucherTypeDao.getVoucherTypeById(flowInstance.getVoucherType());
			//取得业务数据
			Map<String, Map<String, Object>> dataMap = getDataMap(voucherTypes, flowInstance.getDataKey(), audits.getId(), flowInstance.getViewNo());
			//取消提交处理
			processSubmitCancelSQL(systemUser, voucherTypes, dataMap);
			//修改流程实例为已撤销
			flowInstance.setStatus("V");
			flowInstance.setUpdateUser(systemUser);
			flowInstance.setUpdateTime(sdf.format(c.getTime()));
			flowInstanceDao.update(flowInstance);
			//执行提交取消后存储过程
			SqlUtils.runProc(systemUser, headBean, formData, "BackCancelSubmit", businessSQLDao, sqlDao);
		}
	}
	/**
	 * 取得用户一栏
	 * @param systemUser
	 * @param id
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> getUserList(String systemUser, String id, Map<String, Object> map) throws Exception {
		if (id == null || "".equals(id)) {
			throw new Exception("审批ID不正确！");
		}
		//取得审核数据
		Audits audits = auditDao.getAuditById(id);
		if (audits == null) {
			//没有审批数据
			throw new Exception("没有找到对应的审批数据！");
		}
		//取得单据类型
		VoucherTypes voucherType = voucherTypeDao.getVoucherTypeById(audits.getVoucherType());
		if (voucherType == null) {
			throw new Exception("没有找到对应的单据类型！");
		}
		//取得用户一栏SQL
		String userSql = voucherType.getUserSql();
		if (userSql == null || "".equals(userSql)) {
			//没有设定用户一栏SQL
			throw new Exception("单据类型（" + audits.getVoucherTypeName() + "）没有设定用户一栏取得方法！");
		}
		Gson gson = new Gson();
		SqlProcBean sqlProcBean = gson.fromJson(userSql, SqlProcBean.class);
		if (sqlProcBean == null) {
			throw new Exception("单据类型（" + audits.getVoucherTypeName() + "）的用户一栏取得方法不正确！");
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("showFields", sqlProcBean.getShowFields());
		dataMap.put("conditionFields", sqlProcBean.getConditionFields());
		if (sqlProcBean.getProc() != null && !"".equals(sqlProcBean.getProc())) {
			//执行存储过程
			if ("B".equals(sqlProcBean.getTarget())) {
				dataMap.put("data",businessSQLDao.select(sqlProcBean.getProc(), map));
			} else if ("W".equals(sqlProcBean.getTarget())) {
				dataMap.put("data", sqlDao.select(sqlProcBean.getProc(), map));
			}
			return dataMap;
		} else if (sqlProcBean.getSql() != null && !"".equals(sqlProcBean.getSql())) {
			//执行SQL
			List<FieldBean> conditionFields = sqlProcBean.getConditionFields();
			Object[] arrData = null;
			StringBuffer param = new StringBuffer("");
			if (conditionFields != null && conditionFields.size() > 0) {
				arrData = new Object[conditionFields.size()];
				for (int i = 0; i < conditionFields.size(); i++) {
					FieldBean fieldBean = conditionFields.get(i);
					Object value = map.get(fieldBean.getId());
					if (value == null || "".equals(String.valueOf(value))) {
						arrData[i] = "";
					} else {
						arrData[i] = value;
					}
					param.append(fieldBean.getId()).append(",");
				}
				if (param.length() > 0) {
					param = new StringBuffer(param.substring(0, param.length() - 1));
				}
			}
			String funcName = "condition_function";
			String func = new StringBuffer("")
							.append("function ")
							.append(funcName)
							.append("(")
							.append(param.toString())
							.append("){")
							.append(sqlProcBean.getConditionFunc())
							.append("}").toString();
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine engine = sem.getEngineByName("JavaScript");
			engine.eval(func);
			Invocable invocable = (Invocable)engine;
			Object result = invocable.invokeFunction(funcName, arrData);
			String condition = "";
			if (result == null || "".equals(String.valueOf(result))) {
				condition = "1=1";
			} else {
				condition = String.valueOf(result);
			}
			String sql = sqlProcBean.getSql();
			sql = sql.replaceAll("\\$condition\\$", condition);
			if ("B".equals(sqlProcBean.getTarget())) {
				dataMap.put("data",businessSQLDao.select(sql, map));
			} else if ("W".equals(sqlProcBean.getTarget())) {
				dataMap.put("data", sqlDao.select(sql, map));
			}
			return dataMap;
		} else {
			throw new Exception("单据类型（" + audits.getVoucherTypeName() + "）的用户一栏取得方法不正确！");
		}
	}
	/**
	 * 审核加签处理
	 * @param id
	 * @param userNo
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addUserAudit(String systemUser, String id, String userNo) throws Exception {
		if (id == null || "".equals(id)) {
			throw new Exception("审批ID不正确！");
		}
		if (userNo == null || "".equals(userNo)) {
			//没有指定加签人员
			throw new Exception("请指定加签人员！");
		}
		String[] arrUserNo = userNo.split(",");
		if (arrUserNo == null || arrUserNo.length <= 0) {
			throw new Exception("请指定加签人员！");
		}
		//取得审核数据
		Audits audits = auditDao.getAuditById(id);
		if (audits == null) {
			//没有审批数据
			throw new Exception("没有找到对应的审批数据！");
		}
		//取得工作流实例
		FlowInstance flowInstance = flowInstanceDao.getInstanceById(audits.getInstanceId());
		if (flowInstance == null) {
			//工作流实例不存在
			throw new Exception("工作流实例不存在！");
		}
		Audits objAudits = audits;
		if (!"0".equals(audits.getAuditProp())) {
			List<?> auditsList = auditDao.getNextNormalByLevel(audits.getInstanceId(), audits.getLevel());
			if (auditsList == null || auditsList.size() <= 0) {
				throw new Exception("没有找到审核人员！");
			}
			objAudits = (Audits)auditsList.get(0);
		}
		//当前Level
		BigDecimal level = objAudits.getLevel();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		//将所有的数据的审核级别加1
		auditDao.addOneToLevel(objAudits.getInstanceId(), audits.getLevel(), systemUser, sdf.format(c.getTime()));
		//更新工作流实例当前处理阶层
		flowInstance.setCurrentLevel(level.add(new BigDecimal(1)));
		flowInstance.setUpdateUser(systemUser);
		flowInstance.setUpdateTime(sdf.format(c.getTime()));
		flowInstanceDao.update(flowInstance);
		for (int i = 0; i < arrUserNo.length; i++) {
			String sUserNo = arrUserNo[i];
			if (sUserNo == null || "".equals(sUserNo)) {
				continue;
			}
			Audits tempAudits = new Audits();
			BeanUtils.copyProperties(audits, tempAudits);
			tempAudits.setId(UUID.randomUUID().toString());
			tempAudits.setAuditProp("1");
			tempAudits.setLevel(audits.getLevel());
			tempAudits.setUserNo(sUserNo);
			tempAudits.setStatus("0");
			tempAudits.setContent(null);
			tempAudits.setRealUserNo(null);
			tempAudits.setCreateUser(systemUser);
			tempAudits.setCreateTime(sdf.format(c.getTime()));
			tempAudits.setUpdateUser(null);
			tempAudits.setUpdateTime(null);
			auditDao.add(tempAudits);
		}
	}
	/**
	 * 复制数据至本地数据库
	 * @param voucherType
	 * @param dataKey
	 * @throws Exception
	 */
	private Map<String, Map<String, Object>> createLocalVoucherData(VoucherTypes voucherType, String dataKey, Map<String, String> tableMap) throws Exception {
		List<?> tableList = voucherTableDao.getVoucherTableList(voucherType.getNo());
		//Map处理
		Map<String, Map<String, Object>> rstMap = new HashMap<String, Map<String, Object>>();
		//单据数据本地化
		if (tableList != null && tableList.size() > 0) {
			for (int i = 0; i < tableList.size(); i++) {
				//table
				VoucherTables table = (VoucherTables)tableList.get(i);
				if (table == null) {
					continue;
				}
				if (table.getTableId() == null || "".equals(table.getTableId())) {
					continue;
				}
				if (table.getTableName() != null && !"".equals(table.getTableName())) {
					tableMap.put(table.getTableId().toLowerCase(), table.getTableName());
				}				
				if (table.getSelectSql() == null || "".equals(table.getSelectSql())) {
					continue;
				}
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("dataKey", dataKey);
				List<?> dataList = null;
				if ("B".equals(table.getDatabaseType())) {
					dataList = businessSQLDao.select(table.getSelectSql(), paramMap);
				} else {
					dataList = sqlDao.select(table.getSelectSql(), paramMap);
				}
				if (dataList == null || dataList.size() <= 0) {
					continue;
				}
				int index = 0;
				if (table.getConditionRowIndex() == null || table.getConditionRowIndex().intValue() <= 0) {
					index = 0;
				} else {
					index = table.getConditionRowIndex().intValue() - 1;
				}
				if ("B".equals(table.getDatabaseType())) {
					for (int j = 0; j < dataList.size(); j++) {
						Map<String, Object> map = (Map<String, Object>)dataList.get(j);
						Map<String, Object> dataMap = new HashMap<String, Object>();
						if (map != null && map.size() > 0) {
							Iterator<Entry<String, Object>> it = map.entrySet().iterator();
							while (it.hasNext()) {
								Entry<String, Object> entry = it.next();
								if (entry.getValue() != null) {
									dataMap.put(entry.getKey().toLowerCase(), entry.getValue());
								}
							}
						}
						if (index == j) {
							rstMap.put(table.getTableId().toLowerCase(), dataMap);
						}
						if (table.getLocalTableId() == null || "".equals(table.getLocalTableId())) {
							continue;
						}
						sqlDao.insert(table.getLocalTableId(), dataMap);
					}
				} else {
					//不需要本地化
					Map<String, Object> map = (Map<String, Object>)dataList.get(index);
					Map<String, Object> dataMap = new HashMap<String, Object>();
					if (map != null && map.size() > 0) {
						Iterator<Entry<String, Object>> it = map.entrySet().iterator();
						while (it.hasNext()) {
							Entry<String, Object> entry = it.next();
							if (entry.getValue() != null) {
								dataMap.put(entry.getKey().toLowerCase(), entry.getValue());
							}
						}
						rstMap.put(table.getTableId().toLowerCase(), dataMap);
					}
				}				
			}
		}
		return rstMap;
	}
	/**
	 * 取得流程数据
	 * @param voucherType
	 * @return
	 * @throws Exception
	 */
	private Node getFlowNode(VoucherTypes voucherType, Flows flows) throws Exception {
		PublishedFlows publishedFlows = publishedFlowDao.getLastFlowByVoucherType(voucherType.getNo());
		if (publishedFlows == null) {
			throw new Exception("没有流程数据！");
		}
		BeanUtils.copyProperties(publishedFlows, flows);
		if (flows.getFlowScript() == null || "".equals(flows.getFlowScript())) {
			throw new Exception("没有流程脚本！");
		}
		Gson gson = new Gson();
		String flowScript = flows.getFlowScript();
		Flow flow = (Flow)gson.fromJson(flowScript, Flow.class);
		List<Node> list = FlowUtils.createFlowTree(flow);
		if (list == null || list.size() <= 0) {
			throw new Exception("流程数据不正确！");
		}
		Node startNode = list.get(0);
		return startNode;
	}
	/**
	 * 处理流程
	 * @param node
	 * @param voucherType
	 * @param dataKey
	 * @throws Exception
	 */
	private void processFlow(Node node, VoucherTypes voucherType, String dataKey, Map<String, String> tableMap, Map<String, Map<String, Object>> dataMap, String instanceId, BigDecimal level, String systemUser) throws Exception {
		if (node == null) {
			//节点为NULL
			return;
		}
		if (node instanceof EndNode) {
			//结束节点
			return;
		}
		if (node instanceof ConditionNode) {
			//条件节点
			ConditionNode conditionNode = (ConditionNode)node;
			if (conditionNode.getCondition() == null || "".equals(conditionNode.getCondition())) {
				//没有指定条件
				throw new Exception("条件节点[" + conditionNode.getName() + "]没有指定条件！");
			}
			if (processCondition(conditionNode.getCondition(), tableMap, dataMap)) {
				//满足条件
				processFlow(conditionNode.getYesChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
				return;
			} else {
				//不满足条件
				processFlow(conditionNode.getNoChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
				return;
			}
		}
		if (node instanceof AuditNode) {
			//审核节点
			AuditNode auditNode = (AuditNode)node;
			String auditUserCondition = auditNode.getAuditUserCondition();
			if (auditUserCondition == null || "".equals(auditUserCondition)) {
				//没有指定审核人
				processFlow(auditNode.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
				return;
			} else {
				auditUserCondition = new StringBuffer("")
						.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><processor>")
						.append(auditUserCondition.replaceAll("<br>", "\n"))
						.append("</processor>").toString();
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(auditUserCondition.getBytes("utf-8")));
				Element root = doc.getRootElement();
				List<?> list = root.elements();
				if (list == null || list.size() <= 0) {
					processFlow(auditNode.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
					return;
				}
				String type = null;
				String condition = null;
				String sql = null;
				String dbType = null;
				for (int i = 0; i < list.size(); i++) {
					Element element = (Element)list.get(i);
					if ("type".equals(element.getName())) {
						type = element.getData() != null ? String.valueOf(element.getData()) : "";
						continue;
					}
					if ("condition".equals(element.getName())) {
						condition = element.getData() != null ? String.valueOf(element.getData()) : "";
						continue;
					}
					if ("sql".equals(element.getName())) {
						sql = element.getData() != null ? String.valueOf(element.getData()) : "";
						continue;
					}
					if ("dbtype".equals(element.getName())) {
						dbType = element.getData() != null ? String.valueOf(element.getData()) : "";
					}
				}
				if ("C".equals(type)) {
					//条件
					if (condition == null || "".equals(condition)) {
						//没有指定条件
						processFlow(auditNode.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
						return;
					} else {
						//执行条件
						if (processUserCondition(systemUser, condition, voucherType, dataKey, tableMap, dataMap, instanceId, auditNode.getFirstId(), auditNode.getAuditProcessSql(), level)) {
							level = level.add(new BigDecimal(1));
						}
					}
				} else if ("S".equals(type)) {
					//SQL文
					if (sql == null || "".equals(sql)) {
						//没有指定SQL文件
						processFlow(auditNode.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
						return;
					} else {
						if (dbType == null || "".equals(dbType)) {
							//没有指定数据库类型
							processFlow(auditNode.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
							return;
						}
						//执行SQL文
						//processSQL(String sql, Map<String, Map<String, Object>> dataMap, String dbType, String currentUser, String instanceId, String sectionId, BigDecimal level)
						if (processSQL(systemUser, sql, voucherType, dataMap, dbType, instanceId, auditNode.getFirstId(), auditNode.getAuditProcessSql(), level)) {
							level = level.add(new BigDecimal(1));
						}
					}
				}
				processFlow(auditNode.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
				return;
			}
		}
		processFlow(node.getChild(), voucherType, dataKey, tableMap, dataMap, instanceId, level, systemUser);
	}
	/**
	 * 处理审核用户条件
	 * @param condition
	 * @param voucherType
	 * @param dataKey
	 * @throws Exception
	 */
	private boolean processUserCondition(String systemUser, String condition, VoucherTypes voucherType, String dataKey, Map<String, String> tableMap, Map<String, Map<String, Object>> dataMap, String instanceId, String sectionId, String processSql, BigDecimal level) throws Exception {
		if (condition == null || "".equals(condition)) {
			//没有指定用户条件
			return false;
		}
		if (tableMap == null || tableMap.size() <= 0) {
			//没有指定的数据表
			return false;
		}
		Iterator<Entry<String, String>> it = tableMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String tableId = entry.getKey();
			String tableName = entry.getValue();
			condition = condition.replaceAll(tableName, tableId);
			List<?> fieldList = tableFieldDao.getTableFieldList(tableId);
			if (fieldList == null || fieldList.size() <= 0) {
				continue;
			}
			for (int i = 0; i < fieldList.size(); i++) {
				TableFields field = (TableFields)fieldList.get(i);
				condition = condition.replaceAll(tableId + "." + field.getFieldName(), tableId + "." + field.getFieldId());
				if (dataMap.get(tableId) != null) {
					if ("C".equals(field.getFieldType())) {
						//字符类型字段
						Object obj = dataMap.get(tableId).get(field.getFieldId().toLowerCase());
						String data = obj == null ? "" : String.valueOf(obj);
						condition = condition.replaceAll(tableId + "." + field.getFieldId(), "'" + data + "'");
					} else if ("N".equals(field.getFieldType())) {
						//数值类型
						Object obj = dataMap.get(tableId).get(field.getFieldId().toLowerCase());
						BigDecimal data = new BigDecimal(obj== null ? "0" : String.valueOf(obj));
						condition = condition.replaceAll(tableId + "." + field.getFieldId(), data.toString());
					} else {
						throw new Exception ("字段类型不正确！");
					}
				}
				//去除表名
				condition = condition.replaceAll(tableId + "." + field.getFieldId(), field.getFieldId());
			}
		}
		//取得用户表
		List<?> tableList = voucherTableDao.getVoucherTableList(voucherType.getNo());
		if (tableList == null) {
			return false;
		}
		VoucherTables userTable = null;
		for (int i = 0; i < tableList.size(); i++) {
			VoucherTables table = (VoucherTables)tableList.get(i);
			if (!"U".equals(table.getTableType())) {
				//非用户表
				continue;
			}
			userTable = table;
			break;
		}
		if (userTable == null) {
			//没有指定用户表
			return false;
		}
		//生成SQL文		
		if (userTable.getUserNoField() == null || "".equals(userTable.getUserNoField())) {
			return false;
		}
		if (userTable.getTableId() == null || "".equals(userTable.getTableId())) {
			return false;
		}
		StringBuffer sql = new StringBuffer("")
				.append("SELECT\n")
				.append(userTable.getUserNoField()).append("\n")
				.append("FROM\n")
				.append(userTable.getTableId()).append("\n");
		if (condition != null && !"".equals(condition)) {
			sql.append("WHERE\n")
				.append(condition);
		}
		//执行SQL文
		List<?> userList = null;
		if ("B".equals(userTable.getDatabaseType())) {
			//业务数据库
			userList = businessSQLDao.select(sql.toString(), null);
		} else if ("W".equals(userTable.getDatabaseType())) {
			//工作流数据库
			userList = sqlDao.select(sql.toString(), null);
		} else {
			return false;
		}
		if (userList == null || userList.size() <= 0) {
			return false;
		}
		//新增数据至用户审核用户表
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String currentTime = sdf.format(c.getTime());
		boolean result = false;
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> map = (Map<String, Object>)userList.get(i);
			Object obj = map.get(userTable.getUserNoField());
			String userNo = obj == null ? null : String.valueOf(obj);
			if (userNo == null || "".equals(userNo)) {
				//没有审批人
				continue;
			}
			//生成审批人
			Audits audit = new Audits();
			audit.setId(UUID.randomUUID().toString());
			audit.setInstanceId(instanceId);
			audit.setVoucherType(voucherType.getNo());
			audit.setVoucherTypeName(voucherType.getName());
			audit.setAuditSection(sectionId);
			audit.setProcessSql(processSql);
			audit.setLevel(level);
			audit.setUserNo(userNo);
			audit.setStatus("0");
			//普通审核用户
			audit.setAuditProp("0");
			audit.setCreateUser(systemUser);
			audit.setCreateTime(currentTime);
			auditDao.add(audit);
			result = true;
		}
		return result;
	}
	/**
	 * 处理SQL文
	 * @param sql
	 * @param voucherType
	 * @param dataKey
	 * @throws Exception
	 */
	private boolean processSQL(String systemUser, String sql, VoucherTypes voucherType, Map<String, Map<String, Object>> dataMap, String dbType, String instanceId, String sectionId, String processSql, BigDecimal level) throws Exception {
		//SQL文参数生成
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//系统用户
		paramMap.put("system_user", systemUser);
		if (dataMap != null && dataMap.size() > 0) {
			Iterator<Entry<String, Map<String, Object>>> it = dataMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Map<String, Object>> entry = it.next();
				String tableId = entry.getKey().toLowerCase();
				Map<String, Object> fieldMap = entry.getValue();
				if (fieldMap != null && fieldMap.size() > 0) {
					Iterator<Entry<String, Object>> fieldIt = fieldMap.entrySet().iterator();
					while(fieldIt.hasNext()) {
						Entry<String, Object> fieldEntry = fieldIt.next();
						if (fieldEntry.getValue() != null && !"".equals(String.valueOf(fieldEntry.getValue()))) {
							paramMap.put(tableId + "_" + fieldEntry.getKey().toLowerCase(), fieldEntry.getValue());
						}
					}
				}
			}
		}
		List<?> list = null;
		if ("B".equals(dbType)) {
			//业务数据库			
			list = businessSQLDao.select(sql, paramMap);
			
		} else if ("W".equals(dbType)) {
			//工作流数据库
			list = sqlDao.select(sql, paramMap);
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String currentTime = sdf.format(c.getTime());
		boolean result = false;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = (Map<String, Object>)list.get(i);
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if ("userno".equals(entry.getKey().toLowerCase())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							//创建审核用户
							Audits audit = new Audits();
							audit.setId(UUID.randomUUID().toString());
							audit.setInstanceId(instanceId);
							audit.setVoucherType(voucherType.getNo());
							audit.setVoucherTypeName(voucherType.getName());
							audit.setAuditSection(sectionId);
							audit.setProcessSql(processSql);
							audit.setLevel(level);
							audit.setUserNo(String.valueOf(entry.getValue()));
							audit.setStatus("0");
							//普通审核用户
							audit.setAuditProp("0");
							audit.setCreateUser(systemUser);
							audit.setCreateTime(currentTime);
							auditDao.add(audit);
							result = true;
						}
					}
				}
			}
		}
		return result;
	}
	/**
	 * 执行条件节点的条件
	 * @param condition
	 * @param voucherType
	 * @param dataKey
	 * @return
	 * @throws Exception
	 */
	private boolean processCondition(String condition, Map<String, String> tableMap, Map<String, Map<String, Object>> dataMap) throws Exception {
		//取得数据
		if (condition == null || "".equals(condition)) {
			return true;
		}
		//替换所有tab
		condition = condition.replaceAll("\t", " ");
		//替换所有的<br>
		condition= condition.replaceAll("<br>", "\n");
		//替换and
		condition = condition.replaceAll(" AND ", " && ");
		condition = condition.replaceAll(" AND\n", " &&\n");
		//替换or
		condition = condition.replaceAll(" OR ", " || ");
		condition = condition.replaceAll(" OR\n", " ||\n");
		int eqIndex = 0;
		while ((eqIndex = condition.indexOf("=", eqIndex)) >= 0) {
			if (eqIndex > 0) {
				char ch = condition.charAt(eqIndex - 1);
				if (ch == '=' || ch == '>' || ch == '<' || ch == '!') {
					eqIndex++;
					continue;
				}
				eqIndex++;
				if (eqIndex < condition.length()) {
					char ch1 = condition.charAt(eqIndex);
					if (ch1 == '=') {
						eqIndex++;
						continue;
					}
				}
				String sBef = condition.substring(0, eqIndex);
				String sBak = condition.substring(eqIndex);
				condition = new StringBuffer("")
						.append(sBef)
						.append("=")
						.append(sBak).toString();
				eqIndex++;
			}
		}
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		//取得字段信息
		Iterator<Entry<String, String>> it = tableMap.entrySet().iterator();
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer paramString = new StringBuffer("");
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String tableId = entry.getKey();
			String tableName = entry.getValue();
			condition = condition.replaceAll(tableName, tableId);
			List<?> fieldList = tableFieldDao.getTableFieldList(tableId);
			if (fieldList == null || fieldList.size() <= 0) {
				continue;
			}
			for (int i = 0; i < fieldList.size(); i++) {
				TableFields field = (TableFields)fieldList.get(i);
				condition = condition.replaceAll(tableId + "." + field.getFieldName(), tableId + ".get(\"" + field.getFieldId().toLowerCase() + "\")");
			}
			paramString.append(tableId)
				.append(",");
			paramList.add(dataMap.get(tableId));
		}
		String paramName = "";
		if (paramString.length() > 0) {
			paramName = paramString.substring(0, paramString.length() - 1);
		}
		String functionName = new StringBuffer("")
											.append("condition_")
											.append(UUID.randomUUID().toString().replaceAll("-", "")).toString();
		String functionContent = new StringBuffer("")
											.append("function ")
											.append(functionName)
											.append("(")
											.append(paramName)
											.append(") {return (")
											.append(condition)
											.append(");}").toString().replaceAll("'", "\"");
		engine.eval(functionContent);
		try {
			if (engine instanceof Invocable) {
				Invocable invocable = (Invocable)engine;
				Object result = invocable.invokeFunction(functionName, paramList.toArray());
				if (result == null) {
					return true;
				}
				boolean bResult = Boolean.parseBoolean(String.valueOf(result));
				return bResult;
			}
		} catch (ScriptException e) {
			throw new Exception(e.getMessage());
		}
		return true;
	}
	/**
	 * 处理提交SQL
	 * @param systemUser
	 * @param objVoucherType
	 * @param dataMap
	 * @throws Exception
	 */
	private void processSubmitSQL(String systemUser, VoucherTypes objVoucherType, Map<String, Map<String, Object>> dataMap) throws Exception {
		if (objVoucherType.getSubmitSql() == null || "".equals(objVoucherType.getSubmitSql())) {
			return;
		}
		String sql = objVoucherType.getSubmitSql();
		Gson gson = new Gson();
		DatabaseBean databaseBean = gson.fromJson(sql, DatabaseBean.class); 
		//SQL文参数生成
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//系统用户
		paramMap.put("systemUser", systemUser);
		if (dataMap != null && dataMap.size() > 0) {
			Iterator<Entry<String, Map<String, Object>>> it = dataMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Map<String, Object>> entry = it.next();
				String tableId = entry.getKey().toLowerCase();
				Map<String, Object> fieldMap = entry.getValue();
				if (fieldMap != null && fieldMap.size() > 0) {
					Iterator<Entry<String, Object>> fieldIt = fieldMap.entrySet().iterator();
					while(fieldIt.hasNext()) {
						Entry<String, Object> fieldEntry = fieldIt.next();
						if (fieldEntry.getValue() != null && !"".equals(String.valueOf(fieldEntry.getValue()))) {
							paramMap.put(tableId + "_" + fieldEntry.getKey().toLowerCase(), fieldEntry.getValue());
						}
					}
				}
			}
		}
		//业务数据库处理
		businessSQLDao.update(databaseBean, paramMap);
		//工作流数据库处理
		sqlDao.update(databaseBean, paramMap);
	}
	/**
	 * 处理取消提交SQL
	 * @param systemUser
	 * @param objVoucherType
	 * @param dataMap
	 * @throws Exception
	 */
	private void processSubmitCancelSQL(String systemUser, VoucherTypes objVoucherType, Map<String, Map<String, Object>> dataMap) throws Exception {
		if (objVoucherType.getSubmitSql() == null || "".equals(objVoucherType.getSubmitSql())) {
			return;
		}
		String sql = objVoucherType.getSubmitCancelSql();
		Gson gson = new Gson();
		DatabaseBean databaseBean = gson.fromJson(sql, DatabaseBean.class); 
		//SQL文参数生成
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//系统用户
		paramMap.put("systemUser", systemUser);
		if (dataMap != null && dataMap.size() > 0) {
			Iterator<Entry<String, Map<String, Object>>> it = dataMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Map<String, Object>> entry = it.next();
				String tableId = entry.getKey().toLowerCase();
				Map<String, Object> fieldMap = entry.getValue();
				if (fieldMap != null && fieldMap.size() > 0) {
					Iterator<Entry<String, Object>> fieldIt = fieldMap.entrySet().iterator();
					while(fieldIt.hasNext()) {
						Entry<String, Object> fieldEntry = fieldIt.next();
						if (fieldEntry.getValue() != null && !"".equals(String.valueOf(fieldEntry.getValue()))) {
							paramMap.put(tableId + "_" + fieldEntry.getKey().toLowerCase(), fieldEntry.getValue());
						}
					}
				}
			}
		}
		//业务数据库处理
		businessSQLDao.update(databaseBean, paramMap);
		//工作流数据库处理
		sqlDao.update(databaseBean, paramMap);
	}
	/**
	 * 处理取消终审SQL
	 * @param systemUser
	 * @param objVoucherType
	 * @param dataMap
	 * @throws Exception
	 */
	private void processCompleteCancelSQL(String systemUser, VoucherTypes objVoucherType, Map<String, Map<String, Object>> dataMap) throws Exception {
		if (objVoucherType.getCompleteCancelSql() == null || "".equals(objVoucherType.getCompleteCancelSql())) {
			return;
		}
		String sql = objVoucherType.getCompleteCancelSql();
		Gson gson = new Gson();
		DatabaseBean databaseBean = gson.fromJson(sql, DatabaseBean.class); 
		//SQL文参数生成
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//系统用户
		paramMap.put("systemUser", systemUser);
		if (dataMap != null && dataMap.size() > 0) {
			Iterator<Entry<String, Map<String, Object>>> it = dataMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Map<String, Object>> entry = it.next();
				String tableId = entry.getKey().toLowerCase();
				Map<String, Object> fieldMap = entry.getValue();
				if (fieldMap != null && fieldMap.size() > 0) {
					Iterator<Entry<String, Object>> fieldIt = fieldMap.entrySet().iterator();
					while(fieldIt.hasNext()) {
						Entry<String, Object> fieldEntry = fieldIt.next();
						if (fieldEntry.getValue() != null && !"".equals(String.valueOf(fieldEntry.getValue()))) {
							paramMap.put(tableId + "_" + fieldEntry.getKey().toLowerCase(), fieldEntry.getValue());
						}
					}
				}
			}
		}
		//业务数据库处理
		businessSQLDao.update(databaseBean, paramMap);
		//工作流数据库处理
		sqlDao.update(databaseBean, paramMap);
	}
	/**
	 * 新增流程实例
	 * @param systemUser
	 * @param instanceId
	 * @param dataKey
	 * @param flows
	 * @param voucherTypes
	 * @param viewNo
	 * @throws Exception
	 */
	private void saveFlowInstance(String systemUser, String instanceId, String dataKey, Flows flows, VoucherTypes voucherTypes, String viewNo) throws Exception {
		FlowInstance flowInstance = new FlowInstance();
		flowInstance.setId(instanceId);
		flowInstance.setDataKey(dataKey);
		flowInstance.setFlowNo(flows.getNo());
		flowInstance.setFlowVersion(flows.getPublishVersion());
		flowInstance.setStatus("N");
		flowInstance.setVoucherType(voucherTypes.getNo());
		flowInstance.setVoucherTypeName(voucherTypes.getName());
		flowInstance.setCurrentLevel(new BigDecimal(1));
		flowInstance.setAuditType(flows.getAuditType());
		flowInstance.setViewNo(viewNo);
		flowInstance.setMessage(flows.getMessage());
		flowInstance.setCreateUser(systemUser);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		flowInstance.setCreateTime(sdf.format(c.getTime()));
		flowInstanceDao.add(flowInstance);
	}
	/**
	 * 取得单据数据
	 * @param voucherType
	 * @param dataKey
	 * @param tableMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Map<String, Object>> getDataMap(VoucherTypes voucherType, String dataKey, String auditId, String viewNo) throws Exception {
		List<?> tableList = voucherTableDao.getVoucherTableList(voucherType.getNo());
		//Map处理
		Map<String, Map<String, Object>> rstMap = new HashMap<String, Map<String, Object>>();
		//单据数据取得
		if (tableList != null && tableList.size() > 0) {
			for (int i = 0; i < tableList.size(); i++) {
				//table
				VoucherTables table = (VoucherTables)tableList.get(i);
				if (table == null) {
					continue;
				}
				if (table.getTableId() == null || "".equals(table.getTableId())) {
					continue;
				}
				if (table.getSelectSql() == null || "".equals(table.getSelectSql())) {
					continue;
				}
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("dataKey", dataKey);
				List<?> dataList = null;
				if ("B".equals(table.getDatabaseType())) {
					dataList = businessSQLDao.select(table.getSelectSql(), paramMap);
				} else {
					dataList = sqlDao.select(table.getSelectSql(), paramMap);
				}
				if (dataList == null || dataList.size() <= 0) {
					continue;
				}
				int index = 0;
				if (table.getConditionRowIndex() == null || table.getConditionRowIndex().intValue() <= 0) {
					index = 0;
				} else {
					index = table.getConditionRowIndex().intValue() - 1;
				}
				if (index >= dataList.size()) {
					continue;
				}
				Map<String, Object> map = (Map<String, Object>)dataList.get(index);
				Map<String, Object> dataMap = new HashMap<String, Object>();
				if (map != null && map.size() > 0) {
					Iterator<Entry<String, Object>> it = map.entrySet().iterator();
					while (it.hasNext()) {
						Entry<String, Object> entry = it.next();
						if (entry.getValue() != null) {
							dataMap.put(entry.getKey().toLowerCase(), entry.getValue());
						}
					}
				}
				rstMap.put(table.getTableId().toLowerCase(), dataMap);
			}
		} else {
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(viewNo);
			if (publishedView == null) {
				throw new Exception("对应画面数据不存在！");
			}
			//取得HeadScript
			String headScript = publishedView.getHeadScript();
			if (headScript == null || "".equals(headScript)) {
				throw new Exception("画面设定不正确！");
			}
			Gson gson = new Gson();
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
			Map<String, Object> formData = SqlUtils.getDbData(headBean, detailBeanList, dataKey, businessSQLDao, sqlDao);
			this.createDataMap(rstMap, formData);
		}
		//取得当前审核
		String sql = "select * from Audits where ID=:id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", auditId);
		List<?> list = sqlDao.select(sql, paramMap);
		if (list == null || list.size() <= 0) {
			return rstMap;
		}
		Map<String, Object> auditMap = (Map<String, Object>)list.get(0);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (auditMap != null && auditMap.size() > 0) {
			Iterator<Entry<String, Object>> it = auditMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if (entry.getValue() != null) {
					dataMap.put(entry.getKey().toLowerCase(), entry.getValue());
				}
			}
		}
		rstMap.put("audits", dataMap);
		return rstMap;
	}
	/**
	 * 处理终审SQL
	 * @param systemUser
	 * @param objVoucherType
	 * @param dataMap
	 * @throws Exception
	 */
	private void processCompleteSQL(String systemUser, VoucherTypes objVoucherType, Map<String, Map<String, Object>> dataMap) throws Exception {
		if (objVoucherType.getCompleteSql() == null || "".equals(objVoucherType.getCompleteSql())) {
			return;
		}
		String sql = objVoucherType.getCompleteSql();
		if (sql == null || "".equals(sql)) {
			return;
		}
		Gson gson = new Gson();
		DatabaseBean databaseBean = gson.fromJson(sql, DatabaseBean.class);
		//SQL文参数生成
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//系统用户
		paramMap.put("systemUser", systemUser);
		if (dataMap != null && dataMap.size() > 0) {
			Iterator<Entry<String, Map<String, Object>>> it = dataMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Map<String, Object>> entry = it.next();
				String tableId = entry.getKey().toLowerCase();
				Map<String, Object> fieldMap = entry.getValue();
				if (fieldMap != null && fieldMap.size() > 0) {
					Iterator<Entry<String, Object>> fieldIt = fieldMap.entrySet().iterator();
					while(fieldIt.hasNext()) {
						Entry<String, Object> fieldEntry = fieldIt.next();
						if (fieldEntry.getValue() != null && !"".equals(String.valueOf(fieldEntry.getValue()))) {
							paramMap.put(tableId + "_" + fieldEntry.getKey().toLowerCase(), fieldEntry.getValue());
						}
					}
				}
			}
		}
		//业务数据库处理
		businessSQLDao.update(databaseBean, paramMap);
		//工作流数据库处理
		sqlDao.update(databaseBean, paramMap);
	}
	/**
	 * 处理审核SQL
	 * @param systemUser
	 * @param processSql
	 * @param section
	 * @throws Exception
	 */
	private void processAuditSQL(String systemUser, String processSql, String section, Map<String, Map<String, Object>> dataMap) throws Exception {
		if (processSql == null || "".equals(processSql)) {
			//没有需要处理的SQL
			return;
		}
		if (section == null  || "".equals(section)) {
			return;
		}
		if (dataMap == null) {
			return;
		}
		Gson gson = new Gson();
		List<ProcessSql> list = gson.fromJson(processSql, new TypeToken<ArrayList<ProcessSql>>(){}.getType());
		if (list == null || list.size() <= 0) {
			return;
		}
		List<ProcessSql> processList = new ArrayList<ProcessSql>();
		for (int i = 0; i < list.size(); i++) {
			ProcessSql sql = list.get(i);
			if (!section.equals(sql.getSection())) {
				continue;
			}
			if (sql.getSql() == null || "".equals(sql.getSql())) {
				continue;
			}
			processList.add(sql);
		}
		if (processList.size() <= 0) {
			//没有需要处理的SQL
			return;
		}
		//做成参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemUser", systemUser);
		Iterator<Entry<String, Map<String, Object>>> it = dataMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Map<String, Object>> entry = it.next();
			String tableId = entry.getKey().toLowerCase();
			Map<String, Object> dataTable = entry.getValue();
			Iterator<Entry<String, Object>> itField = dataTable.entrySet().iterator();
			while (itField.hasNext()) {
				Entry<String, Object> entry2 = itField.next();
				String fieldId = entry2.getKey().toLowerCase();
				Object fieldValue = entry2.getValue();
				if (fieldValue == null) {
					continue;
				}
				map.put(tableId + "_" + fieldId, fieldValue);
			}
		}
		//执行SQL文
		for (int i = 0; i < processList.size(); i++) {
			ProcessSql sql = processList.get(i);
			if ("B".equals(sql.getTarget())) {
				businessSQLDao.update(sql.getSql(), map);
			} else {
				sqlDao.update(sql.getSql(), map);
			}
		}
	}
}
