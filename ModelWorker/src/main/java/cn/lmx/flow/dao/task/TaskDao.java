package cn.lmx.flow.dao.task;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.task.Tasks;
@Repository("TaskDao")
public class TaskDao extends BaseDao {
	/**
	 * 取得所有任务
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from Tasks");
		return q.list();
	}
	/**
	 * 根据ID取得任务设定
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Tasks getById(String id) throws Exception {
		return (Tasks)getSession().get(Tasks.class, id);
	}
}
