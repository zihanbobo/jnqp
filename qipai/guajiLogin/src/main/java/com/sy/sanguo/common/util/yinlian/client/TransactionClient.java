package com.sy.sanguo.common.util.yinlian.client;

import java.net.URLEncoder;

import com.sy.sanguo.common.util.yinlian.tools.Base64;
import com.sy.sanguo.common.util.yinlian.tools.Tools;
import com.sy.sanguo.common.util.yinlian.tools.Xml;
import com.sy.sanguo.common.util.yinlian.tools.http.HttpClient;
import com.sy.sanguo.common.util.yinlian.tools.rsa.Signatory;
/**
 * 易联服务器交易接口调用API封装，分别对以下接口调用进行了封装；
 * 接口封装了参数的转码（中文base64转码）、签名和验证签名、通讯和通讯报文处理
 * 1、商户订单下单接口
 * 2、商户订单查询接口
 * 3、商户订单冲正接口
 * 4、订单结果通知签名验证
 */
public class TransactionClient {

	/**
	 * 商户订单下单接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param amount		:	商户订单金额
	 * @param orderDesc		:	商户订单描述	  字符最大128，中文最多40个；参与签名：采用UTF-8编码提交参数：采用UTF-8的base64格式编码
	 * @param tradeTime		:	商户订单提交时间
	 * @param expTime		:	交易超时时间； 超过订单超时时间未支付，订单作废；不提交该参数，采用系统的默认时间（从接收订单后超时时间为30分钟）
	 * @param notifyUrl		:	异步通知URL ； 提交参数时，做URLEncode处理
	 * @param extData		:	商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码 ； 提交参数：采用UTF-8的base64格式编码
	 * @param miscData		:	订单扩展信息   根据不同的行业，传送的信息不一样；参与签名：采用UTF-8编码，提交参数：采用UTF-8的base64格式编码
	 * @param notifyFlag	:	订单通知标志    0：成功才通知，1：全部通知（成功或失败）  不填默认为“1：全部通知”
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				:  处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String MerchantOrder(String merchantId, String merchOrderId, 
			String amount, String orderDesc ,String tradeTime, String expTime, String notifyUrl,
			String extData, String miscData, String notifyFlag, String priKey, String pubKey, 
			String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "PlaceOrder";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
		//进行数据签名
		String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
				+ "&Amount=" + amount + "&OrderDesc=" + orderDesc + "&TradeTime=" + tradeTime + "&ExpTime="
				+ expTime + "&NotifyUrl=" + notifyUrl + "&ExtData=" + extData
				+ "&MiscData=" + miscData
				+ "&NotifyFlag=" + notifyFlag;
		//Version=2.0.0&MerchantId=502050000074&MerchOrderId=1422427034703&Amount=1.00&OrderDesc=测试商品&TradeTime=20150128143715&ExpTime=&NotifyUrl=http://192.168.1.117:8080/guajiLogin/support!yinlianOvali.guajilogin&ExtData=863437026064485,10,10&MiscData=&NotifyFlag=0
		// 私钥签名
		String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}

		//提交参数包含中文的需要做base64转码，通知地址做URLEncoder处理
		String orderDesc64 = Base64.encodeBytes(orderDesc.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
		String extData64 = Base64.encodeBytes(extData.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
		String miscData64 = Base64.encodeBytes(miscData.getBytes(ConstantsClient.PAYECO_DATA_ENCODE));
		String notifyUrlEn = URLEncoder.encode(notifyUrl, ConstantsClient.PAYECO_DATA_ENCODE);
		String data64 = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
				+ "&Amount=" + amount + "&OrderDesc=" + orderDesc64 + "&TradeTime=" + tradeTime + "&ExpTime="
				+ expTime + "&NotifyUrl=" + notifyUrlEn + "&ExtData=" + extData64
				+ "&MiscData=" + miscData64	+ "&NotifyFlag=" + notifyFlag;

		//通讯报文
		String url= payecoUrl + "/ppi/merchant/itf.do"; //下订单URL
		data64 = "TradeCode="+tradeCode+"&"+ data64 + "&Sign=" + sign;
		HttpClient httpClient = new HttpClient();

		//GameBackLogger.SYS_LOG.info("rquest yinlian url:"+url);
		//GameBackLogger.SYS_LOG.info("rquest yinlian body:"+data64);
		
		String retStr = httpClient.send(url, data64, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);

		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		//retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retOrderId = Tools.getXMLValue(retStr, "OrderId");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setTradeTime(tradeTime);
		retXml.setOrderId(retOrderId);
		retXml.setSign(retSign);
		
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
				+ "&Amount=" + retAmount + "&TradeTime=" + retTradeTime + "&OrderId=" + retOrderId;
		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);

		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}
	
	/**
	 * 商户订单查询接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param tradeTime		:	商户订单提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderQuery(String merchantId, String merchOrderId, String tradeTime, 
			String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "QueryOrder";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	             + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}


		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //查询订单URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();

		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);

		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		//retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retExtData = Tools.getXMLValue(retStr, "ExtData");
		if (retExtData != null){
			retExtData = retExtData.replaceAll(" ", "+");
			retExtData = new String (Base64.decode(retExtData), ConstantsClient.PAYECO_DATA_ENCODE);
		}
		String retOrderId = Tools.getXMLValue(retStr, "OrderId");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retPayTime = Tools.getXMLValue(retStr, "PayTime");
		String retSettleDate = Tools.getXMLValue(retStr, "SettleDate");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setExtData(retExtData);
		retXml.setOrderId(retOrderId);
		retXml.setStatus(retStatus);
		retXml.setPayTime(retPayTime);
		retXml.setSettleDate(retSettleDate);
		retXml.setSign(retSign);
		  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
		  + "&Amount=" + retAmount + "&ExtData=" + retExtData + "&OrderId=" + retOrderId
		  + "&Status=" + retStatus + "&PayTime=" + retPayTime + "&SettleDate=" + retSettleDate;

		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);

		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}
	
	
	/**
	 * 商户订单冲正接口
	 * @param merchantId:		商户代码
	 * @param merchOrderId	:	商户订单号
	 * @param amount        :   订单金额
	 * @param tradeTime		:	商户订单提交时间
	 * @param priKey		:	商户签名的私钥
	 * @param pubKey        :   易联签名验证公钥
	 * @param payecoUrl		：	易联服务器URL地址，只需要填写域名部分
	 * @param retXml        :   通讯返回数据；当不是通讯错误时，该对象返回数据
	 * @return 				: 处理状态码： 0000 : 处理成功， 其他： 处理失败
	 * @throws Exception    :  E101:通讯失败； E102：签名验证失败；  E103：签名失败；
	 */
	public static String OrderReverse(String merchantId, String merchOrderId, String amount, String tradeTime, 
			String priKey, String pubKey, String payecoUrl, Xml retXml) 
			throws Exception{
		//交易参数
		String tradeCode = "QuashOrder";
		String version = ConstantsClient.COMM_INTF_VERSION;
		
	    //进行数据签名
	    String signData = "Version="+version+"&MerchantId=" + merchantId + "&MerchOrderId=" + merchOrderId 
	             + "&Amount=" + amount + "&TradeTime=" + tradeTime;
	    
	    // 私钥签名
	    String sign = Signatory.sign(priKey, signData, ConstantsClient.PAYECO_DATA_ENCODE);
		if(Tools.isStrEmpty(sign)){
			throw new Exception("E103");
		}


		//通讯报文
	    String url= payecoUrl + "/ppi/merchant/itf.do?TradeCode="+tradeCode; //查询订单URL
	    signData = signData + "&Sign=" + sign;
	    HttpClient httpClient = new HttpClient();

		String retStr = httpClient.send(url, signData, ConstantsClient.PAYECO_DATA_ENCODE, ConstantsClient.PAYECO_DATA_ENCODE,
				ConstantsClient.CONNECT_TIME_OUT, ConstantsClient.RESPONSE_TIME_OUT);

		if(Tools.isStrEmpty(retStr)){
			throw new Exception("E101");
		}

		//返回数据的返回码判断
		//retXml.setXmlData(retStr);
		String retCode = Tools.getXMLValue(retStr, "retCode");
		retXml.setRetCode(retCode);
		retXml.setRetMsg(Tools.getXMLValue(retStr, "retMsg"));
		if(!"0000".equals(retCode)){
			return retCode;
		}
		
		//获取返回数据
		String retVer = Tools.getXMLValue(retStr, "Version");
		String retMerchantId = Tools.getXMLValue(retStr, "MerchantId");
		String retMerchOrderId = Tools.getXMLValue(retStr, "MerchOrderId");
		String retAmount = Tools.getXMLValue(retStr, "Amount");
		String retStatus = Tools.getXMLValue(retStr, "Status");
		String retTradeTime = Tools.getXMLValue(retStr, "TradeTime");
		String retSign = Tools.getXMLValue(retStr, "Sign");
		//设置返回数据
		retXml.setTradeCode(tradeCode);
		retXml.setVersion(retVer);
		retXml.setMerchantId(retMerchantId);
		retXml.setMerchOrderId(retMerchOrderId);
		retXml.setAmount(retAmount);
		retXml.setStatus(retStatus);
		retXml.setSign(retSign);
  
		//验证签名的字符串
		String backSign = "Version="+retVer+"&MerchantId=" + retMerchantId + "&MerchOrderId=" + retMerchOrderId 
				+ "&Amount=" + retAmount + "&Status=" + retStatus + "&TradeTime=" + retTradeTime;
		
		//验证签名
		retSign = retSign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, backSign, retSign, ConstantsClient.PAYECO_DATA_ENCODE);

