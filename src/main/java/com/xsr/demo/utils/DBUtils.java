package com.xsr.demo.utils;

import com.xsr.demo.mygen.def.ColumnMetadata;
import com.xsr.demo.mygen.def.LinkMetadata;
import com.xsr.demo.mygen.def.PKColumnMetadata;
import com.xsr.demo.mygen.def.TableMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {

	public static final Logger log = Logger.getLogger(DBUtils.class);

	public static List<String> removePrefixChars=new ArrayList<String>();
	public static final String CONFIG_FILE = "config.properties";

	public static final String KEY_DRIVER = "jdbc.driver";
	public static final String KEY_URL = "jdbc.url";
	public static final String KEY_USERNAME = "jdbc.username";
	public static final String KEY_PASSWORD = "jdbc.password";

	public static Connection getConn() {
		log.info("初始化数据库【开始】");
		Connection conn = null;
		ConfigUtils.Cfg cfg = ConfigUtils.getCfg(CONFIG_FILE);
		String driver = cfg.get("jdbc.driver");    //"com.mysql.jdbc.Driver";
		String url = cfg.get("jdbc.url");	//"jdbc:mysql://localhost:3306/jeesite?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		String username = cfg.get("jdbc.username");	//"root";
		String password = cfg.get("jdbc.password"); 	//"admin";
		String prefixList="";
		if(StringUtils.isNotEmpty(prefixList)){
			String[] strings=prefixList.split(",+");
			if(strings!=null&&strings.length>0){
				for (String prefix : strings) {
					removePrefixChars.add(prefix.toLowerCase());
				}
			}
		}
		log.info(String.format("  加载配置项driver  ：%s", driver));
		log.info(String.format("  加载配置项url     ：%s", url));
		log.info(String.format("  加载配置项username：%s", username));
		log.info(String.format("  加载配置项password：%s", password));
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(String.format("[JDBC驱动加载错误][%s]", driver), e);
		} catch (SQLException e) {
			throw new RuntimeException(String.format("[数据库连接失败]"), e);
		}
		log.info("初始化数据库【完成】！");
		return conn;
	}

	public static Connection getConn(String host, String port, String username, String password, String dbname) {
		log.info("初始化数据库【开始】");
		Connection conn = null;
		ConfigUtils.Cfg cfg = ConfigUtils.getCfg(CONFIG_FILE);
		String driver = cfg.get("jdbc.driver");    //"com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
//		String username = cfg.get("jdbc.username");	//"root";
//		String password = cfg.get("jdbc.password"); 	//"admin";
		String prefixList="";
		if(StringUtils.isNotEmpty(prefixList)){
			String[] strings=prefixList.split(",+");
			if(strings!=null&&strings.length>0){
				for (String prefix : strings) {
					removePrefixChars.add(prefix.toLowerCase());
				}
			}
		}
		log.info(String.format("  加载配置项driver  ：%s", driver));
		log.info(String.format("  加载配置项url     ：%s", url));
		log.info(String.format("  加载配置项username：%s", username));
		log.info(String.format("  加载配置项password：%s", password));
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(String.format("[JDBC驱动加载错误][%s]", driver), e);
		} catch (SQLException e) {
			throw new RuntimeException(String.format("[数据库连接失败]"), e);
		}
		log.info("初始化数据库【完成】！");
		return conn;
	}

	public static DatabaseMetaData getDatabaseMetaData(Connection conn) {
		DatabaseMetaData dbmd = null;
		ResultSet rs = null;
		try {
			dbmd = conn.getMetaData();
			rs = dbmd.getTypeInfo();
			while (rs.next()) {
				log.info(String.format("类型名称【%s】 JavaType【%s】最大精度【%s】", rs.getString(1), SqlTypeUtils.decodeToJavaType(rs.getInt(2)), rs.getString(3)));
			}
			String dbType = dbmd.getDatabaseProductName();
			String dbVersion = dbmd.getDatabaseProductVersion();
			String driverName = dbmd.getDriverName();
			String driverVersion = dbmd.getDriverVersion();
			log.info(String.format("数据库类型【%s】数据库版本【%s】数据库驱动名称【%s】数据库驱动程序版本【%s】", dbType, dbVersion, driverName, driverVersion));
		} catch (SQLException e) {
			throw new RuntimeException("获取元数据出错", e);
		}
		return dbmd;
	}
