package com.adaptris.core.json.streaming;

import com.adaptris.core.ServiceCase;
import com.adaptris.core.ServiceList;
import com.adaptris.core.services.splitter.AdvancedMessageSplitterService;

public class JsonSplitterTest extends ServiceCase {

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
  @Override
  protected AdvancedMessageSplitterService retrieveObjectForSampleConfig() {
    AdvancedMessageSplitterService service = new AdvancedMessageSplitterService();
    service.setService(new ServiceList());
    service.setSplitter(new JsonStreamingSplitter("/path/to/nested/item"));
    return service;
  }
  
}
