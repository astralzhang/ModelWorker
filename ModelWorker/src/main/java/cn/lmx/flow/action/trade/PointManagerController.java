package cn.lmx.flow.action.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.officework.DocumentAcceptsBean;
import cn.lmx.flow.service.officework.PointManagerService;
import cn.lmx.flow.utils.UserInfo;

/**
 * 积分管理
 * @author zh
 *
 */
@Controller
@RequestMapping("/Point")
public class PointManagerController {
	//公文管理
	@Resource(name="PointManagerService")
	private PointManagerService pointManagerService;
	
	/**
	 * 取得积分信息一栏
	 * @return
	 */
	@RequestMapping(value="/list")
	public String init(Model model) {
		Map<String, Object> paraMap  = new HashMap<String, Object>();
		paraMap.put("search_info", "");
		paraMap.put("search_level", "");
		paraMap.put("search_magazines", "");
		paraMap.put("search_accept", "");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = pointManagerService.pointList(paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data", map);
		return "Trade/PointList";
	}

	@RequestMapping(value = "/query")
	public String list(HttpServletRequest request, String search_info,String search_level,
			String search_magazines,String search_accept,Model model,@RequestParam Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = pointManagerService.pointList(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("search_info", data.get("search_info").toString());
		map.put("search_level", data.get("search_level").toString());
		map.put("search_magazines", data.get("search_magazines").toString());
		map.put("search_accept", data.get("search_accept").toString());
		model.addAttribute("data", map);
		return "Trade/PointList";
	}
	/**
	 * 保存积分设定信息
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody DocumentAcceptsBean documentAcceptsBean) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<DocumentAcceptsBean> rstList = new ArrayList<DocumentAcceptsBean>();
		List<DocumentAcceptsBean> saveList = new ArrayList<DocumentAcceptsBean>();
		if(documentAcceptsBean.getDocumentAcceptsList() != null && documentAcceptsBean.getDocumentAcceptsList().size()>0){
			rstList=documentAcceptsBean.getDocumentAcceptsList();
			Pattern pattern = Pattern.compile("[0-9]*");
			for(int i=0;i<rstList.size();i++){
				DocumentAcceptsBean bean= new DocumentAcceptsBean();
				bean=rstList.get(i);
				//判断point栏位是否是数值
				if(bean.getPoint() != null && !"".equals(bean.getPoint())){
					if(!pattern.matcher(bean.getPoint().toString().trim()).matches()){
						map.put("errType", "1");
						map.put("errMessage", "积分值只能为正整数！");
						return map;
					}
				}else{
					bean.setPoint(BigDecimal.ZERO);
				}
				saveList.add(bean);
			}
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			try {
				pointManagerService.savePoint(saveList,userBean.getUserNo());
				map.put("errType", "0");
				map.put("errMessage", "保存成功！");
			} catch (Exception e) {
				map.put("errType", "1");
				map.put("errMessage", e.getMessage());
			}
		}else{
			map.put("errType", "1");
			map.put("errMessage", "请选择你要设置积分的数据！");
			return map;
		}
		return map;
	}
}
