package cn.lmx.flow.dao.trade;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.trade.AcceptLevel;
/**
 * 采用级别
 * @author yujx
 *
 */
@Repository("AcceptLevelDao")
public class AcceptLevelDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from AcceptLevel order by code");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AcceptLevel getById(String id) throws Exception {
		return (AcceptLevel)getSession().get(AcceptLevel.class, id);
	}
	/**
	 * 新增数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void add(AcceptLevel acceptLevel) throws Exception {
		getSession().save(acceptLevel);
	}
	/**
	 * 修改数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void edit(AcceptLevel acceptLevel) throws Exception {
		getSession().saveOrUpdate(acceptLevel);
	}
	/**
	 * 删除数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void delete(AcceptLevel acceptLevel) throws Exception {
		getSession().delete(acceptLevel);
	}
}
