package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
@Repository("ImportGroupDao")
public class ImportGroupDao extends BaseDao {
	/**
	 * 根据状态取得列表
	 * @param deleted
	 * @return
	 * @throws Exception
	 */
	public List<?> list(String deleted) throws Exception {
		Query q = getSession().createQuery("from ImportGroup where deleted=:deleted");
		q.setParameter("deleted", deleted);
		return q.list();
	}
}
