package cn.lmx.flow.dao.module;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.Desktop;
@Repository("DesktopDao")
public class DesktopDao extends BaseDao {
	/**
	 * 新增
	 * @param desktop
	 * @throws Exception
	 */
	public void add(Desktop desktop) throws Exception {
		getSession().save(desktop);
	}
	/**
	 * 通过用户编码取得桌面数据
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public List<?> listByUserNo(String userNo) throws Exception {
		Query q = getSession().createQuery("from Desktop where userNo=:userNo order by showOrder");
		q.setParameter("userNo", userNo);
		return q.list();
	}
}
