/**
 * 
 */
package cn.strong.fastdfs.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.fastdfs.client.protocol.response.Receiver;
import cn.strong.fastdfs.ex.FastdfsTimeoutException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 链接守护处理器
 * 
 * @author liulongbiao
 *
 */
public class ConnectionWatchdog extends ChannelInboundHandlerAdapter {
	private final static Logger LOG = LoggerFactory.getLogger(ConnectionWatchdog.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			throw new FastdfsTimeoutException("channel was idle for maxIdleSeconds");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		onError(ctx, cause);
	}

	public static void onError(ChannelHandlerContext ctx, Throwable cause) {
		if (cause instanceof FastdfsTimeoutException) {
			LOG.info(cause.getMessage());
		} else {
			LOG.error(cause.getMessage(), cause);
		}
		Receiver<?> receiver = ctx.channel().attr(Receiver.RECEIVER).get();
		if (receiver != null) {
			receiver.tryError(cause);
		}
		ctx.close();
	}

}
