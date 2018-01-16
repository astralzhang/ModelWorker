package cn.lmx.flow.dao.flow;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	/**
	 * 构造函数
	 */
	public BaseDao() {

	}
	/**
	 * 取得Session
	 * @return
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}
