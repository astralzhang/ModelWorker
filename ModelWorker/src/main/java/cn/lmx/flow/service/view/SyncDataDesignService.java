package cn.lmx.flow.service.view;

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

import cn.lmx.flow.bean.view.SyncDataDesignBean;
import cn.lmx.flow.dao.view.SyncDataDesignDao;
import cn.lmx.flow.dao.view.SyncDataPublishDao;
import cn.lmx.flow.dao.view.SyncGroupDao;
import cn.lmx.flow.entity.view.SyncDataDesign;
import cn.lmx.flow.entity.view.SyncDataPublish;

@Repository("SyncDataDesignService")
public class SyncDataDesignService {
	//导入设定Dao
	@Resource(name="SyncDataDesignDao")
	private SyncDataDesignDao syncDataDesignDao;
	//模块
	@Resource(name="SyncGroupDao")
	private SyncGroupDao syncGroupDao;
	//导入发布
	@Resource(name="SyncDataPublishDao")
	private SyncDataPublishDao syncDataPublishDao;
	/**
	 * 列表取得
	 * @param no
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String no, String status) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			List<?> list = syncDataDesignDao.list(no, status);
			//取得导入设定一栏
			List<SyncDataDesignBean> rstList = new ArrayList<SyncDataDesignBean>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					rstList.add((SyncDataDesignBean)list.get(i));
				}
			}
			//取得module一栏
			List<?> groupList = syncGroupDao.list("N");
			map.put("Design", rstList);
			map.put("Group", groupList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 新增版本
	 * @param systemUser
	 * @param bean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void save(String systemUser, SyncDataDesignBean bean) throws Exception {
		if (bean == null) {
			throw new Exception("没有指定要保存的数据！");
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			SyncDataDesign maxSyncDataDesign = syncDataDesignDao.getMaxVersion(bean.getNo());
			int version = 0;
			if (maxSyncDataDesign != null) {
				version = Integer.parseInt(maxSyncDataDesign.getDesignVersion());
			}
			version++;
			String sVersion = new StringBuffer("")
					.append("00000")
					.append(version).toString();
			sVersion = sVersion.substring(sVersion.length() - 5);
			SyncDataDesign design = new SyncDataDesign();
			BeanUtils.copyProperties(bean, design);
			design.setDesignVersion(sVersion);
			design.setId(UUID.randomUUID().toString());
			design.setStatus("0");
			design.setCreateUser(systemUser);
			design.setCreateTime(sdf.format(c.getTime()));
			syncDataDesignDao.add(design);
		}	catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 发布
	 * @param systemUser
	 * @param no
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public	void publish(String systemUser, String no, String moduleNo) throws Exception {
		try {
			List<?> list = syncDataDesignDao.list(no, null);
			if (list == null || list.size() <= 0) {
				throw new Exception("没有指定编码的数据待发布，请刷新页面重试！");
			}
			SyncDataDesignBean designBean = (SyncDataDesignBean)list.get(0);
			if ("1".equals(designBean.getStatus())) {
				throw new Exception("最新版本已发布，无需重新发布！");
			}
			SyncDataPublish syncDataPublish = syncDataPublishDao.getById(no);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			String version = "";
			if (syncDataPublish == null) {
				//新增
				version = "00001";
				syncDataPublish = new SyncDataPublish();
				BeanUtils.copyProperties(designBean, syncDataPublish);
				syncDataPublish.setModuleNo(moduleNo);
				syncDataPublish.setVersion(version);
				syncDataPublish.setCreateUser(systemUser);
				syncDataPublish.setCreateTime(sdf.format(c.getTime()));
				syncDataPublishDao.add(syncDataPublish);
				//修改当前版本为发布版本
				syncDataDesignDao.publish(systemUser, sdf.format(c.getTime()), designBean.getId(), version);
			} else {
				//修改
				version = new StringBuffer("")
						.append("00000")
						.append(Integer.parseInt(syncDataPublish.getVersion()) + 1).toString();
				version = version.substring(version.length() - 5);
				BeanUtils.copyProperties(designBean, syncDataPublish);
				syncDataPublish.setVersion(version);
				syncDataPublish.setModuleNo(moduleNo);
				syncDataPublish.setUpdateUser(systemUser);
				syncDataPublish.setUpdateTime(sdf.format(c.getTime()));
				syncDataPublishDao.edit(syncDataPublish);
				//修改当前版本为发布版本
				syncDataDesignDao.publish(systemUser, sdf.format(c.getTime()), designBean.getId(), version);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
