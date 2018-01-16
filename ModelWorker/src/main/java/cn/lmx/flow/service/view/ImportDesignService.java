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

import cn.lmx.flow.bean.view.ImportDesignBean;
import cn.lmx.flow.dao.view.ImportDesignDao;
import cn.lmx.flow.dao.view.ImportGroupDao;
import cn.lmx.flow.dao.view.ImportPublishDao;
import cn.lmx.flow.entity.view.ImportDesign;
import cn.lmx.flow.entity.view.ImportPublish;

@Repository("ImportDesignService")
public class ImportDesignService {
	//导入设定Dao
	@Resource(name="ImportDesignDao")
	private ImportDesignDao importDesignDao;
	//导入组
	@Resource(name="ImportGroupDao")
	private ImportGroupDao importGroupDao;
	//导入发布
	@Resource(name="ImportPublishDao")
	private ImportPublishDao importPublishDao;
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
			List<?> list = importDesignDao.list(no, status);
			//取得导入设定一栏
			List<ImportDesignBean> rstList = new ArrayList<ImportDesignBean>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					rstList.add((ImportDesignBean)list.get(i));
				}
			}
			//取得导入组一栏
			List<?> groupList = importGroupDao.list("N");
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
	public void save(String systemUser, ImportDesignBean bean) throws Exception {
		if (bean == null) {
			throw new Exception("没有指定要保存的数据！");
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			ImportDesign maxImportDesign = importDesignDao.getMaxVersion(bean.getNo());
			int version = 0;
			if (maxImportDesign != null) {
				version = Integer.parseInt(maxImportDesign.getDesignVersion());
			}
			version++;
			String sVersion = new StringBuffer("")
					.append("00000")
					.append(version).toString();
			sVersion = sVersion.substring(sVersion.length() - 5);
			ImportDesign design = new ImportDesign();
			BeanUtils.copyProperties(bean, design);
			design.setDesignVersion(sVersion);
			design.setId(UUID.randomUUID().toString());
			design.setStatus("0");
			design.setCreateUser(systemUser);
			design.setCreateTime(sdf.format(c.getTime()));
			importDesignDao.add(design);
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
	public	void publish(String systemUser, String no, String groupNo) throws Exception {
		try {
			List<?> list = importDesignDao.list(no, null);
			if (list == null || list.size() <= 0) {
				throw new Exception("没有指定编码的数据待发布，请刷新页面重试！");
			}
			ImportDesignBean designBean = (ImportDesignBean)list.get(0);
			if ("1".equals(designBean.getStatus())) {
				throw new Exception("最新版本已发布，无需重新发布！");
			}
			ImportPublish importPublish = importPublishDao.getById(no);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			String version = "";
			if (importPublish == null) {
				//新增
				version = "00001";
				importPublish = new ImportPublish();
				BeanUtils.copyProperties(designBean, importPublish);
				importPublish.setGroupNo(groupNo);
				importPublish.setVersion(version);
				importPublish.setCreateUser(systemUser);
				importPublish.setCreateTime(sdf.format(c.getTime()));
				importPublishDao.add(importPublish);
				//修改当前版本为发布版本
				importDesignDao.publish(systemUser, sdf.format(c.getTime()), designBean.getId(), version);
			} else {
				//修改
				version = new StringBuffer("")
						.append("00000")
						.append(Integer.parseInt(importPublish.getVersion()) + 1).toString();
				version = version.substring(version.length() - 5);
				BeanUtils.copyProperties(designBean, importPublish);
				importPublish.setVersion(version);
				importPublish.setGroupNo(groupNo);
				importPublish.setUpdateUser(systemUser);
				importPublish.setUpdateTime(sdf.format(c.getTime()));
				importPublishDao.edit(importPublish);
				//修改当前版本为发布版本
				importDesignDao.publish(systemUser, sdf.format(c.getTime()), designBean.getId(), version);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
