package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.ImportPublish;
@Repository("ImportPublishDao")
public class ImportPublishDao extends BaseDao {
	/**
	 * 取得一栏
	 * @return
	 * @throws Exception
	 */
	public List<?> list() throws Exception {
		Query q = getSession().createQuery("from ImportPublish order by groupNo");
		return q.list();
	}
	/**
	 * 取得一栏通过组编码
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	public List<?> listByGroup(String groupNo) throws Exception {
		Query q = getSession().createQuery("from ImportPublish where groupNo=:groupNo");
		q.setParameter("groupNo", groupNo);
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public ImportPublish getById(String no) throws Exception {
		return (ImportPublish)getSession().get(ImportPublish.class, no);
	}
	/**
	 * 新增
	 * @param importPublish
	 * @throws Exception
	 */
	public void add(ImportPublish importPublish) throws Exception {
		getSession().save(importPublish);
	}
	/**
	 * 修改
	 * @param importPublish
	 * @throws Exception
	 */
	public void edit(ImportPublish importPublish) throws Exception {
		getSession().saveOrUpdate(importPublish);
	}
}
