/**
 * 
 */
package cn.strong.fastdfs.client.protocol.request.storage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.File;
import java.nio.charset.Charset;

import cn.strong.fastdfs.client.CommandCodes;
import cn.strong.fastdfs.client.Consts;
import cn.strong.fastdfs.model.StoragePath;

/**
 * 追加文件内容请求
 * 
 * @author liulongbiao
 *
 */
public class AppendRequest extends AbstractFileRequest {

	public final StoragePath spath;

	public AppendRequest(File file, StoragePath spath) {
		super(file);
		this.spath = spath;
	}

	public AppendRequest(Object content, long size, StoragePath spath) {
		super(content, size);
		this.spath = spath;
	}

	@Override
	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_APPEND_FILE;
	}

	@Override
	protected ByteBuf meta(ByteBufAllocator alloc, Charset charset) {
		byte[] pathBytes = spath.path.getBytes(charset);
		int metaLen = 2 * Consts.FDFS_PROTO_PKG_LEN_SIZE + pathBytes.length;
		ByteBuf buf = alloc.buffer(metaLen);
		buf.writeLong(pathBytes.length);
		buf.writeLong(size);
		buf.writeBytes(pathBytes);
		return buf;
	}

}
