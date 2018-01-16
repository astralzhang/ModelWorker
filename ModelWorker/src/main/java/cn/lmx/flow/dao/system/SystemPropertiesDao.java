package cn.lmx.flow.dao.system;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;

@Repository("SystemPropertiesDao")
public class SystemPropertiesDao extends BaseDao {
	/**
	 * 取得所有的系统属性
	 * @return
	 * @throws Exception
	 */
	public List<?> list() throws Exception {
		Query q = getSession().createQuery("from SystemProperties");
		return q.list();
	}
	/**
	 * 根据类型取得系统属性
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<?> getPropertiesByType(String type) throws Exception {
		Query q = getSession().createQuery("from SystemProperties where type=:type");
		q.setParameter("type", type);
		return q.list();
	}
}
