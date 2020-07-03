package com.acrobat.eureka.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xutao
 * @date 2018-11-16 14:39
 */
@Controller
@RequestMapping("/upload")
public class FileUploadController {


    /**
     * 文件上传微服务
     *
     * 如果通过zuul网关的服务id调用这个上传服务，只能上传小文件（1M）
     * 上传大文件需要在上传路径中加上前缀：/zuul/**，这里是/zuul/upload/common
     */
    @PostMapping("/common")
    @ResponseBody
    public String uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        StringBuilder sb = new StringBuilder("");

        // 文件非空检查
        if (file == null) {
            sb.append("failed upload because of empty file").append("\n");
            return sb.toString();
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            sb.append("failed upload because of empty file name").append("\n");
            return sb.toString();
        }

        byte[] bytes = file.getBytes();
        File fileToSave = new File(fileName);
        FileCopyUtils.copy(bytes, fileToSave);
        return fileToSave.getAbsolutePath();
    }


    /**
     * 使用poi上传excel
     */
    @PostMapping("/excel")
    @ResponseBody
    public String uploadExcel(@RequestParam("file")MultipartFile file) {
        StringBuilder sb = new StringBuilder("");

        // 文件非空检查
        if (file == null) {
            sb.append("failed upload because of empty file").append("\n");
            return sb.toString();
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            sb.append("failed upload because of empty file name").append("\n");
            return sb.toString();
        }
        sb.append("upload filename: ").append(fileName);

        // 检查文件名
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            sb.append("failed upload because of wrong file format").append("\n");
            return sb.toString();
        }

        try {
            // 建立poi实例
            boolean isExcel2003 = fileName.matches("^.+\\.(?i)(xls)$");
            InputStream is = file.getInputStream();
            Workbook workbook = null;
            if (isExcel2003) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }

            // 读取excel
            int sheetNum = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // 遍历每一行，注意：第 0 行为标题
                int rowNum = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rowNum; j++) {
                    Row row = sheet.getRow(j);

                    int cellNum = row.getPhysicalNumberOfCells();
                    for (int k=0; k<cellNum; k++) {
                        Cell cell = row.getCell(k);

                        sb.append(cell).append("   ");
                    }
                    sb.append("\n");
                }
            }
        } catch (IOException e) {
            sb.append("failed upload because of file reading exception").append("\n");
        }

        return sb.toString();
    }
}
