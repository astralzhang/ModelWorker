package cn.lmx.flow.dao.officework;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.officework.DocumentAttachment;
/**
 * 公文附件
 * @author yujx
 *
 */
@Repository("DocumentAttachmentDao")
public class DocumentAttachmentDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from DocumentAttachment order by createTime");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public DocumentAttachment getById(String id) throws Exception {
		return (DocumentAttachment)getSession().get(DocumentAttachment.class, id);
	}
	/**
	 * 新增数据
	 * @param data
	 * @throws Exception
	 */
	public void add(DocumentAttachment data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param data
	 * @throws Exception
	 */
	public void edit(DocumentAttachment data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param data
	 * @throws Exception
	 */
	public void delete(DocumentAttachment data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据公文ID取得数据
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public List<?> getByDocId(String docId) throws Exception {
		Query q = getSession().createQuery("from DocumentAttachment where parentId=:docId");
		q.setParameter("docId", docId);
		return q.list();
	}
	/**
	 * 根据公文ID删除附件
	 * @param id
	 */
	public void deleteByDcoId(String id) {
		Query q = getSession().createQuery("delete from DocumentAttachment where parentId=:parentId");
		q.setParameter("parentId", id);
		q.executeUpdate();
	}
}
