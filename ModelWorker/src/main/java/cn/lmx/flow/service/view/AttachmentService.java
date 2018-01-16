package cn.lmx.flow.service.view;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import cn.lmx.flow.bean.view.AttachmentBean;
import cn.lmx.flow.dao.view.AttachmentDao;
import cn.lmx.flow.entity.view.Attachments;
@Repository("AttachmentService")
public class AttachmentService {
	//文件
	@Resource(name="AttachmentDao")
	private AttachmentDao attachmentDao;
	/**
	 * 保存文件
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public AttachmentBean saveFile(String systemUser, AttachmentBean bean, String maxSeqNo) throws Exception {
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			if (bean.getId() == null || "".equals(bean.getId())) {
				//新增
				bean.setId(UUID.randomUUID().toString());
				//取得最大序号
				String seqNo = null;
				if (bean.getDataId() != null && !"".equals(bean.getDataId())) {
					Attachments maxAtta = attachmentDao.getMaxSeqNo(bean.getDataId());
					if (maxAtta == null) {
						seqNo = "00001";
					} else {
						int maxSeqNo2 = Integer.parseInt(maxAtta.getSeqNo());
						maxSeqNo2++;
						seqNo = new StringBuffer("")
									.append("0000")
									.append(maxSeqNo2).toString();
						seqNo = seqNo.substring(seqNo.length() - 5);
					}
				} else {
					int maxSeqNo2 = Integer.parseInt(maxSeqNo);
					maxSeqNo2++;
					seqNo = new StringBuffer("")
								.append("0000")
								.append(maxSeqNo2).toString();
					seqNo = seqNo.substring(seqNo.length() - 5);
				}				
				bean.setSeqNo(seqNo);
				Attachments attachment = new Attachments();
				BeanUtils.copyProperties(bean, attachment);
				attachment.setCreateUser(systemUser);
				attachment.setCreateTime(sdf.format(c.getTime()));
				attachmentDao.add(attachment);
			} else {
				//修改
				Attachments attachment = new Attachments();
				BeanUtils.copyProperties(bean, attachment);
				attachment.setUpdateUser(systemUser);
				attachment.setUpdateTime(sdf.format(c.getTime()));
				attachmentDao.add(attachment);
			}
			return bean;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			} else {
				throw e;
			}
		}
	}
	/**
	 * 删除指定文件
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteFile(String systemUser, String id) throws Exception {
		try {
			attachmentDao.delete(id);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			} else {
				throw e;
			}
		}
	}
	/**
	 * 查看文件
	 * @param systemUser
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> viewFile(String systemUser, String id, int pageNo) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Attachments attachment = attachmentDao.getById(id);
			if (attachment != null) {
				map.put("fileName", attachment.getFileName());
				map.put("fileExtend", attachment.getFileExtend());
				if (attachment.getFileExtend() != null) {
					if ("jpg".equals(attachment.getFileExtend().toLowerCase())
						|| "png".equals(attachment.getFileExtend().toLowerCase())
						|| "gif".equals(attachment.getFileExtend().toLowerCase())) {
						//图片文件
						String fileContent = Base64.encode(attachment.getFileContent());
						map.put("fileContent", fileContent);
					} else if ("pdf".equals(attachment.getFileExtend().toLowerCase())) {
						//PDF文件
						ByteBuffer buf = ByteBuffer.wrap(attachment.getFileContent());
						PDFFile file = new PDFFile(buf);
						System.out.println(file.getNumPages());
						if (pageNo <= 0) {
							pageNo = 1;
						} else if (pageNo >= file.getNumPages()) {
							pageNo = file.getNumPages();
						}
						PDFPage page = file.getPage(pageNo);
						Rectangle rect = new Rectangle(0, 0, (int)page.getBBox().getWidth(), (int)page.getBBox().getHeight());
						Image image = page.getImage(rect.width, rect.height, rect, null, true, true);
						BufferedImage bufImg = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
						bufImg.getGraphics().drawImage(image, 0, 0, rect.width, rect.height, null);
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
						encoder.encode(bufImg);
						byte[] imageByte = out.toByteArray();
						String fileContent = Base64.encode(imageByte);
						map.put("fileContent", fileContent);
						map.put("pageNo", pageNo);
						map.put("totalPage", file.getNumPages());
					} else {
						map.put("fileContent", attachment.getFileContent());
					}
				}
			}
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			} else {
				throw e;
			}
		}
	}
}
