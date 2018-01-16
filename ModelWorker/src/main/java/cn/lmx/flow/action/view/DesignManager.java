package cn.lmx.flow.action.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.flow.VoucherTypeBean;
import cn.lmx.flow.bean.module.MenuBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.view.UserDefineViewBean;
import cn.lmx.flow.entity.view.PublishedViews;
import cn.lmx.flow.service.flow.VoucherTypeService;
import cn.lmx.flow.service.module.ModuleService;
import cn.lmx.flow.service.view.UserDefineViewService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/viewDesign")
public class DesignManager {
	//单据类型
	@Resource(name="VoucherTypeService")
	private VoucherTypeService voucherTypeService;
	//用户自定义画面
	@Resource(name="UserDefineViewService")
	private UserDefineViewService userDefineViewService;
	//功能模块
	@Resource(name="ModuleService")
	private ModuleService moduleService;
	/**
	 * 防止Session过期处理
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/refresh")
	public Map<String, Object> reflesh() {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("refresh。。。。。。。。。。。");
		return map;
	}
	/**
	 * 自定义画面一栏
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(String viewNo, Model model) {		
		try {
			//单据类型一栏
			List<VoucherTypeBean> typeList = voucherTypeService.list();
			model.addAttribute("voucherTypeList", typeList);
			//自定义画面一栏
			List<UserDefineViewBean> list = userDefineViewService.getLastVersionViewList(viewNo, "");
			model.addAttribute("resultList", list);
			UserDefineViewBean bean = new UserDefineViewBean();
			bean.setNo(viewNo);
			//取得功能模块一栏
			List<MenuBean> moduleList = moduleService.listModule();
			model.addAttribute("viewBean", bean);
			model.addAttribute("moduleList", moduleList);
		} catch (Exception e) {
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/list";
	}
	/**
	 * 取得指定画面的发布模块
	 * @param viewNo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="getModule")
	public Map<String, Object> getPublishedModule(String viewNo) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			PublishedViews publishedViews = userDefineViewService.getPublishedView(viewNo);
			result.put("errType", "0");
			result.put("moduleNo", publishedViews.getModuleNo());
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 新增画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="edit")
	public String add(Model model, String viewNo, String viewName, String voucherType, String voucherTypeName) {
		try {
			UserDefineViewBean viewBean = null;
			if (voucherType != null && !"".equals(voucherType)) {
				//新增
				viewBean = new UserDefineViewBean();
				viewBean.setNo(viewNo);
				viewBean.setName(viewName);
				viewBean.setVoucherType(voucherType);
				viewBean.setVoucherTypeName(voucherTypeName);
				viewBean.setHeadScript("");
				viewBean.setDetailScript("[{label:'" + viewName + "',table:'',process:{type:'S',sql:'',fields:[]}}]");
				viewBean.setListScript("{}");
				//model.addAttribute("voucherNo", viewNo);
				//model.addAttribute("voucherName", viewName);
				//model.addAttribute("voucherType", voucherType);
				//model.addAttribute("voucherTypeName", voucherTypeName);
			} else {
				//修改
				List<UserDefineViewBean> list = userDefineViewService.getLastVersionViewList(viewNo, "");
				if (list != null && list.size() > 0) {
					viewBean = list.get(0);
				}
			}
			model.addAttribute("viewBean", viewBean);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}		
		return "ViewDesign/edit";
	}
	/**
	 * 保存自定义画面
	 * @param no
	 * @param name
	 * @param voucherType
	 * @param headScript
	 * @param detailScript
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> saveDesignView(String no, String name, String voucherType, String listScript, String headScript, String detailScript) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userDefineViewService.saveUserDefineView(userBean.getUserNo(), no, name, voucherType, listScript, headScript, detailScript);
			map.put("errType", "0");
			map.put("errMessage", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 画面发布
	 * @param viewNo
	 * @param moduleNo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/publish")
	public Map<String, Object> publish(String viewNo, String moduleNo, String modules) throws Exception {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			userDefineViewService.publishView(userBean.getUserNo(), viewNo, moduleNo, modules);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
