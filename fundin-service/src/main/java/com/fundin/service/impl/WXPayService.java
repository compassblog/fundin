package com.fundin.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fundin.dao.mybatis.FundinPayRecordDao;
import com.fundin.dao.mybatis.FundinProjDao;
import com.fundin.dao.mybatis.WXPayOrderDao;
import com.fundin.domain.entity.FundinPayRecord;
import com.fundin.domain.entity.FundinProj;
import com.fundin.domain.entity.WXPayOrder;
import com.fundin.domain.type.RedPacketStatusEnum;
import com.fundin.pay.wx.WXCustomSSL;
import com.fundin.pay.wx.WXHelper;
import com.fundin.pay.wx.WXNotifyReq;
import com.fundin.pay.wx.WXNotifyResp;
import com.fundin.pay.wx.WXOrderReq;
import com.fundin.pay.wx.WXOrderResp;
import com.fundin.pay.wx.WXPayConstants;
import com.fundin.pay.wx.WXRefundReq;
import com.fundin.pay.wx.WXRefundResp;
import com.fundin.service.common.Constants;
import com.fundin.service.common.RedisConstants;
import com.fundin.service.common.RedisUtil;
import com.fundin.service.common.ServiceException;
import com.fundin.utils.common.Clock;
import com.fundin.utils.common.DateUtil;
import com.fundin.utils.common.MD5Util;
import com.fundin.utils.common.QRUtils;
import com.fundin.utils.common.StringUtil;
import com.fundin.utils.https.SSLUtils;
import com.fundin.utils.mapper.JaxbMapper;
import com.google.zxing.common.BitMatrix;

@Component("wxPayService")
public class WXPayService {

	private static final Logger LOG = LoggerFactory.getLogger(WXPayService.class);
	
	@Resource
	private ProjService projService;
	@Resource
	private FundinProjDao projDao;
	@Resource
	private FundinPayRecordDao payRecordDao;
	@Resource
	private WXPayOrderDao wxPayOrderDao;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private RedPacketService redPacketService;
	
