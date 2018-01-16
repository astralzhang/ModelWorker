package cn.lmx.flow.dao.flow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.message.AuditMessageBean;
import cn.lmx.flow.entity.flow.Audits;

@Repository("AuditDao")
public class AuditDao extends BaseDao {
	/**
	 * 新增数据
	 * @param audit
	 * @throws Exception
	 */
	public void add(Audits audit) throws Exception {
		getSession().save(audit);
	}
	/**
	 * 根据用户和单据类型取得需审批一栏
	 * @param userNo
	 * @param voucherType
	 * @return
	 * @throws Exception
	 */
	public List<?> getAuditByUserNo(String userNo, String voucherType) throws Exception {
		StringBuffer sql = new StringBuffer("");
		//一般审核用户
		sql.append("select\n")
			.append("	A.ID as id,\n")
			.append("	A.VoucherType as voucherType,\n")
			.append("	A.VoucherTypeName as voucherTypeName,\n")
			.append("	A.BeforeContent as beforeContent,\n")
			.append("	B.DataKey as dataKey,\n")
			.append("	B.AuditType as auditType,\n")
			.append("	A.InstanceId as instanceId,\n")
			.append("	A.AuditProp as auditProp\n")
			.append("from\n")
			.append("	Audits as A\n")
			.append("	inner join\n")
			.append("		FlowInstance as B\n")
			.append("	on\n")
			.append("		A.InstanceId = B.ID and\n")
			.append("		A.Level = B.CurrentLevel\n")
			.append("where\n")
			.append("	A.AuditProp = '0' and\n")
			.append("	B.Status = 'N' and\n")
			.append("	A.Status = '0' and\n")
			.append("	A.UserNo = :userNo\n");
		if (voucherType != null && !"".equals(voucherType)) {
			sql.append(" and A.VoucherType = :voucherType\n");
		}
		Query q = getSession().createSQLQuery(sql.toString());
		q.setParameter("userNo", userNo);
		if (voucherType != null && !"".equals(voucherType)) {
			q.setParameter("voucherType", voucherType);
		}
		q.setResultTransformer(Transformers.aliasToBean(AuditMessageBean.class));
		List<?> list = q.list();
		//加签审核用户
		sql = new StringBuffer("");
		sql.append("select\n")
			.append("	A.ID as id,\n")
			.append("	A.VoucherType as voucherType,\n")
			.append("	A.VoucherTypeName as voucherTypeName,\n")
			.append("	A.BeforeContent as beforeContent,\n")
			.append("	B.DataKey as dataKey,\n")
			.append("	B.AuditType as auditType,\n")
			.append("	A.InstanceId as instanceId,\n")
			.append("	A.AuditProp as auditProp\n")
			.append("from\n")
			.append("	Audits as A\n")
			.append("	inner join\n")
			.append("		FlowInstance as B\n")
			.append("	on\n")
			.append("		A.InstanceId = B.ID and\n")
			.append("		A.Level <= B.CurrentLevel\n")
			.append("where\n")
			.append("	A.AuditProp = '1' and\n")
			.append("	B.Status = 'N' and\n")
			.append("	A.Status = '0' and\n")
			.append("	A.UserNo = :userNo\n");
		if (voucherType != null && !"".equals(voucherType)) {
			sql.append(" and A.VoucherType = :voucherType\n");
		}
		Query q2 = getSession().createSQLQuery(sql.toString());
		q2.setParameter("userNo", userNo);
		if (voucherType != null && !"".equals(voucherType)) {
			q2.setParameter("voucherType", voucherType);
		}
		q2.setResultTransformer(Transformers.aliasToBean(AuditMessageBean.class));
		List<?> list2 = q2.list();
		List<AuditMessageBean> resultList = new ArrayList<AuditMessageBean>();
		if (list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				resultList.add((AuditMessageBean)list.get(i));
			}
		}
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i <list2.size(); i++) {
				resultList.add((AuditMessageBean)list2.get(i));
			}
		}
		return resultList;
	}
	/**
	 * 根据用户和单据类型取得已审批一栏
	 * @param userNo
	 * @param voucherType
	 * @return
	 * @throws Exception
	 */
	public List<?> getAuditedByUserNo(String userNo, String voucherType, String status) throws Exception {
		StringBuffer sql = new StringBuffer("")
							.append("select\n")
							.append("	A.ID as id,\n")
							.append("	A.VoucherType as voucherType,\n")
							.append("	A.VoucherTypeName as voucherTypeName,\n")
							.append("	A.BeforeContent as beforContent,\n")
							.append("	A.Content as content,\n")
							.append("	B.DataKey as dataKey\n")
							.append("from\n")
							.append("	Audits as A\n")
							.append("	left join\n")
							.append("		FlowInstance as B\n")
							.append("	on\n")
							.append("		A.InstanceId = B.ID\n")
							.append("where\n")
							.append("	A.RealUserNo=:userNo and\n")
							.append("	A.Status=:status");
		if (voucherType != null && !"".equals(voucherType)) {
			sql.append(" and\n")
				.append("	A.VoucherType=:voucherType");
		}
		Query q = getSession().createSQLQuery(sql.toString());
		q.setParameter("userNo", userNo);
		if (voucherType != null && !"".equals(voucherType)) {
			q.setParameter("voucherType", voucherType);
		}
		q.setParameter("status", status);
		q.setResultTransformer(Transformers.aliasToBean(AuditMessageBean.class));
		return q.list();
	}
	/**
	 * 通过ID取得审核人信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Audits getAuditById(String id) throws Exception {
		return (Audits)getSession().get(Audits.class, id);
	}
	/**
	 * 修改审核人信息
	 * @param audits
	 * @throws Exception
	 */
	public void edit(Audits audits) throws Exception {
		getSession().saveOrUpdate(audits);
	}
	/**
	 * 根据阶层和实例更新处理状态
	 * @param instanceId
	 * @param level
	 * @throws Exception
	 */
	public int editAuditStatusByInstanceLevel(String instanceId, BigDecimal level, String status, String systemUser, String currentTime) throws Exception {
		Query q = getSession().createQuery("update Audits set status=:status, updateUser=:systemUser, updateTime=:currentTime where instanceId=:instanceId and level=:level");
		q.setParameter("status", status);
		q.setParameter("instanceId", instanceId);
		q.setParameter("level", level);
		q.setParameter("systemUser", systemUser);
		q.setParameter("currentTime", currentTime);
		return q.executeUpdate();
	}
	/**
	 * 根据阶层和实例更新前一处理人处理内容
	 * @param instanceId
	 * @param level
	 * @throws Exception
	 */
	public int editAuditContentByInstanceLevel(String instanceId, BigDecimal level, String processContent, String status, String systemUser, String currentTime) throws Exception {
		Query q = getSession().createQuery("update Audits set beforeContent=:processContent, status=:status, updateUser=:systemUser, updateTime=:currentTime where instanceId=:instanceId and level=:level");
		q.setParameter("processContent", processContent);
		q.setParameter("status", status);
		q.setParameter("instanceId", instanceId);
		q.setParameter("level", level);
		q.setParameter("systemUser", systemUser);
		q.setParameter("currentTime", currentTime);
		return q.executeUpdate();
	}
	/**
	 * 根据阶层和实例取得上一层非加签处理人一栏
	 * @param intanceId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public List<?> getPreNormalAuditByInstanceLevel(String intanceId, BigDecimal level) throws Exception {
		Query q = getSession().createQuery("from Audits where instanceId=:instanceId and level<=:level and auditProp='0' order by level desc");
		q.setParameter("instanceId", intanceId);
		q.setParameter("level", level);
		return q.list();
	}
	/**
	 * 取得下个非加签的处理人
	 * @param instanceId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public List<?> getNextNormalByLevel(String instanceId, BigDecimal level) throws Exception {
		Query q = getSession().createQuery("from Audits where instanceId=:instanceId and level>:level and auditProp = '0' order by level");
		q.setParameter("instanceId", instanceId);
		q.setParameter("level", level);
		return q.list();
	}
	/**
	 * 取消指定阶层之前的所有未审核的加签数据
	 * @param instanceId
	 * @param level
	 * @param systemUser
	 * @param currentTime
	 * @throws Exception
	 */
	public void cancelUnauditAddSignByLevel(String instanceId, BigDecimal level, String systemUser, String currentTime) throws Exception {
		Query q = getSession().createQuery("update Audits set status=:status, updateUser=:systemUser, updateTime=:currentTime where status='0' and auditProp='1' and instanceId=:instanceId and level<:level");
		q.setParameter("status", "4");
		q.setParameter("systemUser", systemUser);
		q.setParameter("currentTime", currentTime);
		q.setParameter("instanceId", instanceId);
		q.setParameter("level", level);
		q.executeUpdate();
	}
	/**
	 * 指定Level之后的审核数据的阶层加1
	 * @param instanceId
	 * @param level
	 * @param systemUser
	 * @param currentTime
	 * @throws Exception
	 */
	public void addOneToLevel(String instanceId, BigDecimal level, String systemUser, String currentTime) throws Exception {
		Query q = getSession().createQuery("update Audits set level = level + 1, updateUser=:systemUser, updateTime=:currentTime where level >= :level and (status='0' or status='1')");
		q.setParameter("systemUser", systemUser);
		q.setParameter("currentTime", currentTime);
		q.setParameter("level", level);
		q.executeUpdate();
	}
	/**
	 * 根据指定的Level范围更新上已审核人处理意见
	 * @param instanceId
	 * @param startLevel
	 * @param endLevel
	 * @param content
	 * @param systemUser
	 * @param currentTime
	 * @return
	 * @throws Exception
	 */
	public int editAddSignContentByLevel(String instanceId, BigDecimal startLevel, BigDecimal endLevel, String content, String systemUser, String currentTime) throws Exception {
		Query q = getSession().createQuery("update Audits set beforeContent=:content,updateUser=:systemUser,updateTime=:currentTime where instanceId=:instanceId and auditProp='1' and status='0' and level>:startLevel and level<:endLevel");
		q.setParameter("content", content);
		q.setParameter("systemUser", systemUser);
		q.setParameter("currentTime", currentTime);
		q.setParameter("instanceId", instanceId);
		q.setParameter("startLevel", startLevel);
		q.setParameter("endLevel", endLevel);
		return q.executeUpdate();
	}
	/**
	 * 根据实例和阶层取得需审批一栏
	 * @param instanceId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public List<?> getAuditListByInstance(String instanceId, BigDecimal level) throws Exception {
		Query q = null;
		if (level != null) {
			q = getSession().createQuery("from Audits where instanceId=:instanceId and level>=:level and status='0' order by level");
			q.setParameter("instanceId", instanceId);
			q.setParameter("level", level);
		} else {
			q = getSession().createQuery("from Audits where instanceId=:instanceId and status='0' order by Level");
			q.setParameter("instanceId", instanceId);
		}
		return q.list();
	}
}