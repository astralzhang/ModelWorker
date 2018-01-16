package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("ProxyUserDao")
public class ProxyUserDao extends BaseDao {
	/**
	 * 根据代理用户取得被代理用户
	 * @param proxyUserNo
	 * @param currentTime
	 * @return
	 * @throws Exception
	 */
	public List<?> getUserByProxyUser(String proxyUserNo, String currentTime) throws Exception {
		Query q = getSession().createQuery("from ProxyUsers where proxyUserNo=:proxyUserNo and startDate<=:currentTime and endDate>= :currentTime");
		q.setParameter("proxyUserNo", proxyUserNo);
		q.setParameter("currentTime", currentTime);
		return q.list();
	}
}
