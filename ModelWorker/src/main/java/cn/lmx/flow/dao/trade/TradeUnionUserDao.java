package cn.lmx.flow.dao.trade;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.trade.TradeUnionUser;
/**
 * 工会操作人员
 * @author yujx
 *
 */
@Repository("TradeUnionUserDao")
public class TradeUnionUserDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from TradeUnionUser");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TradeUnionUser getById(String id) throws Exception {
		return (TradeUnionUser)getSession().get(TradeUnionUser.class, id);
	}
	/**
	 * 新增数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void add(TradeUnionUser data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void edit(TradeUnionUser data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void delete(TradeUnionUser data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据用户编码取得数据
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> listByUser(String userNo) throws Exception {
		Query q = getSession().createQuery("from TradeUnionUser where code=:code");
		q.setParameter("code", userNo);
		return q.list();
	}
}
