package cn.lmx.flow.service.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.view.OpenWindowBean;
import cn.lmx.flow.dao.view.OpenWindowDao;
import cn.lmx.flow.entity.view.OpenWindows;

@Repository("OpenWindowService")
public class OpenWindowService {
	//弹窗
	@Resource(name="OpenWindowDao")
	private OpenWindowDao openWindowDao;
	/**
	 * 取得弹窗一栏
	 * @param no
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<OpenWindowBean> list(String no, String name) throws Exception {
		List<?> list = openWindowDao.list(no, name);
		if (list == null) {
			return null;
		}
		List<OpenWindowBean> rstList = new ArrayList<OpenWindowBean>();
		for (int i = 0; i < list.size(); i++) {
			OpenWindows openWindow = (OpenWindows)list.get(i);
			OpenWindowBean bean = new OpenWindowBean();
			BeanUtils.copyProperties(openWindow, bean);
			rstList.add(bean);
		}
		return rstList;
	}
	/**
	 * 保存弹窗
	 * @param systemUser
	 * @param bean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveOpenWin(String systemUser, OpenWindowBean bean) throws Exception {
		OpenWindows openWindow = openWindowDao.getByNo(bean.getNo());
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		if (openWindow == null) {
			//新建
			openWindow = new OpenWindows();
			BeanUtils.copyProperties(bean, openWindow);
			openWindow.setCreateUser(systemUser);
			openWindow.setCreateTime(sdf.format(c.getTime()));
			openWindowDao.add(openWindow);
		} else {
			//修改
			BeanUtils.copyProperties(bean, openWindow);
			openWindow.setUpdateUser(systemUser);
			openWindow.setUpdateTime(sdf.format(c.getTime()));
			openWindowDao.edit(openWindow);
		}
	}
	/**
	 * 取得弹窗
	 * @param no
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public OpenWindowBean getByNo(String no) throws Exception {
		OpenWindows openWindow = openWindowDao.getByNo(no);
		if (openWindow == null) {
			throw new Exception("弹窗不存在！");
		}
		OpenWindowBean bean = new OpenWindowBean();
		BeanUtils.copyProperties(openWindow, bean);
		return bean;
	}
}
