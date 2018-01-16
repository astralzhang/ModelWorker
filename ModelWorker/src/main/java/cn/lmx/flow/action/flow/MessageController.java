package cn.lmx.flow.action.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.flow.VoucherTypeBean;
import cn.lmx.flow.bean.message.AuditMessageBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.flow.VoucherTypeService;
import cn.lmx.flow.service.message.MessageService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/message")
public class MessageController {
	//审批消息
	@Resource(name="MessageService")
	private MessageService messageService;
	//单据类型
	@Resource(name="VoucherTypeService")
	private VoucherTypeService voucherTypeService;
	/**
	 * 需审批一栏
	 * @return
	 */
	@RequestMapping(value="/auditList")
	public String auditList(Model model, String voucherType, String status, String startDate, String endDate, String auditMessage) {
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			List<AuditMessageBean> list = null;
			if (status == null || "".equals(status) || "0".equals(status)) {
				//未审批
				list = messageService.getAuditList(userBean.getUserNo(), voucherType, status, auditMessage);
			} else if ("2".equals(status)) {
				//我同意
				list = messageService.getAuditedList(userBean.getUserNo(), voucherType, status, startDate, endDate, auditMessage);
			} else if ("1".equals(status)) {
				//我提交
				list = messageService.getSubmitedList(userBean.getUserNo(), voucherType, startDate, endDate, auditMessage);
			} else if ("3".equals(status)) {
				//我否决
				list = messageService.getAuditedList(userBean.getUserNo(), voucherType, status, startDate, endDate, auditMessage);
			}
			model.addAttribute("errType", "0");
			model.addAttribute("data", list);
			//取得单据类型一栏
			List<VoucherTypeBean> typeList = voucherTypeService.list();
			model.addAttribute("voucherTypeList", typeList);
			model.addAttribute("voucherType", voucherType);
			model.addAttribute("status", status);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("auditMessage", auditMessage);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "message/auditList";
	}
	/**
	 * 取得需审核一栏数据
	 * @param voucherType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/auditListByAjax")
	public Map<String, Object> getAuditList(String voucherType, String auditMessage) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			List<AuditMessageBean> list = messageService.getAuditList(userBean.getUserNo(), voucherType, "0", auditMessage);
			map.put("errType", "0");
			map.put("data", list);
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
