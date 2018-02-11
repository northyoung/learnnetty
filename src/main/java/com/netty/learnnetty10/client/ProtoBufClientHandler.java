//package com.netty.learnnetty10.client;
//
//import com.netty.learnnetty10.proto.RichManProto;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//
//import com.netty.learnnetty10.proto.RichManProto.RichMan.Car;
//import com.netty.learnnetty10.proto.RichManProto.RichMan.CarType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println("-------------------------");
//        RichManProto.RichMan.Builder builder = RichManProto.RichMan.newBuilder();
//        builder.setName("me");
//        builder.setId(1);
//        builder.setEmail("321321@qq.com");
//
//        List<Car> cars = new ArrayList<Car>();
//        Car car1 = RichManProto.RichMan.Car.newBuilder().setName("大众").setType(CarType.DASAUTO).build();
//        Car car2 = RichManProto.RichMan.Car.newBuilder().setName("Aventador").setType(CarType.LAMBORGHINI).build();
//        Car car3 = RichManProto.RichMan.Car.newBuilder().setName("奔驰SLS级AMG").setType(CarType.BENZ).build();
//
//        cars.add(car1);
//        cars.add(car2);
//        cars.add(car3);
//
//        builder.addAllCars(cars);
//        ctx.writeAndFlush(builder.build());
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