		if(!b){
			throw new Exception("E102");
		}
		return retCode;
	}

	/**
	 * 验证订单结果通知签名
	 * @param version       ： 通讯版本号
	 * @param merchantId    ： 商户代码
	 * @param merchOrderId  ：商户订单号
	 * @param amount		： 商户订单金额
	 * @param extData		：商户保留信息； 通知结果时，原样返回给商户；字符最大128，中文最多40个；参与签名：采用UTF-8编码 ； 提交参数：采用UTF-8的base64格式编码
	 * @param orderId		：易联订单号
	 * @param status		：订单状态
	 * @param payTime		：订单支付时间
	 * @param settleDate	：订单结算日期
	 * @param sign			：签名数据
	 * @param pubKey		：易联签名验证公钥
	 * @return				： true：验证通过； false：验证不通过
	 * @throws Exception
	 */
	public static boolean bCheckNotifySign(String version, String merchantId, 
			String merchOrderId, String amount, String extData, String orderId, 
			String status, String payTime, String settleDate, String sign,
			String pubKey) 
			throws Exception{
		// 对extData进行转码处理: base64转码
		if (extData != null) {
			extData = extData.replaceAll(" ", "+");
			extData = new String(Base64.decode(extData), ConstantsClient.PAYECO_DATA_ENCODE);

		}
		 
		// 进行数据签名
		String data = "Version=" + version + "&MerchantId=" + merchantId
				+ "&MerchOrderId=" + merchOrderId + "&Amount=" + amount
				+ "&ExtData=" + extData + "&OrderId=" + orderId + "&Status="
				+ status + "&PayTime=" + payTime + "&SettleDate=" + settleDate;

		// 验证签名
		sign = sign.replaceAll(" ", "+");
		boolean b = Signatory.verify(pubKey, data, sign, ConstantsClient.PAYECO_DATA_ENCODE);

		return b;
	}
}
