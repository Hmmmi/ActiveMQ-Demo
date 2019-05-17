package com.mijing.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * @author 生产者
 *
 */
public class Producer {

     //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);

        try {
            //连接
            Connection connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //消息目的地
            Destination destination = session.createQueue("FirstQueue");
            //消息生产者
            MessageProducer producer = session.createProducer(destination);
            //设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //发送消息
            TextMessage m1 = session.createTextMessage("卖家已发货");
            producer.send(m1);
            senMsg(session, producer, "上海", "上海集散中心");
            TextMessage m2 = session.createTextMessage("顺丰快运已收取快件");
            producer.send(m2);
            senMsg(session, producer, "上海集散中心", "上海虹桥集散中心");
            senMsg(session, producer, "上海虹桥集散中心", "武汉吴家山集散中心");
            senMsg(session, producer, "武汉吴家山集散中心", "武汉北港服务点");
            TextMessage m3 = session.createTextMessage("正在派送途中，请您准备签收");
            producer.send(m3);
            
            TextMessage m4 = session.createTextMessage("在官网。。。。。");
            producer.send(m4);
            
            TextMessage m5 = session.createTextMessage("已签收！");
            producer.send(m5);

            session.commit();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } 
    }
    
    public static void senMsg( Session session, MessageProducer producer, String arriver, String next) throws JMSException{
    	TextMessage msg = session.createTextMessage("快件已抵达【"+arriver+"】，正转运至【"+next+"】");
    	producer.send(msg);
    }
}