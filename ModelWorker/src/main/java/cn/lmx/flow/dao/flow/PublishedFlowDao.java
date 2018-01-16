package cn.lmx.flow.dao.flow;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.entity.flow.PublishedFlows;

@Repository("PublishedFlowDao")
public class PublishedFlowDao extends BaseDao {
	/**
	 * 取得所有的已发布版本
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from PublishedFlows");
		return q.list();
	}
	/**
	 * 取得最新的发布版本
	 * @param voucherType
	 * @return
	 * @throws Exception
	 */
	public PublishedFlows getLastFlowByVoucherType(String voucherType) throws Exception {
		Query q = getSession().createQuery("from PublishedFlows where voucherType=:voucherType order by publishVersion desc");
		q.setParameter("voucherType", voucherType);
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (PublishedFlows)list.get(0);
	}
	/**
	 * 根据单据类型和指定版本取得已发布的流程
	 * @param voucherType
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public PublishedFlows getFlowByVoucherTypeVersion(String voucherType, String version) throws Exception {
		Query q = getSession().createQuery("from PublishedFlows where voucherType=:voucherType and publishVersion=:version");
		q.setParameter("voucherType", voucherType);
		q.setParameter("version", version);
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (PublishedFlows)list.get(0);
	}
	/**
	 * 新增发布版本
	 * @param flows
	 * @throws Exception
	 */
	public void add(PublishedFlows flows) throws Exception {
		getSession().save(flows);
	}
}
