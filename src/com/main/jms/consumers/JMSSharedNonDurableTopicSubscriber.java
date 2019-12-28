package com.main.jms.consumers;

import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSSharedNonDurableTopicSubscriber {

  public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
    Hashtable<String, String> ht = new Hashtable<>();
    ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
    ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
    Context context = new InitialContext(ht);
    ConnectionFactory connFactory = (ConnectionFactory) context.lookup("jms/TestConnectionFactory");
    Topic topic = (Topic) context.lookup("jms/TestTopic");
    try (JMSContext jmsContext1 = connFactory.createContext();) {
      JMSConsumer consumer1 = jmsContext1.createSharedConsumer(topic, "subscriber-1");
      try (JMSContext jmsContext2 = connFactory.createContext();) {
        JMSConsumer consumer2 = jmsContext2.createSharedConsumer(topic, "subscriber-1");
        for (int lp1 = 1; lp1 <= 5; lp1++) {
          String msg1 = consumer1.receiveBody(String.class);
          String msg2 = consumer2.receiveBody(String.class);
          System.out.println("Message recieved from publisher msg1 :" + msg1 + " msg2 :" + msg2);
          Thread.sleep(1000);
        }
      }
    }
  }
}
