package org.egov.utils;

import org.apache.poi.ss.usermodel.*;
import org.egov.masters.model.AccountEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

@Component
public class AccountExcelReader {

    public List<AccountEntity> parse(MultipartFile file) throws Exception {
        List<AccountEntity> accounts = new ArrayList<>();

       try {

//           FileInputStream fileInputStream   = new FileInputStream();

       } catch (Exception e) {
           e.printStackTrace();
       }


        return accounts;
    }

//    private String getString(Cell cell) {
//        if (cell == null) return "";
//        return switch (cell.getCellType()) {
//            case STRING -> cell.getStringCellValue();
//            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
//            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
//            default -> "";
//        };
//    }
}


