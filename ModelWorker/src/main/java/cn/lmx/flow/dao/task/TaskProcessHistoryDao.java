package cn.lmx.flow.dao.task;

import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.task.TaskProcessHistory;
@Repository("TaskProcessHistoryDao")
public class TaskProcessHistoryDao extends BaseDao {
	/**
	 * 取得所有的执行任务
	 * @return
	 * @throws Exception
	 */
	public void add(TaskProcessHistory history) throws Exception {
		getSession().save(history);
	}
}
