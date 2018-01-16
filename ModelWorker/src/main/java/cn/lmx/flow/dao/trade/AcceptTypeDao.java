package cn.lmx.flow.dao.trade;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.trade.AcceptType;
/**
 * 采用类别
 * @author yujx
 *
 */
@Repository("AcceptTypeDao")
public class AcceptTypeDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from AcceptType");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AcceptType getById(String id) throws Exception {
		return (AcceptType)getSession().get(AcceptType.class, id);
	}
	/**
	 * 新增数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void add(AcceptType data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void edit(AcceptType data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void delete(AcceptType data) throws Exception {
		getSession().delete(data);
	}
}
