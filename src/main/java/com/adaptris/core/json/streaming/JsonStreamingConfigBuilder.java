package com.adaptris.core.json.streaming;

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.BooleanUtils;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.util.Args;
import com.adaptris.util.KeyValuePairBag;
import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;

/**
 * Wraps {@code de.odysseus.staxon.json.JsonXMLConfigBuilder} for marshalling purposes.
 * 
 * @config json-streaming-config
 */
@XStreamAlias("json-streaming-config")
@DisplayOrder(order =
{
    "prettyPrint", "autoArray", "autoPrimitive", "namespaceDeclarations", "virtualRoot", "multipleProcessingInstruction",
    "namespaceMappings", "namespaceSeparator", "repairingNamespaces"
})
public class JsonStreamingConfigBuilder {

  @InputFieldDefault(value = "false")
  private Boolean autoArray;
  @InputFieldDefault(value = "false")
  private Boolean autoPrimitive;
  @InputFieldDefault(value = "true")
  @AdvancedConfig
  private Boolean multipleProcessingInstruction;
  @InputFieldDefault(value = "true")
  private Boolean namespaceDeclarations;
  @InputFieldDefault(value = ":")
  @AdvancedConfig
  private Character namespaceSeparator;
  @InputFieldDefault(value = "false")
  private Boolean prettyPrint;
  @InputFieldDefault(value = "false")
  @AdvancedConfig
  private Boolean repairingNamespaces;
  @InputFieldDefault(value = "null")
  private String virtualRoot;
  @AdvancedConfig
  private KeyValuePairSet namespaceMappings;

  public JsonStreamingConfigBuilder() {
  }

  public JsonXMLConfig build() {
    return new JsonXMLConfigBuilder().autoArray(autoArray()).autoPrimitive(autoPrimitive()).multiplePI(multiplePI())
        .namespaceDeclarations(namespaceDeclarations()).namespaceSeparator(namespaceSeparator()).prettyPrint(prettyPrint())
        .repairingNamespaces(repairingNamespaces()).virtualRoot(virtualRoot()).namespaceMappings(namespaceMappings()).build();
  }

  public Boolean getAutoArray() {
    return autoArray;
  }

  /**
   * 
   * @param b trigger json arrays automatically; default is false if not specified.
   */
  public void setAutoArray(Boolean b) {
    this.autoArray = b;
  }

  public JsonStreamingConfigBuilder withAutoArray(Boolean b) {
    setAutoArray(b);
    return this;
  }

  public boolean autoArray() {
    return BooleanUtils.toBooleanDefaultIfNull(getAutoArray(), JsonXMLConfig.DEFAULT.isAutoArray());
  }

  public Boolean getAutoPrimitive() {
    return autoPrimitive;
  }

  /**
   * 
   * @param whether to convert element text to number/boolean/null primitives automatically; default is false if not specified.
   */
  public void setAutoPrimitive(Boolean b) {
    this.autoPrimitive = b;
  }

  public JsonStreamingConfigBuilder withAutoPrimitive(Boolean b) {
    setAutoPrimitive(b);
    return this;
  }

  public boolean autoPrimitive() {
    return BooleanUtils.toBooleanDefaultIfNull(getAutoPrimitive(), JsonXMLConfig.DEFAULT.isAutoPrimitive());
  }

  public Boolean getNamespaceDeclarations() {
    return namespaceDeclarations;
  }

  /** 
   * 
   * @param b set whether to write namespace declarations; default is true if not specified.
   */
  public void setNamespaceDeclarations(Boolean b) {
    this.namespaceDeclarations = b;
  }

  public JsonStreamingConfigBuilder withNamespaceDeclarations(Boolean b) {
    setNamespaceDeclarations(b);
    return this;
  }

  public boolean namespaceDeclarations() {
    return BooleanUtils.toBooleanDefaultIfNull(getNamespaceDeclarations(), JsonXMLConfig.DEFAULT.isNamespaceDeclarations());
  }

