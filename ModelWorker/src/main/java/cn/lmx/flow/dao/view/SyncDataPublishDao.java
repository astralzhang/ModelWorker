package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.SyncDataPublish;
@Repository("SyncDataPublishDao")
public class SyncDataPublishDao extends BaseDao {
	/**
	 * 取得一栏
	 * @return
	 * @throws Exception
	 */
	public List<?> list() throws Exception {
		Query q = getSession().createQuery("from SyncDataPublish order by groupNo");
		return q.list();
	}
	/**
	 * 取得一栏通过组编码
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	public List<?> listByGroup(String groupNo) throws Exception {
		Query q = getSession().createQuery("from SyncDataPublish where moduleNo=:groupNo");
		q.setParameter("groupNo", groupNo);
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public SyncDataPublish getById(String no) throws Exception {
		return (SyncDataPublish)getSession().get(SyncDataPublish.class, no);
	}
	/**
	 * 新增
	 * @param syncDataPublish
	 * @throws Exception
	 */
	public void add(SyncDataPublish syncDataPublish) throws Exception {
		getSession().save(syncDataPublish);
	}
	/**
	 * 修改
	 * @param syncDataPublish
	 * @throws Exception
	 */
	public void edit(SyncDataPublish syncDataPublish) throws Exception {
		getSession().saveOrUpdate(syncDataPublish);
	}
}
