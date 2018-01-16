package cn.lmx.flow.dao.officework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.officework.DeductInfoBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.officework.DeductInfo;
/**
 * 扣分
 * @author yujx
 *
 */
@Repository("DeductInfoDao")
public class DeductInfoDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from DeductInfo");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public DeductInfo getById(String id) throws Exception {
		return (DeductInfo)getSession().get(DeductInfo.class, id);
	}
	/**
	 * 新增数据
	 * @param data
	 * @throws Exception
	 */
	public void add(DeductInfo data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param data
	 * @throws Exception
	 */
	public void edit(DeductInfo data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param data
	 * @throws Exception
	 */
	public void delete(DeductInfo data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据工会编码和年份取得数据
	 * @param code
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public List<?> listByCodeYear(String code, String year) throws Exception {
		StringBuffer sql = new StringBuffer("")
				.append("select\n")
				.append("	A.ID as id,\n")
				.append("	A.TradeCode as tradeCode,\n")
				.append("	B.Name as tradeName,\n")
				.append("	A.DeductDate as deductDate,\n")
				.append("	A.DeductPoint as deductPoint,\n")
				.append("	A.DeductYear as deductYear,\n")
				.append("	A.DeductCause as deductCause\n")
				.append("FROM\n")
				.append("	deductinfo AS A\n")
				.append("	LEFT JOIN\n")
				.append("		tradeunioninfo as B\n")
				.append("	ON\n")
				.append("		A.TradeCode = B.Code\n");
		StringBuffer condition = new StringBuffer("");
		Map<String, Object> map = new HashMap<String, Object>();
		if (code != null && !"".equals(code)) {
			if (condition.length() > 0) {
				condition.append(" and\n");
			}
			condition.append("	A.TradeCode=:tradeCode");
			map.put("tradeCode", code);
		}
		if (year != null && !"".equals(year)) {
			if (condition.length() > 0) {
				condition.append(" and\n");
			}
			condition.append("	A.DeductYear=:deductYear");
			map.put("deductYear", year);
		}
		if (condition.length() > 0) {
			sql.append("WHERE\n")
					.append(condition).append("\n")
					.append("order by\n")
					.append("	A.DeductDate desc");
		} else {
			sql.append("ORDER BY\n")
				.append("	A.DeductDate desc");		
		}
		Query q = getSession().createSQLQuery(sql.toString());
		q.setProperties(map);
		q.setResultTransformer(Transformers.aliasToBean(DeductInfoBean.class));
		return q.list();
	}
}