  public Character getNamespaceSeparator() {
    return namespaceSeparator;
  }

  /**
   * 
   * @param sep namespace separator; default is ':' if not specified.
   */
  public void setNamespaceSeparator(Character sep) {
    this.namespaceSeparator = sep;
  }

  public JsonStreamingConfigBuilder withNamespaceSeparator(Character b) {
    setNamespaceSeparator(b);
    return this;
  }

  public char namespaceSeparator() {
    return getNamespaceSeparator() != null ? getNamespaceSeparator().charValue() : JsonXMLConfig.DEFAULT.getNamespaceSeparator();
  }

  public Boolean getPrettyPrint() {
    return prettyPrint;
  }

  /**
   * 
   * @param b pretty print or not; default is false if not specified.
   */
  public void setPrettyPrint(Boolean b) {
    this.prettyPrint = b;
  }

  public JsonStreamingConfigBuilder withPrettyPrint(Boolean b) {
    setPrettyPrint(b);
    return this;
  }

  public boolean prettyPrint() {
    return BooleanUtils.toBooleanDefaultIfNull(getPrettyPrint(), JsonXMLConfig.DEFAULT.isPrettyPrint());
  }

  public String getVirtualRoot() {
    return virtualRoot;
  }

  /**
   * Set the virtual root
   * <p>
   * JSON documents may have have multiple root properties. However, XML requires a single root element. This property specifies the
   * root as a "virtual" element, which will be removed from the stream when writing and added to the stream when reading.
   * </p>
   * 
   * @param root (parsed with {@link QName#valueOf(String)})
   */
  public void setVirtualRoot(String root) {
    this.virtualRoot = root;
  }

  public JsonStreamingConfigBuilder withVirtualRoot(String b) {
    setVirtualRoot(b);
    return this;
  }

  public QName virtualRoot() {
    return getVirtualRoot() != null ? QName.valueOf(getVirtualRoot()) : JsonXMLConfig.DEFAULT.getVirtualRoot();
  }

  public Boolean getRepairingNamespaces() {
    return repairingNamespaces;
  }

  /**
   * 
   * @param b Whether or not to repair namespaces when writing; default is false if not specified.
   */
  public void setRepairingNamespaces(Boolean b) {
    this.repairingNamespaces = b;
  }

  public JsonStreamingConfigBuilder withRepairingNamespaces(Boolean b) {
    setRepairingNamespaces(b);
    return this;
  }

  public boolean repairingNamespaces() {
    return BooleanUtils.toBooleanDefaultIfNull(getRepairingNamespaces(), JsonXMLConfig.DEFAULT.isRepairingNamespaces());
  }

  public KeyValuePairSet getNamespaceMappings() {
    return namespaceMappings;
  }

  public void setNamespaceMappings(KeyValuePairSet map) {
    this.namespaceMappings = Args.notNull(map, "namespaceMappings");
  }

  public JsonStreamingConfigBuilder withNamespaceMappings(KeyValuePairSet b) {
    setNamespaceMappings(b);
    return this;
  }

  public Map<String, String> namespaceMappings() {
    if (null == getNamespaceMappings()) {
      return JsonXMLConfig.DEFAULT.getNamespaceMappings();
    }
    return KeyValuePairBag.asMap(getNamespaceMappings());
  }

  public Boolean getMultipleProcessingInstruction() {
    return multipleProcessingInstruction;
  }

  /**
   * 
   * @param b true to emit "&lt;?xml-multiple ?>" in the XML; default true if not specified.
   */
  public void setMultipleProcessingInstruction(Boolean b) {
    this.multipleProcessingInstruction = b;
  }

  public JsonStreamingConfigBuilder withMultipleProcessingInstruction(Boolean b) {
    setMultipleProcessingInstruction(b);
    return this;
  }

  public boolean multiplePI() {
    return BooleanUtils.toBooleanDefaultIfNull(getMultipleProcessingInstruction(), JsonXMLConfig.DEFAULT.isMultiplePI());
  }
}
