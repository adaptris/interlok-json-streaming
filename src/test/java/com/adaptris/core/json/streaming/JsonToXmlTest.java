package com.adaptris.core.json.streaming;

import com.adaptris.core.ServiceCase;
import com.adaptris.stax.DefaultWriterFactory;
import com.adaptris.stax.StaxStreamingService;

public class JsonToXmlTest extends ServiceCase {

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
  @Override
  protected StaxStreamingService retrieveObjectForSampleConfig() {
    return new StaxStreamingService()
        .withInputBuilder(new JsonStreamingInputFactory().withConfig(
            new JsonStreamingConfigBuilder().withAutoArray(Boolean.TRUE).withMultipleProcessingInstruction(Boolean.FALSE)))
        .withOutputBuilder(new DefaultWriterFactory());
  }

  @Override
  protected String createBaseFileName(Object object) {
    return super.createBaseFileName(object) + "-JSON-XML";
  }
  
}
