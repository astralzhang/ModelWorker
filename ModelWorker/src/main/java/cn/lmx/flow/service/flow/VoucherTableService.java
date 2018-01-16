package cn.lmx.flow.service.flow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.flow.VoucherTableBean;
import cn.lmx.flow.dao.flow.VoucherTableDao;
import cn.lmx.flow.entity.flow.VoucherTables;

@Repository("VoucherTableService")
public class VoucherTableService {
	@Resource(name="VoucherTableDao")
	private VoucherTableDao voucherTableDao;
	/**
	 * 根据单据类型取得数据表列表
	 * @param voucherType
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<VoucherTableBean> getVoucherTableList(String voucherType) throws Exception {
		try {
			List<?> list = voucherTableDao.getVoucherTableList(voucherType);
			List<VoucherTableBean> rstList = new ArrayList<VoucherTableBean>();
			if (list == null || list.size() <= 0) {
				return rstList; 
			}
			for (int i = 0; i < list.size(); i++) {
				VoucherTables table = (VoucherTables)list.get(i);
				VoucherTableBean bean = new VoucherTableBean();
				BeanUtils.copyProperties(table, bean);
				rstList.add(bean);
			}
			return rstList;
		} catch (Exception e) {
			throw e;
		}
	}
}
