package cn.lmx.flow.service.officework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.officework.DocumentAcceptsBean;
import cn.lmx.flow.dao.trade.AcceptLevelDao;
import cn.lmx.flow.dao.trade.AcceptTypeDao;
import cn.lmx.flow.dao.trade.InfomationTypeDao;
import cn.lmx.flow.dao.trade.MagazinesDao;
import cn.lmx.flow.dao.trade.PointManagerDao;
import cn.lmx.flow.entity.trade.PointManager;

@Repository("PointManagerService")
public class PointManagerService {
	//信息类型
	@Resource(name="InfomationTypeDao")
	private InfomationTypeDao infomationTypeDao;
	//期刊
	@Resource(name="MagazinesDao")
	private MagazinesDao magazinesDao;
	//采用级别
	@Resource(name="AcceptLevelDao")
	private AcceptLevelDao acceptLevelDao;
	//采用类型
	@Resource(name="AcceptTypeDao")
	private AcceptTypeDao acceptTypeDao;
	//积分管理
	@Resource(name="PointManagerDao")
	private PointManagerDao pointManagerDao;
	/**
	 * 获取积分列表
	 * @param paraMap
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> pointList(Map<String, Object> paraMap) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		//信息列表
		List<?> infomationTypeList = infomationTypeDao.listAll();
		result.put("infoList", infomationTypeList);
		//采用期刊
		List<?> magazinesList = magazinesDao.listAll();
		result.put("magazineList", magazinesList);
		//录用级别
		List<?> acceptLevelList = acceptLevelDao.listAll();
		result.put("levelList", acceptLevelList);
		//录用类型
		List<?> acceptTypeList = acceptTypeDao.listAll();
		result.put("acceptList", acceptTypeList);
		
		//获取已经设置了的积分列表
		List<?> pointList = pointManagerDao.pointList(paraMap);
		List<DocumentAcceptsBean> rstList = new ArrayList<DocumentAcceptsBean>();
		for (int i = 0; i < pointList.size(); i++) {
			DocumentAcceptsBean bean = (DocumentAcceptsBean)pointList.get(i);
			rstList.add(bean);
		}
		result.put("rstList", rstList);
		return result;
	}
	/**
	 * 保存积分设定
	 * @param saveList
	 * @param userNo
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void savePoint(List<DocumentAcceptsBean> saveList,String userNo) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Calendar c = Calendar.getInstance();
		for(int i=0;i<saveList.size();i++){
			DocumentAcceptsBean bean = saveList.get(i);
			PointManager point = new PointManager();
			//根据ID判断是新增还是修改
			if(bean.getId() != null && !"".equals(bean.getId())){
				point = pointManagerDao.getById(bean.getId());
				if(point != null){
					point.setPoint(bean.getPoint());
					point.setUpdateTime(sdf.format(c.getTime()));
					point.setUpdateUser(userNo);
					pointManagerDao.edit(point);
				}
			}else{
				point.setId(UUID.randomUUID().toString());
				point.setAcceptType(bean.getAcceptType());
				point.setInfoType(bean.getInfoType());
				point.setMagazineCode(bean.getMagazineCode());
				point.setPoint(bean.getPoint());
				point.setCreateUser(userNo);
				point.setCreateTime(sdf.format(c.getTime()));
				pointManagerDao.add(point);
			}
		}
	}
}
