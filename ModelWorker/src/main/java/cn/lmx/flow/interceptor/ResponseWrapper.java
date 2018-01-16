package cn.lmx.flow.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {
	private PrintWriter cachedWriter;
	private ByteArrayOutputStream bufferedWriter;
	private ServletOutputStream out = null;
	public ResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
		//保存返回结果用
		bufferedWriter = new ByteArrayOutputStream();
		out = new WrapperOutputStream(bufferedWriter);
		//让所有的结果通过PrintWriter写入到bufferedWriter
		cachedWriter = new PrintWriter(new OutputStreamWriter(bufferedWriter, this.getCharacterEncoding()));
	}
	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		return cachedWriter;
	}
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return out;
	}
	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		if (out != null) {
			out.flush();
		}
		if (cachedWriter != null) {
			cachedWriter.flush();
		}
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		bufferedWriter.reset();
	}
	/**
	 * 获取Response内容
	 * @return
	 * @throws IOException
	 */
	public byte[] getResult() throws IOException {
		flushBuffer();
		return bufferedWriter.toByteArray();
	}
	private class WrapperOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream bos = null;
		public WrapperOutputStream(ByteArrayOutputStream stream) throws IOException {
			this.bos = stream;
		}
		@Override
		public void write(int b) throws IOException {
			// TODO Auto-generated method stub
			bos.write(b);
		}
		@Override
		public void write(byte[] b) throws IOException {
			// TODO Auto-generated method stub
			bos.write(b, 0, b.length);
		}
	}
}
