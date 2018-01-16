package cn.lmx.flow.dao.view;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.view.PublishedViews;
@Repository("PublishedViewDao")
public class PublishedViewDao extends BaseDao {
	/**
	 * 新增保存
	 * @param publishedView
	 * @throws Exception
	 */
	public void add(PublishedViews publishedView) throws Exception  {
		getSession().save(publishedView);
	}
	/**
	 * 修改
	 * @param publishedView
	 * @throws Exception
	 */
	public void edit(PublishedViews publishedView) throws Exception {
		getSession().saveOrUpdate(publishedView);
	}
	/**
	 * 根据画面编号取得已发布画面
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public PublishedViews getViewByNo(String no) throws Exception {
		Query q = getSession().createQuery("from PublishedViews where no=:no");
		q.setParameter("no", no);
		List<?> list = q.list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		return (PublishedViews)list.get(0);
	}
	/**
	 * 删除已发布版本
	 * @param no
	 * @throws Exception
	 */
	public void deleteView(String no) throws Exception {
		Query q = getSession().createQuery("delete from PublishedViews where no=:no");
		q.setParameter("no", no);
		q.executeUpdate();
	}
}
