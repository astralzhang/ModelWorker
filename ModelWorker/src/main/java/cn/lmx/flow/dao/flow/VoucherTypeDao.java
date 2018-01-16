package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.entity.flow.VoucherTypes;
@Repository("VoucherTypeDao")
public class VoucherTypeDao extends BaseDao {
	/**
	 * 单据类型列表
	 * @return
	 * @throws Exception
	 */
	public List<?> getVoucherTypeList() throws Exception {
		try {
			Query q = getSession().createQuery("from VoucherTypes");
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 根据类型编码取得类型数据
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public VoucherTypes getVoucherTypeById(String no) throws Exception {
		return (VoucherTypes)getSession().get(VoucherTypes.class, no);
	}
}
