package cn.lmx.flow.action.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.view.OpenWindowBean;
import cn.lmx.flow.service.view.OpenWindowService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/openWinDesign")
public class OpenWinDesignManager {
	//弹窗
	@Resource(name="OpenWindowService")
	private OpenWindowService openWindowService;
	/**
	 * 弹窗列表
	 * @param no
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(String no, String name, Model model) {
		try {
			List<OpenWindowBean> list = openWindowService.list(no, name);
			model.addAttribute("errType", "0");
			model.addAttribute("data", list);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		model.addAttribute("no", no);
		model.addAttribute("name", name);
		return "ViewDesign/OpenWinList";
	}
	/**
	 * 取得弹窗列表
	 * @param no
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/listOpenWin")
	public Map<String, Object> list(String no, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<OpenWindowBean> list = openWindowService.list(no, name);
			map.put("errType", "0");
			map.put("data", list);
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 弹窗新增
	 * @param no
	 * @return
	 */
	@RequestMapping(value="add")
	public String add(Model model) {
		try {
			OpenWindowBean bean = new OpenWindowBean();
			bean.setCondition("[]");
			bean.setViewField("[]");
			model.addAttribute("errType", "0");
			model.addAttribute("data", bean);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/OpenWinEdit";
	}
	/**
	 * 弹窗编辑
	 * @param no
	 * @return
	 */
	@RequestMapping(value="edit")
	public String edit(String no, Model model) {
		try {
			OpenWindowBean bean = null;
			if (no != null && !"".equals(no)) {
				bean = openWindowService.getByNo(no);
			} else {
				bean = new OpenWindowBean();
				bean.setCondition("[]");
				bean.setViewField("[]");
			}
			model.addAttribute("errType", "0");
			model.addAttribute("data", bean);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/OpenWinEdit";
	}
	/**
	 * 保存弹窗
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="save")
	public Map<String, Object> save(@RequestBody OpenWindowBean bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			openWindowService.saveOpenWin(userBean.getUserNo(), bean);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
