package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.entity.flow.Flows;
@Repository("FlowDao")
public class FlowDao extends BaseDao {
	/**
	 * 根据条件取得流程列表
	 * @param flows
	 * @return
	 * @throws Exception
	 */
	public List<?> getFlowList(Flows flows) throws Exception {
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("select").append("\n")
				.append("	a.ID id,").append("\n")
				.append("	a.No no,").append("\n")
				.append("	a.Name name,").append("\n")
				.append("	a.VoucherType voucherType,")
				.append("	a.ViewScript viewScript,").append("\n")
				.append("	a.FlowScript flowScript,").append("\n")
				.append("	cast(a.Status as varchar(1)) status,").append("\n")
				.append("	a.Version version,").append("\n")
				.append("	cast(isnull(a.DisagreeAudit, '') as varchar(1)) disagreeAudit,").append("\n")
				.append("	isnull(a.AuditType, '') auditType,\n")
				.append("	isnull(a.AuditMessage, '[]') message,\n")
				.append("	a.CreateUser createUser,").append("\n")
				.append("	a.CreateTime createTime,").append("\n")
				.append("	a.UpdateUser updateUser,").append("\n")
				.append("	a.UpdateTime updateTime").append("\n")
				.append("from").append("\n")
				.append("	Flows a").append("\n")
				.append("	left join").append("\n")
				.append("		(select").append("\n")
				.append("			No,").append("\n")
				.append("			MAX(Version) Version").append("\n")
				.append("		from").append("\n")
				.append("			Flows").append("\n");
			if (flows.getStatus() != null && !"".equals(flows.getStatus())) {
				sql.append("		where").append("\n")
					.append("			Status=:status").append("\n");
			}
			sql.append("		group by").append("\n")
				.append("			No").append("\n")
				.append("		) b").append("\n")
				.append("	on").append("\n")
				.append("		a.No = b.No and").append("\n")
				.append("		a.Version = b.Version").append("\n")
				.append("	left join").append("\n")
				.append("		VoucherTypes c").append("\n")
				.append("	on").append("\n")
				.append("		a.VoucherType = c.No").append("\n")
				.append("where").append("\n")
				.append("	isnull(b.Version, '') <>  ''").append("\n");
			StringBuffer condition = new StringBuffer(" ");
			if (flows.getNo() != null && !"".equals(flows.getNo())) {
				if (condition.length() > 0) {
					condition.append(" and ");
				}
				condition.append("a.no=:no");
			}
			if (flows.getVoucherType() != null && !"".equals(flows.getVoucherType())) {
				if (condition.length() > 0) {
					condition.append(" and ");
				}
				condition.append("a.voucherType=:voucherType");
			}
			if (flows.getStatus() != null && !"".equals(flows.getStatus())) {
				if (condition.length() > 0) {
					condition.append(" and ");
				}
				condition.append("a.status=:status");
			}
			if (condition.length() > 0) {
				sql.append(condition.toString());
			}
			Query q = getSession().createSQLQuery(sql.toString());
			q.setProperties(flows);
			q.setResultTransformer(new AliasToBeanResultTransformer(Flows.class));
			return q.list();
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 取得指定FlowNo的最大版本
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public List<?> getMaxVersion(String no) throws Exception {
		Query q = getSession().createQuery("from Flows where no=:no order by version desc");
		q.setParameter("no", no);
		return q.list();
	}
	/**
	 * 新增工作流
	 * @param flow
	 * @throws Exception
	 */
	public void addFlow(Flows flow) throws Exception {
		try {
			getSession().save(flow);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 修改工作流
	 * @param flow
	 * @return
	 * @throws Exception
	 */
	public void editFlow(Flows flow) throws Exception {
		try {
			getSession().saveOrUpdate(flow);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 根据流程编号和版本取得流程
	 * @param no
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public Flows getFlowsByVersion(String no, String version) throws Exception {
		try {
			Query q = getSession().createQuery("from Flows where no=:no and version=:version");
			q.setParameter("no", no);
			q.setParameter("version", version);
			List<?> list = q.list();
			if (list == null || list.size() <= 0) {
				return null;
			}
			return (Flows)list.get(0);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 根据ID取得流程
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Flows getFlowsById(String id) throws Exception {
		return (Flows)getSession().get(Flows.class, id);
	}
}
