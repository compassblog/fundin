package com.fundin.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundin.domain.dto.InviteSort;
import com.fundin.domain.type.RedPacketTypeEnum;
import com.fundin.service.common.Constants;
import com.fundin.service.impl.RedPacketService;
import com.fundin.service.impl.UserService;
import com.fundin.web.common.Resp;

@Controller
public class RedPacketController {
	private static final Logger LOG = LoggerFactory.getLogger(RedPacketController.class);

	@Resource
	RedPacketService redPacketService;
	@Resource
	UserService userService;

	@RequestMapping(value = "/redp/no1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object inviteNo1(@RequestParam(value = "inviteCount", required = true) Integer inviteCount,
			@RequestParam(value = "token", required = true) String token, HttpServletResponse response) {
		try {
			if (!Constants.DEFAULT_TOKEN.equals(token)) {
				response.getWriter().write("error token.");
				return null;
			}
			List<InviteSort> No1s = userService.getInviteNo1(inviteCount);
			List<InviteSort> sorted = userService.getInviteSort();
			if (No1s != null)
				for (InviteSort inviteNo1 : No1s) {
					redPacketService.createRedPacket(inviteNo1.getId(), RedPacketTypeEnum.REDPACKET_TYPE_Z.getCode(),
							Constants.REDPACKET_AMOUNT_200, 1);
				}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("No1", No1s);
			map.put("sort", sorted);
			return Resp.succ(map);
		} catch (Exception ex) {
			LOG.error("auditRet ex !", ex);
		}
		return Resp.fail(null);
	}
}
