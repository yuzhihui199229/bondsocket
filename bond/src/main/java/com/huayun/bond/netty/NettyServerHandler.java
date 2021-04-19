package com.huayun.bond.netty;

import com.huayun.bond.pojo.MessageProtocol;
import com.huayun.bond.service.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Autowired
    private TellerService tellerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RecallOrderService recallOrderService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private DealService dealService;

    @Autowired
    private UserService userService;

    @Autowired
    private StockHolderService stockHolderService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AssetInfoService assetInfoService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private TradeTimeGroupService tradeTimeGroupService;

    private static Map<String, String> tokenMap = new HashMap<>();

    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel active......");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        short uiFuncNo = msg.getUiFuncNo();
        MessageProtocol result = null;
        if (uiFuncNo == 28672) {//登录0x7000
            result = tellerService.login(msg);
//            byte[] content = result.getContent();
//            byte[] tokenBytes = new byte[33];
//            System.arraycopy(content, 32, tokenBytes, 0, 32);
//            String token = ByteUtil.getString(tokenBytes);
//            tokenMap.put(token, token);
        }
        if (uiFuncNo != 28672) {
            MessageProtocol tokenMsg = tellerService.getToken(msg);
            if (tokenMsg.getUiRetCode() == 0) {
                switch (uiFuncNo) {
//                    case 28673: //心跳0x7001
//                        result = token;
//                        break;
                    case 28674://登出0x7002
                    {
                        result = tellerService.logout(msg);
                        if (result.getUiRetCode() == 0) {
                            ctx.close();
                        }
                    }
                    break;
                    case 28690://订单查询0x7012
                        result = orderService.qryOrder(msg);
                        break;
                    case 28689://撤消订单查询0x7011
                        result = recallOrderService.qryRecallOrder(msg);
                        break;
                    case 28691://持仓查询0x7013
                        result = positionService.qryPosition(msg);
                        break;
                    case 28692://成交查询0x7014
                        result = dealService.qryDeal(msg);
                        break;
                    case 28693://柜员信息查询0x7015
                        result = tellerService.qryTellerInfo(msg);
                        break;
                    case 28694://客户信息查询0x7016
                        result = userService.qryUserInfo(msg);
                        break;
                    case 28695://股东信息查询0x7017
                        result = stockHolderService.qryStockHolderInfo(msg);
                        break;
                    case 28696://证券信息查询0x7018
                        result = securityService.qrySecurity(msg);
                        break;
                    case 28704://客户资金查询0x7020
                        result = assetInfoService.qryAssetInfo(msg);
                        break;
                    case 28705://资金流水查询0x7021
                        result = assetService.qryAsset(msg);
                        break;
                    case 28706://时间组0x7021
                        result = tradeTimeGroupService.QryTradeTimeInfo(msg);
                        break;
//                    default:
//                        result = tokenMsg;
                }
            }
            if (tokenMsg.getUiRetCode() != 0) {
                result = tokenMsg;
            }
        }
        if (result != null) {
            ctx.writeAndFlush(result);
        } else {
            log.info("返回的result为空");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.info(cause.getMessage());
//        ctx.close();
    }
}