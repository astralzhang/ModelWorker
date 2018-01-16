package cn.lmx.flow.service.view;

import java.lang.reflect.Type;
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

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.lmx.flow.bean.view.AttachmentBean;
import cn.lmx.flow.bean.view.ViewBean;
import cn.lmx.flow.bean.view.json.common.ConditionBean;
import cn.lmx.flow.bean.view.json.detail.DetailBean;
import cn.lmx.flow.bean.view.json.detail.FieldBean;
import cn.lmx.flow.bean.view.json.detail.ProcessBean;
import cn.lmx.flow.bean.view.json.detail.PropertyBean;
import cn.lmx.flow.bean.view.json.head.ButtonBean;
import cn.lmx.flow.bean.view.json.head.ButtonProcBean;
import cn.lmx.flow.bean.view.json.head.CellBean;
import cn.lmx.flow.bean.view.json.head.CellPropertyBean;
import cn.lmx.flow.bean.view.json.head.DefaultValueBean;
import cn.lmx.flow.bean.view.json.head.DefaultValueMappingBean;
import cn.lmx.flow.bean.view.json.head.HeadBean;
import cn.lmx.flow.bean.view.json.head.HiddenBean;
import cn.lmx.flow.bean.view.json.list.ListViewBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.dao.module.ModuleItemDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.dao.view.AttachmentDao;
import cn.lmx.flow.dao.view.OpenWindowDao;
import cn.lmx.flow.dao.view.PublishedViewDao;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.module.ModuleItems;
import cn.lmx.flow.entity.system.DataLogs;
import cn.lmx.flow.entity.view.Attachments;
import cn.lmx.flow.entity.view.OpenWindows;
import cn.lmx.flow.entity.view.PublishedViews;
import cn.lmx.flow.utils.SqlUtils;

