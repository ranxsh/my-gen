package com.xsr.demo.app;

import com.xsr.demo.mygen.def.TableMetadata;
import com.xsr.demo.mygen.trans.TableTrans;
import com.xsr.demo.utils.DBUtils;
import org.apache.commons.io.FileUtils;
import org.bee.tl.core.GroupTemplate;
import org.bee.tl.core.Template;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xs on 2017/6/9.
 */
public class App {

    public static void main(String[] args){
        String host = "localhost";
        String port = "3306";
        String userName = "root";
        String password = "admin";
        String genPackage = "com.xsr.gen";
        String dbName = "jeesite";
        List<String> tableNameList = new ArrayList<String>();

        MygenTest mygen = new MygenTest(host, port,dbName, userName, password, genPackage, tableNameList);
        try {
            mygen.gen();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
