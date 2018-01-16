package cn.lmx.flow.service.officework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.officework.DeductInfoBean;
import cn.lmx.flow.dao.officework.DeductInfoDao;
import cn.lmx.flow.dao.trade.TradeUnionInfoDao;
import cn.lmx.flow.entity.officework.DeductInfo;
import cn.lmx.flow.entity.trade.TradeUnionInfo;
/**
 * 扣分管理
 * @author yujx
 *
 */
@Repository("DeductPointService")
public class DeductPointService {
	//扣分管理
	@Resource(name="DeductInfoDao")
	private DeductInfoDao deductInfoDao;
	//工会信息
	@Resource(name="TradeUnionInfoDao")
	private TradeUnionInfoDao tradeUnionInfoDao;
	/**
	 * 取得扣分一栏
	 * @param tradeCode
	 * @param cYear
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list() throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//取得年份一栏
			Calendar c = Calendar.getInstance();
			List<Map<String, String>> yearList = new ArrayList<Map<String, String>>();
			int iYear = c.get(Calendar.YEAR);
			for (int i = 10; i >= 0; i--) {
				Map<String, String> yearMap = new HashMap<String, String>();
				yearMap.put("year", String.valueOf(iYear - i));
				if (i== 0) {
					yearMap.put("defaultData", "Y");
				}
				yearList.add(yearMap);
			}
			map.put("yearList", yearList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得扣除信息一栏
	 * @param tradeCode
	 * @param cYear
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> list(String tradeCode, String cYear) throws Exception {
		try {
			List<?> deductList = deductInfoDao.listByCodeYear(tradeCode, cYear);
			return deductList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得工会信息
	 * @param systemUser
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> listTradeByUser(String systemUser) throws Exception {
		try {
			return tradeUnionInfoDao.listTrade(systemUser);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得扣分信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> edit(String id) throws Exception {
		try {
			DeductInfo deductInfo = deductInfoDao.getById(id);
			if (deductInfo == null) {
				throw new Exception("您查询的扣分数据不存在！");
			}
			TradeUnionInfo tradeUnionInfo = tradeUnionInfoDao.getById(deductInfo.getTradeCode());
			if (tradeUnionInfo == null) {
				throw new Exception("工会信息错误，请确认！");
			}
			DeductInfoBean bean = new DeductInfoBean();
			BeanUtils.copyProperties(deductInfo, bean);
			bean.setTradeName(tradeUnionInfo.getName());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", bean);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 扣分数据保存
	 * @param systemUser
	 * @param bean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void save(String systemUser, DeductInfoBean bean) throws Exception {
		try {
			if (bean == null) {
				throw new Exception("系统错误！");
			}
			//数据check
			if (bean.getDeductCause().getBytes("UTF-8").length > 500) {
				throw new Exception("扣分理由不可以超过500个字节！");
			}
			if (bean.getDeductDate() == null || "".equals(bean.getDeductDate())) {
				throw new Exception("扣分日期不可以为空！");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			if (bean.getId() == null || "".equals(bean.getId())) {
				String tradCode=bean.getTradeCode();
				String[] arrCode = tradCode.split(",");
				for (int i = 0; i < arrCode.length; i++) {
					String code = arrCode[i];
					//新增
					DeductInfo deduct = new DeductInfo();
//					BeanUtils.copyProperties(bean, deduct);
					deduct.setId(UUID.randomUUID().toString());
					deduct.setDeductPoint(bean.getDeductPoint());
					deduct.setDeductCause(bean.getDeductCause());
					deduct.setTradeCode(code);
					deduct.setDeductYear(String.valueOf(c.get(Calendar.YEAR)));
					deduct.setDeductDate(sdf2.format(c.getTime()));
					deduct.setCreateUser(systemUser);
					deduct.setCreateTime(sdf.format(c.getTime()));
					deductInfoDao.add(deduct);
				}
			} else {
				//修改
				DeductInfo deduct = deductInfoDao.getById(bean.getId());
				if (deduct == null) {
					throw new Exception("您修改的数据不存在！");
				}
				BeanUtils.copyProperties(bean, deduct);
				Calendar c2 = Calendar.getInstance();
				c2.setTime(sdf2.parse(bean.getDeductDate()));
				deduct.setDeductYear(String.valueOf(c2.get(Calendar.YEAR)));
				deduct.setUpdateUser(systemUser);
				deduct.setUpdateTime(sdf.format(c.getTime()));
				deductInfoDao.edit(deduct);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 删除数据
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void delete(String systemUser, String ids) throws Exception {
		try {
			if (ids == null || "".equals(ids)) {
				throw new Exception("系统错误！");
			}
			String[] arrId = ids.split(",");
			if (arrId == null || arrId.length < 0) {
				throw new Exception("系统错误！");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			for (int i = 0; i < arrId.length; i++) {
				String id = arrId[i];
				DeductInfo deductInfo = deductInfoDao.getById(id);
				if (deductInfo == null) {
					continue;
				}
				deductInfoDao.delete(deductInfo);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
