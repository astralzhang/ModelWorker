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

import cn.lmx.flow.bean.data.ImportGroupBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.service.data.ImportService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/dataImport")
public class DataImportController {
	//导入
	@Resource(name="ImportService")
	private ImportService importService;
	/**
	 * 一栏画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model) {
		try {
			List<ImportGroupBean> list = importService.getList();
			model.addAttribute("errType", "0");
			model.addAttribute("data", list);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "data/ImportList";
	}
	/**
	 * 数据导入
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/import/{no}")
	public Map<String, Object> uploadFile(HttpServletRequest request, @PathVariable String no, @RequestParam("importFile") MultipartFile importFile) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
				Iterator<Entry<String, MultipartFile>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, MultipartFile> entry = it.next();
					MultipartFile file = entry.getValue();
					importService.importData(userBean.getUserNo(), file.getOriginalFilename(), file.getInputStream(), no);					
				}
			}
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
}
