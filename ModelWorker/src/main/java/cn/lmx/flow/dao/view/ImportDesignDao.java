package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.view.ImportDesignBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.ImportDesign;
@Repository("ImportDesignDao")
public class ImportDesignDao extends BaseDao {
	/**
	 * 一栏取得
	 * @param no
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<?> list(String no, String status) throws Exception {
		StringBuffer condition = new StringBuffer("");
		if (no != null && !"".equals(no)) {
			condition.append("No=:no");
		}
		if (status != null && !"".equals(status)) {
			if (condition.length() > 0) {
				condition.append(" and\n");
			}
			condition.append("Status=:status\n");
		}
		StringBuffer sql = new StringBuffer("")
				.append("select\n")
				.append("	A.ID as id,\n")
				.append("	A.No as no,\n")
				.append("	A.Name as name,\n")
				.append("	A.Model as model,\n")
				.append("	A.Script as script,\n")
				.append("	A.DesignVersion as designVersion,\n")
				.append("	A.PublishVersion as publishVersion,\n")
				.append("	A.Status as status\n")
				.append("from\n")
				.append("	ImportDesign as A,\n")
				.append("	(select\n")
				.append("		No,\n")
				.append("		Max(DesignVersion) DesignVersion\n")
				.append("	from\n")
				.append("		ImportDesign\n");
		if (condition.length() > 0) {
			sql.append("	where\n")
				.append("		").append(condition);
		}
		sql.append("	group by\n")
				.append("		No\n")
				.append("	) as B\n")
				.append("where\n")
				.append("	A.No = B.No and\n")
				.append("	A.DesignVersion = B.DesignVersion\n");
		Query q = getSession().createSQLQuery(sql.toString());
		if (no != null && !"".equals(no)) {
			q.setParameter("no", no);
		}
		if (status != null && !"".equals(status)) {
			q.setParameter("status", status);
		}
		q.setResultTransformer(Transformers.aliasToBean(ImportDesignBean.class));
		return q.list();
	}
	/**
	 * 取得最大版本
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public ImportDesign getMaxVersion(String no) throws Exception {
		if (no == null || "".equals(no)) {
			throw new Exception("没有指定导入编号！");
		}
		StringBuffer sql = new StringBuffer("")
				.append("select\n")
				.append("	No as no,\n")
				.append("	Max(DesignVersion) designVersion\n")
				.append("from\n")
				.append("	ImportDesign\n")
				.append("where\n")
				.append("	No=:no\n")
				.append("group by\n")
				.append("	No\n");
		Query q = getSession().createSQLQuery(sql.toString());
		q.setParameter("no", no);
		q.setResultTransformer(Transformers.aliasToBean(ImportDesign.class));
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (ImportDesign)list.get(0);
	}
	/**
	 * 新增
	 * @param design
	 * @throws Exception
	 */
	public void add(ImportDesign design) throws Exception {
		try {
			getSession().save(design);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发布
	 * @param systemUser
	 * @param currentTime
	 * @param id
	 * @param version
	 * @throws Exception
	 */
	public void publish(String systemUser, String currentTime, String id, String version) throws Exception {
		Query q = getSession().createQuery("update ImportDesign set publishVersion=:version, status='1',updateUser=:systemUser,updateTime=:currentTime where id=:id");
		q.setParameter("version", version);
		q.setParameter("systemUser", systemUser);
		q.setParameter("currentTime", currentTime);
		q.setParameter("id", id);
		q.executeUpdate();
	}
}
