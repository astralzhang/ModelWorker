package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.User;
@Repository("UserDao")
public class UserDao extends BaseDao {
	/**
	 * 通过用户No和密码取得用户
	 * @param userNo
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public List<?> getUserByPassword(String userNo, String password) throws Exception {
		try {
			Query q= getSession().createQuery("from User where userNo=:userNo and userPassword=:password and status='Y'");
			q.setParameter("userNo", userNo);
			q.setParameter("password", password);
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 新增用户
	 * @param user
	 * @throws Exception
	 */
	public void add(User user) throws Exception {
		try {
			getSession().save(user);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 修改用户
	 * @param user
	 * @throws Exception
	 */
	public void edit(User user) throws Exception {
		try {
			getSession().saveOrUpdate(user);
		} catch(Exception e) {
			throw e;
		}
	}
	/**
	 * 取得正常用户一栏
	 * @return
	 * @throws Exception
	 */
	public List<?> list() throws Exception {
		Query q = getSession().createQuery("from User where status<>'V' order by userNo");
		return q.list();
	}
	/**
	 * 根据用户编码取得用户
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public User getUserById(String no) throws Exception {
		return (User)getSession().get(User.class, no);
	}
	/**
	 * 根据用户编码名称取得用户一栏
	 * @param no
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<?> listByNoName(String no, String name) throws Exception {
		StringBuffer sql = new StringBuffer("").append("from User where status<>'V'");
		if (no != null && !"".equals(no)) {
			sql.append(" and userNo=:userNo");
		}
		if (name != null && !"".equals(name)) {
			sql.append(" and userName like '%").append(name).append("%'").toString();
		}
		sql.append(" order by userNo");
		Query q = getSession().createQuery(sql.toString());
		if (no != null && !"".equals(no)) {
			q.setParameter("userNo", no);
		}
		return q.list();
	}
	/**
	 * 删除系统用户
	 * @param user
	 * @throws Exception
	 */
	public void delete(User user) throws Exception {
		getSession().delete(user);
	}
}
