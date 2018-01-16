package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.ModuleItems;
@Repository("ModuleItemDao")
public class ModuleItemDao extends BaseDao {
	/**
	 * 取得系统菜单
	 * @return
	 * @throws Exception
	 */
	public List<?> getMenu() throws Exception {
		Query q = getSession().createQuery("from ModuleItems where menuFlag='Y' order by moduleNo, itemNo");
		return q.list();
	}
	/**
	 * 根据模块ID取得功能一栏
	 * @param moduleNo
	 * @return
	 * @throws Exception
	 */
	public List<?> getModuleItem(String moduleNo) throws Exception {
		Query q = getSession().createQuery("from ModuleItems where menuFlag = 'Y' and moduleNo=:moduleNo order by showOrder");
		q.setParameter("moduleNo", moduleNo);
		return q.list();
	}
	/**
	 * 根据ItemNo删除功能
	 * @param moduleNo
	 * @throws Exception
	 */
	public void deleteModuleItem(String itemNo) throws Exception {
		Query q = getSession().createQuery("delete from ModuleItems where itemNo=:itemNo");
		q.setParameter("itemNo", itemNo);
		q.executeUpdate();
	}
	/**
	 * 新增保存功能
	 * @param moduleItems
	 * @throws Exception
	 */
	public void add(ModuleItems moduleItems) throws Exception {
		getSession().save(moduleItems);
	}
	/**
	 * 修改菜单
	 * @param moduleItems
	 * @throws Exception
	 */
	public void edit(ModuleItems moduleItems) throws Exception {
		getSession().saveOrUpdate(moduleItems);
	}
	/**
	 * 根据ItemNo取得功能
	 * @param itemNo
	 * @return
	 * @throws Exception
	 */
	public ModuleItems getItemByNo(String itemNo) throws Exception {
		return (ModuleItems)getSession().get(ModuleItems.class, itemNo);
	}
}
