package com.balance.service.common;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.balance.architecture.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


/**
 * 阿里OSS服务
 */
@Service
public class AliOSSBusiness {

	private OSSClient client;

	private static String OSS_COMMON_BUCKET_NAME ;
	private static String SENSITIVE_BUCKET_NAME ;
	private static String SYSTEM_DOMAIN ;

	@Autowired
	private GlobalConfigService globalConfigService;

	public final static String IMG_URL = "/img";

	private static  String endPoint ;

	@PostConstruct
	public void init(){

		String appKey = globalConfigService.get(GlobalConfigService.Enum.OSS_ACCESS_KEY_ID);
		String secret = globalConfigService.get(GlobalConfigService.Enum.OSS_ACCESS_KEY_SECRET);

		OSS_COMMON_BUCKET_NAME = globalConfigService.get(GlobalConfigService.Enum.OSS_COMMON_BUCKET_NAME);

		//系统域名
		SYSTEM_DOMAIN = globalConfigService.get(GlobalConfigService.Enum.SYSTEM_DOMAIN);

		//敏感bucket_name
		SENSITIVE_BUCKET_NAME = globalConfigService.get(GlobalConfigService.Enum.OSS_SENSITIVE_BUCKET_NAME);

		endPoint = globalConfigService.get(GlobalConfigService.Enum.OSS_END_POINT);

		client = new OSSClient(endPoint , appKey, secret);
	}



	/**
	 * 删除cms配图 imgUri规则为: "/"+文件夹名称+"/"+文件名
	 *
	 * @param imgUri
	 * @throws BusinessException
	 */
	/*public void delCmsImg(String imgUri) throws BusinessException {
		if (!StringUtils.isBlank(imgUri) && imgUri.startsWith("/")) {
			this.deleteObject(cfgBusiness.get(CfgBusiness.KEYS.FS_BUCKET_NAME_CMS), imgUri.substring(1));
		}
	}*/


	/**
	 * 上传普通文件
	 * @param file
	 * @param directory
	 * @return
	 */
	public String uploadCommonPic(MultipartFile file, String directory){

		String fileName = String.valueOf(new Date().getTime());

		String contentType = file.getContentType();

		Integer index = contentType.indexOf("/");

		String suffix = contentType.substring(index + 1);

		fileName = fileName + "." + suffix;

		String picUrl = null;
		try {
			InputStream inputStream = file.getInputStream();
			picUrl = this.putObjectByStream(OSS_COMMON_BUCKET_NAME, inputStream,file.getSize(),file.getContentType(),directory,fileName);
		} catch (IOException e) {
//			Log.e(e);
		}

		picUrl = "http://" + OSS_COMMON_BUCKET_NAME + "." + endPoint + picUrl;
		return picUrl;
	}

	/**
	 * 上传敏感图片
	 * @param file
	 * @param directory
	 * @return
	 */
	public String uploadSensitivePic(MultipartFile file, String directory){

		String fileName = DateFormatUtils.format(new Date(),"yyyy-MM-dd|HH");

		String contentType = file.getContentType();

		Integer index = contentType.indexOf("/");

		String suffix = contentType.substring(index + 1);

		fileName = fileName + "." + suffix;

		String picUrl = null;
		try {
			InputStream inputStream = file.getInputStream();
			picUrl = this.putObjectByStream(SENSITIVE_BUCKET_NAME, inputStream,file.getSize(),file.getContentType(),"sensitive/"+directory,fileName);
		} catch (IOException e) {
//			Log.e(e);
		}

		picUrl = SYSTEM_DOMAIN + IMG_URL + picUrl;
		return picUrl;
	}



	/**
	 * 上传文件
	 *
	 * @param bucketName
	 *            bucketName,阿里OSS上定义
	 * @param is
	 *            上传文件流
	 * @param fileSize
	 *            文件大小
	 * @param contentType
	 *            html Conent Type
	 * @param dirname
	 *            上传目录名，前后不加"/" 例如 aaa/bbb
	 * @return 调用正常反回文件访问路径，如 /buckname/filename.jpg， 上传失败返回null.
	 */
	public String putObjectByStream(String bucketName, InputStream is, Long fileSize, String contentType, String dirname ,String filename  ) {
		try {
			if (is == null) {
				return null;
			}

			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(fileSize); // 必须设置ContentLength
			meta.setContentType(contentType);

			String key ;

			if (!StringUtils.isBlank(dirname)) {
				key = dirname + "/" + filename;
			} else {
				key = filename;
			}

			// 上传Object.
			PutObjectResult result = client.putObject(bucketName, key, is, meta);

			// MD5
//			Log.i("Put Ojb, bucketName:[" + bucketName + "] dirname:[" + dirname + "] filename:[" + filename + "] return MD5:[" + result.getETag() + "].");

			return "/" + key;
		} catch (OSSException oe) {
			oe.printStackTrace();
//			Log.e("Oss Exception.");

			return null;
		} catch (ClientException ce) {
			ce.printStackTrace();
//			Log.e("Oss Client Exception.");

			return null;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * 删除文件，参数key为URI全路径，但是开头不加'/', 如uri: "/images/123.jpg", 则传入Key为:
	 * "images/123.jpg"
	 *
	 * @param bucketName
	 * @param key
	 * @throws BusinessException
	 */
	private void deleteObject(String bucketName, String key) throws BusinessException {
		try {
			// 删除
			client.deleteObject(bucketName, key);
		} catch (IllegalArgumentException ile) {
			ile.printStackTrace();
			throw new BusinessException("Oss object key invalid.");
		} catch (OSSException oe) {
			oe.printStackTrace();
			throw new BusinessException("Oss Exception.");
		} catch (ClientException ce) {
			ce.printStackTrace();
			throw new BusinessException("Oss Client Exception.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("Ali Oss delete object exception.");
		}
	}

	/**
	 * 删除文件
	 *
	 * @param bucketName
	 * @param dirname
	 * @param filename
	 * @throws BusinessException
	 */
	private void deleteObejct(String bucketName, String dirname, String filename) throws BusinessException {
		String key;

		if (!StringUtils.isBlank(dirname)) {
			key = dirname + "/" + filename;
		} else {
			key = filename;
		}

		this.deleteObject(bucketName, key);
	}

}
