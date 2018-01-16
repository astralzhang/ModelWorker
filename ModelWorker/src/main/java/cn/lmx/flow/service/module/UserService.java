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

import com.google.gson.Gson;

import cn.lmx.flow.bean.module.GroupBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.module.GroupDao;
import cn.lmx.flow.dao.module.GroupUserDao;
import cn.lmx.flow.dao.module.UserDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.entity.module.Group;
import cn.lmx.flow.entity.module.GroupUser;
import cn.lmx.flow.entity.module.User;
import cn.lmx.flow.entity.system.DataLogs;
import cn.lmx.flow.utils.MD5;

@Repository("UserService")
public class UserService {
	//用户
	@Resource(name="UserDao")
	private UserDao userDao;
	//组
	@Resource(name="GroupDao")
	private GroupDao groupDao;
	//用户组
	@Resource(name="GroupUserDao")
	private GroupUserDao groupUserDao;
	//数据Log
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	//SQLDao
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	/**
	 * 用户登录
	 * @param userNo
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public UserBean login(String userNo, String password) throws Exception {
		try {
			password = MD5.encrypt(password);
System.out.println(password);			
			List<?> userList = userDao.getUserByPassword(userNo, password);
			if (userList == null || userList.size() <= 0) {
				return null;
			}
			User user = (User)userList.get(0);
			UserBean userBean = new UserBean();
			BeanUtils.copyProperties(user, userBean);
			return userBean;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 取得用户一栏
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String userNo, String userName) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//取得用户一栏
			List<?> list = userDao.listByNoName(userNo, userName);
			if (list == null || list.size() <= 0) {
				return null;
			}
			List<UserBean> rstList = new ArrayList<UserBean>();
			for (int i = 0; i < list.size(); i++) {
				User user = (User)list.get(i);
				if ("demo".equals(user.getUserNo().toLowerCase())) {
					continue;
				}
				UserBean userBean = new UserBean();
				BeanUtils.copyProperties(user, userBean);
				rstList.add(userBean);
			}
			map.put("userList", rstList);
			//取得用户组一栏
			List<?> groupList = groupDao.list();
			if (groupList != null) {
				List<GroupBean> tempList = new ArrayList<GroupBean>();
				for (int i = 0; i < groupList.size(); i++) {
					Group group = (Group)groupList.get(i);
					GroupBean groupBean = new GroupBean();
					BeanUtils.copyProperties(group, groupBean);
					tempList.add(groupBean);
				}
				map.put("groupList", tempList);
			}
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得用户
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public UserBean getUserByNo(String no) throws Exception {
		try {
			User user = userDao.getUserById(no);
			if (user == null) {
				throw new Exception("用户不存在！");
			}
			if ("V".equals(user.getStatus())) {
				throw new Exception("用户已删除！");
			}
			UserBean userBean = new UserBean();
			BeanUtils.copyProperties(user, userBean);
			return userBean;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 保存用户
	 * @param systemUser
	 * @param userBean
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveUser(String systemUser, UserBean userBean) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			User tempUser = userDao.getUserById(userBean.getUserNo());
			if (tempUser == null) {
				//新增用户
				tempUser = new User();
				BeanUtils.copyProperties(userBean, tempUser);
				tempUser.setStatus("Y");
				tempUser.setCreateUser(systemUser);
				tempUser.setCreateTime(sdf.format(c.getTime()));
				tempUser.setUserPassword(MD5.encrypt("123456"));
				userDao.add(tempUser);
			} else {
				//写修改Log
				Gson gson = new Gson();
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setBeforeData(gson.toJson(tempUser));
				//修改
				String password = tempUser.getUserPassword();
				String status = tempUser.getStatus();
				BeanUtils.copyProperties(userBean, tempUser);
				tempUser.setUserPassword(password);
				tempUser.setUpdateUser(systemUser);
				tempUser.setUpdateTime(sdf.format(c.getTime()));
				tempUser.setStatus(status);
				userDao.edit(tempUser);
				//
				dataLog.setDataTable("Users");
				dataLog.setAfterData(gson.toJson(tempUser));
				dataLog.setType("Update");
				dataLog.setProcessor("修改");
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 重置密码
	 * @param systemUser
	 * @param userNo
	 * @param password
	 */
	@Transactional(rollbackFor=Exception.class)
	public void resetPassword(String systemUser, String userNo, String newPassword, String confirmPassword) throws Exception {
		try {
			if (userNo == null || "".equals(userNo)) {
				throw new Exception("请指定用户编码！");
			}
			if (newPassword == null || "".equals(newPassword)) {
				throw new Exception("请输入新密码！");
			}
			if (newPassword.length() > 20) {
				throw new Exception("请输入20位以内的密码！");
			}
			if (!newPassword.equals(confirmPassword)) {
				throw new Exception("两次输入密码不一致！");
			}
			User user = userDao.getUserById(userNo);
			if (user == null) {
				throw new Exception("用户不存在！");
			}
			DataLogs dataLog = new DataLogs();
			Gson gson = new Gson();
			dataLog.setBeforeData(gson.toJson(user));
			dataLog.setId(UUID.randomUUID().toString());
			user.setUserPassword(MD5.encrypt(newPassword));
			user.setUpdateUser(systemUser);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			user.setUpdateTime(sdf.format(c.getTime()));
			userDao.edit(user);
			dataLog.setAfterData(gson.toJson(user));
			dataLog.setDataTable("Users");
			dataLog.setType("ReetPassword");
			dataLog.setProcessor("重置密码");
			dataLog.setCreateUser(systemUser);
			dataLog.setCreateTime(sdf.format(c.getTime()));
			dataLogDao.add(dataLog);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 用户删除
	 * @param systemUser
	 * @param userNo
	 */
	@Transactional(rollbackFor=Exception.class)
	public void delete(String systemUser, String ids) throws Exception {
		try {
			if (ids == null || "".equals(ids)) {
				throw new Exception("请指定需要删除的用户！");
			}
			String[] userNos = ids.split(",");
			if (userNos == null || userNos.length <= 0) {
				throw new Exception("请指定需要删除的用户！");
			}
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Gson gson = new Gson();
			for (int i = 0; i < userNos.length; i++) {
				String userNo = userNos[i];
				User user = userDao.getUserById(userNo);
				if (user == null) {
					throw new Exception("用户不存在！");
				}
				user.setUpdateTime(sdf.format(c.getTime()));
				userDao.delete(user);
				DataLogs dataLog = new DataLogs();
				dataLog.setBeforeData(gson.toJson(user));
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setAfterData(null);
				dataLog.setDataTable("Users");
				dataLog.setType("Delete");
				dataLog.setProcessor("删除系统用户");
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
				List<?> groupUserList = groupUserDao.getGroupByUserNo(userNo);
				if (groupUserList != null) {
					for (int j = 0; j < groupUserList.size(); j++) {
						GroupUser groupUser = (GroupUser)groupUserList.get(j);
						dataLog = new DataLogs();
						dataLog.setBeforeData(gson.toJson(groupUser));
						dataLog.setId(UUID.randomUUID().toString());
						dataLog.setAfterData(null);
						dataLog.setDataTable("GroupUsers");
						dataLog.setType("Delete");
						dataLog.setProcessor("删除组用户");
						dataLog.setCreateUser(systemUser);
						dataLog.setCreateTime(sdf.format(c.getTime()));
						dataLogDao.add(dataLog);
					}
					groupUserDao.deleteByUserNo(userNo);
				}
			}
			
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 根据用户取得所属组
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<GroupBean> getGroupByUserNo(String userNo) throws Exception {
		try {
			List<?> list = groupUserDao.getGroupByUserNo(userNo);
			if (list == null || list.size() <= 0) {
				return null;
			}
			List<GroupBean> groupList = new ArrayList<GroupBean>();
			for (int i = 0; i < list.size(); i++) {
				GroupUser groupUser = (GroupUser)list.get(i);
				GroupBean bean = new GroupBean();
				bean.setGroupNo(groupUser.getGroupNo());
				groupList.add(bean);
			}
			return groupList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 用户加入工作组
	 * @param systemUser
	 * @param userNo
	 * @param groups
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addGroup(String systemUser, String userNo, String groups) throws Exception {
		try {
			List<?> groupUserList = groupUserDao.getGroupByUserNo(userNo);
			//删除用户所属工作组
			groupUserDao.deleteByUserNo(userNo);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Gson gson = new Gson();
			if (groups == null || "".equals(groups)) {
				//写Log
				if (groupUserList != null && groupUserList.size() > 0) {
					for (int i = 0; i < groupUserList.size(); i++) {
						DataLogs dataLog = new DataLogs();
						dataLog.setId(UUID.randomUUID().toString());
						dataLog.setType("Delete");
						dataLog.setProcessor("删除用户所属工作组！");
						dataLog.setBeforeData(gson.toJson(groupUserList.get(i)));
						dataLog.setCreateUser(systemUser);
						dataLog.setCreateTime(sdf.format(c.getTime()));
						dataLogDao.add(dataLog);
					}
				}
				return;
			}
			String[] arrGroup = groups.split(",");
			for (int i = 0; i < arrGroup.length; i++) {
				String groupNo = arrGroup[i];
				boolean hasGroup = false;
				for (int j = 0; j < groupUserList.size(); j++) {
					GroupUser groupUser = (GroupUser)groupUserList.get(j);
					if (groupNo.equals(groupUser.getGroupNo())) {
						//原来的用户工作组
						groupUserDao.add(groupUser);
						groupUserList.remove(j);
						hasGroup = true;
						break;
					}
				}
				if (hasGroup == false) {
					//没有找到
					GroupUser groupUser = new GroupUser();
					groupUser.setId(UUID.randomUUID().toString());
					groupUser.setUserNo(userNo);
					groupUser.setGroupNo(groupNo);
					groupUser.setCreateUser(systemUser);
					groupUser.setCreateTime(sdf.format(c.getTime()));
					groupUserDao.add(groupUser);
					//写Log
					DataLogs dataLog = new DataLogs();
					dataLog.setId(UUID.randomUUID().toString());
					dataLog.setType("新增");
					dataLog.setProcessor("新增用户所属工作组！");
					dataLog.setBeforeData(gson.toJson(groupUser));
					dataLog.setCreateUser(systemUser);
					dataLog.setCreateTime(sdf.format(c.getTime()));
					dataLogDao.add(dataLog);
				}
			}
			//判断是否还有需要删除的数据
			if (groupUserList.size() > 0) {
				for (int i = 0; i < groupUserList.size(); i++) {
					DataLogs dataLog = new DataLogs();
					dataLog.setId(UUID.randomUUID().toString());
					dataLog.setType("Delete");
					dataLog.setProcessor("删除用户所属工作组！");
					dataLog.setBeforeData(gson.toJson(groupUserList.get(i)));
					dataLog.setCreateUser(systemUser);
					dataLog.setCreateTime(sdf.format(c.getTime()));
					dataLogDao.add(dataLog);
				}
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得用户对应的数据权限
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, List<String>> getUserDataPrivilege(String userNo) throws Exception {
		try {
			String sql = "select A.DeptNo from M_UserData_Authority_Detail A left join M_UserData_Authority_Head B on A.ParentId=B.ID where B.UserNo=:userNo";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userNo", userNo);
			List<?> list = sqlDao.select(sql, paramMap);
			if (list == null) {
				return null;
			}
			Map<String, List<String>> dataPrivilegeMap = new HashMap<String, List<String>>();
			List<String> privilegeList = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = (Map<String, Object>)list.get(i);
				Object obj = map.get("DeptNo");
				if (obj == null || "".equals(obj)) {
					continue;
				}
				privilegeList.add(String.valueOf(obj));
			}
			dataPrivilegeMap.put("deptCode", privilegeList);
			return dataPrivilegeMap;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
