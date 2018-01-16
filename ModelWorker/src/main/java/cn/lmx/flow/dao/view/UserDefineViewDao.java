package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.view.UserDefineViewBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.UserDefineViews;
@Repository("UserDefineViewDao")
public class UserDefineViewDao extends BaseDao {
	/**
	 * 取得最大版本的用户自定义画面
	 * @param no
	 * @return
	 */
	public UserDefineViews getMaxVersion(String no) throws Exception {
		Query q = getSession().createQuery("from UserDefineViews where no=:no order by version desc");
		q.setParameter("no", no);
		//q.setResultTransformer(Transformers.aliasToBean(UserDefineViews.class));
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (UserDefineViews)list.get(0);
	}
	/**
	 * 取得自定义画面列表
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public List<?> getLastVersionList(String no, String status) throws Exception {
		StringBuffer sql = new StringBuffer("")
				.append("select\n")
				.append("	C.ID as id,\n")
				.append("	C.No as no,\n")
				.append("	C.Name as name,\n")
				.append("	C.Version as version,\n")
				.append("	C.VoucherType as voucherType,\n")
				.append("	D.Name as voucherTypeName,\n")
				.append("	C.Status as status,\n")
				.append("	C.ListScript as listScript,\n")
				.append("	C.HeadScript as headScript,\n")
				.append("	C.DetailScript as detailScript\n")
				.append("from\n")
				.append("	(select\n")
				.append("		A.ID,\n")
				.append("		A.No,\n")
				.append("		A.Name,\n")
				.append("		A.Version,\n")
				.append("		A.VoucherType,\n")
				.append("		A.Status,\n")
				.append("		A.ListScript,\n")
				.append("		A.HeadScript,\n")
				.append("		A.DetailScript\n")
				.append("	from\n")
				.append("		UserDefineViews as A,\n")
				.append("		(select\n")
				.append("			No,\n")
				.append("			max(Version) as Version\n")
				.append("		from\n")
				.append("			UserDefineViews\n");
		if (status != null && !"".equals(status)) {
			sql.append("		where\n")
				.append("			Status=:status\n");
		}
		sql.append("		group by No\n")
				.append("		) as B\n")
				.append("	where\n")
				.append("		A.No = B.No and\n")
				.append("		A.Version = B.Version\n");
		if (no != null && !"".equals(no)) {
			sql.append("and A.No=:no\n");
		}
		sql.append("	) as C\n")
				.append("	left join\n")
				.append("		VoucherTypes D\n")
				.append("	on\n")
				.append("		C.VoucherType = D.No\n");
		Query q = getSession().createSQLQuery(sql.toString());
		if (no != null && !"".equals(no)) {
			q.setParameter("no", no);
		}
		if (status != null && !"".equals(status)) {
			q.setParameter("status", status);
		}
		q.setResultTransformer(Transformers.aliasToBean(UserDefineViewBean.class));
		return q.list();
	}
	/**
	 * 新增
	 * @param userDefineView
	 * @throws Exception
	 */
	public void add(UserDefineViews userDefineView) throws Exception {
		getSession().save(userDefineView);
	}
	/**
	 * 版本发布
	 * @param id
	 * @param systemUser
	 * @param systemTime
	 * @throws Exception
	 */
	public void publish(String id, String version, String systemUser, String systemTime) throws Exception {
		Query q = getSession().createQuery("update UserDefineViews set status='1',publishVersion=:version,updateUser=:systemUser,updateTime=:systemTime where id=:id");
		q.setParameter("version", version);
		q.setParameter("systemUser", systemUser);
		q.setParameter("systemTime", systemTime);
		q.setParameter("id", id);
		q.executeUpdate();
	}
}
