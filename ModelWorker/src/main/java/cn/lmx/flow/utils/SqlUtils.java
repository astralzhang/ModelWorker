package cn.lmx.flow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.lmx.flow.bean.view.json.detail.DetailBean;
import cn.lmx.flow.bean.view.json.head.HeadBean;
import cn.lmx.flow.bean.view.json.head.ProcBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;

public class SqlUtils {
	/**
	 * 取得SQL的参数
	 * @param sql
	 * @return
	 */
	public static List<String> getParameter(String sql) {
		if (sql == null || "".equals(sql)) {
			return null;
		}
		int iPos = sql.indexOf(":");
		if (iPos < 0) {
			return null;
		}
		List<String> paramList = new ArrayList<String>();
		StringBuffer paramName = new StringBuffer("");
		while (iPos >= 0) {
			iPos++;
			if (iPos >= sql.length()) {
				break;
			}
			int count = SqlUtils.appearNumber(sql, "'", 0, iPos);
			if (count % 2 != 0) {
				iPos = sql.indexOf(":", iPos);
				continue;
			}
			char ch = sql.charAt(iPos);
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_') {
				paramName.append(ch);
			} else {
				paramList.add(paramName.toString());
				paramName = new StringBuffer("");
				iPos = sql.indexOf(":", iPos);
			}
		}
		if (paramName.length() > 0) {
			paramList.add(paramName.toString());
		}
		return paramList;
	}
	/**
	 * 运行按钮存储过程
	 * @param systemUser
	 * @param headBean
	 * @param data
	 * @param runTime
	 * @throws Exception
	 */
	public static void runProc(String systemUser, HeadBean headBean, Map<String, Object> data, String runTime, BusinessSQLDao businessSqlDao, SQLDao sqlDao) throws Exception {
		List<ProcBean> procList = headBean.getSaveProc();
		if (procList == null || procList.size() <= 0) {
			return;
		}
		if (runTime == null || "".equals(runTime)) {
			return;
		}
		List<ProcBean> runList = new ArrayList<ProcBean>();
		if (procList != null) {
			for (int i = 0; i < procList.size(); i++) {
				ProcBean procBean = procList.get(i);
				if (runTime.equals(procBean.getRunTime())) {
					if (procBean.getProc() == null || "".equals(procBean.getProc())) {
						continue;
					}
					if (!"HeadOnly".equals(procBean.getParamType())) {
						if (procBean.getTable() == null || "".equals(procBean.getTable())) {
							continue;
						}
					}
					runList.add(procBean);
				}
			}
		}
		if (runList.size() > 0) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, Object> headDataMap = (Map<String, Object>)data.get("headData");
			if (headDataMap != null) {
				Iterator<Entry<String, Object>> it = headDataMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					paramMap.put(headBean.getTable().toLowerCase() + "_" + entry.getKey().toLowerCase(), entry.getValue());
				}
			}
			paramMap.put("systemUser", systemUser);
			for (int i = 0; i < runList.size(); i++) {
				ProcBean procBean = runList.get(i);
				if ("HeadOnly".equals(procBean.getParamType())) {
					if ("R".equals(procBean.getTarget())) {
						businessSqlDao.update(procBean.getProc(), paramMap);
					} else {
						sqlDao.update(procBean.getProc(), paramMap);
					}
				} else if ("DetailLoop".equals(procBean.getParamType())) {
					//每条明细循环
					String[] tables = procBean.getTable().split(";");
					if (tables == null || tables.length <= 0) {
						continue;
					}
					for (int j = 0; j < tables.length; j++) {
						//取得指定表的明细数据
						Map<String, Object> tableData = (Map<String, Object>)data.get("detailData");
						if (tableData == null) {
							//没有找到明细数据
							continue;
						}
						List<Map<String, Object>> detailDataList = (List<Map<String, Object>>)tableData.get(tables[j]);
						if (detailDataList == null || detailDataList.size() <= 0) {
							continue;
						}
						for (int m = 0; m < detailDataList.size(); m++) {
							Iterator<Entry<String, Object>> it = detailDataList.get(m).entrySet().iterator();
							while (it.hasNext()) {
								Entry<String, Object> entry = it.next();
								paramMap.put(tables[j].toLowerCase() + "_" + entry.getKey().toLowerCase(), entry.getValue());
							}
							if ("R".equals(procBean.getTarget())) {
								businessSqlDao.update(procBean.getProc(), paramMap);
							} else {
								sqlDao.update(procBean.getProc(), paramMap);
							}
						}
					}
				} else if ("DetailTop".equals(procBean.getParamType())) {
					//第一条明细数据
					String[] tables = procBean.getTable().split(";");
					if (tables == null || tables.length <= 0) {
						continue;
					}
					for (int j = 0; j < tables.length; j++) {
						Map<String, Object> tableData = (Map<String, Object>)data.get("detailData");
						if (tableData == null) {
							//没有找到明细数据
							continue;
						}
						List<Map<String, Object>> detailDataList = (List<Map<String, Object>>)tableData.get(tables[j]);
						if (detailDataList == null || detailDataList.size() <= 0) {
							continue;
						}
						Iterator<Entry<String, Object>> it = detailDataList.get(0).entrySet().iterator();
						while (it.hasNext()) {
							Entry<String, Object> entry = it.next();
							paramMap.put(tables[j].toLowerCase() + "_" + entry.getKey().toLowerCase(), entry.getValue());
						}
					}
					if ("R".equals(procBean.getTarget())) {
						businessSqlDao.update(procBean.getProc(), paramMap);
					} else {
						sqlDao.update(procBean.getProc(), paramMap);
					}
				} else if ("DetailLast".equals(procBean.getParamType())) {
					//最后一条明细数据
					String[] tables = procBean.getTable().split(";");
					if (tables == null || tables.length <= 0) {
						continue;
					}
					for (int j = 0; j < tables.length; j++) {
						Map<String, Object> tableData = (Map<String, Object>)data.get("detailData");
						if (tableData == null) {
							//没有找到明细数据
							continue;
						}
						List<Map<String, Object>> detailDataList = (List<Map<String, Object>>)tableData.get(tables[j]);
						if (detailDataList == null || detailDataList.size() <= 0) {
							continue;
						}
						Iterator<Entry<String, Object>> it = detailDataList.get(detailDataList.size() - 1).entrySet().iterator();
						while (it.hasNext()) {
							Entry<String, Object> entry = it.next();
							paramMap.put(tables[j].toLowerCase() + "_" + entry.getKey().toLowerCase(), entry.getValue());
						}
					}
					if ("R".equals(procBean.getTarget())) {
						businessSqlDao.update(procBean.getProc(), paramMap);
					} else {
						sqlDao.update(procBean.getProc(), paramMap);
					}
				}
			}
		}
	}
	/**
	 * 取得数据库数据（画面设计）
	 * @param headBean
	 * @param detailBeanList
	 * @param id
	 * @param businessSqlDao
	 * @param sqlDao
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getDbData(HeadBean headBean, List<DetailBean> detailBeanList, String id, BusinessSQLDao businessSqlDao, SQLDao sqlDao) throws Exception {
		//取得数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//取得Head数据
		if (headBean.getTable() != null && !"".equals(headBean.getTable())) {
			String sql = new StringBuffer("select * from ")
						.append(headBean.getTable())
						.append(" where ID=:id").toString();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id", id);
			//执行SQL
			List<?> headDataList = null;
			if ("R".equals(headBean.getTarget())) {
				headDataList = businessSqlDao.select(sql, paramMap);
			} else {
				headDataList = sqlDao.select(sql, paramMap);
			}
			if (headDataList != null && headDataList.size() > 0) {
				dataMap.put("headData", headDataList.get(0));
			}
		}
		//取得明细数据
		Map<String, Object> detailDataMap = new HashMap<String, Object>();
		for (int j = 0; j < detailBeanList.size(); j++) {
			DetailBean detailBean = detailBeanList.get(j);
			if (detailBean.getTable() != null && !"".equals(detailBean.getTable())) {
				String sql = new StringBuffer("select * from ")
							.append(detailBean.getTable())
							.append(" where ParentId=:id").toString();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", id);
				//执行SQL
				List<?> detailDataList = null;
				if ("R".equals(detailBean.getProcess().getTarget())) {
					detailDataList = businessSqlDao.select(sql, paramMap);
				} else {
					detailDataList = sqlDao.select(sql, paramMap);
				}
				detailDataMap.put(detailBean.getTable(), detailDataList);
			}
		}
		dataMap.put("detailData", detailDataMap);
		return dataMap;
	}
	/**
	 * 取得指定字符串出现的次数
	 * @param srcText
	 * @param findText
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static int appearNumber(String srcText, String findText, int startIndex, int endIndex) {
		int count = 0;
		int index = startIndex;
		while ((index = srcText.indexOf(findText, index)) != -1) {
			index += findText.length();
			if (index > endIndex) {
				break;
			}
			count++;
		}
		return count;
	}
}
