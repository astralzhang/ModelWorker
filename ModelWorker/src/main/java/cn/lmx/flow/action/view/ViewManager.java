package cn.lmx.flow.action.view;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

import cn.lmx.flow.bean.flow.AuditUserBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.view.AttachmentBean;
import cn.lmx.flow.bean.view.ViewBean;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.module.ModuleItems;
import cn.lmx.flow.service.flow.FlowService;
import cn.lmx.flow.service.view.AttachmentService;
import cn.lmx.flow.service.view.ViewService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("view")
public class ViewManager {
	@Resource(name="ViewService")
	private ViewService viewService;
	//流程
	@Resource(name="FlowService")
	private FlowService flowService;
	//附件
	@Resource(name="AttachmentService")
	private AttachmentService attachmentService;
	/**
	 * 执行存储过程
	 * @param proc
	 * @param target
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="proc")
	public Map<String, Object> runProc(@RequestBody Map<String, Object> data) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> list = viewService.runProc(userBean.getUserNo(), data.get("proc") == null ? null : String.valueOf(data.get("proc")), data.get("target") == null ? null : String.valueOf(data.get("target")), (Map<String, Object>)data.get("data"));
			map.put("data", list);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 画面初始化
	 * @param no
	 * @return
	 */
	@RequestMapping(value="/init/{no}")
	public String init(@PathVariable String no, Model model, HttpSession session) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			String token = UUID.randomUUID().toString();
			//保存Token
			session.setAttribute("token", token);
			Map<String, Object> map = viewService.list(userBean.getUserNo(), no);
			map.put("token", token);
			model.addAttribute("errType","0");
			model.addAttribute("data", map);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "view/list";
	}
	/**
	 * 画面数据查询
	 * @param no
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list/{no}")
	public Map<String, Object> query(@PathVariable String no, @RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			List<?> list = viewService.query(userBean.getUserNo(), no, map);
			result.put("errType", "0");
			result.put("data", list);
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 新增画面
	 * @param no
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/add/{no}")
	public String add(@PathVariable String no, String id, Model model, HttpSession session) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = null;
			map = viewService.getAddData(userBean.getUserNo(), no);			
			model.addAttribute("errType","0");
			Map<String, Object> dataMap = (Map<String, Object>)map.get("data");
			if (dataMap.get("headData") == null || "".equals(dataMap.get("headData"))) {
				dataMap.put("headData", "{}");
			}
			//设定Token
			String token = UUID.randomUUID().toString();
			dataMap.put("token", token);
			session.setAttribute("token", token);
			Gson gson = new Gson();
			model.addAttribute("id", id);
			model.addAttribute("viewBean", map.get("viewBean"));
			model.addAttribute("item", map.get("item"));
			model.addAttribute("module", map.get("module"));
			model.addAttribute("data", gson.toJson(dataMap));
			if (map.get("attachment") != null) {
				model.addAttribute("attachment", gson.toJson(map.get("attachment")));	
			} else {
				model.addAttribute("attachment", "[]");
			}
		} catch (Exception e) {
			model.addAttribute("id", id);
			ViewBean viewBean = new ViewBean();
			viewBean.setHeadScript("{}");
			viewBean.setDetailScript("[{}]");
			viewBean.setNo(no);
			model.addAttribute("viewBean", viewBean);
			ModuleItems item = new ModuleItems();
			model.addAttribute("item", item);
			Module module = new Module();
			model.addAttribute("module", module);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("headData", "{}");
			Gson gson = new Gson();
			model.addAttribute("data", gson.toJson(dataMap));
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
			model.addAttribute("attachment", "[]");
		}
		return "view/edit";
	}
	/**
	 * 修改画面
	 * @param no
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit/{no}")
	public String edit(@PathVariable String no, String id, Model model, HttpSession session) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = viewService.getEditData(no, id);
			model.addAttribute("errType","0");
			Map<String, Object> dataMap = (Map<String, Object>)map.get("data");
			if (dataMap.get("headData") == null || "".equals(dataMap.get("headData"))) {
				dataMap.put("headData", "{}");
			}
			Gson gson = new Gson();
			//更新Token
			String token = UUID.randomUUID().toString();
			session.setAttribute("token", token);
			dataMap.put("token", token);
			model.addAttribute("id", id);
			model.addAttribute("viewBean", map.get("viewBean"));
			model.addAttribute("item", map.get("item"));
			model.addAttribute("module", map.get("module"));
			model.addAttribute("data", gson.toJson(dataMap));
			if (map.get("attachment") != null) {
				model.addAttribute("attachment", gson.toJson(map.get("attachment")));	
			} else {
				model.addAttribute("attachment", "[]");
			}
		} catch (Exception e) {
			model.addAttribute("id", id);
			ViewBean viewBean = new ViewBean();
			viewBean.setHeadScript("{}");
			viewBean.setDetailScript("[{}]");
			viewBean.setNo(no);
			model.addAttribute("viewBean", viewBean);
			ModuleItems item = new ModuleItems();
			model.addAttribute("item", item);
			Module module = new Module();
			model.addAttribute("module", module);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("headData", "{}");
			Gson gson = new Gson();
			model.addAttribute("data", gson.toJson(dataMap));
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
			model.addAttribute("attachment", "[]");
		}
		return "view/edit";
	}
	/**
	 * 查看画面
	 * @param no
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/view/{no}")
	public String view(@PathVariable String no, String id, Model model, HttpSession session) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = null;
			if (id == null || "".equals(id)) {
				map = viewService.getAddData(userBean.getUserNo(), no);
			} else {
				map = viewService.getEditData(no, id);
			}
			model.addAttribute("errType","0");
			Map<String, Object> dataMap = (Map<String, Object>)map.get("data");
			if (dataMap.get("headData") == null || "".equals(dataMap.get("headData"))) {
				dataMap.put("headData", "{}");
			}
			//设定Token
			String token = UUID.randomUUID().toString();
			dataMap.put("token", token);
			session.setAttribute("token", token);
			Gson gson = new Gson();
			model.addAttribute("id", id);
			model.addAttribute("viewBean", map.get("viewBean"));
			model.addAttribute("item", map.get("item"));
			model.addAttribute("module", map.get("module"));
			model.addAttribute("data", gson.toJson(dataMap));
			if (map.get("attachment") != null) {
				model.addAttribute("attachment", gson.toJson(map.get("attachment")));	
			} else {
				model.addAttribute("attachment", "[]");
			}	
		} catch (Exception e) {
			model.addAttribute("id", id);
			ViewBean viewBean = new ViewBean();
			viewBean.setHeadScript("{}");
			viewBean.setDetailScript("[{}]");
			viewBean.setNo(no);
			model.addAttribute("viewBean", viewBean);
			ModuleItems item = new ModuleItems();
			model.addAttribute("item", item);
			Module module = new Module();
			model.addAttribute("module", module);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("headData", "{}");
			Gson gson = new Gson();
			model.addAttribute("data", gson.toJson(dataMap));
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
			model.addAttribute("attachment", "[]");
		}
		return "view/view";
	}
	/**
	 * 保存数据
	 * @param no
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save/{no}")
	public Map<String, Object> saveData(@PathVariable String no, @RequestBody Map<String, Object> data, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			//验证token
			String token = (String)session.getAttribute("token");
			if (token != null && !"".equals(token)) {
				String viewToken = data.get("token") == null ? "" : String.valueOf(data.get("token"));
				if (!viewToken.equals(token)) {
					//画面重复提交
					throw new Exception("请勿重复提交画面！");
				}
			}
			token = UUID.randomUUID().toString();
			session.setAttribute("token", token);
			map.put("token", token);
			//去除Token
			data.remove("token");
			viewService.saveData(userBean.getUserNo(), no, data);
			map.put("errType", "0");
			map.put("id", data.get("id"));
			map.put("errMessage", "保存成功！");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 删除数据
	 * @param no
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete/{no}")
	public Map<String, Object> deleteData(@PathVariable String no, String[] ids) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			viewService.deleteData(userBean.getUserNo(), no, ids);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}		
		return map;
	}
	/**
	 * 文件上传
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/upload/{no}")
	public Map<String, Object> uploadFile(HttpServletRequest request, @PathVariable String no, @RequestParam("importFile") MultipartFile importFile, @RequestParam("id") String id, @RequestParam("dataId") String dataId, @RequestParam("maxSeqNo") String maxSeqNo, @RequestParam("token") String token, HttpSession session) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//check画面重复提交
			String sessionToken = (String)session.getAttribute("token");
			if (sessionToken != null && !"".equals(sessionToken)) {
				if (!sessionToken.equals(token)) {
					//画面重复提交
					throw new Exception("请勿重复提交！");
				}
			}
			sessionToken = UUID.randomUUID().toString();
			session.setAttribute("token", sessionToken);
			result.put("token", sessionToken);
			if (request instanceof MultipartHttpServletRequest) {
				Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
				Iterator<Entry<String, MultipartFile>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, MultipartFile> entry = it.next();
					MultipartFile file = entry.getValue();
					String fileName = file.getOriginalFilename();
					int lastIndex = fileName.lastIndexOf(".");
					String extendName = "";
					if (lastIndex >= 0) {
						extendName = fileName.substring(lastIndex + 1);
					}
					AttachmentBean bean = new AttachmentBean();
					bean.setDataId(dataId);
					bean.setFileContent(file.getBytes());
					bean.setFileExtend(extendName);
					bean.setFileName(fileName);
					bean.setId(id);
					bean.setViewNo(no);
					result.put("data", attachmentService.saveFile(userBean.getUserNo(), bean, maxSeqNo));
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
	 * 删除附件
	 * @param no
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteAttachment/{no}")
	public Map<String, Object> deleteFile(@PathVariable String no, String id) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			attachmentService.deleteFile(userBean.getUserNo(), id);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 附件预览
	 * @param no
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/viewAttachment/{no}")
	public Object viewFile(@PathVariable String no, String id, int pageNo, HttpServletRequest request, HttpServletResponse response) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(pageNo);
			map = attachmentService.viewFile(userBean.getUserNo(), id, pageNo);
			if ("jpg".equals(String.valueOf(map.get("fileExtend")).toLowerCase())
					|| "png".equals(String.valueOf(map.get("fileExtend")).toLowerCase())
					|| "gif".equals(String.valueOf(map.get("fileExtend")).toLowerCase())
					|| "pdf".equals(String.valueOf(map.get("fileExtend")).toLowerCase())) {
				map.put("errType", "0");
				return map;
			//} else if ("pdf".equals(String.valueOf(map.get("fileExtend")))) {
			//	return map.get("fileContent");
			} else {
				OutputStream out = response.getOutputStream();
				response.setContentType("application/x-download");
				String agent = (String)request.getHeader("USER-AGENT");
				String downloadFileName = String.valueOf(map.get("fileName"));
	            if(agent != null && agent.toLowerCase().indexOf("firefox") > 0)
	            {
	            	downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(String.valueOf(map.get("fileName")).getBytes("UTF-8")))) + "?=";
	            } else {
	            	downloadFileName = URLEncoder.encode(String.valueOf(map.get("fileName")), "UTF-8");
	            }
	            response.setHeader("Content-Disposition", "attachment;fileName="+ downloadFileName);
				out.write((byte[])map.get("fileContent"));          
			}
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 提交表单至工作流
	 * @param no
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/submit/{no}")
	public Map<String, Object> submitData(@PathVariable String no, String[] ids, String token, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionToken1 = UUID.randomUUID().toString();
		try {
			//check画面重复提交
			String sessionToken = (String)session.getAttribute("token");
			session.setAttribute("token", sessionToken1);
			if (sessionToken != null && !"".equals(sessionToken)) {
				if (!sessionToken.equals(token)) {
					//画面重复提交
					throw new Exception("请勿重复提交画面！");
				}
			}
			map = viewService.getViewByNo(no);
			ViewBean viewBean = (ViewBean)map.get("viewBean");
			if (viewBean.getVoucherType() == null || "".equals(viewBean.getVoucherType())) {
				throw new Exception("没有指定单据类型，无法提交工作流！");
			}
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			flowService.submit(userBean.getUserNo(), viewBean.getVoucherType(), ids, no);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		} finally {
			map.put("token", sessionToken1);
		}
		return map;
	}
	/**
	 * 撤销提交
	 * @param no
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/cancelSubmit/{no}")
	public Map<String, Object> cancelSubmitData(@PathVariable String no, String token, String[] ids, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sessionToken1 = UUID.randomUUID().toString();
		try {
			//check画面重复提交
			String sessionToken = (String)session.getAttribute("token");
			session.setAttribute("token", sessionToken1);
			if (sessionToken != null && !"".equals(sessionToken)) {
				if (!sessionToken.equals(token)) {
					throw new Exception("请勿重复提交画面！");
				}
			}
			map = viewService.getViewByNo(no);
			ViewBean viewBean = (ViewBean)map.get("viewBean");
			if (viewBean.getVoucherType() == null || "".equals(viewBean.getVoucherType())) {
				throw new Exception("没有指定单据类型，无法撤销工作流！");
			}
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			StringBuffer dataKeys = new StringBuffer("");
			for (int i = 0; i < ids.length; i++) {
				if (dataKeys.length() > 0) {
					dataKeys.append(",");
				}
				dataKeys.append(ids[i]);
			}
			flowService.submitCancel(userBean.getUserNo(), dataKeys.toString(), "dataKey");
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		} finally {
			map.put("token", sessionToken1);
		}
		return map;
	}
	/**
	 * 单据查审
	 * @param no
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/viewAudit/{no}")
	public Map<String, Object> viewAudit(@PathVariable String no, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//取得单据查审信息
			map.put("data", flowService.viewAudit(id, "dataKey"));
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 审核单据
	 * @param no
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/audit/{no}")
	public Map<String, Object> auditData(@PathVariable String no, String[] ids, String token, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//check画面重复提交
			String sessionToken = (String)session.getAttribute("token");
			if (sessionToken != null && !"".equals(sessionToken)) {
				if (!sessionToken.equals(token)) {
					throw new Exception("请勿重复提交画面！");
				}
			}
			sessionToken = UUID.randomUUID().toString();
			session.setAttribute("token", sessionToken);
			map.put("token", sessionToken);
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			viewService.auditData(userBean.getUserNo(), no, ids);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 撤销审核
	 * @param no
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/cancelAudit/{no}")
	public Map<String, Object> cancelAuditData(@PathVariable String no, String[] ids, String token, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//check画面重复提交
			String sessionToken = (String)session.getAttribute("token");
			if (sessionToken != null && !"".equals(sessionToken)) {
				if (!sessionToken.equals(token)) {
					throw new Exception("请勿重复提交画面！");
				}
			}
			sessionToken = UUID.randomUUID().toString();
			session.setAttribute("token", sessionToken);
			map.put("token", sessionToken);
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			viewService.cancelAuditData(userBean.getUserNo(), no, ids);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 取得下拉列表框数据
	 * @param sql
	 * @param target
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/dropdown")
	public Map<String, Object> dropdownList(String sql, String target) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (sql == null) {
				throw new Exception("没有指定下拉框的取得SQL!");
			}
			List<?> list = viewService.dropdownList(userBean.getUserNo(), sql, target);
			map.put("errType", "0");
			map.put("data", list);
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 自定义按钮处理
	 * @param no
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/process_button/{no}")
	public Map<String, Object> processButton(@PathVariable String no, @RequestBody Map<String, Object> data, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			//验证token
			String token = (String)session.getAttribute("token");
			if (token != null && !"".equals(token)) {
				String viewToken = data.get("token") == null ? "" : String.valueOf(data.get("token"));
				if (!viewToken.equals(token)) {
					//画面重复提交
					throw new Exception("请勿重复提交画面！");
				}
			}
			token = UUID.randomUUID().toString();
			session.setAttribute("token", token);
			map.put("token", token);
			//去除Token
			data.remove("token");
			String buttonId = data.get("userDefinedButton") == null ? "" : String.valueOf(data.get("userDefinedButton"));
			//去除自定义按钮
			data.remove("userDefinedButton");
			map.put("data",viewService.processButton(userBean.getUserNo(), no, buttonId, data));
			map.put("errType", "0");
			map.put("id", data.get("id"));
			map.put("errMessage", "保存成功！");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
