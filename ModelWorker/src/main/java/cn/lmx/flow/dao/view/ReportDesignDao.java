package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.view.ReportDesignBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.ReportDesign;
@Repository("ReportDesignDao")
public class ReportDesignDao extends BaseDao {
	/**
	 * 一栏取得
	 * @param no
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<?> list(String no, String name) throws Exception {
		StringBuffer condition = new StringBuffer("");
		if (no != null && !"".equals(no)) {
			condition.append("No=:no");
		}
		if (name != null && !"".equals(name)) {
			if (condition.length() > 0) {
				condition.append(" and\n");
			}
			condition.append("Name like '%").append(name).append("%'");
		}
		StringBuffer sql = new StringBuffer("")
				.append("select\n")
				.append("	A.ID as id,\n")
				.append("	A.No as no,\n")
				.append("	A.Name as name,\n")
				.append("	A.ModuleNo as moduleNo,\n")
				.append("	A.ViewScript as viewScript,\n")
				.append("	A.Version as version,\n")
				.append("	A.PublishVersion as publishVersion,\n")
				.append("	A.Status as status\n")
				.append("from\n")
				.append("	ReportDesign as A,\n")
				.append("	(select\n")
				.append("		No,\n")
				.append("		Max(Version) Version\n")
				.append("	from\n")
				.append("		ReportDesign\n");
		if (condition.length() > 0) {
			sql.append("	where\n")
				.append("		").append(condition);
		}
		sql.append("	group by\n")
				.append("		No\n")
				.append("	) as B\n")
				.append("where\n")
				.append("	A.No = B.No and\n")
				.append("	A.Version = B.Version\n");
		Query q = getSession().createSQLQuery(sql.toString());
		if (no != null && !"".equals(no)) {
			q.setParameter("no", no);
		}
		q.setResultTransformer(Transformers.aliasToBean(ReportDesignBean.class));
		return q.list();
	}
	/**
	 * 根据ID取得
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ReportDesign getById(String id) throws Exception {
		return (ReportDesign)getSession().get(ReportDesign.class, id);
	}
	/**
	 * 新增
	 * @param design
	 * @throws Exception
	 */
	public void add(ReportDesign design) throws Exception {
		getSession().save(design);
	}
	/**
	 * 修改
	 * @param design
	 * @throws Exception
	 */
	public void edit(ReportDesign design) throws Exception {
		getSession().saveOrUpdate(design);
	}
}