//
//	public static void loadMetadata(DatabaseMetaData dbmd) {
//		String[] types = { "TABLE" };
//		ResultSet rs = null;
//		try {
//			rs = dbmd.getTables(null, null, null, types);
//			while (rs.next()) {
//				solveTable(rs);
//			}
//			rs = dbmd.getColumns(null, null, null, null);
//			while (rs.next()) {
//				solveColumn(rs);
//			}
//			for (TableMetadata table : TableMetadata.getAllTables().values()) {
//				rs = dbmd.getPrimaryKeys(null, null, table.getTableName());
//				while (rs.next()) {
//					solvePrimaryKey(rs);
//				}
//				rs = dbmd.getImportedKeys(null, null, table.getTableName());
//				while (rs.next()) {
//					solveForeignKey(rs);
//				}
//			}
//		} catch (SQLException e) {
//			throw new RuntimeException("获取表信息出错", e);
//		}
//	}
//
//	private static void solveForeignKey(ResultSet rs) throws SQLException {
//		String targetTableName = rs.getString("PKTABLE_NAME");
//		String targetColumnName = rs.getString("PKCOLUMN_NAME");
//		String tableName = rs.getString("FKTABLE_NAME");
//		String columnName = rs.getString("FKCOLUMN_NAME");
//		ColumnMetadata fromColumn = TableMetadata.find(tableName).getColumn(columnName);
//		ColumnMetadata toColumn = TableMetadata.find(targetTableName).getColumn(targetColumnName);
//		LinkMetadata link = new LinkMetadata(fromColumn, toColumn);
//		TableMetadata.find(fromColumn.getTableName()).addLink(link);
//		TableMetadata.find(toColumn.getTableName()).addLinkBy(link);
//		log.info(String.format("  表【%s】中的列【%s】引用表【%s】的列【%s】", tableName, columnName, targetTableName, targetColumnName));
//	}
//
//	private static void solvePrimaryKey(ResultSet rs) throws SQLException {
//		String tableName = rs.getString("TABLE_NAME");
//		String columnName = rs.getString("COLUMN_NAME");
//		TableMetadata tableMetadata = TableMetadata.find(tableName);
//		ColumnMetadata keyColumn = tableMetadata.getColumns().remove(columnName);
//		tableMetadata.getKeys().put(columnName, PKColumnMetadata.from(keyColumn));
//		log.debug(String.format("  表【%s】中的【%s】列标记为主键", tableName, columnName));
//	}
//
//	private static void solveTable(ResultSet rs) throws SQLException {
//		TableMetadata table = TableMetadata.find(rs.getString("TABLE_NAME ".trim()));
//		table.setTableCat(rs.getString("TABLE_CAT  ".trim()));
//		table.setTableSchema(rs.getString("TABLE_SCHEM".trim()));
//		table.setTableType(rs.getString("TABLE_TYPE ".trim()));
//		table.setRemarks(rs.getString("REMARKS    ".trim()));
//		log.debug(String.format("  发现表【%s】", table.getTableName()));
//	}
//
//	private static void solveColumn(ResultSet rs) throws SQLException {
//		ColumnMetadata column = new ColumnMetadata();
//		column.setTableCat(rs.getString("TABLE_CAT         ".trim()));
//		column.setTableSchema(rs.getString("TABLE_SCHEM       ".trim()));
//		column.setTableName(rs.getString("TABLE_NAME        ".trim()));
//		column.setColumnName(rs.getString("COLUMN_NAME       ".trim()));
//		column.setDataType(rs.getInt("DATA_TYPE         ".trim()));
//		// column.setTypeName (rs.getString("TYPE_NAME         ".trim()));
//		column.setTypeName(SqlTypeUtils.decodeToName(column.getDataType()));
//		column.setColumnSize(rs.getInt("COLUMN_SIZE       ".trim()));
//		column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS    ".trim()));
//		column.setNumPrecRadix(rs.getInt("NUM_PREC_RADIX    ".trim()));
//		column.setNullable(rs.getInt("NULLABLE          ".trim()));
//		column.setRemarks(rs.getString("REMARKS           ".trim()));
//		column.setColumnDef(rs.getString("COLUMN_DEF        ".trim()));
//		column.setCharOctetLength(rs.getInt("CHAR_OCTET_LENGTH ".trim()));
//		column.setOrdinalPosition(rs.getInt("ORDINAL_POSITION  ".trim()));
//		column.setIsNullable(rs.getString("IS_NULLABLE       ".trim()));
//		column.setScopeCatalog(rs.getString("SCOPE_CATALOG     ".trim()));
//		column.setScopeSchema(rs.getString("SCOPE_SCHEMA      ".trim()));
//		column.setScopeTable(rs.getString("SCOPE_TABLE       ".trim()));
//		column.setSourceDataType(rs.getShort("SOURCE_DATA_TYPE  ".trim()));
//		column.setIsAutoincrement(rs.getString("IS_AUTOINCREMENT  ".trim()));
//		TableMetadata targetTable = TableMetadata.find(column.getTableName());
//		targetTable.addColumn(column);
//		log.debug(String.format("  表【%s】发现列【%s】，列类型为【%s】", column.getTableName(), column.getColumnName(), column.getTypeName()));
//	}
}
