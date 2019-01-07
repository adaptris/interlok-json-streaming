package com.adaptris.core.json.streaming;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;

import org.junit.Test;
import org.w3c.dom.Document;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ServiceCase;
import com.adaptris.core.util.DocumentBuilderFactoryBuilder;
import com.adaptris.core.util.XmlHelper;
import com.adaptris.stax.*;
import com.adaptris.util.text.xml.XPath;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class JsonConversionTest {
  private static final String XML_MESSAGE = "<?xml version=\"1.0\" " + "encoding=\"UTF-8\"?>" + System.lineSeparator()
      + "<envelope>" + System.lineSeparator() + "<document><one>1</one></document>" + System.lineSeparator()
      + "<document><two>2</two></document>" + System.lineSeparator() + "<document><three>3</three></document>"
      + System.lineSeparator() + "</envelope>";

  private static final String JSON_MESSAGE = "{\"envelope\":{\"document\":[{\"one\":\"1\"},{\"two\":\"2\"},{\"three\":\"3\"}]}}";

  @Test
  public void testXmlToJson() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage(XML_MESSAGE);
    StaxStreamingService service = new StaxStreamingService().withInputBuilder(new DefaultInputFactory())
        .withOutputBuilder(new JsonStreamingOutputFactory()
            .withConfig(new JsonStreamingConfigBuilder().withAutoArray(Boolean.TRUE).withPrettyPrint(Boolean.FALSE)));
    ServiceCase.execute(service, msg);
    Configuration jsonConfig = new Configuration.ConfigurationBuilder().jsonProvider(new JsonSmartJsonProvider())
        .mappingProvider(new JacksonMappingProvider()).options(EnumSet.noneOf(Option.class)).build();
    ReadContext ctx = JsonPath.parse(msg.getInputStream(), jsonConfig);
    assertEquals("3", unwrap(ctx.read("$..document.length()").toString()));
  }

  @Test
  public void testJsonToXml() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage(JSON_MESSAGE);
    // multiple-PI false -> gets rid of <?xml-multiple document?> in the output
    StaxStreamingService service = new StaxStreamingService()
        .withInputBuilder(new JsonStreamingInputFactory().withConfig(
            new JsonStreamingConfigBuilder().withAutoArray(Boolean.TRUE).withMultipleProcessingInstruction(Boolean.FALSE)))
        .withOutputBuilder(new DefaultWriterFactory());
    ServiceCase.execute(service, msg);
    System.out.println(msg.getContent());
    XPath xpath = new XPath();
    Document d = XmlHelper.createDocument(msg, DocumentBuilderFactoryBuilder.newInstance());
    assertEquals(3, xpath.selectNodeList(d, "/envelope/document").getLength());
  }

  private static String unwrap(final String json) {
    if (json.startsWith("[") && json.endsWith("]")) {
      return json.substring(1, json.length() - 1);
    }
    return json;
  }
}
