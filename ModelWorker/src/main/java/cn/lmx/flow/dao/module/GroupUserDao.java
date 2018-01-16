package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.GroupUser;

@Repository("GroupUserDao")
public class GroupUserDao extends BaseDao {
	/**
	 * 根据UserNo取得组
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getGroupByUserNo(String userNo) throws Exception {
		try {
			Query q = getSession().createQuery("from GroupUser where userNo=:userNo");
			q.setParameter("userNo", userNo);
			return q.list();
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 根据组编码取得用户一栏
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getUserByGroupNo(String groupNo) throws Exception {
		try {
			Query q = getSession().createQuery("from GroupUser where groupNo=:groupNo");
			q.setParameter("groupNo", groupNo);
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 根据用户编码删除用户工作组
	 * @param userNo
	 * @throws Exception
	 */
	public void deleteByUserNo(String userNo) throws Exception {
		Query q = getSession().createQuery("delete from GroupUser where userNo=:userNo");
		q.setParameter("userNo", userNo);
		q.executeUpdate();
	}
	/**
	 * 根据组编码删除用户
	 * @param groupNo
	 * @throws Exception
	 */
	public void deleteByGroupNo(String groupNo) throws Exception {
		Query q = getSession().createQuery("delete from GroupUser where groupNo=:groupNo");
		q.setParameter("groupNo", groupNo);
		q.executeUpdate();
	}
	/**
	 * 新增用户工作组
	 * @param groupUser
	 * @throws Exception
	 */
	public void add(GroupUser groupUser) throws Exception {
		getSession().save(groupUser);
	}
	/**
	 * 删除工作组用户
	 * @param groupUser
	 * @throws Exception
	 */
	public void delete(GroupUser groupUser) throws Exception {
		getSession().delete(groupUser);
	}
}
