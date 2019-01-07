package com.adaptris.core.json.streaming;

public abstract class JsonStreamBuilderImpl {
  private JsonStreamingConfigBuilder config;

  public JsonStreamingConfigBuilder getConfig() {
    return config;
  }

  public void setConfig(JsonStreamingConfigBuilder builder) {
    this.config = builder;
  }

  public <T extends JsonStreamBuilderImpl> T withConfig(JsonStreamingConfigBuilder b) {
    setConfig(b);
    return (T) this;
  }
  
  public JsonStreamingConfigBuilder config() {
    return getConfig() != null ? getConfig() : new JsonStreamingConfigBuilder();
  }
}
