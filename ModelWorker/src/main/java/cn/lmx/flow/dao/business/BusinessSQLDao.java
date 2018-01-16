package cn.lmx.flow.dao.business;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.SQL.SqlProcBean;
import cn.lmx.flow.bean.flow.DatabaseBean;
import cn.lmx.flow.utils.SqlUtils;
@Repository("BusinessSQLDao")
public class BusinessSQLDao extends BaseBusinessDao {
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
		} finally {
			closeSession();
		}
	}
	/**
	 * 更新SQL
	 * @param sql
	 * @param paramMap
	 * @return
	 * @throws Exception
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
				if (!"B".equals(sqlProcBean.getTarget())) {
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
			throw e;
		} finally {
			closeSession();
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
		try {
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
		} catch (Exception e) {
			throw e;
		} finally {
			closeSession();
		}
	}
}
