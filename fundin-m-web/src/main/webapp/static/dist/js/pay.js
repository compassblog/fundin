"use strict";define(["Zepto","service","utils","wxClient"],function(a,b,c,d){function e(){var b=a("#payAmount").val();0==b.length?a("#payAmount").val(0):a("#payAmount").val(b.replace(/^0\d+|\D+/,""))}function f(){var b=a("#payAmount").val();a('input[name="redpacket"]:checked').each(function(){b-=a(this).val()}),b<=0?a("#support-amt").text(0):a("#support-amt").text(b),j=b}function g(){var b=a("#payAmount").val(),c=0;a('input[name="redpacket"]:checked').each(function(){c=Number(c)+Number(a(this).val()),b-=a(this).val()}),b<=0&&(a("#payAmount").val(c),j=0)}var h={title:projTitle,link:d.getOauthUrl("http://m.fundin.cn/pay/"+projId),imgUrl:projImgUrl},i={title:projTitle,desc:a("#projDesc").val(),link:d.getOauthUrl("http://m.fundin.cn/pay/"+projId),imgUrl:projImgUrl};d.isInWeixin&&(b.getJsApiConfig({url:window.location.href},function(a){d.wxConfig(a)}),d.wxConfigReady(function(){d.shareTimeline(h),d.shareAppMessage(i)})),d.isInWeixin?a("#support-now").tap(function(){var e=0,f="";a("input[name=redpacket]:checked").each(function(){e+=Number(a(this).val()),f+=a(this).attr("id").substring(9)+"_"});var g={projId:projId,openid:openid,rAmount:e,amount:j};c.showLoadingToast("提交中..."),j>0?b.createNewWXPayForM(g,function(a){c.hideLoadingToast(),d.chooseWXPay({timestamp:a.timeStamp,nonceStr:a.nonceStr,"package":a.pkg,signType:a.signType,paySign:a.paySign,success:function(a){location.href="http://m.fundin.cn/proj/"+projId}})},function(a){c.hideLoadingToast(),c.showToastMsg(a,"warn")}):e>0&&j<=0&&b.createNewPay({projId:projId,rAmount:e,redIds:f},function(a){a&&(location.href="http://m.fundin.cn/proj/"+projId)})}):a("#support-now").tap(function(){c.showToastMsg("请在微信中打开此网页进行支付操作","warn")}),a("input[name=repay-radio]").change(function(){a("#support-amt").text(a(this).attr("data-repay-amt"))}),a("input[name=repay-radio]:first").trigger("click"),a("#payAmount").keyup(function(){e(),f()}),a("#payAmount").blur(function(){e(),g()});var j=0;a('input[name="redpacket"]').click(function(){j=a(this).prop("checked")?Number(j)-Number(a(this).val()):Number(j)+Number(a(this).val()),j<0?a("#support-amt").text(0):a("#support-amt").text(j),g()})});