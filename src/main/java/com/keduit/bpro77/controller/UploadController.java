package com.keduit.bpro77.controller;

import com.keduit.bpro77.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {
	@Value("{com.keduit.upload.path}")
	private String uploadPath;
	
	@PostMapping("/uploadAjax")
	public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) throws Exception {
		
		List<UploadResultDTO> resultDTOList = new ArrayList<>();
		for (MultipartFile uploadFile : uploadFiles) {
			if (!uploadFile.getContentType().startsWith("image")) {
				log.warn("이미지 파일을 넣어주세요");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			String originalName = uploadFile.getOriginalFilename();
			String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
			log.info("FileName========>" + fileName);
			String folderPath = makeFolder();
			String uuid = UUID.randomUUID().toString();
			String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
			Path savePath = Paths.get(saveName);
			uploadFile.transferTo(savePath);
			resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
		}
		return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
	}
	
	private String makeFolder() {
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String folderPath = str.replace("//", File.separator);
		File uploadPathFolder = new File(uploadPath, folderPath);
		if (!uploadPathFolder.exists()) {
			uploadPathFolder.mkdirs();
		}
		return folderPath;
	}
	
	@GetMapping("/display")
	public ResponseEntity<byte[]> getFile(String fileName) {
		ResponseEntity<byte[]> result = null;
		try {
			String srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
			log.info("fileName : " + fileName);
			File file = new File(uploadPath + File.separator + srcFileName);
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
}
