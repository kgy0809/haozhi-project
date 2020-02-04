package com.haozhi.greenroom.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Excel<T> {
    public Excel(){}

    public List<T> getRes(Class cls, InputStream in) throws IllegalAccessException, InstantiationException {
       List<T> lists=new ArrayList<T>();
//        File xlsFile = new File(file);
        // 获得工作簿
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(in);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            // 获得行数
            int rows = sheet.getLastRowNum() + 1;
            // 获得列数，先获得一行，在得到改行列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }
            int cols = tmp.getPhysicalNumberOfCells();
            //获取头
            Row r1 = sheet.getRow(0);

            // 读取数据
            for (int row = 1; row < rows; row++)
            {
                Object object=cls.newInstance();
                Field[] fields=object.getClass().getDeclaredFields();
                Row r = sheet.getRow(row);
                System.out.println(row);
                for (int col = 0; col < cols; col++)
                {
                    Integer type=r.getCell(col).getCellType();
                    String row1=r1.getCell(col).getStringCellValue();
                    for (Field field:fields){
                       if(field.getName().equals(row1)){
                           field.setAccessible(true);
                           if(type==Cell.CELL_TYPE_STRING){
                               field.set(object,r.getCell(col).getStringCellValue());
                               break;
                           }else if(type== Cell.CELL_TYPE_NUMERIC){
                               if(field.getGenericType().toString().equals("Integer")){
                                   field.set(object,(int)r.getCell(col).getNumericCellValue());
                                   break;
                               }else if(field.getGenericType().toString().equals("Double")){
                                   field.set(object,(double)r.getCell(col).getNumericCellValue());
                                   break;
                               }else{
                                   break;
                               }

                           }else{
//                               field.set(row1,"");
                               break;
                           }
                       }else{
                           continue;
                        }
                    }

                }
                lists.add((T) object);
            }
        }
        return lists;
    }

}

