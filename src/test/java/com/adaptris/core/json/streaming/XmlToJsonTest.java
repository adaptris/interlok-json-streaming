package com.adaptris.core.json.streaming;

import com.adaptris.core.ServiceCase;
import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.fs.FsProducer;
import com.adaptris.stax.DefaultInputFactory;
import com.adaptris.stax.DefaultWriterFactory;
import com.adaptris.stax.StaxStreamingService;

public class XmlToJsonTest extends ServiceCase {

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
