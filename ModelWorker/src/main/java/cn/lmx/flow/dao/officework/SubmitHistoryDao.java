package cn.lmx.flow.dao.officework;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.officework.SubmitHistory;
/**
 * 上报履历
 * @author yujx
 *
 */
@Repository("SubmitHistoryDao")
public class SubmitHistoryDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from SubmitHistory");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SubmitHistory getById(String id) throws Exception {
		return (SubmitHistory)getSession().get(SubmitHistory.class, id);
	}
	/**
	 * 新增数据
	 * @param data
	 * @throws Exception
	 */
	public void add(SubmitHistory data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param data
	 * @throws Exception
	 */
	public void edit(SubmitHistory data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param data
	 * @throws Exception
	 */
	public void delete(SubmitHistory data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据公文ID取得数据
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public List<?> listByDocId(String docId) throws Exception {
		Query q = getSession().createQuery("from SubmitHistory where parentId=:docId and status<>'V' order by submitDate desc");
		q.setParameter("docId", docId);
		return q.list();
	}
}
