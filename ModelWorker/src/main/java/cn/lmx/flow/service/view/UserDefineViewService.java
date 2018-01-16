package cn.lmx.flow.service.view;

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

import cn.lmx.flow.bean.view.UserDefineViewBean;
import cn.lmx.flow.dao.module.ModuleItemDao;
import cn.lmx.flow.dao.module.PermissionDao;
import cn.lmx.flow.dao.module.PrivilegeDao;
import cn.lmx.flow.dao.view.PublishedViewDao;
import cn.lmx.flow.dao.view.UserDefineViewDao;
import cn.lmx.flow.entity.module.ModuleItems;
import cn.lmx.flow.entity.module.Permission;
import cn.lmx.flow.entity.module.Privilege;
import cn.lmx.flow.entity.view.PublishedViews;
import cn.lmx.flow.entity.view.UserDefineViews;

@Repository("UserDefineViewService")
public class UserDefineViewService {
	//用户自定义画面
	@Resource(name="UserDefineViewDao")
	private UserDefineViewDao userDefineViewDao;
	//自定义画面发布
	@Resource(name="PublishedViewDao")
	private PublishedViewDao publishedViewDao;
	//菜单
	@Resource(name="ModuleItemDao")
	private ModuleItemDao moduleItemDao;
	//权限
	@Resource(name="PermissionDao")
	private PermissionDao permissionDao;
	//功能
	@Resource(name="PrivilegeDao")
	private PrivilegeDao privilegeDao;
	/**
	 * 用户自定义画面保存
	 * @param no
	 * @param name
	 * @param voucherType
	 * @param headScript
	 * @param detailScript
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveUserDefineView(String systemUser, String no, String name, String voucherType, String listScript, String headScript, String detailScript) throws Exception {
		UserDefineViews userDefineView = userDefineViewDao.getMaxVersion(no);
		String version = "";
		if (userDefineView == null) {
			version = "00001";
		} else {
			version = new StringBuffer("")
					.append("0000")
					.append(Integer.parseInt(userDefineView.getVersion()) + 1).toString();
			version = version.substring(version.length() - 5);
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		UserDefineViews param = new UserDefineViews();
		param.setId(UUID.randomUUID().toString());
		param.setNo(no);
		param.setName(name);
		param.setVoucherType(voucherType);
		param.setVersion(version);
		param.setStatus("0");
		param.setListScript(listScript);
		param.setHeadScript(headScript);
		param.setDetailScript(detailScript);
		param.setCreateUser(systemUser);
		param.setCreateTime(sdf.format(c.getTime()));
		userDefineViewDao.add(param);
	}
	/**
	 * 取得最新版本的自定义画面一栏
	 * @param no
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<UserDefineViewBean> getLastVersionViewList(String no, String status) throws Exception {
		List<?> list = userDefineViewDao.getLastVersionList(no, status);
		List<UserDefineViewBean> resultList = new ArrayList<UserDefineViewBean>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				resultList.add((UserDefineViewBean)list.get(i));
			}
		}
		return resultList;
	}
	/**
	 * 取得指定编码的发布画面
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public PublishedViews getPublishedView(String no) throws Exception {
		PublishedViews publishedView = publishedViewDao.getViewByNo(no);
		if (publishedView == null) {
			return new PublishedViews();
		}
		return publishedView;
	}
	/**
	 * 发布画面
	 * @param systemUser
	 * @param no
	 * @param moduleNo
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void publishView(String systemUser, String no, String moduleNo, String modules) throws Exception {
		List<?> list = userDefineViewDao.getLastVersionList(no, "");
		if (list == null || list.size() <= 0) {
			throw new Exception("没有需要发布的版本！");
		}
		UserDefineViewBean bean = (UserDefineViewBean)list.get(0);
		if ("1".equals(bean.getStatus())) {
			throw new Exception("最终版本为已发布版本，无需再次发布！");
		}
		String[] arrModule = modules.split(",");
		if (arrModule.length <= 0) {
			throw new Exception("请选择需要发布的机能！");
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < arrModule.length; i++) {
			map.put(arrModule[i], "Y");
		}
		//取得已发布的版本
		PublishedViews publishedView = publishedViewDao.getViewByNo(bean.getNo());
		String version = "";
		if (publishedView == null) {
			version = "00001";
		} else {
			version = new StringBuffer("00000")
						.append(Integer.parseInt(publishedView.getVersion()) + 1).toString();
			version = version.substring(version.length() - 5);
			//删除已发布版本
			publishedViewDao.deleteView(bean.getNo());
			//删除已发布菜单
			moduleItemDao.deleteModuleItem(bean.getNo());
			moduleItemDao.deleteModuleItem(bean.getNo() + "List");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Add");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Edit");
			moduleItemDao.deleteModuleItem(bean.getNo() + "View");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Delete");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Save");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Submit");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Upload");
			moduleItemDao.deleteModuleItem(bean.getNo() + "ViewAttachment");
			moduleItemDao.deleteModuleItem(bean.getNo() + "Audit");
			moduleItemDao.deleteModuleItem(bean.getNo() + "CancelAudit");
			moduleItemDao.deleteModuleItem(bean.getNo() + "CancelSubmit");
			//删除已发布的功能
			privilegeDao.deleteByNo(bean.getNo());
			privilegeDao.deleteByNo(bean.getNo() + "List");
			privilegeDao.deleteByNo(bean.getNo() + "Add");
			privilegeDao.deleteByNo(bean.getNo() + "Edit");
			privilegeDao.deleteByNo(bean.getNo() + "View");
			privilegeDao.deleteByNo(bean.getNo() + "Delete");
			privilegeDao.deleteByNo(bean.getNo() + "Save");
			privilegeDao.deleteByNo(bean.getNo() + "Submit");
			privilegeDao.deleteByNo(bean.getNo() + "Upload");
			privilegeDao.deleteByNo(bean.getNo() + "Audit");
			privilegeDao.deleteByNo(bean.getNo() + "CancelAudit");
			privilegeDao.deleteByNo(bean.getNo() + "CancelSubmit");
			//删除系统管理员权限
			permissionDao.deletePermission("system", bean.getNo());
			permissionDao.deletePermission("system", bean.getNo() + "List");
			permissionDao.deletePermission("system", bean.getNo() + "Add");
			permissionDao.deletePermission("system", bean.getNo() + "Edit");
			permissionDao.deletePermission("system", bean.getNo() + "View");
			permissionDao.deletePermission("system", bean.getNo() + "Delete");
			permissionDao.deletePermission("system", bean.getNo() + "Save");
			permissionDao.deletePermission("system", bean.getNo() + "Submit");
			permissionDao.deletePermission("system", bean.getNo() + "Upload");
			permissionDao.deletePermission("system", bean.getNo() + "Audit");
			permissionDao.deletePermission("system", bean.getNo() + "CancelAudit");
			permissionDao.deletePermission("system", bean.getNo() + "CancelSubmit");
		}
		//根据模块编码取得菜单一栏
		List<?> menuList = moduleItemDao.getModuleItem(moduleNo);
		String showOrder = "";
		ModuleItems menu = null;
		if (menuList == null || menuList.size() <= 0) {
			showOrder = "00000";
		} else {
			menu = (ModuleItems)menuList.get(menuList.size() - 1);
			showOrder = new StringBuffer("")
						.append("00000")
						.append(Integer.parseInt(menu.getShowOrder()) + 5).toString();
			showOrder = showOrder.substring(showOrder.length() - 5);
		}
		//新增已发布版本
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		PublishedViews paramPublishedView = new PublishedViews();
		BeanUtils.copyProperties(bean, paramPublishedView);
		paramPublishedView.setVersion(version);
		paramPublishedView.setModuleNo(moduleNo);
		paramPublishedView.setShowOrder(showOrder);
		paramPublishedView.setCreateUser(systemUser);
		paramPublishedView.setCreateTime(sdf.format(c.getTime()));
		publishedViewDao.add(paramPublishedView);
		//修改当前版本的发布版本
		userDefineViewDao.publish(bean.getId(), version, systemUser, sdf.format(c.getTime()));
		//新增菜单
		boolean add = true;
		//初始化菜单
		menu = moduleItemDao.getItemByNo(bean.getNo());
		if (menu == null) {
			menu = new ModuleItems();
			menu.setItemNo(bean.getNo());
			menu.setCreateUser(systemUser);
			menu.setCreateTime(sdf.format(c.getTime()));
			add = true;
		} else {
			menu.setUpdateUser(systemUser);
			menu.setUpdateTime(sdf.format(c.getTime()));
			add = false;
		}
		menu.setItemName(bean.getName());
		menu.setDescription(bean.getName());
		menu.setActionUrl("/view/init/" + bean.getNo());
		menu.setModuleNo(moduleNo);
		menu.setMenuFlag("Y");
		menu.setShowOrder(showOrder);
		menu.setCssStyle("fa fa-dashboard");
		if (add) {
			moduleItemDao.add(menu);
		} else {
			moduleItemDao.edit(menu);
		}
		//新增功能
		Privilege privilege = new Privilege();
		privilege.setNo(bean.getNo());
		privilege.setName(bean.getName());
		privilege.setModuleNo(moduleNo);
		privilege.setActionUrl("/view/init/" + bean.getNo());
		privilege.setPrivType("M");
		privilege.setCreateUser(systemUser);
		privilege.setCreateTime(sdf.format(c.getTime()));
		privilegeDao.save(privilege);
		//初始化权限
		Permission permission = new Permission();
		permission.setId(UUID.randomUUID().toString());
		permission.setUserNo("system");
		permission.setUserType("G");
		permission.setItemNo(bean.getNo());
		permission.setCreateUser(systemUser);
		permission.setCreateTime(sdf.format(c.getTime()));
		permissionDao.add(permission);
		//搜索菜单
		menu = moduleItemDao.getItemByNo(bean.getNo() + "List");
		add = true;
		if (menu == null) {
			menu = new ModuleItems();
			menu.setItemNo(bean.getNo() + "List");
			menu.setCreateUser(systemUser);
			menu.setCreateTime(sdf.format(c.getTime()));
			add = true;
		} else {
			menu.setUpdateUser(systemUser);
			menu.setUpdateTime(sdf.format(c.getTime()));
			add = false;
		}
		menu.setItemName(bean.getName() + "搜索");
		menu.setDescription(bean.getName() + "搜索");
		menu.setActionUrl("/view/list/" + bean.getNo());
		menu.setModuleNo(moduleNo);
		menu.setMenuFlag("N");
		menu.setShowOrder(null);
		menu.setCssStyle("fa fa-dashboard");
		if (add) {
			moduleItemDao.add(menu);
		} else {
			moduleItemDao.edit(menu);
		}
		permission = new Permission();
		permission.setId(UUID.randomUUID().toString());
		permission.setUserNo("system");
		permission.setUserType("G");
		permission.setItemNo(bean.getNo() + "List");
		permission.setCreateUser(systemUser);
		permission.setCreateTime(sdf.format(c.getTime()));
		permissionDao.add(permission);
		if ("Y".equals(map.get("add"))) {
			//新增菜单
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Add");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Add");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "新增");
			menu.setDescription(bean.getName() + "新增");
			menu.setActionUrl("/view/add/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Add");
			privilege.setName(bean.getName() + "新增");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/add/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			//权限
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Add");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("edit"))) {
			//修改菜单
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Edit");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Edit");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "修改");
			menu.setDescription(bean.getName() + "修改");
			menu.setActionUrl("/view/edit/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Edit");
			privilege.setName(bean.getName() + "修改");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/edit/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			//权限
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Edit");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("view"))) {
			//查看菜单
			menu = moduleItemDao.getItemByNo(bean.getNo() + "View");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "View");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "查看");
			menu.setDescription(bean.getName() + "查看");
			menu.setActionUrl("/view/view/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "View");
			privilege.setName(bean.getName() + "查看");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/view/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "View");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("delete"))) {
			//删除
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Delete");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Delete");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "删除");
			menu.setDescription(bean.getName() + "删除");
			menu.setActionUrl("/view/delete/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Delete");
			privilege.setName(bean.getName() + "删除");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/delete/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Delete");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("audit"))) {
			//审核
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Audit");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Audit");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "审核");
			menu.setDescription(bean.getName() + "审核");
			menu.setActionUrl("/view/audit/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Audit");
			privilege.setName(bean.getName() + "审核");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/audit/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Audit");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("cancelAudit"))) {
			//撤销审核
			menu = moduleItemDao.getItemByNo(bean.getNo() + "CancelAudit");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "CancelAudit");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "撤审");
			menu.setDescription(bean.getName() + "撤审");
			menu.setActionUrl("/view/cancelAudit/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "CancelAudit");
			privilege.setName(bean.getName() + "撤审");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/cancelAudit/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "CancelAudit");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("attachment"))) {
			//附件上传
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Upload");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Upload");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "附件上传");
			menu.setDescription(bean.getName() + "附件上传");
			menu.setActionUrl("/view/upload/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Upload");
			privilege.setName(bean.getName() + "附件上传");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/upload/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Upload");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("save"))) {
			//保存
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Save");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Save");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "保存");
			menu.setDescription(bean.getName() + "保存");
			menu.setActionUrl("/view/save/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Save");
			privilege.setName(bean.getName() + "保存");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/save/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Save");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("submit"))) {
			//提交
			menu = moduleItemDao.getItemByNo(bean.getNo() + "Submit");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "Submit");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "提交");
			menu.setDescription(bean.getName() + "提交");
			menu.setActionUrl("/view/submit/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "Submit");
			privilege.setName(bean.getName() + "提交");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/submit/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "Submit");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		if ("Y".equals(map.get("cancelSubmit"))) {
			//撤销工作流
			menu = moduleItemDao.getItemByNo(bean.getNo() + "CancelSubmit");
			add = true;
			if (menu == null) {
				menu = new ModuleItems();
				menu.setItemNo(bean.getNo() + "CancelSubmit");
				menu.setCreateUser(systemUser);
				menu.setCreateTime(sdf.format(c.getTime()));
				add = true;
			} else {
				menu.setUpdateUser(systemUser);
				menu.setUpdateTime(sdf.format(c.getTime()));
				add = false;
			}
			menu.setItemName(bean.getName() + "撤销");
			menu.setDescription(bean.getName() + "撤销");
			menu.setActionUrl("/view/cancelSubmit/" + bean.getNo());
			menu.setModuleNo(moduleNo);
			menu.setMenuFlag("N");
			menu.setShowOrder(null);
			menu.setCssStyle("fa fa-dashboard");
			if (add) {
				moduleItemDao.add(menu);
			} else {
				moduleItemDao.edit(menu);
			}
			//新增功能
			privilege = new Privilege();
			privilege.setNo(bean.getNo() + "CancelSubmit");
			privilege.setName(bean.getName() + "撤销");
			privilege.setModuleNo(moduleNo);
			privilege.setActionUrl("/view/cancelSubmit/" + bean.getNo());
			privilege.setPrivType("B");
			privilege.setParentNo(bean.getNo());
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setItemNo(bean.getNo() + "CancelSubmit");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
		}
		//附件查看
		menu = moduleItemDao.getItemByNo(bean.getNo() + "ViewAttachment");
		add = true;
		if (menu == null) {
			menu = new ModuleItems();
			menu.setItemNo(bean.getNo() + "ViewAttachment");
			menu.setCreateUser(systemUser);
			menu.setCreateTime(sdf.format(c.getTime()));
			add = true;
		} else {
			menu.setUpdateUser(systemUser);
			menu.setUpdateTime(sdf.format(c.getTime()));
			add = false;
		}
		menu.setItemName(bean.getName() + "附件预览");
		menu.setDescription(bean.getName() + "附件预览");
		menu.setActionUrl("/view/viewAttachment/" + bean.getNo());
		menu.setModuleNo(moduleNo);
		menu.setMenuFlag("N");
		menu.setShowOrder(null);
		menu.setCssStyle("fa fa-dashboard");
		if (add) {
			moduleItemDao.add(menu);
		} else {
			moduleItemDao.edit(menu);
		}
		permission = new Permission();
		permission.setId(UUID.randomUUID().toString());
		permission.setUserNo("system");
		permission.setUserType("G");
		permission.setItemNo(bean.getNo() + "ViewAttachment");
		permission.setCreateUser(systemUser);
		permission.setCreateTime(sdf.format(c.getTime()));
		permissionDao.add(permission);
	}
}
