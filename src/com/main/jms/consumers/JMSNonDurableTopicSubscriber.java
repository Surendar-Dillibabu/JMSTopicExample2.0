package com.main.jms.consumers;

import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSNonDurableTopicSubscriber {

  public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
    Hashtable<String, String> ht = new Hashtable<>();
    ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
    ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
    Context context = new InitialContext(ht);
    ConnectionFactory connFactory = (ConnectionFactory) context.lookup("jms/TestConnectionFactory");
    Topic topic = (Topic) context.lookup("jms/TestTopic");
    try (JMSContext jmsContext = connFactory.createContext();) {
      JMSConsumer consumer = jmsContext.createConsumer(topic);
      for (int lp1 = 1; lp1 <= 10; lp1++) {
        TextMessage message = (TextMessage) consumer.receive();
        System.out.println("Message recieved from publisher :" + message.getText());
        Thread.sleep(2000);
      }
    }
  }
}
