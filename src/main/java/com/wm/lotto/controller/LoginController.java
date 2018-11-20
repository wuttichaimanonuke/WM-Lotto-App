package com.wm.lotto.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wm.lotto.entity.general.LoginUser;
import com.wm.lotto.entity.general.RequestDataEntity;
import com.wm.lotto.entity.token_login.TokenLogin;
import com.wm.lotto.service.login.ILoginService;

@RestController
@RequestMapping("login_app")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private ILoginService loginService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loginApp(@RequestBody RequestDataEntity<LoginUser> seriesValue) {
		log.info("(POST) mapping to login : Begin.");
		log.info("@RequestBody : token = {}, dataValue = {}", seriesValue.getToken(),seriesValue.getDataValue());
		log.info("seriesValue = {}",seriesValue.toString());
		LoginUser loginUser = seriesValue.getDataValue().get(0);
		log.info("username = {} ,password = {}",loginUser.getUsername(),loginUser.getPassword());
		log.info("loginUser = {}",loginUser.toString());
		Map<String, Object> result = new HashMap<String, Object>();
		TokenLogin tokenLogin = new TokenLogin();
		try {
			tokenLogin = loginService.login(loginUser);
			result.put("token", tokenLogin.getTlToken());
			log.info("Service return token = "+tokenLogin.getTlToken());
			if (result.get("token") == null) {
				return new ResponseEntity<Map<String,Object>>(result, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(result, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String,Object>>(result, HttpStatus.FOUND);
	}
	
}
