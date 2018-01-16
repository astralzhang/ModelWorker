package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
@Repository("TableFieldDao")
public class TableFieldDao extends BaseDao {
	/**
	 * 根据TableId取得对应字段一栏
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	public List<?> getTableFieldList(String tableId) throws Exception {
		try {
			Query q = getSession().createQuery("from TableFields where tableId=:tableId");
			q.setParameter("tableId", tableId);
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
}
