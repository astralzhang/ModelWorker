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
import cn.lmx.flow.dao.module.GroupDao;
import cn.lmx.flow.dao.module.GroupUserDao;
import cn.lmx.flow.dao.module.UserDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.entity.module.Group;
import cn.lmx.flow.entity.module.GroupUser;
import cn.lmx.flow.entity.module.User;
import cn.lmx.flow.entity.system.DataLogs;

@Repository("GroupService")
public class GroupService {
	//工作组
	@Resource(name="GroupDao")
	private GroupDao groupDao;
	//用户
	@Resource(name="UserDao")
	private UserDao userDao;
	//Log
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	//用户组
	@Resource(name="GroupUserDao")
	private GroupUserDao groupUserDao;
	/**
	 * 工作组一栏
	 * @param groupNo
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String groupNo, String groupName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> list = groupDao.listByNoName(groupNo, groupName);
			if (list == null || list.size() <= 0) {
				return null;
			}
			List<GroupBean> rstList = new ArrayList<GroupBean>();
			for(int i = 0; i < list.size(); i++) {
				Group group = (Group)list.get(i);
				GroupBean bean = new GroupBean();
				BeanUtils.copyProperties(group, bean);
				rstList.add(bean);
			}
			map.put("groupList", rstList);
			//取得用户一栏
			List<?> userList = userDao.list();
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			if (userList != null) {
				for (int i = 0; i < userList.size(); i++) {
					User user = (User)userList.get(i);
					UserBean bean = new UserBean();
					BeanUtils.copyProperties(user, bean);
					userBeanList.add(bean);
				}
			}
			map.put("userList", userBeanList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 根据编码取得组信息
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public GroupBean getByNo(String groupNo) throws Exception {
		try {
			if (groupNo == null || "".equals(groupNo)) {
				throw new Exception("没有指定组编号！");
			}
			Group group = groupDao.getGroupByNo(groupNo);
			if (group == null) {
				throw new Exception("指定的组不存在！");
			}
			GroupBean groupBean = new GroupBean();
			BeanUtils.copyProperties(group, groupBean);
			return groupBean;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 根据组编码取得用户列表
	 * @param groupNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<UserBean> getUserByGroupNo(String groupNo) throws Exception {
		try {
			List<?> list = groupUserDao.getUserByGroupNo(groupNo);
			if (list == null || list.size() <= 0) {
				return null;
			}
			List<UserBean> userList = new ArrayList<UserBean>();
			for (int i = 0; i < list.size(); i++) {
				GroupUser groupUser = (GroupUser)list.get(i);
				UserBean bean = new UserBean();
				bean.setUserNo(groupUser.getUserNo());
				userList.add(bean);
			}
			return userList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 保存组
	 * @param systemUser
	 * @param groupBean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void save(String systemUser, GroupBean groupBean) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			Group group = groupDao.getGroupByNo(groupBean.getGroupNo());
			if (group == null) {
				//新增组
				group = new Group();
				BeanUtils.copyProperties(groupBean, group);
				group.setCreateUser(systemUser);
				group.setCreateTime(sdf.format(c.getTime()));
				groupDao.add(group);
			} else {
				//写修改Log
				Gson gson = new Gson();
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setBeforeData(gson.toJson(group));
				//修改
				BeanUtils.copyProperties(groupBean, group);
				group.setUpdateUser(systemUser);
				group.setUpdateTime(sdf.format(c.getTime()));
				groupDao.edit(group);
				//
				dataLog.setDataTable("Groups");
				dataLog.setAfterData(gson.toJson(group));
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
	 * 删除组
	 * @param groupNos
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void delete(String systemUser, String groupNos) throws Exception {
		try {
			if (groupNos == null || "".equals(groupNos)) {
				throw new Exception("没有指定组编号！");
			}
			String[] arrGroup = groupNos.split(",");
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			for (int i = 0; i < arrGroup.length; i++) {
				String groupNo = arrGroup[i];
				if ("system".equals(groupNo)) {
					throw new Exception("不能删除系统组！");
				}
				Group group = groupDao.getGroupByNo(groupNo);
				if (group == null) {
					throw new Exception("指定的组不存在！");
				}
				Gson gson = new Gson();
				groupDao.delete(group);
				//写日志
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Delete");
				dataLog.setProcessor("删除工作组");
				dataLog.setDataTable("Groups");
				dataLog.setBeforeData(gson.toJson(group));
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
				List<?> groupUserList = groupUserDao.getUserByGroupNo(groupNo);
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
					groupUserDao.deleteByGroupNo(groupNo);
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
	 * 加入用户组
	 * @param systemUser
	 * @param groupNo
	 * @param userNos
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addUsers(String systemUser, String groupNo, String userNos) throws Exception {
		try {
			//取得用户组
			List<?> userList = groupUserDao.getUserByGroupNo(groupNo);
			Gson gson = new Gson();
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			if (userNos == null || "".equals(userNos)) {
				//根据组编码删除
				groupUserDao.deleteByGroupNo(groupNo);
				//写log
				if (userList != null) {
					for (int i = 0; i < userList.size(); i++) {
						GroupUser groupUser = (GroupUser)userList.get(i);
						DataLogs dataLog = new DataLogs();
						dataLog.setId(UUID.randomUUID().toString());
						dataLog.setType("Delete");
						dataLog.setDataTable("GroupUser");
						dataLog.setProcessor("删除工作组中所有用户！");
						dataLog.setBeforeData(gson.toJson(groupUser));
						dataLog.setCreateUser(systemUser);
						dataLog.setCreateTime(sdf.format(c.getTime()));
						dataLogDao.add(dataLog);
					}
				}
			} else {
				String[] arrUser = userNos.split(",");
				for (int i = 0; i < arrUser.length; i++) {
					String userNo = arrUser[i];
					boolean hasData = false;
					for (int j = 0; j < userList.size(); j++) {
						GroupUser groupUser = (GroupUser)userList.get(j);
						if (userNo.equals(groupUser.getUserNo())) {
							//数据存在
							userList.remove(j);
							hasData = true;
							break;
						}
					}
					if (hasData == false) {
						GroupUser groupUser = new GroupUser();
						groupUser.setId(UUID.randomUUID().toString());
						groupUser.setGroupNo(groupNo);
						groupUser.setUserNo(userNo);
						groupUser.setCreateUser(systemUser);
						groupUser.setCreateTime(sdf.format(c.getTime()));
						groupUserDao.add(groupUser);
						DataLogs dataLog = new DataLogs();
						dataLog.setId(UUID.randomUUID().toString());
						dataLog.setDataTable("GroupUser");
						dataLog.setType("Add");
						dataLog.setProcessor("工作组中新增用户！");
						dataLog.setAfterData(gson.toJson(groupUser));
						dataLog.setCreateUser(systemUser);
						dataLog.setCreateTime(sdf.format(c.getTime()));
						dataLogDao.add(dataLog);
					}
				}
				//删除用户
				for (int i = 0; i < userList.size(); i++) {
					GroupUser groupUser = (GroupUser)userList.get(i);
					groupUserDao.delete(groupUser);
					DataLogs dataLog = new DataLogs();
					dataLog.setId(UUID.randomUUID().toString());
					dataLog.setType("Delete");
					dataLog.setDataTable("GroupUser");
					dataLog.setProcessor("删除工作组中用户！");
					dataLog.setBeforeData(gson.toJson(groupUser));
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
}
