(function($, window){
    var ajaxServices = function() {
        var services = {
            "login" : {url : "/login", method : "POST"},
            "register" : {url : "/register", method : "POST"},
            "forgetPasswd" : {url : "/forgetPasswd", method :"POST"},
            "loadList" : {url : "/loadList", method : "POST"},
            "startNew" : {url : "/proj/startNew", method : "POST"},
            "saveDraft" : {url : "/proj/saveDraft", method : "POST"},
            "saveEdit" : {url : "/proj/saveEdit", method : "POST"},
            
            "saveAction" : {url : "/saveAction", method : "POST"},
            "saveUserAction" : {url : "/saveUserAction", method : "POST"},
            "updateMessageStatus" : {url : "/updateMessageStatus", method : "POST"},
            
            "newComment" : {url : "/comment/newComment", method : "POST"},
            "delComment" : {url : "/comment/delComment", method : "POST"},
            
            "getUnivArray" : {url : "/user/getUnivArray", method : "POST"},
            "getSchool4Univ" : {url : "/user/getSchool4Univ", method : "POST"},
            "updatePersonalInfo" : {url : "/user/updatePersonalInfo", method : "POST"},
            "updateBankAccountInfo" : {url : "/user/updateBankAccountInfo", method : "POST"},
            "updatePasswd" : {url : "/user/updatePasswd", method : "POST"},
            "updateSomeInfo" : {url : "/user/updateSomeInfo", method : "POST"},
            
            "getProjViewListByPage" : {url : "/getProjViewListByPage", method : "POST"},
            "addNewProgress" : {url : "/proj/addNewProgress", method : "POST"},
            "updateProj" : {url : "/proj/updateProj", method : "POST"},
            
            "addNewAttention" : {url : "/addNewAttention", method : "POST"},
            "cancleAttention" : {url : "/cancleAttention", method : "POST"},
            "addNewMember" : {url : "/addNewMember", method : "POST"},
            
            "getAttentionList" : {url : "/getAttentionList", method : "POST"},
            "getBeAttentionList" : {url : "/getBeAttentionList", method : "POST"},
            "getRepayUserList" : {url : "/getRepayUserList", method : "POST"},
            
            "getLetterList" : {url : "/getLetterList", method : "POST"},
            "getMyProjList" : {url : "/getMyProjList", method : "POST"},
            "getMySupportList" : {url : "/getMySupportList", method : "POST"},
            "getMyFollowList" : {url : "/getMyFollowList", method : "POST"},
            
            "createNewWXPay" : {url : "/pay/createNewWXPay", method : "POST"},
            "createNewPay" : {url : "/pay/createNewPay", method : "POST"}, 
            "getWXPayQRCode" : {url : "/pay/getWXPayQRCode", method : "POST"},
            "hasWXPayed" : {url : "/pay/hasWXPayed", method : "POST"},
            
            "hasPersonalInfo" : {url : "/user/hasPersonalInfo", method : "POST"},
            "hasBankAccountInfo" : {url : "/user/hasBankAccountInfo", method : "POST"},
            
            "resetPasswd" : {url : "/resetPasswd" , method : "POST"},
            
            /** 明信片比赛 **/
            "startNewPost" : {url : "/act/startNewPost", method : "POST"},
            "savePostDraft" : {url : "/act/savePostDraft", method : "POST"},
            "savePostEdit" : {url : "/act/savePostEdit", method : "POST"}
        };

        $.each(services, function(funcName, reqParams) {
            services[funcName] = function(data, callback1, callback0) {
                return $.ajax({
                    method : reqParams.method,
                    url : reqParams.url,
                    cache : false,
                    dataType : 'JSON',
                    data : data,
                    success : function (resp) {
                        if (resp.code) {
                            typeof callback1 == "function" && callback1(resp.data);
                        } else {
                        	typeof callback0 == "function" && callback0(resp.msg, resp.data);
                        }
                    }
                });
            };
        });

        return services;
    }();
    window.ajaxServices = ajaxServices;
})(jQuery, window);
