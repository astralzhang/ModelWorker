package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.Group;
@Repository("GroupDao")
public class GroupDao extends BaseDao {
	/**
	 * 取得组一栏
	 * @return
	 * @throws Exception
	 */
	public List<?> list() throws Exception {
		Query q = getSession().createQuery("from Group");
		return q.list();
	}
	/**
	 * 根据组编码和名称取得组一栏
	 * @param no
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<?> listByNoName(String no, String name) throws Exception {
		StringBuffer sql = new StringBuffer("")
				.append("from Group");
		StringBuffer condition = new StringBuffer("");
		if (no != null && !"".equals(no)) {
			if (condition.length() > 0) {
				condition.append(" and ");
			}
			condition.append("groupNo=:no");
		}
		if (name != null && !"".equals(name)) {
			if (condition.length() > 0) {
				condition.append(" and ");
			}
			condition.append("groupName like '%")
					.append(name)
					.append("%'");
		}
		if (condition.length() > 0) {
			sql.append(" where ")
				.append(condition);
		}
		Query q = getSession().createQuery(sql.toString());
		if (no != null && !"".equals(no)) {
			q.setParameter("no", no);
		}
		return q.list();
	}
	/**
	 * 根据组编码取得组信息
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	public Group getGroupByNo(String groupNo) throws Exception {
		return (Group)getSession().get(Group.class, groupNo);
	}
	/**
	 * 新增组
	 * @param group
	 * @throws Exception
	 */
	public void add(Group group) throws Exception {
		getSession().save(group);
	}
	/**
	 * 修改组
	 * @param group
	 * @throws Exception
	 */
	public void edit(Group group) throws Exception {
		getSession().saveOrUpdate(group);
	}
	/**
	 * 删除组
	 * @param group
	 * @throws Exception
	 */
	public void delete(Group group) throws Exception {
		getSession().delete(group);
	}
}