@Repository("ViewService")
public class ViewService {
	//已发布画面
	@Resource(name="PublishedViewDao")
	private PublishedViewDao publishedViewDao;
	//菜单
	@Resource(name="ModuleItemDao")
	private ModuleItemDao moduleItemDao;
	//模块
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	//弹窗
	@Resource(name="OpenWindowDao")
	private OpenWindowDao openWindowDao;
	//本地数据库SQL
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//远程数据库SQL
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSqlDao;
	//附件
	@Resource(name="AttachmentDao")
	private AttachmentDao attachmentDao;
	//日志
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	/**
	 * 执行存储过程
	 * @param proc
	 * @param target
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> runProc(String systemUser, String proc, String target, Map<String, Object> data) throws Exception {
		try {
			if (proc == null) {
				return null;
			}
			String runProc = URLDecoder.decode(proc, "UTF-8");
			if (runProc == null) {
				return null;
			}
			data.put("systemUser", systemUser);
			if ("R".equals(target)) {
				return businessSqlDao.select(runProc, data);
			} else {
				return sqlDao.select(runProc, data);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 根据画面编号取得画面数据
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> getViewByNo(String no) throws Exception {
		//取得发布的画面
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
		if (publishedView == null) {
			throw new Exception("对应画面数据不存在！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		ViewBean bean = new ViewBean();
		BeanUtils.copyProperties(publishedView, bean);
		map.put("viewBean", bean);
		//取得菜单数据
		ModuleItems item = moduleItemDao.getItemByNo(bean.getNo());
		if (item == null) {
			throw new Exception("该功能已被其他用户修改，请重新登录系统后重试！");
		}
		Module module = moduleDao.getModuleByNo(item.getModuleNo());
		if (module == null) {
			throw new Exception("该功能所属模块被其他用户修改，请重新登录系统后重试！");
		}
		map.put("module", module);
		map.put("item", item);
		return map;
	}
	/**
	 * 列表一栏取得
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String systemUser, String no) throws Exception {
		//取得发布的画面
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
		if (publishedView == null) {
			throw new Exception("对应画面数据不存在！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		ViewBean bean = new ViewBean();
		BeanUtils.copyProperties(publishedView, bean);
		map.put("viewBean", bean);
		//取得菜单数据
		ModuleItems item = moduleItemDao.getItemByNo(bean.getNo());
		if (item == null) {
			throw new Exception("该功能已被其他用户修改，请重新登录系统后重试！");
		}
		Module module = moduleDao.getModuleByNo(item.getModuleNo());
		if (module == null) {
			throw new Exception("该功能所属模块被其他用户修改，请重新登录系统后重试！");
		}
		map.put("module", module);
		map.put("item", item);
		Gson gson = new Gson();
		ListViewBean listViewBean = gson.fromJson(bean.getListScript(), ListViewBean.class);
		map.put("runTime", listViewBean.getRunTime());
		if (!"0".equals(listViewBean.getRunTime())) {
			//不需要立即执行SQL
			map.put("data", "[]");
			return map;
		}
		//默认查询条件
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Map<String, Object> paramSearchMap = new HashMap<String, Object>();
		List<ConditionBean> conditionList = listViewBean.getCondition();
		if (conditionList != null) {
			for (int i = 0; i < conditionList.size(); i++) {
				ConditionBean conditionBean = conditionList.get(i);
				String field = conditionBean.getAlias();
				if (field == null || "".equals(field)) {
					field = conditionBean.getField();
				}
				if (conditionBean.getDefaultValue() == null || "".equals(conditionBean.getDefaultValue())) {
					paramSearchMap.put(field, null);
					continue;
				}
				String defaultType = conditionBean.getDefaultType();
				if (defaultType == null || "".equals(defaultType)) {
					paramSearchMap.put(field, null);
					continue;
				}
				if ("SQL".equals(defaultType) || "PROC".equals(defaultType)) {
					//SQL 或 存储过程
					if (conditionBean.getDefaultField() == null || "".equals(conditionBean.getDefaultField())) {
						paramSearchMap.put(field, null);
						continue;
					}
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("systemUser", systemUser);
					paramMap.put("systemTime", sdf.format(c.getTime()));
					String defaultSql = URLDecoder.decode(conditionBean.getDefaultValue(), "UTF-8");
					List<?> defaultList = null;
					if ("R".equals(conditionBean.getDefaultTarget())) {
						defaultList = businessSqlDao.select(defaultSql, paramMap);						
					} else {
						defaultList = sqlDao.select(defaultSql, paramMap);
					}
					if (defaultList == null || defaultList.size() <= 0) {
						paramSearchMap.put(field, null);
						continue;
					}
					Map<String, Object> defaultMap = (Map<String, Object>)defaultList.get(0);
					paramSearchMap.put(field, defaultMap.get(conditionBean.getDefaultField()));
				} else if ("SCRIPT".equals(defaultType)) {
					
				} else {
					paramSearchMap.put(field, null);
				}
			}
		}
		paramSearchMap.put("systemUser", systemUser);
		paramSearchMap.put("systemTime", sdf.format(c.getTime()));
		List<?> list = queryData(listViewBean, paramSearchMap);
		map.put("data", gson.toJson(list));
		map.put("condition", gson.toJson(paramSearchMap));
		return map;
	}
	/**
	 * 查询数据
	 * @param listViewBean
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private List<?> queryData(ListViewBean listViewBean, Map<String, Object> paramMap) throws Exception {
		String sql = URLDecoder.decode(listViewBean.getSql(), "UTF-8");
		List<?> list = null;
		if ("S".equals(listViewBean.getType())) {
			//SQL文
			List<ConditionBean> conditionList = listViewBean.getCondition();
			if (paramMap == null || conditionList == null || conditionList.size() <= 0) {
				//sql = sql.replaceAll("\\$condition\\$", "Status='0'");
				sql = sql.replaceAll("\\$condition\\$", "1=1");
				if ("R".equals(listViewBean.getTarget())) {
					//远程数据库
					list = businessSqlDao.select(sql, null);
				} else {
					//本地数据库
					list = sqlDao.select(sql, null);
				}				
				return list;
			}
			Iterator<Entry<String, Object>> it = paramMap.entrySet().iterator();
			StringBuffer condition = new StringBuffer("");
			Map<String, Object> tempMap = new HashMap<String, Object>();			
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if (entry.getValue() != null) {
					ConditionBean conditionBean = null;
					for (int i = 0; i < conditionList.size(); i++) {
						ConditionBean tempBean = conditionList.get(i);						
						String fieldAlias = tempBean.getField();
						if (tempBean.getAlias() != null && !"".equals(tempBean.getAlias())) {
							fieldAlias = tempBean.getAlias();
						}
						if (entry.getKey().equals(fieldAlias)) {
							conditionBean = conditionList.get(i);
							break;
						}
					}
					if (conditionBean == null) {
						continue;
					}
					if (entry.getValue() == null || "".equals(entry.getValue())) {
						continue;
					}
					if (condition.length() > 0) {
						condition.append(" and ");
					}
					if ("eq".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("=:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					} else if ("neq".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("<>:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					} else if ("gt".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append(">:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					} else if ("get".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append(">=:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					} else if ("lt".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("<:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					} else if ("let".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("<=:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					} else if ("like".equals(conditionBean.getCompare())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							condition.append(conditionBean.getField())
								.append(" like '%")
								.append(entry.getValue())
								.append("%'");
						}
					} else if ("leftlike".equals(conditionBean.getCompare())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							condition.append(conditionBean.getField())
								.append(" like '")
								.append(entry.getValue())
								.append("%'");
						}
					} else if ("rightlike".equals(conditionBean.getCompare())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							condition.append(conditionBean.getField())
								.append(" like '%")
								.append(entry.getValue())
								.append("'");
						}
					} else {
						condition.append(conditionBean.getField())
								.append("=:")
								.append(entry.getKey());
						tempMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
			tempMap.put("systemUser", paramMap.get("systemUser"));
			if (condition.length() > 0) {
				sql = sql.replaceAll("\\$condition\\$", condition.toString());
			} else {
//				sql = sql.replaceAll("\\$condition\\$", "Status='0'");
				sql = sql.replaceAll("\\$condition\\$", "1=1");
			}
			if ("R".equals(listViewBean.getTarget())) {
				//远程数据库
				list = businessSqlDao.select(sql, tempMap);
			} else {
				//本地数据库
				list = sqlDao.select(sql, tempMap);
			}
		} else {
			//存储过程
			if (paramMap != null) {
				List<ConditionBean> conditionList = listViewBean.getCondition();
				Map<String, Object> tempMap = new HashMap<String, Object>();
				for(int i = 0; i < conditionList.size(); i++) {
					ConditionBean conditionBean = conditionList.get(i);
					String aliasName = conditionBean.getField();
					if (conditionBean.getAlias() != null && !"".equals(conditionBean.getAlias())) {
						aliasName = conditionBean.getAlias();
					}
					tempMap.put(aliasName, paramMap.get(aliasName));
				}
				if ("R".equals(listViewBean.getTarget())) {
					//远程数据库
					list = businessSqlDao.select(sql, tempMap);
				} else {
					//本地数据库
					list = sqlDao.select(sql, tempMap);
				}
			}else {
				if (listViewBean.getCondition() == null || "".equals(listViewBean.getCondition())) {
					if ("R".equals(listViewBean.getTarget())) {
						//远程数据库
						list = businessSqlDao.select(sql, paramMap);
					} else {
						//本地数据库
						list = sqlDao.select(sql, paramMap);
					}
					return list;
				}
				List<ConditionBean> conditionList = listViewBean.getCondition();
				Map<String, Object> tempMap = new HashMap<String, Object>();
				for(int i = 0; i < conditionList.size(); i++) {
					ConditionBean conditionBean = conditionList.get(i);
					String aliasName = conditionBean.getField();
					if (conditionBean.getAlias() != null && !"".equals(conditionBean.getAlias())) {
						aliasName = conditionBean.getAlias();
					}
					tempMap.put(aliasName, null);
				}
				if ("R".equals(listViewBean.getTarget())) {
					//远程数据库
					list = businessSqlDao.select(sql, tempMap);
				} else {
					//本地数据库
					list = sqlDao.select(sql, tempMap);
				}
			}
		}
		return list;
	}
	/**
	 * 查询
	 * @param no
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> query(String systemUser, String no, Map<String, Object> paramMap) throws Exception {
		//取得发布的画面
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
		if (publishedView == null) {
			throw new Exception("对应画面数据不存在！");
		}
		String listScript = publishedView.getListScript();
		if (listScript == null || "".equals(listScript)) {
			throw new Exception("画面的设定不正确，没有找到对应的列表设定信息！");
		}
		Gson gson = new Gson();
		ListViewBean listViewBean = gson.fromJson(listScript, ListViewBean.class);
		if (listViewBean == null) {
			throw new Exception("列表画面设定不正确！");
		}
		if (listViewBean.getSql() == null || "".equals(listViewBean.getSql())) {
			throw new Exception("画面设定不正确，没有设定列表取得方法！");
		}
		paramMap.put("systemUser", systemUser);
		List<?> list = this.queryData(listViewBean, paramMap);
		return list;
	}
	/**
	 * 新增画面数据取得
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> getAddData(String systemUser, String no) throws Exception {
		//取得发布的画面
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
		if (publishedView == null) {
			throw new Exception("对应画面数据不存在！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		ViewBean bean = new ViewBean();
		BeanUtils.copyProperties(publishedView, bean);
		if (bean.getDetailScript() == null || "".equals(bean.getDetailScript())) {
			bean.setDetailScript("[]");
		}
		map.put("viewBean", bean);
		//取得菜单数据
		ModuleItems item = moduleItemDao.getItemByNo(bean.getNo());
		if (item == null) {
			throw new Exception("该功能已被其他用户修改，请重新登录系统后重试！");
		}
		Module module = moduleDao.getModuleByNo(item.getModuleNo());
		if (module == null) {
			throw new Exception("该功能所属模块被其他用户修改，请重新登录系统后重试！");
		}
		map.put("module", module);
		map.put("item", item);
		//查询Head数据
		String headScript = bean.getHeadScript();
		if (headScript == null || "".equals(headScript)) {
			throw new Exception("画面设定错误，没有指定Header！");
		}
		Gson gson = new Gson();
		HeadBean headBean = gson.fromJson(headScript, HeadBean.class);
		//默认值存储Map
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		if (headBean.getDefaultValue() != null) {
			//默认值设定
			DefaultValueBean defaultValueBean = headBean.getDefaultValue();
			if (defaultValueBean.getSql() != null && !"".equals(defaultValueBean.getSql())
					&& defaultValueBean.getMapping() != null && defaultValueBean.getMapping().size() > 0) {
				String sql = URLDecoder.decode(defaultValueBean.getSql(), "UTF-8");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				//系统用户作为参数
				paramMap.put("systemUser", systemUser);
				List<?> dataList = null;
				if ("R".equals(defaultValueBean.getTarget())) {
					dataList = businessSqlDao.select(sql, paramMap);
				} else {
					dataList = sqlDao.select(sql, paramMap);
				}
				//字段映射列表
				List<DefaultValueMappingBean> mapping = defaultValueBean.getMapping();
				if (dataList != null && dataList.size() > 0) {
					//取第一条数据作为默认值
					Map<String, Object> data = (Map<String, Object>)dataList.get(0);
					Iterator<Entry<String, Object>> it = data.entrySet().iterator();
					while (it.hasNext()) {
						Entry<String, Object> entry = it.next();
						for (int j = 0; j < mapping.size(); j++) {
							DefaultValueMappingBean mappingBean = mapping.get(j);
							if (entry.getKey().equals(mappingBean.getSqlField())) {
								Map<String, Object> tempMap = resultMap.get(mappingBean.getTable());
								if (tempMap == null) {
									tempMap = new HashMap<String, Object>();
									resultMap.put(mappingBean.getTable(), tempMap);
								}
								tempMap.put(mappingBean.getViewField(), entry.getValue());
							}
						}
					}
				}
			}
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> headData = resultMap.get(headBean.getTable());
		if (headData == null) {
			dataMap.put("headData", "{}");
		} else {
			dataMap.put("headData", gson.toJson(headData));
		}
		//明细处理
		String detailScript = bean.getDetailScript();
		if (detailScript == null || "".equals(detailScript)) {			
			return map;
		}
		List<DetailBean> detailList = gson.fromJson(detailScript, new TypeToken<ArrayList<DetailBean>>(){}.getType());
		if (detailList == null) {
			return map;
		}
		for (int i = 0; i < detailList.size(); i++) {
			DetailBean detailBean = detailList.get(i);
			Map<String, Object> detailData = resultMap.get(detailBean.getTable());
			if (detailData == null) {
				dataMap.put(detailBean.getTable(), "[{}]");
			} else {
				List<Map<String, Object>> detailDataList = new ArrayList<Map<String, Object>>();
				detailDataList.add(detailData);
				dataMap.put(detailBean.getTable(), gson.toJson(detailDataList));
			}
		}
		map.put("data", dataMap);
		return map;
	}
	/**
	 * 修改画面
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> getEditData(String no, String id) throws Exception {
		//取得发布的画面
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
		if (publishedView == null) {
			throw new Exception("对应画面数据不存在！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		ViewBean bean = new ViewBean();
		BeanUtils.copyProperties(publishedView, bean);
		map.put("viewBean", bean);
		//取得菜单数据
		ModuleItems item = moduleItemDao.getItemByNo(bean.getNo());
		if (item == null) {
			throw new Exception("该功能已被其他用户修改，请重新登录系统后重试！");
		}
		Module module = moduleDao.getModuleByNo(item.getModuleNo());
		if (module == null) {
			throw new Exception("该功能所属模块被其他用户修改，请重新登录系统后重试！");
		}
		map.put("module", module);
		map.put("item", item);
		//查询Head数据
		String headScript = bean.getHeadScript();
		if (headScript == null || "".equals(headScript)) {
			throw new Exception("画面设定错误，没有指定Header！");
		}
		Gson gson = new Gson();
		HeadBean headBean = gson.fromJson(headScript, HeadBean.class);
		if (headBean == null) {
			throw new Exception("画面设定错误，Header不正确！");
		}
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
						.append(tableKey).append("=:").append(tableKey).append("\n");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(tableKey, id);			
			if ("R".equals(headBean.getTarget())) {
				headDataList = businessSqlDao.select(sql.toString(), paramMap);
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
			sql = sql.replaceAll("\\$condition\\$", tableKey + "=:" + tableKey);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(tableKey, id);
			paramMap.put("id", id);
			if ("R".equals(headBean.getTarget())) {
				//远程数据库
				headDataList = businessSqlDao.select(sql, paramMap);
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
		List<?> attaList = attachmentDao.list(no, id);
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
		String detailScript = bean.getDetailScript();
		if (detailScript == null || "".equals(detailScript)) {
			map.put("data", dataMap);
			return map;
		}
		List<DetailBean> detailList = gson.fromJson(detailScript, new TypeToken<ArrayList<DetailBean>>(){}.getType());
		if (detailList == null) {
			map.put("data", dataMap);
			return map;
		}
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
				List<FieldBean> fieldList = detailBean.getProcess().getFields();
				if (fieldList == null || fieldList.size() <= 0) {
					throw new Exception("画面明细设定错误，没有设定字段！");
				}
				for (int j = 0; j < fieldList.size(); j++) {
					FieldBean fieldBean = fieldList.get(j);
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
					detailDataList = businessSqlDao.select(sbSql.toString(), paramMap);
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
					detailDataList = businessSqlDao.select(sql, paramMap);
				} else {
					detailDataList = sqlDao.select(sql, paramMap);
				}
				resultMap.put(detailBean.getTable(), detailDataList);
			}
		}
		return resultMap;
	}
	/**
	 * 保存数据
	 * @param no
	 * @param data
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveData(String systemUser, String no, Map<String, Object> data) throws Exception {
		try {
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(no);
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
			//保存前存储过程执行
			String tableKey = headBean.getTableKey();
			if (tableKey == null || "".equals(tableKey)) {
				tableKey = "id";
			}
			if (data.get(tableKey) != null && !"".equals(data.get(tableKey))) {
				Map<String, Object> headData = (Map<String, Object>)data.get("headData");
				if (headData != null) {
					headData.put(tableKey, data.get(tableKey));
				}
			}
			SqlUtils.runProc(systemUser, headBean, data, "BeforeSave", businessSqlDao, sqlDao);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			if (data.get("id") == null || "".equals(data.get("id"))) {
				//数据新增
				StringBuffer fields = new StringBuffer("");
				StringBuffer values = new StringBuffer("");
				Map<String, Object> headDataMap = (Map<String, Object>)data.get("headData");
				if (headDataMap == null || headDataMap.size() <= 0) {
					throw new Exception("您输入的数据不正确！");
				}
				Iterator<Entry<String, Object>> it = headDataMap.entrySet().iterator();
				Map<String, Object> dataMap = new HashMap<String, Object>();
				List<CellBean> cellList = headBean.getCells();
				boolean hasTableKey = false;
				for (int i = 0; i < cellList.size(); i++) {
					CellBean cellBean = cellList.get(i);
					if (cellBean == null) {
						continue;
					}
					CellPropertyBean propBean = cellBean.getProps();
					if (propBean == null) {
						continue;
					}
					if ("Y".equals(propBean.getSave())) {
						if ("inputEditor".equals(cellBean.getType())) {
							if (propBean.getField() != null && !"".equals(propBean.getField())) {
								if (tableKey.equals(propBean.getField())) {
									hasTableKey = true;
								}
								fields.append(propBean.getField()).append(",");
								values.append(":").append(propBean.getField()).append(",");
								dataMap.put(propBean.getField(), headDataMap.get(propBean.getField()));
							}
							if (propBean.getHidden() != null && !"".equals(propBean.getHidden())) {
								HiddenBean hiddenBean = gson.fromJson(propBean.getHidden(), HiddenBean.class);
								if (hiddenBean == null) {
									continue;
								}
								if ("Y".equals(hiddenBean.getSave())) {
									if (tableKey.equals(hiddenBean.getField())) {
										hasTableKey = true;
									}
									fields.append(hiddenBean.getField()).append(",");
									values.append(":").append(hiddenBean.getField()).append(",");
									dataMap.put(hiddenBean.getField(), headDataMap.get(hiddenBean.getField()));
								}
							}
						} else 	if ("checkEditor".equals(cellBean.getType()) ||
								"dateEditor".equals(cellBean.getType()) ||
								"selectEditor".equals(cellBean.getType()) ||
								"mutilInputEditor".equals(cellBean.getType()) ||
								"label".equals(cellBean.getType())) {
							if (propBean.getField() != null && !"".equals(propBean.getField())) {
								if (tableKey.equals(propBean.getField())) {
									hasTableKey = true;
								}
								fields.append(propBean.getField()).append(",");
								values.append(":").append(propBean.getField()).append(",");
								dataMap.put(propBean.getField(), headDataMap.get(propBean.getField()));
							}
						} else if ("openEditor".equals(cellBean.getType())) {
							if (propBean.getNoField() != null && !"".equals(propBean.getNoField())) {
								if (tableKey.equals(propBean.getNoField())) {
									hasTableKey = true;
								}
								fields.append(propBean.getNoField()).append(",");
								values.append(":").append(propBean.getNoField()).append(",");
								dataMap.put(propBean.getNoField(), headDataMap.get(propBean.getNoField()));
							}
							if ("Y".equals(propBean.getSaveName())) {
								if (propBean.getNameField() != null && !"".equals(propBean.getNameField())) {
									if (tableKey.equals(propBean.getNameField())) {
										hasTableKey = true;
									}
									fields.append(propBean.getNameField()).append(",");
									values.append(":").append(propBean.getNameField()).append(",");
									dataMap.put(propBean.getNameField(), headDataMap.get(propBean.getNameField()));
								}
							}
						}
					}
				}
				/*				
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if ("id".equals(entry.getKey())) {
						continue;
					}
					fields.append(entry.getKey()).append(",");
					values.append(":").append(entry.getKey()).append(",");
					dataMap.put(entry.getKey(), entry.getValue());
				}*/
				if (fields.length() <= 0) {
					throw new Exception("没有数据需要保存！");
				}
				//fields.append("ID,");
				//fields.append("Status,");
				//创建人
				fields.append("CreateUser,");
				//创建时间
				fields.append("CreateTime");
				//values.append(":ID,");
				//values.append(":status,");
				values.append(":createUser,");
				values.append(":createTime");
				if (hasTableKey == false) {
					//没有设定主键值
					fields.append(tableKey);
					values.append(":" + tableKey);
				}
				String sql = new StringBuffer("")
						.append("insert into ").append(headBean.getTable()).append("(")
						.append(fields)
						.append(") values (")
						.append(values).append(")").toString();
				String id = null;
				if (headDataMap.get(tableKey) != null && !"".equals(headDataMap.get(tableKey))) {
					id = String.valueOf(headDataMap.get(tableKey));
				} else {
					id = UUID.randomUUID().toString();;
				}
				dataMap.put("ID", id);
				dataMap.put(tableKey, id);
				dataMap.put("status", "0");	
				dataMap.put("createUser", systemUser);
				dataMap.put("createTime", sdf.format(c.getTime()));
				if ("R".equals(headBean.getTarget())) {
					businessSqlDao.update(sql, dataMap);
				} else {
					sqlDao.update(sql, dataMap);
				}
				//将id新增到data中
				data.put("id", id);
				headDataMap.put("ID", id);
				//数据新增Log
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Add");
				dataLog.setProcessor("新增");
				dataLog.setDataTable(headBean.getTable());
				dataLog.setAfterData(gson.toJson(dataMap));
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
				//明细数据
				if (detailBeanList.size() > 0) {
					//存在明细数据
					Iterator<Entry<String, Object>> itData = data.entrySet().iterator();
					while (itData.hasNext()) {
						Entry<String, Object> entry = itData.next();
						if ("headData".equals(entry.getKey()) || "id".equals(entry.getKey()) || "attachment".equals(entry.getKey()) || tableKey.equals(entry.getKey())) {
							continue;
						}
						//取得每个数据表的数据
						Map<String, Object> detailDataMap = (Map<String, Object>)entry.getValue();
						if (detailDataMap == null || detailDataMap.size() <= 0) {
							continue;
						}
						Iterator<Entry<String, Object>> itDetailData = detailDataMap.entrySet().iterator();
						while (itDetailData.hasNext()) {
							Entry<String, Object> entryDetailData = itDetailData.next();
							String target = null;
							DetailBean detailBean = null;
							for (int i = 0; i < detailBeanList.size(); i++) {
								DetailBean detailBean1 = detailBeanList.get(i);
								if (entryDetailData.getKey().equals(detailBean1.getTable())) {
									if (detailBean1.getProcess() != null) {
										target = detailBean1.getProcess().getTarget();
										detailBean = detailBean1;
										break;
									}
								}
							}
							if (target == null || "".equals(target)) {
								throw new Exception("画面设定不正确！");
							}
							List<Map<String, Object>> dataList = (List<Map<String, Object>>)entryDetailData.getValue();
							String detailSql = null;
							for (int i = 0; i < dataList.size(); i++) {
								if (detailSql == null) {
									//生产SQL
									Map<String, Object> detailData = dataList.get(i);
									Iterator<Entry<String, Object>> itDetail = detailData.entrySet().iterator();
									StringBuffer detailFields = new StringBuffer("");
									StringBuffer detailValues = new StringBuffer("");
									ProcessBean processBean = detailBean.getProcess();
									List<FieldBean> fieldList = processBean.getFields();
									for (int j = 0; j < fieldList.size(); j++) {
										FieldBean fieldBean = fieldList.get(j);
										PropertyBean propBean = fieldBean.getProps();
										if ("Y".equals(propBean.getSave())) {
													if (propBean == null) {
												continue;
											}
											if ("inputEditor".equals(fieldBean.getEditor())) {
												if (propBean.getField() != null && !"".equals(propBean.getField())) {
													detailFields.append(propBean.getField()).append(",");
													detailValues.append(":").append(propBean.getField()).append(",");
												}
												if (propBean.getHidden() != null && !"".equals(propBean.getHidden())) {
													HiddenBean hiddenBean = gson.fromJson(propBean.getHidden(), HiddenBean.class);
													if (hiddenBean == null) {
														continue;
													}
													if ("Y".equals(hiddenBean.getSave())) {
														detailFields.append(hiddenBean.getField()).append(",");
														detailValues.append(":").append(hiddenBean.getField()).append(",");
													}
												}
											} else 	if ("checkEditor".equals(fieldBean.getEditor()) ||
													"dateEditor".equals(fieldBean.getEditor()) ||
													"selectEditor".equals(fieldBean.getEditor()) ||
													"mutilInputEditor".equals(fieldBean.getEditor())) {
												if (propBean.getField() != null && !"".equals(propBean.getField())) {
													detailFields.append(propBean.getField()).append(",");
													detailValues.append(":").append(propBean.getField()).append(",");
												}
											} else if ("openEditor".equals(fieldBean.getEditor())) {
												if (propBean.getNoField() != null && !"".equals(propBean.getNoField())) {
													detailFields.append(propBean.getNoField()).append(",");
													detailValues.append(":").append(propBean.getNoField()).append(",");
												}
												if (propBean.getNameField() != null && !"".equals(propBean.getNameField())) {
													detailFields.append(propBean.getNameField()).append(",");
													detailValues.append(":").append(propBean.getNameField()).append(",");
												}
											}
										}
									}
									/*
									while (itDetail.hasNext()) {
										Entry<String, Object> entryDetail = itDetail.next();
										if ("id".equals(entryDetail.getKey())) {
											continue;
										}
										detailFields.append(entryDetail.getKey()).append(",");
										detailValues.append(":")
													.append(entryDetail.getKey()).append(",");
									}*/
									detailFields.append("ID,");
									detailFields.append("Status,");
									detailFields.append("ParentId,");
									//创建人
									detailFields.append("CreateUser,");
									//创建时间
									detailFields.append("CreateTime");
									detailValues.append(":ID,");
									detailValues.append(":status,");
									detailValues.append(":parentId,");
									detailValues.append(":createUser,");
									detailValues.append(":createTime");
									detailSql = new StringBuffer("")
											.append("insert into ").append(entryDetailData.getKey()).append("(")
											.append(detailFields)
											.append(") values (")
											.append(detailValues).append(")").toString();
								}
								Map<String, Object> detailData = dataList.get(i);						
								detailData.put("ID", UUID.randomUUID().toString());
								detailData.put("parentId", id);
								detailData.put("status", "0");	
								detailData.put("createUser", systemUser);
								detailData.put("createTime", sdf.format(c.getTime()));
								if ("R".equals(target)) {
									businessSqlDao.update(detailSql, detailData);
								} else {
									sqlDao.update(detailSql, detailData);
								}
								//数据新增Log
								dataLog = new DataLogs();
								dataLog.setId(UUID.randomUUID().toString());
								dataLog.setType("Add");
								dataLog.setProcessor("新增");
								dataLog.setDataTable(entryDetailData.getKey());
								dataLog.setAfterData(gson.toJson(dataMap));
								dataLog.setCreateUser(systemUser);
								dataLog.setCreateTime(sdf.format(c.getTime()));
								dataLogDao.add(dataLog);
							}
						}
					}
				}
				//附件
				if (data.get("attachment") != null) {
					List<String> attachments = (List<String>)data.get("attachment");
					if (attachments != null && attachments.size() > 0) {
						for (int i = 0; i < attachments.size(); i++) {
							Attachments attachment = attachmentDao.getById(attachments.get(i));
							if (attachment != null) {
								attachment.setDataId(id);
								attachmentDao.edit(attachment);
							}
						}
					}
				}
			} else {
				//数据修改
				StringBuffer fields = new StringBuffer("");
				Map<String, Object> headData = (Map<String, Object>)data.get("headData");
				Iterator<Entry<String, Object>> it = headData.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if (tableKey.equals(entry.getKey())) {
						continue;
					}
					fields.append(entry.getKey()).append("=:").append(entry.getKey()).append(",");
				}
				if (fields.length() <= 0) {
					throw new Exception("没有数据需要保存！");
				}
				String sql = new StringBuffer("")
						.append("update\n")
						.append(headBean.getTable()).append("\n")
						.append("set\n")
						.append(fields).append("\n")
						.append("UpdateUser=:systemUser,\n")
						.append("UpdateTime=:updateTime\n")
						.append("where\n")
						.append(tableKey)
						.append("=:id").toString();
				headData.put("id", data.get("id"));
				headData.put("systemUser", systemUser);
				headData.put("updateTime", sdf.format(c.getTime()));
				List<?> beforeData = null;
				if ("R".equals(headBean.getTarget())) {
					beforeData = businessSqlDao.select("select * from " + headBean.getTable() + " where " + tableKey + "=:id", headData);
					businessSqlDao.update(sql, headData);
				} else {
					beforeData = sqlDao.select("select * from " + headBean.getTable() + " where " + tableKey + "=:id", headData);
					sqlDao.update(sql, headData);
				}
				//数据新增Log
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Update");
				dataLog.setProcessor("修改");
				dataLog.setDataTable(headBean.getTable());
				if (beforeData != null && beforeData.size() > 0) {
					dataLog.setBeforeData(gson.toJson(beforeData.get(0)));
				}
				dataLog.setAfterData(gson.toJson(headData));
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
				//明细数据
				if (detailBeanList.size() > 0) {
					//存在明细数据
					Iterator<Entry<String, Object>> itData = data.entrySet().iterator();
					while (itData.hasNext()) {
						Entry<String, Object> entry = itData.next();
						if ("headData".equals(entry.getKey()) || "id".equals(entry.getKey()) || "attachment".equals(entry.getKey())) {
							continue;
						}
						//取得每个数据表的数据
						Map<String, Object> detailDataMap = (Map<String, Object>)entry.getValue();
						if (detailDataMap == null || detailDataMap.size() <= 0) {
							continue;
						}
						Iterator<Entry<String, Object>> itDetailData = detailDataMap.entrySet().iterator();
						while (itDetailData.hasNext()) {
							Entry<String, Object> entryDetailData = itDetailData.next();
							String target = null;
							for (int i = 0; i < detailBeanList.size(); i++) {
								DetailBean detailBean = detailBeanList.get(i);
								if (entryDetailData.getKey().equals(detailBean.getTable())) {
									if (detailBean.getProcess() != null) {
										target = detailBean.getProcess().getTarget();
										break;
									}
								}
							}
							if (target == null || "".equals(target)) {
								throw new Exception("画面设定不正确！");
							}
							List<Map<String, Object>> dataList = (List<Map<String, Object>>)entryDetailData.getValue();
							String editDetailSql = null;
							String addDetailSql = null;
							List<String> ids = new ArrayList<String>();
							for (int i = 0; i < dataList.size(); i++) {
								Map<String, Object> detailData = dataList.get(i);
								if (detailData.get("id") == null || "".equals(detailData.get("id"))) {
									//新增数据
									if (addDetailSql == null) {
										Iterator<Entry<String, Object>> itDetail = detailData.entrySet().iterator();
										StringBuffer detailFields = new StringBuffer("");
										StringBuffer detailValues = new StringBuffer("");
										while (itDetail.hasNext()) {
											Entry<String, Object> entryDetail = itDetail.next();
											if ("id".equals(entryDetail.getKey())) {
												continue;
											}
											detailFields.append(entryDetail.getKey()).append(",");
											detailValues.append(":")
														.append(entryDetail.getKey()).append(",");
										}
										detailFields.append("ID,");
										detailFields.append("Status,");
										detailFields.append("ParentId,");
										//创建人
										detailFields.append("CreateUser,");
										//创建时间
										detailFields.append("CreateTime");
										detailValues.append(":ID,");
										detailValues.append(":status,");
										detailValues.append(":parentId,");
										detailValues.append(":createUser,");
										detailValues.append(":createTime");
										addDetailSql = new StringBuffer("")
												.append("insert into ").append(entryDetailData.getKey()).append("(")
												.append(detailFields)
												.append(") values (")
												.append(detailValues).append(")").toString();
									}
									ids.add(UUID.randomUUID().toString());
									detailData.put("ID", ids.get(ids.size() - 1));
									detailData.put("parentId", data.get("id"));
									detailData.put("status", "0");	
									detailData.put("createUser", systemUser);
									detailData.put("createTime", sdf.format(c.getTime()));
									if ("R".equals(target)) {
										businessSqlDao.update(addDetailSql, detailData);
									} else {
										sqlDao.update(addDetailSql, detailData);
									}
									//数据新增Log
									dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setType("Add");
									dataLog.setProcessor("新增");
									dataLog.setDataTable(entryDetailData.getKey());
									dataLog.setAfterData(gson.toJson(headData));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								} else {
									ids.add(String.valueOf(detailData.get("id")));
									//修改数据
									if (editDetailSql == null) {
										//生成SQL
										Iterator<Entry<String, Object>> itDetail = detailData.entrySet().iterator();
										StringBuffer detailFields = new StringBuffer("");
										while (itDetail.hasNext()) {
											Entry<String, Object> entryDetail = itDetail.next();
											if ("id".equals(entryDetail.getKey())) {
												continue;
											}
											detailFields.append(entryDetail.getKey()).append("=:").append(entryDetail.getKey()).append(",");
										}
										detailFields.append("Status=:status,");
										//修改人
										detailFields.append("UpdateUser=:systemUser,");
										//修改时间
										detailFields.append("UpdateTime=:updateTime");
										editDetailSql = new StringBuffer("")
												.append("update\n")
												.append("	").append(entryDetailData.getKey()).append("\n")
												.append("set\n")
												.append(detailFields).append("\n")
												.append("where\n")
												.append("	ID=:id").toString();
									}		
									detailData.put("id", detailData.get("id"));
									detailData.put("parentId", data.get("id"));
									detailData.put("status", "0");	
									detailData.put("systemUser", systemUser);
									detailData.put("updateTime", sdf.format(c.getTime()));
									beforeData = null;
									if ("R".equals(target)) {
										beforeData = businessSqlDao.select("select * from " + entryDetailData.getKey() + " where ID=:id", detailData);
										businessSqlDao.update(editDetailSql, detailData);
									} else {
										beforeData = sqlDao.select("select * from " + entryDetailData.getKey() + " where ID=:id", detailData);
										sqlDao.update(editDetailSql, detailData);
									}
									//数据新增Log
									dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setType("Update");
									dataLog.setProcessor("修改");
									dataLog.setDataTable(entryDetailData.getKey());
									if (beforeData != null && beforeData.size() > 0) {
										dataLog.setBeforeData(gson.toJson(beforeData.get(0)));
									}
									dataLog.setAfterData(gson.toJson(headData));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}							
							}
							StringBuffer notInIds = new StringBuffer("");
							if (ids.size() > 0) {
								for (int m = 0; m < ids.size(); m++) {
									notInIds.append("'").append(ids.get(m)).append("',");
								}
							}
							//删除不需要的数据
							StringBuffer deleteSql = new StringBuffer("")
											.append("delete from ")
											.append(entryDetailData.getKey())
											.append(" where ParentId=:id");
							if (notInIds.length() > 0) {
								deleteSql.append(" and ID not in(")
									.append(notInIds.substring(0, notInIds.length() - 1))
									.append(")");
							}
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("id", data.get("id"));
							beforeData = null;
							if ("R".equals(target)) {
								beforeData = businessSqlDao.select("select * from " + entryDetailData.getKey() + " where ID=:id", paramMap);
								businessSqlDao.update(deleteSql.toString(), paramMap);
							} else {
								beforeData = sqlDao.select("select * from " + entryDetailData.getKey() + " where ID=:id", paramMap);
								sqlDao.update(deleteSql.toString(), paramMap);
							}
							//数据新增Log
							dataLog = new DataLogs();
							dataLog.setId(UUID.randomUUID().toString());
							dataLog.setType("Delete");
							dataLog.setProcessor("删除");
							dataLog.setDataTable(entryDetailData.getKey());
							if (beforeData != null && beforeData.size() > 0) {
								dataLog.setBeforeData(gson.toJson(beforeData.get(0)));
							}
							dataLog.setCreateUser(systemUser);
							dataLog.setCreateTime(sdf.format(c.getTime()));
							dataLogDao.add(dataLog);
						}
					}
				}
			}			
			//保存后存储过程执行
			SqlUtils.runProc(systemUser, headBean, data, "BackSave", businessSqlDao, sqlDao);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			} else {
				throw e;
			}
		}
	}
	/**
	 * 审核
	 * @param systemUser
	 * @param no
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void auditData(String systemUser, String no, String[] ids) throws Exception {
		try {
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(no);
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
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			for (int i = 0; i < ids.length; i++) {
				//取得数据
				Map<String, Object> dataMap = SqlUtils.getDbData(headBean, detailBeanList, ids[i], businessSqlDao, sqlDao);
				if (dataMap == null) {
					dataMap = new HashMap<String, Object>();
				}
				Map<String, Object> headData = (Map<String, Object>)dataMap.get("headData");
				if (headData == null) {
					throw new Exception("数据已被他人删除，请返回一栏画面确认！");
				}
				Object objStatus = headData.get("Status");
				if (objStatus != null) {
					String status = String.valueOf(objStatus);
					if ("1".equals(status)) {
						throw new Exception("数据已提交，请确认！");
					}
					if ("2".equals(status)) {
						throw new Exception("数据已审核，请确认！");
					}
					if (!"0".equals(status)) {
						throw new Exception("数据状态不正确，请确认！");
					}
				}
				//审核前存储过程执行
				SqlUtils.runProc(systemUser, headBean, dataMap, "BeforeAudit", businessSqlDao, sqlDao);
				//修改Head审核码
				String sql = new StringBuffer("")
							.append("update ")
							.append(headBean.getTable())
							.append(" set Status=:status,AuditUser=:systemUser,AuditTime=:systemTime,UpdateUser=:systemUser,UpdateTime=:systemTime where ID=:id").toString();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", ids[i]);
				paramMap.put("status", "2");
				paramMap.put("systemUser", systemUser);
				paramMap.put("systemTime", sdf.format(c.getTime()));
				if ("R".equals(headBean.getTarget())) {
					businessSqlDao.update(sql, paramMap);
				} else {
					sqlDao.update(sql, paramMap);
				}
				//数据新增Log
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Audit");
				dataLog.setProcessor("审核");
				dataLog.setDataTable(headBean.getTable());
				String befData = new StringBuffer("")
						.append("{id:\"")
						.append(ids[i])
						.append("\",status:\"0\",AuditUser:\"")
						.append(headData.get("AuditUser"))
						.append("\",AuditTime:\"")
						.append(headData.get("AuditTime"))
						.append("\",UpdateUser:\"")
						.append(headData.get("UpdateUser"))
						.append("\",UpdateTime:\"")
						.append(headData.get("UpdateTime"))
						.append("\"}").toString();
				dataLog.setBeforeData(befData);
				String bakData = new StringBuffer("")
						.append("{id:\"")
						.append(ids[i])
						.append("\",status:\"2\",AuditUser:\"")
						.append(systemUser)
						.append("\",AuditTime:\"")
						.append(sdf.format(c.getTime()))
						.append("\",UpdateUser:\"")
						.append(systemUser)
						.append("\",UpdateTime:\"")
						.append(sdf.format(c.getTime()))
						.append("\"}").toString();						
				dataLog.setAfterData(bakData);
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);	
				//审核后存储过程执行
				SqlUtils.runProc(systemUser, headBean, dataMap, "BackAudit", businessSqlDao, sqlDao);
			}			
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 撤销审核
	 * @param systemUser
	 * @param no
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void cancelAuditData(String systemUser, String no, String[] ids) throws Exception {
		try {
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(no);
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
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			for (int i = 0; i < ids.length; i++) {
				//取得数据
				Map<String, Object> dataMap = SqlUtils.getDbData(headBean, detailBeanList, ids[i], businessSqlDao, sqlDao);
				if (dataMap == null) {
					dataMap = new HashMap<String, Object>();
				}
				Map<String, Object> headData = (Map<String, Object>)dataMap.get("headData");
				if (headData == null) {
					throw new Exception("数据已被他人删除，请返回一栏画面确认！");
				}
				Object objStatus = headData.get("Status");
				if (objStatus != null) {
					String status = String.valueOf(objStatus);
					if ("0".equals(status)) {
						throw new Exception("数据没有审核，无需弃审！");
					}
					if ("1".equals(status)) {
						throw new Exception("数据为提交状态，无法弃审！");
					}
					if (!"2".equals(status)) {
						throw new Exception("数据状态不正确，请确认！");
					}
				} else {
					throw new Exception("数据状态不正确，请确认！");
				}
				//取消审核前存储过程执行
				SqlUtils.runProc(systemUser, headBean, dataMap, "BeforeCancelAudit", businessSqlDao, sqlDao);
				//修改Head审核码
				String sql = new StringBuffer("")
							.append("update ")
							.append(headBean.getTable())
							.append(" set Status=:status,AuditUser=NULL,AuditTime=NULL,UpdateUser=:systemUser,UpdateTime=:systemTime where ID=:id").toString();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", ids[i]);
				paramMap.put("status", "0");
				paramMap.put("systemUser", systemUser);
				paramMap.put("systemTime", sdf.format(c.getTime()));
				if ("R".equals(headBean.getTarget())) {
					businessSqlDao.update(sql, paramMap);
				} else {
					sqlDao.update(sql, paramMap);
				}
				if (headData != null) {
					//数据新增Log
					DataLogs dataLog = new DataLogs();
					dataLog.setId(UUID.randomUUID().toString());
					dataLog.setType("CancelAudit");
					dataLog.setProcessor("撤销审核");
					dataLog.setDataTable(headBean.getTable());
					String befData = new StringBuffer("")
							.append("{id:\"")
							.append(ids[i])
							.append("\",status:\"2\",AuditUser:\"")
							.append(headData.get("AuditUser"))
							.append("\",AuditTime:\"")
							.append(headData.get("AuditTime"))
							.append("\",UpdateUser:\"")
							.append(headData.get("UpdateUser"))
							.append("\",UpdateTime:\"")
							.append(headData.get("UpdateTime"))
							.append("\"}").toString();
					dataLog.setBeforeData(befData);
					String bakData = new StringBuffer("")
							.append("{id:\"")
							.append(ids[i])
							.append("\",status:\"0\",AuditUser:\"")
							.append(systemUser)
							.append("\",AuditTime:\"")
							.append(sdf.format(c.getTime()))
							.append("\",UpdateUser:\"")
							.append(systemUser)
							.append("\",UpdateTime:\"")
							.append(sdf.format(c.getTime()))
							.append("\"}").toString();						
					dataLog.setAfterData(bakData);
					dataLog.setCreateUser(systemUser);
					dataLog.setCreateTime(sdf.format(c.getTime()));
					dataLogDao.add(dataLog);
				}
				//审核后存储过程执行
				SqlUtils.runProc(systemUser, headBean, dataMap, "BackCancelAudit", businessSqlDao, sqlDao);
			}			
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 删除画面数据
	 * @param no
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteData(String systemUser, String no, String[] ids) throws Exception {
		if (ids == null || ids.length <= 0) {
			return;
		}
		//取得发布的画面
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
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
		if (headBean.getTable() == null || "".equals(headBean.getTable())) {
			throw new Exception("画面Head设定不正确，没有指定数据表！");
		}
		//明细Script
		String detailScript = publishedView.getDetailScript();
		List<DetailBean> detailList = null;
		if (detailScript != null && !"".equals(detailScript)) {
			detailList = gson.fromJson(detailScript, new TypeToken<List<DetailBean>>(){}.getType());
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		//生成SQL文
		String tableKey = headBean.getTableKey();
		if (tableKey == null || "".equals(tableKey)) {
			tableKey = "id";
		}
		String sql = new StringBuffer("")
					.append("delete from ")
					.append(headBean.getTable())
					.append(" where ").append(tableKey).append("=:id").toString();
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			if (id == null || "".equals(id)) {
				continue;
			}
			//删除前数据取得
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", id);
			//取得删除前Head
			Map<String, Object> data = new HashMap<String, Object>();
			List<?> headData = null;
			if ("R".equals(headBean.getTarget())) {
				headData = businessSqlDao.select("select * from " + headBean.getTable() + " where " + tableKey + "=:id", paramMap);
			} else {
				headData = sqlDao.select("select * from " + headBean.getTable() + " where " + tableKey + "=:id", paramMap);
			}
			if (headData != null && headData.size() > 0) {
				data.put("headData", headData.get(0));
			}
			//取得删除前明细数据
			if (detailList != null) {
				for (int j = 0; j < detailList.size(); j++) {
					DetailBean detailBean = detailList.get(j);
					if (detailBean.getProcess() == null) {
						continue;
					}
					List<?> detailData = null;
					if ("R".equals(detailBean.getProcess().getTarget())) {
						detailData = businessSqlDao.select("select * from " + detailBean.getTable() + " where ParentId=:id", paramMap);
					} else {
						detailData = sqlDao.select("select * from " + detailBean.getTable() + " where ParentId=:id", paramMap);
					}
					if (detailData != null) {
						data.put(detailBean.getTable(), detailData);
					}
				}
			}
			//执行删除前存储过程			
			SqlUtils.runProc(systemUser, headBean, data, "BeforeDelete", businessSqlDao, sqlDao);
			if ("R".equals(headBean.getTarget())) {
				businessSqlDao.update(sql, paramMap);
			} else {
				sqlDao.update(sql, paramMap);
			}
			//写HeadLog
			if (headData != null && headData.size() > 0) {
				//数据新增Log
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Delete");
				dataLog.setProcessor("删除");
				dataLog.setDataTable(headBean.getTable());
				dataLog.setBeforeData(gson.toJson(headData.get(0)));
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
			}
			//删除明细数据
			if (detailList == null || detailList.size() <= 0) {
				//执行删除后存储过程			
				SqlUtils.runProc(systemUser, headBean, data, "BackDelete", businessSqlDao, sqlDao);
				//删除附件
				attachmentDao.deleteByVoucherId(id);
				continue;
			}
			for (int j = 0; j < detailList.size(); j++) {
				DetailBean detailBean = detailList.get(j);
				if (detailBean.getProcess() == null) {
					continue;
				}
				//生成SQL文
				String detailSql = new StringBuffer("")
							.append("delete from ")
							.append(detailBean.getTable())
							.append(" where ParentId=:id").toString();
				if ("R".equals(detailBean.getProcess().getTarget())) {
					businessSqlDao.update(detailSql, paramMap);
				} else {
					sqlDao.update(detailSql, paramMap);
				}
			}
			//写明细Log
			Iterator<Entry<String, Object>> it = data.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if (entry.getKey().equals("headData")) {
					continue;
				}
				List<?> list = (List<?>)entry.getValue();
				if (list == null || list.size() <= 0) {
					continue;
				}
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> detailMap = (Map<String, Object>)list.get(j);
					//数据新增Log
					DataLogs dataLog = new DataLogs();
					dataLog.setId(UUID.randomUUID().toString());
					dataLog.setType("Delete");
					dataLog.setProcessor("删除");
					dataLog.setDataTable(entry.getKey());
					dataLog.setBeforeData(gson.toJson(detailMap));
					dataLog.setCreateUser(systemUser);
					dataLog.setCreateTime(sdf.format(c.getTime()));
					dataLogDao.add(dataLog);
				}
			}
			//执行删除后存储过程			
			SqlUtils.runProc(systemUser, headBean, data, "BackDelete", businessSqlDao, sqlDao);
			//删除附件
			attachmentDao.deleteByVoucherId(id);
		}
	}
	/**
	 * 取得下拉列表数据
	 * @param sql
	 * @param target
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> dropdownList(String systemUser, String sql, String target) throws Exception {
		try {
			if (sql == null || "".equals(sql)) {
				throw new Exception("没有指定下拉列表的取得SQL！");
			}
			sql = sql.replaceAll("\\$condition\\$", "1=1");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("systemUser", systemUser);
			List<?> list = null;
			if ("R".equals(target)) {
				list = businessSqlDao.select(sql, map);
			} else {
				list = sqlDao.select(sql, map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 开启弹出窗口
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> openWindow(String systemUser, String no, Map<String, Object> paramData) throws Exception {
		OpenWindows openWindow = openWindowDao.getByNo(no);
		if (openWindow == null) {
			throw new Exception("指定的弹出窗口不存在！");
		}
		List<?> list = queryOpenWinData(systemUser, openWindow, paramData);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openWin", openWindow);
		map.put("data", list);
		return map;
	}
	/**
	 * 查询弹出窗口数据
	 * @param openWindow
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private List<?> queryOpenWinData(String systemUser, OpenWindows openWindow, Map<String, Object> map) throws Exception {
		String sql = openWindow.getSql();
		if (sql == null || "".equals(sql)) {
			throw new Exception("指定的弹出窗口的SQL没有设定！");
		}
		String sqlProcFlag = openWindow.getSqlProcFlag();
		List<?> list = null;
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<ConditionBean>>(){}.getType();			        
		List<ConditionBean> conditionList = gson.fromJson(openWindow.getCondition(), type);
		if (conditionList == null) {
			conditionList = new ArrayList<ConditionBean>();
		}
		if ("S".equals(sqlProcFlag)) {
			//SQL
			if (map == null) {
				sql = sql.replaceAll("\\$condition\\$", "1=1");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("systemUser", systemUser);
				if ("L".equals(openWindow.getTargetDatabase())) {
					//本地数据库
					list = sqlDao.select(sql, paramMap);
				} else {
					//远程数据库
					list = businessSqlDao.select(sql, paramMap);
				}				
				return list;
			}
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			StringBuffer condition = new StringBuffer("");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				if (entry.getValue() != null) {
					ConditionBean conditionBean = null;
					for (int i = 0; i < conditionList.size(); i++) {
						String sField = conditionList.get(i).getField();
						sField = sField.replaceAll("\\.", "_");
						if (entry.getKey().equals(sField)) {
							conditionBean = conditionList.get(i);
							break;
						}
					}
					if (conditionBean == null) {
						continue;
					}
					if (entry.getValue() == null || "".equals(entry.getValue())) {
						continue;
					}
					if (condition.length() > 0) {
						condition.append(" and ");
					}
					if ("eq".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("=:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					} else if ("neq".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("<>:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					} else if ("gt".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append(">:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					} else if ("get".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append(">=:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					} else if ("lt".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("<:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					} else if ("let".equals(conditionBean.getCompare())) {
						condition.append(conditionBean.getField())
								.append("<=:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					} else if ("like".equals(conditionBean.getCompare())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							condition.append(conditionBean.getField())
								.append(" like '%")
								.append(entry.getValue())
								.append("%'");
						}
					} else if ("leftlike".equals(conditionBean.getCompare())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							condition.append(conditionBean.getField())
								.append(" like '")
								.append(entry.getValue())
								.append("%'");
						}
					} else if ("rightlike".equals(conditionBean.getCompare())) {
						if (entry.getValue() != null && !"".equals(entry.getValue())) {
							condition.append(conditionBean.getField())
								.append(" like '%")
								.append(entry.getValue())
								.append("'");
						}
					} else {
						condition.append(conditionBean.getField())
								.append("=:")
								.append(conditionBean.getField().replaceAll("\\.", "_"));
						paramMap.put(conditionBean.getField().replaceAll("\\.", "_"), entry.getValue());
					}
				}
			}
			Map<String, Object> tempMap = (Map<String, Object>)map.get("viewData");
			if (tempMap != null) {
				paramMap.putAll(tempMap);
			}
			if (condition.length() > 0) {
				sql = sql.replaceAll("\\$condition\\$", condition.toString());
			} else {
				sql = sql.replaceAll("\\$condition\\$", "1=1");
			}
			paramMap.put("systemUser", systemUser);
			if ("L".equals(openWindow.getTargetDatabase())) {
				//本地数据库
				list = sqlDao.select(sql, paramMap);
			} else {
				//远程数据库
				list = businessSqlDao.select(sql, paramMap);
			}
		} else {
			//取得条件
			if (map == null) {
				map = new HashMap<String, Object>();
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for (int i = 0; i < conditionList.size(); i++) {
				ConditionBean conditionBean = conditionList.get(i);
				String source = conditionBean.getSource();
				if ("condition".equals(source)) {
					//数据来自画面条件
					String field = conditionBean.getField().replaceAll("\\.", "_");
					paramMap.put(field, map.get(field));
				}
			}
			Map<String, Object> tempMap = (Map<String, Object>)map.get("viewData");
			if (tempMap != null) {
				paramMap.putAll(tempMap);
			}
			paramMap.put("systemUser", systemUser);
			if ("L".equals(openWindow.getTargetDatabase())) {
				//本地数据库
				list = sqlDao.select(sql, paramMap);
			} else {
				//远程数据库
				list = businessSqlDao.select(sql, paramMap);
			}
		}
		return list;
	}
	/**
	 * 自定义按钮处理
	 * @param systemUser
	 * @param no
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<Integer, Object> processButton(String systemUser, String no, String buttonId, Map<String, Object> data) throws Exception {
		try {
			//取得发布的画面
			PublishedViews publishedView = publishedViewDao.getViewByNo(no);
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
			if (headBean.getButtons() == null || headBean.getButtons().size() <= 0) {
				throw new Exception("没有设定按钮信息！");
			}
			ButtonBean buttonBean = null;
			for (int i = 0; i < headBean.getButtons().size(); i++) {
				ButtonBean button = headBean.getButtons().get(i);
				if (buttonId.equals(button.getId())) {
					buttonBean = button;
					break;
				}
			}
			if (buttonBean == null) {
				//没有指定的按钮
				throw new Exception("没有按钮（" + buttonId + "）！");
			}
			if (buttonBean.getProc() == null || buttonBean.getProc().size() <= 0) {
				throw new Exception("按钮（" + buttonBean.getName() + "）没有设定处理逻辑！");
			}
			//创建Head参数
			Map<String, Object> headDataMap = (Map<String, Object>)data.get("headData");
			if (headDataMap == null || headDataMap.size() <= 0) {
				throw new Exception("您输入的数据不正确！");
			}
			Iterator<Entry<String, Object>> it = headDataMap.entrySet().iterator();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("id", data.get("id"));
			dataMap.put("systemUser", systemUser);
			List<CellBean> cellList = headBean.getCells();
			for (int i = 0; i < cellList.size(); i++) {
				CellBean cellBean = cellList.get(i);
				if (cellBean == null) {
					continue;
				}
				CellPropertyBean propBean = cellBean.getProps();
				if (propBean == null) {
					continue;
				}

				if ("inputEditor".equals(cellBean.getType())) {
					if (propBean.getField() != null && !"".equals(propBean.getField())) {
						dataMap.put("head_" + propBean.getField(), headDataMap.get(propBean.getField()));
					}
					if (propBean.getHidden() != null && !"".equals(propBean.getHidden())) {
						HiddenBean hiddenBean = gson.fromJson(propBean.getHidden(), HiddenBean.class);
						if (hiddenBean == null) {
							continue;
						}
						dataMap.put("head_" + hiddenBean.getField(), headDataMap.get(hiddenBean.getField()));
					}
				} else 	if ("checkEditor".equals(cellBean.getType()) ||
						"dateEditor".equals(cellBean.getType()) ||
						"selectEditor".equals(cellBean.getType()) ||
						"mutilInputEditor".equals(cellBean.getType())) {
					if (propBean.getField() != null && !"".equals(propBean.getField())) {
						dataMap.put("head_" + propBean.getField(), headDataMap.get(propBean.getField()));
					}
				} else if ("openEditor".equals(cellBean.getType())) {
					if (propBean.getNoField() != null && !"".equals(propBean.getNoField())) {
						dataMap.put("head_" + propBean.getNoField(), headDataMap.get(propBean.getNoField()));
					}
					if (propBean.getNameField() != null && !"".equals(propBean.getNameField())) {
						dataMap.put("head_" + propBean.getNameField(), headDataMap.get(propBean.getNameField()));
					}
				}
			}
			//创建明细参数
			if (detailBeanList.size() > 0) {
				//存在明细数据
				Iterator<Entry<String, Object>> itData = data.entrySet().iterator();
				while (itData.hasNext()) {
					Entry<String, Object> entry = itData.next();
					if ("headData".equals(entry.getKey()) || "id".equals(entry.getKey()) || "attachment".equals(entry.getKey())) {
						continue;
					}
					//取得每个数据表的数据
					Map<String, Object> detailDataMap = (Map<String, Object>)entry.getValue();
					if (detailDataMap == null || detailDataMap.size() <= 0) {
						continue;
					}
					Iterator<Entry<String, Object>> itDetailData = detailDataMap.entrySet().iterator();
					while (itDetailData.hasNext()) {
						Entry<String, Object> entryDetailData = itDetailData.next();
						String target = null;
						DetailBean detailBean = null;
						for (int i = 0; i < detailBeanList.size(); i++) {
							DetailBean detailBean1 = detailBeanList.get(i);
							if (entryDetailData.getKey().equals(detailBean1.getTable())) {
								if (detailBean1.getProcess() != null) {
									target = detailBean1.getProcess().getTarget();
									detailBean = detailBean1;
									break;
								}
							}
						}
						if (target == null || "".equals(target)) {
							throw new Exception("画面设定不正确！");
						}
						List<Map<String, Object>> dataList = (List<Map<String, Object>>)entryDetailData.getValue();
						for (int i = 0; i < dataList.size(); i++) {
							//生产SQL
							Map<String, Object> detailData = dataList.get(i);
							Iterator<Entry<String, Object>> itDetail = detailData.entrySet().iterator();
							ProcessBean processBean = detailBean.getProcess();
							List<FieldBean> fieldList = processBean.getFields();
							for (int j = 0; j < fieldList.size(); j++) {
								FieldBean fieldBean = fieldList.get(j);
								PropertyBean propBean = fieldBean.getProps();
								if (propBean == null) {
									continue;
								}
								if ("inputEditor".equals(fieldBean.getEditor())) {
									if (propBean.getField() != null && !"".equals(propBean.getField())) {
										String tempData = detailData.get(propBean.getField()) == null ? "" : String.valueOf(detailData.get(propBean.getField()));
										Object obj = dataMap.get(detailBean.getTable() + "_" + propBean.getField());
										if (obj == null) {
											dataMap.put(detailBean.getTable() + "_" + propBean.getField(), tempData);
										} else {
											String dataDetail = new StringBuffer("")
																.append(obj)
																.append(",")
																.append(tempData).toString();
											dataMap.put(detailBean.getTable() + "_" + propBean.getField(), dataDetail);
										}
									}
									if (propBean.getHidden() != null && !"".equals(propBean.getHidden())) {
										HiddenBean hiddenBean = gson.fromJson(propBean.getHidden(), HiddenBean.class);
										if (hiddenBean == null) {
											continue;
										}
										String tempData = detailData.get(hiddenBean.getField()) == null ? "" : String.valueOf(detailData.get(hiddenBean.getField()));
										Object obj = dataMap.get(detailBean.getTable() + "_" + hiddenBean.getField());
										if (obj == null) {
											dataMap.put(detailBean.getTable() + "_" + hiddenBean.getField(), tempData);
										} else {
											String dataDetail = new StringBuffer("")
																.append(obj)
																.append(",")
																.append(tempData).toString();
											dataMap.put(detailBean.getTable() + "_" + hiddenBean.getField(), dataDetail);
										}
									}
								} else 	if ("checkEditor".equals(fieldBean.getEditor()) ||
										"dateEditor".equals(fieldBean.getEditor()) ||
										"selectEditor".equals(fieldBean.getEditor()) ||
										"mutilInputEditor".equals(fieldBean.getEditor())) {
									String tempData = detailData.get(propBean.getField()) == null ? "" : String.valueOf(detailData.get(propBean.getField()));
									Object obj = dataMap.get(detailBean.getTable() + "_" + propBean.getField());
									if (obj == null) {
										dataMap.put(detailBean.getTable() + "_" + propBean.getField(), tempData);
									} else {
										String dataDetail = new StringBuffer("")
															.append(obj)
															.append(",")
															.append(tempData).toString();
										dataMap.put(detailBean.getTable() + "_" + propBean.getField(), dataDetail);
									}
								} else if ("openEditor".equals(fieldBean.getEditor())) {
									if (propBean.getNoField() != null && !"".equals(propBean.getNoField())) {
										String tempData = detailData.get(propBean.getNoField()) == null ? "" : String.valueOf(detailData.get(propBean.getNoField()));
										Object obj = dataMap.get(detailBean.getTable() + "_" + propBean.getNoField());
										if (obj == null) {
											dataMap.put(detailBean.getTable() + "_" + propBean.getNoField(), tempData);
										} else {
											String dataDetail = new StringBuffer("")
																.append(obj)
																.append(",")
																.append(tempData).toString();
											dataMap.put(detailBean.getTable() + "_" + propBean.getNoField(), dataDetail);
										}
									}
									if (propBean.getNameField() != null && !"".equals(propBean.getNameField())) {
										String tempData = detailData.get(propBean.getNameField()) == null ? "" : String.valueOf(detailData.get(propBean.getNameField()));
										Object obj = dataMap.get(detailBean.getTable() + "_" + propBean.getNameField());
										if (obj == null) {
											dataMap.put(detailBean.getTable() + "_" + propBean.getNameField(), tempData);
										} else {
											String dataDetail = new StringBuffer("")
																.append(obj)
																.append(",")
																.append(tempData).toString();
											dataMap.put(detailBean.getTable() + "_" + propBean.getNameField(), dataDetail);
										}
									}
								}
							}
						}
					}
				}
			}
			//执行存储过程
			Map<Integer, Object> result = new HashMap<Integer, Object>();
			List<ButtonProcBean> procList = buttonBean.getProc();
			for (int i = 0; i < procList.size(); i++) {
				ButtonProcBean procBean = procList.get(i);
				String proc = procBean.getProc();
				if (proc == null) {
					continue;
				}
				proc = URLDecoder.decode(proc, "UTF-8");
				if ("select".equals(procBean.getProcess())) {
					//查询
					if ("R".equals(procBean.getTarget())) {
						result.put(i, businessSqlDao.select(proc, dataMap));
					} else {
						result.put(i, sqlDao.select(proc, dataMap));
					}
				} else {					
					//更新
					if ("R".equals(procBean.getTarget())) {
						businessSqlDao.update(proc, dataMap);
					} else {
						sqlDao.update(proc, dataMap);
					}
				}
			}
			return result;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			} else {
				throw e;
			}
		}
	}
}
