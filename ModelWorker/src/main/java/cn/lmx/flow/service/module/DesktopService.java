package cn.lmx.flow.service.module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.module.ModuleItemBean;
import cn.lmx.flow.dao.module.DesktopDao;
import cn.lmx.flow.dao.module.GroupUserDao;
import cn.lmx.flow.dao.module.IconDao;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.dao.module.ModuleItemDao;
import cn.lmx.flow.dao.module.PermissionDao;
import cn.lmx.flow.entity.module.Desktop;
import cn.lmx.flow.entity.module.GroupUser;
import cn.lmx.flow.entity.module.Icons;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.module.ModuleItems;
import cn.lmx.flow.entity.module.Permission;

@Repository("DesktopService")
public class DesktopService {
	//桌面
	@Resource(name="DesktopDao")
	private DesktopDao desktopDao;
	//菜单一栏
	@Resource(name="ModuleItemDao")
	private ModuleItemDao moduleItemDao;
	//模块一栏
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	//权限一栏
	@Resource(name="PermissionDao")
	private PermissionDao permissionDao;
	//用户组
	@Resource(name="GroupUserDao")
	private GroupUserDao groupUserDao;
	//图标
	@Resource(name="IconDao")
	private IconDao iconDao;
	/**
	 * 取得个人桌面
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String userNo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> deskList = desktopDao.listByUserNo(userNo);
			List<?> itemList = moduleItemDao.getMenu();
			List<ModuleItemBean> menuList = new ArrayList<ModuleItemBean>();
			if (itemList != null && itemList.size() > 0) {
				String moduleNo = "";
				Module module = null;
				int index = 0;
				for (int i = 0; i < itemList.size(); i++) {
					ModuleItems item = (ModuleItems)itemList.get(i);
					if (!moduleNo.equals(item.getModuleNo())) {
						if (moduleNo != null && !"".equals(moduleNo)) {
							module = moduleDao.getModuleByNo(moduleNo);
							if (module != null) {
								for (int j = index; j < i; j ++) {
									ModuleItems item1 = (ModuleItems)itemList.get(j);
									if (item1.getModuleNo() != null && item1.getModuleNo().equals(moduleNo)) {
										ModuleItemBean bean = new ModuleItemBean();
										BeanUtils.copyProperties(item1, bean);
										bean.setModuleName(module.getName());
										menuList.add(bean);
									}
								}
								index = i;
							}
						}
						moduleNo = item.getModuleNo();
					}
				}
				ModuleItems item = (ModuleItems)itemList.get(index);
				module = moduleDao.getModuleByNo(item.getModuleNo());
				for (int i = index; i < itemList.size(); i++) {
					ModuleItems item1 = (ModuleItems)itemList.get(i);
					if (item1.getModuleNo() != null && item1.getModuleNo().equals(moduleNo)) {
						ModuleItemBean bean = new ModuleItemBean();
						BeanUtils.copyProperties(item1, bean);
						bean.setModuleName(module.getName());
						menuList.add(bean);
					}
				}
			}
			//取得权限一栏
			List<Permission> permList = new ArrayList<Permission>();
			List<?> userPermList = permissionDao.getPermissionByUserTypeNo("U", userNo);
			if (userPermList != null) {
				for (int i = 0; i < userPermList.size(); i++) {
					permList.add((Permission)userPermList.get(i));
				}
			}
			//取得用户组
			List<?> groupList = groupUserDao.getGroupByUserNo(userNo);
			if (groupList != null) {
				for (int i = 0; i < groupList.size(); i++) {
					GroupUser groupUser = (GroupUser)groupList.get(i);
					List<?> groupPermList = permissionDao.getPermissionByUserTypeNo("G", groupUser.getGroupNo());
					if (groupPermList == null || groupPermList.size() <= 0) {
						continue;
					}
					for (int j = 0; j < groupPermList.size(); j++) {
						permList.add((Permission)groupPermList.get(j));
					}
				}
			}
			//去除没有权限的菜单
			for (int i = 0; i < menuList.size(); i++) {
				ModuleItemBean bean = menuList.get(i);
				if (bean == null || bean.getItemNo() == null) {
					menuList.remove(i);
					i--;
					continue;
				}
				boolean hasPerm = false;
				for (int j = 0; j < permList.size(); j++) {
					Permission perm = permList.get(j);
					if (perm == null) {
						continue;
					}
					if (bean.getItemNo().equals(perm.getItemNo())) {
						hasPerm = true;
						break;
					}
				}
				if (hasPerm == false) {
					menuList.remove(i);
					i--;
					continue;
				}
			}
			map.put("desktop", deskList);
			map.put("menu", menuList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 保存我的桌面
	 * @param systemUser
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void save(String systemUser, String ids) throws Exception {
		try {
			if (ids == null || "".equals(ids)) {
				throw new Exception("请选择添加的功能！");
			}
			String[] arrId = ids.split(",");
			if (arrId == null || arrId.length <= 0) {
				throw new Exception("请选择添加的功能！");
			}
			//取得该用户对应的桌面
			List<?> desktopList = desktopDao.listByUserNo(systemUser);
			if (desktopList == null) {
				desktopList = new ArrayList<Object>();
			}
			String maxShowOrder = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			for (int i = 0; i < arrId.length; i++) {
				String id = arrId[i];
				if (id == null || "".equals(id)) {
					continue;
				}
				String showOrder = null;
				for (int j = 0; j < desktopList.size(); j++) {
					Desktop desktop = (Desktop)desktopList.get(j);
					if (id.equals(desktop.getItemNo())) {
						throw new Exception("功能（" + desktop.getItemName() + "）已经存在于桌面中！");
					}
					if (maxShowOrder == null) {
						if (showOrder == null) {
							showOrder = desktop.getShowOrder();
						}
						if (showOrder.compareTo(desktop.getShowOrder()) < 0) {
							showOrder = desktop.getShowOrder();
						}
					}
				}
				if (maxShowOrder == null) {
					maxShowOrder = showOrder;
				}
				if (maxShowOrder == null || "".equals(maxShowOrder)) {
					maxShowOrder = "00000";
				}
				ModuleItems item = moduleItemDao.getItemByNo(id);
				if (item == null) {
					throw new Exception("对应的功能不存在，请确认后重试！");
				}
				Module module = moduleDao.getModuleByNo(item.getModuleNo());
				if (module == null) {
					throw new Exception("对应的模块不存在，请确认后重试！");
				}
				//功能图标
				Icons icon = iconDao.getIconByItemNo(item.getItemNo());
				int iOrder = Integer.parseInt(maxShowOrder);
				iOrder++;
				maxShowOrder = new StringBuffer("0000").append(iOrder).toString();
				maxShowOrder = maxShowOrder.substring(maxShowOrder.length() - 5);
				Desktop desktop = new Desktop();
				desktop.setId(UUID.randomUUID().toString());
				desktop.setUserNo(systemUser);
				desktop.setItemNo(item.getItemNo());
				desktop.setItemName(item.getItemName());
				desktop.setActionUrl(item.getActionUrl());
				desktop.setShowOrder(maxShowOrder);
				desktop.setShowType("0");
				if (icon != null) {
					desktop.setIconFile(icon.getIconFile());
				} else {
					desktop.setIconFile("/img/index/default.png");
				}
				desktop.setCreateUser(systemUser);
				desktop.setCreateTime(sdf.format(c.getTime()));
				desktopDao.add(desktop);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
