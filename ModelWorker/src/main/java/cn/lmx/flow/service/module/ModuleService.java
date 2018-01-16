package cn.lmx.flow.service.module;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.module.MenuBean;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.entity.module.Module;

@Repository("ModuleService")
public class ModuleService {
	//模块
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	/**
	 * 取得功能模块一栏
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<MenuBean> listModule() throws Exception {
		List<?> list = moduleDao.list();
		List<MenuBean> resultList = new ArrayList<MenuBean>();
		if (list == null || list.size() <= 0) {
			return resultList;
		}
		for (int i = 0; i < list.size(); i++) {
			Module module = (Module)list.get(i);
			MenuBean bean = new MenuBean();
			BeanUtils.copyProperties(module, bean);
			resultList.add(bean);
		}
		return resultList;
	}
}
