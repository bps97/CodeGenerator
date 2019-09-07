import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.Config;
import entity.Field;
import entity.Stint;
import entity.Table;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;


public class Main {
    public static void main(String[] args) {

        String fileName = "demo.json";
        String s = getJSONStr(fileName);

        JSONObject json = JSON.parseObject(s);
        //JSONArray tables = json.getJSONArray("table");

        JSONObject table = json.getJSONObject("table");
        Table tab = JSON.parseObject(table.toString(),Table.class);
        String sql = GenerateDDL(tab);

        JSONObject dbconfig = json.getJSONObject("dataConfig");
        Config config = dbconfig.parseObject(dbconfig.toString(),Config.class);


        //GenerateTable(tab, sql, config);


        //生成
        //GenerateTable(sql);


    }

    private static void GenerateTable(Table tab, String sql, Config config) {
        try{
            Class.forName("com.mysql.jdbc.Driver");//加载驱动
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = config.getUrl();
        String loginName = config.getUsername();
        String password = config.getPassword();


        try(
                Connection con = DriverManager.getConnection(url,loginName,password);
                Statement stmt = con.createStatement()
                //PreparedStatement ps = con.prepareStatement(sql);
        )

        {
            Long res = stmt.executeLargeUpdate(sql);
            if(res==0){
                System.out.println("表"+tab.getTableName()+"创建成功");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private static String GenerateDDL(Table tab) {
        String tableName = tab.getTableName();
        String tableComment = tab.getComment();
        ArrayList<Field> fields = tab.getFields();
        String sql = String.format("CREATE TABLE `%s`(",tableName);
        Iterator<Field> iter = fields.iterator();
        ArrayList<String> pks = new ArrayList<>();
        while (iter.hasNext()){
            Field field = iter.next();
            String fieldName = field.getFieldName();
            String fieldComment = " ";
            fieldComment = field.getComment();
            String jdbcType = field.getFieldType().getJdbcType();
            int fieldSize = field.getSize();
            Stint stint = field.getStint();

            String isNull = "";
            if(!stint.isNullable()){
                isNull = " NOT NULL";
            }

            if(stint.isPrimary()){
                pks.add(fieldName);
            }

            String isUnique = "";
            if(stint.isUnique()){
                isUnique = " UNIQUE";
            }

            String temp = String.format("`%s` %s(%s)%s%s COMMENT '%s',",fieldName,jdbcType,fieldSize,isNull,isUnique,fieldComment);


            sql += '\n';
            sql += '\t';
            sql+=temp;
        }


        if(!pks.isEmpty()){
            String temp = "PRIMARY KEY(";
            Iterator<String> piter = pks.iterator();
            int first = 0;
            while(piter.hasNext()){
                    temp = String.format(temp+"`%s`,",piter.next());
            }temp = temp.substring(0,temp.length()-1);

            temp += ')';
            sql = sql +'\n'+'\t';
            sql += temp;
        }else {
            sql = sql.substring(0,sql.length()-1);
        }

        sql += '\n';
        sql += ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        return sql;
    }


    private static String getJSONStr(String fileName) {
        String path = Main.class.getClassLoader().getResource(fileName).getPath();
        return readJsonFile(path);
    }

    private static void printsm (JSONArray ar,int index)  {
        index++;
        for(int i=0;i<ar.size();i++){
            JSONObject key1 = (JSONObject)ar.get(i);
            for(Object obj:key1.values()){

                if(obj instanceof JSONArray){
                    //System.out.println(obj);
                    printsm((JSONArray)obj,index);
                }else{
                    int c = index;
                    while(c-->0){
                        System.out.print("-");
                    }
                    System.out.println(obj.toString());
                }

            }

        }
    }

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
