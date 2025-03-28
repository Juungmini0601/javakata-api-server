package io.javakata.controller.image;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.image.response.UploadImageResponse;
import io.javakata.service.image.ImageService;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 28.
 */
@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	private static final String PROBLEM_IMAGE_PATH = "/problems/";

	@PostMapping("/api/v1/admin/image")
	public ApiResponse<?> uploadImage(@RequestParam("file") MultipartFile file) {
		String uploadedUrl = imageService.upload(file, PROBLEM_IMAGE_PATH);
		return ApiResponse.success(new UploadImageResponse(uploadedUrl));
	}
}
