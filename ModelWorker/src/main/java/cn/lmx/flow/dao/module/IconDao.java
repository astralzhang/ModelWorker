package cn.lmx.flow.dao.module;

import org.springframework.stereotype.Repository;

import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.module.Icons;
@Repository("IconDao")
public class IconDao extends BaseDao {
	/**
	 * 根据功能编码取得图标文件
	 * @param itemNo
	 * @return
	 * @throws Exception
	 */
	public Icons getIconByItemNo(String itemNo) throws Exception {
		return (Icons)getSession().get(Icons.class, itemNo);
	}
}
