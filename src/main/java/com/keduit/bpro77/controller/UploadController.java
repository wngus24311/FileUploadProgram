package com.keduit.bpro77.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.keduit.bpro77.dto.UploadResultDTO;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keduit.bpro77.dto.UploadResultDTO;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class UploadController {
	
	@Value("${com.keduit.upload.path}")
	private String uploadPath;
	
	//	@PostMapping("/uploadAjax")
//	public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
	@PostMapping("/uploadAjax")
	public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
		
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
			try {
				uploadFile.transferTo(savePath);
				// 썸네일 생성
				String thumbnailSave = uploadPath + File.separator
						+ folderPath + File.separator + "s_" + uuid + "_" + fileName;
				File thumbnailFile = new File(thumbnailSave);
				Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);
			}catch (IOException e) {
				log.error(e.getMessage());
			}
			resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
			System.out.println("result" + resultDTOList);
			System.out.println("saveName" + saveName);
			System.out.println("savePath" + savePath);
		}
		return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
	}
	
	private String makeFolder() {
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		
		String folderPath = str.replace("//", File.separator);
		
		File uploadPathFolder = new File(uploadPath, folderPath);
		
		if (uploadPathFolder.exists() == false) {
			uploadPathFolder.mkdirs();
		}
		
		return folderPath;
	}
	
	@GetMapping("/display")
	public ResponseEntity<byte[]> getFile(String fileName){
		
		log.info("타나?");
		ResponseEntity<byte[]> result = null;
		
		try {
			String srcFileName = URLDecoder.decode(fileName, "UTF-8");
			
			log.info("fileName : " + srcFileName);
			
			File file = new File(uploadPath + File.separator + srcFileName);
			
			log.info(file);
			
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
					header,
					HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	
	@PostMapping("/removeFile")
	public ResponseEntity<Boolean> removeFile(String fileName) {
		String srcFileName = null;
		try {
			srcFileName = URLDecoder.decode(fileName, "UTF-8");
			
			File file = new File(uploadPath + File.separator + srcFileName);
			boolean result = file.delete();
			
			File thumbnail = new File(file.getParentFile(), "s_" + file.getName());
			result = thumbnail.delete();
			
			return new ResponseEntity<Boolean>(result, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}