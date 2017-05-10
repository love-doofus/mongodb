package com.wang.mongo.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author wxe
 * @since 1.0.0
 */
public final class ExcelUtils {

    public static Workbook getXSSFWorkbook(InputStream stream) throws IOException {
        return new XSSFWorkbook(stream);
    }

    public static Workbook getHSSFWorkbook(InputStream stream) throws IOException {
        return new HSSFWorkbook(stream);
    }

    public static Workbook getWorkbook(InputStream stream) {
        Workbook workbook = null;
        try {
            workbook = getXSSFWorkbook(stream);
        } catch (Exception ex) {
            try {
                workbook = getHSSFWorkbook(stream);
            } catch (Exception ex1) {
                return null;
            } finally {
                if (null != stream) {
                    try {
                        stream.close();
                    } catch (IOException ex2) {
                    }
                }
            }
        }
        if (null != stream) {
            try {
                stream.close();
            } catch (Exception ex) {
            }
        }

        return workbook;
    }

    /**
     * excel转变成list(指定sheet指定行开始封装，默认为第0个sheet的第0行)
     *
     * @return
     */
    public static List<List<String>> getExcelToList(Workbook workbook, Integer... idx) {
        List<List<String>> result = new ArrayList<List<String>>();
        Sheet sheet = null;
        if (idx.length == 0) {
            sheet = workbook.getSheetAt(0);
        } else {
            sheet = workbook.getSheetAt(idx[0]);
        }
        int i = 0;
        if (idx.length > 0 && idx.length > 1) {
            i = idx[1];
        }

        for (; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String value = null;
            List<String> values = new ArrayList<String>();
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);

                    if (cell == null) {
                        value = "";
                    } else {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                value = new DecimalFormat("#.##").format(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                value = String.valueOf(cell.getBooleanCellValue());
                                break;
                            default:
                                value = "";
                                break;
                        }
                    }
                    values.add(StringUtils.trimToEmpty(value));
                }

//                while (values.remove(""));
                if (!values.isEmpty()) {
                    result.add(values);
                }
            }
        }

        return result;
    }

}

