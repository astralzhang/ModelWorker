package cn.lmx.flow.dao.trade;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.officework.PointQueueBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.trade.TradeUnionInfo;
/**
 * 工会信息
 * @author yujx
 *
 */
@Repository("TradeUnionInfoDao")
public class TradeUnionInfoDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from TradeUnionInfo order by showOrder");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TradeUnionInfo getById(String id) throws Exception {
		return (TradeUnionInfo)getSession().get(TradeUnionInfo.class, id);
	}
	/**
	 * 新增数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void add(TradeUnionInfo data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void edit(TradeUnionInfo data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void delete(TradeUnionInfo data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 取得所有实体工会
	 * @return
	 * @throws Exception
	 */
	public List<?> listReal() throws Exception {
		Query q = getSession().createQuery("from TradeUnionInfo where tradeType='0' order by showOrder");
		return q.list();
	}
	/**
	 * 取得用户所属工会的下级工会信息
	 * @param systemUser
	 * @return
	 * @throws Exception
	 */
	public List<?> listTrade(String systemUser) throws Exception {
		String sql = new StringBuffer("")
				.append("SELECT\n")
				.append("	A.`Code` AS tradeUnionCode,\n")
				.append("	A.Name as tradeUnionName,\n")
				.append("	C.Code AS virtualUnionCode,\n")
				.append("	C.Name as virtualUnionName,\n")
				.append("	C.ShowVirtual as showVirtual\n")
				.append("FROM\n")
				.append("	tradeunioninfo as A\n")
				.append("	left join\n")
				.append("		tradeunionuser as B\n")
				.append("	ON\n")
				.append("		A.ParentCode = B.TradeUnionCode\n")
				.append("	LEFT JOIN\n")
				.append("		tradeunioninfo as C\n")
				.append("	ON\n")
				.append("		A.VirtualParentCode = C.`Code`\n")
				.append("WHERE\n")
				.append("	A.TradeType = '0' and\n")
				.append("	B.`Code`=:systemUser\n")
				.append("order by\n")
				.append("	A.ShowOrder\n").toString();
		Query q = getSession().createSQLQuery(sql);
		q.setParameter("systemUser", systemUser);
		q.setResultTransformer(Transformers.aliasToBean(PointQueueBean.class));
		return q.list();
	}
}
