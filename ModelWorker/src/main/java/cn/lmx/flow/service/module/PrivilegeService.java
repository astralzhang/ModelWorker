package cn.lmx.flow.service.module;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.module.PrivilegeBean;
import cn.lmx.flow.dao.module.PrivilegeDao;
import cn.lmx.flow.entity.module.Privilege;

@Repository("PrivilegeService")
public class PrivilegeService {
	//功能Bean
	@Resource(name="PrivilegeDao")
	private PrivilegeDao privilegeDao;
	/**
	 * 取得所有的功能
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<PrivilegeBean> listAll() throws Exception {
		try {
			List<?> list = privilegeDao.listAll();
			if (list == null) {
				return null;
			}
			List<PrivilegeBean> rstList = new ArrayList<PrivilegeBean>();
			for (int i = 0; i < list.size(); i++) {
				Privilege privilege = (Privilege)list.get(i);
				PrivilegeBean bean = new PrivilegeBean();
				BeanUtils.copyProperties(privilege, bean);
				rstList.add(bean);
			}
			return rstList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
