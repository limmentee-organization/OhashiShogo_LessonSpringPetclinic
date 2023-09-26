package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//キャッシュのリクエストを受け取る機能

@Controller
class CrashController {

	// /oupsのリクエストを受け取った場合、エラーが意図的に発生するようになっている。
	@GetMapping("/oups")
	public String triggerException() {
		throw new RuntimeException(
			"Expected: controller used to showcase what " + "happens when an exception is thrown");
	}

}
