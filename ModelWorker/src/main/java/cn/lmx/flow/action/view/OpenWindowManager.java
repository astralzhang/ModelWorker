package cn.lmx.flow.action.view;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.view.ViewService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("OpenWindow")
public class OpenWindowManager {
	//自定义画面
	@Resource(name="ViewService")
	private ViewService viewService;
	/**
	 * 开启自定义画面
	 * @param no
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getWindow")
	public Map<String, Object> getOpenWindow(@RequestBody Map<String, Object> paramData) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Object openWinNo = paramData.get("openWinNo");
			if (openWinNo == null || "".equals(openWinNo)) {
				throw new Exception("没有指定弹出窗口编码！");
			}
			paramData.remove("openWinNo");
			map = viewService.openWindow(userBean.getUserNo(), String.valueOf(openWinNo), paramData);			
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
