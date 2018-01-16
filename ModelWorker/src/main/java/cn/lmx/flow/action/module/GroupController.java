package cn.lmx.flow.action.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.GroupBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.module.GroupService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/group")
public class GroupController {
	//工作组
	@Resource(name="GroupService")
	private GroupService groupService;
	/**
	 * 工作组一栏画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model, String groupNo, String groupName) {
		try {
			Map<String, Object> map = groupService.list(groupNo, groupName);
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "group/list";
	}
	/**
	 * 工作组新增画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model) {
		return "group/edit";
	}
	/**
	 * 工作组修改画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(Model model, String groupNo) {
		try {
			GroupBean bean = groupService.getByNo(groupNo);
			model.addAttribute("data", bean);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "group/edit";
	}
	/**
	 * 删除组
	 * @param groupNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public Map<String, Object> delete(String groupNos) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			groupService.delete(userBean.getUserNo(), groupNos);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 保存
	 * @param groupBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody GroupBean groupBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			groupService.save(userBean.getUserNo(), groupBean);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 根据组编码取得用户
	 * @param groupNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUser")
	public Map<String, Object> getUserByGroupNo(String groupNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<UserBean> list = groupService.getUserByGroupNo(groupNo);
			map.put("data", list);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 加入用户至工作组
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addUser")
	public Map<String, Object> addUser(String groupNo, String userNos) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			groupService.addUsers(userBean.getUserNo(), groupNo, userNos);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
