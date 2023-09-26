package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//HOME画面を表示する機能。
@Controller
class WelcomeController {

	@GetMapping("/")
	public String welcome() {
		return "welcome";
	}

}
