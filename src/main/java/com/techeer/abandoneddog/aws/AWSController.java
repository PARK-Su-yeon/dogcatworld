package com.techeer.abandoneddog.aws;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aws")
public class AWSController {
	@GetMapping("")
	public ResponseEntity<?> successHealthCheck() {
		return ResponseEntity.ok().body("AWS Health Check Success");
	}
}
