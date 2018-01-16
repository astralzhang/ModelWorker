package cn.lmx.flow.action.officework;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.officework.DeductInfoBean;
import cn.lmx.flow.service.officework.DeductPointService;
import cn.lmx.flow.utils.UserInfo;
/**
 * 扣分管理
 * @author yujx
 *
 */
@Controller
@RequestMapping("/Deduct")
public class DeductManager {
	//扣分管理
	@Resource(name="DeductPointService")
	private DeductPointService deductPointService;
	/**
	 * 一栏画面
	 * @param model
	 * @param tradeCode
	 * @param year
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String init(Model model) {
		try {
			Map<String, Object> map = deductPointService.list();
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "officework/DeductPointList";
	}
	/**
	 * 扣分一栏取得
	 * @param tradeCode
	 * @param year
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="query")
	public Map<String, Object> queryData(String tradeCode, String year) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap.put("data", deductPointService.list(tradeCode, year));
			resultMap.put("errType", "0");
		} catch (Exception e) {
			resultMap.put("errType", "1");
			resultMap.put("errMessage", e.getMessage());
		}
		return resultMap;
	}
	/**
	 * 根据工会级别取得所有工会信息
	 * @param tradeLevel
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/tradeList")
	public Map<String, Object> listTrade(String tradeLevel) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			List<?> tradeList = deductPointService.listTradeByUser(userBean.getUserNo());
			resultMap.put("data", tradeList);
			resultMap.put("errType", "0");
		} catch (Exception e) {
			resultMap.put("errType", "1");
			resultMap.put("errMessage", e.getMessage());
		}
		return resultMap;
	}
	/**
	 * 新增画面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		DeductInfoBean bean = new DeductInfoBean();
		bean.setDeductPoint(new BigDecimal(2));
		bean.setDeductDate(sdf.format(c.getTime()));
		model.addAttribute("data", bean);
		return "officework/DeductPointEdit";
	}
	/**
	 * 修改画面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(Model model, String id) {
		try {
			Map<String, Object> map = deductPointService.edit(id);
			if (map == null) {
				throw new Exception("数据取得错误！");
			}
			model.addAttribute("data", map.get("data"));
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "officework/DeductPointEdit";
	}
	/**
	 * 保存
	 * @param bean
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody DeductInfoBean bean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			deductPointService.save(userBean.getUserNo(), bean);
			resultMap.put("errType", "0");
		} catch (Exception e) {
			resultMap.put("errType", "1");
			resultMap.put("errMessage", e.getMessage());
		}
		return resultMap;
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public Map<String, Object> delete(String ids) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			deductPointService.delete(userBean.getUserNo(), ids);
			resultMap.put("errType", "0");
		} catch (Exception e) {
			resultMap.put("errType", "1");
			resultMap.put("errMessage", e.getMessage());
		}
		return resultMap;
	}
}
