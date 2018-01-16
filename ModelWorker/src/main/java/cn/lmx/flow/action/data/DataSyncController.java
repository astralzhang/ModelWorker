package cn.lmx.flow.action.data;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.lmx.flow.bean.data.SyncGroupBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.data.SyncService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/dataSync")
public class DataSyncController {
	//导入
	@Resource(name="SyncService")
	private SyncService syncService;
	/**
	 * 一栏画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model) {
		try {
			List<SyncGroupBean> list = syncService.getList();
			model.addAttribute("errType", "0");
			model.addAttribute("data", list);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "data/SyncDataList";
	}
	/**
	 * 数据同步
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/sync/{no}")
	public Map<String, Object> uploadFile(HttpServletRequest request, @PathVariable String no) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			syncService.syncData(userBean.getUserNo(), no, null);
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
}
