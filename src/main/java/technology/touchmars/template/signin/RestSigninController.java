/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package technology.touchmars.template.signin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import technology.touchmars.template.model.LoginAccount;

@RestController
public class RestSigninController {

	@PostMapping(value="/rest/signin")
	public void signin(@Valid @RequestBody LoginAccount loginAccount, BindingResult formBinding, WebRequest request, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:8100");
		System.out.println("rest signin-ing!");
	}
	
	@GetMapping(value="/rest/test")
	public Object test(@AuthenticationPrincipal Object identity, HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("lalala", "lalalla");
		return identity;
	}

}
