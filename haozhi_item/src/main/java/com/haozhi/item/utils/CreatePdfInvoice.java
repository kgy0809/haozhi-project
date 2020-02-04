package com.haozhi.item.utils;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Table;
import com.spire.doc.fields.Field;

import java.io.InputStream;
import java.net.URL;

public class CreatePdfInvoice {

    public static void main(String[] args) {

        //加载Word模板文档
        Document doc = new Document();
        doc.loadFromFile("C:\\Users\\Administrator\\Desktop\\haozhi_project\\haozhi_item\\src\\main\\resources\\static\\data\\1.docx");

        //替换文档中以#开头的文本
        doc.replace("#tihuanziduan", "小伟", true, true);
        doc.replace("#jfGsName", "13601234567", true, true);
        doc.replace("#sbName", "北京市海淀区幸福小区1幢2单元3号", true, true);
        doc.replace("#zjPrice", "2019-05-30", true, true);
        doc.replace("#dxZjPrice", "2516595027", true, true);

        //定义客户购买数据
        String[][] purchaseData = {

                new String[]{"1023","华为 P30 Pro （8G+128G）全网通","1","4288"},
                new String[]{"1429","华为Watch GT运动版","2","1288"},
                new String[]{"1268","华为无线耳机 FreeBuds 2Pro","2","799"},
                new String[]{"1281","华为 MateBook 14 （i5 8G 512G)","1","5999"},
        };

        //将购买数据写入模板文档的第二个表格
        writeDataToDocument(doc, purchaseData);

        //更新域
        doc.isUpdateFields(true);

        //保存为PDF格式文档
        doc.saveToFile("D:\\Invoice.pdf", FileFormat.PDF);
    }



    public static void addRows(Table table, int rowNum) {

        for (int i = 0; i < rowNum; i++) {

            //将指定个数的第二行的复制行依次添加到第二行下面
            table.getRows().insert(2 + i, table.getRows().get(1).deepClone());

            //更新“金额”所对应单元格的公式
            for (Object object : table.getRows().get(2 + i).getCells().get(4).getParagraphs().get(0).getChildObjects()
            ) {
                if (object instanceof Field) {
                    Field field = (Field) object;
                    field.setCode(String.format("=C%d*D%d\\# \"0.00\"", 3 + i, 3 + i));
                }
                break;
            }
        }

        //更新“折扣金额”对应的单元格的公式
        for (Object object : table.getRows().get(4 + rowNum).getCells().get(4).getParagraphs().get(0).getChildObjects()
        ) {
            if (object instanceof Field) {
                Field field = (Field) object;
                field.setCode(String.format("=E%d*0.05\\# \"0.00\"", 3 + rowNum));
            }
            break;
        }

        //更新“总计”对应的单元格的公式
        for (Object object : table.getRows().get(5 + rowNum).getCells().get(4).getParagraphs().get(0).getChildObjects()
        ) {
            if (object instanceof Field) {
                Field field = (Field) object;
                field.setCode(String.format("=E%d-E%d\\# \"￥#,##0.00\"", 3 + rowNum, 5 + rowNum));
            }
            break;
        }
    }


    public static void fillTableWithData(Table table, String[][] data) {

        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[r].length; c++) {

                //将数据从表格的第二行开始写入表格
                table.getRows().get(r + 1).getCells().get(c+1).getParagraphs().get(0).setText(data[r][c]);
            }
        }
    }


    public static void writeDataToDocument(Document doc, String[][] purchaseData) {

        //获取Word模板中的第二个表格
        Table table = doc.getSections().get(0).getTables().get(3);

        //若购买商品多于一项，则添加purhcaseData.Length - 1个行
        /*if (purchaseData.length > 1) {
            addRows(table, purchaseData.length - 1);
        }*/

        //将购买数据填充至表格
        fillTableWithData(table, purchaseData);
    }

}
