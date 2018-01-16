package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.OpenWindows;
@Repository("OpenWindowDao")
public class OpenWindowDao extends BaseDao {
	/**
	 * 取得一栏
	 * @param no
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<?> list(String no, String name) throws Exception {
		StringBuffer sql = null;
		StringBuffer where = new StringBuffer("");
		if (no != null && !"".equals(no)) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append("no=:no");
		}
		if (name != null && !"".equals(name)) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append("name like '%")
				.append(name)
				.append("%'");
		}
		if (where.length() > 0) {
			sql = new StringBuffer("")
					.append("from OpenWindows")
					.append(" where ")
					.append(where.toString());
		} else {
			sql = new StringBuffer("")
					.append("from OpenWindows");
		}
		Query q = getSession().createQuery(sql.toString());
		if (no != null && !"".equals(no)) {
			q.setParameter("no", no);
		}
		return q.list();
	}
	/**
	 * 新增弹窗
	 * @param openWindow
	 * @throws Exception
	 */
	public void add(OpenWindows openWindow) throws Exception {
		getSession().save(openWindow);
	}
	/**
	 * 修改弹窗
	 * @param openWindos
	 * @throws Exception
	 */
	public void edit(OpenWindows openWindos) throws Exception {
		getSession().saveOrUpdate(openWindos);
	}
	/**
	 * 根据编码取得弹窗
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public OpenWindows getByNo(String no) throws Exception {
		return (OpenWindows)getSession().get(OpenWindows.class, no);
	}
}
