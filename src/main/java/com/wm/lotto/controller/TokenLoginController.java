package com.wm.lotto.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wm.lotto.entity.general.RequestDataEntity;
import com.wm.lotto.service.token.ITokenService;

@RestController
@RequestMapping("tokenlogin")
public class TokenLoginController {

	private static final Logger log = LoggerFactory.getLogger(TokenLoginController.class);
	
	@Autowired
	private ITokenService tokenService;
	
	@RequestMapping(value = "/chekThisToken", method = RequestMethod.POST)
	public ResponseEntity<?> checkThisToken(@RequestBody RequestDataEntity<String> seriesValue) {
		log.info("(POST) mapping to chekThisToken : Begin.");
		log.info("@RequestBody : token = {}, dataValue = {}", seriesValue.getToken(),seriesValue.getDataValue());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("result", tokenService.checkThisToken(seriesValue.getToken())); 
			log.info("Service return result[{}].",result);
		} catch (Exception e) {
			result.put("result", false);
			log.info("Service chekThisToken is error. : {}",e.getMessage());
		}
		return new ResponseEntity<Map<String,Object>>(result, HttpStatus.OK);
	}
}