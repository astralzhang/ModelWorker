package cn.lmx.flow.dao.task;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.task.TaskProcessor;
@Repository("TaskProcessorDao")
public class TaskProcessorDao extends BaseDao {
	/**
	 * 取得所有的执行任务
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from TaskProcessor order by processTime");
		return q.list();
	}
	/**
	 * 根据ID取得任务
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskProcessor getById(String id) throws Exception {
		return (TaskProcessor)getSession().get(TaskProcessor.class, id);
	}
	/**
	 * 删除对象
	 * @param taskProcessor
	 * @throws Exception
	 */
	public void delete(TaskProcessor taskProcessor) throws Exception {
		getSession().delete(taskProcessor);
	}
	/**
	 * 新增对象
	 * @param taskProcessor
	 * @throws Exception
	 */
	public void add(TaskProcessor taskProcessor) throws Exception {
		getSession().save(taskProcessor);
	}
	/**
	 * 根据taskID和执行时间取得数据
	 * @param taskId
	 * @param processTime
	 * @throws Exception
	 */
	public List<?> getByTaskTime(String taskId, String processTime) throws Exception {
		Query q = getSession().createQuery("from TaskProcessor where taskId=:taskId and processTime=:processTime");
		q.setParameter("taskId", taskId);
		q.setParameter("processTime", processTime);
		return q.list();
	}
}
