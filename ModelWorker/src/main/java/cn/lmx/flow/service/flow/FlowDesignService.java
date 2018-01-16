package cn.lmx.flow.service.flow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.flow.FlowBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.dao.flow.FlowDao;
import cn.lmx.flow.dao.flow.PublishedFlowDao;
import cn.lmx.flow.dao.flow.VoucherTypeDao;
import cn.lmx.flow.entity.flow.Flows;
import cn.lmx.flow.entity.flow.PublishedFlows;
import cn.lmx.flow.entity.flow.VoucherTypes;
import cn.lmx.flow.utils.UserInfo;
@Repository("FlowDesignService")
public class FlowDesignService {
	//FlowDao
	@Resource(name="FlowDao")
	private FlowDao flowDao;
	//已发布流程
	@Resource(name="PublishedFlowDao")
	private PublishedFlowDao publishedFlowDao;
	//单据类型
	@Resource(name="VoucherTypeDao")
	private VoucherTypeDao voucherTypeDao;
	/**
	 * 流程保存
	 * @param bean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveFlow(FlowBean bean) throws Exception {
		try {
			String flowVersion = null;
			List<?> list = flowDao.getMaxVersion(bean.getNo());
			if (list != null && list.size() > 0) {
				Flows flow = (Flows)list.get(0);
				flowVersion = "00000" + String.valueOf(Integer.parseInt(flow.getVersion()) + 1);
				flowVersion = flowVersion.substring(flowVersion.length() - 5);
			} else {
				flowVersion = "00001";
			}
			Flows flow = new Flows();
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			Calendar c = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			BeanUtils.copyProperties(bean, flow);
			flow.setCreateUser(userBean.getUserNo());
			flow.setCreateTime(format.format(c.getTime()));
			flow.setStatus("0");
			flow.setVersion(flowVersion);
			flow.setId(UUID.randomUUID().toString());
			flowDao.addFlow(flow);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 流程一栏
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<FlowBean> getFlowList(FlowBean bean) throws Exception {
		try {
			Flows paramFlow = new Flows();
			BeanUtils.copyProperties(bean, paramFlow);
			List<?> list = flowDao.getFlowList(paramFlow);
			List<FlowBean> flowList = new ArrayList<FlowBean>();
			List<?> typeList = (List<?>)voucherTypeDao.getVoucherTypeList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Flows flow = (Flows)list.get(i);
					FlowBean flowBean = new FlowBean();
					BeanUtils.copyProperties(flow, flowBean);
					for (int j = 0; j < typeList.size(); j++) {
						VoucherTypes type = (VoucherTypes)typeList.get(j);
						if (flowBean.getVoucherType().equals(type.getNo())) {
							flowBean.setVoucherTypeName(type.getName());
						}
					}
					flowList.add(flowBean);
				}
			}
			return flowList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 发布流程
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void pulishFlow(String systemUser, String id) throws Exception {
		Flows paramFlow = new Flows();
		paramFlow.setNo(id);
		List<?> list = flowDao.getFlowList(paramFlow);		
		if (list == null || list.size() <= 0) {
			throw new Exception("您选择的流程(" + id + ")不存在！");
		}
		Flows flows = (Flows)list.get(0);
		if ("1".equals(flows.getStatus())) {
			throw new Exception("流程已发布，无需重新发布！");
		}
		PublishedFlows publishedFlows = publishedFlowDao.getLastFlowByVoucherType(flows.getVoucherType());
		String publishedVersion = "00001";
		if (publishedFlows != null) {
			int iVersion = Integer.parseInt(publishedFlows.getPublishVersion()) + 1;
			publishedVersion = new StringBuffer("00000")
									.append(iVersion).toString();
			publishedVersion = publishedVersion.substring(publishedVersion.length() - 5);
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		PublishedFlows tempFlows = new PublishedFlows();
		BeanUtils.copyProperties(flows, tempFlows);
		tempFlows.setPublishVersion(publishedVersion);
		tempFlows.setCreateUser(systemUser);
		tempFlows.setCreateTime(sdf.format(c.getTime()));
		tempFlows.setUpdateUser(null);
		tempFlows.setUpdateTime(null);
		publishedFlowDao.add(tempFlows);
		flows.setStatus("1");
		flows.setPublishVersion(publishedVersion);
		flows.setUpdateUser(systemUser);
		flows.setUpdateTime(sdf.format(c.getTime()));
		flowDao.editFlow(flows);
	}
}
