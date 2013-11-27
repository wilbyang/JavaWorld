package opensource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

public class BerkeleyDB {

	private Environment env;
	private Database db;

	public static void main(String[] args) throws Exception {
		BerkeleyDB mydb = new BerkeleyDB("C:/YB/mybdb/", 100000, true);
		// 如下打开（并创建）新的数据库
		// mydb.close();
		// mydb.open("newDB");
		mydb.put("111", "a");
		mydb.put("213", "b");
		mydb.put("012", "c");
		mydb.put("111", "d"); // 覆盖更新
		List<String> vals = mydb.get(10); // 按照key的升序排列
		for (String val : vals) {
			System.out.println(val);
		}
		mydb.close();
	}

	/** set up berkeleyDB with defaultDB 
	 * @throws DatabaseException */
	public BerkeleyDB(String path, long cacheSize, boolean resumable) throws DatabaseException {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(resumable);
		envConfig.setLocking(resumable);
		envConfig.setCacheSize(cacheSize);// The memory available to the
		// database system, in bytes.
		File envHome = new File(path);
		if (!resumable) {
			// TODO: 删除历史记录
			System.err.println("please delete env home:" + envHome);
			throw new RuntimeException("Func Not implemented yet!");
		}
		if (!envHome.exists()) {
			if (!envHome.mkdirs()) {
				throw new RuntimeException("Couldn't create this folder: "
						+ envHome.getAbsolutePath());
			}
		}

		env = new Environment(envHome, envConfig);

		// setup database
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(resumable);
		dbConfig.setDeferredWrite(!resumable);
		db = env.openDatabase(null, "defaultDB", dbConfig);
	}

	/** 构建Database： 指定数据库名字，如果指定名字的数据库不存在，则自动创建。 */
	public void open(String dbName) {
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		try {
			db = env.openDatabase(null, dbName, dbConfig);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	/** 关闭数据库和环境 */
	public void close() {
		try {
			if (db != null) {
				db.close();
			}
			if (env != null) {
				env.close();
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据操作：写 BDB存储的数据是无格式的，都是二进制的数据，无论是KEY，还是VALUE。
	 * 如果我们要存取JAVA对象，需要程序员先序列化成二进制的。
	 * */
	public boolean put(String key, String value) throws Exception {
		byte[] theKey = key.getBytes("UTF-8");
		byte[] theValue = value.getBytes("UTF-8");
		OperationStatus status = db.put(null, new DatabaseEntry(theKey), new DatabaseEntry(theValue));
		if (status == OperationStatus.SUCCESS) {
			return true;
		}
		return false;
	}

	/** 数据操作：读 */
	public String get(String key) throws Exception {
		DatabaseEntry queryKey = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		queryKey.setData(key.getBytes("UTF-8"));

		OperationStatus status = db.get(null, queryKey, value, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			return new String(value.getData());
		}
		return null;
	}

	/** 数据操作：删除 */
	public boolean delete(String key) throws Exception {
		byte[] theKey = key.getBytes("UTF-8");
		OperationStatus status = db.delete(null, new DatabaseEntry(theKey));
		if (status == OperationStatus.SUCCESS) {
			return true;
		}
		return false;
	}

	public static byte[] int2ByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (3 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	public List<String> get(int max) throws DatabaseException {
		int matches = 0;
		List<String> results = new ArrayList<String>(max);

		Cursor cursor = null;
		OperationStatus result;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		Transaction txn = null;

		try {
			cursor = db.openCursor(txn, null);
			result = cursor.getFirst(key, value, null);

			while (matches < max && result == OperationStatus.SUCCESS) {
				if (value.getData().length > 0) {
					results.add(new String(value.getData()));
					matches++;
				}
				result = cursor.getNext(key, value, null);
			}
		} catch (DatabaseException e) {
			throw e;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (txn != null) {
				txn.commit();
			}
		}
		return results;
	}

}