package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.entity.flow.FlowInstance;

@Repository("FlowInstanceDao")
public class FlowInstanceDao extends BaseDao {
	/**
	 * 数据新增
	 * @param flowInstance
	 * @throws Exception
	 */
	public void add(FlowInstance flowInstance) throws Exception {
		getSession().save(flowInstance);
	}
	/**
	 * 根据ID取得实例信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public FlowInstance getInstanceById(String id) throws Exception {
		return (FlowInstance)getSession().get(FlowInstance.class, id);
	}
	/**
	 * 更新数据
	 * @param flowInstance
	 * @throws Exception
	 */
	public void update(FlowInstance flowInstance) throws Exception {
		getSession().saveOrUpdate(flowInstance);
	}
	/**
	 * 根据用户取得提交数据
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getSubmitedList(String userNo, String voucherType, String startDate, String endDate, boolean hasCancel) throws Exception {
		String sql = "from FlowInstance where createUser=:userNo and substring(createTime, 1, 8)>=:startDate and substring(createTime, 1, 8)<=:endDate";
		if (voucherType != null && !"".equals(voucherType)) {
			sql += " and voucherType=:voucherType";
		}
		if (hasCancel == false) {
			sql += " and status<>'V'";
		}
		sql += " order by status,createTime desc";
		Query q = getSession().createQuery(sql);
		q.setParameter("userNo", userNo);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		if (voucherType != null && !"".equals(voucherType)) {
			q.setParameter("voucherType", voucherType);
		}
		return q.list();
	}
	/**
	 * 根据单据主键取得流程实例
	 * @param dataKey
	 * @return
	 * @throws Exception
	 */
	public List<?> getInstanceByDataKey(String dataKey) throws Exception {
		Query q = getSession().createQuery("from FlowInstance where dataKey=:dataKey and status='N'");
		q.setParameter("dataKey", dataKey);
		return q.list();
	}
}
