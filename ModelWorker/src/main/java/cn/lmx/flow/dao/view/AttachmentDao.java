package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.Attachments;
@Repository("AttachmentDao")
public class AttachmentDao extends BaseDao {
	/**
	 * 新增数据
	 * @param attachment
	 * @throws Exception
	 */
	public void add(Attachments attachment) throws Exception {
		getSession().save(attachment);
	}
	/**
	 * 修改数据
	 * @param attachment
	 * @throws Exception
	 */
	public void edit(Attachments attachment) throws Exception {
		getSession().saveOrUpdate(attachment);
	}
	/**
	 * 取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Attachments getById(String id) throws Exception {
		return (Attachments)getSession().get(Attachments.class, id);
	}
	/**
	 * 删除数据
	 * @param id
	 * @throws Exception
	 */
	public void delete(String id) throws Exception {
		Query q = getSession().createQuery("delete from Attachments where id=:id");
		q.setParameter("id", id);
		q.executeUpdate();
	}
	/**
	 * 根据单据ID删除附件
	 * @param voucherId
	 * @throws Exception
	 */
	public void deleteByVoucherId(String voucherId) throws Exception {
		Query q = getSession().createQuery("delete from Attachments where dataId=:dataId");
		q.setParameter("dataId", voucherId);
		q.executeUpdate();
	}
	/**
	 * 取得附件一栏
	 * @param viewNo
	 * @param dataId
	 * @return
	 * @throws Exception
	 */
	public List<?> list(String viewNo, String dataId) throws Exception {
		Query q = getSession().createQuery("from Attachments where viewNo=:viewNo and dataId=:dataId order by seqNo");
		q.setParameter("viewNo", viewNo);
		q.setParameter("dataId", dataId);
		return q.list();
	}
	/**
	 * 取得最大序号
	 * @param dataId
	 * @return
	 * @throws Exception
	 */
	public Attachments getMaxSeqNo(String dataId) throws Exception {
		Query q = getSession().createQuery("from Attachments where dataId=:dataId order by seqNo desc");
		q.setParameter("dataId", dataId);
		List<?> list = q.list();
		if (list != null && list.size() > 0) {
			return (Attachments)list.get(0);
		} else {
			return null;
		}
	}
}
