package cn.lmx.flow.dao.business;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BaseBusinessDao {
	@Resource(name="sessionFactoryForBusiness")
	private SessionFactory sessionFactory;
	//private Session session;
	private final static ThreadLocal<Session> session;
	static {
		session = new ThreadLocal<Session>();
	}
	/**
	 * 构造函数
	 */
	public BaseBusinessDao() {

	}
	/**
	 * 取得Session
	 * @return
	 */
	public Session getSession() {
		Session session1 = (Session)session.get();
		if (session1 != null) {
			return session1;
		} else {
			session1 = sessionFactory.openSession();
			session.set(session1);
		}
		return session1;
	}
	/**
	 * 关闭Session
	 */
	public void closeSession() {
		if (session != null) {
			Session session1 = session.get();
			if (session1 != null) {
				if (session1.isOpen()) {
					session1.close();
				}
			}
			session.remove();
		}
	}
}
