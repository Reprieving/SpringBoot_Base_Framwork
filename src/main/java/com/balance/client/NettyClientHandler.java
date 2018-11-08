package com.balance.client;

import com.balance.architecture.dto.TransferObject;
import com.balance.architecture.exception.DataTypeErrorException;
import com.balance.architecture.exception.StateErrorException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory
            .getLogger(NettyClientHandler.class);
    private TransferObject transferObject;

    public NettyClientHandler(TransferObject transferObject){
        this.transferObject = transferObject;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(transferObject);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransferObject transferObjectTmp;
        if(msg instanceof TransferObject){
            transferObjectTmp = (TransferObject) msg;
            if(TransferObject.STATUS_FAIL.equals(transferObjectTmp.getState())){
                logger.error(transferObjectTmp.getMessage());
                throw new StateErrorException("Netty Client's "+transferObject.getOperatorType()+ " error");
            }
            if(TransferObject.OPERATOR_DOWNLOAD.equals(transferObjectTmp.getOperatorType())){
                transferObject.setBytes(transferObjectTmp.getBytes());
            }
        }else{
            throw new DataTypeErrorException("Netty Server return error data type");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
