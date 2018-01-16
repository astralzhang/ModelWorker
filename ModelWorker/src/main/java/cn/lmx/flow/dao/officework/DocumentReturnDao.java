package cn.lmx.flow.dao.officework;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.officework.DocumentReturn;
/**
 * 公文退回状况
 * @author yujx
 *
 */
@Repository("DocumentReturnDao")
public class DocumentReturnDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from DocumentReturn");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public DocumentReturn getById(String id) throws Exception {
		return (DocumentReturn)getSession().get(DocumentReturn.class, id);
	}
	/**
	 * 新增数据
	 * @param data
	 * @throws Exception
	 */
	public void add(DocumentReturn data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param data
	 * @throws Exception
	 */
	public void edit(DocumentReturn data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param data
	 * @throws Exception
	 */
	public void delete(DocumentReturn data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据公文ID取得退回状况
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public List<?> listByDocId(String docId) throws Exception {
		Query q = getSession().createQuery("from DocumentReturn where parentId=:docId order by returnDate");
		q.setParameter("docId", docId);
		return q.list();
	}
	
	/**
	 * 根据公文ID删除公文采用信息
	 * @param id
	 */
	public void deleteById(String id) {
		Query q = getSession().createQuery("delete from DocumentReturn where parentId=:parentId");
		q.setParameter("parentId", id);
		q.executeUpdate();
	}

}
