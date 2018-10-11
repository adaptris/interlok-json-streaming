package com.adaptris.core.json.streaming;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.CoreException;
import com.adaptris.core.services.splitter.MessageSplitterImp;
import com.adaptris.core.util.Args;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.stax.lms.StaxSplitGenerator;
import com.adaptris.stax.lms.StaxSplitGeneratorConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStream;

/**
 * Splitter implementation that splits based on JSON streaming events.
 * <p>
 * Note that this is only a pseudo-xpath evaluator as it only allows simple element traversal and not any XPath functions.
 * {@code /path/to/repeating/element} would be fine, but {@code //repeating/element} would not. It works based on
 * {@link XMLEventReader} and navigates based on {@link StartElement} events only.
 * </p>
 *
 * @config json-streaming-splitter
 * @author mwarman
 */
@XStreamAlias("json-streaming-splitter")
@DisplayOrder(order = {"path", "bufferSize"})
public class JsonStreamingSplitter extends MessageSplitterImp {

  private transient static final int DEFAULT_BUFFER_SIZE = 8192;

  @NotBlank
  private String path;

  @AdvancedConfig
  private Integer bufferSize;

  public JsonStreamingSplitter() {

  }

  public JsonStreamingSplitter(String path) {
    this();
    setPath(path);
  }
  @Override
  public Iterable<AdaptrisMessage> splitMessage(AdaptrisMessage msg) throws CoreException {
    try {
      String thePath = msg.resolve(getPath());
      BufferedReader buf = new BufferedReader(msg.getReader(), bufferSize());
      XMLEventReader reader = new JsonXMLInputFactory().createXMLEventReader(buf);
      JsonXMLConfig config = new JsonXMLConfigBuilder()
          .autoArray(true)
          .autoPrimitive(true)
          .build();
      XMLEventFactory eventFactory = XMLEventFactory.newInstance();
      return new JsonStreamingSplitGenerator(
          new JsonStreamingSplitGeneratorConfig()
              .withOriginalMessage(msg)
              .withJsonXMLConfig(config)
              .withXMLEventFactory(eventFactory)
              .withXmlEventReader(reader)
              .withPath(thePath)
              .withInputReader(buf));
    }
    catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
  }

  public Integer getBufferSize() {
    return bufferSize;
  }

  /**
   * Set the internal buffer size.
   * <p>
   * This is used when; the default buffer size matches the default buffer size in {@link BufferedReader} and {@link BufferedWriter}
   * , changes to the buffersize will impact performance and memory usage depending on the underlying operating system/disk.
   * </p>
   *
   * @param b the buffer size (default is 8192).
   */
  public void setBufferSize(Integer b) {
    this.bufferSize = b;
  }

  protected int bufferSize() {
    return getBufferSize() != null ? getBufferSize().intValue() : DEFAULT_BUFFER_SIZE;
  }

  public String getPath() {
    return path;
  }

  /**
   * Set the xpath-alike path to the element on which you want to split.
   * <p>
   * Note that this is only a pseudo-xpath evaluator as it only allows simple element traversal and not any XPath functions.
   * {@code /path/to/repeating/element} would be fine, but {@code //repeating/element} would not. It works based on
   * {@link XMLEventReader} and navigates based on {@link StartElement} events only.
   * </p>
   *
   * @param path the path.
   */
  public void setPath(String path) {
    this.path = Args.notBlank(path, "path");
  }

  private class JsonStreamingSplitGeneratorConfig extends StaxSplitGeneratorConfig {
    AdaptrisMessage originalMessage;
    JsonXMLConfig jsonXMLConfig;
    XMLEventFactory xmlEventFactory;

    JsonStreamingSplitGeneratorConfig withOriginalMessage(AdaptrisMessage msg) {
      originalMessage = msg;
      return this;
    }
    JsonStreamingSplitGeneratorConfig withJsonXMLConfig(JsonXMLConfig jsonXMLConfig) {
      this.jsonXMLConfig = jsonXMLConfig;
      return this;
    }
    JsonStreamingSplitGeneratorConfig withXMLEventFactory(XMLEventFactory xmlEventFactory) {
      this.xmlEventFactory = xmlEventFactory;
      return this;
    }

  }
  private class JsonStreamingSplitGenerator extends StaxSplitGenerator<JsonStreamingSplitGeneratorConfig, AdaptrisMessage> {
    private transient AdaptrisMessageFactory factory;

    public JsonStreamingSplitGenerator(JsonStreamingSplitGeneratorConfig cfg) throws Exception {
      super(cfg);
    }

    @Override
    public void init(JsonStreamingSplitGeneratorConfig cfg){
      this.factory = selectFactory(cfg.originalMessage);
    }

    @Override
    public AdaptrisMessage generateNextMessage(XMLEvent event, String elementName) throws Exception {
      if (event == null) {
        return null;
      }
      AdaptrisMessage splitMsg = factory.newMessage();
      try (OutputStream output = splitMsg.getOutputStream()){
        XMLEventWriter writer = new JsonXMLOutputFactory(getConfig().jsonXMLConfig).createXMLEventWriter(output);
        writer.add(getConfig().xmlEventFactory.createStartDocument());
        while (isNotEndElement(event, elementName) && getConfig().getXmlEventReader().hasNext()){
          writer.add(event);
          event = getConfig().getXmlEventReader().nextEvent();
        }
        writer.add(getConfig().xmlEventFactory.createEndDocument());
      }
      copyMetadata(getConfig().originalMessage, splitMsg);
      return splitMsg;
    }
  }
}
