package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.ReportPublish;
@Repository("PublishReportDao")
public class PublishReportDao extends BaseDao {
	/**
	 * 取得报表发布版本
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ReportPublish getById(String id) throws Exception {
		return (ReportPublish)getSession().get(ReportPublish.class, id);
	}
	/**
	 * 根据编码取得发布版本
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public ReportPublish getByNo(String no) throws Exception {
		Query q = getSession().createQuery("from ReportPublish where no=:no");
		q.setParameter("no", no);
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (ReportPublish)list.get(0);
	}
	/**
	 * 新增
	 * @param report
	 * @throws Exception
	 */
	public void add(ReportPublish report) throws Exception {
		getSession().save(report);
	}
	/**
	 * 修改
	 * @param report
	 * @throws Exception
	 */
	public void edit(ReportPublish report) throws Exception {
		getSession().saveOrUpdate(report);
	}
}
