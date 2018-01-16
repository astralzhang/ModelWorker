package cn.lmx.flow.action.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.MenuBean;
import cn.lmx.flow.service.module.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuController {
	//菜单
	@Resource(name="MenuService")
	private MenuService menuService;
	/**
	 * 取得系统菜单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list")
	public Map<String, Object> list() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<MenuBean> menuList = menuService.getMenu();
			result.put("errType", "0");
			result.put("data", menuList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("errType", "0");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
}
