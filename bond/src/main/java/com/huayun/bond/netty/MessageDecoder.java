package com.huayun.bond.netty;

import com.huayun.bond.pojo.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageDecoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //需要将二朝向字节码->MessageProtocol封装成messageProtocol对象
        MessageProtocol messageProtocol = new MessageProtocol();
        int len = in.readInt();
        messageProtocol.setLen(len);
        messageProtocol.setSzMagicNum((short) in.readUnsignedShort());
        messageProtocol.setByVersion(in.readByte());
        messageProtocol.setByMsgType(in.readByte());
        messageProtocol.setUiSourceID(in.readShort());
        messageProtocol.setUiSessionID(in.readInt());
        messageProtocol.setUiFuncNo(in.readShort());
        messageProtocol.setUiMsgSeq(in.readInt());
        messageProtocol.setUiRetCode(in.readInt());
        messageProtocol.setUiMktCode(in.readByte());
        byte[] rev = new byte[7];
        in.readBytes(rev);
        messageProtocol.setByReserved(rev);
        byte[] content = new byte[len - 32];
        in.readBytes(content);
        messageProtocol.setContent(content);
        log.info(messageProtocol.toString());
        out.add(messageProtocol);
        ctx.pipeline().fireChannelRead(out);
    }
}
