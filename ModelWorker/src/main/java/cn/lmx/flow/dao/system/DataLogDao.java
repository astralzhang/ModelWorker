package cn.lmx.flow.dao.system;

import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.system.DataLogs;
@Repository("DataLogDao")
public class DataLogDao extends BaseDao {
	/**
	 * 新增
	 * @param log
	 * @throws Exception
	 */
	public void add(DataLogs log) throws Exception {
		getSession().save(log);
	}
}
