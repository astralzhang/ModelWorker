package cn.lmx.flow.action.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.view.ReportDesignBean;
import cn.lmx.flow.entity.view.ReportDesign;
import cn.lmx.flow.service.view.ReportDesignService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/ReportDesign")
public class ReportDesignManager {
	//报表设计
	@Resource(name="ReportDesignService")
	private ReportDesignService reportDesignService;
	/**
	 * 报表设计一栏
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model, String no, String name) {
		try {
			Map<String, Object> map = reportDesignService.list(no, name);
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/ReportList";
	}
	/**
	 * 报表新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model, String reportNo, String reportName, String moduleNo) {
		try {
			Map<String, Object> map = reportDesignService.getModule();
			ReportDesign design = new ReportDesign();
			design.setViewScript("{}");
			map.put("report", design);
			model.addAttribute("data", map);
			model.addAttribute("reportNo", reportNo);
			model.addAttribute("reportName", reportName);
			model.addAttribute("moduleNo", moduleNo);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/ReportEdit";
	}
	/**
	 * 报表修改
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(Model model, String id) {
		try {
			Map<String, Object> map = reportDesignService.editReport(id);
			model.addAttribute("data", map);
			ReportDesign design = (ReportDesign)map.get("report");
			model.addAttribute("reportNo", design.getNo());
			model.addAttribute("reportName", design.getName());
			model.addAttribute("moduleNo", design.getModuleNo());
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "ViewDesign/ReportEdit";
	}
	/**
	 * 保存报表
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody ReportDesignBean bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			reportDesignService.saveReport(userBean.getUserNo(), bean);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 发布
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/publish")
	public Map<String, Object> publish(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			reportDesignService.publishReport(userBean.getUserNo(), id);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 取得模板
	 * @param reportNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTemplate")
	public Map<String, Object> getTemplate(String reportNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> templateList = reportDesignService.getTemplate(reportNo);
			map.put("data", templateList);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 上传模板
	 * @param reportNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload/{no}")
	public Map<String, Object> uploadTemplate(@PathVariable String no, HttpServletRequest request) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
				Iterator<Entry<String, MultipartFile>> it = map.entrySet().iterator();
				String path = request.getSession().getServletContext().getRealPath("");
				while (it.hasNext()) {
					Entry<String, MultipartFile> entry = it.next();
					MultipartFile file = entry.getValue();
					reportDesignService.upload(userBean.getUserNo(), path, no, file);
				}
			}
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 删除模板
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteTemplate")
	public Map<String, Object> deleteTemplate(String id) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			reportDesignService.deleteTemplate(userBean.getUserNo(), id);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
