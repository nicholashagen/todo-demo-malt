package com.znet.logging.demo.tracing;

import java.net.URI;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptorV2;

import zipkin.Constants;
import zipkin.Endpoint;
import zipkin.TraceKeys;

public class TracingStatementInterceptor implements StatementInterceptorV2 {

	@Override
	public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement,
		  Connection connection) throws SQLException {

		Tracer tracer = Tracing.currentTracer();
		if (tracer == null) return null;

		Span span = tracer.createSpan("mysql");
		if (interceptedStatement instanceof PreparedStatement) {
			sql = ((PreparedStatement) interceptedStatement).getPreparedSql();
		}

		span.tag(TraceKeys.SQL_QUERY, sql);
		parseServerAddress(connection, span);

		return null;
	}

	@Override
	public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement,
		  ResultSetInternalMethods originalResultSet, Connection connection, int warningCount,
		  boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException)
				  throws SQLException {

		Tracer tracer = Tracing.currentTracer();
		if (tracer == null) return null;

		Span span = tracer.getCurrentSpan();
		if (span == null) return null;

		if (statementException != null) {
			span.tag(Constants.ERROR, Integer.toString(statementException.getErrorCode()));
		}

		tracer.close(span);

		return null;
	}

	static void parseServerAddress(Connection connection, Span span) {
		try {
			URI url = URI.create(connection.getMetaData().getURL().substring(5)); // strip "jdbc:"
			int port = url.getPort() == -1 ? 3306 : url.getPort();
			String remoteServiceName = connection.getProperties().getProperty("zipkinServiceName");
			if (remoteServiceName == null || "".equals(remoteServiceName)) {
				String databaseName = connection.getCatalog();
				if (databaseName != null && !databaseName.isEmpty()) {
					remoteServiceName = "mysql-" + databaseName;
				} else {
					remoteServiceName = "mysql";
				}
			}
			Endpoint.Builder builder = Endpoint.builder().serviceName(remoteServiceName).port(port);
			if (!builder.parseIp(connection.getHost())) return;
			// span.remoteEndpoint(builder.build());
		} catch (Exception e) {
			// remote address is optional
		}
	}

	@Override public boolean executeTopLevelOnly() {
		return true; // True means that we don't get notified about queries that other interceptors issue
	}

	@Override public void init(Connection conn, Properties props) throws SQLException {
		// Don't care
	}

	@Override public void destroy() {
		// Don't care
	}
}
