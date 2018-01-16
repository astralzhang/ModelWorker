package cn.lmx.flow.service.flow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.flow.VoucherTypeBean;
import cn.lmx.flow.dao.flow.VoucherTypeDao;
import cn.lmx.flow.entity.flow.VoucherTypes;

@Repository("VoucherTypeService")
public class VoucherTypeService {
	@Resource(name="VoucherTypeDao")
	private VoucherTypeDao voucherTypeDao;
	/**
	 * 取得单据类型列表
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<VoucherTypeBean> list() throws Exception {
		List<?> list = voucherTypeDao.getVoucherTypeList();
		List<VoucherTypeBean> rstList = new ArrayList<VoucherTypeBean>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				VoucherTypes type = (VoucherTypes)list.get(i);
				VoucherTypeBean bean = new VoucherTypeBean();
				BeanUtils.copyProperties(type, bean);
				rstList.add(bean);
			}
		}
		return rstList;
	}
}
