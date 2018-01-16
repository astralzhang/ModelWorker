package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
@Repository("VoucherTableDao")
public class VoucherTableDao extends BaseDao {
	/**
	 * 根据单据类型编码取得对应表一栏
	 * @param typeNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getVoucherTableList(String typeNo) throws Exception {
		try {
			Query q = getSession().createQuery("from VoucherTables where typeNo=:typeNo");
			q.setParameter("typeNo", typeNo);
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
}
