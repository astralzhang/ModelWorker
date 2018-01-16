package cn.lmx.flow.service.module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.lmx.flow.bean.module.UserPermissionBean;
import cn.lmx.flow.dao.module.GroupDao;
import cn.lmx.flow.dao.module.GroupUserDao;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.dao.module.PermissionDao;
import cn.lmx.flow.dao.module.PrivilegeDao;
import cn.lmx.flow.dao.module.UserDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.entity.module.Group;
import cn.lmx.flow.entity.module.GroupUser;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.module.Permission;
import cn.lmx.flow.entity.module.Privilege;
import cn.lmx.flow.entity.module.User;
import cn.lmx.flow.entity.system.DataLogs;

@Repository("PermissionService")
public class PermissionService {
	@Resource(name="PermissionDao")
	private PermissionDao permissionDao;
	@Resource(name="GroupUserDao")
	private GroupUserDao groupUserDao;
	//组
	@Resource(name="GroupDao")
	private GroupDao groupDao;
	//用户
	@Resource(name="UserDao")
	private UserDao userDao;
	//权限功能
	@Resource(name="PrivilegeDao")
	private PrivilegeDao privilegeDao;
	//模块
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	//日志
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	/**
	 * 取得用户权限
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<UserPermissionBean> getPermission(String userNo) throws Exception {
		try {
			//取得用户所属组
			List<?> groupList = groupUserDao.getGroupByUserNo(userNo);
			List<UserPermissionBean> userPermissionList = new ArrayList<UserPermissionBean>();
			if (groupList != null && groupList.size() > 0) {
				//判断是否为系统管理员
				for (int i = 0; i < groupList.size(); i++) {
					GroupUser groupUser = (GroupUser)groupList.get(i);
					if ("system".equals(groupUser)) {
						List<?> privilegeList = privilegeDao.listAll();
						if (privilegeList != null) {
							for (int j = 0; j < privilegeList.size(); j++) {
								Privilege privilege = (Privilege)privilegeList.get(j);
								UserPermissionBean permissionBean = new UserPermissionBean();
								permissionBean.setGroupNo("G");
								permissionBean.setItemNo(privilege.getNo());
								permissionBean.setActionUrl(privilege.getActionUrl());
								permissionBean.setUserNo("system");
								userPermissionList.add(permissionBean);
							}
							return userPermissionList;
						}
					}
				}
				for (int i = 0; i < groupList.size(); i++) {
					GroupUser groupUser = (GroupUser)groupList.get(i);
					List<?> permissionList = (List<?>)permissionDao.getPermissionByUserNo(groupUser.getGroupNo(), "G");
					if (permissionList != null) {
						for (int j = 0; j < permissionList.size(); j++) {
							UserPermissionBean bean = (UserPermissionBean)permissionList.get(j);
							userPermissionList.add(bean);
						}
					}
				}
			}
			//用户特定权限
			List<?> permissionList = (List<?>)permissionDao.getPermissionByUserNo(userNo, "U");
			if (permissionList != null && permissionList.size() > 0) {
				for (int j = 0; j < permissionList.size(); j++) {
					UserPermissionBean bean = (UserPermissionBean)permissionList.get(j);
					userPermissionList.add(bean);
				}
			}
			return userPermissionList;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 取得用户和工作组一栏
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String userType, String userNo, String userName) throws Exception {
		try {
			//工作组一栏
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<?> groupList = null;
			//if (userType == null || "".equals(userType)) {
				//groupList = groupDao.list();
			//} else 	if ("Group".equals(userType)) {
			groupList = groupDao.listByNoName(userNo, userName);
			//}
			Map<String, Object> groupMap = new HashMap<String, Object>();
			groupMap.put("id", "Group");
			groupMap.put("name", "工作组");
			groupMap.put("pId", null);
			groupMap.put("open", true);
			groupMap.put("chkDisabled", true);
			list.add(groupMap);
			if (groupList != null) {				
				for (int i = 0; i < groupList.size(); i++) {
					Group group = (Group)groupList.get(i);
					if ("system".equals(group.getGroupNo())) {
						continue;
					}
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("id", group.getGroupNo());
					tempMap.put("name", group.getGroupName());
					tempMap.put("pId", "Group");
					list.add(tempMap);
				}
			}
			//用户一栏
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", "User");
			userMap.put("name", "用户");
			userMap.put("pId", null);
			userMap.put("open", false);
			userMap.put("chkDisabled", true);
			list.add(userMap);
			List<?> userList = null;
			//if (userType == null || "".equals(userType)) {
			//	userList = userDao.list();
			//} else 	if ("User".equals(userType)) {
			userList = userDao.listByNoName(userNo, userName);
			//}
			if (userList != null) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User)userList.get(i);
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("id", user.getUserNo());
					tempMap.put("name", user.getUserName());
					tempMap.put("pId", "User");
					list.add(tempMap);
				}
			}
			//权限功能一栏
			List<Map<String, Object>> moduleList = new ArrayList<Map<String, Object>>();
			//模块
			List<?> tempModuleList = moduleDao.list();
			if (tempModuleList != null) {
				for (int i = 0; i < tempModuleList.size(); i++) {
					Module module = (Module)tempModuleList.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", module.getNo());
					map.put("name", module.getName());
					map.put("pId", null);
					map.put("open", false);
					moduleList.add(map);
				}
			}
			List<?> privilegeList = privilegeDao.listAll();
			if (privilegeList != null) {
				for (int i = 0; i < privilegeList.size(); i++) {
					Privilege privilege = (Privilege)privilegeList.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", privilege.getNo());
					map.put("pId", privilege.getModuleNo());
					map.put("name", privilege.getName());
					moduleList.add(map);
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Gson gson = new Gson();
			resultMap.put("user", gson.toJson(list));
			resultMap.put("module", gson.toJson(moduleList));
			return resultMap;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得用户权限
	 * @param type
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> getPermission(String type, String userNo) throws Exception {
		try {
			if (type == null || "".equals(type)) {
				throw new Exception("用户类型不正确！");
			}
			if ("Group".equals(type)) {
				return permissionDao.getPermissionByUserNo(userNo, "G");
			} else if ("User".equals(type)) {
				return permissionDao.getPermissionByUserNo(userNo, "U");
			} else {
				throw new Exception("用户类型不正确！");
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 保存权限
	 * @param systemUser
	 * @param userType
	 * @param userNo
	 * @param modules
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void savePermission(String systemUser, String userType, String userNo, String modules) throws Exception {
		try {
			if (userType == null || "".equals(userType)) {
				throw new Exception("没有指定用户类型！");
			}
			if (userNo == null || "".equals(userNo)) {
				throw new Exception("没有指定具体用户！");
			}
			if ("Group".equals(userType)) {
				userType = "G";
			} else if ("User".equals(userType)) {
				userType = "U";
			} else {
				throw new Exception("用户类型不正确！");
			}
			//取得指定用户的所有权限
			List<?> permissionList = permissionDao.getPermissionByUserTypeNo(userNo, userType);
			//删除所有指定用户的权限
			permissionDao.deletePermissionByUserNo(userNo);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Gson gson = new Gson();
			if (modules == null || "".equals(modules)) {
				//写Log
				for (int i = 0; i < permissionList.size(); i++) {
					Permission permission = (Permission)permissionList.get(i);
					DataLogs log = new DataLogs();
					log.setId(UUID.randomUUID().toString());
					log.setType("Delete");
					log.setProcessor("删除权限");
					log.setDataTable("Permissions");
					log.setBeforeData(gson.toJson(permission));
					log.setCreateUser(systemUser);
					log.setCreateTime(sdf.format(c.getTime()));
					dataLogDao.add(log);
				}
				return;
			}
			String[] arrModule = modules.split(",");
			List<Permission> permList = new ArrayList<Permission>();
			for (int i = 0; i < arrModule.length; i++) {
				String moduleNo = arrModule[i];
				if (moduleNo == null || "".equals(moduleNo)) {
					continue;
				}
				boolean hasData = false;
				for (int j = 0; j < permissionList.size(); j++) {
					Permission permission = (Permission)permissionList.get(j);
					if (moduleNo.equals(permission.getItemNo())) {
						permList.add(permission);
						permissionList.remove(j);
						permissionDao.add(permission);
						hasData = true;
						break;
					}
				}
				if (hasData == false) {
					//新增
					Permission permission = new Permission();
					permission.setId(UUID.randomUUID().toString());
					permission.setUserType(userType);
					permission.setUserNo(userNo);
					permission.setItemNo(moduleNo);
					permission.setCreateUser(systemUser);
					permission.setCreateTime(sdf.format(c.getTime()));
					permissionDao.add(permission);
					permList.add(permission);
					DataLogs log = new DataLogs();
					log.setId(UUID.randomUUID().toString());
					log.setType("Add");
					log.setProcessor("新增权限");
					log.setDataTable("Permissions");
					log.setAfterData(gson.toJson(permission));
					log.setCreateUser(systemUser);
					log.setCreateTime(sdf.format(c.getTime()));
					dataLogDao.add(log);
				}
			}
			//删除不需要的权限
			for (int i = 0; i < permissionList.size(); i++) {
				Permission permission = (Permission)permissionList.get(i);
				DataLogs log = new DataLogs();
				log.setId(UUID.randomUUID().toString());
				log.setType("Delete");
				log.setProcessor("删除权限");
				log.setDataTable("Permissions");
				log.setBeforeData(gson.toJson(permission));
				log.setCreateUser(systemUser);
				log.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(log);
			}
			//取得所用功能
			List<?> privList = privilegeDao.listAll();
			if (privList == null || privList.size() <= 0) {
				return;
			}
			//check权限的整合型
			for (int i = 0; i < permList.size(); i++) {
				Permission perm = (Permission)permList.get(i);
				Privilege privilege = null;
				for (int j = 0; j < privList.size(); j++) {
					Privilege priv = (Privilege)privList.get(j);
					if (perm.getItemNo().equals(priv.getNo())) {
						privilege = priv;
						break;
					}
				}
				if (privilege == null) {
					continue;
				}
				if (privilege.getParentNo() == null || "".equals(privilege.getParentNo())) {
					continue;
				}
				String parentNo = privilege.getParentNo();
				boolean ok = false;
				for (int j = 0; j < permList.size(); j++) {
					Permission permission = (Permission)permList.get(j);
					if (parentNo.equals(permission.getItemNo())) {
						ok = true;
						break;
					}
				}
				if (ok == false) {
					throw new Exception("[" + privilege.getName() + "]的权限设定错误，必须设定其画面功能！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
