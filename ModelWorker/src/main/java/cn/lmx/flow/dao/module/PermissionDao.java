package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.module.UserPermissionBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.Permission;

@Repository("PermissionDao")
public class PermissionDao extends BaseDao {
	/**
	 * 根据用户编码取得该用户权限
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getPermissionByUserNo(String userNo, String type) throws Exception {
		try {
			StringBuffer sql = new StringBuffer("")
					.append("select\n");
			if ("U".equals(type)) {
				sql.append("	A.UserNo userNo,\n");
			} else {
				sql.append("	A.UserNo groupNo,\n")
					.append("	'").append(userNo).append("' userNo,\n");
			}
			sql.append("	A.ItemNo itemNo,\n")
					.append("	B.ActionUrl actionUrl\n")
					.append("from\n")
					.append("	Permission A\n")
					.append("	left join\n")
					.append("		ModuleItems B\n")
					.append("	on\n")
					.append("		A.ItemNo = B.ItemNo\n")
					.append("where\n")
					.append("	A.UserNo=:userNo and\n")
					.append("	UserType=:type\n");
			Query q = getSession().createSQLQuery(sql.toString());
			q.setParameter("userNo", userNo);
			q.setParameter("type", type);
			q.setResultTransformer(Transformers.aliasToBean(UserPermissionBean.class));
			return q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 根据用户类型和用户编码取得权限
	 * @param userType
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getPermissionByUserTypeNo(String userType, String userNo) throws Exception {
		Query q = getSession().createQuery("from Permission where userType=:userType and userNo=:userNo");
		q.setParameter("userType", userType);
		q.setParameter("userNo", userNo);
		return q.list();
	}
	/**
	 * 删除指定用户的所有权限
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public int deletePermissionByUserNo(String userNo) throws Exception {
		Query q = getSession().createQuery("delete from Permission where userNo=:userNo");
		q.setParameter("userNo", userNo);
		return q.executeUpdate();
	}
	/**
	 * 删除权限
	 * @param itemNo
	 * @return
	 * @throws Exception
	 */
	public int deletePermission(String userNo, String itemNo) throws Exception {
		Query q = getSession().createQuery("delete from Permission where userNo=:userNo and itemNo=:itemNo");
		q.setParameter("userNo", userNo);
		q.setParameter("itemNo", itemNo);
		return q.executeUpdate();
	}
	/**
	 * 新增权限
	 * @param permission
	 * @throws Exception
	 */
	public void add(Permission permission) throws Exception {
		getSession().save(permission);
	}
}
