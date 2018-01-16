package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.ReportTemplate;
@Repository("ReportTemplateDao")
public class ReportTemplateDao extends BaseDao {
	/**
	 * 根据报表编码取得报表模板
	 * @param reportNo
	 * @return
	 * @throws Exception
	 */
	public List<?> listByReportNo(String reportNo) throws Exception {
		Query q = getSession().createQuery("from ReportTemplate where reportNo=:reportNo");
		q.setParameter("reportNo", reportNo);
		return q.list();
	}
	/**
	 * 根据ID取得报表模板
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ReportTemplate getById(String id) throws Exception {
		return (ReportTemplate)getSession().get(ReportTemplate.class, id);
	}
	/**
	 * 新增报表模板
	 * @param reportTemplate
	 * @throws Exception
	 */
	public void add(ReportTemplate reportTemplate) throws Exception {
		getSession().save(reportTemplate);
	}
	/**
	 * 修改报表模板
	 * @param reportTemplate
	 * @throws Exception
	 */
	public void edit(ReportTemplate reportTemplate) throws Exception {
		getSession().saveOrUpdate(reportTemplate);
	}
	/**
	 * 删除报表模板
	 * @param reportTemplate
	 * @throws Exception
	 */
	public void delete(ReportTemplate reportTemplate) throws Exception {
		getSession().delete(reportTemplate);
	}
}
