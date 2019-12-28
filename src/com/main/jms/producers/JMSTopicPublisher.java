package com.main.jms.producers;

import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSTopicPublisher {

  public static void main(String[] args) throws NamingException {
    Hashtable<String, String> ht = new Hashtable<>();
    ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
    ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
    Context context = new InitialContext(ht);
    ConnectionFactory connFactory = (ConnectionFactory) context.lookup("jms/TestConnectionFactory");
    Topic topic = (Topic) context.lookup("jms/TestTopic");
    try (JMSContext jmsContext = connFactory.createContext();) {
      JMSProducer producer = jmsContext.createProducer();
      for (int lp1 = 1; lp1 <= 10; lp1++) {
        producer.send(topic, "Hi, Am msg-" + lp1);
      }
    }
    System.out.println("Messages published to the JMSProvider");
  }
}
