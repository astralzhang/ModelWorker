package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.Module;
@Repository("ModuleDao")
public class ModuleDao extends BaseDao {
	/**
	 * 根据模块编号取得模块信息
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public Module getModuleByNo(String no) throws Exception {
		return (Module)getSession().get(Module.class, no);
	}
	/**
	 * 取得模块一栏
	 * @return
	 * @throws Exception
	 */
	public List<?> list() throws Exception {
		Query q = getSession().createQuery("from Modules order by showOrder");
		return q.list();
	}
}
