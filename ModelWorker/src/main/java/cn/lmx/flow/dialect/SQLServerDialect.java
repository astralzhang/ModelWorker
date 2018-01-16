package cn.lmx.flow.dialect;

import java.sql.Types;

public class SQLServerDialect extends org.hibernate.dialect.SQLServerDialect {
	public SQLServerDialect() {
		super();
		registerColumnType(Types.CHAR, 255, "char($1)");
		registerHibernateType(Types.NVARCHAR, "string");
		registerHibernateType(Types.LONGNVARCHAR, "string");
		registerHibernateType(Types.NCHAR, "string");
		registerHibernateType(Types.CHAR, "string");
	}
}
