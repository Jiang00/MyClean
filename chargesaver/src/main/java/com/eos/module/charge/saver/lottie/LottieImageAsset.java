package com.eos.module.charge.saver.lottie;

import org.json.JSONObject;

/**
 * Data class describing an image asset exported by bodymovin.
 */
public class LottieImageAsset {
  private final int width;
  private final int height;
  private final String id;
  private final String fileName;

  private LottieImageAsset(int width, int height, String id, String fileName) {
    this.width = width;
    this.height = height;
    this.id = id;
    this.fileName = fileName;
  }

  static class Factory {
    private Factory() {
    }

    static LottieImageAsset newInstance(JSONObject imageJson) {
      return new LottieImageAsset(imageJson.optInt("w"), imageJson.optInt("h"), imageJson.optString("id"),
          imageJson.optString("p"));
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public String getId() {
    return id;
  }

  public String getFileName() {
    return fileName;
  }
}
