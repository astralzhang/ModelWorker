package cn.lmx.flow.action.desktop;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.module.DesktopService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping(value="/desktop")
public class DesktopManager {
	//桌面
	@Resource(name="DesktopService")
	private DesktopService desktopService;
	/**
	 * 取得我的桌面
	 * @return
	 */
	@RequestMapping(value="/desktop")
	public String list(Model model) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = desktopService.list(userBean.getUserNo());
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "desktop";
	}
	/**
	 * 保存我的桌面
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			desktopService.save(userBean.getUserNo(), ids);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
