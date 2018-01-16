package cn.lmx.flow.service.data;

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

import cn.lmx.flow.bean.data.SyncBean;
import cn.lmx.flow.bean.data.SyncGroupBean;
import cn.lmx.flow.bean.view.json.sync.MappingField;
import cn.lmx.flow.bean.view.json.sync.Parameter;
import cn.lmx.flow.bean.view.json.sync.Source;
import cn.lmx.flow.bean.view.json.sync.SqlProc;
import cn.lmx.flow.bean.view.json.sync.SyncSet;
import cn.lmx.flow.bean.view.json.sync.Table;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.dao.view.SyncDataPublishDao;
import cn.lmx.flow.dao.view.SyncGroupDao;
import cn.lmx.flow.entity.system.DataLogs;
import cn.lmx.flow.entity.view.SyncDataPublish;
import cn.lmx.flow.entity.view.SyncGroup;
import cn.lmx.flow.utils.ExcelUtils;

@Repository("SyncService")
public class SyncService {
	//同步设定
	@Resource(name="SyncDataPublishDao")
	private SyncDataPublishDao syncDataPublishDao;
	//同步数据组
	@Resource(name="SyncGroupDao")
	private SyncGroupDao syncGroupDao;	
	//数据Log
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	//SQLDao
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//BusinessSQLDao
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSQLDao;
	/**
	 * 取得同步一栏
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<SyncGroupBean> getList() throws Exception {
		try {
			List<SyncGroupBean> rstList = new ArrayList<SyncGroupBean>();
			//取得同步数据组
			List<?> syncGroupList = syncGroupDao.list("N");
			if (syncGroupList == null || syncGroupList.size() <= 0) {
				return rstList;
			}
			for (int i = 0; i < syncGroupList.size(); i++) {
				SyncGroup group = (SyncGroup)syncGroupList.get(i);
				if (group == null) {
					continue;
				}
				List<?> publishList = syncDataPublishDao.listByGroup(group.getNo());
				if (publishList == null || publishList.size() <= 0) {
					continue;
				}
				SyncGroupBean groupBean = new SyncGroupBean();
				BeanUtils.copyProperties(group, groupBean);
				List<SyncBean> syncList = new ArrayList<SyncBean>();
				for (int j = 0; j < publishList.size(); j++) {
					SyncDataPublish syncDataPublish = (SyncDataPublish)publishList.get(j);
					SyncBean syncBean = new SyncBean();
					BeanUtils.copyProperties(syncDataPublish, syncBean);
					syncList.add(syncBean);
				}
				groupBean.setSyncList(syncList);
				rstList.add(groupBean);
			}
			return rstList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 数据同步
	 * @param systemUser
	 * @param no
	 * @param paramMap
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void syncData(String systemUser, String no, Map<String, Object> paramMap) throws Exception {
		try {
			//取得同步设定信息
			SyncDataPublish syncDataPublish = syncDataPublishDao.getById(no);
			if (syncDataPublish == null) {
				//没有数据同步设定信息
				throw new Exception("设定信息不存在，请和系统管理员联系！");
			}
			String script = syncDataPublish.getScript();
			if (script == null || "".equals(script)) {
				throw new Exception("设定信息不存在，请和系统管理员联系！");
			}
			Gson gson = new Gson();
			SyncSet syncSet = (SyncSet)gson.fromJson(script, SyncSet.class);
			if (syncSet == null) {
				throw new Exception("设定信息不正确，请和管理员联系！");
			}
			//执行数据源表
			List<Source> sourceList = syncSet.getSource();
			if (sourceList == null || sourceList.size() <= 0) {
				throw new Exception("设定信息没有指定数据来源！");
			}
			Source masterSource = null;
			List<?> list = null;
			if (paramMap == null) {
				paramMap = new HashMap<String, Object>();
			}
			for (int i = 0; i < sourceList.size(); i++) {
				Source source = sourceList.get(i);
				if ("Y".equals(source.getMaster())) {
					if (masterSource != null) {
						throw new Exception("数据源设定错误：设定了多个主数据源！");
					} else {
						masterSource = source;
					}
					if (source.getSql() == null || "".equals(source.getSql())) {
						continue;
					}
					String sql = URLDecoder.decode(source.getSql(), "UTF-8");
					List<?> dataList = null;
					if ("R".equals(source.getTarget())) {
						dataList = businessSQLDao.select(sql, paramMap);
					} else {
						dataList = sqlDao.select(sql, paramMap);
					}
					if (dataList == null || dataList.size() <= 0) {
						continue;
					}
					if ("Y".equals(source.getMaster())) {
						list = dataList;
					} else {
						paramMap.put(source.getNo(), dataList);
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			//导入处理
			if (syncSet.getTable() == null || syncSet.getTable().size() <= 0) {
				//没有表需要同步
				throw new Exception("没有设定需要同步的表！");
			}
			Map<String, Map<String,String>> sqlMap = new HashMap<String, Map<String,String>>();
			List<Table> tableList = syncSet.getTable();
			if (tableList != null) {
				for (int i = 0; i < tableList.size() ; i++) {
					Table table = tableList.get(i);
					String insertSql = createAddSql(table);
					Map<String, String> editSqlMap = createEditSql(table);
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put("add", insertSql);
					tempMap.putAll(editSqlMap);
					sqlMap.put(table.getTable(), tempMap);
				}
			}
			if (sqlMap.size() <= 0) {
				throw new Exception("没有需要处理的数据表！");
			}
			//设定前存储过程处理
			executeProc("BefSync", syncSet, paramMap);
			if (list != null && list.size() > 0) {				
				for (int i = 0; i < list.size(); i++) {
					paramMap.put(masterSource.getNo(), list.get(i));
					//取得其他源表数据
					for (int j = 0; j < sourceList.size(); j++) {
						Source source = sourceList.get(j);
						if ("Y".equals(source.getMaster())) {
							//主数据源不执行
							continue;
						}
						if (source.getSql() == null || "".equals(source.getSql())) {
							continue;
						}
						String sql = URLDecoder.decode(source.getSql(), "UTF-8");
						//创建参数
						Iterator<Entry<String, Object>> it = paramMap.entrySet().iterator();
						Map<String, Object> paramTempMap = new HashMap<String, Object>();
						while (it.hasNext()) {
							Entry<String, Object> entry = it.next();
							Map<String, Object> tempMap = (Map<String, Object>)entry.getValue();
							if (tempMap == null || tempMap.size() <= 0) {
								continue;
							}
							Iterator<Entry<String, Object>> it2 = tempMap.entrySet().iterator();
							while (it2.hasNext()) {
								Entry<String, Object> entry2 = it2.next();
								paramTempMap.put(entry.getKey() + "_" + entry2.getKey(), entry2.getValue());
							}
						}
						List<?> assistList = null;
						if ("R".equals(source.getTarget())) {
							assistList = businessSQLDao.select(sql, paramTempMap);
						} else {
							assistList = sqlDao.select(sql, paramTempMap);
						}
						if (assistList == null || assistList.size() <= 0) {
							continue;
						}
						paramMap.put(source.getNo(), assistList.get(0));
					}
					//执行每条数据执行前的存储过程
					executeProc("BefData", syncSet, paramMap);
					//取得当前系统时间
					Calendar c = Calendar.getInstance();
					Map<String, Object> dataMap = (Map<String, Object>)list.get(i);
					Iterator<Entry<String, Map<String, String>>> it = sqlMap.entrySet().iterator();
					while (it.hasNext()) {
						Entry<String, Map<String, String>> entry = it.next();
						Table table = null;
						for (int j = 0; j < tableList.size(); j++) {
							if (entry.getKey().equals(tableList.get(j).getTable())) {
								table = tableList.get(j);
								break;
							}
						}
						if (table == null) {
							continue;
						}
						Map<String, String> tempSqlMap = entry.getValue();
						Map<String, Object> paramMap2 = createParameterMap(table, syncSet, paramMap, masterSource.getNo(), dataMap);
						paramMap2.put("systemUser", systemUser);
						paramMap2.put("systemTime", sdf.format(c.getTime()));
						//读取的Excel该行数据全部为空时，不处理
						if ("add".equals(syncDataPublish.getModel())) {
							//新增模式
							//执行checkSql
							List<?> dataList = null;
							if ("R".equals(table.getTarget())) {
								dataList = businessSQLDao.select(tempSqlMap.get("check"), paramMap2);
							} else {
								dataList = sqlDao.select(tempSqlMap.get("check"), paramMap2);
							}
							if (dataList == null || dataList.size() <= 0) {
								//执行SQL
								if ("R".equals(table.getTarget())) {
									businessSQLDao.update(tempSqlMap.get("add"), paramMap2);
								} else {
									sqlDao.update(tempSqlMap.get("add"), paramMap2);
								}
							} else {
								//写数据修改日志
								DataLogs dataLog = new DataLogs();
								dataLog.setId(UUID.randomUUID().toString());
								dataLog.setType("Sync");
								dataLog.setProcessor("新增模式，相同Key数据不处理");
								dataLog.setBeforeData(gson.toJson(dataList.get(0)));
								dataLog.setAfterData(gson.toJson(paramMap));
								dataLog.setCreateUser(systemUser);
								dataLog.setCreateTime(sdf.format(c.getTime()));
								dataLogDao.add(dataLog);
							}
						} else if ("replace".equals(syncDataPublish.getModel())) {
							//覆盖模式
							if ("R".equals(table.getTarget())) {
								List<?> dataList = businessSQLDao.select(tempSqlMap.get("check"), paramMap2);
								if (dataList == null || dataList.size() <= 0) {
									businessSQLDao.update(tempSqlMap.get("add"), paramMap2);
								} else {
									businessSQLDao.update(tempSqlMap.get("edit"), paramMap2);
									//写数据修改日志
									DataLogs dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setType("Sync");
									dataLog.setProcessor("修改");
									dataLog.setBeforeData(gson.toJson(dataList.get(0)));
									dataLog.setAfterData(gson.toJson(paramMap2));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}
							} else {
								List<?> dataList = sqlDao.select(tempSqlMap.get("check"), paramMap2);
								if (dataList == null || dataList.size() <= 0) {
									System.out.println(gson.toJson(paramMap2));
									sqlDao.update(tempSqlMap.get("add"), paramMap2);
								} else {
									sqlDao.update(tempSqlMap.get("edit"), paramMap2);
									//写数据修改日志
									DataLogs dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setType("Sync");
									dataLog.setProcessor("修改");
									dataLog.setBeforeData(gson.toJson(dataList.get(0)));
									dataLog.setAfterData(gson.toJson(paramMap));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}
							}
							//执行每条数据执行后的存储过程
							executeProc("BakData", syncSet, paramMap);
						}
					}
				}
			}
			//设定后存储过程处理
			executeProc("BakSync", syncSet, paramMap);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 执行存储过程
	 * @param list
	 * @param sheet
	 * @param runTime
	 * @throws Exception
	 */
	private void executeProc(String runTime, SyncSet syncSet, Map<String, Object> paramMap) throws Exception {
		List<SqlProc> list = syncSet.getProc();
		if (list == null || list.size() <= 0) {
			return;
		}
		if (runTime == null || "".equals(runTime)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			SqlProc sqlProc = list.get(i);
			if (runTime.equals(sqlProc.getRunTime())) {
				//执行存储过程
				if (sqlProc.getProc() == null || "".equals(sqlProc.getProc())) {
					//没有指定执行的SQL或存储过程
					continue;
				}
				String proc = URLDecoder.decode(sqlProc.getProc(), "UTF-8");
				List<Parameter> parameterList = sqlProc.getParameter();
				if (parameterList == null || parameterList.size() <= 0) {
					parameterList = new ArrayList<Parameter>();
				}
				//存在参数
				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < parameterList.size(); j++) {
					Parameter parameter = parameterList.get(j);
					parseParameter(syncSet, parameter, paramMap, map);
				}
				if ("update".equals(sqlProc.getType())) {
					if ("R".equals(sqlProc.getTarget())) {
						businessSQLDao.update(proc, map);
					} else {
						sqlDao.update(proc, map);
					}
				} else if ("select".equals(sqlProc.getType())) {
					List<?> dataList = null;
					if ("R".equals(sqlProc.getTarget())) {
						dataList = businessSQLDao.select(proc, map);
					} else {
						dataList = sqlDao.select(proc, map);
					}
					if (dataList == null || dataList.size() <= 0) {
						continue;
					}
					paramMap.put(sqlProc.getNo(), dataList.get(0));
				}
			}
		}
	}
	/**
	 * 参数解析
	 * @param importSet
	 * @param parameter
	 * @param sheet
	 * @param rowNo
	 * @param map
	 * @throws Exception
	 */
	private void parseParameter(SyncSet syncSet, Parameter parameter, Map<String, Object> paramMap, Map<String, Object> map) throws Exception {
		try {
			if (parameter.getSourceNo() == null || "".equals(parameter.getSourceNo())) {
				String sourceNo = "";
				for (int i = 0; i < syncSet.getSource().size(); i++) {
					Source source = syncSet.getSource().get(i);
					if ("Y".equals(source.getMaster())) {
						sourceNo = source.getNo();
					}
				}
				Map<String, Object> tempMap = (Map<String, Object>)paramMap.get(sourceNo);
				map.put(parameter.getTargetField(), tempMap.get(parameter.getSourceField()));
				return;
			} else {
				if (paramMap.get(parameter.getSourceNo()) instanceof Map) {
					Map<String, Object> tempMap = (Map<String, Object>)paramMap.get(parameter.getSourceNo());
					if (tempMap == null) {
						map.put(parameter.getTargetField(), null);
					} else {
						map.put(parameter.getTargetField(), tempMap.get(parameter.getSourceField()));
					}
				} else {
					map.put(parameter.getTargetField(), null);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 创建新增SQL文
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private String createAddSql(Table table) throws Exception {
		List<MappingField> mappingList = table.getMapping();
		if (mappingList == null || mappingList.size() <= 0) {
			throw new Exception("没有指定表(" + table.getTable() + ")的同步字段！");
		}
		StringBuffer sbField = new StringBuffer("");
		StringBuffer sbValue = new StringBuffer("");
		for (int i = 0; i < mappingList.size(); i++) {
			MappingField field = mappingList.get(i);
			if ("source".equals(field.getSource())) {
				//来源Excel
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				sbValue.append(":").append(field.getTargetField());
			} else if ("sqlFunc".equals(field.getSource())) {
				//数据库函数
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				String proc = URLDecoder.decode(field.getFunc(), "UTF-8");
				sbValue.append(proc);
			} else if ("sqlProc".equals(field.getSource())) {
				//SQL文或存储过程
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				sbValue.append(":").append(field.getTargetField());
			} else if ("script".equals(field.getSource())) {
				//脚本函数
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				sbValue.append(":").append(field.getTargetField());
			} else {
				throw new Exception("数据字段的数据来源设定错误！");
			}
		}
		if (sbField.length() <= 0) {
			throw new Exception("没有指定表(" + table.getTable() + ")同步的数据字段！");
		}
		String sql = new StringBuffer("")
				.append("insert into ")
				.append(table.getTable())
				.append("(")
				.append(sbField.toString())
				.append(") values(")
				.append(sbValue.toString())
				.append(")").toString();
		return sql;
	}
	/**
	 * 创建更新SQL文
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> createEditSql(Table table) throws Exception {
		List<MappingField> mappingList = table.getMapping();
		if (mappingList == null || mappingList.size() <= 0) {
			throw new Exception("没有指定表(" + table.getTable() + ")同步字段！");
		}
		StringBuffer sbField = new StringBuffer("");
		StringBuffer sbCondition = new StringBuffer("");
		for (int i = 0; i < mappingList.size(); i++) {
			MappingField field = mappingList.get(i);
			if ("source".equals(field.getSource())) {
				//来源数据源
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField())
							.append("=:").append(field.getTargetField());
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField())
						.append("=:")
						.append(field.getTargetField());
				}
			} else if ("sqlFunc".equals(field.getSource())) {
				//数据库函数
				String proc = URLDecoder.decode(field.getFunc(), "UTF-8");
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField()).append("=").append(proc);
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField()).append("=").append(proc);
				}
			} else if ("sqlProc".equals(field.getSource())) {
				//SQL文或存储过程
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField())
							.append("=:").append(field.getTargetField());
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField()).append("=:").append(field.getTargetField());
				}
			} else if ("script".equals(field.getSource())) {
				//脚本函数
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField())
							.append("=:").append(field.getTargetField());
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField()).append("=:").append(field.getTargetField());
				}
			} else {
				throw new Exception("数据字段的数据来源设定错误！");
			}
		}
		if (sbField.length() <= 0) {
			throw new Exception("没有指定表(" + table.getTable()+ ")的同步数据字段！");
		}
		if (sbCondition.length() <= 0) {
			throw new Exception("没有指定表(" + table.getTable() + ")数据重复用Check字段！");
		}
		String sql = new StringBuffer("")
				.append("update ")
				.append(table.getTable())
				.append(" set ")
				.append(sbField.toString())
				.append(" where ")
				.append(sbCondition.toString()).toString();
		String checkSql = new StringBuffer("")
				.append("select * from ")
				.append(table.getTable())
				.append(" where ")
				.append(sbCondition.toString()).toString();
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put("edit", sql);
		sqlMap.put("check", checkSql);
		return sqlMap;
	}
	/**
	 * 创建SQL参数
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> createParameterMap(Table table, SyncSet syncSet, Map<String, Object> paramMap, String no, Map<String, Object> dataMap) throws Exception {
		List<MappingField> mappingList = table.getMapping();
		if (mappingList == null || mappingList.size() <= 0) {
			throw new Exception("没有指定表(" + table.getTable() + ")的同步字段！");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < mappingList.size(); i++) {
			MappingField field = mappingList.get(i);
			if ("source".equals(field.getSource())) {
				//数据源
				if (no.equals(field.getSourceNo()) || "".equals(field.getSourceNo())) {
					//主数据源
					map.put(field.getTargetField(), dataMap.get(field.getSourceField()));
					continue;
				}
				//非主数据源
				Map<String, Object> sourceMap = (Map<String, Object>)paramMap.get(field.getSourceNo());
				map.put(field.getTargetField(), sourceMap.get(field.getSourceField()));
			} else if ("sqlFunc".equals(field.getSource())) {
				//数据库函数
				if (field.getParameter() == null || field.getParameter().size() <= 0) {
					continue;
				}
				List<Parameter> parameterList = field.getParameter();
				for (int j = 0; j < parameterList.size(); j++) {
					Parameter parameter = parameterList.get(j);
					parseParameter(syncSet, parameter, paramMap, map);
				}
			} else if ("sqlProc".equals(field.getSource())) {
				//SQL文或存储过程
				String sql = URLDecoder.decode(field.getFunc(), "UTF-8");
				Map<String, Object> paramMap2 = new HashMap<String, Object>();
				if (field.getParameter() != null && field.getParameter().size() > 0) {
					List<Parameter> parameterList = field.getParameter();
					for (int j = 0; j < parameterList.size(); j++) {
						Parameter parameter = parameterList.get(j);
						parseParameter(syncSet, parameter, paramMap, paramMap2);
					}
				}
				List<?> dataList = null;
				if ("R".equals(field.getTarget())) {
					dataList = businessSQLDao.select(sql, paramMap);
				} else {
					dataList = sqlDao.select(sql, paramMap);
				}
				Map<String, Object> dataMap2 = null;
				if (dataList != null && dataList.size() > 0) {
					dataMap2 = (Map<String, Object>)dataList.get(0);
				} else {
					dataMap2 = new HashMap<String, Object>();
				}
				Object value = dataMap2.get(field.getSourceField());
				map.put(field.getTargetField(), value);
			} else if ("script".equals(field.getSource())) {
				//脚本函数
				String func = URLDecoder.decode(field.getFunc(), "UTF-8");
				Map<String, Object> paramMap2 = new HashMap<String, Object>();
				if (field.getParameter() != null && field.getParameter().size() > 0) {
					List<Parameter> parameterList = field.getParameter();
					for (int j = 0; j < parameterList.size(); j++) {
						Parameter parameter = parameterList.get(j);
						parseParameter(syncSet, parameter, paramMap, paramMap2);
					}
				}
				Object value = ExcelUtils.runScript(func, paramMap2);
				map.put(field.getTargetField(), value);
			} else {
				throw new Exception("数据字段的数据来源设定错误！");
			}
		}
		return map;
	}
}
