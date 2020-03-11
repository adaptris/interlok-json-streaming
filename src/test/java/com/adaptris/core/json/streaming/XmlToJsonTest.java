package com.adaptris.core.json.streaming;

import com.adaptris.core.ServiceCase;
import com.adaptris.stax.DefaultInputFactory;
import com.adaptris.stax.StaxStreamingService;

public class XmlToJsonTest extends ServiceCase {

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
  @Override
  protected StaxStreamingService retrieveObjectForSampleConfig() {
    return new StaxStreamingService().withInputBuilder(new DefaultInputFactory())
        .withOutputBuilder(new JsonStreamingOutputFactory()
            .withConfig(new JsonStreamingConfigBuilder().withAutoArray(Boolean.TRUE).withPrettyPrint(Boolean.FALSE)));

  }

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-XML-JSON";
  }
  
}
