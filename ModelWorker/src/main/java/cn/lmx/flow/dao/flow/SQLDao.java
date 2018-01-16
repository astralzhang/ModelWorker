package cn.lmx.flow.dao.flow;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.SQL.SqlProcBean;
import cn.lmx.flow.bean.flow.DatabaseBean;
import cn.lmx.flow.utils.SqlUtils;
@Repository("SQLDao")
public class SQLDao extends BaseDao {
	/**
	 * 查询SQL
	 * @param sql
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<?> select(String sql, Map<String, Object> paramMap) throws Exception {
		try {
			if (sql == null || "".equals(sql)) {
				return null;
			}
			Query q = getSession().createSQLQuery(sql);
			//取得SQL参数
			List<String> paramNameList = SqlUtils.getParameter(sql);
			if (paramNameList != null && paramNameList.size() > 0) {
				for (int i = 0; i < paramNameList.size(); i++) {
					String paramName = paramNameList.get(i);
					if (paramName != null && !"".equals(paramName)) {
						if (paramMap != null) {
							q.setParameter(paramName, paramMap.get(paramName));
						} else {
							q.setParameter(paramName, null);
						}
					}
				}
			}
			q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 新增数据
	 * @param tableId
	 * @param dataMap
	 * @throws Exception
	 */
	public int insert(String tableId, Map<String, Object> dataMap) throws Exception {
		try {
			if (tableId == null || "".equals(tableId)) {
				return -1;
			}
			if (dataMap == null || dataMap.size() <= 0) {
				return -1;
			}
			StringBuffer sbField = new StringBuffer("");
			StringBuffer sbValue = new StringBuffer("");
			Iterator<Entry<String, Object>> it = dataMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				sbField.append(entry.getKey()).append(",");
				sbValue.append(":").append(entry.getKey()).append(",");
			}
			StringBuffer sbSql = new StringBuffer("")
						.append("insert into ")
						.append(tableId)
						.append("(")
						.append(sbField.substring(0, sbField.length() - 1))
						.append(") VALUES (")
						.append(sbValue.substring(0, sbValue.length() - 1))
						.append(")");
			Query q = getSession().createSQLQuery(sbSql.toString());
			Iterator<Entry<String, Object>> it2 = dataMap.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<String, Object> entry = it2.next();
				q.setParameter(entry.getKey(), entry.getValue());
			}
			return q.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新处理
	 * @param bean
	 * @param paramMap
	 */
	public void update(DatabaseBean bean, Map<String, Object> paramMap) throws Exception {
		try {
			if (bean == null) {
				return;
			}
			List<SqlProcBean> list = bean.getList();
			if (list == null || list.size() <= 0) {
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				SqlProcBean sqlProcBean = list.get(i);
				if (!"W".equals(sqlProcBean.getTarget())) {
					continue;
				}
				String sql = sqlProcBean.getSql();
				String proc = sqlProcBean.getProc();
				if (sql != null && !"".equals(sql)) {
					//sql处理
					Query q = getSession().createSQLQuery(sql);
					if (paramMap != null && paramMap.size() > 0) {
						q.setProperties(paramMap);
					}
					q.executeUpdate();
				}
				if (proc != null && !"".equals(proc)) {
					//存储过程处理
					Query q = getSession().createSQLQuery(proc);
					if (paramMap != null && paramMap.size() > 0) {
						q.setProperties(paramMap);
					}
					try {
						q.executeUpdate();
					} catch (Exception e) {
						q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						q.list();
					}					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 更新数据
	 * @param sql
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, Map<String, Object> paramMap) throws Exception {
		Query q = getSession().createSQLQuery(sql);
		//取得SQL参数
		List<String> paramNameList = SqlUtils.getParameter(sql);
		if (paramNameList != null && paramNameList.size() > 0) {
			for (int i = 0; i < paramNameList.size(); i++) {
				String paramName = paramNameList.get(i);
				if (paramName != null && !"".equals(paramName)) {
					if (paramMap != null) {
						q.setParameter(paramName, paramMap.get(paramName));
					} else {
						q.setParameter(paramName, null);
					}
				}
			}
		}
		return q.executeUpdate();		
	}
}
