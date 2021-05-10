

import utils.Html;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class spiderMain {

    //斗鱼网页baseUrl
    public static String baseUrl = "https://www.douyu.com/gapi/rkc/directory/mixList/0_0/";

    public static String url = baseUrl+"1";

    //数据存储路径
    public static String savePath = "C:/Users/admin/Desktop/data.xlsx";

    public static void main(String[] args) {
        Html html = new Html();
        String result = "";
        result = html.getDataByGetMethod(url);
        JSONObject getJson = new JSONObject(result);
        JSONObject data = null;
        data = (JSONObject) getJson.get("data");
        JSONArray jsonArray = data.getJSONArray("rl");
        //获取网页的页数
        int totalPage = (int) data.get("pgcnt");

        /*
        * nn: 主播名
        * ol : 热度
        * c2name: 板块
        * rn : 标题
        * */

        List<excelData> tmpData = new ArrayList<>();
        for (int i=0; i < totalPage; i++){
            String iBaseUrl = baseUrl+ i;
            String iResult = html.getDataByGetMethod(iBaseUrl);
            getJson = new JSONObject(iResult);
            data = (JSONObject) getJson.get("data");
            jsonArray = data.getJSONArray("rl");

            for (int j=0; j < jsonArray.length(); j++){
                try{
                    JSONObject iData = (JSONObject) jsonArray.get(j);
                    String nn = (String) iData.get("nn");
                    Integer ol = (Integer) iData.get("ol");
                    String c2name = (String) iData.get("c2name");
                    String rn = (String) iData.get("rn");
                    excelData tmp = new excelData(nn,ol,c2name,rn);
                    tmpData.add(tmp);
                }catch (Exception e){
                    break;
                }

            }
            System.out.println(i);
        }

        ArrayList<excelData> tmpData2 = singleElement((ArrayList<excelData>) tmpData);

        if(saveData(tmpData2)){
            System.out.println("数据爬取完成");
        }else{
            System.out.println("失败");
        }


    }

    public static ArrayList singleElement(ArrayList<excelData> al) {
        //定义一个临时容器
        ArrayList newAl = new ArrayList();
        Iterator it = al.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (!newAl.contains(obj)) {
                newAl.add(obj);
            }
        }
        return newAl;
    }

    public static boolean saveData(ArrayList<excelData> tmpData){

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("DouYuTV");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("主播名");
        cell = row.createCell(1);
        cell.setCellValue("热度");
        cell = row.createCell(2);
        cell.setCellValue("板块");
        cell = row.createCell(3);
        cell.setCellValue("标题");


        for (int n = 0; n < tmpData.size(); n++){

            HSSFRow row1 = sheet.createRow(n + 1);
            excelData user = tmpData.get(n);
            //创建单元格设值
            row1.createCell(0).setCellValue(user.getNn());
            row1.createCell(1).setCellValue(user.getOl());
            row1.createCell(2).setCellValue(user.getC2name());
            row1.createCell(3).setCellValue(user.getRn());
        }

        File file = new File(savePath);

        if (file.exists()){
            file.delete();
        }
        try{
            file.createNewFile();
            workbook.write(file);
            workbook.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

    }
}

class excelData {
    String nn;
    Integer ol;
    String c2name;
    String rn;

    public excelData(String nn, Integer ol, String c2name, String rn) {
        this.nn = nn;
        this.ol = ol;
        this.c2name = c2name;
        this.rn = rn;
    }

    public excelData() {
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public Integer getOl() {
        return ol;
    }

    public void setOl(Integer ol) {
        this.ol = ol;
    }

    public String getC2name() {
        return c2name;
    }

    public void setC2name(String c2name) {
        this.c2name = c2name;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }
}