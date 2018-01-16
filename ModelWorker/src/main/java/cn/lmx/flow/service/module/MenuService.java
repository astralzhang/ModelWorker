package cn.lmx.flow.service.module;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.module.MenuBean;
import cn.lmx.flow.bean.module.ModuleItemBean;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.dao.module.ModuleItemDao;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.module.ModuleItems;

@Repository("MenuService")
public class MenuService {
	//模块
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	//功能
	@Resource(name="ModuleItemDao")
	private ModuleItemDao moduleItemDao;
	/**
	 * 取得菜单数据
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<MenuBean> getMenu() throws Exception {
		List<?> moduleList = moduleDao.list();
		if (moduleList == null) {
			return new ArrayList<MenuBean>();
		}
		List<MenuBean> menuList = new ArrayList<MenuBean>();
		for (int i = 0; i < moduleList.size(); i++) {
			Module module = (Module)moduleList.get(i);
			MenuBean menuBean = new MenuBean();
			menuBean.setNo(module.getNo());
			menuBean.setName(module.getName());
			menuBean.setShowOrder(module.getShorwOrder());
			menuBean.setCssStyle(module.getCssStyle());
			//取得功能一栏
			List<?> itemList = moduleItemDao.getModuleItem(module.getNo());
			/*if (itemList == null || itemList.size() <= 0) {
				continue;
			}*/
			if (itemList != null) {
				for (int j = 0; j < itemList.size(); j++) {
					ModuleItems item = (ModuleItems)itemList.get(j);
					ModuleItemBean itemBean = new ModuleItemBean();
					BeanUtils.copyProperties(item, itemBean);
					menuBean.addItem(itemBean);
				}
			}
			menuList.add(menuBean);
		}
		return menuList;
		/*
		List<?> itemList = moduleItemDao.getMenu();
		if (itemList == null || itemList.size() <= 0) {
			return new ArrayList<MenuBean>();
		}
		String moduleNo = "";
		List<MenuBean> menuList = new ArrayList<MenuBean>();
		for (int i = 0; i < itemList.size(); i++) {
			ModuleItems item = (ModuleItems)itemList.get(i);
			if (!moduleNo.equals(item.getModuleNo())) {
				//新的模块
				moduleNo = item.getModuleNo();
				Module module = moduleDao.getModuleByNo(moduleNo);
				if (module != null) {
					//创建Menu
					MenuBean menuBean = new MenuBean();
					menuBean.setNo(module.getNo());
					menuBean.setName(module.getName());
					menuBean.setShowOrder(module.getShorwOrder());
					ModuleItemBean bean = new ModuleItemBean();
					BeanUtils.copyProperties(item, bean);
					menuBean.addItem(bean);
					menuList.add(menuBean);
				}
			} else {
				//相同模块
				ModuleItemBean bean = new ModuleItemBean();
				BeanUtils.copyProperties(item, bean);
				menuList.get(menuList.size() - 1).addItem(bean);
			}
		}
		//对菜单排序
		int iPos = 0;
		List<MenuBean> list = new ArrayList<MenuBean>();
		for (int i = 0; i < menuList.size(); i++) {
			MenuBean bfBean = menuList.get(i);
			iPos = i;
			for (int j = i + 1; j < menuList.size(); j++) {
				MenuBean afBean = menuList.get(j);
				if (afBean.getShowOrder().compareTo(bfBean.getShowOrder()) < 0) {
					iPos = j;
				}
			}
			list.add(menuList.get(iPos));
			menuList.remove(iPos);
			i--;
		}
		return list;*/
	}
}