	@Transactional(rollbackFor = Exception.class)
	public String createNewWXPayForM(Long projId, Long userId, String openid, String createIp, Integer amount,
			Integer rAmount) {
		FundinProj proj = projService.getProj(projId);
		
		WXOrderReq orderReq = initOrderReq();
		orderReq.setTrade_type(WXPayConstants.WX_TRADE_JS_API);
		orderReq.setSpbill_create_ip(createIp);
		orderReq.setOpenid(openid);
		orderReq.setBody(proj.getTitle() + " --- Fundin.cn众筹");
		orderReq.setDetail(proj.getTitle());
		
		orderReq.setOut_trade_no(WXHelper.generateTradeNo());
		orderReq.setProduct_id(WXHelper.generateProductId(proj.getId()));
		orderReq.setTotal_fee(String.valueOf(amount * 100));
		
		orderReq.setNonce_str(StringUtil.generateRandomString(32));
		orderReq.setSign(generateReqSign(orderReq));
		
		WXOrderResp orderResp = postToWX(orderReq);
		checkRespSign(orderResp);
		checkRespCode(orderResp);
		
		WXPayOrder payOrder = createPayOrder(orderReq, orderResp, projId, userId,amount, rAmount);
		payOrder.setOpenid(openid);
		if (Constants.INSERT_SUCCESS == wxPayOrderDao.createNewOrder(payOrder)) {
			return orderResp.getPrepay_id();
		}
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Long createNewWXPay(Long projId, Long userId, Integer amount, Integer rAmount) {
		FundinProj proj = projService.getProj(projId);

		WXOrderReq orderReq = initOrderReq();
		orderReq.setTrade_type(WXPayConstants.WX_TRADE_NATIVE);
		orderReq.setSpbill_create_ip(WXPayConstants.WX_CREATE_IP);
		orderReq.setBody(proj.getTitle() + " --- Fundin.cn众筹");
		orderReq.setDetail(proj.getTitle());

		orderReq.setOut_trade_no(WXHelper.generateTradeNo());
		orderReq.setProduct_id(WXHelper.generateProductId(proj.getId()));
		orderReq.setTotal_fee(String.valueOf(amount * 100));

		orderReq.setNonce_str(StringUtil.generateRandomString(32));
		orderReq.setSign(generateReqSign(orderReq));

		WXOrderResp orderResp = postToWX(orderReq);
		checkRespSign(orderResp);
		checkRespCode(orderResp);

		WXPayOrder payOrder = createPayOrder(orderReq, orderResp, projId, userId, amount, rAmount);
		if (Constants.INSERT_SUCCESS == wxPayOrderDao.createNewOrder(payOrder)) {
			return payOrder.getId();
		}
		return null;
	}
	
	public Long createNewPay(Long projId, Long userId, Integer rAmount, String redIds) {
		FundinProj proj = projService.getProj(projId);

		FundinPayRecord payRecord = new FundinPayRecord();
		payRecord.setProjId(proj.getId());
		payRecord.setUserId(userId);
		payRecord.setOrderId(null);
		payRecord.setAmount(0);
		payRecord.setWay(WXPayConstants.PAY_WAY_OF_WX);
		payRecord.setTime(new Date());
		payRecord.setPrimaryAmount(rAmount);
		payRecord.setRedpacketAmount(rAmount);
		if (Constants.INSERT_SUCCESS == payRecordDao.addPayRecord(payRecord)) {
			updateRedPacketInfo(proj.getId(), redIds);
			updateProjInfo(payRecord);
			return payRecord.getId();
		}
		return null;
	}
	
	private void updateRedPacketInfo(Long projId, String redIds) {
		Date now = new Date();
		for (String id : redIds.split("_")) {
			if (!"".equals(id))
				redPacketService.updateInfoById(Long.parseLong(id), projId, RedPacketStatusEnum.REDPACKET_USED.getCode(),
						now);
		}
	}

	private void updateRedPacketInfo(Long userId, Long projId, Integer amount) {
		Date now = new Date();
		switch (amount) {
		case 5:
			redPacketService.updateInfoByAmount(userId, projId, 5, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		case 25:
			redPacketService.updateInfoByAmount(userId, projId, 25, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		case 30:
			redPacketService.updateInfoByAmount(userId, projId, 25, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			redPacketService.updateInfoByAmount(userId, projId, 2, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		case 200:
			redPacketService.updateInfoByAmount(userId, projId, 200, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		case 205:
			redPacketService.updateInfoByAmount(userId, projId, 5, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			redPacketService.updateInfoByAmount(userId, projId, 200, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		case 225:
			redPacketService.updateInfoByAmount(userId, projId, 25, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			redPacketService.updateInfoByAmount(userId, projId, 200, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		case 230:
			redPacketService.updateInfoByAmount(userId, projId, 5, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			redPacketService.updateInfoByAmount(userId, projId, 25, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			redPacketService.updateInfoByAmount(userId, projId, 200, RedPacketStatusEnum.REDPACKET_USED.getCode(), now);
			break;
		default:
			break;
		}
	}

	private WXPayOrder createPayOrder(WXOrderReq orderReq, WXOrderResp orderResp, Long projId,
			Long userId, Integer amount, Integer rAmount) {
		WXPayOrder payOrder = new WXPayOrder();
		payOrder.setUserId(userId);
		payOrder.setProjId(projId);
		payOrder.setOut_trade_no(orderReq.getOut_trade_no());
		payOrder.setTotal_fee(orderReq.getTotal_fee());
		payOrder.setTime_start(orderReq.getTime_start());
		payOrder.setTime_expire(orderReq.getTime_expire());
		payOrder.setProduct_id(orderReq.getProduct_id());
		payOrder.setCode_url(orderResp.getCode_url());
		payOrder.setPrepay_id(orderResp.getPrepay_id());
		payOrder.setStatus(WXPayConstants.WX_ORDER_STATUS_UNPAYED);
		payOrder.setPrimaryAmount(amount + rAmount);
		payOrder.setRedpacketAmount(rAmount);
		return payOrder;
	}
	
	private WXOrderResp postToWX(WXOrderReq orderReq) {
		String reqBodyXml = JaxbMapper.toXml(orderReq, Constants.DEFAULT_ENCODING);
		LOG.warn("WXOrder reqBody : {}", reqBodyXml);
		String respBodyXml = SSLUtils.post(WXPayConstants.WX_ORDER_URL, reqBodyXml);
		LOG.warn("WXOrder respBody : {}", respBodyXml);
		return JaxbMapper.fromXml(respBodyXml, WXOrderResp.class);
	}

	private WXOrderReq initOrderReq() {
		WXOrderReq orderReq = new WXOrderReq();
		orderReq.setAppid(WXPayConstants.WX_APP_ID);
		orderReq.setMch_id(WXPayConstants.WX_MCH_ID);
		orderReq.setDevice_info(WXPayConstants.WX_DEVICE_INFO);
		orderReq.setFee_type(WXPayConstants.WX_FEE_TYPE);
		orderReq.setNotify_url(WXPayConstants.WX_NOTIFY_URL);
		orderReq.setTime_start(DateUtil.dateFormat(Clock.DEFAULT.getCurrentDate(), 
				DateUtil.DATEFORMAT_TRIM));
		orderReq.setTime_expire(DateUtil.dateFormat(DateUtil.getNextHourBegin(
				Clock.DEFAULT.getCurrentDate(), 1), DateUtil.DATEFORMAT_TRIM));
		return orderReq;
	}

	private String generateReqSign(Object reqObj) {
		String sign = WXHelper.generateSign(reqObj);
		String stringSignTemp = String.format("%s&key=%s", sign, WXPayConstants.WX_API_KEY);
		return MD5Util.MD5(stringSignTemp).toUpperCase();
	}

	private void checkRespSign(WXOrderResp orderResp) {
		String originalSign = orderResp.getSign();
		orderResp.setSign(null);
		
		String sign = WXHelper.generateSign(orderResp);
		String stringSignTemp = String.format("%s&key=%s", sign, WXPayConstants.WX_API_KEY);
		String checkSign = MD5Util.MD5(stringSignTemp).toUpperCase();
		
		orderResp.setSign(originalSign);
		if (! checkSign.equals(originalSign)) {
			throw new RuntimeException("createNewWXPay checkRespSign error !");
		}
	}
	
	private void checkRespCode(WXOrderResp orderResp) {
		if (WXPayConstants.WX_RETURN_CODE.equals(orderResp.getReturn_code()) && 
				WXPayConstants.WX_RESULT_CODE.equals(orderResp.getResult_code())) {
			return ;
		}
		throw new RuntimeException("createNewWXPay checkRespCode error !");
	}
	
	@Transactional(rollbackFor = Exception.class)
	public String wxPayNotify(String notifyXml) {
		LOG.warn("wxPayNotify notifyReq : {}", notifyXml);
		WXNotifyReq notifyReq = JaxbMapper.fromXml(notifyXml, WXNotifyReq.class);
		checkNotifySign(notifyReq);
		checkNotifyCode(notifyReq);
		
		if (Constants.UPDATE_SUCCESS != wxPayOrderDao.updateFinishedOrder(
				notifyReq.getTransaction_id(), notifyReq.getOpenid(), 
				WXPayConstants.WX_ORDER_STATUS_PAYED, Long.valueOf(notifyReq.getOut_trade_no()))) {
			throw new ServiceException("wxPayNotify wxPayOrderDao updateFinishedOrder "
					+ "failed !!!");
		};
		
		WXPayOrder orderPay = wxPayOrderDao.getPayOrder(Long.valueOf(notifyReq.getOut_trade_no()));
		if (null == orderPay) {
			throw new ServiceException("wxPayNotify wxPayOrderDao getPayOrder "
					+ "failed !!!");
		}
		FundinPayRecord payRecord = addPayRecord(orderPay);
		updateProjInfo(payRecord);
		updateRedPacketInfo(payRecord.getUserId(), payRecord.getProjId(), payRecord.getRedpacketAmount());
		
		return generateNotifyResp();
	}

	private String generateNotifyResp() {
		WXNotifyResp notifyResp = new WXNotifyResp();
		notifyResp.setReturn_code(WXPayConstants.WX_RETURN_CODE);
		notifyResp.setReturn_msg(WXPayConstants.WX_RETURN_MSG);
		
		return JaxbMapper.toXml(notifyResp, Constants.DEFAULT_ENCODING);
	}
	
	private synchronized void updateProjInfo(FundinPayRecord payRecord) {
		FundinProj proj = projService.getProj(payRecord.getProjId());
		proj.setRaisedAmount(proj.getRaisedAmount() + payRecord.getAmount() + payRecord.getRedpacketAmount());
		proj.setRaisedNum(proj.getRaisedNum() + 1);
		BigDecimal raisedRatio = BigDecimal.valueOf(proj.getRaisedAmount()).divide(
				BigDecimal.valueOf(proj.getTotalAmount()), 2, RoundingMode.DOWN);
		proj.setRaisedRatio(raisedRatio);
		
		if (Constants.UPDATE_SUCCESS != projDao.updateRaised(proj)) {
			throw new ServiceException("wxPayNotify projDao updateRaised "
					+ "failed !!!");
		}
		redisUtil.del(RedisConstants.PROJ_ID_KEY + payRecord.getProjId());
	}

	private FundinPayRecord addPayRecord(WXPayOrder orderPay) {
		FundinPayRecord payRecord = new FundinPayRecord();
		payRecord.setUserId(orderPay.getUserId());
		payRecord.setProjId(orderPay.getProjId());
		payRecord.setOrderId(orderPay.getId());
		payRecord.setAmount(Integer.valueOf(orderPay.getTotal_fee()) / 100);
		payRecord.setWay(WXPayConstants.PAY_WAY_OF_WX);
		payRecord.setTime(Clock.DEFAULT.getCurrentDate());
		payRecord.setPrimaryAmount(orderPay.getPrimaryAmount());
		payRecord.setRedpacketAmount(orderPay.getRedpacketAmount());
		
		if (Constants.INSERT_SUCCESS != payRecordDao.addPayRecord(payRecord)) {
			throw new ServiceException("wxPayNotify payRecordDao addPayRecord "
					+ "failed !!!");
		}
		return payRecord;
	}

	private void checkNotifySign(WXNotifyReq notifyReq) {
		String originalSign = notifyReq.getSign();
		notifyReq.setSign(null);
		
		String sign = WXHelper.generateSign(notifyReq);
		String stringSignTemp = String.format("%s&key=%s", sign, WXPayConstants.WX_API_KEY);
		String checkSign = MD5Util.MD5(stringSignTemp).toUpperCase();
		
		notifyReq.setSign(originalSign);
		if (! checkSign.equals(originalSign)) {
			throw new RuntimeException("wxPayNotify checkNotifySign error !");
		}
	}
	
	private void checkNotifyCode(WXNotifyReq notifyReq) {
		if (WXPayConstants.WX_RETURN_CODE.equals(notifyReq.getReturn_code()) && 
				WXPayConstants.WX_RESULT_CODE.equals(notifyReq.getResult_code())) {
			return ;
		}
		throw new RuntimeException("wxPayNotify checkNotifyCode error !");
	}

	private static int QRCodeWidth = 200;
	private static int QRCodeHeight = 200;
	
	public void getWXPayQRCode(Long wxPayId, OutputStream output) throws Exception {
		WXPayOrder payOrder = wxPayOrderDao.getPayOrderById(wxPayId);
		BitMatrix bitMatrix = QRUtils.createQRCode(payOrder.getCode_url(), 
				QRCodeWidth, QRCodeHeight);
		QRUtils.writeToStream(bitMatrix, output);
	}
	
	public boolean hasWXPayed(Long wxPayId) {
		WXPayOrder payOrder = wxPayOrderDao.getPayOrderById(wxPayId);
		if (payOrder.getStatus() == WXPayConstants.WX_ORDER_STATUS_PAYED) {
			return true;
		}
		return false;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void wxRefund(Long projId) {
		List<WXPayOrder> payOrderList = wxPayOrderDao.getPayOrderByProjId(projId);
		for (WXPayOrder payOrder : payOrderList) {
			WXRefundReq refundReq = initRefundReq(payOrder);
			refundReq.setOut_refund_no(WXHelper.generateTradeNo());
			refundReq.setNonce_str(StringUtil.generateRandomString(32));
			refundReq.setSign(generateReqSign(refundReq));
			
			WXRefundResp refundResp = postToWXRefund(refundReq);
			checkRefundSign(refundResp);
			checkRefundCode(refundResp);
			
			if (Constants.UPDATE_SUCCESS != wxPayOrderDao.updateRefundOrder(refundResp.getOut_refund_no(), 
					refundResp.getRefund_id(), WXPayConstants.WX_ORDER_STATUS_REFUNDED, 
					Long.valueOf(refundResp.getOut_trade_no()))){
				throw new ServiceException("wxRefund wxPayOrderDao updateRefundOrder "
						+ "failed !!!");
			}
		}
	}

	private WXRefundResp postToWXRefund(WXRefundReq refundReq) {
		String reqBodyXml = JaxbMapper.toXml(refundReq, Constants.DEFAULT_ENCODING);
		LOG.warn("WXRefund reqBody : {}", reqBodyXml);
		String respBodyXml = WXCustomSSL.post(WXPayConstants.WX_REFUND_URL, reqBodyXml);
		LOG.warn("WXRefund respBody : {}", respBodyXml);
		return JaxbMapper.fromXml(respBodyXml, WXRefundResp.class);
	}
	
	private void checkRefundSign(WXRefundResp refundResp) {
		/** 微信bug，本地处理办法 **/
		refundResp.setRefund_channel(null);
		
		String originalSign = refundResp.getSign();
		refundResp.setSign(null);
		
		String sign = WXHelper.generateSign(refundResp);
		String stringSignTemp = String.format("%s&key=%s", sign, WXPayConstants.WX_API_KEY);
		String checkSign = MD5Util.MD5(stringSignTemp).toUpperCase();
		
		refundResp.setSign(originalSign);
		if (! checkSign.equals(originalSign)) {
			throw new RuntimeException("wxRefund checkRefundSign error !");
		}
	}
	
	private void checkRefundCode(WXRefundResp refundResp) {
		if (WXPayConstants.WX_RETURN_CODE.equals(refundResp.getReturn_code()) && 
				WXPayConstants.WX_RESULT_CODE.equals(refundResp.getResult_code())) {
			return ;
		}
		throw new RuntimeException("wxRefund checkRefundCode error !");
	}
	
	private WXRefundReq initRefundReq(WXPayOrder payOrder) {
		WXRefundReq refundReq = new WXRefundReq();
		refundReq.setAppid(WXPayConstants.WX_APP_ID);
		refundReq.setMch_id(WXPayConstants.WX_MCH_ID);
		refundReq.setDevice_info(WXPayConstants.WX_DEVICE_INFO);
		refundReq.setTransaction_id(payOrder.getTransaction_id());
		refundReq.setTotal_fee(payOrder.getTotal_fee());
		refundReq.setRefund_fee(payOrder.getTotal_fee());
		refundReq.setRefund_fee_type(WXPayConstants.WX_FEE_TYPE);
		refundReq.setOp_user_id(WXPayConstants.WX_MCH_ID);
		return refundReq;
	}
}
