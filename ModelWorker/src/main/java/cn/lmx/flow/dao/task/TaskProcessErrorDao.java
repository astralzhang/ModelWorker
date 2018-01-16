package cn.lmx.flow.dao.task;

import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.task.TaskProcessError;
@Repository("TaskProcessErrorDao")
public class TaskProcessErrorDao extends BaseDao {
	/**
	 * 新增对象
	 * @param error
	 * @throws Exception
	 */
	public void add(TaskProcessError error) throws Exception {
		getSession().save(error);
	}
}
