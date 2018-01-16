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
import cn.lmx.flow.bean.view.SyncDataDesignBean;
import cn.lmx.flow.service.view.SyncDataDesignService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("SyncDesign")
public class SyncDesignManager {
	//同步
	@Resource(name="SyncDataDesignService")
	private SyncDataDesignService syncDataDesignService;
	@RequestMapping(value="/list")
	public String list(String no, String status, Model model) {
		try {
			Map<String, Object> map = syncDataDesignService.list(no, status);
			model.addAttribute("errType", "0");
			model.addAttribute("data", map);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		model.addAttribute("no", no);
		model.addAttribute("status", status);
		return "ViewDesign/SyncDataList";
	}
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("config", "{}");
		model.addAttribute("data", data);
		return "ViewDesign/SyncDataEdit";
	}
	/**
	 * 修改
	 * @param no
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public String edit(String no, Model model) throws Exception {
		try {
			Map<String, Object> map = syncDataDesignService.list(no, null);
			List<SyncDataDesignBean> list = (List<SyncDataDesignBean>)map.get("Design");
			if (list == null || list.size() <= 0) {
				throw new Exception("没有找到对应的同步信息，请刷新页面后重试！");
			}
			SyncDataDesignBean bean = list.get(0);
			model.addAttribute("data", bean);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/SyncDataEdit";
	}
	/**
	 * 保存
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody SyncDataDesignBean bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			syncDataDesignService.save(userBean.getUserNo(), bean);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 信息发布
	 * @param no
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/publish")
	public Map<String, Object> publish(String no, String groupNo) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			syncDataDesignService.publish(userBean.getUserNo(), no, groupNo);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
