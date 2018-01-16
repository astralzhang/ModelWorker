package cn.lmx.flow.dao.trade;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.trade.TradeUnionUserRelate;
/**
 * 可操作工会
 * @author yujx
 *
 */
@Repository("TradeUnionUserRelateDao")
public class TradeUnionUserRelateDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from TradeUnionUserRelate");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TradeUnionUserRelate getById(String id) throws Exception {
		return (TradeUnionUserRelate)getSession().get(TradeUnionUserRelate.class, id);
	}
	/**
	 * 根据人员编码取得可操作工会信息
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> listByUserNo(String userNo) throws Exception {
		Query q = getSession().createQuery("from TradeUnionUserRelate where userNo=:userNo");
		q.setParameter("userNo", userNo);
		return q.list();
	}
	/**
	 * 新增数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void add(TradeUnionUserRelate data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void edit(TradeUnionUserRelate data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void delete(TradeUnionUserRelate data) throws Exception {
		getSession().delete(data);
	}
}
