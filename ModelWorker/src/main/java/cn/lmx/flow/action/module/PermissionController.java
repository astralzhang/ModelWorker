package cn.lmx.flow.action.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.module.PermissionService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	@Resource(name="PermissionService")
	private PermissionService permissionService;
	/**
	 * 权限设定
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Model model, String userType, String userNo, String userName) {
		try {
			Map<String, Object> map = permissionService.list(userType, userNo, userName);
			model.addAttribute("errType", "0");
			model.addAttribute("data", map);
			model.addAttribute("userType", userType);
			model.addAttribute("userNo", userNo);
			model.addAttribute("userName", userName);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "permission/list";
	}
	/**
	 * 取得用户权限
	 * @param type
	 * @param userNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPermission")
	public Map<String, Object> getPermission(String type, String userNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> list = permissionService.getPermission(type, userNo);
			map.put("data", list);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 保存权限
	 * @param userType
	 * @param userNo
	 * @param modules
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> savePermission(String userType, String userNo, String modules) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			permissionService.savePermission(userBean.getUserNo(), userType, userNo, modules);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
