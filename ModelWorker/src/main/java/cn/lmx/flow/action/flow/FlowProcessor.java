package cn.lmx.flow.action.flow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.flow.AuditUserBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.flow.FlowService;
import cn.lmx.flow.utils.UserInfo;
import sun.misc.BASE64Decoder;

@Controller
@RequestMapping("/flowProcessor")
public class FlowProcessor {
	//流程
	@Resource(name="FlowService")
	private FlowService flowService;
	/**
	 * 流程提交
	 * @param voucherType
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/submit")
	@ResponseBody
	public Map<String, Object> submit(String voucherType, String[] dataKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			//单据提交
			flowService.submit(userBean.getUserNo(), voucherType, dataKey, "");
			map.put("errType", "0");
			map.put("errMessage", "提交成功！");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 查审
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/viewAudit")
	public Map<String, Object> viewAudit(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<AuditUserBean> list = flowService.viewAudit(id, "");
			map.put("errType", "0");
			map.put("data", list);
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 取得单据画面
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/view")
	@ResponseBody
	public Map<String, Object> view(String id, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = flowService.view(id, status);
			map.put("errType", "0");
			//map.put("view", viewData);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 审核通过
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="audit")
	@ResponseBody
	public Map<String, Object> audit(String ids, String processContent) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			flowService.audit(userBean.getUserNo(), ids, processContent);
			map.put("errType", "0");
			map.put("errMessage", "审核成功！");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 审核不同意
	 * @param ids
	 * @param processContent
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/auditDisagree")
	public Map<String, Object> auditDisagree(String ids, String processContent) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			flowService.auditDisagree(userBean.getUserNo(), ids, processContent);
			map.put("errType", "0");
			map.put("errMessage", "处理成功！");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 审核取消
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/auditCancel")
	public Map<String, Object> auditCancel(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			flowService.auditCancel(userBean.getUserNo(), ids);
			map.put("errType", "0");
			map.put("errMessage", "处理成功！");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 撤销提交
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/submitCancel")
	public Map<String, Object> submitCancel(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			flowService.submitCancel(userBean.getUserNo(), ids, "message");
			map.put("errType", "0");
			map.put("errMessage", "处理成功！");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 取得用户一栏
	 * @param id
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/userList")
	public Map<String, Object> userList(String id, @RequestParam Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			map.remove("id");
			Map<String, Object> dataMap = flowService.getUserList(userBean.getUserNo(), id, map);
			if (dataMap != null) {
				result.put("data", dataMap.get("data"));
				result.put("showFields", dataMap.get("showFields"));
				result.put("conditionFields", dataMap.get("conditionFields"));
			}
			result.putAll(map);
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 审核加签处理
	 * @param id
	 * @param userNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addAuditUser")
	public Map<String, Object> addAuditUser(String id, String userNo) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			flowService.addUserAudit(userBean.getUserNo(), id, userNo);
			result.put("errType", "0");
			result.put("errMessage", "加签成功！");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 手写签名
	 * @param request
	 * @param data
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveSign")
	public Map<String, Object> saveSign(HttpServletRequest request, String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println(request.getParameter("data"));
		String serverPath = request.getSession().getServletContext().getRealPath("/");
		BASE64Decoder base64 = new BASE64Decoder();
		try {
			//注意点：实际的图片数据是从 data:image/jpeg;base64, 后开始的  
			byte[] k = base64.decodeBuffer(data.substring("data:image/png;base64,".length()));
			InputStream is = new ByteArrayInputStream(k);
			String fileName = UUID.randomUUID().toString();
			String imgFilePath = serverPath + "\\static\\usertemp\\" + fileName + ".png"; 
			BufferedImage img = ImageIO.read(is);
			File file = new File(imgFilePath);
			ImageIO.write(img, "png", file);
			map.put("fileName", fileName);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
