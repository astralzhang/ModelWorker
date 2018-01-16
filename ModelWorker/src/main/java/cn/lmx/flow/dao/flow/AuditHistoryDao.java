package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.message.AuditMessageBean;
import cn.lmx.flow.entity.flow.AuditsHistory;

@Repository("AuditHistoryDao")
public class AuditHistoryDao extends BaseDao {
	/**
	 * 保存审批履历
	 * @param auditHistory
	 * @throws Exception
	 */
	public void add(AuditsHistory auditHistory) throws Exception {
		getSession().save(auditHistory);
	}
	/**
	 * 根据ID取得审批履历数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AuditsHistory getAuditsHistoryById(String id) throws Exception {
		return (AuditsHistory)getSession().get(AuditsHistory.class, id);
	}
	/**
	 * 取得已处理信息
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getAuditsHistoryByUser(String userNo, String voucherType, String status, String startDate, String endDate) throws Exception {
		StringBuffer sql = new StringBuffer("");
		sql.append("select\n")
			.append("	A.ID as id,\n")
			.append("	A.VoucherType as voucherType,\n")
			.append("	A.VoucherTypeName as voucherTypeName,\n")
			.append("	A.BeforeContent as beforeContent,\n")
			.append("	A.Content as content,\n")
			.append("	A.UserNo as userNo,\n")
			.append("	A.RealUserNo as realUserNo,\n")
			.append("	A.AuditProp as auditProp,\n")
			.append("	A.Level as level,\n")
			.append("	A.ProcessTime as processTime,\n")
			.append("	A.DataKey as dataKey,\n")
			.append("	A.AuditType as auditType,\n")
			.append("	A.FlowStatus as status,\n")
			.append("	A.InstanceId as instanceId\n")
			.append("from\n")
			.append("	V_AuditedMessage A\n")
			.append("where\n")
			.append("	A.AuditProp='0' and\n")
			.append("	A.Canceled='N' and\n")
			.append("	(A.UserNo=:userNo or A.RealUserNo=:userNo) and\n")
			.append("	left(isnull(A.ProcessTime,'19000101'), 8) >= :startDate and\n")
			.append("	left(isnull(A.ProcessTime, '19000101'), 8) <= :endDate\n");
		if (voucherType != null && !"".equals(voucherType)) {
			sql.append("	and A.VoucherType=:voucherType\n");
		}
		if (status != null && !"".equals(status)) {
			sql.append("	and A.Status=:status\n");
		}
		sql.append("order by\n")
			.append("	A.ProcessTime DESC\n");
		Query q = getSession().createSQLQuery(sql.toString());
		q.setParameter("userNo", userNo);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		if (voucherType != null && !"".equals(voucherType)) {
			q.setParameter("voucherType", voucherType);
		}
		if (status != null && !"".equals(status)) {
			q.setParameter("status", status);
		}
		q.setResultTransformer(new AliasToBeanResultTransformer(AuditMessageBean.class));
		return q.list();
	}
	/**
	 * 取得最后的审批消息
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	public AuditMessageBean getLastAuditByInstance(String instanceId) throws Exception {
		StringBuffer sql = new StringBuffer("");
		sql.append("select top 1\n")
			.append("	A.ID as id,\n")
			.append("	A.VoucherType as voucherType,\n")
			.append("	A.VoucherTypeName as voucherTypeName,\n")
			.append("	A.BeforeContent as beforeContent,\n")
			.append("	A.Content as content,\n")
			.append("	A.UserNo as userNo,\n")
			.append("	A.RealUserNo as realUserNo,\n")
			.append("	A.AuditProp as auditProp,\n")
			.append("	A.Level as level,\n")
			.append("	A.ProcessTime as processTime,\n")
			.append("	A.DataKey as dataKey\n")
			.append("from\n")
			.append("	V_AuditedMessage A\n")
			.append("where\n")
			.append("	A.AuditProp='0' and\n")
			.append("	A.Canceled = 'N' and\n")
			.append("	A.InstanceId=:instanceId\n")
			.append("order by\n")
			.append("	A.ProcessTime DESC\n");
		Query q = getSession().createSQLQuery(sql.toString());
		q.setParameter("instanceId", instanceId);		
		q.setResultTransformer(new AliasToBeanResultTransformer(AuditMessageBean.class));
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (AuditMessageBean)list.get(0);
	}
	/**
	 * 根据InstanceId取得审批列表
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	public List<?> getAuditedListByInstance(String instanceId) throws Exception {
		Query q = getSession().createQuery("from AuditsHistory where instanceId=:instanceId order by createTime");
		q.setParameter("instanceId", instanceId);
		return q.list();
	}
}
