package cn.lmx.flow.service.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import cn.lmx.Excel.ExcelProcessor.CreateExcel;
import cn.lmx.Excel.ExcelProcessor.CreateNoFormatExcel;
import cn.lmx.flow.bean.report.json.ConditionField;
import cn.lmx.flow.bean.report.json.ViewBean;
import cn.lmx.flow.bean.report.json.ViewField;
import cn.lmx.flow.bean.view.AttachmentBean;
import cn.lmx.flow.bean.view.json.detail.DetailBean;
import cn.lmx.flow.bean.view.json.detail.PropertyBean;
import cn.lmx.flow.bean.view.json.head.CellBean;
import cn.lmx.flow.bean.view.json.head.CellPropertyBean;
import cn.lmx.flow.bean.view.json.head.HeadBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.dao.view.AttachmentDao;
import cn.lmx.flow.dao.view.PublishReportDao;
import cn.lmx.flow.dao.view.PublishedViewDao;
import cn.lmx.flow.dao.view.ReportTemplateDao;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.view.Attachments;
import cn.lmx.flow.entity.view.PublishedViews;
import cn.lmx.flow.entity.view.ReportPublish;
import cn.lmx.flow.entity.view.ReportTemplate;

@Repository("ReportService")
public class ReportService {
	//发布的报表
	@Resource(name="PublishReportDao")
	private PublishReportDao publishReportDao;
	//模块
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	//SQL
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//BusinessSQL
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSQLDao;
	//发布的画面
	@Resource(name="PublishedViewDao")
	private PublishedViewDao publishedViewDao;
	//附件
	@Resource(name="AttachmentDao")
	private AttachmentDao attachmentDao;
	//报表模板
	@Resource(name="ReportTemplateDao")
	private ReportTemplateDao reportTemplateDao;
	/**
	 * 画面初始化
	 * @param reportNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> init(String systemUser, String reportNo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (reportNo == null || "".equals(reportNo)) {
				throw new Exception("报表编码不存在！");
			}
			if (!reportNo.endsWith("Report")) {
				throw new Exception("报表编码不正确！");
			}
			reportNo = reportNo.substring(0, reportNo.length() - "Report".length());
			ReportPublish report = publishReportDao.getByNo(reportNo);
			if (report == null) {
				throw new Exception("您查询的报表不存在，请确认后重试！");
			}
			if (report.getViewScript() == null || "".equals(report.getViewScript())) {
				throw new Exception("报表设定不正确！");
			}
			//取得模块
			Module module = moduleDao.getModuleByNo(report.getModuleNo());
			if (module == null) {
				throw new Exception("没有报表发布的模块！");
			}
			Gson gson = new Gson();
			ViewBean viewBean = gson.fromJson(report.getViewScript(), ViewBean.class);
			if (viewBean == null) {
				throw new Exception("报表设定不正确！");
			}
			Map<String, Object> conditionData = new HashMap<String, Object>();
			List<ConditionField> conditionList = viewBean.getCondition();
			if (conditionList != null && conditionList.size() > 0) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("systemUser", systemUser);
				for (int i = 0; i < conditionList.size(); i++) {
					ConditionField condition = conditionList.get(i);
					if ("SQL".equals(condition.getDefaultType()) || "PROC".equals(condition.getDefaultType())) {
						//SQL处理 或 存储过程
						String sql = condition.getDefaultValue();
						if (sql == null || "".equals(sql)) {
							continue;
						}
						String field = condition.getDefaultField();
						if (field == null || "".equals(field)) {
							continue;
						}
						sql = URLDecoder.decode(sql, "UTF-8");
						List<?> dataList = null;
						if ("R".equals(condition.getDefaultTarget())) {
							//远程数据库
							dataList = businessSQLDao.select(sql, paramMap);
						} else {
							dataList = sqlDao.select(sql, paramMap);
						}
						if (dataList == null || dataList.size() <= 0) {
							continue;
						}
						Map<String, Object> dataMap = (Map<String, Object>)dataList.get(0);
						conditionData.put(field, dataMap.get(field));
					}
				}
			}
			List<?> dataList = new ArrayList<Map<String, Object>>();
			if ("0".equals(viewBean.getRunTime())) {
				//立即查询
				dataList = this.queryData(viewBean, conditionData);
			}
			//查询报表模板
			List<?> reportTemplateList = reportTemplateDao.listByReportNo(reportNo);
			if (reportTemplateList == null) {
				map.put("template", new ArrayList<Map<String, Object>>());
			} else {
				List<Map<String, Object>> templateList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < reportTemplateList.size(); i++) {
					ReportTemplate reportTemplate = (ReportTemplate)reportTemplateList.get(i);
					Map<String, Object> templateMap = new HashMap<String, Object>();
					templateMap.put("id", reportTemplate.getId());
					templateMap.put("name", reportTemplate.getFileName());
					templateList.add(templateMap);
				}
				map.put("template", templateList);
			}
			map.put("report", report);
			map.put("module", module);
			map.put("conditionData", gson.toJson(conditionData));
			map.put("listData", gson.toJson(dataList));
			map.put("fixRow", viewBean.getFixRows());
			map.put("fixCol", viewBean.getFixCols());
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 检索数据
	 * @param reportNo
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> query(String reportNo, Map<String, Object> map) throws Exception {
		try {
			if (reportNo == null || "".equals(reportNo)) {
				throw new Exception("报表编码不存在！");
			}
			if (!reportNo.endsWith("Report")) {
				throw new Exception("报表编码不正确！");
			}
			reportNo = reportNo.substring(0, reportNo.length() - "Report".length());
			ReportPublish report = publishReportDao.getByNo(reportNo);
			if (report == null) {
				throw new Exception("您查询的报表不存在，请确认后重试！");
			}
			if (report.getViewScript() == null || "".equals(report.getViewScript())) {
				throw new Exception("报表设定不正确！");
			}
			Gson gson = new Gson();
			ViewBean viewBean = gson.fromJson(report.getViewScript(), ViewBean.class);
			if (viewBean == null) {
				throw new Exception("报表设定不正确！");
			}
			return queryData(viewBean, map);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}		
	}
	/**
	 * 下载Excel
	 * @param reportNo
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> download(String path, String reportNo, Map<String, Object> map, String templateId) throws Exception {
		try {
			if (reportNo == null || "".equals(reportNo)) {
				throw new Exception("报表编码不存在！");
			}
			if (!reportNo.endsWith("ReportDownload")) {
				throw new Exception("报表编码不正确！");
			}
			reportNo = reportNo.substring(0, reportNo.length() - "ReportDownload".length());
			ReportPublish report = publishReportDao.getByNo(reportNo);
			if (report == null) {
				throw new Exception("您查询的报表不存在，请确认后重试！");
			}
			if (report.getViewScript() == null || "".equals(report.getViewScript())) {
				throw new Exception("报表设定不正确！");
			}
			Gson gson = new Gson();
			ViewBean viewBean = gson.fromJson(report.getViewScript(), ViewBean.class);
			if (viewBean == null) {
				throw new Exception("报表设定不正确！");
			}
			List<?> list = queryData(viewBean, map);
			//取得报表模板
			ReportTemplate reportTemplate = reportTemplateDao.getById(templateId);
			if (reportTemplate == null || reportTemplate.getTemplate() == null && reportTemplate.getTemplate().length <= 0) {
				//没有模板
				List<ViewField> fieldList = viewBean.getView();
				List<String> fieldName = new ArrayList<String>();
				List<String> field = new ArrayList<String>();
				for (int i = 0; i < fieldList.size(); i++) {
					fieldName.add(fieldList.get(i).getLabel());
					field.add(fieldList.get(i).getField());
				}
				String excelFile = CreateNoFormatExcel.createNoFormatExcel(path, (List<Map<String, Object>>)list, report.getName(), fieldName, field);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("fileName", excelFile);
				resultMap.put("report", report);
				return resultMap;
			} else {
				//存在模板
				if (!path.endsWith(File.separator)) {
					path = path + File.separator;
				}
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				String templateFileName = null;
				if (reportTemplate.getFileName().endsWith(".xls")) {
					templateFileName = new StringBuffer("")
											.append(UUID.randomUUID().toString().replaceAll("-", ""))
											.append(".xls").toString();
				} else if (reportTemplate.getFileName().endsWith(".xlsx")) {
					templateFileName = new StringBuffer("")
											.append(UUID.randomUUID().toString().replaceAll("-", ""))
											.append(".xlsx").toString();
				}
				if (templateFileName == null) {
					//创建无格式报表
					List<ViewField> fieldList = viewBean.getView();
					List<String> fieldName = new ArrayList<String>();
					List<String> field = new ArrayList<String>();
					for (int i = 0; i < fieldList.size(); i++) {
						fieldName.add(fieldList.get(i).getLabel());
						field.add(fieldList.get(i).getField());
					}
					String excelFile = CreateNoFormatExcel.createNoFormatExcel(path, (List<Map<String, Object>>)list, report.getName(), fieldName, field);
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("fileName", excelFile);
					resultMap.put("report", report);
					return resultMap;
				}
				File templateFile = new File(path + templateFileName);
				OutputStream outputStream = new FileOutputStream(templateFile);
				outputStream.write(reportTemplate.getTemplate());
				outputStream.flush();
				outputStream.close();
				//根据模板创建Excel
				String excelFile = CreateExcel.create(path, list, path + templateFileName);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("fileName", excelFile);
				resultMap.put("report", report);
				return resultMap;
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}		
	}
	/**
	 * 查询数据
	 * @param reportNo
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private List<?> queryData(ViewBean viewBean, Map<String, Object> map) throws Exception {
		try {
			String sql = viewBean.getSql();
			if (sql == null || "".equals(sql)) {
				throw new Exception("没有设定查询用SQL文！");
			}
			sql = URLDecoder.decode(sql, "UTF-8");
			List<?> list = null;
			if ("S".equals(viewBean.getType())) {
				//SQL文
				List<ConditionField> conditionList = viewBean.getCondition();
				if (map == null || conditionList == null || conditionList.size() <= 0) {
					//sql = sql.replaceAll("\\$condition\\$", "Status='0'");
					sql = sql.replaceAll("\\$condition\\$", "1=1");
					if ("R".equals(viewBean.getTarget())) {
						//远程数据库
						list = businessSQLDao.select(sql, null);
					} else {
						//本地数据库
						list = sqlDao.select(sql, null);
					}				
					return list;
				}
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				StringBuffer condition = new StringBuffer("");
				Map<String, Object> tempMap = new HashMap<String, Object>();			
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if (entry.getValue() != null) {
						ConditionField conditionField = null;
						for (int i = 0; i < conditionList.size(); i++) {
							ConditionField tempBean = conditionList.get(i);						
							String fieldAlias = tempBean.getField();
							if (tempBean.getAlias() != null && !"".equals(tempBean.getAlias())) {
								fieldAlias = tempBean.getAlias();
							}
							if (entry.getKey().equals(fieldAlias)) {
								conditionField = conditionList.get(i);
								break;
							}
						}
						if (conditionField == null) {
							continue;
						}
						if (entry.getValue() == null || "".equals(entry.getValue())) {
							continue;
						}
						if (condition.length() > 0) {
							condition.append(" and ");
						}
						if ("eq".equals(conditionField.getCompare())) {
							condition.append(conditionField.getField())
									.append("=:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						} else if ("neq".equals(conditionField.getCompare())) {
							condition.append(conditionField.getField())
									.append("<>:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						} else if ("gt".equals(conditionField.getCompare())) {
							condition.append(conditionField.getField())
									.append(">:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						} else if ("get".equals(conditionField.getCompare())) {
							condition.append(conditionField.getField())
									.append(">=:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						} else if ("lt".equals(conditionField.getCompare())) {
							condition.append(conditionField.getField())
									.append("<:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						} else if ("let".equals(conditionField.getCompare())) {
							condition.append(conditionField.getField())
									.append("<=:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						} else if ("like".equals(conditionField.getCompare())) {
							if (entry.getValue() != null && !"".equals(entry.getValue())) {
								condition.append(conditionField.getField())
									.append(" like '%")
									.append(entry.getValue())
									.append("%'");
							}
						} else if ("leftlike".equals(conditionField.getCompare())) {
							if (entry.getValue() != null && !"".equals(entry.getValue())) {
								condition.append(conditionField.getField())
									.append(" like '")
									.append(entry.getValue())
									.append("%'");
							}
						} else if ("rightlike".equals(conditionField.getCompare())) {
							if (entry.getValue() != null && !"".equals(entry.getValue())) {
								condition.append(conditionField.getField())
									.append(" like '%")
									.append(entry.getValue())
									.append("'");
							}
						} else {
							condition.append(conditionField.getField())
									.append("=:")
									.append(entry.getKey());
							tempMap.put(entry.getKey(), entry.getValue());
						}
					}
				}
				if (condition.length() > 0) {
					sql = sql.replaceAll("\\$condition\\$", condition.toString());
				} else {
					sql = sql.replaceAll("\\$condition\\$", "1=1");
				}
				if ("R".equals(viewBean.getTarget())) {
					//远程数据库
					list = businessSQLDao.select(sql, tempMap);
				} else {
					//本地数据库
					list = sqlDao.select(sql, tempMap);
				}
			} else {
				//存储过程
				if (map != null) {
					List<ConditionField> conditionList = viewBean.getCondition();
					Map<String, Object> tempMap = new HashMap<String, Object>();
					for(int i = 0; i < conditionList.size(); i++) {
						ConditionField conditionField = conditionList.get(i);
						String aliasName = conditionField.getField();
						if (conditionField.getAlias() != null && !"".equals(conditionField.getAlias())) {
							aliasName = conditionField.getAlias();
						}
						tempMap.put(aliasName, map.get(aliasName));
					}
					if ("R".equals(viewBean.getTarget())) {
						//远程数据库
						list = businessSQLDao.select(sql, tempMap);
					} else {
						//本地数据库
						list = sqlDao.select(sql, tempMap);
					}
				}else {
					if (viewBean.getCondition() == null || "".equals(viewBean.getCondition())) {
						if ("R".equals(viewBean.getTarget())) {
							//远程数据库
							list = businessSQLDao.select(sql, map);
						} else {
							//本地数据库
							list = sqlDao.select(sql, map);
						}
						return list;
					}
					List<ConditionField> conditionList = viewBean.getCondition();
					Map<String, Object> tempMap = new HashMap<String, Object>();
					for(int i = 0; i < conditionList.size(); i++) {
						ConditionField conditionField = conditionList.get(i);
						String aliasName = conditionField.getField();
						if (conditionField.getAlias() != null && !"".equals(conditionField.getAlias())) {
							aliasName = conditionField.getAlias();
						}
						tempMap.put(aliasName, null);
					}
					if ("R".equals(viewBean.getTarget())) {
						//远程数据库
						list = businessSQLDao.select(sql, tempMap);
					} else {
						//本地数据库
						list = sqlDao.select(sql, tempMap);
					}
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
	 * 链接画面数据取得
	 * @param id
	 * @param viewNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> linkView(String id, String viewNo) throws Exception {
		PublishedViews publishedView = publishedViewDao.getViewByNo(viewNo);
		if (publishedView == null) {
			//没有设定画面编码
			throw new Exception("画面显示方法没有设定！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("viewNo", viewNo);
		map.put("viewName", publishedView.getName());
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
			paramMap.put("id", id);			
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
			//SQL文
			String tableKey = headBean.getTableKey();
			if (tableKey == null || "".equals(tableKey)) {
				tableKey = "ID";
			}
			sql = sql.replaceAll("\\$condition\\$", tableKey + "=:id");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", id);
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
		List<?> attaList = attachmentDao.list(viewNo, id);
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
		Map<String, Object> detailDataMap = getDetailData(detailList, id, (Map<String, Object>)headDataList.get(0), headBean.getTable());
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
	 * 链接画面明细数据取得
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
}
