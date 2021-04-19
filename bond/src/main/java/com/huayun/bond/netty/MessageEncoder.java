package com.huayun.bond.netty;

import com.huayun.bond.pojo.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLen());
        out.writeShort(msg.getSzMagicNum());
        out.writeByte(msg.getByVersion());
        out.writeByte(msg.getByMsgType());
        out.writeShort(msg.getUiSourceID());
        out.writeInt(msg.getUiSessionID());
        out.writeShort(msg.getUiFuncNo());
        out.writeInt(msg.getUiMsgSeq());
        out.writeInt(msg.getUiRetCode());
        out.writeByte(msg.getUiMktCode());
        out.writeBytes(msg.getByReserved());
        out.writeBytes(msg.getContent());
        log.info(msg.toString());
    }
}
