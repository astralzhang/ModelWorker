package cn.lmx.flow.action.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.message.AuditMessageBean;
import cn.lmx.flow.service.flow.FlowService;
import cn.lmx.flow.service.message.MessageService;

@Controller
@RequestMapping("/flowApi")
public class FlowApi {
	//流程管理
	@Resource(name="FlowService")
	private FlowService flowService;
	//消息
	@Resource(name="MessageService")
	private MessageService messageService;
	/**
	 * 单据提交
	 * @param voucherType
	 * @param dataKey
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/submit")
	public Map<String, String> submit(String systemUser, String voucherType, String[] dataKey) throws Exception {
		Map<String, String> rstMap = new HashMap<String, String>();
		try {
			flowService.submit(systemUser, voucherType, dataKey, "");
		} catch (Exception e) {
			rstMap.put("errType", "1");
			rstMap.put("errMessage", e.getMessage());
		}
		return rstMap;
	}
	/**
	 * 取得未审核单据一栏
	 * @param systemUser
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/auditMessage")
	@ResponseBody
	public Map<String, Object> auditList(String systemUser, String voucherType, String status, String auditMessage) throws Exception {
		Map<String, Object> rstMap = new HashMap<String, Object>();
		try {
			List<AuditMessageBean> list = messageService.getAuditList(systemUser, voucherType, status, auditMessage);
			rstMap.put("errType", "0");
			rstMap.put("data", list);
		} catch (Exception e) {
			rstMap.put("errType", "1");
			rstMap.put("errMessage", e.getMessage());
		}
		return rstMap;
	}
}
