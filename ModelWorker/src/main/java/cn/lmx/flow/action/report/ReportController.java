package cn.lmx.flow.action.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.entity.view.ReportPublish;
import cn.lmx.flow.service.report.ReportService;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/report")
public class ReportController {
	//报表
	@Resource(name="ReportService")
	private ReportService reportService;
	/**
	 * 报表画面
	 * @param no
	 * @return
	 */
	@RequestMapping(value="/list/{no}")
	public String list(Model model, @PathVariable String no) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = reportService.init(userBean.getUserNo(), no);
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "report/list";
	}
	/**
	 * 查询画面
	 * @param no
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/search/{no}")
	public Map<String, Object> query(@PathVariable String no, @RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<?> list = reportService.query(no, map);
			result.put("errType", "0");
			result.put("data", list);
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 下载Excel
	 * @param no
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/download/{no}")
	public void download(@PathVariable String no, @RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String path = request.getSession().getServletContext().getRealPath("");
			String templateId = map.get("templateId") == null ? "" : String.valueOf(map.get("templateId"));
			Map<String, Object> resultMap = reportService.download(path, no, map, templateId);
			String fileName = (String)resultMap.get("fileName");
			ReportPublish report = (ReportPublish)resultMap.get("report");
			File file = new File(fileName);
			if (!file.exists()) {
				return;
			}
			FileInputStream inputStream = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			response.setContentType("application/x-download");
			String agent = (String)request.getHeader("USER-AGENT");
			String downloadFileName = String.valueOf(map.get("fileName"));
			String fileExtend = "";
			if (fileName.endsWith(".xlsx")) {
				fileExtend = ".xlsx";
			} else if (fileName.endsWith(".xls")) {
				fileExtend = ".xls";
			}
			String tempFileName = report.getName() + fileExtend;
            if(agent != null && agent.toLowerCase().indexOf("firefox") > 0)
            {
            	downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(tempFileName.getBytes("UTF-8")))) + "?=";
            } else {
            	downloadFileName = URLEncoder.encode(tempFileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment;fileName="+ downloadFileName);
            byte[] bData = new byte[1024];
            int count = 0;
            while (count != -1) {
            	count = inputStream.read(bData);
            	out.write(bData, 0, count);
            }
            inputStream.close();
            out.flush();
		} catch (Exception e) {
			
		}
	}
	/**
	 * 链接画面数据取得
	 * @param id
	 * @param viewNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/linkView")
	public Map<String, Object> linkView(String id, String viewNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = reportService.linkView(id, viewNo);
			map.put("errType", "0");
			//map.put("view", viewData);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
}
