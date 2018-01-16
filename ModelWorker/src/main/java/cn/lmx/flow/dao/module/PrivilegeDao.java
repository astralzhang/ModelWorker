package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.Privilege;
@Repository("PrivilegeDao")
public class PrivilegeDao extends BaseDao {
	/**
	 * 取得所有功能列表
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from Privilege order by moduleNo, no");
		return q.list();
	}
	/**
	 * 删除指定编码的功能
	 * @param no
	 * @throws Exception
	 */
	public void deleteByNo(String no) throws Exception {
		Query q = getSession().createQuery("delete Privilege where no=:no");
		q.setParameter("no", no);
		q.executeUpdate();
	}
	/**
	 * 新增功能
	 * @param privilege
	 * @throws Exception
	 */
	public void save(Privilege privilege) throws Exception {
		getSession().save(privilege);
	}
}
