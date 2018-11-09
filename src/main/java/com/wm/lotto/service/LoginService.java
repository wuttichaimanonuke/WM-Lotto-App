package com.wm.lotto.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wm.lotto.entity.LoginUser;
import com.wm.lotto.entity.TokenLogin;
import com.wm.lotto.entity.Users;
import com.wm.lotto.repository.ITokenLoginDAO;
import com.wm.lotto.repository.IUsersDAO;

@Service
public class LoginService implements ILoginService {

	private static final Logger log = LoggerFactory.getLogger(LoginService.class);
	
	@Autowired
	private IUsersDAO usersDAO;
	
	@Autowired
	private ITokenLoginDAO tokenLoginDAO;

	@Override
	public String login(LoginUser loginUser) {
		log.info("(SERVICE) Service login begin.");
		String resultUuidToken = null;
		String userId = null;
		Users user = new Users();
		log.info("User ("+loginUser.getUsername()+") is request login.");
		if (loginUser != null) {
			user.setUUsername(loginUser.getUsername());
			user.setUPassword(loginUser.getPassword());
			try {
				userId = usersDAO.checkUserLogin(user);
				log.info("User ("+userId+") is found.");
			} catch (Exception e) {
				log.info("User ("+userId+") is't found.");
				log.error("ERROR ("+user.getUUsername()+") : UsersDAO : checkUserLogin : "+e.getMessage());
			}
			if (userId != null) {
//				check has token of user in Table TokenLogin.
//				IF (TRUE) has token of user in table TokenLogin then Delete old token.
				TokenLogin tokenLogin = new TokenLogin();
				try {
					tokenLogin = tokenLoginDAO.getTokenLoginByUserId(userId);
					if (tokenLogin != null) {
						log.info("User ("+userId+") is still live in system. Do it clear old token.");
						try {
							tokenLoginDAO.delectTokenLoginByUserId(tokenLogin.getUId());
							log.info("Clear token user ("+userId+") success.");
						} catch (Exception e) {
							log.info("Clear token user ("+userId+") fail. : "+e.getMessage());
						}
					} else {
						log.info("User ("+userId+") is't live in system.");
					}
				} catch (Exception e) {
					log.error("ERROR : LoginService : login : "+e.getMessage());
				}
//				Insert new token.
				UUID uuid = UUID.randomUUID();
				tokenLogin = new TokenLogin();
				tokenLogin.setTlToken(uuid.toString());
				tokenLogin.setUId(userId);
				try {
					tokenLoginDAO.insertTokenLogin(tokenLogin);
					log.info("User ("+userId+") login success.");
					resultUuidToken = tokenLogin.getTlToken();
					log.info("Token = "+tokenLogin.getTlToken());
				} catch (Exception e) {
					log.info("User ("+userId+") login fail. : "+e.getMessage());
				}
			}
		}
		return resultUuidToken;
	}

}
